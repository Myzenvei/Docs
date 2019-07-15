<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
        <div class="account-section">
            <div class="account-section-header no-border">Password Reset Link</div>
            <div class="account-section-content row">
                <form:form method="post" commandName="forgottenPwdForm">
                    <div class="col-md-6">
                    	<div class="description">Sorry, but that password reset link is no longer valid. If you still need to reset your password, please enter your email address below to request a new link. </div>
                        <div class="form-group">
                            <formElement:formInputBox idKey="forgottenPwd.email" labelKey="forgottenPwd.email" path="email" mandatory="true"/>
                        </div>
                        <div class="row login-form-action">
                            <div class="col-sm-6">
                                <button type="submit" class="btn btn-primary btn-block">
                                    Request New Link
                                </button>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        
        <!-- FE Team should link this form submit to rest endpoint {baseSiteId}/forgottenpasswordtokens -->