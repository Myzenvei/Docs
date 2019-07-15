<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
		<div itemscope style="display: none;"
                        itemtype="http://schema.org/Organization">
      	     <c:choose>
  				<c:when test="${not empty {{messages.accessCustomerService.liveChat.callNumber}}}">           
	            	<span itemprop="telephone">{{messages.accessCustomerService.liveChat.callNumber}}</span>
	            </c:when>
	            <c:otherwise>      
	            	<span itemprop="telephone">{{messages.accessCustomerService.liveChat.callExt}}</span>
	            </c:otherwise>
            </c:choose>   
            <c:if test="${not empty {{messages.accessCustomerService.liveChat.emailUs}}}">
        		<span itemprop="email">{{messages.accessCustomerService.liveChat.emailUs}}</span>
        	</c:if>
         </div>
	<div class="container__full">
		<div class="container-fluid">
			<div class="row">
				<h1 itemscope class="contact-page-heading col-xs-12 px-sm-5 my-xs-0 pt-xs-4 pt-sm-5 h3" style="font-weight:400" itemtype="http://schema.org/Organization" itemprop="contactType">We are here to
					help</h1>
			</div>
			<div class=" row">
				<div class="col-xs-12">
					<cms:pageSlot position="LiveChatButtonSlot" var="feature" element="div" class="">
						<cms:component component="${feature}" />
					</cms:pageSlot>

				</div>
			</div>
		</div>
	</div>
</template:page>