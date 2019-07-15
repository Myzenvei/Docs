
<template>
  <div class="home-page container-fluid">
    <mg-home-banner :imagesArray = "imagesArray" ></mg-home-banner>
    <mg-404 :error-info = "fileNotFoundError"></mg-404>
    <mg-video-wrapper :video-wrapper-data="videoWrapperData" ></mg-video-wrapper>
    <mg-sign-up :i18n="messages.signUpLabel" ></mg-sign-up>
    <mg-content-slider :items="contentSliderData"></mg-content-slider>
    <mg-banner-slider></mg-banner-slider>
    <!-- <mg-find-and-buy-us :bannerData="bannerData" ></mg-find-and-buy-us> -->
    <mg-hero-banner-v1 :bannerData="bannerData" ></mg-hero-banner-v1>
    <mg-hero-banner-v1 :bannerData="SignupData" :btnText="SignUpBtnText" :bannerColors="signUpTheme" ></mg-hero-banner-v1>
    <mg-hero-banner-v1 :bannerData="preSignupData" :btnText="preSignUpBtnText" :isShowInput="preSignupForm" :bannerColors="signUpTheme" ></mg-hero-banner-v1>
    <mg-image-text-layout
      :component-data = "napkinsSectionData"
      :component-theme = "napkinsSectionTheme"
      :component-class = "'our-napkins'"
      :is-form = false>
    </mg-image-text-layout>
    <mg-image-text-layout
      :component-data = "teachersSectionData"
      :component-theme = "teachersSectionTheme"
      :component-class = "'teachers-lounge'"
      :is-form = true>
    </mg-image-text-layout>
    <mg-image-text-layout
      :component-data = "faqSectionData"
      :component-theme = "faqSectionTheme"
      :component-class = "'faqs'"
      :is-form = false>
        <mg-faq :i18n="faqData" ></mg-faq>
    </mg-image-text-layout>
    <mg-our-packs :packs-data="ourPacksData"></mg-our-packs>
    <mg-find-a-store></mg-find-a-store>
    <mg-signup-save :i18n="signUpSaveData" ></mg-signup-save>
  </div>
</template>

<script>
import mgVideoWrapper from '../components/mg-video-wrapper/mg-video-wrapper.vue';
import mgSignUp from '../components/mg-sign-up/mg-sign-up.vue';
import mgContentSlider from '../components/mg-content-slider/mg-content-slider.vue';
import mgBannerSlider from '../components/mg-banner-slider/mg-banner-slider.vue';
import mgFindAndBuyUs from '../components/mg-find-and-buy-us/mg-find-and-buy-us.vue';
import mgHeroBannerV1 from '../components/common/mg-hero-banner-v1/mg-hero-banner-v1.vue';
import mgHomeBanner from '../components/mg-home-banner/mg-home-banner.vue';
import mgOurNapkins from '../components/mg-our-napkins/mg-our-napkins.vue';
import mgImageTextLayout from '../components/common/mg-image-text-layout/mg-image-text-layout.vue';
import mgFaq from '../components/mg-faq/mg-faq.vue';
import mgOurPacks from '../components/mg-our-packs/mg-our-packs.vue';
import mgSignupSave from '../components/mg-signup-save/mg-signup-save.vue';
import mgFindAStore from '../components/mg-find-a-store/mg-find-a-store.vue';
import mg404 from '../components/mg-404/mg-404.vue';
import messages from '../locale/messages';

