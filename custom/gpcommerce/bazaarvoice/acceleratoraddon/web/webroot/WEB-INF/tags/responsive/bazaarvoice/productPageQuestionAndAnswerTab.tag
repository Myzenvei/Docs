<%@ attribute name="product" required="true"
			  type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>

<s:eval expression="T(com.bazaarvoice.hybris.utils.BazaarVoiceUtils).ReplaceUnsupportedCharacters(product.code)"
		var="productCode"/>

<div data-bv-show="questions" data-bv-productId="${productCode}">${sBvOutputQA}</div>
