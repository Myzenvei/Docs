const quickOrder = {
  csvUpload: {
    downloadCaption: 'Download a fomatted csv file',
    downloadHeaders: 'data:text/csv;charset=utf-8',
    sampleCsvdata: 'Mfg.Part_Number/Material_Number/Product_ID,Quantity,Product_Name,CMIR,UPC',
    uploadInfo:
      'If you have a spreadsheet of up to 25 product IDs, please upload and then add those products to your cart',
  },
  fileUpload: {
    dragDropCaption: 'Drag and drop file here',
    fileAttachCaption1: 'Or',
    fileAttachCaption2: 'Choose File to Upload',
    removeText: 'Remove File',
    upload: 'UPLOAD',
    fileSize: 'File size: ',
    fileSizeUnit: 'kb',
    attachmentSizeLimitError: "File exceeded size limit and can't be attached",
    attachmentTypeError: 'This file type is not allowed',
    iconAttachmentTitle: 'Attachment',
  },
  quickOrder: {
    quickOrderPageHeading: 'Quick Order',
    pageInfo:
      "If you know the Product ID/SKU numbers for the products you'd like to order, enter them below then add to your shopping cart",
    searchInfo: 'Enter company or product ID/SKU, separate multiple IDs by commas',
    csvUpload: 'UPLOAD CSV',
    nonPurchasable: 'Items may not be available for purchase',
    addItems: 'ADD ITEMS',
    productsNotFound: 'The following product IDs could not be found',
    duplicate: 'The following products IDs are already present in order',
    addToCart: 'Add Items To Cart',
    clearAll: 'Clear All',
    subTotal: 'Subtotal of ',
    items: 'Items:',
    uploadModalHeading: 'Upload Spreadsheet',
    shareListHeading: 'Share List',
    createNewListLabel: 'Create New List',
    selectListLabel: 'Select a List',
    b2bUnit: 'b2bUnit',
    fullFieldsQuery: 'fields=FULL',
    solarParamPrefix: '?query=:relevance:code:',
    outOfStockMessage: 'Out of stock ',
    backOderabledate: 'available: ',
    nonBackOderable: 'not back-orderable',
    onlyCopyText: 'Only ',
    inStockText: ' in stock',
    discontinuedProduct: 'Discontinued',
    replacementProduct: 'Discontinued product, replaced by sku ',
    comingSoonProduct: 'coming soon',
    maxOrderQuantity: 'Max Order Qty is ',
    subTotalLabel: 'sub total',
    maxPurchaseableQuantityErrorCode: '197',
    maxPurchaseableQuantityErrorMessage: 'One of the product in the cart exceeds Maximum allowable quantity. The quantity is updated to meet the threshold.',
    maxPurchaseableQuantityUpdateMessage1: 'Maximum order quantity for this item is',
    maxPurchaseableQuantityUpdateMessage2: 'your cart has been automatically updated.',
    lowStockErrorCode: '198',
    lowStockErrorMessage: 'One of the product in the cart is running low on stock only available quantity is added to your cart',
    addListToCartError: 'One or more products were not added to your cart due to stock availability.',
    item: 'Item',
    iconAddToListTitle: 'Add to list',
    iconShareTitle: 'Share',
    iconCertificationTitle: 'Green Certified',
    iconShippingTitle: 'Free Shipping Available',
    iconOnlineTitle: 'Only Available Online',
    iconSubscriptionTitle: 'Subscribe',
    iconSeasonalTitle: 'Seasonal Product',
    iconTrashTitle: 'Delete'
  },
  productTile: {
    itemId: 'ITEM ID',
    quantity: 'Quantity',
    cmir: 'CMIR',
    notifyMe: 'NOTIFY',
    notifyMeContent: 'Please add the email address you would like to be notified with when this product becomes available.',
    emailAddress: 'Email Address',
    submit: 'Submit',
    notifyMeResponse: 'Recipient will be informed when in Stock',
    notifyMeHeading: 'Notify Me',
    notifyError: 'Please enter in a valid Email Address',
    useReplacement: 'USE REPLACEMENT',
    add: 'ADD',
    iconCertificationTitle: 'Green Certified',
    iconShippingTitle: 'Free Shipping Available',
    iconOnlineTitle: 'Only Available Online',
    iconSubscriptionTitle: 'Subscribe',
    iconSeasonalTitle: 'Seasonal Product',
    iconTrashTitle: 'Delete'
  },
  shareList: {
    shareListDescription:
      'Add the email addresses you would like to share with. Separate multiple emails with a comma',
    shareListEmail: 'Email Address',
    shareListButton: 'Share',
    shareListError: 'Please enter in a valid Email Address',
    shareListResponse: 'List Shared Successfully',
  },
  saveList: {
    createNewListLabel: 'Create New List',
    save: 'Save',
    emptyListError: 'Please enter in a valid New List Name',
    saveAListError: 'Existing List has already been selected',
    existingListError: 'List Name is already in use. Please enter in a New List Name',
    saveListResponse: 'List Saved Successfully',
  },
};
export default quickOrder;
