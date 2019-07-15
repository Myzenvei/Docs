/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.core.services.impl;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;


/**
 * Service to create and send emails.
 */
public class DefaultGPEmailService extends DefaultEmailService
{
	public static final String EMAIL_ATTACHMENT_NAME = "cart.csv";
	public static final String PRODUCT_ATTACHMENT_NAME = "product.pdf";
	private static final String QUICK_ORDER = "quickorder";
	private static final String SHARE_PRODUCT = "product";
	private static final String QUICK_ORDER_ATTACHMENT = "quickorder.csv";
	private static final String SHARE_WISHLIST = "Wishlist";
	private static final String FAILED_ORDER = "failedOrder";
	private static final String FAILED_CONSIGN= "failedConsignment";
	private static final String FAILED_ORDER_ATTACHMENT = "failedOrders.csv";
	private static final String FAILED_CONSIGN_ATTACHMENT = "failedConsignments.csv";
	private static final String GP_TAX = "|taxEmail";
	private static final String DOT_CHARACTER = ".";
	private static final String UNDERSCORE = "_";

	/* (non-Javadoc)
	 * @see de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService#createEmailAttachment(java.io.DataInputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public EmailAttachmentModel createEmailAttachment(final DataInputStream masterDataStream,final String filename,
			final String mimeType)
	{
		String realFileName = null;
		final EmailAttachmentModel attachment = getModelService().create(EmailAttachmentModel.class);

		if(StringUtils.isNotBlank(filename))
		{	
			if(filename.contains(QUICK_ORDER)){
				realFileName = QUICK_ORDER_ATTACHMENT;
			}
			else if(filename.contains(SHARE_WISHLIST)){
				realFileName = filename;
			}
			else if(filename.contains(FAILED_ORDER)){
				realFileName=FAILED_ORDER_ATTACHMENT;
			}
			else if(filename.contains(FAILED_CONSIGN)){
				realFileName=FAILED_CONSIGN_ATTACHMENT;
			}
			else if(filename.contains(SHARE_PRODUCT)){
				realFileName=PRODUCT_ATTACHMENT_NAME;
			}
			else if(filename.contains(GP_TAX)){
				realFileName = setFileName(filename);
			}
			else{
				realFileName = EMAIL_ATTACHMENT_NAME;
			}
		}
		attachment.setCode(filename);
		attachment.setMime(mimeType);
		attachment.setRealFileName(realFileName);
		attachment.setCatalogVersion(getCatalogVersion());
		getModelService().save(attachment);

		getMediaService().setStreamForMedia(attachment, masterDataStream, realFileName, mimeType, getEmailAttachmentsMediaFolder());
		return attachment;
	}
	
	/**
	 * Modifies the filename
	 * @param filename
	 * @return filename
	 */
	private String setFileName(String filename) {
		String splitFileName = "";
		if(StringUtils.isNotEmpty(filename) && filename.contains(GP_TAX))
		{
			splitFileName = splitFileName(filename);
		}
		return splitFileName;
	}
	
	/**
	 * Splits fileName by the given string
	 * @param filename
	 * @return List<String>
	 */
	private String splitFileName(String filename) {
		String fileNameBeforeExt = "";
		String fileNameAfterExt = "";
		String ext = "";
        if (filename.indexOf(DOT_CHARACTER) >= 0)
        {
        	fileNameBeforeExt = filename.substring(0, filename.lastIndexOf(DOT_CHARACTER));
        	fileNameAfterExt = filename.substring(filename.lastIndexOf(DOT_CHARACTER), filename.length()-1);
        	ext= fileNameAfterExt.substring(0, fileNameAfterExt.indexOf(UNDERSCORE));
        	filename = fileNameBeforeExt + ext;
        }
       
		return filename;
	}

}
