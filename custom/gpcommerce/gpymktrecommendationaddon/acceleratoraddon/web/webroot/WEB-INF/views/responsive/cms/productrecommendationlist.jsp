<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ulid" value="prodRecoUL${recoId}"/>

<jsp:useBean id="random" class="java.util.Random" scope="application"/>
<c:set var="divId" value="div${random.nextInt(1000)}"/>

<c:choose>
	<c:when test="${not empty productReferences}">
	    <div id="${divId}" class="carousel__component--headline">${fn:escapeXml(title)}</div>
	    <div id="${ulid}" class="carousel__component--carousel js-owl-carousel js-owl-default">
	        <c:forEach items="${productReferences}" var="product">
	        	<c:url value="${product.target.url}" var="productUrl"/>
	            <div class="carousel__item"
	            data-prodreco-item="prodRecoItem"
	            data-prodreco-item-code='${product.target.code}'
	            data-prodreco-item-component-id='${componentId}'>
					<div class="tile-icon wishlist-btn" tabindex="0" role="button" aria-label="Favorites">
						<button class="${product.target.isFavorite == true ? 'favorite active' : 'favorite' }" data-code="${product.target.code}" data-price='<format:fromPrice priceData="${product.target.price}"/>' data-name="${fn:escapeXml(product.target.name)}"> <!-- v-bind:class="{ 'active': isActive }" -->
							<%-- SVG for Employee Store --%>
							<svg width="20" height="20" viewBox="0 0 20 20" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
							class="heart gpemployee">
								<desc>Created with Sketch.</desc>
								<defs></defs>
								<g id="UI-Desktop" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
									<g id="PDP-Desktop-/-No-Subscription-/-R1" transform="translate(-1266.000000, -293.000000)" fill-rule="nonzero">
									<g id="Save-for-later" transform="translate(1266.000000, 292.000000)">
										<g id="heart-(5)">
										<path d="M23.4518256,7.5 C23.4518256,9.22443585 22.7665872,10.8781987 21.5471068,12.0971068 L12.7071068,20.9371068 C12.3165825,21.3276311 11.6834175,21.3276311 11.2928932,20.9371068 L2.45289321,12.0971068 C-0.0860186957,9.55819479 -0.0860186651,5.44180522 2.45289328,2.90289328 C4.99180522,0.363981335 9.10819479,0.363981304 11.6471068,2.90289322 L12,3.25578645 L12.3527273,2.9030592 C13.5718013,1.68341276 15.2255641,0.998174379 16.95,0.998174379 C18.6743576,0.998174379 20.3280486,1.68335058 21.5471068,2.9028932 C22.7666494,4.12195139 23.4518256,5.77564239 23.4518256,7.5 Z M12,18.8157864 L19.0728932,11.7428932 L20.1330592,10.6827273 C20.9774298,9.83875293 21.4518256,8.6938402 21.4518256,7.5 C21.4518256,6.3061598 20.9774298,5.16124707 20.1330592,4.31727273 C19.2887529,3.47257018 18.1438402,2.99817438 16.95,2.99817438 C15.7561598,2.99817438 14.6112471,3.47257018 13.7671068,4.31710678 L12.7071068,5.37710678 C12.3165825,5.76763107 11.6834175,5.76763107 11.2928932,5.37710678 L10.2328932,4.31710679 C8.47502984,2.55924345 5.62497021,2.55924347 3.86710684,4.31710684 C2.10924347,6.07497021 2.10924345,8.92502984 3.86710678,10.6828932 L12,18.8157864 Z"
											id="heart-outline" fill="#000000"></path>
										<path d="M12,18.8157864 L19.0728932,11.7428932 L20.1330592,10.6827273 C20.9774298,9.83875293 21.4518256,8.6938402 21.4518256,7.5 C21.4518256,6.3061598 20.9774298,5.16124707 20.1330592,4.31727273 C19.2887529,3.47257018 18.1438402,2.99817438 16.95,2.99817438 C15.7561598,2.99817438 14.6112471,3.47257018 13.7671068,4.31710678 L12.7071068,5.37710678 C12.3165825,5.76763107 11.6834175,5.76763107 11.2928932,5.37710678 L10.2328932,4.31710679 C8.47502984,2.55924345 5.62497021,2.55924347 3.86710684,4.31710684 C2.10924347,6.07497021 2.10924345,8.92502984 3.86710678,10.6828932 L12,18.8157864 Z"
											id="fill" fill="#FFFFFF"></path>
										</g>
									</g>
									</g>
								</g>
							</svg>
							<%-- SVG for Whitelable solutions --%>
							<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" class="heart whitelabel">
								<g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
									<g id="heart-outline" transform="translate(1.000000, 0.000000)" stroke="#999">
									<path id="fill" d="M22.3634,0.9763 C19.9054,0.9763 17.1654,1.9943 15.9764,5.2263 C15.8984,5.4373 15.5944,5.4293 15.5214,5.2163 C14.4144,1.9913 11.5714,0.9763 9.1154,0.9763 C4.4974,0.9763 0.7394,5.0363 0.7394,10.0263 C0.7394,12.3713 1.5684,14.5963 3.0754,16.2903 L15.0314,28.2673 C15.4224,28.6583 16.0564,28.6593 16.4484,28.2673 L28.1794,16.5343 C29.8234,14.8303 30.7394,12.5113 30.7394,10.0263 C30.7394,5.0363 26.9814,0.9763 22.3634,0.9763"
										fill="#ffffff"></path>
									</g>
								</g>
							</svg>
						</button>
						</div>
	                <a href="${productUrl}">
	                    <div class="carousel__item--thumb">
	                        <product:productPrimaryImage product="${product.target}" format="product"/>
	                    </div>
	                    <div class="carousel__item--name">${fn:escapeXml(product.target.name)}</div>
						<c:if test="${product.target.stock.stockLevelStatus == 'inStock'}">
							<div class="carousel__item--stock">In Stock</div>
						</c:if>
						<c:if test="${product.target.stock.stockLevelStatus == 'lowStock'}">
							<div class="carousel__item--stock">In Stock</div>
						</c:if>
						<c:if test="${product.target.stock.stockLevelStatus == 'outOfStock'}">
							<div class="error-text carousel__item--stock">Out of stock</div>
						</c:if>
						<div class="carousel__item--weblistprice">${product.target.weblistPrice.formattedValue}</div>
	                    <div class="carousel__item--price"><format:fromPrice priceData="${product.target.price}"/></div>
	                </a>
					<%--
