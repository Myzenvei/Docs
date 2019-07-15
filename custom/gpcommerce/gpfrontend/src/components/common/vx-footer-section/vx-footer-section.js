import globals from '../globals';
import mobileMixin from '../mixins/mobile-mixin';

export default {
  name: 'vx-footer-section',
  components: {},
  props: {
    footerData: {
      type: Object,
      required: true,
    },
    copyrightText: {
      type: String,
    },
    isCheckoutFooter: {
      type: Boolean,
      required: true,
    },
    isHomePage: {
      type: Boolean,
      required: false,
    },
  },
  mixins: [mobileMixin],
  data() {
    return {
      globals,
      socialLinks: {
        facebook: '',
        pinterest: '',
        linkedin: '',
        youtube: '',
        twitter: '',
        instagram: '',
        snapchat: '',
        socialText: '',
      },
      footerColumnLength: 4,
      footerDataLength: this.footerData.columns.length,
    };
  },
  computed: {
    // checkFooterColumns() {
    //   let footerClass = '';
    //   if (this.isCheckoutFooter) {
    //     footerClass = 'col-sm-12 justify-content-start';
    //   } else {
    //     footerClass = 'col-sm-9 col-md-8';
    //     if (
    //       this.filteredFooterData &&
    //       this.filteredFooterData.length > this.footerColumnLength
    //     ) {
    //       footerClass = `${footerClass} justify-content-start`;
    //     } else {
    //       footerClass = `${footerClass} justify-content-between`;
    //     }
    //   }
    //   return footerClass;
    // },

    filteredFooterData() {
      let filteredFooterData = [];
      if (this.isCheckoutFooter) {
        filteredFooterData = this.footerData.columns;
      } else {
        filteredFooterData = this.footerData.columns.slice(
          0,
          this.footerDataLength - 1,
        );
      }
      return filteredFooterData;
    },
  },
  mounted() {
    // if (this.filteredFooterData && !this.isCheckoutFooter) {
    //   const authoredSocialLinks = this.filteredFooterData[this.filteredFooterData.length - 1]
    //     .links;
    //   this.socialLinks.socialText = this.filteredFooterData[
    //     this.filteredFooterData.length - 1
    //   ].title;
    //   for (let i = 0; i < authoredSocialLinks.length; i++) {
    //     if (authoredSocialLinks[i].linkText) {
    //       this.socialLinks[authoredSocialLinks[i].linkText.toLowerCase()] =
    //         authoredSocialLinks[i].linkTo;
    //     }
    //   }
    //   this.filteredFooterData.pop();
    // }
    const footerHeight = document.getElementsByClassName('footer-section')[0]
      .offsetHeight;
    this.$root.$emit('footer-height', footerHeight);
  },
  methods: {
    toggle(event) {
      if (!this.isCheckoutFooter) {
        event.currentTarget.parentElement.classList.toggle('active');
        if (this.isTablet()) {
          if (
            event.currentTarget.children[0].classList.contains(
              'icon-chevron-down',
            )
          ) {
            event.currentTarget.children[0].classList.remove(
              'icon-chevron-down',
            );
            event.currentTarget.children[0].classList.add('icon-chevron-up');
            event.target.setAttribute('aria-expanded', true);
            event.target.setAttribute('aria-label', 'expanded');
          } else {
            event.currentTarget.children[0].classList.remove('icon-chevron-up');
            event.currentTarget.children[0].classList.add('icon-chevron-down');
            event.target.setAttribute('aria-expanded', false);
            event.target.setAttribute('aria-label', 'collapsed');
          }
        }
      }
    },
  },
};
