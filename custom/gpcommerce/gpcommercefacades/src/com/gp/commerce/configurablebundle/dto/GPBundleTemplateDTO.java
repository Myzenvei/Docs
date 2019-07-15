/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.configurablebundle.dto;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;

public class GPBundleTemplateDTO {

    private String id;

    private String name;

    private List<ProductData> products;

    private int maxItemsAllowed;

    private int minItemsAllowed;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductData> products) {
        this.products = products;
    }

    public int getMaxItemsAllowed() {
        return maxItemsAllowed;
    }

    public void setMaxItemsAllowed(int maxItemsAllowed) {
        this.maxItemsAllowed = maxItemsAllowed;
    }

    public int getMinItemsAllowed() {
        return minItemsAllowed;
    }

    public void setMinItemsAllowed(int minItemsAllowed) {
        this.minItemsAllowed = minItemsAllowed;
    }

}
