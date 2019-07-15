import globals from '../common/globals';
import vxDropdownPrimary from '../common/vx-dropdown-primary/vx-dropdown-primary.vue';
import SearchBrowseService from '../common/services/search-browse-service';
import flyoutBannerMixin from '../common/vx-flyout-banner/vx-flyout-banner-mixin';
import vxProductTile from '../common/vx-product-tile/vx-product-tile.vue';
import vxModal from '../common/vx-modal/vx-modal.vue';
import vxPdfGenerator from '../common/vx-pdf-generator/vx-pdf-generator.vue';
import vxDownloadInfo from '../common/vx-download-info/vx-download-info.vue';
import vxSaveCart from '../manage-shopping-cart/vx-save-cart/vx-save-cart.vue';
import { globalEventBus } from '../../modules/event-bus';
import { flyoutStatus } from '../common/mixins/vx-enums';
import PdpService from '../common/services/pdp-service';
import AnalyticsService from '../common/services/analytics-service';

export default {
  name: 'vx-cross-search',
  mixins: [flyoutBannerMixin],
  components: {
    vxDropdownPrimary,
    vxProductTile,
    vxModal,
    vxSaveCart,
    vxPdfGenerator,
    vxDownloadInfo,
  },
  props: {
    crossReferenceCategories: {
      type: Object,
      default: {},
    },
    i18n: Object,
    pdfi18n: Object,
    colorCodes: Object,
  },
  data() {
    return {
      globals,
      searchBrowseService: new SearchBrowseService(),
      formattedCategories: this.createFormattedArray(
        this.crossReferenceCategories,
      ),
      formattedSubCategories: this.createFormattedArray(
        this.crossReferenceCategories[0].subcategories,
      ),
      selectedCategory: this.createFormattedArray(
        this.crossReferenceCategories,
      )[0],
      selectedSubCategory: this.createFormattedArray(
        this.crossReferenceCategories[0].subcategories,
      )[0],
      searchTerm: '',
      searchResponse: [],
      getprodId: '',
      prodId: '',
      pdpService: new PdpService(),
      loadPdfGenerator: false,
      analyticsService: new AnalyticsService(),
      guestListName: '',
    };
  },
  watch: {
    searchResponse(newVal) {
      return newVal;
    },
  },
  mounted() {
    this.makeServiceCall();
    this.onEnter();
  },
  methods: {
    onInit(productId) {
      const self = this;
      const requestConfig = {};
      requestConfig.data = {
        listName: this.listName,
      };
      self.pdpService.getProductData(
        {},
        this.handleSuccessResponse,
        this.handleErrorResponse,
        productId,
      );
    },
    handleSuccessResponse(response) {
      // sending the data to Google Analytics on PDP page load
      if (typeof dataLayer !== 'undefined') {
        this.analyticsService.trackImpressions(response.data);
        this.analyticsService.trackDetailImpressions(response.data);
      }
      this.pdpProductInfo = response.data;
      this.loadPdfGenerator = true;
    },
    handleErrorResponse() {},
    makeServiceCall() {
      const requestConfig = {};
      const params = {
        selectedCategory: this.selectedCategory.value,
        selectedSubCategory: this.selectedSubCategory.value,
      };
      this.searchBrowseService.getCrossReferenceSearch(
        requestConfig,
        this.handleCrossSearchResponse,
        this.handleCrossSearchError,
        params,
      );
    },
    createFormattedArray(crossReferenceCategories) {
      const formattedValue = [];
      if (crossReferenceCategories && crossReferenceCategories.length !== 0) {
        crossReferenceCategories.map((item) => {
          if (item && item.name) {
            formattedValue.push({
              label: item.name,
              value: item.id,
              subcategories: item.subcategories,
            });
          }
        });
      }
      return formattedValue;
    },
    handleTextSearch() {
      const searchParam = {
        searchTerm: this.searchTerm,
      };
      this.searchBrowseService.crossReferenceSearchByText(
        {},
        this.handleCrossSearchResponse,
        this.handleCrossSearchError,
        searchParam,
      );
      this.selectedCategory = {};
      this.selectedSubCategory = {};
      this.formattedSubCategories = this.createFormattedArray(
        this.crossReferenceCategories[0].subcategories,
      );
      this.$refs.categories.setDropdownLabel(this.i18n.selectOptionLabel);
      this.$refs.subcategories.setDropdownLabel(this.i18n.selectOptionLabel);
    },
    handleDefaultCategoryalue() {
      this.$refs.categories.setDropdownLabel(this.selectedCategory.label);
    },
    handleDefaultSubCategoryalue() {
      this.$refs.subcategories.setDropdownLabel(this.selectedSubCategory.label);
    },
    updateSelectedCategory(selectedCategory) {
      if (this.selectedCategory.value !== selectedCategory.value) {
        this.selectedCategory = selectedCategory;
        this.formattedCategories.map((item) => {
          if (selectedCategory.value === item.value) {
            this.formattedSubCategories = this.createFormattedArray(
              item.subcategories,
            );
          }
        });

        if (!this.formattedSubCategories.length) {
          this.selectedSubCategory = {};
          this.$refs.subcategories.setDropdownLabel('');
        } else {
          this.selectedSubCategory = this.formattedSubCategories[0];
          this.$refs.subcategories.setDropdownLabel(
            this.selectedSubCategory.label,
          );
        }
        this.makeServiceCall();
      }
      this.searchTerm = '';
    },
    updateSelectedSubCategory(selectedSubCategory) {
      this.selectedSubCategory = selectedSubCategory;
      this.searchTerm = '';
      this.makeServiceCall();
    },
    handleCrossSearchResponse(response) {
      this.searchResponse = response.data;
      if (!response.data.products.length) {
        this.showErrorMessage();
      }
    },
    handleCrossSearchError(error) {
      this.showErrorMessage();
    },
    showErrorMessage() {
      this.showFlyout(flyoutStatus.error, this.i18n.searchErrorMessage, true);
    },
    onSelectListSuccess() {
      this.$refs.selectListModal.close();
      this.showFlyout(
        flyoutStatus.success,
        this.pdfi18n.selectListResponse,
        true,
      );
    },
    handleSelectList(event, code) {
      this.getprodId = code;
      if (this.globals.loggedIn) {
        this.$refs.selectListModal.open(event);
        this.guestListName = '';
      } else if (this.globals.isB2C()) {
        globalEventBus.$emit(
          'announce',
          'For adding to list, you need to login',
        );
        setTimeout(() => {
          this.globals.navigateToUrl('login');
        }, 300);
      } else {
        this.guestListName = this.pdfi18n.guestList;
      }
    },
    handleDownloadInfo(event, productId) {
      this.prodId = productId;
      this.onInit(productId);
      this.$refs.downloadInfoModal.open(event);
    },
    createPDF(pdfInfo) {
      this.$refs.pdfModule.createPdfPDPFormat(pdfInfo);
    },
    limitCharacters(data) {
      let newData = data.length > 500 ? `${data.substring(0, 500)}...` : data;
      newData = this.replaceText(newData);
      return newData;
    },
    replaceText(text) {
      const mapObj = {
        '™': '<sup>(TM)</sup>',
        '’': "'",
        '–': '-',
        '<br>': '',
        '”': '"',
      };
      const newText = text.replace(
        /™|’|–|<br>|”/gi,
        matched => mapObj[matched],
      );
      return newText;
    },
    onImageDownloadInit(imageInfo) {
      const requestConfig = {};
      requestConfig.params = {
        productCode: this.prodId,
        imageformat: imageInfo.format,
        resolution: imageInfo.size,
        allimages: imageInfo.allImages,
      };
      this.pdpService.getImagesInZipFormat(
        requestConfig,
        this.handleimageDownloadResponse,
        this.handleImageDownloadError,
        this.prodId,
      );
    },
    handleimageDownloadResponse(response) {
      const blob = new Blob([response.data], {
        type: 'application/zip',
      });
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(
          blob,
          this.pdfi18n.defaultDownloadedFilename,
        );
        this.$refs.downloadInfoModal.close();
      }
      link.download = this.pdfi18n.defaultDownloadedFilename;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      this.$refs.downloadInfoModal.close();
    },
    handleImageDownloadError(error) {
      if (error) {
        this.$refs.downloadInfoModal.close();
        this.showFlyout('error', this.pdfi18n.imageDownloadError, true);
      }
    },
    onEnter() {
      const input = document.getElementById('searchSku');
      // Execute a function when the user releases a key on the keyboard
      input.addEventListener('keyup', (event) => {
        if (event.keyCode === 13) {
          event.preventDefault();
          this.handleTextSearch();
        }
      });
    },
  },
};
