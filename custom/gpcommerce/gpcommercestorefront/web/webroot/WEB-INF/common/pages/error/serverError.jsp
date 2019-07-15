<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="error" tagdir="/WEB-INF/tags/responsive/error"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:choose>
<c:when test="${fn:contains(pageContext.request.requestURL, 'mardigras')}">
<error:mardigrasServerError/>
</c:when>
<c:when test="${fn:contains(pageContext.request.requestURL, 'copperandcrane')}">
<error:copperCraneServerError/>
</c:when>
<c:when test="${fn:contains(pageContext.request.requestURL, 'vanityfairnapkins')}">
<error:vanityFairNapkinServerError/>
</c:when>
<c:when test="${fn:contains(pageContext.request.requestURL, 'brawny')}">
<error:brawnyServerError/>
</c:when>
<c:when test="${fn:contains(pageContext.request.requestURL, 'sparkle')}">
<error:sparkleServerError/>
</c:when>
<c:when test="${fn:contains(pageContext.request.requestURL, 'innovia')}">
<error:innoviaServerError/>
</c:when>
<c:otherwise>
<error:gpEmployeeServerError/>
</c:otherwise>
</c:choose> 
