<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
<div class="container-fluid">
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot1" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>

	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot2" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>

	<div class="row no-margin">
		<div class="col-xs-12 no-space">
			<div class="col-xs-8">
				<cms:pageSlot position="ContentSlot3" var="feature" element="div"
					class="row no-margin">
					<cms:component component="${feature}" element="div"
						class="col-xs-4 structureViewSection yComponentWrapper" />
				</cms:pageSlot>
			</div>
			<div class="col-xs-4">
				<div class="row no-margin">
					<cms:pageSlot position="ContentSlot4" var="feature" element="div"
						class="no-margin">
						<cms:component component="${feature}" element="div"
							class="col-xs-4 structureViewSection yComponentWrapper" />
					</cms:pageSlot>
				</div>
			</div>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot5" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot6" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot7" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot8" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot9" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot10" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot11" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot12" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
</div>
</template:page>