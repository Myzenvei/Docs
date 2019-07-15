<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
    <vx-build-bundle :i18n="messages.buildBundle" :is-bazaar-voice="'${isBazaarVoiceEnabled}'"></vx-build-bundle>
    <%-- <c:out value="${bundleData}"/> --%>
</template:page>
