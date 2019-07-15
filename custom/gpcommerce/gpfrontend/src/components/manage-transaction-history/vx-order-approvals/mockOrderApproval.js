const mockOrderApproval = {
  orders: [
    {
      code: '00012004',
      guid: '9fdd48a2-f101-49b5-8b84-af1aa01d54e0',
      placed: '2018-08-31T06:09:00+0000',
      creationTime: '2012-08-22T06:07:33+0000',
      shippingAddress: {
        addresses: [
          {
            country: {
              isocode: 'US',
            },
            defaultAddress: false,
            defaultBillingAddress: false,
            firstName: 'shikha',
            id: '8796355198999',
            lastName: 'gupta',
            line1: 'address1',
            line2: 'address2',
            phone: '537-687-9790',
            postalCode: '32004',
            region: {
              isocode: 'US-AL',
            },
            town: 'NEW YORK',
          },
        ],
        isAdmin: false,
      },
      status: 'CREATED',
      statusDisplay: 'created',
      total: {
        currencyIso: 'USD',
        formattedValue: '$11,769.00',
        priceType: 'BUY',
        value: 11769,
      },
      totalNumberOfProducts: '1',
    },
    {
      code: '00012005',
      guid: '9fdd48a2-f101-49b5-8b84-af1aa01d54e0',
      placed: '2018-08-31T06:09:00+0000',
      shippingAddress: {
        addresses: [
          {
            country: {
              isocode: 'US',
            },
            defaultAddress: false,
            defaultBillingAddress: false,
            firstName: 'shikha',
            id: '8796355198999',
            lastName: 'gupta',
            line1: 'address1',
            line2: 'address2',
            phone: '537-687-9790',
            postalCode: '32004',
            region: {
              isocode: 'US-AL',
            },
            town: 'NEW YORK',
          },
        ],
        isAdmin: false,
      },
      status: 'CREATED',
      statusDisplay: 'shipped',
      total: {
        currencyIso: 'USD',
        formattedValue: '$11,769.00',
        priceType: 'BUY',
        value: 11769,
      },
      totalNumberOfProducts: '1',
    },
  ],
  pagination: {
    currentPage: 0,
    pageSize: 20,
    sort: 'byDate',
    totalPages: 1,
    totalResults: 1,
  },
  sorts: [
    {
      code: 'byDate',
      selected: true,
    },
    {
      code: 'byOrderNumber',
      selected: false,
    },
  ],
};
export default mockOrderApproval;
