/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.acceleratorservices.email.EmailService;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.FailedOrderEmailProcessModel;
import com.gp.commerce.core.services.impl.DefaultGPEmailService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;


public class FailedOrderContext extends GPAbstractEmailContext<FailedOrderEmailProcessModel>{

	private static final String ORDER_DATA_LIST="orderDataList";
	private static final String CONSIGNMENT_DATA_LIST="consignDataList";
	private static final String CSR_EMAIL="csr.email";
	private static final Logger LOG = Logger.getLogger(FailedOrderContext.class);
	public static final String DELIMITER = ",";
	public static final String LINE_SEPERATOR = "\n";
	private static final String FAILED_ORDER_CONSIGNMENT = "FAILED_ORDER_CONSIGNMENT";
	private static final String CSR_EMAIL_LIST = "CSR_EMAIL_LIST";
	private static final String ATTACHMENT_FAILED_ORDER = "ATTACHMENT_FAILED_ORDER";
	private static final String CSR_ADMIN = "CSR_Admin";

	private DefaultGPEmailService defaultGPEmailService;
	private EmailService emailService;
	List<EmailAttachmentModel> attachmentList;

	public void init(final FailedOrderEmailProcessModel failedOrderEmailProcessModel, final EmailPageModel emailPageModel)
	{
		LOG.info("Inside FailedOrderContext");
		super.init(failedOrderEmailProcessModel, emailPageModel);
		final Collection<OrderModel> orders = failedOrderEmailProcessModel.getOrder();
		final Collection<ConsignmentModel> consignments = failedOrderEmailProcessModel.getConsignments();
		LOG.info("FailedOrderContext | No of orders found in error state are :" + orders.size());
		LOG.info("FailedOrderContext | No of consignments found in error state are :" + consignments.size());

		final String csrEmail = getConfigurationService().getConfiguration().getString(CSR_EMAIL, "admin@gppro.com");
		if(LOG.isDebugEnabled()) {
			LOG.debug("FailedOrderContext | CSR email is configured to email address : " +csrEmail);
		}
		final List<EmailAddressModel> toEmailList = new ArrayList<>();
		EmailAddressModel toCsrEmail;
		if(StringUtils.isNotBlank(csrEmail))
		{
			if(StringUtils.contains(csrEmail, DELIMITER))
			{
				final String[] csrRecipientEmails = StringUtils.split(csrEmail, DELIMITER);
				for (final String csrRecipient : csrRecipientEmails)
				{
					toCsrEmail= getEmailService().getOrCreateEmailAddressForEmail(csrRecipient, StringUtils.EMPTY);
					toEmailList.add(toCsrEmail);
				}
			}
			else
			{
				toCsrEmail= getEmailService().getOrCreateEmailAddressForEmail(csrEmail, StringUtils.EMPTY);
				toEmailList.add(toCsrEmail);
			}
		}
		try{
			attachmentList = populateCSV(orders,consignments);
		}catch(final Exception e){
			LOG.error("############### Error in populating products to csv in order fulfillment error ############################"+e.getMessage(),e);
		}
		put(FAILED_ORDER_CONSIGNMENT, true);
		put(CSR_EMAIL_LIST, toEmailList);
		put(ORDER_DATA_LIST, orders);
		put(CONSIGNMENT_DATA_LIST, consignments);
		put(ATTACHMENT_FAILED_ORDER, attachmentList);
		put(EMAIL,csrEmail);
		put(TITLE, "Mr");
		put(DISPLAY_NAME, CSR_ADMIN);
		put(GpcommerceCoreConstants.CONTACT_NUMBER, getCustomerNumber());

	}

	private List<EmailAttachmentModel> populateCSV(final Collection<OrderModel> orders, final Collection<ConsignmentModel> consignments) throws IOException {
		final List<EmailAttachmentModel> attachments = new ArrayList<>();
		final EmailAttachmentModel ordersAttachment = createFailedOrders(orders,consignments);
		attachments.add(ordersAttachment);
		return attachments;

	}

