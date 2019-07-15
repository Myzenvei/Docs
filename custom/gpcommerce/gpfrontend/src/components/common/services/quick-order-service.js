import RootService from './root-service';
import globals from '../globals';


class QuickOrderService extends RootService {
  /**
   * User login service
   * @param  {object}   header header required fields
   * @param  {object}   body   request body data
   * @param  {Function} cb     callback function
   * @return {object}          null
   */
  getProductMappingsService(requestConfig, successCallback, errorCallback, searchInput) {
    const config = requestConfig;
    let url = '';
    const queryParam = searchInput;
    if (globals.loggedIn) {
      url = `${globals.getRestUrl('cmirProductMapUrl')}/${globals.userInfo.unit}/${queryParam}`;
    } else {
      url = `${globals.getRestUrl('cmirProductMapUrl')}/b2bUnit/${queryParam}`;
    }
    config.url = url;
    this.get(config, successCallback, errorCallback);
  }
  postProductMappings(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.headers = {
      contentType: 'multipart/form-data',
    };
    let url = '';
    if (globals.loggedIn) {
      url = `${globals.getRestUrl('cmirProductMapUrl')}/${globals.userInfo.unit}/`;
    } else {
      url = `${globals.getRestUrl('cmirProductMapUrl')}/b2bUnit/`;
    }
    config.url = url;
    this.post(config, successCallback, errorCallback);
  }
  getProductInfoService(requestConfig, successCallback, errorCallback, queryString) {
    const config = requestConfig;
    let url = '';
    url = `${globals.getRestUrl('quickOrderSearchUrl') + queryString}&fields=FULL`;
    config.url = url;
    this.get(config, successCallback, errorCallback);
  }
  addItemsToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('addMultipleProductsToCart', 'cart');
    this.post(config, successCallback, errorCallback);
  }
}
export {
  QuickOrderService as default,
};
