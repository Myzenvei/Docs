/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.v2.helper;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.DeliveryModesData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commercewebservicescommons.dto.order.CartListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gp.commerce.cart.impl.CommerceWebServicesCartFacade;
import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.dto.group.dto.DeliveryAddressGroupWsDTO;
import com.gp.commerce.exceptions.NoCheckoutCartException;
import com.gp.commerce.facade.order.data.GPRestrictionProductListWsDTO;
import com.gp.commerce.facade.order.data.GPRestrictionProductWsDTO;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryWsDTO;
import com.gp.commerce.facades.cart.GPCartFacade;
import com.gp.commerce.facades.product.GPProductFacade;
import com.gp.commerce.facades.suggestion.SimpleSuggestionFacade;
import com.gp.commerce.order.data.CartDataList;
import com.gp.commerce.product.data.PromotionResultDataList;
import com.gp.commerce.request.support.impl.PaymentProviderRequestSupportedStrategy;


/**
 * Generic Helper class for All Controllers
 */
@Component
public class GPCommonHelper extends AbstractHelper
{
	private static final String SPLIT_ENTRY = "splitEntry";
	private static final String UID = "uid";

	@Resource(name = "splitEntryWsDTOValidator")
	private Validator splitEntryWsDTOValidator;
	@Resource(name = "gpCarFacade")
	private GPCartFacade gpCarFacade;
	@Resource(name = "commerceWebServicesCartFacade2")
	private CartFacade cartFacade;
	@Resource(name = "simpleSuggestionFacade")
	private SimpleSuggestionFacade simpleSuggestionFacade;
	@Resource(name = "productFacade")
	private GPProductFacade productFacade;
	@Resource(name = "paymentProviderRequestSupportedStrategy")
	private PaymentProviderRequestSupportedStrategy paymentProviderRequestSupportedStrategy;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;
	@Resource(name = "paymentDetailsDTOValidator")
	private Validator paymentDetailsDTOValidator;
	@Resource(name = "addressDTOValidator")
	private Validator addressDTOValidator;
	@Resource(name = "saveCartFacade")
	private SaveCartFacade saveCartFacade;

	/**
	 *
	 * @param cart
	 * @param fields
	 * @return PromotionResultListWsDTO
	 */
	public PromotionResultListWsDTO getPromotions(final CartData cart, final String fields)
	{
		final List<PromotionResultData> appliedPromotions = new ArrayList<>();
		final List<PromotionResultData> orderPromotions = cart.getAppliedOrderPromotions();
		final List<PromotionResultData> productPromotions = cart.getAppliedProductPromotions();
		appliedPromotions.addAll(orderPromotions);
		appliedPromotions.addAll(productPromotions);
		final PromotionResultDataList dataList = new PromotionResultDataList();
		dataList.setPromotions(appliedPromotions);
		return getDataMapper().map(dataList, PromotionResultListWsDTO.class, fields);
	}

	/**
	 *
	 * @param shippingProductAdressList
	 * @param fields
	 * @return GPRestrictionProductListWsDTO
	 * @throws CommerceCartModificationException
	 */
	public GPRestrictionProductListWsDTO getShppingRestrictions(final GPRestrictionProductListWsDTO shippingProductAdressList,final String cartId ,
			final String fields) throws CommerceCartModificationException
	{
		final GPRestrictionProductListWsDTO restrictedListWsDTO = new GPRestrictionProductListWsDTO();
		final List<GPRestrictionProductWsDTO> shippingRestWsDTOList = new ArrayList<>();
		final List<ShippingRestrictionsData> shippingRestDataList = gpCarFacade
				.fetchShippingRestrictions(shippingProductAdressList);
		for (final ShippingRestrictionsData shippingRestData : shippingRestDataList)
		{
			final GPRestrictionProductWsDTO shippingRestWsDTO = getDataMapper().map(shippingRestData,
					GPRestrictionProductWsDTO.class, fields);
			shippingRestWsDTOList.add(shippingRestWsDTO);
		}
		restrictedListWsDTO.setShippingDetails(shippingRestWsDTOList);
		if (restrictedListWsDTO.getShippingDetails().isEmpty() && null != shippingProductAdressList.getShippingAddress())
		{
			 //update address for multiple shipping address
				validate(shippingProductAdressList.getShippingAddress(), SPLIT_ENTRY, splitEntryWsDTOValidator);
				((CommerceWebServicesCartFacade) cartFacade).updateSpliEntries(shippingProductAdressList.getShippingAddress(),cartId);
		}
		return restrictedListWsDTO;
	}

