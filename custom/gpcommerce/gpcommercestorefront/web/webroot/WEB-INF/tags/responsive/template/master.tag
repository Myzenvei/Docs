<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="metaDescription" required="false" %>
<%@ attribute name="metaKeywords" required="false" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/shared/analytics" %>
<%@ taglib prefix="addonScripts" tagdir="/WEB-INF/tags/responsive/common/header" %>
<%@ taglib prefix="generatedVariables" tagdir="/WEB-INF/tags/shared/variables" %>
<%@ taglib prefix="debug" tagdir="/WEB-INF/tags/shared/debug" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="htmlmeta" uri="http://hybris.com/tld/htmlmeta"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<!DOCTYPE html>
<html lang="${fn:escapeXml(currentLanguage.isocode)}">

<head>
    <c:choose>
	    <c:when test="${not gtmId}">
        <!-- Google Tag Manager -->
        <script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
        new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
        j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
        'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
        })(window,document,'script','dataLayer','${gtmId}');</script>
        <!-- End Google Tag Manager -->
        </c:when>
	    <c:otherwise>
        </c:otherwise>
    </c:choose>
    
<%--  The below code is to enable Krux in Mardigras when 
		a. gp.mardigras.krux.enable=true which is available in local.properties
		b. when siteUid = mardigras
--%>	
		<c:set var="krux" value="false" />
		<c:if test="${fn:length(kruxEnabledSites) > 0}">
		<c:forEach var="item" items="${kruxEnabledSites}">
			<c:if test="${item eq siteUid}">
		    	<c:set var="krux" value="true" />
		  	</c:if>
		</c:forEach>
		</c:if>
	   <c:choose>
    	<c:when test="${krux}">
	    	<!-- BEGIN Krux ControlTag for "Mardi Gras" -->
			<script class="kxct" data-id="rb1keg13b" data-timing="async" data-version="3.0" type="text/javascript">
			  window.Krux||((Krux=function(){Krux.q.push(arguments)}).q=[]);
			  (function(){
			    var k=document.createElement('script');k.type='text/javascript';k.async=true;
			    k.src=(location.protocol==='https:'?'https:':'http:')+'//cdn.krxd.net/controltag/rb1keg13b.js';
			    var s=document.getElementsByTagName('script')[0];s.parentNode.insertBefore(k,s);
			  }());
			</script>
			<!-- END Krux ControlTag -->
    	</c:when>
    </c:choose> 
    <title>
        ${not empty pageTitle ? pageTitle : not empty cmsPage.title ? fn:escapeXml(cmsPage.title) : 'Accelerator Title'}
    </title>
    <%-- Meta Content --%>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta property="og:title" content="${productName}" />
    <meta property="og:image" content="${productImageUrl}" />
    <meta property="og:url" content="${productUrl}" />
    <meta property="og:description" content="${productDesc}" />
    <meta name="twitter:url" content="${productUrl}">
    <meta name="twitter:title" content="${productName}">
    <meta name="twitter:image" content="${productImageUrl}" />
    <meta name="twitter:card" content="summary">
    <meta name="twitter:description" content="${productDesc}" />
    
    <htmlmeta:meta items="${metatags}" />

	<%-- Favourite Icon --%>
   
    <c:choose>
        <%-- if empty webroot, skip originalContextPath, simply use favIconPath --%>
        <c:when test="${fn:length(originalContextPath) eq 1}">
            <link rel="shortcut icon" type="image/x-icon" media="all" href="${faviconImageUrl}" />
        </c:when>
        <c:otherwise>
            <link rel="shortcut icon" type="image/x-icon" media="all" href="${originalContextPath}${faviconImageUrl}" />
        </c:otherwise>
    </c:choose>



    <c:if test="${not empty canonicalUrl}">
        <link rel="canonical" href="${canonicalUrl}" />
    </c:if>
		
    <%-- CSS Files Are Loaded First as they can be downloaded in parallel --%>
    <template:styleSheets />

    <%-- Inject any additional CSS required by the page --%>
    <jsp:invoke fragment="pageCss" />
    <analytics:analytics />
    <generatedVariables:generatedVariables />
    
</head>

<body class="${pageBodyCssClasses} ${cmsPageRequestContextData.liveEdit ? ' yCmsLiveEdit' : ''} language-${fn:escapeXml(currentLanguage.isocode)}">

    <c:choose>
	    <c:when test="${not gtmId}">
        <!-- Google Tag Manager (noscript) -->
        <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=${gtmId}" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
        <!-- End Google Tag Manager (noscript) -->
        </c:when>
	    <c:otherwise>
        </c:otherwise>
    </c:choose>
   
    <%-- Inject the page body here --%>
    <jsp:doBody />

    <form name="accessiblityForm">
        <input type="hidden" id="accesibility_refreshScreenReaderBufferField" name="accesibility_refreshScreenReaderBufferField" value="" />
    </form>
    <div id="ariaStatusMsg" class="skip" role="status" aria-relevant="text" aria-live="polite"></div>

    <%-- Load JavaScript required by the site --%>
    <template:javaScript />

    <%-- Inject any additional JavaScript required by the page --%>
    <jsp:invoke fragment="pageScripts" />

    <%-- Inject CMS Components from addons using the placeholder slot--%>
    <addonScripts:addonScripts />


</body>

<debug:debugFooter />

</html>