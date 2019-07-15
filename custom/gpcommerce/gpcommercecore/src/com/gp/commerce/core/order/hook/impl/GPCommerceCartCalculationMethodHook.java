/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.hook.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.price.service.GPCommercePriceService;

import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;

public class GPCommerceCartCalculationMethodHook implements CommerceCartCalculationMethodHook {

	private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(GPCommerceCartCalculationMethodHook.class);

	@Resource(name = "gpDefaultCommercePriceService")
	private GPCommercePriceService gpDefaultCommercePriceService;
	private ModelService modelService;
	private CalculationService calculationService;
	private CartService cartService;

	private List<AbstractOrderEntryModel> giveAwayEntriesSnapShotBeforePromotion;

	@Override
	public void afterCalculate(CommerceCartParameter parameter) {
		restoreAndSaveCartAttributesForGiveAwayProducts(parameter);
	 
	}

	protected void restoreAndSaveCartAttributesForGiveAwayProducts(final CommerceCartParameter parameter) {
		final CartModel cartModel = parameter.getCart();
		boolean recalculateTotals=false ;
		if (giveAwayEntriesSnapShotBeforePromotion != null && !giveAwayEntriesSnapShotBeforePromotion.isEmpty()) {
			getModelService().refresh(cartModel);

			List<AbstractOrderEntryModel> giveAwayEntriesSnapShotAfterPromotion = cartModel.getEntries().stream()
					.filter(x -> x.getGiveAway()).collect(Collectors.toList());

				for (AbstractOrderEntryModel abstractOrderEntryModel : giveAwayEntriesSnapShotAfterPromotion) {
				AbstractOrderEntryModel obj = giveAwayEntriesSnapShotBeforePromotion.stream()
						.filter(x -> x.getProduct().getCode().equalsIgnoreCase(abstractOrderEntryModel.getProduct().getCode()))
						.findFirst().orElse(null);
				if (obj != null) {
					try {
						if (CollectionUtils.isNotEmpty(obj.getSplitEntry())) {
							abstractOrderEntryModel.setSplitEntry(obj.getSplitEntry());
						}
						else
						{
							Optional<AbstractOrderEntryModel> firstAnyAddressInCart=cartModel.getEntries().stream().filter(x -> !x.getSplitEntry().isEmpty()).findAny();
							if (firstAnyAddressInCart.isPresent() &&  firstAnyAddressInCart.get()!=null && !firstAnyAddressInCart.get().getSplitEntry().isEmpty()) {
								final SplitEntryModel splitEntryModel = getModelService().clone(firstAnyAddressInCart.get().getSplitEntry().get(0));
								splitEntryModel.setProductCode(abstractOrderEntryModel.getProduct().getCode());
								splitEntryModel.setQty(abstractOrderEntryModel.getQuantity().toString());
								splitEntryModel.setPrice(BigDecimal.valueOf((abstractOrderEntryModel.getTotalPrice()/abstractOrderEntryModel.getQuantity())*Long.parseLong(splitEntryModel.getQty())).doubleValue());
								splitEntryModel.setCode(abstractOrderEntryModel.getOrder().getCode() + "_" + abstractOrderEntryModel.getEntryNumber() + "_" + System.currentTimeMillis());
								getModelService().save(splitEntryModel);
								final List<SplitEntryModel> spList= new ArrayList<>();
								spList.add(splitEntryModel);
								abstractOrderEntryModel.setSplitEntry(spList);

							}
						}
						getModelService().save(abstractOrderEntryModel);
						recalculateTotals=true ;
						
					} catch (Exception ex) {
						LOGGER.error(ex);
					}

				}
				else
				{

				}
			}


		}
	}

	@Override
	public void beforeCalculate(final CommerceCartParameter parameter) {
		final CartModel cartModel = parameter.getCart();
		createGiveAwayEntriesSnapShotBeforePromotion(cartModel);   
		for (final AbstractOrderEntryModel entry : cartModel.getEntries()) {
			double mapPrice = gpDefaultCommercePriceService.getMapPriceForProduct(entry.getProduct());   
			entry.setMapPrice(mapPrice);
		}
	}

	public void createGiveAwayEntriesSnapShotBeforePromotion(final CartModel cartModel) {
		List<AbstractOrderEntryModel> giveAwayEntriesBeforePromotion = cartModel.getEntries().stream()
				.filter(x -> x.getGiveAway()).collect(Collectors.toList());
		giveAwayEntriesSnapShotBeforePromotion = new ArrayList<>();

		for (AbstractOrderEntryModel var : giveAwayEntriesBeforePromotion) {
			AbstractOrderEntryModel model = new AbstractOrderEntryModel();
			model.setSplitEntry(var.getSplitEntry());
			model.setProduct(var.getProduct());
			giveAwayEntriesSnapShotBeforePromotion.add(model);
		}
	}

	public GPCommercePriceService getGpDefaultCommercePriceService() {
		return gpDefaultCommercePriceService;
	}

	public void setGpDefaultCommercePriceService(GPCommercePriceService gpDefaultCommercePriceService) {
		this.gpDefaultCommercePriceService = gpDefaultCommercePriceService;
	}

	public CalculationService getCalculationService() {
		return calculationService;
	}

	public void setCalculationService(CalculationService calculationService) {
		this.calculationService = calculationService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}

	public List<AbstractOrderEntryModel> getGiveAwayEntriesSnapShotBeforePromotionMap() {
		return giveAwayEntriesSnapShotBeforePromotion;
	}

	public void setGiveAwayEntriesSnapShotBeforePromotionMap(
			List<AbstractOrderEntryModel> giveAwayEntriesSnapShotBeforePromotionMap) {
		this.giveAwayEntriesSnapShotBeforePromotion = giveAwayEntriesSnapShotBeforePromotionMap;
	}
}
