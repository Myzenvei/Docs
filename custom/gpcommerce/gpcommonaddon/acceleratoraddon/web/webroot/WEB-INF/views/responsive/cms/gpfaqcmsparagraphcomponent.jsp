<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<vx-accordion-group :accordion-group-data="'${ycommerce:encodeHTML(content)}'"></vx-accordion-group>