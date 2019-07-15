<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<vx-breadcrumb :breadcrumbs='${breadcrumbs}'></vx-breadcrumb>
<vx-order-details :i18n="messages.manageTransactionHistory" :contact-number="'${contactNumber}'"></vx-order-details>