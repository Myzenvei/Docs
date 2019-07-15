<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />


<vx-product-carousel :product-carousel-data='${productCarousel}'></vx-product-carousel>