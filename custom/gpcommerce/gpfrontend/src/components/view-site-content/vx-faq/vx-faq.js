import AccordionComp from '../../../components/common/vx-accordion/vx-accordion.vue';

export default {
  name: 'vx-faq',
  components: {
    'accordion-comp': AccordionComp
  },
  props: {
    accordionGroupData: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      manipulatedArray: []
    }
  },
  computed: {

  },
  mounted() {
    this.manipulatedArray = this.manipulateData(this.accordionGroupData);
  },
  methods: {
    manipulateData(data) {
      return data.match(/<que>(.*?)<\/ans>/g).map(function(faq, index) {
        return {
          "accordionTitle": faq.match(/<que>(.*?)<\/que>/g)[0].replace(/<\/?que>/g, ''),
          "accordionId": faq.match(/<que>(.*?)<\/que>/g)[0].replace(/<\/?que>/g, '') + 'i' + index,
          "accordionBody": faq.match(/<ans>(.*?)<\/ans>/g)[0].replace(/<\/?ans>/g, '')
        };
      });
    }
  }
}