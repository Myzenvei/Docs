/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.facades.populators;

import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.data.PdfDownloadUserData;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

public class GPPdfUserDetailsPopulator implements Populator<CustomerModel, PdfDownloadUserData> {
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	
	@Resource(name = "mediaService")
	private MediaService mediaService;

	private static final String GPEXPRESS = "gpxpress";
	private static final String B2BWHITELABEL = "b2bwhitelabel";
	private static final String PDFTEXT1 = "gpxpress.pdf.text1";
	private static final String PDFTEXT2 = "gpxpress.pdf.text2";
	private static final String PDFTEXT3 = "gpxpress.pdf.text3";
	private static final String GPXPRESS_RETAIL_SOLDTO_ID = "gpxpress.retail.soldToId";
	private static final String DELIMITER = "*";

	private static final Logger LOG = Logger.getLogger(GPPdfUserDetailsPopulator.class);

	private Converter<AddressModel, AddressData> addressConverter;

	@Override
	public void populate(CustomerModel source, PdfDownloadUserData target) throws ConversionException {
		Assert.notNull(source, "Parameter CustomerModel cannot be null.");
		Assert.notNull(target, "Parameter PdfDownloadUserData cannot be null.");

		if (null != source.getPdfDownloadUserDetails()) {
			target.setBarColor(source.getPdfDownloadUserDetails().getBarColor());
			target.setEmailId(source.getPdfDownloadUserDetails().getEmailId());
			target.setHeadLineColor(source.getPdfDownloadUserDetails().getHeadLineColor());
			target.setLargeHeading(source.getPdfDownloadUserDetails().getLargeHeading());
			target.setMediumHeading(source.getPdfDownloadUserDetails().getMediumHeading());
			target.setPdfName(source.getPdfDownloadUserDetails().getPdfName());
			target.setPhoneNumber(source.getPdfDownloadUserDetails().getPhoneNumber());
			target.setSenderMessage(source.getPdfDownloadUserDetails().getSenderMessage());
			target.setHeadLineColor(source.getPdfDownloadUserDetails().getHeadLineColor());
			target.setListFormat(source.getPdfDownloadUserDetails().getListFormat());
			target.setCoverPage(source.getPdfDownloadUserDetails().getCoverPage());
			target.setDisplayHeadlineFirstPageOnly(
					source.getPdfDownloadUserDetails().getDisplayHeadlineFirstPageOnly());
			target.setIsProductSellingStatement(source.getPdfDownloadUserDetails().getIsProductSellingStatement());
			target.setIsCategoryDescription(source.getPdfDownloadUserDetails().getIsCategoryDescription());
			target.setFeatureCheckedItems(source.getPdfDownloadUserDetails().getFeatureCheckedItems());
			target.setFeatureCheckedItemsValue(source.getPdfDownloadUserDetails().getFeatureCheckedItemsValue());
		}
		CMSSiteModel currentSite = cmsSiteService.getCurrentSite();
		boolean isB2BLogoRequired = false;
		if (currentSite.getUid().equalsIgnoreCase(GPEXPRESS) || currentSite.getUid().equalsIgnoreCase(B2BWHITELABEL)) {
			if (source instanceof B2BCustomerModel) {
				final B2BUnitModel b2bUnitModel = ((B2BCustomerModel) source).getDefaultB2BUnit();
				if (null != b2bUnitModel.getContactAddress()) {
					target.setContactAddress(getAddressConverter().convert(b2bUnitModel.getContactAddress()));
				}
				String distributorImageURL = b2bUnitModel.getDistributorImage();
				if (StringUtils.isNotEmpty(distributorImageURL)) {
					try {
						URL url = new URL(distributorImageURL);
						InputStream is = url.openStream();
						byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
						String encodedImage = Base64.getEncoder().encodeToString(bytes);
						target.setDistributorImage(encodedImage);
					} catch (Exception e) {
						LOG.error("Error while encoding image to Base64" + e.getMessage(),e);
					}
				}
				if (currentSite.getUid().equalsIgnoreCase(GPEXPRESS) && (b2bUnitModel.getUid().equalsIgnoreCase(configurationService.getConfiguration().getString(GPXPRESS_RETAIL_SOLDTO_ID)))) {
                    String text1 = configurationService.getConfiguration().getString(PDFTEXT1);
                    String text2 = configurationService.getConfiguration().getString(PDFTEXT2);
                    String text3 = configurationService.getConfiguration().getString(PDFTEXT3);
                    target.setHeaderText(text1 + DELIMITER + text2 + DELIMITER + text3);
                    try {
                    	MediaModel media = getMediaByCode("headerLogoGPxpress");
						final InputStream is = mediaService.getStreamFromMedia(media);
						final byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
	                    String encodedImage = Base64.getEncoder().encodeToString(bytes);
                        target.setHeaderLogo(encodedImage);
                       }
					catch (Exception e) {
						LOG.error("Error while encoding header logo image to Base64" + e.getMessage(), e);
					}
				}else{
						isB2BLogoRequired = true;
				}

			 }else {
				isB2BLogoRequired = true;
			}
			if(isB2BLogoRequired) {
				try {
					MediaModel media = getMediaByCode("headerLogoB2BWhitelabel");
					InputStream is=mediaService.getStreamFromMedia(media);
					final byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
                    String encodedImage = Base64.getEncoder().encodeToString(bytes);
                    target.setHeaderLogo(encodedImage);
                    }
                    catch (Exception e) {
                                  LOG.error("Error while encoding header logo image to Base64" + e.getMessage(),e);
                    }
			}
			
		}
	}
	
	/**
	 * @param mediaCode
	 * @return
	 */
	protected MediaModel getMediaByCode(final String mediaCode)
	{
		if (StringUtils.isNotEmpty(mediaCode))
		{
			for (final CatalogVersionModel catalogVersionModel : getCatalogVersionService().getSessionCatalogVersions())
			{
				final MediaModel media = getMediaByCodeAndCatalogVersion(mediaCode, catalogVersionModel);
				if (media != null)
				{
					return media;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @param mediaCode
	 * @param catalogVersionModel
	 * @return
	 */
	protected MediaModel getMediaByCodeAndCatalogVersion(final String mediaCode, final CatalogVersionModel catalogVersionModel)
	{
		try
		{
			return getMediaService().getMedia(catalogVersionModel, mediaCode);
		}
		catch (final UnknownIdentifierException ignore)
		{
			// Ignore this exception
		}
		return null;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}
	public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
		this.catalogVersionService = catalogVersionService;
	}
	public MediaService getMediaService() {
		return mediaService;
	}
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}
	public Converter<AddressModel, AddressData> getAddressConverter() {
		return addressConverter;
	}

	public void setAddressConverter(Converter<AddressModel, AddressData> addressConverter) {
		this.addressConverter = addressConverter;
	}

}
