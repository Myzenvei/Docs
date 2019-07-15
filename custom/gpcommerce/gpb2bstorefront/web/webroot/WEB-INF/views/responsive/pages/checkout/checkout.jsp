<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<vx-checkout :i18n="messages.checkout" b2b-unit-level="${user.b2bUnitLevel}" guest-email='${guestEmail}' apple-pay-merchant-name='${applePayMerchantName}' ></vx-checkout>
