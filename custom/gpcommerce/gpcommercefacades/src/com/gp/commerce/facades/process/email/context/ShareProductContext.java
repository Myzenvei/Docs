/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import com.gp.commerce.core.model.ShareProductEmailProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.product.data.ProductResourcesVideosData;
import com.gp.commerce.facades.product.impl.GPDefaultProductFacade;
import com.gp.commerce.facades.product.references.data.GPShareProductData;

/**
 * Share Product Email Process Context
 *
 */
public class ShareProductContext extends GPAbstractEmailContext<ShareProductEmailProcessModel> {
	private String subject;
	private String productURL;
	private String productCode;
	private String productName;
	private String productDescription;
	private String emailAttachmentName;
	private String emailDownloadURL;
    private List<EmailAttachmentModel> attachments;
    private Boolean isAttached;
    private Boolean addLink;
    private ProductResourcesVideosData videos;
    public ProductResourcesVideosData getVideos() {
		return videos;
	}
	public void setVideos(ProductResourcesVideosData videos) {
		this.videos = videos;
	}
	
	private String message;


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	private static final String PRODUCT_IMAGE = "image";
    private Converter<ProductModel, ProductData> emailProductConverter;
    @Resource
    private ProductData productData;

   @Resource
    private GPShareProductData shareProductData;
   
   
   @Resource(name = "gpProductFacade")
	private GPDefaultProductFacade gpProductFacade;

    
	public GPShareProductData getShareProductData() {
	return shareProductData;
}
public void setShareProductData(GPShareProductData shareProductData) {
	this.shareProductData = shareProductData;
}
	public Boolean getIsAttached() {
		return isAttached;
	}
	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}
	public void setAttachments(final List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}
	public String getEmailDownloadURL() {
		return emailDownloadURL;
	}

	public String getEmailAttachmentName() {
		return emailAttachmentName;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getProductURL() {
		return productURL;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getSubject() {
		return subject;
	}

	public Boolean getAddLink() {
		return addLink;
	}
	public void setAddLink(final Boolean addLink) {
		this.addLink = addLink;
	}


	private static final Logger LOG = Logger.getLogger(ShareProductContext.class);
	private static final String VIDEO_NOT_EMPTY = "videoNotEmpty";
	private static final String ATTACHMENT_SHARE_PRODUCT = "ATTACHMENT_SHARE_PRODUCT";
	private static final String SHARE_PRODUCT = "SHARE_PRODUCT";
	private static final String SOLD_TO = "soldTo";
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	
	/**
	 * To initialize the email process. putting values in context, so that these
	 * values become accessible in email
	 *
	 * @param shareProductEmailProcessModel
	 * @param emailPageModel
	 */
	@Override
	public void init(final ShareProductEmailProcessModel shareProductEmailProcessModel, final EmailPageModel emailPageModel) {
		if(LOG.isDebugEnabled()){
			LOG.debug("Method: init " + shareProductEmailProcessModel + emailPageModel);
		}
		super.init(shareProductEmailProcessModel, emailPageModel);
		put(EMAIL, String.join(",", shareProductEmailProcessModel.getRecipientEmails()));
		put(DISPLAY_NAME, shareProductEmailProcessModel.getSenderName());
		put(FROM_EMAIL, shareProductEmailProcessModel.getSenderEmail());
		put(FROM_DISPLAY_NAME, shareProductEmailProcessModel.getSenderName());
		put(SOLD_TO, shareProductEmailProcessModel.getSoldTo());
		attachments = shareProductEmailProcessModel.getEmailAttachmentModelList();
		put(ATTACHMENT_SHARE_PRODUCT, getAttachments());
		put(SHARE_PRODUCT, true);
		productURL = shareProductEmailProcessModel.getProductPageURL();
		productCode = shareProductEmailProcessModel.getPdtCode();
		subject = shareProductEmailProcessModel.getSubject();
		productName = shareProductEmailProcessModel.getPdtName();
		productDescription = shareProductEmailProcessModel.getProductDescription();
		addLink = shareProductEmailProcessModel.getAddLink();
		message=shareProductEmailProcessModel.getSenderMessage();
		
		if(CollectionUtils.isNotEmpty(shareProductEmailProcessModel.getProducts()))
		{
			productCode=shareProductEmailProcessModel.getProducts().iterator().next().getCode();
		}
		

		Map<String, String> shareProduct = new HashMap<>();
		shareProduct=shareProductEmailProcessModel.getShareProductMap();
		
		
		if (CollectionUtils.isNotEmpty(shareProductEmailProcessModel.getEmailAttachmentModelList()) && shareProductEmailProcessModel.getEmailAttachmentModelList().get(0) != null) {
			isAttached = true;
			emailAttachmentName = shareProductEmailProcessModel.getEmailAttachmentModelList().get(0).getRealFileName();
			if(LOG.isDebugEnabled()){
				LOG.debug("Method: init " + emailAttachmentName);
			}
			emailDownloadURL = shareProductEmailProcessModel.getEmailAttachmentModelList()
					.get(0).getDownloadURL();
			if(LOG.isDebugEnabled()){
				LOG.debug("Method: init " + emailDownloadURL);
			}
		}else {
			isAttached = false;
		}
		Collection<ProductData> entries = new ArrayList<ProductData>();
		for (ProductModel product : shareProductEmailProcessModel.getProducts()) {

			ProductData productData = getEmailProductConverter().convert(product);
			gpProductFacade.populateKnwoledgeCenterData(productData);
			productData.setPdtCode(product.getCode());
			productData.setProductDescription(product.getDescription());
			productData.setPdtName(product.getName());
			productData.setProductURL(shareProduct.get(product.getCode()));
			
			if (null != productData) {
				videos = productData.getProductResourceVideos();
				if (null!=videos) {
					put(VIDEO_NOT_EMPTY, "notEmpty");
					productData.setVideoEmpty("notEmpty");

				}


				if (null != productData.getImages()) {
					productData.getImages().forEach(image -> {
						if (image.getImageType().equals(ImageDataType.PRIMARY)
								&& "thumbnail".equals(image.getFormat())) {
							put(PRODUCT_IMAGE, image);
							productData.setShareProdImage(image);
						}
					});
				} else {
					if (LOG.isDebugEnabled()) {
						LOG.debug("ShareProductContext | No images found for product :" + productCode);
					}
				}
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("ShareProductContext | No product found");
				}
			}

			entries.add(productData);
		}

		shareProductData.setEntries(entries);
	}

	
	public Converter<ProductModel, ProductData> getEmailProductConverter() {
		return emailProductConverter;
	}
	public void setEmailProductConverter(Converter<ProductModel, ProductData> emailProductConverter) {
		this.emailProductConverter = emailProductConverter;
	}
	@Override
	protected BaseSiteModel getSite(final ShareProductEmailProcessModel shareProductEmailProcessModel) {
		return shareProductEmailProcessModel.getSite();
	}
	@Override
	protected CustomerModel getCustomer(final ShareProductEmailProcessModel shareProductEmailProcessModel) {
		return (CustomerModel) shareProductEmailProcessModel.getUser();
	}
	@Override
	protected LanguageModel getEmailLanguage(final ShareProductEmailProcessModel shareProductEmailProcessModel) {
		return shareProductEmailProcessModel.getSite().getDefaultLanguage();
	}

}
