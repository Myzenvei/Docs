<%@ tag import="java.math.BigDecimal" %>
<%@ tag import="de.hybris.platform.commercefacades.order.data.OrderEntryData" %>
<%@ tag import="java.math.RoundingMode" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="order" required="true"
              type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<c:set var="subTotal" value="${order.subTotal}"/>
<jsp:useBean id="subTotal" class="de.hybris.platform.commercefacades.product.data.PriceData"/>

<c:set var="totalTax" value="${order.totalTax}"/>
<jsp:useBean id="totalTax" class="de.hybris.platform.commercefacades.product.data.PriceData"/>

<c:set var="orderNet" value="${order.net}"/>
<jsp:useBean id="orderNet" class="java.lang.Object"/>

<c:set var="productCodeToPriceWithoutTax" value="${productCodeToPriceWithoutTax}"/>

<%
    String subTotalResult = "";
    if ((Boolean)orderNet) {
        subTotalResult = subTotal.getValue().toString();
    } else {
        BigDecimal subTotalResultWithScale = subTotal.getValue().subtract(totalTax.getValue());
        subTotalResultWithScale = subTotalResultWithScale.setScale(2, RoundingMode.HALF_EVEN);
        subTotalResult = subTotalResultWithScale.toString();
    }
%>

<script type="text/javascript">
    window.bvCallback = function (BV) {
        BV.pixel.trackTransaction({
            "orderId": "${order.code}",
            "tax": "${order.totalTax.value}",
            "shipping": "${order.deliveryCost.value}",
            "total": "<%=subTotalResult%>",
            <c:if test="${not empty order.deliveryAddress.town}">"city": "${order.deliveryAddress.town}", </c:if>
            <c:if test="${not empty order.deliveryAddress.region.name}">"state": "${order.deliveryAddress.region.name}", </c:if>
            <c:if test="${not empty order.deliveryAddress.country.name}">"country": "${order.deliveryAddress.country.name}", </c:if>
            "currency": "${order.totalPrice.currencyIso}",

            "items": [
                <c:forEach items="${order.entries}" var="entry" varStatus="status">
                <s:eval expression="T(com.bazaarvoice.hybris.utils.BazaarVoiceUtils).ReplaceUnsupportedCharacters(entry.product.code)" var="productCode" />
                <s:eval expression="T(com.bazaarvoice.hybris.utils.BazaarVoiceUtils).getProductPrice(order, entry.basePrice.value, entry.product.code, productCodeToPriceWithoutTax)" var="bvPrice" />
                {
                    "sku": "${productCode}",
                    "name": "${entry.product.name}",
                    "price": "${bvPrice}",
                    "quantity": "${entry.quantity}" <c:if test="${not empty entry.product.categories}">,
                    "category": "${entry.product.categories[0].name}"
                    </c:if>
                }<c:if test="${not status.last}">, </c:if>
                </c:forEach>
            ],
            "locale": "${cmsSite.bvLocale}",
            "partnerSource": "bazaarvoice hybris extension v${cmsSite.bvConfig.extensionVersion}",
            "email": "${email}"

        });
    };

</script>
