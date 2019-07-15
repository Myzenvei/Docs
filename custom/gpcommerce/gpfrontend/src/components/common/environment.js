/**
 * All environment endpoints and mappings
 * */

const environment = {
  /* Local environment */
  LOCAL: 'https://localhost:9002',

  /* Dev URLs */
  DEV_AWS: 'https://gpa-d-adm.srv.gapac.com/',
  DEV_VPN: 'http://gpa-d-www.ms.ycs.io/',

  /* Stage URLs */
  STAGE_AWS: 'https://gpa-d-adm.srv.gapac.com/',
  STAGE_VPN: 'https://gpa-s-www.ms.ycs.io/',
};

// Service urls
const serviceUrls = {
  AUTH: '/authorizationserver/oauth/token',
  REGISTER: '/users',
  FORGOT_PASSWORD: '/forgottenpasswordtokens',
  UPDATE_PASSWORD: '/forgottenpasswordtokens/updatepassword',
  SURVEY_DETAILS: '/survey',
};

const BASE_URL = environment.DEV_AWS;

// Get the Rest Url
function getbaseURL() {
  // TODO add conditional statement for environment loading
  return BASE_URL;
}

const environmentsDetails = {
  environment,
  serviceUrls,
  BASE_URL,
  getbaseURL,
};

export { environmentsDetails as default };
