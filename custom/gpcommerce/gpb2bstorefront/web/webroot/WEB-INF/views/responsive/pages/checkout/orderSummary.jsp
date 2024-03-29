<%@ page trimDirectiveWhitespaces="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
			<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
				<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

					<vx-order-summary :i18n="messages.checkout.orderSummary" page="checkout">
						<div slot="bottomButton">
							<vx-place-order :i18n="messages.checkout.checkout"></vx-place-order>
						</div>
					</vx-order-summary>
					<%-- <template:page pageTitle="${pageTitle}">
	<div class="checkout-login">
		<div class="row">
			<div class="col-md-6">
				<cms:pageSlot position="LeftContentSlot" var="feature" element="div" class="checkout-login-left-content-slot">
					<cms:component component="${feature}" element="div"  class="checkout-login-left-content-component"/>
				</cms:pageSlot>
			</div>

			<div class="col-md-6">
				<cms:pageSlot position="RightContentSlot" var="feature" element="div" class="checkout-login-right-content-slot">
					<cms:component component="${feature}" element="div" class="checkout-login-right-content-component"/>
				</cms:pageSlot>
			</div>
		</div>


		<cms:pageSlot position="CenterContentSlot" var="feature" class="checkoutLoginPageCenter" element="div">
			<cms:component component="${feature}" class="checkoutLoginPageCenter-component"/>
		</cms:pageSlot>
	</div>
</template:page> --%>