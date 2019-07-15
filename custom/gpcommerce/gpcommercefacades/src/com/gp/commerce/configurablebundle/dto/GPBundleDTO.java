/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.configurablebundle.dto;

import java.util.List;

public class GPBundleDTO {

    private String id;

    private String name;

    private String description;

    private String bundleImage;

    private List<GPBundleTemplateDTO> bundleTemplateList;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBundleImage() {
        return bundleImage;
    }

    public void setBundleImage(String bundleImage) {
        this.bundleImage = bundleImage;
    }

    public List<GPBundleTemplateDTO> getBundleTemplateList() {
        return bundleTemplateList;
    }

    public void setBundleTemplateList(List<GPBundleTemplateDTO> bundleTemplateList) {
        this.bundleTemplateList = bundleTemplateList;
    }
}
