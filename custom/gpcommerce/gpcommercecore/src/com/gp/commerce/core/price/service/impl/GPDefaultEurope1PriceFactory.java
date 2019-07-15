/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.price.service.impl;

import com.gp.commerce.core.price.service.GPEurope1PriceFactory;
import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import org.apache.log4j.Logger;


import java.util.*;


public class GPDefaultEurope1PriceFactory extends Europe1PriceFactory implements GPEurope1PriceFactory
{
	final static public String GP_USER_PRICE_GROUP="gpUserPriceGroup";
	final static public String USER_PRICE_GROUP="userPriceGroup";

	private transient NetGrossStrategy netGrossStrategy;
	private transient TimeService timeService;
	private transient ModelService modelService;

	private static final Logger LOG = Logger.getLogger(GPDefaultEurope1PriceFactory.class);


    @Override
    public PriceValue getBasePrice(AbstractOrderEntry entry) throws JaloPriceFactoryException {
			SessionContext ctx = super.getSession().getSessionContext();
			AbstractOrder order = entry.getOrder(ctx);
			Currency currency = order.getCurrency(ctx);
			EnumerationValue productGroup = null;
			User user = null;
			EnumerationValue userGroup = null;
			Unit unit = null;
			long quantity = 0L;
			boolean net = false;
			Date date = null;
			if(entry != null && entry.getProduct() != null){ ; }
			ProductModel product = getModelService().get(entry.getProduct().getPK());

			//TODO put in filter for qty vs minQTY
			GPFilteredPriceContainer gpFilteredPriceContainer = this.getFilteredPriceContainerForProducts(product);
			boolean giveAwayMode = entry.isGiveAway(ctx);
			boolean entryIsRejected = entry.isRejected(ctx);
			PriceRowModel row;
			if (giveAwayMode && entryIsRejected) {
				row = null;
			} else {
				row = gpFilteredPriceContainer.findBasePrice();
			}

			if (row != null) {
				CurrencyModel rowCurr = row.getCurrency();
				double price;
					price = row.getPrice().doubleValue() / row.getUnitFactor().doubleValue();

				UnitModel priceUnit = row.getUnit();
				Unit entryUnit = entry.getUnit();
				return new PriceValue(rowCurr.getIsocode(), price, row.getNet().booleanValue());
			} else if (giveAwayMode) {
				return new PriceValue(order.getCurrency(ctx).getIsocode(ctx), 0.0D,order.isNet().booleanValue());
			} else {
				String msg = Localization.getLocalizedString("exception.europe1pricefactory.getbaseprice.jalopricefactoryexception1", new Object[]{product, productGroup, user, userGroup, Long.toString(quantity), unit, currency, date, Boolean.toString(net)});
				throw new JaloPriceFactoryException(msg, 0);
			}
		}

	protected List<PriceRow> filterPriceRows(List<PriceRow> priceRows) {
		if (priceRows.isEmpty()) {
			return Collections.emptyList();
		}

		return priceRows;
	}

	/**
	 * Returns a GPPriceContainer with the up to 3 types of prices, List, Sale & Map
	 * @param product
	 * @return
	 */
	public GPFilteredPriceContainer getFilteredPriceContainerForProducts(final ProductModel product){
		GPFilteredPriceContainer gpPriceContainer = new GPFilteredPriceContainerImpl();

		List<PriceRow> priceRows = getPriceInformationsForProduct(product);
		for(PriceRow row: priceRows){
			if (row != null) {
				PriceRowModel rowModel =  getModelService().get(row.getPK());
				gpPriceContainer.addPriceRow(rowModel);
			}
		}
		//need to due further sorting on the start/end dates
		gpPriceContainer.filterPrices();

		return gpPriceContainer;
	}



