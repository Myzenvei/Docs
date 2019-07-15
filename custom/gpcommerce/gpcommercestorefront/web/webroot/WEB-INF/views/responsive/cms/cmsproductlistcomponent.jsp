<!-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>

<div class="product__listing product__list">
    <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
        <product:productListerItem product="${product}"/>
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

<storepickup:pickupStorePopup/> -->
<vx-search-result :is-favorites="${isFavoritesEnabled}" category-id="${categoryCode}"
    :is-bazaar-voice="'${isBazaarVoiceEnabled}'" :is-content-enabled="${isContentIndexingEnabled}"
    :is-bulk-enabled="${isBulkEnabled}" :search-browse-i18n="messages.searchBrowse" :is-sample-cart="'${enablesamplecart}'" :single-product-enabled="${singleProductEnabled}"></vx-search-result>