import globals from '../../common/globals';

export default {
  name: 'vx-product-icons',
  components: {},
  props: {
    productIcons: {
      type: Array,
      required: true,
    },
    i18n: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      globals,
      productIconsData: [],
      productIconsDetails: [{
        certificate: {
          className: 'icon-certification',
          title: this.i18n.certification,
        },
      },
      {
        freeShipping: {
          className: 'icon-shipping',
          title: this.i18n.freeShipping,
        },
      },
      {
        online_only: {
          className: 'icon-online',
          title: this.i18n.onlineOnly,
        },
      },
      {
        subscribable: {
          className: 'icon-subscription-new',
          title: this.i18n.subscribable,
        },
      },
      {
        seasonal: {
          className: 'icon-seasonal-new',
          title: this.i18n.seasonal,
        },
      },
      {
        bundle_available: {
          className: 'icon-bundle',
          title: this.i18n.bundleAvailable,
        },
      },
      {
        customizable: {
          className: 'icon-customisation',
          title: this.i18n.customisation,
        },
      },
      {
        installable: {
          className: 'icon-installation',
          title: this.i18n.installation,
        },
      },
      {
        sampleEligible: {
          className: 'icon-sample',
          title: this.i18n.sample,
        },
      },
      ],
    };
  },
  computed: {},
  mounted() {
    this.getProductIconsData();
  },
  methods: {
    getProductIconsData() {
      if (this.productIcons.length) {
        this.productIconsData = this.productIconsDetails
          .filter(item => this.productIcons.indexOf(Object.keys(item)[0]) > -1)
          .map(item => item[Object.keys(item)[0]]);
      }
    }
  },
  watch: {
    productIcons() {
      this.getProductIconsData();
    },
  },
};
