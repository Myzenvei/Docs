/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gpintegration.service;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPDispenserKeyTicketModel;

import java.util.List;

/**
 * The Interface GPTicketService.
 */
public interface GPTicketService {

    /**
     * Gets the dispenser tickets by case replication status.
     *
     * @param status the status
     * @return the dispenser tickets by case replication status
     */
    List<GPDispenserKeyTicketModel> getDispenserTicketsByCaseReplicationStatus(GPNetsuiteOrderExportStatus status);

}
