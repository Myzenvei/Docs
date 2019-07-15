/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;
import com.gpintegration.dto.out.paypal.Amount;

public class PayPalRefundResponse {
	 private String parent_payment;

	    private String update_time;

	    private Amount amount;

	    private String create_time;

	    private Links[] links;

	    private String id;

	    private String state;

	    private String capture_id;

	    private String invoice_number;

	    public String getParent_payment ()
	    {
	        return parent_payment;
	    }

	    public void setParent_payment (String parent_payment)
	    {
	        this.parent_payment = parent_payment;
	    }

	    public String getUpdate_time ()
	    {
	        return update_time;
	    }

	    public void setUpdate_time (String update_time)
	    {
	        this.update_time = update_time;
	    }

	    public Amount getAmount ()
	    {
	        return amount;
	    }

	    public void setAmount (Amount amount)
	    {
	        this.amount = amount;
	    }

	    public String getCreate_time ()
	    {
	        return create_time;
	    }

	    public void setCreate_time (String create_time)
	    {
	        this.create_time = create_time;
	    }

	    public Links[] getLinks ()
	    {
	        return links;
	    }

	    public void setLinks (Links[] links)
	    {
	        this.links = links;
	    }

	    public String getId ()
	    {
	        return id;
	    }

	    public void setId (String id)
	    {
	        this.id = id;
	    }

	    public String getState ()
	    {
	        return state;
	    }

	    public void setState (String state)
	    {
	        this.state = state;
	    }

	    public String getCapture_id ()
	    {
	        return capture_id;
	    }

	    public void setCapture_id (String capture_id)
	    {
	        this.capture_id = capture_id;
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