	/**
	 *
	 * @param component
	 * @return List<ProductWsDTO>
	 */
	public List<ProductWsDTO> getProductList(final CartSuggestionComponentModel component)
	{
		final List<ProductData> productData = simpleSuggestionFacade.getSuggestionsForProductsInCart(
				component.getProductReferenceTypes(), component.isFilterPurchased(), component.getMaximumNumberProducts());
		productData.removeIf(product -> com.gp.commerce.core.enums.MaterialStatusEnum.OBSOLETE.toString().equalsIgnoreCase(
				product.getMaterialStatus()) && StockLevelStatus.OUTOFSTOCK.equals(product.getStock().getStockLevelStatus()));
		if (CollectionUtils.isNotEmpty(productData))
		{
			final List<ProductData> productList = new ArrayList<>();
			final List<ProductWsDTO> products = new ArrayList<>();
			for (final ProductData prod : productData)
			{
				final ProductData product = productFacade.getProductForCodeAndOptions(prod.getCode(), getProductOptions());
				productList.add(product);
			}
			productFacade.setFavoriteFlagForProducts(productList);
			for (final ProductData prod : productList)
			{
				final ProductWsDTO productWsDTO = getDataMapper().map(prod, ProductWsDTO.class, FieldSetLevelHelper.FULL_LEVEL);
				products.add(productWsDTO);
			}
			return products;
		}
		return Collections.emptyList();
	}

	/**
	 *
	 * @param cartData
	 * @param fields
	 * @return DeliveryAddressGroupWsDTO
	 * @throws Exception
	 */
	public DeliveryAddressGroupWsDTO getSupportedDeliveryModesforMultishipping(final CartData cartData, final String fields)
			throws Exception
	{
		final CartWsDTO cart = getDataMapper().map(cartData, CartWsDTO.class, fields);
		final DeliveryAddressGroupWsDTO deliveryAddressGroup = getDeliveryGroup(cart);
		final Map<String, SplitEntryListWsDTO> spliEntryList = deliveryAddressGroup.getDeliveryGroup();
		final Map<String, List<DeliveryModeData>> deliveryModeList = gpCarFacade.getSupportedDeliveryModes(deliveryAddressGroup);
		final Map<String, DeliveryModeListWsDTO> deliveryModeMap = new HashMap<>();
		for (final Entry<String, List<DeliveryModeData>> deliverymode : deliveryModeList.entrySet())
		{
			final DeliveryModesData deliveryModesData = new DeliveryModesData();
			deliveryModesData.setDeliveryModes(deliverymode.getValue());
			final DeliveryModeListWsDTO deliveryModeListWsDTO = getDataMapper().map(deliveryModesData, DeliveryModeListWsDTO.class,
					fields);
			if (spliEntryList.containsKey(deliverymode.getKey()))
			{
				final SplitEntryListWsDTO splientryList = spliEntryList.get(deliverymode.getKey());
				splientryList.setDeliveryModes(deliveryModeListWsDTO);
				deliveryAddressGroup.setDeliveryGroup(spliEntryList);
			}
			deliveryModeMap.put(deliverymode.getKey(), deliveryModeListWsDTO);
		}
		gpCarFacade.updateSpliEntriesWithDeliveryModes(deliveryModeList);
		return deliveryAddressGroup;
	}

