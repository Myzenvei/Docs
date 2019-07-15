const checkoutData = {
  type: 'cartWsDTO',
  appliedOrderPromotions: [],
  appliedProductPromotions: [{
      consumedEntries: [{
          adjustedUnitPrice: 23.26,
          orderEntryNumber: 1,
          quantity: 1,
        },
        {
          adjustedUnitPrice: 13.54,
          orderEntryNumber: 3,
          quantity: 1,
        },
      ],
      description: 'Buy any product priced above £12.00 and get £1.00 on it',
      promotion: {
        code: 'GP_product_price_threshold_fixed_discount',
        couldFireMessages: [],
        description: 'A fixed price discount is applied on products whose base price exceeds the specified threshold value',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
    {
      consumedEntries: [{
          adjustedUnitPrice: 23.26,
          orderEntryNumber: 4,
          quantity: 2,
        },
        {
          adjustedUnitPrice: 13.54,
          orderEntryNumber: 0,
          quantity: 1,
        },
      ],
      description: 'Some promo code applied',
      promotion: {
        code: 'GP_product_price_threshold_fixed_discount',
        couldFireMessages: [],
        description: 'A fixed price discount is applied on products whose base price exceeds the specified threshold value',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
  ],
  appliedVouchers: [],
  calculated: true,
  code: '00002005',
  deliveryItemsQuantity: 1,
  deliveryOrderGroups: [{
    entries: [{
      basePrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      entryNumber: 0,
      product: {
        availableForPickup: true,
        baseOptions: [{
            selected: {
              code: '300611196',
              priceData: {
                currencyIso: 'GBP',
                formattedValue: '£14.54',
                priceType: 'BUY',
                value: 14.54,
              },
              stock: {
                stockLevel: 15,
                stockLevelStatus: 'inStock',
              },
              url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
              variantOptionQualifiers: [{
                  name: 'Size',
                  qualifier: 'size',
                  value: '10.0',
                },
                {
                  name: 'Style',
                  qualifier: 'style',
                  value: 'leopard',
                },
              ],
            },
            variantType: 'ApparelSizeVariantProduct',
          },
          {
            selected: {
              code: '121941_leopard',
              priceData: {
                currencyIso: 'GBP',
                formattedValue: '£14.54',
                priceType: 'FROM',
                value: 14.54,
              },
              stock: {
                stockLevel: 0,
                stockLevelStatus: 'outOfStock',
              },
              url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
              variantOptionQualifiers: [{
                image: {
                  format: '30Wx30H',
                  url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
                },
                name: 'Style',
                qualifier: 'style',
                value: 'leopard',
              }, ],
            },
            variantType: 'ApparelStyleVariantProduct',
          },
        ],
        baseProduct: '121941_leopard',
        categories: [{
            code: '400300',
            url: '/category/400300',
          },
          {
            code: 'Volcom',
            image: {
              format: '96Wx96H',
              url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
            },
            url: '/category/Volcom',
          },
        ],
        code: '300611196',
        images: [{
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'zoom',
            imageType: 'PRIMARY',
            url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'product',
            imageType: 'PRIMARY',
            url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
          },
        ],
        name: 'Rocking 2 Creedlers Women leopard 10.0',
        purchasable: true,
        stock: {
          stockLevel: 15,
          stockLevelStatus: 'inStock',
        },
        url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
      },
      quantity: 1,
      totalPrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      updateable: true,
    }, ],
    totalPriceWithTax: {
      currencyIso: 'GBP',
      formattedValue: '£14.54',
      priceType: 'BUY',
      value: 14.54,
    },
  }, ],
  entries: [{
      "additionalAttributes": {
        "entry": [{
            "key": "installableProductCode",
            "value": "123456"
          },
          {
            "key": "giftableProduct",
            "value": "123456"
          }
        ]
      },
      "basePrice": {
        "currencyIso": "USD",
        "formattedValue": "$19,999.00",
        "priceType": "BUY",
        "value": 19999
      },
      "entryNumber": 0,
      "leasable": false,
      "leasableMessage": "Lease Approval required for purchase",
      "product": {
        "availableForPickup": false,
        "baseOptions": [{
            "isSizeVariant": false,
            "isStyleVariant": false,
            "selected": {
              "code": "300786274",
              "isSelected": false,
              "priceData": {
                "currencyIso": "USD",
                "formattedValue": "$19,999.00",
                "priceType": "BUY",
                "value": 19999
              },
              "stock": {
                "stockLevelStatus": "inStock"
              },
              "url": "/Categories/Streetwear-women/Skirts-and-Dresses-women/Strybal-Dress-Women/p/300786274",
              "variantOptionQualifiers": [{
                  "qualifier": "size",
                  "value": "L"
                },
                {
                  "qualifier": "style",
                  "value": "heron blue"
                }
              ]
            },
            "variantType": "GPCommerceSizeVariantProduct"
          },
          {
            "isSizeVariant": false,
            "isStyleVariant": false,
            "selected": {
              "code": "121868_heron_blue",
              "isSelected": false,
              "priceData": {
                "currencyIso": "USD",
                "formattedValue": "$19,999.00",
                "priceType": "FROM",
                "value": 19999
              },
              "stock": {
                "stockLevel": 0,
                "stockLevelStatus": "outOfStock"
              },
              "url": "/Categories/Streetwear-women/Skirts-and-Dresses-women/Strybal-Dress-Women/p/121868_heron_blue",
              "variantOptionQualifiers": [{
                "qualifier": "style",
                "value": "heron blue"
              }]
            },
            "variantType": "GPCommerceStyleVariantProduct"
          }
        ],
        "baseProduct": "121868_heron_blue",
        "categories": [{
            "code": "260700",
            "url": "/category/260700"
          },
          {
            "code": "streetwear",
            "url": "/category/streetwear"
          },
          {
            "code": "Volcom",
            "image": {
              "format": "96Wx96H",
              "mimeType": "image/jpeg",
              "url": "/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2hkNS9oNWMvODc5ODYxMTk5NjcwMi5qcGd8ZDk5ODAwYWE0NTNkMGZmNzhmYzJlMmU5ZDI3ZTJlZGE0MjFlZWU3NmZiMTQ1NDU4NzM4YTZmNTUwZGYwZWUyZg"
            },
            "url": "/category/Volcom"
          }
        ],
        "code": "300786274",
        "featureList": [

        ],
        "images": [{
            "altText": "Strybal Dress Women heron blue L",
            "format": "zoom",
            "imageType": "PRIMARY",
            "mimeType": "image/jpeg",
            "url": "/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w2MzQ0NHxpbWFnZS9qcGVnfGltYWdlcy9oZTgvaDlmLzg3OTg3NDQzNzk0MjIuanBnfDYwNGY2OTRiOWMyMTRkMGVmNmVlYTQ4MjY5YWQ4ZTBhNTFmNjk4NTQ2N2MzMmM1NjEzMTQyNWRlNTgwYTEwZWQ"
          },
          {
            "altText": "Strybal Dress Women heron blue L",
            "format": "product",
            "imageType": "PRIMARY",
            "mimeType": "image/jpeg",
            "url": "/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyMDczMHxpbWFnZS9qcGVnfGltYWdlcy9oM2IvaGMxLzg3OTg3NDQ0MTIxOTAuanBnfDZhNTgyYzVkM2Y4ZTI2MDJkNDhlNjc3YTc2Y2IyYzdjYzlkMzczNTdkMjY4NWU5NmM0MTg1ZTRiMDJmMjZlYjA"
          },
          {
            "altText": "Strybal Dress Women heron blue L",
            "format": "thumbnail",
            "imageType": "PRIMARY",
            "mimeType": "image/jpeg",
            "url": "/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wzNzk2fGltYWdlL2pwZWd8aW1hZ2VzL2gwNi9oZTQvODc5ODc0NDQ0NDk1OC5qcGd8MGNhZTYzMTEyYTNjMWYxODNlNjQ3Mjc0OGM4Mjc4OTVjOGQ5OWVlOWU1MDA4NTFiY2I0NjRlNjcyNjEzNzRkMA"
          },
          {
            "altText": "Strybal Dress Women heron blue L",
            "format": "cartIcon",
            "imageType": "PRIMARY",
            "mimeType": "image/jpeg",
            "url": "/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyMzE2fGltYWdlL2pwZWd8aW1hZ2VzL2hjYy9oMjYvODc5ODc0NDQ3NzcyNi5qcGd8MDc3ODNmMDFhODkwZGI3MGI3MWQwYjI0YWQwMjU3ODNkMDAyNDkxOWZlNzJmODkwZGY4MDRkMzcxZGJlZTgxZg"
          }
        ],
        "isBulkBuy": false,
        "isBundleAvailable": false,
        "isCustomizable": false,
        "isDropShipEligible": false,
        "isLeaseable": false,
        "isLockerEligible": false,
        "isOnlineOnly": false,
        "isPrivateLabel": false,
        "isSample": false,
        "isSeasonal": false,
        "isSubscribable": false,
        "name": "Strybal Dress Women heron blue L",
        "purchasable": true,
        "stock": {
          "stockLevelStatus": "inStock"
        },
        "url": "/Categories/Streetwear-women/Skirts-and-Dresses-women/Strybal-Dress-Women/p/300786274"
      },
      "quantity": 3,
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
        "qty": "1"
      }, {
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
        "qty": "2"
      }],
      "totalPrice": {
        "currencyIso": "USD",
        "formattedValue": "$19,999.00",
        "priceType": "BUY",
        "value": 19999
      },
      "updateable": true,
      "visible": true
    },

    // {
    //   splitEntries: [],
    //   visible: true,
    //   additionalAttributes: {
    //     entry: [{
    //         key: 'giftableProduct',
    //         value: '123456',
    //       },
    //       {
    //         key: 'giftOpted',
    //         value: 'true',
    //       },
    //       {
    //         key: 'giftableProductPrice',
    //         value: '$30',
    //       },
    //     ],
    //   },
    //   "giftMessage": "HELLO HELLO HELLO",
    //   "giftWrapped": true,
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 0,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611197',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 3,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },

    // {
    //   visible: true,
    //   additionalAttributes: {
    //     entry: [{
    //         key: 'giftableProduct',
    //         value: '123456',
    //       },
    //       {
    //         key: 'giftOpted',
    //         value: 'true',
    //       },
    //       {
    //         key: 'giftableProductPrice',
    //         value: '$20',
    //       },
    //     ],
    //   },
    //   "giftWrap": {
    //     "giftMessage": "",
    //     "giftWrapped": false,
    //   },
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 1,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611199',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 3,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
    // {
    //   visible: true,
    //   additionalAttributes: {
    //     entry: [{
    //         key: 'giftableProduct',
    //         value: '123456',
    //       },
    //       {
    //         key: 'giftOpted',
    //         value: 'false',
    //       },
    //     ],
    //   },
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 2,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611196',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'inStock',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 1,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
    // {
    //   visible: true,
    //   isLeasable: false,
    //   leasableMessage: 'Lease Approval required for purchase',
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 3,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611196',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 4,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
    // {
    //   visible: true,
    //   isLeasable: true,
    //   leasableMessage: 'Lease Approval required for purchase',
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 4,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611196',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 4,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
    // {
    //   visible: false,
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 5,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611196',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 4,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
    // {
    //   visible: true,
    //   additionalAttributes: {
    //     entry: [{
    //         key: 'installableProductCode',
    //         value: '123456',
    //       },
    //       {
    //         key: 'installed',
    //         value: 'true',
    //       },
    //     ],
    //   },
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 6,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611196',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 3,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
    // {
    //   visible: true,
    //   additionalAttributes: {
    //     entry: [{
    //         key: 'installableProductCode',
    //         value: '123456',
    //       },
    //       {
    //         key: 'installed',
    //         value: 'true',
    //       },
    //     ],
    //   },
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 7,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611196',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 3,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
    // {
    //   visible: true,
    //   additionalAttributes: {
    //     entry: [{
    //         key: 'installableProductCode',
    //         value: '123456',
    //       },
    //       {
    //         key: 'installed',
    //         value: 'false',
    //       },
    //     ],
    //   },
    //   basePrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   entryNumber: 8,
    //   product: {
    //     availableForPickup: true,
    //     baseOptions: [{
    //         selected: {
    //           code: '300611196',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'BUY',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 15,
    //             stockLevelStatus: 'inStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //           variantOptionQualifiers: [{
    //               name: 'Size',
    //               qualifier: 'size',
    //               value: '10.0',
    //             },
    //             {
    //               name: 'Style',
    //               qualifier: 'style',
    //               value: 'leopard',
    //             },
    //           ],
    //         },
    //         variantType: 'ApparelSizeVariantProduct',
    //       },
    //       {
    //         selected: {
    //           code: '121941_leopard',
    //           priceData: {
    //             currencyIso: 'GBP',
    //             formattedValue: '£14.54',
    //             priceType: 'FROM',
    //             value: 14.54,
    //           },
    //           stock: {
    //             stockLevel: 0,
    //             stockLevelStatus: 'outOfStock',
    //           },
    //           url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
    //           variantOptionQualifiers: [{
    //             image: {
    //               format: '30Wx30H',
    //               url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
    //             },
    //             name: 'Style',
    //             qualifier: 'style',
    //             value: 'leopard',
    //           }, ],
    //         },
    //         variantType: 'ApparelStyleVariantProduct',
    //       },
    //     ],
    //     baseProduct: '121941_leopard',
    //     categories: [{
    //         code: '400300',
    //         url: '/category/400300',
    //       },
    //       {
    //         code: 'Volcom',
    //         image: {
    //           format: '96Wx96H',
    //           url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
    //         },
    //         url: '/category/Volcom',
    //       },
    //     ],
    //     code: '300611196',
    //     images: [{
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'zoom',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'product',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'thumbnail',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
    //       },
    //       {
    //         altText: 'Rocking 2 Creedlers Women leopard 10.0',
    //         format: 'cartIcon',
    //         imageType: 'PRIMARY',
    //         url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
    //       },
    //     ],
    //     name: 'Rocking 2 Creedlers Women leopard 10.0',
    //     purchasable: true,
    //     stock: {
    //       stockLevel: 15,
    //       stockLevelStatus: 'Discontinued',
    //     },
    //     url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
    //   },
    //   quantity: 3,
    //   totalPrice: {
    //     currencyIso: 'GBP',
    //     formattedValue: '£14.54',
    //     priceType: 'BUY',
    //     value: 14.54,
    //   },
    //   updateable: true,
    // },
  ],
  guid: 'd982a685-7085-41fa-9f20-5cdccda3cece',
  net: false,
  orderDiscounts: {
    currencyIso: 'GBP',
    formattedValue: '£0.00',
    priceType: 'BUY',
    value: 0,
  },
  pickupItemsQuantity: 0,
  pickupOrderGroups: [],
  productDiscounts: {
    currencyIso: 'GBP',
    formattedValue: '£0.00',
    priceType: 'BUY',
    value: 0,
  },
  site: 'apparel-uk',
  store: 'apparel-uk',
  subTotal: {
    currencyIso: 'GBP',
    formattedValue: '£14.54',
    priceType: 'BUY',
    value: 14.54,
  },
  totalDiscounts: {
    currencyIso: 'GBP',
    formattedValue: '£0.00',
    priceType: 'BUY',
    value: 0,
  },
  totalItems: 1,
  totalPrice: {
    currencyIso: 'GBP',
    formattedValue: '£14.54',
    priceType: 'BUY',
    value: 14.54,
  },
  totalPriceWithTax: {
    currencyIso: 'GBP',
    formattedValue: '£14.54',
    priceType: 'BUY',
    value: 14.54,
  },
  totalTax: {
    currencyIso: 'GBP',
    formattedValue: '£0.00',
    priceType: 'BUY',
    value: 0,
  },
  potentialOrderPromotions: [],
  potentialProductPromotions: [],
  totalUnitCount: 1,
  "paymentInfo": {
    "accountHolderName": "Jerry",
    "billingAddress": {
      "billingAddress": false,
      "country": {
        "isocode": "US",
        "name": "United States"
      },
      "defaultAddress": false,
      "email": "ps@test.com",
      "firstName": "Tom",
      "formattedAddress": "1 Card Lane, California, My City, 94043",
      "id": "8796125822999",
      "lastName": "Doe",
      "line1": "1 Card Lane",
      "postalCode": "94043",
      "region": {
        "countryIso": "US",
        "isocode": "US-CA",
        "isocodeShort": "CA",
        "name": "California"
      },
      "shippingAddress": false,
      "title": "Mr",
      "titleCode": "mr",
      "town": "My City",
      "visibleInAddressBook": true
    },
    "cardNumber": "************1113",
    "cardType": {
      "code": "visa",
      "name": "Visa"
    },
    "defaultPayment": false,
    "expiryMonth": "11",
    "expiryYear": "2020",
    "id": "8796125823018",
    "saved": false,
    "subscriptionId": "MockedSubscriptionID"
  },
};

export default checkoutData;
