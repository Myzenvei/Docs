
import { Validator } from 'vee-validate';
import globals from '../../../components/common/globals';
import ViewSiteContentService from '../../../components/common/services/view-site-content-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-qples-sign-up',
  components: {
    vxSpinner,
  },
  props: {
    i18n: {
      type: Object,
    },
    firstEmail: {
      type: String,
    },
  },
  data() {
    return {
      globals,
      vanityFairService: new ViewSiteContentService(),
      signUpModel: {
        email: '',
        confirmEmail: '',
        firstName: '',
        lastName: '',
        postalCode: '',
        address1: '',
        address2: '',
        opt: '',
        age: '',
        fromPage: '',
      },
    };
  },
  computed: {

  },
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          emailID: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailFormatError,
          },
          ConfirmEmail: {
            confirmed: this.i18n.confirmEmailError,
          },
          firstName: {
            required: this.i18n.firstNameRequiredError,
          },
          lastName: {
            required: this.i18n.lastNameRequiredError,
          },
          postalCode: {
            required: this.i18n.postalCodeRequiredError,
            numeric: this.i18n.postalCodeNumericError,
          },
          age: {
            required: this.i18n.ageRequiredError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    this.signUpModel.email = this.firstEmail;
    this.signUpModel.confirmEmail = this.firstEmail;
  },
  methods: {
    submitForm() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          this.$refs.spinner.showSpinner();
          const requestConfig = {};
          this.signUpModel = {
            ...this.signUpModel,
            fromPage: this.pageUid,
            opt: this.signUpModel.opt ? 'Y' : 'N',
          };
          requestConfig.data = this.signUpModel;
          this.vanityFairService.saveCustomerData(
            requestConfig,
            this.handleSaveCustomerResponse,
            this.handleSaveCustomerError,
          );
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    handleSaveCustomerResponse(response) {
      this.$emit('customer-added', response);
    },
    handleSaveCustomerError(response) {
      this.$emit('customer-added', response);
    },
  },
};