	private Set<ProductOption> getProductOptions()
	{
		String productOptions = "";

		for (final ProductOption option : ProductOption.values())
		{
			productOptions = productOptions + option.toString() + " ";
		}
		productOptions = productOptions.trim().replace(" ", YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		return extractOptions(productOptions);
	}

	private Set<ProductOption> extractOptions(final String options)
	{
		final String[] optionsStrings = options.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);
		final Set<ProductOption> opts = new HashSet<>();
		for (final String option : optionsStrings)
		{
			opts.add(ProductOption.valueOf(option));
		}
		return opts;
	}

	protected void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

	/**
	 * @param cart
	 * @return DeliveryAddressGroupWsDTO
	 */
	public DeliveryAddressGroupWsDTO getDeliveryGroup(final CartWsDTO cart)
	{
		final DeliveryAddressGroupWsDTO addressGroup = new DeliveryAddressGroupWsDTO();
		if (CollectionUtils.isNotEmpty(cart.getEntries()))
		{
			final Map<String, SplitEntryListWsDTO> addressGroupMap = new HashMap<>();
			for (final OrderEntryWsDTO entry : cart.getEntries())
			{
				if (CollectionUtils.isNotEmpty(entry.getSplitEntries()))
				{
					for (final SplitEntryWsDTO splitEntry : entry.getSplitEntries())
					{
						final String key = splitEntry.getDeliveryAddress().getId();
						if (null != addressGroupMap.get(key))
						{
							final SplitEntryListWsDTO entryList = addressGroupMap.get(key);
							splitEntry.setEntryNumber(entry.getEntryNumber().toString());
							entryList.getSplitEntries().add(splitEntry);
						}
						else
						{
							final SplitEntryListWsDTO splitEntryList = new SplitEntryListWsDTO();
							splitEntryList.setDeliveryMode(splitEntry.getDeliveryMode());
							splitEntryList.setDeliveryInstruction(splitEntry.getDeliveryInstruction());
							final List<SplitEntryWsDTO> splitEntryDto = new ArrayList<>();
							splitEntry.setEntryNumber(entry.getEntryNumber().toString());
							splitEntryDto.add(splitEntry);
							splitEntryList.setSplitEntries(splitEntryDto);
							addressGroupMap.put(key, splitEntryList);
						}
					}
				}
			}
			addressGroup.setDeliveryGroup(addressGroupMap);
		}
		return addressGroup;
	}

	/**
	 * @param paymentDetails
	 * @throws NoCheckoutCartException
	 */
	public void validatePayment(final PaymentDetailsWsDTO paymentDetails) throws NoCheckoutCartException
	{
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot add PaymentInfo. There was no checkout cart created yet!");
		}
		validate(paymentDetails, "paymentDetails", paymentDetailsDTOValidator);
	}

	/**
	 * @param address
	 * @param fields
	 * @return AddressData
	 */
	public AddressData createAndSetAddress(final AddressWsDTO address, final String fields)
	{
		validate(address, "address", addressDTOValidator);
		final RegionData regionData = getDataMapper().map(address.getRegion(), RegionData.class, "isocode,isocodeShort");
		final B2BUnitData unit = getDataMapper().map(address.getUnit(), B2BUnitData.class, UID);
		final AddressData addressData = getDataMapper().map(address, AddressData.class,
				"titleCode,firstName,lastName,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress,phone,companyName");
		addressData.setRegion(regionData);
		addressData.setUnit(unit);
		return addressData;
	}

	public CartListWsDTO getCarts(final boolean savedCartsOnly, final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final CartDataList cartDataList = new CartDataList();
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(currentPage);
		pageableData.setPageSize(pageSize);
		pageableData.setSort(sort);
		final List<CartData> allCarts = new ArrayList<>(
				saveCartFacade.getSavedCartsForCurrentUser(pageableData, null).getResults());
		if (!savedCartsOnly)
		{
			allCarts.addAll(cartFacade.getCartsForCurrentUser());
		}
		cartDataList.setCarts(allCarts);
		return getDataMapper().map(cartDataList, CartListWsDTO.class, fields);
	}
}
