<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<mg-video-wrapper :video-wrapper-data='${videoData}' ></mg-video-wrapper>