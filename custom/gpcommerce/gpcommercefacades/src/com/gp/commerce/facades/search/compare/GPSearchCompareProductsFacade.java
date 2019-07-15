/**
 *
 */
package com.gp.commerce.facades.search.compare;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;

import com.gp.commerce.core.exceptions.GPCommerceDataException;
import com.gp.commerce.facades.search.compare.data.CompareSpecificationsData;
import com.gp.commerce.facades.search.compare.data.ComparisonData;


/**
 * SearchCompareProducts facade interface. Its main purpose is to compare product classifications using productservice.
 */
public interface GPSearchCompareProductsFacade
{
	/**
	 * Gets the products and its classification comparison
	 *
	 * @param productCodes
	 *           string of product codes delimited with :
	 * @param pageType
	 * 			 the page type
	 * @return the {@link ComparisonData}
	 * @throws GPCommerceDataException
	 *            when either the product codes are incorrect or missing
	 */
	ComparisonData compare(String productCodes, String pageType) throws GPCommerceDataException;

	/**
	 * Retrieves the product classifications and builds the response with the list of features for each classification.
	 *
	 * @param product the product
	 * @return the specifications
	 */
	List<CompareSpecificationsData> getSpecifications(final ProductData product);
}
