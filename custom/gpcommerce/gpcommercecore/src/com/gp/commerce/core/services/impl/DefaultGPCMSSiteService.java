/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSSiteService;
import com.gp.commerce.core.util.GPSiteConfigUtils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gp.commerce.core.services.GPCMSSiteService;

/**
 * This class is used for GP CMS Site service
 */
public class DefaultGPCMSSiteService extends DefaultCMSSiteService implements GPCMSSiteService {

	private static final String IS_SAMPLE_CART_ENABLED = "enable.samplecart";
	
    @Override
    public String getSiteConfig(String key) {
        return GPSiteConfigUtils.getSiteConfig(getCurrentSite(), key);
    }

    @Override
    public String getSiteEnvConfig(String key) {
        return GPSiteConfigUtils.getSiteEnvConfig(getCurrentSite(), key);
    }

	@Override
	public boolean isSampleSite() {
		String value=this.getSiteConfig(IS_SAMPLE_CART_ENABLED);
		return StringUtils.isNotEmpty(value) && Boolean.parseBoolean(value);
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.services.GPCMSSiteService#getTopicOfInquiry()
	 */
	@Override
	public Map<String,String> getTopicOfInquiry() {
		return GPSiteConfigUtils.getTopicOfInquiry(getCurrentSite());
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.services.GPCMSSiteService#getPdfCoverImages()
	 */
	@Override
	public Map<String,String> getPdfCoverImages() {
		return GPSiteConfigUtils.getPdfCoverImages(getCurrentSite());
	} 
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.services.GPCMSSiteService#getCertificationImagesForCodes(java.lang.String)
	 */
	@Override
	public Map<String,String> getCertificationImagesForCodes(String cerficationCodes) {
		return GPSiteConfigUtils.getCertificationImagesForCodes(getCurrentSite(),cerficationCodes);
	}

}
