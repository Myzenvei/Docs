import Paginate from 'vuejs-paginate';
import vxStoreSearch from './../vx-store-search/vx-store-search.vue';
import vxStoreList from './../vx-store-list/vx-store-list.vue';
import vxStoreLocation from './../vx-store-location/vx-store-location.vue';
import ViewSiteContentService from '../../../common/services/view-site-content-service';
import PdpService from '../../../common/services/pdp-service';
import VxSpinner from '../../../common/vx-spinner/vx-spinner.vue';
import globals from '../../../common/globals';
import storeLocatorMixin from '../../../common/mixins/store-locator-mixin';
import mobileMixin from '../../../common/mixins/mobile-mixin';
import googleMapsMixin from '../../../common/mixins/google-maps-mixin';
import flyoutBannerMixin from '../../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import detectDeviceMixin from '../../../common/mixins/detect-device-mixin';
import { flyoutStatus } from '../../../common/mixins/vx-enums';
import { globalEventBus } from '../../../../modules/event-bus';

export default {
  name: 'vx-find-distributor',
  components: {
    vxStoreList,
    vxStoreLocation,
    vxStoreSearch,
    ViewSiteContentService,
    VxSpinner,
    'vx-paginate': Paginate,
  },
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    isPdpPage: {
      type: String,
      default: 'false',
    },
    prdCode: {
      type: String,
    },
    timeoutVal: {
      type: Number,
      default: 15000,
    },
    listPageUrl: {
      type: String,
      default: '',
    },
  },
  mixins: [storeLocatorMixin, mobileMixin, googleMapsMixin, flyoutBannerMixin, detectDeviceMixin],
  data() {
    return {
      globals,
      viewSiteContentService: new ViewSiteContentService(),
      pdpService: new PdpService(),
      storesList: [],
      productList: [],
      isNearByClicked: false,
      permissionDenied: false,
      pagination: {
        next: '&#62;',
        previous: '&#60;',
        totalPages: '',
        pageRange: 5,
        totalResults: '',
      },
      distributorFilters: {
        radius: 50,
        longitude: '',
        latitude: '',
        pageSize: 6,
        currentPage: 0,
        searchText: '',
        productCode: '',
        wishlistUid: '',
      },
      productInfo: {},
      noOfDistributor: 0,
      locationTimeout: '',
      resetFilterFlag: false,
      userPosition: {
        coords: {
          latitude: '',
          longitude: '',
        },
      },
      errorMsgFlag: false,
      flyoutStatus,
      showSearchError: false,
      selectedProductlist: {
        label: '',
        value: '',
      },
      existingList: true,
      isLinkShown: false,
    };
  },
  computed: {

  },
  mounted() {
    this.isFromPdp();
    this.getLocation(false);
    if (globals.getIsLoggedIn() && !this.isFromPdp()) {
      this.getAllWishList();
    }
    globalEventBus.$on('list-create-or-update', (data) => {
      if (globals.getIsLoggedIn() && !this.isFromPdp()) {
        if (!data.existingList) {
          this.$refs.pageSpinner.showSpinner();
          this.existingList = false;
          this.getAllWishList();
        } else if (this.selectedProductlist.value === data.wishlistId) {
          this.getProductDistributors(this.selectedProductlist);
        }
      }
    });
  },
  methods: {
    getDistributorsData(evt) {
      this.$refs.pageSpinner.showSpinner();
      this.distributorFilters = {
        ...this.distributorFilters,
        searchText: evt,
      };
      this.paginationCallback();
    },
    getLocation(isFindNearby) {
      if (navigator.geolocation) {
        this.dismissFlyout();
        if (this.isIEBrowser() && this.userPosition.coords.latitude === '' && this.userPosition.coords.longitude === '') {
          this.locationTimeout = setTimeout(this.geoErrorHandler, this.timeoutVal);
        }
        navigator.geolocation.getCurrentPosition(this.currentPosition, this.geoErrorHandler);
        this.$refs.pageSpinner.showSpinner();
      }
      this.isNearByClicked = isFindNearby;
    },

    geoErrorHandler(error) {
      clearTimeout(this.locationTimeout);
      if (error && error.code === error.PERMISSION_DENIED) {
        if (this.isFromPdp() && !this.isNearByClicked) {
          this.getProductInfo(this.prdCode);
        } else {
          if (this.errorMsgFlag) {
            this.showFlyout(
              this.flyoutStatus.error,
              `${this.i18n.findDistributor.permissionDeniedError}`,
              false,
            );
          }
          this.$refs.pageSpinner.hideSpinner();
        }
      } else {
        if (this.errorMsgFlag) {
          this.showFlyout(
            this.flyoutStatus.error,
            `${this.i18n.findDistributor.permissionDeniedError}`,
            false,
          );
        }
        this.$refs.pageSpinner.hideSpinner();
      }
      this.errorMsgFlag = true;
      if (this.isIEBrowser() && this.userPosition.coords.latitude !== '' && this.userPosition.coords.longitude !== '') {
        this.$refs.pageSpinner.showSpinner();
        this.currentPosition(this.userPosition);
      }
    },
    currentPosition(position) {
      clearTimeout(this.locationTimeout);
      this.resetFilterFlag = !this.resetFilterFlag;
      this.distributorFilters = {
        ...this.distributorFilters,
        longitude: position.coords.longitude,
        latitude: position.coords.latitude,
        searchText: '',
        radius: 50,
      };
      if (!this.isFromPdp()) {
        this.paginationCallback();
      } else {
        if (!this.isNearByClicked) {
          this.getProductInfo(this.prdCode);
        } else {
          this.paginationCallback();
        }
      }
      if (this.isIEBrowser()) {
        this.userPosition.coords.latitude = position.coords.latitude;
        this.userPosition.coords.longitude = position.coords.longitude;
      }
    },
    handleDistributorsResponse(resp) {
      this.storesList = resp.data.stores;
      this.pagination.totalPages = resp.data.pagination.totalPages;
      this.pagination.totalResults = resp.data.pagination.totalResults;
      this.noOfDistributor = resp.data.pagination.totalResults;
      if (this.storesList.length) {
        this.storeSelected(this.storesList[0], true);
      }
      this.$refs.pageSpinner.hideSpinner();
    },
    handleDistributorsError(err) {
      this.$refs.pageSpinner.hideSpinner();
    },
    getUpdatedDistributors(evt) {
      this.distributorFilters = {
        ...this.distributorFilters,
        radius: evt.value,
      };
      if (this.distributorFilters.searchText || (this.distributorFilters.latitude && this.distributorFilters.longitude)) {
        this.$refs.pageSpinner.showSpinner();
        this.paginationCallback();
      }
    },
    fetchDistributors() {
      this.viewSiteContentService.fetchStoresWithLatLong(this.distributorFilters, this.handleDistributorsResponse, this.handleDistributorsError);
    },
    paginationCallback(pageNum) {
      this.distributorFilters = {
        ...this.distributorFilters,
        currentPage: pageNum - 1,
      };
      this.showSearchError = false;
      if (this.pagination.totalPages > this.distributorFilters.currentPage) {
        this.$refs.pageSpinner.showSpinner();
      } else {
        this.distributorFilters = {
          ...this.distributorFilters,
          currentPage: 0,
        };
      }
      this.dismissFlyout();
      this.fetchDistributors();
    },
    isFromPdp() {
      return this.isPdpPage === true;
    },
    getProductInfo(id) {
      this.pdpService.getProductData(
        {},
        this.handleSuccessResponse,
        this.handleErrorResponse,
        id,
      );
    },
    handleSuccessResponse(response) {
      const data = response.data;
      let primaryImg = {};
      if (data.images) {
        primaryImg = data.images.map((img) => {
          if (img.imageType === 'PRIMARY') {
            return {
              ...primaryImg,
              pImage: img.thumbnailUrl,
              pAltText: img.altText,
            };
          }
        })[0];
      }
      this.productInfo = {
        ...this.productInfo,
        image: primaryImg.pImage ? primaryImg.pImage : '',
        altText: primaryImg.pAltText ? primaryImg.pAltText : '',
        prdTitle: data.name ? data.name : '',
        prdSKUId: data.code ? data.code : '',
        prdCustId: data.cmirCode ? data.cmirCode : '',
        pdpLink: data.url ? data.url : '',
        displayAttributes: data.displayAttributes,
      };
      this.getDistributorFromProdID(true);
    },
    handleErrorResponse() { },
    getAllWishList() {
      this.viewSiteContentService.getShoppingLists({}, this.handleGetWishlistResponse, this.handleGetWishlistError);
    },
    handleGetWishlistResponse(response) {
      let data = response.data.wishlists;
      if (data && data.length > 0) {
        if (response.data.wishlists.length > 1) {
          data = data.sort((list1, list2) => new Date(list1.modifiedTime) - new Date(list2.modifiedTime)).reverse();
        }
        data.map((item, index) =>
          this.$set(this.productList, index, {
            label: item.name,
            value: item.wishlistUid,
          }));
        this.isLinkShown = false;
      } else if (globals.getIsLoggedIn()) {
        this.$set(this.productList, 0, {
          label: this.i18n.findDistributor.noProductListLabel,
          value: '',
          disable: true,
        });
        this.isLinkShown = true;
      } else {
        this.productList = [];
        this.isLinkShown = false;
      }
      if (!this.existingList) {
        this.$refs.pageSpinner.hideSpinner();
      }
    },
    handleGetWishlistError() { },
    getProductDistributors(selectedList) {
      this.selectedProductlist = selectedList;
      if (Object.keys(selectedList).length > 0) {
        this.distributorFilters = {
          ...this.distributorFilters,
          wishlistUid: selectedList.value,
        };
      } else {
        this.distributorFilters = {
          ...this.distributorFilters,
          wishlistUid: '',
        };
      }
      if (this.distributorFilters.searchText || (this.distributorFilters.latitude && this.distributorFilters.longitude)) {
        this.$refs.pageSpinner.showSpinner();
        this.paginationCallback();
      } else {
        this.showSearchError = true;
        this.showFlyout(this.flyoutStatus.error, `${this.i18n.findDistributor.searchCriteriaError}`, false);
      }
    },
    getDistributorFromProdID(prodStock) {
      this.distributorFilters = {
        ...this.distributorFilters,
        productCode: prodStock ? this.prdCode : '',
        wishlistUid: '',
      };
      if ((this.distributorFilters.latitude && this.distributorFilters.longitude) || this.distributorFilters.searchText) {
        this.$refs.pageSpinner.showSpinner();
        this.paginationCallback();
      } else {
        this.$refs.pageSpinner.hideSpinner();
        this.showSearchError = true;
        this.showFlyout(this.flyoutStatus.error, `${this.i18n.findDistributor.searchCriteriaError}`, false);
      }
    }
  },
};
