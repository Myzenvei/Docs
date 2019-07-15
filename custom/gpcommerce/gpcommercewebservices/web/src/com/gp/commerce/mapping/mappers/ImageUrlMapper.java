/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.mapping.mappers;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;
import com.gp.commerce.constants.YcommercewebservicesConstants;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang3.StringUtils;

public class ImageUrlMapper extends AbstractCustomMapper<ImageData, ImageWsDTO>
{
    @Override
    public void mapAtoB(final ImageData a, final ImageWsDTO b, final MappingContext context)
    {
        // other fields are mapped automatically

        context.beginMappingField("url", getAType(), a, "url", getBType(), b);
        try
        {
            if (shouldMap(a, b, context) && null != a.getUrl()  && !a.getUrl().startsWith(YcommercewebservicesConstants.HTTP_START))
            {
                String url = YcommercewebservicesConstants.V2_ROOT_CONTEXT + a.getUrl();
                b.setUrl(url);
            }
        }
        finally
        {
            context.endMappingField();
        }
    }
}
