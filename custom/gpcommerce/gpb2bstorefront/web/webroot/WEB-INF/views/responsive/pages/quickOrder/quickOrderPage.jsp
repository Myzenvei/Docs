<%@ page trimDirectiveWhitespaces="true"%>
    <%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
        <%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

            <template:page pageTitle="${pageTitle}">
                <vx-quick-order :i18n="messages.quickOrder"></vx-quick-order>

                <cms:pageSlot position="MarketingMiddleContent" var="feature">
                    <cms:component component="${feature}" element="div" class="yComponentWrapper" />
                </cms:pageSlot>
                <cms:pageSlot position="BottomContentSlot" var="feature">
                    <cms:component component="${feature}" element="div" class="yComponentWrapper quick-order" />
                </cms:pageSlot>
            </template:page>