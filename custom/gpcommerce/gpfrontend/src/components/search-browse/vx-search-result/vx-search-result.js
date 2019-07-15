import globals from '../../common/globals';
import vxListPage from '../vx-list-page/vx-list-page.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxShareResource from '../../pdp/vx-share-resource/vx-share-resource.vue';
import AnalyticsService from '../../common/services/analytics-service';
import SearchBrowseService from '../../common/services/search-browse-service';
import {
  eventBus,
  cartEventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import mobileMixin from '../../common/mixins/mobile-mixin';
import flyoutBannerMixin from '../../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxStarRating from '../../common/vx-star-rating/vx-star-rating.vue';
import {
  facetName,
  contentType,
  brandValues,
  cookies,
  addToCartStatus,
  flyoutStatus,
} from '../../common/mixins/vx-enums';
import vxSaveCart from '../../manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import vxShareItem from '../../pdp/vx-share-item/vx-share-item.vue';

export default {
  name: 'vx-search-result',
  mixins: [cookiesMixin, mobileMixin, flyoutBannerMixin],
  props: [
    'categoryId',
    'searchResultPage',
    'isFavorites',
    'isBazaarVoice',
    'isContentEnabled',
    'isBulkEnabled',
    'searchBrowseI18n',
    'isSampleCart',
    'singleProductEnabled',
  ],
  components: {
    vxListPage,
    vxDropdownPrimary,
    vxSpinner,
    vxStarRating,
    vxModal,
    vxShareResource,
    vxSaveCart,
    vxShareItem,
  },
  data() {
    return {
      globals,
      cookies,
      addToCartStatus,
      flyoutStatus,
      contentType,
      brandValues,
      i18n: this.searchBrowseI18n.searchResult,
      searchData: {},
      appliedFilterList: [],
      sortsArray: [],
      contentSortArray: [],
      contentSortValue: '',
      contentSortType: '',
      searchString: '',
      currentSortValue: '',
      loadMoreProducts: false,
      pageNumber: 0,
      sortType: '',
      filterHidden: true,
      bottom: false,
      productList: [],
      lazyLoadRequest: false,
      noResults: false,
      analyticsService: new AnalyticsService(),
      searchBrowseService: new SearchBrowseService(),
      dataLoaded: false,
      searchedTerm: '',
      showRating: false,
      showReviews: false,
      facetName,
      contentSearch: {},
      showContentTab: false,
      shareResource: {},
      showBackToTop: false,
      contentSearchData: [],
      selectedBulkProducts: [],
      selectAll: false,
      showPanel: false,
      bulkLazyLoad: false,
      maxCompareCount: '',
      guestListName: '',
      maxCompare: {
        mobile: 2,
        desktop: 4,
      },
      isCompareDisabled: false,
      compareCount: 0,
      isSampleOrderDisabled: true,
      showSampleOrderButton: false,
      defaultContentTab: false,
      selectedListName: '',
    };
  },
  watch: {
    bottom(bottom) {
      if (bottom) {
        this.loadMore();
      }
      this.toggleSelectAll();
    },
    selectedBulkProducts() {
      if (this.selectedBulkProducts.length > 0) {
        this.showPanel = true;
      } else {
        this.showPanel = false;
      }
      this.toggleSelectAll();
    },
  },
  computed: {
    isCategoryPage() {
      return !(
        this.searchData.freeTextSearch &&
        !(this.categoryId && this.categoryId.length > 0)
      );
    },
  },
  mounted() {
    const self = this;
    this.onInitCheck();
    if (self.isMobile()) {
      self.maxCompareCount = self.maxCompare.mobile;
    } else {
      self.maxCompareCount = self.maxCompare.desktop;
    }
    window.addEventListener('scroll', (e) => {
      if (window.scrollY > 0 || window.pageYOffset > 0) {
        this.showBackToTop = true;
      } else {
        this.showBackToTop = false;
      }
      self.bottom = self.bottomVisible();
    });
    eventBus.$on('selectCheckedElement', (data) => {
      if (this.selectedBulkProducts.indexOf(data) < 0) {
        this.selectedBulkProducts.push(data);
        this.isSelectedProductsComparable(this.selectedBulkProducts);
      }
    });
    eventBus.$on('selectUnCheckedElement', (data) => {
      this.selectedBulkProducts.splice(
        this.selectedBulkProducts.indexOf(data),
        1,
      );
      this.isSelectedProductsComparable(this.selectedBulkProducts);
    });
    globalEventBus.$on('bulkShare', (val) => {
      this.bulkShare(val);
    });
    globalEventBus.$on('wishlist-name', (val) => {
      this.selectedListName = val;
    });
  },
  updated() {
    if (this.$refs.sortBy != undefined) {
      this.$refs.sortBy.setDropdownLabel(
        `${this.i18n.sortPrefix} ${this.currentSortValue}`,
      );
    }
    if (this.$refs.contentSortBy != undefined) {
      this.$refs.contentSortBy.setDropdownLabel(
        `${this.i18n.sortPrefix} ${this.contentSortValue}`,
      );
    }
  },
  methods: {
    bottomVisible() {
      const scrollY = window.scrollY || window.pageYOffset;
      const visible = document.documentElement.clientHeight;
      const pageHeight = document.documentElement.scrollHeight;
      const bottomOfPage = visible + scrollY >= pageHeight;
      return bottomOfPage || pageHeight < visible;
    },
    loadMore(event) {
      if ((this.searchData && this.searchData.pagination.totalPages > this.pageNumber &&
          this.searchData.pagination.totalPages !== this.pageNumber + 1 && !((this.contentSearchData && this.contentSearchData.length) || !this.isCategoryPage)) ||
          (this.searchData && this.searchData.pagination.totalPages > this.pageNumber &&
            this.searchData.pagination.totalPages !== this.pageNumber + 1 && !((this.contentSearchData && this.contentSearchData.length) || this.showContentTab)) ||
        (this.searchData && this.searchData.pagination.totalPages > this.pageNumber &&
          this.searchData && this.searchData.pagination.totalPages !== this.pageNumber + 1 &&
          document.querySelector('.tab-container ul li.tab-item.active #tab-products')) ||
        (this.contentSearch &&
          this.contentSearch.pagination &&
          this.contentSearch.pagination.totalPages > this.pageNumber &&
          this.contentSearch.pagination.totalPages !== this.pageNumber + 1 &&
          document.querySelector('.tab-container ul li.tab-item.active #tab-content'))
      ) {
        this.lazyLoadRequest = true;
        if (this.isBulkEnabled) {
          this.bulkLazyLoad = this.lazyLoadRequest;
        }
        this.pageNumber += 1;
        if (this.categoryId && this.categoryId.length > 0) {
          this.requestUrl = `/category/${this.categoryId}${
            window.location.search
          }&currentPage=${this.pageNumber}`;
        } else {
          this.requestUrl = `/search${window.location.search}&currentPage=${
            this.pageNumber
          }`;
        }
        this.generateUrlRequest(this.requestUrl);
      }
    },
    onInitCheck() {
      const self = this;
      if (this.categoryId && this.categoryId.length > 0) {
        // this.requestUrl = '/category/' + this.categoryId + window.location.search.replace('?q', '?query');
        this.requestUrl = `/category/${this.categoryId}${
          window.location.search
        }`;
        // Storing the value for category dropdown in B2B
        this.createCookie('categoryVal', this.categoryId);
      } else {
        this.requestUrl = `/search${window.location.search}`;
      }
      this.generateUrlRequest(this.requestUrl);
      self.readCookie('ProductCompare'); // calling cookie mixin
    },
    updateSortBy(sortBy) {
      this.sortType = sortBy;
      let currentQuery = window.location.search;
      const selectedItem = this.sortType.value;
      this.currentSortValue = this.sortType.label;
      this.resetCurrentPageNumber();
      if (this.categoryId) {
        currentQuery = currentQuery.replace(
          '?',
          `/category/${this.categoryId}?sort=${selectedItem}&`,
        );
      } else {
        currentQuery = currentQuery.replace(
          '?',
          `/search?sort=${selectedItem}&`,
        );
      }
      this.eraseCookie(this.cookies.selectedBulkProducts);
      this.generateUrlRequest(currentQuery);
    },
    updateContentSortBy(sortBy) {
      this.contentSortType = sortBy;
      let currentQuery = window.location.search;
      const selectedItem = this.contentSortType.value;
      this.contentSortValue = this.contentSortType.label;
      this.resetCurrentPageNumber();
      if (this.categoryId) {
        currentQuery = currentQuery.replace(
          '?',
          `/category/${this.categoryId}?sort=${selectedItem}&`,
        );
      } else {
        currentQuery = currentQuery.replace(
          '?',
          `/search?sort=${selectedItem}&`,
        );
      }
      this.generateUrlRequest(currentQuery);
    },
    createSortsArray(sortData) {
      if (this.searchData) {
        if (sortData.length !== 0) {
          this.sortsArray = [];
          sortData.map((item) => {
            this.sortsArray.push({
              label: item.name,
              value: item.code,
              selected: item.selected,
            });
          });
          if (!this.currentSortValue) {
            this.currentSortValue = this.sortsArray.filter(
              item => item.selected !== false,
            )[0].label;
          }
        }
      }
    },
    createContentSortsArray(sortData) {
      if (this.contentSearch) {
        if (sortData.length !== 0) {
          this.contentSortArray = [];
          sortData.map((item) => {
            this.contentSortArray.push({
              label: item.name,
              value: item.code,
              selected: item.selected,
            });
          });
          if (!this.contentSortValue) {
            this.contentSortValue = this.contentSortArray.filter(
              item => item.selected !== false,
            )[0].label;
          }
        }
      }
    },
    generateUrlRequest(query) {
      // query = updateQuery(query, response.data.freeTextSearch);
      let updatedQuery = query;
      if (this.searchedTerm !== '' && query.indexOf('searchText') < 0) {
        updatedQuery = `${query}${this.searchedTerm}`;
      }
      const self = this;
      if (this.globals.loggedIn) {
        const userId = this.globals.uid;
        self.searchBrowseService.getSearchResults({},
          this.handleSearchResponse,
          this.handleSearchError,
          updatedQuery,
          userId,
        );
      } else {
        self.searchBrowseService.getSearchResults({},
          this.handleSearchResponse,
          this.handleSearchError,
          updatedQuery,
        );
      }
      self.$refs.spinner.showSpinner();
    },
    // This function will check selected facets and genrate applied filter tags
    checkSelectedFilters() {
      const self = this;
      let selectedFilterObj = {};
      self.appliedFilterList = [];
      if (this.searchData.breadcrumbs.length) {
        this.searchData.breadcrumbs.map((item) => {
          selectedFilterObj = {
            category: item.facetName,
            filterValue: item.facetValueName,
          };
          self.appliedFilterList.push(selectedFilterObj);
        });
      }
    },
    // This function handles ajax response
    handleSearchResponse(response) {
      if (response.data.productResult) {
        if (this.isBulkEnabled && !this.lazyLoadRequest) {
          this.selectedBulkProducts = [];
        }
        this.searchData = response.data.productResult;
        if (response.data.contentResult) {
          this.contentSearch = response.data.contentResult;
          if (this.contentSearch.content && !this.searchData.products.length) {
            this.defaultContentTab = true;
          }
        }
      } else {
        this.searchData = response.data;
      }
      this.searchData.products.map((item) => {
        this.$set(item, 'isBulk', false);
      });

      // enabling content search tab on basis of following conditions
      if (
        (this.isContentEnabled &&
          !this.isCategoryPage &&
          this.contentSearch.content.length > 0) ||
        this.globals.isVanityfair()
      ) {
        this.showContentTab = true;
      }
      this.$refs.spinner.hideSpinner();
      const self = this;
      this.updateURL(
        this.searchData.currentQuery.url,
        this.searchData.freeTextSearch,
      );
      if (this.isContentEnabled && !this.isCategoryPage && this.contentSearch) {
        this.createContentSortsArray(this.contentSearch.sorts);
      }
      if (
        this.searchData.products.length > 0 ||
        (this.isContentEnabled &&
          !this.isCategoryPage &&
          this.contentSearch &&
          this.contentSearch.content &&
          this.contentSearch.content.length > 0)
      ) {
        self.noResults = false;
        this.createSortsArray(this.searchData.sorts);
        if (!this.isBulkEnabled) {
          this.setCompareProductsChecked();
        }
      } else {
        self.noResults = true;
      }
      if (this.lazyLoadRequest) {
        this.searchData.products.map((item) => {
          self.productList.push(item);
        });
        if (this.contentSearch && this.contentSearch.content) {
          this.contentSearch.content.forEach((item) => {
            self.contentSearchData.push(item);
          });
        }
        self.lazyLoadRequest = false;
        self.noResults = false;
      } else {
        this.productList = this.searchData.products;
        if (this.contentSearch && this.contentSearch.content) {
          this.contentSearchData = this.contentSearch.content;
        }
      }
      // Reading Cookie which has the bulk selected items
      if (this.isBulkEnabled && this.globals.loggedIn) {
        if (this.readCookie(this.cookies.selectedBulkProducts)) {
          const selectedBulkProductsCookie = JSON.parse(
            this.readCookie(this.cookies.selectedBulkProducts),
          );
          const selectedBulkProductsCodes =
            selectedBulkProductsCookie.selectedBulkProducts;
          selectedBulkProductsCodes.map((item) => {
            this.productList.map((product) => {
              if (item === product.code) {
                this.selectedBulkProducts.push(product);
                this.$set(product, 'isBulk', true);
              }
            });
          });
          if (selectedBulkProductsCookie.lazyLoad) {
            this.loadMore();
          }
          this.toggleSelectAll();
        }
      }
      // function to add applied filters
      this.checkSelectedFilters();
      // sending the data to Google Analytics on enter in the search field
      if (typeof dataLayer !== 'undefined') {
        this.analyticsService.trackSearchResults(this.searchData);
      }
      this.dataLoaded = true;
      // generate categories banner data

      if (this.searchData.topImage) {
        this.globals.isCategoryImageAvailable = true;
      } else {
        this.globals.isCategoryImageAvailable = false;
      }

      this.globals.catergoryBannerData.componetType = 'imageWrapper';
      this.globals.catergoryBannerData.componentTheme = 'hero-center';
      this.globals.catergoryBannerData.backgroundImageD = this.searchData.topImage;
      this.globals.catergoryBannerData.headingText = this.searchData.categoryName;
    },
    // set checked attribute on product tile
    setCompareProductsChecked() {
      const self = this;
      const compareProductCodes = self.getCompareProductCodes();
      this.searchData.products.map((item) => {
        if (
          compareProductCodes !== undefined &&
          compareProductCodes.indexOf(item.code) >= 0
        ) {
          self.$set(item, 'checked', true);
        } else {
          self.$set(item, 'checked', false);
        }
      });
    },
    // read cookie
    getCompareProductCodes() {
      let checkCompare = this.readCookie(this.cookies.compareCookie);
      if (checkCompare !== null) {
        checkCompare = JSON.parse(checkCompare);
        const comparedProductCodes = checkCompare.map(item => item.code);
        return comparedProductCodes;
      }
    },
    // This function update current URL as per searched term
    updateURL(url, term) {
      if (url.indexOf('/search') >= 0) {
        url = url.replace('/search', '');
      } else if (this.categoryId) {
        url = `?${url.split('?')[1]}`;
      }
      if (this.searchedTerm === '') {
        this.searchedTerm = `&searchText=${term}`;
      }
      const path = window.location.pathname + url + this.searchedTerm;
      history.replaceState({}, 'Product Search', path);
    },
    // This function will handle ajax errors
    handleSearchError(error) {
      this.$refs.spinner.hideSpinner();
    },
    // This function will return searched category and filter name
    returnFilterObject(categoryName, filterOptValue) {
      const categoryArr = this.searchData.facets.filter(
        item => item.name === categoryName,
      );
      const fiterOptionArr = categoryArr[0].values.filter(
        fOption => fOption.name === filterOptValue,
      );
      return fiterOptionArr[0];
    },
    // This function will be used to handled selection and deselection of facets
    updateFilterList(category, filter) {
      const self = this;
      if (filter.selected) {
        filter.selected = false;
        const fiteredData = this.returnFilterObject(category, filter.name);
        self.appliedFilterList.map((item, idx) => {
          if (fiteredData.name === item.filterValue) {
            self.appliedFilterList.splice(idx, 1);
          }
        });
        self.generateUrlRequest(filter.query.url);
      } else {
        const filterObj = {
          category,
          filterValue: filter.name,
        };
        this.appliedFilterList.push(filterObj);
        this.createSearchQueryString(this.appliedFilterList);
        filter.selected = true;
        this.resetCurrentPageNumber();
        this.eraseCookie(this.cookies.selectedBulkProducts);
        self.generateUrlRequest(filter.query.url);
      }
    },
    resetCurrentPageNumber() {
      this.pageNumber = 0;
    },
    // This function will be used to create and encode search param url
    createSearchQueryString(list) {
      this.searchString = '';
      list.map((item, index) => {
        this.searchString += `${item.category}:${item.filterValue}:`;
      });
      this.searchString = encodeURIComponent(this.searchString);
    },
    // This function handles clear all functionality of facets
    resetFilter() {
      if (this.searchData.breadcrumbs.length > 0) {
        let resetUrl = '/search?query=';
        if (this.categoryId) {
          resetUrl = `/category/${this.categoryId}?query=`;
        }
        let baseQuery = `${this.searchData.freeTextSearch}:${
          this.searchData.pagination.sort
        }`;
        baseQuery = encodeURIComponent(baseQuery);
        resetUrl += baseQuery;
        this.resetCurrentPageNumber();
        this.generateUrlRequest(resetUrl);
      }
    },
    // This function handle removal of facets from applied filter list
    removeFilters(tag) {
      const self = this;
      this.resetCurrentPageNumber();
      this.searchData.breadcrumbs.map((elem) => {
        if (
          elem.facetName === tag.category &&
          elem.facetValueName === tag.filterValue
        ) {
          self.generateUrlRequest(elem.removeQuery.url);
        }
      });
    },
    toggleFilter(event) {
      this.filterHidden = !this.filterHidden;
      setTimeout(() => {
        document.querySelector('.facet-container span.heading-cls').focus();
      }, 500);
      if (this.filterHidden) {
        document.querySelector('body').classList.remove('filter-open');
      } else {
        document.querySelector('body').classList.add('filter-open');
      }
    },
    openShareModal(resource) {
      this.shareResource = {
        altText: resource.title,
        description: resource.description,
        resourceURL: resource.url,
        url: resource.resourceImageUrl,
        mimeType: resource.contentType,
      };
      this.$refs.shareResourceModal.open();
    },
    onShareResourceSuccess() {
      this.shareResource = {};
      this.$refs.shareResourceModal.close();
      this.showFlyout(
        this.flyoutStatus.success,
        this.i18n.shareModal.shareResourceSuccessMsg,
        true,
      );
    },
    onBackToTop() {
      window.scrollTo(0, 0);
    },
    onSelectAll() {
      this.selectAll = !this.selectAll;
      if (this.selectAll) {
        this.productList.map((item) => {
          this.$set(item, 'isBulk', true);
          eventBus.$emit('selectCheckedElement', item);
        });
      } else {
        this.productList.map((item) => {
          this.$set(item, 'isBulk', false);
        });
        this.selectedBulkProducts = [];
      }
    },
    toggleSelectAll() {
      if (
        this.isBulkEnabled &&
        this.globals.isGpXpress() &&
        this.selectedBulkProducts
      ) {
        this.isSampleOrderEnable(this.selectedBulkProducts);
      }
      if (this.selectedBulkProducts.length === this.productList.length) {
        this.selectAll = true;
      } else {
        this.selectAll = false;
      }
    },
    handleSelectList(event) {
      if (this.globals.loggedIn) {
        this.$refs.selectListModal.open(event);
      } else {
        // Storing the bulk selected items in the cookie
        const selectedBulkProductsCode = [];
        this.selectedBulkProducts.map((item) => {
          selectedBulkProductsCode.push(item.code);
        });
        const selectedBulkProductsCookie = {
          selectedBulkProducts: selectedBulkProductsCode,
          lazyLoad: this.bulkLazyLoad,
        };
        this.createCookie(
          this.cookies.selectedBulkProducts,
          JSON.stringify(selectedBulkProductsCookie),
        );
        if (this.globals.siteConfig.isGuestList) {
          setTimeout(() => {
            this.guestListName = this.i18n.guestList;
            this.showFlyout(
              this.flyoutStatus.success,
              `${this.selectedBulkProducts.length} ${this.i18n.addToListSuccess} ${this.i18n.guestList}`,
              true,
            );
          }, 300);
        } else {
          this.globals.navigateToUrl('login');
          globalEventBus.$emit(
            'announce',
            'For adding to list, you need to login',
          );
          setTimeout(() => {
            this.globals.navigateToUrl('login');
          }, 300);
        }
      }
    },
    onSelectListSuccess() {
      this.$refs.selectListModal.close();
      if (this.globals.loggedIn) {
        this.showFlyout(
          this.flyoutStatus.success,
          `${this.selectedBulkProducts.length} ${this.i18n.addToListSuccess} ${this.selectedListName}`,
          true,
        );
      }
    },
    handleAddToCart() {
      this.$refs.spinner.showSpinner();
      const bulkProducts = [];
      this.selectedBulkProducts.map((item) => {
        if (this.globals.isGpXpress() && item.isSample) {
          bulkProducts.push({
            code: item.code,
            minOrderQuantity: item.minOrderQuantity || 1,
          });
        }
        else if(!this.globals.isGpXpress()){
          bulkProducts.push({
            code: item.code,
            minOrderQuantity: item.minOrderQuantity || 1,
          });
        }
      });
      const requestBody = {
        products: bulkProducts,
      };
      const requestConfig = {};
      requestConfig.data = requestBody;
      this.searchBrowseService.addBulkToCart(
        requestConfig,
        this.handleAddBulkToCartResponse,
        this.handleAddBulkToCartError,
      );
    },
    handleAddBulkToCartResponse(response) {
      if (response.data) {
        cartEventBus.$emit('call-basic-cart');
        this.$refs.spinner.hideSpinner();
        const errorProducts = [];
        this.selectedBulkProducts.map((item) => {
          response.data.map((product) => {
            if (product.statusCode === this.addToCartStatus.failure) {
              if (product.errorProductCode === item.code) {
                errorProducts.push(item.code);
              }
              if (errorProducts.length === this.selectedBulkProducts.length) {
                this.showFlyout(
                  this.flyoutStatus.error,
                  this.i18n.bulkAddToCartError,
                  true,
                );
              } else {
                this.showFlyout(
                  this.flyoutStatus.error,
                  this.i18n.addToCartError,
                  true,
                );
              }
            }
            if (errorProducts.length === 0) {
              this.showFlyout(
                this.flyoutStatus.success,
                this.i18n.addToCartSuccess,
                true,
              );
            }
          });
        });
      }
    },
    handleAddBulkToCartError() {
      this.$refs.spinner.hideSpinner();
    },
    handleCompare() {
      if (this.readCookie(this.cookies.compareCookie)) {
        this.eraseCookie(this.cookies.compareCookie);
      }
      this.createCookie(
        this.cookies.compareCookie,
        JSON.stringify(this.selectedBulkProducts),
      );
      const self = this;
      let compareProductsString = '';
      const prodArr = self.selectedBulkProducts.map(item => item.code);
      compareProductsString = self.generateProductsString(prodArr);
      this.globals.navigateToUrlWithParams(
        'compareProducts',
        compareProductsString,
        'productCodes',
      );
    },
    handleShareItem(event) {
      this.$refs.shareItemModal.open(event);
    },
    bulkShare(val) {
      this.form = val;
      const bulkProducts = [];
      this.selectedBulkProducts.map((item) => {
        this.searchData.products.map((product) => {
          if (product.code === item.code) {
            bulkProducts.push({
              url: product.url,
              code: item.code,
            });
          }
        });
      });
      this.form.products = bulkProducts;
      const requestData = this.form;
      const requestConfig = {};
      requestConfig.data = requestData;
      this.searchBrowseService.bulkShareItem(
        requestConfig,
        this.handleBulkShareResponse,
        this.handleBulkShareError,
      );
    },
    handleBulkShareResponse(response) {
      if (response) {
        this.$refs.shareItemModal.close(event);
      }
    },
    handleBulkShareError(error) {
      if (error) {
        this.$refs.shareItemModal.close(event);
      }
    },
    isSelectedProductsComparable(data) {
      this.isCompareDisabled = false;
      if (data.length > 1) {
        if (data.length > this.maxCompareCount) {
          this.isCompareDisabled = true;
        } else {
          const baseAssetCode = data[0].assetCode;
          this.compareCount = 0;
          data.map((item) => {
            if (item.assetCode !== baseAssetCode) {
              this.isCompareDisabled = true;
            } else {
              this.compareCount += 1;
            }
          });
        }
      } else {
        this.isCompareDisabled = true;
        this.compareCount = 1;
      }
    },
    // function to generate query string
    generateProductsString(prodArr) {
      return prodArr.join(':');
    },
    isSampleOrderEnable(data) {
      if (data) {
        const isSampleTrueProducts = [];
        const isApprovedSampleTrueProducts = [];
        data.map((item) => {
          if (item.isSample) {
            isSampleTrueProducts.push(item.code);
          }
          if (item.approvedSampleStatus) {
            isApprovedSampleTrueProducts.push(item.code);
          }
        });
        if (data.length && !isApprovedSampleTrueProducts.length) {
          this.showSampleOrderButton = false;
        } else {
          this.showSampleOrderButton = true;
          if (data.length &&
            isSampleTrueProducts.length &&
            isApprovedSampleTrueProducts.length === data.length
          ) {
            this.isSampleOrderDisabled = false;
          } else {
            this.isSampleOrderDisabled = true;
          }
        }
      }
    },
  },
};
