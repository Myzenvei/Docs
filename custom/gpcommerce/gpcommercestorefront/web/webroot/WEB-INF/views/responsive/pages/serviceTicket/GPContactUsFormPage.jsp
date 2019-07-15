<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
	<div itemscope style="display: none;"
                        itemtype="http://schema.org/Organization">
 	     <c:choose>
		<c:when test="${siteUid=='mardigras'}">           
        	<span itemprop="telephone">{{messages.liveChat.callExt}}</span>
        	<c:if test="${not empty contactUsEmail}">
        		<span itemprop="email">${contactUsEmail}</span>
        	</c:if>
        </c:when>
        <c:otherwise>
        	<span itemprop="telephone">{{messages.accessCustomerService.liveChat.callNumber}}</span>
        	<c:if test="${not empty {{messages.accessCustomerService.liveChat.emailAddress}}}">
        		<span itemprop="email">{{messages.accessCustomerService.liveChat.emailAddress}}</span>
        	</c:if>
        </c:otherwise>	            
       </c:choose>   
    </div>
	<div class="${siteUid=='mardigras' ? 'container mardigras-contact-us p-xs-0' : 'container__full'}">
		<div class="${siteUid=='mardigras' ? '' : 'container-fluid'}" >
			<div class="${siteUid=='mardigras' ? '' : 'row'}">
				<c:choose>
					<c:when test="${siteUid == 'mardigras'}"> 
						<h1 itemscope itemtype="http://schema.org/Organization" itemprop="contactType" class="contact-page-heading col-xs-12 px-sm-5 my-xs-0 pt-xs-4 pt-sm-5 h3" style="font-weight:400" tabindex="0" v-html="'CONTACT MARDI GRAS <sup>&#174;</sup> NAPKINS'"></h1>
					</c:when>
					<c:otherwise>
						<h1 itemscope class="contact-page-heading col-xs-12 px-sm-5 my-xs-0 pt-xs-4 pt-sm-5 h3" style="font-weight:400" tabindex="0" itemtype="http://schema.org/Organization" itemprop="contactType">We
								are here to
								help</h1>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="${siteUid=='mardigras'?'' : 'row' }">
				<div class="${siteUid=='mardigras'?'col-xs-12 p-md-0' : 'col-xs-12' }">
					<cms:pageSlot position="LiveChatButtonSlot" var="feature" element="div" class="">
						<cms:component component="${feature}" />
					</cms:pageSlot>

				</div>
			</div>
		</div>
	</div>
</template:page>
