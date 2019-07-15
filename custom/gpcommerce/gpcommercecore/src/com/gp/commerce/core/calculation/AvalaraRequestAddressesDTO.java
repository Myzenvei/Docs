/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.calculation;

import org.codehaus.jackson.annotate.JsonProperty;

public class AvalaraRequestAddressesDTO {

    @JsonProperty("shipFrom")
    private AddressStructureDTO shipFrom;
    @JsonProperty("shipTo")
    private AddressStructureDTO shipTo;
    @JsonProperty("pointOfOrderOrigin")
    private AddressStructureDTO pointOfOrderOrigin;
    @JsonProperty("pointOfOrderAcceptance")
    private AddressStructureDTO pointOfOrderAcceptance;


    public AddressStructureDTO getShipFrom() {
        return shipFrom;
    }

    public void setShipFrom(AddressStructureDTO shipFrom) {
        this.shipFrom = shipFrom;
    }

    public AddressStructureDTO getShipTo() {
        return shipTo;
    }

    public void setShipTo(AddressStructureDTO shipTo) {
        this.shipTo = shipTo;
    }

    public AddressStructureDTO getPointOfOrderOrigin() {
        return pointOfOrderOrigin;
    }

    public void setPointOfOrderOrigin(AddressStructureDTO pointOfOrderOrigin) {
        this.pointOfOrderOrigin = pointOfOrderOrigin;
    }

    public AddressStructureDTO getPointOfOrderAcceptance() {
        return pointOfOrderAcceptance;
    }

    public void setPointOfOrderAcceptance(AddressStructureDTO pointOfOrderAcceptance) {
        this.pointOfOrderAcceptance = pointOfOrderAcceptance;
    }
}
