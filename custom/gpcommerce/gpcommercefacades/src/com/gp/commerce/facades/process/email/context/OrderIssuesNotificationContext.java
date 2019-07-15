/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.OrderIssuesNotificationProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.impl.DefaultGPEmailService;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.util.Config;

public class OrderIssuesNotificationContext  extends GPAbstractEmailContext<OrderIssuesNotificationProcessModel>{
	
	private static final Logger LOG = Logger.getLogger(ShareCartEmailContext.class);

	
	private static final String ORDER_REPORT_SITE_CONFIG = "orderReportRecipient";
	
	private static final String ORDER_ISSUE_NOTIFY = "ORDER_ISSUE_NOTIFY";
	private static final String ATTACHMENTS = "ATTACHMENTS";
	private static final String RECIPIENTS_EMAIL_ID = "RECIPIENTS_EMAIL_ID";
	private static final String DELIMITER = ",";
	
	final Map<String,List<String>> issueMap = new HashMap<>();
	private List<EmailAttachmentModel> attachments;
	private String recepientEmailString;
	StringBuilder csvContent = new StringBuilder();
	
	@Resource
	private DefaultGPEmailService emailService;
	
	@Resource
	GPCMSSiteService cmsSiteService;
	
	
	@Override
	public void init(final OrderIssuesNotificationProcessModel processModel, final EmailPageModel emailPageModel)
	{
		super.init(processModel, emailPageModel);
		
		issueMap.putAll(processModel.getIssueMap());
		
		try {
			generateCsv(processModel.getIssueMap());
		}
		catch (final IOException e) {
              LOG.error(e.getMessage(),e);
		}
		
		put(ORDER_ISSUE_NOTIFY, true);
		put(ATTACHMENTS, getAttachments());
		put(RECIPIENTS_EMAIL_ID, getRecepientEmailModels((CMSSiteModel)processModel.getSite()));
		if (StringUtils.contains(recepientEmailString, DELIMITER))
		{
		put(EMAIL,recepientEmailString);
		}
		else
		{
			put(EMAIL,recepientEmailString+DELIMITER);
		}
		
	}
	
	@Override
	protected BaseSiteModel getSite(OrderIssuesNotificationProcessModel businessProcessModel) {
		return businessProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(OrderIssuesNotificationProcessModel businessProcessModel) {
		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(OrderIssuesNotificationProcessModel businessProcessModel) {
		return businessProcessModel.getSite().getDefaultLanguage();
	}
	
	
	@Override
	public String getDisplayName() {
		return WordUtils.capitalizeFully((String) get(AbstractEmailContext.DISPLAY_NAME));
	}
	
	

	private List<EmailAddressModel> getRecepientEmailModels(CMSSiteModel site)
	{
		final List<EmailAddressModel> toEmails = new ArrayList<>();
		String[] recipientEmails = null;
		EmailAddressModel toAddress;
		
		recepientEmailString=GPSiteConfigUtils.getSiteConfig(site, ORDER_REPORT_SITE_CONFIG);
		
		if(StringUtils.isNotBlank(recepientEmailString)) {
			if (StringUtils.contains(recepientEmailString, DELIMITER))
			{
				recipientEmails = StringUtils.split(recepientEmailString, DELIMITER);
				
			}else{
				String[] strArray = new String[1];
				strArray[0]=recepientEmailString;
				recipientEmails=strArray;
			}
			for(final String emailId: recipientEmails) {
				toAddress = emailService.getOrCreateEmailAddressForEmail(emailId, StringUtils.EMPTY);
				toEmails.add(toAddress);
			}
		}
		return toEmails;
	}

	private void generateCsv(final Map<String,List<String>> map) throws IOException
	{
		createCsvHeader();
		createCsvContent(map);
		
		final String directoryPath = Config.getParameter(GpcommerceFacadesConstants.FAILED_ORDER_EMAIL);
		final File dir = new File(directoryPath);
		final String fileName = "orderIssues"+ "_" +System.currentTimeMillis();
		final File temp = new File(dir, fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION);

		try (FileWriter fileWriter = new FileWriter(
				directoryPath + "///" + fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION);
				BufferedWriter bw = new BufferedWriter(fileWriter))
		{
            bw.write(csvContent.toString());
			}catch (final IOException e) {
				LOG.error(e.getMessage(),e);
			}

		final DataInputStream in = new DataInputStream(new FileInputStream(temp));
		final EmailAttachmentModel attachment = emailService.createEmailAttachment(in,
				fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION, GpcommerceFacadesConstants.CSV_FILE_TYPE);
		final List<EmailAttachmentModel> attachmentsList = new ArrayList<>();
		attachmentsList.add(attachment);
		setAttachments(attachmentsList);
		temp.delete();
	}
	
	private void createCsvHeader()
	{
		final List<String> headers = new ArrayList<>();

		headers.add("Status: Not Exported");
		headers.add("Status: Failure");
		headers.add("Status: Not Able to Process");
		headers.add("Status: Failed Customer Replication");
		headers.add("Status: Payment Error");
		
		for (int i=0; i < headers.size(); i++)
		{
			csvContent.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(DELIMITER);
		}
		csvContent.append('\n');
	}
	
	private void createCsvContent(Map<String,List<String>> map)
	{
		List<String> notExportedList=new ArrayList<>();
		List<String> failureList=new ArrayList<>();
		List<String> unprocessedList=new ArrayList<>();
		List<String> notReplicatedList=new ArrayList<>();
		List<String> paymentErrorList=new ArrayList<>();
		map.keySet().forEach(key ->{
			if(key.equalsIgnoreCase(OrderStatus.PAYMENT_ERROR.getCode()))
			{
				paymentErrorList.addAll(map.get(key));
			}
			else if(key.equalsIgnoreCase(GPNetsuiteOrderExportStatus.NOTEXPORTED.getCode()))
			{
				notExportedList.addAll(map.get(key));
			}
			else if(key.equalsIgnoreCase(GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS.getCode()))
			{
				unprocessedList.addAll(map.get(key));
			}
			else if(key.equalsIgnoreCase(GPNetsuiteOrderExportStatus.FAILURE.getCode()))
			{
				failureList.addAll(map.get(key));
			}
			else if(key.equalsIgnoreCase(GPNetsuiteOrderExportStatus.FAILED_CUSTOMER_REPLICATION.getCode()))
			{
				notReplicatedList.addAll(map.get(key));
			}
			
		});
		
		int size=0;
		size=Math.max(size, notExportedList.size());
		size=Math.max(size,paymentErrorList.size());
		size=Math.max(size,failureList.size());
		size=Math.max(size,unprocessedList.size());
		size=Math.max(size,notReplicatedList.size());
		for(int i=0;i<size;i++)
		{
			csvContent.append(i<notExportedList.size()? notExportedList.get(i)+DELIMITER:DELIMITER);
			csvContent.append(i<failureList.size()? failureList.get(i)+DELIMITER:DELIMITER);
			csvContent.append(i<unprocessedList.size()? unprocessedList.get(i)+DELIMITER:DELIMITER);
			csvContent.append(i<notReplicatedList.size()? notReplicatedList.get(i)+DELIMITER:DELIMITER);
			csvContent.append(i<paymentErrorList.size()? paymentErrorList.get(i):"");
			csvContent.append('\n');
		}
	
	}



	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}


	public Map<String, List<String>> getIssueMap() {
		return issueMap;
	}
	

}

