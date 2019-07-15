<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<vx-product-support :tabs-data= '${tipsAndKnowledgeInfo}' :component-title= '${title}' component-id='${componentId}' component-theme='${componentTheme}' :active-tab='${activeTab}' />
