/**
 *
 */
package de.hybris.platform.multicountry.cscockpit.impersonation.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.services.impl.DefaultMulticountryRestrictionService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.daos.TypeDao;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test the Multi Country impersonation service - typically used from cockpits and back office, to set state on the
 * session.
 */
@IntegrationTest
public class DefaultMultiCountryImpersonationServiceTest extends ServicelayerTransactionalTest
{

	private DefaultMulticountryImpersonationService multiCountryImpersonationService;

	@Mock
	private DefaultMulticountryRestrictionService multicountryRestrictionService;

	@Mock
	private ProductAvailabilityGroupModel pag;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private SearchRestrictionService searchRestrictionService;

	@Resource
	private TypeDao typeDao;

	private SessionService sessionService;

	@Resource
	private UserService userService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Mock
	private OrderModel order;

	@Mock
	private UserTaxGroup taxGroup;

	@Mock
	private ImpersonationContext impersonationContext;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private BaseStoreService baseStoreService;

	@Before
	public void setUp() throws ImpExException
	{

		MockitoAnnotations.initMocks(this);
		final Collection<ProductAvailabilityGroupModel> groups = new ArrayList<ProductAvailabilityGroupModel>();

		sessionService = new MockSessionService();

		importCsv("/multicountry/test/testImpersonate.impex", "UTF-8");

		// get created site
		final BaseSiteModel site = baseSiteService.getBaseSiteForUID("testCmsSite");

		final Collection<CatalogVersionModel> catalogs = new ArrayList<CatalogVersionModel>();

		// Prepare the context object
		given(impersonationContext.getCurrency()).willReturn(site.getStores().get(0).getDefaultCurrency());
		given(impersonationContext.getLanguage()).willReturn(site.getDefaultLanguage());
		given(impersonationContext.getSite()).willReturn(site);
		given(order.getUser()).willReturn(null);
		given(impersonationContext.getOrder()).willReturn(order);
		given(impersonationContext.getTaxGroup()).willReturn(taxGroup);
		given(impersonationContext.getCatalogVersions()).willReturn(catalogs);
		given(impersonationContext.getProductAvailabilityGroups()).willReturn(groups);

		// Prepare service
		multiCountryImpersonationService = new DefaultMulticountryImpersonationService();
		multiCountryImpersonationService.setI18nService(commonI18NService);
		multiCountryImpersonationService.setBaseSiteService(baseSiteService);
		multiCountryImpersonationService.setUserService(userService);
		multiCountryImpersonationService.setCatalogVersionService(catalogVersionService);
		multiCountryImpersonationService.setSessionService(sessionService);

		multicountryRestrictionService = new DefaultMulticountryRestrictionService();
		multicountryRestrictionService.setSessionService(sessionService);
		multiCountryImpersonationService.setMulticountryRestrictionService(multicountryRestrictionService);
		multiCountryImpersonationService.setMulticountryRestrictionService(multicountryRestrictionService);

		// set current user
		userService.setCurrentUser(userService.getAnonymousUser());

	}

	@Test
	public void availabilitiesGroupsShouldBeEmpty()
	{

		// set availability groups to session
		multiCountryImpersonationService.configureSession(impersonationContext);

		// get availability groups from session
		final Collection<ProductAvailabilityGroupModel> groups = multicountryRestrictionService
				.getCurrentProductAvailabilityGroup();

		Assert.assertTrue(groups.isEmpty());
	}


	@Test
	public void availabilitiesGroupsShouldNotBeEmpty()
	{

		final Collection<ProductAvailabilityGroupModel> groups = new ArrayList<ProductAvailabilityGroupModel>();

		given(pag.getId()).willReturn("TestGroup");

		groups.add(pag);

		given(impersonationContext.getProductAvailabilityGroups()).willReturn(groups);


		// set availability groups to session
		multiCountryImpersonationService.configureSession(impersonationContext);

		// get availability groups from session
		final Collection<ProductAvailabilityGroupModel> currentGroups = multicountryRestrictionService
				.getCurrentProductAvailabilityGroup();

		String testGroupName = null;
		Assert.assertFalse(currentGroups.isEmpty());

		// check that the added group is available in the session
		if (!currentGroups.isEmpty())
		{
			testGroupName = currentGroups.iterator().next().getId();
		}

		Assert.assertEquals("TestGroup", testGroupName);

	}


}