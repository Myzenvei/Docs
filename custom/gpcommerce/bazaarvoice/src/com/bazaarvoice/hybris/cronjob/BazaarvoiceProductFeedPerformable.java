package com.bazaarvoice.hybris.cronjob;
import com.bazaarvoice.hybris.constants.BazaarvoiceConstants;
import com.bazaarvoice.hybris.exception.BazaarvoiceProductFeedExportException;
import com.bazaarvoice.hybris.model.BazaarvoiceConfigModel;
import com.bazaarvoice.hybris.model.BazaarvoiceProductFeedExportCronJobModel;
import com.bazaarvoice.hybris.xml.ProductFeedXmlCreator;
import com.bazaarvoice.hybris.xml.tags.ProductFeed;
import com.bazaarvoice.hybris.xml.tags.ProductTag;
import com.gp.commerce.core.product.services.GPProductService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.GZIPOutputStream;


/**
 * @author Christina Romashchenko, Artem Labunko
 */

@Component("bazaarvoiceProductFeedExporter")
@Scope("prototype")
public class BazaarvoiceProductFeedPerformable extends AbstractJobPerformable<BazaarvoiceProductFeedExportCronJobModel>
{

	private static final String PRODUCT_FEED_XMLNS = "http://www.bazaarvoice.com/xs/PRR/ProductFeed/5.6";
	private static final int PRODUCT_MAX_NUMBER = 75000;

