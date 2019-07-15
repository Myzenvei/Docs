/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionWsDTO;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class GPShippingRestrictionPopulator  implements Populator<ShippingRestrictionModel, ShippingRestrictionsData>{


	@Override
	public void populate(ShippingRestrictionModel source, ShippingRestrictionsData target) throws ConversionException {
				CountryWsDTO country = new CountryWsDTO();
				RegionWsDTO region = new RegionWsDTO();
				country.setIsocode(source.getCountryIsoCode());
				region.setIsocode(source.getRegion());
				target.setProductCode(source.getProductCode());
				target.setCountry(country);
				target.setRegion(region);
			}
}