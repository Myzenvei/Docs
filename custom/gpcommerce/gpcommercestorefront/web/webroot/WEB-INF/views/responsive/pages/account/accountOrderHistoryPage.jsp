<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
       <c:when test="${siteUid == 'gpemployee'}">
			<div class="pr-xs-5">
       </c:when>
       <c:otherwise>
			<div class="pr-md-5">
       </c:otherwise>
</c:choose>
				<vx-order-history :i18n="messages.manageTransactionHistory.orderHistory"></vx-order-history>
			</div>
