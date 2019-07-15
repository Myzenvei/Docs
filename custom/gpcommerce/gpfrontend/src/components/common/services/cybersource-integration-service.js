/* Services for Cyber Source Payment Gateway Integration */

import RootService from './root-service';
import globals from '../globals';

class CyberSourceIntegrationService extends RootService {
  /**
   * Get signature service
   * @param  {object}   header header required fields
   * @param  {object}   body   request body data
   * @param  {Function} cb     callback function
   * @return {object}          null
   */
  getSignatureService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getSignature');
    // TODO validate header data as per the service
    // TODO validate body data as per the service

    this.post(config, successCallback, errorCallback);
  }

  /**
   * Get Address service
   * @return {object}     null
   */
  getAddressService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.headers = {
      pragma: 'no-cache',
    };
    config.url = globals.getRestUrl('fetchAddress');
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Get Countries
   * @return {object}     null
   */
  getCountries(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getCountry');
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Get expiry year upto next 10 years
   * @return {object}     null
   */
  getExpiryYears(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getExpiryYears');
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Get States
   * @return {object}     null
   */
  getStates(requestConfig, successCallback, errorCallback, countryCode) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getStates')}/${countryCode}`;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Get All Saved Payment / Card Details service
   * @return {object}     null
   */
  fetchSavedCardsService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.headers = {
      pragma: 'no-cache',
    };
    config.url = globals.getRestUrl('fetchSavedCards');
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Get Saved Payment / Card Details service
   * @return {object}     null
   */
  fetchSavedCardDetails(requestConfig, successCallback, errorCallback, unitId) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('fetchSavedCardDetails')}/${unitId}`;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Get Saved Payment / Card Details service
   * @return {object}     null
   */
  deleteSavedCardDetails(requestConfig, successCallback, errorCallback, unitId) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('fetchSavedCardDetails')}/${unitId}`;
    this.delete(config, successCallback, errorCallback);
  }

  /**
   * Save card details service
   * @param  {object}   header header required fields
   * @param  {object}   body   request body data
   * @param  {Function} cb     callback function
   * @return {object}          null
   */
  saveCardDetailsService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const cardDetailsHeader = {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    };
    config.headers = cardDetailsHeader;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('saveCardGuest', 'cart');
    } else {
      config.url = globals.getRestUrl('saveCardGuest', 'subscribe');
    }
    // config.url = globals.getRestUrl('saveCardDetails');

    // TODO validate header data as per the service
    // TODO validate body data as per the service
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Edit saved card details service
   * @param  {object}   header header required fields
   * @param  {object}   body   request body data
   * @param  {Function} cb     callback function
   * @return {object}          null
   */
  editCardDetailsService(requestConfig, successCallback, errorCallback, savedPaymentId) {
    const config = requestConfig;
    const cardDetailsHeader = {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    };
    config.headers = cardDetailsHeader;
    config.url = `${globals.getRestUrl('editCardDetails')}/${savedPaymentId}`;

    // TODO validate header data as per the service
    // TODO validate body data as per the service
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Save signature service for guest user
   * @param  {object}   header header required fields
   * @param  {object}   body   request body data
   * @param  {Function} cb     callback function
   * @return {object}          null
   */
  saveCardDetailsServiceGuest(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const cardDetailsHeader = {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    };
    config.headers = cardDetailsHeader;
    if (!globals.getIsSubscription()) {
      config.url = globals.getRestUrl('saveCardGuest', 'cart');
    } else {
      config.url = globals.getRestUrl('saveCardGuest', 'subscribe');
    }

    // TODO validate header data as per the service
    // TODO validate body data as per the service

    this.post(config, successCallback, errorCallback);
  }
}

export {
  CyberSourceIntegrationService as
  default,
};
