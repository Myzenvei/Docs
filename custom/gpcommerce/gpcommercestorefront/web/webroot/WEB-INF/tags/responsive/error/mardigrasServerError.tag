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
  <title>Mardi Gras&reg;</title>
  <link rel="shortcut icon" type="image/x-icon" media="all" href="/_ui/responsive/theme-alpha/images/mardigras/favicon.ico" />
  <style type="text/css">
    @font-face {
      font-family: 'icomoon';
      src: url('/_ui/responsive/common/fonts/31F5BE_A_0.woff');
    }

    html,
    body {
      margin: 0;
      padding: 0;
      font-family: 'icomoon';
      text-transform: uppercase;
    }

    .container {
      display: flex;
      align-items: center;
      flex-direction: column;
      position: absolute;
      width: 100%;
      height: 100vh;
      min-height: 600px;
    }

    header {
      height: 72px;
      background-color: #522773;
      width: 100%;
    }

    header .logo {
      height: auto;
      width: 100%;
      padding: 10px 20px 0 80px;
      background-size: cover;

    }

    .brand-container {
      display: inline-block;
      width: 90px;
      height: 90px;
    }

    header .logo-text {
      color: #fff;
      padding-left: 10px;
      font-weight: bold;
    }

    footer .page-footer {
      height: 200px;
      position: fixed;
      bottom: 0;
      left: 0;
      width: 100%;
      display: flex;
      background: url('/_ui/responsive/common/images/footer-bg.png');
      background-repeat: repeat-x;
    }

    footer .page-footer .copywrite-text {
      background-color: #522773;
      color: #FFFFFF;
      height: 60px;
      font-size: 20px;
      position: relative;
      top: 45%;
      width: 100%;
      text-align: center;
      padding-bottom: 10px;
    }

    .grey-container {
      background-color: #FAFAFA;
      width: 85%;
      padding: 30px 30px 30px 40px;
      margin: 40px;
    }

    .site-heading {
      color: #222222;
      font-size: 48px;
      font-weight: 500;
      margin-bottom: 40px;
    }

    .error-msg {
      margin-bottom: 20px;
      color: #222222;
      font-size: 28px;
      font-weight: 700;
      letter-spacing: 0.4px;
    }

    .immediate-assitance {
      color: #222222;
      font-size: 24px;
      font-weight: 300;
      letter-spacing: 0.4px;
    }
    @media only screen and (max-width: 1199px) {
      .white-container {
        width: 95%;
      }
      header a .logo {
        padding-left: 40px;
      }
    }
    @media only screen and (max-width: 767px) {
      .site-heading {
        font-size: 32px;
      }
      .error-msg,
      .immediate-assitance {
        font-size: 20px;
      }
      .grey-container {
        width: auto;
      }
    }
  </style>
</head>

<body>
  <div class='container'>
    <header>
      <a href="/" class="brand-container"><img class='logo' src="${contextPath}/_ui/responsive/common/images/mardigras-logo.png"></a>
    
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
        <div class='copywrite-text'>
          <p>&copy;&nbsp;<spring:theme code="text.servererror.copyright.mardigras.message" arguments="${currentYear}" argumentSeparator="$$"/></p>
        </div>
      </div>
    </footer>
  </div>
</body>

</html>