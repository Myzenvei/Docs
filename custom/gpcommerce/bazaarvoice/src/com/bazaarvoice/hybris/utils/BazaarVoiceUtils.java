package com.bazaarvoice.hybris.utils;

import de.hybris.platform.commercefacades.order.data.OrderData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Created by artlaber on 3/17/14.
 */
public abstract class BazaarVoiceUtils {

	public static final String ReplaceUnsupportedCharacters(String code) {
		return code.replaceAll("[^\\w\\*\\-.]", "_");
	}

	public static final BigDecimal getProductPrice(OrderData order, BigDecimal basePrice, String productCode, Map<String, Double> map) {
		if (order.isNet()) {
			return basePrice;
		} else {
			if (!map.isEmpty()) {
				BigDecimal productPrice = BigDecimal.valueOf(map.get(productCode));
				productPrice = productPrice.setScale(2, RoundingMode.HALF_EVEN);
				return productPrice;
			} else {
				return basePrice;
			}
		}
	}
}
