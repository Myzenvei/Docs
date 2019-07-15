/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.subscription.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.GPSubscriptionFrequencyModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.strategies.GPSubscriptionAddToCartStrategy;
import com.gp.commerce.subscription.dao.GPSubscriptionDao;
import com.gp.commerce.subscription.service.GPSubscriptionService;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributeValueModel;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

public class GPSubscriptionServiceImpl implements GPSubscriptionService {

	@Resource(name="productService")
	private ProductService productService;
	
	@Resource(name = "subscriptionCommerceAddToCartStrategy")
	private GPSubscriptionAddToCartStrategy subscriptionCommerceAddToCartStrategy;

	@Resource(name = "subscriptionCartDaoImpl")
	private GPSubscriptionDao subscriptionCartDaoImpl;

	
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService ;
	
   	@Resource(name="contextualAttributeValuesSessionService")
   	private  ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;
   	
   	@Resource(name = "cardPaymentInfoReverseConverter")
   	private Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReverseConverter;
   	
   	@Resource(name = "modelService")
	private ModelService modelService;
   	
   	@Resource(name = "userService")
	private GPUserService userService;
   	
   	@Resource(name="defaultCustomerAccountService")
   	private DefaultCustomerAccountService accountService;
   	
	@Resource(name = "addressReversePopulator")
	private Populator<AddressData, AddressModel> addressReversePopulator;
	

	@Override
	public List<GPSubscriptionFrequencyModel> getSubsfrequencyforProduct(String productcode) {
		List<GPSubscriptionFrequencyModel> gpSubscriptionfreqList = new ArrayList<>();
		ProductModel product = productService.getProductForCode(productcode);
		if (CollectionUtils.isNotEmpty(product.getContextualAttributeValues())) {   
			for (final ContextualAttributeValueModel contextualAttributeValue : product
					.getContextualAttributeValues()) {
				if (null != contextualAttributeValue && contextualAttributeValue.getContext()
						.equals(contextualAttributeValuesSessionService.getCurrentContext())) {
					gpSubscriptionfreqList = contextualAttributeValue.getSubscriptionfrequency();
				}
			}
		}
		return gpSubscriptionfreqList;
	}
	
	@Override
	public List<GPSubscriptionCartModel> getSubscriptionCartModels(String susbscriptionCartStatus, boolean isActive)
	{
	return subscriptionCartDaoImpl.getSubscriptionCartModels(susbscriptionCartStatus,isActive);
	}
	
	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		return subscriptionCommerceAddToCartStrategy.addToCart(parameter);
	}

	@Override
	public List<GPSubscriptionCartModel> cancelSubscription(String code) {
		return subscriptionCartDaoImpl.cancelSubscription(code);
	}
	
	@Override
	public List<GPSubscriptionCartModel> getActiveSubscriptions(UserModel currentUser) 
	{
		return subscriptionCartDaoImpl.getActiveSubscriptions(currentUser);
	}
	
	@Override
	public List<GPSubscriptionCartModel> getInActiveSubscriptions(UserModel currentUser) 
	{
		return subscriptionCartDaoImpl.getInActiveSubscriptions(currentUser);
	}

	@Override
	public void updateSubscription(AddressData addData, CCPaymentInfoData payInfoData, String code) {
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		final int entryNumber=0;
		List<GPSubscriptionCartModel> subscriptionCartList = subscriptionCartDaoImpl.cancelSubscription(code);
		GPSubscriptionCartModel subscriptionCartModel = subscriptionCartList.stream().findFirst().orElse(null);
		if(null != payInfoData && null != payInfoData.getId() && null !=subscriptionCartModel) {
			final CreditCardPaymentInfoModel paymentInfoModel = accountService.getCreditCardPaymentInfoForCode(currentCustomer, payInfoData.getId());
			cardPaymentInfoReverseConverter.convert(payInfoData, paymentInfoModel);
			subscriptionCartModel.setPaymentInfo(paymentInfoModel);
		}
		if(null != addData && null != addData.getId() && null !=subscriptionCartModel) {
			final AddressModel addressModel = accountService.getAddressForCode(currentCustomer, addData.getId());
			addressReversePopulator.populate(addData, addressModel);
			final SplitEntryModel splitEntryModel = modelService.create(SplitEntryModel.class);
			final AbstractOrderEntryModel entryModel = getCartEntryForNumber(subscriptionCartModel, entryNumber);
			final List<SplitEntryModel> spList= new ArrayList<>();
			
			splitEntryModel.setCode(subscriptionCartModel.getCode()+"-"+entryNumber+"-"+System.currentTimeMillis());
			splitEntryModel.setDeliveryAddress(addressModel);
			splitEntryModel.setProductCode(entryModel.getProduct().getCode());
			splitEntryModel.setQty(entryModel.getQuantity().toString());
			splitEntryModel.setPrice(BigDecimal.valueOf(entryModel.getTotalPrice()).doubleValue());
			subscriptionCartModel.setDeliveryAddress(addressModel);
			modelService.save(splitEntryModel);
			spList.add(splitEntryModel);
			
			entryModel.setSplitEntry(spList);
			modelService.save(entryModel);
		}
		modelService.save(subscriptionCartModel);
	}
	
	protected AbstractOrderEntryModel getCartEntryForNumber(final GPSubscriptionCartModel cartModel, final long number) throws GPException //NOSONAR
	{
		final List<AbstractOrderEntryModel> entries = cartModel.getEntries();
		if (entries != null && !entries.isEmpty())
		{
			final Integer requestedEntryNumber = Integer.valueOf((int) number);
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
				{
					return entry;
				}
			}
		}
		throw new GPException("Entry not found", String.valueOf(number));
	}
	
	public ProductService getProductService() {
		return productService;
	}
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
}
