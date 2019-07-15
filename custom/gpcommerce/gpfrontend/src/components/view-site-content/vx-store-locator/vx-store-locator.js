import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import vxMapLocator from '../../common/vx-map-locator/vx-map-locator.vue';
import globals from '../../common/globals';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import VxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import googleMapsMixin from '../../common/mixins/google-maps-mixin';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';
import ViewSiteContentService from '../../common/services/view-site-content-service';
import { iriProductId, brandValues } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-store-locator',
  components: {
    vxMapLocator,
    vxModal,
    VxSpinner,
    vxDropdownPrimary,
  },
  mixins: [googleMapsMixin, detectDeviceMixin, mobileMixin],
  props: {
    i18n: {
      type: Object,
      required: true,
    },
  },
  provide() {
    return {
      getMap: this.$refs.storemap,
    };
  },
  data() {
    return {
      globals,
      viewSiteContentService: new ViewSiteContentService(),
      productUrl: '',
      storesUrl: '',
      pincode: '',
      storesError: '',
      categoryError: '',
      productError: '',
      showProductDropdown: false,
      selectedStore: {
        NAME: '',
        ADDRESS: '',
        PHONE: '',
        img: '',
        STORE_ID: '',
        CITY: '',
        STATE: '',
        ZIP: '',
      },
      distanceList: [
        {
          label: '0-5 miles',
          value: '5',
          ariaLabel: '0 to 5 miles',
        },
        {
          label: '6-10 miles',
          value: '10',
          ariaLabel: '6 to 10 miles',
        },
        {
          label: '10-15 miles',
          value: '15',
          ariaLabel: '10 to 15 miles',
        },
        {
          label: '15+ miles',
          value: '16',
          ariaLabel: '15+ miles',
        },
      ],
      categoryList: [
        {
          label: '',
          value: '',
        },
      ],
      productList: [],
      storesList: [],
      filters: {
        categoryItem: '',
        productItem: '',
        distanceItem: '',
      },
      storeData: {},
      isMapCreated: false,
      mapLocatorObj: '',
      location: '',
      activeStore: false,
    };
  },
  computed: {},
  mounted() {
    this.onInit();
  },
  methods: {
    onInit() {
      /* Commenting this since we are hardcoding categories for now
      this.$refs.category.requestConfig.params = { fields: this.i18n.custom };
      this.$refs.category.generateRequest(); */
      switch (this.globals.siteId) {
        case brandValues.sparkle: {
          this.categoryList[0].value = iriProductId.sparkle;
          break;
        }
        case brandValues.brawny: {
          this.categoryList[0].value = iriProductId.brawny;
          break;
        }
        default: {
          this.categoryList[0].value = iriProductId.vanityFair;
          break;
        }
      }
      this.categoryList[0].label = this.i18n.categoryLabel;

      this.$refs.distanceDropdown.setDropdownLabel(this.distanceList[1].label);
      this.filters.distanceItem = this.distanceList[1];

      this.$refs.categoryDropdown.setDropdownLabel(this.categoryList[0].label);
      this.filters.categoryItem = this.categoryList[0];

      this.viewSiteContentService.product(
        {},
        this.handleProductResponse,
        this.handleProductsError,
        this.filters.categoryItem.value,
      );
    },

    createFormattedAddress(add, city, state, zip) {
      return `${add},${city},${state},${zip},USA`;
    },

    storeSelected(store, isDesktop) {
      const self = this;
      self.selectedStore.NAME = store.NAME;
      self.selectedStore.ADDRESS = store.ADDRESS;
      self.selectedStore.PHONE = store.PHONE;
      self.selectedStore.img = `${store.STORE_LOGO}`;
      self.selectedStore.STORE_ID = store.STORE_ID;
      self.selectedStore.CITY = store.CITY;
      self.selectedStore.STATE = store.STATE;
      self.selectedStore.ZIP = store.ZIP;
      self.location = { lat: store.LATITUDE, lng: store.LONGITUDE };
      if (this.$refs.storemap) {
        this.$refs.storemap.testGeoLocationMarker(this.location);
      }

      if (!isDesktop && this.isMobile()) {
        const device = this.getDevice();
        if (device === 'IOS') {
          if (this.isIOSChrome()) {
            window.open(
              `https://maps.google.com/maps?q=${store.ADDRESS}
              &zoom=14&amp;ll=`,
            );
          } else {
            window.open(
              `maps://maps.google.com/maps?q=${self.location.lat},${
                self.location.lng
              }&zoom=14&amp;`,
            );
          }
        } else if (device === 'ANDROID') {
          window.open(
            `https://maps.google.com/?q=${store.ADDRESS}
            &zoom=14&amp;ll=`,
          );
        }
      }
    },

    mapLoaded() {
      this.$refs.storemap.testGeoLocationMarker(this.location);
    },

    desktopMapNavigation() {
      window.open(
        `https://maps.google.com/?daddr=${
          this.selectedStore.ADDRESS
        }&zoom=14&amp;ll=`,
      );
    },

    searchStores() {
      this.storesError = '';
      this.categoryError = '';
      this.productError = '';
      if (!this.pincode) {
        this.storesError = this.i18n.pinError;
        this.$refs.filtersModal.close();
        return;
      } else if (!this.filters.categoryItem.value) {
        this.categoryError = this.i18n.categoryError;
        return;
      }
      const productId =
        this.filters.productItem.value || this.filters.categoryItem.value;
      this.$refs.filtersModal.close();
      this.$refs.spinner.showSpinner();
      this.viewSiteContentService.store(
        {},
        this.handleStoresResponse,
        this.handleStoresError,
        productId,
        this.pincode,
        this.filters.distanceItem.value,
      );
    },

    getLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(this.currentPosition);
        this.$refs.spinner.showSpinner();
      }
      navigator.geolocation.watchPosition(
        this.geoSuccessHandler,
        this.geoErrorHandler,
      );
    },

    geoSuccessHandler(position) {},

    geoErrorHandler(error) {
      if (error.code === error.PERMISSION_DENIED) {
        this.$refs.spinner.hideSpinner();
        this.$refs.locationModal.open();
      }
    },

    currentPosition(position) {
      const self = this;
      const latlng = `${position.coords.latitude},${position.coords.longitude}`;
      self.getLatitudeLongitude(latlng, (result) => {
        const fullAddress = _.find(result.address_components, (address) => {
          const resultType = _.find(address, type => type == 'postal_code');
          if (resultType) {
            return address;
          }
        });
        if (fullAddress) {
          self.pincode = fullAddress.short_name || fullAddress.long_name;
          self.searchStores();
          self.$refs.spinner.hideSpinner();
        }
      });
    },

    openFilterModal(event) {
      this.$refs.filtersModal.open(event);
    },

    categoriesDropdownLoaded() {
      if (this.filters.distanceItem.value) {
        this.$refs.distanceDropdown.setDropdownLabel(
          this.filters.distanceItem.label,
        );
      }
      if (this.filters.categoryItem.value) {
        this.$refs.categoryDropdown.setDropdownLabel(
          this.filters.categoryItem.label,
        );
      } else {
        this.$refs.categoryDropdown.setDropdownLabel(this.i18n.allProducts);
      }
    },

    productDropdownLoaded() {
      if (this.filters.productItem.value) {
        this.$refs.productDropdown.setDropdownLabel(
          this.filters.productItem.label,
        );
      } else {
        this.$refs.productDropdown.setDropdownLabel(this.i18n.productType);
      }
    },

    createDropdownList(item) {
      return {
        label: item.group_name,
        value: item.group_id,
      };
    },

    createProductDropdownList(item) {
      return {
        label: item.upc_name,
        value: item.upc_code,
      };
    },

    handleCategoryResponse(response) {
      this.categoryList = response.data.groups.group.map(
        this.createDropdownList,
      );
      this.$refs.categoryDropdown.setDropdownLabel(this.i18n.allProducts);
    },

    handleCategoryError(error) {},

    handleProductResponse(response) {
      this.productList = response.data.products.product.map(
        this.createProductDropdownList,
      );
      if (response.data.products.product.length) {
        this.showProductDropdown = true;
      }
      this.$refs.spinner.hideSpinner();
    },

    handleProductsError(error) {
      this.$refs.spinner.hideSpinner();
    },

    handleStoresResponse(response) {
      this.storesList = response.data.RESULTS.STORES
        ? response.data.RESULTS.STORES.STORE
        : [];
      if (this.storesList.length) {
        this.storeData = response.data.RESULTS.STORES.STORE[0];
        this.storeSelected(response.data.RESULTS.STORES.STORE[0], true);
      }
      this.$refs.spinner.hideSpinner();
    },

    handleStoresError(error) {
      this.$refs.spinner.hideSpinner();
    },
  },
  watch: {
    /* commenting for now since categories are hardcoded to jsut 1
    'filters.categoryItem': function() {
      this.$refs.spinner.showSpinner();
      this.categoryError = '';
      this.$refs.product.requestConfig.url =
        this.globals.serviceUrls.products + this.filters.categoryItem.value;
      this.$refs.product.generateRequest();
    },
    'filters.productItem': function() {
      this.productError = '';
    }, */
  },
};
