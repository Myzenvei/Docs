import mgSlickSlider from '../../../mardi-gras/components/common/mg-slick-slider/mg-slick-slider.vue';
import globals from '../../common/globals';

export default {
  name: 'vx-product-carousel',
  components: {
    mgSlickSlider,
  },
  props: {
    productCarouselData: {
      type: Object,
      default: () => ({
        componentHeader: 'Dixie Ultra® Interfold Napkin Dispensers',
        componentTheme: 'product-solution ruler',
        ctaText: 'Learn More',
        ctaStyle: 'btn btn-primary',
        ctaLink: 'https://www.google.com',
        noOfColumns: '5',
        isCarousel: false,
        products: [
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/l11pl.png?la=en',
          },
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/sxp9path.png?la=en',
          },
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/toc.png?la=en',
          },
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/ch56nc7.png?la=en',
          },
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/sxp9path.png?la=en',
          },
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/toc.png?la=en',
          },
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/sxp9path.png?la=en',
          },
          {
            name: 'sample',
            url: '#',
            code: 'Cost Effective',
            price: '890.78',
            imageUrl:
              'https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/cafeteria/saladcoldbar/toc.png?la=en',
          },
        ],
      }),
    },
  },
  computed: {},
  data() {
    return {
      globals,
      sliderConfig: {
        accessibility: true,
        mobileFirst: true,
        dots: true,
        arrows: false,
        slidesToShow: 1,
        slidesToScroll: 1,
        responsive: [
          {
            breakpoint: 1024,
            settings: {
              slidesToShow: 4,
              slidesToScroll: 4,
              infinite: false,
              dots: false,
              arrows: true,
              prevArrow: '<span class="icon-chevron-left slick-prev"></span>',
              nextArrow: '<span class="icon-chevron-right slick-next"></span>',
            },
          },
          {
            breakpoint: 767,
            settings: {
              slidesToShow: 4,
              slidesToScroll: 4,
              arrows: true,
              dots: false,
              prevArrow: '<span class="icon-chevron-left slick-prev"></span>',
              nextArrow: '<span class="icon-chevron-right slick-next"></span>',
            },
          },
          // You can unslick at a given breakpoint now by adding:
          // settings: "unslick"
          // instead of a settings object
        ],
      },
    };
  },
  computed: {},
  mounted() {},
  methods: {
    imageTileWidth() {
      if (this.productCarouselData.noOfColumns) {
        return `width: ${100 / this.productCarouselData.noOfColumns}%`;
      }
    },
  },
};
