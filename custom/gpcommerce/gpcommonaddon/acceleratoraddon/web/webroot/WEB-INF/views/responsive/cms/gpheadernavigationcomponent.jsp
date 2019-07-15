<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<c:choose>
  <c:when test="${not isFavoritesEnabled}">
    <vx-header-section :header-data='${headerSection}' :i18n="messages.common.headerSection"
      :is-home-page="${fn:containsIgnoreCase(cmsPage.typeCode,'ContentPage') and (fn:containsIgnoreCase(cmsPage.uid,'HomePage') or fn:containsIgnoreCase(cmsPage.label,'HomePage'))}">
  </c:when>
  <c:otherwise>
    <vx-header-section :header-data='${headerSection}' :i18n="messages.common.headerSection"
      :is-favorites-enabled='${isFavoritesEnabled}' :is-home-page="${fn:containsIgnoreCase(cmsPage.typeCode,'ContentPage') and (fn:containsIgnoreCase(cmsPage.uid,'HomePage') or fn:containsIgnoreCase(cmsPage.label,'HomePage'))}">
  </c:otherwise>
</c:choose>
<c:choose>
      <c:when test="${not isFavoritesEnabled}">
        <vx-auto-suggest slot="search" product-catalog='${productCatalog}' root-category='${rootCategory}'
          slot-scope="toggleSearchIcon" :toggle-search-icon="toggleSearchIcon.toggleSearchIcon"
          :is-bazaar-voice="'${isBazaarVoiceEnabled}'" :search-browse-i18n="messages.searchBrowse" :single-product-enabled="${singleProductEnabled}"></vx-auto-suggest>
      </c:when>
      <c:otherwise>
        <vx-auto-suggest slot="search" product-catalog='${productCatalog}' root-category='${rootCategory}'
          :is-favorites="${isFavoritesEnabled}" slot-scope="toggleSearchIcon"
          :toggle-search-icon="toggleSearchIcon.toggleSearchIcon" :is-bazaar-voice="'${isBazaarVoiceEnabled}'"
          :search-browse-i18n="messages.searchBrowse" :single-product-enabled="${singleProductEnabled}"></vx-auto-suggest>
      </c:otherwise>
</c:choose>
<vx-mini-cart slot="cart" :i18n="messages.manageShoppingCart.miniCart"></vx-mini-cart>
<c:if test='${accountMenu ne null and not empty accountMenu}'>
  <vx-left-nav slot="left-nav" :left-nav-data='${accountMenu}' :i18n="messages.manageProfileShoppingLists.leftNav"
    :show-company-section='${showCompanyLinks}'></vx-left-nav>
</c:if>
</vx-header-section>
