import mgContentSlider from '../mg-content-slider/mg-content-slider.vue';
import mgImageTextLayout from '../common/mg-image-text-layout/mg-image-text-layout.vue';

export default {
  name: 'mg-our-napkins',
  components: {mgContentSlider, mgImageTextLayout}, 
  props: {
    componentData: {
      type: Object
    }
  },
  data () {
    return {
      textLayout: {},
      layoutBackground: '',
      imageLayout: ''
    }
  },
  computed: {

  },
  mounted () {
    this.constructLayoutDetails();
  },
  methods: {
    constructLayoutDetails() {
      this.layoutBackground = this.componentData.layoutBackground;
      this.imageLayout = this.componentData.bannerImgUrl;
      this.textLayout = {
        title: {
          text: this.componentData.titleText,
          color: this.componentData.titleColor
        },
        subTitle: {
          title1: this.componentData.subTitleText1,
          title2: this.componentData.subTitleText2,
          color: this.componentData.subTitleColor
        },
        imgDotSrc: this.componentData.DotImgUrl,
        description: {
          text: this.componentData.descriptionText,
          color: this.componentData.descriptionColor
        },
      };
    }
  }
}
