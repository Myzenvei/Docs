/**
 *
 */
package com.gp.commerce.facades.search.compare.impl;

import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPCommerceDataException;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.facades.search.compare.GPSearchCompareProductsFacade;
import com.gp.commerce.facades.search.compare.data.CompareAttributesData;
import com.gp.commerce.facades.search.compare.data.CompareSpecificationsData;
import com.gp.commerce.facades.search.compare.data.ComparisonData;

/**
 * Default implementation of {@link GPSearchCompareProductsFacade}.
 *
 */
public class GPDefaultSearchCompareProductsFacade implements GPSearchCompareProductsFacade
{

	private static final Logger LOG = Logger.getLogger(GPDefaultSearchCompareProductsFacade.class);
	private ProductFacade productFacade;

	private GPProductService gpProductService;

	private ConfigurationService configurationService;

	private static final String NEW_LINE = "<br/>";
	private static final String HYPHEN = "-";
	private static final String COLON = ":";
	private static final String DELIMETER = ",";
	private static final String NULL = "NULL";
	private static final String DEFAULTPOSITION = "50";

	/**
	 * Gets the products and its classification comparison
	 *
	 * @param productCodes
	 *           string of product codes delimited with :
	 * @return ComparisonData the comparison of product classification attribute
	 */
	@Override
	public ComparisonData compare(final String productCodes,final String pageType) throws GPCommerceDataException
	{
		final List<ProductOption> options = new ArrayList<>(Arrays.asList(ProductOption.BASIC,
				ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.CLASSIFICATION, ProductOption.STOCK,ProductOption.VARIANT_FULL));
		final List<ProductData> productsList = new ArrayList<ProductData>();

		final List<String> productCodesList = getProductCodesList(productCodes);

		List<String> masterList = new ArrayList<>();

		final int maxProducts = Integer.parseInt(configurationService.getConfiguration().getString("search.compare.maxProducts"));

		// case when productcodes count is more than allowable limit throw exception
		if(productCodesList.size() > maxProducts)
		{
			throw new GPCommerceDataException("1301","Invalid request: exceeded max number of products allowed for comparison");
		}// case when productcodes count is less than 2 throw exception
		else if (productCodesList.size() < 2)
		{
			throw new GPCommerceDataException("1302","The product codes are either incorrect or missing");
		}

		String assetCode = null;

		for (final String productCode : productCodesList)
		{
			final ProductData compareProd = productFacade.getProductForCodeAndOptions(productCode, options);
			productsList.add(compareProd);
			if(assetCode == null)
			{
			assetCode= compareProd.getAssetCode();
			}
			else if(!assetCode.equals(compareProd.getAssetCode()))
			{
				throw new GPCommerceDataException("1303","The access code for products under comparision is not same");
			}
		}

		masterList = getAttributeListFOrDisplay(assetCode, pageType);

		return compareProducts(productsList,masterList);
	}

	/**
	 * This method gets Attribute list for display
	 * @param assetCode
	 * 			the asset code
	 * @param pageType
	 * 			the page type
	 * @return attribute list for display
	 */
	public List<String> getAttributeListFOrDisplay(final String assetCode, final String pageType)
	{
		return gpProductService.getAttributesOfProduct(assetCode,pageType);
	}

	/**
	 * Builds the ComparisonData object, from the ProductData objects
	 *
	 * @param productsList
	 *
	 * @return ComparisonData the comparison of product classification attribute
	 */
	private ComparisonData compareProducts(final List<ProductData> productsList,final List<String> masterList)
	{

		final ComparisonData comparisonData = new ComparisonData();

		comparisonData.setProducts(productsList);

		comparisonData.setSpecifications(getSpecifications(productsList,masterList));


		return comparisonData;
	}

	/**
	 * This method gets specifications of products
	 * @param product
	 * 		the product
	 * @return list of specifications of products
	 */
	public List<CompareSpecificationsData> getSpecifications(final ProductData product)
	{
		final List<ProductData> productList = new ArrayList<>();
		productList.add(product);
		return getSpecifications(productList,getAttributeListFOrDisplay(product.getAssetCode(), configurationService.getConfiguration().getString("product.detail.pagetype")));
	}

