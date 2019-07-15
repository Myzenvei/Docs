<%@ page trimDirectiveWhitespaces="true"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
      <%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

        <template:page pageTitle="${pageTitle}">
        <vx-breadcrumb :breadcrumbs='${breadcrumbs}'></vx-breadcrumb>
          <div class="wrapper d-flex">
           
            <div class="right col-xs-12">
              <cms:pageSlot position="GPUserGroupDetailsSlot" var="feature" element="div" class="">
                <cms:component component="${feature}" />
              </cms:pageSlot>
            </div>
          </div>
        </template:page>
