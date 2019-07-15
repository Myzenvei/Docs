<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<vx-bundle-carousel :i18n="messages.bundleCarousel" bundle-carousel-title="${bundleCarouselTitle}" :bundle-carousel-data='${bundleCarousel}'></vx-bundle-carousel>
