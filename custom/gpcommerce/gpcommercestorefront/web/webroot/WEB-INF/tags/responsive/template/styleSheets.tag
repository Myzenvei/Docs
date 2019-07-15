<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="cms" tagdir="/WEB-INF/tags/responsive/template/cms" %>


  <c:if test="${siteUid == 'gpemployee' || siteUid == 'b2cwhitelabel'}" >
    <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,700" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="https://cloud.typography.com/6228154/6448192/css/fonts.css" />
  </c:if>
  <c:if test="${siteUid == 'innovia'}" >
   <link href="https://fonts.googleapis.com/css?family=Montserrat:400,600,700&display=swap" rel="stylesheet">
   <link href="https://fonts.googleapis.com/css?family=Karla:400,700&display=swap" rel="stylesheet">
   </c:if>
  <c:if test="${siteUid == 'vanityfairnapkins'}" >
    <link rel="stylesheet" type="text/css" href="https://cloud.typography.com/6228154/7125212/css/fonts.css" />
  </c:if>
  <c:if test="${siteUid == 'copperandcrane'}" >
  	<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/assets/fonts/copper-crane/fonts/MyFontsWebfontsKit.css" />
  	<link rel="stylesheet" type="text/css" href="${contextPath}/_ui/addons/gpassistedservicesstorefront/responsive/common/css/gpassistedservicesstorefront.css">
  </c:if>
  <c:if test="${siteUid == 'sparkle'}" >
  	<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/assets/fonts/sparkle/MyFontsWebfontsKit.css" />
  </c:if>
  <c:if test="${siteUid == 'brawny'}" >
  	<link rel="stylesheet" type="text/css" href="https://use.typekit.net/cjl6dbo.css">
  </c:if>
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
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/dixie.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'dixie'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/dixie.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/dixie.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'mardigras'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/mardi-gras.styles.min.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/mardi-gras.css">
			</c:when>
			<c:when test="${siteUid == 'vanityfairnapkins'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/vanity-fair.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/vanity-fair.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'copperandcrane'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/copper-crane.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/copper-crane.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'b2cwhitelabel'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/b2c-white-label.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/b2c-white-label.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'gpemployee'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-employee.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-employee.styles.min.css">
			</c:when>
			<c:when test="${siteUid == 'gpxpress'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-xpress.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-xpress.styles.min.css">
			</c:when>

				<c:when test="${siteUid == 'innovia'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/innovia.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/innovia.styles.min.css">
	</c:when>
			<c:when test="${siteUid == 'brawny'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/brawny.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/brawny.styles.min.css">

			</c:when>
			<c:when test="${siteUid == 'sparkle'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/sparkle.css">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/sparkle.styles.min.css">
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
			<c:when test="${siteUid == 'gpxpress'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-xpress.css">
			</c:when>
			<c:when test="${siteUid == 'dixie'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/dixie.css">
			</c:when>
			<c:when test="${siteUid == 'mardigras'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/mardi-gras.css">
			</c:when>
			<c:when test="${siteUid == 'vanityfairnapkins'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/vanity-fair.css">
			</c:when>
			<c:when test="${siteUid == 'copperandcrane'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/copper-crane.css">
			</c:when>
			<c:when test="${siteUid == 'b2cwhitelabel'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/b2c-white-label.css">
			</c:when>
			<c:when test="${siteUid == 'gpemployee'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-employee.css">
			</c:when>
			<c:when test="${siteUid == 'brawny'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/brawny.css">
			</c:when>
			<c:when test="${siteUid == 'sparkle'}">
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/sparkle.css">
			</c:when>
			<c:otherwise>
				<link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/gp-pro.css">
			</c:otherwise>
		</c:choose>

		<%-- <link rel="stylesheet" type="text/css" href="${commonResourcePath}/dist/static/css/bundle.component.min.css"> --%>


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
