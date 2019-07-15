/* Use this mixin to generate the data for info section on details page */
const mb2bInfoDataMixin = {
  methods: {
    /**
     * Generate Info Section Data
     * @param {string} arrayName    array name under which info data is stored
     * @param {string} infoSection  info section for which data is generated
     * @param {boolean} hasStatus   has status or not
     */
    generateInfoData(arrayName, infoSection, hasStatus, unitDetailsUrl, unitParam) {
      if (hasStatus) {
        this.$set(
          this.detailPageData,
          arrayName,
          this.detailPageData.unitDetailsData[infoSection].map(child => ({
            label: child.name,
            info: child.displayUid ? child.displayUid : child.uid,
            link: `${this.globals.getB2BBaseURL()}${unitDetailsUrl}/?${unitParam}=${encodeURIComponent(child.uid)}`,
            status: child.active,
            units: child.units ? child.units.length : '',
            uid: child.uid,
          })),
        );
      } else {
        this.$set(
          this.detailPageData,
          arrayName,
          this.detailPageData.unitDetailsData[infoSection].map(child => ({
            label: child.name,
            info: child.displayUid ? child.displayUid : child.uid,
            link: `${this.globals.getB2BBaseURL()}${unitDetailsUrl}/?${unitParam}=${encodeURIComponent(child.uid)}`,
            units: child.units ? child.units.length : '',
            uid: child.uid,
          })),
        );
      }
    },
  },
};

export default mb2bInfoDataMixin;
