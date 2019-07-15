<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty bannerImageData}">
    <vx-hero-banner :hero-banner-data='${bannerImageData}'></vx-hero-banner>
</c:if>
