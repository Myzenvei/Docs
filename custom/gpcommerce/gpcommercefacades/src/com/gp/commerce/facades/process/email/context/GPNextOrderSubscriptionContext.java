/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.GPSubscriptionNextOrderEmailProcessModel;
import com.gp.commerce.core.util.GPCommerceCoreUtils;
import com.gp.commerce.facades.populators.GPCustomerPopulator;
import com.gp.commerce.facades.subscription.populators.GPSubscriptionDefaultCartPopulator;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;


import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributesContextModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;


public class GPNextOrderSubscriptionContext  extends GPAbstractEmailContext<GPSubscriptionNextOrderEmailProcessModel> {
	
	private static Logger LOG = Logger.getLogger(GPNextOrderSubscriptionContext.class);

	
	public static final String CART = "cartData";
	public static final String EMAIL = "email";
	public static final String FIRSTNAME="firstName";
	public static final String NEXTORDERDATE = "nextOrderDate";

	
	@Resource(name = "cartPopulator")
	private GPSubscriptionDefaultCartPopulator cartPopulator;
	
	@Resource(name = "customerPopulator")
	private GPCustomerPopulator gpCustomerPopulator;
	
	@Resource(name = "contextualAttributeValuesSessionService")
	private ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;

	@Resource(name = "gpCommerceCoreUtils")
	private GPCommerceCoreUtils gpCommerceCoreUtils;

	
	@Override
	public void init(final GPSubscriptionNextOrderEmailProcessModel nextOrderEmailProcess, final EmailPageModel emailPageModel)
	{
		super.init(nextOrderEmailProcess, emailPageModel);
		
		CartData cartData=new CartData();
		SimpleDateFormat sdf=new SimpleDateFormat("MMMMM dd, yyyy");
		CustomerData customerData=new CustomerData();
		cartPopulator.populate(nextOrderEmailProcess.getSubscriptionCart(), cartData);
		gpCustomerPopulator.populate((CustomerModel) nextOrderEmailProcess.getSubscriptionCart().getUser(), customerData);
		put(FIRSTNAME,customerData.getFirstName());
		
		put(NEXTORDERDATE,sdf.format(calculateNextOrderShipmentDate(nextOrderEmailProcess.getSubscriptionCart())));
		put(CART,cartData);
	}
	
	
	private Date calculateNextOrderShipmentDate(GPSubscriptionCartModel subscriptionCart) {
		Calendar currentDate=Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0); 
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		try {
			Optional<AbstractOrderEntryModel> cartEntry = subscriptionCart.getEntries().stream().findFirst();
			final ContextualAttributesContextModel currentContext = subscriptionCart.getStore()
					.getContextualAttributesContext();
			contextualAttributeValuesSessionService.setCurrentContext(currentContext);
			Integer nDays = new Integer(0);
			if (null != cartEntry.get().getProduct()) {
				if(null!=gpCommerceCoreUtils.getProductAttribute(cartEntry.get().getProduct(),"nDaysBeforeSubscription"))
					nDays = (Integer) gpCommerceCoreUtils.getProductAttribute(cartEntry.get().getProduct(),"nDaysBeforeSubscription");
			}
			currentDate.add(Calendar.DAY_OF_YEAR, nDays.intValue());
			return currentDate.getTime();
		}
		catch(Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return currentDate.getTime();
	}


	@Override
	protected BaseSiteModel getSite(final GPSubscriptionNextOrderEmailProcessModel nextOrderEmailProcess)
	{
		return nextOrderEmailProcess.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final GPSubscriptionNextOrderEmailProcessModel nextOrderEmailProcess)
	{
		return nextOrderEmailProcess.getCustomer();
	}

	@Override
	protected LanguageModel getEmailLanguage(final GPSubscriptionNextOrderEmailProcessModel nextOrderEmailProcess)
	{
		return nextOrderEmailProcess.getLanguage();
	}
	
	


	

	
}
