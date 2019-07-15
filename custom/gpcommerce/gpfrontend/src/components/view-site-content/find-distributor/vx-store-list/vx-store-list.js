import storeLocatorMixin from '../../../common/mixins/store-locator-mixin';
import detectDeviceMixin from '../../../common/mixins/detect-device-mixin';
import mobileMixin from '../../../common/mixins/mobile-mixin';

export default {
  name: 'vx-store-list',
  components: {},
  props: {
    i18n: {
      type: Object,
      required: true,
    },
    storesList: {
      type: Array,
      required: true,
    },
    noOfDistributor: {
      type: Number,
      required: true,
    }
  },
  mixins: [storeLocatorMixin, detectDeviceMixin, mobileMixin],
  data() {
    return {
      isListMounted: '',
    };
  },
  computed: {
    stores() {
      return this.storesList.length > 0;
    },
  },
  mounted() {
    this.isListMounted = true;
  },
  updated() {
    if (this.isListMounted && this.storesList.length) {
      (this.$refs.store0)[0].classList.add('active');
    }
  },
  methods: {},
};
