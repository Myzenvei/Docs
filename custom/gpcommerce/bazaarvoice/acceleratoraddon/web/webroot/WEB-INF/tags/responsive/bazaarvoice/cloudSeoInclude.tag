<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@tag import="com.bazaarvoice.seo.sdk.config.*"%>
<%@tag import="com.bazaarvoice.seo.sdk.model.*"%>
<%@tag import="com.bazaarvoice.seo.sdk.*"%>
<%@ attribute name="product" required="true"
			  type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="zone" value="${cmsSite.bvConfig.zone}" />
<c:set var="normalizedZone" value="${fn:replace(zone,  ' ', '_')}" />

<c:set var="environmentType"
	   value="${cmsSite.bvConfig.environment.code}" />
<c:set var="environment" value="${environmentType}" />
<jsp:useBean id="environment" class="java.lang.String" />

<c:set var="seoKey" value="${cmsSite.bvConfig.seoKey}" />
<jsp:useBean id="seoKey" class="java.lang.String" />

<c:set var="displayCode" value="${normalizedZone}-${cmsSite.bvLocale}" />
<jsp:useBean id="displayCode" class="java.lang.String" />

<c:url value="${product.url}" var="productUrl" />
<jsp:useBean id="productUrl" class="java.lang.String" />

<s:eval expression="T(com.bazaarvoice.hybris.utils.BazaarVoiceUtils).ReplaceUnsupportedCharacters(product.code)" var="productCode" />
<jsp:useBean id="productCode" class="java.lang.String" />

<c:set var="websiteUrl" value="${websiteUrl}" />
<jsp:useBean id="websiteUrl" class="java.lang.String" />


<%


	BVConfiguration _bvConfig = new BVSdkConfiguration();
	String sBvOutputSummary = "";
	String sBvOutputReviews = "";
	String sBvOutputQA = "";

	_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true"); //Use this as a kill switch.  The true/false string for this is often passed in from a common utility class.
	_bvConfig.addProperty(BVClientConfig.EXECUTION_TIMEOUT, "1500"); //Adjust as desired.  Values below 1000 are not recommended.
	//_bvConfig.addProperty(BVClientConfig.EXECUTION_TIMEOUT_BOT, "2000"); //Adjust this as required for bot functionality.
	//_bvConfig.addProperty(BVClientConfig.CRAWLER_AGENT_PATTERN, "msnbot|google|teoma|bingbot|yandexbot|yahoo"); //Use this to override the default list of search engine bot user agents

	//the following 3 lines must be updated to not use sample configurations.
	_bvConfig.addProperty(BVClientConfig.CLOUD_KEY, seoKey); // Each client will get this value from Bazaarvoice, so this key needs to be a configuration setting.
	_bvConfig.addProperty(BVClientConfig.STAGING,
			(environment != null && environment.equals("Production")) ? "false"  : "true"); //Set to true for staging environment data.
	_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER,
			displayCode); //Get this value from BV. This is also known as Display Code.

	//Create BVParameters for each injection.  If the page contains multiple injections, for example,
	//reviews and questions, set unique parameters for each injection.
	BVParameters _bvParam = new BVParameters();
	_bvParam.setUserAgent(request.getHeader("User-Agent"));
	String queryString = request.getQueryString();
	String additionalParamentrs = (queryString != null) ? ("?" + queryString)
			: "";
	String pageUrl = websiteUrl + productUrl + additionalParamentrs;
	_bvParam.setPageURI(pageUrl); //This should be URI/URL of the current page with all URL parameters.
	_bvParam.setContentType(ContentType.REVIEWS); //Set to REVIEWS, QUESTIONS, etc.

	_bvParam.setSubjectType(SubjectType.PRODUCT);

	//the following 2 lines must be configured for each page.
	String baseUrl = websiteUrl + productUrl;
	_bvParam.setBaseURI(baseUrl); //Insert the URI/URL of the page. This is typically the canonical URL.  The SDK will append pagination parameters to this URI/URL to create search-friendly pagination links.
	_bvParam.setSubjectId(productCode); //Insert the product ID

	BVUIContent _bvOutput1 = new BVManagedUIContent(_bvConfig);
	//String sBvOutputReviews = _bvOutput.getContent(_bvParam);  //This method returns both the reviews markup and aggregate rating into a single string.  Use this method if there is no summary div.
	sBvOutputSummary = _bvOutput1.getAggregateRating(_bvParam); //This method returns only the aggregate rating markup.
	sBvOutputReviews = _bvOutput1.getReviews(_bvParam); //This method returns only the reviews markup with no aggregate rating markup.

	_bvParam.setContentType(ContentType.QUESTIONS);
	BVUIContent _bvOutput2 = new BVManagedUIContent(_bvConfig);

	sBvOutputQA= _bvOutput2.getContent(_bvParam);

%>
<c:set var="sBvOutputSummary" value="<%=sBvOutputSummary%>" scope="request"/>
<c:set var="sBvOutputReviews" value="<%=sBvOutputReviews%>" scope="request" />
<c:set var="sBvOutputQA" value="<%=sBvOutputQA%>" scope="request" />
