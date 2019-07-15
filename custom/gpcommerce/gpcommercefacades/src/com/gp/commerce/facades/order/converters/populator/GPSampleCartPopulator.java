/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.facades.order.converters.populator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.order.data.SampleCartData;
import com.gp.commerce.order.data.SampleCartImageUrlsData;
import com.gp.commerce.order.data.SampleCartProductData;
import com.gp.commerce.order.data.SampleCartRequestData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPSampleCartPopulator implements Populator<CartModel, SampleCartRequestData> {

	@Override
	public void populate(CartModel source, SampleCartRequestData target) throws ConversionException {
		SampleCartData cartdata = new SampleCartData();
		List<SampleCartProductData> productList = new ArrayList<>();
			for (AbstractOrderEntryModel cartentry : source.getEntries()) {
				SampleCartProductData productData = new SampleCartProductData();
				ProductModel product=cartentry.getProduct();
				productData.setSku(product.getCode());
				productData.setQty(cartentry.getQuantity().toString());
				populateImageUrls(product, productData);
				productList.add(productData);
			}
			cartdata.setId(source.getCode());
			cartdata.setItems(productList);
			String[] targetUidarray=StringUtils.split(source.getUser().getUid(), "|");
			target.setUserid(targetUidarray[0]);
			target.setCart(cartdata);
		}
	
	private void populateImageUrls(ProductModel product, SampleCartProductData productData)
	{
		SampleCartImageUrlsData imageUrls=new SampleCartImageUrlsData();
		String normalUrl=null;
		Collection<MediaModel> mediaModelList=	product.getNormal() ;
			if(mediaModelList.iterator().hasNext())
			{
				normalUrl=mediaModelList.iterator().next().getURL();
			}
		imageUrls.setThumbnail((null !=product.getThumbnail())?product.getThumbnail().getURL():"");
		imageUrls.setNormal(null !=normalUrl?normalUrl:"");
		productData.setImageURLs(imageUrls);
		
	}

}
