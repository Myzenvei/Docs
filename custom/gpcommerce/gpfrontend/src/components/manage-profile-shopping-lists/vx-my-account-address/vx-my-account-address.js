import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import vxAddEditAddress from '../../common/vx-add-edit-address/vx-add-edit-address.vue';
import vxDeleteAddress from '../../common/vx-delete-address/vx-delete-address.vue';
import vxProfileCard from '../../common/vx-profile-card/vx-profile-card.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  address
} from './address-mock';
import {
  myAccountAddressStatus
} from '../../common/mixins/vx-enums';
import {
  eventBus
} from '../../../modules/event-bus';

export default {
  name: 'vx-my-account-address',
  components: {
    vxModal,
    vxSpinner,
    vxAddEditAddress,
    vxProfileCard,
    vxDeleteAddress,
  },
  props: ['i18n'],
  data() {
    return {
      globals,
      hideUnderReviewSection: true,
      addresses: [],
      isAdmin: false,
      selectedAddress: {},
      editAddressUrl: '',
      addressModalHeading: '',
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      isActive: false,
      isDisabled: false,
      modalScrollable: true
    };
  },
  computed: {},
  mounted() {
    this.getAddresses();
    // TODO Update Service instead of Ajax Calls
    // Service Changes
    // const manageProfileShoppingListService = new ManageProfileShoppingListService(this.globals.getHeaders());
    // manageProfileShoppingListService.getAddresses(this.handleGetAddressResponse);
    this.$refs.spinner.showSpinner();
    eventBus.$on('removeScrollbar', (isRemoveScrollbar) => {
      this.modalScrollable = !isRemoveScrollbar;
    });
  },
  methods: {
    getAddresses() {
      this.manageProfileShoppingListService.getAddresses({}, this.handleGetAddressResponse, this.handleGetAddressError);
    },
    deletedAddress() {
      this.$refs.deleteAddressModal.close();
      this.getAddresses();
    },
    saveAddress(address) {
      const addressObj = {
        ...address.value,
        fromPage: 'my-account',
      };
      const requestConfig = {};
      requestConfig.data = addressObj;
      if (address.value.id) {
        // Edit Address
        this.manageProfileShoppingListService.updateAddress(requestConfig, this.handleCreateAddressResponse, this.handleCreateAddressError, address.value.id);
      } else {
        // Add Address
        this.manageProfileShoppingListService.createShippingAddress(requestConfig, this.handleCreateAddressResponse, this.handleCreateAddressError);
      }
      this.$refs.addNewAddressModal.close();
      this.$refs.spinner.showSpinner();
    },
    handleEditAddressResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.getAddresses();
    },
    handleEditAddressError(error) {
      this.$refs.spinner.hideSpinner();
    },
    handleAddressModal(updatedAddress) {
    },
    openAddressModal(isNew, address, event) {
      if (isNew) {
        this.selectedAddress = {};
        this.addressModalHeading = this.i18n.addAddress.addAddressHeading;
      } else {
        this.selectedAddress = address;
        this.addressModalHeading = this.i18n.addAddress.editAddressHeading;
      }

      this.$refs.addNewAddressModal.open(event);
    },
    openDeleteModal(address, event) {
      this.selectedAddress = address;
      this.$refs.deleteAddressModal.open(event);
    },
    handleGetAddressResponse(response) {
      this.addresses = response.data.addresses;
      this.isActive = false;
      this.isDisabled = false;
      this.addresses.map((address) => {
        if (address.approvalStatus === myAccountAddressStatus.active) { this.isActive = true; }
        if (address.approvalStatus === myAccountAddressStatus.disabled) { this.isDisabled = true; }
      })
      this.isAdmin = response.data.isAdmin;
      for (const i in this.addresses) {
        if (
          this.addresses[i].approvalStatus === myAccountAddressStatus.pending ||
          this.addresses[i].approvalStatus === myAccountAddressStatus.pendingbyadmin ||
          this.addresses[i].approvalStatus === myAccountAddressStatus.pendingbygp
        ) {
          this.hideUnderReviewSection = false;
          break;
        }
      }
      this.$refs.spinner.hideSpinner();
    },
    handleGetAddressError(error) {
      this.$refs.spinner.hideSpinner();
    },
    handleCreateAddressResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.getAddresses();
    },
    handleCreateAddressError(error) {
      this.$refs.spinner.hideSpinner();
    },
    enableUserAddress(addId, uid, userId) {
      this.$refs.spinner.showSpinner();
      const addConfig = {
        addId,
        payload: {
          approvalStatus: myAccountAddressStatus.active,
          unit: {
            uid,
          },
          userId,
        },
      };
      const manageProfileShoppingListService = new ManageProfileShoppingListService(
        this.globals.getHeaders(),
      );
      manageProfileShoppingListService.toggleUserAddress({}, this.handleAddressUpdateResponse, this.handleAddressUpdateError, addConfig);
    },
    disableUserAddress(addId, uid, userId) {
      this.$refs.spinner.showSpinner();
      const addConfig = {
        addId,
        payload: {
          approvalStatus: myAccountAddressStatus.disabled,
          unit: {
            uid,
          },
          userId,
        },
      };
      const manageProfileShoppingListService = new ManageProfileShoppingListService(
        this.globals.getHeaders(),
      );
      manageProfileShoppingListService.toggleUserAddress({}, this.handleAddressUpdateResponse, this.handleAddressUpdateError, addConfig);
    },
    handleAddressUpdateResponse(response) {
      const status = response.status;
      const data = response.data;
      if (status) {
        this.getAddresses();
      }
    },
    handleAddressUpdateError(error) {},
    rejectUserAddress(addId, uid, userId) {
      this.$refs.spinner.showSpinner();
      const addConfig = {
        addId,
        payload: {
          approvalStatus: 'REJECTED',
          unit: {
            uid,
          },
          userId,
          fromPage: 'my-account',
        },
      };
      const manageProfileShoppingListService = new ManageProfileShoppingListService(
        this.globals.getHeaders(),
      );
      manageProfileShoppingListService.toggleUserAddress({}, this.handleAddressUpdateResponse, this.handleAddressUpdateError, addConfig);
    },
    approveUserAddress(addId, uid, userId) {
      this.$refs.spinner.showSpinner();
      const addConfig = {
        addId,
        payload: {
          approvalStatus: 'PENDINGBYGP',
          unit: {
            uid,
          },
          userId,
          fromPage: 'my-account',
        },
      };
      const manageProfileShoppingListService = new ManageProfileShoppingListService(
        this.globals.getHeaders(),
      );
      manageProfileShoppingListService.toggleUserAddress({}, this.handleAddressUpdateResponse, this.handleAddressUpdateError, addConfig);
    },
  },
};
