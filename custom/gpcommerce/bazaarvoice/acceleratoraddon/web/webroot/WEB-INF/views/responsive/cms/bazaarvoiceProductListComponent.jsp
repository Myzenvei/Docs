<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/product" %>
<%--BAZAARVOICE_MODIFICATION_END--%>

<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>

<div class="product__listing product__list">
    <c:forEach items="${searchPageData.results}" var="product" varStatus="status">

        <%--BAZAARVOICE_MODIFICATION_START--%>
        <product:bazaarvoiceProductListerItem product="${product}"/>
        <%--BAZAARVOICE_MODIFICATION_END--%>

    </c:forEach>
</div>

<div id="addToCartTitle" class="display-none">
    <div class="add-to-cart-header">
        <div class="headline">
            <span class="headline-text"><spring:theme code="basket.added.to.basket"/></span>
        </div>
    </div>
</div>

<nav:pagination top="false" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>

<storepickup:pickupStorePopup/>