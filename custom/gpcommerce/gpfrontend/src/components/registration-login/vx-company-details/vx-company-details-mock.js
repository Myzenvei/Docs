// This will come from service
const companyDetailsMock = {
  role: {
    label: 'Your Role',
    options: [
      {
        label: 'role1',
        value: 'role1',
      },
      {
        label: 'role2',
        value: 'role2',
      },
      {
        label: 'role3',
        value: 'role3',
      },
    ],
  },
  companySurveyDetails: {
    questionSelected: [
      {
        code: 'Q1',
        label: 'test question1',
        options: [
          {
            code: 'o1',
            label: 'option 1',
            reDirectQuestion: 'Q2',
          },
          {
            code: 'o2',
            label: 'option 2',
            reDirectQuestion: 'Q3',
          },
        ],
        questionType: 'SELECT',
      },
      {
        code: 'Q2',
        label: 'test question2',
        options: [
          {
            code: 'o22',
            label: 'option 2',
            reDirectQuestion: 'Q3',
          },
          {
            code: 'o23',
            label: 'option 3',
            reDirectQuestion: 'Q4',
          },
        ],
        questionType: 'RADIO',
      },
      {
        code: 'Q3',
        label: 'test question3',
        options: [
          {
            code: 'o31',
            label: 'option 32',
            reDirectQuestion: 'Q4',
          },
          {
            code: 'o32',
            label: 'option 33',
            reDirectQuestion: 'Q4',
          },
        ],
        questionType: 'SELECT',
      },
      {
        code: 'Q4',
        label: 'test question4',
        options: [
          {
            code: 'o42',
            label: 'option 42',
            reDirectQuestion: '',
          },
          {
            code: 'o43',
            label: 'option 43',
            reDirectQuestion: '',
          },
        ],
        questionType: 'RADIO',
      },
    ],
    surveyCode: 'S1',
  },
  employeeNo: {
    label: 'How many employees do you have in your location?',
    options: [
      {
        label: '20',
        value: '20',
      },
      {
        label: '30',
        value: '30',
      },
      {
        label: '120',
        value: '120',
      },
    ],
  },
  productAreas: {
    label: 'Which areas do you need products for?',
    options: [
      {
        label: 'Break Room',
        value: 'Break Room',
      },
      {
        label: 'Bathroom',
        value: 'Bathroom',
      },
      {
        label: 'Kitchen Area',
        value: 'Kitchen Area',
      },
      {
        label: 'Patient Room',
        value: 'Patient Room',
      },
    ],
  },
  companyEmployeeNo: {
    label: 'Number of Employees',
    options: [
      {
        label: '20',
        value: '20',
      },
      {
        label: '50',
        value: '50',
      },
      {
        label: '100',
        value: '100',
      },
    ],
  },
  typeOfBusiness: {
    label: 'Type of Business',
    options: [
      {
        label: 'business type1',
        value: 'business type1',
      },
      {
        label: 'business type2',
        value: 'business type2',
      },
      {
        label: 'business type3',
        value: 'business type3',
      },
    ],
  },
};
export default companyDetailsMock;
