<div class="guest-create-account-container col-md-8">
    <vx-guest-create-account :i18n="messages.checkout.guestCreateAccount" class="col-xs-12 px-xs-5 py-xs-5">
    </vx-guest-create-account>
</div>

<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                <%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                        <%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>

                            <spring:htmlEscape defaultHtmlEscape="true" />
                            <spring:url value="/login/register/termsandconditions" var="getTermsAndConditionsUrl" />

                            <div class="checkout-success">
                                <div class="checkout-success__body">
                                    <div class="checkout-success__body__headline">
                                        <spring:theme code="checkout.orderConfirmation.thankYouForOrder" />
                                    </div>
                                    <p>
                                        <spring:theme code="text.account.order.orderNumberLabel" />
                                        <b> ${fn:escapeXml(orderData.code)}</b>
                                    </p>
                                    <p>
                                        <spring:theme code="checkout.orderConfirmation.copySentToShort" />
                                        <b> ${fn:escapeXml(email)}</b>
                                    </p>
                                </div>

                                <order:giftCoupons giftCoupons="${giftCoupons}" />

                                <c:if test="${not empty guestRegisterForm}">
                                    <div class="checkout__new-account">
                                        <div class="checkout__new-account__headline">
                                            <spring:theme code="guest.register" />
                                        </div>
                                        <p>
                                            <spring:theme code="order.confirmation.guest.register.description" />
                                        </p>

                                        <form:form method="post" commandName="guestRegisterForm" class="checkout__new-account__form clearfix">
                                            <div class="col-sm-8 col-sm-push-2 col-md-6 col-md-push-3">
                                                <form:hidden path="orderCode" />
                                                <form:hidden path="uid" />

                                                <div class="form-group">
                                                    <label for="email" class="control-label ">
                                                        <spring:theme code="register.email" />
                                                    </label>
                                                    <input type="text" value="${fn:escapeXml(guestRegisterForm.uid)}" class="form-control" name="email" id="email" readonly>
                                                </div>

                                                <formElement:formPasswordBox idKey="password" labelKey="guest.pwd" path="pwd" inputCSS="password strength form-control" mandatory="true"
                                                />
                                                <formElement:formPasswordBox idKey="guest.checkPwd" labelKey="guest.checkPwd" path="checkPwd" inputCSS="password form-control"
                                                    mandatory="true" />
                                                <c:if test="${ not empty consentTemplateData }">
                                                    <form:hidden path="consentForm.consentTemplateId" value="${consentTemplateData.id}" />
                                                    <form:hidden path="consentForm.consentTemplateVersion" value="${consentTemplateData.version}" />
                                                    <div class="checkbox">
                                                        <label class="control-label uncased">
                                                            <form:checkbox path="consentForm.consentGiven" />
                                                            <c:out value="${consentTemplateData.description}" />
                                                        </label>
                                                    </div>
                                                    <div class="help-block">
                                                        <spring:theme code="registration.consent.link" />
                                                    </div>
                                                </c:if>

                                                <div class="checkbox">
                                                    <label class="control-label uncased">
                                                        <input type="checkbox" id="registerChkTermsConditions" name="registerChkTermsConditions" />
                                                        <spring:theme code="register.termsConditions" arguments="${getTermsAndConditionsUrl}" htmlEscape="false" />
                                                    </label>
                                                </div>

                                                <div class="accountActions-bottom">
                                                    <ycommerce:testId code="guest_Register_button">
                                                        <button type="submit" class="btn btn-block btn-primary" disabled="disabled">
                                                            <spring:theme code="guest.register" />
                                                        </button>
                                                    </ycommerce:testId>
                                                </div>
                                            </div>
                                        </form:form>
                                    </div>
                                </c:if>
                            </div>

                            <div class="well well-tertiary well-single-headline">
                                <div class="well-headline">
                                    <spring:theme code="checkout.multi.order.summary" />
                                </div>
                            </div>
                            --%>