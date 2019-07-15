import RootService from './root-service';
import globals from '../globals';

class BundleService extends RootService {
  /**
   * Get Build Bundle Page Data
   */
  getBundlesData(requestConfig, successCallback, errorCallback, bundle) {
    // Using auto suggest service for now
    const bundleId = encodeURIComponent(bundle);
    const config = requestConfig;
    config.params = {
      bundleId,
    };
    config.url = `${globals.getRestUrl('getBundleDetail')}`;
    this.get(config, successCallback, errorCallback);
  }
  /**
   * Add Bundle To Cart
   */
  addBundleToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('addBundleToCart', 'cart')}`;
    this.post(config, successCallback, errorCallback);
  }

  getBundleCartDetail(requestConfig, successCallback, errorCallback, bundle, bundleNumber) {
    const bundleId = encodeURIComponent(bundle);
    const bundleNo = encodeURIComponent(bundleNumber);
    const config = requestConfig;
    config.params = {
      bundleId,
      bundleNo,
    };
    config.url = `${globals.getRestUrl('getBundleCartDetail', 'cart')}`;
    this.get(config, successCallback, errorCallback);
  }

  updateBundleInCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('updateBundleInCart', 'cart')}`;
    this.post(config, successCallback, errorCallback);
  }

  deleteBundle(requestConfig, successCallback, errorCallback, bundleNumber) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('deleteBundle', 'cart')}/${bundleNumber}`;
    this.delete(config, successCallback, errorCallback);
  }
}

export default BundleService;
