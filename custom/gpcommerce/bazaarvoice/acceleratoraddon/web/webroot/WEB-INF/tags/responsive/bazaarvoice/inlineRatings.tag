<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="product" required="true"
              type="de.hybris.platform.commercefacades.product.data.ProductData"%>

<s:eval expression="T(com.bazaarvoice.hybris.utils.BazaarVoiceUtils).ReplaceUnsupportedCharacters(product.code)" var="productCode" />

<div data-bv-show="inline_rating" data-bv-productId="${productCode}"></div>