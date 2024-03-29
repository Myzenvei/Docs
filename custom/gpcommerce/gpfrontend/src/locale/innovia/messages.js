import common from './common-i18n';
import registrationLogin from './registration-login-i18n';
import manageShoppingCart from './manage-shoppping-cart-i18n';
import quickOrder from './quick-order-i18n';
import viewSiteContent from './view-site-content-i18n';
import accessCustomerService from './access-customer-service-i18n';
import checkout from './checkout-i18n';
import manageProfileShoppingLists from './manage-profile-shopping-lists-i18n';
import nfr from './nfr-i18n';
import manageTransactionHistory from './manage-transaction-history-i18n';
import searchBrowse from './search-browse-i18n';
import manageSubscription from './manage-subscription-i18n';

const messages = {
  'en-US': {
    manageProfileShoppingLists,
    registrationLogin,
    accessCustomerService,
    // This below pdp section should come under one pdp.i18n
    pdpProductInfo: {
      itemId: 'ITEM ID',
      itemIdA11y: 'Item ID:',
      cmir: 'CMIR',
      outOfStock: 'Out of stock',
      lowInventoryMessage: 'Low Inventory',
      only: 'Only ',
      leftInStock: ' left in stock',
      backorderItem: 'Available on',
      notifyAvailability: 'Notify',
      quantity: 'Quantity',
      size: 'Size',
      scent: 'Scent',
      count: 'Count',
      variant: 'Colour',
      leaseAgreement: 'This product requires a lease agreement.',
      addToCart: 'Add to Cart',
      shareItemHeading: 'Share Item',
      content:
        'Fill out the information below to send an email with information about this product.',
      recipientEmail: 'Recipient Emails*',
      helperText: 'Separate multiple email addresses with a comma.',
      senderName: 'Sender' + 's Name*',
      senderEmail: 'Sender' + 's Email Address*',
      a11ySenderOptionalMsgLabel: 'Please Enter Optional Message',
      subject: 'Subject*',
      message: 'Message (optional)',
      attachPDF: 'Include product detail PDF',
      addLink: 'Include links to additional product information',
      disclaimerLine1:
        'For consumers and customers located in the European Economic Area (EEA):',
      disclaimerLine2:
        'Georgia-Pacific subscribes to the Safe Harbor privacy principles.',
      disclaimerLine3: 'Please visit our ',
      disclaimerLine4: 'Privacy Policy ',
      disclaimerLine5: 'to learn how you can exercise and amend your choices.',
      privacyPolicyLink:
        'https://www.gp.com/legal/legal-notices/privacy-notice',
      sendEmail: 'SEND EMAIL',
      recipientEmailValidation: 'Please enter in a valid Email Address',
      senderNameRequired: "Please enter in a valid Sender's Name",
      senderNameRegexError: 'Please enter only alphabets',
      senderEmailRequired: "Please enter in a valid Sender's Email Address",
      subjectRequired: 'Please enter in a Subject',
      shareItemResponse: 'Product Shared Successfully',
      notifyMeHeading: 'Notify Me',
      notifyMeContent:
        'We can notify you when the product becomes available. If you would like us to contact multiple email addresses, please use a comma to separate each address',
      emailAddress: 'Email Address',
      add: 'ADD',
      notifyMeResponse: 'Recipient will be informed when in Stock',
      notifyError: 'Please enter in a valid Email Address.',
      selectListHeading: 'Select a List',
      createNewListLabel: 'Create New List',
      save: 'Save',
      whenYouSubscribe: '% when you subscribe - ',
      oneTimeDelivery: 'One time delivery - ',
      subscribe: 'Subscribe',
      selectListResponse: 'List Saved Successfully',
      emptyListError: 'Please enter in a valid New List Name.',
      saveAListError: 'Existing List has already been selected.',
      existingListError:
        'List Name is already in use. Please enter in a New List Name.',
      refillsHeading: 'Also Consider',
      noThanks: 'No, Thanks',
      refillsResponse: 'Products successfully added to cart',
      refillAvailable: 'Also consider these complimentary products.',
      comingSoon: 'COMING SOON',
      discontinued: 'Discontinued',
      replacedBy: 'and Replaced By',
      submitLabel: 'Submit',
      availableSoon: 'Available Soon',
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
      iconChevronUpTitle: 'Up',
      iconChevronDownTitle: 'Down',
      iconMaximizeTitle: 'Zoom In',
      iconCertificationTitle: 'Green Certified',
      iconShippingTitle: 'Free Shipping Available',
      iconOnlineTitle: 'Only Available Online',
      iconSubscriptionTitle: 'Subscribe',
      iconSeasonalTitle: 'Seasonal Product',
      iconAddToListTitle: 'Add to list',
      iconShareTitle: 'Share',
      iconXTitle: 'Close',
      iconAlertCircleTitle: 'Important Notice',
      iconDownloadTitle: 'Download PDF/Images',
      downloadInfoHeading: 'PDF & Image Downloads',
      a11ySlashedPrice: 'Slashed Price',
      a11yCurrentPrice: 'Current Price',
      iconSavedToFavoriteTitle: 'Saved To Favorites',
      iconSaveToFavoriteTitle: 'Save To Favorites',
      maxValueUpdatedStatus:
        'The product quantity is adjusted to the maximum allowed quantity.',
      minValueUpdatedStatus:
        'The product quantity is adjusted to the minimum allowed quantity.',
      stockLevelUpdatedStatus:
        'The product quantity is adjusted to the stock availability.',
      imageDownloadError: 'No images are currently available for this product.',
      defaultDownloadedFilename: 'imagedownload.zip',
      totalReviewsLabel: 'Total Reviews',
      averageRatingLabel: 'Average Rating',
      maxQuantity: 'The maximum allowable quantity is',
      minQuantity: 'The minimum allowable quantity is',
      downloadInfo: {
        tabPdf: 'PDF Download',
        tabImage: 'Image Download',
      },
      pdfDownload: {
        basicInformation:
          'Fill out any of the following information to include on the PDF',
        nameOnPdf: 'Name to include on the PDF (Optional)',
        phoneNumber: 'Phone Number (Optional)',
        emailAddress: 'Email Address (Optional)',
        message: 'Message (Optional)',
        barColor: 'Bar Color',
        optionalHeadlines: 'Optional Headlines and Settings',
        headlineLine1: 'Large headline on Line 1',
        headlineLine2: 'Medium headline on Line 2',
        headlineColor: 'Headline Color',
        coverPage: 'Cover Page',
        onlyOnFirstPage: 'Only on first page?',
        onlyOnFirstPageDescription:
          'Only display headlines on the first page of the PDF',
        formatList: 'Format the list in the chosen format',
        option1: 'Display in one column',
        option1Description: 'Structure the content in one column',
        option2: 'Display in two columns',
        option2Description: 'Divide and structure the content in two columns',
        option3: 'Display in three columns',
        option3Description: 'Divide and structure the content in three columns',
        option4: 'Display as full detail',
        option4Description: 'Divide and structure the content in two columns',
        category: 'Category description',
        categoryDescription: 'Provide category descriptions',
        productSellingStatement: 'Product selling statement',
        productSellingStatementDescription: 'Include product selling statement',
        createAndDownloadPdf: 'Download',
        phoneMinError: 'The Phone Number entered is invalid, please try again.',
        emailInvalidError: 'Please enter a valid email address.',
        dropdownInputDefault: 'Select a Cover',
        disclaimerLine1:
          'For consumers and customers located in the European Economic Area (EEA):',
        disclaimerLine2:
          'Georgia-Pacific subscribes to the Safe Harbor privacy principles.',
        disclaimerLine3:
          'Please visit our Safe Harbor Privacy Policy to learn how you can exercise and amend your choices.',
        resetLink: 'Reset Form',
      },
      imageDownload: {
        imageDownloadDescription: 'Fill out any of the download options below.',
        imageDownloadcontent:
          'Note: Downloading time will increase with more images',
        allImageDownloadCheckbox: 'Include full set of images.',
        allImageDownloadOption: 'Download all images for this product',
        imageDownloadFileFormat: 'File Format Options',
        imageDownloadFileSize: 'File Size Options',
        imageDownloadButton: 'DOWNLOAD',
        imageDownloadJpg: 'JPG',
        imageDownloadPng: 'PNG',
        imageDownloadGif: 'GIF',
        imageDownloadOriginalSize: 'Original Size (2000px x 2000px)',
        imageDownloadLarge: 'Large (1200px x 1200px)',
        imageDownloadMedium: 'Medium (515px x 515px)',
        imageDownloadSmall: 'Small (300px x 300px)',
        imageDownloadThumbnail: 'Thumbnail (96px x 96px)',
        disclaimerLine1:
          'For consumers and customers located in the European Economic Area (EEA):',
        disclaimerLine2:
          'Georgia-Pacific subscribes to the Safe Harbor privacy principles.',
        disclaimerLine3:
          'Please visit our Safe Harbor Privacy Policy to learn how you can exercise and amend your choices.',
        formatJPG: 'jpg',
        formatPNG: 'png',
        formatGIF: 'gif',
        sizeDefault: '2000',
        size1200: '1200',
        size515: '515',
        size300: '300',
        size96: '96',
      },
      productIconsTitle: {
        freeShipping: 'Free Shipping Eligible',
        onlineOnly: 'Only Available Online',
        subscribable: 'Subscription Eligible',
        seasonal: 'Seasonal Product',
        bundleAvailable: 'Bundle Eligible',
        certification: 'Certifications Available',
        customisation: 'Customizable Product',
        installation: 'Installation Service Available',
        sample: 'Sample Eligible',
      },
    },
    pdpTabContainer: {
      tabDetails: 'Details',
      tabReviews: 'Reviews',
      tabProductResources: 'Product Resources',
      tabRelatedProducts: 'Related Products',
      tabCompareProducts: 'Compare Products',
      upsellCarouselHeading: 'Customer also viewed',
    },
    pdp: {
      productDetails: {
        topLeftHeading: 'Product Overview',
        topRightHeading: 'Product Features',
        bottomHeading: 'Product Specifications',
      },
      productResourcesTab: {
        topLeftHeading: 'Product Information',
        topRightHeading: 'Product Resources',
        subHeading1: 'Documents',
        subHeading2: 'Download',
        subHeading3: 'Audio and Videos',
        view: 'View',
      },
      relatedProductsTab: {
        topLeftHeading: 'Related Products',
      },
    },
    bundleCarousel: {
      buildABundle: 'BUILD A BUNDLE',
      notAvailable: 'NOT AVAILABLE',
    },
    buildBundle: {
      updateBundle: 'Update Bundle',
      productTile: {
        label: {
          compare: 'Compare',
          select: 'Select',
          remove: 'Remove',
          addToCart: 'Add to Cart',
          findAStore: 'Find a Store',
          notifyMe: 'Notify',
          productCode: 'ITEM ID',
          productCodeA11y: 'Item ID:',
          chooseOption: 'Choose Option',
          a11ySlashedPrice: 'Slashed Price',
          a11yCurrentPrice: 'Current Price',
          quantity: 'Quantity',
        },
        notifyMeHeading: 'Notify Me',
        notifyMeContent:
          'Please add the email address you would like to be notified with when this product becomes available.',
        emailAddress: 'Email Address',
        submit: 'Submit',
        notifyMeResponse: 'Recipient will be informed when in Stock',
        notifyError: 'Please enter in a valid Email Address.',
        selectListHeading: 'Select a List',
        createNewListLabel: 'Create New List',
        save: 'Save',
        selectListResponse: 'List Saved Successfully',
        emptyListError: 'Please enter in a valid New List Name.',
        saveAListError: 'Existing List has already been selected.',
        existingListError:
          'List Name is already in use. Please enter in a New List Name.',
        comingSoonBanner: 'Coming Soon',
        inStock: 'In Stock',
        outOfStock: 'Out of stock',
        lowInventoryMessage: 'Low Inventory',
        writeReview: 'Write a review',
        leftInStock: ' left in stock',
        only: 'Only ',
        discontinued: 'Discontinued',
        replacedBy: 'and Replaced By',
        submitLabel: 'SUBMIT',
        availableSoon: 'Available Soon',
        add: 'ADD',
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
        iconXCircleTitle: 'Error',
        iconAddToListTitle: 'Add to list',
        iconCertificationTitle: 'Green Certified',
        iconShippingTitle: 'Free Shipping Available',
        iconOnlineTitle: 'Only Available Online',
        iconSubscriptionTitle: 'Subscribe',
        iconSeasonalTitle: 'Seasonal Product',
        iconSavedToFavoriteTitle: 'Saved To Favorites',
        iconSaveToFavoriteTitle: 'Save To Favorites',
        maxValueUpdatedStatus:
          'Requested quantity not available. Cart updated with max quantity in stock.',
        mfg: 'MFG',
        cust: 'CUST',
        productAvailability: 'Available for this location',
        averageRatingLabel: 'Average Rating',
        totalReviewsLabel: 'Total Reviews',
        guestList: 'Guest List',
        productIconsTitle: {
          freeShipping: 'Free Shipping Eligible',
          onlineOnly: 'Only Available Online',
          subscribable: 'Subscription Eligible',
          seasonal: 'Seasonal Product',
          bundleAvailable: 'Bundle Eligible',
          certification: 'Certifications Available',
          customisation: 'Customizable Product',
          installation: 'Installation Service Available',
          sample: 'Sample Eligible',
        },
      },
      noProductName: 'no product name',
    },
    manageShoppingCart,
    quickOrder,
    checkout,
    common,
    viewSiteContent,
    nfr,
    manageTransactionHistory,
    searchBrowse,
    manageSubscription,
  },
};

export default messages;
