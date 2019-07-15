/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gpintegration.service.impl;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPDispenserKeyTicketModel;
import com.gpintegration.dao.GPTicketDao;
import com.gpintegration.service.GPTicketService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

public class GPDefaultTicketServiceImpl implements GPTicketService {

    private static final Logger LOG = Logger.getLogger(GPDefaultTicketServiceImpl.class);

    @Resource
    private GPTicketDao gpTicketDao;

    @Override
    public List<GPDispenserKeyTicketModel> getDispenserTicketsByCaseReplicationStatus(GPNetsuiteOrderExportStatus status) {

        List<GPDispenserKeyTicketModel> dispenserKeyTicketModels=null;

        try {

             dispenserKeyTicketModels = gpTicketDao.getDispenserTicketsByCaseReplicationStatus(status);

        }catch (Exception e){

            LOG.error("Exception in getDispenserTicketsByCaseReplicationStatus method : "+e.getMessage(), e);

            return null;
        }

        return dispenserKeyTicketModels;
    }


}
