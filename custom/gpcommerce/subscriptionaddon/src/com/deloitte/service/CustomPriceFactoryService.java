/**
 *
 */
package com.deloitte.service;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;

import java.util.List;


/**
 * @author asomjal
 *
 */
public interface CustomPriceFactoryService
{
	List<PriceData> getAllPricesForProduct(final String productCode);

	List<PriceRow> getPriceInformationsForProduct(final ProductModel productModel);
}
