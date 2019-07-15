<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:choose>
<c:when test="${page.uid eq 'gpHomePage'}">
       <c:set var="index" value="0"/>
</c:when>
<c:when test="${page.uid eq 'ourNapkinsPage'}">
       <c:set var="index" value="1"/>
</c:when>
<c:when test="${page.uid eq 'findAndBuyUsPage'}">
       <c:set var="index" value="2"/>
</c:when>
<c:when test="${page.uid eq 'faqPage'}">
       <c:set var="index" value="3"/>
</c:when>
<c:when test="${page.uid eq 'teachersPage'}">
       <c:set var="index" value="4"/>
</c:when>
<c:otherwise>
       <c:set var="index" value="6"/>
</c:otherwise>
</c:choose>
<c:choose>
  <c:when test="${fn:containsIgnoreCase(cmsPage.typeCode,'ContentPage') and (fn:containsIgnoreCase(cmsPage.uid,'HomePage') or fn:containsIgnoreCase(cmsPage.label,'HomePage'))}">
  <mg-header-section :nav-data='${headerSection}' :nav-social-links='${headerData}' :active-link=${index} :i18n="messages.altTexts" :is-home-page="true"></mg-header-section>
</c:when>
<c:otherwise>
  <mg-header-section :nav-data='${headerSection}' :nav-social-links='${headerData}' :active-link=${index} :i18n="messages.altTexts" :is-home-page="false"></mg-header-section>
</c:otherwise>
</c:choose>


