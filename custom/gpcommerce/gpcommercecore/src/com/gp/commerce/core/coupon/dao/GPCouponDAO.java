/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.coupon.dao;

import de.hybris.platform.couponservices.model.AbstractCouponModel;

public interface GPCouponDAO {

	/**
	 * getCouponForCode
	 *
	 * @param code
	 */
	AbstractCouponModel getCouponForCode(String code);
}
