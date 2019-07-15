/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.gp.commerce.facades.process.email.context.BackInStockNotificationEmailContext;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.stocknotificationservices.model.StockNotificationProcessModel;

import java.util.Locale;

import javax.annotation.Resource;

public class GPBackInStockNotificationEmailContext extends BackInStockNotificationEmailContext {

	private ProductInterestData productInterestData;
	
	@Resource(name = "productInterestConverter")
	private Converter<ProductInterestModel, ProductInterestData> productInterestConverter;

	@Override
	public void init(final StockNotificationProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		
		productInterestData = productInterestConverter.convert(businessProcessModel.getProductInterest());
		
		put(EMAIL,productInterestData.getEmailAddress());

	}
	
	public ProductInterestData getProductInterestData() {
		
		return productInterestData;
	}

	public void setProductInterestData(ProductInterestData productInterestData) {
		
		this.productInterestData = productInterestData;
	}

	@Override
	protected void updateTitle(StockNotificationProcessModel businessProcessModel, Locale emailLocale) {
		final TitleModel title = businessProcessModel.getProductInterest().getCustomer()!=null ? 
				businessProcessModel.getProductInterest().getCustomer().getTitle() : null;
		if(null != title)
		{
			String customerTitle = title.getName(emailLocale);
			put(TITLE, customerTitle);
		}
		
	}
	
}
