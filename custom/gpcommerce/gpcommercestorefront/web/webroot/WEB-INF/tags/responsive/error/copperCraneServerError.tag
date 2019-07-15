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
  <title>Copper and Crane</title>
  <!-- To place font file (if needed) -->
  <link rel="icon" href="/_ui/responsive/theme-alpha/images/copperandcrane/favicon.png" sizes="16x16" type="image/png">
  <style type="text/css">

    @font-face {
      font-family: 'CocogoosePro-Light';
      src: url('/_ui/responsive/common/fonts/copperandcrane/3911A0_19_0.eot');
      src: url('/_ui/responsive/common/fonts/copperandcrane/3911A0_19_0.eot?#iefix') format('embedded-opentype'),url('/_ui/responsive/common/fonts/copperandcrane/3911A0_19_0.woff2') format('woff2'),url('/_ui/responsive/common/fonts/copperandcrane/3911A0_19_0.woff') format('woff'),url('/_ui/responsive/common/fonts/copperandcrane/3911A0_19_0.ttf') format('truetype');
    }
    @font-face {
      font-family: 'Queulat-Regular';
      src: url('/_ui/responsive/common/fonts/copperandcrane/3911A0_D_0.eot');
      src: url('/_ui/responsive/common/fonts/copperandcrane/3911A0_D_0.eot?#iefix') format('embedded-opentype'),url('/_ui/responsive/common/fonts/copperandcrane/3911A0_D_0.woff2') format('woff2'),url('/_ui/responsive/common/fonts/copperandcrane/3911A0_D_0.woff') format('woff'),url('/_ui/responsive/common/fonts/copperandcrane/3911A0_D_0.ttf') format('truetype');
    }

    html,
    body {
      margin: 0;
      padding: 0;
    }

    .container {
      display: flex;
      align-items: center;
      flex-direction: column;
      position: absolute;
      width: 100%;
      height: 100vh;
    }

    header {
      height: 56px;
      background-color: #37302c;
      align-self: flex-start;
      width: 100%;
      display: flex;
      align-items: center;
    }

    header .logo {
      height: 24px;
      padding-left: 40px;
    }

    footer {
      height: 64px;
      background-color: #37302c;
      position: fixed;
      bottom: 0;
      width: 100%;
      display: flex;
      align-items: center;
    }

    footer .copywrite-text {
      padding-left: 40px;
      color: #ffffff;
      font-family: CocogoosePro-Light;
      font-size: 14px;
    }

    .grey-container {
      background-color: #f9f8f5;
      padding: 32px;
      width: 85%;
      margin-top: 32px;
    }

    .site-heading {
      color: #222222;
      font-family: Queulat-Regular;
      font-size: 24px;
      font-weight: 300;
      margin-bottom: 32px;
    }

    .error-msg {
      margin-bottom: 32px;
      color: #222222;
      font-family: CocogoosePro-Light;
      font-size: 14px;
      font-weight: bold;
      letter-spacing: 0.4px;
    }

    .immediate-assitance {
      color: #222222;
      font-family: CocogoosePro-Light;
      font-size: 12px;
      font-weight: 300;
      letter-spacing: 0.4px;
    }

  </style>
</head>

<body>
  <div class='container'>
    <header>
      <div class="header-inner-wrapper">
        <a href="/"><img class='logo' src="${contextPath}/_ui/responsive/common/images/copper-crane-logo.svg"></a>
      </div>
    </header>
    <div class='grey-container'>
      <div class='site-heading'>
       <spring:theme code="text.servererror.oops.message" text="Oops, something went wrong!"/>
      </div>
      <div class='error-msg'>
        <spring:theme code="text.servererror.errorfound" text="500 Error Found"/> 
      </div>
      <div class='immediate-assitance'>
       <spring:theme code="text.servererror.cc.technical.issue" text="Sorry, We had some technical problem."/>
      </div>
    </div>
    <footer>
      <div class="footer-inner-wrapper">
        <p class='copywrite-text'>&copy;&nbsp;<spring:theme code="text.servererror.cc.copyright.message" arguments="${currentYear}" argumentSeparator="$$"/></p>
      </div>
    </footer>
  </div>
</body>

</html>