	@Override
	public List<PriceRow> getPriceInformationsForProduct(final ProductModel model)
	{

		final boolean net = getNetGrossStrategy().isNet();
		final Product product = (Product) getModelService().getSource(model);
		try
		{
			final SessionContext ctx = product.getSession().getSessionContext();
			final List<PriceRow> priceRows = this.filterPriceRows(
					super.matchPriceRowsForInfo(ctx, product, super.getPPG(ctx, product), ctx.getUser(),
							super.getUPG(ctx, ctx.getUser()), ctx.getCurrency(), timeService.getCurrentTime(), net));

			LOG.debug("priceRows b4 sort " + priceRows);
			priceRows.forEach(
					(price)->LOG.debug(price.getPrice())
			);

			List<PriceRow> gpPriceRows = filterGPUserPriceGroup(ctx,priceRows,net);
			gpPriceRows.sort(Comparator.comparing(PriceRow::getPrice));
			LOG.debug("sorted priceRows " + gpPriceRows);
			gpPriceRows.forEach(
					(price)->LOG.debug(price.getPrice())
			);

			return gpPriceRows;
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new SystemException(e.getMessage(), e);
		}

	}


	public EnumerationValue getGPUserPriceGroup(User user) throws JaloPriceFactoryException {
		return getEnumFromGroups(user,USER_PRICE_GROUP);
	}


	public List<PriceRow> filterGPUserPriceGroup(SessionContext ctx,List<PriceRow> priceRows, boolean net){

		List<PriceRow> adjustedPriceRows = new ArrayList<>();
		PriceRow lowestUGPrice = null;
		LOG.debug("filterGPUserPriceGroup prices " + priceRows);
		try {
			EnumerationValue enumerationValue = getEnumFromGroups(ctx.getUser(), USER_PRICE_GROUP);
			LOG.debug("enumVal for " + ctx.getUser()  +"=" + enumerationValue);

			for(PriceRow priceRow : priceRows) {
				LOG.debug("filtering price " + priceRow.getPrice());
				PriceRowModel prmModel = getModelService().get(priceRow.getPK());

				UserPriceGroup userPriceGroupEnum = prmModel.getGpUserPriceGroup();
				LOG.debug("priceRow gpUPG " + userPriceGroupEnum);

				if(userPriceGroupEnum != null) {

					LOG.debug("priceRow userGroup=gpUPG " + enumerationValue+"="+userPriceGroupEnum);
					if(enumerationValue != null && enumerationValue.getCode().equals(userPriceGroupEnum.getCode())){

						LOG.debug("upg matched for  " + priceRow.getPrice() + " lowestUGP="+lowestUGPrice);

						if(lowestUGPrice == null && !prmModel.getMinAdvertisedPrice()){
							lowestUGPrice = priceRow;
						}
						//compare and return the lowest price
						if(null !=lowestUGPrice && lowestUGPrice.getPrice().doubleValue() > priceRow.getPrice().doubleValue()){
							LOG.debug("setting lowest ug price " + priceRow.getPrice());
							lowestUGPrice = priceRow;
						}
					}
				}else{
					LOG.debug("adding priceRow with no UG price " + priceRow.getPrice());
					adjustedPriceRows.add(priceRow);
				}
			}

			if(lowestUGPrice != null){
				LOG.debug("added lowest ug price " + lowestUGPrice.getPrice());
				adjustedPriceRows.add(lowestUGPrice);
			}

		} catch (JaloPriceFactoryException e) {
			throw new SystemException(e.getMessage(), e);
		}

		LOG.debug("returning prices " + adjustedPriceRows);
		return adjustedPriceRows;
	}



	public static double convertExact(UnitModel sourceUnit, Unit targetUnit, double value) throws JaloInvalidParameterException {
		ServicesUtil.validateParameterNotNullStandardMessage("sourceUnit", sourceUnit);
		ServicesUtil.validateParameterNotNullStandardMessage("targetUnit", targetUnit);
		LOG.debug("comparing "+ sourceUnit.getUnitType()+"="+targetUnit.getUnitType());
		if (!sourceUnit.getUnitType().equals(targetUnit.getUnitType())) {

			throw new JaloInvalidParameterException("cant convert: unit types dont match", 290774);
		} else {
			return CoreAlgorithms.convert(sourceUnit.getConversion(), targetUnit.getConversionFactor(), value);
		}
	}

	public NetGrossStrategy getNetGrossStrategy()
	{
		return netGrossStrategy;
	}

	public void setNetGrossStrategy(final NetGrossStrategy netGrossStrategy)
	{
		this.netGrossStrategy = netGrossStrategy;
	}

	public TimeService getTimeService()
	{
		return timeService;
	}

	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
