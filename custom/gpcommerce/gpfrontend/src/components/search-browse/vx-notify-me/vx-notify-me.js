import globals from '../../common/globals';
import SearchBrowseService from '../../common/services/search-browse-service';
import {
  Validator,
} from 'vee-validate';

/**
 * Component to notify user for out of stock products
 */

export default {
  name: 'vx-notify-me',
  components: {},
  props: [],
  data() {
    return {}
  },
  computed: {},
  mounted() {
    const veeCustomErrorMessage = {
      en: {
        custom: {
          notifyMe: {
            required: this.i18n.notifyError,
            email: this.i18n.notifyError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    this.form.productCode = this.productCode;
  },
  components: {},
  props: ['title', 'productCode', 'i18n'],
  data() {
    return {
      form: {
        EMAIL: true,
        notifyMeEmailId: '',
        productCode: '',
      },
      globals,
      searchBrowseService: new SearchBrowseService(),
    };
  },
  methods: {
    handleSubmit(event) {
      this.$validator.validateAll().then((result) => {
        if (result) {
          const requestConfig = {};
          requestConfig.params = {
            productCode: this.form.productCode,
            EMAIL: this.form.EMAIL,
            notifyMeEmailId: this.form.notifyMeEmailId,
          };
          requestConfig.data = this.form;
          this.searchBrowseService.notifyMe(requestConfig, this.handleSubmitResponse, this.handleSubmitError);
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    handleSubmitResponse(response) {
      if (response) {
        this.$refs.notifyMe.reset();
        this.$emit('notify-me-success', this.i18n.notifyMeResponse);
      }
    },
    handleSubmitError(error) {
      if (error) {
        this.$refs.notifyMe.reset();
        this.$emit('notify-me-error', error.response.data.errors[0].message);
      }
    },
  },
};
