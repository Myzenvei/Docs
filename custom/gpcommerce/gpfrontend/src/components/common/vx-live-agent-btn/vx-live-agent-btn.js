import globals from '../../common/globals';

export default {
  name: 'vx-live-agent-btn',
  components: {},
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    allPageLiveChat: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      liveAgentConfig: globals.thirdPartyApps.liveAgent,
      globals,
      showButtons: false,
    };
  },
  computed: {

  },
  /* eslint-disable */
  created() {
    if (this.allPageLiveChat) {
      liveagent.init(
        this.liveAgentConfig.endPoint,
        this.liveAgentConfig.orgId,
        this.liveAgentConfig.deploymentId,
      );
    }
  },
  mounted() {
    if (this.allPageLiveChat) {
      const self = this;
      if (!window._laq) {
        window._laq = [];
      }
      window._laq.push(function () {
        liveagent.showWhenOnline(
          self.liveAgentConfig.buttonId,
          self.$refs.chatAllPageOnline,
        );
        liveagent.showWhenOffline(
          self.liveAgentConfig.buttonId,
          self.$refs.chatAllPageOffline,
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
  /* eslint-enable */
};
