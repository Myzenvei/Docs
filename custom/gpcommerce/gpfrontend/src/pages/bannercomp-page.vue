<template>
  <div>
    <vx-hero-banner
      :heroBannerData=heroBannerData
    />
    <banner-carousel
      :carouselData=carouselData />

</div>
</template>

<script>
import BannerCarousel from '../components/view-site-content/vx-banner-carousel/vx-banner-carousel.vue';
import vxHeroBanner from '../components/view-site-content/vx-hero-banner/vx-hero-banner.vue';
import VxAccordionGroup from '../components/view-site-content/vx-faq/vx-faq.vue';
import VxSectionTitle from '../components/common/vx-title/vx-title.vue';

export default {
  name: 'Hello',
  components: {
    'vx-hero-banner': vxHeroBanner,
    'banner-carousel': BannerCarousel,
    'accordion-group': VxAccordionGroup,
    'section-title': VxSectionTitle,
  },
  data() {
    return {
      heroBannerData: {
        isAnchor: false,
        headingText: "You do more than serve food,and we think that's worth celebrating",
        linkTo: 'https://www.gppro.com/',
        backgroundImage: {
          backgroundImage360: 'https://fakeimg.pl/300x500/',
          backgroundImage768: 'https://fakeimg.pl/500x500/',
          backgroundImage1200: 'https://fakeimg.pl/1024x500/',
        },
        altText: 'alt text',
        btnText: 'Learn More',
        headingColor: '#000000',
        subHeadingColor: '#000000',
        textPosition: 'banner__position__left',
      },
      sectionTitle: {
        tag: 'h1',
        title: 'Frequently Asked Questions',
      },
      accordionGroupData: [
        {
          accordionTitle: 'Collapsible Group Item #1 ',
          accordionId: 'accord-1',
          accordionBody:
            'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries',
        },
        {
          accordionTitle: 'Collapsible Group Item #1 ',
          accordionId: 'accord-2',
          accordionBody:
            'Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh',
        },
        {
          accordionTitle: 'Collapsible Group Item #1 ',
          accordionId: 'accord-3',
          accordionBody:
            'It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using',
        },
      ],

      carouselData: {
        loopCarousel: true,
        autoStart: 3110,
        componetType: 'default-carousel',
        gpRotatingData: [
          {
            headingText: 'Lorem Ipsum',
            altText: 'Video Text',
            ctaText: 'Learn More',
            ctaColor: '#000000',
            subHeadingColor: '#B1B1B1',
            componetType: 'videoWrapper',
            textPosition: 'banner__position__left',
            videoSrc: 'https://www.youtube.com/embed/2d7mNcLLF_A',
            playIconColor: '#000000',
            backgroundImageM: 'https://fakeimg.pl/300x500/',
            backgroundImageT: 'https://fakeimg.pl/500x500/',
            backgroundImageD: 'https://fakeimg.pl/1024x500/',
          },
          {
            headingText: 'Its a sample Heading from Image comp',
            altText: 'GpImage',

            ctaColor: '#ffffff',
            ctaBgColor: '#005293',
            subHeadingColor: '#747678',
            textPosition: 'banner__position__lower-left',
            componetType: 'imageWrapper',
            backgroundImageM: 'https://fakeimg.pl/300x500/',
            backgroundImageT: 'https://fakeimg.pl/500x500/',
            backgroundImageD: 'https://fakeimg.pl/1024x500/',
          },
        ],
      },
    };
  },
};
</script>
