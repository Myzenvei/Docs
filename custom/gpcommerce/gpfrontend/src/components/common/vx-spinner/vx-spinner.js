export default {
  name: 'vx-spinner',
  components: {}, 
  props: {
    fullScreen: Boolean,
    image: String,
    text: String,
  },
  data () {
    return {
      spinnerVisible: false
    }
  },
  computed: {

  },
  mounted () {

  },
  methods: {
    // Call to show Spinner
    showSpinner() {
      this.spinnerVisible = true;
    },
    // Call to hide Spinner
    hideSpinner() {
      this.spinnerVisible = false;
    }

  }
}
