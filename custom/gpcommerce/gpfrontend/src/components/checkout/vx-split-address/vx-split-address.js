/* Handles split quantity logic and population of address drop-down */
import { Validator } from 'vee-validate';
import vxStepperControl from '../../common/vx-stepper-control/vx-stepper-control.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import { checkoutEventBus } from '../../../modules/event-bus';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  myAccountAddressStatus,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-split-address',
  components: {
    vxStepperControl,
    vxDropdownPrimary,
  },
  mixins: [mobileMixin],
  props: {
    i18n: Object,
    /**
     * Saved addresses of user
     */
    addresses: Array,
    /**
     * Product infortmation
     */
    productDetails: Object,
    /**
     * Index of product in cart
     */
    entryNumber: Number,
    /**
     * Flag for apply split logic
     */
    splitApplicable: Boolean,
    isShippingMultiple: Boolean,
    defaultAddress: Object,
  },
  data() {
    return {
      isSplit: false,
      globals,
      selectedAddress: {
        label: '',
        value: {},
      },
      addressId: '',
      validationSuccess: true,
      shippingAddresses: [],
      productAddressObject: {},
      splitEntryObject: {},
      splitEntry: [],
      totalQuantity: 0,
      totalQuantityError: false,
      totalAddressError: false,
      shppingDetailsAddresses: {},
      indexSelected: 0,
    };
  },
  computed: {},
  mounted() {
    this.addingNewEntry(
      this.productDetails.quantity,
      this.productDetails.product.code,
      this.productDetails.basePrice.value,
    );
    const veeCustomErrorMessage = {
      en: {
        custom: {
          selectedAddress: {
            required: 'Please select any address',
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    checkoutEventBus.$on('restriction-error', (restrictedEntries) => {
      this.shippingAddresses = [];
    });
    this.handleSelectedAddresses();

    if (this.globals.isB2B()) {
      this.handleDropdownLabelB2B(this.addresses);
    } else if (this.globals.isB2C()) {
      this.handleDropdownLabelB2C(this.addresses);
    }
    this.handleSelectedAddresses();
  },
  methods: {
    // Adding new split row
    addingNewEntry(quantity, code, price) {
      if (!this.isSplit) {
        this.splitEntry = [];
      }
      this.splitEntryObject = {
        qty: quantity,
        productCode: code,
        price,
      };
      this.splitEntry.push(this.splitEntryObject);
      if (this.splitEntry.length === this.productDetails.quantity) {
        this.totalQuantityError = true;
      }
    },
    // Set dropdown value dynamically
    setAddressDropdownValue(data) {
      if (
        this.$refs.shippingAddressDropdown &&
        this.$refs.shippingAddressDropdown.length &&
        this.$refs.shippingAddressDropdown.length !== 0
      ) {
        this.$refs.shippingAddressDropdown.map((item) => {
          const currentDropdownLabel = item.getDropdownLabel();
          if (currentDropdownLabel === this.i18n.filledDropdownLabel) item.setDropDownItem(data);
        });
      }
      this.selectedAddress = data;
      for (let i = 0; i < this.splitEntry.length; i += 1) {
        if (this.globals.isB2C() || this.globals.isEmployeeStore()) {
          this.$refs.shippingAddressDropdown.setDropDownItem(this.selectedAddress);
        } else {
          this.$refs.shippingAddressDropdown[i].setDropDownItem(this.selectedAddress);
        }
      }
    },
    // Click functionality on split quantity
    splitTheQuantity(event) {
      if (this.splitEntry.length < this.productDetails.quantity) {
        this.addingNewEntry(
          1,
          this.productDetails.product.code,
          this.productDetails.basePrice.value,
        );
      } else {
        this.totalQuantityError = true;
      }
    },
    // Forming split entry request data
    productSplitEntryData() {
      const shippingDetailsArray = [];
      const shippingAddressArray = [];
      let selectedItemData = {};
      let splitError = {};
      this.totalQuantity = 0;
      this.totalAddressError = false;
      for (let i = 0; i < this.splitEntry.length; i += 1) {
        if (this.globals.isB2C() || this.globals.isEmployeeStore()) {
          selectedItemData = this.$refs.shippingAddressDropdown.selectedItem;
        } else {
          selectedItemData = this.$refs.shippingAddressDropdown[i].selectedItem;
        }
        if (this.$refs.stepperController && this.$refs.stepperController[i]) {
          this.splitEntry[i].qty = this.$refs.stepperController[i].counter;
        }
        this.totalQuantity += this.splitEntry[i].qty;
        if (selectedItemData && selectedItemData.label && selectedItemData.value) {
          // Address details array
          this.shippingAddresses.push({
            productCode: this.productDetails.product.code,
            label: selectedItemData.label,
            value: selectedItemData.value,
            id: selectedItemData.value.id,
          });

          // Request Shipping details  Array
          shippingDetailsArray.push({
            country: selectedItemData.value.country,
            productCode: this.productDetails.product.code,
            region: selectedItemData.value.region,
          });
          // Request Shipping Address array with split entry
          shippingAddressArray.push({
            deliveryAddress: {
              id: selectedItemData.value.id,
            },
            price: this.splitEntry[i].price,
            productCode: this.splitEntry[i].productCode,
            qty: this.splitEntry[i].qty,
          });
          this.shppingDetailsAddresses = {
            productData: {
              entryNumber: this.entryNumber,
              splitEntry: shippingAddressArray,
            },
            addressData: shippingDetailsArray,
            allAddresses: this.shippingAddresses,
          };
        } else {
          this.totalAddressError = true;
        }
      }
      if (this.totalQuantity !== this.productDetails.quantity) {
        splitError = {
          quantityError: true,
        };
      } else if (this.totalAddressError) {
        splitError = {
          addressEmptyError: true,
        };
      }
      if (
        (splitError && splitError.quantityError) ||
        (splitError && splitError.addressEmptyError)
      ) {
        this.$emit('splitError', splitError);
      } else {
        return this.shppingDetailsAddresses;
      }
    },
    // Split addresses
    splitAddresses() {
      this.handleDropdownLabelB2B();
      this.isSplit = true;
      checkoutEventBus.$emit('enable-split');
      this.splitTheQuantity();
    },
    deleteSplitRow(index, event) {
      this.totalQuantityError = false;
      if (index >= 0 && this.splitEntry.length > 1) {
        this.splitEntry.splice(index, 1);
        if (this.splitEntry.length === 1) {
          this.$set(this.splitEntry, 0, {
            qty: this.productDetails.quantity,
            productCode: this.productDetails.product.code,
            price: this.productDetails.basePrice.value
          });
          this.isSplit = false;
          checkoutEventBus.$emit('disable-split');
        }
      } else if (index === 0 && this.splitEntry.length === 1) {
        this.splitEntry[index].qty = this.productDetails.quantity;
        this.isSplit = false;
        checkoutEventBus.$emit('disable-split');
      }
    },
    mappedShippingAddress() {
      if (this.shippingAddresses && this.shippingAddresses.length !== 0) {
        this.$emit('shippingAddresses', this.shippingAddresses);
        return this.productAddressObject;
      }
      this.$emit('shippingAddressesError', true);
    },
    handleQuantity(item, index, itemQuantity) {
      this.splitEntry[index].qty = itemQuantity;
    },
    handleDropdownLabelB2C() {
      if (this.addresses.length === 0) {
        this.$refs.shippingAddressDropdown.setDropdownLabel(this.i18n.emptyDropdownLabel);
      } else {
        this.$refs.shippingAddressDropdown.setDropdownLabel(this.i18n.filledDropdownLabel);
      }
    },
    handleDropdownLabelB2B() {
      // For B2B shippingAddressDropdown is an array
      this.$nextTick(() => {
        if (this.addresses.length === 0) {
          for (let i = 0; i < this.splitEntry.length; i += 1) {
            if(this.$refs.shippingAddressDropdown[i]) {
              this.$refs.shippingAddressDropdown[i].setDropdownLabel(this.i18n.emptyDropdownLabel);
            }
          }
        } else {
          for (let i = 0; i < this.splitEntry.length; i += 1) {
            if(this.$refs.shippingAddressDropdown[i]) {
              this.$refs.shippingAddressDropdown[i].setDropdownLabel(this.i18n.filledDropdownLabel);
            }
          }
        }
      });
    },
    formAddressString(address) {
      let userAddress = '';
      let companyName = '';
      const name = address.firstName + ' ' + address.lastName;
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      if (address) {
        userAddress = [
          name,
          companyName,
          address.line1,
          address.line2,
          address.town,
          address.region.isocodeShort,
          address.postalCode,
          address.country.isocode,
        ]
          .filter(Boolean)
          .join(', ');
      }
      return userAddress;
    },
    handleSelectedAddresses() {
      if (this.isShippingMultiple) {
        if (this.productDetails.splitEntries.length) {
          this.productDetails.splitEntries.map((address, index) => {
            let addressObject = {};
            if (address.deliveryAddress.approvalStatus.toUpperCase() === myAccountAddressStatus.active) {
              addressObject = {
                label: this.formAddressString(address.deliveryAddress),
                value: address.deliveryAddress,
              };
            } else {
              this.$refs.shippingAddressDropdown[index].setDropdownLabel(this.i18n.filledDropdownLabel);
            }

            if (this.globals.isB2C()) {
              this.$refs.shippingAddressDropdown.setDropDownItem(addressObject);
            } else if (this.globals.isB2B()) {
              this.$nextTick(() => {
                if (this.$refs.shippingAddressDropdown[index]) {
                  this.$refs.shippingAddressDropdown[index].setDropDownItem(addressObject);
                }
              });
            }
          });
        } else if (this.productDetails && this.productDetails.splitEntries.length === 0) {
          this.settingDefaultAddress();
        }
      }
    },
    settingDefaultAddress() {
      if (this.defaultAddress && this.defaultAddress.value && this.defaultAddress.label) {
        for (let i = 0; i < this.splitEntry.length; i += 1) {
          if (this.globals.isB2C() || this.globals.isEmployeeStore()) {
            this.$nextTick(() => {
              this.$refs.shippingAddressDropdown.setDropDownItem(this.defaultAddress);
            });
          } else {
            this.$nextTick(() => {
              this.$refs.shippingAddressDropdown[i].setDropDownItem(this.defaultAddress);
            });
          }
        }
      }
    },
    handleAddressChange(data, index) {
      if (this.entryNumber === 0 && index === 0) {
        this.$emit('selectfirstAddress', data);
      }
      this.validationSuccess = true;
    },
  },
  watch: {
    selectedAddress(data) {
      if (this.entryNumber === 0 && this.indexSelected === 0) {
        this.$emit('selectfirstAddress', data);
      }
      this.validationSuccess = true;
    },
  },
};
