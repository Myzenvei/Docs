/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.promotion.action.impl;

import java.math.BigDecimal;
import java.util.Collections;

import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.promotionengineservices.action.impl.AbstractRuleActionStrategy;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderChangeDeliveryModeActionModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.PriceValue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import de.hybris.platform.promotions.model.PromotionResultModel;

public class GPDefaultShippingActionStrategy extends
AbstractRuleActionStrategy<RuleBasedOrderChangeDeliveryModeActionModel> {


	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultShippingActionStrategy.class);
	private DeliveryModeDao deliveryModeDao;
	@Resource(name="deliveryService")
	private DeliveryService deliveryService;

	public List<PromotionResultModel> apply(AbstractRuleActionRAO action) {
		LOG.info("Inside GP Shipping Action Strategy");
		if (!(action instanceof ShipmentRAO)) {
			LOG.error("cannot apply {}, action is not of type ShipmentRAO, but {}", this.getClass().getSimpleName(),
					action);
			return Collections.emptyList();
		} else {
			ShipmentRAO changeDeliveryMethodAction = (ShipmentRAO) action;
			if (!(changeDeliveryMethodAction.getAppliedToObject() instanceof CartRAO)) {
				LOG.error("cannot apply {}, appliedToObject is not of type CartRAO, but {}",
						this.getClass().getSimpleName(), action.getAppliedToObject());
				return Collections.emptyList();
			} else {
				PromotionResultModel promoResult = this.getPromotionActionService().createPromotionResult(action);
				if (promoResult == null) {
					LOG.error("cannot apply {}, promotionResult could not be created.",
							this.getClass().getSimpleName());
					return Collections.emptyList();
				} else {
					AbstractOrderModel order = this.getPromotionResultUtils().getOrder(promoResult);
					if (Objects.isNull(order)) {
						LOG.error("cannot apply {}, order or cart not found: {}", this.getClass().getSimpleName(),
								order);
						if (this.getModelService().isNew(promoResult)) {
							this.getModelService().detach(promoResult);
						}

						return Collections.emptyList();
					} else {
						ShipmentRAO shipmentRAO = (ShipmentRAO) action;
						DeliveryModeModel shipmentModel = this.getDeliveryModeForCode(shipmentRAO.getMode().getCode());
						if (shipmentModel == null) {
							LOG.error("Delivery Mode for code {} not found!", shipmentRAO.getMode());
							return Collections.emptyList();
						} else {
							DeliveryModeModel shipmentModelToReplace = order.getDeliveryMode();
							Double deliveryCostToReplace = 0.0;
							if(null == shipmentModelToReplace && null != order.getPreviousDeliveryMode()){
								if(order.getPreviousDeliveryMode() instanceof ZoneDeliveryModeModel){
									ZoneDeliveryModeModel zoneDeliveryMode = (ZoneDeliveryModeModel) order.getPreviousDeliveryMode();
									
									final PriceValue deliveryCost = deliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(zoneDeliveryMode, order);
									deliveryCostToReplace = deliveryCost.getValue();
								}
								shipmentModelToReplace = order.getPreviousDeliveryMode();
							}
							else{
								deliveryCostToReplace = order.getDeliveryCost();
							}
							order.setDeliveryMode(shipmentModel);
							order.setDeliveryCost(Double.valueOf(shipmentRAO.getMode().getCost().doubleValue()));
							
							RuleBasedOrderChangeDeliveryModeActionModel actionModel = (RuleBasedOrderChangeDeliveryModeActionModel) this
									.createPromotionAction(promoResult, action);
							this.handleActionMetadata(action, actionModel);
							actionModel.setDeliveryMode(shipmentModel);
							actionModel.setDeliveryCost(shipmentRAO.getMode().getCost());
							actionModel.setReplacedDeliveryMode(shipmentModelToReplace);
							actionModel
									.setReplacedDeliveryCost(BigDecimal.valueOf(deliveryCostToReplace.doubleValue()));
							this.getModelService().saveAll(new Object[]{promoResult, actionModel, order});
							return Collections.singletonList(promoResult);
						}
					}
				}
			}
		}
	}

	

	protected DeliveryModeModel getDeliveryModeForCode(String code) {
		ServicesUtil.validateParameterNotNull(code, "Parameter code cannot be null");
		List deliveryModes = this.getDeliveryModeDao().findDeliveryModesByCode(code);
		return CollectionUtils.isNotEmpty(deliveryModes) ? (DeliveryModeModel) deliveryModes.get(0) : null;
	}

	protected DeliveryModeDao getDeliveryModeDao() {
		return this.deliveryModeDao;
	}

	@Required
	public void setDeliveryModeDao(DeliveryModeDao deliveryModeDao) {
		this.deliveryModeDao = deliveryModeDao;
	}

	@Override
	public void undo(ItemModel item) {
		if (item instanceof RuleBasedOrderChangeDeliveryModeActionModel) {
			this.handleUndoActionMetadata((RuleBasedOrderChangeDeliveryModeActionModel) item);
			RuleBasedOrderChangeDeliveryModeActionModel action = (RuleBasedOrderChangeDeliveryModeActionModel) item;
			AbstractOrderModel order = this.getPromotionResultUtils().getOrder(action.getPromotionResult());
			order.setDeliveryMode(action.getReplacedDeliveryMode());
			order.setDeliveryCost(Double.valueOf(action.getReplacedDeliveryCost().doubleValue()));
			this.undoInternal(action);
			this.getModelService().save(order);
		}
	}

}
