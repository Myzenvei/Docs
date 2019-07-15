package com.bazaarvoice.hybris.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;


/**
 * This interface is to provide services for product ratings
 */
public interface GPBazaarVoiceRatingImportService
{

	/**
	 * @param filePath
	 * @param catalogVersion
	 * @throws JAXBException
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public void populateProductRatings(final String filePath,final CatalogVersionModel catalogVersion) throws JAXBException, XMLStreamException, IOException;

}
