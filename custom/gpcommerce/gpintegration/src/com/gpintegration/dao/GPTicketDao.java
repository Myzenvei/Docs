/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gpintegration.dao;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPDispenserKeyTicketModel;

import java.util.List;

public interface GPTicketDao {

    List<GPDispenserKeyTicketModel> getDispenserTicketsByCaseReplicationStatus(GPNetsuiteOrderExportStatus status);
}
