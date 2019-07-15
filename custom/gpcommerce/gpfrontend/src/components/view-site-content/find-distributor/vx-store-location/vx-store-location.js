import vxMapLocator from '../../../common/vx-map-locator/vx-map-locator.vue';
import globals from '../../../common/globals';

export default {
  name: 'vx-store-location',
  components: {
    vxMapLocator,
  },
  props: {
    selectedStore: {
      type: Object,
      required: true,
    },
    location: '',
  },
  provide() {
    return {
      getMap: this.$refs.storemap,
    };
  },
  data() {
    return {
      isMapCreated: false,
      mapLocatorObj: '',
      globals,
    };
  },
  computed: {
    selectedStoreAvailable() {
      return this.selectedStore && this.location;
    },
  },
  mounted() {

  },
  methods: {
    mapLoaded() {
      this.$refs.storemap.testGeoLocationMarker(this.location);
    },

    desktopMapNavigation() {
      window.open(
        `https://maps.google.com/?daddr=${
        this.location.lat
        },${this.location.lng}&zoom=14&amp;ll=`,
      );
    },
  },
  watch: {
    location() {
      if (this.$refs.storemap) {
        this.$refs.storemap.testGeoLocationMarker(this.location);
      }
    },
  },
};
