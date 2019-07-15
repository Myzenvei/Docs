/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;

import bsh.StringUtil;
import de.hybris.platform.util.Config;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

/**
 * This class contains site config utilities
 * 
 */
public class GPSiteConfigUtils {
	
	private static final Logger LOG = Logger.getLogger(GPSiteConfigUtils.class);

    /**
     * Util method constructor should be private because instance should not be created for this class.
     */
    private GPSiteConfigUtils() {
        //do nothing
    }

    public static String getSiteConfig(final CMSSiteModel cmsSite, final String key) {

        final Map<String, String> siteConfig = cmsSite.getSiteConfig();

        return null != siteConfig ? siteConfig.get(key) : null;
    }

    public static String getSiteEnvConfig(final CMSSiteModel cmsSite, final String key) {

        final Map<String, String> siteEnvConfig = cmsSite.getSiteEnvConfig();

        return null != siteEnvConfig ? siteEnvConfig.get(key) : null;
    }

	public static String getDecimalFormatValue(final Double formattedValue)
	{
		final DecimalFormat decimalFormat = new DecimalFormat(Config.getParameter("permissions.thersholdvalue.decimal.format"));

		return String.format(decimalFormat.format(formattedValue));
	}
	
	/**
	 * This method verifies if site is B2B
	 * 
	 * @param site
	 * @return
	 */
    public static boolean isB2BSite(final BaseSiteModel site) {
    	return null != site.getChannel() && GpcommerceCoreConstants.B2B.equalsIgnoreCase(site.getChannel().getCode());
	}

    /**
     * @param cmsSite
     * @return map
     */
    public static Map<String, String> getTopicOfInquiry(final CMSSiteModel cmsSite) {
    	
    	return cmsSite.getTopicOfInquiry();
    }
    
   /**
    * @param cmsSite
    * @return
    */
    public static Map<String,String> getPdfCoverImages(final CMSSiteModel cmsSite) {
    	
    	Map<String,String> pdfImages = new HashMap<>();
    	Map<String, String> pdfMap = cmsSite.getPdfCoverImages();
    	if(MapUtils.isNotEmpty(pdfMap)) {
    		for (Map.Entry<String, String> entry : pdfMap.entrySet()) {
    			if(entry.getValue() != null) {
	    			try{
		    			URL url = new URL(entry.getValue());
						InputStream is = url.openStream();
						byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
						String encodedImage = Base64.getEncoder().encodeToString(bytes);
						pdfImages.put(entry.getKey(), encodedImage);
	    			}
	    			catch (Exception e) {
						LOG.error("Error while encoding image to Base64" + e.getMessage(),e);
					}
    			}
    		}
    	}
		return pdfImages;
    }
    
    /**
     * @param cmsSite
     * @param cerficationCodes
     * @return
     */
    public static Map<String,String> getCertificationImagesForCodes(final CMSSiteModel cmsSite, String cerficationCodes) {
    	
    	final List<String> cerficationCodesList = Arrays.asList(cerficationCodes.split(","));
    	Map<String, String> certificationImg = cmsSite.getGpCertificationsImages();
    	Map<String,String> encodedMap = new HashMap<>();
    	for(String code: cerficationCodesList) {
    		for(Map.Entry<String, String> entry : certificationImg.entrySet()) {
    			if(entry.getKey().equals(code)) {
    				try{
		    			URL url = new URL(entry.getValue());
						InputStream is = url.openStream();
						byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
						String encodedImage = Base64.getEncoder().encodeToString(bytes);
						encodedMap.put(entry.getKey(), encodedImage);
	    			}
	    			catch (Exception e) {
						LOG.error("Error while encoding image to Base64" + e.getMessage(),e);
					}
    			}
    		}
    	}
    	return encodedMap;
    }
    
}
