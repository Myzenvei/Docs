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
package com.gp.b2baccaddon.commerce.checkout.steps.validation.impl;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import com.gp.b2baccaddon.commerce.checkout.steps.validation.AbstractB2BCheckoutStepValidator;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class DefaultB2BDeliveryAddressCheckoutStepValidator extends AbstractB2BCheckoutStepValidator
{
	@Override
	protected ValidationResults doValidateOnEnter(final RedirectAttributes redirectAttributes)
	{
		final B2BPaymentTypeData checkoutPaymentType = getCheckoutFacade().getCheckoutCart().getPaymentType();

		if (checkoutPaymentType == null)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.paymentType.notprovided");
			return ValidationResults.REDIRECT_TO_PAYMENT_TYPE;
		}

		if (CheckoutPaymentType.ACCOUNT.getCode().equals(checkoutPaymentType.getCode())
				&& getCheckoutFacade().getCheckoutCart().getCostCenter() == null)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.costCenter.notprovided");
			return ValidationResults.REDIRECT_TO_PAYMENT_TYPE;
		}

		return ValidationResults.SUCCESS;
	}
}