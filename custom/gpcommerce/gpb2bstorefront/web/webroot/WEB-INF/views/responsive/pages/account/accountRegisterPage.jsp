<%@ page trimDirectiveWhitespaces="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
			<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

				<template:page pageTitle="${pageTitle}">
					<div class="container-fluid business-login-page">
						<div class="row">
							<div class="d-flex business-register-component flex-row flex-wrap">
								<cms:pageSlot position="CenterContentSlot" var="feature">
									<cms:component component="${feature}" />
								</cms:pageSlot>
							</div>
						</div>
					</div>
				</template:page>
