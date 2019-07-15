import globals from '../globals';
import vxSpinner from '../vx-spinner/vx-spinner.vue';
import flyoutBannerMixin from '../vx-flyout-banner/vx-flyout-banner-mixin';
import PdpService from '../../common/services/pdp-service';
import {
  favorites
} from '../../common/mixins/vx-enums';
import {
  eventBus
} from '../../../modules/event-bus';

export default {
  name: 'vx-favorite-btn',
  components: {
    vxSpinner,
  },
  props: {
    i18n: {
      type: Object,
    },
  },
  mixins: [flyoutBannerMixin],
  data() {
    return {
      globals,
      favorites,
      pdpService: new PdpService(),
      productId: "",
      isActive: false,
      isFavoriteFilled: false,
      isSafari: /^((?!chrome|android).)*safari/i.test(navigator.userAgent)
    };
  },
  computed: {},
  mounted() {
    eventBus.$on("ymktToggleFavorite", (product) => {
      if(product.code){
        this.productId = product.code;
        this.isActive = product.isActive;
        this.toggleFavorite();
      }
    });
  },
  methods: {
    toggleFavorite() {
      this.isActive = !this.isActive;
      this.handleFavorites();
    },
    handleFavorites(){
      if (this.globals.loggedIn) {
        this.pdpService.getShoppingLists({},
          this.handleGetWishlistResponse,
          this.handleGetWishlistError,
        );
      } else {
        this.navigateToLogin();
      }
    },
    handleGetWishlistResponse(response) {
      if (response) {
        const item = response.data.wishlists.filter(list => list.name === this.favorites.favorites);
        if (this.isActive) {
          const requestdata = {
            code: this.productId,
            entryType: 'PRODUCT',
            wishlistUid: item[0].wishlistUid,
            quantity: "1",
          };
          const requestConfig = {};
          requestConfig.data = requestdata;
          this.pdpService.saveAList(requestConfig, this.handleSaveListResponse, this.handleSaveListError);
        } else {
          this.removeFavorite(item[0].wishlistUid);
        }
      }
    },
    /**
    * Handle the error of get all wishlists service
    */
    handleGetWishlistError(error) {
      if (error) {
        this.showFlyout('error', error.data.description, true);
      }
    },
    /**
    * This function is to handle the save list response
    */
    handleSaveListResponse(response){
      if (response) {
        this.showFlyout('success', response.data.description, true);
        window.updateFavoriteProduct(this.productId);
      }
    },
    /**
    * This function is to handle the save list error
    */
    handleSaveListError(error){
      if (error) {
        this.showFlyout('error', error.data.description, true);
      }
    },
    /**
    * This function is to navigate user to Login page
    */
    navigateToLogin() {
      let url = this.globals.getNavBaseUrl() + '/login?site=' + this.globals.siteId;
      window.location = url;
    },
    /**
    * This function is to remove the item from Favorites List
    */
   removeFavorite(wishlistUid) {
     const requestBody = {
       wishlistUid,
       product: {
         code: this.productId,
       },
     };
     const requestConfig = {};
     requestConfig.data = requestBody;
     this.pdpService.deleteCartItem(requestConfig, this.handleRemoveFavoriteResponse, this.handleRemoveFavoriteError);
   },
   /**
    * This function handles the response of delete cart item service
    */
   handleRemoveFavoriteResponse() {
    window.updateFavoriteProduct(this.productId);
   },
   /**
    * This function handles the error of delete cart item service
    */
   handleRemoveFavoriteError() {},
  },
};
