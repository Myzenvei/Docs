/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.commercefacades.search.converters.populator.FacetPopulator;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import javax.annotation.Resource;
import com.gp.commerce.core.services.GPCMSSiteService;

public class GPFacetPopulator<QUERY, STATE> extends FacetPopulator<QUERY, STATE> implements Populator<FacetData<QUERY>, FacetData<STATE>>
{
  @Resource(name = "cmsSiteService")
  private GPCMSSiteService cmsSiteService;

	@Override
	public void populate(FacetData<QUERY> source, FacetData<STATE> target) throws ConversionException {
		super.populate(source, target);
		//ShowFacetCount depending on flag
		if(getSiteConfigProperty("showFacetCount")){
		target.setCount(getCount(source));
		}
		
	}

	private long getCount(FacetData<QUERY> source) {
		long count = 0;
		if(null != source.getValues()){
			for (FacetValueData data : source.getValues()) {
				count+=data.getCount();
			}
		}
		return count;
	}
	
   private Boolean getSiteConfigProperty(final String key)
   {
   return null != cmsSiteService.getSiteConfig(key) ? Boolean.valueOf(cmsSiteService.getSiteConfig(key)) : Boolean.FALSE;
   }
}
