<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%@ taglib prefix="bazaarvoice" tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/bazaarvoice"%>
<%@ taglib prefix="bazaarvoiceAction" tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/action" %>
<%--BAZAARVOICE_MODIFICATION_END--%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>

<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>

<li class="product__list--item">

	<%--BAZAARVOICE_MODIFICATION_START--%>
	<bazaarvoice:inlineRatings product="${product}"/>
		<%--BAZAARVOICE_MODIFICATION_END--%>

	<ycommerce:testId code="test_searchPage_wholeProduct">
		<a class="product__list--thumb" href="${productUrl}" title="${fn:escapeXml(product.name)}" >
			<product:productPrimaryImage product="${product}" format="thumbnail"/>
		</a>
		<ycommerce:testId code="searchPage_productName_link_${product.code}">
			<a class="product__list--name" href="${productUrl}">${ycommerce:sanitizeHTML(product.name)}</a>
		</ycommerce:testId>

		<div class="product__list--price-panel">
			<c:if test="${not empty product.potentialPromotions}">
				<div class="product__listing--promo">
					<c:forEach items="${product.potentialPromotions}" var="promotion">
						${ycommerce:sanitizeHTML(promotion.description)}
					</c:forEach>
				</div>
			</c:if>

			<ycommerce:testId code="searchPage_price_label_${product.code}">
				<div class="product__listing--price"><product:productListerItemPrice product="${product}"/></div>
			</ycommerce:testId>
		</div>

		<c:if test="${not empty product.summary}">
			<div class="product__listing--description">${ycommerce:sanitizeHTML(product.summary)}</div>
		</c:if>



		<c:set var="product" value="${product}" scope="request"/>
		<c:set var="addToCartText" value="${addToCartText}" scope="request"/>
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>
		<div class="addtocart">
			<div id="actions-container-for-${fn:escapeXml(component.uid)}" class="row">
					<%--BAZAARVOICE_MODIFICATION_START--%>
				<bazaarvoiceAction:bazaarVoiceProductListItemActions element="div" parentComponent="${component}"  />
							<%--BAZAARVOICE_MODIFICATION_END--%>
			</div>
		</div>

	</ycommerce:testId>
</li>







