import globals from '../../common/globals';
import PdpService from '../../common/services/pdp-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxPdfGenerator from '../../common/vx-pdf-generator/vx-pdf-generator.vue';
import { eventBus, globalEventBus } from '../../../modules/event-bus';
import { Validator } from 'vee-validate';
import { socialShareSize } from '../../common/mixins/vx-enums';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';

const DEFAULT_RECIPIENTS = [];

export default {
  name: 'vx-share-item',
  components: {
    vxSpinner,
    vxPdfGenerator,
  },
  props: {
    /**
     * url of the product
     */
    url: {
      type: String,
      default: '',
    },
    /**
     * Title of the Modal
     */
    title: {
      type: String,
      default: '',
    },
    /**
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
    /**
     * Id of the product
     */
    productCode: {
      type: String,
      default: '',
    },
    /**
     * indicates whether social share is enabled
     */
    isSocialShare: {
      type: Boolean,
      default: false,
    },
    /**
     * indicates whether bulk operations is enabled for site
     */
    isBulkEnabled: {
      type: Boolean,
      default: false,
    },
    /**
     * data of product
     */
    productInfo: {
      type: Object,
      default: {},
    },
    wishlistId: {
      type: String,
      default: '',
    },
    listId: {
      type: String,
      default: '',
    },
    isList: {
      type: Boolean,
      default: false,
    },
    certificationsName: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      globals,
      pdpService: new PdpService(),
      enableSocialShare: false,
      enableClick: false,
      recipients: DEFAULT_RECIPIENTS,
      recipientValue: DEFAULT_RECIPIENTS.join(),
      form: {
        recipientEmails: [],
        senderEmail: '',
        senderName: globals.getIsLoggedIn()
          ? `${globals.userInfo.firstName} ${globals.userInfo.lastName}`
          : '',
        subject: '',
        senderMessage: '',
        attachPDF: true,
        addLink: true,
        encodedString: '',
        products: [
          {
            url: this.url,
            code: this.productCode,
          },
        ],
      },
      socialShareSize,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      pdfData: {},
      pdfInfo: {},
      loadPdfGenerator: false,
    };
  },
  watch: {
    recipientValue(val, oldVal) {
      this.recipients = val.split(',').map(value => value.trim());
      if (this.recipients.length > 20) {
        this.recipientValue = oldVal;
      }
    },
  },
  created() {
    const addToAnyJs = document.createElement('script');
    addToAnyJs.setAttribute('src', this.globals.addToAny);
    addToAnyJs.setAttribute('async', 'true');
    document.head.appendChild(addToAnyJs);
  },
  computed: {
    socialShareSizeClass() {
      let iconSize = '';
      if (this.globals.isVanityfair() || this.globals.isCopperCrane()) {
        iconSize = this.socialShareSize.small;
      } else iconSize = this.socialShareSize.default;

      const sizeClass = `a2a_kit_size_${iconSize}`;
      return sizeClass;
    },
  },
  mounted() {
    if (this.globals.isB2B()) {
      const veeCustomErrorMessage = {
        en: {
          custom: {
            senderName: {
              required: this.i18n.senderNameRequired,
            },
            sender: {
              required: this.i18n.senderEmailRequired,
              email: this.i18n.senderEmailRequired,
            },
            subject: {
              required: this.i18n.subjectRequired,
            },
          },
        },
      };
      Validator.localize(veeCustomErrorMessage);
      if (this.isList) {
        // get pdf details if pdf share is called form list page
        this.form.subject = `${this.i18n.subjectText1} ${this.listId} ${
          this.i18n.subjectText2
        }`;
        const requestConfig = {};
        this.manageProfileShoppingListService.getListDetails(
          requestConfig,
          this.handleListDetailsSuccess,
          this.handleListDetailsError,
          this.wishlistId,
          true,
        );
        this.$refs.shareItemSpinner.showSpinner();
      } else {
        this.form.subject = `${this.i18n.subjectText1} ${this.productCode} ${
          this.i18n.subjectText2
        }`;
        this.loadPdfGenerator = true;
        this.pdfData = this.productInfo;
        const requestConfig = {};
        this.pdpService.getSavedPdfDetails(
          requestConfig,
          this.handlePdfInfoResponse,
          this.handlePdfInfoError,
          this.certificationsName,
        );
        this.$refs.shareItemSpinner.showSpinner();
      }
    }
    const self = this;
    eventBus.$on('show-social-share', (data) => {
      if (data) {
        self.showSocialShare();
      }
    });
    eventBus.$on('hide-social-share', (data) => {
      if (data) {
        self.enableSocialShare = false;
      }
    });
    if (this.globals.getIsLoggedIn()) {
      if (this.globals.isGpXpress()) {
        this.form.senderEmail = this.globals.userInfo.userEmail;
      } else {
        this.form.senderEmail = this.globals.userInfo.email;
      }
    }
  },
  methods: {
    /**
     * This function is triggered on the click of submit button
     */
    onSubmit(event) {
      const self = this;
      if (event) {
        this.$validator.validateAll().then((result) => {
          if (result) {
            this.form.recipientEmails = this.recipients;
            if (this.isBulkEnabled) {
              globalEventBus.$emit('bulkShare', this.form);
              return;
            }
            if (this.form.attachPDF) {
              if (this.isList) {
                // this function creates pdf for list
                this.form.encodedString = this.$refs.pdfModule.createTwoColumnFormatShare(
                  this.pdfInfo,
                );
              } else {
                // this function creates pdf for product
                this.form.encodedString = this.$refs.pdfModule.createPdfPDPBase64(
                  this.pdfInfo,
                );
              }
            } else {
              this.form.encodedString = '';
            }
            const requestConfig = {};
            requestConfig.data = this.form;
            if (this.isList) {
              this.$delete(requestConfig.data, 'products');
              requestConfig.params = {
                wishlistId: this.wishlistId,
              };
              // this is the pdf list share call
              this.manageProfileShoppingListService.shareList(
                requestConfig,
                this.handleShareResponse,
                this.handleShareError,
                this.wishlistId,
                true,
              );
            } else {
              // this is the pdf product share call
              self.pdpService.shareItem(
                requestConfig,
                this.handleShareResponse,
                this.handleShareError,
              );
            }
            this.$refs.shareItemSpinner.showSpinner();
          } else {
            this.globals.setFocusByName(
              this.$el,
              this.globals.getElementName(this.errors),
            );
          }
        });
      }
    },
    /**
     * This function toggles the social share icons
     */
    showSocialShare() {
      this.enableSocialShare = !this.enableSocialShare;
    },
    /**
     * This function handles the response of share service which emits call success
     */
    handleShareResponse(response) {
      this.$refs.shareItemSpinner.hideSpinner();
      if (response) {
        this.$refs.shareItemForm.reset();
        this.$emit('share-item-success', this.i18n.shareItemResponse);
      }
    },
    /**
     * This function handles the error of share service which emits call error
     */
    handleShareError(error) {
      this.$refs.shareItemSpinner.hideSpinner();
      if (error) {
        this.$refs.shareItemForm.reset();
        this.$emit('share-item-error', error.response.data.errors[0].message);
      }
    },
    /**
     * This function limits the characters in the data
     */
    limitCharacters(data) {
      let newData = data.length > 500 ? `${data.substring(0, 500)}...` : data;
      newData = this.replaceText(newData);
      return newData;
    },
    /**
     * This function replaces text
     */
    replaceText(text) {
      const mapObj = {
        '™': '<sup>(TM)</sup>',
        '’': "'",
        '–': '-',
        '<br>': '',
        '”': '"',
      };
      const newText = text.replace(
        /™|’|–|<br>|”/gi,
        matched => mapObj[matched],
      );
      return newText;
    },
    /**
     * This function handles the response of list pdf details call
     */
    handleListDetailsSuccess(response) {
      this.$refs.shareItemSpinner.hideSpinner();
      if (response) {
        this.loadPdfGenerator = true;
        this.pdfData = response.data;
        const requestConfig = {};
        // This function gets the images of the product certificates
        this.pdpService.getSavedPdfDetails(
          requestConfig,
          this.handlePdfInfoResponse,
          this.handlePdfInfoError,
          this.certificationsName,
        );
        this.$refs.shareItemSpinner.showSpinner();
      }
    },
    /**
     * This function handles the error of list pdf details call
     */
    handleListDetailsError() {
      this.$refs.shareItemSpinner.hideSpinner();
    },
    /**
     * This function handles the response of product certificate call
     */
    handlePdfInfoResponse(response) {
      this.$refs.shareItemSpinner.hideSpinner();
      if (response.data) {
        this.pdfInfo = {
          categoryDescription: true,
          productSellingStatement: true,
          gpCertificationsImages: response.data.gpCertificationsImages,
          headerLogo: response.data.headerLogo,
          isPdp: !this.isList,
          distributorImage: response.data.distributorImage,
        };
      }
    },
    /**
     * This function handles the error of product certificate call
     */
    handlePdfInfoError() {
      this.$refs.shareItemSpinner.hideSpinner();
    },
  },
};
