import globals from '../globals';

import {
  globalEventBus,
  eventBus,
} from '../../../modules/event-bus';
import {
  flyoutStatus
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-flyout-banner-secondary',
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
  created() {
    globalEventBus.$on('service-error', () => {
      this.flyoutObject.autoDismiss = true;
      this.showFlyoutSecondary(this.flyoutObject);
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
      if (this.globals.getStorage('flyoutMsgSecondary')) {
        this.isPersisted = true;
      }
    },
    showStorageMsg() {
      if (this.isPersisted) {
        const flyoutObject = JSON.parse(this.globals.getStorage('flyoutMsg'));
        this.showFlyoutSecondary(flyoutObject);
      }
    },
    deleteStorageMsg() {
      this.globals.deleteStorage('flyoutMsg');
    },
    showFlyoutSecondary(flyoutObject) {
      this.flyoutObject = flyoutObject;
      globalEventBus.$emit('announce', this.flyoutObject.message);
      this.visible = true;
      if (flyoutObject.autoDismiss) {
        (function (self) {
          setTimeout(() => self.dismissFlyoutSecondary(), flyoutObject.timer || 3000);
        })(this);
      }
    },
    // Shows msg on the next navigation page
    showFlyoutNextPageSecondary(flyoutObject) {
      this.globals.setStorage('flyoutMsg', JSON.stringify(flyoutObject));
    },
    dismissFlyoutSecondary(event) {
      this.visible = false;
      if (this.isPersisted) this.deleteStorageMsg();
    },
  },
};
