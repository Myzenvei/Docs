// Global file to add Enums

const ProductAvailability = {
  OUT_OF_STOCK: 'outOfStock',
  IN_STOCK: 'inStock',
  LOW_STOCK: 'lowStock',
  OBSOLETE: 'OBSOLETE',
  COMING_SOON: 'COMING_SOON',
  NO_STOCK: 'noStock',
  ACTIVE: 'ACTIVE',
};
Object.freeze(ProductAvailability);

const subscriptionAvailability = {
  OUT_OF_STOCK: 'OutofStock',
  PAYMENT_FAILURE: 'PaymentFailure',
  ACTIVE: 'ACTIVE',
};
Object.freeze(subscriptionAvailability);

const subscriptionStatus = {
  outOfStock: 'Out of Stock',
  paymentFailure: 'Payment Failure',
  active: 'Active',
};
Object.freeze(subscriptionStatus);

const MaterialStatus = {
  ACTIVE_PRODUCT: 'ACTIVE',
};
Object.freeze(MaterialStatus);

const PdpCarouselConfig = {
  CYCLE: 'false',
  AUTO_START: 'false',
  CAROUSEL_NAME: 'pdpProductCarousel',
};
Object.freeze(PdpCarouselConfig);

const footerLinkTypes = {
  SIMPLE_TEXT: 'text',
  SIMPLE_TEL: 'tel',
  SIMPLE_MAIL: 'mailto',
};
Object.freeze(footerLinkTypes);

const duplicateMailErrorCode = {
  DUP_EMAIL_ERROR_CODE: '100',
};
Object.freeze(duplicateMailErrorCode);

const noEmailErrorCode = {
  NO_EMAIL_ERROR_CODE: '1000',
};
Object.freeze(noEmailErrorCode);

const UserRoles = {
  ADMINISTRATORS: 'administrators',
  BUYERS: 'buyers',
  ADMIN: 'b2badmingroup',
  CUSTOMER: 'b2bcustomergroup',
  DISPLAY_ADMIN: 'Admin',
  DISPLAY_BUYER: 'Buyer',
};
Object.freeze(UserRoles);

const UserDetails = {
  USER_GROUP_ID: 'ugid',
  USER_ID: 'uid',
};
Object.freeze(UserDetails);

const UnitDetails = {
  UNIT_ID: 'unitid',
};
Object.freeze(UnitDetails);

const imageFormat = {
  PRODUCT: 'product',
  THUMBNAIL: 'thumbnail',
};
Object.freeze(imageFormat);

const cartTypes = {
  FULL: 'FULL',
  DEFAULT: 'DEFAULT',
  BASIC: 'BASIC',
  SUMMARY: 'SUMMARY',
};
Object.freeze(cartTypes);

const singleDigitDate = ['01', '02', '03', '04', '05', '06', '07', '08', '09'];
Object.freeze(singleDigitDate);

const month = {
  options: [
    {
      value: '01',
      label: '01',
    },
    {
      value: '02',
      label: '02',
    },
    {
      value: '03',
      label: '03',
    },
    {
      value: '04',
      label: '04',
    },
    {
      value: '05',
      label: '05',
    },
    {
      value: '06',
      label: '06',
    },
    {
      value: '07',
      label: '07',
    },
    {
      value: '08',
      label: '08',
    },
    {
      value: '09',
      label: '09',
    },
    {
      value: '10',
      label: '10',
    },
    {
      value: '11',
      label: '11',
    },
    {
      value: '12',
      label: '12',
    },
  ],
};
Object.freeze(month);

const monthLiterals = {
  options: [
    {
      value: '01',
      label: 'January',
    },
    {
      value: '02',
      label: 'February',
    },
    {
      value: '03',
      label: 'March',
    },
    {
      value: '04',
      label: 'April',
    },
    {
      value: '05',
      label: 'May',
    },
    {
      value: '06',
      label: 'June',
    },
    {
      value: '07',
      label: 'July',
    },
    {
      value: '08',
      label: 'August',
    },
    {
      value: '09',
      label: 'September',
    },
    {
      value: '10',
      label: 'October',
    },
    {
      value: '11',
      label: 'November',
    },
    {
      value: '12',
      label: 'December',
    },
  ],
};
Object.freeze(monthLiterals);

