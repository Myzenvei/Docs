<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:choose>
  <c:when test="${page.uid eq 'faqPage'}">
    <mg-footer-section :footer-data='${footerData}' :is-background-color="true"></mg-footer-section>       
  </c:when>
  <c:otherwise>
      <mg-footer-section :footer-data='${footerData}'></mg-footer-section>
</c:otherwise>
</c:choose>

