<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<div class="container row m-xs-auto ">
 <mg-hero-banner-v1 :banner-data='${storeLocatorData}' class-name='find-a-store'></mg-hero-banner-v1>

 <mg-find-a-store :i18n="messages.findAStore"></mg-find-a-store>
 </div>
