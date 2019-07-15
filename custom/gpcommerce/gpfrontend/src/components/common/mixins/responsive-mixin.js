const responsiveMixin = {
  data() {
    return {
      viewports: {
        mobile: {
          min: 320,
          max: 767,
        },
        tablet: {
          min: 768,
          max: 1023,
        },
        desktop: {
          min: 1024,
          max: 1440,
        },
      },
    };
  },
  mounted() {},
  methods: {
    maxMobileViewPort() {
      return this.viewports.mobile.max;
    },
    minMobileViewPort() {
      return this.viewports.mobile.min;
    },
    maxTabletViewPort() {
      return this.viewports.tablet.max;
    },
    minTabletViewPort() {
      return this.viewports.tablet.min;
    },
    maxDesktopViewPort() {
      return this.viewports.desktop.max;
    },
    minDesktopViewPort() {
      return this.viewports.desktop.min;
    },
  },
};

export default responsiveMixin;
