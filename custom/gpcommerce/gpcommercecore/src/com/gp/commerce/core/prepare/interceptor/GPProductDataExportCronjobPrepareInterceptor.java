/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.prepare.interceptor;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import com.gp.commerce.core.model.ProductDataExportCronjobModel;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

public class GPProductDataExportCronjobPrepareInterceptor implements PrepareInterceptor<ProductDataExportCronjobModel> {

	@Resource
	ModelService modelService;
	@Resource
	ConfigurationService configurationService;
	@Resource
	MediaService mediaService;

	@Override
	public void onPrepare(ProductDataExportCronjobModel cronJob, InterceptorContext ctx) throws InterceptorException {

		if (ctx.isModified(cronJob, ProductDataExportCronjobModel.INPUTFORMAT)) {

			Map<String, String> exportAttributes = new HashMap<>();
			String attr1 = StringUtils.EMPTY;
			if (StringUtils.isNotBlank(cronJob.getInputFormat())) {
				attr1 = configurationService.getConfiguration()
						.getString("export.attributes." + cronJob.getInputFormat());
			}
			if (!attr1.isEmpty()) {
				String[] attributes = attr1.split(";");
				for (String attribute : attributes) {
					String[] values = attribute.split(":");
					exportAttributes.put(values[0], values[1]);
				}
				cronJob.setExportAttributes(exportAttributes);

			} else {
				cronJob.setExportAttributes(exportAttributes);
			}

		}

	}

}
