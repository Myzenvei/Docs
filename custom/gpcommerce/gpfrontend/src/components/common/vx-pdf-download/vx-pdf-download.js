import vxDropdownPrimary from '../vx-dropdown-primary/vx-dropdown-primary.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  TheMask
} from 'vue-the-mask';
import {
  Validator
} from 'vee-validate';
import globals from '../globals';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import {
  cookies,
  pdfColors,
} from '../../common/mixins/vx-enums';
import PdpService from '../../common/services/pdp-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-pdf-download',
  mixins: [cookiesMixin, detectDeviceMixin, mobileMixin],
  components: {
    vxDropdownPrimary,
    TheMask,
    globals,
    vxSpinner,
  },
  props: {
    i18n: Object,
    /**
     * Indicates whether it is PDP page
     */
    isPdp: {
      type: Boolean,
      default: false,
    },
    colorCodes: {
      type: Object,
      default: {},
    },
    productEntries: {
      type: Object,
      default: {},
    },
    certificationsName: {
      type: String,
    }
  },
  data() {
    return {
      globals,
      pdpService: new PdpService(),
      cookies,
      pdfColors,
      imgSrc: '',
      onlyOnFirstPage: false,
      featureChecked: [{
          label: this.i18n.featureCheckedNone,
          description: this.i18n.featureCheckedNoneDescription,
          value: this.i18n.featureCheckedNone,
        },
        {
          label: this.i18n.featureCheckedFull,
          description: this.i18n.featureCheckedFullDescription,
          value: this.i18n.featureCheckedFull,
        },
      ],
      displayFormat: [{
          label: this.i18n.oneColumnOption,
          description: this.i18n.oneColumnOptionDescription,
          value: this.i18n.oneColumnOption,
        },
        {
          label: this.i18n.twoColumnOption,
          description: this.i18n.twoColumnOptionDescription,
          value: this.i18n.twoColumnOption,
        },
        {
          label: this.i18n.threeColumnOption,
          description: this.i18n.threeColumnOptionDescription,
          value: this.i18n.threeColumnOption,
        },
        {
          label: this.i18n.fullDescptionOption,
          description: this.i18n.fullDescptionOptionDescription,
          value: this.i18n.fullDescptionOption,
        }
      ],
      radioButtonChecked: '',
      colorList: [],
      coverpage: {
        options: [{
            label: this.i18n.dropdownInputDefault,
            value: this.i18n.dropdownInputDefaultValue,
          },
          {
            label: this.i18n.foodServiceOptions,
            value: this.i18n.foodServicesOptionsValue,
          },
          {
            label: this.i18n.washRoomWiperOptions,
            value: this.i18n.washRoomWiperOptionsValue,
          },
          {
            label: this.i18n.perfecTouchCupsOptions,
            value: this.i18n.perfecTouchCupsOptionsValue,
          },
        ],
      },
      pdfInfo: {
        nameOnPdf: '',
        phoneNumber: '',
        emailAddress: '',
        message: '',
        barColor: '',
        formatColumns: this.i18n.oneColumnOption,
        categoryDescription: true,
        productSellingStatement: false,
        headlineLine1: '',
        headlineLine2: '',
        headlineColor: '',
        onlyOnFirstPage: false,
        coverPage: '',
        featureCheckedItems: this.i18n.featureCheckedNone,
        featureProducts: [],
        featureCheckedItemsValue: [],
        multiData: {},
        pdfCoverImages: {},
      },
      defaultColorDropdownValue: '#000000',
      selectFeatureItems: [],
      featureCheckedItems: this.i18n.featureCheckedNone,
      formatColumns: this.i18n.oneColumnOption,
    };
  },
  computed: {

  },
  mounted() {
    if (!this.isPdp) {
      this.productEntries.map((item) => {
        this.selectFeatureItems.push({
          label: item.product.name,
          value: item.product.code,
        })
      });
    }
    const veeCustomErrorMessage = {
      en: {
        custom: {
          phoneNumber: {
            min: this.i18n.phoneMinError,
          },
          emailAddress: {
            email: this.i18n.emailInvalidError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    this.handleColorCodeData();
    const requestConfig = {};
    this.$refs.barColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
    this.$refs.headlineColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
    // Reading Cookie to pre-populate fields in the modal
    if (!this.globals.loggedIn) {
      if (this.readCookie(this.cookies.pdfDetails)) {
        this.pdfInfo = JSON.parse(this.readCookie(this.cookies.pdfDetails));
        this.pdfInfo.phoneNumber = this.pdfInfo.phoneNumber && this.pdfInfo.phoneNumber.indexOf(1) === 0 ? `1${this.pdfInfo.phoneNumber}` : this.pdfInfo.phoneNumber;
        if (!this.pdfInfo.barColor) {
          this.$refs.barColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
        }
        if (!this.pdfInfo.headlineColor) {
          this.$refs.headlineColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
        }
      }
      this.pdfInfo.isPdp = this.isPdp;
      if (!this.isPdp) {
        this.$refs.spinner.showSpinner();
        this.pdpService.getSavedPdfDetails(
          requestConfig,
          this.handleGetSavedPdfGuestResponse,
          this.handleGetSavedPdfDetailsError,
          this.certificationsName,
        );
      }
    } else {
      this.$refs.spinner.showSpinner();
      this.pdpService.getSavedPdfDetails(
        requestConfig,
        this.handleGetSavedPdfDetailsResponse,
        this.handleGetSavedPdfDetailsError,
        this.certificationsName,
      );
    }
  },
  methods: {
    handleGetSavedPdfGuestResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (!_.isEmpty(response.data)) {
        this.pdfInfo.pdfCoverImages = response.data.pdfCoverImages;
        this.pdfInfo.gpCertificationsImages = response.data.gpCertificationsImages;
        this.pdfInfo.headerLogo = response.data.headerLogo;
      }
    },
    radioButtonOptionChange(item) {
      this.pdfInfo.formatColumns = item.value;
    },
    radioButtonOptionFeature(item) {
      this.pdfInfo.featureCheckedItems = item.value;
    },
    handleSelectFeatureValue() {
      this.$refs.selectFeatureDropdown.setDropdownLabel(this.i18n.featureDropdownDefault);
    },
    handleCoverPageDefaultValue() {
      this.$refs.coverPageDropdown.setDropdownLabel(this.i18n.dropdownInputDefault);
    },
    handleCoverPageValue(evt) {
      this.pdfInfo.coverPage = evt.value;
      this.imgSrc = `${globals.assetsPath}images/${evt.value}.png`;
    },
    imgSrcAlt() {
      this.imgSrc = '';
    },
    handleBarColorValue(evt) {
      this.pdfInfo.barColor = evt.value;
    },
    handleHeadlineColorValue(evt) {
      this.pdfInfo.headlineColor = evt.value;
    },
    handleColorCodeData() {
      var keyArray = Object.keys(this.colorCodes);
      keyArray.map((colorHex) => {
        var color = {};
        color.label = this.colorCodes[colorHex];
        color.value = colorHex;
        this.colorList.push(color);
      });
    },
    handleResetLink() {
      const formatColumns = this.i18n.oneColumnOption;
      const featureCheckedItems = this.i18n.featureCheckedNone;
      const pdfCoverImages = this.pdfInfo.pdfCoverImages;
      const gpCertificationsImages = this.pdfInfo.gpCertificationsImages;
      this.pdfInfo = {
        nameOnPdf: '',
        phoneNumber: '',
        emailAddress: '',
        message: '',
        barColor: '',
        formatColumns,
        categoryDescription: true,
        productSellingStatement: false,
        headlineLine1: '',
        headlineLine2: '',
        headlineColor: '',
        onlyOnFirstPage: false,
        coverPage: '',
        featureCheckedItems,
        pdfCoverImages,
        gpCertificationsImages,
        featureCheckedItemsValue: [],
      };
      this.$refs.selectFeatureDropdown.setMultiSelectDropdownValue(this.pdfInfo.featureCheckedItemsValue, this.productEntries);
      this.handleSelectFeatureValue();
      // this.$refs.barColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
      this.$refs.headlineColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
      if (this.$refs.coverPageDropdown) {
        this.$refs.coverPageDropdown.setDropdownLabel(this.i18n.dropdownInputDefault);
      }
      this.imgSrc = '';
    },
    submitForm() {
      const self = this;
      if (!this.isPdp) {
        this.pdfInfo.multiData = this.$refs.selectFeatureDropdown.getMultiSelectOptions();
        this.pdfInfo.featureCheckedItemsValue = this.pdfInfo.multiData.map((product) => {
          return product.value;
        });
      }
      if (!self.globals.loggedIn) {
        if (self.readCookie(self.cookies.pdfDetails)) {
          self.eraseCookie(self.cookies.pdfDetails);
        }
        // Storing the value for download pdf modal
        self.createCookie(this.cookies.pdfDetails, JSON.stringify(this.pdfInfo));
      } else {
        const requestBody = {
          pdfName: this.pdfInfo.nameOnPdf,
          phoneNumber: this.pdfInfo.phoneNumber,
          senderMessage: this.pdfInfo.message,
          barColor: this.pdfInfo.barColor,
          largeHeading: this.pdfInfo.headlineLine1,
          headLineColor: this.pdfInfo.headlineColor,
          mediumHeading: this.pdfInfo.headlineLine2,
          emailId: this.pdfInfo.emailAddress,
          listFormat: this.pdfInfo.formatColumns,
          isCategoryDescription: this.pdfInfo.categoryDescription,
          isProductSellingStatement: this.pdfInfo.productSellingStatement,
          displayHeadlineFirstPageOnly: this.pdfInfo.onlyOnFirstPage,
          coverPage: this.pdfInfo.coverPage,
          featureCheckedItems: this.pdfInfo.featureCheckedItems,
          featureCheckedItemsValue: this.pdfInfo.featureCheckedItemsValue,
        };
        const requestConfig = {};
        requestConfig.data = requestBody;
        this.pdpService.savePdfDetails(
          requestConfig,
          this.handleSavePdfDetailsResponse,
          this.handleSavePdfDetailsError,
        );
      }
      this.$validator.validateAll().then((result) => {
        if (result) {
          this.pdfInfo.featureProducts = this.pdfInfo.featureCheckedItemsValue;
          self.$emit('onPdfDownloadInit', this.pdfInfo);
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    handleSavePdfDetailsResponse() {},
    handleSavePdfDetailsError() {},
    handleGetSavedPdfDetailsResponse(response) {
      if (!this.isPdp) {
        const contactAdd = response.data.contactAddress;
        if (contactAdd) {
          this.pdfInfo.unitCompany = contactAdd.companyName || '';
          const unitAdd = [
            ...contactAdd.line1 ? [contactAdd.line1] : [],
            ...contactAdd.line2 ? [contactAdd.line2] : [],
            ...contactAdd.town ? [contactAdd.town] : [],
            ...contactAdd.region ? [contactAdd.region.name] : [],
            ...contactAdd.postalCode ? [contactAdd.postalCode] : [],
          ];
          this.pdfInfo.unitAddress = unitAdd.join(',');
          this.pdfInfo.b2bphoneNumber = contactAdd.phone;
          this.pdfInfo.b2bWebsite = contactAdd.url;
        }
        this.pdfInfo.distributorImage = response.data.distributorImage;
        this.pdfInfo.headerText = response.data.headerText;
        this.pdfInfo.headerLogo = response.data.headerLogo;
      }
      this.pdfInfo.isPdp = this.isPdp;
      this.$refs.spinner.hideSpinner();
      const emptydataCheck = _.isEmpty(response.data);
      if (emptydataCheck) {
        this.pdfInfo.nameOnPdf = '';
        this.pdfInfo.phoneNumber = '';
        this.pdfInfo.message = '';
        this.pdfInfo.barColor = this.pdfColors.barColor;
        this.pdfInfo.headlineLine1 = '';
        this.pdfInfo.headlineColor = this.pdfColors.headlineColor;
        this.pdfInfo.headlineLine2 = '';
        this.pdfInfo.emailAddress = '';
        this.pdfInfo.formatColumns = 'Display in one column';
        this.pdfInfo.featureCheckedItems = 'None';
        this.pdfInfo.featureCheckedItemsValue = [];
        this.pdfInfo.categoryDescription = false;
        this.pdfInfo.productSellingStatement = false;
        this.pdfInfo.onlyOnFirstPage = false;
        this.pdfInfo.coverPage = '';
        if (!this.isPdp) {
          this.pdfInfo.featureCheckedItemsValue = !this.pdfInfo.featureCheckedItemsValue ? [] : this.pdfInfo.featureCheckedItemsValue;
          this.$refs.selectFeatureDropdown.setMultiSelectDropdownValue(this.pdfInfo.featureCheckedItemsValue, this.productEntries);
        }
        if (this.$refs.coverPageDropdown) {
          this.pdfInfo.coverPage = !this.pdfInfo.coverPage ? '' : this.pdfInfo.coverPage;
          this.$refs.coverPageDropdown.setDropdownValue(this.pdfInfo.coverPage);
        }
        if (!this.pdfInfo.barColor) {
          this.pdfInfo.barColor = !this.pdfInfo.barColor ? this.defaultColorDropdownValue : this.pdfInfo.barColor;
          this.$refs.barColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
        }
        if (!this.pdfInfo.headlineColor) {
          this.pdfInfo.headlineColor = !this.pdfInfo.headlineColor ? this.defaultColorDropdownValue : this.pdfInfo.headlineColor;
          this.$refs.headlineColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
        }
        this.pdfInfo.pdfCoverImages = response.data.pdfCoverImages;
        this.pdfInfo.gpCertificationsImages = response.data.gpCertificationsImages;
      } else {
        this.pdfInfo.nameOnPdf = response.data.pdfName;
        this.pdfInfo.phoneNumber = response.data.phoneNumber && response.data.phoneNumber.indexOf(1) === 0 ? `1${response.data.phoneNumber}` : response.data.phoneNumber;
        this.pdfInfo.message = response.data.senderMessage;
        this.pdfInfo.barColor = response.data.barColor;
        this.pdfInfo.headlineLine1 = response.data.largeHeading;
        this.pdfInfo.headlineColor = response.data.headLineColor;
        this.pdfInfo.headlineLine2 = response.data.mediumHeading;
        this.pdfInfo.emailAddress = response.data.emailId;
        this.pdfInfo.formatColumns = response.data.listFormat;
        this.pdfInfo.featureCheckedItems = response.data.featureCheckedItems;
        this.pdfInfo.featureCheckedItemsValue = response.data.featureCheckedItemsValue;
        this.pdfInfo.categoryDescription = response.data.isCategoryDescription;
        this.pdfInfo.productSellingStatement = response.data.isProductSellingStatement;
        this.pdfInfo.onlyOnFirstPage = response.data.displayHeadlineFirstPageOnly;
        this.pdfInfo.coverPage = response.data.coverPage;
        this.imgSrc = `${globals.assetsPath}images/${this.pdfInfo.coverPage}.png`;
        this.pdfInfo.pdfCoverImages = response.data.pdfCoverImages;
        if (!this.isPdp) {
          this.pdfInfo.featureCheckedItemsValue = !this.pdfInfo.featureCheckedItemsValue ? [] : this.pdfInfo.featureCheckedItemsValue;
          this.$refs.selectFeatureDropdown.setMultiSelectDropdownValue(this.pdfInfo.featureCheckedItemsValue, this.productEntries);
        }
        if (this.$refs.coverPageDropdown) {
          this.pdfInfo.coverPage = !this.pdfInfo.coverPage ? '' : this.pdfInfo.coverPage;
          this.$refs.coverPageDropdown.setDropdownValue(this.pdfInfo.coverPage);
        }
        if (!this.pdfInfo.barColor) {
          this.pdfInfo.barColor = !this.pdfInfo.barColor ? this.defaultColorDropdownValue : this.pdfInfo.barColor;
          this.$refs.barColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
        }
        if (!this.pdfInfo.headlineColor) {
          this.pdfInfo.headlineColor = !this.pdfInfo.headlineColor ? this.defaultColorDropdownValue : this.pdfInfo.headlineColor;
          this.$refs.headlineColorDropdown.setDropdownValue(this.defaultColorDropdownValue);
        }
        this.pdfInfo.pdfCoverImages = response.data.pdfCoverImages;
        this.pdfInfo.gpCertificationsImages = response.data.gpCertificationsImages;
      }
    },
    handleGetSavedPdfDetailsError() {},
  },
};