	/**
	 *
	 * Retrieves the product classifications and builds the response with the list of features for each classification
	 *
	 * @param productsList
	 *
	 * @return List<CompareSpecificationsData> the comparison of product classification attribute
	 */
	private List<CompareSpecificationsData> getSpecifications(final List<ProductData> productsList,final List<String> masterList)
	{
		final Map<String,Map<String,String[]>> classificaionMap = getClassificationMap(productsList,masterList);
		final List<CompareSpecificationsData> specificationList = new ArrayList<CompareSpecificationsData>();
		FeatureList featureList = null;
		if (!CollectionUtils.isEmpty(productsList))
		{
			featureList = gpProductService.getFeaturesForProduct(productsList.get(0).getCode());
		}
		final Iterator<String> classifications = classificaionMap.keySet().iterator();
		while (classifications.hasNext())
		{
			final List<CompareAttributesData> attributesList = new ArrayList<CompareAttributesData>();
			final String classificationName = classifications.next();
			final CompareSpecificationsData classificationObject = new CompareSpecificationsData();
			classificationObject.setCategoryLabel(classificationName.substring(0, classificationName.indexOf(DELIMETER)));
			classificationObject.setCategoryCode(classificationName.substring(classificationName.indexOf(DELIMETER) + 1));
			populateAttributes(classificaionMap, classificationName, attributesList, featureList);
			Collections.sort(attributesList, new Comparator<CompareAttributesData>()
			{

				public int compare(final CompareAttributesData comAttrData1, final CompareAttributesData comAttrData2)
				{
					return Integer.valueOf(comAttrData1.getAttributePosition())
							.compareTo(Integer.valueOf(comAttrData2.getAttributePosition()));
				}
			});
			classificationObject.setSubCategories(attributesList);
			specificationList.add(classificationObject);
		}
		Collections.sort(specificationList, new Comparator<CompareSpecificationsData>() {
			@Override
			public int compare(final CompareSpecificationsData code1, final CompareSpecificationsData code2) {
				return code1.getCategoryCode().compareTo(code2.getCategoryCode());
			}
		});
		return specificationList;
	}


	/**
	 * Retrieves the classification features and builds the response with the list of attributes for each feature
	 */
	private void populateAttributes(final Map<String, Map<String, String[]>> classificaionMap, final String classificationName,
			final List<CompareAttributesData> attributesList, final FeatureList featureList)
	{
		final Map<String,String[]> featureMap = classificaionMap.get(classificationName);
		final Iterator<String> features = featureMap.keySet().iterator();
		while (features.hasNext())
		{
			final String featureName = features.next();
			final CompareAttributesData featureObject = new CompareAttributesData();
			attributesList.add(featureObject);
			featureObject.setAttributeLabel(featureName);
			featureObject.setAttributePosition(setAttributePosition(featureList, featureName));
			final List<String> columnValues = new ArrayList<String>();
			featureObject.setColumnValues(columnValues);
			final String[] featureArray = featureMap.get(featureName);

			for (final String featureValueData:featureArray)
			{
				if (featureValueData != null)
				{
					columnValues.add(featureValueData);
				}
				else
				{
					columnValues.add(NULL);
				}
			}

		}
	}

	private String setAttributePosition(final FeatureList featureList, final String featureName)
	{
		if (featureList != null && featureName != null)
		{
			for (final Feature feature : featureList.getFeatures())
			{
				final String systemId = feature.getClassAttributeAssignment().getClassificationClass().getCatalogVersion()
						.getCatalog().getId();
				if (featureName.equalsIgnoreCase(feature.getName())
						&& systemId.equalsIgnoreCase(GpcommerceCoreConstants.GP_US_CLASSIFICATION_CODE))
				{
					return feature.getClassAttributeAssignment().getPosition() != null
							? feature.getClassAttributeAssignment().getPosition().toString() : DEFAULTPOSITION;
				}
			}
		}
		return DEFAULTPOSITION;

	}

