/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.price.strategies.impl;

import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.channel.strategies.impl.DefaultRetrieveChannelStrategy;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;



public class GPDefaultRetrieveChannelStrategy extends DefaultRetrieveChannelStrategy {

    private static final String PRICE_ROW_CHANNEL = "PriceRowChannel";
	private static final String CHANNEL = "channel";
	private static final String CURRENT_SITE = "currentSite";
	private static final Logger LOG = Logger.getLogger(GPDefaultRetrieveChannelStrategy.class);
    private ModelService modelService;
    private EnumerationService enumerationService;


    @Override
    public PriceRowChannel getChannel(SessionContext ctx) {
        LOG.debug("Inside ChannelRetrievalStrategy.");
        PriceRowChannel priceRowChannel = null;
        if (ctx != null && ctx.getAttribute(CURRENT_SITE) != null) {
            priceRowChannel = (PriceRowChannel) ctx.getAttribute(CHANNEL);
            if (priceRowChannel == null) {
                CMSSite cmsSite = (CMSSite)ctx.getAttribute(CURRENT_SITE);

                if (cmsSite != null) {
                    LOG.debug("getChannel is using site " +cmsSite.getUid());
                    priceRowChannel = getEnumValueForCode(cmsSite.getUid());
                }

                if (priceRowChannel != null) {
                    ctx.setAttribute(CHANNEL, priceRowChannel);// 56
                }
            }

        }
        if (priceRowChannel == null) {
            //try the super class which looks at the UI
            priceRowChannel = super.getChannel(ctx);

        }

        return priceRowChannel;
    }

    protected PriceRowChannel getEnumValueForCode(String channel) {
        PriceRowChannel channelFromDb = null;

        try {
            channelFromDb = (PriceRowChannel)this.enumerationService.getEnumerationValue(PRICE_ROW_CHANNEL, channel);
        } catch (UnknownIdentifierException var3) {
            LOG.debug("This Enum is not setup in PriceRowChannel dynamic enum");
        }

        return channelFromDb;
    }


    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public EnumerationService getEnumerationService() {
        return enumerationService;
    }

    @Override
    public void setEnumerationService(EnumerationService enumerationService) {
        this.enumerationService = enumerationService;
    }
}
