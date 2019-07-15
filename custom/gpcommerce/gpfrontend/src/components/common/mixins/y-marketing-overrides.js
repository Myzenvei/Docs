const ymOverrides = {
  maskTelephone() {
    const telephoneField = document.querySelector('.sapCRLPage .sap-tele-num');
    if (telephoneField) {
      const telephoneNodes = document.querySelectorAll('.sapCRLPage .sap-tele-num');
      for(let i=0; i < telephoneNodes.length; i++) {
        let el = telephoneNodes[i];
        el.addEventListener('input', (e) => {
          /**
           * regular expression to match the telephone number with 10 digits
           */
          const x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
          /**
           * masking the input value as (xxx)xxx-xxxx
           */
          e.target.value = !x[2] ? x[1] : `(${x[1]}) ${x[2]}${x[3] ? `-${x[3]}` : ''}`;
        });
      }
    }
  },
  /**
   * the below class names inside array should be
   * authored in yforms at the respective
   * inputs with that lables
   */
  setHiddenFieldValues() {
    const queryParamsObj = {};
    const utmArray = ['utm_source', 'utm_medium', 'utm_campaign', 'utm_term', 'utm_content'];
    const queryParam = window.location.search.substr(1).split('&');
    queryParam.forEach((el) => {
      queryParamsObj[el.split('=')[0]] = el.split('=')[1];
    });
    utmArray.forEach((element) => {
      document.querySelectorAll(`.${element}`).forEach((el) => {
        const utmId = el;
        utmId.value = queryParamsObj[element] ? queryParamsObj[element] : '';
      });
    });
  },
};
export default ymOverrides;
