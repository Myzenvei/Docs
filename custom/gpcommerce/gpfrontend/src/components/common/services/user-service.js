import RootService from './root-service';
import globals from '../globals';

class UserService extends RootService {
  // constructor(headers) {
  //   super(headers);
  // }

  /**
   * User login service
   * @param  {object}   header header required fields
   * @param  {object}   body   request body data
   * @param  {Function} cb     callback function
   * @return {object}          null
   */
  loginService(header, body, cb) {
    const endpoint = globals.getRestUrl('register');
    console.info('endpoint', environmentsDetails.BASE_URL);
    const headerData = header || {};
    const bodyData = body || {};

    // TODO validate header data as per the service
    // TODO validate body data as per the service

    function callback(data) {
      let isSuccess = false;
      if (data && data.access_token) {
        isSuccess = true;
      }
      cb(isSuccess, data);
    }

    this.post(endpoint, headerData, bodyData, callback);
  }

  /**
   * User Registration service service
   * @param  {object}   header header required fields
   * @param  {object}   body   request body data
   * @param  {Function} cb     callback function
   * @return {object}          null
   */
  registrationService(header, body, cb) {
    const endpoint = environmentsDetails.BASE_URL + environmentsDetails.serviceUrls.REGISTER;
    const headerData = header || {};
    const bodyData = body || {};

    // TODO validate header data as per the service
    // TODO validate body data as per the service

    function callback(data) {
      let isSuccess = false;
      if (data && data.access_token) {
        isSuccess = true;
      }
      cb(isSuccess, data);
    }

    this.post(endpoint, headerData, bodyData, callback);
  }
}

// export default UserService;
export { UserService as default };
