<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>

<vx-live-chat :i18n='messages.accessCustomerService.liveChat' :is-live-chat-enabled='${showLiveChat}'></vx-live-chat>