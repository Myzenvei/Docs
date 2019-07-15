<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" tagdir="/WEB-INF/tags/responsive/template/cms" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>

<c:url value="/" var="siteRootUrl"/>
<template:javaScriptVariables/>

<c:choose>
	<c:when test="${wro4jEnabled}">
		<script type="text/javascript" src="${commonResourcePath}/js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" src="${contextPath}/wro/all_responsive.js"></script>
	  	<script type="text/javascript" src="${contextPath}/wro/addons_responsive.js"></script>
		<script type="text/javascript" src="${contextPath}/_ui/addons/hybrisanalyticsaddon/shared/common/js/piwik-min.js"></script>
		
		<!-- Piwik -->
		<script type="text/javascript">

			var analyticsConfig;
			if(typeof ACC != 'undefined'){
				analyticsConfig = ACC.addons.hybrisanalyticsaddon;
			}else{
				analyticsConfig = ACCMOB.addons.hybrisanalyticsaddon;
			}
			var piwikTrackerUrl = "${contextPath}/events"; // Use Piwik HTTPS Endpoint by default
			var piwikSiteId = "${piwikSiteId}";
			var sessionId = "${SESSION_ID}";

			if(location.protocol != 'https:')	// if the current page does not use https protocol, use the normal HTTP endpoint:
			{
				piwikTrackerUrl = "${contextPath}/events";
			}

			var tracker = Piwik.getAsyncTracker(piwikTrackerUrl, piwikSiteId);

			tracker.setTrackerUrl(piwikTrackerUrl);
			tracker.setSiteId(piwikSiteId);
			tracker.setRequestMethod('POST');
			tracker.setRequestContentType('application/json; charset=UTF-8');

			var processPiwikRequest = (function (request) {
				try {
					var pairs = request.split('&');

					var requestParametersArray = {};
					for (index = 0; index < pairs.length; ++index) {
						var pair = pairs[index].split('=');
						requestParametersArray[pair[0]] = decodeURIComponent(pair[1] || '');
					}
					requestParametersArray['sessionId'] = sessionId;

					return JSON.stringify(requestParametersArray);
				} catch (err) {
					return request;
				}
			});

			tracker.setCustomRequestProcessing(processPiwikRequest);

			var hybrisAnalyticsPiwikPlugin = (function() {
				function _getEventtypeParam(suffix) { return '&eventtype=' + suffix; }
				function ecommerce() { return _getEventtypeParam("ecommerce"); }
				function event() { return _getEventtypeParam("event"); }
				function goal() { return _getEventtypeParam("goal"); }
				function link() { return _getEventtypeParam("link"); }
				function load() { return _getEventtypeParam("load"); }
				function log() { return _getEventtypeParam("log"); }
				function ping() { return _getEventtypeParam("ping"); }
				function run() { return _getEventtypeParam("run"); }
				function sitesearch() { return _getEventtypeParam("sitesearch"); }
				function unload() { return _getEventtypeParam("unload"); }

				return {
					ecommerce: ecommerce,
					event : event,
					goal : goal,
					link : link,
					load : load,
					log : log,
					ping: ping,
					sitesearch : sitesearch,
					unload : unload
				};
			})();
			tracker.addPlugin('hybrisAnalyticsPiwikPlugin', hybrisAnalyticsPiwikPlugin);
			// Hybris Analytics specifics - END


			<c:choose>
				<c:when test="${pageType == 'PRODUCT'}">
					//View Product event
					<c:set var="categories" value="" />
						<c:forEach items="${product.categories}" var="category">
						  <c:set var="categories">${categories},'${fn:escapeXml(category.code)}'</c:set>
						</c:forEach>
						tracker.setEcommerceView('${fn:escapeXml(product.code)}',  // (required) SKU: Product unique identifier
										'${fn:escapeXml(product.name)}',  // (optional) Product name
										[${fn:substringAfter(categories, ',')}],  // (optional) Product category, or array of up to 5 categories
										'${product.price.value}');

						tracker.trackPageView('ViewProduct');  //Do we really need this ??
				</c:when>



				<c:when test="${pageType == 'CATEGORY'}">
				//View category - Start
					<c:choose>
						<c:when test="${searchPageData.pagination.totalNumberOfResults > 0}">
							<c:if test="${not empty breadcrumbs}">
								tracker.trackSiteSearch('${searchPageData.freeTextSearch}',  // Search keyword searched for
										'${fn:escapeXml(categoryCode)}:${fn:escapeXml(categoryName)}',  // Search category selected in your search engine. If you do not need this, set to false
										'${searchPageData.pagination.totalNumberOfResults}',// Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
										tracker.setCustomData('categoryName', '${fn:escapeXml(categoryName)}'));
							</c:if>
						</c:when>
						<c:otherwise>
								tracker.trackSiteSearch('${searchPageData.freeTextSearch}',  // Search keyword searched for
										false,  // Search category selected in your search engine. If you do not need this, set to false
										'0'  // Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
								);
						</c:otherwise>
					</c:choose>
				</c:when>



				<c:when test="${pageType == 'PRODUCTSEARCH'}">
				//View Product search - START
					<c:choose>
						<c:when test="${searchPageData.pagination.totalNumberOfResults > 0}">
							<c:if test="${not empty breadcrumbs}">
								<c:set var="categories" value="" />
								<c:forEach items="${breadcrumbs}" var="breadcrumb">
									<c:set var="categories">${categories},'${fn:escapeXml(breadcrumb.name)}'</c:set>
								</c:forEach>
								tracker.trackSiteSearch('${searchPageData.freeTextSearch}',  // Search keyword searched for
										[${fn:substringAfter(categories, ',')}],  // Search category selected in your search engine. If you do not need this, set to false
										'${searchPageData.pagination.totalNumberOfResults}'  // Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
								);
							</c:if>
						</c:when>
						<c:otherwise>
							tracker.trackSiteSearch('${searchPageData.freeTextSearch}',  // Search keyword searched for
									false,  // Search category selected in your search engine. If you do not need this, set to false
									'0'  // Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
							);
						</c:otherwise>
					</c:choose>
				</c:when>

			<c:when test="${pageType == 'ORDERCONFIRMATION'}">
					<c:forEach items="${orderData.entries}" var="entry">
						tracker.setCustomVariable(1,"ec_id","${fn:escapeXml(orderData.code)}","page");
						<c:forEach items="${entry.product.categories}" var="category">
							<c:set var="categories">${categories},'${fn:escapeXml(category.code)}'</c:set>
						</c:forEach>
						tracker.setEcommerceView('${fn:escapeXml(entry.product.code)}',  // (required) SKU: Product unique identifier
								'${fn:escapeXml(entry.product.name)}',  // (optional) Product name
								[${fn:substringAfter(categories, ',')}],  // (optional) Product category. You can also specify an array of up to 5 categories eg. ["Books", "New releases", "Biography"]
								'${entry.product.price.value}'  // (recommended) Product price
						);
						tracker.addEcommerceItem('${fn:escapeXml(entry.product.code)}',  // (required) SKU: Product unique identifier
								'${fn:escapeXml(entry.product.name)}', // (optional) Product name
								[${fn:substringAfter(categories, ',')}],  // (optional) Product category. You can also specify an array of up to 5 categories eg. ["Books", "New releases", "Biography"]
								'${entry.product.price.value}',  // (recommended) Product price
								'${entry.quantity}'.toString()  // (optional, default to 1) Product quantity
						);

					</c:forEach>
					tracker.trackEcommerceOrder('${fn:escapeXml(orderData.code)}',  // (required) Unique Order ID
							'${orderData.totalPrice.value}',  // (required) Order Revenue grand total (includes tax, shipping, and subtracted discount)
							'${orderData.totalPrice.value - orderData.deliveryCost.value}',  // (optional) Order sub total (excludes shipping)
							'${orderData.totalTax.value}',  // (optional) Tax amount
							'${orderData.deliveryCost.value}',  // (optional) Shipping amount
							false  // (optional) Discount offered (set to false for unspecified parameter)
					);

					tracker.setCustomVariable(1,"ec_id","${fn:escapeXml(orderData.code)}","page");

					tracker.trackEvent('checkout', 'success', '','');
					tracker.trackPageView(); // we recommend to leave the call to trackPageView() on the Order confirmation page
			</c:when>
				<c:otherwise>
					//Otherwise default to page view event
					tracker.trackPageView('ViewPage');
				</c:otherwise>
			</c:choose>

			tracker.enableLinkTracking();
			// handlers and their subscription for cart events


			function trackAddToCart_piwik(productCode, quantityAdded, cartData) {
				tracker.setEcommerceView(String(cartData.productCode),  // (required) SKU: Product unique identifier
						cartData.productName,  // (optional) Product name
						[ ], // (optional) Product category, string or array of up to 5 categories
						quantityAdded+""  // (optional, default to 1) Product quantity
				);
				tracker.addEcommerceItem(String(cartData.productCode),  // (required) SKU: Product unique identifier
						cartData.productName,  // (optional) Product name
						[ ], // (optional) Product category, string or array of up to 5 categories
						cartData.productPrice+"", // (recommended) Product price
						quantityAdded+""  // (optional, default to 1) Product quantity
				);

				if (!cartData.cartCode)
				{
					cartData.cartCode="${fn:escapeXml(cartData.code)}";
				}
				tracker.setCustomVariable(1,"ec_id",cartData.cartCode,"page");
				tracker.trackEcommerceCartUpdate(
						'0'  // (required) Cart amount
				);
			}

			function trackBannerClick_piwik(url) {
				tracker.setCustomVariable(1,"bannerid",url,"page");
				tracker.trackLink(url, 'banner');
			}

			function trackContinueCheckoutClick_piwik() {
				tracker.setCustomVariable(1,"ec_id","${fn:escapeXml(cartData.code)}","page");
				tracker.trackEvent('checkout', 'proceed', '','');
			}

		    function trackShowReview_piwik() {
		        tracker.trackEvent('review', 'view', '', '');
		    }

			function trackUpdateCart_piwik(productCode,initialQuantity,newQuantity,cartData) {
				if (initialQuantity != newQuantity) {
					trackAddToCart_piwik(productCode, newQuantity,cartData);
				}
			}

			function trackRemoveFromCart_piwik(productCode, initialQuantity,cartData) {
				trackAddToCart_piwik(productCode, 0, cartData);
			}

			window.mediator.subscribe('trackAddToCart', function(data) {
				if (data.productCode && data.quantity) {
					trackAddToCart_piwik(data.productCode, data.quantity,data.cartData);
				}
			});

			window.mediator.subscribe('trackUpdateCart', function(data) {
				if (data.productCode && data.initialCartQuantity && data.newCartQuantity) {
					trackUpdateCart_piwik(data.productCode,data.initialCartQuantity,data.newCartQuantity,data);
				}
			});

			window.mediator.subscribe('trackRemoveFromCart', function(data) {
				if (data.productCode && data.initialCartQuantity) {
					trackRemoveFromCart_piwik(data.productCode, data.initialCartQuantity,data);
				}
			});

		    window.mediator.subscribe('trackShowReviewClick', function() {
		        trackShowReview_piwik();
		    });

			$('.simple-banner').click(function(){
		 		trackBannerClick_piwik( $(this).find("a").prop('href'));
			});

			$('.js-continue-checkout-button').click(function(){
				trackContinueCheckoutClick_piwik();
			});

		</script>

		<noscript><p><img src="${piwikTrackerUrl}?idsite=${piwikSiteId}" style="border:0;" alt="" /></p></noscript>
		<script type='text/javascript' src="${livechatDeployUrl}"></script>
		<%--Bootstrap Js  --%>
		<script type="text/javascript" src="${commonResourcePath}/bootstrap/dist/js/bootstrap.min.js"></script>
		<%--Vue Js Build Files  --%>
		<script type="text/javascript" src="${commonResourcePath}/dist/static/js/manifest.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/dist/static/js/vendor.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/dist/static/js/bundle.common.min.js"></script>
		<%-- conditions to load banner specific js--%>
		<c:choose>
			<c:when test="${siteUid == 'gppro'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-pro.js"></script>
			</c:when>
			<c:when test="${siteUid == 'dixie'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/dixie.js"></script>
			</c:when>
			<c:when test="${siteUid == 'mardigras'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/mardi-gras.js"></script>
			</c:when>
			<c:when test="${siteUid == 'vanityfairnapkins'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/vanity-fair.js"></script>
			</c:when>
			<c:when test="${siteUid == 'copperandcrane'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/copper-crane.js"></script>
			</c:when>
			<c:when test="${siteUid == 'b2cwhitelabel'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/b2c-white-label.js"></script>
			</c:when>
			<c:when test="${siteUid == 'gpemployee'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-employee.js"></script>
			</c:when>
			<c:when test="${siteUid == 'gpxpress'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-xpress.js"></script>
			</c:when>
			<c:when test="${siteUid == 'innovia'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/innovia.js"></script>
		</c:when>
			<c:when test="${siteUid == 'brawny'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/brawny.js"></script>
			</c:when>
			<c:when test="${siteUid == 'sparkle'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/sparkle.js"></script>
			</c:when>
			<c:otherwise>
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-pro.js"></script>
			</c:otherwise>
		</c:choose>

	</c:when>
	<c:otherwise>
		<%-- jquery --%>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery-3.2.1.min.js"></script>

		<%-- plugins --%>
		<script type="text/javascript" src="${commonResourcePath}/js/enquire.min.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/Imager.min.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.blockUI-2.66.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.colorbox-min.js"></script>
		<%-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.form.min.js"></script> --%>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.hoverIntent.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.pstrength.custom-1.2.0.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.syncheight.custom.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.tabs.custom.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery-ui-1.12.1.min.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.zoom.custom.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/owl.carousel.custom.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.tmpl-1.0.0pre.min.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.currencies.min.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.waitforimages.min.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/jquery.slideviewer.custom.1.2.js"></script>

		<%-- Custom ACC JS --%>

		<script type="text/javascript" src="${commonResourcePath}/js/acc.address.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.autocomplete.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.carousel.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.cart.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.cartitem.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.checkout.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.checkoutaddress.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.checkoutsteps.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.cms.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.colorbox.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.common.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.forgottenpassword.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.global.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.hopdebug.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.imagegallery.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.langcurrencyselector.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.minicart.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.navigation.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.order.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.paginationsort.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.payment.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.paymentDetails.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.pickupinstore.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.product.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.productDetail.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.quickview.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.ratingstars.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.refinements.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.sanitizer.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.silentorderpost.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.tabs.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.termsandconditions.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.track.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.storefinder.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.futurelink.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.productorderform.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.savedcarts.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.multidgrid.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.quickorder.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.quote.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.consent.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.cookienotification.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/js/acc.closeaccount.js"></script>

		<script type="text/javascript" src="${commonResourcePath}/js/acc.csv-import.js"></script>

		<script type="text/javascript" src="${commonResourcePath}/js/_autoload.js"></script>
		<%-- <script type="text/javascript" src="${commonResourcePath}/js/dist/build.js"></script> --%>
		<script type='text/javascript' src="${livechatDeployUrl}"></script>
		<%--Bootstrap Js  --%>
		<script type="text/javascript" src="${commonResourcePath}/bootstrap/dist/js/bootstrap.min.js"></script>
		<%--Vue Js Build Files  --%>
		<script type="text/javascript" src="${commonResourcePath}/dist/static/js/manifest.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/dist/static/js/vendor.js"></script>
		<script type="text/javascript" src="${commonResourcePath}/dist/static/js/bundle.common.min.js"></script>
		<%-- conditions to load banner specific js--%>
		<c:choose>
			<c:when test="${siteUid == 'gppro'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-pro.js"></script>
			</c:when>
			<c:when test="${siteUid == 'dixie'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/dixie.js"></script>
			</c:when>
			<c:when test="${siteUid == 'mardigras'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/mardi-gras.js"></script>
			</c:when>
			<c:when test="${siteUid == 'vanityfairnapkins'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/vanity-fair.js"></script>
			</c:when>
			<c:when test="${siteUid == 'copperandcrane'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/copper-crane.js"></script>
			</c:when>
			<c:when test="${siteUid == 'b2cwhitelabel'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/b2c-white-label.js"></script>
			</c:when>
			<c:when test="${siteUid == 'gpemployee'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-employee.js"></script>
			</c:when>
			<c:when test="${siteUid == 'gpxpress'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-xpress.js"></script>
			</c:when>

			<c:when test="${siteUid == 'innovia'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/innovia.js"></script>
			</c:when>
			<c:when test="${siteUid == 'brawny'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/brawny.js"></script>
			</c:when>
			<c:when test="${siteUid == 'sparkle'}">
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/sparkle.js"></script>
			</c:when>
			<c:otherwise>
				<script type="text/javascript" src="${commonResourcePath}/dist/static/js/gp-pro.js"></script>
			</c:otherwise>
		</c:choose>


		<%-- Cms Action JavaScript files --%>
		<c:forEach items="${cmsActionsJsFiles}" var="actionJsFile">
		    <script type="text/javascript" src="${commonResourcePath}/js/cms/${actionJsFile}"></script>
		</c:forEach>

		<%-- AddOn JavaScript files --%>
		<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
		    <script type="text/javascript" src="${addOnJavaScript}"></script>
		</c:forEach>

	</c:otherwise>
</c:choose>


<cms:previewJS cmsPageRequestContextData="${cmsPageRequestContextData}" />
