package de.hybris.platform.multicountry.setup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;


@SystemSetup(extension = "multicountry")
public class SampleDataSystemSetup extends AbstractSystemSetup
{
	public static final String IMPORT_MULTICOUNTRY_DATA = "importMultiCountryData";
	public static final String IMPORT_MULTICOUNTRY_SEARCH_RESTRICTION = "importMultiCountrySearchRestrictions";
	public static final String IMPORT_ESSENTIAL_DATA = "importEssentialData";
	static final Logger LOG = Logger.getLogger(SampleDataSystemSetup.class);

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_MULTICOUNTRY_DATA, "Import Multi Country Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_MULTICOUNTRY_SEARCH_RESTRICTION,
				"Import Multi Country Search Restrictions", false));
		params.add(createBooleanSystemSetupParameter(IMPORT_ESSENTIAL_DATA, "Import Essential Country Data", true));

		return params;
	}

	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/multicountry/impex/essentialdata-Promotions.impex", false);
		importImpexFile(context, "/multicountry/impex/essentialdata-SearchRestrictions.impex", false);
		
	}

	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		if (getBooleanSystemSetupParameter(context, IMPORT_MULTICOUNTRY_DATA))
		{
			logInfo(context, "Importing Multi country data...");
			
			importImpexFile(context, "/multicountry/impex/essentialdata_multicountry.impex", true);
			importImpexFile(context, "/multicountry/impex/essentialdata-Promotions.impex", true);
			//importImpexFile(context, "/multicountry/impex/sampledata_multibrand.impex", true);
			//importImpexFile(context, "/multicountry/impex/sampledata_solr.impex", true);
			importImpexFile(context, "/multicountry/impex/essentialdata_multicountry.impex", true);
			//importImpexFile(context, "/multicountry/impex/sampledata_contextualattributevalues.impex", true);
			//importImpexFile(context, "/multicountry/impex/sampledata_priceandavailability.impex", true);
			
		}

		if (getBooleanSystemSetupParameter(context, IMPORT_MULTICOUNTRY_SEARCH_RESTRICTION))
		{
			importImpexFile(context, "/multicountry/impex/essentialdata-SearchRestrictions.impex", true);
		}
		
		
		if (getBooleanSystemSetupParameter(context, IMPORT_ESSENTIAL_DATA))
		{
			logInfo(context, "Importing Essential data...");
			
			File folder = new File("/Users/you/folder/");
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        System.out.println(file.getName());
			    }
			}
		}
	}
}
