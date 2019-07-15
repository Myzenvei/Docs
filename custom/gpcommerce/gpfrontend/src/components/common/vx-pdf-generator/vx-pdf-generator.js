import * as JSPDF from 'jspdf';
import filter from 'lodash/filter';

import VueBarcode from 'vue-barcode';
import globals from '../../common/globals';
// import certificates from '../../../../assets/base64/certificates-icons';
import imgBlob from '../../../../assets/base64/gppro-icons';
import { generatePdf } from '../../common/mixins/vx-enums';

export default {
  name: 'vx-pdf-generator',
  components: {
    barcode: VueBarcode,
  },
  props: {
    name: {
      type: String,
      required: true,
    },
    pdfData: {
      type: Object,
      required: true,
    },
    hasSustainabilityPage: {
      type: String,
    },
  },
  data() {
    return {
      imgBlob: imgBlob.noImage,
      // Default export is a4 paper, portrait, using milimeters for units
      pdf: {},
      pdfDefaults: {
        barColor: '#000000',
        headingColor: '#000000',
        headerTextColorList: '#000000',
        headerTextColor: '#FFFFFF',
        borderColor: '#FAFAFA',
        footerTextColor: '#222222',
        footerBackgroundColor: '#CECECE',
        subFooterBgColor: '#FFFFFF',
        timeStampTextColor: '#FFFFFF',
        footerText: '© 2019 Georgia-Pacific Consumer Products LP. All rights reserved.',
        textColor: '#222222',
      },
      pdpProductInfo: {},
      coverPageBlob: '',
      globals,
      headingTextBoxHeight: 0,
      firstPage: true,
      generatePdf,
      featuredData: [],
      isFullDetail: false,
      uncheckedData: [],
      counter: 0,
      maxFullDetail: false,
      data: {},
      pdfInfo: {},
    };
  },
  mounted() {
    if (this.pdfData) {
      this.data = JSON.parse(JSON.stringify(this.pdfData));
    }
    // if (this.hasSustainabilityPage) {
    //   const sustainabilityImage = `${window.location.origin +
    //     globals.assetsPath}images/sustainability.jpg`;
    //   this.toDataURL(
    //     sustainabilityImage,
    //     this.updateImageSrc,
    //     'sustainabilityBlob',
    //   );
    // }
  },
  methods: {
    resetDimensions() {
      this.headingTextBoxHeight = 0;
      this.firstPage = true;
      this.featuredData = [];
    },
    createHeader(customData) {
      this.pdf.setFontSize(12);
      // Render Header Top Bar
      if (customData.isPdp) {
        this.pdf.setFillColor(0, 82, 147);
        this.pdf.setTextColor(this.pdfDefaults.headerTextColor);
      } else {
        this.pdf.setFillColor(255, 255, 255);
        this.pdf.setTextColor(this.pdfDefaults.headerTextColorList);
      }
      this.pdf.rect(0, 0, 600, 55, 'F');
      if (customData.headerText && !customData.isPdp) {
        this.pdf.setFontSize(10);
        if (customData.headerLogo) {
          this.pdf.addImage(customData.headerLogo, 'PNG', 16, 18, 120, 40);
        }
        const headerText = customData.headerText.split('*');
        this.pdf.setFontStyle('bold');
        this.pdf.text(headerText[0], 300, 20, {
          align: 'left',
        });
        this.pdf.setFontStyle('normal');
        this.pdf.text([headerText[1], headerText[2]], 300, 32, {
          align: 'left',
        });
      } else {
        // Render site logo
        if (customData.distributorImage) {
          this.pdf.addImage(customData.distributorImage, 'PNG', 16, 8, 0, 40);
        } else if (!customData.isPdp) {
          if (customData.headerLogo) {
            this.pdf.addImage(customData.headerLogo, 'PNG', 16, 8, 120, 40);
          }
        } else {
          this.pdf.addImage(imgBlob.gpProLogo, 'PNG', 16, 8, 40, 40);
        }
        if (customData.unitCompany || customData.unitAddress || customData.b2bphoneNumber || customData.b2bWebsite) {
          this.pdf.setFontSize(10);
          this.pdf.setFontStyle('bold');
          this.pdf.text(customData.unitCompany, 300, 20, {
            align: 'center',
          });
          this.pdf.setFontStyle('normal');
          const phoneNumberNwebsite = [
            ...customData.b2bphoneNumber ? [customData.b2bphoneNumber] : [],
            ...customData.b2bWebsite ? [customData.b2bWebsite] : [],
          ];
          const unitAddPhoneWebsite = [
            ...customData.unitAddress ? [customData.unitAddress] : [],
            ...phoneNumberNwebsite ? [phoneNumberNwebsite.join(' | ')] : [],
          ];
          this.pdf.text(unitAddPhoneWebsite, 300, customData.unitCompany ? 32 : 20, {
            align: 'center',
          });
        } else if (!customData.distributorImage) {
          this.pdf.setFontSize(10);
          this.pdf.setFontStyle('bold');
          this.pdf.text('GP PRO', 400, 20, {
            align: 'left',
          });
          this.pdf.setFontStyle('normal');
          this.pdf.text(['1-866-HELLO GP (435-5647)', 'www.gppro.com'], 400, 32, {
            align: 'left',
          });
        }
      }

      // Render header Strip
      if (customData.barColor && customData.barColor !== '') {
        this.pdf.setFillColor(customData.barColor);
      } else {
        this.pdf.setFillColor(this.pdfDefaults.barColor);
      }
      this.pdf.rect(0, 55, 600, 10, 'F');
    },

    // If header text entered
    createSubHeader(customData) {
      if (customData.headlineLine1 || customData.headlineLine2) {
        this.headingTextBoxHeight = 50;
        // Render Heading box
        if (customData.headlineColor !== '') {
          this.pdf.setTextColor(customData.headlineColor);
        } else {
          this.pdf.setTextColor(this.pdfDefaults.headingColor);
        }
        this.pdf.setFillColor(this.pdfDefaults.borderColor);
        this.pdf.rect(0, 65, 600, 50, 'F');
        if (customData.headlineLine1) {
          this.pdf.setFontSize(14);
          this.pdf.text(customData.headlineLine1, 25, 80, {
            align: 'left',
          });
        }
        if (customData.headlineLine2) {
          this.pdf.setFontSize(12);
          this.pdf.text(customData.headlineLine2, 25, 100, {
            align: 'left',
          });
        }
        // Render heading text bottom strip
        if (customData.barColor !== '') {
          this.pdf.setFillColor(customData.barColor);
        } else {
          this.pdf.setFillColor(this.pdfDefaults.barColor);
        }
        this.pdf.rect(0, 110, 600, 3, 'F');
      }
    },
    detailedProductName(specifications,name,left,productTop,productSplitSize,productNameSize,minNameHeight,sizeFactor){
      if (specifications && specifications.length) {
          let productName = `${name}, ${specifications}`;
          let nameHeight = this.appendProductName(productName,left,productTop,productSplitSize,productNameSize,minNameHeight,sizeFactor);
          productTop += nameHeight;
          return productTop;
      } else {
        let productName = `${name}`;
        let nameHeight = this.appendProductName(productName,left,productTop,productSplitSize,productNameSize,minNameHeight,sizeFactor);
        productTop += nameHeight;
        return productTop;
      }
    },
    appendProductName(productName,left,productTop,productSplitSize,productNameSize,minNameHeight,sizeFactor){
      const productNameBreak = this.pdf.splitTextToSize(productName, productSplitSize);
      this.pdf.text(left + 50, productTop, productNameBreak);
      return (productName.length < productNameSize ? minNameHeight : productName.length / sizeFactor * 12);
      },
    createFooter(customData) {
      const dateObj = new Date();
      const printedDate = `Printed: ${`0${dateObj.getMonth() + 1}`.slice(
        -2,
      )}/${`0${dateObj.getDate()}`.slice(-2)}/${dateObj.getFullYear()}`;
      if (customData.barColor && customData.barColor !== '') {
        this.pdf.setFillColor(customData.barColor);
      } else {
        this.pdf.setFillColor(this.pdfDefaults.barColor);
      }
      this.pdf.rect(0, 765, 600, 10, 'F');
      this.pdf.setTextColor(this.pdfDefaults.timeStampTextColor);
      this.pdf.setFontSize(8);
      this.pdf.text(printedDate, 510, 772);
      // Render Sub Footer
      this.pdf.setFillColor(this.pdfDefaults.subFooterBgColor);
      this.pdf.setTextColor(this.pdfDefaults.footerTextColor);
      this.pdf.rect(0, 775, 600, 50, 'F');
      this.pdf.setFontSize(8);
      this.pdf.setLineWidth(0.1);
      this.pdf.line(150, 780, 150, 820);
      let subFooterTop = 790;
      if (customData.nameOnPdf && customData.nameOnPdf !== '') {
        this.pdf.text(customData.nameOnPdf, 340, subFooterTop, {
          align: 'center',
        });
        subFooterTop += 14;
      }
      const emailAddress = !customData.emailAddress ? '' : customData.emailAddress;
      let phoneNumber = '';
      if (customData.phoneNumber && customData.phoneNumber !== '') {
        const phonefir = customData.phoneNumber.substring(0, 3);
        const phoneSec = customData.phoneNumber.substring(3, 6);
        const phoneThird = customData.phoneNumber.substring(6);
        phoneNumber = `+1 ${phonefir}-${phoneSec}-${phoneThird}`;
      }
      this.pdf.text(
        `${
          emailAddress
        }  ${phoneNumber}`,
        340,
        subFooterTop, {
          align: 'center',
        },
      );
      subFooterTop += 14;
      if (customData.message && customData.message !== '') {
        this.pdf.text(customData.message, 340, subFooterTop, {
          align: 'center',
        });
        subFooterTop += 14;
      }
      // Render Footer
      this.pdf.setFontSize(8);
      this.pdf.setFillColor(this.pdfDefaults.footerBackgroundColor);
      this.pdf.rect(0, 825, 600, 16, 'F');
      this.pdf.setTextColor(this.pdfDefaults.footerTextColor);
      this.pdf.text(this.pdfDefaults.footerText, 280, 835, {
        align: 'center',
      });
      if (customData.distributorImage) {
        if (!customData.isPdp) {
          if (customData.headerLogo) {
            this.pdf.addImage(customData.headerLogo, 'PNG', 16, 781, 120, 40);
          }
        } else {
          this.pdf.addImage(imgBlob.gpProLogo, 'PNG', 16, 781, 40, 40);
        }
      }
    },
    renderSustainability(pdfInfo) {
      const width = this.pdf.internal.pageSize.getWidth();
      const height = this.pdf.internal.pageSize.getHeight();
      let sustainabilityImage = '';
      pdfInfo.pdfCoverImages.entry.map((coverPage) => {
        if (coverPage.key === 'sustainability') {
          sustainabilityImage = coverPage.value;
        }
      });
      this.pdf.addPage();
      this.pdf.addImage(
        `data:image/jpeg;base64,${sustainabilityImage}`,
        this.getExtension(sustainabilityImage),
        0,
        0,
        width,
        height,
      );
    },
    renderCoverPage(pdfInfo) {
      const width = this.pdf.internal.pageSize.getWidth();
      const height = this.pdf.internal.pageSize.getHeight();
      let coverImage = '';
      pdfInfo.pdfCoverImages.entry.map((coverPage) => {
        if (pdfInfo.coverPage === coverPage.key) {
          coverImage = coverPage.value;
        }
      });
      this.pdf.addImage(
        `data:image/jpeg;base64,${coverImage}`,
        this.getExtension(coverImage),
        0,
        0,
        width,
        height,
      );
      this.pdf.addPage();
    },
    savePdf(pdfInfo) {
      if (this.hasSustainabilityPage) {
        this.renderSustainability(pdfInfo);
      }
      this.pdf.save(`${this.name}.pdf`);
      if (this.pdfData) {
        this.data = JSON.parse(JSON.stringify(this.pdfData));
      }
    },
    createPDFInstance() {
      this.pdf = new JSPDF('p', 'pt', 'a4');
    },
    createPdfPDPFormat(pdfInfo) {
      this.pdfInfo = pdfInfo;
      this.createPDFInstance();
      this.pdpProductInfo = this.data;
      const fromPdp = true;
      this.createHeader(pdfInfo);
      this.createSubHeader(pdfInfo);
      this.createFooter(pdfInfo);
      this.createLeftSection(fromPdp);
      this.createRightSection(fromPdp);
      this.savePdf(pdfInfo);
    },
    createPdfPDPBase64(pdfInfo) {
      this.pdfInfo = pdfInfo;
      this.createPDFInstance();
      this.pdpProductInfo = this.data;
      const fromPdp = true;
      this.createHeader({ isPdp: fromPdp });
      this.createSubHeader({});
      this.createFooter({ isPdp: fromPdp });
      this.createLeftSection(fromPdp);
      this.createRightSection(fromPdp);
      if (this.hasSustainabilityPage) {
        this.renderSustainability();
      }
      return btoa(this.pdf.output());
    },
    createListPdfPDPFormat(pdfInfo) {
      this.pdfInfo = pdfInfo;
      this.resetDimensions();
      this.createPDFInstance();
      const productLength = this.data.wishlistEntries.length;
      if (pdfInfo.coverPage) {
        this.renderCoverPage(pdfInfo);
      }
      this.data.wishlistEntries.map((pdpProductInfo, index) => {
        this.pdpProductInfo = pdpProductInfo.product;
        this.pdpProductInfo.customAttr1Value = pdpProductInfo.customAttr1Value;
        this.pdpProductInfo.customAttr2Value = pdpProductInfo.customAttr2Value;
        this.pdpProductInfo.customAttr3Value = pdpProductInfo.customAttr3Value;
        this.createHeader(pdfInfo);
        if (pdfInfo.onlyOnFirstPage && this.firstPage) {
          this.createSubHeader(pdfInfo);
          this.firstPage = false;
        } else if (!pdfInfo.onlyOnFirstPage) {
          this.createSubHeader(pdfInfo);
        } else {
          this.headingTextBoxHeight = 0;
        }
        this.createFooter(pdfInfo);
        this.createLeftSection();
        this.createRightSection();
        if (productLength === index + 1) {
          this.savePdf(pdfInfo);
        } else {
          this.pdf.addPage();
        }
      });
    },
    createPdfBulkPdp() {},
    toDataURL(url, callback, flag) {
      const xhr = new XMLHttpRequest();
      xhr.onload = function() {
        const reader = new FileReader();
        reader.onloadend = function() {
          callback(reader.result, flag);
        };
        reader.readAsDataURL(xhr.response);
      };
      xhr.open('GET', url);
      xhr.responseType = 'blob';
      xhr.send();
    },
    updatedImageSrc(response, type) {
      if (type === generatePdf.coverPageBlob) {
        this.coverPageBlob = response;
      }
      this.renderCoverPage();
    },
    updateImageSrc(response, type) {
      if (type === generatePdf.imgBlob) {
        this.imgBlob = response;
      }
    },
    getCertificateImage(key, pdfInfo) {
      let imageKey = '';
      if (
        pdfInfo.gpCertificationsImages &&
        pdfInfo.gpCertificationsImages.entry
      ) {
        pdfInfo.gpCertificationsImages.entry.map((certificate) => {
          if (key === certificate.key) {
            imageKey = `data:image/jpeg;base64,${certificate.value}`;
          }
        });
      }
      return imageKey;
    },
    generatePdfBarcode(upcValue) {
      if (upcValue && (upcValue.length === 12 || upcValue.length === 11)) {
        JsBarcode('#itf', upcValue, {
          format: 'upc',
        });
        const barcodeImg = document.querySelector('img#itf');
        this.pdf.addImage(
          barcodeImg.src,
          'JPEG',
          25,
          this.headingTextBoxHeight + 660,
          150,
          40,
        );
      }
    },
    getExtension(name) {
      let extension = 'jpeg';
      if (name.includes('image/png')) {
        extension = 'png';
      }
      return extension;
    },
    createLeftSection(fromPdp) {
      // const totalPages = pdf.internal.getNumberOfPages();
      this.pdf.setDrawColor(0);
      const id = `#${this.pdpProductInfo.code}`;
      const source2 = fromPdp ? $('#left-section')[0] : $(id)[0];
      const source3 = $('#features')[0];
      const source4 = $('#feature-heading')[0];
      const marginsForLeftSection = {
        top: this.headingTextBoxHeight + 75,
        bottom: 0,
        left: 25,
        width: 250,
      };
      const renderFeaturesHeading = (logoTop) => {
        this.pdf.fromHTML(
          source4, // HTML string or DOM elem ref.
          marginsForLeftSection.left, // x coord
          logoTop + 15, // y coord
          {
            width: marginsForLeftSection.width, // max width of content on PDF
            elementHandlers: () => {},
          },
          marginsForLeftSection,
        );
      };
      const renderFeatures = (logoTop) => {
        this.pdf.fromHTML(
          source3, // HTML string or DOM elem ref.
          marginsForLeftSection.left, // x coord
          logoTop + 40, // y coord
          {
            width: marginsForLeftSection.width, // max width of content on PDF
            elementHandlers: () => {},
          },
          marginsForLeftSection,
        );
      };

      this.pdf.fromHTML(
        source2, // HTML string or DOM elem ref.
        marginsForLeftSection.left, // x coord
        marginsForLeftSection.top, // y coord
        {
          width: marginsForLeftSection.width, // max width of content on PDF
          elementHandlers: () => {},
        },
        marginsForLeftSection,
      );
      // render certification logos

      let logoTop = this.headingTextBoxHeight + 250;
      let logoLeft = 25;
      if (
        this.pdpProductInfo.description &&
        this.pdpProductInfo.description.length > 0
      ) {
        logoTop =
          this.headingTextBoxHeight +
          150 +
          (this.pdpProductInfo.description.length > 500
            ? 500
            : this.pdpProductInfo.description.length) /
            50 *
            12;
      }
      if (this.pdpProductInfo.priceText) {
        this.pdf.setFontStyle('bold');
        this.pdf.text(logoLeft, logoTop + 10, this.pdpProductInfo.priceText);
        logoTop += 20;
      }
      if (this.pdpProductInfo.gpCertifications) {
        for (let i = 0; i < this.pdpProductInfo.gpCertifications.length; i++) {
          const image = this.getCertificateImage(
            this.pdpProductInfo.gpCertifications[i].id,
            this.pdfInfo,
          );
          this.pdf.addImage(
            image,
            this.getExtension(image),
            logoLeft,
            logoTop,
            30,
            30,
          );
          logoLeft += 40;
        }
        logoTop += 30;
      }
      renderFeatures(logoTop);
      renderFeaturesHeading(logoTop);
      if (this.pdpProductInfo.upc) {
        this.generatePdfBarcode(this.pdpProductInfo.upc);
      }
    },
    renderImage(param) {
      const { id, src } = param;
      const img = id ? document.querySelector(id).src : src;
      this.toDataURL(img, this.updateImageSrc, 'imgBlob');
    },
    createRightSection(fromPdp) {
      let top = this.headingTextBoxHeight + 170;
      const imgData =
        this.pdpProductInfo.encodedImages &&
        this.pdpProductInfo.encodedImages[0]
          ? `data:image/jpeg;base64,${this.pdpProductInfo.encodedImages[0]}`
          : imgBlob.noImage;
      if (fromPdp) {
        this.pdf.addImage(
          imgData,
          this.getExtension(imgData),
          410,
          this.headingTextBoxHeight + 90,
          80,
          80,
        );
      } else {
        this.pdf.addImage(
          imgData,
          this.getExtension(imgData),
          410,
          this.headingTextBoxHeight + 90,
          80,
          80,
        );
      }

      if (
        this.pdpProductInfo.specifications &&
        this.pdpProductInfo.specifications.length > 0
      ) {
        for (let i = 0; i < this.pdpProductInfo.specifications.length; i++) {
          const section = this.pdpProductInfo.specifications[i].subCategories;
          this.pdf.setDrawColor(0);
          this.pdf.setFontSize(10);
          this.pdf.setFillColor(211, 211, 211);
          top += 10;
          this.pdf.rect(330, top, 250, 20, 'F');
          this.pdf.setTextColor('#222222');
          top += 15;
          this.pdf.setFontStyle('bold');
          this.pdf.text(
            340,
            top,
            this.pdpProductInfo.specifications[i].categoryLabel.toUpperCase(),
          );
          top += 4;
          this.pdf.setFontStyle('normal');
          for (let j = 0; j < section.length; j++) {
            top += 15;
            this.pdf.setFontSize(8);
            this.pdf.text(340, top, section[j].attributeLabel);
            this.pdf.text(470, top, section[j].columnValues[0]);
            if (
              this.pdpProductInfo.specifications[i].categoryLabel ===
                generatePdf.productDetails &&
              j === section.length - 1
            ) {
              if (
                this.pdpProductInfo.customAttr1Value &&
                this.pdpProductInfo.customAttr1Value !== ''
              ) {
                top += 15;
                this.pdf.text(340, top, this.data.customAttr1);
                this.pdf.text(470, top, this.pdpProductInfo.customAttr1Value);
              }
              if (
                this.pdpProductInfo.customAttr2Value &&
                this.pdpProductInfo.customAttr2Value !== ''
              ) {
                top += 15;
                this.pdf.text(340, top, this.data.customAttr2);
                this.pdf.text(470, top, this.pdpProductInfo.customAttr2Value);
              }
              if (
                this.pdpProductInfo.customAttr3Value &&
                this.pdpProductInfo.customAttr3Value !== ''
              ) {
                top += 15;
                this.pdf.text(340, top, this.data.customAttr3);
                this.pdf.text(470, top, this.pdpProductInfo.customAttr3Value);
              }
            }
          }
        }
      }
      // Render Right side box
      this.pdf.setDrawColor('#D3D3D3');
      this.pdf.rect(330, this.headingTextBoxHeight + 80, 250, 630);
    },
    createOneColumnFormat(pdfInfo) {
      this.resetDimensions();
      this.createPDFInstance();
      if (pdfInfo.coverPage) {
        this.renderCoverPage(pdfInfo);
      }
      this.addFormattingOneColumn(pdfInfo);
      this.addContentOneColumn(pdfInfo);
      this.savePdf(pdfInfo);
    },
    addFormattingOneColumn(pdfInfo) {
      this.createHeader(pdfInfo);
      if (pdfInfo.onlyOnFirstPage && this.firstPage) {
        this.createSubHeader(pdfInfo);
        this.firstPage = false;
      } else if (!pdfInfo.onlyOnFirstPage) {
        this.createSubHeader(pdfInfo);
      } else {
        this.headingTextBoxHeight = 0;
      }
      this.createFooter(pdfInfo);
    },
    addContentOneColumn(pdfInfo) {
      let imgData = '';
      let left = 35;
      let splitTitle = '';
      let categoryHeight = 0;
      let productTop = 100 + this.headingTextBoxHeight;
      let logoTop = 0;
      let logoLeft = 85;
      let nameHeight = 0;
      let productSellingHeight = 0;
      let productCounter = 0;
      let newCategoryPage = false;
      let productSplitSize = 495;
      let productNameSize = 100;
      let minNameHeight = 10;
      let sizeFactor = 40;
      this.data.wishlistEntriesGroup.map((category) => {
        if (category.value.wishlistEntryGroup.length > 0) {
          if (productCounter !== 0 && productCounter % 4 === 0) {
            this.pdf.addPage();
            newCategoryPage = true;
            this.addFormattingOneColumn(pdfInfo);
            productTop = 120 + this.headingTextBoxHeight;
            left = 35;
          }
          if (
            pdfInfo.categoryDescription &&
            category.value.wishlistEntryGroup[0].product.categories[0]
              .description
          ) {
            categoryHeight =
              category.value.wishlistEntryGroup[0].product.categories[0].name
                .length < 105 &&
              category.value.wishlistEntryGroup[0].product.categories[0]
                .description.length < 110
                ? 35
                : Math.ceil(
                  (category.value.wishlistEntryGroup[0].product.categories[0]
                    .name.length +
                      category.value.wishlistEntryGroup[0].product.categories[0]
                        .description.length) /
                      127,
                ) *
                    8 +
                  35;
          } else {
            categoryHeight =
              category.value.wishlistEntryGroup[0].product.categories[0].name
                .length < 38
                ? 25
                : category.value.wishlistEntryGroup[0].product.categories[0]
                  .name.length /
                  40 *
                  12;
          }
          this.pdf.setFillColor(211, 211, 211);
          this.pdf.rect(left - 5, productTop - 15, 500, categoryHeight, 'F');
          this.pdf.setFontSize(10);
          this.pdf.setFontStyle('bold');
          this.pdf.text(
            left,
            productTop,
            category.value.wishlistEntryGroup[0].product.categories[0].name,
          );
          this.pdf.setFontSize(8);
          productTop += 10;
          this.pdf.setFontStyle('normal');
          if (
            pdfInfo.categoryDescription &&
            category.value.wishlistEntryGroup[0].product.categories[0]
              .description
          ) {
            splitTitle = this.pdf.splitTextToSize(
              category.value.wishlistEntryGroup[0].product.categories[0]
                .description,
              495,
            );
            this.pdf.text(35, productTop, splitTitle);
          }
          productTop += categoryHeight - 10;
          category.value.wishlistEntryGroup.map((product) => {
            imgData =
              product.product.encodedImages && product.product.encodedImages[0]
                ? `data:image/jpeg;base64,${product.product.encodedImages[0]}`
                : imgBlob.noImage;
            if (productCounter % 4 === 0 && !newCategoryPage) {
              if (productCounter !== 0) {
                this.pdf.addPage();
                this.addFormattingOneColumn(pdfInfo);
                productTop = 120 + this.headingTextBoxHeight;
                left = 35;
              }
            }
            this.pdf.addImage(
              imgData,
              'JPEG',
              left - 5,
              productTop - 5,
              50,
              50,
            );
            this.pdf.setFontStyle('bold');
            this.pdf.text(
              left + 50,
              productTop,
              `Mfg # ${product.product.code}`,
            );
            if (product.product.cmirCode) {
              this.pdf.setTextColor(150);
              this.pdf.text(left + 115, productTop, '|');
              this.pdf.setTextColor(0);
              this.pdf.text(
                left + 130,
                productTop,
                `${product.product.cmirCode}`,
              );
            }
            productTop += 20;
            if (
              product.product &&
              product.product.priceText &&
              product.product.priceText !== ''
            ) {
              this.pdf.setFontStyle('bold');
              this.pdf.text(
                left + 50,
                productTop + 10,
                product.product.priceText,
              );
            }
            productTop += 20;
            this.pdf.setFontStyle('normal');
            if (product.customAttr1Value && product.customAttr1Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr1} : ${product.customAttr1Value}`,
              );
              productTop += 10;
            }
            if (product.customAttr2Value && product.customAttr2Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr2}  : ${product.customAttr2Value}`,
              );
              productTop += 10;
            }
            if (product.customAttr3Value && product.customAttr3Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr3}  : ${product.customAttr3Value}`,
              );
              productTop += 10;
            }
            let specifications = product.product.displayAttributes;
            let name = product.product.name;
            productTop = this.detailedProductName(specifications,name,left,productTop,productSplitSize,productNameSize,minNameHeight,sizeFactor);
            if (
              pdfInfo.productSellingStatement &&
              product.product.sellingstmt
            ) {
              splitTitle = this.pdf.splitTextToSize(
                product.product.sellingstmt,
                110,
              );
              this.pdf.text(left + 50, productTop, splitTitle);
              productSellingHeight =
                product.product.sellingstmt.length < 50
                  ? 15
                  : product.product.sellingstmt.length / 30 * 12;
              productTop += productSellingHeight;
              logoTop = productTop;
              productTop += 40;
            } else {
              logoTop = productTop;
              productTop += 40;
            }
            if (product.product.gpCertifications) {
              for (
                let i = 0;
                i < product.product.gpCertifications.length;
                i++
              ) {
                const image = this.getCertificateImage(
                  product.product.gpCertifications[i].id,
                  pdfInfo,
                );
                this.pdf.addImage(
                  image,
                  this.getExtension(image),
                  logoLeft,
                  logoTop,
                  15,
                  15,
                );
                logoLeft += 20;
              }
              logoTop += 50;
              logoLeft = 85;
            }
            productCounter += 1;
          });
        }
      });
    },

    createThreeColumnFormat(pdfInfo) {
      this.resetDimensions();
      this.createPDFInstance();
      if (pdfInfo.coverPage) {
        this.renderCoverPage(pdfInfo);
      }
      this.addFormattingThreeColumn(pdfInfo);
      this.addContentThreeColumn(pdfInfo);
      this.savePdf(pdfInfo);
    },
    addFormattingThreeColumn(pdfInfo) {
      this.createHeader(pdfInfo);
      if (pdfInfo.onlyOnFirstPage && this.firstPage) {
        this.createSubHeader(pdfInfo);
        this.firstPage = false;
      } else if (!pdfInfo.onlyOnFirstPage) {
        this.createSubHeader(pdfInfo);
      } else {
        this.headingTextBoxHeight = 0;
      }
      this.createFooter(pdfInfo);
      this.pdf.setDrawColor('#D3D3D3');
      this.pdf.setLineWidth(0.1);
      this.pdf.line(205, this.headingTextBoxHeight + 100, 205, 750);
      this.pdf.line(385, this.headingTextBoxHeight + 100, 385, 750);
      this.pdf.rect(
        24,
        this.headingTextBoxHeight + 100,
        540,
        this.headingTextBoxHeight < 50 ? 650 : this.headingTextBoxHeight + 550,
      );
    },
    addContentThreeColumn(pdfInfo) {
      let productTop = 120 + this.headingTextBoxHeight;
      let left = 35;
      let newPage = false;
      let imgData = '';
      let splitTitle = '';
      let categoryHeight = 0;
      let logoTop = 0;
      let logoLeft = 85;
      let nameHeight = 0;
      let productSellingHeight = 0;
      let productCounter = 0;
      let categoryLeft = false;
      let newCategoryPage = false;
      let productSplitSize = 110;
      let productNameSize = 30;
      let minNameHeight = 10;
      let sizeFactor = 30;
      this.data.wishlistEntriesGroup.map((category) => {
        if (category.value.wishlistEntryGroup.length > 0) {
          if (productCounter !== 0 && productCounter % 4 === 0) {
            if (productCounter % 12 !== 0) {
              left += 180;
              logoLeft = left + 50;
              productTop = 120 + this.headingTextBoxHeight;
              categoryLeft = true;
            } else {
              this.pdf.addPage();
              newCategoryPage = true;
              this.addFormattingThreeColumn(pdfInfo);
              newPage = true;
              productTop = 120 + this.headingTextBoxHeight;
              left = 35;
              logoLeft = 85;
            }
          }
          if (
            pdfInfo.categoryDescription &&
            category.value.wishlistEntryGroup[0].product.categories[0]
              .description
          ) {
            categoryHeight =
              category.value.wishlistEntryGroup[0].product.categories[0].name
                .length < 38 &&
              category.value.wishlistEntryGroup[0].product.categories[0]
                .description.length < 40
                ? 35
                : Math.ceil(
                  (category.value.wishlistEntryGroup[0].product.categories[0]
                    .name.length +
                      category.value.wishlistEntryGroup[0].product.categories[0]
                        .description.length) /
                      46,
                ) *
                    8 +
                  35;
          } else {
            categoryHeight =
              category.value.wishlistEntryGroup[0].product.categories[0].name
                .length < 38
                ? 25
                : category.value.wishlistEntryGroup[0].product.categories[0]
                  .name.length /
                  15 *
                  12;
          }
          this.pdf.setFillColor(211, 211, 211);
          this.pdf.rect(left - 5, productTop - 15, 170, categoryHeight, 'F');
          this.pdf.setFontSize(10);
          this.pdf.setFontStyle('bold');
          this.pdf.text(
            left,
            productTop,
            category.value.wishlistEntryGroup[0].product.categories[0].name,
          );
          productTop += 10;
          this.pdf.setFontSize(8);
          this.pdf.setFontStyle('normal');
          if (
            pdfInfo.categoryDescription &&
            category.value.wishlistEntryGroup[0].product.categories[0]
              .description
          ) {
            splitTitle = this.pdf.splitTextToSize(
              category.value.wishlistEntryGroup[0].product.categories[0]
                .description,
              165,
            );
            this.pdf.text(35, productTop, splitTitle);
          }
          productTop += categoryHeight - 5;
          category.value.wishlistEntryGroup.map((product) => {
            imgData =
              product.product.encodedImages && product.product.encodedImages[0]
                ? `data:image/jpeg;base64,${product.product.encodedImages[0]}`
                : imgBlob.noImage;
            if (productCounter % 12 === 0 && !newCategoryPage) {
              if (productCounter !== 0) {
                this.pdf.addPage();
                this.addFormattingThreeColumn(pdfInfo);
                newPage = true;
                productTop = 120 + this.headingTextBoxHeight;
                left = 35;
                logoLeft = 85;
              }
            }
            if (!productCounter || productCounter % 4 !== 0 || newPage) {
              newPage = false;
            } else if (categoryLeft) {
              categoryLeft = false;
            } else {
              left += 180;
              logoLeft = left + 50;
              productTop = 120 + this.headingTextBoxHeight;
            }
            this.pdf.addImage(
              imgData,
              'JPEG',
              left - 5,
              productTop - 5,
              50,
              50,
            );
            this.pdf.setFontStyle('bold');
            this.pdf.text(
              left + 50,
              productTop,
              `Mfg # ${product.product.code}`,
            );
            if (product.product.cmirCode) {
              this.pdf.text(
                left + 50,
                productTop + 10,
                `${product.product.cmirCode}`,
              );
              productTop += 10;
            }
            productTop += 20;
            if (
              product.product &&
              product.product.priceText &&
              product.product.priceText !== ''
            ) {
              this.pdf.setFontStyle('bold');
              this.pdf.text(
                left + 50,
                productTop + 10,
                product.product.priceText,
              );
            }
            this.pdf.setFontStyle('normal');
            productTop += 10;
            if (product.customAttr1Value && product.customAttr1Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr1} : ${product.customAttr1Value}`,
              );
              productTop += 10;
            }
            if (product.customAttr2Value && product.customAttr2Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr2}  : ${product.customAttr2Value}`,
              );
              productTop += 10;
            }
            if (product.customAttr3Value && product.customAttr3Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr3}  : ${product.customAttr3Value}`,
              );
              productTop += 10;
            }
            let specifications = product.product.displayAttributes;
            let name = product.product.name;
            productTop = this.detailedProductName(specifications,name,left,productTop,productSplitSize,productNameSize,minNameHeight,sizeFactor);
            if (
              pdfInfo.productSellingStatement &&
              product.product.sellingstmt
            ) {
              splitTitle = this.pdf.splitTextToSize(
                product.product.sellingstmt,
                110,
              );
              this.pdf.text(left + 50, productTop, splitTitle);
              productSellingHeight =
                product.product.sellingstmt.length < 30
                  ? 10
                  : product.product.sellingstmt.length / 30 * 12;
              productTop += productSellingHeight;
              logoTop = productTop;
              productTop += 40;
            } else {
              logoTop = productTop;
              productTop += 40;
            }
            if (product.product.gpCertifications) {
              for (
                let i = 0;
                i < product.product.gpCertifications.length;
                i++
              ) {
                const image = this.getCertificateImage(
                  product.product.gpCertifications[i].id,
                  pdfInfo,
                );
                this.pdf.addImage(
                  image,
                  this.getExtension(image),
                  logoLeft,
                  logoTop,
                  15,
                  15,
                );
                logoLeft += 20;
              }
              logoTop += 50;
              logoLeft = 85;
            }
            productCounter += 1;
          });
        }
      });
    },
    createCustomStylePDF(pdfInfo, featureProducts) {
      this.resetDimensions();
      this.createPDFInstance();
      if (pdfInfo.coverPage) {
        this.renderCoverPage(pdfInfo);
      }
      this.uncheckedData = [];
      if (featureProducts.length) {
        featureProducts.map((item) => {
          const productCode = item;
          this.data.wishlistEntriesGroup.map((category) => {
            category.value.wishlistEntryGroup.map((product, index) => {
              if (productCode === product.product.code) {
                this.featuredData.push(product);
                category.value.wishlistEntryGroup.splice(index, 1);
              }
            });
          });
        });
      }
      this.data.wishlistEntriesGroup.map((category) => {
        category.value.wishlistEntryGroup.map((product) => {
          this.uncheckedData.push(product);
        });
      });
      this.customFullPdf(pdfInfo, this.featuredData);
      if (this.uncheckedData.length > 0 && this.maxFullDetail) {
        this.maxFullDetail = false;
        this.pdf.addPage();
        this.showSustainibility = false;
        switch (pdfInfo.formatColumns) {
          case 'Display in one column':
            this.addFormattingOneColumn(pdfInfo);
            this.addContentOneColumn(pdfInfo);
            break;
          case 'Display in two columns':
            this.addFormattingTwoColumn(pdfInfo);
            this.addContentTwoColumn(pdfInfo);
            break;
          case 'Display in three columns':
            this.addFormattingThreeColumn(pdfInfo);
            this.addContentThreeColumn(pdfInfo);
            break;
          case 'Display as full detail':
            this.customFullPdf(pdfInfo, this.uncheckedData);
            this.isFullDetail = true;
            break;
          default:
            this.createOneColumnFormat();
        }
      }
      this.savePdf(pdfInfo);
    },
    customFullPdf(pdfInfo, data) {
      this.pdfInfo = pdfInfo;
      const productLength = data.length;
      data.map((pdpProductInfo, index) => {
        this.pdpProductInfo = pdpProductInfo.product;
        this.pdpProductInfo.customAttr1Value = pdpProductInfo.customAttr1Value;
        this.pdpProductInfo.customAttr2Value = pdpProductInfo.customAttr2Value;
        this.pdpProductInfo.customAttr3Value = pdpProductInfo.customAttr3Value;
        this.createHeader(pdfInfo);
        if (pdfInfo.onlyOnFirstPage && this.firstPage) {
          this.createSubHeader(pdfInfo);
          this.firstPage = false;
        } else if (!pdfInfo.onlyOnFirstPage) {
          this.createSubHeader(pdfInfo);
        } else {
          this.headingTextBoxHeight = 0;
        }
        this.createFooter(pdfInfo);
        this.createLeftSection();
        this.createRightSection();
        if (productLength === index + 1) {
          this.maxFullDetail = true;
        } else {
          this.pdf.addPage();
        }
      });
    },
    createTwoColumnFormatShare(pdfInfo) {
      this.resetDimensions();
      this.createPDFInstance();
      this.addFormattingTwoColumn(pdfInfo);
      this.addContentTwoColumn(pdfInfo);
      return btoa(this.pdf.output());
    },
    createTwoColumnFormat(pdfInfo) {
      this.resetDimensions();
      this.createPDFInstance();
      if (pdfInfo.coverPage) {
        this.renderCoverPage(pdfInfo);
      }
      this.addFormattingTwoColumn(pdfInfo);
      this.addContentTwoColumn(pdfInfo);
      this.savePdf(pdfInfo);
    },
    addFormattingTwoColumn(pdfInfo) {
      this.createHeader(pdfInfo);
      if (pdfInfo.onlyOnFirstPage && this.firstPage) {
        this.createSubHeader(pdfInfo);
        this.firstPage = false;
      } else if (!pdfInfo.onlyOnFirstPage) {
        this.createSubHeader(pdfInfo);
      } else {
        this.headingTextBoxHeight = 0;
      }
      this.createFooter(pdfInfo);
      this.pdf.setDrawColor('#D3D3D3');
      this.pdf.setLineWidth(0.1);
      this.pdf.line(290, this.headingTextBoxHeight + 100, 294, 750);
      this.pdf.rect(
        24,
        this.headingTextBoxHeight + 100,
        540,
        this.headingTextBoxHeight < 50 ? 650 : this.headingTextBoxHeight + 550,
      );
    },
    addContentTwoColumn(pdfInfo) {
      let productTop = 120 + this.headingTextBoxHeight;
      let left = 35;
      let newPage = false;
      let imgData = '';
      let splitTitle = '';
      let categoryHeight = 0;
      let logoLeft = 85;
      let logoTop = 0;
      let nameHeight = 0;
      let productSellingHeight = 0;
      let productCounter = 0;
      let categoryLeft = false;
      let newCategoryPage = false;
      let productSplitSize = 200;
      let productNameSize = 50;
      let minNameHeight = 15;
      let sizeFactor = 30;
      this.data.wishlistEntriesGroup.map((category) => {
        if (category.value.wishlistEntryGroup.length > 0) {
          if (productCounter !== 0 && productCounter % 4 === 0) {
            if (productCounter % 8 !== 0) {
              left += 269;
              logoLeft = left + 50;
              productTop = 120 + this.headingTextBoxHeight;
              categoryLeft = true;
            } else {
              this.pdf.addPage();
              newCategoryPage = true;
              this.addFormattingTwoColumn(pdfInfo);
              newPage = true;
              productTop = 120 + this.headingTextBoxHeight;
              left = 35;
              logoLeft = 85;
            }
          }
          if (
            pdfInfo.categoryDescription &&
            category.value.wishlistEntryGroup[0].product.categories[0]
              .description
          ) {
            categoryHeight =
              category.value.wishlistEntryGroup[0].product.categories[0].name
                .length < 58 &&
              category.value.wishlistEntryGroup[0].product.categories[0]
                .description.length < 65
                ? 35
                : Math.ceil(
                  (category.value.wishlistEntryGroup[0].product.categories[0]
                    .name.length +
                      category.value.wishlistEntryGroup[0].product.categories[0]
                        .description.length) /
                      64,
                ) *
                    8 +
                  35;
          } else {
            categoryHeight =
              category.value.wishlistEntryGroup[0].product.categories[0].name
                .length < 38
                ? 25
                : category.value.wishlistEntryGroup[0].product.categories[0]
                  .name.length /
                  40 *
                  12;
          }
          this.pdf.setFillColor(211, 211, 211);
          this.pdf.rect(left - 5, productTop - 15, 255, categoryHeight, 'F');
          this.pdf.setFontSize(10);
          this.pdf.setFontStyle('bold');
          this.pdf.text(
            left,
            productTop,
            category.value.wishlistEntryGroup[0].product.categories[0].name,
          );
          productTop += 10;
          this.pdf.setFontSize(8);
          this.pdf.setFontStyle('normal');
          if (
            pdfInfo.categoryDescription &&
            category.value.wishlistEntryGroup[0].product.categories[0]
              .description
          ) {
            splitTitle = this.pdf.splitTextToSize(
              category.value.wishlistEntryGroup[0].product.categories[0]
                .description,
              250,
            );
            this.pdf.text(35, productTop, splitTitle);
          }
          productTop += categoryHeight - 5;
          category.value.wishlistEntryGroup.map((product) => {
            imgData =
              product.product.encodedImages && product.product.encodedImages[0]
                ? `data:image/jpeg;base64,${product.product.encodedImages[0]}`
                : imgBlob.noImage;
            if (productCounter % 8 === 0 && !newCategoryPage) {
              if (productCounter !== 0) {
                this.pdf.addPage();
                this.addFormattingTwoColumn(pdfInfo);
                newPage = true;
                productTop = 120 + this.headingTextBoxHeight;
                left = 35;
                logoLeft = 85;
              }
            }
            if (!productCounter || productCounter % 4 !== 0 || newPage) {
              newPage = false;
            } else if (categoryLeft) {
              categoryLeft = false;
            } else {
              left += 269;
              logoLeft = left + 50;
              productTop = 120 + this.headingTextBoxHeight;
            }
            this.pdf.addImage(imgData, 'JPEG', left - 5, productTop, 50, 50);
            this.pdf.setFontStyle('bold');
            this.pdf.text(
              left + 50,
              productTop,
              `Mfg # ${product.product.code}`,
            );
            if (product.product.cmirCode) {
              this.pdf.setTextColor(150);
              this.pdf.text(left + 115, productTop, '|');
              this.pdf.setTextColor(0);
              this.pdf.text(
                left + 130,
                productTop,
                `${product.product.cmirCode}`,
              );
            }
            productTop += 20;
            if (
              product.product &&
              product.product.priceText &&
              product.product.priceText !== ''
            ) {
              this.pdf.setFontStyle('bold');
              this.pdf.text(
                left + 50,
                productTop + 10,
                product.product.priceText,
              );
            }
            this.pdf.setFontStyle('normal');
            productTop += 20;
            if (product.customAttr1Value && product.customAttr1Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr1} : ${product.customAttr1Value}`,
              );
              productTop += 10;
            }
            if (product.customAttr2Value && product.customAttr2Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr2}  : ${product.customAttr2Value}`,
              );
              productTop += 10;
            }
            if (product.customAttr3Value && product.customAttr3Value !== '') {
              this.pdf.text(
                left + 50,
                productTop,
                `${this.data.customAttr3}  : ${product.customAttr3Value}`,
              );
              productTop += 10;
            }
            let specifications = product.product.displayAttributes;
            let name = product.product.name;
            productTop = this.detailedProductName(specifications,name,left,productTop,productSplitSize,productNameSize,minNameHeight,sizeFactor);
            if (
              pdfInfo.productSellingStatement &&
              product.product.sellingstmt
            ) {
              splitTitle = this.pdf.splitTextToSize(
                product.product.sellingstmt,
                110,
              );
              this.pdf.text(left + 50, productTop, splitTitle);
              productSellingHeight =
                product.product.sellingstmt.length < 50
                  ? 15
                  : product.product.sellingstmt.length / 30 * 12;
              productTop += productSellingHeight;
              logoTop = productTop;
              productTop += 40;
            } else {
              logoTop = productTop;
              productTop += 40;
            }
            if (product.product.gpCertifications) {
              for (
                let i = 0;
                i < product.product.gpCertifications.length;
                i++
              ) {
                const image = this.getCertificateImage(
                  product.product.gpCertifications[i].id,
                  pdfInfo,
                );
                this.pdf.addImage(
                  image,
                  this.getExtension(image),
                  logoLeft,
                  logoTop,
                  15,
                  15,
                );
                logoLeft += 20;
              }
              logoTop += 50;
              logoLeft = 85;
            }
            productCounter += 1;
          });
        }
      });
    },
  },
};
