const accessCustomerService = {
  liveChat: {
    liveChat: 'Live Chat',
    callOrTextUsBody: 'Call our support specialists if you have any questions or concerns.',
    callOrTextUsHeading: 'Call Us',
    callNumber: '800-975-9462',
    callOrTextUsTimings1: '(9 a.m. - 6 p.m. EST) Monday - Friday',
    // commenting as not getting used
    // callOrTextUsTimings2: '(8 a.m. - 6 p.m. ET) Friday',
    callText: 'Call:',
    iconPhoneTitle: 'Call Us',
    iconMessageTitle: 'Chat',
  },
  serviceTicket: {
    heading: 'Send Email',
    topicOfInquiry: 'Topic of Inquiry',
    firstName: 'First Name',
    lastName: 'Last Name',
    email: 'Email Address',
    phone: 'Phone',
    optional: '(optional)',
    jobTitle: 'Job Title',
    streetAddress: 'Address 1',
    company: 'Company Name',
    country: 'Country',
    state: 'State',
    city: 'City',
    postalCode: 'Zip/Postal Code',
    orderId: 'Order #',
    questionsOrComments: 'Questions or Comments',
    attachFile: 'Attach File',
    chooseFile: 'Choose File to Attach',
    countryList: [{
        label: 'United States',
        value: 'US',
      },
      {
        label: 'Canada',
        value: 'CA',
      },
      {
        label: 'Mexico',
        value: 'MX',
      },
      {
        label: 'International Others',
        value: 'INTL',
      },
    ],
    submit: 'Send',
    remove: 'Remove',
    haveAccount: "Already have a account?. <a href='#'>Log in</a>.",
    commentsCharacterCount: 'of chars left',
    submitFailure: 'Your ticket is not created. Please try again.',
    submitSuccess: 'Your ticket has been created successfully',
    fileAttachment: {
      heading: 'Attach Files',
      helpText: 'If you have a file, use upload or drag and drop functionality below.',
      dragDropCaption: 'Drag and drop file here',
      fileAttachCaption1: 'Or',
      fileAttachCaption2: 'Choose File to Upload',
      removeText: 'Remove File',
      upload: 'UPLOAD',
      fileSize: 'File size: ',
      fileSizeUnitKB: 'KB',
      fileSizeUnitMB: 'MB',
      attachmentSizeLimitError: 'You have exceeded our allowed attachment size. Please attach a file below 10 MB.',
      duplicateFileNameError: 'This name already exists.Please provide a unique name',
      attachmentTypeError: 'Please use an attachment in one of the following formats: .jpg, .jpeg, .gif, .png, .txt, .doc, .docx, .pdf, .ppt, .pptx, .xls, .xlsx, .csv',
      iconAttachmentTitle: 'Attachment',
    },
    formErrorMsg: {
      topicOfInquiry: {
        required: 'Please select the "Topic of Inquiry"',
      },
      firstName: {
        required: 'Please provide first name',
        regex: 'Please provide first name',
      },
      lastName: {
        required: 'Please provide last name',
        regex: 'Please provide last name',
      },
      email: {
        required: 'Please provide email address',
        email: 'Please provide valid email address',
      },
      address: {
        required: 'Please add the address',
      },
      city: {
        required: 'Please add the City',
      },
      state: {
        required: 'Please Select the State',
      },
      zipcode: {
        required: 'Please enter the Zipcode',
        regex: 'Please enter the Zipcode',
        numeric: 'Please enter the Zipcode',
        alpha_num: 'Please enter the Zipcode',
      },
      orderId: {
        regex: 'Please enter a valid Order #',
      },
      comments: {
        required: 'Please provide comments or questions.',
      },
      phone: {
        min: 'Please provide a valid phone number',
      },
    },
    userRoles: {
      administrators: 'Administrators',
      buyers: 'Buyers',
      admin: 'Admin',
      customer: 'Customer',
    },
  },
};

export default accessCustomerService;
