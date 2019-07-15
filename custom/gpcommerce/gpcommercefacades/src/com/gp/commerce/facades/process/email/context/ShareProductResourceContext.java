/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
 
package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.ShareProductResourceEmailProcessModel;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * Share Product Email Process Context
 *
 */
public class ShareProductResourceContext extends GPAbstractEmailContext<ShareProductResourceEmailProcessModel> {
	private List<String> recipientEmails = new ArrayList<String>();
	private String senderEmail;
	private String senderName;
	private String senderMessage;
	private String resourceTitle;
	private String resourceDescription;
	private String imgurl;
	private String resourcePageUrl;
	private String embeddedLink;
	private Boolean checkBoxSelected;
	private static final String SHARE_PRODUCT_RESOURCE = "SHARE_PRODUCT_RESOURCE";

	
	private static final Logger LOG = Logger.getLogger(ShareProductResourceContext.class);

	/**
	 * To initialize the email process. putting values in context, so that these
	 * values become accessible in email
	 *
	 * @param shareProductEmailProcessModel
	 * @param emailPageModel
	 */
	@Override
	public void init(final ShareProductResourceEmailProcessModel shareProductEmailProcessModel, final EmailPageModel emailPageModel) {
		if(LOG.isDebugEnabled()){
			LOG.debug("Method: init " + shareProductEmailProcessModel + emailPageModel);
		}
		super.init(shareProductEmailProcessModel, emailPageModel);
		recipientEmails.addAll(shareProductEmailProcessModel.getRecipientEmails());
		if (shareProductEmailProcessModel.getCheckBoxSelected()) {
			recipientEmails.add(shareProductEmailProcessModel.getSenderEmail());
		}
		senderName=shareProductEmailProcessModel.getSenderName();
		senderMessage=shareProductEmailProcessModel.getSenderMessage();
		resourceTitle=shareProductEmailProcessModel.getResourceTitle();
		resourceDescription=shareProductEmailProcessModel.getResourceDescription();
		imgurl=shareProductEmailProcessModel.getImgurl();
		resourcePageUrl=shareProductEmailProcessModel.getResourcePageUrl();
		checkBoxSelected=shareProductEmailProcessModel.getCheckBoxSelected();
		embeddedLink=shareProductEmailProcessModel.getEmbeddedLink();
		put(EMAIL, String.join(",", recipientEmails));
		put(DISPLAY_NAME, shareProductEmailProcessModel.getSenderName());
		put(FROM_DISPLAY_NAME, shareProductEmailProcessModel.getSenderName());
		put(SHARE_PRODUCT_RESOURCE,true);

	}
	
	public List<String> getRecipientEmails() {
		return recipientEmails;
	}

	public void setRecipientEmails(List<String> recipientEmails) {
		this.recipientEmails = recipientEmails;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderMessage() {
		return senderMessage;
	}

	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}

	public String getResourceTitle() {
		return resourceTitle;
	}

	public void setResourceTitle(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}

	public String getResourceDescription() {
		return resourceDescription;
	}

	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getResourcePageUrl() {
		return resourcePageUrl;
	}

	public void setResourcePageUrl(String resourcePageUrl) {
		this.resourcePageUrl = resourcePageUrl;
	}

	public String getEmbeddedLink() {
		return embeddedLink;
	}

	public void setEmbeddedLink(String embeddedLink) {
		this.embeddedLink = embeddedLink;
	}

	public Boolean getCheckBoxSelected() {
		return checkBoxSelected;
	}

	public void setCheckBoxSelected(Boolean checkBoxSelected) {
		this.checkBoxSelected = checkBoxSelected;
	}

	@Override
	protected BaseSiteModel getSite(final ShareProductResourceEmailProcessModel shareProductEmailProcessModel) {
		return shareProductEmailProcessModel.getSite();
	}
	@Override
	protected CustomerModel getCustomer(final ShareProductResourceEmailProcessModel shareProductEmailProcessModel) {
		return (CustomerModel) shareProductEmailProcessModel.getUser();
	}
	@Override
	protected LanguageModel getEmailLanguage(final ShareProductResourceEmailProcessModel shareProductEmailProcessModel) {
		return shareProductEmailProcessModel.getSite().getDefaultLanguage();
	}

}
