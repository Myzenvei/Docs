<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<vx-hero-banner v-if="globals.isCategoryImageAvailable" :hero-banner-data="{}" :is-categories-banner='true'>
</vx-hero-banner>
<cms:pageSlot position="GPCategoryAuthoringContentSlot" var="feature" element="div">
	<cms:component component="${feature}" />
</cms:pageSlot> 

<vx-search-result category-id="${categoryCode}" :is-favorites="${isFavoritesEnabled}"
    :is-bazaar-voice="'${isBazaarVoiceEnabled}'" :is-content-enabled="${isContentIndexingEnabled}"
    :is-bulk-enabled="${isBulkEnabled}" :search-browse-i18n="messages.searchBrowse"
    :is-sample-cart="'${enablesamplecart}'" :single-product-enabled="${singleProductEnabled}"></vx-search-result>
<vx-category-desc :category-description="'${categoryDescription}'"></vx-category-desc>