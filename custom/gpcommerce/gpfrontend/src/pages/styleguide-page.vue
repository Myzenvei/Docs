<template>
<div class="header-container">
  <h1>{{ msg }}</h1>

  <div style="padding:20px;">

    <hr>
    <section style="font-size: 30px;" class="fonts-section">
      <h2>Font Icons</h2>
      <div class="row">
        <div class="col-sm-12">
          <span class="icon-facebook"></span>
          <span class="icon-google"></span>
          <span class="icon-chevron-right"></span>
          <span class="icon-chevron-left"></span>
          <span class="icon-chevron-down"></span>
          <span class="icon-chevron-up"></span>
          <span class="icon-check"></span>
          <span class="icon-bookmark"></span>
          <span class="icon-attachment"></span>
          <span class="icon-alert-triangle"></span>
          <span class="icon-alert-circle"></span>
          <span class="icon-circle"></span>
          <span class="icon-disc"></span>
          <span class="icon-download"></span>
          <span class="icon-edit"></span>
          <span class="icon-eye"></span>
          <span class="icon-file"></span>
          <span class="icon-filter"></span>
          <span class="icon-heart"></span>
          <span class="icon-info"></span>
          <span class="icon-loader"></span>
          <span class="icon-mail"></span>
          <span class="icon-maximize"></span>
          <span class="icon-menu"></span>
          <span class="icon-message-square"></span>
          <span class="icon-minimize"></span>
          <span class="icon-minus"></span>
          <span class="icon-more-horizontal"></span>
          <span class="icon-more-vertical"></span>
          <span class="icon-phone"></span>
          <span class="icon-play"></span>
          <span class="icon-plus"></span>
          <span class="icon-printer"></span>
          <span class="icon-save"></span>
          <span class="icon-search"></span>
          <span class="icon-settings"></span>
          <span class="icon-share"></span>
          <span class="icon-square"></span>
          <span class="icon-star"></span>
          <span class="icon-trash"></span>
          <span class="icon-user"></span>
          <span class="icon-x-circle"></span>
          <span class="icon-x"></span>
          <span class="icon-zoom-in"></span>
          <span class="icon-zoom-out"></span>
          <span class="icon-add-to-list"></span>
          <span class="icon-certification"></span>
          <span class="icon-online"></span>
          <span class="icon-save-cart"></span>
          <span class="icon-seasonal"></span>
          <span class="icon-shipping"></span>
          <span class="icon-subscription"></span>
          <span class="icon-cart"></span>
          <span class="icon-quick-order"></span>
          <span class="icon-star-half-filled"></span>
          <span class="icon-star-filled"></span>
        </div>
      </div>
    </section>

    <hr>

    <div class="row" style="padding:20px;">
      <button class="btn btn-default">Default</button>
      <button class="btn btn-primary">Primary</button>
      <button class="btn btn-tertiary">Tertiary</button>
    </div>
    <div class="row" style="padding:20px;">
      <button class="btn btn-default btn-small-text">Default Small Text</button>
      <button class="btn btn-primary btn-small-text">Primary Small Text</button>
      <button class="btn btn-tertiary btn-small-text">Tertiary Small Text</button>
    </div>
    <div class="row" style="padding:20px;">
        <h1>Custom Checkboxes (No id binding Required - Accessible)</h1>
        <label class="checkbox-container">One
          <input type="checkbox" checked>
          <span class="checkmark"></span>
        </label>
        <label class="checkbox-container">Two
          <input type="checkbox">
          <span class="checkmark"></span>
        </label>
        <label class="checkbox-container">Three
          <input type="checkbox">
          <span class="checkmark"></span>
        </label>
        <label class="checkbox-container">Four
          <input type="checkbox">
          <span class="checkmark"></span>
        </label>

        <h1>Custom Checkboxes ID based - Acccessible</h1>
        <div class="form-group">
          <input id="checkbox" type="checkbox" />
          <label for="checkbox">Checkbox</label>
        </div>

        <h1>Noraml Checkboxes - Acccessible</h1>
        <input id="checkbox" type="checkbox" />Normal Checkbox
        <br/>

      <div class="tag">
        <span class="tag-content">Filter tag</span>
        <!-- <img class="icn-close" src="../assets/icon-cross.svg" alt="Dismiss Tag"> -->
      </div>
    </div>
  </div>

  <div class="row" style="padding:20px;">
    <h6>Form Elements</h6>
    <h7>Input field</h7>
    <section class="form-group">
      <label for="sample">Label</label>
      <input type="text" class="form-control" id="sample" placeholder="Sample">
    </section>
    <h7>Input field without label</h7>
    <section class="form-group">
      <input type="text" class="form-control no-title" id="sample1" placeholder="Sample1">
    </section>
    <h7>Input field with errors</h7>
    <section class="form-group">
      <label for="sample2">Label</label>
      <input type="text" class="form-control input-error" v-validate="'required|alpha'" id="sample2" placeholder="Sample2" name="sample2">
      <span class="error-style">{{errors.first('sample2') }}</span>
    </section>
    <h6>Stepper Control</h6>
    <span>Stepper control one way binding</span>
    <vx-stepper-control :value="stepperVal"></vx-stepper-control>

    <span>Stepper control two way binding</span>
    <vx-stepper-control v-model="stepperVal"></vx-stepper-control>
    <div>{{stepperVal}}</div>
    <h7>Badge</h7>
    <br>
    <span class="badge">13</span>
    <br>
    <h6>Radio Button</h6>
    <section class="form-group">
      <input type="radio" id="radioButton">
      <label for="radioButton">Radio</label>
    </section>
    <h6>Radio Button Group</h6>
    <vx-radio-button-group :radiobutton-values='radioOptions' name="myoptions"></vx-radio-button-group>
  </div>

</div>
</template>

<script>
import vxStepperControl from '../components/common/vx-stepper-control/vx-stepper-control.vue';
import vxRadioButtonGroup from '../components/common/vx-radio-button-group/vx-radio-button-group.vue';

export default {
  name: 'StyleGuide',
  components: {
    vxStepperControl,
    vxRadioButtonGroup,
  },
  data() {
    return {
      msg: 'Style Guide',
      stepperVal: 12,
      radioOptions: [
        { label: 'option1', value: 'option1' },
        { label: 'option2', value: 'option2' },
        { label: 'option3', value: 'option3' },
      ],
    };
  },
};

</script>
