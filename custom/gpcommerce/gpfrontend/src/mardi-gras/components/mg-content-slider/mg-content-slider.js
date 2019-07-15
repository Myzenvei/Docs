import mgSlickSlider from '../common/mg-slick-slider/mg-slick-slider.vue';
import globals from '../../../components/common/globals';
export default {
  name: 'mg-content-slider',
  components: {
    mgSlickSlider
  },
  props: {
    items: {
      type: Array,
      default: [{
          slideURL: "./static/assets/images/mardi-gras/Ding_Ding.png",
        },
        {
          slideURL: "./static/assets/images/mardi-gras/Ding_Ding.png",
        },
        {
          slideURL: "./static/assets/images/mardi-gras/Ding_Ding.png",
        },
        {
          slideURL: "./static/assets/images/mardi-gras/Ding_Ding.png",
        },
        {
          slideURL: "https://fakeimg.pl/200x200/",
        },
        {
          slideURL: "https://fakeimg.pl/200x200/",
        },
        {
          slideURL: "https://fakeimg.pl/200x200/",
        },
        {
          slideURL: "https://fakeimg.pl/200x200/",
        },
      ]
    },
    componentClass: {
      type: String,
    }
  },
  data() {
    return {
      globals,
      zommedImgSrc: '',
      zoomedImgAlt: '',
      zommedContainertoggle: false,

    };
  },
  computed: {

  },
  mounted() {},
  methods: {
    getClickedSlideInfo(evt) {
      this.zommedImgSrc = `${this.globals.contextPath}${evt.srcElement.currentSrc}`;
      this.zoomedImgAlt = `${evt.srcElement.alt}`;
      this.zommedContainertoggle = true;
    },
    toggleZoomWindow() {
      this.zommedContainertoggle = false;
    },
  },
};
