/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.configurablebundle;

import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;

import javax.annotation.Nonnull;

/**
 * The Interface GPBundleFacade.
 */
public interface GPBundleFacade {
    
    /**
     * Builds the bundle tree.
     *
     * @param bundleId the bundle id
     * @return the bundle template data
     */
    public BundleTemplateData buildBundleTree(@Nonnull final String bundleId);

}
