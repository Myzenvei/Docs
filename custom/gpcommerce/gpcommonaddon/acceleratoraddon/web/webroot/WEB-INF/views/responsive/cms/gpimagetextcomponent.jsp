<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="row m-xs-0">
      <vx-image-text :image-tile-data='${image}' :text-tile-data='${text}'  component-theme='${componentTheme}' />
</div>
