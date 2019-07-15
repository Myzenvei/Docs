import VeeValidate, { Validator } from 'vee-validate';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
import globals from '../globals';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import getCreditCardTypeMixin from '../../common/mixins/creditcard-type-finder-mixin';
import CyberSourceIntegrationService from '../../common/services/cybersource-integration-service';

import vxProfileCard from '../../common/vx-profile-card/vx-profile-card.vue';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import paymentFormDetails from './payment-form-details';
import {
  myAccountAddressStatus,
  brandValues,
  defaultCountry,
  paymentStatus,
} from '../../common/mixins/vx-enums';

export default {
  name: 'vx-add-payment-form',
  components: {
    vxDropdownPrimary,
    vxRadioButtonGroup,
    vxSpinner,
    vxProfileCard,
  },
  mixins: [getCreditCardTypeMixin, flyoutBannerMixin, myAccountAddressStatus],
  props: {
    /**
     * @model details
     */
    // paymentFormData: {
    // type: Array,
    // required: true,
    // },
    prePopulateFormFields: Boolean,
    showTopHeadings: Boolean,
    reloadOnSuccess: {
      default: false,
      type: Boolean,
    },
    savedFormData: Object,
    i18n: Object,
    isCheckout: {
      type: Boolean,
      default: false,
    },
    shippingAddress: Object,
    isFirstCard: {
      type: Boolean,
      default: false,
    },
    guestEmail: {
      type: String,
    },
    isFirstCardImages: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      shippingAdresssChecked: false,
      globals,
      csService: new CyberSourceIntegrationService(),
      ifNewAddress: false,
      isValidCardExpiry: false,
      populateFormData: this.prePopulateFormFields,
      showSavedAddressBox: false,
      fetchedFormData: this.savedFormData,
      savedAddress: [],
      paymentResponseObj: {},
      pageReloadOnSuccess: this.reloadOnSuccess,
      savedPaymentFormId: '',
      isCardNameInvalid: false,
      foreName: '',
      surName: '',
      paymentform: {
        subscriptionId: '',
        cardNumberMasked: '',
        cardNumber: '',
        cardHolderName: '',
        cardType: '',
        month: '',
        year: '',
        cardCvn: '',
        defaultPaymentMethod: '',
        address: '',
        country: {},
        billToAddressLine1: '',
        billToAddressLine2: '',
        billToAddressCity: '',
        state: {},
        billToAddressPostalCode: '',
        billToEmail: '',
        billToPhone: '',
        productEntries: Object,
      },
      checkbox: {
        label: 'Default Payment Method',
        value: true,
      },
      paymentformDetails: paymentFormDetails,
      saveForLater: false,
      myAccountAddressStatus,
      siteId: '',
      cardIsAdded: false,
      addressDropdownSelected: false,
    };
  },
  created() {
    const self = this;
    Validator.extend('cardexpirydate', {
      getMessage(field, params, data) {
        return 'Field expiration is not valid!';
      },
      // Returns a boolean value
      validate(value) {
        return self.validateCreditCardExpiry();
      },
    });
  },
  computed: {
    creditCardNumberRegex() {
      return this.globals.siteConfig.isDinersCardAvailable
        ? /^3(?:[47]\d\d{4}(?:\d{4}){2}|[68]\d{12})$|^4(?:\d\d\d)\d{4}(?:\d{4}){2}$|^6011(?:\d\d\d)?\d{4}(?:\d{4}){2}$|^6[23457]\d\d(?:\d\d\d)?\d{4}(?:\d{4}){2}$|^5[0-5]\d\d\d{4}(?:\d{4}){2}$|^2[2-7]\d{14}$/
        : /^3[47]\d{13,14}$|^4(?:\d\d\d)\d{4}(?:\d{4}){2}$|^6011(?:\d\d\d)?\d{4}(?:\d{4}){2}$|^6[23457]\d\d(?:\d\d\d)?\d{4}(?:\d{4}){2}$|^5[0-5]\d\d\d{4}(?:\d{4}){2}$|^2[2-7]\d{14}$/;
    },
  },
  mounted() {
    const cvvCardImagePath = `${
      this.globals.assetsPath
    }images/card-with-cvv-highlighting.png`;
    $('.tooltip-btn').tooltip({
      title: `<img src='${cvvCardImagePath}' />`,
      html: true,
      placement: 'bottom',
    });
    const veeCustomErrorMessage = {
      en: {
        custom: {
          cardNumber: {
            required: this.i18n.cardnumberRequiredError,
            regex: this.i18n.cardnumberValidError,
            max: this.i18n.cardnumberMaxError,
          },
          cardHolderName: {
            required: this.i18n.cardholdernameRequiredError,
            regex: this.i18n.cardholdernameRegexError,
            max: this.i18n.cardholdernameMaxError,
          },
          cardCvn: {
            required: this.i18n.cardcvnRequiredError,
            regex: this.i18n.cardcvnRegexError,
          },
          billToAddressLine1: {
            required: this.i18n.addressline1RequiredError,
            max: this.i18n.addressline1MaxError,
          },
          billToAddressLine2: {
            required: this.i18n.addressline2RequiredError,
            max: this.i18n.addressline2MaxError,
          },
          billToAddressCity: {
            required: this.i18n.cityRequiredError,
            max: this.i18n.cityMaxError,
          },
          billToAddressPostalCode: {
            required: this.i18n.postalcodeRequiredError,
            regex: this.i18n.postalcodeRegexError,
            max: this.i18n.postalcodeMaxError,
          },
          billToEmail: {
            required: this.i18n.emailRequiredError,
            email: this.i18n.emailInvalidError,
            max: this.i18n.emailMaxError,
          },
          billToPhone: {
            required: this.i18n.phoneRequiredError,
            min: this.i18n.phoneMinError,
          },
          paymentFormMonth: {
            required: this.i18n.monthRequiredError,
            cardexpirydate: this.i18n.monthValidError,
          },
          paymentFormYear: {
            required: this.i18n.yearRequiredError,
            cardexpirydate: this.i18n.yearValidError,
          },
          paymentFormAddress: {
            required: this.i18n.addressRequiredError,
          },
          paymentFormCountry: {
            required: this.i18n.countryRequiredError,
          },
          paymentFormState: {
            required: this.i18n.stateRequiredError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);

    if (!this.populateFormData) {
      this.$refs.monthDropdown.setDropdownLabel(this.i18n.month);
      this.$refs.yearDropdown.setDropdownLabel(this.i18n.year);
      this.$refs.addressDropdown.setDropdownLabel(this.i18n.selectAddress);
      this.$refs.countryDropdown.setDropDownItem(defaultCountry);
      this.paymentform.country = defaultCountry;
      this.$refs.stateDropdown.setDropdownLabel(this.i18n.dropdownLabel);
    }

    if (this.populateFormData) {
      if (
        this.fetchedFormData &&
        Object.keys(this.fetchedFormData).length > 0
      ) {
        this.populateFormFields(this.fetchedFormData);
      }
    }
    this.onPageLoad();
    this.findSiteId();
  },
  watch: {
    'paymentform.cardHolderName': function() {
      if (this.paymentform.cardHolderName) {
        const cardArray = this.paymentform.cardHolderName.split(' ');
        this.foreName = cardArray.slice(0, -1).join(' ');
        this.surName = cardArray.splice(cardArray.length - 1).join(' ');
      }
      if (this.foreName.length > 60 || this.surName.length > 60) {
        this.isCardNameInvalid = true;
      } else this.isCardNameInvalid = false;
    },
    'paymentform.month': function() {
      this.validateCreditCardExpiry();
    },
    'paymentform.year': function() {
      this.validateCreditCardExpiry();
    },
    'paymentform.country': function() {
      if (this.paymentform.country.value) {
        // fetching state list from Hybris
        this.$refs.stateDropdown.setDropdownLabel(this.i18n.dropdownLabel);
        this.csService.getStates(
          {},
          this.handleStatesData,
          this.handleStatesDataError,
          this.paymentform.country.value,
        );
      }
    },
    'paymentform.address': function() {
      if (!this.shippingAdresssChecked) {
        if (this.paymentform.address.value === 'NEW') {
          this.ifNewAddress = true;
          this.addressDropdownSelected = false;
          this.paymentform.billToAddressLine1 = '';
          this.paymentform.billToAddressLine2 = '';
          this.paymentform.billToAddressCity = '';
          this.paymentform.billToAddressPostalCode = '';
          this.paymentform.state = {};
          this.paymentform.country = {};
          this.$nextTick(() => {
            if (this.$refs.countryDropdown) {
              this.$refs.countryDropdown.setDropdownLabel(
                this.i18n.dropdownLabel,
              );
            }
            if (this.$refs.stateDropdown) {
              this.$refs.stateDropdown.setDropdownLabel(
                this.i18n.dropdownLabel,
              );
            }
          });
        } else {
          this.ifNewAddress = false;
          this.addressDropdownSelected = true;
          const selAddressval = this.paymentform.address.value;
          let billingAddressLine2 = '';
          if (this.savedAddress[selAddressval].line2) {
            billingAddressLine2 = this.savedAddress[selAddressval].line2;
          }
          this.paymentform.billToAddressLine1 = this.savedAddress[
            selAddressval
          ].line1;
          this.paymentform.billToAddressLine2 = billingAddressLine2;
          this.paymentform.billToAddressCity = this.savedAddress[
            selAddressval
          ].city;
          this.paymentform.billToAddressPostalCode = this.savedAddress[
            selAddressval
          ].postalCode;
          this.$nextTick(() => {
            if (this.$refs.countryDropdown) {
              this.$refs.countryDropdown.setDropdownLabel(
                this.savedAddress[selAddressval].countryName,
              );
            }
            if (this.$refs.stateDropdown) {
              this.$refs.stateDropdown.setDropdownLabel(
                this.savedAddress[selAddressval].stateName,
              );
            }
          });
          this.paymentform.state = {
            value: this.savedAddress[selAddressval].state,
          };
          this.paymentform.country = {
            value: this.savedAddress[selAddressval].country,
          };
        }
      }
    },
    shippingAdresssChecked() {
      if (this.shippingAdresssChecked) {
        this.ifNewAddress = false;
        let address = '';
        let billingAddressLine2 = '';
        if (this.shippingAddress.line2) {
          billingAddressLine2 = this.shippingAddress.line2;
        }
        if (this.shippingAddress.line2) {
          address = `${this.shippingAddress.line1}, ${
            this.shippingAddress.line2
          }, ${this.shippingAddress.town}, ${
            this.shippingAddress.region.isocode
          }, ${this.shippingAddress.postalCode}`;
        } else {
          address = `${this.shippingAddress.line1}, ${
            this.shippingAddress.town
          },
  ${this.shippingAddress.region.isocode},
  ${this.shippingAddress.postalCode}`;
        }
        const index = this.shippingAddress.region.isocode.lastIndexOf('-');
        const state = this.shippingAddress.region.isocode.slice(index + 1);
        this.paymentform.address = address;
        this.paymentform.billToAddressLine1 = this.shippingAddress.line1;
        this.paymentform.billToAddressLine2 = billingAddressLine2;
        this.paymentform.billToAddressCity = this.shippingAddress.town;
        this.paymentform.billToAddressPostalCode = this.shippingAddress.postalCode;
        this.paymentform.state = state;
        this.paymentform.country = {
          value: this.shippingAddress.country.isocode,
        };
        this.$nextTick(() => {
          if (this.$refs.addressDropdown) {
            this.$refs.addressDropdown.setDropdownLabel(address);
          }
          if (this.$refs.countryDropdown) {
            this.$refs.countryDropdown.setDropdownLabel(
              this.shippingAddress.country.name,
            );
          }
          if (this.$refs.stateDropdown) {
            this.$refs.stateDropdown.setDropdownLabel(
              this.shippingAddress.region.name,
            );
          }
        });
      } else {
        this.addressDropdownSelected = false;
        this.paymentform.billToAddressLine1 = '';
        this.paymentform.billToAddressLine2 = '';
        this.paymentform.billToAddressCity = '';
        this.paymentform.billToAddressPostalCode = '';
        this.$nextTick(() => {
          if (this.$refs.addressDropdown) {
            this.$refs.addressDropdown.setDropdownLabel(
              this.i18n.selectAddress,
            );
          }
          if (this.$refs.countryDropdown) {
            this.$refs.countryDropdown.setDropdownLabel(
              this.i18n.dropdownLabel,
            );
          }
          if (this.$refs.stateDropdown) {
            this.$refs.stateDropdown.setDropdownLabel(this.i18n.dropdownLabel);
          }
        });
        this.paymentform.country = {
          value: this.savedAddress[0].country,
        };
        this.paymentform.state = {
          value: this.savedAddress[0].state,
        };
      }
    },
  },
  methods: {
    populateFormFields(dataObj) {
      let billingAddress2 = '';
      if (dataObj.billingAddress.line2) {
        billingAddress2 = dataObj.billingAddress.line2;
      }
      this.savedPaymentFormId = dataObj.id;
      this.paymentform.subscriptionId = dataObj.subscriptionId;
      this.paymentform.cardNumberMasked = dataObj.cardNumber;
      this.paymentform.cardHolderName = dataObj.accountHolderName;
      this.paymentform.defaultPaymentMethod = dataObj.defaultPayment;
      this.paymentform.firstName = dataObj.billingAddress.firstName;
      this.paymentform.lastName = dataObj.billingAddress.lastName;
      this.paymentform.country = {
        label: dataObj.billingAddress.country.isocode,
        value: dataObj.billingAddress.country.isocode,
      };
      this.paymentform.state = {
        label: dataObj.billingAddress.region.isocode.split('-')[1],
        value: dataObj.billingAddress.region.isocode.split('-')[1],
      };
      this.paymentform.billToAddressLine1 = dataObj.billingAddress.line1;
      this.paymentform.billToAddressLine2 = billingAddress2;
      this.paymentform.streetAddress = `${dataObj.billingAddress.line1},${
        dataObj.billingAddress.line2
      }`;
      this.paymentform.city = dataObj.billingAddress.town;
      this.paymentform.billToAddressCity = dataObj.billingAddress.town;
      this.paymentform.postalCode = dataObj.billingAddress.postalCode;
      this.paymentform.billToAddressPostalCode =
        dataObj.billingAddress.postalCode;
      this.paymentform.billToEmail = dataObj.billingAddress.email;
      this.paymentform.billToPhone = dataObj.billingAddress.phone;
      this.$nextTick(() => {
        if (this.$refs.monthDropdown) {
          this.$refs.monthDropdown.setDropdownLabel(dataObj.expiryMonth);
        }
        if (this.$refs.yearDropdown) {
          this.$refs.yearDropdown.setDropdownLabel(dataObj.expiryYear);
        }
      });
      this.paymentform.month = {
        value: dataObj.expiryMonth,
      };
      this.paymentform.year = {
        value: dataObj.expiryYear,
      };
    },
    findSiteId() {
      switch (this.globals.getSiteId()) {
        case 'dixie':
          this.siteId = brandValues.dixie;
          break;
        case 'gppro':
          this.siteId = brandValues.gppro;
          break;
        case 'gpemployee':
          this.siteId = brandValues.gpemployee;
          break;
        case 'vanityfair':
          this.siteId = brandValues.vanityfair;
          break;
        case 'mardigras':
          this.siteId = brandValues.mardigras;
          break;
        default:
          this.siteId = '';
      }
    },
    // PASSING VALUES TO IFRAME  *********************/s
    iframeResponse(data, pageId) {
      const self = this;
      const respData = data;
      if (respData.decision === 'ACCEPT') {
        const currentDate = new Date();
        let currMonth = parseInt(currentDate.getMonth()) + 1;
        currMonth = currMonth < 10 ? `0${currMonth}` : currMonth;
        let currYear = currentDate.getFullYear();
        currYear = currYear.toString();
        currYear = currYear.slice(-2);

        let expYear = respData.expiry.split('-')[1];
        expYear = String(expYear);
        // Updated for YCOM-8909
        // expYear = expYear.slice(-2);
        // let expYear = respData.expiry + "";

        self.paymentResponseObj.accountHolderName = respData.cardHolderName;
        self.paymentResponseObj.cardType = {
          name: respData.cardType,
          code: respData.cardType,
        };
        self.paymentResponseObj.cardNumber = respData.cardNumber;
        self.paymentResponseObj.startMonth = currMonth;
        self.paymentResponseObj.startYear = currYear;
        self.paymentResponseObj.expiryMonth = respData.expiry.split('-')[0];
        self.paymentResponseObj.expiryYear = expYear;
        self.paymentResponseObj.issueNumber = respData.reasonCode;
        self.paymentResponseObj.subscriptionId = respData.subscriptionId;
        self.paymentResponseObj.paymentToken = respData.subscriptionId;
        self.paymentResponseObj.transactionType = respData.transactionType;
        if (self.isCheckout) {
          self.paymentResponseObj.saved = self.saveForLater;
        } else {
          self.paymentResponseObj.saved = true;
        }
        const requestConfig = {};
        requestConfig.data = self.paymentResponseObj;
        requestConfig.data.pageId = pageId;
        if (self.globals.getIsLoggedIn()) {
          if (respData.transactionType === 'update_payment_token') {
            self.csService.editCardDetailsService(
              requestConfig,
              self.handlePaymentResponse,
              self.handlePaymentError,
              self.savedPaymentFormId,
            );
          } else if (!self.cardIsAdded) {
            self.cardIsAdded = true;
            self.csService.saveCardDetailsService(
              requestConfig,
              (response) => {
                const status = response.status;
                const data = response.data;

                if (status) {
                  self.showFlyout(
                    'success',
                    self.i18n.updatePaymentSuccessMsg,
                    true,
                  );
                  self.$emit('card-added', data);
                } else {
                  this.handleAddPaymentErrorCallback(data);
                }
              },
              this.handleSaveCardDetailsError,
            );
          }
        } else {
          self.csService.saveCardDetailsServiceGuest(
            requestConfig,
            (response) => {
              const status = response.status;
              const data = response.data;

              if (status) {
                self.showFlyout(
                  'success',
                  self.i18n.updatePaymentSuccessMsg,
                  true,
                );
                self.$emit('card-added', data);
              } else {
                this.handleAddPaymentErrorCallback(data);
              }
            },
            this.handleSaveCardDetailsGuestError,
          );
        }
      } else if (
        respData.decision === paymentStatus.error ||
        respData.decision === paymentStatus.decline
      ) {
        this.initializeIframe();
        self.$emit('addPaymentFailed', respData.message);
      }
    },
    onPageLoad() {
      const self = this;
      // fetching country list from Hybris
      this.csService.getCountries(
        {},
        this.handleCountryData,
        this.handleCountryDataError,
      );

      // fetching expiry year list from Hybris
      this.csService.getExpiryYears(
        {},
        this.handleExpiryYearsData,
        this.handleExpiryYearsDataError,
      );
      this.initializeIframe();
      if (self.globals.getIsLoggedIn()) {
        // during edit we wan't to clear new address option in address dropdown
        if (this.populateFormData) {
          this.paymentformDetails.address.options = [];
        }
        self.csService.getAddressService(
          {},
          this.handleAddressData,
          this.handleAddressDataError,
        );
      }
    },
    initializeIframe() {
      const iframeObj = document.getElementById('destination');
      iframeObj.setAttribute('src', globals.getCreditCardiFrameURL());
    },
    handleAddPaymentErrorCallback(data) {
      this.showFlyout('error', data, true);
    },
    showSavedAddresses() {
      this.showSavedAddressBox = true;
    },
    handlePaymentResponse(response) {
      const status = response.status;
      const data = response.data;
      const self = this;
      if (status) {
        self.showFlyout('success', self.i18n.updatePaymentSuccessMsg, true);
        self.$emit('card-updated');
      } else {
        this.handleAddPaymentErrorCallback(data);
      }
    },
    handlePaymentError(error) {
      const data = error.response.data;
      this.showFlyout('error', data, true);
    },
    handleAddressData(response) {
      const data = response.data;
      const status = response.status;
      if (status) {
        if (data) {
          this.paymentformDetails.address.options = [];
          this.paymentformDetails.address.options.push({
            value: 'NEW',
            label: 'New Address',
          });
          const addressData = data.addresses.filter(
            val =>
              val.approvalStatus.toUpperCase() ===
              myAccountAddressStatus.active,
          );
          for (let loop = 0; loop < addressData.length; loop++) {
            if (
              addressData[loop] &&
              addressData[loop].approvalStatus &&
              addressData[loop].approvalStatus.toUpperCase() ===
                myAccountAddressStatus.active
            ) {
              const temp = {};
              temp.displayAddress = [
                addressData[loop].line1,
                addressData[loop].line2,
                addressData[loop].town,
                addressData[loop].region.isocode.split('-')[1],
                addressData[loop].country.isocode,
                addressData[loop].postalCode,
              ]
                .filter(Boolean)
                .join(', ');
              temp.indexValue = loop;
              temp.line1 = addressData[loop].line1;
              temp.line2 = addressData[loop].line2;
              temp.city = addressData[loop].town;
              temp.state = addressData[loop].region.isocode.split('-')[1];
              temp.stateName = addressData[loop].region.name;
              temp.country = addressData[loop].country.isocode;
              temp.countryName = addressData[loop].country.name;
              temp.postalCode = addressData[loop].postalCode;
              this.savedAddress.push(temp);
              const tempAddObj = {
                label: temp.displayAddress,
                value: temp.indexValue,
              };
              this.paymentformDetails.address.options.push(tempAddObj);
            }
          }
        }
      }
    },
    handleAddressDataError(error) {},
    handleSignatureResponse(response) {
      if (response) {
      }
    },
    handleSignatureError(error) {},
    handleCountryData(response) {
      const status = response.status;
      const data = response.data;
      this.paymentformDetails.country.options = [];
      if (status) {
        if (data) {
          const countryData = data.countries;
          for (let loop = 0; loop < countryData.length; loop++) {
            const temp = {};
            temp.indexValue = loop;
            temp.country = countryData[loop].name;
            temp.countryISO = countryData[loop].isocode;
            const tempCtryObj = {
              label: temp.country,
              value: temp.countryISO,
            };
            this.paymentformDetails.country.options.push(tempCtryObj);
          }
        }
      }
    },
    handleExpiryYearsData(response) {
      const data = response.data;
      const status = response.status;
      this.paymentformDetails.year.options = [];
      if (status) {
        if (data) {
          const expiryYearsData = data;
          for (let loop = 0; loop < expiryYearsData.length; loop++) {
            const temp = {};
            temp.indexValue = loop;
            temp.expYear = expiryYearsData[loop];
            const tempExpYearObj = {
              label: temp.expYear,
              value: temp.expYear,
            };
            this.paymentformDetails.year.options.push(tempExpYearObj);
          }
        }
      }
    },
    handleExpiryYearsDataError(error) {},

    handleStatesData(response) {
      const data = response.data;
      const status = response.status;
      this.paymentformDetails.state.options = [];
      if (status) {
        if (data) {
          const statesData = data.regions;
          for (let loop = 0; loop < statesData.length; loop++) {
            const temp = {};
            temp.indexValue = loop;
            temp.state = statesData[loop].name;
            temp.stateISO = statesData[loop].isocode;
            temp.stateCode = statesData[loop].isocodeShort;
            temp.stateCountryCode = statesData[loop].countryIso;
            const tempStateObj = {
              label: temp.state,
              value: temp.stateCode,
            };
            this.paymentformDetails.state.options.push(tempStateObj);
          }
        }
      }
    },
    handleStatesDataError(error) {},
    getDateString() {
      let d = new Date();
      d = d.toISOString();
      return `${d.substring(0, 19)}Z`;
    },
    doSignatureCall() {
      const self = this;
      const firstName = this.foreName;
      const lastName = this.surName;
      let state = '';
      let country = '';
      if (this.shippingAdresssChecked) {
        state = this.paymentform.state;
        country = this.shippingAddress.country.isocode;
      } else {
        state = this.paymentform.state.value;
        country = this.paymentform.country.value;
      }

      const requestObjParams = {
        // reference_number: '',
        // signed_date_time: this.getDateString(),
        // signed_field_names: 'req_currency,req_card_type,req_card_number,decision,req_locale,req_bill_to_surname,req_bill_to_address_city,req_card_expiry_date,req_transaction_uuid,req_bill_to_address_postal_code,req_bill_to_phone,reason_code,req_bill_to_forename,req_bill_to_address_country,req_transaction_type,req_payment_method,req_access_key,req_profile_id,req_reference_number,req_bill_to_address_state,req_amount,req_bill_to_email,req_bill_to_address_line1,signed_field_names,signed_date_time',
        bill_to_address_city: this.paymentform.billToAddressCity,
        bill_to_address_state: state,
        bill_to_address_country: country,
        bill_to_address_line1: this.paymentform.billToAddressLine1,
        bill_to_address_line2: this.paymentform.billToAddressLine2,
        bill_to_address_postal_code: this.paymentform.billToAddressPostalCode,
        bill_to_email:
          this.paymentform.billToEmail ||
          self.globals.userInfo.email ||
          this.guestEmail,
        bill_to_forename: firstName,
        bill_to_phone: this.paymentform.billToPhone || '',
        bill_to_surname: lastName,
        // csrf_token: ACC.config.CSRFToken,
        payment_method: 'card',
        transaction_type: 'create_payment_token',
        signed_field_names:
          'access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency,payment_method,bill_to_forename,bill_to_surname,bill_to_email,bill_to_phone,bill_to_address_line1,bill_to_address_city,bill_to_address_state,bill_to_address_country,bill_to_address_postal_code',
        unsigned_field_names: 'card_type,card_number,card_expiry_date',
        amount: '0.00',
        submit: 'Submit',
      };

      if (this.populateFormData) {
        requestObjParams.transaction_type = 'update_payment_token';
        requestObjParams.payment_token = this.paymentform.subscriptionId;
        requestObjParams.signed_field_names =
          'access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency,payment_method,bill_to_forename,bill_to_surname,bill_to_email,bill_to_phone,bill_to_address_line1,bill_to_address_city,bill_to_address_state,bill_to_address_country,bill_to_address_postal_code,payment_token';
      }

      this.paymentResponseObj.defaultPayment = this.paymentform.defaultPaymentMethod;
      this.paymentResponseObj.billingAddress = {};
      this.paymentResponseObj.billingAddress.firstName = firstName;
      this.paymentResponseObj.billingAddress.lastName = lastName;
      this.paymentResponseObj.billingAddress.titleCode = this.i18n.userTitle;
      this.paymentResponseObj.billingAddress.line1 = this.paymentform.billToAddressLine1;
      this.paymentResponseObj.billingAddress.line2 = this.paymentform.billToAddressLine2;
      this.paymentResponseObj.billingAddress.town = this.paymentform.billToAddressCity;
      this.paymentResponseObj.billingAddress.postalCode = this.paymentform.billToAddressPostalCode;
      this.paymentResponseObj.billingAddress.country = {
        isocode: country,
      };
      this.paymentResponseObj.billingAddress.region = {
        isocode: `${country}-${state}`,
      };
      this.paymentResponseObj.billingAddress.email =
        this.paymentform.billToEmail || self.globals.userInfo.email;
      this.paymentResponseObj.billingAddress.phone =
        this.paymentform.billToPhone || '';
      this.paymentResponseObj.billingAddress.defaultAddress = false;

      const requestConfig = {};
      requestConfig.data = requestObjParams;
      this.csService.getSignatureService(
        requestConfig,
        (response) => {
          const status = response.status;
          const data = response.data;
          if (status) {
            for (const key in data) {
              if (data.hasOwnProperty(key)) {
                const val = data[key];
                requestObjParams[key] = val;
              }
            }

            requestObjParams.card_type = this.paymentform.cardType;
            requestObjParams.card_number = this.paymentform.cardNumber;
            requestObjParams.card_expiry_date = `${
              this.paymentform.month.value
            }-${this.paymentform.year.value}`;

            this.passFormValuestoIframe(requestObjParams);
          } else {
            this.handleErrorCallback(data);
          }
        },
        this.handleSignatureError,
      );
    },
    handleErrorCallback(errorData) {
      if (errorData) {
      }
    },
    submitForm() {
      const self = this;
      this.$validator.validateAll().then((result) => {
        if (result) {
          self.doSignatureCall();
        } else {
          this.globals.setFocusByName(
            this.$el,
            this.globals.getElementName(this.errors),
          );
        }
      });
    },

    passFormValuestoIframe(requestObj) {
      const self = this;
      const eventMethod = window.addEventListener
        ? 'addEventListener'
        : 'attachEvent';
      const eventer = window[eventMethod];
      const messageEvent =
        eventMethod === 'attachEvent' ? 'onmessage' : 'message';

      const dest = document.getElementById('destination').contentWindow;

      dest.postMessage(requestObj, '*');
    },

    cvvValidation() {
      if (this.paymentform.cardType === '003') {
        return {
          required: true,
          max: 4,
          regex: /^\d\d\d\d$/,
        };
      }
      return {
        required: true,
        max: 4,
        regex: /^\d\d\d$/,
      };
    },
    fetchCreditCardType(event) {
      const self = this;
      let cardType = '';
      cardType = self.getCardType(event.target.value);

      if (cardType === 'Visa' || cardType === 'Visa Electron') {
        cardType = '001';
      } else if (cardType === 'Master') {
        cardType = '002';
      } else if (cardType === 'AMEX') {
        cardType = '003';
      } else if (cardType === 'Discover') {
        cardType = '004';
      } else if (
        cardType === 'Diners' ||
        cardType === 'Diners - Carte Blanche'
      ) {
        cardType = '005';
      } else if (cardType === 'JCB') {
        cardType = '007';
      } else if (cardType === 'Maestro') {
        cardType = '024';
      } else if (cardType === '') {
        cardType = 'UNKNOWN';
      }
      self.paymentform.cardType = cardType;
    },

    validateCreditCardExpiry() {
      let today,
        someday;
      const exMonth = this.paymentform.month;
      const exYear = this.paymentform.year;

      if (!exYear && !exMonth) {
        return false;
      } else if (!exMonth) {
        return true;
      } else if (!exYear) {
        return true;
      }

      today = new Date();
      someday = new Date();
      someday.setFullYear(exYear.value, exMonth.value, 1);

      if (someday < today) {
        return false;
      }

      this.paymentform.month = exMonth;
      this.paymentform.year = exYear;

      this.$validator.errors.remove('paymentFormMonth');
      this.$validator.errors.remove('paymentFormYear');

      return true;
    },
    zipCodeValidation() {
      if (this.paymentform.country.value == 'US') {
        return {
          required: true,
          max: 30,
          regex: /^\d{5}(-\d{4})?$/,
        };
      } else if (this.paymentform.country.value == 'CA') {
        return {
          required: true,
          max: 30,
          regex: /^([A-Za-z]\d[A-Za-z]\s?\d[A-Za-z]\d)$/,
        };
      }
      return {
        required: true,
        max: 30,
        regex: /^((\d{5}-\d{4})|(\d{5})|([A-Za-z]\d[A-Za-z]\s?\d[A-Za-z]\d))$/,
      };
    },
  },
  handleSaveCardDetailsResponse(response) {},
  handleSaveCardDetailsError(error) {},
  handleSaveCardDetailsGuestResponse(response) {},
  handleSaveCardDetailsGuestError(error) {},
};
