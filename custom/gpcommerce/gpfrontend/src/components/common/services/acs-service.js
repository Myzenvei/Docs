import RootService from './root-service';
import globals from '../globals';

class AcsService extends RootService {
  /* create Service Ticket */
  createServiceTicket(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('createContactUsTicket');
    this.post(config, successCallback, errorCallback);
  }
  /* submit attachments of Service Ticket */
  submitAttachments(requestConfig, successCallback, errorCallback, ticketNumber) {
    const config = requestConfig;
    config.headers = {
      contentType: 'multipart/form-data',
    };
    config.url = globals.getRestUrl('submitContactUsAttachments');
    config.params = {
      ticketNumber,
    };
    this.post(config, successCallback, errorCallback);
  }
  /* get topic of inquiry */
  getTopicOfInquiry(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getTopicOfInquiry');
    this.get(config, successCallback, errorCallback);
  }
  getDispenserTicketParams(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getDispenserTicketParams');
    this.get(config, successCallback, errorCallback);
  }
  submitDispenserTicket(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('submitDispenserTicket');
    this.post(config, successCallback, errorCallback);
  }
}

// export default AcsService;
export {
  AcsService as
    default,
};
