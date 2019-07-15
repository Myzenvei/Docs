/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.wishlist.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.wishlist2.impl.daos.impl.DefaultWishlist2Dao;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.WishlistTypeEnum;
import com.gp.commerce.core.wishlist.dao.GPWishlistDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GP DAO class to perform custom whishlist DAO functionalities
 */
public class GPDefaultWishlistDao extends DefaultWishlist2Dao implements GPWishlistDao
{

	private static final String NAME = "name";
	private static final String USER = "user";
	private static final String WISH_LIST_ID = "wishListId";
	private static final String LIST_NAME = "listName";
	private static final String WISHLIST_TYPE = "type";
	private static final String CMSSITE = "cmsSite";
	private static final String MODIFIEDTIME = "modifiedtime";
	private static final Logger LOG = Logger.getLogger(GPDefaultWishlistDao.class.getName());
	private static final String SELECT_FAVORITES= "SELECT {" + Wishlist2Model.PK + "} FROM {" + Wishlist2Model._TYPECODE + "}";
	private static final String PRECURATED_LIST = "PRE_CURATED_LIST";
	private static final String USER_LIST = "USER_LIST";
	private static final String SHARE_CART_LIST = "SHARE_CART_LIST";
	private static final String GET_WISHLIST_BY_TYPE = "SELECT {w:PK} FROM {Wishlist2 as w JOIN WishlistTypeEnum as we ON {w.wishlistType}={we.pk}} where "
			+ "{w.user}=?user and {we.code}=?type";
	private static final String GET_SHARE_CART_LIST="SELECT {w:PK} FROM {Wishlist2 as w JOIN CMSSite as site on {w.site}= {site.pk}} where {w.wishlistType}=?type AND {site.pk}=?cmsSite AND {" +Wishlist2Model.CREATIONTIME+"} <= ?modifiedtime";
	private static final String GET_ALL_USER_LIST="SELECT {w.pk} FROM {WishList2 AS w} WHERE {user} = ?user and  {w.wishlistType}=?type";
	private static final String COMMA =",";
	private static final String GET_BRANDS_SPECIFIC_PRODUCTS="select {p.pk} from {product as p join brandsenum as b on {b.pk}={p.brand} join b2bunit2brandrel as rel on {rel.target}={b.pk} join b2bunit as unit on {unit.pk}={rel.source}} where {unit.uid}=?b2bunitUid";
	private static final String GET_B2B_CUSTOMER_FOR_EMAIL=" SELECT {b2bcustomer:pk} FROM {B2BCustomer AS b2bcustomer} WHERE {b2bcustomer.email} = ?email";
	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;
	
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Override
	public Wishlist2Model getWishlistByName(final UserModel user, final String name){
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_FAVORITES +" WHERE {user} = ?user AND {name} =?name ");
		fQuery.addQueryParameter(USER, user);
		fQuery.addQueryParameter(NAME, name);
		final SearchResult<Wishlist2Model> searchResult = getFlexibleSearchService().search(fQuery);
		if (searchResult.getCount() > 1) {
			LOG.warn("More than one favorites wishlist defined for user " + user.getName() + ". Returning first!");
		}
		return searchResult.getCount() > 0 ? (Wishlist2Model) searchResult.getResult().iterator().next() : null;
	}


	/**
	 * Fetch the wishlist of particular Type
	 * @param user
	 *          UserModel
	 *  @param type
	 *          Type of the Wishlist        
	 * @return
	 * 			Wishlist2Model
	 */
	@Override
	public Wishlist2Model getWishlistByType(final UserModel user, final String type){
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_WISHLIST_BY_TYPE);
		fQuery.addQueryParameter(USER, user);
		fQuery.addQueryParameter(WISHLIST_TYPE, type);
		final SearchResult<Wishlist2Model> searchResult = getFlexibleSearchService().search(fQuery);
		return searchResult.getCount() > 0 ? (Wishlist2Model) searchResult.getResult().iterator().next() : null;
	}

	@Override
	public Wishlist2Model getWishlistById(final String wishListId){
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_FAVORITES +" WHERE {wishlistUid} =?wishListId ");
		fQuery.addQueryParameter(WISH_LIST_ID, wishListId);
		final SearchResult<Wishlist2Model> searchResult = getFlexibleSearchService().search(fQuery);

		return searchResult.getCount() > 0 ? (Wishlist2Model) searchResult.getResult().iterator().next() : null;
	}

	/**
	 * Query DB and returns Precurated list by specific list name
	 * @param listName
	 * @return
	 */
	public Wishlist2Model getPrecuratedlist(final String listName) {
             final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {w.pk} FROM {WishList2 AS w JOIN CMSSITE AS c on {w:site} = {c:PK}} WHERE {w.wishlistUid} = ?listName and  {w.wishlistType}=?type and {c.uid}=?site");
             fQuery.addQueryParameter(LIST_NAME, listName);
             fQuery.addQueryParameter(WISHLIST_TYPE, WishlistTypeEnum.valueOf(PRECURATED_LIST));
             fQuery.addQueryParameter(GpcommerceCoreConstants.SITE, cmsSiteService.getCurrentSite().getUid());
             final SearchResult<Wishlist2Model> searchResult = getFlexibleSearchService().search(fQuery);
             if (searchResult.getCount() > 1) {
                    LOG.warn("More than one precurated list defined for user " + ". Returning first!");
             }
             return searchResult.getCount() > 0 ? (Wishlist2Model) searchResult.getResult().iterator().next() : null;
       }
	
	

	/**
	 * Fetch all the wishlists of particular Type
	 * @param site
	 *          Site in which wishlist belongs
	 * @param type
	 *          Type of the Wishlist  
	 * @param modifiedtime
	 *         Created time of the wishlist before the modified time     
	 * @return
	 * 			List of Wishlists
	 */
	public List<Wishlist2Model> getWishlistsByType(final WishlistTypeEnum type ,final CMSSiteModel site, Date modifiedtime)
		{
			final Map params = new HashMap();
			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_SHARE_CART_LIST);
			params.put(WISHLIST_TYPE, type);
			params.put(CMSSITE, site);
			params.put(MODIFIEDTIME, modifiedtime);
			searchQuery.addQueryParameters(params);
	
			return getFlexibleSearchService().<Wishlist2Model>search(searchQuery).getResult();
		}
	
	
	public List<Wishlist2Model> getAllWishlistByType(final UserModel user, final WishlistTypeEnum type)
	{
		final Map params = new HashMap();
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_ALL_USER_LIST);
		params.put(USER, user);
		params.put(WISHLIST_TYPE, type);
		fQuery.addQueryParameters(params);
		
		return getFlexibleSearchService().<Wishlist2Model>search(fQuery).getResult();
	}
	
	@Override
	public Wishlist2Model getSharedlist(String listName,String type) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {w.pk} FROM {WishList2 AS w} WHERE {w.wishlistUid} = ?listName and  {w.wishlistType}=?type");
        fQuery.addQueryParameter(LIST_NAME, listName);
        if(type.equalsIgnoreCase(WishlistTypeEnum.valueOf(USER_LIST).getCode()))
        {
        fQuery.addQueryParameter(WISHLIST_TYPE, WishlistTypeEnum.valueOf(USER_LIST));
        }
        
        if(type.equalsIgnoreCase(WishlistTypeEnum.valueOf(SHARE_CART_LIST).getCode()))
        {
        	fQuery.addQueryParameter(WISHLIST_TYPE, WishlistTypeEnum.valueOf(SHARE_CART_LIST));
        }
        final SearchResult<Wishlist2Model> searchResult = getFlexibleSearchService().search(fQuery);
        if (searchResult.getCount() > 1) {
               LOG.warn("More than one precurated list defined for user " + ". Returning first!");
        }
        return searchResult.getCount() > 0 ? (Wishlist2Model) searchResult.getResult().iterator().next() : null;
	}


	@Override
	public List<ProductModel> getProductsForCodes(String productCodes) {
	
		productCodes = productCodes.toUpperCase();
		final List<String> productCodeList = new ArrayList<>(Arrays.asList(productCodes.split(COMMA)));
		List<ProductModel> pcodes = new ArrayList<>(getProductCodesForCMIR(productCodeList));
		String query = "select tab.pk from (" + 
				"{{SELECT {pk}  from {Product} where upper({code}) in (?code) }}\r\n" + 
				"UNION ALL " + 
				"{{SELECT {pk}  from {Product} where upper({upc}) in (?upc) }}\r\n" + 
				"UNION ALL " + 
				"{{SELECT {pk} from {Product} where upper({gtin}) in (?gtin) }}  \r\n" + 
				"  )tab";
		
		
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		
		final Map<String, Object> params = new HashMap<>();
		params.put("code", productCodeList);
		params.put("upc", productCodeList);
		params.put("gtin", productCodeList);
		params.put("cmir", productCodeList);
		searchQuery.addQueryParameters(params);
		List<ProductModel> prodModel = new ArrayList<>(getFlexibleSearchService().<ProductModel>search(searchQuery).getResult());
		if(CollectionUtils.isNotEmpty(pcodes)) {
			prodModel.addAll(pcodes);
		}
		return prodModel;
	}


	private List<ProductModel> getProductCodesForCMIR(List<String> productCodeList) {
		
		String query = "select {pk} from {product as P JOIN GPCustomerMaterialInfo AS GF ON {GF.PRODUCTCODE}={P.CODE} } where upper({GF.cmircode})  in (?cmir)";
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		final Map<String, Object> params = new HashMap<>();
		params.put("cmir", productCodeList);
		searchQuery.addQueryParameters(params);
		return getFlexibleSearchService().<ProductModel>search(searchQuery).getResult();
	}


	@Override
	public Wishlist2Model getGuestWishList(String uuid, UserModel currentUser) {
		boolean isNameUnique = findAllWishListForUser(currentUser);
		FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {wishlistUid} = ?uuid");
		final Map<String, Object> params = new HashMap<>();
		params.put("uuid", uuid);
		fQuery.addQueryParameters(params);
		final List<Wishlist2Model> wishlist2Models = getFlexibleSearchService().<Wishlist2Model>search(fQuery).getResult();
		Wishlist2Model wishlist2Model=CollectionUtils.isNotEmpty(wishlist2Models) ? wishlist2Models.stream().filter(list -> uuid.equals(list.getWishlistUid())).findFirst().orElse(null):null;
		
		if(null !=wishlist2Model) {
			wishlist2Model.setUser(currentUser);
			if(!isNameUnique) {
				String name = wishlist2Model.getName()+"_"+System.currentTimeMillis();
				wishlist2Model.setName(name);
			}
			getModelService().save(wishlist2Model);
		}
		return wishlist2Model;
	}


	private boolean findAllWishListForUser(UserModel currentUser) {
			boolean isGuestListNameUnique = true;
			FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user");
			fQuery.addQueryParameter("user", currentUser);
			final List<Wishlist2Model> wishlist2Models = getFlexibleSearchService().<Wishlist2Model>search(fQuery).getResult();
			for(Wishlist2Model wishlist: wishlist2Models) {
				if("Guest List".equalsIgnoreCase(wishlist.getName())) {
					isGuestListNameUnique =false;
					break;
				}
			}
			return isGuestListNameUnique;
	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.wishlist.dao.GPWishlistDao#getAllBrandSpecificProducts(de.hybris.platform.b2b.model.B2BUnitModel)
	 */
	@Override
	public List<ProductModel> getAllBrandSpecificProducts(B2BUnitModel b2bunit) {
			FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_BRANDS_SPECIFIC_PRODUCTS);
			final Map<String, Object> params = new HashMap<>();
			params.put("b2bunitUid", b2bunit.getUid());
			searchQuery.addQueryParameters(params);
			LOG.info("Search Query:" +searchQuery + "b2b id:" + b2bunit.getUid());
			return getFlexibleSearchService().<ProductModel>search(searchQuery).getResult();
	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.wishlist.dao.GPWishlistDao#getB2BCustomerForEmail(java.lang.String)
	 */
	@Override
	public List<B2BCustomerModel> getB2BCustomerForEmail(String email) {
		FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_B2B_CUSTOMER_FOR_EMAIL);
		final Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		searchQuery.addQueryParameters(params);
		LOG.info("Search Query:" +searchQuery + "email:" + email);
		
		searchRestrictionService.disableSearchRestrictions();
		final List<B2BCustomerModel> b2bCustomers = getFlexibleSearchService().<B2BCustomerModel>search(searchQuery).getResult();
		searchRestrictionService.enableSearchRestrictions();

		return CollectionUtils.isEmpty(b2bCustomers) ? Collections.emptyList() : b2bCustomers;
		
	}
}