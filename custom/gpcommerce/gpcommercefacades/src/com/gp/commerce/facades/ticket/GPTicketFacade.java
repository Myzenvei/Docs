/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gp.commerce.facades.ticket;

import com.gp.commerce.data.ticket.GPDispenserTicketData;
import de.hybris.platform.customerticketingfacades.data.TicketData;

/**
 * The Interface GPTicketFacade.
 */
public interface GPTicketFacade {

    /**
     * Gets the dispenser ticket values.
     *
     * @return the dispenser ticket values
     */
    GPDispenserTicketData getDispenserTicketValues();

    /**
     * Creates the GP dispenser ticket.
     *
     * @param ticketData the ticket data
     * @return true, if successful
     */
    boolean createGPDispenserTicket(TicketData ticketData);
}
