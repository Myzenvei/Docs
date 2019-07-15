/**
 *
 */
package com.deloitte.populators;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.deloitte.enums.SubscriptionFrequencyEnum;
import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public class SubscriptionCommerceCartParameterBasicPopulator implements Populator<AddToCartParams, CommerceCartParameter>
{
	private ProductService productService;
	private CartService cartService;
	private PointOfServiceService pointOfServiceService;
	private CommonI18NService commonI18NService;
	private NetGrossStrategy netGrossStrategy;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private KeyGenerator guidKeyGenerator;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

	@Resource(name = "userService")
	private UserService userService;


	@Override
	public void populate(final AddToCartParams addToCartParams, final CommerceCartParameter parameter) throws ConversionException
	{
		parameter.setEnableHooks(true);
		if (StringUtils.isBlank(addToCartParams.getCartId()))
		{
			parameter.setCart(getSubscriptionCartModel(addToCartParams));
		}
		else
		{
			parameter.setCart(commerceCartService.getCartForCodeAndUser(addToCartParams.getCartId(), userService.getCurrentUser()));
		}
		if (StringUtils.isNotEmpty(addToCartParams.getStoreId()))
		{
			final PointOfServiceModel pointOfServiceModel = getPointOfServiceService()
					.getPointOfServiceForName(addToCartParams.getStoreId());
			parameter.setPointOfService(pointOfServiceModel);
		}
		// Product code may be optional, e.g. when it's addressed by order entry.
		if (addToCartParams.getProductCode() != null)
		{
			final ProductModel product = getProductService().getProductForCode(addToCartParams.getProductCode());
			parameter.setProduct(product);
			parameter.setUnit(product.getUnit());
		}
		parameter.setQuantity(addToCartParams.getQuantity());
		parameter.setCreateNewEntry(false);
		parameter.setEntryGroupNumbers(addToCartParams.getEntryGroupNumbers());
		parameter.setFrequency(SubscriptionFrequencyEnum.valueOf(addToCartParams.getFrequency()));
	}

	/**
	 * This method is used to create new SubscriptionCartModel instance for current user.
	 *
	 * @param addToCartParams
	 * @return
	 */
	protected SubscriptionCartModel getSubscriptionCartModel(final AddToCartParams addToCartParams)
	{
		final SubscriptionCartModel subscriptionCartModel = modelService.create(SubscriptionCartModel.class);
		subscriptionCartModel.setFrequency(SubscriptionFrequencyEnum.valueOf(addToCartParams.getFrequency()));
		subscriptionCartModel.setNet(Boolean.valueOf(getNetGrossStrategy().isNet()));
		subscriptionCartModel.setSite(getBaseSiteService().getCurrentBaseSite());
		subscriptionCartModel.setStore(getBaseStoreService().getCurrentBaseStore());
		subscriptionCartModel.setGuid(getGuidKeyGenerator().generate().toString());
		return createCartInternal(subscriptionCartModel);
	}

	protected SubscriptionCartModel createCartInternal(final SubscriptionCartModel subscriptionCartModel)
	{
		final UserModel user = userService.getCurrentUser();
		final CurrencyModel currency = commonI18NService.getCurrentCurrency();
		subscriptionCartModel.setCode(String.valueOf(getGuidKeyGenerator().generate()));
		subscriptionCartModel.setUser(user);
		subscriptionCartModel.setCurrency(currency);
		subscriptionCartModel.setDate(new Date());
		subscriptionCartModel.setNet(true);
		subscriptionCartModel.setIsActive(Boolean.TRUE);
		modelService.save(subscriptionCartModel);
		return subscriptionCartModel;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	@Required
	public void setPointOfServiceService(final PointOfServiceService pointOfServiceService)
	{
		this.pointOfServiceService = pointOfServiceService;
	}


	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected NetGrossStrategy getNetGrossStrategy()
	{
		return netGrossStrategy;
	}

	@Required
	public void setNetGrossStrategy(final NetGrossStrategy netGrossStrategy)
	{
		this.netGrossStrategy = netGrossStrategy;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService siteService)
	{
		this.baseSiteService = siteService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	protected KeyGenerator getGuidKeyGenerator()
	{
		return guidKeyGenerator;
	}

	@Required
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator)
	{
		this.guidKeyGenerator = guidKeyGenerator;
	}
}
