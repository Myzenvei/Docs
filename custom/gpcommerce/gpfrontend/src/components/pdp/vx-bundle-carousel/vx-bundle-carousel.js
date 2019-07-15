import mgSlickSlider from '../../../mardi-gras/components/common/mg-slick-slider/mg-slick-slider.vue';
import globals from '../../common/globals';
import { ProductAvailability } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-bundle-carousel',
  components: {
    mgSlickSlider,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    bundleCarouselTitle: {
      type: String,
      default: '',
    },
    bundleCarouselData: {
      type: Object,
      default: {},
    },
  },
  computed: {},
  data() {
    return {
      globals,
      ProductAvailability,
      sliderConfig: {
        accessibility: true,
        mobileFirst: true,
        dots: false,
        arrows: true,
        prevArrow: '<span class="icon-chevron-left slick-prev"></span>',
        nextArrow: '<span class="icon-chevron-right slick-next"></span>',
        slidesToShow: 2,
        slidesToScroll: 2,
        responsive: [
          {
            breakpoint: 1024,
            settings: {
              slidesToShow: 4,
              slidesToScroll: 4,
              infinite: false,
              dots: false,
              arrows: true,
            },
          },
          {
            breakpoint: 767,
            settings: {
              slidesToShow: 4,
              slidesToScroll: 4,
              arrows: true,
              dots: false,
            },
          },
          // You can unslick at a given breakpoint now by adding:
          // settings: "unslick"
          // instead of a settings object
        ],
      },
      isDisabled: false,
    };
  },
  mounted() {
    this.isDisabled = this.bundleCarouselData.materialStatus === this.ProductAvailability.COMING_SOON ||
    (this.bundleCarouselData.stock.stockLevelStatus.code === this.ProductAvailability.OUT_OF_STOCK && !this.bundleCarouselData.stock.nextAvailableDate);
  },
  methods: {
    navigateToBundle(bundleId) {
      window.location.href = `${this.globals.getNavBaseUrl()}${this.globals.navigations.bundlePage}/?bundleId=${bundleId}&productCode=${this.bundleCarouselData.code}`;
    },
  },
};
