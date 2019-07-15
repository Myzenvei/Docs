package com.bazaarvoice.hybris.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.bazaarvoice.gp.feed.models.FeedType;
import com.bazaarvoice.gp.feed.models.ProductType;
import com.bazaarvoice.gp.feed.models.ReviewStatisticsType;
import com.bazaarvoice.hybris.constants.BazaarvoiceConstants;
import com.bazaarvoice.hybris.service.GPBazaarVoiceRatingImportService;


/**
 * Service to update Product with review and ratings data
 */
public class DefaultGPBazaarVoiceRatingImportService implements GPBazaarVoiceRatingImportService
{

	private static final Logger LOG = Logger.getLogger(DefaultGPBazaarVoiceRatingImportService.class.getName());
	private static final String PROJECT_ROOT_PATH = Utilities.getExtensionInfo(BazaarvoiceConstants.EXTENSIONNAME)
			.getExtensionDirectory().getAbsolutePath();
	private static final String DECOMPRESSED_FILE_PATH = PROJECT_ROOT_PATH
			+ Config.getParameter(BazaarvoiceConstants.LOCAL_XML_BAZAAR_DECOMPRESSED_DIRECTORY);

	private static final int DECIMALROUNDOFFFDIGIT = 1;

	private ProductService productService;
	private ModelService modelService;

	@Override
	public void populateProductRatings(final String filePath, final CatalogVersionModel catalogVersion)
			throws JAXBException, XMLStreamException, IOException {

		LOG.info("Inside BazaarVoiceRatingImportServiceImpl ---- To Update Product with Reviews and Ratings data");
		final InputStream fileInputStream = new FileInputStream(filePath);
		final FeedType productFeed = unmarshalFeed(fileInputStream);
		populateProductRatings(productFeed, catalogVersion);
	}

	protected void populateProductRatings(final FeedType productFeed, final CatalogVersionModel catalogVersion)
	{
		if (CollectionUtils.isEmpty(productFeed.getProduct()))
		{
			return;
		}

		for (final ProductType product : productFeed.getProduct())
		{
			final ReviewStatisticsType reviewStatistics = product.getReviewStatistics();

			final Float avgOverallRating = reviewStatistics.getAverageOverallRating();
			final Integer numberOfReviews = reviewStatistics.getTotalReviewCount();
			final Integer overallRatingRange = reviewStatistics.getOverallRatingRange();
			final Integer ratingsOnlyReviewCount = reviewStatistics.getRatingsOnlyReviewCount();
			final Integer recommendedCount = reviewStatistics.getRecommendedCount();
			try
			{
				final String bvProductCode = product.getId();
				String actualProductCode = null;
				if (bvProductCode.contains("_"))
				{
					actualProductCode = bvProductCode.replace("_", "/");
				}
				else
				{
					actualProductCode = bvProductCode;
				}
				final ProductModel productModel = productService.getProductForCode(catalogVersion, actualProductCode);

				if (productModel != null)
				{
					if (avgOverallRating != null)
					{
						BigDecimal avgRating = new BigDecimal(avgOverallRating.toString());
						avgRating = avgRating.setScale(DECIMALROUNDOFFFDIGIT, BigDecimal.ROUND_HALF_UP);
						productModel.setBvAverageRating(Double.valueOf(String.valueOf(avgRating)));
					}
					productModel.setBvNumberOfReviews(numberOfReviews);
					productModel.setOverallRatingRange(overallRatingRange);
					productModel.setRatingsOnlyReviewCount(ratingsOnlyReviewCount);
					productModel.setRecommendedCount(recommendedCount);
					modelService.save(productModel);
				}
			}

			catch (final Exception e)
			{
				LOG.error("Error updating Product with ID " + product.getId() + " with Review and Ratings Data");
			}

		}
	}

	protected FeedType unmarshalFeed(final InputStream feed) throws JAXBException, XMLStreamException, IOException
	{
		final JAXBContext jaxbContext = JAXBContext.newInstance(FeedType.class);
		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		final XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
		xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		JAXBElement<FeedType> feedElement = null;
		XMLStreamReader xmlStreamReader = null;
		try {
			xmlStreamReader = xmlInputFactory.createXMLStreamReader(feed);

			feedElement = unmarshaller.unmarshal(xmlStreamReader, FeedType.class);
		} finally {
			if (xmlStreamReader != null) {
				try {
					xmlStreamReader.close();
				} catch (final XMLStreamException e) {
					throw e;
				}
			}
		}
		return feedElement.getValue();
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(final ProductService productService) {
		this.productService = productService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

}
