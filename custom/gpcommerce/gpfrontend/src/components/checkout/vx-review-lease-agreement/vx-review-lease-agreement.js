import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import CheckOutService from '../../common/services/checkout-service';

import {
  country,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-review-lease-agreement',
  components: {
    vxSpinner,
  },
  props: {
    i18n: Object,
    country: Object,
    leaseAccepted: Boolean,
    orderDetails: Boolean,
  },
  data() {
    return {
      language: 'en',
      globals,
      checkOutService: new CheckOutService(),
      isActive: true,
      leaseAgrementData: {},
      countryEnum: country,
      legalAgreementName: '',
      legalAgreementContent: '',
      leaseAcceptedBtn: false,
    };
  },

  computed: {},
  mounted() {
    this.getLeaseAgrement();
  },
  methods: {
    agreementDecision(agreementDecision, event) {
      const agreementDetails = {
        agreementDecision,
        id: this.leaseAgrementData.legalTermsId,
      };
      this.$emit('onAcceptingTerms', agreementDetails);
    },
    getLeaseAgrement() {
      this.language = this.language.toLowerCase();
      const setLanguage = `${this.country.isocode}_${this.language}`;
      this.checkOutService.leaseAgreement({}, this.handleLeaseAgrementLanguageResponse, this.handleLeaseAgrementLanguageError, setLanguage);
      this.$refs.spinner.showSpinner();
    },
    handleLeaseAgrementLanguageError(error) {
      this.$refs.spinner.hideSpinner();
    },
    handleLeaseAgrementLanguageResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response) {
        this.$nextTick(() => {
          this.leaseAgrementData = response.data;
          this.legalAgreementName = response.data.legalTermName;
          this.legalAgreementContent = response.data.legalTermsText;
        });
      }
    },
    toggleLanguage(event) {
      // Check value
      if (this.isActive) {
        this.isActive = false;
      } else {
        this.isActive = true;
      }
      this.language = event.target.textContent;
      this.getLeaseAgrement();
    },
    agreementAccepted(event) {
      this.$emit('acceptedTerms');
    },
  },
};
