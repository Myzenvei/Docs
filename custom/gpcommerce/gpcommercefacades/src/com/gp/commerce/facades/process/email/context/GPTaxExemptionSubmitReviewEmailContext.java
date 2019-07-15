/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.TaxExemptionSubmitReviewEmailProcessModel;
import com.gp.commerce.core.services.impl.DefaultGPEmailService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * 
 * Velocity context for a tax exemption submit review email.
 *
 */
public class GPTaxExemptionSubmitReviewEmailContext
		extends GPAbstractEmailContext<TaxExemptionSubmitReviewEmailProcessModel> {
	
	private static final String TAX_EXEMPTION_SUBMIT = "TAX_EXEMPTION_SUBMIT";
	private static final String TAX_ATTACHMENT = "TAX_ATTACHMENT";
	private List<EmailAttachmentModel> attachments;
	private CustomerData customerData;
	private Converter<UserModel, CustomerData> customerConverter;
	private MediaService mediaService;
	private EmailService emailService;
	
	private String selectedRole;

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public Converter<UserModel, CustomerData> getCustomerConverter() {
		return customerConverter;
	}

	@Required
	public void setCustomerConverter(Converter<UserModel, CustomerData> customerConverter) {
		this.customerConverter = customerConverter;
	}

	@Override
	public void init(TaxExemptionSubmitReviewEmailProcessModel businessProcessModel,
			final EmailPageModel emailPageModel) {
		super.init(businessProcessModel, emailPageModel);
		TaxExemptionSubmitReviewEmailProcessModel emailProcessModel = (TaxExemptionSubmitReviewEmailProcessModel) businessProcessModel;
		put(EMAIL, emailProcessModel.getToEmail());
		put(DISPLAY_NAME, emailProcessModel.getToEmail());
		

		customerData = getCustomerConverter()
				.convert(getCustomer(businessProcessModel));
		if (null != getCustomer(businessProcessModel).getSelectedRole()) {
			selectedRole = getCustomer(businessProcessModel).getSelectedRole().getCode();
		}
		
		List<MediaModel> taxExemptionDocuments = businessProcessModel.getTaxExemptionDocuments();
		final List<EmailAttachmentModel> documents = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(taxExemptionDocuments)) {
			taxExemptionDocuments.forEach(document ->{

				EmailAttachmentModel doc = emailService.createEmailAttachment(new DataInputStream(mediaService.getStreamFromMedia(document)),
							 document.getCode()+"|taxEmail" , document.getMime());
				documents.add(doc);
				});
			setAttachments(documents);
		}
		
		
		put(TAX_EXEMPTION_SUBMIT, true);
		put(TAX_ATTACHMENT, getAttachments());
	}
	
	public CustomerData getCustomerData() {
		return customerData;
	}

	public void setCustomerData(CustomerData customerData) {
		this.customerData = customerData;
	}

	@Override
	protected BaseSiteModel getSite(TaxExemptionSubmitReviewEmailProcessModel businessProcessModel) {
		return businessProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(TaxExemptionSubmitReviewEmailProcessModel businessProcessModel) {
		if (businessProcessModel.getUser() instanceof CustomerModel) {
			return (CustomerModel) businessProcessModel.getUser();
		}
		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(TaxExemptionSubmitReviewEmailProcessModel businessProcessModel) {
		return null;
	}

	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}

	public MediaService getMediaService() {
		return mediaService;
	}

	@Required
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	@Required
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

}
