/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.GpBrandLabelPopulatorCronJobModel;
import com.gp.commerce.core.model.ProductSpecificationsCronJobModel;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.util.GPProductSpecificationUtils;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class GpBrandLabelPopulatorJob extends AbstractJobPerformable<GpBrandLabelPopulatorCronJobModel>
{



	private static final Logger LOG = Logger.getLogger(GpBrandLabelPopulatorJob.class);
	private static final String GP_CATALOG_STAGED_VERSION = "Staged";

	private ClassificationSystemService classificationSystemService;
	private GPProductService gpProductService;
	private GPProductSpecificationUtils gpProductSpecificationUtils;
	private ClassificationService classificationService;
	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel )
	 */
	@Override
	public PerformResult perform(final GpBrandLabelPopulatorCronJobModel cronJob) {
		LOG.info("***** GpBrandLabel Populator CronJob started *****");
		try {
			
			List<ProductModel> products = null;			
			String prods = cronJob.getProductCodeList();
			if (prods == null) 
			{
			List<ProductModel> prodList = getGpProductService().getAllProductsForGpBrandLabelPopulatorJob(cronJob.getStartTime(),
					cronJob.getFeatureUpdateDate(), cronJob.getProductCode(), GP_CATALOG_STAGED_VERSION);
			 products = prodList;
			}			
			
		    
			else
			{
				String[] gpProductArray = prods.split(",");				
				List<String> gpProductList = Arrays.asList(gpProductArray);
				List<ProductModel> prodList = new ArrayList<ProductModel>();
				
				for (final String product : gpProductList) 
				{

					final List<ProductModel> productList = getGpProductService()
							.getAllProductsForGpBrandLabelPopulatorJob(cronJob.getStartTime(),
									cronJob.getFeatureUpdateDate(), product, GP_CATALOG_STAGED_VERSION);
					
					prodList.addAll(productList);					
					products = prodList;
				}
				
			}
			
			if(CollectionUtils.isNotEmpty(products))
			{
				for(final ProductModel product : products) {
					try {						
						ClassificationClassModel classModel = new ClassificationClassModel();
						classModel.setCode("Z_CATEGORY");
						classModel = flexibleSearchService.getModelByExample(classModel);						
						boolean flag = product.getClassificationClasses().contains(classModel);
						
						if (flag)
						{	
							LOG.info("***** GpBrandLabel Populator CronJob is executed on product code **************** "+product.getCode());
							getGpProductSpecificationUtils().updateProductBrandLabel(product);							
						}
					}
					catch(final Exception ex) {
						LOG.error("GpBrandLabelPopulatorJob | Error in updating brand label values for product: " + product.getCode(), ex);
						continue;
					}
				}
			}
		}
		catch(final Exception e) {
			LOG.error("Error in running Brand Label  Populator CronJob: " + e.getMessage(),e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		LOG.info("Brand Label Populator CronJob finished successfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	

	

	protected GPProductService getGpProductService() {
		return gpProductService;
	}

	@Required
	public void setGpProductService(final GPProductService gpProductService) {
		this.gpProductService = gpProductService;
	}

	protected ClassificationSystemService getClassificationSystemService() {
		return classificationSystemService;
	}

	@Required
	public void setClassificationSystemService(final ClassificationSystemService classificationSystemService) {
		this.classificationSystemService = classificationSystemService;
	}

	protected GPProductSpecificationUtils getGpProductSpecificationUtils() {
		return gpProductSpecificationUtils;
	}

	@Required
	public void setGpProductSpecificationUtils(final GPProductSpecificationUtils gpProductSpecificationUtils) {
		this.gpProductSpecificationUtils = gpProductSpecificationUtils;
	}

	public ClassificationService getClassificationService()
	{
		return classificationService;
	}

	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}
	
}
