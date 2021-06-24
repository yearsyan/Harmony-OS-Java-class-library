package ohos.usb;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.sysability.samgr.SysAbilityManager;

public class USBCore {
    public static final String ACTION_USB_DEVICE_ATTACHED = "ohos.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DEVICE_DETACHED = "ohos.usb.action.USB_DEVICE_DETACHED";
    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_PERMISSION_GRANTED = "permission";
    private static final Map<String, Long> FUNCTION_NAME_TO_CODE;
    public static final long FUNC_AUDIO_SOURCE = 64;
    public static final long FUNC_HDB = 4096;
    public static final long FUNC_HDC = 1;
    public static final long FUNC_HISI_DEBUG = 32768;
    public static final long FUNC_HISUITE = 2048;
    public static final long FUNC_MANUFACTURE = 16384;
    public static final long FUNC_MASS_STORAGE = 8192;
    public static final long FUNC_MIDI = 8;
    public static final long FUNC_MTP = 4;
    public static final long FUNC_NCM = 1048576;
    public static final long FUNC_NONE = 0;
    public static final long FUNC_PTP = 16;
    public static final long FUNC_RNDIS = 32;
    public static final long FUNC_SERIAL = 65536;
    private static final String HILOG_TAG = "USBCore";
    private static final HiLogLabel LABEL = USBLoggerFactory.getHilogLabel(HILOG_TAG);
    public static final String NCM_REQUESTED = "ncm_requested";
    private static final int REGISTER_SYSCAP_SUCCESS = 0;
    private static final long SETTABLE_FUNCTIONS = 1177660;
    private static final String USB_CORE_JNI_NAME = "usb_jni.z";
    public static final String USB_FUNC_AUDIO_SOURCE = "audio_source";
    public static final String USB_FUNC_HDB = "hdb";
    public static final String USB_FUNC_HDC = "hdc";
    public static final String USB_FUNC_HISI_DEBUG = "hisi_debug";
    public static final String USB_FUNC_HISUITE = "hisuite";
    public static final String USB_FUNC_MANUFACTURE = "manufacture";
    public static final String USB_FUNC_MASS_STORAGE = "mass_storage";
    public static final String USB_FUNC_MIDI = "midi";
    public static final String USB_FUNC_MTP = "mtp";
    public static final String USB_FUNC_NCM = "ncm";
    public static final String USB_FUNC_NONE = "none";
    public static final String USB_FUNC_PTP = "ptp";
    public static final String USB_FUNC_RNDIS = "rndis";
    public static final String USB_FUNC_SERIAL = "serial";
    private static final String USB_MANAGER_SYSCAP = "SystemCapability.Usb.UsbManager";
    private Context context;
    private USBCoreAdapter coreAdapter;

    static {
        HashMap hashMap = new HashMap();
        hashMap.put(USB_FUNC_MTP, 4L);
        hashMap.put(USB_FUNC_PTP, 16L);
        hashMap.put(USB_FUNC_RNDIS, 32L);
        hashMap.put(USB_FUNC_MIDI, 8L);
        hashMap.put(USB_FUNC_AUDIO_SOURCE, 64L);
        hashMap.put(USB_FUNC_HDC, 1L);
        hashMap.put(USB_FUNC_NCM, Long.valueOf((long) FUNC_NCM));
        hashMap.put(USB_FUNC_HISUITE, 2048L);
        hashMap.put(USB_FUNC_HDB, 4096L);
        hashMap.put(USB_FUNC_MASS_STORAGE, 8192L);
        hashMap.put(USB_FUNC_MANUFACTURE, 16384L);
        hashMap.put(USB_FUNC_HISI_DEBUG, 32768L);
        hashMap.put(USB_FUNC_SERIAL, 65536L);
        FUNCTION_NAME_TO_CODE = Collections.unmodifiableMap(hashMap);
        try {
            System.loadLibrary(USB_CORE_JNI_NAME);
        } catch (UnsatisfiedLinkError unused) {
            HiLog.error(LABEL, "load usb_jni.z.so got UnsatisfiedLinkError error!", new Object[0]);
        }
    }

    public USBCore(Context context2) {
        this.context = context2;
        this.coreAdapter = new USBCoreAdapter(context2);
        int addSystemCapability = SysAbilityManager.addSystemCapability("SystemCapability.Usb.UsbManager");
        if (addSystemCapability != 0) {
            HiLog.error(LABEL, "USBCore: call addSystemCapability for %{public}s failed, ret = %{public}d.", "SystemCapability.Usb.UsbManager", Integer.valueOf(addSystemCapability));
        }
    }

    public HashMap<String, USBDevice> getDevices() {
        HashMap<String, USBDevice> hashMap = new HashMap<>();
        HiLog.info(LABEL, "calling getDevices begin", new Object[0]);
        this.coreAdapter.getDevices(hashMap);
        return hashMap;
    }

    public USBDevicePipe connectDevice(USBDevice uSBDevice) {
        return this.coreAdapter.connectDevice(uSBDevice);
    }

    public boolean hasRight(USBDevice uSBDevice) {
        return this.coreAdapter.hasRight(uSBDevice);
    }

    public void requestRight(USBDevice uSBDevice, String str) {
        this.coreAdapter.requestRight(uSBDevice, str);
    }

    public static String usbFunctionsToString(long j) {
        if (j == 0) {
            return USB_FUNC_NONE;
        }
        StringJoiner stringJoiner = new StringJoiner(",");
        if ((2048 & j) != 0) {
            stringJoiner.add(USB_FUNC_HISUITE);
        }
        if ((4 & j) != 0) {
            stringJoiner.add(USB_FUNC_MTP);
        }
        if ((16 & j) != 0) {
            stringJoiner.add(USB_FUNC_PTP);
        }
        if ((32 & j) != 0) {
            stringJoiner.add(USB_FUNC_RNDIS);
        }
        if ((8 & j) != 0) {
            stringJoiner.add(USB_FUNC_MIDI);
        }
        if ((64 & j) != 0) {
            stringJoiner.add(USB_FUNC_AUDIO_SOURCE);
        }
        if ((8192 & j) != 0) {
            stringJoiner.add(USB_FUNC_MASS_STORAGE);
        }
        if ((32768 & j) != 0) {
            stringJoiner.add(USB_FUNC_HISI_DEBUG);
        }
        if ((16384 & j) != 0) {
            stringJoiner.add(USB_FUNC_MANUFACTURE);
        }
        if ((65536 & j) != 0) {
            stringJoiner.add(USB_FUNC_SERIAL);
        }
        if ((FUNC_NCM & j) != 0) {
            stringJoiner.add(USB_FUNC_NCM);
        }
        if ((1 & j) != 0) {
            stringJoiner.add(USB_FUNC_HDC);
        }
        if ((j & 4096) != 0) {
            stringJoiner.add(USB_FUNC_HDB);
        }
        return stringJoiner.toString();
    }

    public static long usbFunctionsFromString(String str) {
        if (str == null || str.equals(USB_FUNC_NONE)) {
            return 0;
        }
        String[] split = str.split(",");
        long j = 0;
        for (String str2 : split) {
            if (FUNCTION_NAME_TO_CODE.containsKey(str2)) {
                j |= FUNCTION_NAME_TO_CODE.get(str2).longValue();
            } else {
                HiLog.error(LABEL, "Invalid usb function", new Object[0]);
            }
        }
        return j;
    }
}
