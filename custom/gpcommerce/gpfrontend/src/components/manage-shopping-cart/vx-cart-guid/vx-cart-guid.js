import globals from '../../common/globals';
import cookiesMixin from '../../common/mixins/cookies-mixin';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';
import {
  cartEventBus
} from '../../../modules/event-bus';
import ManageShoppingCartService from '../../common/services/manage-shopping-cart-service';


export default {
  name: 'vx-cart-guid',
  mixins: [cookiesMixin],
  components: {
    vxSpinner
  },
  props: [],
  data() {
    return {
      globals,
      guid: "guid",
      guid_user: 'guid_user',
      events: ['load', 'mousemove', 'mousedown', 'click', 'scroll', 'keypress'],
      userTimeInterval: '',
      idleTime: 0,
      cartInactiveTime: 30,
      manageShoppingCartService: new ManageShoppingCartService(),
      restrictBasicCartPages: ['cartPage', 'checkoutPage'],
    };
  },
  computed: {

  },
  mounted() {
    cartEventBus.$on('call-basic-cart', () => {
      this.getBasicCart();
    });
    this.conditionCheck();
    this.idleTimeHandler();
  },
  methods: {
    /**
     * This function checks whether user is logged in or not
     */
    conditionCheck() {
      // When user is loggedin
      if (this.globals.getIsLoggedIn()) {
        this.loggedInHandler();
      } else {
        this.loggedOutHandler();
      }
    },
    /**
     * This function creates cart for logged in user
     */
    loggedInHandler() {
      if (this.readCookie(this.guid_user) == 'anonymous' && !this.readCookie('asmCartId')) {
        let anonymousGuid = this.readCookie(this.guid);
        this.eraseCookie(this.guid);
        this.eraseCookie(this.guid_user);
        // Pass the Old guid for merge cart call
        this.callCreateCart(anonymousGuid);
      } else {

        this.getBasicCart();

      }
    },
    /**
     *This function creates cart for logged out user
     */
    loggedOutHandler() {
      if (!this.readCookie(this.guid)) {
        this.callCreateCart();
      } else {
        this.getBasicCart();
      }
    },
    /**
     *This function gets the cart details
     */
    getBasicCart() {
      if (this.restrictBasicCartPages.indexOf(this.globals.currentPage) >= 0 && this.globals.getIsLoggedIn()) {
        this.$root.$data.cartExists = true;
        return;
      }
      const requestConfig = {};
      this.manageShoppingCartService.getBasicCartService(requestConfig, this.handleBasicCartResponse, this.handleBasicCartError);
      this.$refs.spinner.showSpinner();
    },
    /**
     *This function handles the repsonse of cart details call
     */
    handleBasicCartResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.$root.$data.cartExists = true;
      cartEventBus.$emit('total-items-updated', response.data.totalItems);
    },
    /**
     *This function handles the error of cart details call
     */
    handleBasicCartError() {
      this.$refs.spinner.hideSpinner();
      if (!this.globals.getIsLoggedIn()) {
        this.callCreateCart();
      }
    },
    /**
     *This function creates cart
     * @param  {String} anonymousGuid cart id for anonymous user
     */
    callCreateCart(anonymousGuid) {
      const requestConfig = {};
      if (anonymousGuid) {
        requestConfig.params = {
          oldCartId: anonymousGuid
        }
      }
      this.manageShoppingCartService.createCartService(requestConfig, this.handleCreateCartResponse, this.handleCreateCartError);
      this.$refs.spinner.showSpinner();
    },
    /**
     *This function handles the response for create cart call
     */
    handleCreateCartResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.$root.$data.cartExists = true;
      cartEventBus.$emit('total-items-updated', response.data.totalItems);
      this.createCookie(this.guid, response.data.guid);
      if (this.globals.getIsLoggedIn()) {
        this.createCookie(this.guid_user, "current");
      } else {
        this.createCookie(this.guid_user, "anonymous");
      }
    },
    /**
     *This function handles the error for create cart call
     */
    handleCreateCartError(error) {
      this.$refs.spinner.hideSpinner();
    },
    /**
     *This function deletes cart after 30 min of time for guest user
     */
    idleTimeHandler() {
      if (!globals.getIsLoggedIn()) {
        this.startTimer();
        for (let i = 0; i < this.events.length; i++) {
          document.addEventListener(this.events[i], this.handleEvents.bind(this));
        }
      }
    },
    handleEvents() {
      this.idleTime = 0;
    },
    /**
     *This function stops the timer and calls the delete cart method
     */
    stopTimer() {
      clearInterval(this.userTimeInterval);
      this.userTimeInterval = '';
      this.idleTime = 0;
      const requestConfig = {};
      this.manageShoppingCartService.deleteCartService(requestConfig, this.handleDeleteCartResponse, this.handleDeleteCartError);
      this.$refs.spinner.showSpinner();
    },
    /**
     *This function handles the response of stopping the timer and calling the delete cart method
     */
    handleDeleteCartResponse() {
      this.eraseCookie(this.guid);
      this.eraseCookie(this.guid_user);
      this.$refs.spinner.hideSpinner();
    },
    /**
     *This function handles the response of stopping the timer and calling the delete cart method
     */
    handleDeleteCartError() {
      this.$refs.spinner.hideSpinner();
    },
    /**
     *This function starts the timer
     */
    startTimer() {
      let self = this;
      this.userTimeInterval = setInterval(function () {
        self.idleTime = self.idleTime + 1;
        if (self.idleTime > self.cartInactiveTime) {
          self.stopTimer();
        }
      }, 60000);
    },
  }
}
