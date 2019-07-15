<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />


<c:choose>
			<c:when test="${page.uid eq 'teachersPage'}">
                <mg-hero-banner-v1 
                    :banner-data='${couponsData}' 
                    :btn-text="'Next'" 
                    :is-show-input="true"
                    :is-hide-age="false" 
                    :page-uid="'${page.uid}'"
                    :banner-colors="{bannerTitleColor: '#522773',
                                    rowReverse: true,
                                    promoArrow:'images/mardi-gras/mg_savings_arrow.png',
                                    bannerHeadingColor: '#E3218f',
                                    isDownloadKit:true,
                                    bannerInfoColor: '#522773'}"
                    :i18n = "messages.teachers" >
                </mg-hero-banner-v1> 
			</c:when>
			<c:when test="${page.uid eq 'signupPage'}">
                <mg-hero-banner-v1 
                    :banner-data='${couponsData}' 
                    :btn-text="'Next'" 
                    :is-show-input="true" 
                    :page-uid="'${page.uid}'"
                    :is-hide-age="true"
                    :banner-colors="{bannerTitleColor: '#522773',
                                    bannerHeadingColor: '#E3218f',
                                    bannerInfoColor: '#522773'}"
                    :i18n = "messages.teachers" >
                </mg-hero-banner-v1> 
			</c:when>
			
</c:choose>
