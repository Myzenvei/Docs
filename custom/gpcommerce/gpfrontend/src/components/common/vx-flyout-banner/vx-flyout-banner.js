import globals from '../globals';

import {
  globalEventBus,
} from '../../../modules/event-bus';
import {
  flyoutStatus
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-flyout-banner',
  components: {},
  props: {
    text: {
      type: String,
    },
  },
  data() {
    return {
      flyoutObject: {
        status: 'error',
        message: 'Some error occurred, please try again later',
        autoDismiss: false,
        timer: 5000
      },
      isPersisted: false,
      visible: false,
      globals,
      isCheckout: false,
      flyoutStatus,
      i18n: this.$root.messages.common.commonTitles,
    };
  },
  computed: {
    // For simplycity removed the flyout link from store
    // status() {
    //   return this.$store.state.flyoutStore.status;
    // },
    // flyoutVisible() {
    //   return this.$store.state.flyoutStore.flyoutVisible;
    // },
    // message() {
    //    return this.$store.state.flyoutStore.message;
    // }
  },
  created() {
    globalEventBus.$on('service-error', () => {
      this.flyoutObject.autoDismiss = true;
      this.showFlyout(this.flyoutObject);
    });
    globalEventBus.$on('is-checkout', (val) => {
      this.isCheckout = val;
    });
  },
  mounted() {
    // On Mounted check if the previos page has some Msg
    this.checkIsPersisted();
    this.showStorageMsg();
  },
  methods: {
    checkIsPersisted() {
      if (this.globals.getStorage('flyoutMsg')) {
        this.isPersisted = true;
      }
    },
    showStorageMsg() {
      if (this.isPersisted) {
        const flyoutObject = JSON.parse(this.globals.getStorage('flyoutMsg'));
        this.showFlyout(flyoutObject);
      }
    },
    deleteStorageMsg() {
      this.globals.deleteStorage('flyoutMsg');
    },
    showFlyout(flyoutObject) {
      this.flyoutObject = flyoutObject;
      globalEventBus.$emit('announce', this.flyoutObject.message);
      this.visible = true;
      if (flyoutObject.autoDismiss) {
        (function (self) {
          setTimeout(() => self.dismissFlyout(), flyoutObject.timer || 3000);
        })(this);
      }
    },
    // Shows msg on the next navigation page
    showFlyoutNextPage(flyoutObject) {
      this.globals.setStorage('flyoutMsg', JSON.stringify(flyoutObject));
    },
    dismissFlyout(event) {
      this.visible = false;
      if (this.isPersisted) this.deleteStorageMsg();
    },
  },
};
