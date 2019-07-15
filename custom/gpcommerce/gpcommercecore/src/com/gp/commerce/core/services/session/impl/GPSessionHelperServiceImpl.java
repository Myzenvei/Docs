/*
 * Copyright (c) 2019. Georgia-Pacific.  All rights reserved.
 * This software is the confidential and proprietary information of Georgia-Pacific.
 *
 *
 */

package com.gp.commerce.core.services.session.impl;

import com.gp.commerce.core.services.session.GPSessionHelperService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.log4j.Logger;

public class GPSessionHelperServiceImpl implements GPSessionHelperService {

    private static final Logger LOG = Logger.getLogger(GPSessionHelperServiceImpl.class);

    private SessionService sessionService;

    @Override
    public void updateASMSession() {

        Object actingASMUserUID = getSessionService().getAttribute("ACTING_USER_UID");
        if (actingASMUserUID == null) {
            actingASMUserUID = "none";
        }
        getSessionService().setAttribute("ACTING_USER_UID", actingASMUserUID);
        LOG.debug(getClass() + " ACTING_USER_UID=" + getSessionService().getAttribute("ACTING_USER_UID"));

    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
}
