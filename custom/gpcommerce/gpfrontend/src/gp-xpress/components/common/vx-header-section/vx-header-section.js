import globals from '../../../../components/common/globals';
import vxAutoSuggest from '../../../../components/search-browse/vx-auto-suggest/vx-auto-suggest.vue';
import headerMixin from '../../../../components/common/mixins/header-mixin';
import mobileMixin from '../../../../components/common/mixins/mobile-mixin';
import vxMiniCart from '../../../../components/manage-shopping-cart/vx-mini-cart/vx-mini-cart.vue';

import {
  globalEventBus,
  cartEventBus
} from '../../../../modules/event-bus';

/**
 * Header section
 */
export default {
  name: 'vx-header-section',
  mixins: [headerMixin, mobileMixin],
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
    navigationLinks: {
      type: Object,
      required: true,
      default: {
        navLinks: [
          {
            linkText: 'Lists Link',
            linkURL: '/my-account/lists',
          },
        ],
      },
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
      isMiniCartHovered: false,
      miniCartTimeout: 250,
      miniCartTimer: {},
      tabletView: false,
      toggleSearchIcon: false,
      windowWidth: window.innerWidth,
    };
  },
  computed: {
    headerOptions() {
      return this.headerData.headerOptions;
    },
    navMenu() {
      return this.headerData.navMenu;
    },
    checkoutMenu() {
      return this.headerData.headerOptions.checkoutMenu;
    },
    navLinks() {
      return this.navigationLinks.navLinks;
    },
  },
  components: {
    vxAutoSuggest,
    vxMiniCart,
  },
  methods: {
    /**
     * This function displays mini cart
     */
    displayMiniCart() {
      clearTimeout(this.miniCartTimer);
      this.showMiniCart = true;
    },
    /**
     * This function hides mini cart
     */
    hideMiniCart() {
      this.miniCartTimer = window.setTimeout(() => {
        if (!this.isMiniCartHovered) {
          this.showMiniCart = false;
        }
      }, this.miniCartTimeout);
    },

    dismissMobileMenu() {
      if (this.screenWidth <= this.desktopWidthMin) {
        this.showMobileNav = !this.showMobileNav;
      }
    },
    updateAria(isExpand) {
      if (isExpand) {
        if (document.querySelector('.navbar-bottom-right')) {
          document.querySelector('.navbar-bottom-right').setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document.querySelector('.main__inner-wrapper').setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.footer-section')) {
          document.querySelector('.footer-section').setAttribute('aria-hidden', true);
        }
      } else {
        if (document.querySelector('.navbar-bottom-right')) {
          document.querySelector('.navbar-bottom-right').setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document.querySelector('.main__inner-wrapper').setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.footer-section')) {
          document.querySelector('.footer-section').setAttribute('aria-hidden', false);
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
          document.querySelector('.navbar-bottom-right').setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document.querySelector('.main__inner-wrapper').setAttribute('aria-hidden', true);
        }
        if (document.querySelector('.footer-section')) {
          document.querySelector('.footer-section').setAttribute('aria-hidden', true);
        }
      } else {
        if (document.querySelector('.navbar-bottom-right')) {
          document.querySelector('.navbar-bottom-right').setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document.querySelector('.main__inner-wrapper').setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.footer-section')) {
          document.querySelector('.footer-section').setAttribute('aria-hidden', false);
        }
      }
    },
    isSecondaryAvailable(primaryItem) {
      if (primaryItem.secondary) {
        return !(
          (!primaryItem.secondary.title || primaryItem.secondary.title === '') &&
          (!primaryItem.secondary.secOptions || primaryItem.secondary.secOptions.length === 0)
        );
      }
      return false;
    },
    focusParent(el, menu) {
      this.selectedPrimary = '';
      for (let i in menu) {
        if (menu[i].primary === el) {
          setTimeout(() => {
            document.querySelectorAll('.primary-menu-item')[i].childNodes[0].focus();
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
    handleResize() {
      if (window.innerWidth !== this.windowWidth) {
        this.tabletView = this.isTablet();
        this.windowWidth = window.innerWidth;
      }
    },
    setHeaderFlyoutWidth(event, count) {
      const targetedElement = event.target;
      const defaultWidth = 656;

      if (count > 10) {
        const liWidth = $(targetedElement).find('.flyout-sec li').innerWidth();
        $(targetedElement).find('.secondary-menu').innerWidth(liWidth + defaultWidth);
      } else {
        $(targetedElement).find('.secondary-menu').innerWidth(defaultWidth);
      }
    },
  },
  mounted() {
    this.clearKauthData();
    this.tabletView = this.isTablet();
    this.loginAreaLabel = globals.getIsLoggedIn() ? this.i18n.loginSectionTitle : this.i18n.loginHeading;
    cartEventBus.$on('total-items-updated', (items) => {
      this.cartItems = items;
    });
    // drop down key board navigation support
    $(document).ready(() => {
      /**
       * used to make the drop down accessible with keyboard for desktop
       */
      if (!this.isTablet()) {
        $(".navigation-inner-wrapper").accessibleDropDown();
      }

      /**
       * desktop keyborad navigation controlling with shift+tab and tab
       */
      $('body').on('keydown', '.navbar-bottom', (e) => {
        let keycode = (window.event) ? event.keyCode : e.keyCode;
        if (!this.isTablet()) {
          if (e.shiftKey && e.keyCode === 9) {
            //when both shift and tab is pressed
            if (document.activeElement.parentElement.className.indexOf('js-primary-menu-item') > -1) {
              $('.navigation-inner-wrapper').find('.hovered').removeClass('hovered');
              document.activeElement.parentElement.classList.add('js-primary-menu-item');
            }
          } else if (e.keyCode === 9) { /** triggers for only tab */
            if (document.activeElement.parentElement.className.indexOf('js-secondary-child') > -1) {
              /**gets the target element node */
              let nodes = Array.prototype.slice.call(document.activeElement.parentElement.parentElement.children);
              /**gets the target element parent node*/
              let liRef = e.target.parentElement;
              /**checks the condition if the index of target element parent node is equal to the length of the root target element minus 1*/
              if (nodes.indexOf(liRef) === (document.activeElement.parentElement.parentElement.children.length - 1)) {
                $('.navigation-inner-wrapper').find('.hovered').removeClass('hovered');
                this.findAncestor(e.target, 'js-primary-menu-item').setAttribute('aria-expanded', false);
              }
            }
          }
        } else {
          /**the below block triggers for tablet and mobile */
          /**triggers for the combination of shift and tab */
          if (e.shiftKey && e.keyCode === 9) {
            /**checks if the class is present for the parent element node */
            if (e.target.parentElement.className.indexOf('js-primary-menu-item') > -1) {
              /**the below logic gets the index of the target element */
              let nodes = Array.prototype.slice.call(document.querySelector('.primary-menu').children);
              let liRef = e.target.parentElement;
              /**if the backward tab reaches to first node in primary level the next reverse tab should focus to hamburer icon */
              if (nodes.indexOf(liRef) === 0) {
                setTimeout(() => {
                  document.querySelector('.icon-menu').focus();
                }, 100);
              }
            } else if (e.target.className.indexOf('js-secondary-action-el') > -1) {
              /**if the backward tab reaches to first node in secondary level the next reverse tab should focus to back icon */
              setTimeout(() => {
                document.querySelector('.flyout-sec').lastChild.childNodes[0].focus();
              }, 100);
            }
          } else if (e.keyCode === 9) {
            /**forward tabbed navigation logic for mobile */
            /**checks if the class name is available */
            if (e.target.parentElement.className.indexOf('js-primary-menu-item') > -1) {
              /**gets the children as an array for the given selector */
              let nodes = Array.prototype.slice.call(document.querySelector('.login-mobile-view').children);
              /**gets target parent */
              let liRef = e.target.parentElement;
              if (nodes.indexOf(liRef) === (document.querySelector('.login-mobile-view').children.length - 1)) {
                setTimeout(() => {
                  document.querySelector('.icon-menu').focus();
                }, 100);
              }
            } else if (e.target.parentElement.className.indexOf('js-secondary-child') > -1) {
              let nodes = Array.prototype.slice.call(document.querySelector('.flyout-sec').children);
              let liRef = e.target.parentElement;
              if (nodes.indexOf(liRef) === (document.querySelector('.flyout-sec').children.length - 1)) {
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
          /**checks the class presence */
          if (document.querySelector('.navbar-bottom-left').className.indexOf('mobile-view') > -1) {
            e.preventDefault();
            setTimeout(() => {
              /**sets the focus on the last node */
              document.querySelector('.login-mobile-view').lastElementChild.childNodes[0].focus();
            }, 100);
          }
        } else if (e.keyCode === 9) {
          /**tab navigation */
          /**checks the class presence */
          if (document.querySelector('.navbar-bottom-left').className.indexOf('mobile-view') > -1) {
            e.preventDefault();
            setTimeout(() => {
              /**sets the focus on the first primary link */
              document.querySelectorAll('.primary-menu-item')[0].childNodes[0].focus();
            }, 100);
          }
        }
      });
      $(document).on('click', 'body', (e) => {
        if (typeof e.target.parentElement.className !== 'object' && !(e.target.parentElement.className.indexOf('js-primary-menu-item') > -1)) {
          $('.navigation-inner-wrapper').find('.hovered').removeClass('hovered').first('a').attr('aria-expanded', false);
        }
      });
    });

    $.fn.accessibleDropDown = function () {
      var el = $(this);
      /* Make dropdown menus keyboard accessible */
      $("a", el).focus(function () {
        $(this).parents(".js-primary-menu-item").addClass("hovered").first('a').attr('aria-expanded', true);
      });
    };

    window.addEventListener('resize', this.handleResize);
    this.handleResize();
  },
  destroyed() {
    window.removeEventListener('resize', this.handleResize);
  },
};