const genderList = [
  {
    label: 'Male',
    value: 'Male',
  },
  {
    label: 'Female',
    value: 'Female',
  },
  {
    label: 'Prefer not to identify',
    value: 'PreferNotToIdentify',
  },
  {
    label: 'Select Gender',
    value: '',
  },
];
Object.freeze(genderList);
const PreferNotToIdentify = {
  otherGender: 'PreferNotToIdentify',
};
Object.freeze(PreferNotToIdentify);

const dateSuffix = {
  first: 'st',
  second: 'nd',
  third: 'rd',
  other: 'th',
};
Object.freeze(dateSuffix);

const monthCollection1 = ['01', '03', '05', '07', '08', '10', '12'];
Object.freeze(monthCollection1);

const monthCollection2 = [
  '01',
  '03',
  '04',
  '05',
  '06',
  '07',
  '08',
  '09',
  '10',
  '11',
  '12',
];
Object.freeze(monthCollection2);

const monthNames = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];
Object.freeze(monthNames);

const country = {
  options: [
    {
      label: 'US',
      value: 'US',
    },
    {
      label: 'CANADA',
      value: 'CANADA',
    },
  ],
};
Object.freeze(country);

const state = {
  options: [
    {
      label: 'Alabama',
      value: 'AL',
    },
    {
      label: 'Alaska',
      value: 'AK',
    },
    {
      label: 'Arizona',
      value: 'AZ',
    },
    {
      label: 'Arkansas',
      value: 'AR',
    },
    {
      label: 'California',
      value: 'CA',
    },
    {
      label: 'Colorado',
      value: 'CO',
    },
    {
      label: 'Connecticut',
      value: 'CT',
    },
    {
      label: 'Delaware',
      value: 'DE',
    },
    {
      label: 'District of Columbia',
      value: 'DC',
    },
    {
      label: 'Florida',
      value: 'FL',
    },
    {
      label: 'Georgia',
      value: 'GA',
    },
    {
      label: 'Hawaii',
      value: 'HI',
    },
    {
      label: 'Idaho',
      value: 'ID',
    },
    {
      label: 'Illinois',
      value: 'IL',
    },
    {
      label: 'Indiana',
      value: 'IN',
    },
    {
      label: 'Iowa',
      value: 'IA',
    },
    {
      label: 'Kansa',
      value: 'KS',
    },
    {
      label: 'Kentucky',
      value: 'KY',
    },
    {
      label: 'Lousiana',
      value: 'LA',
    },
    {
      label: 'Maine',
      value: 'ME',
    },
    {
      label: 'Maryland',
      value: 'MD',
    },
    {
      label: 'Massachusetts',
      value: 'MA',
    },
    {
      label: 'Michigan',
      value: 'MI',
    },
    {
      label: 'Minnesota',
      value: 'MN',
    },
    {
      label: 'Mississippi',
      value: 'MS',
    },
    {
      label: 'Missouri',
      value: 'MO',
    },
    {
      label: 'Montana',
      value: 'MT',
    },
    {
      label: 'Nebraska',
      value: 'NE',
    },
    {
      label: 'Nevada',
      value: 'NV',
    },
    {
      label: 'New Hampshire',
      value: 'NH',
    },
    {
      label: 'New Jersey',
      value: 'NJ',
    },
    {
      label: 'New Mexico',
      value: 'NM',
    },
    {
      label: 'New York',
      value: 'NY',
    },
    {
      label: 'North Carolina',
      value: 'NC',
    },
    {
      label: 'North Dakota',
      value: 'ND',
    },
    {
      label: 'Ohio',
      value: 'OH',
    },
    {
      label: 'Oklahoma',
      value: 'OK',
    },
    {
      label: 'Oregon',
      value: 'OR',
    },
    {
      label: 'Pennsylvania',
      value: 'PA',
    },
    {
      label: 'Rhode Island',
      value: 'RI',
    },
    {
      label: 'South Carolina',
      value: 'SC',
    },
    {
      label: 'South Dakota',
      value: 'SD',
    },
    {
      label: 'Tennessee',
      value: 'TN',
    },
    {
      label: 'Texas',
      value: 'TX',
    },
    {
      label: 'Utah',
      value: 'UT',
    },
    {
      label: 'Vermont',
      value: 'VT',
    },
    {
      label: 'Virginia',
      value: 'VA',
    },
    {
      label: 'Washington',
      value: 'WA',
    },
    {
      label: 'West Virginia',
      value: 'WV',
    },
    {
      label: 'Wisconsin',
      value: 'WI',
    },
    {
      label: 'Wyoming',
      value: 'WY',
    },
  ],
};
Object.freeze(state);

