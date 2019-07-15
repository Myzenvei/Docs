<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<!-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}" numberPagesShown="${numberPagesShown}"/>

<div class="product__listing product__grid">
    <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
        <product:productListerGridItem product="${product}" />
    </c:forEach>
</div>

<div id="addToCartTitle" class="display-none">
    <div class="add-to-cart-header">
        <div class="headline">
            <span class="headline-text"><spring:theme code="basket.added.to.basket"/></span>
        </div>
    </div>
</div>

<nav:pagination top="false"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"  searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/> -->


<vx-hero-banner v-if="globals.isCategoryImageAvailable" :hero-banner-data="{}" :is-categories-banner='true'>
</vx-hero-banner>
<cms:pageSlot position="GPCategoryAuthoringContentSlot" var="feature" element="div">
	<cms:component component="${feature}" />
</cms:pageSlot> 
<vx-search-result category-id="${categoryCode}" :is-bazaar-voice="'${isBazaarVoiceEnabled}'"
    :is-content-enabled="${isContentIndexingEnabled}" :is-bulk-enabled="${isBulkEnabled}"
    :search-browse-i18n="messages.searchBrowse" :is-sample-cart="'${enablesamplecart}'"
    :single-product-enabled="${singleProductEnabled}">
</vx-search-result>
<vx-category-desc :category-description="'${categoryDescription}'"></vx-category-desc>