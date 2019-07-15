import RootService from './root-service';
import globals from '../globals';

class ManageSubscriptionService extends RootService {
  /** Get subscription Details */
  getSubscriptionDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = globals.getRestUrl('getSubscriptionDetails', 'user');
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }
  saveSubscriptionInformation(
    requestConfig,
    successCallback,
    errorCallback,
    subscrId,
  ) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl(
      'subscriptionCart',
      'user',
    )}/${subscrId}/updatesubscription`;
    this.post(config, successCallback, errorCallback);
  }

  /** Cancel Subscription */
  cancelSubscription(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl(
      'cancelSubscription',
      'user',
    )}/cancelsubscription`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }
  placeSubscriptionOrder(
    requestConfig,
    successCallback,
    errorCallback,
    subscrId,
  ) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl(
      'subscriptionCart',
      'user',
    )}/${subscrId}/placeorder`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }
}

export { ManageSubscriptionService as default };
