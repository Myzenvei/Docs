<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="product" required="true"
              type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ tag trimDirectiveWhitespaces="true" %>

<s:eval expression="T(com.bazaarvoice.hybris.utils.BazaarVoiceUtils).ReplaceUnsupportedCharacters(product.code)" var="productCode" />


<div data-bv-show="rating_summary" data-bv-productId="${productCode}">${sBvOutputSummary}</div>

