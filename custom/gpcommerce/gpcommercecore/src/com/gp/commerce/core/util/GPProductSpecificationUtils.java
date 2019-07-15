/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.core.util;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.BrandLabelEnum;
import com.gp.commerce.core.model.AlternateUOMModel;
import com.gp.commerce.core.services.GPClassificationAttributeAssignmentService;

/**
 * Util class to populate product features
 */
public class GPProductSpecificationUtils
{
	private static final Logger LOG = Logger.getLogger(GPProductSpecificationUtils.class);
	private ClassificationService classificationService;
	private GPClassificationAttributeAssignmentService gpClassificationAttributeAssignmentService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private ConfigurationService configurationService;
	private MessageSource messageSource;
	private ModelService modelService;
	private EnumerationService enumerationService;
	private FlexibleSearchService flexibleSearchService;

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
	public EnumerationService getEnumerationService() {
		return enumerationService;
	}
	public void setEnumerationService(final EnumerationService enumerationService) {
		this.enumerationService = enumerationService;
	}
	
	private Map<String, String> specsAttributesMap = new HashMap<String, String>();
	private static final String PRODUCT_DETAILS_SPEC_ATTRIBUTES_LIST = "productdetails.product.spec.attributes.list";
	private static final String PRODUCT_DETAILS_ERP_SPEC_ATTRIBUTES_LIST = "productdetails.erp.spec.attributes.list";
	private static final String CASE_PRODUCT_SPEC_ATTRIBUTES_LIST = "case.product.spec.attributes.list";
	private static final String UNIT_PRODUCT_SPEC_ATTRIBUTES_LIST = "unit.product.spec.attributes.list";
	private static final String UNIT_ERP_SPEC_ATTRIBUTES_LIST = "unit.erp.spec.attributes.list";
	private static final String UNIT_GP_UOM_SPEC_ATTRIBUTES_LIST = "unit.product.gp.uom.attributes.list";
	private static final String QSU_ERP_SPEC_ATTRIBUTES_LIST = "qsu.erp.spec.attributes.list";
	private static final String EACH_PRODUCT_SPEC_ATTRIBUTES_LIST = "each.product.spec.attributes.list";
	private static final String BUNDLE_PRODUCT_SPEC_ATTRIBUTES_LIST = "bundle.product.spec.attributes.list";
	private static final String QSU_PRODUCT_SPEC_ATTRIBUTES_LIST = "qsu.product.spec.attributes.list";
	private static final String DIMENSIONS_INCH_VALUE = "dimensions.inch.value";
	private static final String DIMENSION_VALUE_WITH_UNIT = "dimensions.value.with.unit";
	private static final String VOLUME_CFT_VALUE = "volume.cft.value";
	private static final String PRODUCT_DETAILS_GP_UOM_SPEC_ATTRIBUTES_LIST = "productdetails.product.gp.uom.spec.attributes.list";
	private static final String DISCARDSPECSVALUES = "product.specs.discarded.values";

	/**
	 * Default constructor.
	 */
	public GPProductSpecificationUtils()
	{
	}

	/**
	 * @param product
	 */
	public void updateProductDetailsFeatures(final ProductModel product)
	{
		final List<String> productDetailsSpecAttributes = getSpecAttributesList(product, PRODUCT_DETAILS_SPEC_ATTRIBUTES_LIST);
		final List<String> productERPSpecAttributes = getSpecAttributesList(product, PRODUCT_DETAILS_ERP_SPEC_ATTRIBUTES_LIST);
		final List<String> productGPUOMspecAttributes = getSpecAttributesList(product, PRODUCT_DETAILS_GP_UOM_SPEC_ATTRIBUTES_LIST);
		saveFeatureValues(product, productDetailsSpecAttributes, null, GpcommerceCoreConstants.PRODUCT_DETAIL);
		saveFeaturesFromGPAlternateUOM(product, productGPUOMspecAttributes);
		saveERPFeatureValues(product, productERPSpecAttributes, GpcommerceCoreConstants.PRODUCT_DETAIL);
	}

