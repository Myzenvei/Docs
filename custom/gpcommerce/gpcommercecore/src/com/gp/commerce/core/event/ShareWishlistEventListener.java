/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.user.UserModel;
import com.gp.commerce.core.model.WishlistProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.event.ShareWishlistEvent;
import com.gp.commerce.core.wishlist.services.impl.GPDefaultWishlistService;

public class ShareWishlistEventListener extends GPAbstractAcceleratorSiteEventListener<ShareWishlistEvent>
{

	private static final Logger LOG = Logger.getLogger(ShareWishlistEventListener.class);
	private static final String SHARE_WISHLIST_EMAIL_PROCESS = "shareWishlistEmailProcess";
	private static final String SHARE_WISHLIST_EMAIL_PROCESS_LABEL = "shareWishlistEmailProcess-";
	private EmailService emailService;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;
	
	private String siteUid;
	private String subject;
	private String senderMessage;
	private Boolean attachPDF;
	private Boolean soldTo;
	private Boolean addLink;

	@Autowired
	private BaseSiteService baseSiteService;
	private List<EmailAttachmentModel> attachments;

	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}
	public void setAttachments(final List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}
	
	public I18NService getI18nService() {
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService) {
		this.i18nService = i18nService;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	protected void onSiteEvent(final ShareWishlistEvent event)
	{
		final Wishlist2Model wishlistModel = ((WishlistProcessModel)event.getSource()).getWishlist();
		final UserModel customer = ((WishlistProcessModel)event.getSource()).getUser();
		final WishlistProcessModel wishlistProcessModel = (WishlistProcessModel) getBusinessProcessService().createProcess(
				SHARE_WISHLIST_EMAIL_PROCESS_LABEL + wishlistModel.getWishlistUid() + "-" + System.currentTimeMillis(),
				SHARE_WISHLIST_EMAIL_PROCESS);
		wishlistProcessModel.setWishlist(wishlistModel);
		wishlistProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		wishlistProcessModel.setUser(customer);
		wishlistProcessModel.setToEmail(event.getToEmail());
		wishlistProcessModel.setSenderEmail(event.getSenderEmail());
		wishlistProcessModel.setSenderName(event.getSenderName());
		wishlistProcessModel.setSubject(event.getSubject());
		wishlistProcessModel.setSendersMessage(event.getSenderMessage());
		wishlistProcessModel.setAddLink(event.getAddLink());
		wishlistProcessModel.setAttachPDF(event.getAttachPDF());
		wishlistProcessModel.setSoldTo(event.getSoldTo());
		wishlistProcessModel.setEmailAttachmentModelList(event.getEmailAttachmentList());
		getModelService().save(wishlistProcessModel);
		getBusinessProcessService().startProcess(wishlistProcessModel);

	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final ShareWishlistEvent event)
	{
		final BaseSiteModel site = baseSiteService.getCurrentBaseSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_SITE, site);
		return site.getChannel();

	}

	@Override
	protected void onEvent(final ShareWishlistEvent event)
	{
		LOG.debug("Inside Share wislist Event");
		final Wishlist2Model wishlistModel = ((WishlistProcessModel)event.getSource()).getWishlist();
		final UserModel customer = ((WishlistProcessModel)event.getSource()).getUser();
		final WishlistProcessModel wishlistProcessModel = (WishlistProcessModel) getBusinessProcessService().createProcess(
				SHARE_WISHLIST_EMAIL_PROCESS_LABEL + wishlistModel.getWishlistUid() + "-" + System.currentTimeMillis(),
				SHARE_WISHLIST_EMAIL_PROCESS);
		wishlistProcessModel.setWishlist(wishlistModel);
		wishlistProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		wishlistProcessModel.setUser(customer);
		wishlistProcessModel.setToEmail(event.getToEmail());
		wishlistProcessModel.setSenderEmail(event.getSenderEmail());
		wishlistProcessModel.setSenderName(event.getSenderName());
		wishlistProcessModel.setSubject(event.getSubject());
		wishlistProcessModel.setSendersMessage(event.getSenderMessage());
		wishlistProcessModel.setAddLink(event.getAddLink());
		wishlistProcessModel.setAttachPDF(event.getAttachPDF());
		wishlistProcessModel.setSoldTo(event.getSoldTo());
		wishlistProcessModel.setEmailAttachmentModelList(event.getEmailAttachmentList());
		getModelService().save(wishlistProcessModel);
		getBusinessProcessService().startProcess(wishlistProcessModel);
	}

	public String getSiteUid() {
		return siteUid;
	}

	public void setSiteUid(String siteUid) {
		this.siteUid = siteUid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSenderMessage() {
		return senderMessage;
	}

	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}

	public Boolean getAttachPDF() {
		return attachPDF;
	}

	public void setAttachPDF(Boolean attachPDF) {
		this.attachPDF = attachPDF;
	}

	public Boolean getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(Boolean soldTo) {
		this.soldTo = soldTo;
	}

	public Boolean getAddLink() {
		return addLink;
	}

	public void setAddLink(Boolean addLink) {
		this.addLink = addLink;
	}

}