const year = {
  options: [
    {
      value: '2018',
      label: '2018',
    },
    {
      value: '2019',
      label: '2019',
    },
    {
      value: '2020',
      label: '2020',
    },
    {
      value: '2021',
      label: '2021',
    },
    {
      value: '2022',
      label: '2022',
    },
    {
      value: '2023',
      label: '2023',
    },
    {
      value: '2024',
      label: '2024',
    },
    {
      value: '2025',
      label: '2026',
    },
    {
      value: '2027',
      label: '2027',
    },
    {
      value: '2028',
      label: '2028',
    },
  ],
};
Object.freeze(year);

const socialAccountType = {
  GOOGLE: 'Google',
  FACEBOOK: 'Facebook',
};
Object.freeze(socialAccountType);

const businessUnitLevel = {
  L1: 'L1',
  L2: 'L2',
  L3: 'L3',
};
Object.freeze(businessUnitLevel);

const socialConnect = {
  password: 'password',
};
Object.freeze(socialConnect);

const countryList = {
  options: [
    {
      label: 'United States',
      value: 'US',
    },
    {
      label: 'Canada',
      value: 'CA',
    },
  ],
};
Object.freeze(countryList);

const userStates = {
  pending: 'PENDING',
  approved: 'APPROVED',
  rejected: 'REJECTED',
  underReview: 'Under Review',
  status: 'STATUS',
  l1: 'L1',
  l2: 'L2',
  l3: 'L3',
};
Object.freeze(userStates);

const cardNames = {
  visa: 'Visa',
  masterCard: 'Mastercard',
  discover: 'Discover',
  americanExpress: 'American Express',
  diner: "Diner's Club",
  googlePay: 'Google Pay',
};
Object.freeze(cardNames);

const approvalDefaultStatus = {
  pendingApproval: 'PENDING_APPROVAL',
};
Object.freeze(approvalDefaultStatus);

const approvalDetailStatus = {
  created: 'CREATED',
  rejected: 'REJECTED',
};
Object.freeze(approvalDetailStatus);
const flyoutStatus = {
  success: 'success',
  error: 'error',
  alert: 'alert',
};
Object.freeze(flyoutStatus);

const backgroundColor = {
  transparent: 'transparent',
};
Object.freeze(backgroundColor);

const myAccountAddressStatus = {
  disabled: 'DISABLED',
  active: 'ACTIVE',
  pending: 'PENDING',
  pendingbyadmin: 'PENDINGBYADMIN',
  pendingbygp: 'PENDINGBYGP',
};
Object.freeze(myAccountAddressStatus);

const otherCountry = {
  label: 'Others',
  value: 'other',
};
Object.freeze(otherCountry);

const defaultCountry = {
  label: 'United States of America',
  value: 'US',
};
Object.freeze(defaultCountry);

const defaultGender = {
  label: 'Select Gender',
  value: '',
};
Object.freeze(defaultGender);

const defaultMonth = {
  label: 'Month',
  value: '',
};
Object.freeze(defaultMonth);

const defaultDate = {
  label: 'Date',
  value: '',
};
Object.freeze(defaultDate);

const pageType = {
  cart: 'cart',
  checkout: 'checkout',
  orderConfirmation: 'orderConfirmation',
  orderDetails: 'orderDetails',
  orderApprovalDetails: 'orderApprovalDetails',
  page: 'page',
};
Object.freeze(pageType);

