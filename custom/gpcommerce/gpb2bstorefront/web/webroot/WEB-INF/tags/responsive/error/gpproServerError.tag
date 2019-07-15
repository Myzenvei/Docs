<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="url" value="${pageContext.request.requestURL}"/>
<c:set var="contextPath" value="${fn:substringBefore(url, pageContext.request.requestURI)}"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
  <title>GP PRO</title>
  <link rel="stylesheet" type="text/css" href="https://cloud.typography.com/6228154/6448192/css/fonts.css">
  <link rel="icon" href="/_ui/responsive/theme-alpha/images/favicon.ico" sizes="16x16" type="image/png">
  <style type="text/css">
    html,
    body {
      margin: 0;
      padding: 0;
    }

    .container {
      display: flex;
      flex-direction: column;
      position: absolute;
      width: 100%;
      height: 100vh;
    }

    header {
      height: 72px;
      background-color: #167BB4;
      align-self: flex-start;
      width: 100%;
      display: flex;
      align-items: center;
    }

    header .logo {
      height: 47px;
      width: 47px;
      padding-left: 40px;
    }

    header .logo-text {
      color: #fff;
      padding-left: 10px;
      font-weight: bold;
      font-family: Gotham A, Gotham B, Helvetica, Roboto, Arial, sans-serif;
    }

    footer {
      height: 64px;
      background-color: #DFDFDF;
      position: fixed;
      bottom: 0;
      left:0;
      width: 100%;
      display: flex;
      align-items: center;
    }

    footer .page-footer .copywrite-text {
      padding-left: 40px;
      color: #747678;
      font-family: Gotham A, Gotham B, Helvetica, Roboto, Arial, sans-serif;
    }

    .grey-container {
      background-color: #FAFAFA;
      padding: 30px;
      margin: 40px;
    }
    
    .logo-text {
      color: white;
      font-family: Gotham A, Gotham B, Helvetica, Roboto, Arial, sans-serif;
      font-size: 18px;
    }

    .site-heading {
      color: #222222;
      font-family: Gotham A, Gotham B, Helvetica, Roboto, Arial, sans-serif;
      font-size: 24px;
      font-weight: 300;
      margin-bottom: 40px;
    }

    .error-msg {
      margin-bottom: 20px;
      color: #222222;
      font-family: Gotham A, Gotham B, Helvetica, Roboto, Arial, sans-serif;
      font-size: 16px;
      font-weight: bold;
      letter-spacing: 0.4px;
    }

    .immediate-assitance {
      color: #222222;
      font-family: Gotham A, Gotham B, Helvetica, Roboto, Arial, sans-serif;
      font-size: 14px;
      font-weight: 300;
      letter-spacing: 0.4px;
      line-height: 18px;
    }

  </style>
</head>

<body>
  <div class='container'>
    <header>
      <a href="/"><img class='logo' src="${contextPath}/gp/_ui/responsive/common/images/gppro-logo.svg"></a>
      <span class='logo-text'><spring:theme code="text.servererror.company.logo" text="Georgia-Pacific"/></span>
    </header>
    <div class='grey-container'>
      <div class='site-heading'>
        <spring:theme code="text.servererror.oops.message" text="Oops, something went wrong!"/>
      </div>
      <div class='error-msg'>
       <spring:theme code="text.servererror.errorfound" text="500 Error Found"/> 
      </div>
      <div class='immediate-assitance'>
       <spring:theme code="text.servererror.technical.issue" text="Sorry, we're experiencing some technical problems. Please try back later."/>
      </div>
    </div>
    <footer>
      <div class='page-footer'>
        <span class='copywrite-text'>&copy;&nbsp;<spring:theme code="text.servererror.copyright.message" arguments="${currentYear}" argumentSeparator="$$"/></span>
      </div>
    </footer>
  </div>
</body>

</html>
