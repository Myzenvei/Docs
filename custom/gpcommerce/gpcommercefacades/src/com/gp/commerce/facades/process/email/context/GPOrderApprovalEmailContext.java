/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;
import de.hybris.platform.acceleratorservices.email.EmailService;

import javax.annotation.Resource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;


/**
 * Gp Order Approval email context for approval email
 */
public class GPOrderApprovalEmailContext extends OrderNotificationEmailContext
{

	private static final String ORDER_ADMIN_EMAIL = "ORDER_ADMIN_EMAIL";

	private static final String APPROVAL_LINK = "approvalLink";

	private static final String CUSTOMER_NAME = "customerName";

	private Converter<OrderModel, OrderData> orderConverter;

	@Resource(name = "emailService")
	private EmailService emailService;
	String approvalLink;
	
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	public void init(final OrderProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{

		super.init(businessProcessModel, emailPageModel);

		final OrderModel order = businessProcessModel.getOrder();

		approvalLink = "/businessorg/orderapproval/" + order.getCode();
		put(APPROVAL_LINK, getApprovalLink(approvalLink));
		put(CUSTOMER_NAME, gpDefaultCustomerNameStrategy.getName(order.getUser().getName()));
		put(ORDER_ADMIN_EMAIL, true);

		if (null!=businessProcessModel && businessProcessModel.getAdminDetails() instanceof B2BCustomerModel)
		{
			put(DISPLAY_NAME, ((B2BCustomerModel)businessProcessModel.getAdminDetails()).getDisplayName());
			put(EMAIL, ((B2BCustomerModel)businessProcessModel.getAdminDetails()).getEmail());
		}
		else
		{
			put(DISPLAY_NAME, GpcommerceCoreConstants.ADMIN_INITIAL_CAP);
			put(EMAIL, Config.getParameter(GpcommerceFacadesConstants.B2B_APPROVAL_EMAIL));
		}
	}

	public String getUrlEncoding() {
		final String url = getUrlEncodingAttributes();
		return url.contains("powertools") ? "":url;
	}

	public String getApprovalLink(final String approve)
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncoding(), true, approve);
	}

	@Override
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Override
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}


}
