
<template>
  <div class="home-page">
    <h1>{{ msg }}</h1>
    <image-title :img-title-data="imgTitleData"></image-title>
    <product-solution :product-solution-data="productSolutionData"></product-solution>
    <product-category-solution :tabs-data="productSolutionData1"></product-category-solution>
    <!-- <vx-feature-product :img-title-data='featureProduct'></vx-feature-product> -->
    <vx-bundle-carousel></vx-bundle-carousel>
    <div class="two-column-text">
      GP PRO, a division of Georgia-Pacific, and our Dixie® brand proudly stand behind those who Serve Greatness. Working alongside the nation’s top foodservice companies, we’ve engineered one of the industry’s broadest portfolios for on- and off-premise eating needs.
      <br>The Dixie® collection specializes in advanced dispensing systems and foodservice disposables that emphasize waste and cost reduction, food safety and sustainability. In an industry where change is the norm, customers rely on our premium Dixie Ultra® innovations to deliver the next-generation patron experience.
      <br>Knowing how a clean (or dirty) restroom can affect the overall impression of an operation, our GP PRO restroom solutions are also engineered for quality, performance and savings. From back-of-house to front, dining room to restroom, customers can get all the benefits from a single source.
    </div>
  </div>
</template>

<script>
import ImageTitle from "../components/view-site-content/vx-image-title/vx-image-title.vue";
import ProductSolution from "../components/view-site-content/vx-product-solution/vx-product-solution.vue";
import ProductCategorySolution from "../components/view-site-content/vx-product-category-solution/vx-product-category-solution.vue";
import vxFeatureProduct from "../components/view-site-content/vx-feature-product/vx-feature-product.vue";
import vxBundleCarousel from "../components/pdp/vx-bundle-carousel/vx-bundle-carousel.vue";

