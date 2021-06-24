package ohos.bluetooth.ble;

import com.huawei.ace.plugin.ErrorCode;
import com.huawei.ace.plugin.EventGroup;
import com.huawei.ace.plugin.EventNotifier;
import com.huawei.ace.plugin.Function;
import com.huawei.ace.plugin.ModuleGroup;
import com.huawei.ace.plugin.RequestCodeMapping;
import com.huawei.ace.plugin.Result;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ohos.ace.ability.AceAbility;
import ohos.app.Context;
import ohos.bluetooth.BluetoothHost;
import ohos.bluetooth.LogHelper;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.fastjson.JSONObject;

public class BleCentralManagerPlugin implements ModuleGroup.ModuleGroupHandler, EventGroup.EventGroupHandler, ErrorCode {
    private static int BLUETOOTH_ERROR_CODE = 100;
    private static final int DEFAULT_DUTYRATIO = 2;
    private static final int DEFAULT_INTERVAL = 0;
    private static final int DEFAULT_MATCHINGMODE = 1;
    private static final String DISCOVER_BLUETOOTH_PERMISSION = "ohos.permission.DISCOVER_BLUETOOTH";
    private static final String LOCATION_PERMISSION = "ohos.permission.LOCATION";
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "BleCentralManagerPlugin");
    private static final int TIME_UNIT = 1000000;
    private static BleCentralManagerPlugin bleCentralManagerPlugin;
    private static BluetoothHost bluetoothHost;
    private BleCentralManager bleCentralManager;
    private BleScanCallback bleScanCallback = new BleScanCallback();
    private Context context;
    private EventNotifier eventNotifier = null;
    private Result eventResult = null;
    private int interval = 0;
    private boolean isSubscribe = false;
    private Result moduleResult = null;

    public static String getJsCode() {
        return "var catching = global.systemplugin.catching;var bluetooth = {    moduleGroup: null,    eventGroup: null,    isSubscribe: false,    startBLEScan: async function startBLEScan(param) {        if (typeof param.interval !== 'undefined' &&            Math.floor(param.interval) !== param.interval) {            commonCallback(param.fail, 'fail', 'interval is not available', 202);            commonCallback(param.complete, 'complete');            return;        }        if (!bluetooth.isSubscribe) {            commonCallback(param.fail, 'fail', 'no Subscribe', 202);            return;        }        if (bluetooth.moduleGroup == null) {            bluetooth.moduleGroup = ModuleGroup.getGroup(\"AceModuleGroup/BleCentralManager\");        }        return await catching(bluetooth.moduleGroup.callNative(\"startBLEScan\", param.interval),                   param);    },    stopBLEScan: async function stopBLEScan(param) {        if (bluetooth.moduleGroup == null) {            bluetooth.moduleGroup = ModuleGroup.getGroup(\"AceModuleGroup/BleCentralManager\");        }        return await catching(bluetooth.moduleGroup.callNative(\"stopBLEScan\"),                   param);    },    subscribeBLEFound: async function subscribeBLEFound(param) {        if (bluetooth.eventGroup == null) {            bluetooth.eventGroup = EventGroup.getGroup(\"AceEventGroup/BleCentralManager\");        }        if (bluetooth.isSubscribe) {            await bluetooth.eventGroup.unsubscribe();        }        try {            var ret = await bluetooth.eventGroup.subscribe(function (callback) {                var val = JSON.parse(callback);                commonCallback(param.success, 'success', val.data);            });        } catch (pluginError) {            var pluginErrorJson = JSON.parse(pluginError);            commonCallback(param.fail, 'fail', pluginErrorJson.data, pluginErrorJson.code);        }        var val = JSON.parse(ret);        bluetooth.isSubscribe = true;    },    unsubscribeBLEFound: async function unsubscribeBLEFound() {        if (bluetooth.eventGroup == null) {            return;        }        if (bluetooth.isSubscribe) {            await bluetooth.eventGroup.unsubscribe();            bluetooth.isSubscribe = false;        }    }};global.systemplugin.bluetooth = {    startBLEScan: bluetooth.startBLEScan,    stopBLEScan: bluetooth.stopBLEScan,    subscribeBLEFound: bluetooth.subscribeBLEFound,    unsubscribeBLEFound: bluetooth.unsubscribeBLEFound};";
    }

    /* access modifiers changed from: private */
    public class BleScanCallback implements BleCentralManagerCallback {
        @Override // ohos.bluetooth.ble.BleCentralManagerCallback
        public void groupScanResultsEvent(List<BleScanResult> list) {
        }

        @Override // ohos.bluetooth.ble.BleCentralManagerCallback
        public void scanFailedEvent(int i) {
        }

        public BleScanCallback() {
        }

        @Override // ohos.bluetooth.ble.BleCentralManagerCallback
        public void scanResultEvent(BleScanResult bleScanResult) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("addrType", (Object) "1");
            jSONObject.put("addr", (Object) bleScanResult.getPeripheralDevice().getDeviceAddr());
            jSONObject.put("rssi", (Object) Integer.valueOf(bleScanResult.getRssi()));
            jSONObject.put("txpower", (Object) "0");
            jSONObject.put("data", (Object) bleScanResult.getRawData());
            if (BleCentralManagerPlugin.this.isSubscribe) {
                BleCentralManagerPlugin.this.eventNotifier.success(jSONObject);
            }
        }
    }

    public static Set<String> getPluginGroup() {
        HashSet hashSet = new HashSet();
        hashSet.add("AceModuleGroup/BleCentralManager");
        hashSet.add("AceEventGroup/BleCentralManager");
        return hashSet;
    }

    public static void register(Context context2) {
        HiLog.info(TAG, "register", new Object[0]);
        bleCentralManagerPlugin = new BleCentralManagerPlugin();
        bleCentralManagerPlugin.onRegister(context2);
        bluetoothHost = BluetoothHost.getDefaultHost(context2);
        Integer valueOf = context2 instanceof AceAbility ? Integer.valueOf(((AceAbility) context2).getAbilityId()) : null;
        ModuleGroup.registerModuleGroup("AceModuleGroup/BleCentralManager", bleCentralManagerPlugin, valueOf);
        EventGroup.registerEventGroup("AceEventGroup/BleCentralManager", bleCentralManagerPlugin, valueOf);
    }

    public static void deregister(Context context2) {
        Integer valueOf = context2 instanceof AceAbility ? Integer.valueOf(((AceAbility) context2).getAbilityId()) : null;
        ModuleGroup.registerModuleGroup("AceModuleGroup/BleCentralManager", null, valueOf);
        EventGroup.registerEventGroup("AceEventGroup/BleCentralManager", null, valueOf);
    }

    private void onRegister(Context context2) {
        HiLog.info(TAG, "onRegister", new Object[0]);
        this.context = context2;
        this.bleCentralManager = new BleCentralManager(context2, this.bleScanCallback);
    }

    private boolean checkPermission(Result result) {
        if (this.context.verifySelfPermission("ohos.permission.LOCATION") == 0 && this.context.verifySelfPermission("ohos.permission.DISCOVER_BLUETOOTH") == 0) {
            return true;
        }
        HiLog.info(TAG, "permission has got", new Object[0]);
        if (this.context.canRequestPermission("ohos.permission.LOCATION") || this.context.canRequestPermission("ohos.permission.DISCOVER_BLUETOOTH")) {
            this.context.requestPermissionsFromUser(new String[]{"ohos.permission.LOCATION"}, RequestCodeMapping.BLUETOOTHSTART.getCode());
            return true;
        }
        result.error(BLUETOOTH_ERROR_CODE, "user rejects the perssion request and forbids to again");
        return true;
    }

    public static void permissionResult(int[] iArr) {
        if (iArr.length > 0 && iArr[0] == 0 && iArr[1] == 0) {
            HiLog.info(TAG, "permission approved", new Object[0]);
            BleCentralManagerPlugin bleCentralManagerPlugin2 = bleCentralManagerPlugin;
            if (bleCentralManagerPlugin2.moduleResult != null) {
                bleCentralManagerPlugin2.startBLEScan();
            } else {
                bleCentralManagerPlugin2.subscribeBLEFound();
            }
        } else {
            HiLog.info(TAG, "permission denied", new Object[0]);
            BleCentralManagerPlugin bleCentralManagerPlugin3 = bleCentralManagerPlugin;
            Result result = bleCentralManagerPlugin3.moduleResult;
            if (result != null) {
                result.error(BLUETOOTH_ERROR_CODE, "user rejects the perssion request");
            } else {
                bleCentralManagerPlugin3.eventResult.error(BLUETOOTH_ERROR_CODE, "user rejects the perssion request");
            }
        }
    }

    @Override // com.huawei.ace.plugin.ModuleGroup.ModuleGroupHandler
    public void onFunctionCall(Function function, Result result) {
        HiLog.info(TAG, "into onFunctionCall", new Object[0]);
        if (function.arguments.size() <= 0) {
            this.interval = 0;
        } else if (function.arguments.get(0) instanceof Integer) {
            this.interval = ((Integer) function.arguments.get(0)).intValue();
            if (this.interval <= 0) {
                this.interval = 0;
            }
        } else {
            result.error(202, "interval is not an available integer");
            return;
        }
        HiLog.info(TAG, "interval = %{public}d  ", Integer.valueOf(this.interval));
        if ("startBLEScan".equals(function.name)) {
            if (checkPermission(result)) {
                this.moduleResult = result;
                startBLEScan();
            }
        } else if ("stopBLEScan".equals(function.name)) {
            stopBLEScan(result);
        }
    }

    @Override // com.huawei.ace.plugin.EventGroup.EventGroupHandler
    public void onSubscribe(List<Object> list, EventNotifier eventNotifier2, Result result) {
        HiLog.info(TAG, "into onSubscribe", new Object[0]);
        this.eventResult = result;
        this.eventNotifier = eventNotifier2;
        if (checkPermission(result)) {
            subscribeBLEFound();
        }
    }

    @Override // com.huawei.ace.plugin.EventGroup.EventGroupHandler
    public void onUnsubscribe(List<Object> list, Result result) {
        HiLog.info(TAG, "into onUnsubscribe", new Object[0]);
        this.isSubscribe = false;
        result.success("unsubscribe success");
    }

    private void startBLEScan() {
        HiLog.info(TAG, "startBLEScan", new Object[0]);
        if (bluetoothHost.getBtState() == 2) {
            HiLog.info(TAG, "STATE_ON, scan interval = %{public}d ", Integer.valueOf(this.interval * 1000000));
            this.bleCentralManager.startScan(new ArrayList(), 2, 1, (long) (this.interval * 1000000));
            this.moduleResult.success("start scan success");
            return;
        }
        HiLog.info(TAG, "BluetoothHost STATE_OFF", new Object[0]);
        this.moduleResult.error(BLUETOOTH_ERROR_CODE, "bluetooth switch is close");
    }

    private void stopBLEScan(Result result) {
        HiLog.info(TAG, "stopBLEScan", new Object[0]);
        this.bleCentralManager.stopScan();
        result.success("stop scan success");
    }

    private void subscribeBLEFound() {
        HiLog.info(TAG, "into subscribeBLEFound", new Object[0]);
        if (bluetoothHost.getBtState() == 2) {
            this.isSubscribe = true;
            this.eventResult.success("subscribe success");
            return;
        }
        HiLog.info(TAG, "BluetoothHost STATE_OFF", new Object[0]);
        this.eventResult.error(BLUETOOTH_ERROR_CODE, "bluetooth switch is close");
    }
}
