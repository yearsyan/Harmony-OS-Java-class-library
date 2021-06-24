package ohos.distributedhardware.devicemanager;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.huawei.android.app.ActivityThreadEx;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import ohos.distributedhardware.devicemanager.DmAccessInterface;

public final class DmAccessAdapter {
    private static final String DMACCESS_SERVICE_NAME = "com.huawei.systemserver.dmaccessservice.DmAccessService";
    private static final String DM_ACTION = "com.huawei.systemserver.dms.action.LOADER";
    private static final Object DM_LOCK = new Object();
    private static final String DM_PKG_NAME = "com.huawei.systemserver";
    private static final int ERROR_CODE_DISCONNECTED = 1006;
    private static final int ERROR_CODE_INVALID_ARGUMENT = 1001;
    private static final int ERROR_CODE_REMOTE_EXCEPTION = 1005;
    private static final int SUCCESS = 0;
    private static final String TAG = "DmAccessAdapter";
    private static DMAccessServiceConnection sConnection;
    private static Context sContext;
    private static boolean sDMBound = false;
    private static DmAccessAdapter sDmAccessAdapter;
    private static DmAccessInterface sDmAccessInterface;

    public interface DmAccessAdapterCallback {
        void onAdapterGet(DmAccessAdapter dmAccessAdapter);

        void onBinderDied();
    }

    private DmAccessAdapter() {
    }

    public static synchronized int createInstance(DmAccessAdapterCallback dmAccessAdapterCallback) {
        synchronized (DmAccessAdapter.class) {
            HwLog.info(TAG, "createInstance start ");
            synchronized (DM_LOCK) {
                if (dmAccessAdapterCallback == null) {
                    HwLog.error(TAG, "createInstance but callback is null");
                    return 1001;
                }
                Application currentApplication = ActivityThreadEx.currentApplication();
                if (currentApplication == null) {
                    HwLog.error(TAG, "createInstance but currentApplication is null");
                    return 1006;
                }
                sContext = currentApplication.getApplicationContext();
                HwLog.info(TAG, "createInstance context: " + sContext);
                if (sContext != null) {
                    HwLog.info(TAG, "start connect getPackageName: " + sContext.getPackageName());
                    HwLog.info(TAG, "start connect getPackageResourcePath: " + sContext.getPackageResourcePath());
                    if (sDmAccessAdapter != null) {
                        dmAccessAdapterCallback.onAdapterGet(sDmAccessAdapter);
                        return 0;
                    }
                    return bindService(dmAccessAdapterCallback);
                }
                HwLog.error(TAG, "createInstance but context is null");
                return 1006;
            }
        }
    }

    public static synchronized void releaseInstance() {
        synchronized (DmAccessAdapter.class) {
            if (sContext == null) {
                HwLog.error(TAG, "Instance of DmAccessAdapter already released or have not got yet");
                return;
            }
            unbindService(sContext);
            sContext = null;
        }
    }

