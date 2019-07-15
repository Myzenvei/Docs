/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Extension of {@link DefaultEmailGenerationService}
 */
public class DefaultGPEmailGenerationService extends DefaultEmailGenerationService
{

	private static final String SHARE_PRODUCT = "SHARE_PRODUCT";
	private static final String SHARE_CART = "SHARE_CART";
	private static final String RECIPIENTS_EMAIL_ID = "RECIPIENTS_EMAIL_ID";
	private static final String INVITEDUSEREMAIL = "INVITEDUSEREMAIL";
	private static final String APPROVAL_EMAIL_TO_ADMINS = "APPROVAL_EMAIL_TO_ADMINS";
	private static final String INVITE_EMAIL_TO_USER = "INVITE_EMAIL_TO_USER";
	private static final String QUICK_ORDER = "QUICK_ORDER";
	private static final String FAILED_ORDER_CONSIGNMENT = "FAILED_ORDER_CONSIGNMENT";
	private static final String ORDER_ADMIN_EMAIL = "ORDER_ADMIN_EMAIL";
	private static final String SHARE_PRODUCT_RESOURCE = "SHARE_PRODUCT_RESOURCE";
	private static final String CSR_EMAIL_LIST = "CSR_EMAIL_LIST";
	private static final String B2B_USER_UPDATE_EMAILS_TO_ADMINS = "B2B_USER_UPDATE_EMAILS_TO_ADMINS";
	private static final String BCCEMAIL = "BCCEMAIL";
	private static final String GP_EMAIL_CC_LIST = "gp.email.cc.list";
	private static final String ATTACHMENTS = "ATTACHMENTS";
	private static final String ATTACHMENT_SHARE_PRODUCT = "ATTACHMENT_SHARE_PRODUCT";
	private static final String ATTACHMENT_QUICK_ORDER = "ATTACHMENT_QUICK_ORDER";
	private static final String ATTACHMENT_FAILED_ORDER = "ATTACHMENT_FAILED_ORDER";
	private static final String ORDER_ISSUE_NOTIFY = "ORDER_ISSUE_NOTIFY";
	private static final String EMAIL = "email";
	private static final String DISPLAY_NAME = "displayName";
	private static final Logger LOG = Logger.getLogger(DefaultGPEmailGenerationService.class);
	private static final String TAX_EXEMPTION_SUBMIT = "TAX_EXEMPTION_SUBMIT";
	private static final String TAX_ATTACHMENT = "TAX_ATTACHMENT";
	private static final String SHARE_WISHLIST = "SHARE_WISHLIST";
	
	private static final String INSTALLATION_PRODUCT = "INSTALLATION_PRODUCT";
	private static final String INSTALLATION_NOTIFICATION_EMAIL_ID = "INSTALLATION_NOTIFICATION_EMAIL_ID";

