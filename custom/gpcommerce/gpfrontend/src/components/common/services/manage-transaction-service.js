/* Services for Manage Transaction */

import RootService from './root-service';
import globals from '../globals';

class ManageTransactionService extends RootService {
  /**
   * add and update permissions
   */
  cancelOrder(requestConfig, successCallback, errorCallback, orderId) {
    // make url here with order ID
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('cancelOrder', 'user')}/${orderId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  getOrderHistoryB2B(requestConfig, successCallback, errorCallback, daterange) {
    const endpoint = `${globals.getRestUrl('getB2BOrderHistory', 'user')}/${
      globals.userInfo.unit
    }/b2b?dateRange=${daterange}`;
    const config = requestConfig;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  getOrderHistoryB2C(requestConfig, successCallback, errorCallback, daterange) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl(
      'getB2COrderHistory',
      'user',
    )}?dateRange=${daterange}`;
    this.get(config, successCallback, errorCallback);
  }

  getOrderDetailsB2C(requestConfig, successCallback, errorCallback, orderCode) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl(
      'getOrderDetails',
      'user',
    )}/${orderCode}`;
    config.headers = {
      'Cache-Control': 'no-cache',
    };
    this.get(config, successCallback, errorCallback);
  }

  getOrderDetailsB2BAdmin(
    requestConfig,
    successCallback,
    errorCallback,
    orderCode,
  ) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getOrderDetails')}/${orderCode}`;
    config.headers = {
      'Cache-Control': 'no-cache',
    };
    this.get(config, successCallback, errorCallback);
  }

  getOrderDetailsB2BBuyer(requestConfig, successCallback, errorCallback, orderCode) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getOrderDetailsBuyer', 'user')}/${orderCode}`;
    config.headers = {
      'Cache-Control': 'no-cache',
    };
    this.get(config, successCallback, errorCallback);
  }
  getOrderApprovalsB2B(
    requestConfig,
    successCallback,
    errorCallback,
  ) {
    const endpoint = `${globals.getRestUrl(
      'getB2BOrderApprovals',
      'user',
    )}`;
    const config = requestConfig;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }
  getOrderApprovalsStatus(
    requestConfig,
    successCallback,
    errorCallback,
    status,
  ) {
    const endpoint = `${globals.getRestUrl(
      'getB2BOrderApprovals',
      'user',
    )}?statuses=${status.join(',')}`;
    const config = requestConfig;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }
  getLeaseAgreement(requestConfig, successCallback, errorCallback, orderCode) {
    const config = requestConfig;
    let url = '';
    if (globals.getIsLoggedIn()) {
      url = `${globals.getRestUrl('getLease', 'user') + orderCode}`;
    } else {
      url = `${globals.getRestUrl('getLease') + orderCode}`;
    }
    config.url = url;
    this.get(config, successCallback, errorCallback);
  }
  orderStatusUpdated(
    requestConfig,
    successCallback,
    errorCallback,
    orderCode,
    status,
    comments,
  ) {
    const endpoint = `${globals.getRestUrl(
      'orderStatus',
      'user',
    )}/${orderCode}/${status}`;
    const config = requestConfig;
    config.url = endpoint;
    config.data = {
      approverComments: comments,
    };
    this.post(config, successCallback, errorCallback);
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
}

export {
  ManageTransactionService as
  default,
};
