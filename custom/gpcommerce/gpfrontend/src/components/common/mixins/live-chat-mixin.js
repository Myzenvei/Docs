const liveChatMixin = {
    created() {
        liveagent.init(
            this.liveAgentConfig.endPoint,
            this.liveAgentConfig.orgId,
            this.liveAgentConfig.deploymentId,
        );
    },
    mounted() {
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
    },
    methods: {
        triggerChat(event) {
            liveagent.startChat(this.liveAgentConfig.buttonId);
        },
    },
}

export default liveChatMixin;