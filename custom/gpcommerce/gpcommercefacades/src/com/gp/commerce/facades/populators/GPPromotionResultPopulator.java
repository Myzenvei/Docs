/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commercefacades.order.converters.populator.PromotionResultPopulator;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderAdjustTotalActionModel;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;

public class GPPromotionResultPopulator extends PromotionResultPopulator {
	
	
	@Override
	public void populate(PromotionResultModel source, PromotionResultData target) {
		// TODO Auto-generated method stub
		super.populate(source, target);
		target.setAppliedCouponCodes(getCouponCodesAppliedFromPromotion(source));
		
		
		
	}
	
	private List<String> getCouponCodesAppliedFromPromotion(PromotionResultModel source) {
		List<String> coupons = new ArrayList<>();
		if(null != source.getAllPromotionActions())
		{
			for (AbstractPromotionActionModel action : source.getAllPromotionActions()) {
				if(action instanceof RuleBasedOrderAdjustTotalActionModel){
					if(CollectionUtils.isNotEmpty(((RuleBasedOrderAdjustTotalActionModel) action).getUsedCouponCodes())){
						coupons.addAll(((RuleBasedOrderAdjustTotalActionModel) action).getUsedCouponCodes());
					}
				}
			}
		}
		return coupons;
		
	}

}
