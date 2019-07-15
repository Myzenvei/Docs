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
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,600,700&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Karla:400,700&display=swap" rel="stylesheet">
  <link rel="icon" href="/_ui/responsive/theme-alpha/images/innovia/innovia_Favicon.png" sizes="16x16" type="image/png">
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
      background-color: #ffffff;
    }

    header {
      height: 48px;
      background-color: #1C6B76;
      align-self: flex-start;
      width: 100%;
      border: 1px solid #E7E7E7;
    }

    .header-inner-wrapper {
      display: flex;
      align-items: center;
      max-width: 1200px;
      height: 100%;
      margin: 0 auto;
    }
    .header-inner-wrapper a {
        padding-left: 16px;
    }
    header .logo {
      width: 75px;
      height: 32px;
    }

    footer {
      height: 64px;
      background-color: #FAFAFA;
      position: fixed;
      bottom: 0;
      width: 100%;
    }

    .footer-inner-wrapper {
      display: flex;
      align-items: center;
      max-width: 1200px;
      height: 100%;
      margin: 0 auto;

    }

    footer .copywrite-text {
      color: #747678;
      font-size: 14px;
      font-weight: 400;
      letter-spacing: 0.15px;
      font-family: karla, sans-serif;
      padding-left: 16px;
    }

    .white-container {
      background-color: #FAFAFA;
      padding: 24px 16px 0px 16px;
      border-radius: 0px;
      min-height: 330px;
    }

    .site-heading {
      color: #222222;
      font-family: Montserrat, sans-serif;
      font-size: 21px;
      font-weight: 400;
      margin-bottom: 24px;
    }

    .error-msg {
      margin-bottom: 20px;
      color: #222222;
      font-family: karla, sans-serif;
      font-size: 16px;
      letter-spacing: 0.4px;
    }
    .error-msg span {
      font-weight: 700;
    }
    .immediate-assitance {
      color: #222222;
      font-family: karla, sans-serif;
      font-size: 16px;
      font-weight: 400;
      letter-spacing: 0.15px;
    }

    @media only screen and (min-width: 768px) {
      header {
        height: 48px;
      }
      .white-container {
        width: 95%;
        padding: 32px 32px 0px 32px;
        margin: 32px 16px 32px 16px;
      }
      header .logo {
        width: 120px;
        height: 26.73px;
      }
      .header-inner-wrapper a {
        padding-left: 32px;
      }
      .site-heading {
        line-height: 32px;
        font-size: 21px;
      }
      .error-msg {
        font-size: 16px;
      }
      .immediate-assitance {
        font-size: 16px;
        font-weight: 400;
      }
      footer .copywrite-text {
        padding-left: 32px;
      }
    }

    @media only screen and (min-width: 1200px) {
      header {
        height: 72px;
      }
      .white-container {
        width: 94%;
        max-width: 1200px;
        padding: 32px;
        margin-top: 42px;
      }
      header .logo {
        width: 150px;
        height: 38.9px;
      }
      .header-inner-wrapper a {
        padding-left: 32px;
      }
      .site-heading {
        font-size: 24px;
      }
      .error-msg {
        font-size: 16px;
      }
      .immediate-assitance {
        font-size: 16px;
        font-weight: 400;
      }
      footer .copywrite-text {
        padding-left: 30px;
      }

    }

    @media only screen and (max-width: 768px) {
      header .logo {
        height: 25.99px;
        width: 100px;
      }
    }

    @media only screen and (max-width: 1200px) and (min-width: 768px){
      .white-container {
        margin-left: 8px !important;
      }
    }
  </style>
</head>


  <body>
    <div class="container">
      <header>
        <div class="header-inner-wrapper">
          <a><img class="logo" src="${contextPath}/_ui/responsive/common/images/innovia-logo.svg"/></a>
        </div>
      </header>
      <div class="white-container">
        <div class="site-heading">
          <spring:theme code="text.servererror.oops.message" text="Oops, something went wrong!"/>
        </div>
        <div class="error-msg">
          <spring:theme code="text.servererror.errorfound" text="500 Error Found"/> 
        </div>
        <div class="immediate-assitance">
          <spring:theme code="text.servererror.cc.technical.issue" text="Sorry, We had some technical problem."/>
        </div>
      </div>
      <footer>
        <div class="footer-inner-wrapper">
          <p class="copywrite-text"><spring:theme code="text.servererror.copyright.message" arguments="${currentYear}" argumentSeparator="$$"/></p>
        </div>
      </footer>
    </div>
  </body>
</html>
