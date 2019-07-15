/* Services for Manage Profile Shopping List Service */

import RootService from './root-service';
import globals from '../globals';

class ManageProfileShoppingListService extends RootService {
  /**
   * Get addresses data service
   * @param {Function} cb callback function
   * @return {object}     null
   */
  getAddresses(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = globals.getRestUrl('getAddress', 'user');
    config.url = endpoint;
    config.headers = {
      pragma: 'no-cache',
    };
    // TODO: Set Headers to the Root Service
    // const headers = globals.getHeaders();
    this.get(config, successCallback, errorCallback);
  }
  createShippingAddress(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = globals.getRestUrl('getAddress', 'user');
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }
  updateAddress(requestConfig, successCallback, errorCallback, id) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('updateAddress', 'user')}/${id}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }
  /**
   * Update address data service
   * @param {id} String id of the address to be updated
   * @param {Function} cb callback function
   * @return {object}     null
   */
  deleteAddress(requestConfig, successCallback, errorCallback, id) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('getAddress', 'user')}/${id}
    `;
    // TODO: Set Headers to the Root Service
    // const headers = globals.getHeaders();
    config.url = endpoint;
    this.delete(config, successCallback, errorCallback);
  }

  /**
   * Enable address
   * @param {enableConfig}  Object with address id and unit id
   * @param {*} cb callback function
   */
  toggleUserAddress(requestConfig, successCallback, errorCallback, addConfig) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('getAddress', 'user')}/${addConfig.addId}
    `;
    config.url = endpoint;
    config.data = addConfig.payload;
    this.patch(config, successCallback, errorCallback);
  }
  /* Change Password */
  updateProfilePassword(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('updateProfilePassword', 'user')}/`;
    this.put(config, successCallback, errorCallback);
  }
  /* updateTaxExemption */
  updateTaxExemption(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.headers = {
      contentType: 'multipart/form-data',
    };
    config.url = `${globals.getRestUrl('uploadTaxExemption', 'user')}`;
    this.post(config, successCallback, errorCallback);
  }
  /* Disconnect Social Account */
  disconnectSocialAccount(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('socialDisconnect', 'user');
    this.post(config, successCallback, errorCallback);
  }
  /* Share List */
  shareList(requestConfig, successCallback, errorCallback, wishlistId, isPdf) {
    const config = requestConfig;
    /* Share list call based on whether pdf share is enabled */
    if (!isPdf) {
      config.url = `${globals.getRestUrl('createList', 'user')}/${wishlistId}/sharewishlist`;
    } else {
      config.url = `${globals.getRestUrl('createList', 'user')}/${wishlistId}/sharelist`;
    }
    this.post(config, successCallback, errorCallback);
  }
  /* Edit List Name */
  editListName(requestConfig, successCallback, errorCallback, listName) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('updateWishlistName', 'user')}/${listName}`;
    this.post(config, successCallback, errorCallback);
  }
  /* Create List */
  createList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('createList', 'user');
    this.post(config, successCallback, errorCallback);
  }
  /* Save List */
  saveAList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('createList', 'user');
    this.put(config, successCallback, errorCallback);
  }
  /* Shopping List */
  getShoppingLists(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getAllWishLists', 'user');
    this.get(config, successCallback, errorCallback);
  }
  /* get Prcurated List Details */
  getPrecuratedListDetails(requestConfig, successCallback, errorCallback, fromPdf) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getPrecuratedLists', 'user');
    config.url += `/${encodeURI(requestConfig.data.listName)}`;
    config.url = !fromPdf ? config.url : `${config.url}&pdfImage=true`;
    this.get(config, successCallback, errorCallback);
  }
  /* get Shared List Details */
  getSharedListDetails(requestConfig, successCallback, errorCallback, fromPdf) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getsharedlist', 'user');
    config.url += `/${encodeURI(requestConfig.data.listName)}`;
    config.url = !fromPdf ? config.url : `${config.url}&pdfImage=true`;
    this.get(config, successCallback, errorCallback);
  }
  /* get Favorites List Details */
  getFavoritesListDetails(requestConfig, successCallback, errorCallback, fromPdf) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('createList', 'user')}/favorites`;
    config.url = !fromPdf ? config.url : `${config.url}&pdfImage=true`;
    this.get(config, successCallback, errorCallback);
  }
  /* get List Details */
  getListDetails(requestConfig, successCallback, errorCallback, listName, fromPdf, isShareList) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('createList', 'user')}/${listName}/?fields=FULL`;
    config.url = !fromPdf ? config.url : `${config.url}&pdfImage=true`;
    // isShareList param is added when user navigates to list page via share list pdf email
    if (isShareList) {
      config.params = {
        isShareList,
      };
    }
    this.get(config, successCallback, errorCallback);
  }
  /* Add List to Cart */
  addListToCart(requestConfig, successCallback, errorCallback, listName) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('addListToCart', 'cart')}/${listName}`;
    this.post(config, successCallback, errorCallback);
  }
  /* Add Product to Cart */
  addProductToCart(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('addProductToCart', 'cart');
    this.post(config, successCallback, errorCallback);
  }
  /* Add Product to List */
  addProductToList(requestConfig, successCallback, errorCallback, listName) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('createList', 'user')}/${listName}/additembyproductid`;
    this.put(config, successCallback, errorCallback);
  }
  /* Update Quantity */
  updateQuantity(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('updateQuantity', 'user');
    this.post(config, successCallback, errorCallback);
  }

  /* Delete item from cart */
  deleteCartItem(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('deleteListEntry', 'user');
    this.delete(config, successCallback, errorCallback);
  }
  /* get User Details */
  getUserDetails(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.headers = {
      pragma: 'no-cache',
    };
    config.url = globals.getRestUrl('empty', 'user');
    this.get(config, successCallback, errorCallback);
  }
  /* get Tax Exemption Details */
  getTaxExemptionStatus(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('getTaxExemption', 'user');
    this.get(config, successCallback, errorCallback);
  }
  /* Save Profile Information */
  connectSocialAccount(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('socialConnect', 'user');
    this.post(config, successCallback, errorCallback);
  }
  /* Save Profile Information */
  saveProfileInformation(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('updatePersonalDetails', 'user');
    this.post(config, successCallback, errorCallback);
  }
  /* Delete List */
  deleteList(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('deleteList', 'user');
    this.delete(config, successCallback, errorCallback);
  }
  /* Communication Preferences */
  getCommunicationPreferences(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.headers = {
      pragma: 'no-cache',
    };
    config.url = globals.getRestUrl('marketingPreferences', 'user');
    this.get(config, successCallback, errorCallback);
  }
  /* Update Preferences */
  updatePreferences(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('updateMarketingPreferences', 'user');
    this.post(config, successCallback, errorCallback);
  }
  passwordVerification(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = globals.getRestUrl('updatePersonalDetails', 'user');
    this.post(config, successCallback, errorCallback);
  }
  /* download Images in zip for products */
  getImagesInZipFormat(requestConfig, successCallback, errorCallback, wishlistId) {
    const config = requestConfig;
    config.responseType = 'arraybuffer';
    config.url = `${globals.getRestUrl('createList', 'user')}/${wishlistId}/exportimage`;
    this.get(config, successCallback, errorCallback);
  }
  /* add custom attributes to list*/
  addCustomAttributes(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('addCustomAttributes', 'user')}`;
    this.put(config, successCallback, errorCallback);
  }
  /* save custom attributes */
  saveCustomAttributes(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('saveCustomAttributes', 'user')}`;
    this.put(config, successCallback, errorCallback);
  }
  /* download Images in zip for products */
  getPdpListData(requestConfig, successCallback, errorCallback, productIds) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('listPdpFormatDataUrl')}/${productIds}`;
    config.params = {
      fields: 'FULL',
    };
    this.get(config, successCallback, errorCallback);
  }
  /* download Excel file for products */
  getExcelFile(requestConfig, successCallback, errorCallback, wishlistId) {
    const config = requestConfig;
    config.responseType = 'arraybuffer';
    config.url = `${globals.getRestUrl('downloadExcel', 'user')}/${wishlistId}`;
    this.get(config, successCallback, errorCallback);
  }
  /* remove category from list details */
  removeCategory(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('removeCategory', 'user')}`;
    this.delete(config, successCallback, errorCallback);
  }
  /* get support tickets for user B2C */
  getSupportTicketsB2C(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getSupportTicketsB2C')}`;
    this.get(config, successCallback, errorCallback);
  }
  /* get support tickets for user B2B */
  getSupportTicketsB2C(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getSupportTicketsB2B')}/${
      globals.userInfo.unit
    }/getTicketDetails`;
    this.get(config, successCallback, errorCallback);
  }
  /* get support tickets after sorting */
  sortSupportTicketData(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('getSupportTicketsB2B')}/${
      globals.userInfo.unit
    }/getTicketDetails`;
    this.get(config, successCallback, errorCallback);
  }
}

export { ManageProfileShoppingListService as default };
