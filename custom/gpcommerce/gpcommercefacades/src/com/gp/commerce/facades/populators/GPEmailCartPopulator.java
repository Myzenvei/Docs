/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.product.services.impl.GPB2BDefaultProductService;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class GPEmailCartPopulator implements Populator<CartModel, CartData> {

	private Converter<CartModel, CartData> cartConverter;

	private static final String CATALOG_ID = "gpUSProductCatalog";
	private static final String ENABLE_DISPLAY_ATRR = "enableDisplayAttributes";
	@Resource(name = "gpB2BProductService")
	private GPB2BDefaultProductService gpB2BProductService;
	@Resource(name = "cmsSiteService")
	GPCMSSiteService cmsSiteService;
	@Resource(name = "classificationService")
	private ClassificationService classificationService;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "catalogVersionService")
	CatalogVersionService catalogVersionService;

	@Override
	public void populate(CartModel source, CartData target) throws ConversionException {

		cartConverter.convert(source, target);
		UserModel user = source.getUser();
		if (user instanceof B2BCustomerModel) {
			B2BCustomerModel customer = (B2BCustomerModel) user;
			CatalogVersionModel catalogVersion = catalogVersionService.getSessionCatalogVersionForCatalog(CATALOG_ID);
			for (OrderEntryData cartEntryData : target.getEntries()) {

				ProductModel product = productService.getProductForCode(catalogVersion,
						cartEntryData.getProduct().getCode());
				populateCMIRCode(product, cartEntryData.getProduct(), customer);
				populateProductAttributes(product, cartEntryData.getProduct());
			}

		}

	}

	private void populateProductAttributes(ProductModel product, ProductData product2) {

		String enable = cmsSiteService.getSiteConfig(ENABLE_DISPLAY_ATRR);
		if (StringUtils.isNotBlank(enable) && Boolean.valueOf(enable).equals(Boolean.TRUE)) {
			product2.setDisplayAttributes(gpB2BProductService.getDisplayAttributesForProduct(product));
		}
	}

	private void populateCMIRCode(final ProductModel source, final ProductData productData,
			final B2BCustomerModel customer) {
		final B2BUnitModel b2bUnit = customer.getDefaultB2BUnit();
		final String cmir = gpB2BProductService.getCMIRCodeForProductAndB2BUnit(source.getCode(), b2bUnit.getUid());
		if (!StringUtils.isEmpty(cmir)) {
			productData.setCmirCode(cmir);
		}
	}

	public Converter<CartModel, CartData> getCartConverter() {
		return cartConverter;
	}

	public void setCartConverter(Converter<CartModel, CartData> cartConverter) {
		this.cartConverter = cartConverter;
	}

}