export default {
  name: 'home-page',
  components: {
    mgVideoWrapper,
    mgSignUp,
    mgContentSlider,
    mgBannerSlider,
    mgFindAndBuyUs,
    mgHeroBannerV1,
    mgHomeBanner,
    mgOurNapkins,
    mgImageTextLayout,
    mgFaq,
    mgOurPacks,
    mgSignupSave,
    mgFindAStore,
    mg404,
  },
  props: {},
  data() {
    return {
      messages: messages['en-US'],
      msg: 'Home Page Test',
      bannerData: {
        bannerTitle: 'Sign Up',
        bannerHeading: 'COULD WE BE ANY CLOSER? YES! OH YES.',
        bannerInfo:
          '<b>now that we’re besties, we want to keep in touch all year round via email.</b> as a subscriber, you wll get all the beg.)',
        bannerImg: './static/assets/images/mardi-gras/find_us.png',
      },
      signUpSaveData: {
        title: 'Signup && Save',
        info:
          'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris molestie dui a eleifend sodales.',
        fname: 'First Name',
        lname: 'Last Name',
        email: 'Email',
        submit: 'Submit',
      },
      imagesArray: {
        images: [
          {
            imageAltText: 'mg-brand1.png',
            imageSrcD: './static/assets/images/mardi-gras/mardi-gras-brand.png',
          },
          {
            imageAltText: 'mg-brand2.png',
            imageSrcD: './static/assets/images/mardi-gras/mg_header_rightpod.png',
          },
        ],
      },
      ourPacksData: {
        title: 'Our Packs',
        imagesArray: [
          {
            imgSrc: './static/assets/images/mardi-gras/200pack.png',
            altText: 'sample image1',
          },
          {
            imgSrc: './static/assets/images/mardi-gras/200pack.png',
            altText: 'sample image2',
          },
          {
            imgSrc: './static/assets/images/mardi-gras/200pack.png',
            altText: 'sample image3',
          },
          {
            imgSrc: './static/assets/images/mardi-gras/200pack.png',
            altText: 'sample image4',
          },
        ],
      },
      faqData: {
        faqArray: [
          {
            question: 'IS THERE LIFE ON MARS?',
            answer:
              ' We really hope so. Because the idea of helping Martians wipe however many mouths they have when they’re finished eating delights us to no end.',
          },
          {
            question: 'HOW AFFORDABLE ARE MARDI GRAS NAPKINS?',
            answer:
              'So very! At about a penny per square, our napkins are super affordable.Mmhmm, those random Abe Lincolns in your pockets and kitchen drawers never looked so good, did they?',
          },
          {
            question: 'CHOCOLATE CAKE VS. APPLE PIE?',
            answer:
              'That’s really not fair. Can we say both? There’s room for all kinds of yummy desserts in our book. Now then, do we smell cookies? Well, could we.',
          },
        ],
      },
      SignupData: {
        bannerTitle: 'Sign Up',
        bannerHeading: 'Thanks!',
        bannerInfo:
          '<b>Whew.</b> Glad thats over with. Keep an eye on your inbox for <b><em>fun things.</em></b>',
      },
      signUpTheme: {
        bannerTitleColor: '#522773',
        bannerHeadingColor: '#E3218f',
        bannerInfoColor: '#522773',
      },
      SignUpBtnText: 'Go Home',
      preSignupData: {
        bannerTitle: 'Sign Up',
        bannerHeading: 'COULD WE BE ANY CLOSER? YES! OH YES.',
        bannerInfo:
          '<b>now that we’re besties, we want to keep in touch all year ‘round via email</b>. as a subscriber, you’ll get all the latest news, promos & offers available to our most <b><em<loyal</em></b> fans. so sign up today! (don’t make us beg.)',
        bannerImg: './static/assets/images/mardi-gras/Signup_social.png',
      },
      preSignupTheme: {
        bannerTitleColor: '#522773',
        bannerHeadingColor: '#E3218f',
        bannerInfoColor: '#522773',
      },
      preSignupForm: true,
      preSignUpBtnText: 'Next',
      contentSliderData: [
        {
          slideURL: './static/assets/images/mardi-gras/Ding_Ding.png',
        },
        {
          slideURL: './static/assets/images/mardi-gras/Ding_Ding.png',
        },
        {
          slideURL: 'https://fakeimg.pl/200x200/',
        },
        {
          slideURL: 'https://fakeimg.pl/200x200/',
        },
        {
          slideURL: 'https://fakeimg.pl/200x200/',
        },
        {
          slideURL: 'https://fakeimg.pl/200x200/',
        },
      ],
      videoWrapperData: {
        wrapperTitle: 'Watch our Videos!',
        imagesList: [
          {
            url: 'https://www.youtube.com/embed/tkumSmWPANE',
            backgroundImage: './static/assets/images/mardi-gras/hangry.png',
          },
          {
            url: 'https://www.youtube.com/embed/h2zgkADa5YA',
            backgroundImage: './static/assets/images/mardi-gras/mardiGrasMeetHarts.png',
          },
          {
            url: 'https://www.youtube.com/embed/X2oh8HWItqA',
            backgroundImage: './static/assets/images/mardi-gras/meet_the_harts.png',
          },
          {
            url: 'https://www.youtube.com/embed/tkumSmWPANE',
            backgroundImage: './static/assets/images/mardi-gras/momming.png',
          },
        ],
      },
      findBuyUs: {
        title: 'FIND & BUY US',
        subtitle1: 'YOO-HOO! HELLO!',
        subtitle2: 'CAN YOU SEE US?',
        description1: 'MARDI GRAS',
        description2: 'NAPKINS',
        description3:
          "CAN BE FOUND ONLINE AND IN-STORE AT SOME OF YOUR FAVORITE GROCERY AND RETAIL STORES.TRUTH BE TOLD, WE'VE BEEN HIDING IN ",
        description4: 'plain sight',
        description5: 'FOR YEARS!',
        imgSrc: './static/assets/images/mardi-gras/mg_dots_purple.png',
      },
      napkinsSectionData: {
        bannerImgUrl: './static/assets/images/mardi-gras/mg_brand_napkins.gif',
        bannerImgAlt: 'Funny Printed Paper Napkins',
        titleText: 'OUR NAPKINS',
        subTitleText: 'ONE SHAPE,<br>ENDLESS CONVOS!',
        descriptionText:
          '<b>We’ve been called a square.</b>And that’s<b><em>OKAY</em></b>. We are napkins, after all. That said, we do come in several great pack sizes—all featuring our ridiculously awesome collection of clever conversation starters. What better way to spark some lively (and often lovely) mealtime moments? None, we tell you.</p>',
      },
      napkinsSectionTheme: {
        layoutBackgroundType: 'color',
        layoutBackground: '#814C9E',
        titleColor: '#FFFFFF',
        subTitleColor: '#58c9e7',
        descriptionColor: '#FFFFFF',
        hideBannerInMobile: true,
      },
      teachersSectionData: {
        bannerImgUrl: './static/assets/images/mardi-gras/mg_grab_napkins.png',
        bannerImgAlt: 'Headed out for field trip? Grab Napkins.',
        titleText: "TEACHER'S LOUNGE",
        subTitleText: 'SPARK A CLASSROOM <br>CONVERSATION',
        descriptionText:
          'We believe that children are the future. No, <em><strong>really</strong></em>But first, you’ve got to get them to open up and share their ideas. That’s why we’re partnering with teachers everywhere to help children develop better <em><strong>communication skills</strong></em> starting in the classroom. Get their conversations going by practicing their social skills, writing, and vocabulary. Start by downloading our <em><strong>super fun</strong></em> activity page below and be sure to sign up for coupons and all kinds of great offers in the future.',
        downloadBtnLabel: 'DOWNLOAD ACTIVITY KIT',
        downloadBtnLink: '#',
        signUpBtnLabel: 'SIGN UP + ',
        freeCouponBtnLabel: 'GET A FREE COUPON',
        signUpBtnLink: '#',
        signUpPlaceholder: 'Enter your email address',
        savingsLabel: 'PSSST! INSTANT SAVINGS!',
        savingsArrow: './static/assets/images/mardi-gras/mg_savings_arrow.png',
        savingsArrowAlt: 'Savings Arrow',
      },
      teachersSectionTheme: {
        layoutBackgroundType: 'image',
        layoutBackground: './static/assets/images/mardi-gras/mg_teachers_background.jpg',
        titleColor: '#522773',
        subTitleColor: '#e3218f',
        descriptionColor: '#522773',
        hideBannerInMobile: false,
      },
      faqSectionData: {
        bannerImgUrl: './static/assets/images/mardi-gras/mg_faq_girl.gif',
        bannerImgAlt: 'Is there life on Mars?',
        titleText: 'FAQ',
        subTitleText: "SOME F.R.A'S FOR,<br>YOUR F.A.Q'S!",
        descriptionText:
          'You’ve got frequently asked questions? We’ve got <br>frequently repeated answers!',
      },
      faqSectionTheme: {
        layoutBackgroundType: 'color',
        layoutBackground: '#814C9E',
        titleColor: '#FFFFFF',
        subTitleColor: '#58c9e7',
        descriptionColor: '#FFFFFF',
        hideBannerInMobile: true,
      },
      technicalError: {
        title: 'Oops, something went wrong!',
        description: '500 Error Found',
        help: 'Sorry, We had some technical problem.',
      },
      fileNotFoundError: {
        title: 'Oops, something went wrong!',
        description: '404 File Not Found',
        help: 'If you need immediate assistance with anything, please <a href="#">contact us.</a>',
        helpBtnLabel: 'Go Home',
        helpBtnUrl: '#',
      },
    };
  },
};
</script>
