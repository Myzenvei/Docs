import RootService from './root-service';
import globals from '../globals';

// Currently this file is not being used for checkout Services

class CheckOutService extends RootService {
  /* Full Cart Call */
  fullCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('empty', 'cart');
    config.params = {
      fields: 'FULL',
      calculationType: 'checkout',
    };
    this.get(config, successCallback, errorCallback);
  }

  /* Full Cart Call */
  getSubscriptionCart(requestConfig, successCallback, errorCallback, subscriptionCartId, calculationType) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('subscriptionCart', 'user')}/${subscriptionCartId}`;
    if (!calculationType) {
    	config.params = {
    		      fields: 'FULL',
    		      calculationType: 'checkout',
    		    };
    } else {
    	config.params = {
    		      fields: 'FULL',
    		      calculationType: 'cart',
    		    };
    }

    this.get(config, successCallback, errorCallback);
  }

  /* Shipping Address */
  getCheckoutAddress(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.headers = {
      pragma: 'no-cache',
    };
    config.url = globals.getRestUrl('checkoutGetAddress', 'user');
    this.get(config, successCallback, errorCallback);
  }

  getGuestCheckout(requestConfig, successCallback, errorCallback, guestEmail) {
    const config = requestConfig;
    config.url = globals.getRestUrl('guestemail', 'cart');
    config.params = {
      email: guestEmail,
    };
    this.put(config, successCallback, errorCallback);
  }

  getGuestAddress(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('getAddress', 'cart');
    } else {
      config.url = globals.getRestUrl('getAddress', 'subscribe');
    }
    this.get(config, successCallback, errorCallback);
  }

  /* Single and Multi Shipping Address Module */
  deleteCartItem(requestConfig, successCallback, errorCallback, itemNumber) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = `${globals.getRestUrl(
        'deleteCartEntry',
        'cart',
      )}/${itemNumber}`;
    } else {
      config.url = `${globals.getRestUrl(
        'deleteCartEntry',
        'subscribe',
      )}/${itemNumber}`;
    }
    this.delete(config, successCallback, errorCallback);
  }
  shippingRestriction(requestConfig, successCallback, errorCallback, data) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('shippingRestriction', 'cart');
    } else {
      config.url = globals.getRestUrl('shippingRestriction', 'subscribe');
    }
      if (config.params) {
          config.params.refreshCart = 'false';
      }
      else {
          config.params = {
              refreshCart: 'false',
          };
      }
    config.data = data;
    this.put(config, successCallback, errorCallback);
  }
  addAddress(requestConfig, successCallback, errorCallback, data) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('addAddress', 'cart');
    } else {
      config.url = globals.getRestUrl('addAddress', 'subscribe');
    }
    config.data = data;
    this.post(config, successCallback, errorCallback);
  }
  /* Shipping Method Related Services */
  /* Multishipping Related Service */
  getMultipleMethod(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('getMultipleShippingMethod', 'cart');
    } else {
      config.url = globals.getRestUrl('getMultipleShippingMethod', 'subscribe');
    }
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
  saveMultipleMethod(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('setMultipleShippingMethod', 'cart');
    } else {
      config.url = globals.getRestUrl('setMultipleShippingMethod', 'subscribe');
    }
    this.put(config, successCallback, errorCallback);
  }
  /* Singleshipping Related Service */
  getSingleMethod(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('getSingleShippingMethod', 'cart');
    } else {
      config.url = globals.getRestUrl('getSingleShippingMethod', 'subscribe');
    }
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
  saveSingleMethod(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
          if (config.params) {
              config.params.refreshCart = 'false';
          }
          else {
              config.params = {
                  refreshCart: 'false',
              };
          }
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('setSingleShippingMethod', 'cart');
    } else {
      config.url = globals.getRestUrl('setSingleShippingMethod', 'subscribe');
    }
    this.put(config, successCallback, errorCallback);
  }
  /* Installation Related Service */
  getInstallationDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('scheduleInstallation', 'cart');
    } else {
      config.url = globals.getRestUrl('scheduleInstallation', 'subscribe');
    }
    this.get(config, successCallback, errorCallback);
  }
  setInstallationDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('scheduleInstallation', 'cart');
    } else {
      config.url = globals.getRestUrl('scheduleInstallation', 'subscribe');
    }
    this.post(config, successCallback, errorCallback);
  }
  /* Gift Options Related services */
  saveGift(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('setGiftWrap', 'cart');
    } else {
      config.url = globals.getRestUrl('setGiftWrap', 'subscribe');
    }
    this.post(config, successCallback, errorCallback);
  }
  /* lease agreement */
  saveLeaseAgrement(requestConfig, successCallback, errorCallback, data) {
    const config = requestConfig;
    config.data = data;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('saveLeaseAgrement', 'cart');
    } else {
      config.url = globals.getRestUrl('saveLeaseAgrement', 'subscribe');
    }
    this.post(config, successCallback, errorCallback);
  }
  /* review lease agreement */
  leaseAgreement(requestConfig, successCallback, errorCallback, setLanguage) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = `${globals.getRestUrl(
        'leaseAgreement',
        'cart',
      )}/${setLanguage}`;
    } else {
      config.url = `${globals.getRestUrl(
        'leaseAgreement',
        'subscribe',
      )}/${setLanguage}`;
    }
    this.get(config, successCallback, errorCallback);
  }

  /* Payment Billing Component
  get Payment Details */
  getPaymentDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('paymentDetails', 'user');
    this.get(config, successCallback, errorCallback);
  }

  // save payment
  savePayment(requestConfig, successCallback, errorCallback, paymentId) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('paymentDetails', 'cart');
    } else {
      config.url = globals.getRestUrl('paymentDetails', 'subscribe');
    }
    config.params = {
      paymentDetailsId: paymentId,
    };
    this.put(config, successCallback, errorCallback);
  }

  /* Place order */
  placeOrder(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.params = {
        cartId: globals.getCartGuid(),
      };
    } else {
      config.params = {
        cartId: globals.getSubscribtionCartId(),
      };
    }
    config.url = globals.getRestUrl('placeOrder', 'user');
    this.post(config, successCallback, errorCallback);
  }

  /* Order Confirmation Component
  get Order Details */
  getOrderDetails(requestConfig, successCallback, errorCallback, orderCode) {
    const config = requestConfig;
    if (globals.getIsLoggedIn()) {
      config.url = `${globals.getRestUrl('getOrder', 'user')}/${orderCode}`;
    } else {
      config.url = `${globals.getRestUrl('getOrder')}/${orderCode}`;
    }
    this.get(config, successCallback, errorCallback);
  }

  /* show Lease Agreement */
  showLeaseAgreement(requestConfig, successCallback, errorCallback, orderCode) {
    const config = requestConfig;
    if (globals.getIsLoggedIn()) {
      config.url = `${globals.getRestUrl('getLease', 'user')}/${orderCode}`;
    } else {
      config.url = `${globals.getRestUrl('getLease')}/${orderCode}`;
    }
    this.get(config, successCallback, errorCallback);
  }

  paypalRequest(requestConfig, successCallback, errorCallback, type) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = `${globals.getRestUrl(
        'paypalPaymentDetails',
        'cart',
      )}?paypalType=${type}`;
    } else {
      config.url = `${globals.getRestUrl(
        'paypalPaymentDetails',
        'subscribe',
      )}?paypalType=${type}`;
    }
    this.post(config, successCallback, errorCallback);
  }

  savePaypalResponse(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    if (!globals.getIsSubscription()) {
      config.url = `${globals.getRestUrl('paypalSavePayment', 'cart')}`;
    } else {
      config.url = `${globals.getRestUrl('paypalSavePayment', 'subscribe')}`;
    }
    this.post(config, successCallback, errorCallback);
  }
}

// export default UserService;
export default CheckOutService;
