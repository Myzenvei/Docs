/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.multicountry.productcockpit.listview;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.productcockpit.components.listview.impl.AbstractProductAction;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;


/**
 * Renders an icon in the status bar to signal that a product is locked.
 * 
 */
public class MulticountryLockedStatus extends AbstractProductAction
{
	protected static final String LOCKED_STATE_ICON = "cockpit/images/icon_locked.png";
	protected static final String lOCKED_TOOLTIP = "gridview.item.locked.status.tooltip";

	private TypeService typeService = null;

	@Override
	public Menupopup getContextPopup(final Context context)
	{
		return null;
	}

	@Override
	public EventListener getEventListener(final Context context)
	{
		return null;
	}

	@Override
	public String getImageURI(final Context context)
	{
		return isLocked(context) ? LOCKED_STATE_ICON : null;
	}

	@Override
	public String getMultiSelectImageURI(final Context context)
	{
		return null;
	}

	@Override
	public Menupopup getMultiSelectPopup(final Context context)
	{
		return null;
	}

	@Override
	public Menupopup getPopup(final Context context)
	{
		return null;
	}

	@Override
	public String getStatusCode(final Context context)
	{
		return "";
	}

	@Override
	public String getTooltip(final Context context)
	{
		return isLocked(context) ? Labels.getLabel(lOCKED_TOOLTIP) : "";
	}

	@Override
	protected void doCreateContext(final Context context)
	{
		//empty
	}

	protected TypeService getTypeService()
	{
		if (typeService == null)
		{
			typeService = UISessionUtils.getCurrentSession().getTypeService();
		}
		return typeService;
	}

	/**
	 * Determines if the product is locked by someone.
	 */
	protected boolean isLocked(final Context context)
	{
		final TypedObject item = context.getItem();
		return getTypeService().getBaseType("Product").isAssignableFrom(item.getType())
				&& !((ProductModel) item.getObject()).getLockedBy().isEmpty();
	}
}
