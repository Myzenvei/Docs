/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dataimport.impl;

import java.io.InputStream;
import java.util.List;

import de.hybris.platform.commerceservices.dataimport.AbstractDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.validation.services.ValidationService;

/**
 * This class provides services to process Data import services
 */
public class GPDeltaCoreDataImportService extends AbstractDataImportService {
	public static final String IMPORT_ESSENTIAL_DATA = "importEssentialData";

	@Override
	public void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context, final List<ImportData> importData)
	{
		final boolean importCoreData = systemSetup.getBooleanSystemSetupParameter(context, IMPORT_ESSENTIAL_DATA);

		if (importCoreData)
		{
			for (final ImportData data : importData)
			{
				importAllData(systemSetup, context, data, false);
			}

			final ValidationService validation = getBeanForName("validationService");
			validation.reloadValidationEngine();
		}
	}

	@Override
	protected void importCommonData(final String extensionName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/updatedata/R2/essentialdata/common/essential-data.impex", extensionName),
				true);
		getSetupImpexService().importImpexFile(String.format("/%s/import/updatedata/R2/essentialdata/common/countries.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/updatedata/R2/essentialdata/common/delivery-modes.impex", extensionName),
				false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/updatedata/R2/essentialdata/common/themes.impex", extensionName), false);
	}

	@Override
	protected void importProductCatalog(final String extensionName, final String productCatalogName)
	{
		getSetupImpexService()
				.importImpexFile(
						String.format("/%s/import/updatedata/R2/essentialdata/productCatalogs/%sProductCatalog/catalog.impex", extensionName,
								productCatalogName), false);
	}

	@Override
	protected void importContentCatalog(final String extensionName, final String contentCatalogName)
	{
		getSetupImpexService()
				.importImpexFile(
						String.format("/%s/import/updatedata/R2/essentialdata/contentCatalogs/%sContentCatalog/catalog.impex", extensionName,
								contentCatalogName), false);

		if (isResponsive())
		{
			final String responsiveContentFile = String.format(
					"/%s/import/updatedata/R2/essentialdata/contentCatalogs/%sContentCatalog/cms-responsive-content.impex", extensionName,
					contentCatalogName);
			if (getInputStream(responsiveContentFile) != null)
			{
				getSetupImpexService().importImpexFile(responsiveContentFile, false);
			}
			else
			{
				getSetupImpexService().importImpexFile(
						String.format("/%s/import/updatedata/R2/essentialdata/contentCatalogs/%sContentCatalog/cms-content.impex", extensionName,
								contentCatalogName), false);
			}
		}
		else
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/updatedata/R2/essentialdata/contentCatalogs/%sContentCatalog/cms-content.impex", extensionName,
							contentCatalogName), false);

			if (getConfigurationService().getConfiguration().getBoolean(IMPORT_MOBILE_DATA, false))
			{
				getSetupImpexService().importImpexFile(
						String.format("/%s/import/updatedata/R2/essentialdata/contentCatalogs/%sContentCatalog/cms-mobile-content.impex", extensionName,
								contentCatalogName), false);
			}
		}

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/updatedata/R2/essentialdata/contentCatalogs/%sContentCatalog/email-content.impex", extensionName,
						contentCatalogName), false);
	}

	@Override
	protected void importStore(final String extensionName, final String storeName, final String productCatalogName)
	{
		final String responsiveStoreFile = String.format("/%s/import/updatedata/R2/essentialdata/stores/%s/store-responsive.impex", extensionName,
				storeName);
		final String responsiveSiteFile = String.format("/%s/import/updatedata/R2/essentialdata/stores/%s/site-responsive.impex", extensionName,
				storeName);

		if (isResponsive() && getInputStream(responsiveStoreFile) != null)
		{
			getSetupImpexService().importImpexFile(responsiveStoreFile, false);
		}
		else
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/updatedata/R2/essentialdata/stores/%s/store.impex", extensionName, storeName), false);
		}

		if (isResponsive() && getInputStream(responsiveSiteFile) != null)
		{
			getSetupImpexService().importImpexFile(responsiveSiteFile, false);
		}
		else
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/updatedata/R2/essentialdata/stores/%s/site.impex", extensionName, storeName), false);
		}
	}

	@Override
	protected void importSolrIndex(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/updatedata/R2/essentialdata/stores/%s/solr.impex", extensionName, storeName),
				false);

		getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/updatedata/R2/essentialdata/stores/%s/solrtrigger.impex", extensionName, storeName), false);
	}

	@Override
	protected void importJobs(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/updatedata/R2/essentialdata/stores/%s/jobs.impex", extensionName, storeName),
				false);
	}

	protected InputStream getInputStream(final String fileName)
	{
		return getClass().getResourceAsStream(fileName);
	}

	protected boolean isResponsive()
	{
		return ResponsiveUtils.isResponsive();
	}
}
