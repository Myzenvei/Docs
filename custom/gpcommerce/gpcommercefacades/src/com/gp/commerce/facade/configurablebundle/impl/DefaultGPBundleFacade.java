/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.configurablebundle.impl;

import com.gp.commerce.core.services.configurablebundle.GPBundleService;
import com.gp.commerce.facade.configurablebundle.GPBundleFacade;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

/**
 * The Class DefaultGPBundleFacade.
 */
public class DefaultGPBundleFacade implements GPBundleFacade {
    @Resource(name = "bundleTemplateService")
    BundleTemplateService bundleTemplateService;

    @Resource(name = "gpBundleService")
    GPBundleService gpBundleService;

    @Resource(name= "gpBundleConverter")
    private Converter<BundleTemplateModel, BundleTemplateData> gpBundleConverter;



    /**
     * Builds the bundle tree.
     *
     * @param bundleId the bundle id
     * @return the bundle template data
     */
    @Override
    public BundleTemplateData buildBundleTree(@Nonnull final String bundleId) {
        Boolean bundleValidity = gpBundleService.validateBundleSetup(bundleId);
        if (bundleValidity == Boolean.TRUE) {
        	BundleTemplateModel rootBundleTemplate = gpBundleService.getRootBundleTemplate(bundleId);
            return getGpBundleConverter().convert(rootBundleTemplate);
        }
        return null;
    }


    public BundleTemplateService getBundleTemplateService() {
        return bundleTemplateService;
    }

    public void setBundleTemplateService(BundleTemplateService bundleTemplateService) {
        this.bundleTemplateService = bundleTemplateService;
    }

    public GPBundleService getGpBundleService() {
        return gpBundleService;
    }

    public void setGpBundleService(GPBundleService gpBundleService) {
        this.gpBundleService = gpBundleService;
    }

    public Converter<BundleTemplateModel, BundleTemplateData> getGpBundleConverter() {
        return gpBundleConverter;
    }

    public void setGpBundleConverter(Converter<BundleTemplateModel, BundleTemplateData> gpBundleConverter) {
        this.gpBundleConverter = gpBundleConverter;
    }
}
