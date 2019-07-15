<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Verified that there's a pre-existing bug regarding the setting of showTax; created issue  --%>

<c:if test="${enablesamplecart != true}">
    <vx-order-summary :i18n="messages.manageShoppingCart.orderSummary" page="cart" :order-details='cartData' v-if='cartDataLoaded'></vx-order-summary>
</c:if>