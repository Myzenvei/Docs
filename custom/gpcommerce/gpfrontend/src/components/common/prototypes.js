
export default {
  init: () => {
    /* eslint-disable */
    String.prototype.includes = function(str) {
      let returnValue = false;
      if (this.indexOf(str) !== -1) {
        returnValue = true;
      }
      return returnValue;
    };
  },
};