	/**
	 * List the classifications from the ProductData objects for comparison
	 */
	private Map<String,Map<String,String[]>> getClassificationMap(final List<ProductData> productList,final List<String> masterList)
	{
		final Map<String,Map<String,String[]>> classificaionMap = new HashMap<String,Map<String,String[]>>();
		int column = 0;

		for (final ProductData product : productList)
		{
			if(product.getClassifications()!=null && !product.getClassifications().isEmpty())
			{
				final Iterator<ClassificationData> itr = product.getClassifications().iterator();
				while (itr.hasNext())
				{
					final ClassificationData classification = itr.next();
					if(classification.getFeatures()!=null && !classification.getFeatures().isEmpty())
					{
						populateFeatureMap(classificaionMap, classification, column, productList.size(),masterList);
					}
				}
				column++;
			}
		}
		return classificaionMap;
	}

	/**
	 * List the features with in the classifications from the ClassificationData objects for comparison
	 */
	private void populateFeatureMap(final Map<String, Map<String, String[]>> classificaionMap,
			final ClassificationData classification, final int column, final int productsListSize,final List<String> masterList)
	{
		final String classificationName = classification.getName()+","+classification.getCode();
		Map<String,String[]> featureMap = classificaionMap.get(classificationName);
		if(featureMap == null) {
			featureMap = new HashMap<String,String[]>();
		}
		final Iterator<FeatureData> featureItr = classification.getFeatures().iterator();
		while (featureItr.hasNext())
		{
			final FeatureData feature = featureItr.next();
			try
			{
				final String attributeAndClass = feature.getCode().toLowerCase().substring(feature.getCode().toLowerCase().indexOf(feature.getName().toLowerCase()));
				if(feature!=null && feature.getFeatureValues() !=null && !(masterList.indexOf(attributeAndClass.toString())== -1))
				{
					populateFeatureArray(featureMap, feature, column, productsListSize);
				}
			}
			catch(final StringIndexOutOfBoundsException e)
			{
				final String errorMessage = "Class attribute" + feature.getCode() +
						" for feature name " + feature.getName() + " does not match \n" + e.getMessage();
				LOG.error(errorMessage,e);
			}
		}
		if (featureMap.size()>0)
		{
			classificaionMap.put(classificationName, featureMap);
		}
	}

	/**
	 * List the attributes for each feature from the FeatureData objects for comparison
	 */
	private void populateFeatureArray(final Map<String, String[]> featureMap, final FeatureData feature, final int column, final int productsListSize)
	{
		if (featureMap.get(feature.getName()) == null)
		{
			featureMap.put(feature.getName(), new String[productsListSize]);
		}

		final String[] featureArray = featureMap.get(feature.getName());
		final Iterator<FeatureValueData> featureValuesItr = feature.getFeatureValues().iterator();
		final StringBuilder featureValue = new StringBuilder();
		final boolean isRange = feature.isRange();
		int i=0;

		//form the final feature value string
		while(featureValuesItr.hasNext())
		{
			final FeatureValueData featureValueData = featureValuesItr.next();
			featureValue.append(featureValueData.getValue());
			if (!isRange && feature.getFeatureUnit()!=null)
			{
				featureValue.append(feature.getFeatureUnit().getSymbol());
			}
			if (i < feature.getFeatureValues().size() - 1) //if not last element
			{
				if(isRange)
				{
					featureValue.append(HYPHEN);
				}
				else
				{
					featureValue.append(NEW_LINE);
				}
			}
			i++;
		}
		if(isRange && feature.getFeatureUnit()!=null)
		{
			featureValue.append(feature.getFeatureUnit().getSymbol());
		}

		featureArray[column] = featureValue.toString();
	}

	/**
	 * Filtering the valid product codes before service call
	 */
	private List<String> getProductCodesList(final String productCodes)
	{
		final String[] productCodesArray = productCodes.split(COLON);
		final List<String> productCodesList = new ArrayList<String>();
		for(final String productCode : productCodesArray)
		{
			if(!productCode.trim().isEmpty())
			{
				productCodesList.add(productCode);
			}
		}
		return productCodesList;
	}

	public void setProductFacade(final ProductFacade productFacade) {
		this.productFacade = productFacade;
	}

	public ProductFacade getProductFacade() {
		return productFacade;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public GPProductService getGpProductService() {
		return gpProductService;
	}
	public void setGpProductService(final GPProductService gpProductService) {
		this.gpProductService = gpProductService;
	}
}

