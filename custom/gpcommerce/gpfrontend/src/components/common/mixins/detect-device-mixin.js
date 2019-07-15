const detectDeviceMixin = {
  data() {
    return {
      nVer: navigator.appVersion,
      nAgt: navigator.userAgent,
    };
  },

  methods: {
    isIEBrowser() {
      return document.documentMode;
    },
    isEdgeBrowser() {
      return /Edge/.test(navigator.userAgent);
    },
    isIOSChrome() {
      return navigator.userAgent.match('CriOS');
    },

    detectDevice() {
      const clientStrings = [
        { s: 'Windows 10', r: /(Windows 10.0|Windows NT 10.0)/ },
        { s: 'Windows 8.1', r: /(Windows 8.1|Windows NT 6.3)/ },
        { s: 'Windows 8', r: /(Windows 8|Windows NT 6.2)/ },
        { s: 'Windows 7', r: /(Windows 7|Windows NT 6.1)/ },
        { s: 'Windows Vista', r: /Windows NT 6.0/ },
        { s: 'Windows Server 2003', r: /Windows NT 5.2/ },
        { s: 'Windows XP', r: /(Windows NT 5.1|Windows XP)/ },
        { s: 'Windows 2000', r: /(Windows NT 5.0|Windows 2000)/ },
        { s: 'Windows ME', r: /(Win 9x 4.90|Windows ME)/ },
        { s: 'Windows 98', r: /(Windows 98|Win98)/ },
        { s: 'Windows 95', r: /(Windows 95|Win95|Windows_95)/ },
        {
          s: 'Windows NT 4.0',
          r: /(Windows NT 4.0|WinNT4.0|WinNT|Windows NT)/,
        },
        { s: 'Windows CE', r: /Windows CE/ },
        { s: 'Windows 3.11', r: /Win16/ },
        { s: 'Windows Phone', r: /Windows/ },
        { s: 'Android', r: /Android/ },
        { s: 'iOS', r: /(iPhone|iPad|iPod)/ },
      ];
      let os;
      const mobile = /Mobile|mini|Fennec|Android|iP(ad|od|hone)/.test(
        this.nVer,
      );
      for (let id = 0; id < clientStrings.length; id += 1) {
        const cs = clientStrings[id];
        if (cs.r.test(this.nAgt)) {
          os = cs.s;
          break;
        } else {
          os = 'unknown';
        }
      }
      const device = { os, mobile };
      return device;
    },

    getDevice() {
      const deviceDetails = this.detectDevice();
      let deviceName;
      if (deviceDetails.mobile) {
        switch (deviceDetails.os) {
          case 'iOS':
            deviceName = 'IOS';
            break;
          case 'Windows Phone':
            deviceName = 'WINDOWS';
            break;
          case 'Android':
            deviceName = 'ANDROID';
            break;
          default:
            if (
              /Kindle Fire|Silk|KFAPWA|KFSOWI|KFJWA|KFJWI|KFAPWI|KFAPWI|KFOT|KFTT|KFTHWI|KFTHWA|KFASWI|KFTBWI|KFMEWI|KFFOWI|KFSAWA|KFSAWI|KFARWI/i.test(
                this.nAgt,
              )
            ) {
              deviceName = 'AMAZON';
            } else {
              deviceName = 'DEFAULT';
            }
        }
      } else {
        deviceName = 'DESKTOP';
        if (
          /Kindle Fire|Silk|KFAPWA|KFSOWI|KFJWA|KFJWI|KFAPWI|KFAPWI|KFOT|KFTT|KFTHWI|KFTHWA|KFASWI|KFTBWI|KFMEWI|KFFOWI|KFSAWA|KFSAWI|KFARWI/i.test(
            this.nAgt,
          )
        ) {
          deviceName = 'AMAZON';
        }
      }
      return deviceName;
    },
    isDeviceAndroid() {
      if (this.getDevice() !== 'IOS' && this.detectDevice().mobile) {
        return false;
      }
      return true;
    },
  },
};

/**
 * @mixin
 */
export default detectDeviceMixin;
