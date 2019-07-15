<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:htmlEscape defaultHtmlEscape="true" />


<c:choose>
  <c:when test="${siteUid == 'mardigras'}"> 
  <div class="container p-xs-0">
     <vx-live-chat :i18n="messages.liveChat" live-chat-theme='${componentTheme}' :show-contact-us='${showContactUs}' :image-text-data='${imageTextData}' :contact-us-email="'${contactUsEmail}'"></vx-live-chat>
  </div>
  </c:when>
  <c:otherwise>
      <vx-contact-us :i18n="messages.accessCustomerService" live-chat-theme='${componentTheme}'
      :show-contact-us='${showContactUs}' :image-text-data='${imageTextData}'
      :is-live-chat-enabled='${showLiveChat}'></vx-contact-us>
  </c:otherwise>
</c:choose>
