/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.b2binitialdata.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.core.initialization.SystemSetupContext;


/**
 * Implementation to handle specific Sample Data Import services to GP B2B site.
 */
public class GpB2BSampleDataImportService extends SampleDataImportService
{

	/**
	 * Imports the data related to Commerce Org.
	 *
	 * @param context
	 *           the context used.
	 */
	public void importCommerceOrgData(final SystemSetupContext context)
	{
		final String extensionName = context.getExtensionName();

		getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/commerceorg/user-groups.impex", extensionName),
				false);
	}
	
	/**
	 * Imports data related to countries.
	 * 
	 * @param context 
	 * 			 the context used.
	 */
	public void importMultiCountryData(final SystemSetupContext context)
	{
		final String extensionName = context.getExtensionName();
		
		getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/multicountry/sampledata_solr.impex", extensionName),
				false);

		getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/multicountry/sampledata_contextualattributevalues.impex", extensionName),
				false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/multicountry/sampledata_priceandavailability.impex", extensionName),
				false);
		
		
	}

}
