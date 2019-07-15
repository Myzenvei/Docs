import VeeValidate from 'vee-validate';

const email = {
  getMessage(field, args) {
    return `The ${field} must be a valid email`;
  },
  validate(value, args) {
    const EMAILREG = /^\w+[\w-\.]*\@\w+((-\w+)|(\w*))\.[a-z]{2,3}$/;
    return VeeValidate.Rules.email(value) || EMAILREG.test(value);
  },
};

VeeValidate.Validator.extend('email', email);

export default email;
