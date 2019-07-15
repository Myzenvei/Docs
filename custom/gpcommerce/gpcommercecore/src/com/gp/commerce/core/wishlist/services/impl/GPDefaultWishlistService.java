/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.wishlist.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.util.UriComponentsBuilder;
import com.gp.commerce.core.constants.GpErrorConstants;
import com.gp.commerce.core.enums.WishlistTypeEnum;
import com.gp.commerce.core.exceptions.GPWishlistException;
import com.gp.commerce.core.util.GPWishlistUtil;
import com.gp.commerce.core.wishlist.dao.GPWishlistDao;
import com.gp.commerce.core.wishlist.services.GPWishlistService;
import com.gp.commerce.dto.wishlist.WishlistEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import com.gp.commerce.facades.data.GPQuickOrderData;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.impl.DefaultWishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

/**
 * GP Service class to perform custom whishlist functionalities
 */
public class GPDefaultWishlistService extends DefaultWishlist2Service implements GPWishlistService {
	private static final Logger LOG = Logger.getLogger(GPDefaultWishlistService.class);
	private static final String SUCCESS = "success";
	private static final String REMOVED ="removed successfully";
	private static final String FAILURE ="failure";
	private static final String PRECURATED_LIST = "PRE_CURATED_LIST";
	private static final String CUSTOMER_WISHLIST = "Customer Wishlist";
	private static final String QUICK_ORDER_LIST = "quick-order-list";
	private static final String IMG_TYPE = "PRIMARY";
	private static final String PRID = "prid";
	private static final String WIDTH = "Wx";
	private static final String HEIGHT = "H";

