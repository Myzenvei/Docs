<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:choose>
  <c:when test="${fn:containsIgnoreCase(cmsPage.typeCode,'ContentPage') and (fn:containsIgnoreCase(cmsPage.uid,'HomePage') or fn:containsIgnoreCase(cmsPage.label,'HomePage'))}">
  <vx-footer-section :is-checkout-footer=false  :copyright-text="'${companyText}'" :footer-data='${footerData}' :is-home-page="true"></vx-footer-section>
</c:when>
<c:otherwise>
  <vx-footer-section :is-checkout-footer=false  :copyright-text="'${companyText}'" :footer-data='${footerData}' :is-home-page="false"></vx-footer-section>
</c:otherwise>
</c:choose>
