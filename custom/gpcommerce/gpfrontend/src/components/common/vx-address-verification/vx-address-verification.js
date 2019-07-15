import globals from '../../common/globals';
import CommonService from '../../common/services/common-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import mobileMixin from '../../common/mixins/mobile-mixin';
import {
  avsStatusCodes,
} from '../../common/mixins/vx-enums';
// import verifiedAddress from './vx-address-verification-mock';

export default {
  name: 'vx-address-verification',
  components: {
    vxSpinner,
  },
  mixins: [mobileMixin],
  props: {
    unverifiedAddress: Object,
    i18n: Object,
    isBussinessUnit: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      commonService: new CommonService(),
      // addressVerificationMock,
      savedAddress: '',
      verifiedAddress: {},
      current: 'current',
      verified: 'verified',
      addressVerificationSuccess: false,
      status: {},
      hideVerifiedAddress: false,
    };
  },
  computed: {},
  created() {},
  mounted() {
    /**  *On mounted call address verification service */
    this.unverifiedAddress = {
      ...this.unverifiedAddress,
      fromPage: 'my-account', //ask by hybris
    };
    const requestConfig = {};
    requestConfig.data = this.unverifiedAddress;
    this.commonService.addressVerification(requestConfig, this.handleAddressVerificationResponse, this.handleAddressVerificationError);
    this.$refs.spinner.showSpinner();
  },
  methods: {
    /**  * Method to select an address and emit it */
    selectedAddress(value, event) {
      if (value === this.current) {
        this.savedAddress = this.unverifiedAddress;
      } else if (value === this.verified) {
        this.savedAddress = this.verifiedAddress;
        if (this.isBussinessUnit) {
          this.savedAddress = { ...this.savedAddress,
            unit: {
              uid: this.unverifiedAddress.unit.uid
            }
          }
        }
        if (this.unverifiedAddress.userId) {
          this.savedAddress = { ...this.savedAddress,
            userId: this.unverifiedAddress.userId,
          }
        }
      }
      this.$emit('selectedAddress', {
        label: this.formAddressValues(this.savedAddress),
        value: this.savedAddress,
      });
    },
    /**  *  Format Address value */
    formAddressValues(address) {
      let userAddress = '';
      let companyName = '';
      let addressLine2 = '';
      const region = `<span class='address-content'>${address.town}</span> <span class='address-content'>${address.region.isocodeShort}</span> <span class='address-content'>${address.postalCode}</span>`;
      const name = address.firstName + ' ' + address.lastName;
      if (address && address.companyName) {
        companyName = address.companyName;
      }
      if (address && address.line2) {
        addressLine2 = address.line2;
      }
      if (address) {
        userAddress = [
          name,
          companyName,
          address.line1,
          addressLine2,
          region,
          address.country.isocode,
        ]
          .filter(Boolean);
      }
      return userAddress;
    },
    /**  *  On successfull response of AVS we will get verified address */
    handleAddressVerificationResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response.data) {
        this.status.code = response.data.code;
        this.status.message = response.data.formattedAddress;
        if (this.status.code && this.status.code === avsStatusCodes.failure) {
          this.hideVerifiedAddress = true;
        } else {
          this.hideVerifiedAddress = false;
          this.verifiedAddress = response.data;
        }
        this.addressVerificationSuccess = true;
      }
    },
    /**  * On Error from AVS */
    handleAddressVerificationError(error) {
      this.$refs.spinner.hideSpinner();
      this.$emit('error', error);
    },

  },
};
