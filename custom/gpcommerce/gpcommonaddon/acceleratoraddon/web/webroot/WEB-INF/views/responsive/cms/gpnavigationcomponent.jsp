<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<vx-left-nav :left-nav-data='${navigationData}' :show-company-section='${showCompanyLinks}'></vx-left-nav>