import FocusTrap from 'vue-focus-lock';

export default {
  name: 'vx-modal',
  components: {
    FocusTrap,
  },
  props: {
    heading: {
      type: String,
    },
    size: {
      type: String,
      default: 'small',
    },
    isModalScrollable: {
      type: Boolean,
      default: false,
    },
    showCloseBtn: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      showModal: false,
      callingParent: null,
    };
  },
  computed: {
    wrapperSize() {
      if (this.size === 'small') {
        return 'col-xs-12 px-xs-0 modal-small';
      } else if (this.size === 'large') {
        return 'col-xs-12 px-xs-0 modal-large';
      } else if (this.size === 'medium') {
        return 'col-xs-12 px-xs-0 modal-medium';
      } else if (this.size === 'extra-small') {
        return 'col-xs-12 px-xs-0 modal-extra-small';
      }
    },
  },
  created() {
    /* Added event for Esc in Internet Explorer */
    document.body.addEventListener('keyup', (e) => {
      if (e.keyCode === 27 && this.showModal) {
        this.closeModal();
      }
    });
  },
  mounted() {},
  methods: {
    // this open is to be used by the parent using refs
    open(callingParent) {
      this.showModal = true;
      document.body.classList.add('modal-open');
      setTimeout(() => {
        if (this.$el.querySelector('.heading-container h2')) {
          this.$el.querySelector('.heading-container h2').focus();
        }
      }, 500);
      if (callingParent) {
        this.callingParent = callingParent;
      }
    },
    // this close is to be used by the parent using refs
    close() {
      this.showModal = false;
      document.body.classList.remove('modal-open');
      this.focusCallingTarget();
    },
    // this function will be called when we click on close icon
    closeModal(event) {
      this.showModal = false;
      this.$emit('close');
      document.body.classList.remove('modal-open');
      this.focusCallingTarget();
    },
    /* For Focusing on the parent on click of which the modal opened, this can be used */
    focusCallingTarget() {
      this.$nextTick(() => {
        if (this.callingParent && this.callingParent.target) {
          setTimeout(() => {
            this.callingParent.target.focus();
          }, 100);
        }
      });
    },
  },
};
