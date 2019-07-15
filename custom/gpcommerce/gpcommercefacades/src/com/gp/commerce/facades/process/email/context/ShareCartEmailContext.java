/**
 *
 */
package com.gp.commerce.facades.process.email.context;

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

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.CartProcessModel;
import com.gp.commerce.core.product.services.impl.GPB2BDefaultProductService;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.impl.DefaultGPEmailGenerationService;
import com.gp.commerce.core.services.impl.DefaultGPEmailService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.WishlistData;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

/**
 * @author dadidam
 *
 */
public class ShareCartEmailContext extends GPAbstractEmailContext<CartProcessModel>
{
	private static final Logger LOG = Logger.getLogger(ShareCartEmailContext.class);

	private Converter<CartModel, CartData> emailCartConverter;
	private CartData cartData;
	private EmailService emailService;
	public static final String LINE_SEPERATOR = "\n";
	public static final String DELIMITER = ",";

	private static final String SHARE_CART = "SHARE_CART";

	private static final String ATTACHMENTS = "ATTACHMENTS";

	private static final String RECIPIENTS_EMAIL_ID = "RECIPIENTS_EMAIL_ID";
	public static final String DISPLAY_NAME = "displayName";
	public static final String EMAIL = "email";
	public static final String FROM_EMAIL = "fromEmail";
	public static final String FROM_DISPLAY_NAME = "fromDisplayName";
	private String customerType;
	
	@Resource
	private Converter<Wishlist2Model, WishlistData> wishlistConverter;
	@Resource
	private WishlistData wishlistData;

