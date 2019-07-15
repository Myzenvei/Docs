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
package com.gp.commerce.v2.controller;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.catalog.CatalogFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commercefacades.product.data.ProductReferencesData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.product.data.SuggestionData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.storefinder.StoreFinderStockFacade;
import de.hybris.platform.commercefacades.storefinder.data.StoreFinderStockSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductReferenceListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.StockWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.SuggestionListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.queues.ProductExpressUpdateElementListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderStockSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.content.search.data.GPSolrSearchPageData;
import com.gp.commerce.core.constants.GeneratedGpcommerceCoreConstants.Enumerations.MaterialStatusEnum;
import com.gp.commerce.core.exceptions.GPCommerceDataException;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.forms.ShareProductResourceForm;
import com.gp.commerce.core.model.GPCompetitorProductModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.dto.cart.GPQuickOrderWSDTO;
import com.gp.commerce.facade.quickorder.GPQuickOrderFacade;
import com.gp.commerce.facades.data.GPQuickOrderData;
import com.gp.commerce.facades.product.GPProductFacade;
import com.gp.commerce.facades.product.references.data.GPProductReferenceData;
import com.gp.commerce.facades.search.compare.GPSearchCompareProductsFacade;
import com.gp.commerce.facades.shareproduct.GPShareProductFacade;
import com.gp.commerce.formatters.WsDateFormatter;
import com.gp.commerce.product.data.GPProductDataList;
import com.gp.commerce.dto.product.GPProductDataListWsDTO;
import com.gp.commerce.product.data.GPProductListWsDTO;
import com.gp.commerce.product.data.ReviewDataList;
import com.gp.commerce.product.data.SuggestionDataList;
import com.gp.commerce.product.references.data.GPProductReferenceWsDTO;
import com.gp.commerce.queues.data.ProductExpressUpdateElementData;
import com.gp.commerce.queues.data.ProductExpressUpdateElementDataList;
import com.gp.commerce.queues.impl.ProductExpressUpdateQueue;
import com.gp.commerce.search.compare.data.ComparisonWsDTO;
import com.gp.commerce.search.data.AutocompleteResultWsDTO;
import com.gp.commerce.search.dto.GPSolrSearchResultWsDTO;
import com.gp.commerce.stock.CommerceStockFacade;
import com.gp.commerce.swagger.ApiBaseSiteIdParam;
import com.gp.commerce.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import com.gp.commerce.v2.helper.ProductsHelper;
import com.gp.commerce.validator.PointOfServiceValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;


/**
 * Web Services Controller to expose the functionality of the
 * {@link de.hybris.platform.commercefacades.product.ProductFacade} and SearchFacade.
 */

