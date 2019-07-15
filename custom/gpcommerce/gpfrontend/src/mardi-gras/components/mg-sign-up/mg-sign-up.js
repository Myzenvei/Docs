import vxDropdownPrimary from '../../../components/common/vx-dropdown-primary/vx-dropdown-primary.vue';
import {
  Validator
} from 'vee-validate';
import globals from '../../../components/common/globals';
import ViewSiteContentService from '../../../components/common/services/view-site-content-service';

export default {
  name: 'mg-sign-up',
  components: {
    vxDropdownPrimary,
  },
  props: {
    i18n: {
      type: Object,
    },
    firstEmail: {
      type: String,
    },
    isHideAge: {
      type: Boolean,
      default: false,
    },
    pageUid: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      globals,
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
      ageList: [],
      margediGrasService: new ViewSiteContentService(),
    };
  },
  computed: {},
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          emailID: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailFormatError,
          },
          ConfirmEmail: {
            confirmed: this.i18n.confirmEmailError
          },
          firstName: {
            required: this.i18n.firstNameRequiredError
          },
          lastName: {
            required: this.i18n.lastNameRequiredError
          },
          postalCode: {
            required: this.i18n.postalCodeRequiredError,
            numeric: this.i18n.postalCodeNumericError,
          },
          age: {
            required: this.i18n.ageRequiredError
          }
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    this.signUpModel.email = this.firstEmail;
    this.signUpModel.confirmEmail = this.firstEmail;
    this.prepareAgeDropDown();
  },
  methods: {
    submitForm() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          this.signUpModel = {
            ...this.signUpModel,
            fromPage: this.pageUid,
            opt: this.signUpModel.opt ? 'Y' : 'N',
          };
          requestConfig.data = this.signUpModel;
          this.margediGrasService.saveCustomerData(
            requestConfig,
            this.handleSaveCustomerResponse,
            this.handleSaveCustomerError,
          );
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      })
    },
    handleSaveCustomerResponse(response) {
      this.$emit('customer-added', response);
    },
    handleSaveCustomerError(response) {
      this.$emit('customer-added', response);
    },
    prepareAgeDropDown() {
      for (let i = 18; i <= 100; i++) {
        const obj = {};
        obj.label = i;
        obj.value = i;
        this.ageList.push(obj);
      }
    },
  },
};