const order = {
  ascending: 'asc',
  descending: 'dsc',
  orderId: 'orderId',
};
Object.freeze(order);

const addToCartStatus = {
  failure: 'FAILURE',
};
Object.freeze(addToCartStatus);

const surveyQuestionType = {
  select: 'SELECT',
  radio: 'RADIO',
  textField: 'TEXT_FIELD',
};
Object.freeze(surveyQuestionType);

const discount = {
  discountZero: '0.00',
};
Object.freeze(discount);
const brandNames = {
  dixie: 'dixie',
  gppro: 'gppro',
  gpemployee: 'gpemployee',
  mardigras: 'mardigras',
  vanityfair: 'vanityfairnapkins',
  b2bwhitelabel: 'b2bwhitelabel',
  b2cwhitelabel: 'b2cwhitelabel',
  gpxpress: 'gpxpress',
  copperandcrane: 'copperandcrane',
  brawny: 'brawny',
  innovia: 'innovia',
  sparkle: 'sparkle',
};
Object.freeze(brandNames);

const leftNavigation = {
  orderHistory: 'Order History',
  yourCompany: 'YOUR COMPANY',
  yourAccount: 'YOUR ACCOUNT',
};
Object.freeze(leftNavigation);

const favorites = {
  favorites: 'favorites',
};
Object.freeze(favorites);

// brandValues will be used only to display for ui puproses. Brandnames is the value of siteId
const brandValues = {
  dixie: 'Dixie',
  gppro: 'GP Pro',
  gpemployee: 'GP Employee',
  mardigras: 'Mardigras',
  vanityfair: 'Vanity Fair',
  sparkle: 'sparkle',
  brawny: 'brawny',
};
Object.freeze(brandValues);

const httpStatusCodes = {
  unauthorized: 401,
};
Object.freeze(httpStatusCodes);
const fileDownload = {
  csv: 'Sample.csv',
};
Object.freeze(fileDownload);

const logoutFlowCookie = {
  logoutFlow: 'logoutFlow',
  emailUpdate: 'emailUpdate',
  socialConnect: 'socialConnect',
  socialDisconnect: 'socialDisconnect',
};
Object.freeze(logoutFlowCookie);

const booleanFlags = {
  isShippingAddress: 'isShippingAddress',
  isContactUs: 'isContactUs',
};
Object.freeze(booleanFlags);

const avsStatusCodes = {
  failure: 'S1000',
  success: 'S0000',
};
Object.freeze(avsStatusCodes);

const referenceTypes = {
  similar: 'SIMILAR',
  refills: 'RELATEDPRODUCTSMODAL',
};
Object.freeze(referenceTypes);