	private DefaultGPEmailGenerationService defaultGPEmailGenerationService;
	private DefaultGPEmailService defaultGPEmailService;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "gpB2BProductService")
	private GPB2BDefaultProductService gpB2BProductService;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	@Resource(name = "mediaService")
	private MediaService mediaService;
	
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	
	private Converter<AddressModel, AddressData> addressConverter;

	private static final String GPEXPRESS = "gpxpress";
	
	StringBuilder csvContent = new StringBuilder();
	private List<EmailAttachmentModel> attachments;

	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}
	public void setAttachments(final List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}
	@Override
	public void init(final CartProcessModel cartProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(cartProcessModel, emailPageModel);
		cartData = getEmailCartConverter().convert(cartProcessModel.getCart());
		
				wishlistData = wishlistConverter.convert(cartProcessModel.getWishlist());

		final UserModel userModel = cartProcessModel.getCart().getUser();
		if (userModel instanceof CustomerModel) {
			customerType = ((CustomerModel) userModel).getType() != null ? ((CustomerModel) userModel).getType().getCode() : CustomerType.GUEST.getCode();
			put("customerType", customerType);
		}
		
		/**YCOM-10892: Adding sold To Address and image to share cart email for GPExpress**/
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();
		if(currentSite.getUid().equalsIgnoreCase(GPEXPRESS)){
			if (userModel instanceof B2BCustomerModel) {
				final B2BUnitModel b2bUnitModel = ((B2BCustomerModel) userModel).getDefaultB2BUnit();
				if(null!=b2bUnitModel.getContactAddress()){
					put("soldToAddress", getAddressConverter().convert(b2bUnitModel.getContactAddress()));
				}
				if(StringUtils.isEmpty(b2bUnitModel.getDistributorImage())){
					MediaModel media = getMediaByCode("defaultSoldToLogoGpxpress");
					put("soldToImage",media.getURL());
				}else{
					put("distributorImage",b2bUnitModel.getDistributorImage());
				}
			}	
		}
		
		String[] recipientEmails = null;
		try {
			generateCsv(cartProcessModel.getCart());
		}
		catch (final IOException e) {
              LOG.error(e.getMessage(),e);
		}
		final List<EmailAddressModel> toEmails = new ArrayList<>();
		EmailAddressModel toAddress;
		final String rEmail= cartProcessModel.getToEmail();
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

		put(SHARE_CART, true);
		put(DISPLAY_NAME, cartProcessModel.getSenderName());
		put(FROM_EMAIL, cartProcessModel.getSenderEmail());
		put(FROM_DISPLAY_NAME, cartProcessModel.getSenderName());
		put(ATTACHMENTS, getAttachments());
		put(EMAIL, cartProcessModel.getToEmail());
		put(RECIPIENTS_EMAIL_ID, toEmails);
	}
	
		/**
	 * @param mediaCode
	 * @return
	 */
	protected MediaModel getMediaByCode(final String mediaCode)
	{
		if (StringUtils.isNotEmpty(mediaCode))
		{
			for (final CatalogVersionModel catalogVersionModel : getCatalogVersionService().getSessionCatalogVersions())
			{
				final MediaModel media = getMediaByCodeAndCatalogVersion(mediaCode, catalogVersionModel);
				if (media != null)
				{
					return media;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @param mediaCode
	 * @param catalogVersionModel
	 * @return
	 */
	protected MediaModel getMediaByCodeAndCatalogVersion(final String mediaCode, final CatalogVersionModel catalogVersionModel)
	{
		try
		{
			return getMediaService().getMedia(catalogVersionModel, mediaCode);
		}
		catch (final UnknownIdentifierException ignore)
		{
			// Ignore this exception
		}
		return null;
	}

	public void generateCsv(final CartModel cart) throws IOException
	{
		final String cartId = cart.getCode();
		final StringWriter writer = new StringWriter();
		final List<String> headers = new ArrayList<>();
		final String siteChannel = getBaseSiteService().getCurrentBaseSite().getChannel().toString();

		headers.add("Material Number (SKU)");
		headers.add("Product Name");
		headers.add("Product Quantity");

		if(null != siteChannel && (GpcommerceCoreConstants.B2B.equalsIgnoreCase(siteChannel))) {
 			headers.add("CUST#");
 			headers.add("UPC");
 		}
		if (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries()))
		{
			int i = 0;
			for (; i < headers.size() - 1; i++)
			{
				csvContent.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(DELIMITER);
			}
			csvContent.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(LINE_SEPERATOR);
			writer.write(csvContent.toString());
			final List<OrderEntryData> entries = cartData.getEntries(); 
			String cmir = null;
			for (final OrderEntryData entry : entries)
			{
				if (cart.getUser() instanceof B2BCustomerModel)
				{
					final B2BCustomerModel customer = (B2BCustomerModel) cart.getUser();
					if (null != customer && null != customer.getDefaultB2BUnit())
					{
						cmir = gpB2BProductService.getCMIRCodeForProductAndB2BUnit(entry.getProduct().getCode(),
								customer.getDefaultB2BUnit().getUid());
					}
				}

				if (Boolean.TRUE.equals(entry.getProduct().getMultidimensional()))
				{
					for (final OrderEntryData subEntry : entry.getEntries())
					{
						writeOrderEntry(writer, subEntry, siteChannel, cmir);
					}
				}
				else
				{
					writeOrderEntry(writer, entry, siteChannel, cmir);
				}
			}
		}

		final String directoryPath = Config.getParameter(GpcommerceFacadesConstants.CART_EMAIL);
		final File dir = new File(directoryPath);
		final String fileName = "cart"+ "_" + cartId + System.currentTimeMillis();
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
		final EmailAttachmentModel attachment = getDefaultGPEmailService().createEmailAttachment(in,
				fileName + GpcommerceFacadesConstants.CSV_FILE_EXTENSION, GpcommerceFacadesConstants.CSV_FILE_TYPE);
		final List<EmailAttachmentModel> attachmentsList = new ArrayList<>();
		attachmentsList.add(attachment);
		setAttachments(attachmentsList);
		temp.delete();
	}

	protected void writeOrderEntry(final Writer writer, final OrderEntryData entry, final String siteChannel,
			String cmir) throws IOException
	{
		if (GpcommerceCoreConstants.B2C.equalsIgnoreCase(siteChannel)
				|| GpcommerceCoreConstants.EMPSTORE.equalsIgnoreCase(siteChannel))
		{
			csvContent.append(StringEscapeUtils.escapeCsv(entry.getProduct().getCode())).append(DELIMITER)
			.append(StringEscapeUtils.escapeCsv(entry.getProduct().getName())).append(DELIMITER)
			.append(StringEscapeUtils.escapeCsv(entry.getQuantity().toString())).append(DELIMITER).append(LINE_SEPERATOR);
			writer.write(csvContent.toString());
		}
	else if(GpcommerceCoreConstants.B2B.equalsIgnoreCase(siteChannel))

	{
			cmir = (null != cmir) ? cmir : StringUtils.EMPTY;
			final String ean = (null != entry.getProduct().getEan()) ? entry.getProduct().getEan() : StringUtils.EMPTY;

			csvContent.append(StringEscapeUtils.escapeCsv(entry.getProduct().getCode())).append(DELIMITER)
					.append(StringEscapeUtils.escapeCsv(entry.getProduct().getName())).append(DELIMITER)
					.append(StringEscapeUtils.escapeCsv(entry.getQuantity().toString())).append(DELIMITER)
					.append(StringEscapeUtils.escapeCsv(cmir)).append(DELIMITER).append(StringEscapeUtils.escapeCsv(ean))
					.append(DELIMITER).append(LINE_SEPERATOR);
			writer.write(csvContent.toString());
		}
	}

	
	public String getSecureWishlistUrl() throws UnsupportedEncodingException
 	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, "/sharelist/?listName="+wishlistData.getWishlistUid());
	}
	
	public String getDisplaySecureWishlistUrl() throws UnsupportedEncodingException
		{
			return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, "/sharecart");
	 	}
	 
		public String getSecureWishlistUrlB2C() throws UnsupportedEncodingException
		{
			return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, "/sharefavourite/?listName="+wishlistData.getWishlistUid());
		}
		
		public String getDisplaySecureWishlistUrlB2C() throws UnsupportedEncodingException
		{
			return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),"", true, "/sharecart");
		}
	
	
	
	@Override
	protected BaseSiteModel getSite(final CartProcessModel cartProcessModel) {

		return cartProcessModel.getCart().getSite();
	}
	@Override
	protected CustomerModel getCustomer(final CartProcessModel cartProcessModel) {

		return (CustomerModel) cartProcessModel.getCart().getUser();
	}
	@Override
	protected LanguageModel getEmailLanguage(final CartProcessModel cartProcessModel) {

		return null;
	}
	public String getSecureCartUrl() throws UnsupportedEncodingException
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), false, "/cart");
	}

	public CartData getCartData() {
		return cartData;
	}

	public void setCartData(final CartData cartData) {
		this.cartData = cartData;
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

	public GPB2BDefaultProductService getGpB2BProductService()
	{
		return gpB2BProductService;
	}

	public void setGpB2BProductService(final GPB2BDefaultProductService gpB2BProductService)
	{
		this.gpB2BProductService = gpB2BProductService;
	}
	public Converter<CartModel, CartData> getEmailCartConverter() {
		return emailCartConverter;
	}
	public void setEmailCartConverter(Converter<CartModel, CartData> emailCartConverter) {
		this.emailCartConverter = emailCartConverter;
	}
	
	public GPCMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}
	public void setCmsSiteService(GPCMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}
	public Converter<AddressModel, AddressData> getAddressConverter() {
		return addressConverter;
	}
	public void setAddressConverter(Converter<AddressModel, AddressData> addressConverter) {
		this.addressConverter = addressConverter;
	}
	public MediaService getMediaService() {
		return mediaService;
	}
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}
	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}
	public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
		this.catalogVersionService = catalogVersionService;
	}
	
}
