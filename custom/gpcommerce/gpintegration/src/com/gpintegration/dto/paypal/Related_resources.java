/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;

public class Related_resources
{
    private Authorization authorization;

    public Authorization getAuthorization ()
    {
        return authorization;
    }

    public void setAuthorization (Authorization authorization)
    {
        this.authorization = authorization;
    }
}