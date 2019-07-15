/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.price.service.impl;

import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 * Class to manage all the filtered prices for a Product.  Currently there can be up to 3 different prices
 * for a product.  List, Sale & MAP price.
 * This class will hold more than 1 per type of price, but current business rules is that there is only 1
 * per type once filtered by UG, StartEnd/Date,Customer, etc....
 */
public class GPFilteredPriceContainerImpl extends GPFilteredPriceContainer {

    private static final Logger LOG = Logger.getLogger(GPFilteredPriceContainerImpl.class);

    /**
     * Will place the priceRow into 1 of the 3 types of Lists depending on the flags salePrice or
     * minAdvertisedPrice == true.  If both are false then added to the List Price List
     *
     * @param priceRow
     */
    public void addPriceRow(PriceRowModel priceRow) {
        if (priceRow != null) {
            if (Boolean.TRUE.equals(priceRow.getSalePrice())) {
                salePrices.add(priceRow);
            } else if (Boolean.TRUE.equals(priceRow.getMinAdvertisedPrice())) {
                mapPrices.add(priceRow);
            } else {
                listPrices.add(priceRow);
            }
        }
    }


    /**
     * will return the difference between List and Sale Price.
     * Assumption for now is that there is only 1 valid List and Sale Price in each List and the 1 entry
     * will be used from each list.
     *
     * @param quantity
     * @return
     */
    public double slashedPriceDiff(final double quantity) {

        double listPrice = getListPrice();
        double salePrice = getSalePrice();
        double retVal = 0d;

        //if there is no list or saleprice then those values will be -1, which is invalid
        //so no need to do the comparison
        if (listPrice < 0d || salePrice < 0d)
            return retVal;

        if (salePrice < listPrice) {
            retVal = (listPrice - salePrice) * quantity;
        }


        return retVal;
    }

    public double slashedPriceDiffForSubscription(final double quantity) {
    	double subscriptionPrice=0.0d ;
        double listPrice = getListPrice();
        double salePrice = getSalePrice();
        PriceRowModel salePriceRow=getSalePricePriceRow() ;
        PriceRowModel listPriceRow=getListPricePriceRow() ;
        if(salePriceRow!=null) {
			if (salePriceRow.getAmountOff() != null && salePriceRow.getPercentageDiscount()==null) {
				subscriptionPrice = getSubscriptionPriceforDollaroff(salePriceRow.getAmountOff(), salePrice * quantity,
						quantity);
			} else {
				subscriptionPrice = getSubscriptionPrice(salePriceRow.getPercentageDiscount(),
						salePriceRow.getAmountOff(), salePrice, quantity);
			} 
		}else {
			if (listPriceRow.getAmountOff() != null &&listPriceRow.getPercentageDiscount()==null) {
				
				subscriptionPrice = getSubscriptionPriceforDollaroff(listPriceRow.getAmountOff(), listPrice * quantity,
						quantity);
			}else {
			subscriptionPrice = getSubscriptionPrice(listPriceRow.getPercentageDiscount(), listPriceRow.getAmountOff(),
					listPrice, quantity);
			}
		}
        double retVal = 0d;

        if(salePrice<0 && listPrice>0 ) {
        	 retVal = (listPrice - subscriptionPrice) * quantity;
        }
        if (salePrice < listPrice) {
            retVal = (listPrice - subscriptionPrice) * quantity;
        }


        return retVal;
    }
    
    private double getSubscriptionPriceforDollaroff(String amountOff, double price, double quantity) {
    	double subscriptionPrice=  amountOff != null ? price - Double.valueOf(amountOff)*quantity : 0.0;
    	if(subscriptionPrice>0.0) {
    		subscriptionPrice=subscriptionPrice/quantity ;
    	}
    	return subscriptionPrice ;
	}
    /**
     * Method will sort any start/end dates with the shortest overlapping span at the top.
     * Assumption is that the prices were sorted before calling this method.
     */
    @Override
    public void filterPrices() {
        LOG.debug("***************before filter***************");
        print();
        listPrices = this.filterPrices(getListPrices());
        salePrices = this.filterPrices(getSalePrices());
        mapPrices = this.filterPrices(getMapPrices());
        LOG.debug("***************after filter***************");
        print();

    }

    public void print() {
        LOG.debug("##########List Prices##########");
        printList(getListPrices());
        LOG.debug("");

        LOG.debug("%%%%%%%%%%Sale Prices:%%%%%%%%%%");
        printList(getSalePrices());
        LOG.debug("");

        LOG.debug("@@@@@@@@@@Map Prices:@@@@@@@@@@");
        printList(getMapPrices());
        LOG.debug("");

    }

    private static void printList(List<PriceRowModel> priceRowModels) {
        for (PriceRowModel priceRow : priceRowModels) {
            LOG.debug("Produce ID: " + priceRow.getProductId() + "->"
                    + priceRow.getPrice()
                    + ": onSale=" + priceRow.getSalePrice()
                    + ", mapPrice=" + priceRow.getMinAdvertisedPrice()
                    + " startDate:" + priceRow.getStartTime() + " endDate:" + priceRow.getEndTime());
        }
    }

