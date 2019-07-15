/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.conv;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.gp.commerce.constants.YcommercewebservicesConstants;

import java.util.Optional;

public class ImageUrlConverter implements SingleValueConverter
{
    @Override
    public String toString(Object o)
    {
        return Optional.ofNullable(o)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(this::addRootContext)
                .orElseGet(() -> null);
    }

    protected String addRootContext(final String imageUrl){
        return YcommercewebservicesConstants.V1_ROOT_CONTEXT + imageUrl;
    }

    @Override
    public Object fromString(String s)
    {
        return null;
    }

    @Override
    public boolean canConvert(Class type)
    {
        return type == String.class;
    }
}
