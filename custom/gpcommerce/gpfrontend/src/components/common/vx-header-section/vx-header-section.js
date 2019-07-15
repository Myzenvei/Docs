import globals from '../globals';
import vxAutoSuggest from '../../search-browse/vx-auto-suggest/vx-auto-suggest.vue';
import headerMixin from '../mixins/header-mixin';
import vxMiniCart from '../../manage-shopping-cart/vx-mini-cart/vx-mini-cart.vue';
import vxLeftNav from '../../manage-profile-shopping-lists/vx-left-nav/vx-left-nav.vue';
import ManageProfileShoppingListService from '../services/manage-profile-shopping-lists-service';
import mobileMixin from '../mixins/mobile-mixin';
import vxSpinner from '../vx-spinner/vx-spinner.vue';
import cookiesMixin from '../mixins/cookies-mixin';
import vxExtole from '../../../copper-crane/components/common/vx-extole/vx-extole.vue';
import { cookies } from '../../common/mixins/vx-enums';

import { globalEventBus, cartEventBus } from '../../../modules/event-bus';

/**
 * Header section
 */
export default {
  name: 'vx-header-section',
  mixins: [headerMixin, mobileMixin, cookiesMixin],
  props: {
    /**
     * @model details
     */
    headerData: {
      type: Object,
      required: true,
    },
    i18n: {
      type: Object,
      required: true,
    },
    isFavoritesEnabled: {
      type: Boolean,
      default: false,
    },
    isHomePage: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      globals,
      showMobileNav: false,
      selectedPrimary: '',
      cartItems: 0,
      screenWidth: screen.width,
      desktopWidthMin: 1200,
      showMiniCart: false,
      userName: '',
      isMiniCartHovered: false,
      miniCartTimeout: 250,
      miniCartTimer: {},
      tabletView: false,
      toggleSearchIcon: false,
      cookies,
      windowWidth: window.innerWidth,
      manageProfileShoppingListService: new ManageProfileShoppingListService(),
      isPromoEnabled: false,
    };
  },
  computed: {
    headerOptions() {
      return this.headerData.headerOptions;
    },
    loginMenu() {
      return this.headerData.loginMenu;
    },
    navMenu() {
      return this.headerData.navMenu;
    },
    findMenu() {
      return this.headerData.findMenu;
    },
    checkoutMenu() {
      return this.headerData.headerOptions.checkoutMenu;
    },
  },
  components: {
    vxAutoSuggest,
    vxMiniCart,
    vxLeftNav,
    vxSpinner,
    vxExtole,
  },
  methods: {
    setHeaderHeight() {
      if (this.showMobileNav) {
        setTimeout(() => {
          this.$el.querySelector(
            '.header-left-section .mobile-view',
          ).style.height = `${window.innerHeight - 52}px`;
        }, 400);
      }
    },
    dismissMobileMenu() {
      if (this.screenWidth <= this.desktopWidthMin) {
        this.showMobileNav = !this.showMobileNav;
      }
    },
    updateAria(isExpand) {
      if (isExpand) {
        if (document.querySelector('.navbar-bottom-right')) {
          document
            .querySelector('.navbar-bottom-right')
            .setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document
            .querySelector('.main__inner-wrapper')
            .setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.footer-section')) {
          document
            .querySelector('.footer-section')
            .setAttribute('aria-hidden', true);
        }
      } else {
        if (document.querySelector('.navbar-bottom-right')) {
          document
            .querySelector('.navbar-bottom-right')
            .setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document
            .querySelector('.main__inner-wrapper')
            .setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.footer-section')) {
          document
            .querySelector('.footer-section')
            .setAttribute('aria-hidden', false);
        }
      }
    },

    selectPrimary(selectedItem) {
      this.selectedPrimary = selectedItem;
      const self = this;
      if (this.screenWidth <= this.desktopWidthMin) {
        setTimeout(() => {
          self.$refs.navigationMenu.scrollTop = 0;
          document.querySelector('.js-secondary-action-el').focus();
        }, 150);
      }
      if (this.selectPrimary !== '') {
        if (document.querySelector('.navbar-bottom-right')) {
          document
            .querySelector('.navbar-bottom-right')
            .setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document
            .querySelector('.main__inner-wrapper')
            .setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.footer-section')) {
          document
            .querySelector('.footer-section')
            .setAttribute('aria-hidden', true);
        }
      } else {
        if (document.querySelector('.navbar-bottom-right')) {
          document
            .querySelector('.navbar-bottom-right')
            .setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document
            .querySelector('.main__inner-wrapper')
            .setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.footer-section')) {
          document
            .querySelector('.footer-section')
            .setAttribute('aria-hidden', false);
        }
      }
    },
    isSecondaryAvailable(primaryItem) {
      if (primaryItem.secondary) {
        return !(
          (!primaryItem.secondary.title ||
            primaryItem.secondary.title === '') &&
          (!primaryItem.secondary.secOptions ||
            primaryItem.secondary.secOptions.length === 0)
        );
      }
      return false;
    },
    focusParent(el, menu) {
      this.selectedPrimary = '';
      for (const i in menu) {
        if (menu[i].primary === el) {
          setTimeout(() => {
            document
              .querySelectorAll('.primary-menu-item')
              [i].childNodes[0].focus();
          }, 100);
        }
      }
    },
    /**
     * The below method is used to get the parent
     * element with specific class
     * @param {*the current element} el
     * @param {*the target class } cls
     */
    findAncestor(el, cls) {
      while ((el = el.parentElement) && !el.classList.contains(cls));
      return el;
    },
    displayMiniCart() {
      clearTimeout(this.miniCartTimer);
      this.showMiniCart = true;
    },
    hideMiniCart() {
      this.miniCartTimer = window.setTimeout(() => {
        if (!this.isMiniCartHovered) {
          this.showMiniCart = false;
        }
      }, this.miniCartTimeout);
    },
    handleResize() {
      if (window.innerWidth !== this.windowWidth) {
        this.tabletView = this.isTablet();
        this.windowWidth = window.innerWidth;
      }
    },
    navigateTomyLists() {
      if (this.globals.loggedIn || !this.globals.siteConfig.isGuestList) {
        this.globals.navigateToUrl('myList');
      } else {
        const wuid = this.readCookie(cookies.guestListUid)
          ? this.readCookie(cookies.guestListUid)
          : '';
        window.location = `${this.globals.getNavigationUrl(
          'listDetails',
        )}/?listName=${wuid}`;
      }
    },
    setHeaderFlyoutWidth(event, count) {
      const targetedElement = event.target;
      const defaultWidth = 656;
      const oneColumnWidth = 323;

      if (count > 10 && count < 16) {
        const liWidth = $(targetedElement)
          .find('.flyout-sec li')
          .innerWidth();
        $(targetedElement)
          .find('.secondary-menu')
          .innerWidth(liWidth + defaultWidth);
      } else if (count > 15) {
        const liWidth = $(targetedElement)
          .find('.flyout-sec li')
          .innerWidth();
        $(targetedElement)
          .find('.secondary-menu')
          .innerWidth((liWidth * 2) + defaultWidth);
      } else if (count < 6) {
        $(targetedElement)
          .find('.secondary-menu')
          .innerWidth(oneColumnWidth);
        $(targetedElement)
          .find('.js-secondary-child')
          .css('max-width', '100%');
      } else {
        $(targetedElement)
          .find('.secondary-menu')
          .innerWidth(defaultWidth);
      }
    },
  },
  mounted() {
    this.clearKauthData();
    this.tabletView = this.isTablet();
    this.isPromoEnabled = !(
      this.globals.siteConfig.isFindAStoreEnabled ||
      this.globals.siteConfig.isFindADistributorEnabled ||
      this.globals.siteConfig.isReferAFriend
    );
    this.loginAreaLabel = globals.getIsLoggedIn()
      ? this.i18n.loginSectionTitle
      : this.i18n.loginHeading;
    this.userName = this.headerData.loginMenu.menuOptions.length
      ? this.headerData.loginMenu.menuOptions[0].option
      : '';
    cartEventBus.$on('total-items-updated', (items) => {
      this.cartItems = items;
    });
    globalEventBus.$emit(
      'is-checkout',
      this.headerData.headerOptions.isCheckout,
    );
    globalEventBus.$on('updated-name', (val) => {
      this.userName = val;
    });
    // drop down key board navigation support
    $(document).ready(() => {
      /**
       * used to make the drop down accessible with keyboard for desktop
       */
      if (!this.isTablet()) {
        $('.navigation-inner-wrapper').accessibleDropDown();
      }

      /**
       * desktop keyborad navigation controlling with shift+tab and tab
       */
      $('body').on('keydown', '.navbar-bottom', (e) => {
        const keycode = window.event ? event.keyCode : e.keyCode;
        if (!this.isTablet()) {
          if (e.shiftKey && e.keyCode === 9) {
            // when both shift and tab is pressed
            if (
              document.activeElement.parentElement.className.indexOf(
                'js-primary-menu-item',
              ) > -1
            ) {
              $('.navigation-inner-wrapper')
                .find('.hovered')
                .removeClass('hovered');
              document.activeElement.parentElement.classList.add(
                'js-primary-menu-item',
              );
            }
          } else if (e.keyCode === 9) {
            /** triggers for only tab */
            if (
              document.activeElement.parentElement.className.indexOf(
                'js-secondary-child',
              ) > -1
            ) {
              /** gets the target element node */
              const nodes = Array.prototype.slice.call(
                document.activeElement.parentElement.parentElement.children,
              );
              /** gets the target element parent node */
              const liRef = e.target.parentElement;
              /** checks the condition if the index of target element parent node is equal to the length of the root target element minus 1 */
              if (
                nodes.indexOf(liRef) ===
                document.activeElement.parentElement.parentElement.children
                  .length -
                  1
              ) {
                $('.navigation-inner-wrapper')
                  .find('.hovered')
                  .removeClass('hovered');
                this.findAncestor(
                  e.target,
                  'js-primary-menu-item',
                ).setAttribute('aria-expanded', false);
              }
            }
          }
        } else {
          /** the below block triggers for tablet and mobile */
          /** triggers for the combination of shift and tab */
          if (e.shiftKey && e.keyCode === 9) {
            /** checks if the class is present for the parent element node */
            if (
              e.target.parentElement.className.indexOf('js-primary-menu-item') >
              -1
            ) {
              /** the below logic gets the index of the target element */
              const nodes = Array.prototype.slice.call(
                document.querySelector('.primary-menu').children,
              );
              const liRef = e.target.parentElement;
              /** if the backward tab reaches to first node in primary level the next reverse tab should focus to hamburer icon */
              if (nodes.indexOf(liRef) === 0) {
                setTimeout(() => {
                  document.querySelector('.icon-menu').focus();
                }, 100);
              }
            } else if (
              e.target.className.indexOf('js-secondary-action-el') > -1
            ) {
              /** if the backward tab reaches to first node in secondary level the next reverse tab should focus to back icon */
              setTimeout(() => {
                document
                  .querySelector('.flyout-sec')
                  .lastChild.childNodes[0].focus();
              }, 100);
            }
          } else if (e.keyCode === 9) {
            /** forward tabbed navigation logic for mobile */
            /** checks if the class name is available */
            if (
              e.target.parentElement.className.indexOf('js-primary-menu-item') >
              -1
            ) {
              /** gets the children as an array for the given selector */
              const nodes = Array.prototype.slice.call(
                document.querySelector('.login-mobile-view').children,
              );
              /** gets target parent */
              const liRef = e.target.parentElement;
              if (
                nodes.indexOf(liRef) ===
                document.querySelector('.login-mobile-view').children.length - 1
              ) {
                setTimeout(() => {
                  document.querySelector('.icon-menu').focus();
                }, 100);
              }
            } else if (
              e.target.parentElement.className.indexOf('js-secondary-child') >
              -1
            ) {
              const nodes = Array.prototype.slice.call(
                document.querySelector('.flyout-sec').children,
              );
              const liRef = e.target.parentElement;
              if (
                nodes.indexOf(liRef) ===
                document.querySelector('.flyout-sec').children.length - 1
              ) {
                setTimeout(() => {
                  document.querySelector('.js-secondary-action-el').focus();
                }, 100);
              }
            }
          }
        }
      });
      /**
       * the below block triggers for the tabbed navigation over hamburger icon
       */
      $('body').on('keydown', '.icon-menu', (e) => {
        /**
         * shft + tab naviagtion
         */
        if (e.shiftKey && e.keyCode === 9) {
          /** checks the class presence */
          if (
            document
              .querySelector('.navbar-bottom-left')
              .className.indexOf('mobile-view') > -1
          ) {
            e.preventDefault();
            setTimeout(() => {
              /** sets the focus on the last node */
              document
                .querySelector('.login-mobile-view')
                .lastElementChild.childNodes[0].focus();
            }, 100);
          }
        } else if (e.keyCode === 9) {
          /** tab navigation */
          /** checks the class presence */
          if (
            document
              .querySelector('.navbar-bottom-left')
              .className.indexOf('mobile-view') > -1
          ) {
            e.preventDefault();
            setTimeout(() => {
              /** sets the focus on the first primary link */
              document
                .querySelectorAll('.primary-menu-item')[0]
                .childNodes[0].focus();
            }, 100);
          }
        }
      });
      $(document).on('click', 'body', (e) => {
        if (typeof e.target.parentElement.className !== 'object' &&
          !(
            e.target.parentElement.className.indexOf('js-primary-menu-item') >
            -1
          )
        ) {
          $('.navigation-inner-wrapper')
            .find('.hovered')
            .removeClass('hovered')
            .first('a')
            .attr('aria-expanded', false);
        }
      });
    });

    $.fn.accessibleDropDown = function() {
      const el = $(this);
      /* Make dropdown menus keyboard accessible */
      $('a', el).focus(function() {
        $(this)
          .parents('.js-primary-menu-item')
          .addClass('hovered')
          .first('a')
          .attr('aria-expanded', true);
      });
    };

    window.addEventListener('resize', this.handleResize);
    this.handleResize();
    if (this.globals.siteConfig.isReferAFriend) {
      this.$refs.vxExtoleRef.initiateExtoleOnHeader();
    }
  },
  destroyed() {
    window.removeEventListener('resize', this.handleResize);
  },
  filters: {
    maxCharacters(value) {
      const charLimit = 120;
      return value.substring(0, charLimit);
    },
  },
};
