<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<!-- Verified that there's a pre-existing bug regarding the setting of showTax; created issue   -->
<vx-order-summary :i18n="messages.manageShoppingCart.orderSummary" page="cart" :order-details='cartData' v-if='cartDataLoaded'></vx-order-summary>
