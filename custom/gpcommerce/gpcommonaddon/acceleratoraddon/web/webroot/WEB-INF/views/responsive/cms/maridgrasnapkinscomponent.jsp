<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:htmlEscape defaultHtmlEscape="true" />


<c:choose>
<c:when test="${componentTheme eq 'mardigras-pink'}">
       <mg-image-text-layout
        :component-data = '${napkinsData}'
        :component-theme = "{layoutBackgroundType: 'color',
        layoutBackground: '#ED5AA0',
        titleColor: '#FFFFFF',
        subTitleColor: '#814c9e',
        descriptionColor: '#FFFFFF',
        borderBottom: '7px dotted #d8d8d8',
        hideBannerInMobile: 'true'}"
        :component-class = "'our-napkins mardigras-pink'"
        :is-form = false>
      </mg-image-text-layout>
      <mg-content-slider :items='${imageCarouselData}' background="#ED5AA0" :component-class="'mardigras-pink'"></mg-content-slider>
</c:when>
<c:otherwise>
       <mg-image-text-layout
        :component-data = '${napkinsData}'
        :component-theme = "{layoutBackgroundType: 'color',
        layoutBackground: '#814C9E',
        titleColor: '#FFFFFF',
        subTitleColor: '#58c9e7',
        descriptionColor: '#FFFFFF',
        hideBannerInMobile: 'true'}"
        :component-class = "'our-napkins'"
        :is-form = false>
      </mg-image-text-layout>
      <mg-content-slider :items='${imageCarouselData}'></mg-content-slider>
</c:otherwise>
</c:choose>


    
