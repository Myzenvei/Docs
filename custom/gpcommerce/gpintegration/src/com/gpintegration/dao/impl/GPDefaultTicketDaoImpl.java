/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gpintegration.dao.impl;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPDispenserKeyTicketModel;
import com.gpintegration.dao.GPTicketDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;

import java.util.List;

public class GPDefaultTicketDaoImpl extends AbstractItemDao implements GPTicketDao {

    @Override
    public List<GPDispenserKeyTicketModel> getDispenserTicketsByCaseReplicationStatus(GPNetsuiteOrderExportStatus status) {

        GPDispenserKeyTicketModel gpDispenserKeyTicketModel=new GPDispenserKeyTicketModel();

        gpDispenserKeyTicketModel.setCaseReplicationStatus(status);

        List<GPDispenserKeyTicketModel> dispenserKeyTicketModels=getFlexibleSearchService().getModelsByExample(gpDispenserKeyTicketModel);

        return dispenserKeyTicketModels;
    }
}
