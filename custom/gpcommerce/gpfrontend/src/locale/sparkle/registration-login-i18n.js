const registrationLogin = {
  createAccount: {
    heading: 'Create an Account',
    subHeading: 'Your Information',
    addToMarketComm:
      'I agree to receive occasional news promotions, product information and other communications from Georgia-Pacific Consumer Products.',
    companyModalHeading: 'Give us More Info',
    termasConditionHeading: 'Terms and Condition',
    firstName: 'First Name',
    lastName: 'Last Name',
    email: 'Email Address',
    password: 'Password',
    passwordInstruction:
      'Password must contain upper/lower case letters, numbers, special characters(@,%$*!) and a minimum of 8 characters.',
    mailerText:
      "I'd like to receive marketing & promotion communications from Georgia Pacific.",
    termsPrivacy: "By creating an account you agree to Georgia Pacific's ",
    termsPrivacyB2C: 'By creating an account you agree to our ',
    termsPrivacyLink: 'Terms and Conditions',
    privacyPolicy: ' & Privacy Policy',
    submit: 'Create Account',
    haveAccount: 'Already have an account?',
    gpExpressLinkText: 'Please use below link for',
    gpExpressLink: 'GP Express login',
    login: 'Log in',
    companyDetailsDataModalHeading: 'Enter company details',
    // Internal company details module
    companyName: 'Company Name',
    companyDetailsDecription: 'Company Details',
    addressPart1: 'Address 1',
    addressPart2: 'Address 2 (optional)',
    city: 'City',
    zipcode: 'Zipcode',
    cmpPhnNo: 'Company Phone Number',
    countryLabel: 'Country',
    mobileNo: 'Mobile no',
    other: 'Other',
    skip: 'Skip',
    save: 'Save',
    dropdownInput: 'Select Option',
    emptyStateError: 'Please select a State',
    addressVerificationHeading: 'Address Verification',
    currentAddressHeading: 'Current Address',
    verifiedAddressHeading: 'Verified Address',
    useCurrentAddress: 'Use Current Address',
    useVerifiedAddress: 'Use Verified Address',
    companyNameError: 'Please enter Company Name',
    companyNameMaxError: 'Company Name cannot be more than 40 characters',
    companyPhnNoError: 'Please enter the Company Phone Number',
    phoneMinError: 'Please enter valid Phone Number',
    addressLine1Error: 'Please enter Address line 1',
    cityError: 'Please enter City',
    stateError: 'Please select a State',
    zipCodeError: 'Please enter in a valid Zip/Postal Code',
    zipCodeFormatError: 'Please enter in a valid Zip/Postal Code',
    //
    businessSubHeading: 'Your Information',
    // ErrorCodes
    195: 'An account already exists for that Email Address',
    // Flyout success msg
    registerSuccessMsg: 'You have registered and logged in successfully',
    // Validation messages
    emailRequiredError: 'Please enter in a valid Email Address',
    emailInvalidError: 'Please enter in a valid Email Address',
    passwordRequiredError: 'Please enter in a valid Password',
    passwordInvalidError: 'Please enter in a valid Password',
    passwordMinError: 'Your password must be atleast 8 characters long',
    firstNameRequiredError: 'Please enter in a valid First Name',
    firstNameRegexError: 'Please enter in a valid First Name',
    firstNameMaxError: 'First name should not be more than 35 characters',
    lastNameRequiredError: 'Please enter in a valid Last Name',
    lastNameRegexError: 'Please enter in a valid Last Name',
    lastNameMaxError: 'First name should not be more than 35 characters',
    country: 'Please select a Country',
    gender: 'Gender (Optional)',
    birthDay: 'Birthday (Optional)',
    monthRequiredError: 'Please select the Month',
    dateRequiredError: 'Please select the Date',
    iconEyeShowTitle: 'Show Password',
    iconEyeHideTitle: 'Hide Password',
    iconAlertTriangleTitle: 'Important Notice',
  },
  login: {
    headingforB2c: 'Log in',
    headingForReturningCustomer: 'Returning Customer',
    headingforB2bPart1: 'Log in with your',
    headingforB2bPart2: 'Georgia-Pacific account',
    email: 'Email Address',
    password: 'Password',
    submit: 'Log in',
    forgotPassword: 'Forgot Password?',
    noAccount: "Don't have a GP account?",
    noGPAccount: "Don't have an account?",
    createAccount: 'Create one now',
    gpExpressLinkText: 'Please use below link for',
    gpExpressLink: 'GP Express login',
    // Validation messages
    emailRequiredError: 'Please enter in a valid Email Address',
    emailInvalidError: 'Please enter in a valid Email Address',
    passwordRequiredError: 'Please enter in a valid Password',
    passwordInvalidError: 'Please enter in a valid Password',
    invalidAttemptsError:
      'Your account is locked, please use Forgot Your Password link to reset your password',
    // Internal forgot password module
    passwordModalHeading: 'Reset Password',
    passwordDescription1: 'Please enter your account email address.',
    passwordDescription2:
      'Instructions on how to reset your password will be sent to this address.',
    passwordSocialDescription:
      'If you signed up using a third party service such as Facebook or Google, please login with that instead.',
    passwordButtonValue: 'Reset Password',
    emailLabel: 'Email Address',
    passwordResetPostLabel: 'Password Reset Instructions Sent',
    passwordResetPostDesc:
      'Thank you. Instructions on how to reset your password are being sent to your email address.',
    // ErrorCodes
    189: 'Contact customer service',
    190: 'Please try again, or login with Facebook or Google',
    b2berror190: "Email ID and password doesn't match.",
    191: 'Your account is locked, please use Forgot Your Password link to reset your password.',
    192: 'Something went wrong. Please try again', // siteId error
    193: 'Please try again, or login with Facebook or Google',
    194: 'Token Expired', // 30 days Koch Auth
    // ErrorCode for forget password
    196: 'Cannot reset password for social account',
    198: "Sorry, we couldn't find this email address in our system. \r\nPlease try a different email.",
    199: 'Email id used for registration and authentication are not same',
    support:
      'Call our support specialists if you have any questions or concerns.',
    supportNumber: '800.975.9462',
    checkoutSubmit: 'Log In and Checkout',
    userUpdateSuccess:
      'Your email ID is successfully updated. Please login with new email ID',
    iconEyeShowTitle: 'Show Password',
    iconEyeHideTitle: 'Hide Password',
    activeLoginSession:
      'There is already an active login session. Please close the browser.',
    subscribeText:
      'Items with a subscription require you to log in or sign up to continue checking out.',
  },
  passwordExpiry: {
    passwordHeading: 'Password Reset Link',
    passwordDescription1:
      'Sorry, but that password reset link has expired and is no longer valid. If you still need to update your password, please enter your email address below to request a new link.',
    passwordButtonValue: 'Request New Link',
    emailLabel: 'Email Address',
    // Validation messages
    emailRequiredError: 'Please enter your Email Address',
    emailInvalidError: 'Please enter in a valid Email Address',
    passwordRequiredError: 'Please enter your Password',
    passwordInvalidError: 'Please enter in a valid Password',
    passwordResetPostLabel: 'Password Reset Instructions Sent',
    passwordResetPostDesc:
      'Thank you. Instructions on how to reset your password are being sent to your email address',
    // ErrorCodes
    196: 'Cannot reset password for social account',
    198: 'Sorry we could not find this email address in our system, Please try different one',
  },
  socialLogin: {
    socialRegisterheading: 'Create an account with',
    socialLoginheading: 'Log in with',
    facebook: 'Facebook',
    facebookLoginType: 'facebook',
    googleLoginType: 'google',
    google: 'Google',
    facebookAccessToken: '429517707519313',
    googleAuthCode:
      '301163965846-v6be3aaqbpe4o27thau90ml5tbnkq580.apps.googleusercontent.com',
    support:
      'Call our support specialists if you have any questions or concerns.',
    supportNumber: '800.975.9462',
    // ErrorCodes
    195: 'An account already exists for that email address',
    // Flyout success msg
    registerSuccessMsg: 'You have registered and logged in successfully',
    iconAlertTriangleTitle: 'Important Notice',
    updateEmailHeading: 'Enter Email Address',
    updateEmailBody:
      'An email address is required to finish creating your account. Please enter a valid email address and click Create Account',
    emailAddress: 'Email Address',
    submitBtn: 'Create Account',
    invalidEmailErrorMsg: 'Please enter a valid email address.',
    duplicateMailMsg: 'An account already exists for that email address.',
  },
  updatePassword: {
    currentPasswordLabel: 'Enter Current Password',
    updatePasswordTitle: 'Reset Password',
    newPasswordLabel: 'New Password',
    passwordRequiredError: 'Please enter in a New Password',
    passwordInvalidError:
      'The password selected does not meet the password guidelines, please try again.',
    passwordInstruction:
      'Password must contain upper/lower case letters, numbers, special characters(@,%$*!) and a minimum of 8 characters.',
    newPasswordResetError:
      'The password you have entered matches your previous password. Please enter a different password.',
    showIconLabel: 'Show',
    resetPasswordButton: 'Reset Password',
    passwordSuccessMsg:
      'Success! Your password has been reset. You are now logged into your account.',
    submit: 'Submit',
    // ErrorCodes
    197: 'Link expired',
    iconEyeShowTitle: 'Show Password',
    iconEyeHideTitle: 'Hide Password',
  },
};

export default registrationLogin;
