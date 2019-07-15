const createCookie = (name, value, days) => {
  let expires = '';

  if (days) {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    expires = `; expires= ${date.toUTCString()}`;
  }
  document.cookie = `${name}=${value}${expires}; path=/`;
};

const readCookie = (name) => {
  const nameEQ = `${name}=`;
  const ca = document.cookie.split(';');

  for (let i = 0; i < ca.length; i += 1) {
    let c = ca[i];
    while (c.charAt(0) === ' ') c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
  }
  return null;
};

const eraseCookie = (name) => {
  createCookie(name, '', -1);
};

const cookiesMixin = {
  methods: {
    createCookie,
    readCookie,
    eraseCookie,
  },
};

export default cookiesMixin;
