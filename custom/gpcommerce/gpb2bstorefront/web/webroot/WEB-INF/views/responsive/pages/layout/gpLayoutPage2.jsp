<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
<div class="container-fluid">
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot1" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
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
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot3" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row no-margin">
		<div class="col-xs-12">
			<cms:pageSlot position="ContentSlot4" var="feature" element="div"
				class="no-margin">
				<cms:component component="${feature}" element="div"
					class="yComponentWrapper" />
			</cms:pageSlot>
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
</div>
</template:page>