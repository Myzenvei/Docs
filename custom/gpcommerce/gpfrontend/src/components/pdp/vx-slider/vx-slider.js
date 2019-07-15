import {
  swiper,
  swiperSlide
} from 'vue-awesome-swiper';
import vxProductTileVertical from '../../search-browse/vx-product-tile-vertical/vx-product-tile-vertical.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-slider',
  components: {
    swiper,
    swiperSlide,
    vxProductTileVertical,
  },
  props: {
    /**
     * Data of the product
     */
    productData: {
      type: Object,
      default: {},
    },
    /**
     * Title of the slider
     */
    title: {
      type: String,
      default: '',
    },
    /**
     * indicates whether to show select checkbox or not
     */
    showSelectCheckbox: {
      type: Boolean,
      default: false,
    },
    /**
     * indicates whether the site is configured for favorites
     */
    isFavorites: {
      type: Boolean,
      default: false,
    },
    /**
     * indicates whether the component is refills
     */
    isRefill: {
      type: Boolean,
      default: false,
    },
    /**
     * sets slides per viewport and its respective space
     */
    sliderSlides: {
      type: Object,
      default: () => ({
        desktop: {
          slides: 4,
          spaceAllowed: 56,
        },
        tablet: {
          slides: 4,
          spaceAllowed: 16,
        },
        mobile: {
          slides: 2,
          spaceAllowed: 16,
        },
      }),
    },
    /**
     * indicates whether the site is configured for Bazaar Voice
     */
    isBazaarVoice: {
      type: String,
      default: '',
    },
    /**
     * Copy text coming from properties files for search browse components
     */
    searchBrowseI18n: {
      type: Object,
    },
    isSlotBased: {
      type: Boolean,
      default: false,
    },
    swiperModifierClass: {
      type: String,
      default: '',
    },
    enableSimulateTouch: {
      type: Boolean,
      default: true,
    },
  },
  mixins: [mobileMixin],
  data() {
    return {
      swiper: '',
      showNavigationArrows: false,
      firstSlide: true,
      lastSlide: false,
      swiperOptions: {
        slidesPerView: 4,
        spaceBetween: 50,
        freeMode: false,
        watchOverflow: true,
        loop: false,
        breakpoints: {
          // when window width is <= 767px
          767: {
            slidesPerView: this.sliderSlides.mobile.slides,
            spaceBetween: this.sliderSlides.mobile.spaceAllowed,
          },
          // when window width is <= 1023px
          1199: {
            slidesPerView: this.sliderSlides.tablet.slides,
            spaceBetween: this.sliderSlides.tablet.spaceAllowed,
          },
          // when window width is <= 1440px
          1440: {
            slidesPerView: this.sliderSlides.desktop.slides,
            spaceBetween: this.sliderSlides.desktop.spaceAllowed,
          },
        },
        simulateTouch: this.enableSimulateTouch,
      },
      showSelect: false,
      viewPort: {
        mobile: 767,
        desktop: 1440,
        tablet: 1199,
      },
      i18n: this.$root.messages.common.slider,
      swiperWrapperClass: '.swiper-container',
    };
  },
  created() {
    if (this.showSelectCheckbox) {
      this.showSelect = true;
    }
    if (this.swiperModifierClass) {
      this.swiperOptions.containerModifierClass = this.swiperModifierClass;
    }
  },
  mounted() {
    if (this.swiperModifierClass) {
      if (document.querySelector(`.${this.swiperModifierClass.concat('initialized')}`)) {
        this.swiperWrapperClass = this.swiperWrapperClass.concat('.', this.swiperModifierClass, 'initialized');
      } else if (document.querySelector(`.${this.swiperModifierClass.concat('horizontal')}`)) {
        this.swiperWrapperClass = this.swiperWrapperClass.concat('.', this.swiperModifierClass, 'horizontal');
      } else if (document.querySelector(`.${this.swiperModifierClass.concat('android')}`)) {
        this.swiperWrapperClass = this.swiperWrapperClass.concat('.', this.swiperModifierClass, 'android');
      }
    }
    const self = this;
    self.onInit();
    self.swiper = document.querySelector(this.swiperWrapperClass).swiper;
    self.swiper.on('reachEnd', () => {
      self.firstSlide = false;
      self.lastSlide = true;
    });
    self.swiper.on('reachBeginning', () => {
      self.firstSlide = true;
      self.lastSlide = false;
    });
  },
  watch: {
    productData() {
      this.onInit();
    },
  },
  methods: {
    /**
     * This function sets the slides as per Viewport
     */
    onInit() {
      const self = this;
      if (self.isMobile()) {
        if (self.productData.length > self.swiperOptions.breakpoints[self.viewPort.mobile].slidesPerView) {
          self.showNavigationArrows = true;
        }
      } else if (self.productData.length > self.swiperOptions.breakpoints[self.viewPort.desktop].slidesPerView) {
        self.showNavigationArrows = true;
      }
    },
    /**
     * This function sets the slides to previous product
     */
    slidePrev() {
      this.swiper.slidePrev();
      this.lastSlide = false;
    },
    /**
     * This function sets the slides to next product
     */
    slideNext() {
      this.swiper.slideNext();
      this.firstSlide = false;
    },
  },
};
