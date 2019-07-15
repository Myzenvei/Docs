<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty redirect_url}"><script>window.location.replace(ACC.config.encodedContextPath + "${redirect_url}");</script></c:if>
<c:if test="${not empty customerReload}"><script>location.reload();</script></c:if>
<c:if test="${not empty closeAsm}"><script>$("#_asm").remove(); var oldurl = window.location.href;var newurl = oldurl.replace("&asm=true", "").replace("?asm=true&", "?").replace("?asm=true", "");
window.location.replace(newurl);</script></c:if>