/**
 *
 */
package com.deloitte.facade;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public interface SubscriptionVoucherFacade
{
	void applySubscriptionVoucher(final String voucherCode, final SubscriptionCartModel subscriptionCartModel)
			throws VoucherOperationException;

	void subScriptionReleaseVoucher(final String voucherCode, final SubscriptionCartModel subscriptionCartModel, String baseSite)
			throws VoucherOperationException;

	void recalculateOrderWithPromotion(final AbstractOrderModel order, String baseSite);
}
