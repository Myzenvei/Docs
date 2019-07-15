/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.v2.helper;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteResultData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.FilterQueryOperator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchFilterQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductCategorySearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.content.search.data.GPSolrSearchPageData;
import com.gp.commerce.content.search.facades.GPContentSearchFacade;
import com.gp.commerce.content.search.facades.data.GPContentPageData;
import com.gp.commerce.content.search.facetdata.GPContentSearchPageData;
import com.gp.commerce.content.search.result.dto.GPContentSearchPageWsDTO;
import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.BrandLabelEnum;
import com.gp.commerce.core.enums.MaterialStatusEnum;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPCompetitorProductModel;
import com.gp.commerce.core.model.GPGenericVariantProductModel;
import com.gp.commerce.core.model.GPProductKitModel;
import com.gp.commerce.core.model.GPVariantProductKitModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;
import com.gp.commerce.facades.wishlist.impl.GPDefaultWishlistFacade;
import com.gp.commerce.search.data.AutocompleteResultWsDTO;
import com.gp.commerce.search.dto.GPSolrSearchResultWsDTO;
import com.gp.commerce.util.ws.SearchQueryCodec;


@Component
public class ProductsHelper extends AbstractHelper
{
	private static final String DEFAULT_GPUG = "DEFAULT_GPUG";

	private static final String GP_USERGROUP_STRING = "gpusergroup_string";

	private static final String HAS_VARIANT_BOOLEAN = "hasVariant_boolean";

	private static final String SHOW_VARIANT_PRODUCTS = "showVariantProducts";

	private static final String RATING = "rating";
	
	private static final String SAMPLE = "sample";

	private static final String IS_FAVORITES_ENABLED = "isFavoritesEnabled";

	private static final String DEFAULT_CSV_HEADER = "Mfg.Part_Number/Material_Number/Product_ID,Quantity,Product_Name,CMIR,UPC";

	private static final Logger LOG = Logger.getLogger(ProductsHelper.class);

