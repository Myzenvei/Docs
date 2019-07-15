<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>

		<c:url value="/j_spring_security_check" var="loginActionUrl" />

		<!--<div class="login-section">
			<user:login actionNameKey="login.login" action="${loginActionUrl}" />
		</div>-->

		<!-- i18n for login password through props-->
		<div>
			<vx-minimized-header :img-url="'brands/gpEmployee-header.svg'" :img-alt-text="'Employee Store'" :i18n="messages.common.minimizedHeader"></vx-minimized-header>
			<vx-login action-url='${loginActionUrl}' :i18n="messages.registrationLogin.login"></vx-login>
		</div>