	private EmailAttachmentModel createFailedOrders(final Collection<OrderModel> orders, final Collection<ConsignmentModel> consignments) throws IOException {
		final StringBuilder csvContent = new StringBuilder();

			if (CollectionUtils.isNotEmpty(orders))
			{
				final StringWriter orderWriter = new StringWriter();
				final List<String> orderHeaders = new ArrayList<>();
				orderHeaders.add("Order Number");
				orderHeaders.add("Order Processing Status");
				int i = 0;
				for (; i < orderHeaders.size() - 1; i++)
				{
					csvContent.append(StringEscapeUtils.escapeCsv(orderHeaders.get(i))).append(DELIMITER);
				}
				csvContent.append(StringEscapeUtils.escapeCsv(orderHeaders.get(i))).append(LINE_SEPERATOR);
				orderWriter.write(csvContent.toString());
				for (final OrderModel order : orders)
				{
					writeOrders(order,csvContent,orderWriter);
				}
				csvContent.append(LINE_SEPERATOR);
				orderWriter.write(csvContent.toString());
			}
			if (CollectionUtils.isNotEmpty(consignments))
			{
				final StringWriter consignWriter = new StringWriter();
				final List<String> consignheaders = new ArrayList<>();
				consignheaders.add("Consignment Number");
				consignheaders.add("Consignment Processing Status");
				int i = 0;
				for (; i < consignheaders.size() - 1; i++)
				{
					csvContent.append(StringEscapeUtils.escapeCsv(consignheaders.get(i))).append(DELIMITER);
				}
				csvContent.append(StringEscapeUtils.escapeCsv(consignheaders.get(i))).append(LINE_SEPERATOR);
				consignWriter.write(csvContent.toString());
				for (final ConsignmentModel consign : consignments)
				{
					writeConsignments(consign,csvContent,consignWriter);
				}
				csvContent.append(DELIMITER);
				consignWriter.write(csvContent.toString());
			}

		final String directoryPath = Config.getParameter(GpcommerceFacadesConstants.FAILED_ORDER_EMAIL);
		final File dir = new File(directoryPath);
		//Added timestamp to create unique filename
		final String fileName = "failedOrders"+"_"+System.currentTimeMillis();
		final File temp = new File(dir, fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION);


		try (FileWriter fileWriter = new FileWriter(
				directoryPath + "///" + fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION);
				BufferedWriter bw = new BufferedWriter(fileWriter))
		{
            bw.write(csvContent.toString());
      }catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}

		final DataInputStream in = new DataInputStream(new FileInputStream(temp));
		return getDefaultGPEmailService().createEmailAttachment(in, fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION,
				GpcommerceFacadesConstants.CSV_FILE_TYPE);
	}

	protected  void writeOrders(final OrderModel order, final StringBuilder csvContent, final StringWriter orderWriter) throws IOException
	{
		csvContent.append(StringEscapeUtils.escapeCsv(order.getCode())).append(DELIMITER)
		.append(StringEscapeUtils.escapeCsv(order.getProcessingStatus().toString()));
		csvContent.append(LINE_SEPERATOR);
		orderWriter.write(csvContent.toString());
	}

	private void writeConsignments(final ConsignmentModel consign, final StringBuilder csvContent, final StringWriter consignWriter) {
		csvContent.append(StringEscapeUtils.escapeCsv(consign.getCode())).append(DELIMITER)
		.append(StringEscapeUtils.escapeCsv(consign.getProcessingStatus().toString()));
		csvContent.append(LINE_SEPERATOR);
		consignWriter.write(csvContent.toString());

	}


	@Override
	protected BaseSiteModel getSite(final FailedOrderEmailProcessModel failedOrderEmailProcessModel) {
		return failedOrderEmailProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final FailedOrderEmailProcessModel failedOrderEmailProcessModel) {
		return (CustomerModel) failedOrderEmailProcessModel.getUser();
	}

	@Override
	protected LanguageModel getEmailLanguage(final FailedOrderEmailProcessModel failedOrderEmailProcessModel) {
		return failedOrderEmailProcessModel.getSite().getDefaultLanguage();
	}

	public EmailService getEmailService() {
		return emailService;
	}

	@Required
	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	public DefaultGPEmailService getDefaultGPEmailService() {
		return defaultGPEmailService;
	}

	@Required
	public void setDefaultGPEmailService(final DefaultGPEmailService defaultGPEmailService) {
		this.defaultGPEmailService = defaultGPEmailService;
	}

}
