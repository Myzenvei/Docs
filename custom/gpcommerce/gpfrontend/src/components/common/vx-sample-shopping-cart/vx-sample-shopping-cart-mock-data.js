const shoppingCartData = {
  type: 'cartWsDTO',
  appliedOrderPromotions: [],
  appliedProductPromotions: [
    {
      consumedEntries: [
        {
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
        description:
          'A fixed price discount is applied on products whose base price exceeds the specified threshold value',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
    {
      consumedEntries: [
        {
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
        description:
          'A fixed price discount is applied on products whose base price exceeds the specified threshold value',
        firedMessages: [],
        promotionType: 'Rule Based Promotion',
      },
    },
  ],
  appliedVouchers: [],
  calculated: true,
  code: '00002005',
  deliveryItemsQuantity: 1,
  deliveryOrderGroups: [
    {
      entries: [
        {
          basePrice: {
            currencyIso: 'GBP',
            formattedValue: '£14.54',
            priceType: 'BUY',
            value: 14.54,
          },
          entryNumber: 0,
          product: {
            availableForPickup: true,
            baseOptions: [
              {
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
                  url:
                    '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
                  variantOptionQualifiers: [
                    {
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
                  url:
                    '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
                  variantOptionQualifiers: [
                    {
                      image: {
                        format: '30Wx30H',
                        url:
                          '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
                      },
                      name: 'Style',
                      qualifier: 'style',
                      value: 'leopard',
                    },
                  ],
                },
                variantType: 'ApparelStyleVariantProduct',
              },
            ],
            baseProduct: '121941_leopard',
            categories: [
              {
                code: '400300',
                url: '/category/400300',
              },
              {
                code: 'Volcom',
                image: {
                  format: '96Wx96H',
                  url:
                    '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
                },
                url: '/category/Volcom',
              },
            ],
            code: '300611196',
            images: [
              {
                altText: 'Rocking 2 Creedlers Women leopard 10.0',
                format: 'zoom',
                imageType: 'PRIMARY',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
              },
              {
                altText: 'Rocking 2 Creedlers Women leopard 10.0',
                format: 'product',
                imageType: 'PRIMARY',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
              },
              {
                altText: 'Rocking 2 Creedlers Women leopard 10.0',
                format: 'thumbnail',
                imageType: 'PRIMARY',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
              },
              {
                altText: 'Rocking 2 Creedlers Women leopard 10.0',
                format: 'cartIcon',
                imageType: 'PRIMARY',
                url:
                  '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
              },
            ],
            name: 'Rocking 2 Creedlers Women leopard 10.0',
            purchasable: true,
            stock: {
              stockLevel: 15,
              stockLevelStatus: 'inStock',
            },
            url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
            // color: '#5eb6e3',
            // size: '10.000" x 12.380" x 14.250"',
            customerMaterialNumber: 'CMIR19378TT',
          },
          quantity: 1,
          totalPrice: {
            currencyIso: 'GBP',
            formattedValue: '£14.54',
            priceType: 'BUY',
            value: 14.54,
          },
          updateable: true,
        },
      ],
      totalPriceWithTax: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
    },
  ],
  entries: [
    {
      visible: true,
      additionalAttributes: {
        entry: [
          {
            key: 'installableProductCode',
            value: '123456',
          },
          {
            key: 'installed',
            value: 'true',
          },
        ],
      },
      basePrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      entryNumber: 0,
      product: {
        availableForPickup: true,
        baseOptions: [
          {
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
              variantOptionQualifiers: [
                {
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
              url:
                '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
              variantOptionQualifiers: [
                {
                  image: {
                    format: '30Wx30H',
                    url:
                      '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
                  },
                  name: 'Style',
                  qualifier: 'style',
                  value: 'leopard',
                },
              ],
            },
            variantType: 'ApparelStyleVariantProduct',
          },
        ],
        baseProduct: '121941_leopard',
        categories: [
          {
            code: '400300',
            url: '/category/400300',
          },
          {
            code: 'Volcom',
            image: {
              format: '96Wx96H',
              url:
                '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
            },
            url: '/category/Volcom',
          },
        ],
        code: '300611196',
        images: [
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'zoom',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'product',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
          },
        ],
        name: 'Rocking 2 Creedlers Women leopard 10.0',
        purchasable: true,
        stock: {
          stockLevel: 15,
          stockLevelStatus: 'Discontinued',
        },
        url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
        // color: '#5EB6E3',
        // size: '10.000" x 12.380" x 14.250"',
        customerMaterialNumber: 'CMIR19378TT',
      },
      quantity: 3,
      totalPrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      updateable: true,
    },
    {
      visible: true,
      additionalAttributes: {
        entry: [
          {
            key: 'installableProductCode',
            value: '123456',
          },
          {
            key: 'installed',
            value: 'false',
          },
        ],
      },
      basePrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      entryNumber: 1,
      product: {
        availableForPickup: true,
        baseOptions: [
          {
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
              variantOptionQualifiers: [
                {
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
              url:
                '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
              variantOptionQualifiers: [
                {
                  image: {
                    format: '30Wx30H',
                    url:
                      '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
                  },
                  name: 'Style',
                  qualifier: 'style',
                  value: 'leopard',
                },
              ],
            },
            variantType: 'ApparelStyleVariantProduct',
          },
        ],
        baseProduct: '121941_leopard',
        categories: [
          {
            code: '400300',
            url: '/category/400300',
          },
          {
            code: 'Volcom',
            image: {
              format: '96Wx96H',
              url:
                '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
            },
            url: '/category/Volcom',
          },
        ],
        code: '300611196',
        images: [
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'zoom',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'product',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
          },
        ],
        name: 'Rocking 2 Creedlers Women leopard 10.0',
        purchasable: true,
        stock: {
          stockLevel: 15,
          stockLevelStatus: 'Discontinued',
        },
        url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
        // color: '#FF0000',
        // size: '10.000" x 12.380" x 14.250"',
        customerMaterialNumber: 'CMIR19378TT',
      },
      quantity: 3,
      totalPrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      updateable: true,
    },
    {
      visible: true,
      additionalAttributes: {
        entry: [
          {
            key: 'giftableProduct',
            value: '123456',
          },
          {
            key: 'giftOpted',
            value: 'false',
          },
        ],
      },
      basePrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      entryNumber: 2,
      product: {
        availableForPickup: true,
        baseOptions: [
          {
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
              variantOptionQualifiers: [
                {
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
              url:
                '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
              variantOptionQualifiers: [
                {
                  image: {
                    format: '30Wx30H',
                    url:
                      '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
                  },
                  name: 'Style',
                  qualifier: 'style',
                  value: 'leopard',
                },
              ],
            },
            variantType: 'ApparelStyleVariantProduct',
          },
        ],
        baseProduct: '121941_leopard',
        categories: [
          {
            code: '400300',
            url: '/category/400300',
          },
          {
            code: 'Volcom',
            image: {
              format: '96Wx96H',
              url:
                '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
            },
            url: '/category/Volcom',
          },
        ],
        code: '300611196',
        images: [
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'zoom',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'product',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
          },
        ],
        name: 'Rocking 2 Creedlers Women leopard 10.0',
        purchasable: true,
        stock: {
          stockLevel: 15,
          stockLevelStatus: 'inStock',
        },
        url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
        customerMaterialNumber: 'CMIR19378TT',
      },
      quantity: 1,
      totalPrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      updateable: true,
    },
    {
      visible: true,
      leasable: true,
      leasableMessage: 'Lease Approval required for purchase',
      basePrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      entryNumber: 3,
      product: {
        availableForPickup: true,
        baseOptions: [
          {
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
              variantOptionQualifiers: [
                {
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
              url:
                '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
              variantOptionQualifiers: [
                {
                  image: {
                    format: '30Wx30H',
                    url:
                      '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
                  },
                  name: 'Style',
                  qualifier: 'style',
                  value: 'leopard',
                },
              ],
            },
            variantType: 'ApparelStyleVariantProduct',
          },
        ],
        baseProduct: '121941_leopard',
        categories: [
          {
            code: '400300',
            url: '/category/400300',
          },
          {
            code: 'Volcom',
            image: {
              format: '96Wx96H',
              url:
                '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
            },
            url: '/category/Volcom',
          },
        ],
        code: '300611196',
        images: [
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'zoom',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'product',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
          },
        ],
        name: 'Rocking 2 Creedlers Women leopard 10.0',
        purchasable: true,
        stock: {
          stockLevel: 15,
          stockLevelStatus: 'Discontinued',
        },
        url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
        // color: '#008000',
        // size: '10.000" x 12.380" x 14.250"',
        customerMaterialNumber: 'CMIR19378TT',
      },
      quantity: 4,
      totalPrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      updateable: true,
    },
    {
      visible: false,
      basePrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      entryNumber: 4,
      product: {
        availableForPickup: true,
        baseOptions: [
          {
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
              variantOptionQualifiers: [
                {
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
              url:
                '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/121941_leopard',
              variantOptionQualifiers: [
                {
                  image: {
                    format: '30Wx30H',
                    url:
                      '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMzY1fGltYWdlL2pwZWd8aW1hZ2VzL2gxNi9oOWYvODc5NjgwNTQ5Njg2Mi5qcGd8YTlkNjlhYzg0OWY4NzA0ZjMwM2M3ODYzNGZkZDYwY2M4NjUzODczZWI4ZDFiYTBjYTAwNjBhM2Y2MGE1NWE3Zg',
                  },
                  name: 'Style',
                  qualifier: 'style',
                  value: 'leopard',
                },
              ],
            },
            variantType: 'ApparelStyleVariantProduct',
          },
        ],
        baseProduct: '121941_leopard',
        categories: [
          {
            code: '400300',
            url: '/category/400300',
          },
          {
            code: 'Volcom',
            image: {
              format: '96Wx96H',
              url:
                '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1ODIyfGltYWdlL2pwZWd8aW1hZ2VzL2g4OS9oNWUvODc5NjUyMDY0NDYzOC5qcGd8ZWRhYjUxNWFlOTU1MGJhNzQ5YzVmZjRlOTMwOTJlYTljYWJiZWJjZjQwZTIwZDNlY2RhMjJjNjNhN2FhMGY2YQ',
            },
            url: '/category/Volcom',
          },
        ],
        code: '300611196',
        images: [
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'zoom',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w4MDM1OXxpbWFnZS9qcGVnfGltYWdlcy9oZDYvaDgyLzg3OTY4MDUyMzQ3MTguanBnfDBhNmJhNjQxNGM0MGQ0NWJiMjkxYjZmN2FlNGY2ZGVmMmJhNjlhOGU5YWEzYTRlMmViNjA0NDVhNzQ3ZmEyYzg',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'product',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxMHxpbWFnZS9qcGVnfGltYWdlcy9oMDMvaDUxLzg3OTY4MDUzMDAyNTQuanBnfDI1MTUwYzRjZGQ0NWQ5M2VjMDM5NDJlODhkOGIzNmU0YTE5YmUxYWE5NTg2MTI3YWQ5ZGFiOTk5YzQ0YTQ0ODY',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w1MDk3fGltYWdlL2pwZWd8aW1hZ2VzL2gzMS9oY2YvODc5NjgwNTM2NTc5MC5qcGd8OTI2YzdjNjVlYjFkODgyNTI4OGFlYTI2YWYyZDNlODYwYmQ1MTg5NzgxNzRhNDExYTliMDU1OTljMDJiNDVlOQ',
          },
          {
            altText: 'Rocking 2 Creedlers Women leopard 10.0',
            format: 'cartIcon',
            imageType: 'PRIMARY',
            url:
              '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyOTQ3fGltYWdlL2pwZWd8aW1hZ2VzL2hiNC9oNDAvODc5NjgwNTQzMTMyNi5qcGd8ZjRjOTUyYTc4YjdkOThjZGE3OGQ0MWJhZDkwODk5MjhhOWVjYzU2MTBjMDdmNTkwMTE5YjAwODVmNTdmYmIxNw',
          },
        ],
        name: 'Rocking 2 Creedlers Women leopard 10.0',
        purchasable: true,
        stock: {
          stockLevel: 15,
          stockLevelStatus: 'Discontinued',
        },
        url: '/Categories/Shoes-women/Sandals-women/Rocking-2-Creedlers-Women/p/300611196',
        customerMaterialNumber: 'CMIR19378TT',
      },
      quantity: 4,
      totalPrice: {
        currencyIso: 'GBP',
        formattedValue: '£14.54',
        priceType: 'BUY',
        value: 14.54,
      },
      updateable: true,
    },
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
};

export default shoppingCartData;
