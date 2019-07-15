import RootService from './root-service';
import globals from '../globals';

class CommonService extends RootService {
  applicationAuthService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('auth');
    config.params = globals.getApplicationParams();
    // In case you want to change the headers
    const headers = {
      Authorization: '',
    };
    config.headers = headers;
    this.post(config, successCallback, errorCallback);
  }
  getTermsAndCondition(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('termsCondition');
    this.get(config, successCallback, errorCallback);
  }
  getRegions(requestConfig, successCallback, errorCallback, isoCode, isContactUs) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('regions')}/${isoCode}`;
    if (isContactUs) {
      config.params = {
        contactUs: true,
      };
    }
    this.get(config, successCallback, errorCallback);
  }
  getCountries(requestConfig, successCallback, errorCallback, isShippingAddress) {
    const config = requestConfig;
    if (isShippingAddress) {
      config.url = `${globals.getRestUrl('getShippingCountry')}`;
    } else {
      config.url = `${globals.getRestUrl('getCountry')}`;
    }
    this.get(config, successCallback, errorCallback);
  }
  addressVerification(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('addressVerification');
    this.post(config, successCallback, errorCallback);
  }
  deleteAddress(requestConfig, successCallback, errorCallback, addressId) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getAddress', 'user')}/${addressId}`;
    this.delete(config, successCallback, errorCallback);
  }
}
export { CommonService as default };
