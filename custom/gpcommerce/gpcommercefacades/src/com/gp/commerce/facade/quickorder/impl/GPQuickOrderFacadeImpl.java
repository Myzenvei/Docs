/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.quickorder.impl;

import static java.lang.String.format;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.services.event.GPQuickOrderEvent;
import com.gp.commerce.core.services.quickorder.GPQuickOrderService;
import com.gp.commerce.core.suggestion.SimpleSuggestionService;
import com.gp.commerce.core.wishlist.services.GPWishlistService;
import com.gp.commerce.facade.quickorder.GPQuickOrderFacade;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.GPQuickOrderData;
import com.gp.commerce.facades.populators.GPQuickOrderEventPopulator;
import com.gp.commerce.facades.suggestion.SimpleSuggestionFacade;

public class GPQuickOrderFacadeImpl implements GPQuickOrderFacade{

	private static final Logger LOG = Logger.getLogger(GPQuickOrderFacadeImpl.class);
	private GPQuickOrderService gpQuickOrderService;
	private GPProductService gpProductService;
	private EventService eventService;
	private UserService userService;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private CommonI18NService commonI18NService;
	private KeyGenerator guidKeyGenerator;
	private GPWishlistService gpWishlistService;
	private SimpleSuggestionService simpleSuggestionService;
	private Converter<ProductModel, ProductData> productConverter;
	private SimpleSuggestionFacade simpleSuggestionFacade;


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "gpQuickOrderEventPopulator")
	private GPQuickOrderEventPopulator gpQuickOrderEventPopulator;

	@Override
	public GPQuickOrderData getProductsForQuickOrder(final String b2bUnit, final List<ProductData> productList) {

		final GPQuickOrderData quickOrderData = new GPQuickOrderData();
		final String productCodes = productList.stream().map(item -> item.getCode()).collect(Collectors.joining(","));
		final List<GPCustomerMaterialInfoModel> materialInfos = gpQuickOrderService.getMaterialInfoForB2BUnit(b2bUnit, productCodes);
		setProductData(materialInfos, productList, quickOrderData);
		return quickOrderData;
	}


	private void setProductData(final List<GPCustomerMaterialInfoModel> materialInfos, final List<ProductData> productList, final GPQuickOrderData quickOrderData) {

		final List<ProductData> productDataList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(productList)){
			ProductData productData = null;
			for(final ProductData product : productList){
				productData = new ProductData();
				String cmirCode = StringUtils.EMPTY;
				String productCode = product.getCode();
				try{
					if(CollectionUtils.isNotEmpty(materialInfos)){
						final List<GPCustomerMaterialInfoModel> customerMaterialInfo = materialInfos.stream().
								filter(materialInfo -> (materialInfo.getCmirCode().equalsIgnoreCase(product.getCode())) ||
										(materialInfo.getProductCode().equalsIgnoreCase(product.getCode()))).collect(Collectors.toList());
						if(CollectionUtils.isNotEmpty(customerMaterialInfo)){
							cmirCode = customerMaterialInfo.get(0).getCmirCode();
							productCode =customerMaterialInfo.get(0).getProductCode();
						}
					}

					final ProductModel productModel = getGpProductService().getProductForCodeOrUpc(productCode);
					if (productModel != null)
					{
						populateQuickOrderItem(productModel.getCode(), cmirCode, product.getCount(), true, productData);
					}
					else
					{
						LOG.error(format("Product with code '%1$s' is not available for the site '%2$s' !", productCode,
								baseSiteService.getCurrentBaseSite().getUid()));
						populateQuickOrderItem(productCode, cmirCode, 0, false, productData);
					}
				}catch(final UnknownIdentifierException e){
					LOG.error(format("Product with code '%s' not found in Quick Order search!", productCode));
					populateQuickOrderItem(productCode, cmirCode, 0, false, productData);
				}
				productDataList.add(productData);
			}
		}
		quickOrderData.setItems(productDataList);
	}


	private void populateQuickOrderItem(final String productCode, final String cmirCode, final Integer count, final boolean validFlag, final ProductData target){
		target.setCode(productCode);
		target.setCmirCode(cmirCode);
		target.setCount(count);
		target.setIsValidSKU(validFlag);
	}

	@Override
	public List<ProductData> getProductListFromCSV(final MultipartFile file) throws IOException {

		InputStream is = null;
		BufferedReader br = null;
		final List<ProductData> productList = new ArrayList<>();
		try {
			ProductData entryData = null;
			String line;
		    is = file.getInputStream();
		    br = new BufferedReader(new InputStreamReader(is));

		    //Reading the first line to skip the header
		    final String header=br.readLine();
		   LOG.debug("Comments : "+ header);

		    while ((line = br.readLine()) != null) {
				final String[] csvData = line.split(GpcommerceFacadesConstants.COMMA);
				if(csvData.length >= 1){
					final int productIndex = configurationService.getConfiguration().getInt(GpcommerceFacadesConstants.PRODUCT_CODE_INDEX);
					final String produtCode = csvData[productIndex];

					//when quantity is blank in CSV;puts 1 for default
					Integer quantity = 1;
					final int quantityIndex = configurationService.getConfiguration().getInt(GpcommerceFacadesConstants.PRODUCT_QUANTITY_INDEX);
					if(csvData.length>1 && StringUtils.isNotBlank(csvData[quantityIndex])){
					try {
					quantity = Integer.parseInt(csvData[quantityIndex]);
					}catch(final NumberFormatException e) {
						LOG.error(e);
					}
					}
					if(validateCSVData(produtCode,quantity)){
						entryData = new ProductData();
						entryData.setCode(produtCode);
						entryData.setCount(quantity);
						productList.add(entryData);
					}
				}
		    }

		} catch (final IOException e) {
			LOG.error(" Exception occured in fetching data from Quick Order csv " + e.getMessage(),e);
		}
		finally {
			is.close();
			br.close();
		}
		return productList;
	}


    private boolean validateCSVData(final String productCode, final Integer quantity) {
		return StringUtils.isNotBlank(productCode) && (quantity>0);
	}

	@Override
	public void shareQuickOrderData(final GPQuickOrderData gpQuickOrderData){
		getEventService().publishEvent(initializeEvent(new GPQuickOrderEvent(), gpQuickOrderData));
	}

	protected AbstractCommerceUserEvent initializeEvent(final GPQuickOrderEvent event,final GPQuickOrderData gpQuickOrderData) {

		getGpQuickOrderEventPopulator().populate(gpQuickOrderData, event);
		return event;
	}


	@Override
	public List<ProductData> getSuggestionsForProductsInQuickOrder(final CartSuggestionComponentModel component, final GPQuickOrderData gpQuickOrderData) {

		if (null != gpQuickOrderData && CollectionUtils.isNotEmpty(gpQuickOrderData.getItems()) && null != component)
		{
			final List<ProductData> productList = gpQuickOrderData.getItems();
			final Set<ProductModel> products = new HashSet<>();
			if(null != productList)
			{
				for (final ProductData entry : productList)
				{
					products.addAll(simpleSuggestionFacade.getAllBaseProducts(getGpProductService().getProductForCode(entry.getCode())));
				}
				return Converters.convertAll(
					getSimpleSuggestionService().getReferencesForProducts(new LinkedList<ProductModel>(products), component.getProductReferenceTypes(),
							getUserService().getCurrentUser(), component.isFilterPurchased(), component.getMaximumNumberProducts()), getProductConverter());

			}
		}
		return Collections.emptyList();
	}


	protected GPQuickOrderService getGpQuickOrderService() {
		return gpQuickOrderService;
	}

	public void setGpQuickOrderService(final GPQuickOrderService gpQuickOrderService) {
		this.gpQuickOrderService = gpQuickOrderService;
	}

	protected GPProductService getGpProductService() {
		return gpProductService;
	}

	public void setGpProductService(final GPProductService gpProductService) {
		this.gpProductService = gpProductService;
	}

	protected EventService getEventService() {
		return eventService;
	}

	public void setEventService(final EventService eventService) {
		this.eventService = eventService;
	}

	protected UserService getUserService() {
		return userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	protected BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	protected BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	protected CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	protected KeyGenerator getGuidKeyGenerator() {
		return guidKeyGenerator;
	}

	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator) {
		this.guidKeyGenerator = guidKeyGenerator;
	}

	protected GPWishlistService getGpWishlistService() {
		return gpWishlistService;
	}

	public void setGpWishlistService(final GPWishlistService gpWishlistService) {
		this.gpWishlistService = gpWishlistService;
	}

	protected SimpleSuggestionService getSimpleSuggestionService() {
		return simpleSuggestionService;
	}

	public void setSimpleSuggestionService(final SimpleSuggestionService simpleSuggestionService) {
		this.simpleSuggestionService = simpleSuggestionService;
	}

	protected Converter<ProductModel, ProductData> getProductConverter() {
		return productConverter;
	}

	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter) {
		this.productConverter = productConverter;
	}

	protected SimpleSuggestionFacade getSimpleSuggestionFacade() {
		return simpleSuggestionFacade;
	}

	public void setSimpleSuggestionFacade(final SimpleSuggestionFacade simpleSuggestionFacade) {
		this.simpleSuggestionFacade = simpleSuggestionFacade;
	}
	
	public GPQuickOrderEventPopulator getGpQuickOrderEventPopulator() {
		return gpQuickOrderEventPopulator;
	}

	public void setGpQuickOrderEventPopulator(GPQuickOrderEventPopulator gpQuickOrderEventPopulator) {
		this.gpQuickOrderEventPopulator = gpQuickOrderEventPopulator;
	}

}
