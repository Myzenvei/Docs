/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.wishlist.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.drools.core.util.IoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.util.WebUtils;

import com.gp.commerce.core.constants.GpErrorConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.WishlistTypeEnum;
import com.gp.commerce.core.exceptions.GPWishlistException;
import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.model.WishlistProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPPrecuratedListService;
import com.gp.commerce.core.services.event.ShareWishlistEvent;
import com.gp.commerce.core.services.impl.DefaultGPEmailService;
import com.gp.commerce.core.wishlist.services.GPWishlistService;
import com.gp.commerce.dto.wishlist.WishlistEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistListData;
import com.gp.commerce.dto.wishlist.WishlistMultipleEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import com.gp.commerce.facades.component.data.GPPreCurateddata;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;
import com.gp.commerce.facades.data.WishlistMultipleData;
import com.gp.commerce.facades.product.data.DataSheetsData;
import com.gp.commerce.facades.product.data.ProductResourcesVideosData;
import com.gp.commerce.facades.search.compare.GPSearchCompareProductsFacade;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import com.gp.commerece.facades.component.data.GPPrecuratedListData;
import com.gpintegration.knowledgecenter.dto.GPKnowledgeCenterSkuResponse;
import com.gpintegration.knowledgecenter.dto.InfoResourcesDTO;
import com.gpintegration.knowledgecenter.dto.SupportResourcesDTO;
import com.gpintegration.service.GPKnowledgeCenterService;

import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import sun.misc.BASE64Decoder;

/**
 * The Class GPDefaultWishlistFacade.
 */
