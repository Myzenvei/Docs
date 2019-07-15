<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

  <template:page pageTitle="${pageTitle}">
   
	<div class="container__full">
	<div class=" row">	
		 <%-- Breaking the Code to generate the Server Error to validate the 500 Pages  --%>
	        <cms:pageSlot position="PlaceholderContentSlot" var="feature"  element="div" class="">
				 <cms:component component="${feature}" />
		</div>
	</div>
</template:page>
