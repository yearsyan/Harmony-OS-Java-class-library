package ohos.distributedhardware.devicemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ohos.distributedhardware.devicemanager.DeviceInfo;
import ohos.distributedhardware.devicemanager.DmAccessAdapter;
import ohos.distributedhardware.devicemanager.IDeviceStateCallback;
import ohos.distributedhardware.devicemanager.ISelectDeviceListListener;

public class DeviceManager {
    private static final Object LOCK = new Object();
    public static final String SIFT_FILTER = "siftFilter";
    public static final String SORT_TYPE = "sortType";
    private static final String TAG = "DeviceManager";
    public static final String TARGET_PACKAGE_NAME = "targetPkgName";
    private static DeviceManager sDeviceManager;
    private static DeviceManagerCallback sDeviceManagerCallback;
    private static DmAccessAdapter sDmAccessAdapter;
    private DeviceStateCallback mDeviceStateCallback;
    private IDeviceStateCallback mIDeviceStateCallback;

    public interface DeviceManagerCallback {
        void onDied();

        void onGet(DeviceManager deviceManager);
    }

    private DeviceManager() {
        this.mDeviceStateCallback = null;
        this.mIDeviceStateCallback = new IDeviceStateCallback.Stub() {
            /* class ohos.distributedhardware.devicemanager.DeviceManager.AnonymousClass1 */

            @Override // ohos.distributedhardware.devicemanager.IDeviceStateCallback
            public void onDeviceOffline(DeviceBasicInfo deviceBasicInfo) {
                if (DeviceManager.this.mDeviceStateCallback == null) {
                    HwLog.error(DeviceManager.TAG, "onDeviceOffline callback is null");
                }
                DeviceManager.this.mDeviceStateCallback.onDeviceOffline(DeviceManager.this.convertDeviceInfo(deviceBasicInfo));
            }

            @Override // ohos.distributedhardware.devicemanager.IDeviceStateCallback
            public void onDeviceOnline(DeviceBasicInfo deviceBasicInfo) {
                if (DeviceManager.this.mDeviceStateCallback == null) {
                    HwLog.error(DeviceManager.TAG, "onDeviceOnline callback is null");
                }
                DeviceManager.this.mDeviceStateCallback.onDeviceOnline(DeviceManager.this.convertDeviceInfo(deviceBasicInfo));
            }
        };
    }

    public static synchronized int createInstance(DeviceManagerCallback deviceManagerCallback) {
        synchronized (DeviceManager.class) {
            synchronized (LOCK) {
                HwLog.info(TAG, "createInstance");
                if (deviceManagerCallback == null) {
                    HwLog.error(TAG, "createInstance but callback is null");
                    return 1001;
                }
                sDeviceManagerCallback = deviceManagerCallback;
                if (sDeviceManager != null) {
                    HwLog.info(TAG, "createInstance but instance has been exist");
                    sDeviceManagerCallback.onGet(sDeviceManager);
                    return 0;
                }
                HwLog.info(TAG, "createInstance begin");
                int createInstance = DmAccessAdapter.createInstance(new DmAccessAdapter.DmAccessAdapterCallback() {
                    /* class ohos.distributedhardware.devicemanager.DeviceManager.AnonymousClass2 */

                    @Override // ohos.distributedhardware.devicemanager.DmAccessAdapter.DmAccessAdapterCallback
                    public void onAdapterGet(DmAccessAdapter dmAccessAdapter) {
                        synchronized (DeviceManager.LOCK) {
                            HwLog.info(DeviceManager.TAG, "DmAccessAdapter.createInstance onAdapterGet");
                            if (dmAccessAdapter == null) {
                                DeviceManager.sDeviceManagerCallback.onDied();
                                DeviceManager.releaseInstance();
                                return;
                            }
                            if (DeviceManager.sDeviceManager == null) {
                                DeviceManager unused = DeviceManager.sDeviceManager = new DeviceManager();
                            }
                            DmAccessAdapter unused2 = DeviceManager.sDmAccessAdapter = dmAccessAdapter;
                            if (DeviceManager.sDeviceManagerCallback != null) {
                                DeviceManager.sDeviceManagerCallback.onGet(DeviceManager.sDeviceManager);
                            }
                        }
                    }

                    @Override // ohos.distributedhardware.devicemanager.DmAccessAdapter.DmAccessAdapterCallback
                    public void onBinderDied() {
                        synchronized (DeviceManager.LOCK) {
                            HwLog.info(DeviceManager.TAG, "DmAccessAdapter onBinderDied");
                            DeviceManager.sDeviceManagerCallback.onDied();
                            DeviceManager.releaseInstance();
                        }
                    }
                });
                HwLog.info(TAG, "createInstance end");
                return createInstance;
            }
        }
    }

    public static synchronized void releaseInstance() {
        synchronized (DeviceManager.class) {
            synchronized (LOCK) {
                HwLog.info(TAG, "releaseInstance");
                sDeviceManager = null;
                sDeviceManagerCallback = null;
                sDmAccessAdapter = null;
                HwLog.info(TAG, "releaseInstance begin");
                DmAccessAdapter.releaseInstance();
                HwLog.info(TAG, "releaseInstance end");
            }
        }
    }

