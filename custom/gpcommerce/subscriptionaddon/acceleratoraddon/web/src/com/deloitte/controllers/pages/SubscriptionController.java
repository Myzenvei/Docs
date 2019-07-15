/**
 *
 */
package com.deloitte.controllers.pages;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deloitte.facade.SubscriptionCartFacade;
import com.deloitte.service.impl.CustomPriceFactoryServiceImpl;


/**
 * @author asomjal
 *
 */
@Controller
@RequestMapping("/subscription/services")
public class SubscriptionController
{
	private static final Logger LOG = Logger.getLogger(SubscriptionController.class);

	@Resource(name = "customPriceFactoryService")
	private CustomPriceFactoryServiceImpl customPriceFactoryServiceImpl;

	@Resource(name = "subscriptionCartFacadeImpl")
	private SubscriptionCartFacade subscriptionCartFacadeImpl;

	@RequestMapping(value = "/getAllPricesForProduct", method = RequestMethod.GET)
	@ResponseBody
	public List<PriceData> getPriceInformationsForProduct(
			@RequestParam(value = "productCode", required = true) final String productCode)
	{
		return customPriceFactoryServiceImpl.getAllPricesForProduct(productCode);
	}

	@RequestMapping(value = "/subscription/addCartEntries", method = RequestMethod.GET)
	@ResponseBody
	public CartModificationData addCartEntry(@RequestParam(value = "cartId", required = false) final String cartId,
			@RequestParam(value = "productCode", required = true) final String productCode,
			@RequestParam(value = "qty", required = false, defaultValue = "1") final long qty,
			@RequestParam(value = "subscriptionFrequecy", required = true) final String subscriptionFrequecy)
			throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("addCartEntry: " + logParam("code", productCode) + ", " + logParam("qty", qty));
		}
		return subscriptionCartFacadeImpl.addToCartInternal(productCode, qty, subscriptionFrequecy, cartId);
	}

	@RequestMapping(value = "/getSubcriptionCartsForUser", method = RequestMethod.GET)
	@ResponseBody
	public List<CartData> getSubcriptionCartsForUser(@RequestParam(value = "userId", required = false) final String userId)
	{
		return subscriptionCartFacadeImpl.getSubcriptionCartsForUser(userId);
	}

	protected static String logParam(final String paramName, final long paramValue)
	{
		return paramName + " = " + paramValue;
	}

	protected static String logParam(final String paramName, final Long paramValue)
	{
		return paramName + " = " + paramValue;
	}

	protected static String logParam(final String paramName, final String paramValue)
	{
		return paramName + " = " + paramValue;
	}

}
