import globals from '../../common/globals';
import SearchBrowseService from '../../common/services/search-browse-service';
import vxSpinner from '../../common/vx-spinner/vx-spinner.vue';

export default {
  name: 'vx-content-search-result',
  components: {
    vxSpinner
  },
  props: ['siteKey', 'endPoint', 'i18n'],
  data() {
    return {
      searchTerm: '',
      globals,
      searchBrowseService: new SearchBrowseService(),
      searchResultData: Object,
      pageNumber: 1,
    };
  },
  computed: {},
  created() {},
  mounted() {
    this.readUrl();
    this.searchResult();
  },
  updated() {
    // listen for click event of pagination elements.
    if (this.searchResultData) {
      if (document.querySelectorAll('.search-html-results ul.search_page_list').length > 0) {
        document.querySelector('.search-html-results ul.search_page_list').addEventListener('click', this.searchNavigation);
      }
      this.openLinkInNewTab();
    }
  },
  methods: {
    // Open links in newtab.
    openLinkInNewTab() {
      const links = document.querySelectorAll('a[data-cludo-result]');
      this.forEach(links, (index, item) => {
        item.target = '_blank';
      });
      const images = document.querySelectorAll('.search-result-image img');
      this.forEach(images, (index, image) => {
        if (image.src.indexOf(window.location.origin !== -1)) {

          if (image.src.split('/')[3] === 'catalog') {
            image.src = image.src.replace(window.location.origin, 'https://catalog.gppro.com');
          }
          else {
            image.src = image.src.replace(window.location.origin, 'https://www.gppro.com');
          }
        }
        else {
          image.src = `https://catalog.gppro.com${image.src}`;
        }
      });
    },
    // custom foreach method to loop over nodelist.
    forEach(array, callback, scope) {
      for (let i = 0; i < array.length; i++) {
        callback.call(scope, i, array[i]);
      }
    },
    // Navigate to current page based on user click
    searchNavigation(event) {
      const selectedPage = event.target.getAttribute('data-page');
      if (selectedPage === 'next') {
        this.pageNumber = Number(this.pageNumber) + 1;
      } else if (selectedPage === 'prev') {
        this.pageNumber = Number(this.pageNumber) - 1;
      } else {
        this.pageNumber = selectedPage;
      }
      const path = this.updateUrlString(window.location.href, 'cludopage', this.pageNumber);
      history.pushState({}, 'Product Search', path);
      this.searchResult();
    },
    // function to update url string.
    updateUrlString(uri, key, value) {
      const re = new RegExp(`([?&])${key}=.*?(&|$)`, 'i');
      const separator = uri.indexOf('?') !== -1 ? '&' : '?';
      if (uri.match(re)) {
        return uri.replace(re, `$1${key}=${value}$2`);
      }
      return `${uri + separator + key}=${value}`;
    },
    // function to read params from URL
    readUrl() {
      this.searchTerm = decodeURIComponent(this.getUrlParam('cludoquery', 'Empty'));
      this.pageNumber = this.getUrlParam('cludopage', 'Empty');
    },
    // function to get param
    getUrlParam(parameter, defaultvalue) {
      let urlparameter = defaultvalue;
      if (window.location.href.indexOf(parameter) > -1) {
        urlparameter = this.getUrlVars()[parameter];
      }
      return urlparameter;
    },
    // function to break url params.
    getUrlVars() {
      const vars = {};
      const parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, (m, key, value) => {
        vars[key] = value;
      });
      return vars;
    },
    // function to create search request
    searchResult() {
      const requestObjParams = {
        ResponseType: 'JsonHtml',
        Template: 'SearchContent',
        facets: {
          Category: [],
        },
        filters: {},
        page: this.pageNumber,
        query: this.searchTerm,
        text: '',
        sort: {},
        rangeFacets: {},
        perPage: null,
        applyMultiLevelFacets: true,
        overlay: 'StandardInlineImagesI18N',
      };
      const requestConfig = {};
      requestConfig.headers = {
        Authorization: this.siteKey,
      };
      requestConfig.url = this.endPoint;
      requestConfig.data = requestObjParams;
      this.$refs.spinner.showSpinner();
      this.searchBrowseService.getContentSearch(
        requestConfig,
        this.handleContentSearchResponse,
        this.handleContentSearchErrorResponse,
      );
    },
    // function to handle search result response.
    handleContentSearchResponse(response) {
      this.$refs.spinner.hideSpinner();
      this.searchResultData = response.data;
    },
    // function to handle search result error.
    handleContentSearchErrorResponse(error) {
      this.$refs.spinner.hideSpinner();
    },
  },
};
