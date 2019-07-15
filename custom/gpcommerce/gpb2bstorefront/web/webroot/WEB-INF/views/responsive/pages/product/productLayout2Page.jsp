<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<!-- <script type="text/javascript" async src="https://apps.bazaarvoice.com/deployments/${bvClient}/${gpbvId}/${bvEnv}/${bvLocale}/bv.js"></script>
<c:set var="productId" value="${product.code}"/>
<script type="text/javascript">
	window.addEventListener('load', function () {
		$BV.configure('global', {
			productId: '${productId}'
		});
		$BV.ui('rr', 'inline_ratings', {
			productIds: {
				'${productId}': {
					url: 'http://path_to_productX'
				},
			},
			containerPrefix: 'BVRRInlineRating'
		});

		$BV.ui('rr', 'show_reviews', {
			doShowContent: function () {
				// If the container is hidden (such as behind a tab), put code here to make it visible
				//(open the tab).
			}
		});
		$BV.ui('qa', 'show_questions', {
			doShowContent: function () {
				// If the container is hidden (such as behind a tab), put code here to make it visible
				//(open the tab).
			}
		});
	});
</script> -->

<template:page pageTitle="${pageTitle}">
	<vx-breadcrumb :breadcrumbs='${breadcrumbs}'></vx-breadcrumb>
	<div class="container-fluid pdp-page">
		<div class="row">
			<cms:pageSlot position="LeftContentSlot" var="comp" element="div" class="col-xs-12 col-sm-6 productDetailsPageSection1">
				<cms:component component="${comp}" element="div" class="productDetailsPageSection1-component"/>
			</cms:pageSlot>
			<cms:pageSlot position="RightContentSlot" var="comp" element="div" class="col-xs-12 col-sm-6 productDetailsPageSection2">
				<cms:component component="${comp}" element="div" class="productDetailsPageSection2-component"/>
			</cms:pageSlot>
		</div>
		<div class="row">
			<cms:pageSlot position="MiddleContentSlot" var="comp" element="div" class="productDetailsPageSection3">
				<cms:component component="${comp}" element="div" class="productDetailsPageSection3-component"/>
			</cms:pageSlot>
		</div>
		<div class="row">
			<cms:pageSlot position="BottomContentSlot" var="comp" element="div" class="col-xs-12 productDetailsPageSection4">
				<cms:component component="${comp}" element="div" class="productDetailsPageSection4-component"/>
			</cms:pageSlot>
		</div>
	</div>
</template:page>
