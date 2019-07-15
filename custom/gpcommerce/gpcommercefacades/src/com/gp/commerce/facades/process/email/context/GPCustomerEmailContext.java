/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.acceleratorservices.email.EmailService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.model.GPItemProcessModel;

/**
 * Velocity context for a forgotten password email.
 */
public class GPCustomerEmailContext extends CustomerEmailContext
{

	private Converter<AddressModel, AddressData> addressConverter;
	private static final Logger LOG = Logger.getLogger(GPCustomerEmailContext.class);
	private static final String RECIPIENTS_EMAIL_ID = "RECIPIENTS_EMAIL_ID";
	private static final String INVITEDUSEREMAIL = "INVITEDUSEREMAIL";
	private static final String INVITE_EMAIL_TO_USER = "INVITE_EMAIL_TO_USER";
	private static final String B2B_USER_UPDATE_EMAILS_TO_ADMINS = "B2B_USER_UPDATE_EMAILS_TO_ADMINS";
	private static final String BCCEMAIL = "BCCEMAIL";
	private static final String EMAIL = "EMAIL";
	private static final String APPROVAL_EMAIL_TO_ADMINS = "APPROVAL_EMAIL_TO_ADMINS";

	private AddressData addressData;
	private CustomerData customerData;
	private CustomerData invitedUser;
	private String bccEmail;
	private String emailSubject;
	private CustomerData adminData;
	private String isBackOfficeUser;
	private EmailService emailService;
	private String token;
	private int expiresInMinutes = 24;
	private String adminName;
	private String selectedRole;
	
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public int getExpiresInMinutes() {
		return expiresInMinutes;
	}