@Secured(
{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT","ROLE_GUEST"})
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@Controller
@Api(tags = "Products")
@RequestMapping(value = "/{baseSiteId}/products")
public class ProductsController extends BaseController
{
	private static final String REVIEW_DTO_FIELDS = "alias,rating,headline,comment";
	private static final String REVIEW = "review";
	private static final String REVIEW_DATA = "reviewData";
	private static final String STORE_NAME = "storeName";
	private static final String BASIC_OPTION = "BASIC";
	private static final Set<ProductOption> OPTIONS;
	private static final String MAX_INTEGER = "2147483647";
	private static final int CATALOG_ID_POS = 0;
	private static final int CATALOG_VERSION_POS = 1;
	protected static final String BLANK_STRING = " ";
	private static final Logger LOG = Logger.getLogger(ProductsController.class);
	private static final String PRODUCT_OPTIONS;
	private static final String INVALID_QUICKORDER_PRODUCTLIST = "invalid.quickorder.productlist";
	private static final String INVALID_QUICKORDER_UPLOAD = "invalid.quickorder.upload";
	private static final String INVALID_QUICKORDER_FILE_FORMAT = "invalid.quickorder.file.format";
	private static final String SHARE_QUICKORDER_EXCEPTION = "share.quickorder.exception";
	private static final String SUGGEST_PRODUCT_EXCEPTION = "suggest.from.quickorder.exception";
	private static final String DEFAULT_PAGE_SIZE_PRODUCT_SEARCH = "64";
	@Resource(name = "storeFinderStockFacade")
	private StoreFinderStockFacade storeFinderStockFacade;
	@Resource(name = "wsDateFormatter")
	private WsDateFormatter wsDateFormatter;
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	@Resource(name = "httpRequestReviewDataPopulator")
	private Populator<HttpServletRequest, ReviewData> httpRequestReviewDataPopulator;
	@Resource(name = "reviewValidator")
	private Validator reviewValidator;
	@Resource(name = "reviewDTOValidator")
	private Validator reviewDTOValidator;
	@Resource(name = "commerceStockFacade")
	private CommerceStockFacade commerceStockFacade;
	@Resource(name = "pointOfServiceValidator")
	private PointOfServiceValidator pointOfServiceValidator;
	@Resource(name = "productExpressUpdateQueue")
	private ProductExpressUpdateQueue productExpressUpdateQueue;
	@Resource(name = "catalogFacade")
	private CatalogFacade catalogFacade;
	@Resource(name = "productsHelper")
	private ProductsHelper productsHelper;
	@Resource(name = "searchCompareProductsFacade")
	private GPSearchCompareProductsFacade searchCompareProductsFacade;
    @Resource(name = "shareProductFacade")
	private GPShareProductFacade shareProductFacade;
	@Resource(name = "productFacade")
	private GPProductFacade productFacade;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "gpQuickOrderFacade")
	private GPQuickOrderFacade gpQuickOrderFacade;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "i18NService")
	private I18NService i18NService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	static
	{
		String productOptions = "";

		for (final ProductOption option : ProductOption.values())
		{
			productOptions = productOptions + option.toString() + " ";
		}
		productOptions = productOptions.trim().replace(" ", YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		PRODUCT_OPTIONS = productOptions;
		OPTIONS = extractOptions(productOptions);
	}

	protected static Set<ProductOption> extractOptions(final String options)
	{
		final String[] optionsStrings = options.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<ProductOption> opts = new HashSet<ProductOption>();
		for (final String option : optionsStrings)
		{
			opts.add(ProductOption.valueOf(option));
		}
		return opts;
	}


	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of products and additional data", notes = "Returns a list of products and additional data such as: available facets, available sorting and pagination options."
			+ "It can include spelling suggestions. To make spelling suggestions work you need to: 1. Make sure enableSpellCheck on the SearchQuery is set to true. By default it should be already set to true. 2. Have indexed properties configured to be used for spellchecking.")
	@ApiBaseSiteIdParam
	public GPSolrSearchResultWsDTO searchProducts(
			@ApiParam(value = "Serialized query, free text search, facets. The format of a serialized query: freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String query,
			@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue =DEFAULT_PAGE_SIZE_PRODUCT_SEARCH) final int pageSize,
			@ApiParam(value = "") @RequestParam(required = false) final String sort,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "The context to be used in the search query.") @RequestParam(required = false) final String searchQueryContext,
			@ApiParam(value = "The actual string user searched.") @RequestParam(required = false) final String searchText,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@ApiParam(value = "The user to be put in session.") @RequestParam(required = false) final String userId,
			final HttpServletResponse response)
	{
		productsHelper.setSoldToInUser(soldToId, userId);
		final GPSolrSearchResultWsDTO result = productsHelper.searchProducts(query, currentPage, getConfiguredPageSize(pageSize), sort,
				addPaginationField(fields), searchQueryContext, searchText);

		// X-Total-Count header
		/*
		 * YCOM-568- Start- When the product code matches the typed in search id, then
		 * set the PDP redirect URL
		 */
		final List<ProductWsDTO> productList = result.getProductResult().getProducts();
		List<String> pdpRedirectURL = null;
		if (productList.stream().anyMatch(i -> i.getCode().equalsIgnoreCase(query)))
		{
			pdpRedirectURL = productList.stream().filter(i -> i.getCode().equalsIgnoreCase(query)).map(ProductWsDTO::getUrl)
					.collect(Collectors.toList());
			result.getProductResult().setPdpRedirectURL(pdpRedirectURL.get(0));
		} else {
			result.getProductResult().setPdpRedirectURL("");
		}
		/*
		 * YCOM-568- End - When the product code matches the typed in search id, then
		 * set the PDP redirect URL
		 */
		setTotalCountHeader(response, result.getProductResult().getPagination());
		return result;
	}


	private int getConfiguredPageSize(final int pageSize) {
		int configuredPageSize = Integer.parseInt(DEFAULT_PAGE_SIZE_PRODUCT_SEARCH);
		if(pageSize != Integer.parseInt(DEFAULT_PAGE_SIZE_PRODUCT_SEARCH))
		{
			configuredPageSize = pageSize;
		}
		else if(StringUtils.isNotEmpty(cmsSiteService.getSiteConfig("product.search.page.size")))
		{
			configuredPageSize = Integer.parseInt(cmsSiteService.getSiteConfig("product.search.page.size"));
		}
		return configuredPageSize;
	}


	@RequestMapping(value = "/search", method = RequestMethod.HEAD)
	@ApiOperation(value = "Get a header with total number of products", notes = "Returns X-Total-Count header with total number of products satisfying a query. It doesn't return HTTP body.")
	@ApiBaseSiteIdParam
	public void countSearchProducts(
			@ApiParam(value = "Serialized query, free text search, facets. The format of a serialized query: freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String query,
			@ApiParam(value="") @RequestParam(required=false) final String searchText,
			final HttpServletResponse response)
	{
		GPSolrSearchPageData result  = productsHelper.searchProducts(query, 0, 1, null, searchText);
		setTotalCountHeader(response, result.getProductResult().getPagination());
	}

	@RequestMapping(value = "/compare", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get comparison of product attributes", notes = "Returns comparison of classification attributes for selected products.")
	@ApiBaseSiteIdParam
	public ComparisonWsDTO getComparison(
			@ApiParam(value = "Serialized query, compare products. The format of a serialized query: productCode1:productCode2:productCode3:productCode4") @RequestParam(required = true) final String productCodes,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = true) final String pageType, final HttpServletResponse response) throws GPCommerceDataException
	{
		return getDataMapper().map(searchCompareProductsFacade.compare(productCodes,pageType), ComparisonWsDTO.class, fields);
	}


	@RequestMapping(value = "/{productCode}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get product details", notes = "Returns details of a single product according to a product code.")
	@ApiBaseSiteIdParam
	public ProductWsDTO getProductByCode(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@ApiParam(value = "The user to be put in session.") @RequestParam(required = false) final String userId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductByCode: code=" + sanitize(productCode) + " | options=" + PRODUCT_OPTIONS);
		}

		productsHelper.setSoldToInUser(soldToId, userId);
		final ProductData product = productFacade.getProductForCodeAndOptions(productCode, OPTIONS);
		productFacade.populateAdditionalFields(product);

		return getDataMapper().map(product, ProductWsDTO.class, fields);
	}

	@RequestMapping(value = "/{productCode}/{variantCode}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get product variant details", notes = "Returns details of a single product variant according to product code and variant code.")
	@ApiBaseSiteIdParam
	public ProductWsDTO getProductVariantByCode(
			@ApiParam(value = "Product identifier", required = true) @PathVariable  final String productCode,
			@ApiParam(value = "Product variant identifier", required = true) @PathVariable final String variantCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final String productVariantCode = productCode+"/"+variantCode ;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductByCode: code=" + sanitize(productVariantCode) + " | options=" + PRODUCT_OPTIONS);
		}

		final ProductData product = productFacade.getProductForCodeAndOptions(productVariantCode, OPTIONS);
		productFacade.populateAdditionalFields(product);

		return getDataMapper().map(product, ProductWsDTO.class, fields);
	}

	@RequestMapping(value = "/{productCode}/stock/{storeName}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a product's stock level for a store", notes = "Returns product's stock level for a particular store (POS).")
	public StockWsDTO getStockData(
			@ApiParam(value = "Base site identifier", required = true) @PathVariable final String baseSiteId,
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Store identifier", required = true) @PathVariable final String storeName,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException, StockSystemException //NOSONAR
	{
		validate(storeName, STORE_NAME, pointOfServiceValidator);
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId))
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stockData = commerceStockFacade.getStockDataForProductAndPointOfService(productCode, storeName);
		return getDataMapper().map(stockData, StockWsDTO.class, fields);
	}


	@RequestMapping(value = "/{productCode}/stock", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a product's stock level", notes = "Returns product's stock levels sorted by distance from specific location passed by free-text parameter or longitude and latitude parameters. The following two sets of parameters are available:  location (required), currentPage (optional), pageSize (optional) or longitude (required), latitude (required), currentPage (optional), pageSize(optional).")
	@ApiBaseSiteIdParam
	public StoreFinderStockSearchPageWsDTO searchProductStockByLocation(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode, //NOSONAR
			@ApiParam(value = "Free-text location") @RequestParam(required = false) final String location,
			@ApiParam(value = "Latitude location parameter.") @RequestParam(required = false) final Double latitude,
			@ApiParam(value = "Longitude location parameter.") @RequestParam(required = false) final Double longitude,
			@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletResponse response)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductStockByLocation: code=" + sanitize(productCode) + " | location=" + sanitize(location)
					+ " | latitude=" + latitude + " | longitude=" + longitude);
		}

		final StoreFinderStockSearchPageData result = doSearchProductStockByLocation(productCode, location, latitude, longitude,
				currentPage, pageSize);

		// X-Total-Count header
		setTotalCountHeader(response, result.getPagination());

		return getDataMapper().map(result, StoreFinderStockSearchPageWsDTO.class, addPaginationField(fields));
	}


	@RequestMapping(value = "/{productCode}/stock", method = RequestMethod.HEAD)
	@ApiOperation(value = "Get header with a total number of product's stock levels", notes = "Returns X-Total-Count header with a total number of product's stock levels. It does not return the HTTP body. The following two sets of parameters are available: 1. location (required) or 2. longitude (required), latitude (required).")
	@ApiBaseSiteIdParam
	public void countSearchProductStockByLocation(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Free-text location") @RequestParam(required = false) final String location,
			@ApiParam(value = "Latitude location parameter.") @RequestParam(required = false) final Double latitude,
			@ApiParam(value = "Longitude location parameter.") @RequestParam(required = false) final Double longitude,
			final HttpServletResponse response)
	{
		final StoreFinderStockSearchPageData result = doSearchProductStockByLocation(productCode, location, latitude, longitude, 0,
				1);

		setTotalCountHeader(response, result.getPagination());
	}

	protected StoreFinderStockSearchPageData doSearchProductStockByLocation(final String productCode, final String location,
			final Double latitude, final Double longitude, final int currentPage, final int pageSize)
	{
		final Set<ProductOption> opts = extractOptions(BASIC_OPTION);
		StoreFinderStockSearchPageData result;
		if (latitude != null && longitude != null)
		{
			result = storeFinderStockFacade.productSearch(createGeoPoint(latitude, longitude),
					productFacade.getProductForCodeAndOptions(productCode, opts), createPageableData(currentPage, pageSize, null));
		}
		else if (location != null)
		{
			result = storeFinderStockFacade.productSearch(location, productFacade.getProductForCodeAndOptions(productCode, opts),
					createPageableData(currentPage, pageSize, null));
		}
		else
		{
			throw new RequestParameterException("You need to provide location or longitute and latitute parameters",
					RequestParameterException.MISSING, "location or longitute and latitute");
		}
		return result;
	}


	@RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get reviews for a product", notes = "Returns the reviews for a product with a given product code.")
	@ApiBaseSiteIdParam
	public ReviewListWsDTO getProductReviews(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Maximum count of reviews") @RequestParam(required = false) final Integer maxCount,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final ReviewDataList reviewDataList = new ReviewDataList();
		reviewDataList.setReviews(productFacade.getReviews(productCode, maxCount));
		return getDataMapper().map(reviewDataList, ReviewListWsDTO.class, fields);
	}


	@RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Creates a new customer review as an anonymous user", notes = "Creates a new customer review as an anonymous user.", hidden = true)
	@ApiBaseSiteIdParam
	public ReviewWsDTO createReview(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletRequest request) throws WebserviceValidationException //NOSONAR
	{
		final ReviewData reviewData = new ReviewData();
		httpRequestReviewDataPopulator.populate(request, reviewData);
		validate(reviewData, REVIEW_DATA, reviewValidator);
		final ReviewData reviewDataRet = productFacade.postReview(productCode, reviewData);
		return getDataMapper().map(reviewDataRet, ReviewWsDTO.class, fields);
	}


	@RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Creates a new customer review as an anonymous user", notes = "Creates a new customer review as an anonymous user.")
	@ApiBaseSiteIdParam
	public ReviewWsDTO createReview(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Object contains review details like : rating, alias, headline, comment", required = true) @RequestBody final ReviewWsDTO review,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException //NOSONAR
	{
		validate(review, REVIEW, reviewDTOValidator);
		final ReviewData reviewData = getDataMapper().map(review, ReviewData.class, REVIEW_DTO_FIELDS);
		final ReviewData reviewDataRet = productFacade.postReview(productCode, reviewData);
		return getDataMapper().map(reviewDataRet, ReviewWsDTO.class, fields);
	}


	@RequestMapping(value = {"/{productCode}/references","/{productCode}/{variantCode}/references"}, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a product reference", notes = "Returns references for a product with a given product code. Reference type specifies which references to return.")
	@ApiBaseSiteIdParam
	public ProductReferenceListWsDTO exportProductReferences(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Product identifier", required = false) @PathVariable Optional<String> variantCode,
			@ApiParam(value = "Maximum size of returned results.") @RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize,
			@ApiParam(value = "Reference type according to enum ProductReferenceTypeEnum", required = true) @RequestParam final String referenceType,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<ProductOption> opts = Lists.newArrayList(OPTIONS);
		final ProductReferenceTypeEnum referenceTypeEnum = ProductReferenceTypeEnum.valueOf(referenceType);

		String productCodeCombined = productCode;
		if(variantCode.isPresent()){
			if(productCode.contains("/")){
				productCodeCombined=productCode	;
			}else {
			productCodeCombined = productCode+"/"+variantCode.get() ;
			}
		}
		
		final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(productCodeCombined,
				Arrays.asList(referenceTypeEnum), opts, Integer.valueOf(pageSize));
		final ProductReferencesData productReferencesData = new ProductReferencesData();
		
		if (cmsSiteService.isSampleSite()) {
			productReferences
					.removeIf(ref -> MaterialStatusEnum.OBSOLETE.equalsIgnoreCase(ref.getTarget().getMaterialStatus())
							|| !ref.getTarget().getIsSample());

		} else {
			productReferences
					.removeIf(ref -> MaterialStatusEnum.OBSOLETE.equalsIgnoreCase(ref.getTarget().getMaterialStatus())
							&& StockLevelStatus.OUTOFSTOCK.equals(ref.getTarget().getStock().getStockLevelStatus()));
		}
		productReferencesData.setReferences(productReferences);

		return getDataMapper().map(productReferencesData, ProductReferenceListWsDTO.class, fields);
	}

	protected PageableData createPageableData(final int currentPage, final int pageSize, final String sort)
	{
		final PageableData pageable = new PageableData();

		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		pageable.setSort(sort);
		return pageable;
	}

	protected GeoPoint createGeoPoint(final Double latitude, final Double longitude)
	{
		final GeoPoint point = new GeoPoint();
		point.setLatitude(latitude.doubleValue());
		point.setLongitude(longitude.doubleValue());

		return point;
	}



	@RequestMapping(value = "/suggestions", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of available suggestions", notes = "Returns a list of all available suggestions related to a given term and limits the results to a specific value of the max parameter.")
	@ApiBaseSiteIdParam
	public SuggestionListWsDTO getSuggestions(
			@ApiParam(value = "Specified term", required = true) @RequestParam(required = true) final String term,
			@ApiParam(value = "Specifies the limit of results.", required = true) @RequestParam(required = true, defaultValue = "10") final int max,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<SuggestionData> suggestions = new ArrayList<>();
		final SuggestionDataList suggestionDataList = new SuggestionDataList();

		List<AutocompleteSuggestionData> autoSuggestions = productSearchFacade.getAutocompleteSuggestions(term);
		if (max < autoSuggestions.size())
		{
			autoSuggestions = autoSuggestions.subList(0, max);
		}

		for (final AutocompleteSuggestionData autoSuggestion : autoSuggestions)
		{
			final SuggestionData suggestionData = new SuggestionData();
			suggestionData.setValue(autoSuggestion.getTerm());
			suggestions.add(suggestionData);
		}
		suggestionDataList.setSuggestions(suggestions);

		return getDataMapper().map(suggestionDataList, SuggestionListWsDTO.class, fields);
	}


	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/expressupdate", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get products added to the express update feed", notes = "Returns products added to the express update feed. Returns only elements updated after the provided timestamp. The queue is cleared using a defined cronjob.", authorizations =
	{ @Authorization(value = "oauth2_client_credentials") })
	@ApiBaseSiteIdParam
	public ProductExpressUpdateElementListWsDTO expressUpdate(
			@ApiParam(value = "Only items newer than the given parameter are retrieved from the queue. This parameter should be in ISO-8601 format.", required = true) @RequestParam final String timestamp,
			@ApiParam(value = "Only products from this catalog are returned. Format: catalogId:catalogVersion") @RequestParam(required = false) final String catalog,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws RequestParameterException //NOSONAR
	{
		final Date timestampDate;
		try
		{
			timestampDate = wsDateFormatter.toDate(timestamp);
		}
		catch (final IllegalArgumentException ex)
		{
			throw new RequestParameterException("Wrong time format. The only accepted format is ISO-8601.",
					RequestParameterException.INVALID, "timestamp", ex);
		}
		final ProductExpressUpdateElementDataList productExpressUpdateElementDataList = new ProductExpressUpdateElementDataList();
		final List<ProductExpressUpdateElementData> products = productExpressUpdateQueue.getItems(timestampDate);
		filterExpressUpdateQueue(products, validateAndSplitCatalog(catalog));
		productExpressUpdateElementDataList.setProductExpressUpdateElements(products);
		return getDataMapper().map(productExpressUpdateElementDataList, ProductExpressUpdateElementListWsDTO.class, fields);
	}

	protected void filterExpressUpdateQueue(final List<ProductExpressUpdateElementData> products, final List<String> catalogInfo)
	{
		if (catalogInfo.size() == 2 && StringUtils.isNotEmpty(catalogInfo.get(CATALOG_ID_POS))
				&& StringUtils.isNotEmpty(catalogInfo.get(CATALOG_VERSION_POS)) && CollectionUtils.isNotEmpty(products))
		{
			final Iterator<ProductExpressUpdateElementData> dataIterator = products.iterator();
			while (dataIterator.hasNext())
			{
				final ProductExpressUpdateElementData productExpressUpdateElementData = dataIterator.next();
				if (!catalogInfo.get(CATALOG_ID_POS).equals(productExpressUpdateElementData.getCatalogId())
						|| !catalogInfo.get(CATALOG_VERSION_POS).equals(productExpressUpdateElementData.getCatalogVersion()))
				{
					dataIterator.remove();
				}
			}
		}
	}

	protected List<String> validateAndSplitCatalog(final String catalog) throws RequestParameterException //NOSONAR
	{
		final List<String> catalogInfo = new ArrayList<>();
		if (StringUtils.isNotEmpty(catalog))
		{
			catalogInfo.addAll(Lists.newArrayList(Splitter.on(':').trimResults().omitEmptyStrings().split(catalog)));
			if (catalogInfo.size() == 2)
			{
				catalogFacade.getProductCatalogVersionForTheCurrentSite(catalogInfo.get(CATALOG_ID_POS),
						catalogInfo.get(CATALOG_VERSION_POS), Collections.emptySet());
			}
			else if (!catalogInfo.isEmpty())
			{
				throw new RequestParameterException("Invalid format. You have to provide catalog as 'catalogId:catalogVersion'",
						RequestParameterException.INVALID, "catalog");
			}
		}
		return catalogInfo;
	}

	@RequestMapping(value = "/search/autocomplete/{componentUid}" , method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of products and additional data", notes = "Returns a list of products and additional data such as: available facets, available sorting and pagination options.")
	@ApiBaseSiteIdParam
	public AutocompleteResultWsDTO getAutocompleteProducts(
			@ApiParam(value = "Component identifier", required = true) @PathVariable final String componentUid,
			@RequestParam("maxProducts") final int maxProducts,
			@ApiParam(value = "Serialized query, free text search, facets. The format of a serialized term: freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String term,
			@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false) final String sort,
			@ApiParam(value = "The context to be used in the search query.") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@ApiParam(value = "The user to be put in session.") @RequestParam(required = false) final String userId,
			final HttpServletResponse response) throws Exception
	{
		productsHelper.setSoldToInUser(soldToId, userId);
		return productsHelper.searchProductsForSuggestions(term, currentPage, pageSize, sort, maxProducts, fields);
	}

	@RequestMapping(value = "/category/{categoryCode}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of products and additional data for given category", notes = "Returns a list of products and additional data such as: available facets, available sorting and pagination options.")
	@ApiBaseSiteIdParam
	public ProductSearchPageWsDTO searchCategories(
			@ApiParam(value = "Category code", required = true) @PathVariable final String categoryCode,
			@ApiParam(value = "Serialized query, free text search, facets. The format of a serialized query: sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String query,
			@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE_PRODUCT_SEARCH) final int pageSize,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false) final String sort,
			@ApiParam(value = "The context to be used in the search query.") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@ApiParam(value = "The user to be put in session.") @RequestParam(required = false) final String userId,
			@RequestParam(required = false) final String searchQueryContext, final HttpServletResponse response)
	{
		productsHelper.setSoldToInUser(soldToId, userId);
		final ProductSearchPageWsDTO result = productsHelper.searchCategories(categoryCode, query, currentPage,  getConfiguredPageSize(pageSize), sort,
				addPaginationField(fields), searchQueryContext);
		// X-Total-Count header
		setTotalCountHeader(response, result.getPagination());
		return result;
	}

	/**
	 * Takes the form details from user and triggers email and sends status back as
	 * response
	 *
	 * @param form
	 */
	@RequestMapping(value = "/shareProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Share Product Email Process", notes = "The API takes in submit form details from user and triggers Email, sends response.")
	@ApiBaseSiteIdParam
	public void shareProduct(@RequestBody final ShareProductForm form) {
		if(LOG.isDebugEnabled()){
			LOG.debug("In Products Controller " + form);
		}
		shareProductFacade.shareProduct(form);
	}


	@RequestMapping(value = "/quickorder/{b2bUnit}/{productCodes}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of product codes and cmir codes for given list of codes", notes = "Returns a list of product codes and cmir codes.")
	@ApiBaseSiteIdParam
	public GPQuickOrderWSDTO findQuickOrderItems(
			@ApiParam(value = "b2bUnit", required = true) @PathVariable final String b2bUnit,
			@ApiParam(value = "productCodes", required = true) @PathVariable final String productCodes) throws RequestParameterException
	{
		final Locale currentLocale = i18NService.getCurrentLocale();

		final List<ProductData> productList = productsHelper.prepareProductList(productCodes);

		if(!productsHelper.validateProductList(productList)){
			throw new RequestParameterException(messageSource.getMessage(INVALID_QUICKORDER_PRODUCTLIST, null, currentLocale), RequestParameterException.INVALID, "product list");
		}

		final GPQuickOrderData searchResult = gpQuickOrderFacade.getProductsForQuickOrder(b2bUnit, productList);
		return getDataMapper().map(searchResult, GPQuickOrderWSDTO.class);
	}

	@RequestMapping(value = "/quickorder/{b2bUnit}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	@ApiOperation(value = "Get a list of product codes and cmir codes for given list of codes", notes = "Returns a list of product codes and cmir codes.")
	@ApiBaseSiteIdParam
	public GPQuickOrderWSDTO uploadQuickOrderData(
			@ApiParam(value = "b2bUnit", required = true)  @PathVariable final String b2bUnit,
			@ApiParam(value = "file", required = true)  @RequestParam("file") final MultipartFile file) throws RequestParameterException, IOException
	{
		final Locale currentLocale = i18NService.getCurrentLocale();

		//Validating null check for csv
		if(null == file){
			throw new GPException("109", messageSource.getMessage(INVALID_QUICKORDER_UPLOAD, null, currentLocale));
		}

		//Validating csv header for file format
		if(!productsHelper.validateCSVHeader(file))
		{
			throw new GPException("109", messageSource.getMessage(INVALID_QUICKORDER_FILE_FORMAT, null, currentLocale));
		}

		//Fetching Products List from csv
		final List<ProductData> productList = gpQuickOrderFacade.getProductListFromCSV(file);

		//Validating Products List for allowed range or empty product codes
		if(!productsHelper.validateProductList(productList))
		{
			throw new GPException("109", messageSource.getMessage(INVALID_QUICKORDER_PRODUCTLIST, null, currentLocale));
		}

		final GPQuickOrderData productsForQuickOrder = gpQuickOrderFacade.getProductsForQuickOrder(b2bUnit , productList);

		return getDataMapper().map(productsForQuickOrder, GPQuickOrderWSDTO.class);

	}

	@RequestMapping(value = "/quickorder/share", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ApiBaseSiteIdParam
	public void shareQuickOrderData(@RequestBody final GPQuickOrderWSDTO gpQuickOrderWSDto) throws WebServiceException
	{
		final GPQuickOrderData gpQuickOrderData = getDataMapper().map(gpQuickOrderWSDto, GPQuickOrderData.class);
		final Locale currentLocale = i18NService.getCurrentLocale();
		try{
			gpQuickOrderFacade.shareQuickOrderData(gpQuickOrderData);
		}catch(final Exception e){
			LOG.error("Error Occured while sharing the quick order data "+e.getMessage(),e);
			throw new WebServiceException(messageSource.getMessage(SHARE_QUICKORDER_EXCEPTION, null, currentLocale));
		}
	}



	@RequestMapping(value = "/quickorder/suggestions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiBaseSiteIdParam
	@ApiOperation(value = "Get suggested products for the cart products with a given identifier.", notes = "Returns the suggested products for the carts products")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public List<ProductWsDTO> suggestProductsByQuickOrder(final CartSuggestionComponentModel component,
			@RequestBody final GPQuickOrderWSDTO gpQuickOrderWSDto,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws WebServiceException
	{
		final GPQuickOrderData gpQuickOrderData = getDataMapper().map(gpQuickOrderWSDto, GPQuickOrderData.class);
		final Locale currentLocale = i18NService.getCurrentLocale();
		try{
			final List<ProductData> productList = gpQuickOrderFacade.getSuggestionsForProductsInQuickOrder(component, gpQuickOrderData);
			if(CollectionUtils.isNotEmpty(productList)) {
				final List<ProductWsDTO> products = new ArrayList<>();
				for(final ProductData product : productList) {
					final ProductWsDTO productWsDTO = getDataMapper().map(product, ProductWsDTO.class, FieldSetLevelHelper.FULL_LEVEL);
					products.add(productWsDTO);
				}
				return products;
			}
		}catch(final Exception e){
			LOG.error("Error Occured while suggesting products from quick order data "+e.getMessage() ,e);
			throw new WebServiceException(messageSource.getMessage(SUGGEST_PRODUCT_EXCEPTION, null, currentLocale));
		}
		return Collections.emptyList();
	}
	
	@RequestMapping(value = {"/{productCode}/relatedProducts","/{productCode}/{variantCode}/relatedProducts"}, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a product reference", notes = "Returns references for a product with a given product code. Reference type specifies which references to return.")
	@ApiBaseSiteIdParam
	public List<GPProductReferenceWsDTO> exportAllProductReferences(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCode,
			@ApiParam(value = "Product identifier", required = false) @PathVariable Optional<String> variantCode,
			@ApiParam(value = "Maximum size of returned results.") @RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<ProductOption> opts = Lists.newArrayList(OPTIONS);
		List<GPProductReferenceWsDTO> gpProductReferenceListWsDTO = new ArrayList<>();
		String productCodeCombined = productCode;
		if(variantCode.isPresent()){
			productCodeCombined = productCode+"/"+variantCode.get() ;
		}
		
		String referenceEnums = cmsSiteService.getSiteConfig("gp.site.references");
		List<String> list = Arrays.asList(referenceEnums.split(","));
		List<ProductReferenceTypeEnum> enumList = new ArrayList<>();
		for (String string : list) {
			 ProductReferenceTypeEnum referenceTypeEnum = ProductReferenceTypeEnum.valueOf(string);
			enumList.add(referenceTypeEnum);
		}
		
		List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(productCodeCombined,
				enumList, opts, Integer.valueOf(pageSize));
		
		productReferences.removeIf(ref-> MaterialStatusEnum.OBSOLETE.equalsIgnoreCase(ref.getTarget().getMaterialStatus()) && StockLevelStatus.OUTOFSTOCK.equals(ref.getTarget().getStock().getStockLevelStatus()));
		List<GPProductReferenceData> references = createGroupedListForReferences(productReferences,list);
		for (GPProductReferenceData gpProductReferenceData : references) {
			GPProductReferenceWsDTO gpProductReferenceDTO = getDataMapper().map(gpProductReferenceData, GPProductReferenceWsDTO.class, fields);
			gpProductReferenceListWsDTO.add(gpProductReferenceDTO);
		}
	
		return gpProductReferenceListWsDTO;
	}
	
	@RequestMapping(value = "/shareProductResource", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Share Product Resource Email Process", notes = "The API takes in submit form details from user and triggers Email, sends response.")
	@ApiBaseSiteIdParam
	public void shareProductResource(@RequestBody final ShareProductResourceForm form) {
		if(LOG.isDebugEnabled()){
			LOG.debug("In Products Controller " + form);
		}
		shareProductFacade.shareProduct(form);
	}
	
	private List<GPProductReferenceData> createGroupedListForReferences(List<ProductReferenceData> productReferences, List<String> referenceList) {
		Map<String,List<ProductReferenceData>> groupedReferences = new HashMap<>();
		List<GPProductReferenceData> gpProductReferenceDataList = new ArrayList<>();
		
		for (ProductReferenceData productReferenceData : productReferences) {
			if(!groupedReferences.containsKey(productReferenceData.getReferenceType().getCode())){
				List<ProductReferenceData> list = new ArrayList<>();
				list.add(productReferenceData);
				groupedReferences.put(productReferenceData.getReferenceType().getCode(), list);
			}
			else{
				groupedReferences.get(productReferenceData.getReferenceType().getCode()).add(productReferenceData);
			}
		}
		
		for (String reference : referenceList) {
			List<ProductReferenceData> references= groupedReferences.get(reference);
			if(CollectionUtils.isNotEmpty(references)){
				GPProductReferenceData gpProductReferenceData = new GPProductReferenceData();
				gpProductReferenceData.setReferenceType(reference);
				gpProductReferenceData.setReferences(references);
				gpProductReferenceDataList.add(gpProductReferenceData);
			}
		}
		
		return gpProductReferenceDataList;
	}
	
	@RequestMapping(value = "/productCodes/{productCodes}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get product details", notes = "Returns details of a multiple product according to a product codes.")
	@ApiBaseSiteIdParam
	public GPProductDataListWsDTO getProductByCodes(
			@ApiParam(value = "Product identifier", required = true) @PathVariable final String productCodes,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductByCode: code=" + sanitize(productCodes) + " | options=" + PRODUCT_OPTIONS);
		}
		ProductData product = null;
		List<ProductWsDTO> productsDTOList = new ArrayList<ProductWsDTO>();
		GPProductDataListWsDTO productsDataDTOList = new GPProductDataListWsDTO();
		final String[] productCodeStrings = productCodes.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);
		for (String productCode : productCodeStrings) {
			product = productFacade.getProductForCodeAndOptions(productCode, OPTIONS);
			productFacade.populateAdditionalFields(product);
			productsDTOList.add(getDataMapper().map(product, ProductWsDTO.class, fields));
		}
		productsDataDTOList.setProducts(productsDTOList);

		return getDataMapper().map(productsDataDTOList, GPProductDataListWsDTO.class, fields);
	}

	/**
	 * @param productcodes
	 * @param response
	 * @param redirectModel
	 * @throws IOException
	 */
	@RequestMapping(value = {"/{productCode}/exportimage","/{productCode}/{variantCode}/exportimage"}, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Downloads pdp images.", notes = "Downloads pdp product images.")
	@ApiBaseSiteIdParam
	public void exportImages(
			@ApiParam(value = "productcode", required = true) @RequestParam final String productCode,
			@ApiParam(value = "Product identifier", required = false) @PathVariable Optional<String> variantCode,
			@ApiParam(value = "imageformat", required = true) @RequestParam final String imageformat,
			@ApiParam(value = "resolution", required = true) @RequestParam final String resolution,
			@ApiParam(value = "allimages", required = true) @RequestParam final boolean allimages,
			final HttpServletResponse response) throws IOException
	
	{
		try {
			String productCodeCombined = productCode;
			if(variantCode.isPresent()){
				if(productCode.contains("/")){
					productCodeCombined=productCode	;
				}else {
				productCodeCombined = productCode+"/"+variantCode.get() ;
				}
			}
			
			ProductData product = productFacade.getProductForCodeAndOptions(productCodeCombined, OPTIONS);
			
			File file = productFacade.exportImages(product,imageformat,resolution, productCodeCombined, allimages);
			
			ServletOutputStream outstream = response.getOutputStream();
			response.reset();
			
			try {
				response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
				response.setContentLength((int) file.length());
				response.setContentType("application/octet-stream");
				
				FileUtils.copyFile(file, outstream);
			} 
			finally {
				outstream.flush();
				outstream.close();
				file.delete();
			}
			
	    } catch(Exception e) {
	    	throw new GPException("3333", "No images to download"+e.getMessage(),e);
	    }
	}
	
	@RequestMapping(value = "/crossReferenceSearch", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of products and additional data", notes = "Returns a list of products and additional data such as: available facets, available sorting and pagination options."
			+ "It can include spelling suggestions. To make spelling suggestions work you need to: 1. Make sure enableSpellCheck on the SearchQuery is set to true. By default it should be already set to true. 2. Have indexed properties configured to be used for spellchecking.")
	@ApiBaseSiteIdParam
	public ProductSearchPageWsDTO searchCompetitorProducts(
			@ApiParam(value = "Serialized query, free text search, facets. The format of a serialized query: freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String query,
			@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue =DEFAULT_PAGE_SIZE_PRODUCT_SEARCH) final int pageSize,
			@ApiParam(value = "") @RequestParam(required = false) final String sort,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "The context to be used in the search query.") @RequestParam(required = false) final String searchQueryContext,
			@ApiParam(value = "The actual string user searched.") @RequestParam(required = false) final String searchText,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@ApiParam(value = "The user to be put in session.") @RequestParam(required = false) final String userId,
			final HttpServletResponse response)
	{
		productsHelper.setSoldToInUser(soldToId, userId);
		final ProductSearchPageWsDTO result = productsHelper.searchCompetitorProducts(query, currentPage, getConfiguredPageSize(pageSize), sort,
				addPaginationField(fields), searchQueryContext, searchText);

		setTotalCountHeader(response, result.getPagination());
		return result;
	}
	
	@RequestMapping(value = {"/competitorCategory/{categoryCode}","/competitorCategory/{categoryCode}/{subcategoryCode}"}, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of products and additional data for given category", notes = "Returns a list of products and additional data such as: available facets, available sorting and pagination options.")
	@ApiBaseSiteIdParam
	public ProductSearchPageWsDTO searchCrossReferenceCategories(
			@ApiParam(value = "Category code", required = true) @PathVariable final String categoryCode,
			@ApiParam(value = "SubCategory code", required = false) @PathVariable Optional<String> subcategoryCode,
			@ApiParam(value = "Serialized query, free text search, facets. The format of a serialized query: sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String query,
			@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE_PRODUCT_SEARCH) final int pageSize,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false) final String sort,
			@ApiParam(value = "The context to be used in the search query.") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@ApiParam(value = "The user to be put in session.") @RequestParam(required = false) final String userId,
			@RequestParam(required = false) final String searchQueryContext, final HttpServletResponse response)
	{
		productsHelper.setSoldToInUser(soldToId, userId);
		String categoryCodeToSearch = subcategoryCode.isPresent() ? subcategoryCode.get() : categoryCode;
		final ProductSearchPageWsDTO result = productsHelper.searchCompetitorCategories(categoryCodeToSearch, query, currentPage,  getConfiguredPageSize(pageSize), sort,
				addPaginationField(fields), searchQueryContext);
		// X-Total-Count header
		setTotalCountHeader(response, result.getPagination());
		return result;
	}
}
