const flyoutBannerSecondaryMixin = {
  methods: {
    showFlyoutSecondary(status, message, autoDismiss, timer) {
      this.$root.$refs.flyoutBannerSecondary.showFlyoutSecondary({
        status,
        message,
        autoDismiss,
        timer,
      });
    },
    dismissFlyoutSecondary() {
      this.$root.$refs.flyoutBannerSecondary.dismissFlyoutSecondary();
    },
    showFlyoutNextPageSecondary(status, message, autoDismiss) {
      this.$root.$refs.flyoutBannerSecondary.showFlyoutNextPageSecondary({
        status,
        message,
        autoDismiss,
      });
    },
  },
};

export default flyoutBannerSecondaryMixin;
