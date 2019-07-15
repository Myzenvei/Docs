import RootService from './root-service';
import globals from '../globals';

class ApplePayService extends RootService {
  /**
   * Validates merchant details from hybris and sends token back to frontend
   * @param  {Object} requestConfig
   * @param  {function} successCallback
   * @param  {function} errorCallback
   * @param  {String} validationURL   validationURL recieved from apple pay
   */
  validateMerchant(requestConfig, successCallback, errorCallback, validationURL) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('applepaysessiondetails', 'cart')}`;
    config.data = {
      validationURL,
    };
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Sends Apple pay response object to hybris
   * @param  {Object} requestConfig
   * @param  {function} successCallback
   * @param  {function} errorCallback
   * @param  {String} dataObj  authoreized apple response object
   */
  saveApplePayResponse(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('authorizeApplePay', 'user')}`;
    config.params = {
      cartId: globals.getCartGuid(),
    };
    this.post(config, successCallback, errorCallback);
  }
}

export default ApplePayService;
