package de.hybris.platform.multicountry.productcockpit.listview;

import de.hybris.platform.admincockpit.components.listview.impl.DeleteAction;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.jfree.util.Log;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;



public class ProductAvailabilityDeleteAction extends DeleteAction
{
	private ModelService modelService;


	@Override
	public EventListener getEventListener(final Context context)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event)
			{
				try
				{
					if (Messagebox.show(Labels.getLabel("ba.messagebox.confirm_delete_msg"),
							Labels.getLabel("ba.messagebox.confirm_delete_title"), Messagebox.OK + Messagebox.CANCEL,
							Messagebox.EXCLAMATION) == Messagebox.OK)
					{
						final TypedObject itemToDelete = getItem(context);
						getModelService().remove(itemToDelete.getObject());
						sendUpdateItemsEvent(context);
						final BrowserModel focusedBrowser = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea()
								.getFocusedBrowser();
						((DefaultListModel) context.getListModel().getListModel()).remove(context.getItem());

						if (focusedBrowser instanceof AdvancedBrowserModel)
						{
							final Collection<TypedObject> currentContextItems = Collections2.filter(
									((AdvancedBrowserModel) focusedBrowser).getContextItems(), new Predicate<TypedObject>()
									{
										@Override
										public boolean apply(final TypedObject typedObject)
										{
											return typedObject.getObject() != null && !getModelService().isNew(typedObject.getObject());
										}
									});


							((AdvancedBrowserModel) focusedBrowser).setContextItemsDirectly(
									((AdvancedBrowserModel) focusedBrowser).getContextRootItem(), new ArrayList<>(currentContextItems));
						}
						UISessionUtils.getCurrentSession().sendGlobalEvent(
								new ItemChangedEvent(this, context.getItem(), Collections.emptyList(),
										ItemChangedEvent.ChangeType.REMOVED));
					}
				}
				catch (ModelRemovalException | InterruptedException e)
				{
					Log.error(e);
				}
			}
		};
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
