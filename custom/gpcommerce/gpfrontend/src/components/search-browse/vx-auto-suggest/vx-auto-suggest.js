import _ from 'lodash';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxProductTileVertical from '../vx-product-tile-vertical/vx-product-tile-vertical.vue';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxDropdownPrimary from '../../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import globals from '../../common/globals';
import SearchBrowseService from '../../common/services/search-browse-service';
import detectDeviceMixin from '../../common/mixins/detect-device-mixin';

/**
 * Auto suggest component for loading matching products on search
 */

export default {
  name: 'vx-auto-suggest',
  mixins: [cookiesMixin, detectDeviceMixin, mobileMixin],
  components: {
    vxProductTileVertical,
    vxModal,
    vxDropdownPrimary,
  },
  props: ['productCatalog', 'rootCategory', 'isFavorites', 'toggleSearchIcon', 'isBazaarVoice', 'searchBrowseI18n', 'singleProductEnabled'],
  computed: {
    productList() {
      return this.autosuggestPanel;
    },
  },
  data() {
    return {
      i18n: this.searchBrowseI18n.autoSuggest,
      categories: {},
      myValue: '',
      showAutosuggestPanel: false,
      autosuggestPanel: [],
      searchTerm: '',
      globals,
      isEnterPressed: false,
      isB2B: false,
      clearTextIcon: false,
      categoryValues: [],
      currentCategoryValue: '',
      currentCategoryName: '',
      categoryUrl: '',
      categoryName: '',
      searchIconDisabled: false,
      lastSelectedcategory: '',
      isPdpRedirect: false,
      delayInterval: 500,
      searchBrowseService: new SearchBrowseService(),
    };
  },
  created() {
    const self = this;
    window.addEventListener('click', () => {
      this.showAutosuggestPanel = false;
    });
    self.autosuggest = _.debounce((inp) => {
      if (self.toggleSearchIcon) {
        self.disableSearchIcon();
      }
      self.clearTextIcon = true;
      const e = inp || event;
      if (self.searchTerm.length <= 2) {
        self.onRemoveProduct();
        return false;
      }
      self.autosuggestPanel = [];
      self.showAutosuggestPanel = true;
      let encodedSearchTerm = encodeURIComponent(self.searchTerm);
      //commented for R1 release
      // if (self.globals.isB2B()) {
      //   if (self.categoryName) {
      //     encodedSearchTerm += self.categoryName;
      //   }
      // }
      if (e.which === 13) {
        self.isEnterPressed = true;
        self.isPdpRedirect = true;
      }
      const requestConfig = {};
      if (this.globals.loggedIn) {
        const userId = this.globals.uid;
        requestConfig.params = {
          term: encodedSearchTerm,
          maxProducts: 4,
          fields: 'FULL',
          userId,
        };
      } else {
        requestConfig.params = {
          term: encodedSearchTerm,
          maxProducts: 4,
          fields: 'FULL',
        };
      }
      this.searchBrowseService.getAutosuggest(
        requestConfig,
        this.handleSuggestResponse,
        this.handleSuggestError,
      );
    }, self.delayInterval);
  },
  mounted() {
    this.onInit();
    const vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  },
  methods: {
    onInit() {
      const self = this;
      // Commenting below code as currently we are not supporting to show search categories option

      /* if (self.globals.isB2B()) {
        const requestConfig = {};
        this.searchBrowseService.getCategories(
          requestConfig,
          this.handleCategoryResponse,
          this.handleCategoryError,
          this.productCatalog,
          this.rootCategory,
        );
        self.isB2B = true;
      } */
    },
    viewAllClicked(event) {
      this.isEnterPressed = true;
      const requestConfig = {};
      const encodedSearchTerm = encodeURIComponent(self.searchTerm);
      if (this.globals.loggedIn) {
        const userId = this.globals.uid;
        requestConfig.params = {
          term: encodedSearchTerm,
          maxProducts: 4,
          fields: 'FULL',
          userId,
        };
      } else {
        requestConfig.params = {
          term: encodedSearchTerm,
          maxProducts: 4,
          fields: 'FULL',
        };
      }
      this.searchBrowseService.getAutosuggest(
        requestConfig,
        this.handleSuggestResponse,
        this.handleSuggestError,
      );
    },
    createCategoriesArray() {
      const self = this;
      self.lastCategory = this.readCookie('categoryVal');
      this.categoryValues = [{
        label: 'All',
        value: '',
        url: '',
      }, ];
      this.categories.subcategories.map((item) => {
        this.categoryValues.push({
          label: item.name,
          value: item.id,
          url: item.url,
        });
      });
      this.setCategoryDropdown(self.lastCategory);
    },
    setCategoryDropdown(lastCategory) {
      let lastSelectedcategory = {};
      if (!lastCategory) {
        this.currentCategoryName = this.categoryValues[0].label;
      } else {
        lastSelectedcategory = this.categoryValues.filter(
          category => category.value === lastCategory,
        )[0];
        if (!lastSelectedcategory) {
          this.currentCategoryName = this.categoryValues[0].label;
        } else {
          this.getSelectedDropdown(lastSelectedcategory);
          this.eraseCookie('categoryVal');
        }
      }
      if (this.globals.isB2B()) {
        this.$refs.category.setDropdownLabel(this.currentCategoryName);
      }
    },
    // placeholder for autosuggest fuction which will trigger autosuggest defined in created()
    autosuggest() {},
    onRemoveProduct() {
      this.autosuggestPanel.splice(0, this.autosuggestPanel.length);
      if (this.searchTerm.length === 0) {
        this.clearTextIcon = false;
        if (this.toggleSearchIcon) {
          this.disableSearchIcon();
        }
      }
    },
    handleSuggestResponse(response) {
      const self = this;
      let url = '';
      if (this.isEnterPressed) {
        // enter key
        // Storing the value for category dropdown in B2B
        self.createCookie('categoryVal', this.currentCategoryValue);
        self.eraseCookie('ProductCompare');
        if (response.data.pdpRedirectURL && self.isPdpRedirect) {
          // Navigate to PDP
          url += `${this.globals.getNavBaseUrl()}/`;
          url += `${this.globals.locale + response.data.pdpRedirectURL}?site=${
            this.globals.siteId
          }`;
          window.location = url;
        } else {
          const encodedSearchTerm = encodeURIComponent(this.searchTerm);
          // commented for R1 release
          // if (this.globals.isB2B()) {
          //   const queryParam = encodedSearchTerm + this.categoryName;
          //   this.globals.navigateToUrlWithParams('searchResults', queryParam, 'query');
          // } else {
          this.globals.navigateToUrlWithParams(
            'searchResults',
            encodedSearchTerm,
            'query',
          );
          // }
        }
      } else {
        this.autosuggestPanel = response.data.products;
      }
    },
    /* handleCategoryResponse(response) {
      this.categories = response.data;
      this.createCategoriesArray();
    },
    handleCategoryError(error) {}, */
    // handle B2B category dropdown
    getSelectedDropdown(categoryVal) {
      const self = this;
      this.currentCategoryValue = categoryVal.value;
      this.currentCategoryName = categoryVal.label;
      if (!this.currentCategoryValue) {
        this.categoryName = '';
      } else {
        this.categoryName = encodeURIComponent(
          `:relevance:allCategories:${this.currentCategoryValue}:`,
        );
      }
    },
    clearSearch() {
      this.showAutosuggestPanel = false;
      this.searchTerm = '';
      this.clearTextIcon = false;
      if (this.toggleSearchIcon) {
        this.disableSearchIcon();
      }
    },
    disableSearchIcon() {
      if (this.searchTerm.length != 0) {
        this.searchIconDisabled = true;
      } else {
        this.searchIconDisabled = false;
      }
    },
    handleSuggestError(error) {},
  },
};
