package com.gp.commerce.b2b.storefront.controllers.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.facade.configurablebundle.GPBundleFacade;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.storefront.util.PageTitleResolver;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;

@UnitTest
public class GPBundlePageControllerTest {

	private static final String BUNDLE_ID = "BUNDLE_ID";

	private static final String BUNDLE_PAGE_TITLE = "Bundle Page Title";

	private static final String CONTENT_PAGE_TITLE = "Content Page Title for Bundle Page";

	private static final String KEYWORDS = "";

	private static final String DESCRIPTION = "Bundle Page Description";
	
	public static final String CMS_PAGE_MODEL = "cmsPage";
	public static final String CMS_PAGE_TITLE = "pageTitle";
	
	@Mock
	private CMSPageService cmsPageService;
	
	@Mock
	private PageTitleResolver pageTitleResolver;

	@InjectMocks
	GPBundlePageController gpBundleController = new GPBundlePageController();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetBundlePage() throws CMSItemNotFoundException {
		Model model = setupMocksForBundlePage();
		String page = gpBundleController.getBundlePage(model);
		assertEquals(ControllerConstants.Views.Pages.Product.BundleCMSPage,page);
		
	}
	
	@Test
	public void testSetupBundleCMSPage() throws CMSItemNotFoundException {
		Model model = setupMocksForBundlePage();
		gpBundleController.setupBundleCMSPage(model);
		Map<String, Object> modelMap = model.asMap();
		assertTrue(model.containsAttribute(CMS_PAGE_MODEL));
		assertTrue(model.containsAttribute(CMS_PAGE_TITLE));
		assertNotNull(modelMap);
		assertEquals(CONTENT_PAGE_TITLE,modelMap.get(CMS_PAGE_TITLE));
		assertNotNull(modelMap.get(CMS_PAGE_MODEL));
		assertEquals(BUNDLE_PAGE_TITLE,((ContentPageModel)modelMap.get(CMS_PAGE_MODEL)).getTitle());
		
	}

	private Model setupMocksForBundlePage() throws CMSItemNotFoundException {
		Model model = new ExtendedModelMap();
		ContentPageModel contentPage = mock(ContentPageModel.class);
		when(contentPage.getTitle()).thenReturn(BUNDLE_PAGE_TITLE);
		when(contentPage.getKeywords()).thenReturn(KEYWORDS);
		when(contentPage.getDescription()).thenReturn(DESCRIPTION);
		when(cmsPageService.getPageForLabelOrId(GPBundlePageController.BUNDLE_CMS_PAGE)).thenReturn(contentPage);
		
		when(pageTitleResolver.resolveContentPageTitle(BUNDLE_PAGE_TITLE)).thenReturn(CONTENT_PAGE_TITLE);
		return model;
	}
	
}
