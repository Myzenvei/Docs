<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<c:url value="/j_spring_security_check" var="loginActionUrl" />
<!-- <div class="register__section">
	<c:url value="/login/register" var="registerActionUrl" />
	<user:register actionNameKey="register.submit"
		action="${registerActionUrl}" />
</div> -->
<!-- i18n for social login through props-->
<c:choose>
	<c:when test="${showSocialLogin == true}">
		<vx-social-login action-url='${loginActionUrl}' :i18n="messages.registrationLogin.socialLogin"></vx-social-login>
	</c:when>
	<c:otherwise>
		<vx-minimized-header :img-url="'brands/gpEmployee-header.svg'" :img-alt-text="'Employee Store'" :i18n="messages.common.minimizedHeader"></vx-minimized-header>
		<vx-login action-url='${loginActionUrl}' :i18n="messages.registrationLogin.login"></vx-login>
	</c:otherwise>
</c:choose>