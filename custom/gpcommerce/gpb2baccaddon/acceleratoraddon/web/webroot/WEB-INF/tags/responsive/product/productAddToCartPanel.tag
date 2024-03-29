<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="allowAddToCart" required="true" type="java.lang.Boolean" %>
<%@ attribute name="isMain" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="b2b-product" tagdir="/WEB-INF/tags/addons/gpb2baccaddon/responsive/product" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:url value="${product.url}/orderForm" var="productOrderFormUrl" htmlEscape="false"/>

<div class="qty">
    <c:if test="${product.purchasable}">
        <label for="qtyInput">
            <spring:theme code="basket.page.quantity"/>
        </label>
        <input type="text" maxlength="3" size="1" id="qtyInput" name="qtyInput" class="qty" value="1">
    </c:if>

    <ycommerce:testId code="productDetails_productInStock_label">
        <b2b-product:productStockThreshold product="${product}"/>
    </ycommerce:testId>
    <product:productFutureAvailability product="${product}" futureStockEnabled="${futureStockEnabled}"/>
</div>

<div id="actions-container-for-${component.uid}" class="productAddToCartPanelContainer clearfix">
	<ul class="productAddToCartPanel clearfix">
		<c:if test="${multiDimensionalProduct}" >
			<c:url value="${product.url}/orderForm" var="productOrderFormUrl"/>
			<a href="${productOrderFormUrl}" class="button negative" id="productOrderButton" ><spring:theme code="order.form" /></a>
		</c:if>
		<action:actions element="li" styleClass="productAddToCartPanelItem" parentComponent="${component}"/>
	</ul>
</div>
