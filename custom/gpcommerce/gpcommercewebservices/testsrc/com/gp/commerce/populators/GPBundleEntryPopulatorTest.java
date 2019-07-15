package com.gp.commerce.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.gp.commerce.bundle.data.BundleEntryData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;

@UnitTest
public class GPBundleEntryPopulatorTest {
	private static final String PRODUCT_CODE = "PRODUCT_CODE";
	private static final String BUNDLE_ID = "BUNDLE_ID";
	private static final Integer ENTRYGROUP_NUMBER = 1;
	private static final Integer ENTRY_NUMBER = 10;
	private static final Long QUANTITY = 1L;
	private GPBundleEntryPopulator gpBundleEntryPopulator = new GPBundleEntryPopulator();
	
	@Test
	public void testPopulate() {
		CartEntryModel cartEntryModel = setupCartEntryModel();
		BundleEntryData bundleEntryData = new BundleEntryData();
		gpBundleEntryPopulator.populate(cartEntryModel, bundleEntryData);
		assertNotNull(bundleEntryData);
		assertEquals(PRODUCT_CODE, bundleEntryData.getProductCode());
		assertEquals(BUNDLE_ID, bundleEntryData.getBundleTemplateId());
		assertEquals(ENTRYGROUP_NUMBER.intValue(), bundleEntryData.getEntryGroupNumber());
		assertEquals(ENTRY_NUMBER.intValue(), bundleEntryData.getEntryNumber());
		assertEquals(QUANTITY.longValue(), bundleEntryData.getQuantity());
		
	}

	private CartEntryModel setupCartEntryModel() {
		CartEntryModel cartEntryModel = mock(CartEntryModel.class);
		ProductModel productModel = mock(ProductModel.class);
		BundleTemplateModel bundleTemplate = mock(BundleTemplateModel.class);
		Set<Integer> entryGroupNumbers = new HashSet<Integer>(Arrays.asList(ENTRYGROUP_NUMBER));
		
		when(cartEntryModel.getProduct()).thenReturn(productModel);
	
		when(cartEntryModel.getEntryGroupNumbers()).thenReturn(entryGroupNumbers);
		when(cartEntryModel.getBundleTemplate()).thenReturn(bundleTemplate);
		when(cartEntryModel.getEntryNumber()).thenReturn(ENTRY_NUMBER);
		
		when(cartEntryModel.getQuantity()).thenReturn(QUANTITY);
		
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);
		when(bundleTemplate.getId()).thenReturn(BUNDLE_ID);
		return cartEntryModel;
	}
	
}
