<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<vx-product-solution :tabs-data='${productSolutionData}' component-id='${componentId}' ></vx-product-solution>
 