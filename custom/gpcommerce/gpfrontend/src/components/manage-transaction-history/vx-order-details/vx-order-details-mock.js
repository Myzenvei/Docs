const orderDetailsData = {
  type: 'orderWsDTO',
  appliedOrderPromotions: [],
  appliedProductPromotions: [],
  appliedVouchers: [],
  calculated: true,
  code: '0000044000',
  deliveryAddress: {
    billingAddress: false,
    country: {
      isocode: 'US',
      name: 'United States',
    },
    defaultAddress: false,
    defaultBillingAddress: false,
    firstName: 'AG',
    id: '8796551806999',
    lastName: 'AG',
    line1: 'Address1',
    line2: '3-16-19',
    phone: '9986674989',
    postalCode: '36341',
    region: {
      countryIso: 'US',
      isocode: 'US-AK',
      isocodeShort: 'AK',
      name: 'Alaska',
    },
    shippingAddress: true,
    town: 'Atlanta',
  },
  deliveryGroup: [
    {
      key: '8796125855767',
      value: {
        deliveryInstruction: '',
        deliveryMode: {
          code: 'standard-net',
          deliveryPrice: '$9.99',
        },
        splitEntries: [
          {
            code: '0000043006-0-1536837275030',
            deliveryAddress: {
              billingAddress: false,
              country: {
                isocode: 'US',
                name: 'United States',
              },
              defaultAddress: false,
              defaultBillingAddress: false,
              firstName: 'AG',
              id: '8796125855767',
              lastName: 'AG',
              line1: 'Address1',
              line2: '3-16-19',
              phone: '9986674989',
              postalCode: '36341',
              region: {
                countryIso: 'US',
                isocode: 'US-AK',
                isocodeShort: 'AK',
                name: 'Alaska',
              },
              shippingAddress: true,
              town: 'Atlanta',
            },
            deliveryInstruction: '',
            deliveryMode: {
              code: 'standard-net',
              deliveryPrice: '$9.99',
            },
            entryNumber: '0',
            price: 19999.0,
            productCode: '300791707',
            qty: '1',
          },
          {
            code: '0000043006-1-1536837275079',
            deliveryAddress: {
              billingAddress: false,
              country: {
                isocode: 'US',
                name: 'United States',
              },
              defaultAddress: false,
              defaultBillingAddress: false,
              firstName: 'AG',
              id: '8796125855767',
              lastName: 'AG',
              line1: 'Address1',
              line2: '3-16-19',
              phone: '9986674989',
              postalCode: '36341',
              region: {
                countryIso: 'US',
                isocode: 'US-AK',
                isocodeShort: 'AK',
                name: 'Alaska',
              },
              shippingAddress: true,
              town: 'Atlanta',
            },
            deliveryInstruction: '',
            deliveryMode: {
              code: 'standard-net',
              deliveryPrice: '$9.99',
            },
            entryNumber: '1',
            price: 19999.0,
            productCode: '94063_matte_black+gold',
            qty: '1',
          },
        ],
      },
    },
    {
      key: '8796191358999',
      value: {
        deliveryInstruction: '',
        deliveryMode: {
          code: 'standard-net',
          deliveryPrice: '$9.99',
        },
        splitEntries: [
          {
            code: '0000043006-2-1536837275089',
            deliveryAddress: {
              billingAddress: false,
              country: {
                isocode: 'US',
                name: 'United States',
              },
              defaultAddress: false,
              defaultBillingAddress: false,
              firstName: 'Shikha',
              id: '8796191358999',
              lastName: 'Gupta',
              line1: 'Kondapur',
              line2: 'asdf',
              phone: '703-263-7616',
              postalCode: '32003',
              region: {
                countryIso: 'US',
                isocode: 'US-FL',
                isocodeShort: 'FL',
                name: 'Florida',
              },
              shippingAddress: true,
              town: 'Hyderabad',
            },
            deliveryInstruction: '',
            deliveryMode: {
              code: 'standard-net',
              deliveryPrice: '$9.99',
            },
            entryNumber: '2',
            price: 123.0,
            productCode: '300026673',
            qty: '1',
          },
        ],
      },
    },
  ],
  deliveryItemsQuantity: 3,
  deliveryOrderGroups: [
    {
      entries: [
        {
          basePrice: {
            currencyIso: 'USD',
            formattedValue: '$19,999.00',
            priceType: 'BUY',
            value: 19999.0,
          },
          entryNumber: 0,
          giftWrapped: false,
          leasable: false,
          leasableMessage: 'Lease Approval required for purchase',
          product: {
            availableForPickup: false,
            baseOptions: [
              {
                isSizeVariant: false,
                isStyleVariant: false,
                selected: {
                  code: '300791707',
                  isSelected: false,
                  priceData: {
                    currencyIso: 'USD',
                    value: 19999.0,
                  },
                  stock: {
                    stockLevel: 27,
                  },
                  url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791707',
                  variantOptionQualifiers: [
                    {
                      qualifier: 'style',
                      value: 'coffee',
                    },
                    {
                      qualifier: 'size',
                      value: '40.0',
                    },
                  ],
                },
                variantType: 'GPCommerceSizeVariantProduct',
              },
              {
                isSizeVariant: false,
                isStyleVariant: false,
                selected: {
                  code: '118720_coffee',
                  isSelected: false,
                  priceData: {
                    currencyIso: 'USD',
                    value: 19999.0,
                  },
                  stock: {
                    stockLevel: 0,
                  },
                  url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791704',
                  variantOptionQualifiers: [
                    {
                      qualifier: 'style',
                      value: 'coffee',
                    },
                  ],
                },
                variantType: 'GPCommerceStyleVariantProduct',
              },
            ],
            baseProduct: '118720_coffee',
            categories: [
              {
                code: '400300',
              },
              {
                code: 'Roxy',
              },
            ],
            code: '300791707',
            featureList: [],
            giftWrapProduct: {
              giftWrapped: false,
            },
            images: [
              {
                altText: 'Bene Women coffee 40.0',
                format: 'zoom',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w0MjgwNHxpbWFnZS9qcGVnfGltYWdlcy9oYjQvaDk2Lzg3OTg4Mjg4NTUzMjYuanBnfDJlMTVlYzdlZjBjOTQ1ODI3ZTYwMTYxMWFmMDA1YWU4ZjNjMzlmYWRlN2ZjMDFlZGNmMjg5MTVhN2UxMjVkOGM',
              },
              {
                altText: 'Bene Women coffee 40.0',
                format: 'product',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxNTA2MXxpbWFnZS9qcGVnfGltYWdlcy9oMjMvaGE2Lzg3OTg4Mjg4ODgwOTQuanBnfGRhNGM3ZTc2MzIxNzUxNDA4ZjU1NThlN2E5MjdhZTMzMjdlZDlmZWEzYzY4YjJhOTNmYmIyNTIxYTllYzYyOGY',
              },
              {
                altText: 'Bene Women coffee 40.0',
                format: 'thumbnail',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wzNTAzfGltYWdlL2pwZWd8aW1hZ2VzL2hlYS9oZWEvODc5ODgyODkyMDg2Mi5qcGd8YmYwN2Y3MWUzODdiMGMzODkxYTYxYTA4ZTQ0N2Y1NGNkOWEyYWIwNTQ5NTllYzExMzVmNGVlZTdiZjkyYjkzNw',
              },
              {
                altText: 'Bene Women coffee 40.0',
                format: 'cartIcon',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyMzMzfGltYWdlL2pwZWd8aW1hZ2VzL2hlOS9oMWYvODc5ODgyODk1MzYzMC5qcGd8NGRjNjYyMzdmNmNhNGFlYTM0MDI2MGRlMjY1NDJjYzQyYzFmYWE5MTAwZTcxMDg0NGYyY2NhY2ZlYzA3NmEzYQ',
              },
            ],
            installationProduct: {
              installable: false,
            },
            isBulkBuy: false,
            isBundleAvailable: false,
            isCustomizable: false,
            isDropShipEligible: false,
            isFavorite: false,
            isFreeShipping: false,
            isLeaseable: false,
            isLockerEligible: false,
            isOnlineOnly: false,
            isPrivateLabel: false,
            isSample: false,
            isSeasonal: false,
            isSubscribable: false,
            isValidSKU: false,
            maxPurchaseableQuantity: 100,
            name: 'Bene Women coffee 40.0',
            purchasable: true,
            stock: {
              stockLevel: 27,
              stockLevelStatus: 'inStock',
            },
            url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791707',
          },
          quantity: 1,
          splitEntries: [
            {
              code: '0000043006-0-1536837275030',
              deliveryAddress: {
                billingAddress: false,
                country: {
                  isocode: 'US',
                  name: 'United States',
                },
                defaultAddress: false,
                defaultBillingAddress: false,
                firstName: 'AG',
                id: '8796125855767',
                lastName: 'AG',
                line1: 'Address1',
                line2: '3-16-19',
                phone: '9986674989',
                postalCode: '36341',
                region: {
                  countryIso: 'US',
                  isocode: 'US-AK',
                  isocodeShort: 'AK',
                  name: 'Alaska',
                },
                shippingAddress: true,
                town: 'Atlanta',
              },
              deliveryInstruction: '',
              deliveryMode: {
                code: 'standard-net',
                deliveryPrice: '$9.99',
              },
              entryNumber: '0',
              price: 19999.0,
              productCode: '300791707',
              qty: '1',
            },
          ],
          totalPrice: {
            currencyIso: 'USD',
            formattedValue: '$19,999.00',
            priceType: 'BUY',
            value: 19999.0,
          },
          visible: true,
        },
        {
          basePrice: {
            currencyIso: 'USD',
            formattedValue: '$19,999.00',
            priceType: 'BUY',
            value: 19999.0,
          },
          entryNumber: 1,
          giftWrapped: false,
          leasable: false,
          leasableMessage: 'Lease Approval required for purchase',
          product: {
            availableForPickup: false,
            baseOptions: [
              {
                isSizeVariant: false,
                isStyleVariant: false,
                selected: {
                  code: '94063_matte_black+gold',
                  isSelected: false,
                  priceData: {
                    currencyIso: 'USD',
                    value: 19999.0,
                  },
                  stock: {
                    stockLevel: 10,
                  },
                  url: '/Categories/Accessories/Watches/The-51-30/p/94063_matte_black%2Bgold',
                  variantOptionQualifiers: [
                    {
                      qualifier: 'style',
                      value: 'matte black/gold',
                    },
                  ],
                },
                variantType: 'GPCommerceStyleVariantProduct',
              },
            ],
            baseProduct: '94063',
            categories: [
              {
                code: '340000',
              },
              {
                code: 'Nixon',
              },
            ],
            code: '94063_matte_black+gold',
            featureList: [],
            giftWrapProduct: {
              giftWrapped: false,
            },
            images: [
              {
                altText: 'The 51-30 matte black/gold',
                format: 'zoom',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w2ODI2MHxpbWFnZS9qcGVnfGltYWdlcy9oNWMvaDRjLzg3OTg3OTgyMTcyNDYuanBnfGFlODFlZGYyOTgyYmRkNDI3YjYwOGQ4ODgxMjEzMzAxM2M2NzU3MWE4YTdlZGNiMDQ4MjE3YTQ4N2QxNjE4ODk',
              },
              {
                altText: 'The 51-30 matte black/gold',
                format: 'product',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyNjIzM3xpbWFnZS9qcGVnfGltYWdlcy9oMjEvaDYyLzg3OTg3OTgyNTAwMTQuanBnfDFkZDAxMGYwNzQ2ZTc2OTI5NzRhZmJmMGZmZjJkYTNmMjI1YzNjMzQzZDU3MWU5OWQ5MmYxZmM0ODA1Y2ViYjk',
              },
              {
                altText: 'The 51-30 matte black/gold',
                format: 'thumbnail',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w0OTU1fGltYWdlL2pwZWd8aW1hZ2VzL2g4My9oNzcvODc5ODc5ODI4Mjc4Mi5qcGd8M2JiZjZlZDc1NzhlZGVlNjhiMjA2MWRlOTdjNGQ3MDFhOTVkNGJiOTMxOTQ0ZGM4ODViZWU0YzU5NWZmMDYwMg',
              },
              {
                altText: 'The 51-30 matte black/gold',
                format: 'cartIcon',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODU2fGltYWdlL2pwZWd8aW1hZ2VzL2g2Zi9oMmEvODc5ODc5ODMxNTU1MC5qcGd8ZTYxM2U1YTVhN2E2NzUyZGM5YTY0NmQyYjIxNzIxNWNjNzE0M2MwZjM0MDRiM2Y1NWRiYzM1NTk1OTc3NjA3OA',
              },
            ],
            installationProduct: {
              installable: false,
            },
            isBulkBuy: false,
            isBundleAvailable: false,
            isCustomizable: false,
            isDropShipEligible: false,
            isFavorite: false,
            isFreeShipping: false,
            isLeaseable: false,
            isLockerEligible: false,
            isOnlineOnly: false,
            isPrivateLabel: false,
            isSample: false,
            isSeasonal: false,
            isSubscribable: false,
            isValidSKU: false,
            maxPurchaseableQuantity: 100,
            name: 'The 51-30 matte black/gold',
            purchasable: true,
            stock: {
              stockLevel: 10,
              stockLevelStatus: 'inStock',
            },
            url: '/Categories/Accessories/Watches/The-51-30/p/94063_matte_black%2Bgold',
          },
          quantity: 1,
          splitEntries: [
            {
              code: '0000043006-1-1536837275079',
              deliveryAddress: {
                billingAddress: false,
                country: {
                  isocode: 'US',
                  name: 'United States',
                },
                defaultAddress: false,
                defaultBillingAddress: false,
                firstName: 'AG',
                id: '8796125855767',
                lastName: 'AG',
                line1: 'Address1',
                line2: '3-16-19',
                phone: '9986674989',
                postalCode: '36341',
                region: {
                  countryIso: 'US',
                  isocode: 'US-AK',
                  isocodeShort: 'AK',
                  name: 'Alaska',
                },
                shippingAddress: true,
                town: 'Atlanta',
              },
              deliveryInstruction: '',
              deliveryMode: {
                code: 'standard-net',
                deliveryPrice: '$9.99',
              },
              entryNumber: '1',
              price: 19999.0,
              productCode: '94063_matte_black+gold',
              qty: '1',
            },
          ],
          totalPrice: {
            currencyIso: 'USD',
            formattedValue: '$19,999.00',
            priceType: 'BUY',
            value: 19999.0,
          },
          visible: true,
        },
        {
          basePrice: {
            currencyIso: 'USD',
            formattedValue: '$123.00',
            priceType: 'BUY',
            value: 123.0,
          },
          entryNumber: 2,
          giftWrapped: false,
          leasable: false,
          leasableMessage: 'Lease Approval required for purchase',
          product: {
            availableForPickup: false,
            baseOptions: [
              {
                isSizeVariant: false,
                isStyleVariant: false,
                selected: {
                  code: '300026673',
                  isSelected: false,
                  priceData: {
                    currencyIso: 'USD',
                    value: 123.0,
                  },
                  stock: {
                    stockLevel: 121,
                  },
                  url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026673',
                  variantOptionQualifiers: [
                    {
                      qualifier: 'style',
                      value: 'black/white',
                    },
                    {
                      qualifier: 'size',
                      value: '10.5 US',
                    },
                  ],
                },
                variantType: 'GPCommerceSizeVariantProduct',
              },
              {
                isSizeVariant: false,
                isStyleVariant: false,
                selected: {
                  code: 'M28323',
                  isSelected: false,
                  priceData: {
                    currencyIso: 'USD',
                    value: 123.0,
                  },
                  stock: {
                    stockLevel: 0,
                  },
                  url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026672',
                  variantOptionQualifiers: [
                    {
                      qualifier: 'style',
                      value: 'one',
                    },
                  ],
                },
                variantType: 'GPCommerceStyleVariantProduct',
              },
            ],
            baseProduct: 'M28323',
            categories: [
              {
                code: 'Vans',
              },
              {
                code: 'shoes',
              },
              {
                code: '230000',
              },
            ],
            code: '300026673',
            featureList: [],
            giftWrapProduct: {
              giftWrapped: false,
            },
            images: [
              {
                altText: 'Sneakers Vans Old Skool black/white 10.5',
                format: 'zoom',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxOTc1M3xpbWFnZS9qcGVnfGltYWdlcy9oYjQvaGZlLzg3OTg2OTMyOTQxMTAuanBnfGRmN2UwYzZjNDFmZjY3YWNhZmU4Mjc4N2E1ZTkwYmE0NmU3NDJkMTdmNjNjZjFkYTk1Y2Q4Y2E3YTRlZTkxOTQ',
              },
              {
                altText: 'Sneakers Vans Old Skool black/white 10.5',
                format: 'product',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w3Nzg4fGltYWdlL2pwZWd8aW1hZ2VzL2gxOC9oNTgvODc5ODY5NzIyNjI3MC5qcGd8NTI4YzE4NGYxMjFhMGYwN2QyYThkNTFmYzVjNzk1YzhlZWUzNDdkMGEzODU1ZjUzZjA1MGVlYjA2M2EwZmViNw',
              },
              {
                altText: 'Sneakers Vans Old Skool black/white 10.5',
                format: 'thumbnail',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxOTQ2fGltYWdlL2pwZWd8aW1hZ2VzL2hjOS9oMDkvODc5ODcwMTE1ODQzMC5qcGd8NGI4ZDU1NTNhZThkNDUwZjc4YWNmYjdkOWU4MWNhYjkxMzM3NTIzYzQyODhhNDcyNjFkZjg0NDcxYmQ2Nzk2Mw',
              },
              {
                altText: 'Sneakers Vans Old Skool black/white 10.5',
                format: 'cartIcon',
                imageType: 'PRIMARY',
                mimeType: 'image/jpeg',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMjU0fGltYWdlL2pwZWd8aW1hZ2VzL2hkZi9oYTgvODc5ODcwNTA5MDU5MC5qcGd8ODMyZjdhNGIxMGJlYzUwYzI0NzJiODNkNGFkYTRjODk1NjQ2NDQ2ZjRkZDU5NTUwODM5ZDgwZGE4NWE0MzI3NA',
              },
            ],
            installationProduct: {
              installable: false,
            },
            isBulkBuy: false,
            isBundleAvailable: false,
            isCustomizable: false,
            isDropShipEligible: false,
            isFavorite: false,
            isFreeShipping: false,
            isLeaseable: false,
            isLockerEligible: false,
            isOnlineOnly: false,
            isPrivateLabel: false,
            isSample: false,
            isSeasonal: false,
            isSubscribable: false,
            isValidSKU: false,
            maxPurchaseableQuantity: 100,
            name: 'Sneakers Vans Old Skool black/white 10.5',
            purchasable: true,
            stock: {
              stockLevel: 121,
              stockLevelStatus: 'inStock',
            },
            url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026673',
          },
          quantity: 1,
          splitEntries: [
            {
              code: '0000043006-2-1536837275089',
              deliveryAddress: {
                billingAddress: false,
                country: {
                  isocode: 'US',
                  name: 'United States',
                },
                defaultAddress: false,
                defaultBillingAddress: false,
                firstName: 'Shikha',
                id: '8796191358999',
                lastName: 'Gupta',
                line1: 'Kondapur',
                line2: 'asdf',
                phone: '703-263-7616',
                postalCode: '32003',
                region: {
                  countryIso: 'US',
                  isocode: 'US-FL',
                  isocodeShort: 'FL',
                  name: 'Florida',
                },
                shippingAddress: true,
                town: 'Hyderabad',
              },
              deliveryInstruction: '',
              deliveryMode: {
                code: 'standard-net',
                deliveryPrice: '$9.99',
              },
              entryNumber: '2',
              price: 123.0,
              productCode: '300026673',
              qty: '1',
            },
          ],
          totalPrice: {
            currencyIso: 'USD',
            formattedValue: '$123.00',
            priceType: 'BUY',
            value: 123.0,
          },
          visible: true,
        },
      ],
      totalPriceWithTax: {
        currencyIso: 'USD',
        formattedValue: '$40,121.00',
        priceType: 'BUY',
        value: 40121.0,
      },
    },
  ],
  entries: [
    {
      basePrice: {
        currencyIso: 'USD',
        formattedValue: '$19,999.00',
        priceType: 'BUY',
        value: 19999.0,
      },
      entryNumber: 0,
      giftWrapped: false,
      leasable: false,
      leasableMessage: 'Lease Approval required for purchase',
      product: {
        availableForPickup: false,
        baseOptions: [
          {
            isSizeVariant: false,
            isStyleVariant: false,
            selected: {
              code: '300791707',
              isSelected: false,
              priceData: {
                currencyIso: 'USD',
                value: 19999.0,
              },
              stock: {
                stockLevel: 27,
              },
              url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791707',
              variantOptionQualifiers: [
                {
                  qualifier: 'style',
                  value: 'coffee',
                },
                {
                  qualifier: 'size',
                  value: '40.0',
                },
              ],
            },
            variantType: 'GPCommerceSizeVariantProduct',
          },
          {
            isSizeVariant: false,
            isStyleVariant: false,
            selected: {
              code: '118720_coffee',
              isSelected: false,
              priceData: {
                currencyIso: 'USD',
                value: 19999.0,
              },
              stock: {
                stockLevel: 0,
              },
              url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791704',
              variantOptionQualifiers: [
                {
                  qualifier: 'style',
                  value: 'coffee',
                },
              ],
            },
            variantType: 'GPCommerceStyleVariantProduct',
          },
        ],
        baseProduct: '118720_coffee',
        categories: [
          {
            code: '400300',
          },
          {
            code: 'Roxy',
          },
        ],
        code: '300791707',
        featureList: [],
        giftWrapProduct: {
          giftWrapped: false,
        },
        images: [
          {
            altText: 'Bene Women coffee 40.0',
            format: 'zoom',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w0MjgwNHxpbWFnZS9qcGVnfGltYWdlcy9oYjQvaDk2Lzg3OTg4Mjg4NTUzMjYuanBnfDJlMTVlYzdlZjBjOTQ1ODI3ZTYwMTYxMWFmMDA1YWU4ZjNjMzlmYWRlN2ZjMDFlZGNmMjg5MTVhN2UxMjVkOGM',
          },
          {
            altText: 'Bene Women coffee 40.0',
            format: 'product',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxNTA2MXxpbWFnZS9qcGVnfGltYWdlcy9oMjMvaGE2Lzg3OTg4Mjg4ODgwOTQuanBnfGRhNGM3ZTc2MzIxNzUxNDA4ZjU1NThlN2E5MjdhZTMzMjdlZDlmZWEzYzY4YjJhOTNmYmIyNTIxYTllYzYyOGY',
          },
          {
            altText: 'Bene Women coffee 40.0',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wzNTAzfGltYWdlL2pwZWd8aW1hZ2VzL2hlYS9oZWEvODc5ODgyODkyMDg2Mi5qcGd8YmYwN2Y3MWUzODdiMGMzODkxYTYxYTA4ZTQ0N2Y1NGNkOWEyYWIwNTQ5NTllYzExMzVmNGVlZTdiZjkyYjkzNw',
          },
          {
            altText: 'Bene Women coffee 40.0',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyMzMzfGltYWdlL2pwZWd8aW1hZ2VzL2hlOS9oMWYvODc5ODgyODk1MzYzMC5qcGd8NGRjNjYyMzdmNmNhNGFlYTM0MDI2MGRlMjY1NDJjYzQyYzFmYWE5MTAwZTcxMDg0NGYyY2NhY2ZlYzA3NmEzYQ',
          },
        ],
        installationProduct: {
          installable: false,
        },
        isBulkBuy: false,
        isBundleAvailable: false,
        isCustomizable: false,
        isDropShipEligible: false,
        isFavorite: false,
        isFreeShipping: false,
        isLeaseable: false,
        isLockerEligible: false,
        isOnlineOnly: false,
        isPrivateLabel: false,
        isSample: false,
        isSeasonal: false,
        isSubscribable: false,
        isValidSKU: false,
        maxPurchaseableQuantity: 100,
        name: 'Bene Women coffee 40.0',
        purchasable: true,
        stock: {
          stockLevel: 27,
          stockLevelStatus: 'inStock',
        },
        url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791707',
      },
      quantity: 1,
      splitEntries: [
        {
          code: '0000043006-0-1536837275030',
          deliveryAddress: {
            billingAddress: false,
            country: {
              isocode: 'US',
              name: 'United States',
            },
            defaultAddress: false,
            defaultBillingAddress: false,
            firstName: 'AG',
            id: '8796125855767',
            lastName: 'AG',
            line1: 'Address1',
            line2: '3-16-19',
            phone: '9986674989',
            postalCode: '36341',
            region: {
              countryIso: 'US',
              isocode: 'US-AK',
              isocodeShort: 'AK',
              name: 'Alaska',
            },
            shippingAddress: true,
            town: 'Atlanta',
          },
          deliveryInstruction: '',
          deliveryMode: {
            code: 'standard-net',
            deliveryPrice: '$9.99',
          },
          entryNumber: '0',
          price: 19999.0,
          productCode: '300791707',
          qty: '1',
        },
      ],
      totalPrice: {
        currencyIso: 'USD',
        formattedValue: '$19,999.00',
        priceType: 'BUY',
        value: 19999.0,
      },
      visible: true,
    },
    {
      basePrice: {
        currencyIso: 'USD',
        formattedValue: '$19,999.00',
        priceType: 'BUY',
        value: 19999.0,
      },
      entryNumber: 1,
      giftWrapped: false,
      leasable: false,
      leasableMessage: 'Lease Approval required for purchase',
      product: {
        availableForPickup: false,
        baseOptions: [
          {
            isSizeVariant: false,
            isStyleVariant: false,
            selected: {
              code: '94063_matte_black+gold',
              isSelected: false,
              priceData: {
                currencyIso: 'USD',
                value: 19999.0,
              },
              stock: {
                stockLevel: 10,
              },
              url: '/Categories/Accessories/Watches/The-51-30/p/94063_matte_black%2Bgold',
              variantOptionQualifiers: [
                {
                  qualifier: 'style',
                  value: 'matte black/gold',
                },
              ],
            },
            variantType: 'GPCommerceStyleVariantProduct',
          },
        ],
        baseProduct: '94063',
        categories: [
          {
            code: '340000',
          },
          {
            code: 'Nixon',
          },
        ],
        code: '94063_matte_black+gold',
        featureList: [],
        giftWrapProduct: {
          giftWrapped: false,
        },
        images: [
          {
            altText: 'The 51-30 matte black/gold',
            format: 'zoom',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w2ODI2MHxpbWFnZS9qcGVnfGltYWdlcy9oNWMvaDRjLzg3OTg3OTgyMTcyNDYuanBnfGFlODFlZGYyOTgyYmRkNDI3YjYwOGQ4ODgxMjEzMzAxM2M2NzU3MWE4YTdlZGNiMDQ4MjE3YTQ4N2QxNjE4ODk',
          },
          {
            altText: 'The 51-30 matte black/gold',
            format: 'product',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyNjIzM3xpbWFnZS9qcGVnfGltYWdlcy9oMjEvaDYyLzg3OTg3OTgyNTAwMTQuanBnfDFkZDAxMGYwNzQ2ZTc2OTI5NzRhZmJmMGZmZjJkYTNmMjI1YzNjMzQzZDU3MWU5OWQ5MmYxZmM0ODA1Y2ViYjk',
          },
          {
            altText: 'The 51-30 matte black/gold',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w0OTU1fGltYWdlL2pwZWd8aW1hZ2VzL2g4My9oNzcvODc5ODc5ODI4Mjc4Mi5qcGd8M2JiZjZlZDc1NzhlZGVlNjhiMjA2MWRlOTdjNGQ3MDFhOTVkNGJiOTMxOTQ0ZGM4ODViZWU0YzU5NWZmMDYwMg',
          },
          {
            altText: 'The 51-30 matte black/gold',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODU2fGltYWdlL2pwZWd8aW1hZ2VzL2g2Zi9oMmEvODc5ODc5ODMxNTU1MC5qcGd8ZTYxM2U1YTVhN2E2NzUyZGM5YTY0NmQyYjIxNzIxNWNjNzE0M2MwZjM0MDRiM2Y1NWRiYzM1NTk1OTc3NjA3OA',
          },
        ],
        installationProduct: {
          installable: false,
        },
        isBulkBuy: false,
        isBundleAvailable: false,
        isCustomizable: false,
        isDropShipEligible: false,
        isFavorite: false,
        isFreeShipping: false,
        isLeaseable: false,
        isLockerEligible: false,
        isOnlineOnly: false,
        isPrivateLabel: false,
        isSample: false,
        isSeasonal: false,
        isSubscribable: false,
        isValidSKU: false,
        maxPurchaseableQuantity: 100,
        name: 'The 51-30 matte black/gold',
        purchasable: true,
        stock: {
          stockLevel: 10,
          stockLevelStatus: 'inStock',
        },
        url: '/Categories/Accessories/Watches/The-51-30/p/94063_matte_black%2Bgold',
      },
      quantity: 1,
      splitEntries: [
        {
          code: '0000043006-1-1536837275079',
          deliveryAddress: {
            billingAddress: false,
            country: {
              isocode: 'US',
              name: 'United States',
            },
            defaultAddress: false,
            defaultBillingAddress: false,
            firstName: 'AG',
            id: '8796125855767',
            lastName: 'AG',
            line1: 'Address1',
            line2: '3-16-19',
            phone: '9986674989',
            postalCode: '36341',
            region: {
              countryIso: 'US',
              isocode: 'US-AK',
              isocodeShort: 'AK',
              name: 'Alaska',
            },
            shippingAddress: true,
            town: 'Atlanta',
          },
          deliveryInstruction: '',
          deliveryMode: {
            code: 'standard-net',
            deliveryPrice: '$9.99',
          },
          entryNumber: '1',
          price: 19999.0,
          productCode: '94063_matte_black+gold',
          qty: '1',
        },
      ],
      totalPrice: {
        currencyIso: 'USD',
        formattedValue: '$19,999.00',
        priceType: 'BUY',
        value: 19999.0,
      },
      visible: true,
    },
    {
      basePrice: {
        currencyIso: 'USD',
        formattedValue: '$123.00',
        priceType: 'BUY',
        value: 123.0,
      },
      entryNumber: 2,
      giftWrapped: false,
      leasable: false,
      leasableMessage: 'Lease Approval required for purchase',
      product: {
        availableForPickup: false,
        baseOptions: [
          {
            isSizeVariant: false,
            isStyleVariant: false,
            selected: {
              code: '300026673',
              isSelected: false,
              priceData: {
                currencyIso: 'USD',
                value: 123.0,
              },
              stock: {
                stockLevel: 121,
              },
              url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026673',
              variantOptionQualifiers: [
                {
                  qualifier: 'style',
                  value: 'black/white',
                },
                {
                  qualifier: 'size',
                  value: '10.5 US',
                },
              ],
            },
            variantType: 'GPCommerceSizeVariantProduct',
          },
          {
            isSizeVariant: false,
            isStyleVariant: false,
            selected: {
              code: 'M28323',
              isSelected: false,
              priceData: {
                currencyIso: 'USD',
                value: 123.0,
              },
              stock: {
                stockLevel: 0,
              },
              url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026672',
              variantOptionQualifiers: [
                {
                  qualifier: 'style',
                  value: 'one',
                },
              ],
            },
            variantType: 'GPCommerceStyleVariantProduct',
          },
        ],
        baseProduct: 'M28323',
        categories: [
          {
            code: 'Vans',
          },
          {
            code: 'shoes',
          },
          {
            code: '230000',
          },
        ],
        code: '300026673',
        featureList: [],
        giftWrapProduct: {
          giftWrapped: false,
        },
        images: [
          {
            altText: 'Sneakers Vans Old Skool black/white 10.5',
            format: 'zoom',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxOTc1M3xpbWFnZS9qcGVnfGltYWdlcy9oYjQvaGZlLzg3OTg2OTMyOTQxMTAuanBnfGRmN2UwYzZjNDFmZjY3YWNhZmU4Mjc4N2E1ZTkwYmE0NmU3NDJkMTdmNjNjZjFkYTk1Y2Q4Y2E3YTRlZTkxOTQ',
          },
          {
            altText: 'Sneakers Vans Old Skool black/white 10.5',
            format: 'product',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w3Nzg4fGltYWdlL2pwZWd8aW1hZ2VzL2gxOC9oNTgvODc5ODY5NzIyNjI3MC5qcGd8NTI4YzE4NGYxMjFhMGYwN2QyYThkNTFmYzVjNzk1YzhlZWUzNDdkMGEzODU1ZjUzZjA1MGVlYjA2M2EwZmViNw',
          },
          {
            altText: 'Sneakers Vans Old Skool black/white 10.5',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxOTQ2fGltYWdlL2pwZWd8aW1hZ2VzL2hjOS9oMDkvODc5ODcwMTE1ODQzMC5qcGd8NGI4ZDU1NTNhZThkNDUwZjc4YWNmYjdkOWU4MWNhYjkxMzM3NTIzYzQyODhhNDcyNjFkZjg0NDcxYmQ2Nzk2Mw',
          },
          {
            altText: 'Sneakers Vans Old Skool black/white 10.5',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            mimeType: 'image/jpeg',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMjU0fGltYWdlL2pwZWd8aW1hZ2VzL2hkZi9oYTgvODc5ODcwNTA5MDU5MC5qcGd8ODMyZjdhNGIxMGJlYzUwYzI0NzJiODNkNGFkYTRjODk1NjQ2NDQ2ZjRkZDU5NTUwODM5ZDgwZGE4NWE0MzI3NA',
          },
        ],
        installationProduct: {
          installable: false,
        },
        isBulkBuy: false,
        isBundleAvailable: false,
        isCustomizable: false,
        isDropShipEligible: false,
        isFavorite: false,
        isFreeShipping: false,
        isLeaseable: false,
        isLockerEligible: false,
        isOnlineOnly: false,
        isPrivateLabel: false,
        isSample: false,
        isSeasonal: false,
        isSubscribable: false,
        isValidSKU: false,
        maxPurchaseableQuantity: 100,
        name: 'Sneakers Vans Old Skool black/white 10.5',
        purchasable: true,
        stock: {
          stockLevel: 121,
          stockLevelStatus: 'inStock',
        },
        url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026673',
      },
      quantity: 1,
      splitEntries: [
        {
          code: '0000043006-2-1536837275089',
          deliveryAddress: {
            billingAddress: false,
            country: {
              isocode: 'US',
              name: 'United States',
            },
            defaultAddress: false,
            defaultBillingAddress: false,
            firstName: 'Shikha',
            id: '8796191358999',
            lastName: 'Gupta',
            line1: 'Kondapur',
            line2: 'asdf',
            phone: '703-263-7616',
            postalCode: '32003',
            region: {
              countryIso: 'US',
              isocode: 'US-FL',
              isocodeShort: 'FL',
              name: 'Florida',
            },
            shippingAddress: true,
            town: 'Hyderabad',
          },
          deliveryInstruction: '',
          deliveryMode: {
            code: 'standard-net',
            deliveryPrice: '$9.99',
          },
          entryNumber: '2',
          price: 123.0,
          productCode: '300026673',
          qty: '1',
        },
      ],
      totalPrice: {
        currencyIso: 'USD',
        formattedValue: '$123.00',
        priceType: 'BUY',
        value: 123.0,
      },
      visible: true,
    },
  ],
  guid: 'cdfb3d3a-6d9b-4adb-a534-3b6ddb487d8f',
  net: true,
  orderDiscounts: {
    currencyIso: 'USD',
    formattedValue: '$0.00',
    priceType: 'BUY',
    value: 0.0,
  },
  paymentInfo: {
    accountHolderName: 'John Doe',
    billingAddress: {
      billingAddress: false,
      country: {
        isocode: 'US',
        name: 'United States',
      },
      defaultAddress: false,
      defaultBillingAddress: false,
      email: 'apurohit@gmail.com',
      firstName: 'John',
      id: '8796551872535',
      lastName: 'Doe',
      line1: 'Toyosaki',
      line2: '3-16-19',
      postalCode: '76107',
      region: {
        countryIso: 'US',
        isocode: 'US-TX',
        isocodeShort: 'TX',
        name: 'Texas',
      },
      shippingAddress: false,
      town: 'Osaka',
    },
    cardNumber: '************1111',
    cardType: {
      code: 'visa',
      name: 'Visa',
    },
    defaultPayment: false,
    expiryMonth: '03',
    expiryYear: '2019',
    id: '8796420735018',
    paymentToken: '9909000216368475',
    saved: false,
    subscriptionId: 'MockedSubscriptionID',
  },
  pickupItemsQuantity: 0,
  pickupOrderGroups: [],
  productDiscounts: {
    currencyIso: 'USD',
    formattedValue: '$0.00',
    priceType: 'BUY',
    value: 0.0,
  },
  site: 'vanityfair',
  store: 'vanityfair',
  subTotal: {
    currencyIso: 'USD',
    formattedValue: '$40,121.00',
    priceType: 'BUY',
    value: 40121.0,
  },
  totalDiscounts: {
    currencyIso: 'USD',
    formattedValue: '$0.00',
    priceType: 'BUY',
    value: 0.0,
  },
  totalItems: 3,
  totalPrice: {
    currencyIso: 'USD',
    formattedValue: '$40,148.00',
    priceType: 'BUY',
    value: 40148.0,
  },
  totalPriceWithTax: {
    currencyIso: 'USD',
    formattedValue: '$40,148.00',
    priceType: 'BUY',
    value: 40148.0,
  },
  totalTax: {
    currencyIso: 'USD',
    formattedValue: '$0.00',
    priceType: 'BUY',
    value: 0.0,
  },
  user: {
    active: false,
    name: 'Akshay Purohit',
    uid: 'apurohit@gmail.com|vanityfair',
  },
  userAdmin: false,
  consignments: [
    {
      code: '4000003000',
      entries: [
        {
          orderEntry: {
            basePrice: {
              currencyIso: 'USD',
              formattedValue: '$19,999.00',
              priceType: 'BUY',
              value: 19999.0,
            },
            entryNumber: 0,
            giftWrapped: false,
            leasable: false,
            leasableMessage: 'Lease Approval required for purchase',
            product: {
              availableForPickup: false,
              baseOptions: [
                {
                  isSizeVariant: false,
                  isStyleVariant: false,
                  selected: {
                    code: '300791707',
                    isSelected: false,
                    priceData: {
                      currencyIso: 'USD',
                      value: 19999.0,
                    },
                    stock: {
                      stockLevel: 27,
                    },
                    url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791707',
                    variantOptionQualifiers: [
                      {
                        qualifier: 'style',
                        value: 'coffee',
                      },
                      {
                        qualifier: 'size',
                        value: '40.0',
                      },
                    ],
                  },
                  variantType: 'GPCommerceSizeVariantProduct',
                },
                {
                  isSizeVariant: false,
                  isStyleVariant: false,
                  selected: {
                    code: '118720_coffee',
                    isSelected: false,
                    priceData: {
                      currencyIso: 'USD',
                      value: 19999.0,
                    },
                    stock: {
                      stockLevel: 0,
                    },
                    url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791704',
                    variantOptionQualifiers: [
                      {
                        qualifier: 'style',
                        value: 'coffee',
                      },
                    ],
                  },
                  variantType: 'GPCommerceStyleVariantProduct',
                },
              ],
              baseProduct: '118720_coffee',
              categories: [
                {
                  code: '400300',
                },
                {
                  code: 'Roxy',
                },
              ],
              code: '300791707',
              featureList: [],
              giftWrapProduct: {
                giftWrapped: false,
              },
              images: [
                {
                  altText: 'Bene Women coffee 40.0',
                  format: 'zoom',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w0MjgwNHxpbWFnZS9qcGVnfGltYWdlcy9oYjQvaDk2Lzg3OTg4Mjg4NTUzMjYuanBnfDJlMTVlYzdlZjBjOTQ1ODI3ZTYwMTYxMWFmMDA1YWU4ZjNjMzlmYWRlN2ZjMDFlZGNmMjg5MTVhN2UxMjVkOGM',
                },
                {
                  altText: 'Bene Women coffee 40.0',
                  format: 'product',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxNTA2MXxpbWFnZS9qcGVnfGltYWdlcy9oMjMvaGE2Lzg3OTg4Mjg4ODgwOTQuanBnfGRhNGM3ZTc2MzIxNzUxNDA4ZjU1NThlN2E5MjdhZTMzMjdlZDlmZWEzYzY4YjJhOTNmYmIyNTIxYTllYzYyOGY',
                },
                {
                  altText: 'Bene Women coffee 40.0',
                  format: 'thumbnail',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wzNTAzfGltYWdlL2pwZWd8aW1hZ2VzL2hlYS9oZWEvODc5ODgyODkyMDg2Mi5qcGd8YmYwN2Y3MWUzODdiMGMzODkxYTYxYTA4ZTQ0N2Y1NGNkOWEyYWIwNTQ5NTllYzExMzVmNGVlZTdiZjkyYjkzNw',
                },
                {
                  altText: 'Bene Women coffee 40.0',
                  format: 'cartIcon',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyMzMzfGltYWdlL2pwZWd8aW1hZ2VzL2hlOS9oMWYvODc5ODgyODk1MzYzMC5qcGd8NGRjNjYyMzdmNmNhNGFlYTM0MDI2MGRlMjY1NDJjYzQyYzFmYWE5MTAwZTcxMDg0NGYyY2NhY2ZlYzA3NmEzYQ',
                },
              ],
              installationProduct: {
                installable: false,
              },
              isBulkBuy: false,
              isBundleAvailable: false,
              isCustomizable: false,
              isDropShipEligible: false,
              isFavorite: false,
              isFreeShipping: false,
              isLeaseable: false,
              isLockerEligible: false,
              isOnlineOnly: false,
              isPrivateLabel: false,
              isSample: false,
              isSeasonal: false,
              isSubscribable: false,
              isValidSKU: false,
              maxPurchaseableQuantity: 100,
              name: 'Bene Women coffee 40.0',
              purchasable: true,
              stock: {
                stockLevel: 27,
                stockLevelStatus: 'inStock',
              },
              url: '/Categories/Shoes-women/Sandals-women/Bene-Women/p/300791707',
            },
            quantity: 1,
            splitEntries: [
              {
                code: '0000043006-0-1536837275030',
                deliveryAddress: {
                  billingAddress: false,
                  country: {
                    isocode: 'US',
                    name: 'United States',
                  },
                  defaultAddress: false,
                  defaultBillingAddress: false,
                  firstName: 'AG',
                  id: '8796125855767',
                  lastName: 'AG',
                  line1: 'Address1',
                  line2: '3-16-19',
                  phone: '9986674989',
                  postalCode: '36341',
                  region: {
                    countryIso: 'US',
                    isocode: 'US-AK',
                    isocodeShort: 'AK',
                    name: 'Alaska',
                  },
                  shippingAddress: true,
                  town: 'Atlanta',
                },
                deliveryInstruction: '',
                deliveryMode: {
                  code: 'standard-net',
                  deliveryPrice: '$9.99',
                },
                price: 19999.0,
                productCode: '300791707',
                qty: '1',
              },
            ],
            totalPrice: {
              currencyIso: 'USD',
              formattedValue: '$19,999.00',
              priceType: 'BUY',
              value: 19999.0,
            },
            visible: true,
          },
          quantity: 1,
          trackingList: [
            {
              trackingID: 111111167,
              quantityShipped: 1,
              carrier: 'abc',
            },
          ],
        },
        {
          orderEntry: {
            basePrice: {
              currencyIso: 'USD',
              formattedValue: '$19,999.00',
              priceType: 'BUY',
              value: 19999.0,
            },
            entryNumber: 1,
            giftWrapped: false,
            leasable: false,
            leasableMessage: 'Lease Approval required for purchase',
            product: {
              availableForPickup: false,
              baseOptions: [
                {
                  isSizeVariant: false,
                  isStyleVariant: false,
                  selected: {
                    code: '94063_matte_black+gold',
                    isSelected: false,
                    priceData: {
                      currencyIso: 'USD',
                      value: 19999.0,
                    },
                    stock: {
                      stockLevel: 10,
                    },
                    url: '/Categories/Accessories/Watches/The-51-30/p/94063_matte_black%2Bgold',
                    variantOptionQualifiers: [
                      {
                        qualifier: 'style',
                        value: 'matte black/gold',
                      },
                    ],
                  },
                  variantType: 'GPCommerceStyleVariantProduct',
                },
              ],
              baseProduct: '94063',
              categories: [
                {
                  code: '340000',
                },
                {
                  code: 'Nixon',
                },
              ],
              code: '94063_matte_black+gold',
              featureList: [],
              giftWrapProduct: {
                giftWrapped: false,
              },
              images: [
                {
                  altText: 'The 51-30 matte black/gold',
                  format: 'zoom',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w2ODI2MHxpbWFnZS9qcGVnfGltYWdlcy9oNWMvaDRjLzg3OTg3OTgyMTcyNDYuanBnfGFlODFlZGYyOTgyYmRkNDI3YjYwOGQ4ODgxMjEzMzAxM2M2NzU3MWE4YTdlZGNiMDQ4MjE3YTQ4N2QxNjE4ODk',
                },
                {
                  altText: 'The 51-30 matte black/gold',
                  format: 'product',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyNjIzM3xpbWFnZS9qcGVnfGltYWdlcy9oMjEvaDYyLzg3OTg3OTgyNTAwMTQuanBnfDFkZDAxMGYwNzQ2ZTc2OTI5NzRhZmJmMGZmZjJkYTNmMjI1YzNjMzQzZDU3MWU5OWQ5MmYxZmM0ODA1Y2ViYjk',
                },
                {
                  altText: 'The 51-30 matte black/gold',
                  format: 'thumbnail',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w0OTU1fGltYWdlL2pwZWd8aW1hZ2VzL2g4My9oNzcvODc5ODc5ODI4Mjc4Mi5qcGd8M2JiZjZlZDc1NzhlZGVlNjhiMjA2MWRlOTdjNGQ3MDFhOTVkNGJiOTMxOTQ0ZGM4ODViZWU0YzU5NWZmMDYwMg',
                },
                {
                  altText: 'The 51-30 matte black/gold',
                  format: 'cartIcon',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODU2fGltYWdlL2pwZWd8aW1hZ2VzL2g2Zi9oMmEvODc5ODc5ODMxNTU1MC5qcGd8ZTYxM2U1YTVhN2E2NzUyZGM5YTY0NmQyYjIxNzIxNWNjNzE0M2MwZjM0MDRiM2Y1NWRiYzM1NTk1OTc3NjA3OA',
                },
              ],
              installationProduct: {
                installable: false,
              },
              isBulkBuy: false,
              isBundleAvailable: false,
              isCustomizable: false,
              isDropShipEligible: false,
              isFavorite: false,
              isFreeShipping: false,
              isLeaseable: false,
              isLockerEligible: false,
              isOnlineOnly: false,
              isPrivateLabel: false,
              isSample: false,
              isSeasonal: false,
              isSubscribable: false,
              isValidSKU: false,
              maxPurchaseableQuantity: 100,
              name: 'The 51-30 matte black/gold',
              purchasable: true,
              stock: {
                stockLevel: 10,
                stockLevelStatus: 'inStock',
              },
              url: '/Categories/Accessories/Watches/The-51-30/p/94063_matte_black%2Bgold',
            },
            quantity: 1,
            splitEntries: [
              {
                code: '0000043006-1-1536837275079',
                deliveryAddress: {
                  billingAddress: false,
                  country: {
                    isocode: 'US',
                    name: 'United States',
                  },
                  defaultAddress: false,
                  defaultBillingAddress: false,
                  firstName: 'AG',
                  id: '8796125855767',
                  lastName: 'AG',
                  line1: 'Address1',
                  line2: '3-16-19',
                  phone: '9986674989',
                  postalCode: '36341',
                  region: {
                    countryIso: 'US',
                    isocode: 'US-AK',
                    isocodeShort: 'AK',
                    name: 'Alaska',
                  },
                  shippingAddress: true,
                  town: 'Atlanta',
                },
                deliveryInstruction: '',
                deliveryMode: {
                  code: 'standard-net',
                  deliveryPrice: '$9.99',
                },
                price: 19999.0,
                productCode: '94063_matte_black+gold',
                qty: '1',
              },
            ],
            totalPrice: {
              currencyIso: 'USD',
              formattedValue: '$19,999.00',
              priceType: 'BUY',
              value: 19999.0,
            },
            visible: true,
          },
          quantity: 6,
          trackingList: [
            {
              trackingID: 111111168,
              quantityShipped: 4,
              carrier: 'abc',
            },
            {
              trackingID: 111111167,
              quantityShipped: 2,
              carrier: 'abc',
            },
          ],
        },
      ],
      shippingAddress: {
        billingAddress: false,
        country: {
          isocode: 'US',
          name: 'United States',
        },
        defaultAddress: false,
        defaultBillingAddress: false,
        firstName: 'AG',
        id: '8796125855767',
        lastName: 'AG',
        line1: 'Address1',
        line2: '3-16-19',
        phone: '9986674989',
        postalCode: '36341',
        region: {
          countryIso: 'US',
          isocode: 'US-AK',
          isocodeShort: 'AK',
          name: 'Alaska',
        },
        shippingAddress: true,
        town: 'Atlanta',
      },
      status: 'READY',
    },
    {
      code: '4000003001',
      entries: [
        {
          orderEntry: {
            basePrice: {
              currencyIso: 'USD',
              formattedValue: '$123.00',
              priceType: 'BUY',
              value: 123.0,
            },
            entryNumber: 2,
            giftWrapped: false,
            leasable: false,
            leasableMessage: 'Lease Approval required for purchase',
            product: {
              availableForPickup: false,
              baseOptions: [
                {
                  isSizeVariant: false,
                  isStyleVariant: false,
                  selected: {
                    code: '300026673',
                    isSelected: false,
                    priceData: {
                      currencyIso: 'USD',
                      value: 123.0,
                    },
                    stock: {
                      stockLevel: 121,
                    },
                    url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026673',
                    variantOptionQualifiers: [
                      {
                        qualifier: 'style',
                        value: 'black/white',
                      },
                      {
                        qualifier: 'size',
                        value: '10.5 US',
                      },
                    ],
                  },
                  variantType: 'GPCommerceSizeVariantProduct',
                },
                {
                  isSizeVariant: false,
                  isStyleVariant: false,
                  selected: {
                    code: 'M28323',
                    isSelected: false,
                    priceData: {
                      currencyIso: 'USD',
                      value: 123.0,
                    },
                    stock: {
                      stockLevel: 0,
                    },
                    url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026672',
                    variantOptionQualifiers: [
                      {
                        qualifier: 'style',
                        value: 'one',
                      },
                    ],
                  },
                  variantType: 'GPCommerceStyleVariantProduct',
                },
              ],
              baseProduct: 'M28323',
              categories: [
                {
                  code: 'Vans',
                },
                {
                  code: 'shoes',
                },
                {
                  code: '230000',
                },
              ],
              code: '300026673',
              featureList: [],
              giftWrapProduct: {
                giftWrapped: false,
              },
              images: [
                {
                  altText: 'Sneakers Vans Old Skool black/white 10.5',
                  format: 'zoom',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxOTc1M3xpbWFnZS9qcGVnfGltYWdlcy9oYjQvaGZlLzg3OTg2OTMyOTQxMTAuanBnfGRmN2UwYzZjNDFmZjY3YWNhZmU4Mjc4N2E1ZTkwYmE0NmU3NDJkMTdmNjNjZjFkYTk1Y2Q4Y2E3YTRlZTkxOTQ',
                },
                {
                  altText: 'Sneakers Vans Old Skool black/white 10.5',
                  format: 'product',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w3Nzg4fGltYWdlL2pwZWd8aW1hZ2VzL2gxOC9oNTgvODc5ODY5NzIyNjI3MC5qcGd8NTI4YzE4NGYxMjFhMGYwN2QyYThkNTFmYzVjNzk1YzhlZWUzNDdkMGEzODU1ZjUzZjA1MGVlYjA2M2EwZmViNw',
                },
                {
                  altText: 'Sneakers Vans Old Skool black/white 10.5',
                  format: 'thumbnail',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxOTQ2fGltYWdlL2pwZWd8aW1hZ2VzL2hjOS9oMDkvODc5ODcwMTE1ODQzMC5qcGd8NGI4ZDU1NTNhZThkNDUwZjc4YWNmYjdkOWU4MWNhYjkxMzM3NTIzYzQyODhhNDcyNjFkZjg0NDcxYmQ2Nzk2Mw',
                },
                {
                  altText: 'Sneakers Vans Old Skool black/white 10.5',
                  format: 'cartIcon',
                  imageType: 'PRIMARY',
                  mimeType: 'image/jpeg',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMjU0fGltYWdlL2pwZWd8aW1hZ2VzL2hkZi9oYTgvODc5ODcwNTA5MDU5MC5qcGd8ODMyZjdhNGIxMGJlYzUwYzI0NzJiODNkNGFkYTRjODk1NjQ2NDQ2ZjRkZDU5NTUwODM5ZDgwZGE4NWE0MzI3NA',
                },
              ],
              installationProduct: {
                installable: false,
              },
              isBulkBuy: false,
              isBundleAvailable: false,
              isCustomizable: false,
              isDropShipEligible: false,
              isFavorite: false,
              isFreeShipping: false,
              isLeaseable: false,
              isLockerEligible: false,
              isOnlineOnly: false,
              isPrivateLabel: false,
              isSample: false,
              isSeasonal: false,
              isSubscribable: false,
              isValidSKU: false,
              maxPurchaseableQuantity: 100,
              name: 'Sneakers Vans Old Skool black/white 10.5',
              purchasable: true,
              stock: {
                stockLevel: 121,
                stockLevelStatus: 'inStock',
              },
              url: '/Brands/Vans/Sneakers-Vans-Old-Skool/p/300026673',
            },
            quantity: 1,
            splitEntries: [
              {
                code: '0000043006-2-1536837275089',
                deliveryAddress: {
                  billingAddress: false,
                  country: {
                    isocode: 'US',
                    name: 'United States',
                  },
                  defaultAddress: false,
                  defaultBillingAddress: false,
                  firstName: 'Shikha',
                  id: '8796191358999',
                  lastName: 'Gupta',
                  line1: 'Kondapur',
                  line2: 'asdf',
                  phone: '703-263-7616',
                  postalCode: '32003',
                  region: {
                    countryIso: 'US',
                    isocode: 'US-FL',
                    isocodeShort: 'FL',
                    name: 'Florida',
                  },
                  shippingAddress: true,
                  town: 'Hyderabad',
                },
                deliveryInstruction: '',
                deliveryMode: {
                  code: 'standard-net',
                  deliveryPrice: '$9.99',
                },
                price: 123.0,
                productCode: '300026673',
                qty: '1',
              },
            ],
            totalPrice: {
              currencyIso: 'USD',
              formattedValue: '$123.00',
              priceType: 'BUY',
              value: 123.0,
            },
            visible: true,
          },
          quantity: 1,
          trackingList: [
            {
              trackingID: 111111180,
              quantityShipped: 1,
              carrier: 'abc',
            },
          ],
        },
      ],
      shippingAddress: {
        billingAddress: false,
        country: {
          isocode: 'US',
          name: 'United States',
        },
        defaultAddress: false,
        defaultBillingAddress: false,
        firstName: 'Shikha',
        id: '8796191358999',
        lastName: 'Gupta',
        line1: 'Kondapur',
        line2: 'asdf',
        phone: '703-263-7616',
        postalCode: '32003',
        region: {
          countryIso: 'US',
          isocode: 'US-FL',
          isocodeShort: 'FL',
          name: 'Florida',
        },
        shippingAddress: true,
        town: 'Hyderabad',
      },
      status: 'READY',
    },
  ],
  created: '2018-09-13T11:15:31+0000',
  guestCustomer: false,
  status: 'CREATED',
  statusDisplay: 'created',
  unconsignedEntries: [],
};
export default orderDetailsData;
