/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import de.hybris.platform.acceleratorservices.email.EmailService;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.QuickOrderEmailProcessModel;
import com.gp.commerce.core.product.services.impl.GPB2BDefaultProductService;
import com.gp.commerce.core.services.impl.DefaultGPEmailService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;

public class GPQuickOrderEmailContext extends GPAbstractEmailContext<QuickOrderEmailProcessModel>{

	private static final Logger LOG = Logger.getLogger(GPQuickOrderEmailContext.class);
	public static final String DELIMITER = ",";
	public static final String LINE_SEPERATOR = "\n";
	private static final String QUICK_ORDER = "QUICK_ORDER";
	private static final String ATTACHMENT_QUICK_ORDER = "ATTACHMENT_QUICK_ORDER";
	private static final String RECIPIENTS_EMAIL_ID = "RECIPIENTS_EMAIL_ID";

	@Resource(name = "emailService")
	private EmailService emailService;

	@Resource(name = "defaultGPEmailService")
	private DefaultGPEmailService defaultGPEmailService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	StringBuilder csvContent = new StringBuilder();
	List<EmailAttachmentModel> attachmentList;

	@Resource(name = "wishlistFacade")
	private GPWishlistFacade wishlistFacade;

	@Resource(name = "gpB2BProductService")
	private GPB2BDefaultProductService gpB2BProductService;

	@Override
	public void init(final QuickOrderEmailProcessModel quickOrderEmailProcessModel, final EmailPageModel emailPageModel) {
		super.init(quickOrderEmailProcessModel, emailPageModel);

		final Wishlist2Model wishlist2Model = quickOrderEmailProcessModel.getWishlist();

		LOG.debug(" generating Email subject wishlist uid");

		try{
			attachmentList = populateCSV(wishlist2Model);
		}catch(final Exception e){
			LOG.error("############### Error in populating products to csv in share quickorder ############################"+e.getMessage(),e);
		}

		final List<EmailAddressModel> toEmails = setRecipientEmailIds(quickOrderEmailProcessModel);

		put(QUICK_ORDER, true);
		put(ATTACHMENT_QUICK_ORDER, attachmentList);
		put(EMAIL, (String)quickOrderEmailProcessModel.getEmailIds());
		put(RECIPIENTS_EMAIL_ID, toEmails);
		put(GpcommerceCoreConstants.CONTACT_NUMBER, getCustomerNumber());
	}


	private List<EmailAddressModel> setRecipientEmailIds(final QuickOrderEmailProcessModel quickOrderEmailProcessModel) {
		String[] recipientEmails;
		final List<EmailAddressModel> toEmails = new ArrayList<>();
		EmailAddressModel toAddress;
		final String rEmail= quickOrderEmailProcessModel.getEmailIds();
		if(StringUtils.isNotBlank(rEmail)) {
			if (StringUtils.contains(rEmail, DELIMITER))
			{
				recipientEmails = StringUtils.split(rEmail, DELIMITER);
				for(final String emailId: recipientEmails) {
					toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailId, StringUtils.EMPTY);
					toEmails.add(toAddress);
				}
			}
			else {
				toAddress = getEmailService().getOrCreateEmailAddressForEmail(rEmail, StringUtils.EMPTY);
				toEmails.add(toAddress);
			}
		}
		return toEmails;
	}


	public List<EmailAttachmentModel> populateCSV(final Wishlist2Model wishlist) throws IOException {

		final List<EmailAttachmentModel> attachments = new ArrayList<>();
		final StringWriter writer = new StringWriter();
		final List<String> headers = new ArrayList<>();

		headers.add("Material Number (SKU)");
		headers.add("Product Name");
		headers.add("Product Quantity");
		headers.add("CMIR");

		if (null != wishlist && CollectionUtils.isNotEmpty(wishlist.getEntries()))
		{
			int i = 0;
			for (; i < headers.size() - 1; i++)
			{
				csvContent.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(DELIMITER);
			}
			csvContent.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(LINE_SEPERATOR);
			writer.write(csvContent.toString());
			final List<Wishlist2EntryModel> entries = wishlist.getEntries();

			String cmir = null;
			B2BCustomerModel customer = null;
			if (wishlist.getUser() instanceof B2BCustomerModel)
			{
				customer = (B2BCustomerModel) wishlist.getUser();
			}
			for (final Wishlist2EntryModel entry : entries)
			{
				if (null != customer && null != customer.getDefaultB2BUnit())
				{
					cmir = gpB2BProductService.getCMIRCodeForProductAndB2BUnit(entry.getProduct().getCode(),
							customer.getDefaultB2BUnit().getUid());
				}
				cmir = (null != cmir) ? cmir : StringUtils.EMPTY;
				writeOrderEntry(writer, entry, cmir);
			}
		}

		final String directoryPath = Config.getParameter(GpcommerceFacadesConstants.QUICK_ORDER_EMAIL);
		final File dir = new File(directoryPath);
		//Added timestamp to create unique filename
		final String fileName = "quickorder"+"_"+System.currentTimeMillis();
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
		final EmailAttachmentModel attachment = getDefaultGPEmailService().createEmailAttachment(in,
				fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION, GpcommerceFacadesConstants.CSV_FILE_TYPE);
		attachments.add(attachment);
		return attachments;
	}


	protected  void writeOrderEntry(final Writer writer, final Wishlist2EntryModel entry, final String cmir) throws IOException
	{
		csvContent.append(StringEscapeUtils.escapeCsv(entry.getProduct().getCode())).append(DELIMITER)
		.append(StringEscapeUtils.escapeCsv(entry.getProduct().getName())).append(DELIMITER)
		.append(StringEscapeUtils.escapeCsv(entry.getQuantity().toString())).append(DELIMITER)
		.append(StringEscapeUtils.escapeCsv(cmir)).append(LINE_SEPERATOR);
		writer.write(csvContent.toString());
	}

	@Override
	protected BaseSiteModel getSite(final QuickOrderEmailProcessModel businessProcessModel) {
		return businessProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final QuickOrderEmailProcessModel businessProcessModel) {
		return businessProcessModel.getCustomer();
	}

	@Override
	protected LanguageModel getEmailLanguage(final QuickOrderEmailProcessModel businessProcessModel) {
		return businessProcessModel.getLanguage();
	}

	public EmailService getEmailService() {
		return emailService;
	}
	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}
	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}
	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
	public DefaultGPEmailService getDefaultGPEmailService() {
		return defaultGPEmailService;
	}
	public void setDefaultGPEmailService(final DefaultGPEmailService defaultGPEmailService) {
		this.defaultGPEmailService = defaultGPEmailService;
	}
}
