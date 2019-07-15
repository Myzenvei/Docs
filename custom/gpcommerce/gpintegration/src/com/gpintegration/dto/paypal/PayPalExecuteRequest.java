/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;

public class PayPalExecuteRequest
{
    private String payer_id;

    public String getPayer_id ()
    {
        return payer_id;
    }

    public void setPayer_id (String payer_id)
    {
        this.payer_id = payer_id;
    }

}
