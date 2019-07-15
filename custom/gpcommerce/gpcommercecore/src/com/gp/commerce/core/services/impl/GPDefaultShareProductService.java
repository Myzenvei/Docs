/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPShareProductService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.product.dao.GPProductDao;
import com.gp.commerce.core.util.ShareProductEvent;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import sun.misc.BASE64Decoder;
import java.io.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.DataInputStream;


/**
 * Share Product Email Process Service Implementation
 * 
 */
public class GPDefaultShareProductService implements GPShareProductService {
	private static final Logger LOG = Logger.getLogger(GPDefaultShareProductService.class);
	
	public static final String PDF_FILE_EXTENSION = ".pdf";
	public static final String PDF_FILE_TYPE = "application/pdf";
	public static final String CART_EMAIL = "cart.email.csv.filename";
	private static final String GPXPRESS_RETAILSOLDTO = "gpxpress.retail.soldto";

	
	private EventService eventService;
	private EmailService emailService;
	private MediaService mediaService;
	private ProductService productService;
	private KeyGenerator guidKeyGenerator;
	
	@Resource(name = "gpProductDao")
	private GPProductDao gpProductDao;
	
	private List<EmailAttachmentModel> attachments;
	
	@Resource(name="defaultGPEmailService")
	private DefaultGPEmailService defaultGPEmailService;
	
	@Resource(name="cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	@Resource(name="sessionService")
	private SessionService sessionService;

	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}

	public ProductService getProductService() {
		return productService;
	}

	@Required
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public KeyGenerator getGuidKeyGenerator() {
		return guidKeyGenerator;
	}

	@Required
	public void setGuidKeyGenerator(KeyGenerator guidKeyGenerator) {
		this.guidKeyGenerator = guidKeyGenerator;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	@Required
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public MediaService getMediaService() {
		return mediaService;
	}

	@Required
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public EventService getEventService() {
		return eventService;
	}

	@Required
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * This method sets value in the event from the input form values and publishes
	 * event
	 * 
	 * @param form
	 * @throws IOException 
	 */
	@Override
	public void shareProductService(ShareProductForm form){
		LOG.info("Method: shareProduct " + form);
		ShareProductEvent shareProdEvent = new ShareProductEvent();
		shareProdEvent.setRecipientEmails(form.getRecipientEmails());
		shareProdEvent.setSenderEmail(form.getSenderEmail());
		shareProdEvent.setSenderName(form.getSenderName());
		shareProdEvent.setSubject(form.getSubject());
		shareProdEvent.setSenderMessage(form.getSenderMessage());
		shareProdEvent.setAddLink(form.getAddLink());
		shareProdEvent.setAttachPDF(form.getAttachPDF());
		
		List<String> productCodes = new ArrayList<String>();
		
		for(ProductWsDTO productDto:form.getProducts())
		{
			productCodes.add(productDto.getCode());
		}
		
		List<ProductModel> productModel = gpProductDao.getProductsForCodes(productCodes);
		
		
		for(ProductModel product:productModel)
		{
			for(ProductWsDTO productDto:form.getProducts())
			{
				if(product.getCode().equalsIgnoreCase(productDto.getCode()))
				{
					product.setShareProductUrl(productDto.getUrl());
					break;
				}
			}
		}
		
		shareProdEvent.setProduct(productModel);  
		
		if(form.getProducts().size()<2 && form.getAttachPDF() && null!=form.getEncodedString())
		{
			BASE64Decoder decoder = new BASE64Decoder();
			String encodedBytes = form.getEncodedString();
			byte[] decodedBytes = null;
			try {
				decodedBytes = decoder.decodeBuffer(encodedBytes);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}

			final String directoryPath = Config.getParameter(CART_EMAIL);
			final File dir = new File(directoryPath);

			final String fileName = "product" + System.currentTimeMillis();
			final File temp = new File(dir, fileName + PDF_FILE_EXTENSION);

			try (FileOutputStream fop1 = new FileOutputStream(temp)) {
				fop1.write(decodedBytes);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
			try (DataInputStream in = new DataInputStream(new FileInputStream(temp))) {
				final EmailAttachmentModel attachment = defaultGPEmailService.createEmailAttachment(in,
						fileName + PDF_FILE_EXTENSION, PDF_FILE_TYPE);
				final List<EmailAttachmentModel> attachmentsList = new ArrayList<>();
				attachmentsList.add(attachment);
				setAttachments(attachmentsList);
				temp.delete();

				shareProdEvent.setEmailAttachmentList(attachmentsList);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}

		}
		
		
		
		if(null != cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO)&& cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO).equals(sessionService.getAttribute("soldToUnitId")))
		{
			shareProdEvent.setSoldTo(Boolean.TRUE);
		}
		else
		{
			shareProdEvent.setSoldTo(Boolean.FALSE);
		}
		
		eventService.publishEvent(shareProdEvent);
	}
	

	/**
	 * Gets the list of email attachment specific to product model
	 * 
	 * @param productModel the product
	 * @return list of email attachments
	 */
	public List<EmailAttachmentModel> getEmailAttachmentList(ProductModel productModel) {
		LOG.info("Method: getEmailAttachmentList");
		String filetype=null ;
		List<EmailAttachmentModel> list = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(productModel.getData_sheet()) ) {
		MediaModel dataSheet = (MediaModel) ((List) productModel.getData_sheet()).get(0);
		if (dataSheet != null)
		{
			filetype=StringUtils.substringAfterLast(dataSheet.getRealfilename(), ".")  ;
		}
			String filename = productModel.getCode() + "." + filetype;
			list.add(castMedialModelToEmailAttachmentModel(dataSheet,filename));
		}
		return list;
	}

	/**
	 * Used to cast media model to email attachment model
	 * 
	 * @param mediaModel
	 * @param attachmentName
	 * @return
	 */
	protected EmailAttachmentModel castMedialModelToEmailAttachmentModel(final MediaModel mediaModel,
			final String attachmentName){
		if(LOG.isDebugEnabled()) {
			LOG.debug("Method: castMedialModelToEmailAttachmentModel" + attachmentName);
		}
		return emailService.createEmailAttachment(new DataInputStream(mediaService.getStreamFromMedia(mediaModel)),
				attachmentName, mediaModel.getMime());
	}
	

}
