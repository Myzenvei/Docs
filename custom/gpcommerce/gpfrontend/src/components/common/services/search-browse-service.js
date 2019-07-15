/* Services for Search & Browse */

import RootService from './root-service';
import globals from '../globals';

class SearchBrowseService extends RootService {
  /* Notify related services  */
  notifyMe(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('notifyMe')}`;
    config.url = endpoint;
    this.patch(config, successCallback, errorCallback);
  }

  /* Compare Results services  */
  getCompareResults(requestConfig, successCallback, errorCallback, query, pageType) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('compareUrl')}${query}&pageType=${pageType}&fields=FULL`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /* Autosuggest related services  */
  getAutosuggest(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('autoComplete')}`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /* GPPRO Autosuggest related services  */
  getContentAutosuggest(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    // Passing end point('https://api-eu1.cludo.com/api/v3/553/2504/Autocomplete') through properties file from hybris.
    this.get(config, successCallback, errorCallback);
  }

  getContentSearch(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    // Passing end point('https://api-eu1.cludo.com/api/v3/553/2504/search') through properties file from hybris.
    this.post(config, successCallback, errorCallback);
  }
  /* Categories related services  */
  getCategories(requestConfig, successCallback, errorCallback, productCatalog, rootCategory) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl(
      'allCategoryUrl',
    )}/${productCatalog}/Online/categories/${rootCategory}`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /* Search Results related services  */
  getSearchResults(requestConfig, successCallback, errorCallback, query, userId) {
    const config = requestConfig;
    const facetUrl = 'fields=FULL';
    if (query.indexOf('?') >= 0) {
      config.url = `${globals.getRestUrl('searchUrl') + query}&${facetUrl}`;
    } else {
      config.url = `${globals.getRestUrl('searchUrl') + query}?${facetUrl}`;
    }
    if (userId) {
      config.params = {
        userId,
      };
    }
    this.get(config, successCallback, errorCallback);
  }

  /* Add To Cart related services  */
  addToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('addProductToCart', 'cart')}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /* Save List */
  saveAList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('createList', 'user');
    this.put(config, successCallback, errorCallback);
  }

  /* Shopping List */
  getShoppingLists(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getAllWishLists', 'user');
    this.get(config, successCallback, errorCallback);
  }

  /* delete Item from Favorites List */
  deleteCartItem(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('deleteListEntry', 'user');
    this.delete(config, successCallback, errorCallback);
  }

  /* Add Bulk Entries to Cart */
  addBulkToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('bulkAddToCart', 'cart');
    this.post(config, successCallback, errorCallback);
  }

  /* Bulk Share Item */
  bulkShareItem(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('shareProduct');
    this.post(config, successCallback, errorCallback);
  }

  getCrossReferenceSearch(requestConfig, successCallback, errorCallback, params) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('crossReferenceSearch')}${params.selectedCategory}`;
    if (params.selectedSubCategory) {
      config.url += `/${params.selectedSubCategory}`;
    }
    this.get(config, successCallback, errorCallback);
  }

  crossReferenceSearchByText(requestConfig, successCallback, errorCallback, params) {
    const config = requestConfig;
    config.url = globals.getRestUrl('crossReferenceSearchByText');
    config.url = config.url.replace('{query}', params.searchTerm).replace('{site}', globals.getSiteId());
    this.get(config, successCallback, errorCallback);
  }
}

export {
  SearchBrowseService as
  default
};
