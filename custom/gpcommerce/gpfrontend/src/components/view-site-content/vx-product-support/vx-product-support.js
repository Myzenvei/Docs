import vxProductCategory from '../vx-product-category/vx-product-category.vue';
export default {
  name: 'vx-product-support',
  components: {
    'vx-product-category': vxProductCategory
  },
  props: {
    tabsData: {
      type: Array,
      required: true
    },
    componentTitle: {
      type: String,
      required: true
    },
    componentId: {
      type: String
    },
    componentTheme: {
      type: String
    },
    activeTab: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      tabBody: []
    }
  },
  computed: {

  },
  mounted() {
    $('.vx-product-support [data-toggle="tab"]')
      .on('shown.bs.tab', (e) => {
        this.toggleAttrs(e, 'true', '0', 'false', '1');
      })
      .on('hidden.bs.tab', (e) => {
        this.toggleAttrs(e, 'false', '-1', 'true', '0');
      });
    const keys = new this.KeyCodes();
    $('.nav li a').on('keydown', function (e) {
      const currentTab = $(this).closest('li');
      if (e.which === keys.left) {
        e.preventDefault();
        if (currentTab.prev().length === 0) {
          currentTab.nextAll().last().find('a').click()
        } else {
          currentTab.prev().find('a').click();
        }
      }
      if (e.which === keys.right) {
        e.preventDefault();
        if (currentTab.next().length === 0) {
          currentTab.prevAll().last().find('a').click()
        } else {
          currentTab.next().find('a').click();
        }
      }
      if (e.which === keys.home && currentTab.prev().length > 0) {
        e.preventDefault();
        currentTab.prevAll().last().find('a').click();
      }
      if (e.which === keys.end && currentTab.next().length > 0) {
        e.preventDefault();
        currentTab.nextAll().last().find('a').click();
      }
    });
  },
  methods: {
    toggleAttrs(e, ariaSelected, tabIndex, ariaHidden, focus) {
      const target = e.target;
      const id = $(target).attr('href');
      $(target)
        .attr({
          'aria-selected': ariaSelected,
          'tabindex': tabIndex,
        })
        .closest('.vx-product-support').find(id).attr({
          'aria-hidden': ariaHidden,
        });
      if (focus === '1') {
        target.focus();
      }
    },
    KeyCodes() {
      // Define values for keycodes
      this.left = 37;
      this.right = 39;
      this.end = 35;
      this.home = 36;
    }
  }
}
