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
 <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" type="text/css" href="https://use.typekit.net/cjl6dbo.css">
    <title>Brawny</title>
    <link rel="icon" href="/_ui/responsive/theme-alpha/images/brawny/brawny-favicon.png" sizes="16x16" type="image/png" />
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
        background-color: #000000;
        align-self: flex-start;
        width: 100%;
      }

      .header-inner-wrapper {
        display: flex;
        align-items: center;
        max-width: 1200px;
        height: 100%;
        margin: 0 auto;
      }
      .header-inner-wrapper a {
        margin-left: 16px;
      }

      header .logo {
        width: 75px;
        height: 32px;
      }

      footer {
        height: 64px;
        background-color: #000000;
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
        color: #ffffff;
        font-size: 14px;
        font-weight: 400;
        letter-spacing: 0.15px;
        font-family: franklin-gothic-urw, sans-serif;
        padding-left: 16px;
      }

      .white-container {
        background-color: #fafafa;
        padding: 24px;
        width: 100%;
        max-width: 1200px;
        margin-top: 32px;
        border-radius: 0;
        min-height: 330px;
      }

      .site-heading {
        color: #222222;
        font-family: franklin-gothic-urw, sans-serif;
        font-size: 21px;
        font-weight: 400;
        margin-bottom: 24px;
        letter-spacing: 0.4px;
      }

      .error-msg {
        margin-bottom: 20px;
        color: #222222;
        font-family: franklin-gothic-urw, sans-serif;
        font-size: 14px;
        font-weight: 700;
        letter-spacing: 0.4px;
      }

      .immediate-assitance {
        color: #222222;
        font-family: franklin-gothic-urw, sans-serif;
        font-size: 14px;
        font-weight: 400;
        letter-spacing: 0.4px;
      }

      @media only screen and (min-width: 768px) {
        header {
          height: 48px;
        }
        .white-container {
          width: 95%;
        }
        .site-heading {
          font-size: 21px;
        }
        header .logo {
          width: 75px;
          height: 28px;
        }
        .error-msg {
          font-weight: 400;
        }
        footer .copywrite-text {
          padding-left: 16px;
        }
        .header-inner-wrapper a {
          margin-left: 16px;
        }
      }

      @media only screen and (min-width: 1200px) {
        header {
          height: 72px;
        }
        .site-heading {
          font-size: 24px;
        }
        header .logo {
          width: 93px;
          height: 50px;
        }
        .error-msg,
        .immediate-assitance {
          font-size: 16px;
        }
        footer .copywrite-text {
          padding-left: 40px;
        }
        .header-inner-wrapper a {
          margin-left: 40px;
        }
      }
    </style>
  </head>

  <body>
    <div class="container">
      <header>
        <div class="header-inner-wrapper">
          <a><img class="logo" src="${contextPath}/_ui/responsive/common/images/brawny-logo.svg"/></a>
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
