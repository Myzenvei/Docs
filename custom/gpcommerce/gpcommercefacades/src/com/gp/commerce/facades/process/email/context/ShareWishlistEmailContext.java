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
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import de.hybris.platform.acceleratorservices.email.EmailService;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.constants.GeneratedGpcommerceCoreConstants.Enumerations.WishlistTypeEnum;
import com.gp.commerce.core.model.WishlistProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.impl.DefaultGPEmailGenerationService;
import com.gp.commerce.core.services.impl.DefaultGPEmailService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;

public class ShareWishlistEmailContext extends GPAbstractEmailContext<WishlistProcessModel>
{
	private static final String SHARELIST = "/sharelist/";

	private static final String LIST_NAME = "/?listName=";

	private static final String PRODUCTLIST = "/productlist";

	private static final String LIST_DETAILS = "/my-account/listdetails";
	
	private static final Logger LOG = Logger.getLogger(ShareWishlistEmailContext.class);

	private Converter<Wishlist2Model, WishlistData> wishlistConverter;
	private WishlistData wishlistData;
	private EmailService emailService;
	public static final String LINE_SEPERATOR = "\n";
	public static final String DELIMITER = ",";
	private static final String SHARE_WISHLIST_ENABLED="customShareWishlistEnabled";
	private static final String ATTACHMENTS = "ATTACHMENTS";
	private static final String RECIPIENTS_EMAIL_ID = "RECIPIENTS_EMAIL_ID";
	private static final String WISHLIST = "Wishlist";
	private static final String BCCEMAIL = "BCCEMAIL";

	private static final String SHARE_WISHLIST = "SHARE_WISHLIST";

	private static final String GPXPRESS_REDIRECT_URL = "gpxpress.redirect.url";
	private static final String EMPTY_DISPLAY_NAME="";
	private static final String SHARE_LIST_EMAIL = "true";
	private String customerType;

	private DefaultGPEmailGenerationService defaultGPEmailGenerationService;
	private DefaultGPEmailService defaultGPEmailService;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	private String siteUid;
	private String subject;
	private String senderMessage;
	private Boolean soldTo;
	private Boolean addLink;
	private String gpxpressRedirectUrl;
	