public class GPDefaultWishlistFacade implements GPWishlistFacade {
	private static final Logger LOG = Logger.getLogger(GPDefaultWishlistFacade.class);
	private static final String ENTRY_TYPE = "CART";
	private static final String CUSTOMER_WISHLIST = "Customer Wishlist";
	private static final String PRECURATED_LIST = "PRE_CURATED_LIST";
	private static final String SORT_PARAM_NAME = "name";
	private static final String SORT_PARAM_MODIFIED_TIME = "modifiedTime";
	private static final String SORT_PARAM_NO_OF_PROD = "noOfProducts";
	private static final String SORT_PARAM_SUBTOTAL = "subtotal";
	private static final String DESC_SORT_ORDER = "descending";
	private static final String SUCCESS_MSG = "removed successfully";
	private static final String FALIURE_MSG = "Couldn't remove";
	private static final String SUCCESS_PROD_MSG = "Added Products Successfully";
	private static final String SUCCESS_ATTR_MSG = "Added Attributes Successfully";
	private static final String SUCCESS_SAVE_ATTR_MSG = "Attributes Saved Successfully";
	private static final String FALIURE_PROD_MSG = "Couldn't find product";
	private static final String SUCCESS_PROD_ADD_MSG = " item(s) added to : ";
	private static final String ERROR_PROD_ADD_MSG = " item(s) not added to : ";
	private static final String COMMA =",";
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String GUEST_COOKIE = "guestListUid";
	public static final String PDF_FILE_EXTENSION = ".pdf";
	public static final String PDF_FILE_TYPE = "application/pdf";
	public static final String WISHLIST_EMAIL = "cart.email.csv.filename";
	private static final String GPXPRESS_RETAILSOLDTO = "gpxpress.retail.soldto";
	private static final String WISHLIST = "Wishlist";
	private CommerceCartService commerceCartService;
	private Wishlist2Service wishlistService;
	private KeyGenerator guidKeyGenerator;
	private Converter<Wishlist2Model, WishlistData> wishlistConverter;
	private ProductService productService;
	private ModelService modelService;
	private BaseSiteService baseSiteService;
	private GPWishlistService gpWishlistService;
	private Converter<ProductModel, ProductData> productConverter;
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;
	private List<EmailAttachmentModel> attachments;
	@Resource(name="defaultGPEmailService")
	private DefaultGPEmailService defaultGPEmailService;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "gpPrecuratedListService")
	private GPPrecuratedListService gpPrecuratedListService;
	@Resource(name = "gpPrecuratedListConverter")
	private Converter<WishlistData, GPPreCurateddata> gpPrecuratedListConverter;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Autowired
	private EventService eventService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "gpKnowledgeCenterService")
	GPKnowledgeCenterService gpKnowledgeCenterService;
	@Resource(name = "searchCompareProductsFacade")
	private GPSearchCompareProductsFacade searchCompareProductsFacade;
	@Resource(name = "userService")
	private UserService userService;
	
	public GPWishlistService getGpWishlistService() {
		return gpWishlistService;
	}
	@Resource(name = "gpProductGalleryImagesPopulator")
	private Populator<ProductModel, ProductData> gpProductGalleryImagesPopulator;

	@Required
	public void setGpWishlistService(final GPWishlistService gpWishlistService) {
		this.gpWishlistService = gpWishlistService;
	}

	@Override
	public WishlistData createWishlist(final String name, final String description) {
		final Wishlist2Model wishlist2Model = getGpWishlistService().createWishlistWithGUID(name, CUSTOMER_WISHLIST, Boolean.FALSE);
		return getWishlistConverter().convert(wishlist2Model);
	}

	@Override
	public WishlistData getWishlist(final String uuid, HttpServletRequest request, HttpServletResponse response,boolean pdfImage) {
		Wishlist2Model wishlist2Model = this.getWishListForuuid(uuid);
		if(wishlist2Model !=null) {
			LOG.debug("Wishlist Model not empty");
		} else {
			wishlist2Model = getGpWishlistService().getGuestWishList(uuid, userService.getCurrentUser());
			if(null != request) {
				final Cookie guestCookie = WebUtils.getCookie(request, GUEST_COOKIE);
				if (guestCookie != null) {
					guestCookie.setValue(null);
					guestCookie.setMaxAge(0);
					guestCookie.setPath("/");
					response.addCookie(guestCookie);
				}
			}
		}
		wishlist2Model.setPdfImage(pdfImage);
		WishlistData data = getWishlistConverter().convert(wishlist2Model);
		List<WishlistEntryData> wishliistEntryList= data.getWishlistEntries();
		for(WishlistEntryData wishListData:wishliistEntryList ) {
			ProductData productdata=wishListData.getProduct();
			final ProductModel product = getProductService().getProductForCode(productdata.getCode());
			gpProductGalleryImagesPopulator.populate(product, productdata);
			if(productdata.getAssetCode()!=null) { 
				productdata.setSpecifications(searchCompareProductsFacade.getSpecifications(productdata));
			}
		}
		return data;
	}
	
	/**
	 * Gets the wishlist for specific UUID.
	 * @param uuid the uuid
	 * @return the wish list foruuid
	 */
	private Wishlist2Model getWishListForuuid(final String uuid) {
		final List<Wishlist2Model> wishlist2Models = getWishlistService().getWishlists();
		if (CollectionUtils.isNotEmpty(wishlist2Models)) {
			return wishlist2Models.stream().filter(list -> uuid.equals(list.getWishlistUid())).findFirst().orElse(null);
		}
		return null;
	}

	@Override
	public WishlistListData getAllWishlist() {
		final List<Wishlist2Model> wishlist2Models = getWishlistService().getWishlists();
		final WishlistListData dataList = new WishlistListData();
		if (CollectionUtils.isNotEmpty(wishlist2Models)) {
			dataList.setWishlists(getWishlistConverter().convertAll(wishlist2Models));
		}
		return dataList;
	}

	/**
	 * Add product to wishlist
	 * @param productModels
	 * @param wishlist2Model
	 */
	void addProductToList(final List<ProductModel> productModels, final Wishlist2Model wishlist2Model) {
		for (final ProductModel pModel : productModels) {
			getWishlistService().addWishlistEntry(wishlist2Model, getGpWishlistService().createWishlistEntry(wishlist2Model, pModel));
		}
	}

	@Override
	public WishlistData removeWishlistEntryForProduct(final String productCode, final String uuid) {
		final WishlistData wd = new WishlistData();
		try {
			getWishlistService().removeWishlistEntryForProduct(getProductService().getProductForCode(productCode), getWishListForuuid(uuid));
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(SUCCESS_MSG);
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
			wd.setStatus(GpcommerceFacadesConstants.FAILURE);
			wd.setDescription(FALIURE_MSG);
		}
		return wd;
	}

	@Override
	public WishlistData removeWishlist(final String uuid) {
		final Wishlist2Model wishlist = getWishListForuuid(uuid);
		final WishlistData wd = new WishlistData();
		if (null != wishlist) {
			getModelService().remove(wishlist);
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(SUCCESS_MSG);
		} else {
			wd.setStatus(GpcommerceFacadesConstants.FAILURE);
			wd.setDescription(FALIURE_MSG);
		}
		return wd;
	}

	@Override
	public GPPrecuratedListData getPrecuratedWishList() {
		final GPPrecuratedListData gpPrecuratedListData = new GPPrecuratedListData();
		final List<GPPreCurateddata> gpPrecuratedDataList = new ArrayList<>();
		final List<Wishlist2Model> precuratedWishList = gpPrecuratedListService.getPrecuratedWishList();
		if (CollectionUtils.isNotEmpty(precuratedWishList)) {
			final List<WishlistData> wishlists = getWishlistConverter().convertAll(precuratedWishList);
			for (final WishlistData wishlistData : wishlists) {
				gpPrecuratedDataList.add(gpPrecuratedListConverter.convert(wishlistData));
			}
			gpPrecuratedListData.setPrecuratedList(gpPrecuratedDataList);
			return gpPrecuratedListData;
		}
		return null;
	}

	/**
	 * Remove the selected wish list by passing multiple selected IDs.
	 * @param multiplewishlistuuid the multiplewishlistuuid
	 * @throws GPWishlistException the GP wishlist exception
	 */
	@Override
	public void removeSelectedWishlists(final List<String> multiplewishlistuuid) throws GPWishlistException {
		gpWishlistService.removeSelectedWishlists(multiplewishlistuuid);
	}
	
	/**
	 * Update the quantity for specific product in wish list entry.
	 * @param dto the dto
	 * @return the wishlist data
	 */
	@Override
	public WishlistData updateWishListEntryForQuantity(final WishlistEntryRequestDTO dto) {
		return gpWishlistService.updateWishListEntryForQuantity(dto);
	}

	/**
	 * @see GPWishlistFacade#addItemsToWishlist(WishlistWsDTO) Adds multiple products to wishlist in single service
	 */
	@Override
	public WishlistData addItemsToWishlist(final WishlistWsDTO wishlistWsDTO) {
		final WishlistData wishListData = new WishlistData();
		final List<WishlistEntryRequestDTO> wishlistEntries = wishlistWsDTO.getWishlistEntries();
		final Wishlist2Model wishlistModel = getWishListForuuid(wishlistWsDTO.getWishlistUid());
		Wishlist2EntryModel wishlistEntry = null;
		if (null != wishlistModel) {
			for (final WishlistEntryRequestDTO entry : wishlistEntries) {
				ProductModel product;
				try {
					product = getProductService().getProductForCode(entry.getProduct().getCode());
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
					wishListData.setStatus(GpcommerceFacadesConstants.FAILURE);
					wishListData.setDescription(FALIURE_PROD_MSG + " " + entry.getProduct().getCode());
					return wishListData;
				}
				// Check for the existing wishlist entry for the product. Add if no wishlist entry exists for the product
				if (!wishlistModel.getEntries().stream().anyMatch(wishlistEntryModel -> wishlistEntryModel.getProduct()
						.getCode().equalsIgnoreCase(entry.getProduct().getCode()))) {
					wishlistEntry = getGpWishlistService().createWishlistEntry(wishlistModel, product);
					wishlistEntry.setQuantity(entry.getQuantity());
					getWishlistService().addWishlistEntry(wishlistModel, wishlistEntry);
				} else {
					for (final Wishlist2EntryModel wishlistEntryModel : wishlistModel.getEntries()) {
						if (wishlistEntryModel.getProduct().getCode().equalsIgnoreCase(product.getCode())) {
							wishlistEntryModel.setQuantity(wishlistEntryModel.getQuantity() + entry.getQuantity());
							getGpWishlistService().updateWishlist(wishlistEntryModel);
							break;
						}
					}
				}
			}
			wishListData.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wishListData.setDescription(SUCCESS_PROD_MSG);
		} else {
			throw new GPWishlistException(GpcommerceFacadesConstants.ADD_WISHLIST_ERROR_CODE, "Wishlist is not found to add items");
		}
		return wishListData;
	}

	public WishlistData getFavorites() {
		final Wishlist2Model wishlist2Model = getGpWishlistService().getWishlistByName(userService.getCurrentUser(), GpcommerceCoreConstants.FAVORITES_WISHLIST);
		if (null != wishlist2Model) {
			return getWishlistConverter().convert(wishlist2Model);
		} else {
			final WishlistData data = this.createWishlist(GpcommerceCoreConstants.FAVORITES_WISHLIST, GpcommerceCoreConstants.FAVORITES_WISHLIST_DESC);
			return data;
		}
	}

	protected Wishlist2Service getWishlistService() {
		return wishlistService;
	}
	@Required
	public void setWishlistService(final Wishlist2Service wishlistService) {
		this.wishlistService = wishlistService;
	}
	protected Converter<Wishlist2Model, WishlistData> getWishlistConverter() {
		return wishlistConverter;
	}
	@Required
	public void setWishlistConverter(final Converter<Wishlist2Model, WishlistData> wishlistConverter) {
		this.wishlistConverter = wishlistConverter;
	}
	protected ProductService getProductService() {
		return productService;
	}
	@Required
	public void setProductService(final ProductService productService) {
		this.productService = productService;
	}
	public KeyGenerator getGuidKeyGenerator() {
		return guidKeyGenerator;
	}
	@Required
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator) {
		this.guidKeyGenerator = guidKeyGenerator;
	}
	public ModelService getModelService() {
		return modelService;
	}
	@Required
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}
	public CommerceCartService getCommerceCartService() {
		return commerceCartService;
	}
	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService) {
		this.commerceCartService = commerceCartService;
	}
	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	@Override
	public WishlistListData sortWishlistsByParam(final String sortParam, final String sortOrder) {
		List<Wishlist2Model> wishlist2Models = null;
		WishlistListData dataList = null;
		wishlist2Models = getWishlistService().getWishlists();
		dataList = new WishlistListData();
		if (StringUtils.isNotEmpty(sortParam) && CollectionUtils.isNotEmpty(wishlist2Models)) {
			if (SORT_PARAM_NAME.equals(sortParam)) {
				sortWishlistsByName(wishlist2Models, sortOrder, dataList);
			} else if (SORT_PARAM_MODIFIED_TIME.equals(sortParam)) {
				sortWishlistsByModTime(wishlist2Models, sortOrder, dataList);
			} else if (SORT_PARAM_NO_OF_PROD.equals(sortParam)) {
				sortWishlistsByProdNo(wishlist2Models, sortOrder, dataList);
			} else if (SORT_PARAM_SUBTOTAL.equals(sortParam)) {
				sortWishlistsBySubtotal(wishlist2Models, sortOrder, dataList);
			}
		} else {
			throw new GPWishlistException(GpErrorConstants.SORT_FAILED_CODE, GpErrorConstants.SORT_FAILED_MSG);
		}
		return dataList;
	}

	/**
	 * Method to sort the list by list subtotal and sort order
	 * @param wishlist2Models
	 * @param sortOrder
	 * @param dataList
	 */
	private void sortWishlistsBySubtotal(final List<Wishlist2Model> wishlist2Models, final String sortOrder,
			final WishlistListData dataList) {
		final List<WishlistData> sortBySubtotalList = getWishlistConverter().convertAll(wishlist2Models);
		final List<WishlistData> wishlists = new ArrayList<>(sortBySubtotalList);
		if (DESC_SORT_ORDER.equals(sortOrder)) {
			wishlists.sort(Comparator.comparing(WishlistData::getSubtotal).reversed());
		} else {
			wishlists.sort(Comparator.comparing(WishlistData::getSubtotal));
		}
		dataList.setWishlists(wishlists);
	}
	/**
	 * Method to sort the list by no. of products in the list and sort order
	 * @param wishlist2Models
	 * @param sortOrder
	 * @param dataList
	 */
	private void sortWishlistsByProdNo(final List<Wishlist2Model> wishlist2Models, final String sortOrder,
			final WishlistListData dataList) {
		final List<Wishlist2Model> wishlists = new ArrayList<>(wishlist2Models);
		if (DESC_SORT_ORDER.equals(sortOrder)) {
			wishlists.sort((o1, o2) -> Integer.valueOf(o2.getEntries().size()).compareTo(o1.getEntries().size()));
		} else {
			wishlists.sort((o1, o2) -> Integer.valueOf(o1.getEntries().size()).compareTo(o2.getEntries().size()));
		}
		dataList.setWishlists(getWishlistConverter().convertAll(wishlists));
	}
	/**
	 * Method to sort the list by last modified time and sort order
	 * @param wishlist2Models
	 * @param sortOrder
	 * @param dataList
	 */
	private void sortWishlistsByModTime(final List<Wishlist2Model> wishlist2Models, final String sortOrder,
			final WishlistListData dataList) {
		final List<Wishlist2Model> wishlists = new ArrayList<>(wishlist2Models);
		if (DESC_SORT_ORDER.equals(sortOrder)) {
			wishlists.sort(Comparator.comparing(Wishlist2Model::getModifiedtime).reversed());
		} else {
			wishlists.sort(Comparator.comparing(Wishlist2Model::getModifiedtime));
		}
		dataList.setWishlists(getWishlistConverter().convertAll(wishlists));
	}

	/**
	 * Method to sort the list by list name and sort order
	 * @param wishlist2Models
	 * @param sortOrder
	 * @param dataList
	 */
	private void sortWishlistsByName(final List<Wishlist2Model> wishlist2Models, final String sortOrder,
			final WishlistListData dataList) {
		final List<Wishlist2Model> wishlists = new ArrayList<>(wishlist2Models);
		if (DESC_SORT_ORDER.equals(sortOrder)) {
			wishlists.sort(Comparator.comparing(Wishlist2Model::getName).reversed());
		} else {
			wishlists.sort(Comparator.comparing(Wishlist2Model::getName));
		}
		dataList.setWishlists(getWishlistConverter().convertAll(wishlists));
	}
	
	@Override
	public  WishlistData addCustomAtrrWishlistEntry(final WishlistWsDTO dto) throws GPWishlistException {
		final WishlistData wd = new WishlistData();
		final Wishlist2Model wishlist2Model = getWishListForuuid(dto.getWishlistUid());
		if (null != wishlist2Model) {
			addNewCustomAttributeToList(wishlist2Model, dto.getCustomAttr1(),dto.getCustomAttr2(),dto.getCustomAttr3());
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(SUCCESS_ATTR_MSG);
		}
		return wd;
	}
	
	@Override
	public WishlistData saveCustomAtrribute(WishlistEntryRequestDTO dto) {
		final WishlistData wd = new WishlistData();
		final Wishlist2Model wishlist2Model = getWishListForuuid(dto.getWishlistUid());
		if (null != wishlist2Model) {
			final ProductModel product = getProductService().getProductForCode(dto.getCode());
			updateCustomAttrValueToList(wishlist2Model,product,dto.getCustomAttr1Value(),dto.getCustomAttr2Value(),dto.getCustomAttr3Value());
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(SUCCESS_SAVE_ATTR_MSG);
		}
		return wd;
	}
	
	private void updateCustomAttrValueToList(final Wishlist2Model wishlist2Model, final ProductModel product,
			final String customAttr1Value,final String customAttr2Value,final String customAttr3Value) {
		for (final Wishlist2EntryModel wishlistEntryModel : wishlist2Model.getEntries()) {
			if (wishlistEntryModel.getProduct().getCode().equalsIgnoreCase(product.getCode())) {
				wishlistEntryModel.setCustomAttr1Value(customAttr1Value);
				wishlistEntryModel.setCustomAttr2Value(customAttr2Value);
				wishlistEntryModel.setCustomAttr3Value(customAttr3Value);
				getGpWishlistService().updateWishlist(wishlistEntryModel);
				break;
			}
		}
	}

	@Override
	public WishlistData addWishlistEntry(final WishlistEntryRequestDTO dto) throws GPWishlistException {
		final Wishlist2Model wishlist2Model = getWishListForuuid(dto.getWishlistUid());
		if (null != wishlist2Model) {
			if (ENTRY_TYPE.equalsIgnoreCase(dto.getEntryType())) {
				saveCartToList(dto, wishlist2Model);
			} else {
				try {
					final ProductModel product = getProductService().getProductForCode(dto.getCode());
					//Add the product to the wishlist if it doesn't already exist in the wishlist
					if (!wishlist2Model.getEntries().stream().anyMatch(wishlistEntryModel -> wishlistEntryModel
							.getProduct().getCode().equalsIgnoreCase(product.getCode()))) {
						addNewEntryToList(wishlist2Model, product, dto.getQuantity());
					} else {
						updateEntryToList(wishlist2Model, product, dto.getQuantity());
					}
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
					final WishlistData wd = new WishlistData();
					wd.setStatus(GpcommerceFacadesConstants.FAILURE);
					wd.setDescription(FALIURE_PROD_MSG);
					return wd;
				}
			}
			final WishlistData wd = new WishlistData();
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(SUCCESS_PROD_MSG);
			return wd;
		} else {
			throw new GPWishlistException("1002", "Not ABle to Add to Share list");
		}
	}
	@Override
	public WishlistMultipleData addMultipleWishlistEntries(final WishlistMultipleEntryRequestDTO dto) throws GPWishlistException {
		final Wishlist2Model wishlist2Model = getWishListForuuid(dto.getWishlistUid());
		if (null != wishlist2Model) {
			try {
				for (ProductWsDTO productDto : dto.getProducts()) {
					final ProductModel product = getProductService().getProductForCode(productDto.getCode());
					//Add the product to the wishlist if it doesn't already exist in the wishlist
					if (!wishlist2Model.getEntries().stream().anyMatch(wishlistEntryModel -> wishlistEntryModel
							.getProduct().getCode().equalsIgnoreCase(product.getCode()))) {
						addNewEntryToList(wishlist2Model, product, productDto.getWishlistQuantity());
					} else {
						updateEntryToList(wishlist2Model, product, productDto.getWishlistQuantity());
					}
				}
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
				final WishlistMultipleData wd = new WishlistMultipleData();
				wd.setStatus(GpcommerceFacadesConstants.FAILURE);
				wd.setDescription(FALIURE_PROD_MSG);
				return wd;
			}
			final WishlistMultipleData wd = new WishlistMultipleData();
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(SUCCESS_PROD_MSG);
			return wd;
		} else {
			throw new GPWishlistException("1002", "Not ABle to Add to Share list");
		}
	}
	
	/**
	 * Update/save precurated list(wish list prepared by backoffice user).
	 * @param wishlistdto the wishlistdto
	 */
	public void savePrecuratedList(final WishlistWsDTO wishlistdto) {
		final Wishlist2Model savedPrecurated = getGpWishlistService().getWishlistByType(userService.getCurrentUser(), PRECURATED_LIST);
		final Wishlist2Model gpUserPrecurated = getGpWishlistService().getWishlistById(wishlistdto.getWishlistUid());
		if (null == savedPrecurated) {
			gpWishlistService.createPrecuratedList(wishlistdto, gpUserPrecurated);
		} else {
			gpWishlistService.updatePrecuratedList(wishlistdto, savedPrecurated, gpUserPrecurated);
		}
	}
	
	/**
	 * Update wish list name.
	 * @param wishlistuuid the wishlistuuid
	 * @param listName the list name
	 * @return the wishlist data
	 */
	public WishlistData updateWishlistName(final String wishlistuuid, final String listName) {
		final Wishlist2Model wishlist = getGpWishlistService().getWishlistByName(userService.getCurrentUser(), listName);
		if (null == wishlist) {
			final Wishlist2Model wishlist2Model = this.getWishListForuuid(wishlistuuid);
			return gpWishlistService.updateWishlistName(wishlist2Model, listName);
		} else {
			throw new GPWishlistException(GpErrorConstants.CREATE_WISHLIST_FAILED_CODE, GpErrorConstants.CREATE_WISHLISTS_FAILED_MSG);
		}
	}
	
	/**
	 * Get Precurated list by specific list name.
	 * @param listName the list name
	 * @return the precuratedlist
	 */
	public WishlistData getPrecuratedlist(final String listName) {
		return gpWishlistService.getPrecuratedlist(listName);
	}
	
	/**
	 * Get Shared list by specific list name.
	 * @param listName the list name
	 * @return WishlistData
	 */
	public WishlistData getSharedlist(final String listName) {
		return gpWishlistService.getSharedlist(listName);
	}
	
	/**
	 * Create user list from specific list name.
	 * @param listName the list name
	 * @param form the form
	 * @return WishlistData
	 */
	@Override
	public WishlistData createSharedlistForUser(final String listName, ShareProductForm form) {
		WishlistData wishlistData = null;
		Wishlist2Model wishlistById = gpWishlistService.getWishlistById(listName);
		if(null != form) {
			List<String> recipientEmails = form.getRecipientEmails();
			for (String recipient : recipientEmails) {
				try {
 					B2BCustomerModel b2bCustomerForEmail = gpWishlistService.getB2BCustomerForEmail(recipient);
					if(b2bCustomerForEmail != null && null!= wishlistById) {
					wishlistData = gpWishlistService.createWishListForLoggedInUser(wishlistById,b2bCustomerForEmail);
					}
				} catch (final UnknownIdentifierException e) {
					LOG.error("Recipient email id :"+ recipient +" is not a valid Gpxpress User ID : " + e);
				}
			}
		} else {
			if (null != wishlistById) {
				wishlistData = gpWishlistService.createWishListForLoggedInUser(wishlistById,userService.getCurrentUser());
			} else {
				LOG.error("Wishlist for  :" + listName + "already does not exists for the user : " + userService.getCurrentUser().getUid());
			}
		}
		return wishlistData;
	}

	@Override
	public void shareWishlist(final String toEmail, final WishlistData wishlistData) {
		final String uid = wishlistData.getWishlistUid();
		List<Wishlist2Model> wishListModels = null;
		if (WishlistTypeEnum.PRE_CURATED_LIST.toString().equalsIgnoreCase(wishlistData.getWishlistType())) {
			wishListModels = gpPrecuratedListService.getPrecuratedWishList();
		} else {
			wishListModels = getWishlistService().getWishlists();
		}
		for (final Wishlist2Model wishlist : wishListModels) {
			if (uid != null && (uid.equals(wishlist.getWishlistUid()))) {
				shareWishlist(toEmail, wishlist);
			}
		}
	}

	private void shareWishlist(final String toEmail, final Wishlist2Model wishlist) {
		final Map<String, Object> map = new HashMap<>();
		map.put("WISHLIST", wishlist);
		final WishlistProcessModel wishlistProcessModel = (WishlistProcessModel) businessProcessService.createProcess(
				toEmail + wishlist.getWishlistUid() + System.currentTimeMillis(), "shareWishlistEmailProcess", map);
		wishlistProcessModel.setWishlist(wishlist);
		wishlistProcessModel.setUser(userService.getCurrentUser());
		final ShareWishlistEvent shareWishlistEvent = new ShareWishlistEvent(wishlistProcessModel);
		shareWishlistEvent.setToEmail(toEmail);
		eventService.publishEvent(shareWishlistEvent);
	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.wishlist.GPWishlistFacade#shareWishlist(com.gp.commerce.core.forms.ShareProductForm, com.gp.commerce.facades.data.WishlistData)
	 */
	@Override
	public void shareWishlist(ShareProductForm form, WishlistData wishlistData){
		LOG.debug("Inside shareWishlist()");
		if(null != wishlistData) {
		final String uid = wishlistData.getWishlistUid();
			List<Wishlist2Model> wishListModels = null;
			if (WishlistTypeEnum.PRE_CURATED_LIST.toString().equalsIgnoreCase(wishlistData.getWishlistType())) {
				wishListModels = gpPrecuratedListService.getPrecuratedWishList();
			} else {
				wishListModels = getWishlistService().getWishlists();
			}
			if(null != wishListModels) {
			for (final Wishlist2Model wishlist : wishListModels) {
				if (uid != null && (uid.equals(wishlist.getWishlistUid()))) {
					LOG.debug("WishListModels present for uid :" +uid);
					shareWishlist(form, wishlist);
					} else {
					Wishlist2Model wishlistById = getGpWishlistService().getWishlistById(uid);
					LOG.debug("WishListModels not present for uid :"+uid + "hence fetching by Id, fetched model:" +wishlistById);
					shareWishlist(form, wishlistById);
					break;
				}
			  }
			} else {
				Wishlist2Model wishlistById = getGpWishlistService().getWishlistById(uid);
				LOG.debug("WishListModels empty ,hence fetching by Id, fetched model:" +wishlistById);
				shareWishlist(form, wishlistById);
			}
		} else {
			LOG.error("wishlistData is empty");
		}
	}
	
	/**
	 * Sets all the required fields in business model WishlistProcess 
	 * 		and publishes ShareWishlistEvent.
	 * @param form the form
	 * @param wishlist the wishlist
	 */
	private void shareWishlist(ShareProductForm form, Wishlist2Model wishlist) {
		final Map<String, Object> map = new HashMap<>();
		map.put("WISHLIST", wishlist);
		final WishlistProcessModel wishlistProcessModel = (WishlistProcessModel) businessProcessService.createProcess(
				wishlist.getWishlistUid() + System.currentTimeMillis(), "shareWishlistEmailProcess", map);
		wishlistProcessModel.setWishlist(wishlist);
		wishlistProcessModel.setUser(userService.getCurrentUser());
		final ShareWishlistEvent shareWishlistEvent = new ShareWishlistEvent(wishlistProcessModel);
		String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
		if(!userService.isAnonymousUser(userService.getCurrentUser()) && null != cmsSiteService.getCurrentSite() && cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId)
				&& null != cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO)&& cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO).equals(sessionService.getAttribute("soldToUnitId"))) 
		{
			ArrayList<String> gpxpressRecipients= new ArrayList<>();
			for(String recipient: form.getRecipientEmails()) 
			{
			try {
				B2BCustomerModel b2bCustomerForEmail = gpWishlistService.getB2BCustomerForEmail(recipient);
				if(null != b2bCustomerForEmail) {
					gpxpressRecipients.add(recipient);
				}
				} catch (final UnknownIdentifierException e) {
				LOG.error("Share wishlist email not sent to recipient, as no registered Gpxpress B2B user found for given email id :" +recipient);
			}
		}
			shareWishlistEvent.setToEmail(gpxpressRecipients.toString());
		}else {
			shareWishlistEvent.setToEmail(form.getRecipientEmails().toString());
		}
		shareWishlistEvent.setSenderName(form.getSenderName());
		shareWishlistEvent.setSenderEmail(form.getSenderEmail());
		shareWishlistEvent.setSubject(form.getSubject());
		shareWishlistEvent.setSenderMessage(form.getSenderMessage());
		shareWishlistEvent.setAttachPDF(form.getAttachPDF());
		shareWishlistEvent.setAddLink(form.getAddLink());
		if (form.getAttachPDF() && null != form.getEncodedString()) {
			BASE64Decoder decoder = new BASE64Decoder();
			String encodedBytes = form.getEncodedString();
			byte[] decodedBytes = null;
			try {
				decodedBytes = decoder.decodeBuffer(encodedBytes);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
			final String directoryPath = Config.getParameter(WISHLIST_EMAIL);
			final File dir = new File(directoryPath);
			final String fileName = WISHLIST + System.currentTimeMillis();
			final File temp = new File(dir, fileName + PDF_FILE_EXTENSION);
			try (FileOutputStream fop1 = new FileOutputStream(temp)) {
				fop1.write(decodedBytes);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
			try (DataInputStream in = new DataInputStream(new FileInputStream(temp))) {
				final EmailAttachmentModel attachment = defaultGPEmailService.createEmailAttachment(in,
						fileName + PDF_FILE_EXTENSION, PDF_FILE_TYPE);
				final List<EmailAttachmentModel> attachmentsList = new ArrayList<>();
				attachmentsList.add(attachment);
				setAttachments(attachmentsList);
				temp.delete();
				shareWishlistEvent.setEmailAttachmentList(attachmentsList);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}	
		if (null != cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO) && cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO).equals(sessionService.getAttribute("soldToUnitId"))) {
			shareWishlistEvent.setSoldTo(Boolean.TRUE);
		} else {
			shareWishlistEvent.setSoldTo(Boolean.FALSE);
		}
		LOG.debug("Triggering Share wislist Event");
		eventService.publishEvent(shareWishlistEvent);
	}

	/**
	 * Method to save cart to list.
	 * @param dto the dto
	 * @param wishlist2Model the wishlist 2 model
	 * @return the wishlist data
	 */
	WishlistData saveCartToList(final WishlistEntryRequestDTO dto, final Wishlist2Model wishlist2Model) {
		final CartModel cart = getCommerceCartService().getCartForGuidAndSite(dto.getCode(),
				getBaseSiteService().getCurrentBaseSite());
		final WishlistData wd = new WishlistData();
		if (null != cart) {
			if (CollectionUtils.isNotEmpty(cart.getEntries())) {
				for (final AbstractOrderEntryModel cartEntry : cart.getEntries()) {
					final ProductModel product = cartEntry.getProduct();
					// Add the product to the wishlist if it doesn't already exist in the wishlist
					if (!wishlist2Model.getEntries().stream().anyMatch(wishlistEntryModel -> wishlistEntryModel
							.getProduct().getCode().equalsIgnoreCase(product.getCode()))) {
						addNewEntryToList(wishlist2Model, product, cartEntry.getQuantity());
					} else {
						updateEntryToList(wishlist2Model, product, cartEntry.getQuantity());
					}
				}
			}
		} else {
			wd.setStatus(GpcommerceFacadesConstants.FAILURE);
			wd.setDescription("Couldn't find the Cart");
		}
		return wd;
	}
	
	/**
	 * Method to add new entry to list.
	 * @param wishlist2Model the wishlist 2 model
	 * @param customAttr1 the custom attr 1
	 * @param customAttr2 the custom attr 2
	 * @param customAttr3 the custom attr 3
	 */
	private void addNewCustomAttributeToList(final Wishlist2Model wishlist2Model, 
			final  String customAttr1,final String customAttr2,final String customAttr3) {
		wishlist2Model.setCustomAttr1(customAttr1);
		wishlist2Model.setCustomAttr2(customAttr2);
		wishlist2Model.setCustomAttr3(customAttr3);
		modelService.save(wishlist2Model);
	}

	/**
	 * Method to add new entry to list.
	 * @param wishlist2Model the wishlist 2 model
	 * @param product the product
	 * @param quantity the quantity
	 */
	private void addNewEntryToList(final Wishlist2Model wishlist2Model, final ProductModel product, final Long quantity) {
		Wishlist2EntryModel wishlistEntry = null;
		wishlistEntry = getGpWishlistService().createWishlistEntry(wishlist2Model, product);
		wishlistEntry.setQuantity(quantity);
		getWishlistService().addWishlistEntry(wishlist2Model, wishlistEntry);
	}
	
	/**
	 * Method to update the entry to list.
	 * @param wishlist2Model the wishlist 2 model
	 * @param product the product
	 * @param quantity the quantity
	 */
	private void updateEntryToList(final Wishlist2Model wishlist2Model, final ProductModel product, final Long quantity) {
		for (final Wishlist2EntryModel wishlistEntryModel : wishlist2Model.getEntries()) {
			if (wishlistEntryModel.getProduct().getCode().equalsIgnoreCase(product.getCode())) {
				wishlistEntryModel.setQuantity(wishlistEntryModel.getQuantity() + quantity);
				getGpWishlistService().updateWishlist(wishlistEntryModel);
				break;
			}
		}
	}
	
	/**
	 * Method to fetch all lists of user by type.
	 * @return Wishlists
	 */
	public WishlistListData getAllWishlistByType() {
		final List<Wishlist2Model> wishlist2Models = getGpWishlistService().getAllWishlistByType();
		final WishlistListData dataList = new WishlistListData(); 
		if (CollectionUtils.isNotEmpty(wishlist2Models)) {
			dataList.setWishlists(getWishlistConverter().convertAll(wishlist2Models));
		}
		return dataList;
	}

	@Override
	public WishlistData addItemByProductId(String wishListUid, String productCodes) {
		final Wishlist2Model wishlist2Model = getWishListForuuid(wishListUid);
		final Long quantity = 1L;
		List<ProductModel> products = getGpWishlistService().getProductsForCodes(productCodes);
		AtomicInteger counter = new AtomicInteger(0);
		final List<String> productCodeList = Arrays.asList(productCodes.split(COMMA));
		if (null != wishlist2Model) {
			if(!products.isEmpty()) {
				products.forEach(product->{
					if(!wishlist2Model.getEntries().stream().anyMatch(wishlistEntryModel -> wishlistEntryModel.getProduct().getCode()
							.equalsIgnoreCase(product.getCode()))){
						addNewEntryToList(wishlist2Model, product, quantity);
						counter.getAndIncrement();
					} else {
						updateEntryToList(wishlist2Model, product, quantity);
					}
				});
			} else {
				throw new GPWishlistException("3325", "Invalid product ID has been entered.");
			}
			int noOfProdcutsNotAdded = productCodeList.size() - products.size();
			final WishlistData wd = new WishlistData();
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(counter.get() + SUCCESS_PROD_ADD_MSG + wishlist2Model.getName());
			if(noOfProdcutsNotAdded > 0) {
				wd.setErrorMessage(noOfProdcutsNotAdded + ERROR_PROD_ADD_MSG + wishlist2Model.getName());
			}
			return wd;
		} else {
			throw new GPWishlistException("1002", "Not able to find wishlist");
		}
	}

	@Override
	public File exportImages(WishlistData wishlistData, String imageFormat,
			String resolution, boolean allimages) throws IOException {
		final String tempPath = Config.getParameter(GpcommerceFacadesConstants.CART_EMAIL);
		final String temp = tempPath.substring(0, tempPath.length() - 7);
		String source = temp + GpcommerceFacadesConstants.IMAGE_FILE_PATH + wishlistData.getWishlistUid();
		String zipFile = temp + GpcommerceFacadesConstants.PRODUCT_FORMAT_IMAGE_FILE_PATH + wishlistData.getName()
				+ GpcommerceFacadesConstants.ZIP_FILE_EXTENSION;
		return getGpWishlistService().exportImages(wishlistData, imageFormat, resolution, source, zipFile, allimages);
	}
	
	@Override
	public InputStream getInputStream(final List<ProductData> products, final List<String> files, String zipFileName) throws IOException {
		return gpWishlistService.getInputStream(products,files,zipFileName);
	}

	@Override
	public void setZipFile(String zipFileName,InputStream inputStream, HttpServletResponse response, List<ProductData> products) throws IOException {
		LOG.debug("Inside setZipFile");
		if (null != inputStream) {
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition","attachment; filename=" + zipFileName);
			final OutputStream responseOutputStream = response.getOutputStream();
			IoUtils.copy(inputStream, responseOutputStream);
			response.flushBuffer();
		} else {
			throw new IOException("InputStream is null");
		}
	}

	@Override
	public List<String> getAssociatedFiles(List<String> files,String timestamp, String listkey) {
		if ("currentList".equalsIgnoreCase(listkey)) {
			files.add("Multi"+ timestamp + GpcommerceCoreConstants.CSV_FILE_EXTENSION);
			files.add("Related" + timestamp + GpcommerceCoreConstants.CSV_FILE_EXTENSION);
			files.add("Product" + timestamp + GpcommerceCoreConstants.CSV_FILE_EXTENSION);
		} else {
			files.add("Multi_AllProducts" + GpcommerceCoreConstants.CSV_FILE_EXTENSION);
			files.add("Related_AllProducts" + GpcommerceCoreConstants.CSV_FILE_EXTENSION);
			files.add("Product_AllProducts" + GpcommerceCoreConstants.CSV_FILE_EXTENSION);
		}
		return files;
	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.wishlist.GPWishlistFacade#getAllBrandSpecificProducts()
	 */
	@Override
	public List<ProductData> getAllBrandSpecificProducts() {
		List<ProductData> products = new ArrayList<>();
		getAssociatedProducts(products);
		return products;
	}

	/**
	 * Retrieves list of products to be downloaded .
	 * @param products the products
	 * @return products list
	 */
	private List<ProductData> getAssociatedProducts(List<ProductData> products) {
		List<ProductModel> productModels;
		B2BUnitModel b2bUnit = getB2BUnit();
		final Collection<ProductOption> options = Arrays.asList(ProductOption.DESCRIPTION,ProductOption.BASIC,ProductOption.CLASSIFICATION,ProductOption.SPECIFICATIONS,ProductOption.REFERENCES);
		if (null != b2bUnit) {
			CMSSiteModel site = cmsSiteService.getCurrentSite();
			String gpxpressSiteId = configurationService.getConfiguration()
					.getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
			if (site.getUid().equalsIgnoreCase(gpxpressSiteId)) {
				productModels = gpWishlistService.getAllBrandSpecificProducts(b2bUnit);
			} else {
				productModels = (List<ProductModel>) b2bUnit.getProducts();
			}
			if (CollectionUtils.isNotEmpty(productModels)) {
				productModels.forEach(product -> {
					ProductData productData = productConverter.convert(product);
					getProductConfiguredPopulator().populate(product, productData, options);
					populateKnowledgeCenterData(productData);
					products.add(productData);
				});
				return products;
			}
		}
		return products;
	}
	
	@Override
	public B2BUnitModel getB2BUnit() {
		final UserModel user = userService.getCurrentUser();
		if (user instanceof B2BCustomerModel) {
			final B2BCustomerModel b2bUser = (B2BCustomerModel) user;
			final B2BUnitModel unit = sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID);
			return (null != unit) ? unit :  b2bUser.getDefaultB2BUnit();
		}
		LOG.error("User does not have a B2B Unit assigned");
		return null;
	}
	
	@Override
	public List<ProductData> getWishListProducts(String wishlistuuid,HttpServletRequest request, HttpServletResponse response) {
		WishlistData wishlistData = this.getWishlist(wishlistuuid,request,response,false);
		List<ProductData> products = new ArrayList<>();
		wishlistData.getWishlistEntries().forEach(wishListEntry -> {
			ProductData product = wishListEntry.getProduct();
			populateKnowledgeCenterData(product);
			products.add(product);
		});
		return products;		
	}
	
	/**
	 * Populates products knowledge center data .
	 * @param product the product
	 */
	public void populateKnowledgeCenterData(final ProductData product) {
		try {
			final DataSheetsData dataSheetdata = product.getDataSheets() != null ? product.getDataSheets() : new DataSheetsData();
			final ProductResourcesVideosData productResourceVideosData = product.getProductResourceVideos() != null
					? product.getProductResourceVideos() : new ProductResourcesVideosData();
			if (null != product.getCode()) {
				final GPKnowledgeCenterSkuResponse kcResponse = gpKnowledgeCenterService.getSKUDataFromKC(product.getCode());
				if (null != kcResponse) {
					if (null != kcResponse.getInfoResources()) {
						final List<InfoResourcesDTO> infoResourceList = kcResponse.getInfoResources();
						final List<ImageData> imageDataList = infoResourceList.stream().map(infoResource -> {
							final ImageData imageData = new ImageData();
							imageData.setAltText(infoResource.getResourceTitle());
							imageData.setUrl(infoResource.getResourceImageURL());
							imageData.setMimeType(infoResource.getMediaType());
							imageData.setDescription(infoResource.getResourceTitle());
							try {
								imageData.setStartDate(
										new SimpleDateFormat(DATE_FORMAT).parse(infoResource.getStartDate()));
							} catch (final java.text.ParseException e) {
								LOG.error(e.getMessage());
							}
							imageData.setResourceURL(infoResource.getResourceURL());
							imageData.setResourceId(infoResource.getResourceId());
							imageData.setTrainingId(infoResource.getTrainingId());
							imageData.setTrainingGroupId(infoResource.getTrainingGroupId());
							imageData.setTags(infoResource.getTags());
							imageData.setTrainingBrands(infoResource.getTrainingBrands());
							return imageData;
						}).collect(Collectors.toList());
						if (null != dataSheetdata.getDataSheets()) {
							imageDataList.addAll(dataSheetdata.getDataSheets());
						}
						dataSheetdata.setDataSheets(imageDataList);
						product.setDataSheets(dataSheetdata);
					}
					if (null != kcResponse.getSupportResources()) {
						final List<SupportResourcesDTO> supportResourceList = kcResponse.getSupportResources();
						final List<ImageData> imageDataList = supportResourceList.stream().map(supportResource -> {
							final ImageData imageData = new ImageData();
							imageData.setAltText(supportResource.getResourceTitle());
							imageData.setUrl(supportResource.getResourceImageURL());
							imageData.setMimeType(supportResource.getMediaType());
							imageData.setDescription(supportResource.getResourceTitle());
							try {
								imageData.setStartDate(
										new SimpleDateFormat(DATE_FORMAT).parse(supportResource.getStartDate()));
							} catch (final java.text.ParseException e) {
								LOG.error(e.getMessage());
							}
							imageData.setResourceURL(supportResource.getResourceURL());
							imageData.setResourceId(supportResource.getResourceId());
							imageData.setTrainingId(supportResource.getTrainingId());
							imageData.setTrainingGroupId(supportResource.getTrainingGroupId());
							imageData.setTags(supportResource.getTags());
							imageData.setTrainingBrands(supportResource.getTrainingBrands());
							return imageData;
						}).collect(Collectors.toList());
						if (null != productResourceVideosData.getVideo()) {
							imageDataList.addAll(productResourceVideosData.getVideo());
						}
						productResourceVideosData.setVideo(imageDataList);
						product.setProductResourceVideos(productResourceVideosData);
					}
				}
			}
		} catch (final Exception e) {
			LOG.error("Error occured while mapping Knowledge center response to ProductData",e);
		}
	}
	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.wishlist.GPWishlistFacade#removeWishlistEntriesForCategory(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public WishlistData removeWishlistEntriesForCategory(List<String> productCodeList, String category, String uuid) {
		final WishlistData wd = new WishlistData();
		try {
			for(String productCode: productCodeList) {
				getWishlistService().removeWishlistEntryForProduct(getProductService().getProductForCode(productCode),
					getWishListForuuid(uuid));
			}
			wd.setStatus(GpcommerceFacadesConstants.SUCCESS);
			wd.setDescription(SUCCESS_MSG);
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
			wd.setStatus(GpcommerceFacadesConstants.FAILURE);
			wd.setDescription(FALIURE_MSG);
		}
		return wd;
	}
	
	public Converter<ProductModel, ProductData> getProductConverter() {
		return productConverter;
	}
	@Required
	public void setProductConverter(Converter<ProductModel, ProductData> productConverter) {
		this.productConverter = productConverter;
	}
	public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator() {
		return productConfiguredPopulator;
	}
	@Required
	public void setProductConfiguredPopulator(
			ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator) {
		this.productConfiguredPopulator = productConfiguredPopulator;
	}
	public List<EmailAttachmentModel> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EmailAttachmentModel> attachments) {
		this.attachments = attachments;
	}
}