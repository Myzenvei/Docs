const common = {
  address: {
    firstName: 'First Name',
    lastName: 'Last Name',
    phoneNumber: 'Phone Number',
    country: 'Country',
    companyName: 'Company Name',
    addressHeader: 'Ship to an Address',
    b2caddressLine1: 'Shipping Address 1',
    b2caddressLine2: 'Shipping Address 2 (optional)',
    b2baddressLine1: 'Shipping Address 1',
    b2baddressLine2: 'Shipping Address 2 (optional)',
    city: 'City',
    state: 'State',
    zipcode: 'Zip/Postal Code',
    palletShipments: 'This location accepts pallet shipments',
    useAddress: 'SAVE',
    addressVerificationHeading: 'Address Verification',
    currentAddressHeading: 'Current Address',
    verifiedAddressHeading: 'Verified Address',
    useCurrentAddress: 'Use Current Address',
    useVerifiedAddress: 'Use Verified Address',
  },
  headerSection: {
    hiText: 'Hi, ',
    loginSectionTitle: 'My Account',
    quickOrder: 'Quick Order',
    cartItemsText: 'Items in cart ',
    loginHeading: 'Login',
    iconMenuTitle: 'Menu',
    iconUserTitle: 'My Account',
    iconQuickOrderTitle: 'Quick Order',
    iconCartTitle: 'Cart',
    iconSearchTitle: 'Search',
    myLists: 'My List',
    myFav: 'My Favorites',
    cartWith: 'Cart with ',
    items: ' items',
  },
  headerData: {
    headerOptions: {
      brandLogo: 'images/logo.svg',
      includeMiniCart: true,
      includeSearchBox: true,
      isCheckout: false,
      checkoutMenu: {
        title: 'Checkout Nav Node',
        menuOptions: [
          {
            option: 'Help',
            optionLink: '#',
            external: false,
          },
        ],
      },
    },
    findMenu: {
      option: 'Find a Store',
      optionLink: '#/findStore',
      external: false,
    },
    loginMenu: {
      title: 'Login Nav Node',
      menuOptions: [
        {
          option: 'Login',
          optionLink: '/login',
          external: false,
        },
        {
          option: 'Create Account',
          optionLink: '/register',
          external: false,
        },
        {
          option: 'Help',
          optionLink: '#',
          external: false,
        },
      ],
    },
    navMenu: [
      {
        primary: 'Products',
        primaryLink: '#',
        external: false,
        secondary: {
          title: 'Products Overview',
          menuOptions: [
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'View Full Catalog',
              optionLink: '#',
              external: false,
            },
          ],
        },
      },
      {
        primary: 'Menu2',
        primaryLink: '#',
        external: false,
        secondary: {
          title: 'Menu2 Overview',
          menuOptions: [
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'View Full Catalog',
              optionLink: '#',
              external: false,
            },
          ],
        },
      },
      {
        primary: 'Menu3',
        primaryLink: '#',
        external: false,
        secondary: {
          title: 'Menu3 Overview',
          menuOptions: [
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'View Full Catalog',
              optionLink: '#',
              external: false,
            },
          ],
        },
      },
      {
        primary: 'Menu4',
        primaryLink: '#',
        external: false,
        secondary: {
          title: 'Menu4 Overview',
          menuOptions: [
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'View Full Catalog',
              optionLink: '#',
              external: false,
            },
          ],
        },
      },
      {
        primary: 'Menu5',
        primaryLink: '#',
        external: false,
        secondary: {
          title: 'Menu5 Overview',
          menuOptions: [
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'Sub Nav',
              optionLink: '#',
              external: false,
            },
            {
              option: 'View Full Catalog',
              optionLink: '#',
              external: false,
            },
          ],
        },
      },
    ],
    accountMenu: [
      {
        title: 'YOUR ACCOUNT',
        links: [
          {
            linkText: 'Profile',
            linkTo: '/my-account/profile',
          },
          {
            linkText: 'Addresses',
            linkTo: '/my-account/addresses',
          },
          {
            linkText: 'Payments',
            linkTo: '/my-account/paymentdetails',
          },
          {
            linkText: 'Lists Page',
            linkTo: '/my-account/lists',
          },
        ],
      },
    ],
  },
  footerSection: {
    copyRight: '© 2018 | Georgia Pacific',
    footerColumnLength: 4,
  },
  footerData: [
    {
      title: 'About Company',
      links: [
        {
          linkText: 'Sample Link',
          linkTo: '/about',
          external: true,
        },
        {
          linkText: 'Sample Link',
          linkTo: '/contactUs',
          external: true,
        },
        {
          linkText: 'Sample Link',
          linkTo: '#',
          external: true,
        },
      ],
    },
    {
      title: 'Contact Us',
      links: [
        {
          linkText: 'Sample Link',
          linkTo: '#',
          external: true,
        },
        {
          linkText: 'Sample Link',
          linkTo: '#',
          external: true,
        },
        {
          linkText: 'Sample Link',
          linkTo: '#',
          external: true,
        },
      ],
    },
    {
      title: 'Terms & Conditions',
      links: [
        {
          linkText: 'Sample Link',
          linkTo: '/sample',
          external: false,
        },
        {
          linkText: 'Sample Link',
          linkTo: '#',
          external: false,
        },
        {
          linkText: 'Sample Link',
          linkTo: '#',
          external: false,
        },
      ],
    },
    {
      title: 'Follow Us',
      links: [
        {
          linkText: 'facebook',
          linkTo: '#',
          external: false,
        },
        {
          linkText: 'pinterest',
          linkTo: '#',
          external: false,
        },
        {
          linkText: 'linkedin',
          linkTo: '#',
          external: false,
        },
        {
          linkText: 'youtube',
          linkTo: '#',
          external: false,
        },
        {
          linkText: 'twitter',
          linkTo: '#',
          external: true,
        },
        {
          linkText: 'instagram',
          linkTo: '#',
          external: true,
        },
        {
          linkText: 'snapchat',
          linkTo: '#',
          external: true,
        },
      ],
    },
  ],
  minimizedHeader: {
    placeholderTxt: 'Welcome to the Georgia-Pacific Store',
  },
  commonTitles: {
    iconAddTitle: 'Add',
    iconSuccessTitle: 'Success',
    iconAlertTitle: 'Important Notice',
    iconCloseTitle: 'Close',
  },
  slider: {
    iconChevronLeftTitle: 'Left',
    iconChevronRightTitle: 'Right',
  },
  addToCart: {
    addToCart: 'Add To Cart',
    findAStore: 'Find a Store',
    maxPurchaseableQuantityErrorCode: '197',
    maxPurchaseableQuantityErrorMessage:
      'One of the product in the cart exceeds Maximum allowable quantity. The quantity is updated to meet the threshold.',
    maxPurchaseableQuantityUpdateMessage1:
      'Maximum order quantity for this item is',
    maxPurchaseableQuantityUpdateMessage2:
      'your cart has been automatically updated.',
    lowStockErrorCode: '198',
    lowStockErrorMessage:
      'One of the product in the cart is running low on stock only available quantity is added to your cart',
  },
  notifyButton: {
    notifyMe: 'Notify',
    notifyMeHeading: 'Notify Me',
    notifyMeContent:
      'We can notify you when the product becomes available. If you would like us to contact multiple email addresses, please use a comma to separate each address',
    emailAddress: 'Email Address',
    add: 'ADD',
    notifyMeResponse: 'Recipient will be informed when in Stock',
    notifyError: 'Please enter in a valid Email Address.',
  },
  exportToolLogin: {
    userName: 'User Name',
    password: 'Password',
    login: 'LOGIN',
  },
  favorites: {
    favorites: 'Favorites',
  },
};

export default common;
