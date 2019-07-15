package com.bazaarvoice.hybris.cronjob;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;

import com.bazaarvoice.hybris.constants.BazaarvoiceConstants;
import com.bazaarvoice.hybris.model.BazaarvoiceConfigModel;
import com.bazaarvoice.hybris.model.BazaarvoiceImportRatingsCronJobModel;
import com.bazaarvoice.hybris.service.GPBazaarVoiceRatingImportService;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Job to pick ratings feed from bazaarvoice sftp and update product with review and ratings data
 */
public class GPBazaarVoiceDataFeedDownloadJob extends AbstractJobPerformable<BazaarvoiceImportRatingsCronJobModel> {

	private static final Logger LOG = Logger.getLogger(GPBazaarVoiceDataFeedDownloadJob.class.getName());

	/**
	 * constructor
	 */
	public GPBazaarVoiceDataFeedDownloadJob() {
	}

	private static final String PROJECT_ROOT_PATH = Utilities.getExtensionInfo(BazaarvoiceConstants.EXTENSIONNAME)
			.getExtensionDirectory().getAbsolutePath();
	private static final String FILE_NAME_SITE_CONFIG = "bv_feed_file_name";

	private CMSSiteModel cmsSiteModel;
	private BazaarvoiceConfigModel bazaarvoiceConfig;
	private GPBazaarVoiceRatingImportService gpbazaarVoiceRatingImportService;

	private String ftpServerPort;

	@Override
	public PerformResult perform(final BazaarvoiceImportRatingsCronJobModel cronjob) {
		LOG.info("Download file from the server");
		final String fileName =  GPSiteConfigUtils.getSiteConfig(cronjob.getBazaarvoiceStorefront(), FILE_NAME_SITE_CONFIG);
		final String decompressedFilePath = PROJECT_ROOT_PATH
				+ Config.getParameter(BazaarvoiceConstants.LOCAL_XML_BAZAAR_DECOMPRESSED_DIRECTORY) + "/" + fileName
				+ ".xml";
		final String compressedFilePath = PROJECT_ROOT_PATH
				+ Config.getParameter(BazaarvoiceConstants.LOCAL_XML_BAZAAR_COMPRESSED_DIRECTORY) + "/" + fileName + ".xml.gz";
		final String remoteServerFilePath = Config.getParameter(BazaarvoiceConstants.BAZAARVOICE_REMOTE_SERVER_DOWNLOAD_PATH) + "/" + fileName + ".xml.gz";
		try {
			cmsSiteModel = cronjob.getBazaarvoiceStorefront();
			bazaarvoiceConfig = cmsSiteModel.getBvConfig();
			ftpServerPort = Config.getParameter(BazaarvoiceConstants.FTP_SERVER_PORT);
			final String server = bazaarvoiceConfig.getFtpServer();
			final boolean isFileDownloaded = getFileFromSFTPServer(server,compressedFilePath,decompressedFilePath,remoteServerFilePath);

			if (isFileDownloaded) {
				LOG.info("Bazaar voice data import feed file is downloaded successfully");
				try {

					gpbazaarVoiceRatingImportService.populateProductRatings(decompressedFilePath,
							cronjob.getCatalogVersion());
					LOG.info("Product Data successfully updated with Ratings");
				} catch (final JAXBException e) {

					LOG.error(" Error while unmarshelling the xml");

				} catch (final XMLStreamException e) {

					LOG.error(" Error from XML Stream Reader");

				} catch (final IOException e) {

					LOG.error(" Error while reading from file");
				}
			} else {
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);

			}

		} catch (final Exception e) {
			LOG.error("Ratings Import Job failed ", e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

	}

	public boolean getFileFromSFTPServer(final String server,final String compressedFile,final String decompressedFile,final String remoteServerFile) {

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		Integer port = null;
		LOG.info("preparing the host information for sftp.");
		try {
			port = getPort(server, port);
			final JSch jsch = new JSch();
			session = jsch.getSession(bazaarvoiceConfig.getFtpUserName(), server, port);
			session.setPassword(bazaarvoiceConfig.getFtpPassword());
			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications", "password");
			session.setConfig(config);
			session.connect();
			LOG.info("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			LOG.info("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			final byte[] buffer = new byte[3000000];
			final BufferedInputStream bis = new BufferedInputStream(
					channelSftp.get(remoteServerFile));
			final File newFile = new File(compressedFile);
			final OutputStream os = new FileOutputStream(newFile);
			final BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bos.flush();
			os.flush();
			bos.close();
			os.close();
			bis.close();
			LOG.info("file successfully downloaded from server");
			unGunzipFile(compressedFile, decompressedFile);
			return true;

		} catch (final Exception ex) {

			LOG.error("Error in downloading file from sftp");
			return false;
		}

	}

	public void unGunzipFile(final String compressedFile, final String decompressedFile) {
		final byte[] buffer = new byte[1024];
		try {
			final FileInputStream fileIn = new FileInputStream(compressedFile);
			final GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
			final FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
			int bytes_read;
			while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, bytes_read);
			}
			gZIPInputStream.close();
			fileOutputStream.close();
			LOG.info("The file was decompressed successfully!");
		} catch (final IOException ex) {

			LOG.error("Error while decompressing the file");
		}
	}

	private Integer getPort(final String server, Integer port) {
		if (ftpServerPort == null || ftpServerPort.isEmpty()) {
			if (server != null && !server.isEmpty()) {
				port = (server.toLowerCase().substring(0, 4).equals("sftp") ? Integer.valueOf(22)
						: Integer.valueOf(21));
			}
		} else {
			try {
				port = Integer.valueOf(ftpServerPort);
			} catch (final Exception e) {
				port = Integer.valueOf(21);
			}
		}
		return port;
	}

	@Override
	public boolean isAbortable() {
		return true;
	}

	/**
	 * @return
	 */
	public GPBazaarVoiceRatingImportService getGpbazaarVoiceRatingImportService()
	{
		return gpbazaarVoiceRatingImportService;
	}

	/**
	 * @param gpbazaarVoiceRatingImportService
	 */
	public void setGpbazaarVoiceRatingImportService(final GPBazaarVoiceRatingImportService gpbazaarVoiceRatingImportService)
	{
		this.gpbazaarVoiceRatingImportService = gpbazaarVoiceRatingImportService;
	}


}