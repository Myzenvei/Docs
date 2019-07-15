<%@ attribute name="product" required="true"
			  type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<script type="text/javascript">
    window.bvCallback = function (BV) {
        BV.reviews.on('show', function () {
            <c:if test="${not empty cmsSite.bvConfig.showReviewsScript}">
            ${cmsSite.bvConfig.showReviewsScript}
            </c:if>
        });

        BV.questions.on('show', function () {
            <c:if test="${not empty cmsSite.bvConfig.showQuestionsScript}">
            ${cmsSite.bvConfig.showQuestionsScript}
            </c:if>
        });
    };
</script>


<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>

<s:eval expression="T(com.bazaarvoice.hybris.utils.BazaarVoiceUtils).ReplaceUnsupportedCharacters(product.code)"
		var="productCode"/>

<div data-bv-show="reviews" data-bv-productId="${productCode}">${sBvOutputReviews}</div>
