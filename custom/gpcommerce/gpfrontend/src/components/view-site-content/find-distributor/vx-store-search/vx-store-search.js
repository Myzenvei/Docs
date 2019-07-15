import vxDropdownPrimary from '../../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../../common/globals';
import detectDeviceMixin from '../../../common/mixins/detect-device-mixin';
import googleMapsMixin from '../../../common/mixins/google-maps-mixin';
import vxModal from '../../../common/vx-modal/vx-modal.vue';
import flyoutBannerMixin from '../../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import mobileMixin from '../../../common/mixins/mobile-mixin';
import { flyoutStatus } from '../../../common/mixins/vx-enums';
import ProductMixin from '../../../common/mixins/product-mixin';

export default {
  name: 'vx-store-search',
  components: {
    vxDropdownPrimary,
    vxModal,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    prdInfo: {
      type: Object,
    },
    isFromPdp: {
      type: Boolean,
      default: false,
    },
    permissionDenied: {
      type: Boolean,
      default: false,
    },
    resetFilter: {
      type: Boolean,
      default: false,
      required: true,
    },
    productList: {
      type: Array,
      default: [],
    },
    listPageUrl: {
      type: String,
      default: '',
    },
    showSearchError: {
      type: Boolean,
      default: false,
    },
    isLinkShown: {
      type: Boolean,
      default: false,
    },
  },
  mixins: [
    ProductMixin,
    detectDeviceMixin,
    googleMapsMixin,
    flyoutBannerMixin,
    mobileMixin,
  ],
  data() {
    return {
      globals,
      storesError: '',
      pincode: '',
      showProductDropdown: false,
      distanceList: [
        {
          label: '50 miles',
          value: '50',
        },
        {
          label: '100 miles',
          value: '100',
        },
        {
          label: '200 miles',
          value: '200',
        },
        {
          label: '500 miles',
          value: '500',
        },
      ],
      isDisabled: false,
      isDisabledStock: false,
      showStockProducts: true,
      selectedWishlist: {},
      getProductDistributors: true,
      flyoutStatus,
    };
  },
  mounted() {
    this.onInit();
  },
  methods: {
    onInit() {
      /* Commenting this since we are hardcoding categories for now
      this.$refs.category.requestConfig.params = { fields: this.i18n.custom };
      this.$refs.category.generateRequest(); */
      this.$refs.distanceDropdown.setDropdownLabel(this.distanceList[0].label);
      if (this.$refs.productListdrp) {
        this.$refs.productListdrp.setDropdownLabel(
          this.i18n.productListDropDownLabel,
        );
      }
      if (!globals.getIsLoggedIn()) {
        this.isDisabled = true;
      }
      if (!this.isFromPdp) {
        this.isDisabledStock = true;
        this.showStockProducts = false;
      }
    },
    createDropdownList(item) {
      return {
        label: item.group_name,
        value: item.group_id,
      };
    },
    updateRadius(evt) {
      this.$emit('radiusUpdate', evt);
    },
    getDistributors() {
      if (this.pincode) {
        // this.locationTextErr = false;
        this.$emit('getDistributors', this.pincode);
      } else {
        // this.locationTextErr = true;
        this.showFlyout(
          this.flyoutStatus.error,
          `${this.i18n.locationTextError}`,
          false,
        );
      }
    },
    getLocation() {
      this.$emit('getLocation', true);
    },
    checkStock(event) {
      if (!event.target.checked && this.$refs.productListdrp) {
        if (Object.keys(this.selectedWishlist).length > 0) {
          this.$emit('noListSelected', {});
        }
      }
      this.getProductDistributors = !this.getProductDistributors;
      if (
        !this.isFromPdp &&
        Object.keys(this.selectedWishlist).length > 0 &&
        this.selectedWishlist.value !== '' &&
        this.getProductDistributors
      ) {
        this.$emit('listSelected', this.selectedWishlist);
      } else if (this.isFromPdp) {
        this.$emit('prodcheckedPDP', this.getProductDistributors);
      }
    },
    selectedList(list) {
      if (!list.value) {
        this.$refs.productListdrp.setDropdownLabel(
          this.i18n.productListDropDownLabel,
        );
      } else {
        if (!this.selectedWishlist.label) {
          this.showStockProducts = true;
          this.isDisabledStock = false;
        }
        this.selectedWishlist = list;
        if (this.getProductDistributors) {
          this.$emit('listSelected', this.selectedWishlist);
        }
      }
    },
  },
  watch: {
    resetFilter() {
      this.pincode = '';
      this.$refs.distanceDropdown.setDropdownLabel(this.distanceList[0].label);
    },
  },
};
