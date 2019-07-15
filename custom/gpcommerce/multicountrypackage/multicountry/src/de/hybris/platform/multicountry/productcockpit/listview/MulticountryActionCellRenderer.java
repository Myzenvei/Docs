/**
 *
 */
package de.hybris.platform.multicountry.productcockpit.listview;

import de.hybris.platform.cockpit.components.listview.AdvancedListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.DefaultActionCellRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityAssignmentModel;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menupopup;


/**
 *
 */
public class MulticountryActionCellRenderer extends DefaultActionCellRenderer
{

	private final static Logger LOG = Logger.getLogger(MulticountryActionCellRenderer.class);

	@Override
	public void render(final TableModel model, final int colIndex, final int rowIndex, final Component parent)
	{
		if (model == null)
		{
			LOG.error("Could not render action cell: table model was null.");
			return;
		}
		final Div actionBox = new Div();
		parent.appendChild(actionBox);
		actionBox.setHeight("100%");
		UITools.modifySClass((HtmlBasedComponent) parent, "lvActionCell", true);

		final Set<TypedObject> additionalUpdateNotification = new HashSet<TypedObject>();

		final TypedObject item = (TypedObject) model.getListComponentModel().getValueAt(rowIndex);
		final ColumnDescriptor column = model.getColumnComponentModel().getVisibleColumns().get(colIndex);
		if (item.getObject() != null && !UISessionUtils.getCurrentSession().getModelService().isNew(item.getObject()))
		{
			final List<ListViewAction> actions = getActions();
			for (final ListViewAction listViewAction : actions)
			{
				final ListViewAction.Context context = listViewAction.createContext(model, item, column);

				final Object object = context.getMap().get(ListViewAction.AFFECTED_ITEMS_KEY);
				if (object instanceof Collection)
				{
					final Collection<TypedObject> affectedItems = (Collection<TypedObject>) object;
					additionalUpdateNotification.addAll(affectedItems);
				}

				boolean locked = true;
				final ProductAvailabilityAssignmentModel asign = (ProductAvailabilityAssignmentModel) item.getObject();
				final ProductAvailabilityGroupModel group = asign.getAvailabilityGroup();
				if (group != null)
				{
					final Set<BaseStoreModel> baseStores = group.getStores();
					for (final BaseStoreModel baseStore : baseStores)
					{
						final UserService userService = (UserService) Registry.getApplicationContext().getBean("userService");
						if (checkEmployeeOnBaseStore(baseStore.getEmployees(), userService.getCurrentUser()))
						{
							locked = false;
							break;
						}
					}
				}

				if (!locked)
				{
					// image (button)
					final String imgURI = listViewAction.getImageURI(context);
					if (imgURI != null && imgURI.length() > 0)
					{
						final Image actionImg = new Image(imgURI);

						if (UISessionUtils.getCurrentSession().isUsingTestIDs())
						{
							String id = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(item);
							id = id.replaceAll("\\W", "") + "_" + listViewAction.getClass().getSimpleName();
							final String statusCode = listViewAction.getStatusCode(context);
							if (statusCode != null)
							{
								id += "." + statusCode;
							}
							UITools.applyTestID(actionImg, id);
						}

						actionImg.setStyle("display: inline-block; cursor: pointer;padding-right:3px");



						final Menupopup popup = listViewAction.getPopup(context);

						registerEventListener(actionImg, listViewAction, context, parent);

						if (listViewAction.getTooltip(context) != null && listViewAction.getTooltip(context).length() > 0)
						{
							actionImg.setTooltiptext(listViewAction.getTooltip(context));
						}


						if (popup != null)
						{
							actionBox.appendChild(popup);
							actionImg.setPopup(popup);
						}
						actionBox.appendChild(actionImg);

						// context popup
						final Menupopup contextPopup = listViewAction.getContextPopup(context);
						if (contextPopup != null)
						{
							actionBox.appendChild(contextPopup);
							actionImg.setContext(contextPopup);
						}

						if (model.getListComponentModel() != null
								&& !model.getListComponentModel().isEditable()
								&& listViewAction instanceof AdvancedListViewAction
								&& !((AdvancedListViewAction) listViewAction).isAlwaysEnabled())
						{
							actionImg.setVisible(false);
						}
					}
				}
			}
		}

		if (model.getListComponentModel() != null)
		{
			model.getListComponentModel().addToAdditionalItemChangeUpdateNotificationMap(item, additionalUpdateNotification);
		}
	}


	protected boolean checkEmployeeOnBaseStore(final Collection<EmployeeModel> employees, final UserModel currentUser)
	{
		for (final EmployeeModel employee : employees)
		{
			if (employee.equals(currentUser))
			{
				return true;
			}
		}
		return false;
	}
}
