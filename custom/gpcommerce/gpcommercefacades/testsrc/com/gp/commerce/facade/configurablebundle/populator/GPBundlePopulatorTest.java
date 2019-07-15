package com.gp.commerce.facade.configurablebundle.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.facades.populators.GPCommerceProductPopulator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.PickExactlyNBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.PickNToMBundleSelectionCriteriaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.commercefacades.product.data.ProductData;

@UnitTest
public class GPBundlePopulatorTest {

	private static final int MAX_VAL = 10;

	private static final int MIN_VAL = 1;

	private static final String BUNDLE_NAME_1 = "Bundle Name";
	private static final String BUNDLE_ID_1 = "Bundle Id";

	private static final String BUNDLE_NAME_2 = "Bundle Name";
	private static final String BUNDLE_ID_2 = "Bundle Id";

	GPBundlePopulator gpBundlePopulator = new GPBundlePopulator();

	@Mock
	private GPCommerceProductPopulator productPopulator;

	private List<ProductModel> productList = new ArrayList<ProductModel>();
	private List<ProductData> productData = new ArrayList<ProductData>();
	private List<BundleTemplateModel> childBundleList = new ArrayList<BundleTemplateModel>();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		gpBundlePopulator.setGpCommerceProductPopulator(productPopulator);
	}

	@Test
	public void testSetupMinMaxForPickExactlyNBundleSelectionCriteria() {

		BundleTemplateModel childTemplate = setupChildTemplate(BUNDLE_NAME_1,BUNDLE_ID_1);

		setupNBundleSelection(childTemplate);
		BundleTemplateData bundleTemplateDTO = null;
		bundleTemplateDTO = gpBundlePopulator.setupMinMax(childTemplate, bundleTemplateDTO );
		assertNotNull(bundleTemplateDTO);
		assertEquals(MIN_VAL, bundleTemplateDTO.getMinItemsAllowed());
		assertEquals(MIN_VAL, bundleTemplateDTO.getMaxItemsAllowed());

	}

	@Test
	public void testSetupMinMaxForPickNToMBundleSelectionCriteriaModel() {

		BundleTemplateModel childTemplate = setupChildTemplate(BUNDLE_NAME_1,BUNDLE_ID_1);
		setupPickNtoMBundleSelection(childTemplate);
		BundleTemplateData bundleTemplateDTO = null;
		bundleTemplateDTO = gpBundlePopulator.setupMinMax(childTemplate, bundleTemplateDTO );
		assertNotNull(bundleTemplateDTO);
		assertEquals(MIN_VAL, bundleTemplateDTO.getMinItemsAllowed());
		assertEquals(MAX_VAL, bundleTemplateDTO.getMaxItemsAllowed());
		
	}
	
	@Test
	public void testBuildProductList() {
		BundleTemplateModel childTemplate = setupChildTemplate(BUNDLE_NAME_1,BUNDLE_ID_1);
		setupProductList(childTemplate);
		List<ProductData> productDataList = gpBundlePopulator.buildProductList(childTemplate);
		assertNotNull(productDataList);
		assertEquals(3, productDataList.size());
		productDataList.forEach(product -> {
			assertNotNull(product);
		});
	}
	
	@Test
	public void testbuildProductList() {
		BundleTemplateModel childTemplate = setupChildTemplate(BUNDLE_NAME_1,BUNDLE_ID_1);
		setupProductList(childTemplate);
		
	}
	
	@Test
	public void testSetChildBundles() {
		setupChildBundleList();
		List<BundleTemplateData> bundleDTOList = gpBundlePopulator.setChildBundles(childBundleList);
		assertNotNull(bundleDTOList);
		assertEquals(2, bundleDTOList.size());
		assertEquals(BUNDLE_NAME_1, bundleDTOList.get(0).getName());
		assertEquals(BUNDLE_NAME_2, bundleDTOList.get(1).getName());
		
	}
	
	private void setupChildBundleList() {
		 BundleTemplateModel child1 = setupCompleteChildBundle(BUNDLE_NAME_1,BUNDLE_ID_1);
		 childBundleList.add(child1);
		 BundleTemplateModel child2 = setupCompleteChildBundle(BUNDLE_NAME_2,BUNDLE_ID_2);
		 childBundleList.add(child2);
	}

	private BundleTemplateModel setupCompleteChildBundle(String name, String id) {
		
		BundleTemplateModel child1 = setupChildTemplate(name,id);
		setupPickNtoMBundleSelection(child1);
		setupProductList(child1);
		return child1;
	}
	
	
	private BundleTemplateModel setupChildTemplate(String name, String id) {
		BundleTemplateModel childTemplate = mock(BundleTemplateModel.class);
		when(childTemplate.getName(Locale.US)).thenReturn(name);
		when(childTemplate.getId()).thenReturn(id);
		return childTemplate;
	}
	
	private void setupNBundleSelection(BundleTemplateModel childTemplate) {
		PickExactlyNBundleSelectionCriteriaModel pickExactlyNBundle = new PickExactlyNBundleSelectionCriteriaModel();
		pickExactlyNBundle.setN(MIN_VAL);
		
		BundleSelectionCriteriaModel bundleSelection = pickExactlyNBundle;
		when(childTemplate.getBundleSelectionCriteria()).thenReturn(bundleSelection);
	}
	
	private void setupPickNtoMBundleSelection(BundleTemplateModel childTemplate1) {
		PickNToMBundleSelectionCriteriaModel pickNToMBundle = new PickNToMBundleSelectionCriteriaModel();
		pickNToMBundle.setN(MIN_VAL);
		pickNToMBundle.setM(MAX_VAL);
		
		BundleSelectionCriteriaModel bundleSelection = pickNToMBundle;
		when(childTemplate1.getBundleSelectionCriteria()).thenReturn(bundleSelection);
	}
	

	private void setupProductList(BundleTemplateModel childTemplate) {
		
		ProductModel product1 = mock(ProductModel.class);
		productList.add(product1);
		ProductModel product2 = mock(ProductModel.class);
		productList.add(product2);
		ProductModel product3 = mock(ProductModel.class);
		productList.add(product3);
		when(childTemplate.getProducts()).thenReturn(productList);
		
	}
	
	
	
	
}
