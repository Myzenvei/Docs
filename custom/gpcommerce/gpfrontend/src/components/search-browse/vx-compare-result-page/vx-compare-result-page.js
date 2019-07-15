import vxProductTileVertical from '../vx-product-tile-vertical/vx-product-tile-vertical.vue';
import vxCompareTable from '../vx-compare-table/vx-compare-table.vue';
import {
  eventBus
} from '../../../modules/event-bus';
import globals from '../../common/globals';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import SearchBrowseService from '../../common/services/search-browse-service';
import mobileMixin from '../../common/mixins/mobile-mixin';

/**
 * Component for displaying the selected product tiles and details in tabular form for comparison
 */

export default {
  name: 'vx-compare-result-page',
  components: {
    vxProductTileVertical,
    vxCompareTable,
  },
  props: {
    i18n: {
      type: Object,
    },
    showRemoveOption: {
      type: Boolean,
    },
    isFavorites: {
      type: Boolean,
    },
    isBazaarVoice: {
      type: String,
    },
    pageType: {
      type: String,
    },
    showTitle: {
      type: Boolean,
    },
    pdpCompareIds: {
      type: Array,
    },
  },
  mixins: [cookiesMixin, mobileMixin],
  data() {
    return {
      globals,
      productsCompareData: {},
      showRemoveButton: true,
      comparedProducts: null,
      searchBrowseService: new SearchBrowseService(),
    };
  },
  mounted() {
    this.onInitCheck();
  },
  methods: {
    // init function which will read product codes from cookie and hit webservice
    onInitCheck() {
      const self = this;
      let queryString = '';
      // condition to check if component is called from PDP page or search result page.
      if (this.pdpCompareIds && this.pdpCompareIds.length > 0) {
        queryString = self.generateQuery(this.pdpCompareIds);
      } else {
        self.comparedProducts = this.readCookie('CompareCookie');
        if (
          self.comparedProducts &&
          self.comparedProducts !== undefined &&
          self.comparedProducts !== null
        ) {
          self.comparedProducts = JSON.parse(self.comparedProducts);
          self.comparedProducts = self.comparedProducts.map(item => item.code);
          queryString = self.generateQuery(self.comparedProducts);
        } else {
          queryString = window.location.search;
        }
      }
      self.generateUrlRequest(queryString);
    },
    // function to handle ajax response
    handleCompareResponse(response) {
      if (this.isMobile()) {
        this.productsCompareData = response.data;
        this.productsCompareData.products = this.productsCompareData.products.slice(0, 2);
      } else {
        this.productsCompareData = response.data;
      }
    },
    // function to handle ajax error
    handleCompareError(error) {},
    // function to generate compare result url
    generateUrlRequest(query) {
      const requestConfig = {};
      const self = this;
      self.searchBrowseService.getCompareResults(
        requestConfig,
        self.handleCompareResponse,
        self.handleCompareError,
        query,
        self.pageType,
      );
    },
    generateQuery(productCode) {
      const initialText = '?productCodes=';
      const prodList = initialText + productCode.join(':');
      return prodList;
    },
  },
};
