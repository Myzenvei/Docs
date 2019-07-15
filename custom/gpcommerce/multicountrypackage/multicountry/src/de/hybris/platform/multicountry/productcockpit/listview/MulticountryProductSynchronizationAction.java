package de.hybris.platform.multicountry.productcockpit.listview;

import static de.hybris.platform.cockpit.services.sync.SynchronizationService.SYNCHRONIZATION_OK;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.components.sync.dialog.ManySourceManyTargetItemSyncDialog;
import de.hybris.platform.cockpit.components.sync.dialog.OneSourceManyTargetItemSyncDialog;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.productcockpit.components.listview.impl.ProductSynchronizationAction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.au.out.AuAlert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;


/**
 * Provides synchronization functionality within Listview component
 */
public class MulticountryProductSynchronizationAction extends ProductSynchronizationAction
{
	private static final Logger LOG = Logger.getLogger(MulticountryProductSynchronizationAction.class);

	protected static final String TOOLTIP_SYNC_ACTION_UNAVAILABLE = "gridview.item.synchronized.action.tooltip.unavailable";


	@Override
	public EventListener getEventListener(final Context context)
	{
		return new EventListener()
		{
			private final EventListener listener = new EventListener()
			{
				@Override
				public void onEvent(final Event event)
				{
					try
					{
						final Set<TypedObject> objectsToUpdate = new HashSet<TypedObject>();
						if (getTypeService().getBaseType("Product").isAssignableFrom(context.getItem().getType()))
						{
							final List<SyncItemJobModel>[] matrixRules = getSyncJobs(context);
							final int size = matrixRules[0].size() + matrixRules[1].size();
							if (size > 1)
							{
								//standard window 2 tabs [source|target] final
								final OneSourceManyTargetItemSyncDialog dialog = new OneSourceManyTargetItemSyncDialog(context.getItem(),
										(CatalogVersionModel) context.getMap().get(SOURCE_CATALOG_VERSION_KEY), matrixRules)
								{
									@Override
									public void updateBackground(final List<String> chosenRules)
									{
										sendUpdateItemsEvent(context);
									}
								};
								detachDialog(dialog);
								try
								{
									event.getTarget().getRoot().appendChild(dialog);
									dialog.doHighlighted();
								}
								catch (final Exception e)
								{
									LOG.warn("Could not open synchronization dialog (Reason: '" + e.getMessage() + "')", e);
								}
							}
							else if (matrixRules[0].size() == 1 && matrixRules[1].size() == 0)
							{
								objectsToUpdate.addAll(getSynchronizationService().getSyncTargets(context.getItem()));
								getSynchronizationService().performSynchronization(Collections.singletonList(context.getItem()), null,
										matrixRules[0].get(0).getTargetVersion(), null);
							}


						}
						objectsToUpdate.add(context.getItem());

						for (final TypedObject typedObject : objectsToUpdate)
						{
							UISessionUtils.getCurrentSession().sendGlobalEvent(
									new ItemChangedEvent(this, typedObject, Collections.emptyList()));
						}
					}
					finally
					{
						Clients.showBusy(null, false);
					}
				}
			};

			@Override
			public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
			{
				final Object status = context.getMap().get(CURRENT_STATUS_KEY);
				if (isSyncStatusOk(status))
				{
					return; //product already synched, nothing to do.
				}

				final TypedObject synchObject = context.getItem();
				if (containsLockedItems(Collections.singletonList(synchObject)))
				{
					//Locked product, alert the user and do nothing else.
					final String alertMsg = lockedProductMessage(synchObject);
					Clients.response(new AuAlert(alertMsg));
					return;
				}

				final Component target = event.getTarget();
				target.removeEventListener("onSync", this.listener);
				target.addEventListener("onSync", this.listener);

				if (target instanceof Image)
				{
					((Image) target).setSrc(getSyncBusyImg());
				}
				else
				{
					Clients.showBusy(Labels.getLabel("busy.sync"), true);
				}
				Events.echoEvent("onSync", event.getTarget(), null);
			}

			protected boolean isSyncStatusOk(final Object status)
			{
				return (status instanceof Integer && ((Integer) status) == SYNCHRONIZATION_OK);
			}
		};
	}

