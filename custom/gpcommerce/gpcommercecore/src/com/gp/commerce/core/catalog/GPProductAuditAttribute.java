/*
 * Copyright (c) 2019. Georgia-Pacific.  All rights reserved.
 * This software is the confidential and proprietary information of Georgia-Pacific.
 *
 *
 */

package com.gp.commerce.core.catalog;

import java.util.Date;

public class GPProductAuditAttribute {

    String attributeName;
    Object oldAttributeValue;
    Object newAttributeValue;

    Date dateOfChange;

    public void setAttributes(Object oldAttributeValue, Object newAttributeValue) {


        boolean changed = false;

        if (oldAttributeValue != null)
            changed = !oldAttributeValue.equals(newAttributeValue);
        else if (newAttributeValue != null)
            changed = !newAttributeValue.equals(oldAttributeValue);

        if(changed && (dateOfChange == null)){
            dateOfChange = new Date();
        }
        setOldAttributeValue(oldAttributeValue);
        setNewAttributeValue(newAttributeValue);
    }

    public boolean isChanged() {
        return dateOfChange != null;
    }


    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Object getOldAttributeValue() {
        return oldAttributeValue;
    }

    public void setOldAttributeValue(Object oldAttributeValue) {
        this.oldAttributeValue = oldAttributeValue;
    }

    public Object getNewAttributeValue() {
        return newAttributeValue;
    }

    public void setNewAttributeValue(Object newAttributeValue) {
        this.newAttributeValue = newAttributeValue;
    }

	public Date getDateOfChange() {
		return dateOfChange;
	}

	public void setDateOfChange(Date dateOfChange) {
		this.dateOfChange = dateOfChange;
	}
}
