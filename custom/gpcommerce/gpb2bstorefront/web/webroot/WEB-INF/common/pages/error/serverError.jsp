<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="error" tagdir="/WEB-INF/tags/responsive/error"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:choose>
<c:when test="${fn:contains(pageContext.request.requestURL, 'dixie')}">
<error:dixieServerError/>
</c:when>
<c:when test="${fn:contains(pageContext.request.requestURL, 'gpxpr')}">
<error:gpxpressServerError/>
</c:when>
<c:otherwise>
<error:gpproServerError/>
</c:otherwise>
</c:choose> 