	@Resource(name = "catalogService")
	private CatalogService catalogService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "productFeedXmlCreator")
	private ProductFeedXmlCreator productFeedXmlCreator;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource
	private I18NService i18NService;

	@Resource(name = "gpProductService")
	private GPProductService gpProductService;


	private static final Logger LOG = Logger.getLogger(BazaarvoiceProductFeedPerformable.class);

	private List<CategoryModel> brands;

	private List<ProductModel> products;

	private List<CategoryModel> categories;

	private Map<Locale, String> locales;

	private static final String PROJECT_ROOT_PATH = Utilities.getExtensionInfo(BazaarvoiceConstants.EXTENSIONNAME)
			.getExtensionDirectory().getAbsolutePath()
			+ File.separator + "resources" + File.separator + "feed";


	private CMSSiteModel cmsSiteModel;
	private BazaarvoiceConfigModel bazaarvoiceConfig;
	private CatalogModel catalog;
	private String ftpServerDirectory;
	private String ftpServerPort;
	private String xmlBaseFileName;
	private String localXmlOutputDirectory;
	private String localXmlArchiveDirectory;
	private String specificXmlOutputDirectory;
	private String specificXmlArchiveDirectory;

	@Override
	public PerformResult perform(final BazaarvoiceProductFeedExportCronJobModel paramT)
	{

		try
		{	
			// getting config and site model
			cmsSiteModel = paramT.getBazaarvoiceStorefront();
			bazaarvoiceConfig = cmsSiteModel.getBvConfig();
			catalog = cmsSiteModel.getDefaultCatalog();
			ftpServerDirectory = Config.getParameter(BazaarvoiceConstants.FTP_SERVER_DIRECTORY);
			ftpServerPort = Config.getParameter(BazaarvoiceConstants.FTP_SERVER_PORT);
			xmlBaseFileName = Config.getParameter(BazaarvoiceConstants.XML_BASE_FILE_NAME);
			localXmlOutputDirectory = Config.getParameter(BazaarvoiceConstants.LOCAL_XML_OUTPUT_DIRECTORY);
			localXmlArchiveDirectory = Config.getParameter(BazaarvoiceConstants.LOCAL_XML_ARCHIVE_DIRECTORY);
			specificXmlOutputDirectory = Config.getParameter(BazaarvoiceConstants.SPECIFIC_XML_OUTPUT_DIRECTORY);
			specificXmlArchiveDirectory = Config.getParameter(BazaarvoiceConstants.SPECIFIC_XML_ARCHIVE_DIRECTORY);

			final boolean enoughInformation = isEnoughInformation();
			if (!enoughInformation)
			{
				throw new BazaarvoiceProductFeedExportException("Not enought information");
			}

			// TODO: enable when needed
            if (bazaarvoiceConfig.getLicenseAccepted()==null || !bazaarvoiceConfig.getLicenseAccepted().booleanValue())
				{
					throw new Exception("License has not been accepted.  You must accept the license terms by setting the option in the Bazaarvoice Config panel.");
				}

			LOG.debug("fileRoot is " + PROJECT_ROOT_PATH);
			LOG.debug("BazaarvoiceProductFeedExporter started");
			LOG.debug("Export of product catalogs: "); // TODO: fill product catalog names

			products = new ArrayList<ProductModel>();
			brands = new ArrayList<>();
			categories = new ArrayList<>();
			final Map<String, ProductModel> productsMap = new HashMap<String, ProductModel>();

			setLocales();

			if (cmsSiteModel != null && paramT.getCatalogVersion() != null)
			{
				for (final ProductModel productModel : gpProductService.getProductsForSiteAndCatalogVersion(cmsSiteModel,
						paramT.getCatalogVersion()))
			{
					// we add to the feed products with status approved
					if (productModel.getApprovalStatus() == ArticleApprovalStatus.APPROVED)
				{
						productsMap.put(productModel.getCode(), productModel);
				}
			}
			}

			products.addAll(productsMap.values());
			if (products.isEmpty())
			{
				throw new BazaarvoiceProductFeedExportException("Product catalog is empty!");
			}


			productFeedXmlCreator.setCMSSiteModel(cmsSiteModel);
            productFeedXmlCreator.setUpcMethodName(bazaarvoiceConfig.getUpcMethodName());

			final List<ProductTag> productTags = new ArrayList<ProductTag>();
			productTags.addAll(productFeedXmlCreator.getProductTagsFromCollection(products, locales, bazaarvoiceConfig
					.getIsFamiliesEnabled().booleanValue(),paramT));
			LOG.debug("Count of products   is " + products.size());

			final List<File> files = new ArrayList<File>();
			final Boolean incremental = (productTags.size() > PRODUCT_MAX_NUMBER) ? Boolean.TRUE : Boolean.FALSE;
			final Date date = new Date();
			final String generator = bazaarvoiceConfig.getExtensionVersion();
			final String dateNamber = productFeedXmlCreator.gerenateDate();
			for (int i = 0; i * PRODUCT_MAX_NUMBER < productTags.size(); i++)
			{
				if (clearAbortRequestedIfNeeded(paramT))
				{ 
					LOG.debug("The job is aborted.");
				  return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED); 
				}
				final String fileName = productFeedXmlCreator.generateTodayFilename(xmlBaseFileName, dateNamber,cmsSiteModel.getUid());
				final File pathToDir = getPathToDir(localXmlOutputDirectory, specificXmlOutputDirectory);
				final File file = new File(pathToDir.getAbsolutePath(), fileName + ".xml");
				if (!file.exists()) {
					try {
						final boolean created = file.createNewFile();
						LOG.warn("File created " + created);
					} catch (final Exception ex) {
						ex.printStackTrace();
					}
				}
				final ProductFeed feed = new ProductFeed();
				feed.setName(bazaarvoiceConfig.getClientName());
				feed.setGenerator("bazaarvoice hybris extension v" + generator);
				feed.setXmlns(PRODUCT_FEED_XMLNS);
				feed.setExtractDate(date);
				feed.setIncremental(incremental);
				feed.setBrands(productFeedXmlCreator.getBrandTagsFromCollection(paramT));
				feed.setCategories(productFeedXmlCreator.getCategoryTagsFromCollection(paramT.getCategories()));
				final Collection<ProductTag> partProducts = new ArrayList<>();
				partProducts.addAll(productTags.subList(PRODUCT_MAX_NUMBER * i,
						PRODUCT_MAX_NUMBER * (i + 1) <= productTags.size() ? PRODUCT_MAX_NUMBER * (i + 1) : productTags.size()));
				feed.setProducts(partProducts);
				productFeedXmlCreator.writeXmlToFile(file, feed);
				files.add(file);
			}
			//compressing of file to zip
			final List<File> compressedFiles = compressFilesToGz(files, localXmlArchiveDirectory, specificXmlArchiveDirectory);
			if (compressedFiles == null || compressedFiles.isEmpty())
			{
				LOG.debug("File compression failed");
				throw new BazaarvoiceProductFeedExportException("File compression failed");
			}
			else if (compressedFiles.size() != files.size())
			{
				LOG.debug("File compression was PARTLY failed");
				throw new BazaarvoiceProductFeedExportException("File compression failed");
			}
			else
			{
				LOG.debug("Xml files compressed to gzips");
			}
			//transfering to bazaarvoice ftp server
			final String server = bazaarvoiceConfig.getFtpServer();
			final boolean transferResult;
			if(server.toLowerCase().substring(0,4).equals("sftp")){
				transferResult = transferFileToSFTPServer(compressedFiles, server);
			} else {
				transferResult = transferFileToServer(compressedFiles);
			}

			if (transferResult)
			{
				LOG.debug("Zip transferred to server SUCCESSFULLY");
			}
			else
			{
				LOG.debug("Zip transferred to server NOT SUCCESSFULLY");
				throw new BazaarvoiceProductFeedExportException("Transfer failed");
			}
			
		
		}
		catch (final Exception ex)
		{
			LOG.debug("BazaarvoiceProductFeedExporter finished with status FAILURE");
			ex.printStackTrace();
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
		
		LOG.debug("BazaarvoiceProductFeedExporter finished with status SUCCESS");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		/*
		 * final ActionResult actionResult = new ActionResult(0,
		 * HMCHelper.getLocalizedString("BazaarvoiceProductFeedExporter finished with status SUCCESS"), true);
		 * actionResult.showResult();
		 */
		
	}


	private void setLocales()
	{
		locales = new HashMap<Locale, String>();
		final Set<Locale> locs = i18NService.getSupportedLocales();

		for (final Locale locale : locs)
		{
			LOG.debug("cmsSiteModel.getBvLocale: " + cmsSiteModel.getBvLocale(locale));

			final String localeValue = cmsSiteModel.getBvLocale(locale);
			// TODO: set required locales
			//					paramT.getBazaarvoiceConfig().getLocalesMapper(locale);
			if (localeValue != null)
			{
				locales.put(locale, localeValue);
			}
		}

	}

	//  // Debuging purposes only
	//	private void printCategories(final List<CategoryModel> categories)
	//	{
	//
	//		int i = 0;
	//		for (final CategoryModel category : categories)
	//		{
	//			LOG.debug(i + ". " + category.getName());
	//			i++;
	//		}
	//	}


	private List<CategoryModel> getRootCategories(final CatalogModel catalogModel)
	{
		final List<CategoryModel> allCategories = new ArrayList<>();
		//final CatalogModel catalogModel = catalogService.getCatalogForId(catalogName);
		final CatalogVersionModel catalogVersionModel = catalogModel.getActiveCatalogVersion();
		final Collection<CategoryModel> categories = categoryService.getRootCategoriesForCatalogVersion(catalogVersionModel);

		final Iterator it = categories.iterator();
		while (it.hasNext())
		{
			final CategoryModel cm = (CategoryModel) it.next();
			allCategories.add(cm);
		}

		return allCategories;
	}

	private List<CategoryModel> getSubCategories(final CategoryModel superCategory)
	{
		final List<CategoryModel> categories = new ArrayList<CategoryModel>();

		final Collection<CategoryModel> subCategories = superCategory.getAllSubcategories();
		final Iterator sIt = subCategories.iterator();
		while (sIt.hasNext())
		{
			final CategoryModel sCm = (CategoryModel) sIt.next();
			categories.add(sCm);
			categories.addAll(getSubCategories(sCm));
		}
		return categories;
	}

	@Override
	@Autowired(required = true)
	public void setModelService(final ModelService modelService)
	{

		this.modelService = modelService;
	}


	@Override
	@Autowired(required = true)
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{

		this.flexibleSearchService = flexibleSearchService;
	}


	@Override
	@Autowired(required = true)
	public void setSessionService(final SessionService sessionService)
	{

		this.sessionService = sessionService;
	}

	public boolean transferFileToSFTPServer(final List<File> files, final String server)
	{
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		Integer port = null;
		LOG.info("preparing the host information for sftp.");
		try {
			port = getPort(server, port);
			final JSch jsch = new JSch();
			session = jsch.getSession(bazaarvoiceConfig.getFtpUserName(), bazaarvoiceConfig.getFtpServer(), port);
			session.setPassword(bazaarvoiceConfig.getFtpPassword());
			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			LOG.info("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			LOG.info("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			for (final File file : files)
			{
				LOG.info("Trying to transfer file " + file.getAbsolutePath());
				channelSftp.cd(ftpServerDirectory);
				channelSftp.put(new FileInputStream(file), file.getName());
				LOG.info("File name on server: " + file.getName());
			}

		} catch (final Exception ex) {
			LOG.info("Exception found while tranfer the response.");
			return false;
		}
		finally{

			channelSftp.exit();
			LOG.info("sftp Channel exited.");
			channel.disconnect();
			LOG.info("Channel disconnected.");
			session.disconnect();
			LOG.info("Host Session disconnected.");
		}
		return true;
	}

	public boolean transferFileToServer(final List<File> files)
	{

		boolean result = true;
		final FTPClient client = new FTPClient();
		FileInputStream fis = null;
		String server = null;
		Integer port = null;
		try
		{
			server = bazaarvoiceConfig.getFtpServer();
			port = getPort(server, port);
			//			server = paramT.getBazaarvoiceConfig().getFtpServer();//bazaarvoiceSettingService.getProperty(BazaarvoiceConstants.FTP_STAGING_SERVER);
			//			port = paramT.getBazaarvoiceConfig().getFtpTransferPort();
			client.connect(server, port != null ? port.intValue() : 21);
			LOG.debug("Connection established...");
			final boolean login = client.login(bazaarvoiceConfig.getFtpUserName(), bazaarvoiceConfig.getFtpPassword());

			if (login)
			{

				LOG.debug("Client logged...");
				client.enterLocalPassiveMode();

				LOG.debug("Transfering...");
				client.setFileType(FTP.BINARY_FILE_TYPE);
				for (final File file : files)
				{
					fis = new FileInputStream(file.getAbsoluteFile());
					LOG.info("Trying to transfer file " + file.getAbsolutePath());
					final String serverFileName = ftpServerDirectory + "/" + file.getName();
					LOG.debug("File name on server: " + serverFileName);
					final boolean done = client.storeFile(serverFileName, fis);
					result &= done;
				}

				final boolean logout = client.logout();
				if (logout)
				{
					LOG.debug("Connection close...");
				}
			}
			else
			{
				result = false;
				LOG.debug("Logging failed...");
			}

		}
		catch (final Exception e)
		{
			result = false;
			LOG.debug("Connection failed...");
			if (server == null || port == null)
			{
				LOG.debug("Please check your Bazaarvoice settings! If they are absent, update your system or import them!");
			}
			e.printStackTrace();
			LOG.debug("Transfer failed");
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				client.disconnect();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}

		}
		return result;
	}

	private Integer getPort(final String server, Integer port) {
		if(ftpServerPort==null || ftpServerPort.isEmpty()){
            if(server!=null && !server.isEmpty()) {
                port = (server.toLowerCase().substring(0,4).equals("sftp") ? Integer.valueOf(22) : Integer.valueOf(21));
            }
        } else {
            try{
                port = Integer.valueOf(ftpServerPort);
            } catch (final Exception e) {
                port = Integer.valueOf(21);
            }
        }
		return port;
	}

	private List<File> compressFilesToGz(final List<File> files, final String archiveDirectory, final String specificXmlArchiveDirectory)
	{

		final List<File> compressedFiles = new ArrayList<>();
		if (files == null || files.isEmpty())
		{
			return null;
		}
		GZIPOutputStream gzos = null;
		File outputFile = null;
		final byte[] buffer = new byte[1024];
		try
		{
			FileInputStream in = null;
			for (final File file : files) {

				final File pathToDir = getPathToDir(archiveDirectory, specificXmlArchiveDirectory);
				outputFile = new File(pathToDir.getAbsolutePath(), file.getName() + ".gz");
				in = new FileInputStream(file.getAbsolutePath());
				gzos = new GZIPOutputStream(new FileOutputStream(outputFile));
				int len;
				while ((len = in.read(buffer)) > 0)
				{
					gzos.write(buffer, 0, len);
				}
				compressedFiles.add(outputFile);
				gzos.close();
				in.close();
			}

		}
		catch (final IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
		finally
		{
			if (gzos != null)
			{
				try
				{
					gzos.close();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return compressedFiles;
	}

	private File getPathToDir(final String localDirectory, final String specificDirectory) {
		final File pathToDir;
		if (specificDirectory != null) {
            pathToDir = new File(specificDirectory);
        } else {
            pathToDir = new File(PROJECT_ROOT_PATH + (localDirectory == null ? "" : localDirectory));
        }
		if (!pathToDir.exists())
        {
            pathToDir.mkdirs();
        }
		return pathToDir;
	}

	private boolean isEnoughInformation() {
		boolean enoughInfo = true;
		if (cmsSiteModel == null)
		{
			LOG.debug("Default cmsSite is missing.");
			enoughInfo = false;
		}
		if (bazaarvoiceConfig == null)
		{
			LOG.debug("Default bazaarvoiceConfig is missing.");
			enoughInfo = false;
		}
		if (catalog == null)
		{
			LOG.debug("Default catalog is missing.");
			enoughInfo = false;
		}

		if (ftpServerDirectory == null)
		{
			LOG.debug("ftpServerDirectory property in the config file is missing.");
			enoughInfo = false;
		}
		if (xmlBaseFileName == null)
		{
			LOG.debug("xmlBaseFileName property in the config file is missing.");
			enoughInfo = false;
		}
		if (localXmlOutputDirectory == null && specificXmlOutputDirectory == null)
		{
			LOG.debug("localXmlOutputDirectory and specificXmlOutputDirectory properties in the config file are " +
					"missing. You should specify at least one of them.");
			enoughInfo = false;
		}
		if (localXmlArchiveDirectory == null && specificXmlArchiveDirectory == null)
		{
			LOG.debug("localXmlArchiveDirectory and specificXmlArchiveDirectory properties in the config file are " +
					"missing. You should specify at least one of them.");
			enoughInfo = false;
		}
		return enoughInfo;

	}
	
	@Override
	public boolean isAbortable() {
		return true;
	} 
}