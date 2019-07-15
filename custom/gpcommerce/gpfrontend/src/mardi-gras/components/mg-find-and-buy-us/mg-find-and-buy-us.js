import mgHeroBannerV1 from '../common/mg-hero-banner-v1/mg-hero-banner-v1.vue';

export default {
  name: 'mg-find-and-buy-us',
  components: {
    mgHeroBannerV1
  },
  props: {
    bannerData: {
      type: Object,
      default: {
        bannerTitle: 'Sign Up1',
        bannerHeading: 'COULD WE BE ANY CLOSER? YES! OH YES.',
        bannerInfo: '<b>now that we’re besties, we want to keep in touch all year round via email.</b> as a subscriber, you wll get all the beg.)',
        bannerImg: './static/assets/images/mardi-gras/find_us.png'
      }
    }
  },
  data() {
    return {

    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {

  }
}
