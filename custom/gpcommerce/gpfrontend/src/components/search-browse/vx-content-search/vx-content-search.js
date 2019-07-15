import _ from 'lodash';
import globals from '../../common/globals';
import SearchBrowseService from '../../common/services/search-browse-service';

export default {
  name: 'vx-content-search',
  components: {},
  props: ['siteKey', 'endPoint'],
  data() {
    return {
      results: [],
      globals,
      showAutosuggestPanel: false,
      delayInterval: 500,
      searchBrowseService: new SearchBrowseService(),
      clearTextIcon: false,
      currentItem: 0,
      isKeyDownReleased: true,
    };
  },
  filters: {
    // Filter definitions
    highlight(word, query) {
      const check = new RegExp(query, 'ig');
      return word
        .toString()
        .replace(check, (matchedText, a, b) => `<strong>${matchedText}</strong>`);
    },
  },
  computed: {},
  created() {
    const self = this;
    // this will close autosuggest window if clicked outside the window.
    window.addEventListener('click', () => {
      self.showAutosuggestPanel = false;
    });
    // function to load autosuggest results.
    self.autosuggest = _.debounce((event) => {
      if (self.searchTerm.length <= 2) {
        self.results = [];
        self.showAutosuggestPanel = false;
        return false;
      }
      if (this.isKeyDownReleased) {
        // const encodedSearchTerm = encodeURIComponent(self.searchTerm);
        const requestConfig = {};
        requestConfig.params = {
          text: self.searchTerm,
        };
        requestConfig.headers = {
          Authorization: this.siteKey,
        };
        requestConfig.url = this.endPoint;
        this.searchBrowseService.getContentAutosuggest(
          requestConfig,
          this.handleSuggestResponse,
          this.handleSuggestError,
        );
      }
    }, self.delayInterval);
  },
  mounted() {
    document.addEventListener('keyup', this.nextItem);
  },
  methods: {
    // dummy function to load autosuggest which will call this from created().
    autosuggest() {},
    // function to load autosuggest response.
    handleSuggestResponse(response) {
      this.showAutosuggestPanel = true;
      this.results = response.data;
    },
    // function to load autosuggest response.
    handleSuggestError(error) {},
    // function to select value from autosuggest panel.
    copyText(result) {
      this.searchTerm = result;
      this.showAutosuggestPanel = false;
      this.searchResult();
    },
    // function to navigate to result page.
    searchResult() {
      this.showAutosuggestPanel = false;
      const query = `${this.searchTerm}&cludopage=1`;
      this.globals.navigateToUrlWithParams('cludo', query, 'cludoquery');
    },
    // function to scroll autosuggest with keyboard navigation.
    nextItem(event) {
      if (event.keyCode === 38 && this.currentItem > 0) {
        this.isKeyDownReleased = false;
        this.$nextTick(() => {
          this.searchTerm = this.results[this.currentItem];
        });
        this.currentItem = this.currentItem - 1;
      } else if (event.keyCode === 40 && this.currentItem < this.results.length) {
        this.isKeyDownReleased = false;
        this.$nextTick(() => {
          this.searchTerm = this.results[this.currentItem];
        });
        this.currentItem = this.currentItem + 1;
      } else {
        this.isKeyDownReleased = true;
      }
    },
  },
};