	public String getSiteUid() {
		return siteUid;
	}
	StringBuilder csvContent = new StringBuilder();
	private List<EmailAttachmentModel> attachments;

	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}
	public void setAttachments(final List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}
	@Override
	public void init(final WishlistProcessModel wishlistProcessModel, final EmailPageModel emailPageModel)
	{
		LOG.debug("Inside Share wislist email Context");
		super.init(wishlistProcessModel, emailPageModel);
		wishlistData = getWishlistConverter().convert(wishlistProcessModel.getWishlist());
		siteUid = getBaseSiteService().getCurrentBaseSite().getUid();
		UserModel userModel = wishlistProcessModel.getWishlist().getUser();
		if(userModel==null) {
			userModel=wishlistProcessModel.getUser();
		}
		if (userModel instanceof CustomerModel) {
			customerType = ((CustomerModel) userModel).getType() != null ? ((CustomerModel) userModel).getType().getCode() : CustomerType.GUEST.getCode();
			put("customerType", customerType);
		}
		
		if(null !=cmsSiteService.getSiteConfig(SHARE_WISHLIST_ENABLED) && Boolean.valueOf(cmsSiteService.getSiteConfig(SHARE_WISHLIST_ENABLED)))
		{
			put(FROM_EMAIL,wishlistProcessModel.getSenderEmail());
			put(FROM_DISPLAY_NAME, wishlistProcessModel.getSenderName());
			put(BCCEMAIL,wishlistProcessModel.getSenderEmail());
			put(DISPLAY_NAME,EMPTY_DISPLAY_NAME);
			if(wishlistProcessModel.getAttachPDF()) {
				setAttachments(wishlistProcessModel.getEmailAttachmentModelList());
			}
		}
		else 
		{
			CustomerModel customer = getCustomer(wishlistProcessModel);
			
			if (!StringUtils.isEmpty(customer.getDisplayName()) && customer.getDisplayName().contains("|")) {
				final String displayName = customer.getDisplayName();
				final String[] splittedName = displayName.split(GpcommerceCoreConstants.SITE_DELIMITER);
				put(DISPLAY_NAME,splittedName[0]);
			}
			try {
				generateCsv(wishlistProcessModel.getWishlist().getWishlistUid());
			} catch (final IOException e) {
				LOG.error(e.getMessage(),e);
			}
		}
		String[] recipientEmails = null;
		final List<EmailAddressModel> toEmails = new ArrayList<>();
		EmailAddressModel toAddress;
		String rEmail= wishlistProcessModel.getToEmail();
		rEmail = rEmail.replaceAll("\\[", "").replaceAll("\\]","");
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
		
		setGpxpressRedirectUrl(configurationService.getConfiguration().getString(GPXPRESS_REDIRECT_URL));
		
		put(SHARE_WISHLIST, true);
		put(ATTACHMENTS, getAttachments());
		put(EMAIL, rEmail);
		put(RECIPIENTS_EMAIL_ID, toEmails);
		
		senderMessage= wishlistProcessModel.getSendersMessage();
		soldTo = wishlistProcessModel.getSoldTo();
		subject = wishlistProcessModel.getSubject();
		addLink = wishlistProcessModel.getAddLink();
		
	}
	public void generateCsv(final String wishlistId) throws IOException {
		final StringWriter writer = new StringWriter();
		final List<String> headers = new ArrayList<>();
		String fileName =null;


		headers.add("Material Number (SKU)");
		headers.add("Product Name");
		headers.add("Product Quantity");

		if (null!=wishlistData && CollectionUtils.isNotEmpty(wishlistData.getWishlistEntries()))
		{
			int i = 0;
			for (; i < headers.size() - 1; i++)
			{
				csvContent.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(DELIMITER);
			}
			csvContent.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(LINE_SEPERATOR);
			writer.write(csvContent.toString());
			final List<WishlistEntryData> entries = wishlistData.getWishlistEntries();

			for (final WishlistEntryData entry : entries)
			{

					writeWishlistEntry(writer, entry);
			}
		}
		if(null!=wishlistData && null!=wishlistData.getName())
		{
			fileName = WISHLIST + "_" + wishlistData.getName() + UUID.randomUUID();
		}
		else
		{
			fileName = WISHLIST + "_" + UUID.randomUUID();
		}
		final File temp = new File(fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION);

		try (FileWriter fileWriter = new FileWriter(fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION);
				BufferedWriter bw = new BufferedWriter(fileWriter))
		{
            bw.write(csvContent.toString());
			}catch (final IOException e) {
				LOG.error(e.getMessage(),e);
			}


		final DataInputStream in = new DataInputStream(new FileInputStream(temp));
		final EmailAttachmentModel attachment = getDefaultGPEmailService().createEmailAttachment(in,
				fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION, GpcommerceFacadesConstants.CSV_FILE_TYPE);
		attachment.setRealFileName(fileName);
		final List<EmailAttachmentModel> emailAttachment = new ArrayList<>();
		emailAttachment.add(attachment);
		setAttachments(emailAttachment);
		temp.delete();
	}

	protected  void writeWishlistEntry(final Writer writer, final WishlistEntryData entry) throws IOException
	{
		csvContent.append(StringEscapeUtils.escapeCsv(entry.getProduct().getCode())).append(DELIMITER)
				.append(StringEscapeUtils.escapeCsv(entry.getProduct().getName())).append(DELIMITER)
				.append(StringEscapeUtils.escapeCsv(entry.getQuantity().toString())).append(DELIMITER).append(LINE_SEPERATOR);
		writer.write(csvContent.toString());

	}
	@Override
	protected BaseSiteModel getSite(final WishlistProcessModel wishlistProcessModel) {

		return wishlistProcessModel.getSite();
	}
	@Override
	protected CustomerModel getCustomer(final WishlistProcessModel wishlistProcessModel) {
		UserModel user = wishlistProcessModel.getWishlist().getUser();
		if(null==user) {
			user = wishlistProcessModel.getUser();
		}
		return (CustomerModel)user ;
	}
	@Override
	protected LanguageModel getEmailLanguage(final WishlistProcessModel wishlistProcessModel) {

		return null;
	}
	public String getSecureWishlistUrl() throws UnsupportedEncodingException
	{
		String path = null;
		if(WishlistTypeEnum.PRE_CURATED_LIST.equalsIgnoreCase(wishlistData.getWishlistType())) {
			path = PRODUCTLIST+LIST_NAME+wishlistData.getWishlistUid();
		}else {
			path = SHARELIST+LIST_NAME+wishlistData.getWishlistUid();
		}
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, path);
	}
	
	public String getDisplayListDetailsUrl() throws UnsupportedEncodingException
	{
		String path= LIST_DETAILS + LIST_NAME +wishlistData.getWishlistUid(); 
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),getUrlEncodingAttributes(), true, path, "isShareList=" + SHARE_LIST_EMAIL );
	}
	
	public String getDisplaySecureWishlistUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, "/wishlist");
	}

	public String getSecureWishlistUrlB2C() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, "/sharefavourite/?listName="+wishlistData.getWishlistUid());
	}
	
	public String getDisplaySecureWishlistUrlB2C() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, "/sharefavourite");
	}
	
	public Converter<Wishlist2Model, WishlistData> getWishlistConverter() {
		return wishlistConverter;
	}

	public void setWishlistConverter(final Converter<Wishlist2Model, WishlistData> wishlistConverter) {
		this.wishlistConverter = wishlistConverter;
	}

	public WishlistData getWishlistData() {
		return wishlistData;
	}

	public void setWishlistData(final WishlistData wishlistData) {
		this.wishlistData = wishlistData;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	public DefaultGPEmailGenerationService getDefaultGPEmailGenerationService() {
		return defaultGPEmailGenerationService;
	}

	public void setDefaultGPEmailGenerationService(final DefaultGPEmailGenerationService defaultGPEmailGenerationService) {
		this.defaultGPEmailGenerationService = defaultGPEmailGenerationService;
	}
	public DefaultGPEmailService getDefaultGPEmailService() {
		return defaultGPEmailService;
	}
	public void setDefaultGPEmailService(final DefaultGPEmailService defaultGPEmailService) {
		this.defaultGPEmailService = defaultGPEmailService;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	public I18NService getI18nService() {
		return i18nService;
	}
	public void setI18nService(final I18NService i18nService) {
		this.i18nService = i18nService;
	}
	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}
	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(final String customerType) {
		this.customerType = customerType;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSenderMessage() {
		return senderMessage;
	}
	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}
	public Boolean getSoldTo() {
		return soldTo;
	}
	public void setSoldTo(Boolean soldTo) {
		this.soldTo = soldTo;
	}
	public Boolean getAddLink() {
		return addLink;
	}
	public void setAddLink(Boolean addLink) {
		this.addLink = addLink;
	}
	public String getGpxpressRedirectUrl() {
		return gpxpressRedirectUrl;
	}
	public void setGpxpressRedirectUrl(String gpxpressRedirectUrl) {
		this.gpxpressRedirectUrl = gpxpressRedirectUrl;
	}
}
