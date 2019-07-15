import mobileMixin from './mobile-mixin';

const headerHeightAdjustment = {
  mixins: [mobileMixin],
  data() {
    return {
    };
  },
  mounted() {
  },
  methods: {
    setInnerWrapperPadding() {
      if (document.getElementsByClassName('header-section')[0]) {
        const headerHeight = document.getElementsByClassName('header-section')[0]
          .offsetHeight;
        if (this.isTablet()) {
          if (document.querySelector('.main__inner-wrapper')) {
            document.getElementsByClassName(
              'main__inner-wrapper',
            )[0].style.paddingTop = `${headerHeight}px`;
          }
        }
      }
    },
  },
};
export default headerHeightAdjustment;
