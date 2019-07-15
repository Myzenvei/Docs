import vxHeroBanner from '../vx-hero-banner/vx-hero-banner.vue';
import {
  eventBus
} from '../../../modules/event-bus';
import viewSiteContent from '../../../locale/view-site-content-i18n';
export default {
  name: 'vx-banner-carousel',
  components: {
    vxHeroBanner,
  },
  props: {
    carouselData: Object,
  },
  data() {
    return {
      carouselId: this.carouselData.componentId, //to generate the random string which serves as unique id
      i18n: viewSiteContent.bannerCarousel
    };
  },
  computed: {},
  mounted() {
    $("#" + this.carouselId).children(".left").hide(); // hiding initially the left indicator
    $("#" + this.carouselId).on("slid.bs.carousel", () => this.checkitem()); // on sliding setting triggering the event to hide the first indicator at first active slide and last indicator at last active slide
    $("#" + this.carouselId).carousel({
      interval: this.carouselData.autoStart ? this.carouselData.autoStart : false
    });
    eventBus.$on('carouselBinded', () => {
      setTimeout(() => {
        $("#" + this.carouselId).children(".left").hide();
      }, 100);
    });
  },
  methods: {
    checkitem() {
      var _$this;
      _$this = $("#" + this.carouselId);
      if ($("#" + this.carouselId + " .carousel-inner .item:first").hasClass("active")) {
        _$this.children(".left").hide();
        _$this.children(".right").show();
        if (this.$el.querySelector('.item.active .selected-product-img')) {
          this.$el.querySelector('.item.active .selected-product-img').focus();
        }
      } else if ($("#" + this.carouselId + " .carousel-inner .item:last").hasClass("active")) {
        _$this.children(".right").hide();
        _$this.children(".left").show();
        if (this.$el.querySelector('.item.active .selected-product-img')) {
          this.$el.querySelector('.item.active .selected-product-img').focus();
        }
      } else {
        _$this.children(".carousel-control").show();
      }
    }
  },
};
