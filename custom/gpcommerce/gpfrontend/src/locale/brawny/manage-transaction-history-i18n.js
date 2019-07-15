const manageTransactionHistory = {
  orderHistory: {
    orderNumber: 'Order Number',
    orderPlaced: 'Order Placed',
    status: 'Status',
    shippedTo: 'Shipped To',
    productNumber: '# of Items',
    total: 'Total',
    orderPlacedBy: 'Order Placed By',
    orderHistoryHeading: 'Order History',
    orderHistorySubheading:
      'Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit.',
    noOfOrders: 'Orders Placed',
    filterModalHeading: 'Refine Results',
    dateFilter: 'Filter by Date',
    clearAll: 'Clear All',
    apply: 'Apply',
    emptyTableMessage: 'No past orders found',
    date30DaysFilter: 'Last 30 Days',
    date6MonthFilter: 'Last 6 Months',
    defaultSort: 'Sort By',
    iconFilterTitle: 'Filter',
    b2cSortOptions: {
      options: [
        {
          label: 'Order # Ascending',
          value: '0-asc',
        },
        {
          label: 'Order # Descending',
          value: '0-dsc',
        },
        {
          label: 'Order Date Ascending',
          value: '1-asc',
        },
        {
          label: 'Order Date Descending',
          value: '1-dsc',
        },
        {
          label: 'Status Ascending',
          value: '2-asc',
        },
        {
          label: 'Status Descending',
          value: '2-dsc',
        },
        {
          label: 'Shipped To Ascending',
          value: '3-asc',
        },
        {
          label: 'Shipped To Descending',
          value: '3-dsc',
        },
        {
          label: 'Number of Products Ascending',
          value: '5-asc',
        },
        {
          label: 'Number of Products Descending',
          value: '5-dsc',
        },
        {
          label: 'Total Ascending',
          value: '6-asc',
        },
        {
          label: 'Total Descending',
          value: '6-dsc',
        },
      ],
    },
    b2bBuyerSortOptions: {
      options: [
        {
          label: 'Order # Ascending',
          value: '0-asc',
        },
        {
          label: 'Order # Descending',
          value: '0-dsc',
        },
        {
          label: 'Order Date Ascending',
          value: '1-asc',
        },
        {
          label: 'Order Date Descending',
          value: '1-dsc',
        },
        {
          label: 'Status Ascending',
          value: '2-asc',
        },
        {
          label: 'Status Descending',
          value: '2-dsc',
        },
        {
          label: 'Number of Products Ascending',
          value: '5-asc',
        },
        {
          label: 'Number of Products Descending',
          value: '5-dsc',
        },
        {
          label: 'Total Ascending',
          value: '6-asc',
        },
        {
          label: 'Total Descending',
          value: '6-dsc',
        },
      ],
    },
    transactionYearAriaLabel: 'select transaction year',
    // orderHisoryTableHeadings: 'Order # Ascending,Order # Descending,Order Date Ascending,Order Date Descending,Status Ascending,Status Descending,Number of Products Ascending,Number of Products Descending,Total Ascending,Total Descending',
  },
  orderDetails: {
    subscrOrder: 'Subscription Order',
    subscriptionHeading: 'Subscription',
    manageSubscription: 'Manage Your Subscription',
    itemOrdered: 'Item Ordered',
    reorder: 'reorder product',
    cancelOrder: 'Cancel Order',
    keepOrder: 'No, Keep Order',
    cancelOrderErrorMsg: 'Unable to cancel order, Please try again later',
    cancelOrderSuccessMsg:
      'You order is currently being processed for cancellation',
    cancelOrderText:
      'Do you have concerns about your order? If you have specific questions, please contact Customer Service at 1-800-975-9462',
    orderDetailsHeading: 'Order Details',
    giveAwayCoupon: "You've earned the following coupon!",
    orderNumber: 'Order Number',
    thankYou: 'Thank you for your Order!',
    orderEmail:
      "Your order is complete. We'll send you an email confirmation shortly.",
    order: 'Order',
    orderPlaced: 'Order Placed',
    orderStatus: 'Order Status',
    shippedTo: 'Shipped To',
    billedTo: 'Billed To',
    installation: 'Installation',
    deliveryMethod: 'Delivery Method',
    deliveryInstructions: ' Delivery Instructions',
    leaseAgreement: 'Lease Agreement',
    leaseAgreementName: 'Lease Agreement Name',
    download: 'Download',
    itemsOrdered: 'Items Ordered',
    itemId: 'ITEM ID',
    quantity: 'Quantity',
    promo: 'Promo',
    giftable: 'This item is a gift',
    giftMessageHeading: 'Gift Message',
    installable: 'Installation Requested',
    buyAgainButton: 'Buy Again',
    selectListLabel: 'Select a List',
    orderPlacedBy: 'Order Placed By',
    refund: 'Refund',
    customerServiceButton: 'contact customer service',
    trackingStatus: 'Status',
    trackingNumber: 'Tracking #',
    discontinued: 'Discontinued',
    replacedBy: 'and Replaced By',
    paidByCreditcard: 'Paid with Credit Card',
    iconAddToListTitle: 'Add to list',
    estName: 'Eastern Time (ET)',
    subscribeHeading: 'Review your subscription order',
    subscribeDiscount1: 'With ',
    subscribeDiscount2: '% discount',
    subscribeLearn: 'Learn More',
    subscribeIconTitle: 'Info',
    subscribeFrequencyHeading: 'Subscription: ',
    subscribePopoverText:
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec id elit non mi porta gravida at eget metus. Donec ullamcorper nulla non metus auctor fringilla.',
  },
  leaseAgreementDetails: {
    heading: 'Lease Agreement',
  },
  orderSummary: {
    orderSummaryHeading: 'Order Summary',
    cartProductTotal: 'Cart Subtotal',
    totalDiscounts: 'My savings',
    priceDiscounts: 'Price Discounts',
    promotionalDiscounts: 'Promotional Discounts',
    totalPrice: 'Total',
    itemLabel: 'Items',
    itemLabelSingle: 'Item',
    // Error Messages
    108: 'Promo code entered is invalid or out of date.',
    shipping: 'Shipping',
    savings: 'Total Savings',
    tax: 'Tax',
    gift: 'This order contains a gift.',
    learnMore: 'Learn more',
    install: "You've requested installation services.",
    installHelpContactText1: 'Please contact customer service at',
    installHelpContactNumber: '123-456-7890',
    installHelpContactText2: 'for questions regarding your installation.',
    orderHelpContactText:
      'For questions about your order, please contact customer service at',
    orderHelpContactNumber: '123-456-7890',
    termsConditionInfo:
      "By placing the order, I am confirming that I've read and agree with the",
    termsConditionLink: 'Terms & Conditions',
    subscribeTermsConditionInfo:
      "By subscribing, I am confirming that I've read and agree with the",
    subscribeHelpText: 'You are subscribed for the product',
    subscribeLearn: 'Learn More',
  },
  saveList: {
    createNewListLabel: 'Create New List',
    save: 'Save',
    emptyListError: 'Please enter in a valid New List Name.',
    saveAListError: 'Existing List has already been selected.',
    existingListError:
      'List Name is already in use. Please enter in a New List Name.',
    saveListResponse: 'List Saved Successfully',
  },
  orderApprovals: {
    orderNumber: 'Order Number',
    orderPlaced: 'Order Placed',
    emptyTableMessage: 'No order approvals found',
    status: 'Status',
    shippedTo: 'Shipped To',
    productNumber: 'Number Of Products',
    total: 'Total',
    orderPlacedBy: 'Order Placed By',
    orderHistoryHeading: 'Order History',
    orderHistorySubheading:
      'Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit.',
    noOfOrders: 'Orders Placed',
    filterModalHeading: 'Refine Results',
    dateFilter: 'Filter by Date',
    clearAll: 'Clear All',
    apply: 'Apply',
    date30DaysFilter: 'Last 30 Days',
    date6MonthFilter: 'Last 6 Months',
    approvalsHeader: 'Approvals',
    approvalsSubHeader:
      'Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit-test.',
    refineModalHeading: 'Refine Approvals List',
    filter: 'Filter',
    order: 'Order',
    noOfProducts: 'No of Products',
    transactionYearAriaLabel: 'select transaction year',
    defaultSort: 'Sort By',
    iconFilterTitle: 'Filter',
    iconXTitle: 'Close',
    statusOptions: {
      title: 'Status',
      options: [
        {
          label: 'Pending Approval',
          value: 'PENDING_APPROVAL',
        },
        {
          label: 'Submitted',
          value: 'SUBMITTED',
        },
        {
          label: 'In-process',
          value: 'In-process',
        },
        {
          label: 'Cancelled',
          value: 'CANCELLING',
        },
        {
          label: 'Complete',
          value: 'Complete',
        },
        {
          label: 'Partially cancelled',
          value: 'Partially cancelled',
        },
        {
          label: 'Rejected',
          value: 'REJECTED',
        },
        {
          label: 'Approved',
          value: 'APPROVED',
        },
        {
          label: 'Placed',
          value: 'CREATED',
        },
      ],
    },
    b2cSortOptions: {
      options: [
        {
          label: 'Order # Ascending',
          value: '0-asc',
        },
        {
          label: 'Order # Descending',
          value: '0-dsc',
        },
        {
          label: 'Order Date Ascending',
          value: '1-asc',
        },
        {
          label: 'Order Date Descending',
          value: '1-dsc',
        },
        {
          label: 'Status Ascending',
          value: '2-asc',
        },
        {
          label: 'Status Descending',
          value: '2-dsc',
        },
        {
          label: 'Shipped To Ascending',
          value: '3-asc',
        },
        {
          label: 'Shipped To Descending',
          value: '3-dsc',
        },
        {
          label: 'Number of Products Ascending',
          value: '5-asc',
        },
        {
          label: 'Number of Products Descending',
          value: '5-dsc',
        },
        {
          label: 'Total Ascending',
          value: '6-asc',
        },
        {
          label: 'Total Descending',
          value: '6-dsc',
        },
      ],
    },
    b2bAdminSortOptions: {
      options: [
        {
          label: 'Order # Ascending',
          value: '0-asc',
        },
        {
          label: 'Order # Descending',
          value: '0-dsc',
        },
        {
          label: 'Order Date Ascending',
          value: '1-asc',
        },
        {
          label: 'Order Date Descending',
          value: '1-dsc',
        },
        {
          label: 'Status Ascending',
          value: '2-asc',
        },
        {
          label: 'Status Descending',
          value: '2-dsc',
        },
        {
          label: 'Order Placed By Ascending',
          value: '4-asc',
        },
        {
          label: 'Order Placed By Descending',
          value: '4-dsc',
        },
        {
          label: 'Number of Products Ascending',
          value: '5-asc',
        },
        {
          label: 'Number of Products Descending',
          value: '5-dsc',
        },
        {
          label: 'Total Ascending',
          value: '6-asc',
        },
        {
          label: 'Total Descending',
          value: '6-dsc',
        },
      ],
    },
    b2bBuyerSortOptions: {
      options: [
        {
          label: 'Order # Ascending',
          value: '0-asc',
        },
        {
          label: 'Order # Descending',
          value: '0-dsc',
        },
        {
          label: 'Order Date Ascending',
          value: '1-asc',
        },
        {
          label: 'Order Date Descending',
          value: '1-dsc',
        },
        {
          label: 'Status Ascending',
          value: '2-asc',
        },
        {
          label: 'Status Descending',
          value: '2-dsc',
        },
        {
          label: 'Number of Products Ascending',
          value: '5-asc',
        },
        {
          label: 'Number of Products Descending',
          value: '5-dsc',
        },
        {
          label: 'Total Ascending',
          value: '6-asc',
        },
        {
          label: 'Total Descending',
          value: '6-dsc',
        },
      ],
    },
    b2bUnit: 'b2bUnit',
    removeIconAltText: 'Dismiss Tag',
  },
  orderApprovalDetails: {
    order: 'Order',
    orderDetails: 'Order Details',
    orderPlaced: 'Order Placed',
    orderStatus: 'Order Status',
    shippedTo: 'Shipped To',
    billedTo: 'Billed To',
    installation: 'Installation',
    deliveryMethod: 'Delivery Method',
    deliveryInstructions: ' Delivery Instructions',
    leaseAgreement: 'Lease Agreement',
    leaseAgreementName: 'Lease Agreement name',
    download: 'Download',
    itemsOrdered: 'Items Ordered',
    itemId: 'ITEM ID',
    quantity: 'Quantity',
    promo: 'Promo',
    giftable: 'This item is a gift',
    giftMessageHeading: 'Gift Message',
    installable: 'Installation Requested',
    buyAgainButton: 'Buy Again',
    discontinued: 'Discontinued',
    replacedBy: 'and Replaced By',
    approveOrder: 'Approve Order',
    rejectOrder: 'Reject Order',
    comments: 'Comments (Optional)',
    buisnessUnit: 'Business Unit',
    orderPlacedBy: 'Order Placed By',
  },
  reorderButton: {
    reorderSingleButtonText: 'REORDER',
    reorderMultipleButtonText: 'REORDER PRODUCTS',
    maxPurchaseableQuantityErrorCode: '197',
    lowStockErrorCode: '198',
    success: 'success',
    addedMaximumAllowableItemsMessage:
      'Unable to add one or more products to your cart. The quantity would exceed the maximum allowed.',
    addedMaximumInStockItemsMessage:
      'Unable to add one or more products to your cart. The quantity would exceed the available inventory.',
    oneOrMoreItemsHaveReplacementsMessage:
      'One or more of the items you are reordering have been replaced with a substitute item.',
    couldNotAddItemsFailure:
      'Your chosen product(s) are either discontinued or out of stock at this time. Please try again in few days.',
    generalFailure:
      'Product(s) not added to cart. Please try again in some time.',
    addToCartSuccess: 'Product(s) successfully added to the cart.',
    tooltip: 'Available Product(s) and quantity will be added to your cart',
  },
};
export default manageTransactionHistory;
