<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="url" value="${pageContext.request.requestURL}"/>
<c:set var="contextPath" value="${fn:substringBefore(url, pageContext.request.requestURI)}"/>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" type="text/css" href="https://cloud.typography.com/6228154/7965012/css/fonts.css">
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
      flex-direction: column;
      align-items: center;
      position: absolute;
      width: 100%;
      height: 100vh;
    }

    .mini-header {
      height: 72px;
      width: 100%;
      background-color: #2D5B99;
    }

    .grey-container {
      background-color: #FAFAFA;
      padding: 32px;
      width: 85%;
      margin-top: 32px;
    }

    .site-heading {
      color: #222222;
      font-family: 'Gotham A', 'Gotham B', Helvetica, Roboto, Arial, sans-serif;
      font-size: 24px;
      font-weight: 400;
      margin-bottom: 24px;
      line-height: 32px;
    }

    .error-msg {
      margin-bottom: 16px;
      color: #222222;
      font-family: 'Gotham A', 'Gotham B', Helvetica, Roboto, Arial, sans-serif;
      font-size: 16px;
      font-weight: 400;
      letter-spacing: 0.4px;
      line-height: 28px;
    }

    .error-code {
      font-weight: 500;
    }

    .immediate-assitance {
      color: #222222;
      font-family: 'Gotham A', 'Gotham B', Helvetica, Roboto, Arial, sans-serif;
      font-size: 16px;
      font-weight: 400;
      letter-spacing: 0.4px;
      line-height: 28px;
    }

    @media only screen and (min-width: 768px) and (max-width: 1199px) {
      .mini-header {
        height: 48px;
      }

      .site-heading {
        font-size: 21px;
        line-height: 27px;
      }
    }

    @media only screen and (max-width: 767px) {
      .mini-header {
        height: 48px;
      }

      .grey-container {
        width: 100%;
        margin-top: 0;
        padding: 24px 16px;
      }

      .site-heading {
        font-size: 21px;
      }
    }
  </style>
</head>

<body>
  <div class='container'>
    <div class="mini-header"></div>
    <div class='grey-container'>
      <div class='site-heading'>
        <spring:theme code="text.servererror.oops.message" text="Oops, something went wrong!"/>
      </div>
      <div class='error-msg'>
       <span class='error-code'><spring:theme code="text.servererror.gpxpress.errorCode" text= "500"/></span>&nbsp;
       <span class="error-text"><spring:theme code="text.servererror.gpxpress.errorfound" text= "Error Found"/></span>
      </div>
      
      <div class='immediate-assitance'>
        <spring:theme code="text.servererror.gpxpress.technical.issue" text="Sorry, We had some technical problem."/>
      </div>
    </div>
  </div>
</body>

</html>
