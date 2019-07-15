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
package com.gp.commerce.initialdata.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.dataimport.impl.GPDeltaCoreDataImportService;
import com.gp.commerce.core.dataimport.impl.GPDeltaSampleDataImportService;
import com.gp.commerce.core.setup.GPAbstractSystemSetup;
import com.gp.commerce.initialdata.constants.GpcommerceInitialDataConstants;



/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = GpcommerceInitialDataConstants.EXTENSIONNAME)
public class InitialDataSystemSetup extends GPAbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(InitialDataSystemSetup.class);

	public static final String GP_US = "gpUS";
	public static final String VANITY_FAIR_NAPKINS = "vanityfairnapkins";
	public static final String GP_EMPLOYEE = "gpemployee";
	public static final String SPARKLE = "sparkle";
	public static final String MARDIGRAS = "mardigras";
	public static final String B2CWHITELABEL = "b2cwhitelabel";
	public static final String COPPERNCRANE = "copperandcrane";
	public static final String BRAWNY = "brawny";


	public static final String INNOVIA = "innovia";
	protected static final String BOOLEAN_TRUE = "yes";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	private static final String SELECT_STORES = "stores";

	public static final String IMPORT_ESSENTIAL_DATA = "importEssentialData";


	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;
	private ConfigurationService configurationService;
	private GPDeltaSampleDataImportService gpDeltaSampleDataImportService;
	private GPDeltaCoreDataImportService gpDeltaCoreDataImportService;



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
		params.add(createBooleanSystemSetupParameter(IMPORT_ESSENTIAL_DATA, "Import Essential Country Data", true));
		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during
	 * initialization and system update. Be sure that this method can be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/gpcommerceinitialdata/import/coredata/common/Oauth2.impex");
		importImpexFile(context, "/gpcommerceinitialdata/import/coredata/common/essential-data-colorcodes.impex");
		// Add Essential Data here as you require
	}



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
					.get((GpcommerceInitialDataConstants.EXTENSIONNAME + "_" + SELECT_STORES));
			storesList.addAll(Arrays.asList(stores != null ? stores : new String[0]));
		}


		final List<ImportData> importData = new ArrayList<>();
		final ImportData gpImportData = new ImportData();
		gpImportData.setProductCatalogName(GP_US);
		gpImportData.setContentCatalogNames(storesList);
		gpImportData.setStoreNames(storesList);
		importData.add(gpImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));


		getSampleDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		if (getBooleanSystemSetupParameter(context, IMPORT_ESSENTIAL_DATA))
		{
			getGpDeltaCoreDataImportService().execute(this, context, importData);
			getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

			getGpDeltaSampleDataImportService().execute(this, context, importData);
		}


		importStoreSampleData(context, gpImportData.getStoreNames(), gpImportData.getProductCatalogName());
		readAnyAdditionalFiles(context);
		readDatahubFiles(context);
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public GPDeltaSampleDataImportService getGpDeltaSampleDataImportService() {
		return gpDeltaSampleDataImportService;
	}

	public void setGpDeltaSampleDataImportService(final GPDeltaSampleDataImportService gpDeltaSampleDataImportService) {
		this.gpDeltaSampleDataImportService = gpDeltaSampleDataImportService;
	}

	public GPDeltaCoreDataImportService getGpDeltaCoreDataImportService() {
		return gpDeltaCoreDataImportService;
	}

	public void setGpDeltaCoreDataImportService(final GPDeltaCoreDataImportService gpDeltaCoreDataImportService) {
		this.gpDeltaCoreDataImportService = gpDeltaCoreDataImportService;
	}

	/**
	 * This method will run any impexes in additional folder. The file names will be coming from additionalImpexFiles
	 * property.This method will be called by system creator during initialization and system update. Be sure that this
	 * method can be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	public void readAnyAdditionalFiles(final SystemSetupContext context)
	{
		final String listOfFiles = configurationService.getConfiguration().getString("additionalImpexFiles");
		if (listOfFiles.trim().length() > 0)
		{
			final String[] fileNames = listOfFiles.split(":");
			for (final String fileName : fileNames)
			{
				importImpexFile(context, "/gpcommerceinitialdata/import/sampleData/additional/" + fileName);
			}
		}
	}

	/**
	 * This method will run impexes in datahub folder.This method will be called by system creator during initialization
	 * and system update.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	public void readDatahubFiles(final SystemSetupContext context)
	{

		importImpexFile(context, "/gpcommerceinitialdata/import/sampleData/datahub/projectdata_datahub.impex");
		importImpexFile(context, "/gpcommerceinitialdata/import/sampleData/datahub/customizing.impex");

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
			importImpexFile(context,
					String.format("/gpcommerceinitialdata/import/sampledata/stores/%s/gpAttributeConfig.impex", storeName));
			importImpexFile(context,
					String.format("/gpcommerceinitialdata/import/sampledata/stores/%s/site2product-relations.impex", storeName));
			importImpexFile(context,
					String.format("/gpcommerceinitialdata/import/sampledata/stores/%s/marketingpreference.impex", storeName));
		}
	}

	/**
	 * Get stores
	 *
	 * @return List<String>
	 */
	private List<String> getStores()
	{
		final List<String> stores = new ArrayList<>();
		stores.add(VANITY_FAIR_NAPKINS);
		stores.add(GP_EMPLOYEE);
		stores.add(MARDIGRAS);
		stores.add(B2CWHITELABEL);
		stores.add(COPPERNCRANE);
		stores.add(INNOVIA);
		stores.add(BRAWNY);
		stores.add(SPARKLE);
		return stores;
	}
}
