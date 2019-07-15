/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gp.commerce.facades.ticket.impl;

import com.gp.commerce.core.enums.DispenserTypeEnum;
import com.gp.commerce.core.exceptions.GPTicketException;
import com.gp.commerce.core.model.GPDispenserKeyTicketModel;
import com.gp.commerce.data.ticket.GPDispenserTicketData;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.ticket.GPTicketFacade;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GPTicketFacadeImpl implements GPTicketFacade {

    private static final Logger LOG = Logger.getLogger(GPTicketFacadeImpl.class);

    @Resource(name = "enumerationService")
    private EnumerationService enumerationService;

    @Resource
    private ConfigurationService configurationService;

    @Resource
    private Converter<TicketData, GPDispenserKeyTicketModel> gpDispenserKeyTicketConverter;

    @Resource
    private ModelService modelService;

    @Override
    public GPDispenserTicketData getDispenserTicketValues() {

        GPDispenserTicketData gpDispenserTicketData=new GPDispenserTicketData();

        gpDispenserTicketData.setHeaderText(configurationService.getConfiguration().getString(GpcommerceFacadesConstants.DISPENSER_HEADER_TEXT));

        gpDispenserTicketData.setDescription(configurationService.getConfiguration().getString(GpcommerceFacadesConstants.DISPENSER_HEADER_DESCRIPTION));

        gpDispenserTicketData.setDisclaimer(configurationService.getConfiguration().getString(GpcommerceFacadesConstants.DISPENSER_DISCLAIMER_TEXT));

        gpDispenserTicketData.setDispenserTypeValues(enumerationService.getEnumerationValues(DispenserTypeEnum.class).stream().map(dispenserTypeEnum -> dispenserTypeEnum.getCode()).collect(Collectors.toList()));

        gpDispenserTicketData.setDispenserKeyQuantity(new ArrayList<String>(Arrays.asList(configurationService.getConfiguration().getString(GpcommerceFacadesConstants.DISPENSER_KEY_QUANTITY).split(","))));
        
        return gpDispenserTicketData;
    }

    @Override
    public boolean createGPDispenserTicket(TicketData ticketData) {

        try {

            GPDispenserKeyTicketModel gpDispenserKeyTicketModel = gpDispenserKeyTicketConverter.convert(ticketData);

            modelService.save(gpDispenserKeyTicketModel);

        }catch (Exception e){

            LOG.error(" Exception creating  GPDispenserKeyTicket "+e.getMessage(), e);

            return false;
        }

        return true;
    }
}