	public void setExpiresInMinutes(final int expiresInMinutes) {
		this.expiresInMinutes = expiresInMinutes;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(final String token)
	{
		this.token = token;
	}
	public String getUrlEncoding() {
		final String url = getUrlEncodingAttributes();
		return url.contains("powertools") ? "":url;
	}

	public String getURLEncodedToken() throws UnsupportedEncodingException
	{
		return URLEncoder.encode(token, "UTF-8");
	}

	public String getRequestResetPasswordUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),getUrlEncoding(), false, "/login/pw/request/external");
	}

	public String getSecureRequestResetPasswordUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),getUrlEncoding(), true, "/login/pw/request/external");
	}

	public String getResetPasswordUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncoding(), false, "/login/pw/change",
				"token=" + getURLEncodedToken());
	}

	public String getSecureResetPasswordUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncoding(),true, "/login/pw/change",
				"token=" + getURLEncodedToken());
	}

	public String getDisplayResetPasswordUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),getUrlEncodingAttributes(), false, "/my-account/update-password");
	}

	public String getDisplaySecureResetPasswordUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),getUrlEncodingAttributes(), true, "/my-account/update-password");
	}

	public String getIsBackOfficeUser()
	{
		return isBackOfficeUser;
	}

	public void setIsBackOfficeUser(final String isBackOfficeUser)
	{
		this.isBackOfficeUser = isBackOfficeUser;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	public CustomerData getAdminData()
	{
		return adminData;
	}

	public void setAdminData(final CustomerData adminData)
	{
		this.adminData = adminData;
	}

	public String getEmailSubject()
	{
		return emailSubject;
	}

	public void setEmailSubject(final String emailSubject)
	{
		this.emailSubject = emailSubject;
	}

	public String getBccEmail()
	{
		return bccEmail;
	}

	public void setBccEmail(final String bccEmail)
	{
		this.bccEmail = bccEmail;
	}

	public CustomerData getInvitedUser()
	{
		return invitedUser;
	}

	public void setInvitedUser(final CustomerData invitedUser)
	{
		this.invitedUser = invitedUser;
	}

	public CustomerData getCustomerData()
	{
		return customerData;
	}

	public void setCustomerData(final CustomerData customerData)
	{
		this.customerData = customerData;
	}

	public AddressData getAddressData()
	{
		return addressData;
	}

	public void setAddressData(final AddressData addressData)
	{
		this.addressData = addressData;
	}

	public Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		if (storeFrontCustomerProcessModel instanceof GPItemProcessModel
				&& ((GPItemProcessModel) storeFrontCustomerProcessModel).getItem() instanceof AddressModel
				&& null != (AddressModel) ((GPItemProcessModel) storeFrontCustomerProcessModel).getItem())
		{
			final AddressModel addressModel = (AddressModel) ((GPItemProcessModel) storeFrontCustomerProcessModel).getItem();
			addressData = getAddressConverter().convert(addressModel);
			final List<String> recipientEmails = ((GPItemProcessModel) storeFrontCustomerProcessModel).getRecipientEmails();

			if (!recipientEmails.isEmpty())
			{
				final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
				for (final Object emailId : recipientEmails)
				{
					final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailId.toString(), GpcommerceCoreConstants.ADMIN_INITIAL_CAP);
					toEmails.add(toAddress);
				}
				put(APPROVAL_EMAIL_TO_ADMINS, true);
				put(RECIPIENTS_EMAIL_ID, toEmails);
			}
			if(LOG.isDebugEnabled())
			{
				LOG.debug("In GPCustomerEmailContext list of recipient emails:"+recipientEmails.toString());
			}
			emailSubject = ((GPItemProcessModel) storeFrontCustomerProcessModel).getEmailSubject();
		}

		else if (storeFrontCustomerProcessModel instanceof GPItemProcessModel)
		{
			final CustomerModel invited = ((GPItemProcessModel) storeFrontCustomerProcessModel).getInvitedCustomer();
			if (null != invited)
			{
				invitedUser = getCustomerConverter().convert(invited);
				put(EMAIL, getCustomerEmailResolutionService().getEmailForCustomer(invited));
				if(null!= invited.getSelectedRole()) {
				selectedRole = invited.getSelectedRole().getCode();
				}
			}

			put(RECIPIENTS_EMAIL_ID, ((GPItemProcessModel) storeFrontCustomerProcessModel).getRecipientEmails());
			put(BCCEMAIL, ((GPItemProcessModel) storeFrontCustomerProcessModel).getBccEmail());
			emailSubject = ((GPItemProcessModel) storeFrontCustomerProcessModel).getEmailSubject();
			if (emailSubject.equalsIgnoreCase(EmailSubjectType.INVITE_USER.getSubject()))
			{
				put(INVITE_EMAIL_TO_USER, true);
				put(INVITEDUSEREMAIL, invitedUser.getDisplayUid());
			}
			else if (emailSubject.equalsIgnoreCase(EmailSubjectType.CUSTOMER_REGISTRATION.getSubject()))
			{
				isBackOfficeUser = ((GPItemProcessModel) storeFrontCustomerProcessModel).getIsBackOfficeUser()?"Yes":"No";
				setToken(((GPItemProcessModel) storeFrontCustomerProcessModel).getToken());
			}
			else if(emailSubject.equalsIgnoreCase(EmailSubjectType.NEW_USER_REVIEW.getSubject())||emailSubject.equalsIgnoreCase(EmailSubjectType.L3_ADMIN_APPROVED.getSubject()))
			{
				final List<String> recipientEmails = ((GPItemProcessModel) storeFrontCustomerProcessModel).getRecipientEmails();

				if (!recipientEmails.isEmpty())
				{
					final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
					for (final Object emailId : recipientEmails)
					{
						final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailId.toString(), GpcommerceCoreConstants.ADMIN_INITIAL_CAP);
						toEmails.add(toAddress);
					}
					put(B2B_USER_UPDATE_EMAILS_TO_ADMINS, true);
					put(RECIPIENTS_EMAIL_ID, toEmails);
				}
			}
		}
		customerData = getCustomerConverter()
				.convert(((GPItemProcessModel) storeFrontCustomerProcessModel).getCustomer());
		adminName = ((GPItemProcessModel) storeFrontCustomerProcessModel).getAdminName();
		if(LOG.isDebugEnabled()){
			LOG.debug("customerData" + customerData);
		}
	}

	public String getAddressDetailsUrl() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),null, true, "/my-account/addresses");
    }

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}
	

}
