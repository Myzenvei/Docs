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
package com.gp.b2binitialdata.setup;

import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.gp.b2binitialdata.constants.Gpb2binitialdataConstants;
import com.gp.b2binitialdata.dataimport.impl.GpB2BCoreDataImportService;
import com.gp.b2binitialdata.dataimport.impl.GpB2BSampleDataImportService;
import com.gp.commerce.core.setup.GPAbstractSystemSetup;




/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = Gpb2binitialdataConstants.EXTENSIONNAME)
public class GpB2BInitialDataSystemSetup extends GPAbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GpB2BInitialDataSystemSetup.class);

	public static final String GP_US = "gpUS";
	public static final String GP_PRO = "gppro";
	public static final String DIXIE = "dixie";
	public static final String B2B_WHITELABEL = "b2bwhitelabel";
	public static final String GPXRESS = "gpxpress";

	protected static final String BOOLEAN_TRUE = "yes";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	private static final String SELECT_STORES = "stores";

	private GpB2BCoreDataImportService gpb2bCoreDataImportService;
	private GpB2BSampleDataImportService gpb2bSampleDataImportService;

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();
		params.add(createMultiSelectSystemSetupParameter(SELECT_STORES, "Select Multiple Stores for Data Import", getStores()));
		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		// Add more Parameters here as you require

		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during initialization
	 * and system update. Be sure that this method can be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		// Add Essential Data here as you require
		
	}

	/**
	 * This method will be called during the system initialization. <br>
	 * Add import data for each site you have configured
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<String> storesList = new ArrayList<>();
		if (context.getProcess().isInit())
		{
			storesList.addAll(getStores());
		}
		else
		{
			final String[] stores = context.getParameterMap()
					.get((Gpb2binitialdataConstants.EXTENSIONNAME + "_" + SELECT_STORES));
			storesList.addAll(Arrays.asList(stores != null ? stores : new String[0]));
		}

		final List<ImportData> importData = new ArrayList<>();
		final ImportData gpb2bImportData = new ImportData();
		gpb2bImportData.setProductCatalogName(GP_US);
		gpb2bImportData.setContentCatalogNames(storesList);
		gpb2bImportData.setStoreNames(storesList);
		importData.add(gpb2bImportData);

		getGpb2bCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
		

		getGpb2bSampleDataImportService().execute(this, context, importData);

		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		importStoreSampleData(context, gpb2bImportData.getStoreNames(), gpb2bImportData.getProductCatalogName());

		getGpb2bSampleDataImportService().importCommerceOrgData(context);
		
		getGpb2bSampleDataImportService().importMultiCountryData(context);
		
		importImpexFile(context, "/gpb2binitialdata/import/sampledata/multicountry/cronjobs.impex", true);
		importImpexFile(context, "/gpb2binitialdata/import/coredata/common/essentialdata_bazaarvoice.impex");
	}


	/**
	 * @return the gpb2bCoreDataImportService
	 */
	public GpB2BCoreDataImportService getGpb2bCoreDataImportService()
	{
		return gpb2bCoreDataImportService;
	}

	/**
	 * @param gpb2bCoreDataImportService
	 *           the gpb2bCoreDataImportService to set
	 */
	public void setGpb2bCoreDataImportService(final GpB2BCoreDataImportService gpb2bCoreDataImportService)
	{
		this.gpb2bCoreDataImportService = gpb2bCoreDataImportService;
	}

	/**
	 * @return the gpb2bSampleDataImportService
	 */
	public GpB2BSampleDataImportService getGpb2bSampleDataImportService()
	{
		return gpb2bSampleDataImportService;
	}

	/**
	 * @param gpb2bSampleDataImportService
	 *           the gpb2bSampleDataImportService to set
	 */
	public void setGpb2bSampleDataImportService(final GpB2BSampleDataImportService gpb2bSampleDataImportService)
	{
		this.gpb2bSampleDataImportService = gpb2bSampleDataImportService;
	}

	/**
	 * Import store sample data.
	 *
	 * @param context the context
	 * @param storeNames the store names
	 * @param productCatalog the product catalog
	 */
	protected void importStoreSampleData(final SystemSetupContext context, final List<String> storeNames, final String productCatalog)
	{
		for (final String storeName : storeNames)
		{
			importImpexFile(context, String.format("/gpb2binitialdata/import/sampledata/stores/%s/gpAttributeConfig.impex", storeName));
			importImpexFile(context,
					String.format("/gpb2binitialdata/import/sampledata/stores/%s/site2product-relations.impex", storeName));
			importImpexFile(context, String.format("/gpb2binitialdata/import/sampledata/stores/%s/marketingpreference.impex", storeName));
		}
	}

	/**
	 * Get Stores
	 *
	 * @return List<String>
	 */
	private List<String> getStores()
	{
		final List<String> stores = new ArrayList<>();
		stores.add(GP_PRO);
		stores.add(DIXIE);
		stores.add(B2B_WHITELABEL);
		stores.add(GPXRESS);
		return stores;
	}
}
