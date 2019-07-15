const shippingMethodDetails = {
  deliveryModes: [{
      code: 'standard-net',
      deliveryCost: {
        currencyIso: 'USD',
        formattedValue: '$9.99',
        priceType: 'BUY',
        value: 9.99,
      },
      description: '3-5 business days',
      name: 'Standard Delivery',
    },
    {
      code: 'premium-net',
      deliveryCost: {
        currencyIso: 'USD',
        formattedValue: '$16.99',
        priceType: 'BUY',
        value: 16.99,
      },
      description: '1-2 business days',
      name: 'Premium Delivery',
    },
    {
      code: 'fast-net',
      deliveryCost: {
        currencyIso: 'USD',
        formattedValue: '$25.99',
        priceType: 'BUY',
        value: 25.99,
      },
      description: '1-2 business days',
      name: 'Superfast Delivery',
    },
  ],
  "deliveryMode": {
    "code": ""
  },
  "deliveryInstruction": "",
};

export default shippingMethodDetails;
