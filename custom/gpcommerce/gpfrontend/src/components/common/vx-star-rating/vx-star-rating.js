import {
  starRatingColors,
  vanityFairStarRatingColors,
  copperCraneStarRatingColors,
  brawnyStarRatingColors,
  innoviaStarRatingColors,
} from '../../common/mixins/vx-enums';
import globals from '../../common/globals';

export default {
  name: 'vx-star-rating',
  components: {},
  props: [
    'config',
    'productRating',
    'productReviews',
    'i18n',
    'showRating',
    'showReviews',
    'numberOfStars',
  ],
  data() {
    return {
      globals,
      starRatingColors,
      stars: [],
      emptyStar: 0,
      fullStar: 1,
      totalStars: this.numberOfStars,
      style: {
        fullStarColor: '',
        emptyStarColor: '',
        starWidth: 15,
        starHeight: 15,
      },
    };
  },
  created() {
    this.initStars();
    this.setStars();
    this.setConfigData();
  },
  mounted() {
    this.getStarColor();
  },
  computed: {
    getStarPoints() {
      const centerX = this.style.starWidth / 2;
      const centerY = this.style.starHeight / 2;

      const innerCircleArms = 5; // a 5 arms star

      const innerRadius = this.style.starWidth / innerCircleArms;
      const innerOuterRadiusRatio = 2.5; // Unique value - determines fatness/sharpness of star
      const outerRadius = innerRadius * innerOuterRadiusRatio;

      return this.calcStarPoints(
        centerX,
        centerY,
        innerCircleArms,
        innerRadius,
        outerRadius,
      );
    },
  },
  methods: {
    // Function to calculate the points required to plot the star
    calcStarPoints(
      centerX,
      centerY,
      innerCircleArms,
      innerRadius,
      outerRadius,
    ) {
      let angle = Math.PI / innerCircleArms;
      let angleOffsetToCenterStar = 60;

      let totalArms = innerCircleArms * 2;
      let points = "";
      for (let i = 0; i < totalArms; i++) {
        let isEvenIndex = i % 2 == 0;
        let r = isEvenIndex ? outerRadius : innerRadius;
        let currX = centerX + Math.cos(i * angle + angleOffsetToCenterStar) * r;
        let currY = centerY + Math.sin(i * angle + angleOffsetToCenterStar) * r;
        points += currX + "," + currY + " ";
      }
      return points;
    },
    // Determine the total number of stars to be displayed
    initStars() {
      for (let i = 0; i < this.totalStars; i++) {
        this.stars.push({
          raw: this.emptyStar,
          percent: this.emptyStar + "%"
        });
      }
    },
    // Function to show the total stars
    setStars() {
      let fullStarsCounter = Math.floor(this.productRating);
      for (let i = 0; i < this.stars.length; i++) {
        if (fullStarsCounter !== 0) {
          this.stars[i].raw = this.fullStar;
          this.stars[i].percent = this.calcStarFullness(this.stars[i]);
          fullStarsCounter--;
        } else {
          let surplus = Math.round((this.productRating % 1) * 10) / 10; // Support just one decimal
          let roundedOneDecimalPoint = Math.round(surplus * 10) / 10;
          this.stars[i].raw = roundedOneDecimalPoint;
          return (this.stars[i].percent = this.calcStarFullness(this.stars[i]));
        }
      }
    },
    // Function to apply the required properties of the star icons
    setConfigData() {
      if (this.config) {
        this.setBindedProp(this.style, this.config.style, "fullStarColor");
        this.setBindedProp(this.style, this.config.style, "emptyStarColor");
        this.setBindedProp(this.style, this.config.style, "starWidth");
        this.setBindedProp(this.style, this.config.style, "starHeight");
        if (this.config.isIndicatorActive) {
          this.isIndicatorActive = this.config.isIndicatorActive;
        }
      }
    },
    // Function determine the nature of the star
    getFullFillColor(starData) {
      return starData.raw !== this.emptyStar
        ? this.style.fullStarColor
        : this.style.emptyStarColor;
    },
    // Function to fill the star color
    calcStarFullness(starData) {
      const starFullnessPercent = `${starData.raw * 100}%`;
      return starFullnessPercent;
    },
    setBindedProp(localProp, propParent, propToBind) {
      if (propParent[propToBind]) {
        localProp[propToBind] = propParent[propToBind];
      }
    },
    getStarColor() {
      if (this.globals.isVanityfair()) {
        this.style.fullStarColor = vanityFairStarRatingColors.fullStarColor;
        this.style.emptyStarColor = vanityFairStarRatingColors.emptyStarColor;
      } else if (this.globals.isCopperCrane()) {
        this.style.fullStarColor = copperCraneStarRatingColors.fullStarColor;
        this.style.emptyStarColor = copperCraneStarRatingColors.emptyStarColor;
      } else if (this.globals.isBrawny()) {
        this.style.fullStarColor = brawnyStarRatingColors.fullStarColor;
        this.style.emptyStarColor = brawnyStarRatingColors.emptyStarColor;
      } else if (this.globals.isInnovia()) {
        this.style.fullStarColor = innoviaStarRatingColors.fullStarColor;
        this.style.emptyStarColor = innoviaStarRatingColors.emptyStarColor;
      } else {
        this.style.fullStarColor = starRatingColors.fullStarColor;
        this.style.emptyStarColor = starRatingColors.emptyStarColor;
      }
    },
  },
};