	private static final String COMMA =",";
	private static final String QUICKORDER_PRODUCTS_MAXIMUM = "quickorder.products.maximum";
	private static final String QUICKORDER_CSV_HEADER = "quickorder.csv.header";
	private static final String CODE = "code";
	private static final String CHANNEL_FILTER = "channel_string";
	private static final String PRODUCT_AUTOSUGGEST_ENABLE = "obseleteOutOfStock_boolean";
	private static final String IS_AUTOSUGGEST_FILTER_ENABLED = "enable.autosuggest.obselete";
	private static final String IS_OBSELETE_FILTER_ENABLED = "enable.obselete";
	private static final String USERGROUPFILTER = "usergroup_string";
	private static final String BRANDFILTER = "brand_string";
	private static final String PRODUCT_DISTRIBUTOR_FILTER = "productDistributors_string_mv";
	private static final String ROOTCATEGORYFILTER = "rootCategory_string_mv";
	private static final String PRODUCT_FILTER = "itemtype_string";
	private static final String HIDDEN_PRODUCT_FILTER = "hiddenFromSearch_%context_boolean";
	private static final String IS_PRODUCT_VISIBILITY_CONTROLLED = "isProductVisibilityCotrolled";
	private static final String BRAND_LABEL_FILTER = "brandLabel_string";
	
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	@Resource(name = "cwsSearchQueryCodec")
	private SearchQueryCodec<SolrSearchQueryData> searchQueryCodec;
	@Resource(name = "solrSearchStateConverter")
	private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;
	@Resource(name = "wishlistFacade")
	GPDefaultWishlistFacade wishlistFacade;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "commerceCategoryService")
	private CommerceCategoryService commerceCategoryService;
	@Resource(name = "userService")
	private GPUserService userService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "b2bUnitService")
	protected B2BUnitService<B2BUnitModel, UserModel> b2bUnitService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "gpB2BUnitsService")
	private GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name="contentSearchFacade")
	private GPContentSearchFacade contentSearchFacade;
	
	/**
	 * @deprecated since 6.6. Please use {@link #searchProducts(String, int, int, String, String, String)} instead.
	 */
	@Deprecated
	public GPSolrSearchResultWsDTO searchProducts(final String query, final int currentPage, final int pageSize, final String sort,
			final String fields, final String searchText)
	{
		final GPSolrSearchPageData combinedResults = searchProducts(query, currentPage, pageSize, sort, searchText);
		filterFacets(combinedResults.getProductResult());
		decodeProductURLForVariants(combinedResults.getProductResult());
		populateFavoritesFlag(combinedResults.getProductResult());
		GPSolrSearchResultWsDTO gpSolrSearchResultWsDTO = new GPSolrSearchResultWsDTO();
		GPContentSearchPageWsDTO contentWsDTO = getDataMapper().map(combinedResults.getContentResult(), GPContentSearchPageWsDTO.class, fields);
		ProductSearchPageWsDTO productWsDTO = getDataMapper().map(combinedResults.getProductResult(), ProductSearchPageWsDTO.class, fields);
		
		gpSolrSearchResultWsDTO.setContentResult(contentWsDTO);
		gpSolrSearchResultWsDTO.setProductResult(productWsDTO);
		return gpSolrSearchResultWsDTO;
	}

	public GPSolrSearchPageData searchProducts(final String query, final int currentPage,
			final int pageSize, final String sort, final String searchText)
	{
		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		searchTerm2UpperCase(searchQueryData,searchText);

		applyFilterQueries(searchQueryData);

		final PageableData pageable = createPageableData(currentPage, pageSize, sort);
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = productSearchFacade
				.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
		
		
		orderFacetsByCount(sourceResult);
		
		GPSolrSearchPageData combinedResults = new GPSolrSearchPageData();
		combinedResults.setProductResult(sourceResult);
		
		
		revertSearchTerm(searchQueryData, sourceResult);
		return combinedResults;
	}

	private void orderFacetsByCount(final ProductSearchPageData<SearchStateData, ProductData> sourceResult) {
		if(sourceResult.getFacets() != null && !sourceResult.getFacets().isEmpty())
		{
			for(FacetData<SearchStateData> facetHeader:sourceResult.getFacets())
			{
				if(facetHeader.getValues()!=null && !facetHeader.getValues().isEmpty())
				{
					Collections.sort(facetHeader.getValues(), new Comparator<FacetValueData<SearchStateData>>() {
			            @Override
			            public int compare(final FacetValueData<SearchStateData> o1, final FacetValueData<SearchStateData> o2) {
			            	Long l1 = o1.getCount();
			            	Long l2 = o2.getCount();
			            		return (l2.compareTo(l1));
			            }
			        });
				}
			}
		}
	}

	public GPSolrSearchResultWsDTO searchProducts(final String query, final int currentPage, final int pageSize, final String sort,
			final String fields, final String searchQueryContext, final String searchText)
	{
		GPSolrSearchResultWsDTO gpSolrSearchResultWsDTO = new GPSolrSearchResultWsDTO();
		final SearchQueryContext context = decodeContext(searchQueryContext);

		GPSolrSearchPageData combinedResults = searchProducts(query, currentPage, pageSize, sort,
				context, searchText);

		filterFacets(combinedResults.getProductResult());
		decodeProductURLForVariants(combinedResults.getProductResult());
		populateFavoritesFlag(combinedResults.getProductResult());
		GPContentSearchPageWsDTO contentWsDTO = getDataMapper().map(combinedResults.getContentResult(), GPContentSearchPageWsDTO.class, fields);
		ProductSearchPageWsDTO productWsDTO = getDataMapper().map(combinedResults.getProductResult(), ProductSearchPageWsDTO.class, fields);
		
		gpSolrSearchResultWsDTO.setContentResult(contentWsDTO);
		gpSolrSearchResultWsDTO.setProductResult(productWsDTO);
		return gpSolrSearchResultWsDTO;
	}

	protected GPSolrSearchPageData searchProducts(final String query, final int currentPage,
			final int pageSize, final String sort, final SearchQueryContext searchQueryContext, final String searchText)
	{
		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		searchQueryData.setSearchQueryContext(searchQueryContext);
		
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);
		GPSolrSearchPageData combinedResults = new GPSolrSearchPageData();
		
		if(Boolean.valueOf(cmsSiteService.getSiteConfig(YcommercewebservicesConstants.IS_CONTENT_INDEXING_ENABLED))){
			final GPContentSearchPageData<SearchStateData, GPContentPageData> contentResult = contentSearchFacade
					.contentSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
			combinedResults.setContentResult(contentResult);
		}
		
		searchTerm2UpperCase(searchQueryData, searchText);
		searchQueryData.setSearchQueryContext(searchQueryContext);
		applyFilterQueries(searchQueryData);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = 
				productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
		
		orderFacetsByCount(sourceResult);
		
		combinedResults.setProductResult(sourceResult);
		
		revertSearchTerm(searchQueryData, sourceResult);
		

		return combinedResults;
	}

	protected SearchQueryContext decodeContext(final String searchQueryContext)
	{
		if (StringUtils.isBlank(searchQueryContext))
		{
			return null;
		}

		try
		{
			return SearchQueryContext.valueOf(searchQueryContext);
		}
		catch (final IllegalArgumentException e)
		{
			throw new RequestParameterException(searchQueryContext + " context does not exist", RequestParameterException.INVALID,
					e);
		}
	}

	public ProductSearchPageWsDTO searchCategories(final String categoryCode, final String query, final int currentPage, final int pageSize, final String sort, final String showMode,
			final String fields)
	{
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = searchCategories(categoryCode, query, currentPage, pageSize, sort);
		filterFacets(sourceResult);
		orderFacetsByCount(sourceResult);
		decodeProductURLForVariants(sourceResult);
		populateCategoryMedia(sourceResult);
		populateFavoritesFlag(sourceResult);
		if (sourceResult instanceof ProductCategorySearchPageData)
		{
			return getDataMapper().map(sourceResult, ProductCategorySearchPageWsDTO.class, fields);
		}

		return getDataMapper().map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}

	private void filterFacets(final ProductSearchPageData<SearchStateData, ProductData> sourceResult) {
		//Remove code from facets. Rating as well if the rating facet is selected.
		final List<FacetData<SearchStateData>> facets = sourceResult.getFacets();

		if(sourceResult.getBreadcrumbs().stream().anyMatch(i->i.getFacetCode().equalsIgnoreCase(RATING)))
		{
			facets.removeIf(facet -> facet.getCode().equalsIgnoreCase(RATING));
		}
		final UserModel user = userService.getCurrentUser();
		String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
		if(cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId))
		{
			final B2BCustomerModel b2bUser = (B2BCustomerModel) user;
			if(b2bUser.getApprovedSampleStatus() == null || !b2bUser.getApprovedSampleStatus().booleanValue())
			{
				facets.removeIf(facet -> facet.getCode().equalsIgnoreCase(SAMPLE));
			}
		}
		facets.removeIf(facet -> facet.getName().equalsIgnoreCase(CODE));
	}

	public ProductSearchPageData<SearchStateData, ProductData> searchCategories(final String categoryCode, final String query, final int currentPage,
			final int pageSize, final String sort)
	{

		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		applyFilterQueries(searchQueryData);
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);
		return productSearchFacade.categorySearch(categoryCode,solrSearchStateConverter.convert(searchQueryData), pageable);
	}

	private void applyFilterQueries(final SolrSearchQueryData searchQueryData)
	{
		final String siteUid = configurationService.getConfiguration().getString(YcommercewebservicesConstants.SITE_ID_LIST);
		final List<String> siteIdList = Arrays.asList(siteUid.split(","));
		final List<SolrSearchFilterQueryData> filterQueries = new ArrayList<>();
		
		filterOutCompetitorProducts(filterQueries);
		
		if(Boolean.valueOf(cmsSiteService.getSiteConfig(IS_PRODUCT_VISIBILITY_CONTROLLED))){
			filterOutHiddenProducts(filterQueries);
		}
		if (null != siteIdList && siteIdList.contains(cmsSiteService.getCurrentSite().getUid()))
		{
			createFilterQueries(searchQueryData, filterQueries);
		}
		else {
			// Adding userpricegroup filter for multibrand
			createUserGroupFilter(filterQueries);
			addMaterialStatusFilter(filterQueries);
			if(Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_VARIANT_PRODUCTS))) {
				createBaseProductsFilter(filterQueries);
			}
			searchQueryData.setFilterQueries(filterQueries);
		}
	}

	private void addMaterialStatusFilter(final List<SolrSearchFilterQueryData> filterQueries) {
		boolean obseleteFilterEnabled = false;
		obseleteFilterEnabled = Boolean.valueOf(cmsSiteService.getSiteConfig(IS_OBSELETE_FILTER_ENABLED));
		if(!obseleteFilterEnabled)
		{
			final SolrSearchFilterQueryData autoSuggestFilter = new SolrSearchFilterQueryData();
			autoSuggestFilter.setKey(PRODUCT_AUTOSUGGEST_ENABLE);
			final Set<String> values = new HashSet<>();
			values.add(Boolean.FALSE.toString());
			autoSuggestFilter.setValues(values);
			filterQueries.add(autoSuggestFilter);
		}
	}

	public AutocompleteResultWsDTO searchProductsForSuggestions(final String query, final int currentPage,
			final int pageSize, final String sort, final int maxProducts, final String fields)
	{
		final AutocompleteResultData resultData = new AutocompleteResultData();

		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		final String searchTerm = searchQueryData.getFreeTextSearch();
		searchTerm2UpperCase(searchQueryData,searchTerm);

		applyFilterQueries(searchQueryData);

		final PageableData pageable = createPageableData(currentPage, pageSize, sort);
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = productSearchFacade
					.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
		
		decodeProductURLForVariants(sourceResult);

		final List<ProductData> list = sourceResult.getResults();

		populateFavoritesFlag(sourceResult);

		// When the product code matches the typed in search id, then set the PDP redirect URL
		List<String> pdpRedirectURL = null;
		//Assigning the value to a final variable so that it can be used in the lambda expression.
		if (list.stream().anyMatch(i -> i.getCode().equalsIgnoreCase(searchTerm)))
		{
			pdpRedirectURL = list.stream().filter(i -> i.getCode().equalsIgnoreCase(searchTerm)).map(ProductData::getUrl)
					.collect(Collectors.toList());
			resultData.setPdpRedirectURL(pdpRedirectURL.get(0));
		}
		
		boolean autoSuggestFilterEnabled = false;
		autoSuggestFilterEnabled = Boolean.valueOf(cmsSiteService.getSiteConfig(IS_AUTOSUGGEST_FILTER_ENABLED));
		//filtering after  fetching results to addresss landing on PDP issue
		if(autoSuggestFilterEnabled)
		{
			List<ProductData> filterObseleteRecords = null;
			filterObseleteRecords = list.stream().filter(i -> MaterialStatusEnum.OBSOLETE.getCode().equalsIgnoreCase(i.getMaterialStatus()))
			.collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(filterObseleteRecords))
			{
				for(ProductData obseletRecord: filterObseleteRecords)
				{
					if(sourceResult.getResults().contains(obseletRecord))
					{
						sourceResult.getResults().remove(obseletRecord);
					}
				}
			}
		}

		resultData.setProducts(
				list.isEmpty() ? Collections.emptyList() : ((list.size() > maxProducts) ? list.subList(0, maxProducts) : list));

		return getDataMapper().map(resultData, AutocompleteResultWsDTO.class, fields);

	}

	//changing searchTermtoUpperCase to make it case insensitive search. This is only for product search
	private void searchTerm2UpperCase(final SolrSearchQueryData searchQueryData, final String searchText)
	{
		if(StringUtils.isNotEmpty(searchText)) {
			searchQueryData.setSearchTerm(searchText);
		}else {
			searchQueryData.setSearchTerm(searchQueryData.getFreeTextSearch());
		}
		searchQueryData.setFreeTextSearch(searchQueryData.getFreeTextSearch().toUpperCase(userService.getCurrentLocale()));
	}

	//reverting search term to user entered case after the solr search call
	private void revertSearchTerm(final SolrSearchQueryData searchQueryData,
			final ProductSearchPageData<SearchStateData, ProductData> sourceResult)
	{
		sourceResult.getCurrentQuery().setUrl(sourceResult.getCurrentQuery().getUrl().replaceFirst(searchQueryData.getFreeTextSearch(),
                searchQueryData.getSearchTerm()));

		sourceResult.setFreeTextSearch(searchQueryData.getSearchTerm());
	}


	public List<ProductData> prepareProductList(final String productCodes) {

		final List<ProductData> productDataList = new ArrayList<>();
		final List<String> productCodeList = Arrays.asList(productCodes.split(COMMA));
		ProductData productData = null;
		for(final String code : productCodeList){
			productData = new ProductData();
			productData.setCode(code);
			productData.setCount(1);
			productDataList.add(productData);
		}
		return productDataList;
	}

	/**
	 * Validating Products List in Quick Order CSV
	 * @param productList
	 */
	public boolean validateProductList(final List<ProductData> productList) {

		if(CollectionUtils.isNotEmpty(productList)){
			final int length = productList.size();
			if(!(length <= Config.getInt(QUICKORDER_PRODUCTS_MAXIMUM, 100))){
				return false;
			}
			if(productList.stream().anyMatch(product -> StringUtils.isEmpty(product.getCode()))){
				return false;
			}
			productList.forEach( product -> {
				product.setCode(product.getCode().toUpperCase(userService.getCurrentLocale()));
			});
			return true;
		}
		return false;
	}

	/**
	 * Validating Header of Quick Order CSV
	 * @param file
	 */
	public boolean validateCSVHeader(final MultipartFile file) throws IOException {

		    try(InputStream is = file.getInputStream();BufferedReader br = new BufferedReader(new InputStreamReader(is)))
		    {

		    //Reading the first line to read the header
		    final String line = br.readLine();

		    if(StringUtils.isNotBlank(line)) {
				final String[] csvHeader = line.split(COMMA);
				final List<String> headersArray = Arrays.asList(csvHeader);

				final String expectedHeader = Config.getString(QUICKORDER_CSV_HEADER, DEFAULT_CSV_HEADER);
				final String[] expectedHeaders = expectedHeader.split(COMMA);
				final List<String> expectedHeadersArray = Arrays.asList(expectedHeaders);

				if (expectedHeadersArray.equals(headersArray)) {
			        return true;
			    }
		    }

		} catch (final IOException e) {
			LOG.error("Exception occured in reading Quick Order CSV Header " + e.getMessage() ,e);
		}
		return false;
	}

	/**
	 * Populate favorite flag for a product.
	 * @param sourceResult
	 */
	private void populateFavoritesFlag(final ProductSearchPageData<SearchStateData, ProductData> sourceResult)
	{
		final String isFavoritesConfigured = cmsSiteService.getSiteConfig(IS_FAVORITES_ENABLED);
		if (Boolean.TRUE.equals(Boolean.valueOf(isFavoritesConfigured)))
		{
			final WishlistData wishlistData = wishlistFacade.getFavorites();
			final List<WishlistEntryData> wishListEntries = wishlistData.getWishlistEntries();

			if (null != sourceResult.getResults() && null != wishListEntries)
			{

				sourceResult.getResults().stream().forEach(productData -> {
					wishListEntries.stream().forEach(wishEntry -> {
						if (wishEntry.getProduct().getCode().equalsIgnoreCase(productData.getCode()))
						{
							productData.setIsFavorite(true);
      							}
      						});
      					});
      				}
		}
	}

	/**
	 * Decode slash in the product code in product url
	 * @param sourceResult
	 */
	private void decodeProductURLForVariants(final ProductSearchPageData<SearchStateData, ProductData> sourceResult) {
		if(null != sourceResult.getResults()){
			sourceResult.getResults().stream().forEach(productData -> {
				String url = productData.getUrl();
				final String replaceString = url.substring(url.lastIndexOf("/")).replaceAll("%2F", "/");
				url = url.replace(url.substring(url.lastIndexOf("/")), replaceString);
				productData.setUrl(url);
			});
		}
	}
	/***
	 * Method added to add the Category MEDIA and Name in the category call
	 * @param sourceResult
	 */
	private void populateCategoryMedia(final ProductSearchPageData<SearchStateData, ProductData> sourceResult) {
		final CategoryModel category = commerceCategoryService.getCategoryForCode(sourceResult.getCategoryCode());
		if(null != category.getPicture())
		{
			sourceResult.setTopImage(category.getPicture().getURL());
		}
			sourceResult.setCategoryName(category.getName());
	}

	private void createFilterQueries(final SolrSearchQueryData searchQueryData, List<SolrSearchFilterQueryData> filterQueries) {
		final SolrSearchFilterQueryData channelFilter = new SolrSearchFilterQueryData();

		final String channel = cmsSiteService.getCurrentSite().getUid();
		channelFilter.setKey(CHANNEL_FILTER);
		final Set<String> values = new HashSet<>();
		values.add(channel);
		channelFilter.setValues(values);

		createUserGroupFilter(filterQueries);
		addMaterialStatusFilter(filterQueries);

		if (Boolean.valueOf(cmsSiteService.getSiteConfig(YcommercewebservicesConstants.IS_BRAND_ENABLED))) {
			final SolrSearchFilterQueryData brandLabelFilter = new SolrSearchFilterQueryData();
			brandLabelFilter.setKey(BRAND_LABEL_FILTER);
			final SolrSearchFilterQueryData rootCategoryFilter = new SolrSearchFilterQueryData();
			rootCategoryFilter.setKey(ROOTCATEGORYFILTER);
			Set<String> brandValues = new HashSet<>();
			Set<String> categoryValues = new HashSet<>();
			B2BUnitModel unit = sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID);
			if (null != unit) {
				List<BrandLabelEnum> brandLabels = new ArrayList<>(unit.getBrandLabel());
				for (BrandLabelEnum brandLabel : brandLabels) {
					brandValues.add(brandLabel.getCode());
					brandLabelFilter.setOperator(FilterQueryOperator.OR);
				}
					categoryValues.add(unit.getRootCategoryReference().getCode());
			}
			if (!brandValues.isEmpty()) {
				brandLabelFilter.setValues(brandValues);
			} else {
				String noBrand = configurationService.getConfiguration()
						.getString(YcommercewebservicesConstants.NO_BRAND);
				brandValues.add(noBrand);
				brandLabelFilter.setValues(brandValues);
			}
			if (!categoryValues.isEmpty()) {
				rootCategoryFilter.setValues(categoryValues);
			} else {
				String noCategory = configurationService.getConfiguration()
						.getString((YcommercewebservicesConstants.NO_CATEGORY));
				categoryValues.add(noCategory);
				rootCategoryFilter.setValues(categoryValues);
			}
			filterQueries.add(brandLabelFilter);
			filterQueries.add(rootCategoryFilter);
		}
		
		if(null != searchQueryData.getFilterTerms() && !searchQueryData.getFilterTerms().isEmpty() && Boolean.valueOf(cmsSiteService.getSiteConfig(YcommercewebservicesConstants.IS_AVAILABLE_LOCATION_ENABLED))){
		final String availableLocationKey = configurationService.getConfiguration().getString(YcommercewebservicesConstants.AVAILABLE_LOCATION_KEY);
		Optional<SolrSearchQueryTermData> filterTerm = searchQueryData.getFilterTerms().stream().filter(data -> data.getKey().equalsIgnoreCase(availableLocationKey)).findFirst();
		final Set<String> distributorValues = new HashSet<>();
		if(filterTerm.isPresent()){
			final SolrSearchFilterQueryData distributorFilter = new SolrSearchFilterQueryData();
			distributorFilter.setKey(PRODUCT_DISTRIBUTOR_FILTER);
			UserModel userModel = userService.getCurrentUser();
			if(userService.getCurrentUser() instanceof B2BCustomerModel){
				B2BCustomerModel customer = (B2BCustomerModel) userModel;
				B2BUnitModel unit = customer.getDefaultB2BUnit();
				distributorValues.add(unit.getUid());
			}
			if(distributorValues.isEmpty()){
				String noDistributor = configurationService.getConfiguration().getString(YcommercewebservicesConstants.NO_DISTRIBUTOR);
				distributorValues.add(noDistributor);
			}
			distributorFilter.setValues(distributorValues);
			filterQueries.add(distributorFilter);
			}
		}
		
		if(Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_VARIANT_PRODUCTS))) {
			createBaseProductsFilter(filterQueries);
		}

		searchQueryData.setFilterQueries(filterQueries);
	}

	private void createBaseProductsFilter(List<SolrSearchFilterQueryData> filterQueries) {
		final SolrSearchFilterQueryData baseProductFilter = new SolrSearchFilterQueryData();
		baseProductFilter.setKey(HAS_VARIANT_BOOLEAN);
		Set<String> values =  new HashSet<>();
		values.add(Boolean.FALSE.toString());
		baseProductFilter.setValues(values);
		filterQueries.add(baseProductFilter);
	}
	
	private void createUserGroupFilter(final List<SolrSearchFilterQueryData> filterQueries) {
		final SolrSearchFilterQueryData userGroupFilter = new SolrSearchFilterQueryData();
		final SolrSearchFilterQueryData gpUserGroupFilter = new SolrSearchFilterQueryData();
		gpUserGroupFilter.setKey(GP_USERGROUP_STRING);
		userGroupFilter.setKey(USERGROUPFILTER);
		final Set<String> gpUserGroupValues = new HashSet<>();
		final Set<String> userGroupValues = new HashSet<>();
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		if(null != baseStore.getUserPriceGroup()) {
			userGroupValues.add(baseStore.getUserPriceGroup().getCode());
		}
		
		if(!userService.isAnonymousUser(userService.getCurrentUser())){
			final Set<PrincipalGroupModel> usergroups = userService.getCurrentUser().getGroups();
			for (final PrincipalGroupModel group : usergroups){
				if(group instanceof UserGroupModel){
				final UserGroupModel userGroup = (UserGroupModel) group;
					if(null != userGroup.getUserPriceGroup()){
						gpUserGroupValues.add(userGroup.getUserPriceGroup().getCode());
						break;
					}
				}
			}
			if(CollectionUtils.isEmpty(gpUserGroupValues))
			{
				gpUserGroupValues.add(DEFAULT_GPUG);
			}
		}
		else
		{
			gpUserGroupValues.add(DEFAULT_GPUG);
		}

		userGroupFilter.setValues(userGroupValues);
		
		filterQueries.add(userGroupFilter);
		
		if (!gpUserGroupValues.isEmpty())
		{
			gpUserGroupFilter.setValues(gpUserGroupValues);
			filterQueries.add(gpUserGroupFilter);
		}
		
		
	}
	
	public void setSoldToInUser(final String soldToId, final String userId)
	{
		if (null != soldToId && null != userId)
		{
			final UserModel userModel = userService.getUserForUID(userId);
			if (userModel instanceof B2BCustomerModel)
			{
				final B2BCustomerModel b2bCustomer = (B2BCustomerModel) userModel;
				final B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(soldToId);
				b2bCustomer.setDefaultB2BUnit(b2bUnit);
			}
			userService.setCurrentUser(userModel);
		}
	}

	private void filterOutCompetitorProducts(List<SolrSearchFilterQueryData> filterQueries) {
				final SolrSearchFilterQueryData competitorProductFilter = new SolrSearchFilterQueryData();
		
				competitorProductFilter.setKey(PRODUCT_FILTER);
				competitorProductFilter.setOperator(FilterQueryOperator.OR);
				final Set<String> values = new HashSet<>();
				values.add(GPCommerceProductModel._TYPECODE);
				values.add(GPProductKitModel._TYPECODE);
				if(Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_VARIANT_PRODUCTS))) {
					values.add(GPGenericVariantProductModel._TYPECODE);
					values.add(GPVariantProductKitModel._TYPECODE);
				}
				
				competitorProductFilter.setValues(values);
				filterQueries.add(competitorProductFilter);
	}
	
	private void filterOutHiddenProducts(List<SolrSearchFilterQueryData> filterQueries) {
		final SolrSearchFilterQueryData hiddenProductFilter = new SolrSearchFilterQueryData();
		hiddenProductFilter.setKey(HIDDEN_PRODUCT_FILTER.replace("%", cmsSiteService.getCurrentSite().getUid()));
		final Set<String> values = new HashSet<>();
		values.add("false");
		hiddenProductFilter.setValues(values);
		filterQueries.add(hiddenProductFilter);
}
	
	public ProductSearchPageWsDTO searchCompetitorProducts(final String query, final int currentPage, final int pageSize, final String sort,
			final String fields, final String searchQueryContext, final String searchText)
	{
		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		final SearchQueryContext context = decodeContext(searchQueryContext);
		searchQueryData.setSearchQueryContext(context);
		
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);
		
		createFilterQueryToGetCompetitorProducts(searchQueryData);
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = 
				productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
		
		populateFavoritesFlag(sourceResult);
		filterFacets(sourceResult);
		decodeProductURLForVariants(sourceResult);
		
		for (Object object : sourceResult.getResults()) {
			if(object instanceof ProductData){
				StringBuilder query1 = new StringBuilder();
				ProductData pdata = (ProductData)object;
				if(CollectionUtils.isNotEmpty(pdata.getCompetitorReplacementProductCodes())){
					query1.append(":relevance");
					pdata.getCompetitorReplacementProductCodes().stream().forEach(i->query1.append(":code:"+i));
				}
				if(StringUtils.isNotEmpty(query1.toString())){
					final ProductSearchPageData<SearchStateData, ProductData> replacementProductResults = getReplacementProducts(query1.toString(),null);
					pdata.setCompetitorReplacementProducts(replacementProductResults.getResults());
				}
			}
		}
		return getDataMapper().map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}
	
	private void createFilterQueryToGetCompetitorProducts(SolrSearchQueryData searchQueryData) {
		final List<SolrSearchFilterQueryData> filterQueries = new ArrayList<>();
		final SolrSearchFilterQueryData competitorProductFilter = new SolrSearchFilterQueryData();

		competitorProductFilter.setKey(PRODUCT_FILTER);
		final Set<String> values = new HashSet<>();
		values.add(GPCompetitorProductModel._TYPECODE);
		competitorProductFilter.setValues(values);
		filterQueries.add(competitorProductFilter);
		searchQueryData.setFilterQueries(filterQueries);
	}

	private ProductSearchPageData<SearchStateData, ProductData>  getReplacementProducts(String query, final String searchQueryContext) {
		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		final SearchQueryContext context = decodeContext(searchQueryContext);
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = 
				productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), null);
		
		return sourceResult;
		
	}

	public ProductSearchPageWsDTO searchCompetitorCategories(final String categoryCode, final String query, final int currentPage, final int pageSize, final String sort, final String showMode,
			final String fields)
	{
		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		createFilterQueryToGetCompetitorProducts(searchQueryData);
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);
		ProductSearchPageData<SearchStateData, ProductData> sourceResult =  productSearchFacade.categorySearch(categoryCode,solrSearchStateConverter.convert(searchQueryData), pageable);
		
		filterFacets(sourceResult);
		orderFacetsByCount(sourceResult);
		decodeProductURLForVariants(sourceResult);
		populateCategoryMedia(sourceResult);
		
		for (Object object : sourceResult.getResults()) {
			if(object instanceof ProductData){
				StringBuilder query1 = new StringBuilder();
				ProductData pdata = (ProductData)object;
				if(CollectionUtils.isNotEmpty(pdata.getCompetitorReplacementProductCodes())){
					query1.append(":relevance");
					pdata.getCompetitorReplacementProductCodes().stream().forEach(i->query1.append(":code:"+i));
				}
				if(StringUtils.isNotEmpty(query1.toString())){
					final ProductSearchPageData<SearchStateData, ProductData> replacementProductResults = getReplacementProducts(query1.toString(),null);
					pdata.setCompetitorReplacementProducts(replacementProductResults.getResults());
				}
			}
		}
		
		if (sourceResult instanceof ProductCategorySearchPageData)
		{
			return getDataMapper().map(sourceResult, ProductCategorySearchPageWsDTO.class, fields);
		}
		return getDataMapper().map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}
	
	public B2BUnitService<B2BUnitModel, UserModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, UserModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}
}
