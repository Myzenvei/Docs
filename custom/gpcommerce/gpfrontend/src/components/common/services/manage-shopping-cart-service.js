import RootService from './root-service';
import globals from '../globals';


class ManageShoppingCartService extends RootService {
  getBasicCartService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('empty', 'cart');
    config.params = {
      fields: 'BASIC',
      calculationType: 'cart',
    };
      if (config.params) {
          config.params.refreshCart = 'false';
      }
      else {
          config.params = {
              refreshCart: 'false',
          };
      }
    this.get(config, successCallback, errorCallback);
  }
  getFullCartService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('empty', 'cart');
    config.params = {
      fields: 'FULL',
      calculationType: 'cart',
    };
    this.get(config, successCallback, errorCallback);
  }
  getSummaryCartService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('empty', 'cart');
    config.params = {
      fields: 'SUMMARY',
      calculationType: 'cart',
    };
    this.get(config, successCallback, errorCallback);
  }
  getDefaultCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('empty', 'cart');
    config.params = {
      fields: 'DEFAULT',
    };
      if (config.params) {
          config.params.refreshCart = 'false';
      }
      else {
          config.params = {
              refreshCart: 'false',
          };
      }
    this.get(config, successCallback, errorCallback);
  }

  createCartService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('createCart', 'user');
    this.post(config, successCallback, errorCallback);
  }
  deleteCartService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('empty', 'cart');
    this.delete(config, successCallback, errorCallback);
  }
  /* Share Cart Related */
  shareCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('shareCart', 'cart');
    this.post(config, successCallback, errorCallback);
  }
  /* Share wishlist Related */
  shareWishlist(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('shareQuickList');
    this.post(config, successCallback, errorCallback);
  }
  /* Save Cart Related */
  getAllWishLists(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getAllWishLists', 'user');
    this.get(config, successCallback, errorCallback);
  }
  createList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('createList', 'user');
    this.post(config, successCallback, errorCallback);
  }
  /* Save List */
  saveAList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('createList', 'user');
    this.put(config, successCallback, errorCallback);
  }
  saveCartEntries(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('saveListQuickOrder', 'user');
    this.put(config, successCallback, errorCallback);
  }
  /* Cart Suggestion related */
  getCartSuggestions(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('suggestions', 'cart');
      if (config.params) {
          config.params.refreshCart = 'false';
      }
      else {
          config.params = {
              refreshCart: 'false',
          };
      }
    this.get(config, successCallback, errorCallback);
  }
  quickOrderSuggestions(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('quickOrderSuggestions');
    this.post(config, successCallback, errorCallback);
  }
  /* Gift Related */
  giftDetails(requestConfig, successCallback, errorCallback, giftDetailsCode) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('pdpProductsUrl')}/${giftDetailsCode}`;
    config.params = {
      fields: 'FULL',
    };
    this.get(config, successCallback, errorCallback);
  }
  /* Voucher Related */
  applyVoucher(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('vouchers', 'cart');
    } else {
      config.url = globals.getRestUrl('vouchers', 'subscribe');
    }
    this.post(config, successCallback, errorCallback);
  }
  deleteVoucher(requestConfig, successCallback, errorCallback, code) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
     config.url = `${globals.getRestUrl('vouchers', 'cart')}/${code}`;
    } else {
      config.url = `${globals.getRestUrl('vouchers', 'subscribe')}/${code}`;
    }
    this.delete(config, successCallback, errorCallback);
  }
  /* Installation Realted  */
  addInstallation(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('addProductToCart', 'cart');
    this.post(config, successCallback, errorCallback);
  }
  updateInstallation(requestConfig, successCallback, errorCallback, parentProductEntry) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('editCartEntry', 'cart')}/${parentProductEntry}`;
    this.put(config, successCallback, errorCallback);
  }
  installationDetails(requestConfig, successCallback, errorCallback, installationCode) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('pdpProductsUrl')}/${installationCode}`;
    config.params = {
      fields: 'FULL',
    };
    this.get(config, successCallback, errorCallback);
  }
  /* Cart Tile Related */
  /* editCartItem, removeInstallation, editGift are same */
  deleteCartItem(requestConfig, successCallback, errorCallback, itemNumber) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('deleteCartEntry', 'cart')}/${itemNumber}`;
    this.delete(config, successCallback, errorCallback);
  }
  editCartItem(requestConfig, successCallback, errorCallback, itemNumber) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('editCartEntry', 'cart')}/${itemNumber}`;
    this.put(config, successCallback, errorCallback);
  }
  removeInstallation(requestConfig, successCallback, errorCallback, entryNumber) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('editCartEntry', 'cart')}/${entryNumber}`;
    this.put(config, successCallback, errorCallback);
  }
  editGift(requestConfig, successCallback, errorCallback, entryNumber) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('editCartEntry', 'cart')}/${entryNumber}`;
    this.put(config, successCallback, errorCallback);
  }

  /* Service called when checkout button is pressed */
  validateCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (config.params) {
        config.params.refreshCart = 'false';
    }
    else {
        config.params = {
            refreshCart: 'false',
        };
    }
    config.url = globals.getRestUrl('validateCart', 'cart');
    this.get(config, successCallback, errorCallback);
  }
  /* Service called when order sample button is pressed in gp xpress */
  validateGPXpressCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('gpxpressCheckout', 'cart');
    this.post(config, successCallback, errorCallback);
  }
  /* Add Precurated List to Cart */
  addPrecuratedListToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('savePrecuratedLists', 'user');
    this.put(config, successCallback, errorCallback);
  }
  /* Service called when validate cart is successful and to check incompatible products */
  validateIncompatibleProducts(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
      if (config.params) {
          config.params.refreshCart = 'false';
      }
      else {
          config.params = {
              refreshCart: 'false',
          };
      }
    config.url = globals.getRestUrl('checkIncompatibleProducts', 'cart');
    this.get(config, successCallback, errorCallback);
  }

  /* Save multiple entries to List */
  saveProductsToList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('saveMultipleLists', 'user');
    this.put(config, successCallback, errorCallback);
  }
}
export {
  ManageShoppingCartService as
  default,
};
