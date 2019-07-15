package com.gp.commerce.core.services.configurablebundle.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

import com.gp.commerce.core.dao.GPBundleDao;
import com.gp.commerce.core.exceptions.GPBundleSetupException;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;

@UnitTest
public class DefaultGPBundleServiceTest {
	
	private static final String CART_ID = "CART_ID_1";
	private static final String BUNDLE_NO = "BUNDLE_NO_1";
	private static final int CART_BUNDLE_NO = 1;
	private static final String ROOT_BUNDLE_ID = "PERFECT TABLE BUNDLE";

	@Mock
    private BundleTemplateService bundleTemplateService;

	@Mock
    private GPBundleDao gpBundleDao;
	
	@InjectMocks
	private DefaultGPBundleService gpBundleService = new DefaultGPBundleService();
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	public void testGetOrderEntriesByBundleNumForNullOrderEntries() {
		when(gpBundleDao.getOrderEntriesByBundleNo(CART_BUNDLE_NO, CART_ID)).thenReturn(null);
		List<CartEntryModel> cartEntryModel = gpBundleService.getOrderEntriesByBundleNum(CART_BUNDLE_NO, CART_ID);
		assertNull(cartEntryModel);
		
	}
	
	@Test
	public void testGetOrderEntriesByBundleNumForNonNullOrderEntries() {
		List<CartEntryModel> cartEntryModelList = new ArrayList<>();
		CartEntryModel cartEntryModel = mock(CartEntryModel.class);
		cartEntryModelList.add(cartEntryModel);
		when(gpBundleDao.getOrderEntriesByBundleNo(CART_BUNDLE_NO, CART_ID)).thenReturn(cartEntryModelList);
		
		List<CartEntryModel> cartEntryModelListResult = gpBundleService.getOrderEntriesByBundleNum(CART_BUNDLE_NO, CART_ID);
		assertNotNull(cartEntryModelListResult);
		assertEquals(1, cartEntryModelListResult.size());
	}
	
	@Test(expected = GPBundleSetupException.class)
	public void testValidateChildBundleSetupContainsNestedChildren() {
		List<BundleTemplateModel> childBundleTemplateList = setupChildBundleTemplates();
		BundleTemplateModel childBundleTemplate = getFirstChildBundleTemplate(childBundleTemplateList);
		
		List<BundleTemplateModel> grandChildBundleTemplateList = setupChildBundleTemplates();
		when(childBundleTemplate.getChildTemplates()).thenReturn(grandChildBundleTemplateList);
		
		gpBundleService.validateChildBundleSetup(childBundleTemplateList, BUNDLE_NO);
	}
	
	@Test
	public void testValidateChildBundleSetupwithNoChildTemplates() {
		
		List<BundleTemplateModel> childBundleTemplateList = null;
		Boolean isValid = gpBundleService.validateChildBundleSetup(childBundleTemplateList , BUNDLE_NO);
		assertFalse(isValid);
		
	}
	
	
	@Test(expected = GPBundleSetupException.class)
	public void testValidateChildBundleSetupContainsNoProducts() {
		
		List<BundleTemplateModel> childBundleTemplateList = setupChildBundleTemplates();
		BundleTemplateModel childBundleTemplate = getFirstChildBundleTemplate(childBundleTemplateList);
		
		when(childBundleTemplate.getChildTemplates()).thenReturn(null);
		when(childBundleTemplate.getProducts()).thenReturn(null);
		
		gpBundleService.validateChildBundleSetup(childBundleTemplateList, BUNDLE_NO);
	}
	
	@Test(expected = GPBundleSetupException.class)
	public void testValidateChildBundleSetupContainsNoSelectionCriteria() {
		List<BundleTemplateModel> childBundleTemplateList = setupChildBundleTemplates();
		BundleTemplateModel childBundleTemplate = getFirstChildBundleTemplate(childBundleTemplateList);
		
		List<ProductModel> productList = setupProductList();
		
		when(childBundleTemplate.getChildTemplates()).thenReturn(null);
		when(childBundleTemplate.getProducts()).thenReturn(productList);
		when(childBundleTemplate.getBundleSelectionCriteria()).thenReturn(null);
		
		gpBundleService.validateChildBundleSetup(childBundleTemplateList, BUNDLE_NO);
	}
	
