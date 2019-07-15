const setStorage = (key, value) => localStorage.setItem(key, value);
const getStorage = key => localStorage[key];
const deleteStorage = (key) => {
  if (getStorage(key)) {
    localStorage.removeItem(key);
  }
};

const storageMixin = {
  methods: {
    setStorage,
    // get the storage
    getStorage,
    // delete from storage
    deleteStorage,
  },
};

export default storageMixin;
