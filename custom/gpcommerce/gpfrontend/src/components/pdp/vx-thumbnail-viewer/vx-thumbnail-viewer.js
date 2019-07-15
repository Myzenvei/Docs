import {
  eventBus,
  globalEventBus,
} from '../../../modules/event-bus';
import vxModal from '../../common/vx-modal/vx-modal.vue';
import vxBannerCarousel from '../../view-site-content/vx-banner-carousel/vx-banner-carousel.vue';
import vxProductIcons from '../../common/vx-product-icons/vx-product-icons.vue';
import {
  PdpCarouselConfig,
  product3DHeightBreakPoints,
} from '../../common/mixins/vx-enums';
import globals from '../../common/globals';
import mobileMixin from '../../common/mixins/mobile-mixin';

export default {
  name: 'vx-thumbnail-viewer',
  components: {
    vxModal,
    vxBannerCarousel,
    vxProductIcons,
  },
  props: {
    /**
     * Copy text coming from properties files
     */
    i18n: {
      type: Object,
    },
  },
  mixins: [mobileMixin],
  data() {
    return {
      globals,
      currentCarouselData: [],
      totalCarouselData: [],
      thumbnailObj: {},
      isSlidingToPrevious: false,
      totalCount: 0,
      currentTopIndex: 0,
      currentBottomIndex: 0,
      itemsToDisplay: 5,
      totalImagesCount: 10,
      totalVideoCount: 10,
      selectedItem: '',
      modalHeading: 'PDP Image',
      mobileCarouselObj: {},
      PdpCarouselConfig,
      isResponseHandled: false,
      iFrameHeight: Number,
      productIconsData: [],
    };
  },
  created() {
    this.setCarouselName();
  },
  mounted() {
    // At first show only 5 items
    const self = this;
    if (this.isMobile()) {
      this.iFrameHeight = product3DHeightBreakPoints.mobile;
    } else if (this.isTablet()) {
      this.iFrameHeight = product3DHeightBreakPoints.tablet;
    } else {
      this.iFrameHeight = product3DHeightBreakPoints.desktop;
    }
    eventBus.$on('totalCarouselData', (data) => {
      self.thumbnailObj = data;
      self.isResponseHandled = true;
      if (data && data.images && data.images.length > 0) {
        const totalCarouselData = data.images;

        let imagesArr = totalCarouselData.filter(item => item.mimeType.indexOf('image') >= 0);
        imagesArr = !globals.siteConfig.pdpimageOrVideoConstraintEnabled ? imagesArr :
          imagesArr.slice(0, self.totalImagesCount);

        let videosArr = totalCarouselData.filter(item => item.mimeType.indexOf('video') >= 0);
        videosArr = !globals.siteConfig.pdpimageOrVideoConstraintEnabled ? videosArr :
          videosArr.slice(0, self.totalVideoCount);

        self.totalCarouselData = [...imagesArr, ...videosArr];
        self.mobileCarouselObj.loopCarousel = PdpCarouselConfig.CYCLE;
        self.mobileCarouselObj.autoStart = PdpCarouselConfig.AUTO_START;
        self.mobileCarouselObj.gpRotatingData = JSON.parse(JSON.stringify(self.totalCarouselData));
        self.currentCarouselData = self.totalCarouselData.slice(
          self.currentTopIndex,
          self.itemsToDisplay,
        );
        self.selectedItem = self.currentCarouselData[0];
        // Get Total Count
        self.totalCount = self.totalCarouselData.length;
        // Update current bottom index
        self.currentBottomIndex = self.itemsToDisplay;
        eventBus.$emit('carouselBinded');
      }
      if (data && data.productIcons && data.productIcons.length) {
        this.productIconsData = data.productIcons;
      }
    });
  },
  methods: {
    /**
     * This function moves the focus to top on click of the top arrow
     */
    moveTop() {
      this.isSlidingToPrevious = true;
      this.currentTopIndex += 1;
      this.currentBottomIndex -= 1;
      this.addToTopComputedArr(this.currentBottomIndex);
    },
    /**
     * This function moves the focus to bottom on click of the bottom arrow
     */
    moveBottom() {
      this.isSlidingToPrevious = false;
      this.currentTopIndex -= 1;
      this.currentBottomIndex += 1;
      this.addToBottomComputedArr(this.currentBottomIndex);
    },
    /**
     * This function updates the bottom array
     */
    addToBottomComputedArr(index) {
      // Splice the first item
      this.currentCarouselData.splice(0, 1);
      // Add the next item to the array
      this.currentCarouselData.push(this.totalCarouselData[index - 1]);
    },
    /**
     * This function updates the top array
     */
    addToTopComputedArr(index) {
      // Splice the last item
      this.currentCarouselData.splice(this.currentCarouselData.length - 1, 1);
      // Add item to the beginning of the array
      this.currentCarouselData.unshift(this.totalCarouselData[index - this.itemsToDisplay]);
    },
    /**
     * This function updates the main Product image when any of the images are clicked on
     */
    updatePicture(item) {
      this.selectedItem = item;
      globalEventBus.$emit('announce', `Selected Image ${item.altText}`);
    },
    /**
     * This function opens the modal when the Product image is clicked
     */
    expandPDPImage(event) {
      this.$refs.pdpImageModal.open(event);
    },
    /**
     * This function is used to set Carousel Name
     */
    setCarouselName() {
      this.mobileCarouselObj.componentId = PdpCarouselConfig.CAROUSEL_NAME;
    },
  },
};
