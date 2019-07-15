/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import java.util.Map;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;


/**
 * This class defines services related to Site Configuration
 *
 *
 */
public interface GPCMSSiteService extends CMSSiteService{

    /**
     * This method returns site config value for a given key
     *
     * @param key
     *     the property key
     * @return
     *     the property value
     */
	String getSiteConfig(String key);

    /**
     * This method returns site environment config value for a given key
     *
     * @param key
     *     the property key
     * @return
     *     the property value
     */
	String getSiteEnvConfig(String key);
	
	/**
	 * Checks if it is a sample site.
	 *
	 * @return true, if is sample site
	 */
	boolean isSampleSite();
	
	/**
	 * This method returns topic of inquiry config map
	 * @return map
	 */
	Map<String,String> getTopicOfInquiry();
	
	/**
	 * This method returns pdf cover page
	 * @return map
	 */
	Map<String,String> getPdfCoverImages();
	
	/**
	 * This method returns images for certification codes.
	 *
	 * @param cerficationCodes the cerfication codes
	 * @return map
	 */
	Map<String,String> getCertificationImagesForCodes(String cerficationCodes);
}
