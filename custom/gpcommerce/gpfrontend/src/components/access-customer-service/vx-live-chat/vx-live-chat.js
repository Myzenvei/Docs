import globals from '../../common/globals';

/**
 * Live Chat Component
 */

/* eslint-disable */
export default {
  name: 'vx-live-chat',
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    liveChatTheme: {
      type: String,
      required: false,
    },
    showContactUs: {
      type: Boolean,
      required: true,
      default: true,
    },
    imageTextData: {
      type: Object,
      required: false,
    },
    isLiveChatEnabled: {
      type: Boolean,
      default: false,
      required: true,
    },
    contactUsEmail: {
      type: String,
      default: '',
      required: false,
    }
  },
  data() {
    return {
      liveAgentConfig: globals.thirdPartyApps.liveAgent,
      globals,
      showButtons: false,
      splitParam: new RegExp(':\\s*'),
    };
  },
  computed: {
    callNumber() {
      return this.i18n.callNumber.split(this.splitParam)[1];
    },
    textNumber() {
      return this.i18n.textNumber.split(this.splitParam)[1];
    },
  },
  created() {
    if (this.isLiveChatEnabled) {
      liveagent.init(
        this.liveAgentConfig.endPoint,
        this.liveAgentConfig.orgId,
        this.liveAgentConfig.deploymentId,
      );
    }
  },
  mounted() {
    if (this.isLiveChatEnabled) {
      const self = this;
      if (!window._laq) {
        window._laq = [];
      }
      window._laq.push(function () {
        liveagent.showWhenOnline(
          self.liveAgentConfig.buttonId,
          self.$refs.chatOnline,
        );
        liveagent.showWhenOffline(
          self.liveAgentConfig.buttonId,
          self.$refs.chatOffline,
        );
      });
      if (this.globals.getIsLoggedIn()) {
        liveagent.addCustomDetail('UserFirstName', this.globals.userInfo.firstName);
        liveagent.addCustomDetail('UserLastName', this.globals.userInfo.lastName);
        liveagent.addCustomDetail('UserEmail', this.globals.userInfo.email);
        liveagent.addCustomDetail('UserCompany', this.globals.userInfo.unit);
        liveagent.addCustomDetail('UserPhone', this.globals.userInfo.contactNumber);
      }
    }
  },
  methods: {
    triggerChat(event) {
      liveagent.startChat(this.liveAgentConfig.buttonId);
    },
  },
};
/* eslint-enable */