    private static synchronized int bindService(DmAccessAdapterCallback dmAccessAdapterCallback) {
        synchronized (DmAccessAdapter.class) {
            HwLog.info(TAG, "bindService");
            synchronized (DM_LOCK) {
                if (sDMBound) {
                    return 0;
                }
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(DM_PKG_NAME, DMACCESS_SERVICE_NAME));
                try {
                    sContext.bindService(intent, 1, Executors.newSingleThreadExecutor(), new DMAccessServiceConnection(sContext, dmAccessAdapterCallback));
                    return 0;
                } catch (SecurityException e) {
                    HwLog.error(TAG, "bindService DMAccessService ERROR:" + e.getLocalizedMessage());
                    return 1005;
                }
            }
        }
    }

    private static void unbindService(Context context) {
        try {
            synchronized (DM_LOCK) {
                HwLog.debug(TAG, "unbindService sDMBound = " + sDMBound);
                if (sDMBound) {
                    sDmAccessInterface = null;
                    context.unbindService(sConnection);
                    sDMBound = false;
                    sDmAccessAdapter = null;
                }
            }
        } catch (SecurityException e) {
            HwLog.error(TAG, "error in unbindService DMAccessService" + e.getLocalizedMessage());
            sDMBound = false;
        } catch (IllegalArgumentException e2) {
            HwLog.error(TAG, "IllegalArgumentException in unbindService DMAccessService" + e2.getLocalizedMessage());
            sDMBound = false;
        }
    }

    /* access modifiers changed from: private */
    public static class DMAccessServiceConnection implements ServiceConnection {
        private static final String TAG = "DMAccessServiceConnection";
        private DmAccessAdapterCallback callback;
        private Context context;

        DMAccessServiceConnection(Context context2, DmAccessAdapterCallback dmAccessAdapterCallback) {
            this.context = context2;
            this.callback = dmAccessAdapterCallback;
            HwLog.info(TAG, "DMAccessServiceConnection construct");
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            synchronized (DmAccessAdapter.DM_LOCK) {
                HwLog.info(TAG, "DMAccessService onServiceConnected  || service " + iBinder);
                DmAccessInterface unused = DmAccessAdapter.sDmAccessInterface = DmAccessInterface.Stub.asInterface(iBinder);
                if (DmAccessAdapter.sDmAccessInterface == null) {
                    HwLog.error(TAG, "dmaccessservice permission denied");
                    return;
                }
                try {
                    DmAccessAdapter.sDmAccessInterface.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                        /* class ohos.distributedhardware.devicemanager.DmAccessAdapter.DMAccessServiceConnection.AnonymousClass1 */

                        public void binderDied() {
                            HwLog.info(DMAccessServiceConnection.TAG, "DMAccessService onBinderDied");
                            if (DMAccessServiceConnection.this.callback != null) {
                                DMAccessServiceConnection.this.callback.onBinderDied();
                            }
                            DmAccessAdapter.releaseInstance();
                        }
                    }, 0);
                } catch (RemoteException unused2) {
                    HwLog.error(TAG, "Call DMAccessService linkToDeath RemoteException");
                }
                DMAccessServiceConnection unused3 = DmAccessAdapter.sConnection = this;
                if (DmAccessAdapter.sDmAccessAdapter == null) {
                    DmAccessAdapter unused4 = DmAccessAdapter.sDmAccessAdapter = new DmAccessAdapter();
                }
                boolean unused5 = DmAccessAdapter.sDMBound = true;
                if (this.callback != null) {
                    this.callback.onAdapterGet(DmAccessAdapter.sDmAccessAdapter);
                }
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            synchronized (DmAccessAdapter.DM_LOCK) {
                HwLog.info(TAG, "DMAccessService onServiceDisconnected");
                boolean unused = DmAccessAdapter.sDMBound = false;
                DmAccessInterface unused2 = DmAccessAdapter.sDmAccessInterface = null;
                DmAccessAdapter unused3 = DmAccessAdapter.sDmAccessAdapter = null;
            }
        }
    }

    public List<DeviceBasicInfo> getTrustedDeviceList(String str, Map<String, Object> map) {
        synchronized (DM_LOCK) {
            HwLog.info(TAG, "getTrustedDeviceList");
            if (sDmAccessInterface == null) {
                HwLog.error(TAG, "getTrustedDeviceList but sDmAccessInterface is null");
                return new ArrayList();
            }
            Bundle bundle = new Bundle();
            if (map != null) {
                if (map.containsKey(DeviceManager.TARGET_PACKAGE_NAME)) {
                    if (map.get(DeviceManager.TARGET_PACKAGE_NAME) instanceof String) {
                        bundle.putString(DeviceManager.TARGET_PACKAGE_NAME, (String) map.get(DeviceManager.TARGET_PACKAGE_NAME));
                    } else {
                        HwLog.error(TAG, "getTrustedDeviceList targetPkgName invalid");
                    }
                }
                if (map.containsKey(DeviceManager.SORT_TYPE)) {
                    if (map.get(DeviceManager.SORT_TYPE) instanceof Integer) {
                        bundle.putInt(DeviceManager.SORT_TYPE, ((Integer) map.get(DeviceManager.SORT_TYPE)).intValue());
                    } else {
                        HwLog.error(TAG, "getTrustedDeviceList sortType invalid");
                    }
                }
                if (map.containsKey(DeviceManager.SIFT_FILTER)) {
                    if (map.get(DeviceManager.SIFT_FILTER) instanceof String) {
                        bundle.putString(DeviceManager.SIFT_FILTER, (String) map.get(DeviceManager.SIFT_FILTER));
                    } else {
                        HwLog.error(TAG, "getTrustedDeviceList siftFilter invalid");
                    }
                }
            }
            try {
                return sDmAccessInterface.getTrustedDeviceList(str, bundle);
            } catch (RemoteException e) {
                HwLog.error(TAG, "getTrustedDeviceList ERROR:" + e.getLocalizedMessage());
                return new ArrayList();
            }
        }
    }

    public int selectDeviceList(String str, Map<String, Object> map, ISelectDeviceListListener iSelectDeviceListListener) {
        synchronized (DM_LOCK) {
            HwLog.info(TAG, "selectDeviceList");
            if (sDmAccessInterface == null) {
                HwLog.error(TAG, "selectDeviceList but sDmAccessInterface is null");
                return 1006;
            } else if (iSelectDeviceListListener == null) {
                HwLog.error(TAG, "selectDeviceList but listener is null");
                return 1001;
            } else {
                Bundle bundle = new Bundle();
                if (map != null) {
                    if (map.containsKey(DeviceManager.TARGET_PACKAGE_NAME)) {
                        if (map.get(DeviceManager.TARGET_PACKAGE_NAME) instanceof String) {
                            bundle.putString(DeviceManager.TARGET_PACKAGE_NAME, (String) map.get(DeviceManager.TARGET_PACKAGE_NAME));
                        } else {
                            HwLog.error(TAG, "selectDeviceList targetPkgName invalid");
                        }
                    }
                    if (map.containsKey(DeviceManager.SORT_TYPE)) {
                        if (map.get(DeviceManager.SORT_TYPE) instanceof Integer) {
                            bundle.putInt(DeviceManager.SORT_TYPE, ((Integer) map.get(DeviceManager.SORT_TYPE)).intValue());
                        } else {
                            HwLog.error(TAG, "selectDeviceList sortType invalid");
                        }
                    }
                    if (map.containsKey(DeviceManager.SIFT_FILTER)) {
                        if (map.get(DeviceManager.SIFT_FILTER) instanceof String) {
                            bundle.putString(DeviceManager.SIFT_FILTER, (String) map.get(DeviceManager.SIFT_FILTER));
                        } else {
                            HwLog.error(TAG, "selectDeviceList siftFilter invalid");
                        }
                    }
                }
                bundle.putString("appName", "HarmonyOS service");
                bundle.putString("appDescription", "");
                try {
                    sDmAccessInterface.selectDeviceList(str, bundle, iSelectDeviceListListener);
                    return 0;
                } catch (RemoteException e) {
                    HwLog.error(TAG, "selectDeviceList ERROR:" + e.getLocalizedMessage());
                    return 1005;
                }
            }
        }
    }

    public DeviceBasicInfo getLocalDeviceInfo() {
        synchronized (DM_LOCK) {
            HwLog.info(TAG, "getLocalDeviceInfo");
            if (sDmAccessInterface == null) {
                HwLog.error(TAG, "getLocalDeviceInfo but sDmAccessInterface is null");
                return null;
            }
            try {
                return sDmAccessInterface.getLocalDeviceInfo();
            } catch (RemoteException e) {
                HwLog.error(TAG, "getLocalDeviceInfo ERROR:" + e.getLocalizedMessage());
                return null;
            }
        }
    }

    public int registerDeviceStateCallback(String str, Map<String, Object> map, IDeviceStateCallback iDeviceStateCallback) {
        synchronized (DM_LOCK) {
            HwLog.info(TAG, "registerDeviceStateCallback");
            if (sDmAccessInterface == null) {
                HwLog.error(TAG, "registerDeviceStateCallback but sDmAccessInterface is null");
                return 1006;
            } else if (iDeviceStateCallback == null) {
                HwLog.error(TAG, "registerDeviceStateCallback but callback is null");
                return 1001;
            } else {
                Bundle bundle = new Bundle();
                if (map != null) {
                    if (map.containsKey(DeviceManager.TARGET_PACKAGE_NAME)) {
                        if (map.get(DeviceManager.TARGET_PACKAGE_NAME) instanceof String) {
                            bundle.putString(DeviceManager.TARGET_PACKAGE_NAME, (String) map.get(DeviceManager.TARGET_PACKAGE_NAME));
                        } else {
                            HwLog.error(TAG, "registerDeviceStateCallback targetPkgName invalid");
                        }
                    }
                    if (map.containsKey(DeviceManager.SIFT_FILTER)) {
                        if (map.get(DeviceManager.SIFT_FILTER) instanceof String) {
                            bundle.putString(DeviceManager.SIFT_FILTER, (String) map.get(DeviceManager.SIFT_FILTER));
                        } else {
                            HwLog.error(TAG, "registerDeviceStateCallback siftFilter invalid");
                        }
                    }
                }
                try {
                    return sDmAccessInterface.registerDeviceStateCallback(str, bundle, iDeviceStateCallback);
                } catch (RemoteException e) {
                    HwLog.error(TAG, "registerDeviceStateCallback ERROR:" + e.getLocalizedMessage());
                    return 1005;
                }
            }
        }
    }

    public int unregisterDeviceStateCallback(String str, IDeviceStateCallback iDeviceStateCallback) {
        synchronized (DM_LOCK) {
            HwLog.info(TAG, "unregisterDeviceStateCallback");
            if (sDmAccessInterface == null) {
                HwLog.error(TAG, "unregisterDeviceStateCallback but sDmAccessInterface is null");
                return 1006;
            } else if (iDeviceStateCallback == null) {
                HwLog.error(TAG, "unregisterDeviceStateCallback but callback is null");
                return 1001;
            } else {
                try {
                    return sDmAccessInterface.unregisterDeviceStateCallback(str, iDeviceStateCallback);
                } catch (RemoteException e) {
                    HwLog.error(TAG, "unregisterDeviceStateCallback ERROR:" + e.getLocalizedMessage());
                    return 1005;
                }
            }
        }
    }
}
