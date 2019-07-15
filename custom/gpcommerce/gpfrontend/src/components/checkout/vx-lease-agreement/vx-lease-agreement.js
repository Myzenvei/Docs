/* Handles leasable section on checkout page */
import _ from 'lodash';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxReviewLeaseAgreement from '../vx-review-lease-agreement/vx-review-lease-agreement.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import globals from '../../common/globals';
import {
  checkoutEventBus
} from '../../../modules/event-bus';
import CheckOutService from '../../common/services/checkout-service';


export default {
  name: 'vx-lease-agreement',
  components: {
    vxModal,
    vxSpinner,
    vxReviewLeaseAgreement,
  },
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: {
      type: Object,
    },
    /**
     * Contains leasable products from checkout
     */
    checkoutData: Object,
    country: Array,
    sectionIndex: Number,
    countryQuantity: Object,
  },
  data() {
    return {
      leasableItems: [],
      globals,
      checkOutService: new CheckOutService(),
      isSaved: false,
      isEditable: true,
      quantity: 0,
      currentDate: '',
      leasableProductRequestData: [],
      leaseAccepted: true,
      currentCountry: Object,
    };
  },
  computed: {},
  mounted() {
    checkoutEventBus.$on('update-lease-products', (checkoutData) => {
      this.updateData(checkoutData);
    });
    this.leasableItems = _.filter(this.checkoutData.entries, {
      leasable: true,
    });
    this.quantity = _.sumBy(this.leasableItems, 'quantity');
    const todayDate = new Date();
    this.currentDate = todayDate.toISOString().substring(0, 10);
  },
  methods: {
    updateData(checkoutData) {
      this.leasableItems = _.filter(checkoutData.entries, {
        leasable: true,
      });
    },
    reviewAgreement(event, countryVal, currentIndex) {
      this.currentIndex = currentIndex;
      this.currentCountry = countryVal;
      this.$refs.viewTermsModal.open(event);
    },
    // editMethod(event) {
    //   this.isSaved = !this.isSaved;
    //   this.isEditable = !this.isEditable;
    //   checkoutEventBus.$emit('section-edit', 'leaseAgreement');
    //   this.showSaveButton = false;
    // },
    toggleViewMode(leaseInfo) {
      // this.$refs.viewTermsModal.close();
      if (leaseInfo.agreementDecision) {
        this.$refs.spinner.showSpinner();
        for (let i = 0; i < this.leasableItems.length; i += 1) {
          this.leasableProductRequestData.push({
            materialNo: this.leasableItems[i].product.code,
            materialDescription: this.leasableItems[i].product.name,
            quantity: this.leasableItems[i].quantity,
            unit: this.leasableItems[i].product.unit,
          });
        }
        const data = {
          orderDate: this.currentDate,
          leaseTermsID: leaseInfo.id,
          agreementID: `${this.currentCountry.isocode}_${this.checkoutData.code}`,
          material: this.leasableProductRequestData,
        };
        this.checkOutService.saveLeaseAgrement({}, this.handleSaveLeaseAgrementResponse, this.handleSaveLeaseAgrementError, data);
        this.$refs.spinner.showSpinner();
      } else {
        this.$set(this.country, this.currentIndex, {
          ...this.country[this.currentIndex],
          leaseAccepted: false,
        });
        this.isReviewAccepted();
        this.$refs.viewTermsModal.close();
      }
    },
    handleSaveLeaseAgrementError(error) {
      this.$refs.spinner.hideSpinner();
    },
    handleSaveLeaseAgrementResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.leaseAgrementData = response.data;
      this.$set(this.country, this.currentIndex, {
        ...this.country[this.currentIndex],
        leaseAccepted: true,
      });
      this.isReviewAccepted();
      this.$refs.viewTermsModal.close();
    },
    saveSection() {
      this.isSaved = true;
      this.isEditable = false;
      checkoutEventBus.$emit('section-complete', 'leaseAgreement');
    },
    acceptedTerms() {
      this.$refs.viewTermsModal.close();
    },
    getQuantity(country) {
      return _.sumBy(this.countryQuantity, country);
    },
    isReviewAccepted() {
      const filteredData = this.country.filter(item => !item.leaseAccepted);
      this.leaseAccepted = filteredData.length > 0;
    },
  },
};
