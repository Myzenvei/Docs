import messages from '../../../locale/messages';
import vxProductResourcesTab from '../vx-product-resources-tab/vx-product-resources-tab.vue';
import vxProductDetailsTab from '../vx-product-details-tab/vx-product-details-tab.vue';
import vxRelatedProductsTab from '../vx-related-products-tab/vx-related-products-tab.vue';
import vxCompareResultPage from '../../search-browse/vx-compare-result-page/vx-compare-result-page.vue';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import { eventBus } from '../../../modules/event-bus';
import globals from '../../common/globals';

export default {
  name: 'vx-tab-container',
  components: {
    vxProductResourcesTab,
    vxProductDetailsTab,
    'vx-related-products-tab': vxRelatedProductsTab,
    vxCompareResultPage,
  },
  props: {
    /**
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
    /**
     * indicates whether the site is configured for favorites
     */
    isFavorites: {
      type: Boolean,
      default: false,
    },
    /**
     * indicates whether the site is configured for Bazaar Voice
     */
    isBazaarVoice: {
      type: String,
      default: '',
    },
    /**
     * indicates whether the related products are enabled for the product
     */
    isRelatedEnabled: {
      type: Boolean,
      default: false,
    },
    /**
     * Copy text coming from properties files for search browse components
     */
    searchBrowseI18n: {
      type: Object,
    },
  },
  mixins: [cookiesMixin],
  data() {
    return {
      bvProductInlineRating: 'BVRRInlineRating-',
      pdpProductsData: null,
      referenceTypes: {
        compare: 'SELECT',
        related: 'SIMILAR',
        viewed: 'UPSELLING',
      },
      messages: messages['en-US'],
      compareCookieName: 'CompareCookie',
      compareProductIds: [],
      relatedProductIds: [],
      upsellProductIds: [],
      isRelatedProducts: false,
      globals,
    };
  },
  computed: {},
  mounted() {
    const self = this;
    eventBus.$on('pdpProductsData', (data) => {
      self.pdpProductsData = data;
      self.pdpProductsData.tabInfo.sort((a, b) => a.sequence - b.sequence);
      self.bvProductInlineRating += data.code;
      self.onPdpData();
      setTimeout(() => {
        if (self.pdpProductsData.defaultTabName) {
          $(`#tabs li a[name="${self.pdpProductsData.defaultTabName}"]`).tab(
            'show',
          );
        }
      }, 500);
    });
    $(document).on(
      'shown.bs.tab',
      '.vx-tab-container a[data-toggle="tab"]',
      (e) => {
        this.toggleAttrs(e, 'true', '0', 'false', '1');
      },
    );
    $(document).on(
      'hidden.bs.tab',
      '.vx-tab-container a[data-toggle="tab"]',
      (e) => {
        this.toggleAttrs(e, 'false', '-1', 'true', '0');
      },
    );
    const keys = new this.KeyCodes();
    $(document).on('keydown', '.nav li a', function (e) {
      const currentTab = $(this).closest('li');
      if (e.which === keys.left) {
        e.preventDefault();
        if (currentTab.prev().length === 0) {
          currentTab
            .nextAll()
            .last()
            .find('a')
            .click();
        } else {
          currentTab
            .prev()
            .find('a')
            .click();
        }
      }
      if (e.which === keys.right) {
        e.preventDefault();
        if (currentTab.next().length === 0) {
          currentTab
            .prevAll()
            .last()
            .find('a')
            .click();
        } else {
          currentTab
            .next()
            .find('a')
            .click();
        }
      }
      if (e.which === keys.home && currentTab.prev().length > 0) {
        e.preventDefault();
        currentTab
          .prevAll()
          .last()
          .find('a')
          .click();
      }
      if (e.which === keys.end && currentTab.next().length > 0) {
        e.preventDefault();
        currentTab
          .nextAll()
          .last()
          .find('a')
          .click();
      }
    });
  },
  methods: {
    /**
     * This function is called on mounted to get the complete product data
     */
    onPdpData() {
      const self = this;
      const pdpData = self.pdpProductsData;

      // check for product resources
      if (pdpData.productReferences) {
        if (this.isRelatedEnabled) {
          this.isRelatedProducts = true;
        }

        /** Compare Product Data  * */
        self.compareProductIds = self.getProductIds(
          pdpData.productReferences,
          self.referenceTypes.compare,
        );

        /** Related Product Data  * */
        self.relatedProductIds = self.getProductIds(
          pdpData.productReferences,
          self.referenceTypes.related,
        );

        /** Upsell Carousel * */
        self.upsellProductIds = self.getProductIds(
          pdpData.productReferences,
          self.referenceTypes.viewed,
        );
        if (self.upsellProductIds.length > 0) {
          eventBus.$emit('upsellData', pdpData.code);
        }
      }
    },
    /**
     * This function toggles Attributes
     * @param  {Object} event current event object
     * @param  {Boolean} ariaSelected ariaSelected boolean value
     * @param  {Number} tabIndex index value
     * @param  {Boolean} ariaHidden  ariaHidden boolean value
     * @param {Number} focus focus value
     */
    toggleAttrs(e, ariaSelected, tabIndex, ariaHidden, focus) {
      const target = e.target;
      const id = $(target).attr('href');
      $(target)
        .attr({
          'aria-selected': ariaSelected,
          tabindex: tabIndex,
        })
        .closest('.vx-product-support')
        .find(id)
        .attr({
          'aria-hidden': ariaHidden,
        });
      if (focus === '1') {
        target.focus();
      }
    },
    /**
     * This function defines value of keycode
     */
    KeyCodes() {
      // Define values for keycodes
      this.left = 37;
      this.right = 39;
      this.end = 35;
      this.home = 36;
    },
    /**
     * This function get Product Ids
     * @param  {Array} referencesArr reference array
     * @param  {String} referenceType   type of reference value
     * @return {Array} retruns array of Product Ids
     */
    getProductIds(referencesArr, referenceType) {
      let filteredRefencesData = [];
      filteredRefencesData = referencesArr.filter(
        item => item.referenceType === referenceType,
      );
      const productIdArr = filteredRefencesData.map(item => item.target.code);
      return productIdArr;
    },
  },
};
