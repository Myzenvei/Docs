import RootService from './root-service';
import globals from '../globals';

class ViewSiteContentService extends RootService {
  /* Product Category Solution */
  tabsDynamicData(requestConfig, successCallback, errorCallback, productGroupId) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('productSolution')}/${productGroupId}`;
    this.get(config, successCallback, errorCallback);
  }
  /* Products (Product Locator Service) */
  product(requestConfig, successCallback, errorCallback, categoryItemValue) {
    const config = requestConfig;
    config.url = `${globals.serviceUrls.products}${categoryItemValue}`;
    config.headers = '';
    this.get(config, successCallback, errorCallback);
  }

  /**
   * get product list values
   */

  getProductList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getProducts')}`;
    this.get(config, successCallback, errorCallback);
  }
  /* Store (Product Locator Service) */
  store(requestConfig, successCallback, errorCallback, productId, pinCode, distanceItemValue) {
    const config = requestConfig;
    config.headers = '';
    config.url = `${globals.serviceUrls.stores}&productid=${productId}&zip=${pinCode}&searchradius=${distanceItemValue}`;
    this.get(config, successCallback, errorCallback);
  }
  /* get user permission for coupon */
  getCouponPermissions(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getCouponPermission');
    this.post(config, successCallback, errorCallback);
  }
  /* save data for customer */
  saveCustomerData(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('saveCustomerData');
    this.post(config, successCallback, errorCallback);
  }
  fetchStoresWithLatLong(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('distributors')}?radius=${requestConfig.radius}&latitude=${requestConfig.latitude}&longitude=${requestConfig.longitude}&pageSize=${requestConfig.pageSize}&currentPage=${requestConfig.currentPage}&query=${requestConfig.searchText}&productCode=${requestConfig.productCode}&wishlistUid=${requestConfig.wishlistUid}`;
    this.get(config, successCallback, errorCallback);
  }
  getShoppingLists(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getAllWishLists', 'user');
    this.get(config, successCallback, errorCallback);
  }
}

export {
  ViewSiteContentService as
    default,
};
