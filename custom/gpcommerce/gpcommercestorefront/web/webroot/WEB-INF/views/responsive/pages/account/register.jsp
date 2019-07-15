<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <c:url value="/j_spring_security_check" var="loginActionUrl" />
    <!-- i18n for create account through props-->
    <vx-create-account action-url='${loginActionUrl}'  :country-marketing-values='${userMarketingOption}' :i18n="messages.registrationLogin.createAccount" :is-gender-enabled="${isGenderEnabled}" :is-dob-enabled="${isDOBEnabled}"></vx-create-account>