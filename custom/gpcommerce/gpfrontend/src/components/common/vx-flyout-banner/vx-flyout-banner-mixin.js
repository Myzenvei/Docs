const flyoutBannerMixin = {
  methods: {
    // Removed the store concept for simplicity
    // showFlyout: function(status,message) {
    // this.$store.dispatch("showFlyout", {
    //        "status":status,
    //        "message": message
    // });
    // }
    showFlyout(status, message, autoDismiss, timer) {
      this.$root.$refs.flyoutBanner.showFlyout({
        status,
        message,
        autoDismiss,
        timer,
      });
    },
    dismissFlyout() {
      this.$root.$refs.flyoutBanner.dismissFlyout();
    },
    showFlyoutNextPage(status, message, autoDismiss) {
      this.$root.$refs.flyoutBanner.showFlyoutNextPage({
        status,
        message,
        autoDismiss,
      });
    },
  },
};

export default flyoutBannerMixin;