	private Wishlist2Service wishlistService;
	private Converter<Wishlist2Model, WishlistData> wishlistConverter;
	private ProductService productService;
	private GPWishlistDao gpWishlistDao;
	private GPWishlistUtil gpWishlistUtil;
	private KeyGenerator guidKeyGenerator;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "userService")
 	private UserService userService;

	
	/**
	 * Returns wishlist by name
	 * @param user
	 * 			the user
	 * @param name
	 * 			the name
	 * @return wishlist by name
	 */
	public Wishlist2Model getWishlistByName(final UserModel user, final String name){
		if(user!=null){
			return this.gpWishlistDao.getWishlistByName(user, name);
		}else{
			return null;
		}
	}

	public GPWishlistDao getGpWishlistDao()
	{
		return gpWishlistDao;
	}

	public void setGpWishlistDao(final GPWishlistDao gpWishlistDao)
	{
		this.gpWishlistDao = gpWishlistDao;
	}

	/**
	 * Gets the wishlist for specific ID.
	 *
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

	/**
	 * Remove the specific wishlist entry by passing in specific ID.
	 *
	 * @param uuid the uuid
	 * @return the wishlist data
	 */
	public WishlistData removeWishlist(final String uuid) {
		final Wishlist2Model wishlist = getWishListForuuid(uuid);
		final WishlistData wd = new WishlistData();
		if (null != wishlist) {
			getModelService().remove(wishlist);
			wd.setStatus(SUCCESS);
			wd.setDescription(REMOVED);
		} else {
			throw new GPWishlistException(GpErrorConstants.GET_WISHLIST_FAILED_CODE,GpErrorConstants.GET_WISHLIST_FAILED_MSG);
		}
		return wd;

	}
	
	/**
	 * Method to add precurated wishlist entry.
	 *
	 * @param requestEntry the request entry
	 * @param savedPrecurated the saved precurated
	 * @param gpPrecuratedEntries the gp precurated entries
	 */
	public void addGPPrecuratedEntry(final WishlistEntryRequestDTO requestEntry, final Wishlist2Model savedPrecurated, final List<Wishlist2EntryModel> gpPrecuratedEntries) {
		final Wishlist2EntryModel entryToSave = gpPrecuratedEntries.stream().filter(wishlistEntryModel -> wishlistEntryModel.getProduct()
				.getCode().equalsIgnoreCase(requestEntry.getProduct().getCode())).findFirst().orElse(null);
		final Wishlist2EntryModel precuratedEntryToSave = createWishlistEntry(savedPrecurated, null!=entryToSave?entryToSave.getProduct():null);
		precuratedEntryToSave.setQuantity(requestEntry.getQuantity());
		getWishlistService().addWishlistEntry(savedPrecurated, precuratedEntryToSave);
	}
	

	/**
	 * Remove the selected wish list by passing multiple selected IDs.
	 *
	 * @param multiplewishlistuuid the multiplewishlistuuid
	 * @throws GPWishlistException the GP wishlist exception
	 */

	public void removeSelectedWishlists(final List<String> multiplewishlistuuid) throws GPWishlistException {
		WishlistData wishListData = new WishlistData();
		final List<String> failedUidList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(multiplewishlistuuid)) {
			for (final String uuid : multiplewishlistuuid) {
				if (StringUtils.isNotEmpty(uuid)) {
					wishListData = removeWishlist(uuid);
					if (FAILURE.equalsIgnoreCase(wishListData.getStatus())) {
						failedUidList.add(uuid);
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(failedUidList)) {
			throw new GPWishlistException(GpErrorConstants.REMOVE_WISHLISTS_FAILED_CODE, GpErrorConstants.REMOVE_WISHLISTS_FAILED_MSG + failedUidList);
		}
		return;
	}

	/**
	 * Update the quantity for specific product in wish list entry.
	 *
	 * @param dto the dto
	 * @return the wishlist data
	 */
	public WishlistData updateWishListEntryForQuantity(final WishlistEntryRequestDTO dto) {
		final String uuid = dto.getWishlistUid();
		final String code = dto.getProduct().getCode();
		if (StringUtils.isNotEmpty(uuid)) {
			final Wishlist2Model wishListResult = getWishListForuuid(uuid);
			if (null != wishListResult) {
				final ProductModel productModel = getProductService().getProductForCode(code);
				if (null != productModel) {
					final Wishlist2EntryModel entryModel = wishlistService.getWishlistEntryForProduct(productModel,
							wishListResult);
					if (null != entryModel) {
						entryModel.setQuantity(dto.getQuantity());
						getModelService().save(entryModel);
					}
					if (null != wishListResult) {
						return getWishlistConverter().convert(wishListResult);
					}
				}
			}
		}
		return null;
	}

	/**
	 *{@inheritDoc}
	 */
	public BigDecimal calculateSubtotal(final List<WishlistEntryData> wishlistEntryData)
	{
		return wishlistEntryData.stream()
				.map(entry -> null == entry.getProduct().getPrice().getValue() ? BigDecimal.ZERO
						: ((entry.getProduct().getPrice().getValue()).multiply(BigDecimal.valueOf(entry.getQuantity()))))
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	/**
	 * Update/save precurated list(wish list prepared by backoffice user).
	 *
	 * @param wishlistdto the wishlistdto
	 * @param savedPrecurated the saved precurated
	 * @param gpUserPrecurated the gp user precurated
	 */
	public void updatePrecuratedList(final WishlistWsDTO wishlistdto,final Wishlist2Model savedPrecurated, final Wishlist2Model gpUserPrecurated) {

		List<Wishlist2EntryModel> savedPrecuratedEntries = null;
		Wishlist2EntryModel entryToSave = null;

		if(null != savedPrecurated) {
			savedPrecuratedEntries = new ArrayList<>(savedPrecurated.getEntries());
			if (CollectionUtils.isNotEmpty(wishlistdto.getWishlistEntries())) {
				for(final WishlistEntryRequestDTO requestEntry : wishlistdto.getWishlistEntries()) {
					if(CollectionUtils.isEmpty(savedPrecuratedEntries)) {
						addGPPrecuratedEntry(requestEntry, savedPrecurated, gpUserPrecurated.getEntries());
					}else {
						entryToSave = savedPrecuratedEntries.stream().filter(wishlistEntryModel -> wishlistEntryModel.getProduct()
								.getCode().equalsIgnoreCase(requestEntry.getProduct().getCode())).findFirst().orElse(null);
						if (null != entryToSave) {
							entryToSave.setQuantity(requestEntry.getQuantity());
							getModelService().save(entryToSave);
						} else {
							addGPPrecuratedEntry(requestEntry, savedPrecurated, gpUserPrecurated.getEntries());
						}
					}
				}
				getModelService().save(savedPrecurated);
			}
		}
	}
	
	/**
	 * Update wish list name.
	 *
	 * @param wishlist2Model the wishlist 2 model
	 * @param listName the list name
	 * @return the wishlist data
	 */
	public WishlistData updateWishlistName(final Wishlist2Model wishlist2Model,final String listName) {
		wishlist2Model.setName(listName);
		getModelService().save(wishlist2Model);
		return getWishlistConverter().convert(wishlist2Model);
		}

	/**
	 * Get Precurated list by specific list name.
	 * @param listName the list name
	 * @return the precuratedlist
	 */
	public WishlistData getPrecuratedlist(final String listName) {
		if(null != listName){
			return getWishlistConverter().convert(gpWishlistDao.getPrecuratedlist(listName));
		}else{
			return null;
		}
	}

	/**
	 * Get Shared list by specific list name.
	 * @param listName the list name
	 * @return the sharedlist
	 */
	public WishlistData getSharedlist(final String listName) {
		if(null != listName){
			Wishlist2Model wishlist=getWishlistById(listName);
			return getWishlistConverter().convert(gpWishlistDao.getSharedlist(listName,wishlist.getWishlistType().getCode()));
		}else{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Wishlist2Model getWishlistByType(final UserModel user, final String type){
		if(null != type){
			return gpWishlistDao.getWishlistByType(user, type);
		}else{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Wishlist2Model getWishlistById(final String wishListId){
		if(StringUtils.isNotEmpty(wishListId)){
			return gpWishlistDao.getWishlistById(wishListId);
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.wishlist.services.GPWishlistService#createQuickOrderWishList(com.gp.commerce.facades.data.GPQuickOrderData)
	 * Method to create dummy wishlist and add products as entries during share quickorder
	 */
	@Override
	public Wishlist2Model createQuickOrderWishList(final GPQuickOrderData gpQuickOrderData) throws BusinessException{

		final Wishlist2Model wishlist2Model = getWishlistService().createWishlist(QUICK_ORDER_LIST+System.currentTimeMillis(), CUSTOMER_WISHLIST);
		wishlist2Model.setWishlistUid(getGuidKeyGenerator().generate().toString());
		wishlist2Model.setWishlistType(WishlistTypeEnum.QUICK_ORDER_LIST);
		getModelService().save(wishlist2Model);
		if(CollectionUtils.isNotEmpty(gpQuickOrderData.getItems())){
			final List<ProductData> items = gpQuickOrderData.getItems();
			ProductModel product = null;
			for(final ProductData item : items){
				try{
				product = getProductService().getProductForCode(item.getCode());
				}catch(final Exception e){
					LOG.error(" Cannot find product with code "+item.getCode(),e);
					throw new BusinessException("Product_not_found");
				}
				if(null != product){
					final Wishlist2EntryModel wishlistEntry = createWishlistEntry(wishlist2Model, product);
					wishlistEntry.setQuantity(new Long(item.getCount()));
					getWishlistService().addWishlistEntry(wishlist2Model, wishlistEntry);
				}
			}
		}
		return wishlist2Model;
	}

	/**
	 * {@inheritDoc}
	 */
	public Wishlist2EntryModel createWishlistEntry(final Wishlist2Model wishlist2Model, final ProductModel product) {

		final Wishlist2EntryModel wishlist2EntryModel = getModelService().create(Wishlist2EntryModel.class);
		wishlist2EntryModel.setProduct(product);
		wishlist2EntryModel.setPriority(Wishlist2EntryPriority.MEDIUM);
		wishlist2EntryModel.setAddedDate(new Date());
		wishlist2EntryModel.setQuantity(Long.valueOf(1l));
		wishlist2EntryModel.setWishlist(wishlist2Model);
		return wishlist2EntryModel;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createPrecuratedList(final WishlistWsDTO wishlistdto, final Wishlist2Model gpUserPrecurated) {

		final Wishlist2Model newPrecurated = getWishlistService().createWishlist(wishlistdto.getName(), PRECURATED_LIST);
		newPrecurated.setWishlistUid(getGuidKeyGenerator().generate().toString());
		newPrecurated.setWishlistType(WishlistTypeEnum.PRE_CURATED_LIST);
		if (CollectionUtils.isNotEmpty(wishlistdto.getWishlistEntries())) {
			for (final WishlistEntryRequestDTO requestEntry : wishlistdto.getWishlistEntries())
			{
				if (null != gpUserPrecurated)
				{
					addGPPrecuratedEntry(requestEntry, newPrecurated, gpUserPrecurated.getEntries());
				}
			}
		}
		getModelService().save(newPrecurated);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Wishlist2Model createWishlistWithGUID(final String name, final String description,
			final Boolean defaultWL)
	{
		final UserModel user = this.getCurrentUser();
		final Wishlist2Model wishlist = new Wishlist2Model();
		wishlist.setName(name);
		wishlist.setDescription(description);
		wishlist.setDefault(defaultWL);
		wishlist.setUser(user);
		wishlist.setWishlistUid(getGuidKeyGenerator().generate().toString());
		if (this.saveWishlist(user)) {
			this.getModelService().save(wishlist);
		}
		return wishlist;
	}
	
	@Override
	public void updateWishlist(final Wishlist2EntryModel wishlistEntry)
	{
		if (null != wishlistEntry)
		{
			getModelService().save(wishlistEntry);
		}
	}
	
	/**
	 * Method to create Wishlist with the Products in the Cart.
	 *
	 * @param cart the cart
	 * @return the wishlist 2 model
	 */
	public Wishlist2Model createShareCartWishList(final CartModel cart) {
		Wishlist2Model wishlistModel = createWishlistForCart("SharedCart-" + cart.getCode()+System.currentTimeMillis(),
				"Shared Cart", false);
		Wishlist2EntryModel wishlistEntry = null;
		List<AbstractOrderEntryModel> cartEntries = cart.getEntries();
		if (!CollectionUtils.isEmpty(cartEntries)) {
			for (AbstractOrderEntryModel entries : cartEntries) {
				wishlistEntry = createShareCartEntry(wishlistModel, entries.getProduct(),
						entries.getQuantity());
				wishlistService.addWishlistEntry(wishlistModel, wishlistEntry);
			}
		}
	 return wishlistModel;	
	}
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.wishlist.services.GPWishlistService#createWishlistForCart(String, String, Boolean)
	 */
	public Wishlist2Model createWishlistForCart(final String name, final String description,
						final Boolean defaultWL)
	{
		final UserModel user = this.getCurrentUser();
		final Wishlist2Model wishlist = new Wishlist2Model();
		wishlist.setName(name);
		wishlist.setDescription(description);
		wishlist.setDefault(defaultWL);
		wishlist.setUser(user);
		wishlist.setWishlistUid(getGuidKeyGenerator().generate().toString());
		if (this.saveWishlist(user)) {
			this.getModelService().save(wishlist);
		}
		wishlist.setWishlistType(WishlistTypeEnum.SHARE_CART_LIST);
		wishlist.setSite((CMSSiteModel)baseSiteService.getCurrentBaseSite());
		return wishlist;
	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.wishlist.services.GPWishlistService#createShareCartEntry(Wishlist2Model, ProductModel, Long)
	 */
	public Wishlist2EntryModel createShareCartEntry(final Wishlist2Model wishlist2Model, final ProductModel product,
			final Long quantity) {

		final Wishlist2EntryModel wishlist2EntryModel = getModelService().create(Wishlist2EntryModel.class);
		wishlist2EntryModel.setProduct(product);
		wishlist2EntryModel.setPriority(Wishlist2EntryPriority.MEDIUM);
		wishlist2EntryModel.setAddedDate(new Date());
		wishlist2EntryModel.setQuantity(quantity);
		wishlist2EntryModel.setWishlist(wishlist2Model);
		return wishlist2EntryModel;
	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.wishlist.services.GPWishlistService#createWishListForLoggedInUser(de.hybris.platform.wishlist2.model.Wishlist2Model, de.hybris.platform.core.model.user.UserModel)
	 */
	@Override
	public WishlistData createWishListForLoggedInUser(final Wishlist2Model wishlist, UserModel user) {
		Wishlist2Model wishlistModel = createWishlistForUser(user);
		Wishlist2EntryModel wishlistEntry = null;
		List<Wishlist2EntryModel> wishlistEntries = wishlist.getEntries();
		if (!CollectionUtils.isEmpty(wishlistEntries)) {
			for (Wishlist2EntryModel entries : wishlistEntries) {
				wishlistEntry = createWishlistEntryForUser(wishlistModel, entries.getProduct(),
						entries.getQuantity());
				wishlistService.addWishlistEntry(wishlistModel, wishlistEntry);
			}
		}
		return  getWishlistConverter().convert(wishlistModel);
	}
	/**
	 * create wishlist entry for the wishlist and user.
	 *
	 * @param wishlist2Model the wishlist 2 model
	 * @param product the product
	 * @param quantity the quantity
	 * @return wishlist2EntryModel
	 */
	public Wishlist2EntryModel createWishlistEntryForUser(final Wishlist2Model wishlist2Model, final ProductModel product,
			final Long quantity) {
		final Wishlist2EntryModel wishlist2EntryModel = getModelService().create(Wishlist2EntryModel.class);
		wishlist2EntryModel.setProduct(product);
		wishlist2EntryModel.setPriority(Wishlist2EntryPriority.MEDIUM);
		wishlist2EntryModel.setAddedDate(new Date());
		wishlist2EntryModel.setQuantity(quantity);
		wishlist2EntryModel.setWishlist(wishlist2Model);
		return wishlist2EntryModel;
	}
	
	public List<Wishlist2Model> getAllWishlistByType()
	{
		return gpWishlistDao.getAllWishlistByType(this.getCurrentUser(),WishlistTypeEnum.USER_LIST);
	}

	@Override
	public List<ProductModel> getProductsForCodes(String productCodes) {
		
		return gpWishlistDao.getProductsForCodes(productCodes);
	}

	@Override
       public File exportImages(WishlistData wishlistData, String imageFormat,
                    String resolution, String source, String zipFile, boolean allimages) throws  IOException {
             byte[] b = new byte[2048];
             int count = 1;
             File sourceFile = new File(source);
             File zip = new File(zipFile);
             //Create temp directory and download images based on filters
             try {
                    if (sourceFile.mkdir()) {
                          if (allimages){
                        	  for (WishlistEntryData entry : wishlistData.getWishlistEntries()) {
                                  if(entry.getProduct().getDamGalleryImages() != null) {
                                        for (ImageData image : entry.getProduct().getDamGalleryImages()) {
                                      	    String imageUrl = image.getUrl();
            								final UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(imageUrl);
            								if(imageUrl.contains(PRID)){
                  								urlBuilder.replaceQueryParam(PRID, resolution+WIDTH+resolution+HEIGHT);
                  								URL url = new URL(urlBuilder.build().toUriString());
                  								String destName = sourceFile + "///" + entry.getProduct().getCode() + "_" + count +"."+imageFormat;
                  								try (InputStream is = url.openStream(); OutputStream os = new FileOutputStream(destName);) {
                  									int length;
                  									while ((length = is.read(b)) != -1) {
                  										os.write(b, 0, length);
                  									}
                  								}
                  								count++;
            								}
                                        }
                                 }
                           }
                          }else {            
                                 for (WishlistEntryData entry : wishlistData.getWishlistEntries()) {
                                        if(entry.getProduct().getImages() != null) {
                                              for (ImageData image : entry.getProduct().getImages()) {
                                                    if (!image.getUrl().isEmpty() && IMG_TYPE.equals(image.getImageType().toString())) {
                                                           String primaryUrl;
                                                           primaryUrl = null != image.getThumbnailUrl() ? image.getThumbnailUrl(): image.getZoomUrl();
                                                           if(!primaryUrl.contains(resolution)) {
                                                                  UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(primaryUrl);
                                                                  urlBuilder.replaceQueryParam(PRID, resolution+WIDTH+resolution+HEIGHT);
                                                                  primaryUrl = urlBuilder.build().toUriString();
                                                           }
                                                           URL url = new URL(primaryUrl);
                                                           String destName = sourceFile + "///" + image.getName()+"."+imageFormat;
                                                           try(InputStream is = url.openStream();
                                                           OutputStream os = new FileOutputStream(destName);) {
                                                                  int length;
                                                                  while ((length = is.read(b)) != -1) {
                                                                        os.write(b, 0, length);
                                                                  }
                                                           }
                                                    }
                                              }
                                       }
                                 }
                          }
                          //create zip based on the images folder
                          FileOutputStream fout = new FileOutputStream(zip);
                          gpWishlistUtil.addImagesToZip(fout, sourceFile, resolution);
                          fout.close();
                          fout.flush();
                          File zipFileDownload = new File(zipFile);
                          if (zipFileDownload.length() > 1) {
                                 return zipFileDownload;
                          }
                    }
             }
             finally {
            	 gpWishlistUtil.delete(sourceFile);
             }
             return null;
       }
	
	@Override
	public  InputStream getInputStream(List<ProductData> products,List<String> files,String zipFileName) throws IOException
	{
		return gpWishlistUtil.createExcelFile(products,files,zipFileName);
	}
		@Override
		public Wishlist2Model getGuestWishList(String uuid, UserModel currentUser) {
			return gpWishlistDao.getGuestWishList(uuid, currentUser);
		}

		/* (non-Javadoc)
		 * @see com.gp.commerce.core.wishlist.services.GPWishlistService#getAllBrandSpecificProducts(de.hybris.platform.b2b.model.B2BUnitModel)
		 */
		@Override
		public List<ProductModel> getAllBrandSpecificProducts(B2BUnitModel b2bunit) {
			return gpWishlistDao.getAllBrandSpecificProducts(b2bunit);
		}
		
		/* (non-Javadoc)
		 * @see com.gp.commerce.core.wishlist.services.GPWishlistService#getB2BCustomerForEmail(java.lang.String)
		 */
		@Override
		public B2BCustomerModel getB2BCustomerForEmail(String email) {
			List<B2BCustomerModel> b2bCustomers = gpWishlistDao.getB2BCustomerForEmail(email);
			if(CollectionUtils.isNotEmpty(b2bCustomers)) {
				for (B2BCustomerModel customer :b2bCustomers) {
					if(StringUtils.isNotEmpty(customer.getOriginalUid()) && !gpWishlistUtil.valid(customer.getOriginalUid())) {
						return customer;
					}
					else {
						LOG.error("Invalid gpxpress UID: " + customer.getOriginalUid() + " found for given gpxpress email :" + email);
					}
				}
			}
			else {
				throw new UnknownIdentifierException("Cannot find Gpxpress b2b user with uid '" + email + "'");
			}
			return null;
		}
		
		/**
		 * create wishlist for the user.
		 *
		 * @param user the user
		 * @return wishlistModel
		 */
		public Wishlist2Model createWishlistForUser(UserModel user) {
			final Wishlist2Model wishlistModel = new Wishlist2Model();
			String guid = guidKeyGenerator.generate().toString();
			if(userService.isAnonymousUser(user)) {
				String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				wishlistModel.setName("Guest List_" + timestamp);
			}
			else {
				wishlistModel.setName(guid);
			}
			wishlistModel.setDescription("Wish List");
			wishlistModel.setDefault(Boolean.FALSE);
			wishlistModel.setUser(user);
			wishlistModel.setWishlistUid(guid);
			if (this.saveWishlist(user)) {
				this.getModelService().save(wishlistModel);
			}
			wishlistModel.setWishlistType(WishlistTypeEnum.USER_LIST);
			wishlistModel.setSite((CMSSiteModel)baseSiteService.getCurrentBaseSite());
			return wishlistModel;
		}

		public KeyGenerator getGuidKeyGenerator() {
			return guidKeyGenerator;
		}
		@Required
		public void setGuidKeyGenerator(KeyGenerator guidKeyGenerator) {
			this.guidKeyGenerator = guidKeyGenerator;
		}

		public Converter<Wishlist2Model, WishlistData> getWishlistConverter() {
			return wishlistConverter;
		}

		@Required
		public void setWishlistConverter(final Converter<Wishlist2Model, WishlistData> wishlistConverter) {
			this.wishlistConverter = wishlistConverter;
		}

		public Wishlist2Service getWishlistService() {
			return wishlistService;
		}

		@Required
		public void setWishlistService(final Wishlist2Service wishlistService) {
			this.wishlistService = wishlistService;
		}

		public ProductService getProductService() {
			return productService;
		}

		@Required
		public void setProductService(final ProductService productService) {
			this.productService = productService;
		}

		@Required
		public void setGpWishlistUtil(GPWishlistUtil gpWishlistUtil) {
			this.gpWishlistUtil = gpWishlistUtil;
		}	
}