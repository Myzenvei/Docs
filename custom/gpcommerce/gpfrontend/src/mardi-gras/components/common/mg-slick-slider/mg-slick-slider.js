import Slick from 'vue-slick';
import globals from '../../../../components/common/globals';

export default {
  name: 'mg-slick-slider',
  components: {
    Slick
  },
  props: {
    slickOptions: {
      type: Object,
      default: () => ({
        accessibility: true,
        mobileFirst: true,
        arrows: false,
        dots: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        responsive: [{
            breakpoint: 1024,
            settings: {
              slidesToShow: 4,
              slidesToScroll: 4,
              infinite: false,
              dots: false,
              arrows: true,
              prevArrow: `<img src=${globals.assetsPath}images/mardi-gras/slider_btn.png class="slick-prev">`,
              nextArrow: `<img src=${globals.assetsPath}images/mardi-gras/slider_btn.png class="slick-next">`,
            }
          },
          {
            breakpoint: 767,
            settings: {
              slidesToShow: 4,
              slidesToScroll: 4,
              arrows: true,
              prevArrow: `<img src=${globals.assetsPath}images/mardi-gras/slider_btn.png class="slick-prev">`,
              nextArrow: `<img src=${globals.assetsPath}images/mardi-gras/slider_btn.png class="slick-next">`,
            }
          }
          // You can unslick at a given breakpoint now by adding:
          // settings: "unslick"
          // instead of a settings object
        ]
      })
    }
  },
  data() {
    return {

    };
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    next() {
      this.$refs.slick.next();
    },

    prev() {
      this.$refs.slick.prev();
    },

    reInit() {
      // Helpful if you have to deal with v-for to update dynamic lists
      this.$nextTick(() => {
        this.$refs.slick.reSlick();
      });
    },

    // Events listeners
    handleAfterChange(event, slick, currentSlide) {
      // console.log('handleAfterChange', event, slick, currentSlide);
    },
    handleBeforeChange(event, slick, currentSlide, nextSlide) {
      // console.log('handleBeforeChange', event, slick, currentSlide, nextSlide);
    },
    handleBreakpoint(event, slick, breakpoint) {
      // console.log('handleBreakpoint', event, slick, breakpoint);
    },
    handleDestroy(event, slick) {
      // console.log('handleDestroy', event, slick);
    },
    handleEdge(event, slick, direction) {
      // console.log('handleEdge', event, slick, direction);
    },
    handleInit(event, slick) {
      // console.log('handleInit', event, slick);
    },
    handleReInit(event, slick) {
      // console.log('handleReInit', event, slick);
    },
    handleSetPosition(event, slick) {
      // console.log('handleSetPosition', event, slick);
    },
    handleSwipe(event, slick, direction) {
      // console.log('handleSwipe', event, slick, direction);
    },
    handleLazyLoaded(event, slick, image, imageSource) {
      // console.log('handleLazyLoaded', event, slick, image, imageSource);
    },
    handleLazeLoadError(event, slick, image, imageSource) {
      // console.log('handleLazeLoadError', event, slick, image, imageSource);
    },
  }
}
