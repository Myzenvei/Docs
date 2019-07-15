export default {
  name: 'mg-image-text-layout',
  components: {},
  props: {
    componentData: Object,
    componentTheme: Object,
    componentClass: String,
    isForm: Boolean,
  },
  data() {
    return {

    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    setLayoutBackground(imageType, image) {
      return (imageType === 'image' ? 'url(' + image + ') no-repeat' : image);
    }
  }
}
