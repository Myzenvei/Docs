<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<vx-header-section :header-data='${headerSection}' :i18n="messages.common.headerSection"
	:navigation-links='${headerData}'>
	<c:choose>
		<c:when test="${not isFavoritesEnabled}">
			<vx-auto-suggest slot="search" product-catalog='${productCatalog}' root-category='${rootCategory}'
				slot-scope="toggleSearchIcon" :toggle-search-icon="toggleSearchIcon.toggleSearchIcon"
				:is-bazaar-voice="'${isBazaarVoiceEnabled}'" :search-browse-i18n="messages.searchBrowse" :single-product-enabled="${singleProductEnabled}">
			</vx-auto-suggest>
		</c:when>
		<c:otherwise>
			<vx-auto-suggest slot="search" product-catalog='${productCatalog}' root-category='${rootCategory}'
				:is-favorites="${isFavoritesEnabled}" slot-scope="toggleSearchIcon"
				:toggle-search-icon="toggleSearchIcon.toggleSearchIcon" :is-bazaar-voice="'${isBazaarVoiceEnabled}'"
				:search-browse-i18n="messages.searchBrowse" :single-product-enabled="${singleProductEnabled}"></vx-auto-suggest>
		</c:otherwise>
	</c:choose>
	<vx-mini-cart slot="cart" :is-sample-cart="'${enablesamplecart}'" :i18n="messages.manageShoppingCart.miniCart">
	</vx-mini-cart>
</vx-header-section>