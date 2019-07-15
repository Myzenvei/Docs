/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.impl;

import com.gp.commerce.core.calculation.service.impl.GPCalculationService;
import com.gp.commerce.core.job.ProductDataExportJob;

import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.PromotionGroupModel;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public class GPDefaultCommerceCartCalculationStrategy extends DefaultCommerceCartCalculationStrategy {

	private boolean calculateExternalTaxes = false;
	private static final Logger LOG = Logger.getLogger(GPDefaultCommerceCartCalculationStrategy.class);

	@Override
    public boolean calculateCart(final CommerceCartParameter parameter)
    {
        final CartModel cartModel = parameter.getCart();

        validateParameterNotNull(cartModel, "Cart model cannot be null");

        final CalculationService calcService = getCalculationService();
        boolean recalculated = false;
        if (calcService.requiresCalculation(cartModel))
        {
            try
            {
                parameter.setRecalculate(false);
                beforeCalculate(parameter);
                ((GPCalculationService)calcService).setRequiresTaxCalculations(false);
                calcService.calculate(cartModel);
                getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, PromotionsManager.AutoApplyMode.APPLY_ALL,
                        PromotionsManager.AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());

            }
            catch (final CalculationException calculationException)
            {
                throw new IllegalStateException("Cart model " + cartModel.getCode() + " was not calculated due to: "
                        + calculationException.getMessage(), calculationException);
            }
            finally
            {
                afterCalculate(parameter);
                try {
                    ((GPCalculationService)calcService).setRequiresTaxCalculations(true);
                    calcService.calculateTotals(cartModel,true);

                } catch (CalculationException e) {
                	LOG.error("Error while cart calculation"+e.getMessage(),e);
                }
            }
            recalculated = true;
        }
        if (calculateExternalTaxes)
        {
            getExternalTaxesService().calculateExternalTaxes(cartModel);
        }
        return recalculated;
    }

	@Override
	public Collection<PromotionGroupModel> getPromotionGroups()
	{
		final Collection<PromotionGroupModel> promotionGroupModels = new ArrayList<PromotionGroupModel>();
		if (getBaseSiteService().getCurrentBaseSite() != null
				&& getBaseSiteService().getCurrentBaseSite().getDefaultPromotionGroup() != null)
		{
			promotionGroupModels.add(getBaseSiteService().getCurrentBaseSite().getDefaultPromotionGroup());
		}
		return promotionGroupModels;
	}

	@Override
	public boolean recalculateCart(final CommerceCartParameter parameter) {
		final CartModel cartModel = parameter.getCart();
		final CalculationService calcService = getCalculationService();
		try {
			parameter.setRecalculate(true);
			beforeCalculate(parameter);
			((GPCalculationService) calcService).setRequiresTaxCalculations(false);
			getCalculationService().recalculate(cartModel);
			getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true,
					PromotionsManager.AutoApplyMode.APPLY_ALL, PromotionsManager.AutoApplyMode.APPLY_ALL,
					getTimeService().getCurrentTime());

		} catch (final CalculationException calculationException) {
			throw new IllegalStateException(String.format("Cart model %s was not calculated due to: %s ",
					cartModel.getCode(), calculationException.getMessage()), calculationException);
		} finally {
			afterCalculate(parameter);
			try {
				((GPCalculationService) calcService).setRequiresTaxCalculations(true);
				calcService.calculateTotals(cartModel, true);
			} catch (CalculationException e) {
				LOG.error("Error while recalculatingCart"+e.getMessage(),e);
			}

		}
		return true;
	}

}
