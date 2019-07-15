/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.url.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.FailedOrderEmailProcessModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.core.model.product.ProductModel;


@UnitTest
public class GPDefaultProductModelUrlResolverTest {

	@InjectMocks
	private GPDefaultProductModelUrlResolver productModelResolver = new GPDefaultProductModelUrlResolver();

	@Mock
	private BaseSiteModel currentBaseSite;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private ProductModel source;
	private String url;
	
	@Mock
	VariantProductModel product;
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		source=new ProductModel();
		product=new VariantProductModel();

	}

	@Test
	public void resolveInternalTest(){
		ProductModel baseProduct = Mockito.mock(ProductModel.class);
		BaseSiteModel currentBaseSite = Mockito.mock(BaseSiteModel.class);
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(currentBaseSite);

	}

}
