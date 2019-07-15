import RootService from './root-service';
import globals from '../globals';

class GooglePayService extends RootService {
  /**
   * Recieves object from Hybris containing cart details
   * @param  {Object} requestConfig
   * @param  {function} successCallback
   * @param  {function} errorCallback
   */
  buildGooglePayRequest(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getGooglePayRequestParams', 'cart')}`;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Sends Google pay response object to hybris
   * @param  {Object } requestConfig
   * @param  {function} successCallback
   * @param  {function} errorCallback
   */

  saveGooglePayResponse(
    requestConfig,
    successCallback,
    errorCallback,
    paymentDataWsDTO,
  ) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('addGooglePayPaymentInfo', 'cart')}`;
    // config.params = {
    //   cartId: globals.getCartGuid(),
    // };
    this.post(config, successCallback, errorCallback);
  }
}

export default GooglePayService;
