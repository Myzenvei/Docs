package com.gp.commerce.gpcommerceaddon.controllers.cms;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import com.gp.commerce.core.model.GPBrandBarComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.model.GPBundleCarouselComponentModel;

@Controller("GPBundleCarouselComponentController")
@RequestMapping("/view/GPBundleCarouselComponentController")
public class GPBundleCarouselComponentController extends AbstractCMSAddOnComponentController<GPBundleCarouselComponentModel> {


	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.STARTING_BUNDLES, ProductOption.STOCK);

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPBundleCarouselComponentModel component)
	{
		final ProductModel currentProduct = getRequestContextData(request).getProduct();
		if (currentProduct != null)
		{
			final ProductData productData = getProductFacade().getProductForCodeAndOptions(currentProduct.getCode(), PRODUCT_OPTIONS);
			model.addAttribute("bundleCarousel", GPFunctions.convertToJSON(productData));
			if(StringUtils.isNotEmpty(component.getTitle())) {
				model.addAttribute("bundleCarouselTitle", component.getTitle());
			}
		}
	}
	
	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPBundleCarouselComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}

	private ProductFacade getProductFacade()
	{
		return productFacade;
	}

}
