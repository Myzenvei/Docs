<template>
<div class='login-page'>
  <h1>{{ msg }}</h1>

  <div class='container-fluid'>
    <div class='row'>
      <div class='col-sm-6'>
        <vx-login action-url='/someurl/somepoint' :i18n='messages.registrationLogin.login'></vx-login>
        <vx-social-login action-url='/someurl/somepoint' :i18n='messages.registrationLogin.socialLogin'></vx-social-login>
      </div>
      <div class='col-sm-6'>
        <vx-guest-checkout :i18n='messages.checkout.guestCheckout'></vx-guest-checkout>
      </div>
    </div>
    <h3 class='mb-xs-4'>Login Test for Form Submit</h3>
  </div>
  <vx-slider :product-data='products' title='Customer Also Viewed'></vx-slider>
</div>
</template>

<script>
import vxLogin from '../components/registration-login/vx-login/vx-login.vue';
import vxSocialLogin from '../components/registration-login/vx-social-login/vx-social-login.vue';
import vxSlider from '../components/pdp/vx-slider/vx-slider.vue';

import vxGuestCheckout from '../components/checkout/vx-guest-checkout/vx-guest-checkout.vue';
import messages from '../locale/messages';

export default {
  name: 'login-page',
  components: {
    vxLogin,
    vxSocialLogin,
    'vx-slider': vxSlider,
    vxGuestCheckout,
  },
  data() {
    return {
      msg: 'User Login',
      messages: messages['en-US'],
      products: [{
        availableForPickup: true,
        averageRating: 2.5625,
        classifications: [{
          code: '41',
          features: [{
            code: 'ElectronicsClassification/1.0/41.display, 83',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'LCD',
            }],
            name: 'Display',
            range: false,
          }],
          name: 'Display',
        }, {
          code: '42',
          features: [{
            code: 'ElectronicsClassification/1.0/42.compatible memory cards, 730',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'sd,mmc',
            }],
            name: 'Compatible memory cards',
            range: false,
          }],
          name: 'Memory',
        }],
        code: '669789',
        range: '<b>No experience necessary.</b><br/>- 5.0 MP for prints up to 20 × 30 in. (50 × 75 cm)<br/>- 3X optical zoom lens<br/>- 1.8 in. (4.6 cm) indoor/outdoor color display<br/>- Cropping, auto picture rotation, and undo-delete<br/>- On-camera Share button<br/>- Favorites feature<br/>- Internal memory plus feature',
        images: [{
          format: 'thumbnail',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyNjAzfGltYWdlL2pwZWd8aW1hZ2VzL2g3MC9oYzcvODc5NjI3NDUyNDE5MC5qcGd8ZTcwMmZhZjAzY2QyNzYwZDc0MWMxZjM2NTEwYjFhZWRkNjg5NWE1ZTA0MjJhNzk1YTViYzMwNWIwNDYwNjlkMg',
        }, {
          format: 'product',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMjMwOXxpbWFnZS9qcGVnfGltYWdlcy9oY2EvaGY3Lzg3OTYyNDgxNzg3MTguanBnfDQxOWI4Yzc3MTE0ZWRmZjQyYzJmZWFmODEyNDgxZTRiM2RjZDg5YjkzOGE1NWZhNzhlNmRmODY2ZGUwOTZiMzE',
        }],
        manufacturer: 'Kodak',
        multidimensional: false,
        name: 'C<em class=\'search-results-highlight\'>503</em>',
        price: {
          currencyIso: 'USD',
          formattedValue: '$94.69',
          priceType: 'BUY',
          value: 94.69,
        },
        priceRange: {
        },
        stock: {
          stockLevel: 4,
          stockLevelStatus: 'lowStock',
        },
        summary: 'No experience necessary, C<em class=\'search-results-highlight\'>503</em> with 5.0 MP, 3X optical zoom lens, 1.8 in. ',
        url: '/Open-Catalogue/Cameras/Digital-Cameras/Digital-Compacts/C503/p/669789',
        volumePricesFlag: false,
      }, {
        availableForPickup: true,
        averageRating: 4.444444444444445,
        classifications: [{
          code: '41',
          features: [{
            code: 'ElectronicsClassification/1.0/41.display, 83',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'TFT',
            }],
            name: 'Display',
            range: false,
          }],
          name: 'Display',
        }, {
          code: '42',
          features: [{
            code: 'ElectronicsClassification/1.0/42.compatible memory cards, 730',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'sd,sdhc',
            }],
            name: 'Compatible memory cards',
            range: false,
          }],
          name: 'Memory',
        }],
        code: '4812254',
        range: 'With the EOS 500D, every side of your story comes alive. Capture every detail in stunning 15.1 Megapixel resolution, or switch to shooting pin-sharp Full High Definition video.<br/><br/><b>15.1 MP CMOS sensor</b><br/>Ideal for those who want to create large, poster-size prints – or crop images to perfection without losing any of the detail necessary for printing – Canon’s 15.1MP CMOS sensor is outstanding in low light, and produces images with incredibly low noise.<br/><br/><b>Full HD movie recording</b><br/>The EOS 500D combines exceptional still shooting with the ability to shoot Full HD (1080p) video. For occasions when larger amounts of footage need to be stored on a memory card, users can also choose to shoot at 720p or VGA resolution. An HDMI connection allows High Definition playback of footage and images on any HDTV. <br/><br/><b>High ISO for low light</b><br/>When light levels fall, the EOS 500D offers an ISO range of up to 3200 – expandable to 12800 for those environments where using flash is undesirable. <br/><br/><b>3.4fps with up to 170 JPEG burst</b><br/>The EOS 500D features shooting speeds of up to 3.4fps in continuous bursts of up to 170 large JPEG images (9 RAW). <br/><br/><b>3.0” Clear View LCD with Live View mode</b><br/>Detailed checks on images and video are possible with a 3.0” ClearView LCD screen, which features an ultra-high 920,000 dots for increased clarity. Switch to Live View mode and use the real-time visual feed to shoot from awkward angles.<br/><br/><b>9-point AF system</b><br/>A high-precision AF system employs 9 AF points AF Points are located throughout the frame to better accommodate off-centre subjects. For accurate focusing with fast aperture lenses, the central AF point features a high sensitivity cross-type sensor, suitable for lenses with an aperture of f/2.8 or faster.<br/><br/><b>DIGIC 4</b><br/>Canon’s DIGIC 4 processor works with the CMOS sensor to deliver 14-bit image processing, for smooth gradations and natural looking colours. DIGIC 4 also powers advanced Noise Reduction when shooting at higher ISO speeds, plus split-second startup times and near-instant image review after shooting.<br/><br/><b>EOS Integrated Cleaning System</b><br/>Canon’s built-in dust prevention system offers three ways of guarding images against the effects of dust: reduction of internal dust generation',
        images: [{
          format: 'thumbnail',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODIxfGltYWdlL2pwZWd8aW1hZ2VzL2g2Ny9oY2IvODc5NjI3MTk2ODI4Ni5qcGd8Njg0NzU4ZTAxYTliNTBkNWE3OTliMTkxMjhiOWU3NTgyZDk4MDIxNDgxZjM3NTZiODkwNTdhZGRlYTQwODJiMA',
        }, {
          format: 'product',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMjQxMHxpbWFnZS9qcGVnfGltYWdlcy9oZDgvaGQ1Lzg3OTYyNDU2MjI4MTQuanBnfDk5ZjI4ZWRjZDliMDBkNzVjMGEyMzdlNmY0NTEzZGE2NWRjNWNlYTg5MmE4Njc2ZTI5ZWY5YjBlMGE4ZWNhYTM',
        }],
        manufacturer: 'Canon',
        multidimensional: false,
        name: '500D + 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>mm</em> IS + EF-S <em class=\'search-results-highlight\'>55</em>-250 IS',
        price: {
          currencyIso: 'USD',
          formattedValue: '$927.89',
          priceType: 'BUY',
          value: 927.89,
        },
        priceRange: {
        },
        stock: {
          stockLevelStatus: 'inStock',
        },
        summary: 'EOS 500D + 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>mm</em> IS + EF-S <em class=\'search-results-highlight\'>55</em>-250 IS - 15.1 MP CMOS, Full HD (1080p), 3.4fps, 3.0” Clear View LCD, DIGIC 4, 9-point AF',
        url: '/Open-Catalogue/Cameras/Digital-Cameras/Digital-SLR/500D-%2B-18-55mm-IS-%2B-EF-S-55-250-IS/p/4812254',
        volumePricesFlag: false,
      }, {
        availableForPickup: true,
        classifications: [{
          code: '41',
          features: [{
            code: 'ElectronicsClassification/1.0/41.display, 83',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'TFT',
            }],
            name: 'Display',
            range: false,
          }],
          name: 'Display',
        }, {
          code: '42',
          features: [{
            code: 'ElectronicsClassification/1.0/42.compatible memory cards, 730',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'sd,sdhc',
            }],
            name: 'Compatible memory cards',
            range: false,
          }],
          name: 'Memory',
        }],
        code: '3555166',
        range: 'With the EOS 500D, every side of your story comes alive. Capture every detail in stunning 15.1 Megapixel resolution, or switch to shooting pin-sharp Full High Definition video.<br/><br/><b>15.1 MP CMOS sensor</b><br/>Ideal for those who want to create large, poster-size prints – or crop images to perfection without losing any of the detail necessary for printing – Canon’s 15.1MP CMOS sensor is outstanding in low light, and produces images with incredibly low noise.<br/><br/><b>Full HD movie recording</b><br/>The EOS 500D combines exceptional still shooting with the ability to shoot Full HD (1080p) video. For occasions when larger amounts of footage need to be stored on a memory card, users can also choose to shoot at 720p or VGA resolution. An HDMI connection allows High Definition playback of footage and images on any HDTV. <br/><br/><b>High ISO for low light</b><br/>When light levels fall, the EOS 500D offers an ISO range of up to 3200 – expandable to 12800 for those environments where using flash is undesirable. <br/><br/><b>3.4fps with up to 170 JPEG burst</b><br/>The EOS 500D features shooting speeds of up to 3.4fps in continuous bursts of up to 170 large JPEG images (9 RAW). <br/><br/><b>3.0” Clear View LCD with Live View mode</b><br/>Detailed checks on images and video are possible with a 3.0” ClearView LCD screen, which features an ultra-high 920,000 dots for increased clarity. Switch to Live View mode and use the real-time visual feed to shoot from awkward angles.<br/><br/><b>9-point AF system</b><br/>A high-precision AF system employs 9 AF points AF Points are located throughout the frame to better accommodate off-centre subjects. For accurate focusing with fast aperture lenses, the central AF point features a high sensitivity cross-type sensor, suitable for lenses with an aperture of f/2.8 or faster.<br/><br/><b>DIGIC 4</b><br/>Canon’s DIGIC 4 processor works with the CMOS sensor to deliver 14-bit image processing, for smooth gradations and natural looking colours. DIGIC 4 also powers advanced Noise Reduction when shooting at higher ISO speeds, plus split-second startup times and near-instant image review after shooting.<br/><br/><b>EOS Integrated Cleaning System</b><br/>Canon’s built-in dust prevention system offers three ways of guarding images against the effects of dust: reduction of internal dust generation',
        manufacturer: 'Canon',
        multidimensional: false,
        name: 'EOS 500D + 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>mm</em> IS + EF-S <em class=\'search-results-highlight\'>55</em>-250 IS',
        price: {
          currencyIso: 'USD',
          formattedValue: '$934.41',
          priceType: 'BUY',
          value: 934.41,
        },
        priceRange: {
        },
        stock: {
          stockLevelStatus: 'inStock',
        },
        summary: 'EOS 500D + 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>mm</em> IS + EF-S <em class=\'search-results-highlight\'>55</em>-250 IS - 15.1 MP CMOS, Full HD (1080p), 3.4fps, 3.0” Clear View LCD, DIGIC 4, 9-point AF',
        url: '/Open-Catalogue/Cameras/Digital-Cameras/Digital-SLR/EOS-500D-%2B-18-55mm-IS-%2B-EF-S-55-250-IS/p/3555166',
        volumePricesFlag: false,
      }, {
        availableForPickup: true,
        classifications: [{
          code: '41',
          features: [{
            code: 'ElectronicsClassification/1.0/41.display, 83',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'TFT',
            }],
            name: 'Display',
            range: false,
          }],
          name: 'Display',
        }, {
          code: '42',
          features: [{
            code: 'ElectronicsClassification/1.0/42.compatible memory cards, 730',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'sd,sdhc',
            }],
            name: 'Compatible memory cards',
            range: false,
          }],
          name: 'Memory',
        }],
        code: '3708646',
        range: 'With the EOS 500D, every side of your story comes alive. Capture every detail in stunning 15.1 Megapixel resolution, or switch to shooting pin-sharp Full High Definition video.<br/><br/><b>15.1 MP CMOS sensor</b><br/>Ideal for those who want to create large, poster-size prints – or crop images to perfection without losing any of the detail necessary for printing – Canon’s 15.1MP CMOS sensor is outstanding in low light, and produces images with incredibly low noise.<br/><br/><b>Full HD movie recording</b><br/>The EOS 500D combines exceptional still shooting with the ability to shoot Full HD (1080p) video. For occasions when larger amounts of footage need to be stored on a memory card, users can also choose to shoot at 720p or VGA resolution. An HDMI connection allows High Definition playback of footage and images on any HDTV. <br/><br/><b>High ISO for low light</b><br/>When light levels fall, the EOS 500D offers an ISO range of up to 3200 – expandable to 12800 for those environments where using flash is undesirable. <br/><br/><b>3.4fps with up to 170 JPEG burst</b><br/>The EOS 500D features shooting speeds of up to 3.4fps in continuous bursts of up to 170 large JPEG images (9 RAW). <br/><br/><b>3.0” Clear View LCD with Live View mode</b><br/>Detailed checks on images and video are possible with a 3.0” ClearView LCD screen, which features an ultra-high 920,000 dots for increased clarity. Switch to Live View mode and use the real-time visual feed to shoot from awkward angles.<br/><br/><b>9-point AF system</b><br/>A high-precision AF system employs 9 AF points AF Points are located throughout the frame to better accommodate off-centre subjects. For accurate focusing with fast aperture lenses, the central AF point features a high sensitivity cross-type sensor, suitable for lenses with an aperture of f/2.8 or faster.<br/><br/><b>DIGIC 4</b><br/>Canon’s DIGIC 4 processor works with the CMOS sensor to deliver 14-bit image processing, for smooth gradations and natural looking colours. DIGIC 4 also powers advanced Noise Reduction when shooting at higher ISO speeds, plus split-second startup times and near-instant image review after shooting.<br/><br/><b>EOS Integrated Cleaning System</b><br/>Canon’s built-in dust prevention system offers three ways of guarding images against the effects of dust: reduction of internal dust generation',
        manufacturer: 'Canon',
        multidimensional: false,
        name: 'EOS 500D + 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>mm</em> IS + EF-S <em class=\'search-results-highlight\'>55</em>-250 IS',
        price: {
          currencyIso: 'USD',
          formattedValue: '$968.89',
          priceType: 'BUY',
          value: 968.89,
        },
        priceRange: {
        },
        stock: {
          stockLevelStatus: 'inStock',
        },
        summary: 'EOS 500D + 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>mm</em> IS + EF-S <em class=\'search-results-highlight\'>55</em>-250 IS - 15.1 MP CMOS, Full HD (1080p), 3.4fps, 3.0” Clear View LCD, DIGIC 4, 9-point AF',
        url: '/Open-Catalogue/Cameras/Digital-Cameras/Digital-SLR/EOS-500D-%2B-18-55mm-IS-%2B-EF-S-55-250-IS/p/3708646',
        volumePricesFlag: false,
      }, {
        availableForPickup: true,
        averageRating: 2.4705882352941178,
        classifications: [{
          code: '41',
          features: [{
            code: 'ElectronicsClassification/1.0/41.display, 83',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'TFT',
            }],
            name: 'Display',
            range: false,
          }],
          name: 'Display',
        }, {
          code: '42',
          features: [{
            code: 'ElectronicsClassification/1.0/42.compatible memory cards, 730',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'sd',
            }],
            name: 'Compatible memory cards',
            range: false,
          }],
          name: 'Memory',
        }],
        code: '3557133',
        range: 'With the EOS 500D, every side of your story comes alive. Capture every detail in stunning 15.1 Megapixel resolution, or switch to shooting pin-sharp Full High Definition video.<br/><br/><b>15.1 MP CMOS sensor</b><br/>Ideal for those who want to create large, poster-size prints – or crop images to perfection without losing any of the detail necessary for printing – Canon’s 15.1MP CMOS sensor is outstanding in low light, and produces images with incredibly low noise.<br/><br/><b>Full HD movie recording</b><br/>The EOS 500D combines exceptional still shooting with the ability to shoot Full HD (1080p) video. For occasions when larger amounts of footage need to be stored on a memory card, users can also choose to shoot at 720p or VGA resolution. An HDMI connection allows High Definition playback of footage and images on any HDTV. <br/><br/><b>High ISO for low light</b><br/>When light levels fall, the EOS 500D offers an ISO range of up to 3200 – expandable to 12800 for those environments where using flash is undesirable. <br/><br/><b>3.4fps with up to 170 JPEG burst</b><br/>The EOS 500D features shooting speeds of up to 3.4fps in continuous bursts of up to 170 large JPEG images (9 RAW). <br/><br/><b>3.0” Clear View LCD with Live View mode</b><br/>Detailed checks on images and video are possible with a 3.0” ClearView LCD screen, which features an ultra-high 920,000 dots for increased clarity. Switch to Live View mode and use the real-time visual feed to shoot from awkward angles.<br/><br/><b>9-point AF system</b><br/>A high-precision AF system employs 9 AF points AF Points are located throughout the frame to better accommodate off-centre subjects. For accurate focusing with fast aperture lenses, the central AF point features a high sensitivity cross-type sensor, suitable for lenses with an aperture of f/2.8 or faster.<br/><br/><b>DIGIC 4</b><br/>Canon’s DIGIC 4 processor works with the CMOS sensor to deliver 14-bit image processing, for smooth gradations and natural looking colours. DIGIC 4 also powers advanced Noise Reduction when shooting at higher ISO speeds, plus split-second startup times and near-instant image review after shooting.<br/><br/><b>EOS Integrated Cleaning System</b><br/>Canon’s built-in dust prevention system offers three ways of guarding images against the effects of dust: reduction of internal dust generation',
        manufacturer: 'Canon',
        multidimensional: false,
        name: 'EOS 500D + EF-S 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>IS</em> + EF-S <em class=\'search-results-highlight\'>55</em>-250IS, kit',
        price: {
          currencyIso: 'USD',
          formattedValue: '$929.88',
          priceType: 'BUY',
          value: 929.88,
        },
        priceRange: {
        },
        stock: {
          stockLevelStatus: 'inStock',
        },
        summary: 'EOS 500D + EF-S 18-<em class=\'search-results-highlight\'>55</em><em class=\'search-results-highlight\'>IS</em> + EF-S <em class=\'search-results-highlight\'>55</em>-250IS, kit',
        url: '/Open-Catalogue/Cameras/Digital-Cameras/Digital-SLR/EOS-500D-%2B-EF-S-18-55IS-%2B-EF-S-55-250IS%2C-kit/p/3557133',
        volumePricesFlag: false,
      }, {
        availableForPickup: true,
        averageRating: 2.5,
        code: '791635',
        range: 'This new Ring Lite for close-up and macro shooting is loaded with features and capabilities. Now with Canon\'s E-TTL, it takes full advantage of the flash performance of the EOS-1v, EOS-3, and other E-TTL cameras. Circular twin flash tubes can fire at even power, or varied between them over a six-stop range, and one or more 550EX Speedlites can be used as wireless slaves along with the MR-14EX. The controller unit now has an illuminated full-info LCD panel and its accepts optional hi-capacity battery packs.',
        images: [{
          format: 'thumbnail',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyNDMzfGltYWdlL2pwZWd8aW1hZ2VzL2hjZS9oNjYvODc5NjI3NjQyNDczNC5qcGd8NjAwNDIxYmRlZWJiNDAxNmZjZThmOTA5ODYwNTQwZTQyMDc1NmJhZGM3MjEzMGNiNDFhNGYyZjFkYTk1NTdiMw',
        }, {
          format: 'product',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3w5NzExfGltYWdlL2pwZWd8aW1hZ2VzL2hlNS9oYWYvODc5NjI1MDA3OTI2Mi5qcGd8MGVjZjg5MjNkZDMwNjlkOTcwZjJmMjhiMGJmZDY0Njk3NTNkZmY4NzQyZDU1YjVmOWY2NWNlNmNmYWIxNWRmNw',
        }],
        manufacturer: 'Canon',
        multidimensional: false,
        name: 'Macro Ring Lite MR-14 EX',
        price: {
          currencyIso: 'USD',
          formattedValue: '$569.88',
          priceType: 'BUY',
          value: 569.88,
        },
        priceRange: {
        },
        stock: {
          stockLevelStatus: 'inStock',
        },
        summary: 'Macro Ring Lite MR-14EX',
        url: '/Open-Catalogue/Cameras/Camera-Accessories-%26-Supplies/Camera-Flashes/Macro-Ring-Lite-MR-14-EX/p/791635',
        volumePricesFlag: false,
      }, {
        availableForPickup: true,
        averageRating: 3.7,
        classifications: [{
          code: '41',
          features: [{
            code: 'ElectronicsClassification/1.0/41.display, 83',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'TFT',
            }],
            name: 'Display',
            range: false,
          }],
          name: 'Display',
        }, {
          code: '42',
          features: [{
            code: 'ElectronicsClassification/1.0/42.compatible memory cards, 730',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'sd',
            }],
            name: 'Compatible memory cards',
            range: false,
          }],
          name: 'Memory',
        }],
        code: '2053367',
        range: 'With the EOS 500D, every side of your story comes alive. Capture every detail in stunning 15.1 Megapixel resolution, or switch to shooting pin-sharp Full High Definition video.<br/><br/><b>15.1 MP CMOS sensor</b><br/>Ideal for those who want to create large, poster-size prints – or crop images to perfection without losing any of the detail necessary for printing – Canon’s 15.1MP CMOS sensor is outstanding in low light, and produces images with incredibly low noise.<br/><br/><b>Full HD movie recording</b><br/>The EOS 500D combines exceptional still shooting with the ability to shoot Full HD (1080p) video. For occasions when larger amounts of footage need to be stored on a memory card, users can also choose to shoot at 720p or VGA resolution. An HDMI connection allows High Definition playback of footage and images on any HDTV. <br/><br/><b>High ISO for low light</b><br/>When light levels fall, the EOS 500D offers an ISO range of up to 3200 – expandable to 12800 for those environments where using flash is undesirable. <br/><br/><b>3.4fps with up to 170 JPEG burst</b><br/>The EOS 500D features shooting speeds of up to 3.4fps in continuous bursts of up to 170 large JPEG images (9 RAW). <br/><br/><b>3.0” Clear View LCD with Live View mode</b><br/>Detailed checks on images and video are possible with a 3.0” ClearView LCD screen, which features an ultra-high 920,000 dots for increased clarity. Switch to Live View mode and use the real-time visual feed to shoot from awkward angles.<br/><br/><b>9-point AF system</b><br/>A high-precision AF system employs 9 AF points AF Points are located throughout the frame to better accommodate off-centre subjects. For accurate focusing with fast aperture lenses, the central AF point features a high sensitivity cross-type sensor, suitable for lenses with an aperture of f/2.8 or faster.<br/><br/><b>DIGIC 4</b><br/>Canon’s DIGIC 4 processor works with the CMOS sensor to deliver 14-bit image processing, for smooth gradations and natural looking colours. DIGIC 4 also powers advanced Noise Reduction when shooting at higher ISO speeds, plus split-second startup times and near-instant image review after shooting.<br/><br/><b>EOS Integrated Cleaning System</b><br/>Canon’s built-in dust prevention system offers three ways of guarding images against the effects of dust: reduction of internal dust generation',
        images: [{
          format: 'thumbnail',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wyODI4fGltYWdlL2pwZWd8aW1hZ2VzL2gyYS9oYzQvODc5NjI2MjEzNzg4Ni5qcGd8YWRlZTljODFmZTYyMTgyYzQzODA1OTBhNjNlMzg2MGFkMmFhM2UxY2IyZjIxMGYyZTdhOTU4NTA2NmQ0MTIzYg',
        }, {
          format: 'product',
          imageType: 'PRIMARY',
          url: '/gpcommercewebservices/v2/medias/?context=bWFzdGVyfGltYWdlc3wxMjQwMnxpbWFnZS9qcGVnfGltYWdlcy9oNmIvaGRjLzg3OTYyMzU3OTI0MTQuanBnfDIyOTJmM2QyMWFlYTA1ZTU2YzMwODE3ZjBkZGUzYTU5ZTczYzJlOGI4Y2QwZGI1ZGMzOTk2YTA4OWE4NjU1Y2M',
        }],
        manufacturer: 'Canon',
        multidimensional: false,
        name: 'EOS 500D + EF-S 18-200mm f/3.5-<em class=\'search-results-highlight\'>5.6</em> IS, kit',
        price: {
          currencyIso: 'USD',
          formattedValue: '$2,410.42',
          priceType: 'BUY',
          value: 2410.42,
        },
        priceRange: {
        },
        stock: {
          stockLevelStatus: 'inStock',
        },
        summary: 'EOS 500D + EF-S 18-200mm f/3.5-<em class=\'search-results-highlight\'>5.6</em> IS - 15.1 MP CMOS, Full HD (1080p), 3.4fps, 3.0” Clear View LCD, DIGIC 4, 9-point AF',
        url: '/Open-Catalogue/Cameras/Digital-Cameras/Digital-SLR/EOS-500D-%2B-EF-S-18-200mm-f-3-5-5-6-IS%2C-kit/p/2053367',
        volumePricesFlag: false,
      }, {
        availableForPickup: true,
        averageRating: 4.25,
        classifications: [{
          code: '41',
          features: [{
            code: 'ElectronicsClassification/1.0/41.display, 83',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'TFT',
            }],
            name: 'Display',
            range: false,
          }],
          name: 'Display',
        }, {
          code: '42',
          features: [{
            code: 'ElectronicsClassification/1.0/42.compatible memory cards, 730',
            comparable: true,
            featureUnit: {
              name: '.',
              symbol: '.',
              unitType: '300',
            },
            featureValues: [{
              value: 'sd',
            }],
            name: 'Compatible memory cards',
            range: false,
          }],
          name: 'Memory',
        }],
        code: '2053226',
        range: 'With the EOS 500D, every side of your story comes alive. Capture every detail in stunning 15.1 Megapixel resolution, or switch to shooting pin-sharp Full High Definition video.<br/><br/><b>15.1 MP CMOS sensor</b><br/>Ideal for those who want to create large, poster-size prints – or crop images to perfection without losing any of the detail necessary for printing – Canon’s 15.1MP CMOS sensor is outstanding in low light, and produces images with incredibly low noise.<br/><br/><b>Full HD movie recording</b><br/>The EOS 500D combines exceptional still shooting with the ability to shoot Full HD (1080p) video. For occasions when larger amounts of footage need to be stored on a memory card, users can also choose to shoot at 720p or VGA resolution. An HDMI connection allows High Definition playback of footage and images on any HDTV. <br/><br/><b>High ISO for low light</b><br/>When light levels fall, the EOS 500D offers an ISO range of up to 3200 – expandable to 12800 for those environments where using flash is undesirable. <br/><br/><b>3.4fps with up to 170 JPEG burst</b><br/>The EOS 500D features shooting speeds of up to 3.4fps in continuous bursts of up to 170 large JPEG images (9 RAW). <br/><br/><b>3.0” Clear View LCD with Live View mode</b><br/>Detailed checks on images and video are possible with a 3.0” ClearView LCD screen, which features an ultra-high 920,000 dots for increased clarity. Switch to Live View mode and use the real-time visual feed to shoot from awkward angles.<br/><br/><b>9-point AF system</b><br/>A high-precision AF system employs 9 AF points AF Points are located throughout the frame to better accommodate off-centre subjects. For accurate focusing with fast aperture lenses, the central AF point features a high sensitivity cross-type sensor, suitable for lenses with an aperture of f/2.8 or faster.<br/><br/><b>DIGIC 4</b><br/>Canon’s DIGIC 4 processor works with the CMOS sensor to deliver 14-bit image processing, for smooth gradations and natural looking colours. DIGIC 4 also powers advanced Noise Reduction when shooting at higher ISO speeds, plus split-second startup times and near-instant image review after shooting.<br/><br/><b>EOS Integrated Cleaning System</b><br/>Canon’s built-in dust prevention system offers three ways of guarding images against the effects of dust: reduction of internal dust generation',
        manufacturer: 'Canon',
        multidimensional: false,
        name: 'EOS 500D + EF-S 18-<em class=\'search-results-highlight\'>55</em> IS, kit',
        price: {
          currencyIso: 'USD',
          formattedValue: '$1,538.70',
          priceType: 'BUY',
          value: 1538.7,
        },
        priceRange: {
        },
        stock: {
          stockLevelStatus: 'inStock',
        },
        summary: 'EOS 500D + EF-S 18-<em class=\'search-results-highlight\'>55</em> IS - 15.1 MP CMOS, Full HD (1080p), 3.4fps, 3.0” Clear View LCD, DIGIC 4, 9-point AF',
        url: '/Open-Catalogue/Cameras/Digital-Cameras/Digital-SLR/EOS-500D-%2B-EF-S-18-55-IS%2C-kit/p/2053226',
        volumePricesFlag: false,
      }],
    };
  },
};
</script>
