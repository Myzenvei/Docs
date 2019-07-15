package de.hybris.platform.multicountry.productcockpit.components.navigationarea.renderer;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.multicountry.productcockpit.selector.BaseStoreSectionSelectionSection;
import de.hybris.platform.productcockpit.components.navigationarea.renderer.CatalogSelectorSectionRenderer;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;


public class MulticountryCatalogSelectorSectionRenderer extends CatalogSelectorSectionRenderer
{

	private SessionService sessionService;

	@Override
	protected void selectionChanged(final List<ItemModel> newSelected)
	{
		getSessionService().removeAttribute(BaseStoreSectionSelectionSection.SELECTED_BASESTORE);
		super.selectionChanged(newSelected);
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
