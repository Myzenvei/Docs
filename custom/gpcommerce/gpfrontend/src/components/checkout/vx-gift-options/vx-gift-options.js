import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import globals from '../../common/globals';
import CheckoutService from '../../common/services/checkout-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  checkoutEventBus,
} from '../../../modules/event-bus';

export default {
  name: 'vx-gift-options',
  components: {
    vxProductTile,
    vxSpinner,
  },
  props: {
    giftableProducts: Array,
    i18n: Object,
    sectionIndex: Number,
  },
  data() {
    return {
      isEditable: true,
      globals,
      giftWrapDetails: [],
      dataLoaded: false,
      checkoutService: new CheckoutService(),
    };
  },
  computed: {},
  mounted() {
    this.giftWrapData();
    this.showSavedData();
  },
  methods: {
    editMethod(event) {
      this.isEditable = true;
      checkoutEventBus.$emit('section-edit', 'giftOptions');
    },
    // create an object that stores the user input
    giftWrapData() {
      this.giftWrapDetails = [];
      this.giftableProducts.map((item) => {
        this.giftWrapDetails.push({
          entryNumber: item.entryNumber,
          giftWrapSelected: item.giftWrapped,
          giftWrapMessage: item.giftMessage || '',
          giftWrapCost: this.getKey(item.additionalAttributes.entry, 'giftableProductPrice').value,
        });
      });
      this.dataLoaded = true;
    },
    // show data if any present in the cart call
    showSavedData() {
      this.giftWrapDetails.map((item) => {
        const self = this;
        if (item.giftWrapSelected) {
          self.isEditable = false;
          checkoutEventBus.$emit('section-complete', 'giftOptions');
          checkoutEventBus.$emit('gift-saved', this.giftWrapDetails);
        } else {
          self.isEditable = true;
          checkoutEventBus.$emit('section-edit', 'giftOptions');
        }
      });
    },
    getKey(entry, keyCode) {
      return _.find(entry, {
        key: keyCode,
      });
    },
    createRequestBody() {
      const requestBody = {};
      requestBody.giftWrap = [];
      this.giftableProducts.map((item, index) => {
        requestBody.giftWrap.push({
          entryNumber: item.entryNumber,
          giftMessage: this.giftWrapDetails[index].giftWrapMessage || '',
          giftWrapped: this.giftWrapDetails[index].giftWrapSelected || false,
        });
      });
      return requestBody;
    },
    // save gift options
    saveGiftWrap(event) {
      this.$refs.spinner.showSpinner();
      const requestConfig = {};
      requestConfig.data = this.createRequestBody();
      this.checkoutService.saveGift(requestConfig, this.handleSaveGiftResponse, this.handleSaveGiftError);
    },
    handleSaveGiftResponse() {
      this.$refs.spinner.hideSpinner();
      this.isEditable = false;
      checkoutEventBus.$emit('update-cart');
      checkoutEventBus.$emit('section-complete', 'giftOptions');
      checkoutEventBus.$emit('gift-saved', this.giftWrapDetails);
    },
    handleSaveGiftError() {
      this.isEditable = true;
      this.$refs.spinner.hideSpinner();
    },
    getAddress(item) {
      let address = '';
      if (item.line2) {
        address = `${item.line1}, ${item.line2}, ${item.town}, ${item.postalCode}`;
      } else {
        address = `${item.line1}, ${item.town}, ${item.postalCode}`;
      }
      return address;
    },
  },
};
