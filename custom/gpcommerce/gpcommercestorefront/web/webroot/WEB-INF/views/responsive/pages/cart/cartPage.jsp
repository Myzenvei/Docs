<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:page pageTitle="${pageTitle}">
    <cart:cartValidation/>
    <cart:cartPickupValidation/>


    <div>
        <!-- CartComponent -->
        <div class="px-sm-0 cart-container row" v-if="cartHasVisibleItems">
            <!-- cart products display this component is in  castDisplay.jsp -->
            <cms:pageSlot position="TopContent" var="feature">
                <cms:component component="${feature}" element="div" class="mx-xs-3 mx-sm-5 mx-md-0 col-md-8 yComponentWrapper"/>
            </cms:pageSlot>
            <!--  cart order summary this component is in  cartTotalsDisplay.jsp -->
            <div class= "order-summary-section col-md-4 col-xs-12 mx-xs-0" :class="{'mobile-order-summary-popup':orderSummaryClicked}">
                <cms:pageSlot position="CenterRightContentSlot" var="feature">
                    <cms:component component="${feature}" element="div" class="order-summary-component yComponentWrapper"/>
                </cms:pageSlot>
            <!-- this is for checkout button this component is in checkoutDisplay.jsp -->
                <cms:pageSlot position="CenterLeftContentSlot" var="feature">
                    <cms:component component="${feature}" element="div" class="checkout-component yComponentWrapper"/>
                </cms:pageSlot>
            </div>
        </div>
        <!-- This is for Empty cart -->
				<div v-if="!cartHasVisibleItems">
          <cms:pageSlot position="EmptyCartMiddleContent" var="feature">
              <cms:component component="${feature}" element="div" class="yComponentWrapper content__empty"/>
          </cms:pageSlot>
				</div>
        <!-- This is for banner component this component is in simple-->
        <cms:pageSlot position="BannerMiddleContent" var="feature">
            <cms:component component="${feature}" element="div" class="yComponentWrapper content__empty"/>
        </cms:pageSlot>
				<div v-if="cartHasVisibleItems">
          <!-- carousel component this component is in simplesuggessioncomponent.jsp -->
          <cms:pageSlot position="BottomContentSlot" var="feature">
              <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
          </cms:pageSlot>
				</div>
    </div>
</template:page>
