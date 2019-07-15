/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.job;

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
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.ProductSpecificationsCronJobModel;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.util.GPProductSpecificationUtils;

public class ProductSpecsPopulatorJob extends AbstractJobPerformable<ProductSpecificationsCronJobModel>{

	private static final Logger LOG = Logger.getLogger(ProductSpecsPopulatorJob.class);
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
	public PerformResult perform(final ProductSpecificationsCronJobModel cronJob) {
		LOG.info("***** Product Specifications Populator Cronjob started *****");
		try {
			
			final ClassificationSystemVersionModel systemVersion = getClassificationSystemService().getSystemVersion(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE, GpcommerceCoreConstants.GP_US_CLASSIFICATION_VERSION);

			final ClassificationClassModel productDetailsClass = getClassificationSystemService().getClassForCode(systemVersion, GpcommerceCoreConstants.PRODUCT_DETAILS_CODE);
			final ClassificationClassModel caseShippingInfoClass = getClassificationSystemService().getClassForCode(systemVersion, GpcommerceCoreConstants.CASE_SHIPPING_INFO_CODE);
			final ClassificationClassModel eachShippingInfoClass = getClassificationSystemService().getClassForCode(systemVersion, GpcommerceCoreConstants.EACH_SHIPPING_INFO_CODE);
			final ClassificationClassModel unitShippingInfoClass = getClassificationSystemService().getClassForCode(systemVersion, GpcommerceCoreConstants.UNIT_SHIPPING_INFO_CODE);
			final ClassificationClassModel QSUShippingInfoClass = getClassificationSystemService().getClassForCode(systemVersion, GpcommerceCoreConstants.QSU_SHIPPING_INFO_CODE);
			final ClassificationClassModel bundleShippingInfoClass = getClassificationSystemService().getClassForCode(systemVersion, GpcommerceCoreConstants.BUNDLE_SHIPPING_INFO_CODE);

			final List<ProductModel> products = getGpProductService().getAllProductsForSpecsPopulatorJob(cronJob.getStartTime(),
					cronJob.getFeatureUpdateDate(), cronJob.getProductCode(), GP_CATALOG_STAGED_VERSION);
			if(CollectionUtils.isNotEmpty(products)) {
				for(final ProductModel product : products) {
					try {
						if (clearAbortRequestedIfNeeded(cronJob))
						{
							LOG.debug("The job is aborted.");
							return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
						}
						//Check if GPUSClassification is associated to the product
						boolean featureFlag  = false;
						if(CollectionUtils.isNotEmpty(product.getClassificationClasses())) {
							for(final ClassificationClassModel category : product.getClassificationClasses()) {
								if(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE.equalsIgnoreCase(category.getCatalogVersion().getCatalog().getId())) {
									
									featureFlag  = true;
									break;
								}
							}
						}
						if (null != product.getUnit())
						{
							final String UOM = product.getUnit().getCode();

							//Associate GP US Classification to the product if it is not associated
							if (!featureFlag)
							{

								final List<CategoryModel> productCategories = new ArrayList<CategoryModel>(product.getSupercategories());
								productCategories.add(productDetailsClass);
								productCategories.add(unitShippingInfoClass);

								if (UOM.equalsIgnoreCase(GpcommerceCoreConstants.QSU))
								{
									productCategories.add(QSUShippingInfoClass);
									productCategories.add(bundleShippingInfoClass);
								}
								else
								{
									productCategories.add(caseShippingInfoClass);
									productCategories.add(eachShippingInfoClass);
								}

								product.setSupercategories(productCategories);
								modelService.save(product);
							}

							removeGPUSClassificationFeatureValues(product);
							getGpProductSpecificationUtils().updateProductWithAdditionalAttributes(product);
							//Update Feature Values for the product
							updateFeatures(product, UOM);
						}
					}
					catch(final Exception ex) {
						LOG.error("ProductSpecsPopulatorJob | Error in updating feature values for product: " + product.getCode(), ex);
						continue;
					}
				}
			}
		}
		catch(final Exception e) {
			LOG.error("Error in running Product Specifications Populator CronJob: " + e.getMessage(),e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		LOG.info("Product Specifications Populator CronJob finished successfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private void removeGPUSClassificationFeatureValues(final ProductModel product)
	{
		try
		{

			final FeatureList featureList = classificationService.getFeatures(product);

			for (final Feature productFeature : featureList.getFeatures())
			{
				if (productFeature.getClassAttributeAssignment().getClassificationClass().getCatalogVersion().getCatalog().getId()
						.equalsIgnoreCase(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE))
				{
					productFeature.removeAllValues();
				}
			}
			classificationService.replaceFeatures(product, featureList);
			LOG.debug(String.format(
					"The current gp classification system attribute values for the product [%s] have been removed before importing the new ones.",
					product.getCode()));
		}

		catch (final Exception ex)
		{
			LOG.error(ex);
			LOG.error(
					String.format("Something went wrong while removing classification system attribute values for the product [%s]!",
							product) + ex.getMessage());
		}

	}

	private void updateFeatures(final ProductModel product, final String UOM) {

		if(CollectionUtils.isNotEmpty(product.getClassificationClasses())) {
			if (!UOM.equalsIgnoreCase(GpcommerceCoreConstants.QSU))
			{
			for(final ClassificationClassModel category : product.getClassificationClasses()) {

				if(GpcommerceCoreConstants.PRODUCT_DETAILS_CODE.equalsIgnoreCase(category.getCode())) {
					getGpProductSpecificationUtils().updateProductDetailsFeatures(product);
				}
				else if(GpcommerceCoreConstants.CASE_SHIPPING_INFO_CODE.equalsIgnoreCase(category.getCode())) {
					getGpProductSpecificationUtils().updateCaseShippingInfoFeatures(product, UOM);
				}
				else if(GpcommerceCoreConstants.EACH_SHIPPING_INFO_CODE.equalsIgnoreCase(category.getCode())) {
					getGpProductSpecificationUtils().updateEachShippingInfoFeatures(product, UOM);
				}
				else if(GpcommerceCoreConstants.UNIT_SHIPPING_INFO_CODE.equalsIgnoreCase(category.getCode())) {
					getGpProductSpecificationUtils().updateUnitShippingInfoFeatures(product, UOM);
				}
				}
			}
			else if (UOM.equalsIgnoreCase(GpcommerceCoreConstants.QSU))
			{

				for (final ClassificationClassModel category : product.getClassificationClasses())
				{

					if (GpcommerceCoreConstants.PRODUCT_DETAILS_CODE.equalsIgnoreCase(category.getCode()))
					{
						getGpProductSpecificationUtils().updateProductDetailsFeatures(product);
					}
					else if (GpcommerceCoreConstants.QSU_SHIPPING_INFO_CODE.equalsIgnoreCase(category.getCode()))
					{
						getGpProductSpecificationUtils().updateQSUShippingInfoFeatures(product, UOM);
					}
					else if (GpcommerceCoreConstants.BUNDLE_SHIPPING_INFO_CODE.equalsIgnoreCase(category.getCode()))
					{
						getGpProductSpecificationUtils().updateBundleShippingInfoFeatures(product);
					}
				}


			}
		}

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
	@Override
	public boolean isAbortable() {
		return true;
	} 

}