export const fileSizeMixin = {
  methods: {
    getFileSize(fileList) {
      let sizeInMB = 0,
        totalSizeInBytes = 0,
        fileSizeToValidate = 0;
      fileList.map(file => totalSizeInBytes += file.size);
      sizeInMB = totalSizeInBytes / Math.pow(1024, 2);
      return sizeInMB;
    },
  },
};
