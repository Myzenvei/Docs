<template>
<div class="pdp-page">
  <h1>{{ msg }}</h1>

  <div class="container-fluid">
    <div class="row">
      <div class="col-xs-12 col-sm-6">
        <vx-thumbnail-viewer :total-carousel-data="messages.thumbnailViewer.galleryData"></vx-thumbnail-viewer>
      </div>
      <div class="col-xs-12 col-sm-6">
        <vx-pdp-product-info :i18n="messages.pdpProductInfo" :pdp-product-info-data="messages.pdpProductSection.productInfo"></vx-pdp-product-info>
      </div>
    </div>
    <div class="row">
      <div class="col-xs-12">
        <vx-tab-container :i18n="messages.pdpTabContainer" ></vx-tab-container>
      </div>
    </div>
</div>
</div>
</template>
<script>
import vxThumbnailViewer from '../components/pdp/vx-thumbnail-viewer/vx-thumbnail-viewer.vue';
// import vxPDPProductInfo from '../components/pdp/vx-pdp-product-info/vx-pdp-product-info.vue';
import vxPDPProductInfo from '../components/pdp/vx-pdp-product-info/vx-pdp-product-info.vue';
import vxTabContainer from '../components/pdp/vx-tab-container/vx-tab-container.vue';
import messages from '../locale/messages';

export default {
  name: 'pdp-page',
  components: {
    vxThumbnailViewer,
    'vx-pdp-product-info': vxPDPProductInfo,
    vxTabContainer,
  },
  created() {
    const socialShareScript = document.createElement('script');

    socialShareScript.setAttribute(
      'src',
      'https://static.addtoany.com/menu/page.js',
    );
    document.head.appendChild(socialShareScript);
  },
  data() {
    return {
      msg: 'PDP Page',
      messages: messages['en-US'],
    };
  },
};
</script>