	/**
	 * Generates EmailMessage give business process and cms email page.
	 *
	 * @param businessProcessModel
	 *           Business process object
	 * @param emailPageModel
	 *           Email page
	 * @return EmailMessage the email message model
	 */
	@Override
	public EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
			final AbstractEmailContext<BusinessProcessModel> emailContext)
	{
		LOG.debug("Email for " + emailSubject);
		
		
		//Order Issues Notification
		 	
				final Object isOrderIssuesNotify = emailContext.get(ORDER_ISSUE_NOTIFY);
				if(null != isOrderIssuesNotify && (Boolean)isOrderIssuesNotify && StringUtils.contains(emailContext.getToEmail(), ",")) 
				{
					List<EmailAddressModel> toEmailsId = new ArrayList<>();
					toEmailsId=getEmailList(emailContext.getToEmail());

					final String emailCCList = Config.getParameter(GP_EMAIL_CC_LIST);

					final List<EmailAddressModel> ccEmailList = getEmailList(emailCCList);


					final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
							emailContext.getFromDisplayName());
					final List<EmailAttachmentModel> attachments = (List<EmailAttachmentModel>) emailContext.get(ATTACHMENTS);
					return getEmailService().createEmailMessage(toEmailsId, (List<EmailAddressModel>) ccEmailList,
							new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, attachments);
			
				}
				

		final Object isShareProductEmail = emailContext.get(SHARE_PRODUCT);
		 List<EmailAddressModel> toEmails = new ArrayList<>();
		if(null != isShareProductEmail && (Boolean)isShareProductEmail && StringUtils.contains(emailContext.getToEmail(), ",")) {
			toEmails=getEmailList(emailContext.getToEmail());
		}else {
			final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
					emailContext.getToDisplayName());
			toEmails.add(toAddress);
		}
		
		final Object isShareWishlistEnabled = emailContext.get(SHARE_WISHLIST);
		 List<EmailAddressModel> shareWishlistToEmails = new ArrayList<>();
		if(null != isShareWishlistEnabled && (Boolean)isShareWishlistEnabled && StringUtils.contains(emailContext.getToEmail(), ",")) {
			shareWishlistToEmails=getEmailList(emailContext.getToEmail());
		}else {
			final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
					emailContext.getToDisplayName());
			shareWishlistToEmails.add(toAddress);
		}
		
		final Object isShareProductResourceEmail = emailContext.get(SHARE_PRODUCT_RESOURCE);
		 List<EmailAddressModel> toEmailsId = new ArrayList<>();
		if(null != isShareProductResourceEmail && (Boolean)isShareProductResourceEmail && StringUtils.contains(emailContext.getToEmail(), ",")) {
			toEmailsId=getEmailList(emailContext.getToEmail());
		}else {
			final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
					emailContext.getToDisplayName());
			toEmailsId.add(toAddress);
		}
		
		
		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());
		final Object isShareCartEmail = emailContext.get(SHARE_CART);
		final Object toEmailList = emailContext.get(RECIPIENTS_EMAIL_ID);
		final List<EmailAddressModel> toInvitedEmails = new ArrayList<>();
		final String invitedUserEmail = null != emailContext.get(INVITEDUSEREMAIL) ? (String) emailContext.get(INVITEDUSEREMAIL) : null;
		if (null != invitedUserEmail)
		{
			final EmailAddressModel invitedEmail = getEmailService().getOrCreateEmailAddressForEmail(
					(String) emailContext.get(INVITEDUSEREMAIL), (String) emailContext.get(INVITEDUSEREMAIL));
			toInvitedEmails.add(invitedEmail);
		}

		final Object isTaxExemptionEmail = emailContext.get(TAX_EXEMPTION_SUBMIT);
		 List<EmailAddressModel> toGpTaxTeamEmail = new ArrayList<>();
		if(null != isTaxExemptionEmail && (Boolean)isTaxExemptionEmail && StringUtils.contains(emailContext.getToEmail(), ",")) {
			toGpTaxTeamEmail=getEmailList(emailContext.getToEmail());
		}else {
			final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
					emailContext.getToDisplayName());
			toGpTaxTeamEmail.add(toAddress);
		}

		final Object approvalEmailToAdmins = emailContext.get(APPROVAL_EMAIL_TO_ADMINS);
		final Object isInviteEmailTOUser = emailContext.get(INVITE_EMAIL_TO_USER);

		final Object quickOrderEmail = emailContext.get(QUICK_ORDER);
		final Object isFailedOrderConsignEmail= emailContext.get(FAILED_ORDER_CONSIGNMENT);
		final Object isOrderPendingEmail = emailContext.get(ORDER_ADMIN_EMAIL);
		final Object toCsrEmailList = emailContext.get(CSR_EMAIL_LIST);
		final Object b2bUserUpdateEmailsToAdmins =emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS);
		final List<EmailAddressModel> bccEmails = new ArrayList<>();

		final String bccEmail = null != emailContext.get(BCCEMAIL) ? (String) emailContext.get(BCCEMAIL) : null;

		final String emailCCList = Config.getParameter(GP_EMAIL_CC_LIST);

		final List<EmailAddressModel> ccEmailList = getEmailList(emailCCList);

		final Object isInstallationProduct = emailContext.get(INSTALLATION_PRODUCT);
		final Object installationToEmailList = emailContext.get(INSTALLATION_NOTIFICATION_EMAIL_ID);


		if (null != bccEmail)
		{

			final EmailAddressModel bccEmailAddressModel = getEmailService().getOrCreateEmailAddressForEmail(bccEmail, bccEmail);

			bccEmails.add(bccEmailAddressModel);

		}
		if (Boolean.TRUE.equals((Boolean) isShareCartEmail))
		{
			final List<EmailAttachmentModel> attachments = (List<EmailAttachmentModel>) emailContext.get(ATTACHMENTS);
			return getEmailService().createEmailMessage((List<EmailAddressModel>) toEmailList, (List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody,
					attachments);
		}
		else if (Boolean.TRUE.equals((Boolean) isShareProductEmail)
				&& CollectionUtils.isNotEmpty((Collection) emailContext.get(ATTACHMENT_SHARE_PRODUCT)))
		{
			final List<EmailAttachmentModel> attachments = (List<EmailAttachmentModel>) emailContext.get(ATTACHMENT_SHARE_PRODUCT);
			return getEmailService().createEmailMessage(toEmails, (List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody,
					attachments);
		}
		else if (null != quickOrderEmail && (boolean) quickOrderEmail
				&& CollectionUtils.isNotEmpty((Collection) emailContext.get(ATTACHMENT_QUICK_ORDER)))
		{
			final List<EmailAttachmentModel> attachments = (List<EmailAttachmentModel>) emailContext.get(ATTACHMENT_QUICK_ORDER);
			return getEmailService().createEmailMessage((List<EmailAddressModel>) toEmailList, (List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody,
					attachments);
		}
		else if (Boolean.TRUE.equals((Boolean) approvalEmailToAdmins))
		{
			return getEmailService().createEmailMessage((List<EmailAddressModel>) toEmailList,(List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);
		}
		else if (Boolean.TRUE.equals((Boolean) isInviteEmailTOUser))
		{
			return getEmailService().createEmailMessage(toInvitedEmails, (List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);
		}
		else if(Boolean.TRUE.equals((Boolean) isFailedOrderConsignEmail))
		{
			final List<EmailAttachmentModel> attachments = (List<EmailAttachmentModel>) emailContext.get(ATTACHMENT_FAILED_ORDER);
			return getEmailService().createEmailMessage((List<EmailAddressModel>)toCsrEmailList, (List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, attachments);
		}else if(Boolean.TRUE.equals((Boolean) isOrderPendingEmail))
		{
			final List<EmailAddressModel> adminEmails = new ArrayList<>();
			final EmailAddressModel adminEmail = getEmailService().getOrCreateEmailAddressForEmail(
					(String) emailContext.get(EMAIL), (String) emailContext.get(DISPLAY_NAME));
			adminEmails.add(adminEmail);
			return getEmailService().createEmailMessage(adminEmails, (List<EmailAddressModel>) ccEmailList, bccEmails, fromAddress,
					emailContext.getFromEmail(), emailSubject, emailBody, null);
		}else if(Boolean.TRUE.equals((Boolean) isTaxExemptionEmail))
		{
			final List<EmailAttachmentModel> attachments = (List<EmailAttachmentModel>) emailContext.get(TAX_ATTACHMENT);
			return getEmailService().createEmailMessage(toGpTaxTeamEmail,ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, attachments);
		}
		else if(Boolean.TRUE.equals((Boolean) b2bUserUpdateEmailsToAdmins))
		{
			return getEmailService().createEmailMessage((List<EmailAddressModel>) toEmailList,(List<EmailAddressModel>) ccEmailList,
					(List<EmailAddressModel>) bccEmails, fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);
		}
		
		else if (Boolean.TRUE.equals((Boolean) isShareProductResourceEmail))
		{
			return getEmailService().createEmailMessage(toEmailsId, (List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody,
					null);
		}
		else if (Boolean.TRUE.equals((Boolean) isShareWishlistEnabled))
		{
			final List<EmailAttachmentModel> attachments = (List<EmailAttachmentModel>) emailContext.get(ATTACHMENTS);
			return getEmailService().createEmailMessage(shareWishlistToEmails, new ArrayList<EmailAddressModel>(),
					bccEmails, fromAddress, emailContext.getFromEmail(), emailSubject, emailBody,
					attachments);
		}
		
		else if(Boolean.TRUE.equals((Boolean) isInstallationProduct)){
			return getEmailService().createEmailMessage((List<EmailAddressModel>) installationToEmailList, (List<EmailAddressModel>) ccEmailList,
					new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody,
					null);
		}
		
		else
		{
			return getEmailService().createEmailMessage(toEmails, (List<EmailAddressModel>) ccEmailList, bccEmails, fromAddress,
					emailContext.getFromEmail(), emailSubject, emailBody, null);
		}
	}

	/**
	 * Gets email list
	 * @param emailList
	 * 			the email list
	 * @return email list model
	 */
	public List<EmailAddressModel>  getEmailList(final String emailList) {
		final List<EmailAddressModel> emailListModel =new ArrayList<>();
		if(StringUtils.isNotBlank(emailList))
		{
		final String[] emailArray = emailList.split(",");
		for(final String email : emailArray) {
			final EmailAddressModel ccEmailModel= getEmailService().getOrCreateEmailAddressForEmail(email, "");
			emailListModel.add(ccEmailModel);
		}
		}
		return emailListModel ;
	}

}
