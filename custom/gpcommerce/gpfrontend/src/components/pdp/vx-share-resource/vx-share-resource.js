import globals from '../../common/globals';
import PdpService from '../../common/services/pdp-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import { Validator } from 'vee-validate';
import { eventBus } from '../../../modules/event-bus';
import mobileMixin from '../../common/mixins/mobile-mixin';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

const defaultRecipients = [];
export default {
  name: 'vx-share-resource',
  mixins: [mobileMixin, detectDeviceMixin],
  components: {
    vxSpinner,
  },
  props: {
    /**
     * data of resource
     */
    resource: {
      type: Object,
      default: {},
    },
    /**
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
  },
  data() {
    return {
      globals,
      pdpService: new PdpService(),
      recipients: defaultRecipients,
      recipientValue: defaultRecipients.join(),
      senderName: '',
      senderEmail: '',
      userComments: '',
      copyMe: false,
      screenHeight: window.innerHeight,
      isBtnSticky: true,
      embedURL: '',
    };
  },
  watch: {
    recipientValue(val) {
      this.recipients = val.split(',').map(value => value.trim());
    },
  },
  computed: {
    constructedShareUrl: {
      get() {
        if (this.resource.defaultResourceURL) {
          return this.constructShareUrl(
            this.resource.defaultResourceURL,
            this.resource.resourceId,
          );
        }
      },
    },
  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          recipients: {
            required: this.i18n.error.emailToRequired,
            email: this.i18n.error.emailToError,
          },
          senderName: {
            required: this.i18n.error.senderNameRequired,
            min: this.i18n.error.senderNameError,
          },
          senderEmail: {
            required: this.i18n.error.emailFromRequired,
            email: this.i18n.error.emailFromError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    this.isBtnSticky = this.isSticky();
  },
  methods: {
    /**
     * This function is called on click of share button
     */
    onSubmit() {
      const self = this;
      self.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.data = self.createRequestbody();
          self.pdpService.shareResource(
            requestConfig,
            this.handleShareResourceResponse,
            this.handleShareResourceError,
          );
          this.$refs.shareResourceSpinner.showSpinner();
        } else {
          this.globals.setFocusByName(
            this.$el,
            this.globals.getElementName(this.errors),
          );
        }
      });
    },
    /**
     * This function creates request body
     */
    createRequestbody() {
      const requestBody = {
        recipientEmails: this.recipients.toString(),
        senderEmail: this.globals.getIsLoggedIn()
          ? this.globals.userInfo.email
          : this.senderEmail,
        senderName: this.globals.getIsLoggedIn()
          ? `${this.globals.userInfo.firstName} ${
            this.globals.userInfo.lastName
          }`
          : this.senderName,
        senderMessage: this.userComments,
        resourceTitle: this.resource.altText,
        resourceDescription: this.resource.description,
        imgurl: this.resource.url,
        resourcePageUrl: this.resource.defaultResourceURL
          ? this.constructShareUrl(
            this.resource.defaultResourceURL,
            this.resource.resourceId,
          )
          : this.resource.resourceURL,
        embeddedLink: '',
        checkBoxSelected: this.copyMe,
      };
      return requestBody;
    },
    /**
     * This function handles the response of share resource service
     */
    handleShareResourceResponse(response) {
      this.$refs.shareResourceSpinner.hideSpinner();
      if (response) {
        this.$emit('share-resource-success', this.i18n.shareResourceSuccessMsg);
      }
    },
    /**
     * This function handles the error of share resource service
     */
    handleShareResourceError(error) {
      this.$refs.shareResourceSpinner.hideSpinner();
      if (error) {
        // this.$emit('share-resource-error', error.response.data.errors[0].message);
      }
    },
    /**
     * This function is called if the resource is a video
     */
    isYoutubeVideo(resource) {
      if (
        resource.mimeType == 'Video' &&
        (resource.resourceURL.includes('youtube') ||
          resource.resourceURL.includes('youtu.be'))
      ) {
        // Fetching Video Id for embed url
        const url = /(?:youtube(?:-nocookie)?\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})\W?/i.exec(
          resource.resourceURL,
        );
        if (url && url[1]) {
          this.embedURL = `https://www.youtube.com/embed/${url[1]}`;
        }
        return true;
      }
      return false;
    },
    /**
     * This function sets focus for input
     */
    inputFocusIn() {
      const self = this;
      setTimeout(() => {
        if (self.isMobile() && window.innerHeight < self.screenHeight) {
          document.activeElement.scrollIntoView(true);
        }
      }, 200);
    },
    /**
     * This function sets button sticky
     */
    isSticky() {
      const self = this;
      if (
        document.querySelector('.modal-body').offsetHeight >
        self.screenHeight - 120
      ) {
        return false;
      }
      return true;
    },
    constructShareUrl(dummyUrl, id) {
      const actualUrl = dummyUrl.replace('xxxx', id);
      return actualUrl;
    },
  },
};
