/* Juicer feed */

/* Lint pacification */
/* eslint no-unused-vars: ['error', {'args': 'none'}] */
/* eslint no-console: ['error', { allow: ['log'] }] */

export default {
  name: 'vx-juicer-feed',
  components: {},
  props: {
    /**
     * all the properties for social aggregator
     */
    socialAggregatorOptions: {
      type: Object,
    },
  },
  data() {
    // More details on cusotmization
    // https://www.juicer.io/blog/customizing-your-juicer-embedded-social-media-feed
    return {
      juicerOptions: {

        feedId: this.socialAggregatorOptions.feedId,
        // Feed id
        // Sample - travel-08efe5d2-63c3-4b99-bc32-c97c29024269

        dataPer: this.socialAggregatorOptions.dataPer,
        // 15
        // 'per' attribute defines how many posts show up in each page of
        // your Juicer Feed. When you get to the bottom of your feed Juicer
        // will automatically "infinitely scroll" it will add the next page
        // of results to the bottom of the feed. If you want to
        // prevent this behavior, see the next section pages.

        pages: this.socialAggregatorOptions.pages,
        // 1
        // If you set this to 1 your juicer feed will not infinitely scroll,
        // it will only show the first page of results.
        // If you sent it to 2 it will infinitely scroll just once.

        truncate: this.socialAggregatorOptions.truncate,
        // 500
        // truncatemakes it so that Juicer will only show as many characters
        // as you provide as your value in each post (at the max),
        // for any posts longer it will display a "Read More" link.
        // If the post is shorter than the number of characters you provide it won't change.

        gutter: this.socialAggregatorOptions.gutter,
        // 10
        // he gutter attribute is the space between your posts in your feed in px.
        // On the modern style this defaults to 20 but you can edit this
        // if you want to change the spacing.

        columns: this.socialAggregatorOptions.columns,
        // 4
        // This allows you to change the number of columns your feed will display in.
        // You can also edit this attribute in your feed editor itself.

        interval: this.socialAggregatorOptions.interval,
        // 1000
        // The speed with which your slider or widget progresses, in milliseconds.
        // By default this is 5000 or 5 seconds. Set to a really high number
        // if you don't want it to progress at all.

        filter: this.socialAggregatorOptions.filter,
        // Facebook
        // Pass in either a name of a social account (capitalized) like
        // "Facebook" or "LinkedIn" to only show posts from that source
        // (if you have two facebook sources it will show the posts from both).

        // Or pass in the name on the account to only show posts from that
        // particular source. For example, if I had an instagram source
        // of #tbt in my feed i could pass in tbt to only show posts from that source.

        startingDate: this.socialAggregatorOptions.startDate,
        // 2014-03-01
        // Pass in a date in the format YYYY-MM-DD HH:MM (HH:MM optional)
        // to either starting-at (show posts newer than this date) or
        // ending-at (show posts older than this date. To filter by dates

        endingDate: this.socialAggregatorOptions.endDate,
        // 2014-04-01

        style: this.socialAggregatorOptions.style,
        // slider
        // If you wanted to use the same feed on different pages
        // with different styles you can pass in the name of the template
        // in lowercase ("user", "colors", "classic", "widget" etc) as the
        // style attribute to overwrite the template on a certain embed

        overlay: this.socialAggregatorOptions.overlay,
        // true
        // Set to "true" if you want the feed to open an overlay when a post is clicked in your feed

        // after: this.juicerCallback
        // If you want to run some arbitrary javascript functions
        // after the Juicer feed has rendered (likely to modify it)
        // you can pass in the name of the function to the after attribute
      },
    };
  },
  created() {
    // Following Juicer library files needs to be loaded prior
    // <script src="https://assets.juicer.io/embed.js" type="text/javascript"></script>
    // <link href="https://assets.juicer.io/embed.css" media="all" rel="stylesheet" type="text/css" />

    const juicerJS = document.createElement('script');
    const juicerCSS = document.createElement('link');

    juicerJS.setAttribute('src', 'https://assets.juicer.io/embed-no-jquery.js');
    juicerCSS.setAttribute('href', 'https://assets.juicer.io/embed.css');
    juicerCSS.setAttribute('media', 'all');
    juicerCSS.setAttribute('rel', 'stylesheet');
    juicerCSS.setAttribute('type', 'text/css');

    document.head.appendChild(juicerJS);
    document.head.appendChild(juicerCSS);
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    juicerCallback() {
    },
  },
};