    /**
     * Method will sort any start/end dates with the shortest overlapping span at the top.
     * Assumption is that the prices were sorted before calling this method.
     */
    protected List<PriceRowModel> filterPrices(List<PriceRowModel> priceRowModels) {


        if (priceRowModels.size() <= 1) {
            return priceRowModels;
        }

        /**sort by date range so that any priceRows whose start/end date overlap the one with with shortest
         Assumption is that OOTB date range filtering has already been applied**/

        Collections.sort(priceRowModels, new Comparator<PriceRowModel>() {
            public int compare(PriceRowModel priceRow1, PriceRowModel priceRow2) {

                /**set to max value as start/end date could not be set on the priceRow**/
                long diff1 = Long.MAX_VALUE;
                long diff2 = Long.MAX_VALUE;

                Date start1 = priceRow1.getStartTime();
                Date end1 = priceRow1.getEndTime();
                Date start2 = priceRow2.getStartTime();
                Date end2 = priceRow2.getEndTime();
                if (start1 != null && end1 != null) {
                    diff1 = end1.getTime() - start1.getTime();
                }

                if (start2 != null && end2 != null) {
                    diff2 = end2.getTime() - start2.getTime();
                }
                return Long.compare(diff1, diff2);
            }
        });
        return priceRowModels;


    }

    public double getMapPrice() {

        return getSinglePrice(mapPrices);
    }

    public double getListPrice() {
        return getSinglePrice(listPrices);
    }

    public double getSalePrice() {
        return getSinglePrice(salePrices);
    }
    
	public double getSubscriptionPrice(String percentageOff, String amountOff, double price,double qty) {
		return getSingleSubscriptionPrice( percentageOff,  amountOff,  price,qty) ;
	}
	
	public String calculatePercentage(String amountOff,Double price) {
		double dollarAmountOff=Double.valueOf(amountOff) ;
		double percentageOff=(dollarAmountOff/price)*100 ;
		return String.valueOf((int)percentageOff);
	}
	
	public String calculateRoundOffPercentage(String percentage) {
		if(percentage!=null) {
		double percentageOff=Double.valueOf(percentage) ;
		return String.valueOf((int)percentageOff);
		}
		return percentage;
	}
	
	public  PriceRowModel getListPricePriceRow() {
		PriceRowModel row =null ;
		if(CollectionUtils.isNotEmpty(getAllListPrices())) {
		return (getAllListPrices().get(0));
		}
		return row ;
	}
	
	public  PriceRowModel getSalePricePriceRow() {
		PriceRowModel row =null ;
		if(CollectionUtils.isNotEmpty(getAllSalePrices())) {
		return (getAllSalePrices().get(0));
		}
		return row ;
	}

    public List<PriceRowModel> getAllListPrices() {

        return listPrices;
    }

    public List<PriceRowModel> getAllSalePrices() {

        return salePrices;
    }

    /**
     * will return getPrice() from the PriceRowModel that is at index 0 for the passed in List
     *
     * @param priceRowModels
     * @return
     */
    protected double getSinglePrice(List<PriceRowModel> priceRowModels) {
        double retVal = -1.0;
        if (!priceRowModels.isEmpty()) {
            PriceRowModel priceRowModel = priceRowModels.get(0);
            Double priceDouble = priceRowModel.getPrice();
            if (priceDouble != null) {
                retVal = priceDouble;
            }
        }

        return retVal;
    }
    
    public double getSingleSubscriptionPrice(String percentageOff, String amountOff, double price, double qty) {
    	double subscriptionPrice = 0.0;
    	ServicesUtil.validateParameterNotNull(price, "Parameter price must not be null");
		if (percentageOff != null) {
			subscriptionPrice = price - price * Double.valueOf(percentageOff) / 100;
		} else {
			subscriptionPrice = amountOff != null ? price - Double.valueOf(amountOff)*qty : 0.0;
		}
		return subscriptionPrice;
    }
    
    

    /**
     * Tries to get the Currency from the List price and if that is null, the Sale price
     *
     * @return
     */
    public CurrencyModel getCurrency() {

        CurrencyModel currency = null;

        if (!getListPrices().isEmpty()) {
            currency = getListPrices().get(0).getCurrency();
        } else if (!getSalePrices().isEmpty()) {
            currency = getSalePrices().get(0).getCurrency();
        } else {
            //TODO add in log error as this should never happen
            LOG.error("no currency was set for List or Sale prices.");
        }

        return currency;

    }

    /**
     * Look for the SalesPrice if exists, then return it, else return the ListPrice.  This assumes
     * the prices have been filtered and are in proper sort order
     *
     * @return
     */
    @Override
    public PriceRowModel findBasePrice() {

        PriceRowModel priceRowModel = getListPrices().get(0);
        if (!getSalePrices().isEmpty()) {
            priceRowModel = getSalePrices().get(0);
        }
        return priceRowModel;
    }

    public List<PriceRowModel> getListPrices() {
        return listPrices;
    }

    public void setListPrices(List<PriceRowModel> listPrices) {
        this.listPrices = listPrices;
    }

    public List<PriceRowModel> getSalePrices() {
        return salePrices;
    }

    public void setSalePrices(List<PriceRowModel> salePrices) {
        this.salePrices = salePrices;
    }

    public List<PriceRowModel> getMapPrices() {
        return mapPrices;
    }

    public void setMapPrices(List<PriceRowModel> mapPrices) {
        this.mapPrices = mapPrices;
    }
}
