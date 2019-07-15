/**
 *
 */
package de.hybris.platform.multicountry.productcockpit.selector;

import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.productcockpit.session.impl.NavigationArea;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;



/**
 * @author brendan
 */
public class BaseStoreSectionSelectionSection extends DefaultSectionSelectorSection
{
	private boolean initialized = false;
	private BaseStoreService baseStoreService;
	private UserService userService;
	private TypeService cockpitTypeService;
	private SessionService sessionService;


	public final static String SELECTED_BASESTORE = "selectedBaseStore";

	@Override
	public void selectionChanged()
	{
		final BaseStoreModel baseStore = (BaseStoreModel) this.getSelectedItem().getObject();
		setSelectedBaseStore(baseStore);
		getNavigationArea().setSelectedCatalogItems(Collections.EMPTY_SET, Collections.EMPTY_SET, false);
		refreshView();
	}


	@Override
	public List<TypedObject> getItems()
	{
		if (!(this.initialized))
		{
			final List<BaseStoreModel> baseStores = baseStoreService.getAllBaseStores();
			//filter base stores
			if (!(baseStores.isEmpty()))
			{
				setItems(cockpitTypeService.wrapItems(filterBaseStoreOnEmployee(baseStores)));
			}
			this.initialized = true;
		}
		return super.getItems();
	}

	protected List<BaseStoreModel> filterBaseStoreOnEmployee(final List<BaseStoreModel> baseStores)
	{
		final ArrayList<BaseStoreModel> toReturn = new ArrayList<BaseStoreModel>();
		for (final BaseStoreModel baseStore : baseStores)
		{
			if (checkEmployeeOnBaseStore(baseStore.getEmployees(), userService.getCurrentUser()))
			{
				toReturn.add(baseStore);
			}
		}
		return toReturn;
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

	/**
	 * Set the <code>BaseStoreModel</code> on as a context parameter so in can be used in the search provider to filter
	 * products
	 *
	 * @param baseStore
	 */
	protected void setSelectedBaseStore(final BaseStoreModel baseStore)
	{
		sessionService.setAttribute(SELECTED_BASESTORE, baseStore);
	}

	/**
	 * Return the selected <code>BaseStoreModel</code> that is stored on the <code>Query</code> object
	 *
	 * @return baseStore
	 */
	protected BaseStoreModel getSelectedBaseStore()
	{
		return sessionService.getAttribute(SELECTED_BASESTORE);
	}

	@Override
	public void refreshView()
	{
		// update navigation section
		final SectionPanelModel sectionPanelModel = getNavigationAreaModel().getNavigationArea().getSectionModel();

		if (sectionPanelModel instanceof AbstractSectionPanelModel)
		{
			((AbstractSectionPanelModel) sectionPanelModel).sectionUpdated(getRootSection());
		}
		// update browser
		final BrowserModel browser = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea()
				.getFocusedBrowser();
		if (browser instanceof SearchBrowserModel)
		{
			final SearchBrowserModel searchBrowser = (SearchBrowserModel) browser;
			searchBrowser.setSimpleQuery("");
			searchBrowser.updateItems(0);
		}
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @param typeService
	 *           the typeService to set
	 */
	public void setCockpitTypeService(final TypeService typeService)
	{
		this.cockpitTypeService = typeService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected NavigationArea getNavigationArea()
	{
		return (NavigationArea) ((BaseStoreSelectorSectionRenderer) getRenderer()).getNavigationArea();
	}
}