	@Test
	public void testValidateChildBundleSetup() {
		List<BundleTemplateModel> childBundleTemplateList = setupValidChildBundleTemplateList();
		
		Boolean isValid = gpBundleService.validateChildBundleSetup(childBundleTemplateList, BUNDLE_NO);
		assertTrue(isValid);
	}


	private List<BundleTemplateModel> setupValidChildBundleTemplateList() {
		List<BundleTemplateModel> childBundleTemplateList = setupChildBundleTemplates();
		BundleTemplateModel childBundleTemplate = getFirstChildBundleTemplate(childBundleTemplateList);
		
		List<ProductModel> productList = setupProductList();
		BundleSelectionCriteriaModel bundleSelectionCriteria = mock(BundleSelectionCriteriaModel.class);
		
		when(childBundleTemplate.getChildTemplates()).thenReturn(null);
		when(childBundleTemplate.getProducts()).thenReturn(productList);
		when(childBundleTemplate.getBundleSelectionCriteria()).thenReturn(bundleSelectionCriteria);
		return childBundleTemplateList;
	}
	
	@Test(expected = GPBundleSetupException.class)
	public void testGetRootBundleTemplateForNullTemplateForCode() {
		when(bundleTemplateService.getBundleTemplateForCode(BUNDLE_NO)).thenReturn(null);
		gpBundleService.getRootBundleTemplate(BUNDLE_NO);
	}
	
	@Test
	public void testGetRootBundleTemplate() {
		
		BundleTemplateModel rootBundleTemplate = setupRootBundleTemplate();
		BundleTemplateModel result = gpBundleService.getRootBundleTemplate(BUNDLE_NO);
		assertNotNull(result);
		assertEquals(ROOT_BUNDLE_ID, rootBundleTemplate.getId());
		
	}
	
	@Test
	public void testValidateBundleSetup() {
		BundleTemplateModel rootBundleTemplate = setupRootBundleTemplate();
		List<BundleTemplateModel> childBundleTemplateList = setupValidChildBundleTemplateList();
		when(rootBundleTemplate.getChildTemplates()).thenReturn(childBundleTemplateList);
		Boolean isValid = gpBundleService.validateBundleSetup(BUNDLE_NO);
		assertTrue(isValid);
	}


	private BundleTemplateModel setupRootBundleTemplate() {
		BundleTemplateModel bundleTemplateForCode = mock(BundleTemplateModel.class);
		BundleTemplateModel rootBundleTemplate = mock(BundleTemplateModel.class);
		when(rootBundleTemplate.getId()).thenReturn(ROOT_BUNDLE_ID);
		when(bundleTemplateService.getRootBundleTemplate(bundleTemplateForCode)).thenReturn(rootBundleTemplate);
		when(bundleTemplateService.getBundleTemplateForCode(BUNDLE_NO)).thenReturn(bundleTemplateForCode);
		return rootBundleTemplate;
	}

	

	private List<ProductModel> setupProductList() {
		List<ProductModel> productList = new ArrayList<>();
		ProductModel productModel = mock(ProductModel.class);
		productList.add(productModel);
		return productList;
		
	}

	private List<BundleTemplateModel> setupChildBundleTemplates() {
		List<BundleTemplateModel> childBundleTemplateList = new ArrayList<>();
		BundleTemplateModel childBundleTemplate = mock(BundleTemplateModel.class);
		childBundleTemplateList.add(childBundleTemplate);
		return childBundleTemplateList;
	}
	
	private BundleTemplateModel getFirstChildBundleTemplate(List<BundleTemplateModel> childBundleTemplateList) {
		if(!CollectionUtils.isEmpty(childBundleTemplateList)) {
			return childBundleTemplateList.get(0);
		}
		return null;
	}
	
	
		
}
