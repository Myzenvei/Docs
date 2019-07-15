import RootService from './root-service';
import globals from '../globals';

class RegistrationLoginService extends RootService {
  /* Company Details related services */
  getSurveyDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('surveyDetails');
    this.get(config, successCallback, errorCallback);
  }
  postCompanyDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('surveyDetails');
    this.post(config, successCallback, errorCallback);
  }
  getRegions(requestConfig, successCallback, errorCallback, isoCode) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('regions')}/${isoCode}`;
    this.get(config, successCallback, errorCallback);
  }
  /* Create Account */
  registerUser(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('register');
    this.post(config, successCallback, errorCallback);
  }
  /* Forgot Password */
  forgotPassword(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('forgotPassword');
    this.post(config, successCallback, errorCallback);
  }
  /* Update Password */
  updatePassword(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('updatePassword');
    this.post(config, successCallback, errorCallback);
  }
  getKochAuthToken(requestConfig, successCallback, errorCallback, kochCode) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('kochAuthToken')}/${kochCode}`;
    this.post(config, successCallback, errorCallback);
  }
}
export default RegistrationLoginService;
