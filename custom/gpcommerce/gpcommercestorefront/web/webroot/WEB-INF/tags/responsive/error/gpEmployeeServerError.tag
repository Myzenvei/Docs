<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="url" value="${pageContext.request.requestURL}"/>
<c:set var="contextPath" value="${fn:substringBefore(url, pageContext.request.requestURI)}"/>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />

<html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
  <title>Georgia-Pacific Consumer Products Employee Store</title>
  <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,700" rel="stylesheet">
  <link rel="icon" href="/_ui/responsive/theme-alpha/images/employee/favicon.ico" sizes="16x16" type="image/png">
  <style type="text/css">
    html,
    body {
      margin: 0;
      padding: 0;
    }

    * {
      box-sizing: border-box;
    }

    .container {
      display: flex;
      align-items: center;
      flex-direction: column;
      position: absolute;
      width: 100%;
      height: 100vh;
      background-color: #f1f2f4;
    }

    header {
      height: 100px;
      background-color: #ffffff;
      align-self: flex-start;
      width: 100%;
      padding-left:40px;
    }

    .header-inner-wrapper {
      display: flex;
      align-items: center;
      max-width: 1200px;
      height: 100%;
    }

    header .logo {
      width: 230px;
      height: 100%;
    }

    footer {
      height: 64px;
      background-color: #ffffff;
      position: fixed;
      bottom: 0;
      left: 0;
      width: 100%;
    }

    .footer-inner-wrapper {
      display: flex;
      align-items: center;
      max-width: 1200px;
      height: 100%;
    }

    footer .copywrite-text {
      color: #353333;
      font-family: Poppins;
      padding-left: 40px;
    }

    .white-container {
      background-color: #ffffff;
      padding: 24px;
      width: 95%;
      border-radius: 4px;
      min-height: 330px;
      padding: 30px;
      margin: 40px;
      margin-top: 30px;
    }

    .site-heading {
      color: #353333;
      font-family: Poppins;
      font-size: 24px;
      font-weight: 700;
      margin-bottom: 16px;
    }

    .error-msg {
      margin-bottom: 8px;
      color: #353333;
      font-family: Poppins;
      font-size: 16px;
      font-weight: 300;
      letter-spacing: 0.4px;
    }

    .immediate-assitance {
      color: #353333;
      font-family: Poppins;
      font-size: 16px;
      font-weight: 300;
      letter-spacing: 0.4px;
    }

    @media only screen and (max-width: 1199px) {
      .white-container {
        width: 80%;
      }
    }

    @media only screen and (max-width: 767px) {
      .site-heading {
        font-size: 21px;
      }
      .error-msg,
      .immediate-assitance {
        font-size: 14px;
      }
    }
  </style>
</head>

<body>
  <div class='container'>
    <header>
      <div class="header-inner-wrapper">
        <a href="/"><img class='logo' src="${contextPath}/_ui/responsive/common/images/employee-logo.svg"></a>
      </div>
    </header>
    <div class='white-container'>
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
      <div class="footer-inner-wrapper">
        <p class='copywrite-text'>&copy;&nbsp;<spring:theme code="text.servererror.copyright.message" arguments="${currentYear}" argumentSeparator="$$"/></p>
      </div>
    </footer>
  </div>
</body>
</html>
