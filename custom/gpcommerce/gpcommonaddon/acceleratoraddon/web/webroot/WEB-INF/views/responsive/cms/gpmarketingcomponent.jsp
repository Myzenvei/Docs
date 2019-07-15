<%@ page trimDirectiveWhitespaces="true" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <spring:htmlEscape defaultHtmlEscape="true" />
      <div class="row">
        <vx-marketing-promo class="col-sm-12" :promotion-data='${promotionData}'></vx-marketing-promo>
      </div>
