<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="cms" tagdir="/WEB-INF/tags/responsive/template/cms" %>

<c:choose>
  <c:when test="${siteUid == 'gppro'}">
    <link rel="stylesheet" type="text/css" href="https://cloud.typography.com/6228154/6448192/css/fonts.css" />
  </c:when>
  <c:when test="${siteUid == 'dixie' || siteUid == 'b2bwhitelabel'}">
    <link rel="stylesheet" type="text/css" href="https://cloud.typography.com/6228154/7892812/css/fonts.css" />
  </c:when>
  <c:when test="${siteUid == 'gpxpress'}">
   <link rel="stylesheet" type="text/css" href="https://cloud.typography.com/6228154/7965012/css/fonts.css" />
  </c:when>

</c:choose>  
<c:choose>
	<c:when test="${wro4jEnabled}">
		<%-- <link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/all_responsive.css" />
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/${themeName}_responsive.css" /> --%>
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/addons_responsive.css" />
		<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/bundle.common.min.css">
		<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/assets/lib/yForms/sapContentPage.css"/>
		
		<%-- conditions to load banner specific css--%>
		<c:choose>
			<c:when test="${siteUid == 'gppro'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-pro.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-pro.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'dixie'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/dixie.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/dixie.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'b2bwhitelabel'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/b2b-white-label.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/b2b-white-label.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'gpxpress'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-xpress.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-xpress.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'mardigras'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/mardi-gras.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/mardi-gras.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'vanityfair'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/vanity-fair.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/vanity-fair.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'gpemployee'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-employee.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-employee.styles.min.css">
			</c:when>
			<c:otherwise>
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-pro.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-pro.styles.min.css">
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<%-- Theme CSS files --%>
		<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/style.css"/>
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/bundle.common.min.css">
		<%-- conditions to load banner specific css--%>
		<c:choose>
			<c:when test="${siteUid == 'gppro'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-pro.css">
			</c:when>
			<c:when test="${siteUid == 'dixie'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/dixie.css">
			</c:when>
			<c:when test="${siteUid == 'b2bwhitelabel'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/b2b-white-label.css">
			</c:when>
			<c:when test="${siteUid == 'gpxpress'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-xpress.css">
			</c:when>
			<c:when test="${siteUid == 'mardigras'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/mardi-gras.css">
			</c:when>
			<c:when test="${siteUid == 'vanityfair'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/vanity-fair.css">
			</c:when>
			<c:when test="${siteUid == 'gpemployee'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-employee.css">
			</c:when>
			<c:otherwise>
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-pro.css">
			</c:otherwise>
		</c:choose>
		<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/bundle.common.min.css">



		<%--  AddOn Common CSS files --%>
		<c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
			<link rel="stylesheet" type="text/css" media="all" href="${addOnCommonCss}"/>
		</c:forEach>
	</c:otherwise>
</c:choose>

<%--  AddOn Theme CSS files --%>
<c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnThemeCss}"/>
</c:forEach>

<%-- <link rel="stylesheet" href="${commonResourcePath}/blueprint/print.css" type="text/css" media="print" />
<style type="text/css" media="print">
	@IMPORT url("${commonResourcePath}/blueprint/print.css");
</style>
 --%>

<cms:previewCSS cmsPageRequestContextData="${cmsPageRequestContextData}" />
