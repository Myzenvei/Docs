/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartRestorationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class GPDefaultCommerceCartRestorationStrategy extends DefaultCommerceCartRestorationStrategy {

    private static final Logger LOG = Logger.getLogger(GPDefaultCommerceCartRestorationStrategy.class);

    @Override
    public CommerceCartRestoration restoreCart(final CommerceCartParameter parameter) throws CommerceCartRestorationException
    {
        final CartModel cartModel = parameter.getCart();
        final CommerceCartRestoration restoration = new CommerceCartRestoration();
        final List<CommerceCartModification> modifications = new ArrayList<>();
        if (cartModel != null)
        {
            if (getBaseSiteService().getCurrentBaseSite().equals(cartModel.getSite()))
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Restoring from cart " + cartModel.getCode() + ".");
                }
                if (isCartInValidityPeriod(cartModel))
                {
                    cartModel.setCalculated(Boolean.FALSE);
                    if (!cartModel.getPaymentTransactions().isEmpty())
                    {
                        // clear payment transactions
                        clearPaymentTransactionsOnCart(cartModel);
                        // reset guid since its used as a merchantId for payment subscriptions and is a base id for generating PaymentTransaction.code
                        // see de.hybris.platform.payment.impl.DefaultPaymentServiceImpl.authorize(DefaultPaymentServiceImpl.java:177)
                        cartModel.setGuid(getGuidKeyGenerator().generate().toString());
                    }

                    getModelService().save(cartModel);

                    try
                    {
                        if (cartModel.getCurrency()!=null) {
                            getCommerceCommonI18NService().setCurrentCurrency(cartModel.getCurrency());
                        }
                        getCommerceCartCalculationStrategy().recalculateCart(parameter);
                    }
                    catch (final IllegalStateException ex)
                    {
                        LOG.error("Failed to recalculate order [" + cartModel.getCode() + "]", ex);
                    }

                    getCartService().setSessionCart(cartModel);

                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Cart " + cartModel.getCode() + " was found to be valid and was restored to the session.");
                    }
                }
                else
                {
                    try
                    {
                        modifications.addAll(rebuildSessionCart(parameter));
                    }
                    catch (final CommerceCartModificationException e)
                    {
                        throw new CommerceCartRestorationException(e.getMessage(), e);
                    }
                }
                if (getCommerceCommonI18NService().getCurrentCurrency().getSapCode().equalsIgnoreCase(cartModel.getCurrency().getSapCode())) {
                    getCommerceCommonI18NService().setCurrentCurrency(cartModel.getCurrency());
                    getCommerceCartCalculationStrategy().calculateCart(parameter);
                }
            }
            else
            {
                LOG.warn(String.format("Current Site %s does not equal to cart %s Site %s",
                        getBaseSiteService().getCurrentBaseSite(), cartModel, cartModel.getSite()));
            }
        }
        restoration.setModifications(modifications);
        return restoration;
    }
}
