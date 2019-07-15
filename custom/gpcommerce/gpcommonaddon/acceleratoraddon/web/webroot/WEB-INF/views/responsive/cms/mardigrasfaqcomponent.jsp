<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />

 <mg-image-text-layout
      :component-data ='${faq}'
      :component-theme = "{layoutBackgroundType: 'color',
        layoutBackground: '#814C9E',
        titleColor: '#FFFFFF',
        subTitleColor: '#58c9e7',
        descriptionColor: '#FFFFFF',
        hideBannerInMobile: 'true'}"
      :component-class = "'faqs'"
      :is-form = "false" >
      <mg-faq :i18n='${faq}'></mg-faq>
</mg-image-text-layout> 