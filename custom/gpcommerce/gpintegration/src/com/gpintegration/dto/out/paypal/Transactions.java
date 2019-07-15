/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.out.paypal;

import com.gpintegration.dto.paypal.Amount;
import com.gpintegration.dto.paypal.Item_list;
import com.gpintegration.dto.paypal.Payee;
import com.gpintegration.dto.paypal.Related_resources;

public class Transactions
{
    private Payee payee;

    private Amount amount;

    private Related_resources[] related_resources;

    private Item_list item_list;

    private String custom;

    private String description;

    private String invoice_number;

    public Payee getPayee ()
    {
        return payee;
    }

    public void setPayee (Payee payee)
    {
        this.payee = payee;
    }

    public Amount getAmount ()
    {
        return amount;
    }

    public void setAmount (Amount amount)
    {
        this.amount = amount;
    }

    public Related_resources[] getRelated_resources ()
    {
        return related_resources;
    }

    public void setRelated_resources (Related_resources[] related_resources)
    {
        this.related_resources = related_resources;
    }

    public Item_list getItem_list ()
    {
        return item_list;
    }

    public void setItem_list (Item_list item_list)
    {
        this.item_list = item_list;
    }

    public String getCustom ()
    {
        return custom;
    }

    public void setCustom (String custom)
    {
        this.custom = custom;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getInvoice_number ()
    {
        return invoice_number;
    }

    public void setInvoice_number (String invoice_number)
    {
        this.invoice_number = invoice_number;
    }
}