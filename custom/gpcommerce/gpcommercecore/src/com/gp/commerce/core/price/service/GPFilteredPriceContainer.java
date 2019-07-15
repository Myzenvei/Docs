/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.price.service;

import com.gp.commerce.core.price.strategies.impl.GPDefaultRetrieveChannelStrategy;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class GPFilteredPriceContainer {

    protected List<PriceRowModel> listPrices;
    protected List<PriceRowModel> salePrices;
    protected List<PriceRowModel> mapPrices;

    public GPFilteredPriceContainer() {
        this.listPrices = new ArrayList<PriceRowModel>();
        this.salePrices = new ArrayList<PriceRowModel>();
        this.mapPrices = new ArrayList<PriceRowModel>();
    }

    private static final Logger LOG = Logger.getLogger(GPFilteredPriceContainer.class);

    public abstract void addPriceRow(PriceRowModel priceRow);

    public abstract double slashedPriceDiff(final double quantity);
    
    public abstract double slashedPriceDiffForSubscription(final double quantity) ;

    public abstract void filterPrices();

    public abstract double getMapPrice();

    public abstract double getSalePrice();

    public abstract double getListPrice();
    
    public abstract List<PriceRowModel> getAllSalePrices();
    
    public abstract List<PriceRowModel> getAllListPrices();    

    public abstract CurrencyModel getCurrency();

    public abstract PriceRowModel findBasePrice();

    public static double convertExact(UnitModel sourceUnit, Unit targetUnit, double value) throws JaloInvalidParameterException {
        ServicesUtil.validateParameterNotNullStandardMessage("sourceUnit", sourceUnit);
        ServicesUtil.validateParameterNotNullStandardMessage("targetUnit", targetUnit);
        LOG.info("comparing "+ sourceUnit.getUnitType()+"="+targetUnit.getUnitType());
        if (!sourceUnit.getUnitType().equals(targetUnit.getUnitType())) {

            throw new JaloInvalidParameterException("cant convert: unit types dont match", 290774);
        } else {
            return CoreAlgorithms.convert(sourceUnit.getConversion(), targetUnit.getConversionFactor(), value);
        }
    }

    public abstract double getSubscriptionPrice(String percentageOff, String amountOff, double price, double qty) ;

    public abstract String calculatePercentage(String amountOff,Double price) ;

	public abstract PriceRowModel getListPricePriceRow() ;

	public abstract PriceRowModel getSalePricePriceRow() ;
	
	public abstract String calculateRoundOffPercentage(String percentage) ;


 


}


