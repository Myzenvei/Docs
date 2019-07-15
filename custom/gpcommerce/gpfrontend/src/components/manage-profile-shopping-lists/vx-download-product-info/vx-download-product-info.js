import globals from '../../common/globals';
import ManageProfileShoppingListService from '../../common/services/manage-profile-shopping-lists-service';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-download-product-info',
  components: {
    vxRadioButtonGroup,
    vxSpinner,
  },
  props: {
    i18n: {
      type: Object,
    },
    noProducts: {
      type: Boolean,
    },
  },
  data() {
    return {
      globals,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      downloadOptions: [{
          label: `${this.i18n.downloadProductInfo.downloadProductInfoOptionAll}`,
          name: `${this.i18n.downloadProductInfo.downloadProductInfoOptionAll}`,
          value: `${this.i18n.downloadProductInfo.downloadProductInfoAllproductsValue}`,
          selected: true,
          disabled: false,
        },
        {
          label: `${this.i18n.downloadProductInfo.downloadProductInfoOptionCurrent}`,
          name: `${this.i18n.downloadProductInfo.downloadProductInfoOptionCurrent}`,
          value: `${this.i18n.downloadProductInfo.downloadProductInfoCurrentValue}`,
          selected: false,
          disabled: true,
        },
      ],
      selectedDownloadOpt: `${this.i18n.downloadProductInfo.downloadProductInfoAllproductsValue}`,
    };
  },
  mounted() {
    if (!this.globals.loggedIn){
      this.downloadOptions[0].disabled = true;
    }
    if (this.noProducts) {
      this.$refs.downloadOptions.setSelectedByValue(this.downloadOptions[0].value);
      this.selectedDownloadOpt = `${this.i18n.downloadProductInfo.downloadProductInfoAllproductsValue}`;
    } else {
      this.$refs.downloadOptions.setSelectedByValue(this.downloadOptions[1].value);
      this.downloadOptions[1].disabled = false;
      this.selectedDownloadOpt = `${this.i18n.downloadProductInfo.downloadProductInfoCurrentValue}`;
    }
  },
  methods: {
    downloadExcel() {
      this.$emit('onExcelDownloadInit', this.selectedDownloadOpt);
    },
    selectedDownloadOption(data){
      this.selectedDownloadOpt = data;
    }
  },
};
