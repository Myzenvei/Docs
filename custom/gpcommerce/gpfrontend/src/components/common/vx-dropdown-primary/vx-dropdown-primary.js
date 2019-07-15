import dropdown from './vx-dropdown-primary-i18n';
import { keyCode as keyCodeValue } from '../../common/mixins/vx-enums';

export default {
  name: 'dropdown-primary',
  $_veeValidate: {
    // value getter
    value() {
      return this.$el.value;
    },
    // name getter
    name() {
      return this.name;
    },
  },
  methods: {
    dropdownOptionChange(item, event) {
      this.removeSelectAttributes();
      event.preventDefault();
      if (!item.disable) {
        this.dropdownInput = item.label;
        this.dropdownInputLabel = item.ariaLabel ? item.ariaLabel : item.label;
        this.selectedItem = {
          label: item.label,
          value: item.value,
        };
        this.$emit('selected-item', item);
        this.$el.value = event.target.dataset.value;
        this.$emit('selected-option', this.selectedItem);
        this.$el.value = event.target.dataset.value;
        // to get index if multiple dropdowns are dynamically populated.
        this.$emit('selected-index', this.index);
        this.$el.focus();
      } else {
        event.stopPropagation();
      }
      this.setSelectAttributes(
        event.target.tagName === 'LI'
          ? event.target
          : event.target.parentElement,
      );
    },
    // To set dropdown value
    setDropdownValue(drpdwnValue) {
      // this.dropdownInput = drpdwnValue;
      if (this.dropdownValues && this.dropdownValues.length !== 0) {
        this.selectedItem = this.dropdownValues.filter(
          (item, index) => item.value === drpdwnValue,
        )[0];
        this.dropdownInput = this.selectedItem.label;
        this.dropdownInputLabel = this.selectedItem.ariaLabel
          ? this.selectedItem.ariaLabel
          : this.selectedItem.label;
      }
    },
    // To get dropdown value
    getDropdownValue() {
      return this.dropdownInputValue;
    },
    // To set dropdown label
    setDropdownLabel(drpdwnLabel) {
      this.dropdownInput = drpdwnLabel;
      let isValAvailable = false;
      this.dropdownValues.forEach((element) => {
        if (element.label === drpdwnLabel) {
          this.dropdownInputLabel = element.ariaLabel
            ? element.ariaLabel
            : element.label;
          isValAvailable = true;
        }
      });
      if (!isValAvailable) {
        this.dropdownInputLabel = drpdwnLabel;
      }
    },
    // To get dropdown label
    getDropdownLabel() {
      return this.dropdownInput;
    },
    // To reset the dropdown
    resetDropdown() {
      this.dropdownInput = this.i18n.defaultDropdownValue;
    },
    // To set whole data as selected Item
    setDropDownItem(selectedOption) {
      this.selectedItem = selectedOption;
      this.dropdownInput = this.selectedItem.label;
      this.dropdownInputLabel = this.selectedItem.ariaLabel;
    },
    typeAhead(event) {
      if (!this.multiSelectDropdown) {
        if (this.timeout) {
          clearTimeout(this.timeout);
        }
        const keyCode = event.keyCode || event.which;
        if (keyCode === this.keyCodeValue.enter) {
          this.selectItemOnClick(event);
        }
        const KeyCodePresence =
          (keyCode >= this.keyCodeValue['0'] &&
            keyCode <= this.keyCodeValue['9']) ||
          (keyCode >= this.keyCodeValue.A && keyCode <= this.keyCodeValue.Z) ||
          (keyCode >= this.keyCodeValue.number0 &&
            keyCode <= this.keyCodeValue.number9) ||
          keyCode === this.keyCodeValue.up ||
          keyCode === this.keyCodeValue.down;
        if (KeyCodePresence) {
          if (
            keyCode === this.keyCodeValue.up ||
            keyCode === this.keyCodeValue.down
          ) {
            this.postFiltering(this.dropdownValues, keyCode);
          } else {
            const charEntered = String.fromCharCode(keyCode);
            this.charsEntered += charEntered.toLowerCase();
            const filteredData = this.dropdownValues.filter(
              item => item.label.toLowerCase().indexOf(this.charsEntered) === 0,
            );
            if (filteredData.length && this.checkSelectedItem()) {
              this.postFiltering(filteredData, keyCode);
            }
          }
          this.timeout = setTimeout(() => {
            this.charsEntered = '';
          }, 400);
        }
      }
    },

    checkSelectedItem() {
      if (this.charsEntered.length > 1) {
        const activeItem = this.isDropDownOpen()
          ? this.activatedItem.label
          : this.dropdownInput;
        return activeItem
          ? !(activeItem.toLowerCase().indexOf(this.charsEntered) === 0)
          : true;
      }
      return true;
    },

    postFiltering(filteredData, keyCode) {
      let indexVal = filteredData
        .map(item => item.label)
        .indexOf(
          this.isDropDownOpen() ? this.activatedItem.label : this.dropdownInput,
        );
      this.removeSelectAttributes();
      if (keyCode === this.keyCodeValue.up) {
        indexVal = indexVal - 1 <= -1 ? filteredData.length - 1 : indexVal - 1;
      } else {
        indexVal = indexVal >= filteredData.length - 1 ? 0 : indexVal + 1;
        if (keyCode !== this.keyCodeValue.down && !this.isDropDownOpen()) {
          this.selectedItem = {
            label: filteredData[indexVal].label,
            value: filteredData[indexVal].value,
          };
          this.dropdownInput = this.selectedItem.label;
        }
      }
      this.activatedItem = {
        label: filteredData[indexVal].label,
        value: filteredData[indexVal].value,
      };
      const activeElement = [
        ...this.$el.querySelectorAll('ul.dropdown-menu>li>a'),
      ].filter(
        item => item.textContent === `${filteredData[indexVal].label}`,
      )[0];
      this.setSelectAttributes(activeElement.parentElement);
      this.$nextTick(() => {
        activeElement.focus({
          preventScroll: false,
        });
        if (!this.isDropDownOpen()) {
          this.$emit('selected-item', filteredData[indexVal]);
          this.$emit('selected-option', this.selectedItem);
          this.$emit('selected-index', this.index);
        }
      });
    },

    selectItemOnClick(event) {
      if (
        this.multiSelectDropdown &&
        event.which === 1 &&
        event.x === 0 &&
        event.y === 0
      ) {
        return;
      }
      let activeItem = this.$el.querySelector('ul.dropdown-menu>li>a');
      if (activeItem) {
        activeItem =
          [...this.$el.querySelectorAll('ul.dropdown-menu>li>a')].filter(
            item => item.textContent === this.dropdownInput,
          )[0] || activeItem;
      }
      this.$nextTick(() => {
        if (activeItem && this.isDropDownOpen()) {
          activeItem.focus({
            preventScroll: false,
          });
          this.removeSelectAttributes();
          this.setSelectAttributes(activeItem.parentElement);
          this.activatedItem = {
            label: activeItem.textContent,
            value: activeItem.dataset.value,
          };
        }
      });
    },

    isDropDownOpen() {
      return this.$el
        .querySelector('.dropdown-primary .dropdown')
        .classList.contains('open');
    },

    removeSelectAttributes() {
      if (this.$el.querySelector('ul.dropdown-menu>li.selected>a')) {
        const element = this.$el.querySelector(
          'ul.dropdown-menu>li.selected>a',
        );
        element.parentElement.classList.remove('selected');
        element.parentElement.removeAttribute('aria-selected');
      }
    },

    setSelectAttributes(element) {
      if (!element.classList.contains('selected')) {
        element.classList.add('selected');
        element.setAttribute('aria-selected', true);
      }
    },

    selectCheckBoxClicked(data, event) {
      let allProductText = '';
      event.stopPropagation();
      const productCodes = [];
      this.multiSelectOptions.map((item) => {
        productCodes.push(item.value);
      });
      if (productCodes.indexOf(data.value) > -1) {
        let index = 0;
        index = productCodes.indexOf(data.value);
        this.multiSelectOptions.splice(index, 1);
      } else {
        this.multiSelectOptions.push(data);
      }
      if (this.multiSelectOptions.length) {
        for (let i = 0; i < this.multiSelectOptions.length; i += 1) {
          const selectedProductText = this.multiSelectOptions[i].label;
          allProductText = allProductText.concat(selectedProductText, ',');
          this.dropdownInput = allProductText;
        }
      } else {
        this.dropdownInput = this.i18n.featureDropdownValue;
      }
    },
    getMultiSelectOptions() {
      return this.multiSelectOptions;
    },
    // To pre-populate multiselect dropdown
    setMultiSelectDropdownValue(multiCheckedItems, productList) {
      let allProductText = '';
      this.multiSelectOptions = [];
      if (!multiCheckedItems.length) {
        productList.map((product) => {
          multiCheckedItems.map((selectedItem) => {
            if (selectedItem === product.product.code) {
              const prepopulatedproduct = {};
              prepopulatedproduct.label = product.product.name;
              prepopulatedproduct.value = product.product.code;
              this.multiSelectOptions.push(prepopulatedproduct);
              const selectedProductText = product.product.name;
              allProductText = allProductText.concat(selectedProductText, ',');
              this.dropdownInput = allProductText;
            }
          });
        });
      } else {
        this.dropdownInput = this.i18n.featureDropdownValue;
        this.multiSelectOptions = [];
      }
    },
  },
  data() {
    return {
      i18n: dropdown,
      dropdownInput: 'Select option',
      dropdownInputLabel: 'Select option',
      selectedItem: {
        label: '',
        value: '',
      },
      charsEntered: '',
      timeout: '',
      keyCodeValue,
      activatedItem: {
        label: '',
        value: '',
      },
      multiSelectOptions: [],
    };
  },
  props: {
    dropdownValues: {},
    value: {},
    name: {},
    dropdownError: {},
    isDisabled: {},
    index: {},
    multiSelectDropdown: {
      type: Boolean,
      default: false,
    },
  },
  mounted() {
    this.$emit('primaryDropdownMounted', {});
    this.$el.value = this.value;
  },
};