const starRatingColors = {
  fullStarColor: '#f7bd2b',
  emptyStarColor: '#ffffff',
  strokeColor: '#f7bd2b',
};
Object.freeze(starRatingColors);
const paymentTypes = {
  credit: 'Credit/Debit Card',
  paypal: 'PayPal',
  creditCard: 'CreditCard',
  applePay: 'Apple Pay',
  googlePay: 'Google Pay',
};
Object.freeze(paymentTypes);
const paymentStatus = {
  accept: 'ACCEPT',
  decline: 'DECLINE',
  error: 'ERROR',
  paymentDecision: 'paymentDecission',
};
Object.freeze(paymentStatus);
const paypalType = {
  credit: 'credit',
  nonCredit: 'non-credit',
};
Object.freeze(paypalType);
const product3DHeightBreakPoints = {
  desktop: 448,
  tablet: 288,
  mobile: 248,
};
Object.freeze(product3DHeightBreakPoints);
const facetName = {
  ratingLabel: 'Ratings',
};
Object.freeze(facetName);
const paypalResponseStatus = {
  error: 'ERROR',
};
Object.freeze(paypalResponseStatus);
const cookies = {
  soldTo: 'soldTo',
  userId: 'userId',
  pdfDetails: 'pdfDetails',
  selectedBulkProducts: 'selectedBulkProducts',
  guestListUid: 'guestListUid',
  compareCookie: 'CompareCookie',
  subscrCartId: 'subscrCartId',
};
Object.freeze(cookies);
const googleErrorResponse = {
  popUpClosedError: 'popup_closed_by_user',
};
Object.freeze(googleErrorResponse);
const keyCode = {
  enter: 13,
  up: 38,
  down: 40,
  A: 65,
  Z: 90,
  0: 48,
  9: 57,
  number0: 96,
  number9: 105,
};
Object.freeze(keyCode);
const vanityFairStarRatingColors = {
  fullStarColor: '#ba0c2f',
  emptyStarColor: '#ffffff',
  strokeColor: '#ba0c2f',
};
Object.freeze(vanityFairStarRatingColors);
const relatedCategories = {
  ACCESSORIES: 'Accessories',
  ALSOCONSIDER: 'Also Consider',
  BONUSPACK: 'Bonus Pack',
  BOWLSDISHES: 'Bowls/ Dishes',
  BRICK: 'Brick',
  CASE: 'Case',
  CHIMNEY: 'Chimney',
  COPROMOTE: 'coPromote',
  CUPSCONTAINERS: 'Cups/ Containers',
  DISPENSERKEYS: 'Dispenser Keys',
  DISPENSERPARTS: 'Dispenser Parts',
  DISPENSERS: 'Dispensers',
  DIXIEDISPENSER: 'Dixie Dispenser',
  INDIVIDUALPACK: 'Individual Pack',
  LIDS: 'Lids',
  MERCHANDISINGVEHICLE: 'Merchandising Vehicle',
  PLATES: 'Plates',
  PRODUCTFAMILY: 'Product Family',
  QSU: 'QSU',
  REFILLS: 'Refills',
  REGULARPACK: 'Regular Pack',
  TRADEUP: 'Trade Up',
  TRUCKHIGH: 'Truck High',
  MIXEDPALLETCOMPONENTS: 'Mixed Pallet Components',
  DOWNSELL: 'Downsell',
  UPSELLING: 'Upsell',
};
Object.freeze(relatedCategories);

const contentType = {
  page: 'page',
};
Object.freeze(contentType);

const generatePdf = {
  coverPageBlob: 'coverPageBlob',
  sustainabilityBlob: 'sustainabilityBlob',
  imgBlob: 'imgBlob',
  WASTE_REDUCING: 'WASTE_REDUCING',
  ECO_LOGO: 'ECO_LOGO',
  GREEN_SEAL: 'GREEN_SEAL',
  USGBA_LEED: 'USGBA_LEED',
  EPA: 'EPA',
  SFI: 'SFI',
  PROCESS_CHLORINE_FREE: 'PROCESS_CHLORINE_FREE',
  BIO_PREFERRED: 'BIO_PREFERRED',
  DFE: 'DFE',
  BPI: 'BPI',
  WHC: 'WHC',
  FSC: 'FSC',
  ECC: 'ECC',
  HOW_2_RECYCLE: 'HOW_2_RECYCLE',
  CUSTOMIZABLE: 'CUSTOMIZABLE',
  productDetails: 'Product Details',
  fullDetails: 'Full details',
  soldTo555555: '555555',
  size: 'Size',
};
Object.freeze(generatePdf);

const copperCraneStarRatingColors = {
  fullStarColor: '#B68049',
  emptyStarColor: '#ffffff',
  strokeColor: '#B68049',
};
Object.freeze(copperCraneStarRatingColors);

const pdpVariantTypes = {
  color: 'Color',
  count: 'Count',
  scent: 'Scent',
  size: 'Size',
};
Object.freeze(pdpVariantTypes);

const pdfColors = {
  barColor: '#000000',
  headlineColor: '#000000',
};
Object.freeze(pdfColors);

const supportTicketOrder = {
  ASC: 'ASC',
  DESC: 'DESC',
  ascClass: 'sorting_asc',
  descClass: 'sorting_desc',
  sortingClass: 'sorting',
  ticket: 'Ticket',
};
Object.freeze(supportTicketOrder);
// CJ Event Variables
const CJEventParms = {
  containerTagId: 29756,
  CID: 1550957,
  TYPE: 407506,
};
Object.freeze(CJEventParms);

