/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.populators;

import com.gp.commerce.bundle.data.BundleEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPBundleEntryPopulator implements Populator<CartEntryModel, BundleEntryData> {

    @Override
    public void populate(CartEntryModel cartEntryModel, BundleEntryData bundleEntryData) throws ConversionException {

        bundleEntryData.setProductCode(cartEntryModel.getProduct().getCode());
        bundleEntryData.setBundleTemplateId(cartEntryModel.getBundleTemplate().getId());
        bundleEntryData.setEntryGroupNumber(cartEntryModel.getEntryGroupNumbers().iterator().next());
        bundleEntryData.setEntryNumber(cartEntryModel.getEntryNumber());
        bundleEntryData.setQuantity(cartEntryModel.getQuantity());
    }
}