	/**
	 * Update case shipping info features.
	 * @param product the product
	 * @param UOM the uom
	 */
	public void updateCaseShippingInfoFeatures(final ProductModel product, final String UOM)
	{
		final List<String> caseProductSpecAttributes = getSpecAttributesList(product, CASE_PRODUCT_SPEC_ATTRIBUTES_LIST);
		if (UOM.equalsIgnoreCase(GpcommerceCoreConstants.CS))
		{
			saveFeatureValues(product, caseProductSpecAttributes, GpcommerceCoreConstants.CASE, GpcommerceCoreConstants.CS);
		} else {
			AlternateUOMModel gpAlternateUOM = null;
			for (final AlternateUOMModel alternateUOM : product.getAlternateUOM())
			{
				if (alternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.CS))
				{
					gpAlternateUOM = alternateUOM;
					break;
				}
			}
			saveFeatureValues(product, gpAlternateUOM, caseProductSpecAttributes, GpcommerceCoreConstants.CASE);
		}
	}

	/**
	 * Update each shipping info features.
	 * @param product the product
	 * @param UOM the uom
	 */
	public void updateEachShippingInfoFeatures(final ProductModel product, final String UOM)
	{
		final List<String> eachProductSpecAttributes = getSpecAttributesList(product, EACH_PRODUCT_SPEC_ATTRIBUTES_LIST);
		if (UOM.equalsIgnoreCase(GpcommerceCoreConstants.EA))
		{
			saveFeatureValues(product, eachProductSpecAttributes, GpcommerceCoreConstants.EACH, GpcommerceCoreConstants.EA);
		} else {
			AlternateUOMModel gpAlternateUOM = null;
			for (final AlternateUOMModel alternateUOM : product.getAlternateUOM())
			{
				if (alternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.EA))
				{
					gpAlternateUOM = alternateUOM;
					break;
				}
			}
			saveFeatureValues(product, gpAlternateUOM, eachProductSpecAttributes, GpcommerceCoreConstants.EACH);
		}
	}

	/**
	 * Update unit shipping info features.
	 * @param product the product
	 * @param UOM the uom
	 */
	public void updateUnitShippingInfoFeatures(final ProductModel product, final String UOM)
	{
		final List<String> unitProductSpecAttributes = getSpecAttributesList(product, UNIT_PRODUCT_SPEC_ATTRIBUTES_LIST);
		final List<String> unitERPSpecAttributes = getSpecAttributesList(product, UNIT_ERP_SPEC_ATTRIBUTES_LIST);
		final List<String> unitGPUOMspecAttributes = getSpecAttributesList(product, UNIT_GP_UOM_SPEC_ATTRIBUTES_LIST);
		if (!UOM.equalsIgnoreCase(GpcommerceCoreConstants.ZSU))
		{
			AlternateUOMModel gpAlternateUOM = null;
			for (final AlternateUOMModel alternateUOM : product.getAlternateUOM())
			{
				if (alternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.ZSU))
				{
					gpAlternateUOM = alternateUOM;
					break;
				}
			}
			saveFeatureValues(product, gpAlternateUOM, unitProductSpecAttributes, GpcommerceCoreConstants.UNIT);
		} else {
			saveFeatureValues(product, unitProductSpecAttributes, GpcommerceCoreConstants.UNIT, GpcommerceCoreConstants.ZSU);
		}
		saveERPFeatureValues(product, unitERPSpecAttributes, GpcommerceCoreConstants.UNIT);
		saveFeaturesFromGPAlternateUOM(product, unitGPUOMspecAttributes);
	}

	/**
	 * Update QSU shipping info features.
	 * @param product the product
	 * @param UOM the uom
	 */
	public void updateQSUShippingInfoFeatures(final ProductModel product, final String UOM)
	{
		final List<String> qsuProductSpecAttributes = getSpecAttributesList(product, QSU_PRODUCT_SPEC_ATTRIBUTES_LIST);
		final List<String> unitERPSpecAttributes = getSpecAttributesList(product, QSU_ERP_SPEC_ATTRIBUTES_LIST);
		if (UOM.equalsIgnoreCase(GpcommerceCoreConstants.QSU))
		{
			saveFeatureValues(product, qsuProductSpecAttributes, GpcommerceCoreConstants.QSU, GpcommerceCoreConstants.QSU);
		} else {
			AlternateUOMModel gpAlternateUOM = null;
			for (final AlternateUOMModel alternateUOM : product.getAlternateUOM())
			{
				if (alternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.QSU))
				{
					gpAlternateUOM = alternateUOM;
					break;
				}
			}
			saveFeatureValues(product, gpAlternateUOM, qsuProductSpecAttributes, GpcommerceCoreConstants.QSU);
		}
		saveERPFeatureValues(product, unitERPSpecAttributes, GpcommerceCoreConstants.QSU);
	}

	/**
	 * Update bundle shipping info features.
	 * @param product the product
	 */
	public void updateBundleShippingInfoFeatures(final ProductModel product)
	{
		final List<String> bundleProductSpecAttributes = getSpecAttributesList(product, BUNDLE_PRODUCT_SPEC_ATTRIBUTES_LIST);
		AlternateUOMModel gpAlternateUOM = null;
		for (final AlternateUOMModel alternateUOM : product.getAlternateUOM())
		{
			if (alternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.BDL))
			{
				gpAlternateUOM = alternateUOM;
				break;
			}
		}
		saveFeatureValues(product, gpAlternateUOM, bundleProductSpecAttributes, GpcommerceCoreConstants.BUNDLE);
	}

	private List<String> getSpecAttributesList(final ProductModel product, final String propertyKey)
	{
		List<String> productAttributesList = null;
		final String attributes = getConfigurationService().getConfiguration().getString(propertyKey);
		if (StringUtils.isNotBlank(attributes))
		{
			productAttributesList = Arrays.asList(StringUtils.split(attributes, GpcommerceCoreConstants.COMMA));
		}
		return productAttributesList;
	}

	private void saveFeatureValues(final ProductModel product, final List<String> specAttributesList,
			final String featureNamePrefix, final String classTypeCode)
	{
		final FeatureList features = getClassificationService().getFeatures(product);
		if (features != null && CollectionUtils.isNotEmpty(features.getFeatures()))
		{
			final List<String> discardedAttributeValues = getSpecAttributesList(product, DISCARDSPECSVALUES);
			//Iterate over attributes list and get its corresponding feature names from specs attributes map
			if (CollectionUtils.isNotEmpty(specAttributesList) && CollectionUtils.isNotEmpty(discardedAttributeValues))
			{
				for (final String attribute : specAttributesList)
				{
					String featureName = StringUtils.EMPTY;
					Object attributeValue = null;
					if (StringUtils.isNotBlank(featureNamePrefix))
					{
						featureName = featureNamePrefix + " " + getSpecsAttributesMap().get(attribute);
						if (attribute.equalsIgnoreCase(GpcommerceCoreConstants.DIMENSIONS))
						{
							if (product.getLength() != null && product.getWidth() != null && product.getHeight() != null)
							{
								if (product.getDimensionsUOM() != null && ("INCH".equalsIgnoreCase(product.getDimensionsUOM())
										|| "INH".equalsIgnoreCase(product.getDimensionsUOM())))
								{
									final String attributeValueProperty = configurationService.getConfiguration()
											.getString(DIMENSIONS_INCH_VALUE);
									attributeValue = attributeValueProperty != null ? MessageFormat.format(attributeValueProperty,
											product.getLength(), product.getWidth(), product.getHeight()) : "";
								}
								else
								{
									final String attributeValueProperty = configurationService.getConfiguration()
											.getString(DIMENSION_VALUE_WITH_UNIT);
									final String dimensionUOM = product.getDimensionsUOM() != null ? product.getDimensionsUOM() : "";
									attributeValue = attributeValueProperty != null
											? MessageFormat.format(attributeValueProperty, product.getLength(), dimensionUOM,
													product.getWidth(), dimensionUOM, product.getHeight(), dimensionUOM)
											: "";
								}
							}
						}
						else if (attribute.equalsIgnoreCase(GpcommerceCoreConstants.VOLUME) && product.getVolume() != null)
						{
							final String attributeValueProperty = configurationService.getConfiguration().getString(VOLUME_CFT_VALUE);
							final String volumeUOM = product.getEachvolumeuom() != null
									? "FTQ".equalsIgnoreCase(product.getEachvolumeuom()) ? "CFT" : product.getEachvolumeuom() : "";
							attributeValue = attributeValueProperty != null
									? MessageFormat.format(attributeValueProperty, product.getVolume()) + " " + volumeUOM : "";
						} else if ("netwgt".equalsIgnoreCase(attribute)) {
							attributeValue = product.getNetweight();
						} else {
							attributeValue = modelService.getAttributeValue(product, attribute);
						}
					} else {
						featureName = getSpecsAttributesMap().get(attribute);
						attributeValue = modelService.getAttributeValue(product, attribute);
					}
					if (attributeValue instanceof HybrisEnumValue)
					{
						attributeValue = ((HybrisEnumValue) attributeValue).getCode();
					} else if (attributeValue instanceof ProductModel) {
						attributeValue = ((ProductModel) attributeValue).getCode();
					}
					if (attributeValue != null && !discardedAttributeValues.contains(attributeValue.toString()))
					{
						String attributeValueWithUOM = attributeValue.toString();
						if (attribute.toLowerCase().contains("weight") || attribute.toLowerCase().contains("wgt"))
						{
							for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
							{
								if (gpAlternateUOM.getAlternateUnit() != null
										&& gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(classTypeCode))
								{
									attributeValueWithUOM = attributeValue.toString() + " " + gpAlternateUOM.getWeightUOM();
								}
							}
						}
						final FeatureValue fValue = new FeatureValue(attributeValueWithUOM.toString());
						final List<FeatureValue> fvalues = new ArrayList<FeatureValue>();
						fvalues.add(fValue);
						for (final Feature productfeature : features.getFeatures())
						{
							final String systemId = productfeature.getClassAttributeAssignment().getClassificationClass()
									.getCatalogVersion().getCatalog().getId();
							if (featureName.equalsIgnoreCase(productfeature.getName())
									&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE))
							{
								productfeature.setValues(fvalues);
								getClassificationService().setFeature(product, productfeature);
								break;
							}
						}
					}
				}
			}
		}
	}

	private void saveFeatureValues(final ProductModel product, final AlternateUOMModel gpAlternateUOM,
			final List<String> specAttributesList, final String featureNamePrefix)
	{
		final FeatureList features = getClassificationService().getFeatures(product);
		if (features != null && CollectionUtils.isNotEmpty(features.getFeatures()))
		{
			final List<String> discardedAttributeValues = getSpecAttributesList(product, DISCARDSPECSVALUES);
			//Iterate over attributes list and get its corresponding feature names from specs attributes map
			if (CollectionUtils.isNotEmpty(specAttributesList) && gpAlternateUOM != null)
			{
				for (final String attribute : specAttributesList)
				{
					String featureName = StringUtils.EMPTY;
					Object attributeValue = null;
					if (StringUtils.isNotBlank(featureNamePrefix))
					{
						featureName = featureNamePrefix + " " + getSpecsAttributesMap().get(attribute);
						if (attribute.equalsIgnoreCase(GpcommerceCoreConstants.DIMENSIONS))
						{
							if (gpAlternateUOM.getLength() != null && gpAlternateUOM.getWidth() != null
									&& gpAlternateUOM.getHeight() != null)
							{
								if (gpAlternateUOM.getDimensionsuom() != null
										&& ("INCH".equalsIgnoreCase(gpAlternateUOM.getDimensionsuom())
												|| "INH".equalsIgnoreCase(gpAlternateUOM.getDimensionsuom())))
								{
									final String attributeValueProperty = configurationService.getConfiguration()
											.getString(DIMENSIONS_INCH_VALUE);
									attributeValue = attributeValueProperty != null ? MessageFormat.format(attributeValueProperty,
											gpAlternateUOM.getLength(), gpAlternateUOM.getWidth(), gpAlternateUOM.getHeight()) : "";
								}
								else
								{
									final String attributeValueProperty = configurationService.getConfiguration()
											.getString(DIMENSION_VALUE_WITH_UNIT);
									final String dimensionUOM = gpAlternateUOM.getDimensionsuom() != null
											? gpAlternateUOM.getDimensionsuom() : "";
									attributeValue = attributeValueProperty != null
											? MessageFormat.format(attributeValueProperty, gpAlternateUOM.getLength(), dimensionUOM,
													gpAlternateUOM.getWidth(), dimensionUOM, gpAlternateUOM.getHeight(), dimensionUOM)
											: "";
								}
							}
						}
						else if (attribute.equalsIgnoreCase(GpcommerceCoreConstants.VOLUME) && gpAlternateUOM.getVolume() != null)
						{
							final String attributeValueProperty = configurationService.getConfiguration().getString(VOLUME_CFT_VALUE);
							final String volumeUOM = gpAlternateUOM.getVolumeuom() != null
									? "FTQ".equalsIgnoreCase(gpAlternateUOM.getVolumeuom()) ? "CFT" : gpAlternateUOM.getVolumeuom() : "";
							attributeValue = attributeValueProperty != null
									? MessageFormat.format(attributeValueProperty, gpAlternateUOM.getVolume()) + " " + volumeUOM : "";
						}
						else if ("netwgt".equalsIgnoreCase(attribute))
						{
							attributeValue = gpAlternateUOM.getNetWeight();
						} else {
							attributeValue = modelService.getAttributeValue(gpAlternateUOM, attribute);
						}
					} else {
						featureName = getSpecsAttributesMap().get(attribute);
						attributeValue = modelService.getAttributeValue(gpAlternateUOM, attribute);
					}
					if (attributeValue instanceof HybrisEnumValue)
					{
						attributeValue = ((HybrisEnumValue) attributeValue).getCode();
					}
					else if (attributeValue instanceof ProductModel)
					{
						attributeValue = ((ProductModel) attributeValue).getCode();
					}
					if (attributeValue != null && !discardedAttributeValues.contains(attributeValue.toString()))
					{
						String attributeValueWithUOM = attributeValue.toString();
						if ("weight".toLowerCase().contains(attribute) || "wgt".toLowerCase().contains(attribute))
						{
							attributeValueWithUOM = attributeValue.toString() + " " + gpAlternateUOM.getWeightUOM();
						}
						final FeatureValue fValue = new FeatureValue(attributeValueWithUOM.toString());
						final List<FeatureValue> fvalues = new ArrayList<FeatureValue>();
						fvalues.add(fValue);
						for (final Feature productfeature : features.getFeatures())
						{
							final String systemId = productfeature.getClassAttributeAssignment().getClassificationClass()
									.getCatalogVersion().getCatalog().getId();
							if (featureName.equalsIgnoreCase(productfeature.getName())
									&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE))
							{
								productfeature.setValues(fvalues);
								getClassificationService().setFeature(product, productfeature);
								break;
							}
						}
					}
				}
			}
		}
	}

	private void saveERPFeatureValues(final ProductModel product, final List<String> erpSpecAttributes, final String classTypeCode)
	{
		final FeatureList features = getClassificationService().getFeatures(product);
		final Map<String, String> specsKeyValuePair = new HashMap<>();
		if (features != null && CollectionUtils.isNotEmpty(features.getFeatures()))
		{
			final List<String> discardedAttributeValues = getSpecAttributesList(product, DISCARDSPECSVALUES);
			if (CollectionUtils.isNotEmpty(erpSpecAttributes))
			{
				for (final String erpSpecAttribute : erpSpecAttributes)
				{
					final String[] specAttribute = StringUtils.split(erpSpecAttribute, GpcommerceCoreConstants.DOT);
					final String featureClass = specAttribute[0];
					final String featureCode = specAttribute[1];
					//Fetch ERP Classification attribute with assignment
					Feature erpFeature = null;
					try
					{
						final ClassAttributeAssignmentModel erpClassAttAssignment = getGpClassificationAttributeAssignmentService()
								.findClassAttributeAssignment(GpcommerceCoreConstants.ERP_CLASSIFICATION_CODE,
										GpcommerceCoreConstants.ERP_CLASSIFICATION_VERSION, featureClass, featureCode);
						erpFeature = features.getFeatureByAssignment(erpClassAttAssignment);
					}
					catch (final Exception e)
					{
						LOG.error(String.format("ClassAttributeAssignmentModel is not found for [%s]", featureCode), e);
					}
					for (final Feature productFeature : features.getFeatures())
					{
						final String systemId = productFeature.getClassAttributeAssignment().getClassificationClass()
								.getCatalogVersion().getCatalog().getId();
						final String systemClass = productFeature.getClassAttributeAssignment().getClassificationClass().getCode();
						if (productFeature.getCode().toLowerCase().contains(featureCode.toLowerCase())
								&& systemClass.equalsIgnoreCase(featureClass)
								&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.ERP_CLASSIFICATION_CODE))
						{
							erpFeature = productFeature;
							break;
						}
					}
					if (erpFeature != null && erpFeature.getValue() != null)
					{
						Object attributeValue = erpFeature.getValue().getValue();
						if (attributeValue != null && attributeValue instanceof ClassificationAttributeValueModel)
						{
							attributeValue = ((ClassificationAttributeValueModel) attributeValue)
									.getName(getCommerceCommonI18NService().getCurrentLocale());
						}
						if (attributeValue != null && !discardedAttributeValues.contains(attributeValue.toString()))
						{
							final FeatureValue fValue = new FeatureValue(attributeValue.toString());
							specsKeyValuePair.put(featureCode, fValue.getValue().toString());
							LOG.debug(String.format("feature value for [%s] is [%s] ", featureCode, fValue.getValue().toString()));
							final List<FeatureValue> fvalues = new ArrayList<FeatureValue>();
							fvalues.add(fValue);
							//Get Feature Name for ERP attribute
							final String featureName = getSpecsAttributesMap().get(featureCode);
							if (featureName != null)
							{
								for (final Feature productfeature : features.getFeatures())
								{
									final String systemId = productfeature.getClassAttributeAssignment().getClassificationClass()
											.getCatalogVersion().getCatalog().getId();
									if (featureName.equalsIgnoreCase(productfeature.getName())
											&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE))
									{
										if (CollectionUtils.isEmpty(productfeature.getValues()))
										{
											productfeature.setValues(fvalues);
											getClassificationService().setFeature(product, productfeature);
										}
										break;
									}
								}
							}
						}
					}
				}
				updateDerivedAttributes(specsKeyValuePair, features, product, classTypeCode);
			}
		}
	}

	private void updateDerivedAttributes(final Map<String, String> specsKeyValuePair, final FeatureList features,
			final ProductModel product, final String classTypeCode)
	{
		final List<String> discardedAttributeValues = getSpecAttributesList(product, DISCARDSPECSVALUES);
		final Iterator it = getSpecsAttributesMap().entrySet().iterator();
		while (it.hasNext())
		{
			final Map.Entry pair = (Map.Entry) it.next();
			String featureValue = null;
			String featureName = "";
			if (classTypeCode.equalsIgnoreCase(GpcommerceCoreConstants.PRODUCT_DETAIL))
			{
				final String sheetLength = specsKeyValuePair.get("sheet_length");
				final String sheetWidth = specsKeyValuePair.get("sheet_width");
				final String sheetCount = specsKeyValuePair.get("sheet_count");
				final String itemsPerEach = specsKeyValuePair.get("items_per_each");
				final DecimalFormat formatter = new DecimalFormat("###.###");
				if ("squareInches".equals(pair.getKey()) && sheetLength != null && sheetWidth != null)
				{
					featureName = getSpecsAttributesMap().get("squareInches");
					featureValue = formatter.format(new Double(sheetLength) * new Double(sheetWidth));
				}
				else if ("sheet_widthlength".equals(pair.getKey()) && sheetLength != null && sheetWidth != null)
				{
					if (!discardedAttributeValues.contains(sheetLength) && !discardedAttributeValues.contains(sheetWidth))
					{
					featureName = getSpecsAttributesMap().get("sheet_widthlength");
						featureValue = sheetLength + " x " + sheetWidth;
					}
				}
				else if ("squareCM".equals(pair.getKey()) && sheetLength != null && sheetWidth != null)
				{
					featureName = getSpecsAttributesMap().get("squareCM");
					featureValue = formatter.format((new Double(sheetLength) * 2.54) * (new Double(sheetWidth) * 2.54));

				}
				else if ("squareFeet".equals(pair.getKey()) && itemsPerEach != null && sheetCount != null && sheetLength != null
						&& sheetWidth != null)
				{
					featureName = getSpecsAttributesMap().get("squareFeet");
					featureValue = formatter.format(
							(new Double(sheetLength) * new Double(sheetWidth) * new Double(sheetCount) * new Double(itemsPerEach))
									/ 144);
				}
				else if ("shipContainerType".equals(pair.getKey()) && (specsKeyValuePair.get("floor_unit_load_53ft_truck") != null
						|| specsKeyValuePair.get("pallet_unit_load_53ft_truck") != null))
				{
					final Double floorvalue = specsKeyValuePair.get("floor_unit_load_53ft_truck") != null
							? new Double(specsKeyValuePair.get("floor_unit_load_53ft_truck")) : 0.0;
					final Double palletvalue = specsKeyValuePair.get("pallet_unit_load_53ft_truck") != null
							? new Double(specsKeyValuePair.get("pallet_unit_load_53ft_truck")) : 0.0;
					featureName = getSpecsAttributesMap().get("shipContainerType");
					if (floorvalue > 0)
					{
						featureValue = "53Ft Trk Floor Ship";
					}
					else if (palletvalue > 0)
					{
						featureValue = "53Ft Trk Pallet Ship";
					}
				}
			}
			else if (classTypeCode.equalsIgnoreCase(GpcommerceCoreConstants.QSU))
			{
				if ("qsu_hi".equals(pair.getKey()) && specsKeyValuePair.get("physical_hi") != null)
				{
					featureName = getSpecsAttributesMap().get("qsu_hi");
					featureValue = specsKeyValuePair.get("physical_hi");
				}
				else if ("qsu_ti".equals(pair.getKey()) && specsKeyValuePair.get("physical_ti") != null)
				{
					featureName = getSpecsAttributesMap().get("qsu_ti");
					featureValue = specsKeyValuePair.get("physical_ti");

				}
				else if ("qsu_hi_ti".equals(pair.getKey()) && specsKeyValuePair.get("physical_ti") != null
						&& specsKeyValuePair.get("physical_hi") != null)
				{
					featureName = getSpecsAttributesMap().get("qsu_hi_ti");

					featureValue = String.valueOf(
							new Double(specsKeyValuePair.get("physical_ti")) * new Double(specsKeyValuePair.get("physical_hi")));
				}
			}
			if (featureValue != null && featureName != null && !discardedAttributeValues.contains(featureValue.toString()))
			{
				final FeatureValue fValue = new FeatureValue(featureValue == null ? StringUtils.EMPTY : featureValue.toString());
				final List<FeatureValue> fvalues = new ArrayList<FeatureValue>();
				fvalues.add(fValue);
				for (final Feature productfeature : features.getFeatures())
				{
					final String systemId = productfeature.getClassAttributeAssignment().getClassificationClass().getCatalogVersion()
							.getCatalog().getId();
					if (featureName.equalsIgnoreCase(productfeature.getName())
							&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE))
					{
						productfeature.setValues(fvalues);
						getClassificationService().setFeature(product, productfeature);
						break;
					}
				}
			}
		}
	}

	private void saveFeaturesFromGPAlternateUOM(final ProductModel product, final List<String> gpUOMSpecAttributes)
	{
		final String matchPattern = "0.0";
		for (final String attribute : gpUOMSpecAttributes)
		{
			final List<String> discardedAttributeValues = getSpecAttributesList(product, DISCARDSPECSVALUES);
			String attributeValue = null;
			if ("each_per_ship_unit".equalsIgnoreCase(attribute))
			{
				if (product.getUnit().getCode().equalsIgnoreCase(GpcommerceCoreConstants.CS))
				{
					for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
					{
						if (gpAlternateUOM.getAlternateUnit() != null
								&& gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.EA))
						{
							if (gpAlternateUOM.getAltXUOM() != null)
							{
								attributeValue = gpAlternateUOM.getAltX() != null ? gpAlternateUOM.getAltX().matches(matchPattern)
										? gpAlternateUOM.getAltXUOM() : gpAlternateUOM.getAltX() + " " + gpAlternateUOM.getAltXUOM()
										: gpAlternateUOM.getAltXUOM();
							} else {
								attributeValue = gpAlternateUOM.getAltX();
							}
						}
					}
				}
			}
			else if ("each_per_ship_uom".equalsIgnoreCase(attribute))
			{
				if (product.getUnit().getCode().equalsIgnoreCase(GpcommerceCoreConstants.CS))
				{
					for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
					{
						if (gpAlternateUOM.getAlternateUnit() != null
								&& gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.EA))
						{
							attributeValue = gpAlternateUOM.getAltXUOM();

						}
					}
				}
			}
			else if ("case_total".equalsIgnoreCase(attribute))
			{
				for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
				{
					if (gpAlternateUOM.getAlternateUnit() != null
							&& gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.PCE))
					{
						attributeValue = gpAlternateUOM.getAltX();
					}
				}
			}
			else if ("total_pallet_height".equalsIgnoreCase(attribute))
			{
				for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
				{
					if (gpAlternateUOM.getAlternateUnit() != null
							&& gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.ZSU))
					{
						attributeValue = gpAlternateUOM.getHeight() != null
								? String.valueOf(Double.valueOf(gpAlternateUOM.getHeight()) + 6) : null;
					}
				}
			}
			else if ("buyMultiple".equalsIgnoreCase(attribute))
			{
				attributeValue = product.getBuyMultiple() != null ? (product.getBuyMultiple() + " " + product.getUnit().getCode())
						: product.getUnit().getCode();
			}
			else if ("buy_multiple_uom".equalsIgnoreCase(attribute))
			{
				attributeValue = product.getUnit().getCode();
			}
			else if ("size".equalsIgnoreCase(attribute))
			{
				for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
				{
					if (gpAlternateUOM.getAlternateUnit() != null
							&& gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.PCE))
					{
						if (gpAlternateUOM.getDimensionsuom() != null && ("INCH".equalsIgnoreCase(gpAlternateUOM.getDimensionsuom())
								|| "INH".equalsIgnoreCase(gpAlternateUOM.getDimensionsuom())))
						{
							final String attributeValueProperty = configurationService.getConfiguration()
									.getString(DIMENSIONS_INCH_VALUE);
							attributeValue = attributeValueProperty != null ? MessageFormat.format(attributeValueProperty,
									gpAlternateUOM.getLength(), gpAlternateUOM.getWidth(), gpAlternateUOM.getHeight()) : "";
						}
						else
						{
							final String attributeValueProperty = configurationService.getConfiguration()
									.getString(DIMENSION_VALUE_WITH_UNIT);
							final String dimensionUOM = gpAlternateUOM.getDimensionsuom() != null ? gpAlternateUOM.getDimensionsuom()
									: "";
							attributeValue = attributeValueProperty != null
									? MessageFormat.format(attributeValueProperty, gpAlternateUOM.getLength(), dimensionUOM,
											gpAlternateUOM.getWidth(), dimensionUOM, gpAlternateUOM.getHeight(), dimensionUOM)
									: "";
						}
					}
				}
			}
			else if ("unit_qty".equalsIgnoreCase(attribute))
			{
				for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
				{
					if (gpAlternateUOM.getAlternateUnit() != null
							&& gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.ZSU))
					{
						if (gpAlternateUOM.getAltY() != null)
						{
							attributeValue = gpAlternateUOM.getAltY();
						}
					}
				}
			}
			final String featureName = getSpecsAttributesMap().get(attribute);
			if (attributeValue != null && featureName != null && !discardedAttributeValues.contains(attributeValue.toString()))
			{
				final FeatureList features = getClassificationService().getFeatures(product);
				final FeatureValue fValue = new FeatureValue(attributeValue == null ? StringUtils.EMPTY : attributeValue.toString());
				final List<FeatureValue> fvalues = new ArrayList<FeatureValue>();
				fvalues.add(fValue);
				for (final Feature productfeature : features.getFeatures())
				{
					final String systemId = productfeature.getClassAttributeAssignment().getClassificationClass().getCatalogVersion()
							.getCatalog().getId();
					if (featureName.equalsIgnoreCase(productfeature.getName())
							&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE))
					{
						productfeature.setValues(fvalues);
						getClassificationService().setFeature(product, productfeature);
						break;
					}
				}
			}
		}
	}

	/**
	 * Gets the GP alternate UOM for product.
	 * @param product the product
	 * @param uomCode the uom code
	 * @return the GP alternate UOM for product
	 */
	public AlternateUOMModel getGPAlternateUOMForProduct(final ProductModel product, final String uomCode)
	{
		if (product != null)
		{
			for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
			{
				if (gpAlternateUOM.getAlternateUnit() != null && gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(uomCode))
				{
					return gpAlternateUOM;
				}
			}
		}
		return null;
	}

	/**
	 * Update product with additional attributes.
	 * @param product the product
	 */
	public void updateProductWithAdditionalAttributes(final ProductModel product)
	{
		if (product.getUnit().getCode().equalsIgnoreCase(GpcommerceCoreConstants.QSU)
				|| product.getUnit().getCode().equalsIgnoreCase(GpcommerceCoreConstants.CS))
		{
			for (final AlternateUOMModel gpAlternateUOM : product.getAlternateUOM())
			{
				if (gpAlternateUOM.getAlternateUnit() != null
						&& (gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.EA)
								|| gpAlternateUOM.getAlternateUnit().equalsIgnoreCase(GpcommerceCoreConstants.BDL)))
				{
					if (gpAlternateUOM.getUpc() != null)
					{
						product.setUpc(gpAlternateUOM.getUpc());
						modelService.save(product);
					}
				}
			}
		}
	}

	public synchronized void updateProductBrandLabel(final ProductModel product)
	{
		final FeatureList features = getClassificationService().getFeatures(product);
		final String featureClass = "Z_CATEGORY";
		final String featureCode = "BRAND_LABEL";
		Feature erpFeature = null;
		try
		{
			final ClassAttributeAssignmentModel erpClassAttAssignment = getGpClassificationAttributeAssignmentService()
					.findClassAttributeAssignment(GpcommerceCoreConstants.ERP_CLASSIFICATION_CODE,
							GpcommerceCoreConstants.ERP_CLASSIFICATION_VERSION, featureClass, featureCode);
			erpFeature = features.getFeatureByAssignment(erpClassAttAssignment);
		} catch (final Exception e) {
			LOG.error(String.format("ClassAttributeAssignmentModel is not found for [%s]", featureCode), e);
		}
		for (final Feature productFeature : features.getFeatures())
		{
			final String systemId = productFeature.getClassAttributeAssignment().getClassificationClass()
					.getCatalogVersion().getCatalog().getId();
			final String systemClass = productFeature.getClassAttributeAssignment().getClassificationClass().getCode();
			if (productFeature.getCode().toLowerCase().contains(featureCode.toLowerCase())
					&& systemClass.equalsIgnoreCase(featureClass)
					&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.ERP_CLASSIFICATION_CODE))
			{
				erpFeature = productFeature;
				break;
			}
		}
		if (erpFeature != null && erpFeature.getValue() != null)
		{
			Object attributeValue = erpFeature.getValue().getValue();
			if (attributeValue != null && attributeValue instanceof ClassificationAttributeValueModel)
			{
				attributeValue = ((ClassificationAttributeValueModel) attributeValue)
						.getName(getCommerceCommonI18NService().getCurrentLocale());
				final FeatureValue fValue = new FeatureValue(attributeValue.toString());
				final List<BrandLabelEnum> brandEnumList = getEnumerationService()
						.getEnumerationValues(BrandLabelEnum._TYPECODE);
				final List<String> brandList = new ArrayList<String>();
				for (final BrandLabelEnum brand : brandEnumList)
				{
			           brandList.add(brand.getCode());
			    }
				if(!brandList.contains(fValue.getValue().toString()))
				{
				final EnumerationValueModel model = (EnumerationValueModel)modelService.create("BrandLabelEnum");
				model.setCode(fValue.getValue().toString());
				model.setName(fValue.getValue().toString());
				modelService.save(model);
				}
				final BrandLabelEnum brand = enumerationService.getEnumerationValue("BrandLabelEnum", fValue.getValue().toString());
				product.setBrandLabel(brand);
				modelService.save(product);
			}
		}
	}

	/**
	 * Gets the specs attributes map.
	 * @return the specs attributes map
	 */
	public Map<String, String> getSpecsAttributesMap()
	{
		return this.specsAttributesMap == null ? Collections.emptyMap() : this.specsAttributesMap;
	}
	@Required
	public void setSpecsAttributesMap(final Map<String, String> specsAttributesMap)
	{
		this.specsAttributesMap = specsAttributesMap;
	}
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
	protected ClassificationService getClassificationService()
	{
		return classificationService;
	}
	@Required
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}
	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}
	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}
	protected MessageSource getMessageSource()
	{
		return messageSource;
	}
	@Required
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
	protected ModelService getModelService()
	{
		return modelService;
	}
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
	public GPClassificationAttributeAssignmentService getGpClassificationAttributeAssignmentService()
	{
		return gpClassificationAttributeAssignmentService;
	}
	public void setGpClassificationAttributeAssignmentService(
			final GPClassificationAttributeAssignmentService gpClassificationAttributeAssignmentService)
	{
		this.gpClassificationAttributeAssignmentService = gpClassificationAttributeAssignmentService;
	}
}
