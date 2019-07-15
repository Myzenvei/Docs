<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- <vx-find-distributor :i18n="messages.viewSiteContent"></vx-find-distributor> --%>

<%-- <vx-find-distributor 
  :i18n="messages.viewSiteContent" 
  :is-pdp-page='${isPdpPage}' 
  :prd-code='${product}'>
</vx-find-distributor> --%>
<c:choose>
  <c:when test="${isPdpPage == false}">
    <vx-find-distributor :i18n="messages.viewSiteContent" :timeout-val="15000" :list-page-url="'${listPage}'"></vx-find-distributor>
  </c:when>
  <c:otherwise>
    <vx-find-distributor :i18n="messages.viewSiteContent" :timeout-val="15000" :is-pdp-page='${isPdpPage}' :prd-code="'${product}'" :list-page-url="'${listPage}'">
    </vx-find-distributor>
  </c:otherwise>
</c:choose>