((((product.stock.stockLevelStatus
&& product.stock.stockLevelStatus.code && product.stock.stockLevelStatus.code === ProductAvailability.OUT_OF_STOCK) ||
product.stock.stockLevelStatus === ProductAvailability.OUT_OF_STOCK ) && (!(product.stock.hasOwnProperty('nextAvailableDate')) ||
(product.stock.hasOwnProperty('nextAvailableDate') && product.stock.nextAvailableDate === null ))) || product.materialStatus === ProductAvailability.COMING_SOON)"
					 --%>
					<c:choose>
						<c:when test="${product.target.stock.stockLevelStatus == 'outOfStock'}">
							<div class="notify-me-placeholder">
								<button class="btn btn-tertiary notify-me-btn" data-code="${product.target.code}" data-price='<format:fromPrice priceData="${product.target.price}"/>' data-name="${fn:escapeXml(product.target.name)}">Notify</button>
							</div>
						</c:when>
						<c:otherwise>
							<div class="add-to-cart-placeholder">
								<button class="btn btn-primary" data-code="${product.target.code}" data-price='<format:fromPrice priceData="${product.target.price}"/>' data-name="${fn:escapeXml(product.target.name)}">Add To Cart</button>
							</div>
						</c:otherwise>
					</c:choose>
	            </div>
	        </c:forEach>
	    </div>
	</c:when>
	<c:otherwise>
		<component:emptyComponent/>
	</c:otherwise>
</c:choose>