	/**
	 * Listens to the click of a button and fires the synchro for all selected products in the grid.
	 */
	@Override
	public EventListener getMultiSelectEventListener(final Context context)
	{
		if (CollectionUtils.isEmpty(context.getBrowserModel().getSelectedIndexes()))
		{
			//There are no synch to fire.
			return null;
		}

		//Create a listener that fires the synch process when someone clicks the button
		return new EventListener()
		{
			//This Listener is the one that actually do the the synch process
			private final EventListener listener = new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
				{
					try
					{
						if (context.getBrowserModel() == null)
						{
							return;
						}

						final List<TypedObject> selectedModelItems = context.getBrowserModel().getSelectedItems();
						if (getSynchronizationService().hasMultipleRules(selectedModelItems))
						{
							final Map<String, String>[] rules = getSynchronizationService().getAllSynchronizationRules(
									selectedModelItems);
							final ManySourceManyTargetItemSyncDialog dialog = new ManySourceManyTargetItemSyncDialog(rules,
									selectedModelItems)
							{

								@Override
								public void updateBackground(final List<String> chosenRules)
								{
									sendUpdateItemsEvent(context);
								}
							};
							detachDialog(dialog);
							try
							{
								event.getTarget().getRoot().appendChild(dialog);
								dialog.doHighlighted();
							}
							catch (final Exception e)
							{
								LOG.warn("Could not open synchronization dialog (Reason: '" + e.getMessage() + "')", e);
							}
						}
						else
						{
							//start synchronization immediately - direct synchronization
							getSynchronizationService().performSynchronization(selectedModelItems, null, null, null);

						}

						sendUpdateItemsEvent(context);
					}
					finally
					{
						Clients.showBusy(null, false);
					}
				}
			};

			/**
			 * This event is fired when the user clicks the button. When a locked product is detected, the click does
			 * nothing.
			 */
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
			{

				final BrowserModel browserModel = context.getBrowserModel();
				if (browserModel == null)
				{
					return;
				}

				//Check if there're locked products.
				final List<TypedObject> selectedModelItems = browserModel.getSelectedItems();
				if (containsLockedItems(selectedModelItems))
				{
					//Locked products, alert the user and do nothing else.
					final String alertMsg = lockedProductsMessage(selectedModelItems);
					Clients.response(new AuAlert(alertMsg));

					return; //cannot synch locked products
				}

				//Set the listener for the sync event
				event.getTarget().removeEventListener("onSync", this.listener);
				event.getTarget().addEventListener("onSync", this.listener);
				Clients.showBusy(Labels.getLabel("busy.sync"), true);
				Events.echoEvent("onSync", event.getTarget(), null);
			}
		};
	}

	@Override
	public String getImageURI(final Context context)
	{
		final String imageURI;

		if (context != null && context.getItem() != null && this.containsLockedItems(Collections.singletonList(context.getItem())))
		{
			imageURI = ICON_SYNC_ACTION_UNAVAILABLE;
		}
		else
		{
			imageURI = super.getImageURI(context);
		}

		return imageURI;
	}

	@Override
	public String getMultiSelectImageURI(final Context context)
	{

		final String imageURI;

		if (context != null && context.getBrowserModel() != null
				&& this.containsLockedItems(context.getBrowserModel().getSelectedItems()))
		{
			imageURI = ICON_SYNC_ACTION_UNAVAILABLE;
		}
		else
		{
			imageURI = super.getMultiSelectImageURI(context);
		}

		return imageURI;
	}

	@Override
	public String getTooltip(final Context context)
	{
		final String tooltip;

		if (context != null && context.getItem() != null && this.containsLockedItems(Collections.singletonList(context.getItem())))
		{
			tooltip = this.lockedProductMessage(context.getItem());
		}
		else if (context != null && context.getBrowserModel() != null
				&& this.containsLockedItems(context.getBrowserModel().getSelectedItems()))
		{
			tooltip = this.lockedProductsMessage(context.getBrowserModel().getSelectedItems());
		}
		else
		{
			tooltip = super.getTooltip(context);
		}

		return tooltip;
	}

	/**
	 * Checks if in between the selected items there is at least one with locks.
	 */
	private boolean containsLockedItems(final List<TypedObject> selectedModelItems)
	{

		if (selectedModelItems == null || selectedModelItems.isEmpty())
		{
			return false; //nothing to check.
		}

		boolean containslocked = false;

		for (final TypedObject selectedItem : selectedModelItems)
		{
			final ProductModel productModel = (ProductModel) (selectedItem.getObject());
			if (!productModel.getLockedBy().isEmpty())
			{
				containslocked = true;
				break;
			}
		}

		return containslocked;
	}

	/**
	 * Builds a string with the tooltip/alert message for ONE locked product
	 *
	 * @param object
	 * @return message
	 */
	private String lockedProductMessage(final TypedObject object)
	{
		final StringBuilder lockHolders = new StringBuilder(Labels.getLabel(TOOLTIP_SYNC_ACTION_UNAVAILABLE));

		//Loop through lockers and append their uid.
		final Iterator<EmployeeModel> lockedBy = ((ProductModel) object.getObject()).getLockedBy().iterator();
		lockHolders.append(" (").append(lockedBy.next().getUid());

		while (lockedBy.hasNext())
		{
			lockHolders.append(", ").append(lockedBy.next().getUid());
		}
		lockHolders.append(").");

		return lockHolders.toString();
	}

	/**
	 * Builds a string with the tooltip/alert message for ONE locked product
	 *
	 * @param selectedItems
	 * @return label
	 */
	private String lockedProductsMessage(final List<TypedObject> selectedItems)
	{
		return Labels.getLabel(TOOLTIP_SYNC_ACTION_UNAVAILABLE) + ".";
	}

	/**
	 * Copied from super class.
	 *
	 * @param dialog
	 */
	private void detachDialog(final Window dialog)
	{
		dialog.addEventListener(Events.ON_OPEN, new EventListener()
		{
			@Override
			public void onEvent(final Event dialogEvent) throws Exception //NOPMD: ZK Specific
			{
				if (dialogEvent instanceof OpenEvent && !((OpenEvent) dialogEvent).isOpen())
				{
					dialog.detach();
				}
			}

		});
	}

}
