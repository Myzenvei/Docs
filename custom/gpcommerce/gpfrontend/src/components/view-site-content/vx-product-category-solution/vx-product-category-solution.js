import vxMarketingProduct from '../vx-marketing-product/vx-marketing-product.vue';
import globals from '../../common/globals';
import {
  globalEventBus,
} from '../../../modules/event-bus';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import ViewSiteContentService from '../../common/services/view-site-content-service';

export default {
  name: 'vx-product-category-solution',
  components: {
    'vx-marketing-product': vxMarketingProduct,
    'vx-spinner': vxSpinner,
  },
  props: {
    tabsData: {
      type: Object,
      required: true,
    },
    isBrochureLinkExternal: {
      type: Boolean,
    },
    isLearnMoreLinkExternal: {
      type: Boolean,
    },
    isRequestTrialLinkExternal: {
      type: Boolean,
    },
    componentTheme: {
      type: String,
    },
    componentId: {
      type: String,
    },
  },
  watch: {
    tabsData(newVal, oldVal) {
      // watch it
      if (newVal !== oldVal) {
        this.loadProducts(this.tabsData.tabs[0], 0); // always loads the first tab on page load
      }
    },
  },
  data() {
    return {
      viewSiteContentService: new ViewSiteContentService(),
      globals,
      // dataLoaded: false,
      tabBody: [],
      activeTab: -1,
      tabBodyTitle: '',
    };
  },
  computed: {},
  mounted() {
    this.loadProducts(this.tabsData.tabs[0], 0);
    this.tabBody = this.tabsData.tabs[0].tiles; // always loads the first tab on page load
    this.tabBodyTitle = this.tabsData.tabs[0].componentHeader;
  },
  methods: {
    loadProducts(prdGroup, index) {
      if (this.activeTab != index) {
        this.tabBody = prdGroup.tiles;
        this.tabBodyTitle = prdGroup.componentHeader;
        this.activeTab = index;
        this.isBrochureLinkExternal = this.tabsData.isExternalLink;
        this.isLearnMoreLinkExternal = prdGroup.isExternal;
        this.isRequestTrialLinkExternal = prdGroup.isExternal;
      }
    },
  },
};
