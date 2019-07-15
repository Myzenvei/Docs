import globals from '../../../../components/common/globals';
import vxAutoSuggest from '../../../../components/search-browse/vx-auto-suggest/vx-auto-suggest.vue';
import headerMixin from '../../../../components/common/mixins/header-mixin';
import vxMiniCart from '../../../../components/manage-shopping-cart/vx-mini-cart/vx-mini-cart.vue';
import mobileMixin from '../../../../components/common/mixins/mobile-mixin';
import vxLeftNav from '../../../../components/manage-profile-shopping-lists/vx-left-nav/vx-left-nav.vue';
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
      showSearchBox: false,
      isMiniCartHovered: false,
      miniCartTimeout: 250,
      miniCartTimer: {},
      toggleSearchIcon: false,
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
  },
  methods: {
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
        if (document.querySelector('.vx-employee-footer')) {
          document.querySelector('.vx-employee-footer').setAttribute('aria-hidden', true);
        }
      } else {
        if (document.querySelector('.navbar-bottom-right')) {
          document.querySelector('.navbar-bottom-right').setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.main__inner-wrapper')) {
          document.querySelector('.main__inner-wrapper').setAttribute('aria-hidden', false);
        }
        if (document.querySelector('.vx-employee-footer')) {
          document.querySelector('.vx-employee-footer').setAttribute('aria-hidden', false);
        }
      }
    },
    selectPrimary(selectedItem) {
      this.selectedPrimary = selectedItem;
      const SELF = this;
      if (this.screenWidth <= this.desktopWidthMin) {

        setTimeout(() => {
          SELF.$refs.navigationMenu.scrollTop = 0;
          document.querySelector('.js-secondary-action-el').focus();
        }, 150);
      }
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
    isSecondaryAvailable(primaryItem) {
      if (primaryItem.secondary) {
        return !(
          (!primaryItem.secondary.title || primaryItem.secondary.title === '') &&
          (!primaryItem.secondary.secOptions || primaryItem.secondary.secOptions.length === 0)
        );
      }
      return false;
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
    findAncestor(el, cls) {
      while ((el = el.parentElement) && !el.classList.contains(cls));
      return el;
    },
  },
  created() {
    window.addEventListener('click', () => {
      this.showSearchBox = false;
    });
  },
  mounted() {
    this.clearKauthData();
    cartEventBus.$on('total-items-updated', (items) => {
      this.cartItems = items;
    });
    globalEventBus.$emit('is-checkout', this.headerData.headerOptions.isCheckout);

    $(document).ready(() => {
      if (!this.isTablet()) {
        $(".navigation-inner-wrapper").accessibleDropDown();
      }
      $('body').on('keydown', '.navbar-bottom', (e) => {
        let keycode = (window.event) ? event.keyCode : e.keyCode;
        if (!this.isTablet()) {
          if (e.shiftKey && e.keyCode === 9) {
            //shift was down when tab was pressed
            if (document.activeElement.parentElement.className.indexOf('js-primary-menu-item') > -1) {
              $('.navigation-inner-wrapper').find('.hovered').removeClass('hovered');
              document.activeElement.parentElement.classList.add('js-primary-menu-item');
            }
          } else if (e.keyCode === 9) {
            if (document.activeElement.parentElement.className.indexOf('js-secondary-child') > -1) {
              let nodes = Array.prototype.slice.call(document.activeElement.parentElement.parentElement.children);
              let liRef = e.target.parentElement;
              if (nodes.indexOf(liRef) === (document.activeElement.parentElement.parentElement.children.length - 1)) {
                $('.navigation-inner-wrapper').find('.hovered').removeClass('hovered');
                this.findAncestor(e.target, 'js-primary-menu-item').setAttribute('aria-expanded', false);
              }
            }
          }
        } else {
          if (e.shiftKey && e.keyCode === 9) {
            if (e.target.parentElement.className.indexOf('js-primary-menu-item') > -1) {
              let nodes = Array.prototype.slice.call(document.querySelector('.primary-menu').children);
              let liRef = e.target.parentElement;
              if (nodes.indexOf(liRef) === 0) {
                setTimeout(() => {
                  document.querySelector('.icon-menu').focus();
                }, 100);
              }
            } else if (e.target.className.indexOf('js-secondary-action-el') > -1) {
              setTimeout(() => {
                document.querySelector('.js-flyout-sec').lastChild.childNodes[0].focus();
              }, 100);
            }
          } else if (e.keyCode === 9) {
            if (e.target.parentElement.className.indexOf('js-primary-menu-item') > -1) {
              let nodes = Array.prototype.slice.call(document.querySelector('.login-mobile-view').children);
              let liRef = e.target.parentElement;
              if (nodes.indexOf(liRef) === (document.querySelector('.login-mobile-view').children.length - 1)) {
                setTimeout(() => {
                  document.querySelector('.icon-menu').focus();
                }, 100);
              }
            } else if (e.target.parentElement.className.indexOf('js-secondary-child') > -1) {
              let nodes = Array.prototype.slice.call(document.querySelector('.js-flyout-sec').children);
              let liRef = e.target.parentElement;
              if (nodes.indexOf(liRef) === (document.querySelector('.js-flyout-sec').children.length - 1)) {
                setTimeout(() => {
                  document.querySelector('.js-secondary-action-el').focus();
                }, 100);
              }
            }
          }
        }
      });
      $('body').on('keydown', '.icon-menu', (e) => {
        if (e.shiftKey && e.keyCode === 9) {
          if (document.querySelector('.navbar-bottom-left').className.indexOf('mobile-view') > -1) {
            e.preventDefault();
            setTimeout(() => {
              document.querySelector('.login-mobile-view').lastElementChild.childNodes[0].focus();
            }, 100);
          }
        } else if (e.keyCode === 9) {
          if (document.querySelector('.navbar-bottom-left').className.indexOf('mobile-view') > -1) {
            e.preventDefault();
            setTimeout(() => {
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
  },
  filters: {
    maxCharacters(value) {
      const charLimit = 120;
      return value.substring(0, charLimit);
    },
  },
};