export default {
  name: "home-page",
  components: {
    "image-title": ImageTitle,
    "product-solution": ProductSolution,
    vxFeatureProduct,
    vxBundleCarousel,
    ProductCategorySolution
  },
  props: {},
  data() {
    return {
      msg: "Home Page",
      imgTitleData: {
        componentHeader: "DIXIE ULTRA<sup>®</sup> INNOVATIONS",
        noOfColumns: "5",
        componentTheme: "product-carousel center",
        ctaText: "Discover More Innovations",
        ctaStyle: "btn btn-primary",
        tiles: [
          {
            isVideo: true,
            videoSrc: "https://www.youtube.com/embed/2d7mNcLLF_A",
            playIconColor: "#f0f0f0",
            imgSrcD:
              "https://www.gppro.com/your-industry/-/media/cpg/gpprofessional/img/page/servegreatness/001.jpg",
            imgSrcT:
              "https://www.gppro.com/your-industry/-/media/cpg/gpprofessional/img/page/servegreatness/001.jpg",
            imgSrcM:
              "https://www.gppro.com/your-industry/-/media/cpg/gpprofessional/img/page/servegreatness/001.jpg",
            ctaLink: "https://www.youtube.com/embed/0UYOha_82YU",
            ctaText:
              "Dixie Ultra<sup>®</sup> SmartStock<sup>®</sup> Automated Napkin System &gt;",
            ctaStyle: "",
            imageLink: "/abcv",
            isVideoCta: true
          },
          {
            isVideo: true,
            videoSrc: "https://www.youtube.com/embed/lHQxPbxLC0w",
            playIconColor: "#f00000",
            imgSrcD:
              "https://www.gppro.com/your-industry/-/media/cpg/gpprofessional/img/page/servegreatness/003.jpg",
            imgSrcT:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            imgSrcM:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            headerColor: "#ffffff",
            textColor: "#ffffff",
            ctaLink: "#",
            ctaText:
              "Dixie Ultra<sup>®</sup> SmartStock<sup>®</sup> Wrapped Cutlery System &gt;",
            imageLink: "/"
          },
          {
            imgSrcD:
              "https://www.gppro.com/-/media/cpg/gpprofessional/img/page/servegreatness/insulaire005.jpg?la=en",
            imgSrcT:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            imgSrcM:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            headerColor: "#ffffff",
            textColor: "#ffffff",
            ctaLink: "#",
            ctaText: "Dixie Ultra<sup>®</sup><br> Insulair™ Hot Cup  &gt;",
            imageLink: "/"
          },
          {
            imgSrcD:
              "https://www.gppro.com/your-industry/-/media/cpg/gpprofessional/img/page/servegreatness/005.jpg",
            imgSrcT:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            imgSrcM:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            headerColor: "#ffffff",
            textColor: "#ffffff",
            ctaLink: "#",
            ctaText: "Read More",
            imageLink: "/"
          },
          {
            imgSrcD:
              "https://www.gppro.com/your-industry/-/media/cpg/gpprofessional/img/page/servegreatness/002.jpg",
            imgSrcT:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            imgSrcM:
              "https://www.copperandcrane.com/c.4807301/sca-dev-elbrus/img/Article%20Photos/BERGAMOT/GettyImages-750540245.jpg?resizeid=3&resizeh=360&resizew=360&resizeid=5&resizeh=600&resizew=600",
            headerColor: "#ffffff",
            textColor: "#ffffff",
            ctaLink: "#",
            ctaText: "Read More",
            imageLink: "/"
          }
        ]
      },
      productSolutionData: {
        highlighterText: "Dispenser",
        subHeaderText:
          "The newest innovation from the Dixie Ultra® portfolio, comes an industry first, the Dixie Ultra® SmartStock™ Automated Napkin System. This commercial napkin dispenser is intelligently designed to dispense the right number of napkins each time, every time. Designed to cut napkin usage rates nearly in half, while also reducing cross-contamination. With this commercial napkin dispenser system, the only napkins you’ll be touching are your own.",
        componentHeader: "Dixie Ultra® Interfold Napkin Dispensers",
        componentTheme: "product-solution ruler industry-img-text",
        ctaText: "Learn More",
        ctaStyle: "btn btn-primary",
        ctaText2: "Request Trail",
        ctaLink: "https://www.google.com",
        isExternal: "true",
        ctaLink2: "#",
        ctaColor: "#ffffff",
        ctaColor2: "#ffffff",
        imageLink: "/abc",
        backgroundImageD:
          "https://www.gppro.com/-/media/cpg/gpprofessional/img/products---thumbnails/dixiesssm.jpg",
        tiles: [
          {
            isVideo: true,
            videoSrc: "https://www.youtube.com/embed/2d7mNcLLF_A",
            playIconColor: "#f0f0f0",
            header: "Cost Effective",
            tileText:
              "Reduces cutlery usage on average by 29%*.<br><span>*Compared to open bins.</span>.",
            imgSrcD:
              "https://www.gppro.com/-/media/cpg/GPProfessional/img/Serve%20Greatness/product-innovation/CostEffectiveIcon_200x200",
            ctaLink: "#",
            ctaText:
              "Dixie Ultra<sup>®</sup> SmartStock<sup>®</sup> Automated Napkin System &gt;",
            ctaStyle: "",
            headerColor: "#015293"
          },
          {
            isVideo: true,
            videoSrc: "https://www.youtube.com/embed/lHQxPbxLC0w",
            playIconColor: "#f00000",
            header: "Cost Effective",
            tileText:
              "Reduces cutlery usage on average by 29%*.<br><span>*Compared to open bins.</span>.",
            imgSrcD:
              "https://www.gppro.com/-/media/cpg/GPProfessional/img/Serve%20Greatness/product-innovation/IncreasedFoodSafetyIcon_200x200",
            headerColor: "#ffffff",
            textColor: "#ffffff",
            ctaLink: "#",
            ctaText:
              "Dixie Ultra<sup>®</sup> SmartStock<sup>®</sup> Wrapped Cutlery System &gt;"
          },
          {
            header: "Cost Effective",
            tileText:
              "Reduces cutlery usage on average by 29%*.<br><span>*Compared to open bins.</span>.",
            imgSrcD:
              "https://www.gppro.com/-/media/cpg/GPProfessional/img/Serve%20Greatness/product-innovation/ImageEnhancingIcon_200x200",
            headerColor: "#ffffff",
            textColor: "#ffffff",
            ctaLink: "#",
            ctaText: "Dixie Ultra<sup>®</sup><br> Insulair™ Hot Cup  &gt;"
          }
        ]
      },
      // featureProduct: {
      //   componentHeader: 'Select your operation',
      //   backgroundColor: '#ffffff',
      //   noOfColumns: '2',
      //   isCarousel: false,
      //   tiles: [
      //     {
      //       imgSrcD:
      //         '/gpcommercestorefront/medias/harmon-EcoSmart-650x600.jpg?context\u003dbWFzdGVyfHJvb3R8MTE4OTM3fGltYWdlL2pwZWd8aDg0L2gzNi84Nzk4MzIzNzAzODM4LmpwZ3xlMjRiYjIwMTk3ZWVkNzFlMzk4MWU4YzU3M2ZmMmFmZWE5OGJhNTA0NGJiNjVhMWNhYzE0N2Q0YjliODgwY2Fj',
      //       header: 'Limited Service Restaurant',
      //       tileText:
      //         'We developed recycling technology back in the 1930’s, creating a market for a material which was previously sent to the landfill.',
      //       textColor: '',
      //       ctaLink: 'www.google.com',
      //       ctaText: 'Learn More \u003e',
      //       backgroundColor: '',
      //       isVideo: 'false',
      //       isExternalLink: false,
      //       isExternalImage: false,
      //     },
      //     {
      //       imgSrcD:
      //         '/gpcommercestorefront/medias/LSR-427x401-Image.jpg?context\u003dbWFzdGVyfHJvb3R8MzEzMDl8aW1hZ2UvanBlZ3xoNWYvaGE0Lzg3OTgyODk1NTk1ODIuanBnfDJmZDcyYzQzNDU3NDQ4NmNkZWFmY2UwYjFhMzE0YWRkMWRhNGZiYWYzZjE3MDMwNjdlYTE5YzQ4MGFiZGU3NmQ',
      //       header: 'Cafeteria',
      //       tileText: '',
      //       textColor: '',
      //       ctaLink: 'www.google.com',
      //       ctaText: 'Learn More',
      //       backgroundColor: '',
      //       isVideo: 'false',
      //       videoSrc: '',
      //       playIconColor: '',
      //       isExternalLink: false,
      //       isExternalImage: false,
      //     },
      //     {
      //       imgSrcD:
      //         '/gpcommercestorefront/medias/BreakRoom-427x401-Image.jpg?context\u003dbWFzdGVyfHJvb3R8Mjk1OTN8aW1hZ2UvanBlZ3xoNmIvaGVlLzg3OTgyODk0Mjg1MTAuanBnfGYyMWZiNDRmZTEwNDgzZjUxZTk2NWEwNjFjMGUwM2MzYTQwNDYwZmE1ZmY5ZDNmYzgyZWEwZjdiNDUxNTczMDU',
      //       header: 'Cafeteria',
      //       tileText: '',
      //       textColor: '',
      //       ctaLink: 'www.google.com',
      //       ctaText: 'Learn More \u003e',
      //       backgroundColor: '',
      //       isVideo: 'false',
      //       isExternalLink: false,
      //       isExternalImage: false,
      //     },
      //   ],
      // },
      featureProduct: {
        componentHeader: "Select your operation",
        backgroundColor: "#ffffff",
        noOfColumns: "3",
        isCarousel: false,
        tiles: [
          {
            imgSrcD:
              "https://www.gppro.com/-/media/cpg/gpprofessional/img/ecosmart/harmon-ecosmart-650x600.jpg",
            header: "Limited Service Restaurant",
            secondaryImgD:
              "https://www.gppro.com/-/media/cpg/gpprofessional/img/brand---dixie/serve-greatness/tray.png?la=en",
            tileText:
              "We developed recycling technology back in the 1930’s, creating a market for a material which was previously sent to the landfill.",
            textColor: "",
            ctaLink: "www.google.com",
            ctaText: "Learn More \u003e",
            backgroundColor: "",
            isVideo: false,
            isExternalLink: false,
            isExternalImage: false
          },
          {
            imgSrcD:
              "https://www.gppro.com/-/media/cpg/gpprofessional/img/ecosmart/gp_2,-d-,1_ecosmart_desktop_half-width-what-is-leed-01.jpg",
            header: "What is LEED®?",
            tileText:
              "Leadership in Energy and Environmental Design (LEED®) is a rating system of voluntary standards allowing buildings to earn certification levels. LEED®-certified buildings are resource efficient – which is not only better for the environment, but also better for your bottom line.",
            textColor: "",
            ctaLink: "www.google.com",
            ctaText: "Learn More \u003e",
            backgroundColor: "",
            isVideo: false,
            isExternalLink: false,
            isExternalImage: false
          },
          {
            imgSrcD:
              "https://www.gppro.com/-/media/cpg/gpprofessional/img/ecosmart/gp_2,-d-,1_ecosmart_desktop_half-width-what-is-leed-01.jpg",
            header: "What is LEED®?",
            tileText:
              "Leadership in Energy and Environmental Design (LEED®) is a rating system of voluntary standards allowing buildings to earn certification levels. LEED®-certified buildings are resource efficient – which is not only better for the environment, but also better for your bottom line.",
            textColor: "",
            ctaLink: "www.google.com",
            ctaText: "Learn More \u003e",
            backgroundColor: "",
            isVideo: false,
            isExternalLink: false,
            isExternalImage: false
          }
        ]
      },
      productSolutionData1: {
        componentHeader: "Sample_componetn",
        subHeaderText: "sample _ header text",
        ctaText: "download brochure",
        ctaLink: "/",
        isExternalLink: false,
        tabs: [
          {
            componentHeader: "header",
            backgroundColor: "green",
            noOfColumns: "3",
            isCarousel: false,
            ctaText: "click",
            componentId: "cmsitem_00006000",
            isExternal: false,
            tiles: [
              {
                imgSrcD:
                  "https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/breakroom/beveragestation/5342cd644x644.jpg?la=en",
                header: "Restroom",
                tileText:
                  "Total restroom solutions for commercial facilities providing the best experience for those who use the restroom and those who manage them",
                textColor: "#808080",
                ctaLink: "www.google.com",
                ctaText: "Learn More",
                backgroundColor: "#3498DB",
                isVideo: "false",
                isExternalLink: false,
                isExternalImage: false,
                secondaryImgD:
                  "https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/breakroom/beveragestation/5342cd.png?la=en",
                jumpToHtml: false,
                isVideoCta: false,
                imageAltText: "imagetile2.PNG"
              },
              {
                imgSrcD:
                  "https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/breakroom/beveragestation/d9542b-644x644.jpg",
                header: "Restroom",
                tileText:
                  "Total restroom solutions for commercial facilities providing the best experience for those who use the restroom and those who manage them",
                textColor: "#808080",
                ctaLink: "www.google.com",
                ctaText: "Learn More",
                backgroundColor: "#3498DB",
                isVideo: "false",
                secondaryImgD:
                  "https://www.gppro.com/-/media/cpg/gpprofessional/img/serve-greatness/breakroom/beveragestation/d9542b.png?la=en",
                isExternalLink: false,
                isExternalImage: false,
                jumpToHtml: false,
                imageAltText: "Home-GP-PRO.jpg"
              }
            ]
          },
          {
            componentHeader: "tab name",
            backgroundColor: "black",
            noOfColumns: "3",
            isCarousel: false,
            ctaText: "click",
            ctaLink: "/",
            componentId: "cmsitem_00006001",
            isExternal: false,
            tiles: [
              {
                imgSrcD:
                  "/gp/medias/imagetile2.PNG?context=bWFzdGVyfHJvb3R8NTA4ODN8aW1hZ2UvcG5nfGg5Ni9oZjUvODc5NzczMTY1MTYxNC5wbmd8Y2FiZjcyMGZjYjE5YzU3NzEwOWRkYzk2NmZhOGFhMGE3YTM2MzgwYmVkZjg3MjE1YzFlZmU2Mjc5Y2NhN2NjZA",
                header: "Restroom",
                tileText:
                  "Total restroom solutions for commercial facilities providing the best experience for those who use the restroom and those who manage them",
                textColor: "#808080",
                ctaLink: "www.google.com",
                ctaText: "Learn More",
                backgroundColor: "#3498DB",
                isVideo: "false",
                isExternalLink: false,
                isExternalImage: false,
                secondaryImgD:
                  "/gp/medias/Tray.png?context=bWFzdGVyfGltYWdlc3w2NjE2fGltYWdlL3BuZ3xpbWFnZXMvaDRiL2g4MC84Nzk3MzU3MjExNjc4LnBuZ3w4MmI2ZmQxMDEyYjQ2MzNmZTZjYjc2ZmI4OTZhNzc5MmZmNmYxNWYzMWEzMDM5ZmYwOWQ1MTU1OGFiMDBmY2M5",
                jumpToHtml: false,
                isVideoCta: false,
                imageAltText: "imagetile2.PNG"
              },
              {
                imgSrcD:
                  "/gp/medias/Home-GP-PRO6.jpg?context=bWFzdGVyfGltYWdlc3w5NjA2M3xpbWFnZS9qcGVnfGltYWdlcy9oYjcvaDQ4Lzg3OTc1NDA0MTc1NjYuanBnfGEzODkwYzE2YzU0M2Y4YWQ2MjVhODQzMjNlZTMyZjQ5Zjg2MDE5YmI3OGUyYTVhNGZjYzNjN2M0NmUyYWNhYWI",
                header: "Restroom",
                tileText:
                  "Total restroom solutions for commercial facilities providing the best experience for those who use the restroom and those who manage them",
                textColor: "#808080",
                ctaLink: "www.google.com",
                ctaText: "Learn More",
                backgroundColor: "#3498DB",
                isVideo: "false",
                isExternalLink: false,
                isExternalImage: false,
                jumpToHtml: false,
                imageAltText: "Home-GP-PRO6.jpg"
              }
            ]
          }
        ]
      }
    };
  }
};
</script>
