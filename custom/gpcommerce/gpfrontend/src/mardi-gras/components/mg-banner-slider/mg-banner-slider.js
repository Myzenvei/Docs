import mgSlickSlider from '../common/mg-slick-slider/mg-slick-slider.vue';
export default {
  name: 'mg-banner-slider',
  components: {
    mgSlickSlider
  },
  props: {
    bannerData: {
      type: Object,
      default: () => ({
        "bannerTitle": "About Us",
        "banners": [{
          "backgroundImage": "./static/assets/images/mardi-gras/about_background_1.png",
          "bannerHeading": "Mardi Gras� Paper Napkins Are Here to Help You Wipe Away the Boredom!",
          "bannerDesc": "\"\u003cb\u003eso pull up a chair, let down your hair and lets all grab a square!\u003c/b\u003e\"\n"
        }, {
          "backgroundImage": "./static/assets/images/mardi-gras/about_background_2.png",
          "bannerHeading": "Mardi Gras� Paper Napkins Are Here to Help You Wipe Away the Boredom!",
          "bannerDesc": "\"\u003cb\u003eso pull up a chair, let down your hair and lets all grab a square!\u003c/b\u003e\"\n"
        }]
      })
    }
  },
  data() {
    return {
      sliderConfig: {
        accessibility: true,
        mobileFirst: true,
        arrows: false,
        slidesToShow: 1,
        slidesToScroll: 1,
        infinite: true,
        autoplay: true,
        speed: 10000,
        fade: true,
        cssEase: 'linear'
      }
    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {

  }
}
