/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.price.converters.populator;

import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;
import org.apache.log4j.Logger;


public class GPProductPricePopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{
	private static final Logger LOG = Logger.getLogger(GPProductPricePopulator.class);

	@Resource(name = "gpPriceFactory")
	GPDefaultEurope1PriceFactory gpPriceService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Override
	public void populate(final ProductModel source, final ProductData target) {

        LOG.debug("Populating price for " + source.getCode());

		GPFilteredPriceContainer gpFilteredPriceContainer = gpPriceService.getFilteredPriceContainerForProducts(source);


			PriceData priceData;

			final PriceDataType priceType;
			if (CollectionUtils.isEmpty(source.getVariants()))
			{
				priceType = PriceDataType.BUY;
			}
			else
			{
				priceType = PriceDataType.FROM;
			}

			double listPrice = gpFilteredPriceContainer.getListPrice();
			double salePrice = gpFilteredPriceContainer.getSalePrice();
            double mapPrice = gpFilteredPriceContainer.getMapPrice();
            CurrencyModel currencyModel = gpFilteredPriceContainer.getCurrency();


			if (listPrice > salePrice && salePrice >= 0d) {
				//set sale price , if not negative
				priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(salePrice),
						currencyModel);
				target.setPrice(priceData);
				priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(listPrice),
						currencyModel);
				target.setWeblistPrice(priceData);
			}
			else if (salePrice <= 0){
				priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(listPrice),
						currencyModel);
				target.setPrice(priceData);
			}else{
				priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(salePrice),
						currencyModel);
				target.setPrice(priceData);
			}
			priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(mapPrice),
						currencyModel);
			target.setMapPrice(priceData);




	}

}