const socialShareSize = {
  default: 32,
  small: 24,
};
Object.freeze(socialShareSize);
const placeOrder = {
  cart: 'cart',
  error: 'error',
  empty: 'empty',
};
Object.freeze(placeOrder);

const ApplePay = {
  APIVersion: 2,
  countryCode: 'US',
  currencyCode: 'USD',
  supportedNetworks: ['amex', 'discover', 'jcb', 'masterCard', 'visa'],
  merchantCapabilities: ['supports3DS'],
  requiredBillingContactFields: ['postalAddress', 'name'],
  requiredShippingContactFields: ['postalAddress', 'name', 'email', 'phone'],
  labels: {
    cartSubtotal: 'Cart Subtotal',
    shipping: 'Shipping',
    tax: 'Tax',
    paymentType: 'final',
  },
};
Object.freeze(ApplePay);

const GooglePay = {
  labels: {
    subtotalLabel: 'Subtotal',
    subtotalType: 'SUBTOTAL',
    taxLabel: 'Tax',
    taxType: 'TAX',
    shippingLabel: 'Shipping',
    lineItem: 'LINE_ITEM',
    totalLabel: 'Total',
    paymentType: 'final',
    checkoutOption: 'COMPLETE_IMMEDIATE_PURCHASE',
    allowedCardNetworks: ['AMEX', 'DISCOVER', 'MASTERCARD', 'VISA'],
  },
};
Object.freeze(GooglePay);

const brawnyStarRatingColors = {
  fullStarColor: '#C8102E',
  emptyStarColor: '#ffffff',
  strokeColor: '#C8102E',
};
Object.freeze(brawnyStarRatingColors);

const innoviaStarRatingColors = {
  fullStarColor: '#FCCC5E',
  emptyStarColor: '#ffffff',
  strokeColor: '#FCCC5E',
};
Object.freeze(innoviaStarRatingColors);

const iriProductId = {
  vanityFair: 'VANITYFAIR',
  sparkle: 'SPARKLE',
  brawny: 'BRAWNYTOWEL',
};
Object.freeze(iriProductId);

export {
  ProductAvailability,
  subscriptionAvailability,
  subscriptionStatus,
  MaterialStatus,
  PdpCarouselConfig,
  UserRoles,
  UserDetails,
  UnitDetails,
  cartTypes,
  month,
  monthLiterals,
  genderList,
  monthCollection1,
  monthCollection2,
  monthNames,
  dateSuffix,
  country,
  state,
  year,
  socialAccountType,
  businessUnitLevel,
  socialConnect,
  countryList,
  userStates,
  cardNames,
  approvalDefaultStatus,
  approvalDetailStatus,
  flyoutStatus,
  backgroundColor,
  myAccountAddressStatus,
  otherCountry,
  defaultCountry,
  defaultGender,
  defaultMonth,
  defaultDate,
  pageType,
  order,
  addToCartStatus,
  surveyQuestionType,
  discount,
  brandNames,
  leftNavigation,
  favorites,
  imageFormat,
  brandValues,
  httpStatusCodes,
  fileDownload,
  logoutFlowCookie,
  booleanFlags,
  avsStatusCodes,
  referenceTypes,
  starRatingColors,
  paymentTypes,
  paymentStatus,
  paypalType,
  product3DHeightBreakPoints,
  facetName,
  paypalResponseStatus,
  cookies,
  googleErrorResponse,
  keyCode,
  vanityFairStarRatingColors,
  relatedCategories,
  contentType,
  generatePdf,
  copperCraneStarRatingColors,
  PreferNotToIdentify,
  pdpVariantTypes,
  pdfColors,
  supportTicketOrder,
  singleDigitDate,
  CJEventParms,
  socialShareSize,
  footerLinkTypes,
  ApplePay,
  GooglePay,
  placeOrder,
  duplicateMailErrorCode,
  noEmailErrorCode,
  brawnyStarRatingColors,
  innoviaStarRatingColors,
  iriProductId,
};
