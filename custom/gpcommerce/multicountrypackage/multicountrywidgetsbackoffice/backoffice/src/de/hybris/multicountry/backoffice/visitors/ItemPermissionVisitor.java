/**
 *
 */
package de.hybris.multicountry.backoffice.visitors;

import org.zkoss.zk.ui.Component;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors.ComponentsDisablingVisitor;


/**
 * @author cyrill.pedol@sap.com
 *
 */
public class ItemPermissionVisitor extends ComponentsDisablingVisitor
{

	private static final String MODEL_OBJECT = "currentObject";

	private PermissionFacade permissionFacade;

	private boolean locked = false;


	@Override
	public void initialize(final String typeCode, final WidgetInstanceManager wim, final DynamicForms dynamicForms)
	{
		final Object modelObject = wim.getModel().getValue(MODEL_OBJECT, Object.class);
		locked = !getPermissionFacade().canChangeInstance(modelObject);
	}

	@Override
	public void register(final Component component)
	{
		if (locked)
		{
			super.register(component);
		}
	}

	public void setPermissionFacade(final PermissionFacade permissionFacade)
	{
		this.permissionFacade = permissionFacade;
	}

	public PermissionFacade getPermissionFacade()
	{
		return permissionFacade;
	}
}
