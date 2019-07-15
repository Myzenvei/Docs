<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url value="/j_spring_security_check" var="loginActionUrl" />


<!-- i18n for social login through props-->
<c:if test="${showSocialLogin == true}">
	<vx-social-login action-url='${loginActionUrl}' :i18n="messages.registrationLogin.socialLogin" register></vx-social-login>
</c:if>