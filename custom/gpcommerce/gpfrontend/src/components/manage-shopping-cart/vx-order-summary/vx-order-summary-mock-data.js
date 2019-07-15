const orderSummaryData = {
  type: 'cartWsDTO',
  appliedOrderPromotions: [
    {
      consumedEntries: [],
      description: 'Buy over £10.00 get £20.00 discount on cart',
      promotion: {
        code: 'GP order Total Promotion',
        couldFireMessages: [],
        description:
          'A fixed discount is applied to the cart when the order threshold value is reached',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
    {
      consumedEntries: [],
      description: '20 % discount applied',
      promotion: {
        code: 'apparelPromotion',
        couldFireMessages: [],
        description: '20% discount',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
    {
      consumedEntries: [],
      description:
        'You have received 50% discount on cart using the coupon code [GPPROMO10]',
      promotion: {
        code: 'GP_coupon_code_percentage_discount',
        couldFireMessages: [],
        description:
          'A percentage discount is added to the cart when a valid coupon code is entered',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
  ],
  appliedVouchers: [
    {
      code: 'GPPROMO10',
      freeShipping: false,
      voucherCode: 'GPPROMO10',
    },
  ],
  code: '00002005',
  guid: 'd982a685-7085-41fa-9f20-5cdccda3cece',
  orderDiscounts: {
    currencyIso: 'GBP',
    formattedValue: '£23.55',
    priceType: 'BUY',
    value: 23.55,
  },
  subTotal: {
    currencyIso: 'GBP',
    formattedValue: '£36.80',
    priceType: 'BUY',
    value: 36.8,
  },
  totalDiscounts: {
    currencyIso: 'GBP',
    formattedValue: '£25.55',
    priceType: 'BUY',
    value: 25.55,
  },
  totalItems: 2,
  totalPrice: {
    currencyIso: 'GBP',
    formattedValue: '£13.25',
    priceType: 'BUY',
    value: 13.25,
  },
  totalPriceWithTax: {
    currencyIso: 'GBP',
    value: 13.25,
  },
  potentialOrderPromotions: [
    {
      consumedEntries: [],
      description: 'Buy over £20.00 get 10% discount on cart',
      promotion: {
        code: 'GP_order_threshold_percentage_discount',
        couldFireMessages: [],
        description:
          'A percentage discount is applied to the cart when the order threshold value is reached',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
  ],
};
export default orderSummaryData;
