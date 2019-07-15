<%@ page trimDirectiveWhitespaces="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
			<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

				<template:page pageTitle="${pageTitle}">
					<div class="container-fluid business-login-page">
						<div class="row no-margin">
							<div class="col-xs-12">
								<cms:pageSlot position="Section1Slot" var="feature" element="div"
									class="no-margin">
									<cms:component component="${feature}" element="div"
										class="yComponentWrapper" />
								</cms:pageSlot>
							</div>
						</div>
						<div class="row no-margin">
							<div class="col-xs-12">
								<cms:pageSlot position="Section2Slot" var="feature" element="div"
									class="no-margin">
									<cms:component component="${feature}" element="div"
										class="yComponentWrapper" />
								</cms:pageSlot>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12">
								<cms:pageSlot position="DispenserFormSlot" var="feature">
									<cms:component component="${feature}" />
								</cms:pageSlot>
							</div>
						</div>
						<div class="row no-margin">
							<div class="col-xs-12">
								<cms:pageSlot position="Section3Slot" var="feature" element="div"
									class="no-margin">
									<cms:component component="${feature}" element="div"
										class="yComponentWrapper" />
								</cms:pageSlot>
							</div>
						</div>
						<div class="row no-margin">
							<div class="col-xs-12">
								<cms:pageSlot position="Section3Slot" var="feature" element="div"
									class="no-margin">
									<cms:component component="${feature}" element="div"
										class="yComponentWrapper" />
								</cms:pageSlot>
							</div>
						</div>
					</div>
				</template:page>
