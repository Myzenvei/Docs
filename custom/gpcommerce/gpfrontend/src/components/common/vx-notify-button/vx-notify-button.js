import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxNotifyMe from '../../search-browse/vx-notify-me/vx-notify-me.vue';
import globals from '../../common/globals';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  eventBus
} from '../../../modules/event-bus';
export default {
  name: 'vx-notify-button',
  components: {
    vxModal,
    vxNotifyMe,
    vxSpinner
  },
  mixins: [flyoutBannerMixin],
  data() {
    return {
      globals,
      productCode: ""
    }
  },
  props: {
    i18n: Object
  },
  mounted(){
    const self = this;
    eventBus.$on("ymktNotifyButton", (product) => {
      if(product.code){
        self.productCode = product.code;
      }
      self.handleNotifyMe();
    });
  },
  methods: {
    handleNotifyMe(event) {
      if (this.globals.loggedIn) {
        this.$refs.notifyMeModal.open(event);
      } else {
        this.navigateToLogin();
      }
    },
    onNotifyMeSuccess(success) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('success', success, true);
    },
    onNotifyMeError(error) {
      this.$refs.notifyMeModal.close();
      this.showFlyout('error', error, true);
    },
    navigateToLogin() {
      const url = `${this.globals.getNavBaseUrl()}/login?site=${this.globals.siteId}`;
      window.location = url;
    },
  }
}
