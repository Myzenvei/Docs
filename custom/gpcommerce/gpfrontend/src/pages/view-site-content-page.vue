<template>
  <div class="view-site-content">
    <div class="container-fluid">
    <div class="row d-flex flex-wrap">
      <vx-image-tile class="col-sm-6 col-sm-push-6 col-xs-12" :imageTileData="viewSiteContent['imageTile']"></vx-image-tile>
      <vx-text-tile class="col-sm-6 col-sm-pull-6 col-xs-12" :textTileData="viewSiteContent['textTileTwo']"></vx-text-tile>
    </div>
    <br>
    <div class="row m-xs-0">
      <vx-image-text :imageTileData="viewSiteContent['imageTile']" :textTileData="viewSiteContent['textTile']"  component-theme='' />
    </div>
    <br>
    <div class="row m-xs-0">
      <vx-image-text :imageTileData="viewSiteContent['imageTile2']" :textTileData="viewSiteContent['textTileTwo']"  component-theme='text-white' />
    </div>
    <br>
    <div class="row">
      <vx-brand-bar :brand-bar-data="viewSiteContent['brandBar']"></vx-brand-bar>
    </div>
    <br>
    <div class="row">
      <vx-marketing-promo :promotion-data="viewSiteContent['promoData']"></vx-marketing-promo>
    </div>
    <div class="row">
      <vx-bundles :bundles-data="viewSiteContent['bundlesData']"></vx-bundles>
    </div>
  </div>
  </div>
</template>

<script>
import vxTextTile from '../components/view-site-content/vx-text-tile/vx-text-tile.vue';
import vxImageText from '../components/view-site-content/vx-image-text/vx-image-text.vue';
import vxImageTile from '../components/view-site-content/vx-image-tile/vx-image-tile.vue';
import vxBrandBar from '../components/view-site-content/vx-brand-bar/vx-brand-bar.vue';
import vxMarketingPromo from '../components/view-site-content/vx-marketing-promo/vx-marketing-promo.vue';
import vxBundles from '../components/common/vx-bundles/vx-bundles.vue';

export default {
  name: 'ViewSiteContentPage',
  components: {
    vxTextTile,
    vxImageTile,
    vxBrandBar,
    vxMarketingPromo,
    vxBundles,
    vxImageText,
  },
  data() {
    return {
      viewSiteContent: {
        textTile: {
          header: 'Restroom',
          headerColor: '#FFFFFF',
          text:
            'Total restroom solutions for commercial facilities providing the best experience for those who use the restroom and those who manage them',
          textColor: '#FFFFFF',
          ctaText: 'Learn More',
          ctaLink: '#',
          ctaColor: '#FFFFFF',
          backgroundColor: '#005293',
          backgroundImageD: '',
          backgroundImageT: '',
          backgroundImageM: '',
          textAlignment: 'center',
          imageWidth: 50,
        },
        textTileTwo: {
          header: 'Restroom',
          headerColor: '#FFFFFF',
          text:
            'Total restroom solutions for commercial facilities providing the best experience for those who use the restroom and those who manage them',
          textColor: '#FFFFFF',
          ctaText: 'Learn More',
          ctaLink: '#',
          ctaColor: '#FFFFFF',
          fontColor: '#FFFFFF',
          backgroundColor: '#005293',
          backgroundImageD: '',
          backgroundImageT: '',
          backgroundImageM: '',
          textAlignment: 'left',
          imageWidth: 0,
        },
        imageTile: {
          imageSrcD:
            'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
          imageSrcT:
            'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
          imageSrcM:
            'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
          imageLink: '#',
          imageAltText: 'Alternate Text',
          imageWidth: 50,
        },
        imageTile2: {
          imageSrcD:
            'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
          imageSrcT:
            'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
          imageSrcM:
            'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
          imageLink: '#',
          imageAltText: 'Alternate Text',
          imageWidth: 0,
        },
        brandBar: {
          header: 'Our Brands',
          headerColor: '#747678',
          subHeader: 'We are the best!',
          subHeaderColor: '#747678',
          backgroundColor: '#ffffff',
          backgroundImageD: '',
          backgroundImageT: '',
          backgroundImageM: '',
          brands: [
            {
              brandName: 'Dixie',
              brandImage:
                'https://www.gppro.com/-/media/cpg/GPProfessional/img/svg/brands/dixie-color.svg',
              brandLink: 'https://www.gppro.com/brands/dixie',
            },
            {
              brandName: 'Dixie',
              brandImage:
                'https://www.gppro.com/-/media/cpg/GPProfessional/img/svg/brands/dixie-color.svg',
              brandLink: 'https://www.gppro.com/brands/dixie',
            },
            {
              brandName: 'Dixie',
              brandImage:
                'https://www.gppro.com/-/media/cpg/GPProfessional/img/svg/brands/dixie-color.svg',
              brandLink: 'https://www.gppro.com/brands/dixie',
            },
            {
              brandName: 'Dixie',
              brandImage:
                'https://www.gppro.com/-/media/cpg/GPProfessional/img/svg/brands/dixie-color.svg',
              brandLink: 'https://www.gppro.com/brands/dixie',
            },
            {
              brandName: 'Dixie',
              brandImage:
                'https://www.gppro.com/-/media/cpg/GPProfessional/img/svg/brands/dixie-color.svg',
              brandLink: 'https://www.gppro.com/brands/dixie',
            },
            {
              brandName: 'Dixie',
              brandImage:
                'https://www.gppro.com/-/media/cpg/GPProfessional/img/svg/brands/dixie-color.svg',
              brandLink: 'https://www.gppro.com/brands/dixie',
            },
            {
              brandName: 'Dixie',
              brandImage:
                'https://www.gppro.com/-/media/cpg/GPProfessional/img/svg/brands/dixie-color.svg',
              brandLink: 'https://www.gppro.com/brands/dixie',
            },
          ],
          ctaText: 'View All',
          ctaLink: '#',
          ctaColor: '#ffffff',
        },
        promoData: {
          header: 'FOR 10% OFF YOUR NEXT PURCHASE, USE PROMO CODE: CC',
          url: '#',
          fontColor: '#ffffff',
          backgroundColor: '#000000',
          backgroundImageD: '',
          backgroundImageT: '',
          backgroundImageM: '',
        },
        bundlesData: [
          {
            imageSrcD:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcT:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcM:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageLink: '#',
            imageAltText: 'Alternate Text',
          },
          {
            imageSrcD:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcT:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcM:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageLink: '#',
            imageAltText: 'Alternate Text',
          },
          {
            imageSrcD:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcT:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcM:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageLink: '#',
            imageAltText: 'Alternate Text',
          },
          {
            imageSrcD:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcT:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageSrcM:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/home/home-gp-pro-760x318-2.jpg',
            imageLink: '#',
            imageAltText: 'Alternate Text',
          },
        ],
      },
    };
  },
};
</script>
