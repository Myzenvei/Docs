const shippingMethodDetails = {
  "deliveryGroup": [{
      "key": "8796256894999",
      "value": {
        "deliveryInstruction": "",
        "deliveryMode": {
          "code": ""
        },
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
            name: 'Super Delivery',
          },
        ],
        "splitEntries": [{
            "code": "00016000-0-1533118551226",
            "deliveryAddress": {
              "billingAddress": false,
              "country": {
                "isocode": "US"
              },
              "defaultAddress": false,
              "firstName": "AG",
              "id": "8796256894999",
              "lastName": "AG",
              "line1": "Address1",
              "line2": "3-16-19",
              "postalCode": "36341",
              "region": {
                "isocode": "US-AK"
              },
              "town": "Atlanta"
            },
            "deliveryInstruction": "do not delivery",
            "deliveryMode": {
              "code": "standard-net"
            },
            "entryNumber": "0",
            "qty": "1"
          },
          {
            "code": "00016000-1-1533117603105",
            "deliveryAddress": {
              "billingAddress": false,
              "country": {
                "isocode": "US"
              },
              "defaultAddress": false,
              "firstName": "AG",
              "id": "8796256894999",
              "lastName": "AG",
              "line1": "Address1",
              "line2": "3-16-19",
              "postalCode": "36341",
              "region": {
                "isocode": "US-AK"
              },
              "town": "Atlanta"
            },
            "entryNumber": "3",
            "qty": "1"
          }
        ]
      }
    },
    {
      "key": "8796256894993",
      "value": {
        "deliveryInstruction": "",
        "deliveryMode": {
          "code": ""
        },
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
        "splitEntries": [{
            "code": "00016000-0-1533118551226",
            "deliveryAddress": {
              "billingAddress": false,
              "country": {
                "isocode": "US"
              },
              "defaultAddress": false,
              "firstName": "AG",
              "id": "8796256894999",
              "lastName": "AG",
              "line1": "Address1",
              "line2": "3-16-19",
              "postalCode": "36341",
              "region": {
                "isocode": "US-AK"
              },
              "town": "Atlanta"
            },
            "deliveryInstruction": "do not delivery",
            "deliveryMode": {
              "code": "standard-net"
            },
            "entryNumber": "4",
            "qty": "1"
          },
          {
            "code": "00016000-1-1533117603105",
            "deliveryAddress": {
              "billingAddress": false,
              "country": {
                "isocode": "US"
              },
              "defaultAddress": false,
              "firstName": "AG",
              "id": "8796256894999",
              "lastName": "AG",
              "line1": "Address1",
              "line2": "3-16-19",
              "postalCode": "36341",
              "region": {
                "isocode": "US-AK"
              },
              "town": "Atlanta"
            },
            "entryNumber": "2",
            "qty": "1"
          }
        ]
      }
    }
  ]
};

export default shippingMethodDetails;
