const backgroundImageMixin = {
  methods: {
    setBackGroundImage(bgImgD, bgImgT, bgImgM) {
      let bg;
      if (window.innerWidth < 768 && bgImgM) {
        bg = `url('${bgImgM}')`;
      } else if (window.innerWidth < 1200 && bgImgT) {
        bg = `url('${bgImgT}')`;
      } else {
        bg = `url('${bgImgD}')`;
      }
      return bg;
    },
  },
};

export default backgroundImageMixin;
