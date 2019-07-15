/* Services for Manage B2B Org */

import RootService from './root-service';
import globals from '../globals';

class ManageB2bOrgService extends RootService {
  /**
   * Generate Endpoint
   * @param {string} serviceUrlKey parameter for generating endpoint
   */
  generateMb2bOrgEndpoint = serviceUrlKey => globals.getRestUrl(serviceUrlKey, 'user');

  /**
   * Business units data service
   * @param {string} unitId Unit for which details are fetched
   * @param {Function} cb callback function
   * @return {object}     null
   */
  getBusinessUnitsDataService(requestConfig, successCallback, errorCallback, unitId) {
    const config = requestConfig;
    config.url = `${this.generateMb2bOrgEndpoint('businessUnits')}/${unitId}`;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Permissions data
   */
  getPermissions(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${globals.getRestUrl('permissions', 'user')}?fields=FULL`;
    this.get(config, successCallback, errorCallback);
  }

  /* this.get(endpoint, callback);
   * Disable child unit service
   * @param {string} unitId Unit to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  disableUnitsService(requestConfig, successCallback, errorCallback, unitId, flag) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('businessUnits')}/${unitId}/${flag}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * User Groups data service
   * @return {object}     null
   */
  getUserGroups(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('getUserGroups')}`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Users data service
   * @return {object}     null
   */
  getUsersData(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('getUsers')}`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }
  /**
   * Permission Details
   */
  permissionDetailsDataService(requestConfig, successCallback, errorCallback, pCode) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('getPermissions', 'user')}/${pCode}?fields=FULL`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Disable permissions
   */
  disablePermissionService(requestConfig, successCallback, errorCallback, pid) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('disablePermissions', 'user')}/${pid}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Enable Permissions
   */

  enablePermissions(requestConfig, successCallback, errorCallback, pid) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('enablePermissions', 'user')}/${pid}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }
  /**
   * Business units list service
   * @return {object}     null
   */
  getBusinessUnits(requestConfig, successCallback, errorCallback, uid, unit) {
    const config = requestConfig;
    let endpoint = `${globals.getRestUrl('getBusinessUnits', 'user')}/`;

    if (uid) {
      endpoint += `${uid}`;
    }
    if (unit) {
      endpoint += `?unit=${unit}`;
    }
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Business unit landing page data service
   * @param {Function} cb callback function
   */
  getBusinessLandingDataService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('businessUnits')}`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * add and update permissions
   */
  updatePermissions(requestConfig, successCallback, errorCallback, isEdit) {
    const config = requestConfig;
    const permissionURL = isEdit ? 'editPermissions' : 'addPermissions';
    config.url = `${globals.getRestUrl(permissionURL, 'user')}`;

    this.post(config, successCallback, errorCallback);
  }

  /**
   * Create User service
   * @return {String}     null
   * @return {object}     null
   * @return {object}     null
   */
  createUser(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    config.url = `${this.generateMb2bOrgEndpoint('addUser')}`;
    if (userId) {
      config.url += `?userUid=${userId}`;
    }

    this.post(config, successCallback, errorCallback);
  }

  /**
   * Create User Group service
   * @return {String}     null
   * @return {object}     null
   * @return {object}     null
   */
  createUserGroup(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    let endpoint = `${this.generateMb2bOrgEndpoint('manageusergroup')}`;
    if (userId) {
      endpoint += `?userGroupUid=${userId}`;
    }
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Get b2b users
   * @param {function} cb callback function
   * @return {object}     null
   */
  getB2bUsersService(requestConfig, successCallback, errorCallback, unitId) {
    const config = requestConfig;
    let endpoint = `${this.generateMb2bOrgEndpoint('getB2bUsers')}`;
    if (unitId) {
      endpoint += `/${unitId}?fields=FULL`;
    }
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * Get b2b userGroups
   * @param {function} cb callback function
   * @return {object}     null
   */
  getB2bUserGroupsService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('getB2bUserGroups')}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Get b2b permissions with unitId
   * @param {function} cb callback function
   * @return {object}     null
   */
  getB2bPermissionsService(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('getB2bPermissions')}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Get b2b parent units
   * @param {function} cb callback function
   * @return {object}     null
   */
  getParentUnitsService(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('getB2bParentUnits')}/${userId}`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * User Groups data service
   * @return {object}     null
   */
  getUserGroupDetails(requestConfig, successCallback, errorCallback, userGroupId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('getUserGroups')}/${userGroupId}`;
    config.url = endpoint;
    this.get(config, successCallback, errorCallback);
  }

  /**
   * User data service
   * @return {object}     null
   */
  getUsersDetails(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    config.url = `${this.generateMb2bOrgEndpoint('userDetails')}/${userId}?fields=FULL`;

    this.get(config, successCallback, errorCallback);
  }

  /**
   * Add existing user to buyer or admin
   * @param {string} unitId   Unit id
   * @param {object} payload  Request data
   * @param {function} cb     callback function
   */
  addExistingUserService(requestConfig, successCallback, errorCallback, unitId, roleLabel) {
    const config = requestConfig;
    config.url = `${this.generateMb2bOrgEndpoint('addUsers')}/${unitId}/${roleLabel}`;

    this.put(config, successCallback, errorCallback);
  }

  /**
   * Create/Edit child unit
   * @param {string} unitName Unit name
   * @param {string} parentId parent id
   * @param {function} cb     callback function
   */
  createChildUnitService(requestConfig, successCallback, errorCallback, unitName, parentId, uid) {
    const config = requestConfig;
    let endpoint = `${this.generateMb2bOrgEndpoint(
      'businessUnits',
    )}?unitName=${unitName}&parentUid=${parentId}`;
    if (uid) {
      endpoint = `${endpoint}&uid=${uid}`;
    }
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }
  /**
   * Disable child units/permissions under user group
   * @param {string} unitId Unit to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  postDisableItem(requestConfig, successCallback, errorCallback, userGroupId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('disableUserGroups')}/${userGroupId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Disable user
   * @param {string} userId User to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  postDisableUser(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('disableUser')}/${userId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Enable user
   * @param {string} userId User to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  postenableUser(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('enableUser')}/${userId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Delete user group
   * @param {string} userGroupId Unit to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  postDeleteItem(requestConfig, successCallback, errorCallback, userGroupId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('removeUserGroup')}/${userGroupId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Reset Password
   * @param {string} userId User to reset password
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  resetPassword(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('resetPassword')}?resetUserId=${userId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Send Invite for existing user
   * @return {object}       null
   */
  sendUserInvite(requestConfig, successCallback, errorCallback, userId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('inviteUser')}/${userId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Delete user group
   * @param {string} userGroupId Unit to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  postDeleteInfoItem(requestConfig, successCallback, errorCallback, label, payload) {
    const config = requestConfig;
    let endpoint;
    if (label === 'parentUnit') {
      endpoint = `${this.generateMb2bOrgEndpoint('removeUnitFromUser')}/${payload.units[0].name}`;
    } else if (label === 'permission') {
      endpoint = `${this.generateMb2bOrgEndpoint('removePermissionFromUser')}/${
        payload.units[0].id
      }/${payload.units[0].name}`;
    } else {
      endpoint = `${this.generateMb2bOrgEndpoint('removeUserGroupFromUser')}/${
        payload.units[0].id
      }/${payload.units[0].name}`;
    }
    config.url = endpoint;
    config.data = payload;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Delete permission from user group
   * @param {string} userGroupId Unit to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  postDeletePermission(requestConfig, successCallback, errorCallback, sectionInfo, payload) {
    const config = requestConfig;
    let removeLabel;
    if (sectionInfo.from === 'Permissions') {
      removeLabel = 'removePermission';
    } else {
      removeLabel = 'removeUsers';
    }
    const endpoint = `${this.generateMb2bOrgEndpoint(removeLabel)}/${encodeURI(
      payload.userGroupId,
    )}/${encodeURI(payload.id)}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  /**
   * Disable child unit service
   * @param {string} unitId Unit to be disabled
   * @param {Function} cb   callback function
   * @return {object}       null
   */
  enableUser(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    config.url = `${this.generateMb2bOrgEndpoint('getUserGroups')}`;
    this.post(config, successCallback, errorCallback);
  }

  postExistingItems(requestConfig, successCallback, errorCallback, id, itemLabel) {
    const config = requestConfig;
    let endPointLabel = '';
    if (itemLabel === 'Permissions') {
      endPointLabel = 'addPermissionsToUserGroup';
    } else if (itemLabel === 'Users') {
      endPointLabel = 'addUsersToUserGroup';
    }
    const endpoint = `${this.generateMb2bOrgEndpoint(endPointLabel)}/${id}`;

    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  postExistingItemsToUser(requestConfig, successCallback, errorCallback, id, itemLabel) {
    const config = requestConfig;
    let endPointLabel = '';
    if (itemLabel === 'Permissions') {
      endPointLabel = 'addPermissionsToUser';
    } else if (itemLabel === 'User Groups') {
      endPointLabel = 'addUserGroupsToUser';
    } else {
      endPointLabel = 'addUnitsToUser';
    }
    const endpoint = `${this.generateMb2bOrgEndpoint(endPointLabel)}/${id}`;
    config.url = endpoint;
    if (itemLabel === 'Parent Units') {
      this.put(config, successCallback, errorCallback);
    } else {
      this.post(config, successCallback, errorCallback);
    }
  }

  /**
   * Remove existing user from buyer or admin
   * @param {string} unitId   Unit id
   * @param {object} payload  Request data
   * @param {function} cb     callback function
   */
  removeExistingUserService(requestConfig, successCallback, errorCallback, unitId) {
    const config = requestConfig;
    const endpoint = `${this.generateMb2bOrgEndpoint('removeUser')}/${unitId}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }

  getCompanySurveyData(requestConfig, successCallback, errorCallback) {
    const config = requestConfig;
    const endpoint = `${globals.getRestUrl('getSurveyData')}`;
    config.url = endpoint;
    this.post(config, successCallback, errorCallback);
  }
}

export { ManageB2bOrgService as default };
