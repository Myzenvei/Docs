import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
// import shippingMethodDetails from './vx-single-shipping-method-mock';
import CheckoutService from '../../common/services/checkout-service';
import globals from '../../common/globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  checkoutEventBus
} from '../../../modules/event-bus';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

export default {
  name: 'vx-single-shipping-method',
  mixins: [detectDeviceMixin],
  components: {
    vxRadioButtonGroup,
    vxSpinner,
  },
  props: {
    isEditable: Boolean,
    i18n: Object,
    checkoutData: Object,
    addressChanged: Boolean,
  },
  data() {
    return {
      selectedShippingMethod: {
        value: '',
        instructions: '',
        deliveryDetails: '',
        label: '',
        name: '',
        cost: '',
      },
      shippingMethodDetails: {},
      deliveryMethods: [],
      checkoutService: new CheckoutService(),
      globals,
      dataLoaded: false,
    };
  },
  computed: {},
  watch: {
    'checkoutData.deliveryMode': function () {
      this.$nextTick(() => {
        this.showSavedData();
      });
    },
  },
  mounted() {
    // this.selectedShippingMethodData();
    this.getShippingMethod();
  },
  methods: {
    // get Shipping method response and data
    getShippingMethod() {
      this.checkoutService.getSingleMethod({}, this.handleGetMethodResponse, this.handleGetMethodError);
      this.$refs.spinner.showSpinner();
    },
    handleGetMethodResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        this.shippingMethodDetails = response.data;
        this.createDeliveryMethodArray();
        this.showSavedData();
      }
    },
    handleGetMethodError() {
      this.$refs.spinner.hideSpinner();
    },
    // creating array for shipping method radio button
    createDeliveryMethodArray() {
      this.shippingMethodDetails.deliveryModes.map((item) => {
        let label = '';
        if (this.globals.siteConfig.showDeliveryCost) {
          label = `${item.name} : ${item.deliveryCost.formattedValue}`;
        } else {
          label = item.name;
        }
        this.deliveryMethods.push({
          label,
          value: item.code,
          // deliveryDetails: item.description,
          name: item.name,
          cost: item.deliveryCost.formattedValue,
        });
      });
      this.dataLoaded = true;
    },
    // set data in shippingMethodDetails
    setData(data) {
      this.selectedShippingMethod.label = data.label;
      this.selectedShippingMethod.value = data.value;
      // this.selectedShippingMethod.deliveryDetails = data.deliveryDetails;
      this.selectedShippingMethod.name = data.name;
      this.selectedShippingMethod.cost = data.cost;
    },
    setValue() {
      let value = '';
      if (this.shippingMethodDetails.deliveryMode) {
        value = this.shippingMethodDetails.deliveryMode.code;
      }
      return value;
    },
    // set the lowest shipping method as default or if the service
    // returns the value selected by user then show that
    handleDefault() {
      if (this.selectedShippingMethod.value) {
        this.deliveryMethods.map((item) => {
          if (this.selectedShippingMethod.value === item.value) {
            this.$refs.shippingMethod.setSelectedByValue(item.value);
            this.setData(item);
          }
        });
      } else {
        this.$refs.shippingMethod.setSelectedByValue(this.deliveryMethods[0].value);
        this.setData(this.deliveryMethods[0]);
      }
    },
    // storing the shipping method radio button values
    handleSelected(event) {
      this.setData(event);
    },
    // Show the data that the user has selected
    // if service has the saved data
    showSavedData() {
      if (this.checkoutData.deliveryMode && !this.addressChanged) {
        checkoutEventBus.$emit('show-saved-mode');
        // this.selectedShippingMethod.deliveryDetails = data.deliveryDetails;
        this.selectedShippingMethod.instructions = this.checkoutData.deliveryMode.description;
        const savedData = this.deliveryMethods.filter(method => method.value === this.checkoutData.deliveryMode.code);
        if (savedData.length) {
          this.setData(savedData[0]);
        } else {
          this.selectedShippingMethod.name = this.checkoutData.deliveryMode.name;
          this.selectedShippingMethod.cost = this.checkoutData.deliveryMode.deliveryCost.formattedValue;
        }
      } else {
        checkoutEventBus.$emit('show-edit-mode');
      }
    },
    // save shipping method
    saveSingleMethod(event) {
      const requestConfig = {};
      requestConfig.params = {
        deliveryModeId: this.selectedShippingMethod.value,
        deliveryInst: this.selectedShippingMethod.instructions,
      };
      this.checkoutService.saveSingleMethod(requestConfig, this.handleSaveMethodResponse, this.handleSaveMethodError);
      this.$refs.spinner.showSpinner();
    },
    handleSaveMethodResponse() {
      checkoutEventBus.$emit('show-saved-mode');
      checkoutEventBus.$emit('update-cart');
      this.$refs.spinner.hideSpinner();
    },
    handleSaveMethodError() {
      this.$refs.spinner.hideSpinner();
    },
  },
};
