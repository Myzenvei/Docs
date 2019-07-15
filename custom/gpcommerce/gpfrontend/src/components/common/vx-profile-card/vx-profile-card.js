export default {
  name: 'vx-profile-card',
  components: {},
  props: {
    /**
     * Card Type: 'social','add','column'
     * @type {String}
     */
    cardType: String,
    /**
     * Disabling Profile and Column Card
     * @type {Boolean}
     */
    isDisabled: {
      type: Boolean,
      default: false,
    },
    /**
     * Enabling Profile and Column Card
     * @type {Boolean}
     */
    isEnabled: {
      type: Boolean,
      default: false,
    },
    /**
     * Provide default height of 176px
     * @type {Boolean}
     */
    isDefaultHeight: {
      type: Boolean,
      default: false,
    },
    /**
     * Add Custom CSS Classes to Card Container
     * @type {String}
     */
    cardClasses: {
      type: String,
      default: '',
    },
    /**
     * Show/Hide Icons from the Card
     * @type {Boolean}
     */
    hasIcons: {
      type: Boolean,
      default: true,
    },
    /**
     * Show/Hide Footer from the Card
     * @type {Boolean}
     */
    hasFooter: {
      type: Boolean,
      default: true,
    },

    /*
     * Checking that if the address is default
     * @type {Boolean}
     */
    isDefault: {
      type: Boolean,
      default: false,
    },

    /*
     * Checking that if the address is default billing address
     * @type {Boolean}
     */
    isDefaultBilling: {
      type: Boolean,
      default: false,
    },

    /*
     * Checking that if the address is default billing address
     * @type {Boolean}
     */
    isDefaultShipping: {
      type: Boolean,
      default: false,
    },
    /*
     * Checking that if the pallet shipment is default
     * @type {Boolean}
     */
    isPalletShipment: {
      type: Boolean,
      default: false,
    },

    addressId: {
      type: String,
    },
    unitId: {
      type: String,
    },

    /*
    * Showing trash icon even for disabled tiles.
    */
    showTrash: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  computed: {},
  mounted() {},
  methods: {
    enableAddress() {
      this.$emit('enableUserAddress');
    },
    disableAddress() {
      this.$emit('disableUserAddress');
    },
    rejectAddress() {
      this.$emit('rejectUserAddress');
    },
    approveAddress() {
      this.$emit('approveUserAddress');
    },
  },
};
