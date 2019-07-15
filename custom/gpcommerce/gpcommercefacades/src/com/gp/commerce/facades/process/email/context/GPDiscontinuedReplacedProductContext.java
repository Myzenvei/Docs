/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.process.email.context;

import com.gp.commerce.core.enums.MaterialStatusEnum;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.SubscriptionCartProcessModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.gp.commerce.facades.process.email.context.GPAbstractEmailContext;
import de.hybris.platform.core.model.user.CustomerModel;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import de.hybris.platform.commercefacades.product.ProductOption;
import java.util.List;
import de.hybris.platform.commercefacades.product.data.ProductData;
import com.gp.commerce.facades.product.GPProductFacade;
import java.util.Arrays;
import javax.annotation.Resource;
/**
 * Velocity context for a order notification email.
 */
public class GPDiscontinuedReplacedProductContext extends GPAbstractEmailContext<SubscriptionCartProcessModel>
{

	private Converter<GPSubscriptionCartModel, CartData> emailCartConverter;
	private CartData cartData;
	
	@Resource(name = "productFacade")
	private GPProductFacade gpProductFacade;

	@Override
	public void init(final SubscriptionCartProcessModel subscriptionCartProcessModel, final EmailPageModel emailPageModel)
	{	
		boolean isDiscontinued = false; 
		super.init(subscriptionCartProcessModel, emailPageModel);
		cartData = getEmailCartConverter().convert(subscriptionCartProcessModel.getSubscriptionCart());
		if(subscriptionCartProcessModel.getSubscriptionCart().getEntries().get(0).getProduct().getMaterialStatus().getCode().equalsIgnoreCase(MaterialStatusEnum.OBSOLETE.toString())
				&& subscriptionCartProcessModel.getSubscriptionCart().getEntries().get(0).getProduct().getReplacedBy() == null)
		{
			isDiscontinued = true; 
		}
		else
		{
			if(subscriptionCartProcessModel.getSubscriptionCart().getEntries().get(0).getProduct().getReplacedBy() != null)
			{
				final List<ProductOption> extraOptions = Arrays.asList(ProductOption.DESCRIPTION,ProductOption.IMAGES);
				final ProductData productData = gpProductFacade.getProductForCodeAndOptions(subscriptionCartProcessModel.getSubscriptionCart().getEntries().get(0).getProduct().getReplacedBy().getCode(), extraOptions);
				put("productData", productData);
			}
			
		}
		put(GpcommerceCoreConstants.IS_DISCONTINUED_ONLY, isDiscontinued);
		
	}

	public Converter<GPSubscriptionCartModel, CartData> getEmailCartConverter() {
		return emailCartConverter;
	}

	public void setEmailCartConverter(Converter<GPSubscriptionCartModel, CartData> emailCartConverter) {
		this.emailCartConverter = emailCartConverter;
	}
	
	@Override
	protected BaseSiteModel getSite(final SubscriptionCartProcessModel subscriptionCartProcessModel)
	{
		return subscriptionCartProcessModel.getSubscriptionCart().getSite();
	}
	@Override
	protected CustomerModel getCustomer(final SubscriptionCartProcessModel subscriptionCartProcessModel) {

		return (CustomerModel) subscriptionCartProcessModel.getSubscriptionCart().getUser();
	}
	public CartData getCartData() {
		return cartData;
	}

	public void setCartData(final CartData cartData) {
		this.cartData = cartData;
	}
}

