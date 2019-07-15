<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
    <div class="container-fluid">
    <%-- <cms:pageSlot position="JuicerFeedSection" var="feature" element="div">
            <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
        </cms:pageSlot> --%>
        <cms:pageSlot position="Section1" var="feature">
            <cms:component component="${feature}" />
        </cms:pageSlot>
        <div class="row no-margin">
            <div class="col-xs-12 col-md-6 no-space">
                <cms:pageSlot position="Section2A" var="feature" element="div" class="row no-margin">
                    <cms:component component="${feature}" element="div" class="col-xs-12 col-sm-6 no-space yComponentWrapper"/>
                </cms:pageSlot>
            </div>
            <div class="col-xs-12 col-md-6 no-space">
                <cms:pageSlot position="Section2B" var="feature" element="div" class="row no-margin">
                    <cms:component component="${feature}" element="div" class="col-xs-12 col-sm-6 no-space yComponentWrapper"/>
                </cms:pageSlot>
            </div>
            <div class="col-xs-12">
                <cms:pageSlot position="Section2C" var="feature" element="div" class="landingLayout2PageSection2C">
                    <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
                </cms:pageSlot>
            </div>
        </div>

        <cms:pageSlot position="Section3" var="feature" element="div">
            <cms:component component="${feature}" element="div" class="no-space yComponentWrapper"/>
        </cms:pageSlot>

		 <%-- These 3 Slots are specific to gpemployee Store only --%>
         <cms:pageSlot position="SapHolderSlot" var="feature">
            <cms:component component="${feature}"/>
        </cms:pageSlot>

		 <cms:pageSlot position="BannerHolderSlot" var="feature">
            <cms:component component="${feature}" />
        </cms:pageSlot>

		 <cms:pageSlot position="BundleHolderSlot" var="feature">
            <cms:component component="${feature}"/>
        </cms:pageSlot>

        <cms:pageSlot position="Section4" var="feature" element="div" class="row no-margin">
            <cms:component component="${feature}" element="div" class="col-xs-6 col-md-3 no-space yComponentWrapper"/>
        </cms:pageSlot>

        <cms:pageSlot position="Section5" var="feature" element="div">
            <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
        </cms:pageSlot>
        
        <cms:pageSlot position="JuicerFeedSlot" var="feature"
				element="div" class="">
				<cms:component component="${feature}" />
        </cms:pageSlot>
		<!-- <vx-qples-form :i18n="messages.common.qples"></vx-qples-form> -->
    </div>
</template:page>
