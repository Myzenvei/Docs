import _ from 'lodash';
import { Validator } from 'vee-validate';
import globals from '../../common/globals';
import mobileMixin from '../../common/mixins/mobile-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import vxRadioButtonGroup from '../../common/vx-radio-button-group/vx-radio-button-group.vue';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';
import { cartEventBus, globalEventBus } from '../../../modules/event-bus';

import detectDeviceMixin from '../../common/mixins/detect-device-mixin';
import { cookies } from '../../common/mixins/vx-enums';
import cookiesMixin from '../../common/mixins/cookies-mixin';

export default {
  name: 'vx-save-cart',
  components: {
    vxSpinner,
    vxRadioButtonGroup,
  },
  mixins: [mobileMixin, detectDeviceMixin, cookiesMixin],
  props: {
    /**
     * text coming from property file
     */
    i18n: Object,
    /**
     * contains cart guid
     */
    cartGuid: String,
    /**
     * wishlist array
     */
    wishlistEntries: [],
    /**
     * indicates that entryType should be product
     */
    isProduct: Boolean,
    /**
     * contains product quantity
     */
    prodQuantity: Number,
    /**
     * indicates that the list is of precurated type
     */
    isPrecurated: Boolean,
    /**
     * indicates that the Bulk functionality is enabled
     */
    isBulkEnabled: {
      type: Boolean,
      default: false,
    },
    /**
     * array of selected bulk products
     */
    selectedBulkProducts: {
      type: Array,
      default: [],
    },

    /* Getting name for guest list */
    guestListName: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      manageShoppingCartService: new ManageShoppingCartService(),
      createNewListName: '',
      globals,
      selectedList: {},
      selectedUid: '',
      wishLists: [],
      cartEmpty: false,
      inlineError: false,
      existingListError: false,
      existingList: false,
      emptyListError: false,
      queryInput: [],
      precuratedListData: [],
      cookies,
      selectedListName: '',
      // listOptions: {
      //   label: 'list options',
      //   code: '10000',
      //   options: [
      //     {
      //       description: 'Customer Wishlist',
      //       name: 'test',
      //       wishlistEntries: [],
      //       wishlistUid: 'e775403c-414d-4d84-a7ca-8a5fb5fb89d4',
      //     },
      //     {
      //       description: 'Customer Wishlist',
      //       name: 'test1',
      //       wishlistEntries: [],
      //       wishlistUid: '77f8e2ba-ff0f-4b4c-86ea-99d81904e117',
      //     },
      //   ],
      // },
    };
  },
  computed: {},
  watch: {
    createNewListName() {
      if (this.existingListError) {
        this.existingListError = false;
      }
    },
  },
  mounted() {
    // this.wishLists = this.renameArrayObjectKeys(
    //   this.listOptions.options,
    //   this.wishLists,
    // );
    if (this.guestListName) {
      this.createNewListName = `${this.guestListName}_${new Date().getTime()}`;
      const cookieData = this.readCookie(cookies.guestListUid);
      if (cookieData) {
        this.saveAList(cookieData);
      } else {
        this.createListService();
      }
    }

    const veeCustomErrorMessage = {
      en: {
        custom: {
          createNewList: {
            required: this.i18n.emptyListError,
          },
        },
      },
    };
    Validator.localize(veeCustomErrorMessage);
    cartEventBus.$on('precurated-list-products', (data) => {
      this.precuratedListData = data;
    });
    this.$refs.spinner.showSpinner();
    this.manageShoppingCartService.getAllWishLists(
      {},
      this.handleGetWishlistResponse,
      this.handleGetWishlistError,
    );
    if (this.wishlistEntries) {
      this.wishlistEntries.map((product) => {
        this.queryInput.push(this.filterList(product, 'code', 'quantity'));
      });
    }
    this.heightAdjustmentInMobile();
    window.addEventListener('resize', this.heightAdjustmentInMobile);
  },
  methods: {
    /**
     * This function shows list error message on blur
     */
    showListError(event) {
      this.$validator.validateAll().then((result) => {
        if (!result) {
          globalEventBus.$emit('announce', this.i18n.emptyListError);
        }
      });
    },
    /**
     * This function creates a list of cart entries
     */
    createAList() {
      this.$validator.validateAll().then((result) => {
        if (this.isPrecurated) {
          this.$emit('precurated-list');
        }
        if (!this.existingList) {
          if (this.createNewListName === '') {
            this.emptyListError = true;
          } else {
            this.createListService();
          }
        } else if (this.createNewListName !== '' && this.existingList) {
          this.inlineError = true;
        } else {
          this.saveAList(this.selectedUid);
        }
      });
    },

    /* Function which handles create list Ajax call */
    createListService() {
      globalEventBus.$emit('wishlist-name', this.createNewListName);
      const requestData = {
        name: this.createNewListName.trim(),
      };
      const requestConfig = {};
      requestConfig.data = requestData;
      this.manageShoppingCartService.createList(
        requestConfig,
        this.handleCreateListResponse,
        this.handleCreateListError,
      );
    },

    /**
     * This function selects a list from wishlist
     * @param  {Array} selectedOption selected wishlist
     */
    selectAList(selectedOption) {
      this.createNewListName = '';
      this.existingList = true;
      this.selectedUid = selectedOption.wishlistUid;
      this.selectedListName = selectedOption.label;
      globalEventBus.$emit('wishlist-name', this.selectedListName);
    },
    /**
     * This function handles response of getting all wishlist
     */
    handleGetWishlistResponse(response) {
      this.$refs.spinner.hideSpinner();
      if (response.data) {
        this.wishLists = this.renameArrayObjectKeys(
          response.data.wishlists,
          this.wishLists,
        );
      }
    },
    /**
     * This function handles the response of creating a list
     */
    handleCreateListResponse(response) {
      if (response.data) {
        this.saveAList(response.data.wishlistUid);
        if (this.guestListName) {
          this.createCookie(
            cookies.guestListUid,
            response.data.wishlistUid,
            3000,
          );
        }
      }
    },
    filterList(object, code, qty) {
      const remapedObject = {
        product: {
          code: object[code],
        },
        quantity: object[qty],
      };
      return remapedObject;
    },
    /**
     * This function saves a newly created list
     */
    saveAList(uid) {
      this.savePrecuratedList(this.precuratedListData, uid);
      let requestData;
      let wishList = false;
      if (this.isProduct) {
        if (this.isBulkEnabled) {
          const selectedProducts = [];
          this.selectedBulkProducts.map((item) => {
            selectedProducts.push({
              code: item.code,
              wishlistQuantity: 1,
            });
          });
          requestData = {
            entryType: 'PRODUCT',
            wishlistUid: uid,
            products: selectedProducts,
          };
        } else {
          requestData = {
            code: this.cartGuid,
            entryType: 'PRODUCT',
            wishlistUid: uid,
            quantity: this.prodQuantity,
          };
        }
      } else if (!this.wishlistEntries) {
        requestData = {
          code: this.cartGuid,
          entryType: 'CART',
          wishlistUid: uid,
        };
      } else {
        requestData = {
          wishlistEntries: this.queryInput,
          wishlistUid: uid,
        };
        wishList = true;
      }
      const requestConfig = {};
      requestConfig.data = requestData;
      if (!wishList) {
        if (this.isBulkEnabled) {
          this.manageShoppingCartService.saveProductsToList(
            requestConfig,
            this.handleSaveCartResponse,
            this.handleSaveCartError,
          );
        } else {
          this.manageShoppingCartService.saveAList(
            requestConfig,
            this.handleSaveCartResponse,
            this.handleSaveCartError,
          );
        }
      } else {
        this.manageShoppingCartService.saveCartEntries(
          requestConfig,
          this.handleSaveCartResponse,
          this.handleSaveCartError,
        );
      }
    },
    /**
     * This function handles the response of saving a newly created list
     */
    handleSaveCartResponse(response) {
      this.$emit('list-saved');
      globalEventBus.$emit('list-create-or-update', {
        existingList: this.existingList,
        wishlistId: JSON.parse(response.config.data).wishlistUid,
      });
    },
    /**
     * This function handles the error of creating a list
     */
    handleCreateListError(error) {
      if (error && error.response.data.errors[0]) {
        this.existingListError = true;
      }
    },
    /**
     * This function handles the response of saving a newly created list
     */
    handleSaveCartError(error) {},
    /**
     * This function handles response of getting all wishlist
     */
    handleGetWishlistError(error) {
      this.$refs.spinner.hideSpinner();
    },
    /**
     * This function renames object key
     */
    renameObjectKeys(object, label, value) {
      const remapedObject = {
        label: object[label],
        value: object[value],
      };
      return _.assignIn({}, remapedObject, object);
    },
    /**
     * This function renames object key in array and return that
     * @param  {Array} inputArray Array to be modified
     * @param  {Array} outputArray Modified array
     */
    renameArrayObjectKeys(inputArray, outputArray) {
      if (inputArray) {
        for (let i = 0; i < inputArray.length; i++) {
          outputArray.push(
            this.renameObjectKeys(inputArray[i], 'name', 'wishlistUid'),
          );
        }
        return outputArray;
      }
    },
    /**
     * This function will clear the radio button on click of create new input box.
     */
    clearRadioGroup() {
      if (this.$refs.existingWhishlists) {
        this.existingList = false;
        this.$refs.existingWhishlists.resetRadioGroup();
      }
    },
    /**
     * This function saves the precurated list
     * @param  {Array} products products array
     * @param  {String} wishlistUid wishlist id
     */
    savePrecuratedList(products, wishlistUid) {
      const requestData = {
        wishlistUid,
        wishlistEntries: products,
      };
      const requestConfig = {};
      requestConfig.data = requestData;
      this.manageShoppingCartService.addPrecuratedListToCart(
        requestConfig,
        this.handleAddPrecuratedListResponse,
        this.handleAddPrecuratedistError,
      );
    },
    /**
     * This function handles the response of the save precurated list call
     */
    handleAddPrecuratedListResponse() {
      this.$emit('list-saved');
    },
    /**
     * This function handles the error of the save precurated list call
     */
    handleAddPrecuratedListError() {},
    /**
     * This function handles height
     */
    heightAdjustmentInMobile() {
      if (document.querySelector('.vx-save-cart')) {
        if (this.isMobile()) {
          document.querySelector(
            '.vx-save-cart',
          ).style.height = `${window.innerHeight - 159}px`;
        } else {
          document.querySelector('.vx-save-cart').style.height = '';
        }
      }
    },
  },
  destroyed() {
    window.removeEventListener('resize', this.heightAdjustmentInMobile);
  },
};
