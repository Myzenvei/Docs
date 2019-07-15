const mobileMixin = {
  data() {
    return {
      mobileBreakPoint: 768,
      tabletBreakPoint: 1200,
    };
  },
  mounted() {

  },
  methods: {
    isMobile() {
      if (window.innerWidth < this.mobileBreakPoint) {
        return true;
      }
      return false;
    },
    isTablet() {
      if (window.innerWidth < this.tabletBreakPoint) {
        return true;
      }
      return false;
    },

    getResponsiveAlt(altD, altT, altM) {
      let alt = 'Image';
      if (altD || altT || altM) {
        if (this.isMobile() && altM) {
          alt = altM;
        } else if (this.isTablet() && altT) {
          alt = altT;
        } else if (altD) {
          alt = altD;
        }
      }
      return alt;
    },

    getResponsiveImage(imageD, imageT, imageM) {
      let image = '';
      if (imageD || imageT || imageM) {
        if (this.isMobile() && (imageM || imageT)) {
          image = imageM || imageT;
        } else if (this.isTablet() && (imageM || imageT)) {
          image = imageT || imageM;
        } else if (imageD) {
          image = imageD;
        }
      }
      return image;
    },
  },
};

export default mobileMixin;
