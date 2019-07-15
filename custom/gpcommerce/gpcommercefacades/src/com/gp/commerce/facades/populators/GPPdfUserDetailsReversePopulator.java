/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.springframework.util.Assert;

import com.gp.commerce.core.model.GPPdfDownloadUserDetailsModel;
import com.gp.commerce.facades.data.PdfDownloadUserData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class GPPdfUserDetailsReversePopulator implements Populator<PdfDownloadUserData, GPPdfDownloadUserDetailsModel>
{

	@Override
	public void populate(PdfDownloadUserData source, GPPdfDownloadUserDetailsModel target) throws ConversionException {
		Assert.notNull(source, "Parameter PdfDownloadUserData cannot be null.");
		Assert.notNull(target, "Parameter GPPdfDownloadUserDetailsModel cannot be null.");
		
		target.setPdfName(source.getPdfName());
		target.setPhoneNumber(source.getPhoneNumber());
		target.setEmailId(source.getEmailId());
		target.setSenderMessage(source.getSenderMessage());
		target.setBarColor(source.getBarColor());
		target.setLargeHeading(source.getLargeHeading());
		target.setMediumHeading(source.getMediumHeading());
		target.setHeadLineColor(source.getHeadLineColor());
		target.setListFormat(source.getListFormat());
		target.setCoverPage(source.getCoverPage());
		target.setDisplayHeadlineFirstPageOnly(source.getDisplayHeadlineFirstPageOnly());
		target.setIsProductSellingStatement(source.getIsProductSellingStatement());
		target.setIsCategoryDescription(source.getIsCategoryDescription());
		target.setFeatureCheckedItems(source.getFeatureCheckedItems());
		target.setFeatureCheckedItemsValue(source.getFeatureCheckedItemsValue());
		
	}
}
