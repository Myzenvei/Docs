// import shippingMethodDetails from './vx-multiple-shipping-method-mock';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxProductTile from '../../common/vx-product-tile/vx-product-tile.vue';
import globals from '../../common/globals';
import vxAccordion from '../../common/vx-accordion/vx-accordion.vue';
import CheckoutService from '../../common/services/checkout-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  checkoutEventBus,
} from '../../../modules/event-bus';
import {
  ProductAvailability,
  discount
} from '../../common/mixins/vx-enums';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxProductIcons from '../../common/vx-product-icons/vx-product-icons.vue';

export default {
  name: 'vx-multiple-shipping-method',
  mixins: [mobileMixin],
  components: {
    vxDropdownPrimary,
    vxProductTile,
    vxAccordion,
    vxSpinner,
    vxProductIcons,
  },
  props: {
    isEditable: Boolean,
    i18n: Object,
    checkoutData: Object,
    splitApplicable: Boolean,
    addressChanged: Boolean,
  },
  data() {
    return {
      selectedShippingMethod: [],
      shippingMethodDetails: {},
      globals,
      formattedEntries: {},
      ProductAvailability,
      checkoutService: new CheckoutService(),
      dataLoaded: false,
      discount,
    };
  },
  computed: {},
  mounted() {
    this.getShippingMethod();
    checkoutEventBus.$on('cart-entries-count-updated', () => {
      this.getShippingMethod();
    });
  },
  methods: {
    // get Shipping method response and data
    getShippingMethod() {
      this.checkoutService.getMultipleMethod({}, this.handleGetMethodResponse, this.handleGetMethodError);
      this.$refs.spinner.showSpinner();
    },
    handleGetMethodResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        this.shippingMethodDetails = response.data;
        this.selectedShippingMethodData();
        this.showSavedData();
        this.formattedEntries = this.arrayToObject(this.checkoutData.entries, 'entryNumber');
      }
    },
    handleGetMethodError(error) {
      this.$refs.spinner.hideSpinner();
    },
    // creating array for shipping method dropdown
    createDeliveryMethodArray(array) {
      const deliveryMethods = [];
      array.map((item) => {
        deliveryMethods.push({
          label: `${item.name} : ${item.deliveryCost.formattedValue}`,
          value: item.code,
          // deliveryDetails: item.description,
        });
      });
      return deliveryMethods;
    },
    // get the count of products for each address group
    getProductCount(array) {
      let productCount = 0;
      array.value.splitEntries.map((item) => {
        productCount += Number(item.qty);
      });
      return productCount;
    },
    // Object that stores everything that user selects
    selectedShippingMethodData() {
      this.selectedShippingMethod = [];
      this.shippingMethodDetails.deliveryGroup.map((item) => {
        this.selectedShippingMethod.push({
          // value: item.value.deliveryMode.code || '',
          value: this.setValue(item.value),
          instructions: item.value.deliveryInstruction || '',
          deliveryDetails: '',
          label: '',
        });
      });
      this.dataLoaded = true;
    },
    setValue(item) {
      let value = '';
      if (item.deliveryMode) {
        value = item.deliveryMode.code;
      }
      return value;
    },
    // Show the data that the user has selected if service has the saved data
    showSavedData() {
      let isDataAvailable = false;
      this.$nextTick(function () {
        this.shippingMethodDetails.deliveryGroup.map((item, index) => {
          const self = this;
          if (item.value.deliveryMode && !this.addressChanged) {
            checkoutEventBus.$emit('show-saved-mode');
            isDataAvailable = true;
            const dropdownValues = self.$refs.deliveryMethodDropdown[index].dropdownValues;
            dropdownValues.map((value) => {
              if (value.value === item.value.deliveryMode.code) {
                this.selectedShippingMethod[index].value = value.value;
                this.selectedShippingMethod[index].label = value.label;
                this.selectedShippingMethod[index].deliveryDetails = value.deliveryDetails;
              }
            });
          } else {
            checkoutEventBus.$emit('show-edit-mode');
          }
        });
        if (isDataAvailable) {
          this.saveMultipleMethod();
        }
      });
    },
    // storing the shipping method dropdown values
    handleSelected(event, index) {
      this.selectedShippingMethod[index].value = event.value;
      this.selectedShippingMethod[index].deliveryDetails = event.deliveryDetails;
      this.selectedShippingMethod[index].label = event.label;
    },
    // set the lowest shipping method as default or if the service
    // returns the value selected by user then show that
    handleDefault(index) {
      const dropdownValues = this.$refs.deliveryMethodDropdown[index].dropdownValues;
      if (this.selectedShippingMethod[index].value) {
        dropdownValues.map((item) => {
          if (item.value === this.selectedShippingMethod[index].value) {
            this.$refs.deliveryMethodDropdown[index].setDropdownLabel(item.label);
            this.selectedShippingMethod[index].value = item.value;
            this.selectedShippingMethod[index].label = item.label;
            this.selectedShippingMethod[index].deliveryDetails = item.deliveryDetails;
          }
        });
      } else {
        this.$refs.deliveryMethodDropdown[index].setDropdownLabel(dropdownValues[0].label);
        this.selectedShippingMethod[index].value = dropdownValues[0].value;
        this.selectedShippingMethod[index].label = dropdownValues[0].label;
        this.selectedShippingMethod[index].deliveryDetails = dropdownValues[0].deliveryDetails;
      }
    },
    // creating the request body to send in the save call
    createRequestBody() {
      const requestBody = [];
      this.shippingMethodDetails.deliveryGroup.map((item, index) => {
        requestBody.push({
          deliveryInstruction: this.selectedShippingMethod[index].instructions,
          deliveryMode: {
            code: this.selectedShippingMethod[index].value,
            name: this.selectedShippingMethod[index].label.split(':')[0],
          },
          splitEntries: [],
        });
        item.value.splitEntries.map((entry) => {
          requestBody[index].splitEntries.push({
            code: entry.code,
          });
        });
      });
      return requestBody;
    },
    // save shipping method
    saveMultipleMethod(event) {
      const requestConfig = {};
      requestConfig.data = this.createRequestBody();
      this.checkoutService.saveMultipleMethod(requestConfig, this.handleSaveMethodResponse, this.handleSaveMethodError);
      this.$refs.spinner.showSpinner();
    },
    handleSaveMethodResponse() {
      checkoutEventBus.$emit('show-saved-mode');
      this.$refs.spinner.hideSpinner();
      checkoutEventBus.$emit('update-cart');
    },
    handleSaveMethodError() {
      this.$refs.spinner.hideSpinner();
    },
    // converting array to obejct with a particular key
    arrayToObject(array, keyField) {
      const modifiedObj = array.reduce((obj, item) => {
        obj[item[keyField]] = item;
        return obj;
      }, {});
      return modifiedObj;
    },
  },
};
