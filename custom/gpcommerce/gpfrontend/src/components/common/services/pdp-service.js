import RootService from './root-service';
import globals from '../globals';

class PdpService extends RootService {
  /* Product Info */
  getProductData(requestConfig, successCallback, errorCallback, productId, userId) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('pdpProductsUrl')}/${productId}`;
    if (userId) {
      config.params = {
        fields: 'FULL',
        userId,
      };
    } else {
      config.params = {
        fields: 'FULL',
      };
    }
    this.get(config, successCallback, errorCallback);
  }
  addProductToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('addProductToCart', 'cart');
    this.post(config, successCallback, errorCallback);
  }
  subscribeProductToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('subscribeProductToCart', 'cart');
    this.post(config, successCallback, errorCallback);
  }
  getSubscriptionFrequency(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getSubscriptionFrequency', 'cart');
    this.get(config, successCallback, errorCallback);
  }
  addRefillsProducts(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('refillsUrl', 'cart');
    this.post(config, successCallback, errorCallback);
  }
  /* Share Item */
  shareItem(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('shareProduct');
    this.post(config, successCallback, errorCallback);
  }
  /* Common service for upsell, crossell and related products */
  getProductsData(requestConfig, successCallback, errorCallback, productId) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getProductsUrl')}/${productId}/references`;
    this.get(config, successCallback, errorCallback);
  }
  /* Service for upsell, crossell and similar products in Related Products Tab in sections */
  getRelatedProductsData(requestConfig, successCallback, errorCallback, productId) {
    const config = requestConfig;
    if (globals.siteConfig.isRelatedEnabled) {
      config.url = `${globals.getRestUrl('getProductsUrl')}/${productId}/relatedProducts`;
    }
    this.get(config, successCallback, errorCallback);
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
  /* Share Resource */
  shareResource(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('shareResource');
    this.post(config, successCallback, errorCallback);
  }
  /* download Images in zip for products */
  getImagesInZipFormat(requestConfig, successCallback, errorCallback, productId) {
    const config = requestConfig;
    config.responseType = 'arraybuffer';
    config.url = `${globals.getRestUrl('pdpProductsUrl')}/${productId}/exportimage`;
    this.get(config, successCallback, errorCallback);
  }

  /* save pdf details */
  savePdfDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('savePdfDetails', 'user')}`;
    this.post(config, successCallback, errorCallback);
  }

  /* get saved pdf details */
  getSavedPdfDetails(requestConfig, successCallback, errorCallback, certificationsName) {
    const config = requestConfig;
    config.headers = {
      pragma: 'no-cache',
    };
    config.url = `${globals.getRestUrl('savePdfDetails', 'user')}?certificationCodes=${certificationsName}`;
    this.get(config, successCallback, errorCallback);
  }

  /* Full Cart Call */
  getSubscriptionCart(requestConfig, successCallback, errorCallback, subscriptionCartId) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('subscriptionCart', 'user')}/${subscriptionCartId}`;
    config.params = {
      fields: 'FULL',
      calculationType: 'cart',
    };
    this.get(config, successCallback, errorCallback);
  }
}
export {
  PdpService as
  default,
};
