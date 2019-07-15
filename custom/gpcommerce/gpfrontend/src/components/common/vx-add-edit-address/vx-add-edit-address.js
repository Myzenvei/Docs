/* Handles the add address form */
import {
  TheMask
} from 'vue-the-mask';
import {
  Validator
} from 'vee-validate';

import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import vxAddressVerification from '../vx-address-verification/vx-address-verification.vue';
import ManageB2bOrgService from '../../common/services/manage-b2b-org-service';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import CommonService from '../../common/services/common-service';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  userStates,
  brandNames,
  booleanFlags,
  defaultCountry,
} from '../../common/mixins/vx-enums';
import {
  eventBus
} from '../../../modules/event-bus';
export default {
  name: 'vx-add-edit-address',
  components: {
    vxDropdownPrimary,
    vxAddressVerification,
    vxModal,
    TheMask,
    vxSpinner,
  },
  mixins: [detectDeviceMixin, mobileMixin],
  props: {
    /**
     * Labels, button and caption texts
     */
    i18n: Object,
    /**
     * Save button text
     */
    isSingleShipping: {
      type: Boolean,
      default: false,
    },
    buttonText: {
      type: String,
      default: 'SAVE',
    },
    submitButtonText: {
      type: String,
      default: 'Submit',
    },
    /**
     * Edit Existing address
     */
    editAddress: {
      type: Object,
      default: {},
    },
    /**
     * To toggle profile checkboxes
     */
    isProfile: {
      type: Boolean,
      default: false,
    },
    /**
     * For address verification
     */
    isBussinessUnit: {
      type: Boolean,
      default: false,
    },
    palletShipment: {
      type: Boolean,
      default: false,
    },
    isFirstAddress: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      userStates,
      commonService: new CommonService(),
      masked: true,
      selectedAddress: '',
      businessUnitsDropDown: [],
      regionsData: [],
      countryData: {},
      /**  *country and region list should come from service */
      countryList: {
        label: 'Country',
        options: [],
      },
      regionList: {
        label: 'State',
        options: [],
      },
      address: {
        firstName: '',
        lastName: '',
        companyName: '',
        phone: '',
        titleCode: '',
        line1: '',
        line2: '',
        town: '',
        id: '',
        postalCode: '',
        country: {},
        region: {},
        defaultAddress: false,
        defaultBillingAddress: false,
        shippingAddress: false,
        palletShipment: false,
        unit: {}
      },
      unverifiedAddress: {},
      defaultCountry,
    };
  },
  computed: {},
  mounted() {
    this.callCountryService();
    if (this.isBussinessUnit) {
      this.fetchBusinessUnitValues();
    }
    if (this.editAddress) {
      this.address = {
        ...this.editAddress
      };
    }
    if (this.$refs.countryDropdown && this.address.hasOwnProperty('country')) {
      this.$refs.countryDropdown.setDropdownValue(this.address.country.isocode);
      this.callRegionService(this.address.country.isocode);
    }

    if (
      this.$refs.businessUnitsDropDown &&
      this.address.hasOwnProperty('bUnit') &&
      this.isBussinessUnit
    ) {
      this.$refs.businessUnitsDropDown.setDropdownValue(this.address.bUnit.value);
    }
    if (this.$refs.stateDropdown && this.address.hasOwnProperty('region')) {
      this.$refs.stateDropdown.setDropdownValue(
        this.address.region.isocode.split('-')[1],
      );
    }

    const veeCustomErrorMessage = {
      en: {
        custom: {
          firstName: {
            required: this.i18n.firstNameRequiredError,
            regex: this.i18n.firstNameRegexError,
            max: this.i18n.firstNameMaxError,
          },
          lastName: {
            required: this.i18n.lastNameRequiredError,
            regex: this.i18n.lastNameRegexError,
            max: this.i18n.lastNameMaxError,
          },
          phoneNumber: {
            required: this.i18n.phoneRequiredError,
            min: this.i18n.phoneMinError,
          },
          country: {
            required: this.i18n.countryError,
          },
          unit: {
            required: this.i18n.businessUnitError,
          },
          addressLine1: {
            required: this.i18n.addressLine1Error,
          },
          city: {
            required: this.i18n.cityError,
          },
          State: {
            required: this.i18n.stateError,
          },
          zipcode: {
            required: this.i18n.zipcodeError,
            regex: this.i18n.zipCodeRegexError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
  },
  updated() { },
  methods: {
    /**  *On selecting address which is returned by address verification service we will send
     that address to save. */
    handleSelectedAddressResponse(data) {
      // TODO: Uncomment below line after address verification
      this.closeAddressVerificationModal();
      this.selectedAddress = data;
      if (!data.id) {
        this.selectedAddress.value["id"] = this.address.id;
      }
      this.$emit('onAddressSaved', this.selectedAddress);
    },
    fetchBusinessUnitValues() {
      const mb2BService = new ManageB2bOrgService();
      // this.$emit('showSpinner');
      mb2BService.getBusinessUnits({}, this.onBusinessUnitsResponse, this.onBusinessUnitsError, '');
    },
    isDixieSite() {
      return globals.getSiteId() === brandNames.dixie;
    },
    onBusinessUnitsResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status && data.units) {
        this.getBusinessUnitDDvalues(data.units);
        // this.$emit('hideSpinner');
      }
    },
    onBusinessUnitsError(error) {

    },
    getBusinessUnitDDvalues(data) {
      data.map((val, index) => {
        const obj = {};
        obj.label = val.name;
        obj.value = val.id;
        this.businessUnitsDropDown.push(obj);
      });
      this.$nextTick(() => {
        if (this.$refs.businessUnitDropdown && this.address.hasOwnProperty('unit')) {
          this.$refs.businessUnitDropdown.setDropdownValue(this.address.unit.uid);
        }
        // setting the selected drop down value to the first businessUnitsDropDown value by default for Dixie Food Service  in R1 release
        if (globals.isDixie()) {
          this.$refs.businessUnitDropdown.selectedItem = {
            label: this.businessUnitsDropDown[0].label,
            value: this.businessUnitsDropDown[0].value,
          };
          this.setDropdownOption('unit', this.businessUnitsDropDown[0]);
        } //ends here
      });

    },
    /**  *On error */
    handleSelectedAddressError(error) {
      this.closeAddressVerificationModal();
    },
    /** set dropdown value  */
    setDropdownOption(key, evt) {
      this.$set(this.address, key, evt);
      if (key === 'country') {
        this.callRegionService(evt.value);
      }
    },
    callRegionService(isoCode) {
      this.commonService.getRegions({}, this.handleGetRegionsResponse, this.handleGetRegionsError, isoCode);
      this.$refs.spinner.showSpinner();
    },
    handleGetRegionsResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.regionsData = response.data.regions;
      this.$refs.stateDropdown.resetDropdown();
      this.regionList.options = [];
      for (let i = 0; i < this.regionsData.length; i += 1) {
        this.$set(this.regionList.options, i, {
          label: this.regionsData[i].name,
          value: this.regionsData[i].isocodeShort,
          countryIso: this.regionsData[i].countryIso,
          isocode: this.regionsData[i].isocode,
        });
      }
      if (this.editAddress) {
        this.setStateInEditMode();
      }
    },
    setStateInEditMode() {
      this.$nextTick(() => {
        if (this.$refs.stateDropdown && this.address.hasOwnProperty('region')) {
          this.$refs.stateDropdown.setDropdownValue(this.address.region.isocode.split('-')[1]);
        }
      });
    },
    handleGetRegionsError(error) {
      this.$refs.spinner.hideSpinner();
    },
    callCountryService() {
      this.commonService.getCountries({}, this.handleGetCountriesResponse, this.handleGetCountriesError, booleanFlags.isShippingAddress);
      this.$refs.spinner.showSpinner();
    },
    handleGetCountriesResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response && response.data) {
        this.countryData = response.data.countries;
        this.$refs.countryDropdown.resetDropdown();
        this.countryList.options = [];
        for (let i = 0; i < this.countryData.length; i += 1) {
          this.$set(this.countryList.options, i, {
            label: this.countryData[i].name,
            value: this.countryData[i].isocode,
          });
        }
        if (this.editAddress && this.editAddress.country) {
          this.$refs.countryDropdown.setDropdownLabel(this.editAddress.country.name);
          this.$refs.countryDropdown.selectedItem = {
            label: this.editAddress.country.name,
            value: this.editAddress.country.isocode,
          };
          this.setDropdownOption('country', this.$refs.countryDropdown.selectedItem);
        } else {
          this.$refs.countryDropdown.setDropdownLabel(this.defaultCountry.label);
          this.$refs.countryDropdown.selectedItem = {
            label: this.defaultCountry.label,
            value: this.defaultCountry.value,
          };
          this.setDropdownOption('country', this.defaultCountry);
        }
      };
    },
    handleGetCountriesError(error) {
      this.$refs.spinner.hideSpinner();
    },

    /**  *to open address verification modal */
    callAddressVerification(event) {
      const self = this;
      const $countryDropdown = self.$refs.countryDropdown;
      const $stateDropdown = self.$refs.stateDropdown;
      this.$validator.validateAll().then((result) => {
        if (result) {
          self.unverifiedAddress = {
            titleCode: 'mr',
            firstName: self.address.firstName,
            lastName: self.address.lastName,
            companyName: self.address.companyName,
            phone: self.address.phone,
            line1: self.address.line1,
            town: self.address.town,
            id: self.address.id,
            postalCode: self.address.postalCode,
            country: {
              isocode: $countryDropdown.selectedItem.value,
              name: $countryDropdown.selectedItem.label,
            },
            region: {
              countryIso: $countryDropdown.selectedItem.value,
              isocode: `${$countryDropdown.selectedItem.value}-${
                $stateDropdown.selectedItem.value
                }`,
              isocodeShort: $stateDropdown.selectedItem.value,
              name: $stateDropdown.selectedItem.label,
            },
            defaultAddress: self.address.defaultAddress,
            defaultBillingAddress: self.address.defaultBillingAddress,
            shippingAddress: self.address.defaultAddress,
            palletShipment: self.address.palletShipment,
            billingAddress: self.address.defaultBillingAddress,
            userId: self.address.userId,
          };
          if (this.isBussinessUnit) {
            self.unverifiedAddress = {
              ...self.unverifiedAddress,
              unit: {
                uid: self.address.unit.value || self.address.unit.uid
              }
            }
          }
          self.unverifiedAddress.line2 = self.address.line2 || '';
          eventBus.$emit('removeScrollbar', true);
          // TODO: Uncomment after address verification
          self.$refs.addressVerificationModal.open();

          // TODO: Temporary Code for checking add and edit Address
          // if (self.address.hasOwnProperty("id")) {
          //   self.unverifiedAddress['id'] = self.address.id;
          // }
          // this.handleSelectedAddressResponse(this.unverifiedAddress);
        } else {
          this.globals.setFocusByName(this.$el, this.globals.getElementName(this.errors));
        }
      });
    },
    /**  *To close address verification modal */
    closeAddressVerificationModal() {
      eventBus.$emit('removeScrollbar', false);
      this.$refs.addressVerificationModal.close();
    },
  },
};