    public List<DeviceInfo> getTrustedDeviceList(String str, Map<String, Object> map) throws SecurityException {
        synchronized (LOCK) {
            HwLog.info(TAG, "getTrustedDeviceList");
            if (sDmAccessAdapter == null) {
                HwLog.error(TAG, "getTrustedDeviceList but sDmAccessAdapter is null");
                return new ArrayList(0);
            } else if (str == null) {
                HwLog.error(TAG, "getTrustedDeviceList invalid parameter");
                return new ArrayList(0);
            } else {
                HwLog.info(TAG, "getTrustedDeviceList begin");
                List<DeviceBasicInfo> trustedDeviceList = sDmAccessAdapter.getTrustedDeviceList(str, map);
                HwLog.info(TAG, "getTrustedDeviceList end");
                ArrayList arrayList = new ArrayList(0);
                if (trustedDeviceList != null) {
                    for (DeviceBasicInfo deviceBasicInfo : trustedDeviceList) {
                        arrayList.add(convertDeviceInfo(deviceBasicInfo));
                    }
                }
                return arrayList;
            }
        }
    }

    public int selectDeviceList(String str, Map<String, Object> map, final SelectDeviceListListener selectDeviceListListener) throws SecurityException {
        synchronized (LOCK) {
            HwLog.info(TAG, "selectDeviceList");
            if (sDmAccessAdapter == null) {
                HwLog.error(TAG, "selectDeviceList but sDmAccessAdapter is null");
                return 1006;
            }
            if (!(str == null || map == null)) {
                if (selectDeviceListListener != null) {
                    AnonymousClass3 r1 = new ISelectDeviceListListener.Stub() {
                        /* class ohos.distributedhardware.devicemanager.DeviceManager.AnonymousClass3 */

                        @Override // ohos.distributedhardware.devicemanager.ISelectDeviceListListener
                        public int onGetDeviceList(List<DeviceBasicInfo> list, int i) {
                            ArrayList arrayList = new ArrayList(0);
                            for (DeviceBasicInfo deviceBasicInfo : list) {
                                arrayList.add(DeviceManager.this.convertDeviceInfo(deviceBasicInfo));
                            }
                            selectDeviceListListener.onGetDeviceList(arrayList, i);
                            return 0;
                        }
                    };
                    HwLog.info(TAG, "selectDeviceList begin");
                    int selectDeviceList = sDmAccessAdapter.selectDeviceList(str, map, r1);
                    HwLog.info(TAG, "selectDeviceList end");
                    return selectDeviceList;
                }
            }
            HwLog.error(TAG, "selectDeviceList invalid parameter");
            return 1001;
        }
    }

    public DeviceInfo getLocalDeviceInfo() throws SecurityException {
        synchronized (LOCK) {
            HwLog.info(TAG, "getLocalDeviceInfo");
            if (sDmAccessAdapter == null) {
                HwLog.error(TAG, "getLocalDeviceInfo but sDmAccessAdapter is null");
                return null;
            }
            HwLog.info(TAG, "getLocalDeviceInfo begin");
            DeviceBasicInfo localDeviceInfo = sDmAccessAdapter.getLocalDeviceInfo();
            HwLog.info(TAG, "getLocalDeviceInfo end");
            return convertDeviceInfo(localDeviceInfo);
        }
    }

    public int registerDeviceStateCallback(String str, Map<String, Object> map, DeviceStateCallback deviceStateCallback) throws SecurityException {
        synchronized (LOCK) {
            HwLog.info(TAG, "registerDeviceStateCallback");
            if (sDmAccessAdapter == null) {
                HwLog.error(TAG, "registerDeviceStateCallback but sDmAccessAdapter is null");
                return 1006;
            }
            if (str != null) {
                if (deviceStateCallback != null) {
                    this.mDeviceStateCallback = deviceStateCallback;
                    HwLog.info(TAG, "registerDeviceStateCallback begin");
                    int registerDeviceStateCallback = sDmAccessAdapter.registerDeviceStateCallback(str, map, this.mIDeviceStateCallback);
                    HwLog.info(TAG, "registerDeviceStateCallback end");
                    return registerDeviceStateCallback;
                }
            }
            HwLog.error(TAG, "registerDeviceStateCallback invalid parameter");
            return 1001;
        }
    }

    public int unregisterDeviceStateCallback(String str, DeviceStateCallback deviceStateCallback) throws SecurityException {
        synchronized (LOCK) {
            HwLog.info(TAG, "unregisterDeviceStateCallback");
            if (sDmAccessAdapter == null) {
                HwLog.error(TAG, "unregisterDeviceStateCallback but sDmAccessAdapter is null");
                return 1006;
            }
            if (str != null) {
                if (deviceStateCallback != null) {
                    this.mDeviceStateCallback = null;
                    HwLog.info(TAG, "unregisterDeviceStateCallback begin");
                    int unregisterDeviceStateCallback = sDmAccessAdapter.unregisterDeviceStateCallback(str, this.mIDeviceStateCallback);
                    HwLog.info(TAG, "unregisterDeviceStateCallback end");
                    return unregisterDeviceStateCallback;
                }
            }
            HwLog.error(TAG, "unregisterDeviceStateCallback invalid parameter");
            return 1001;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private DeviceInfo convertDeviceInfo(DeviceBasicInfo deviceBasicInfo) {
        if (deviceBasicInfo != null) {
            return new DeviceInfo(deviceBasicInfo.getDeviceId(), deviceBasicInfo.getDeviceName(), DeviceInfo.DeviceType.valueOf(deviceBasicInfo.getDeviceType().value()));
        }
        HwLog.info(TAG, "DeviceBasicInfo is null");
        return null;
    }
}
