package ohos.security.permission;

import android.content.Context;
import com.huawei.security.dpermission.DistributedPermissionManager;
import com.huawei.security.dpermission.IPermissionRecordQueryCallback;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IPCSkeleton;
import ohos.rpc.RemoteException;
import ohos.security.SystemPermission;
import ohos.security.permission.adapter.ActivityManagerAdapter;
import ohos.security.permission.adapter.AppOpsManagerAdapter;
import ohos.security.permission.adapter.DistributedPermissionManagerAdapter;
import ohos.security.permission.adapter.PackageManagerAdapter;
import ohos.security.permission.infrastructure.model.Result;
import ohos.security.permission.infrastructure.utils.DataValidUtil;
import ohos.security.permission.infrastructure.utils.DeviceIdUtil;
import ohos.security.permission.infrastructure.utils.GzipUtil;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;
import ohos.security.permission.infrastructure.utils.JsonUtil;
import ohos.utils.StringUtils;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.TypeReference;
import ohos.utils.zson.ZSONException;
import ohos.utils.zson.ZSONObject;

public class PermissionInner {
    private static final int DEFAULT_SIZE = 14;
    public static final int DENIED = -1;
    static final int DUSER_ID_ANDROID = 126;
    private static final int DUSER_ID_HARMONY = 125;
    private static final int FAILTURE_CODE = -1;
    public static final int FAILURE = -1;
    public static final int GRANTED = 0;
    public static final int INVALID_DEVICEID = -3;
    public static final int INVALID_RUID = -4;
    private static final HiLogLabel LABEL = HiLogLabelUtil.INNER_KIT.newHiLogLabel(TAG);
    private static final String MANAGE_APP_POLICY = "ohos.permission.MANAGE_APP_POLICY";
    public static final int MAX_DEVICE_ID_LENGTH = 64;
    private static final int PER_USER_RANGE = 100000;
    private static final Set<String> RESTRICTED_PERMISSION_SET = new HashSet(14);
    public static final int SUCCESS = 0;
    private static final String TAG = "PermissionInner";

    static {
        RESTRICTED_PERMISSION_SET.add(SystemPermission.ANSWER_CALL);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.READ_CALL_LOG);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.WRITE_CALL_LOG);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.READ_CELL_MESSAGES);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.READ_MESSAGES);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.RECEIVE_MMS);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.RECEIVE_SMS);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.RECEIVE_WAP_MESSAGES);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.SEND_MESSAGES);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.READ_CONTACTS);
        RESTRICTED_PERMISSION_SET.add(SystemPermission.WRITE_CONTACTS);
    }

    private PermissionInner() {
    }

    public static boolean isRestrictedPermission(String str) {
        return RESTRICTED_PERMISSION_SET.contains(PermissionConversion.getZosPermissionNameIfPossible(str));
    }

    public static int registerUsingPermissionReminder(OnUsingPermissionReminder onUsingPermissionReminder) {
        if (onUsingPermissionReminder == null) {
            return -1;
        }
        return PermissionReminderInner.registerUsingPermissionReminder(onUsingPermissionReminder);
    }

    public static int unregisterUsingPermissionReminder(OnUsingPermissionReminder onUsingPermissionReminder) {
        if (onUsingPermissionReminder == null) {
            return -1;
        }
        PermissionReminderInner.unregisterUsingPermissionReminder(onUsingPermissionReminder);
        return 0;
    }

    public static void addPermissionUsedRecord(String str, String str2, int i, int i2) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "invalid param for permName: %{public}s or appIdInfo: %{public}s", str, str2);
            return;
        }
        AppIdInfoHelper createAppIdInfo = AppIdInfoHelper.createAppIdInfo(str2);
        DistributedPermissionManager.getDefault().addPermissionRecord(PermissionConversion.getAosPermissionNameIfPossible(str), createAppIdInfo.getAppDeviceId(), createAppIdInfo.getAppUid(), i, i2);
    }

    public static int getPermissionUsedRecords(QueryPermissionUsedRequest queryPermissionUsedRequest, QueryPermissionUsedResult queryPermissionUsedResult) {
        if (Objects.isNull(queryPermissionUsedRequest) || Objects.isNull(queryPermissionUsedResult)) {
            HiLog.error(LABEL, "invalid param for request: %{public}s or result: %{public}s", queryPermissionUsedRequest, queryPermissionUsedResult);
            return -1;
        }
        Optional fromJson = JsonUtil.fromJson(new String(GzipUtil.uncompress(Base64.getDecoder().decode(DistributedPermissionManager.getDefault().getPermissionRecord(Base64.getEncoder().encodeToString(GzipUtil.compress(JsonUtil.toJson(queryPermissionUsedRequest).getBytes(StandardCharsets.UTF_8)))).getBytes(StandardCharsets.UTF_8)))), new TypeReference<Result<QueryPermissionUsedResult>>() {
            /* class ohos.security.permission.PermissionInner.AnonymousClass1 */
        });
        if (!fromJson.isPresent() || ((Result) fromJson.get()).getData() == null) {
            queryPermissionUsedResult.setCode(-1);
            return -1;
        }
        Result result = (Result) fromJson.get();
        queryPermissionUsedResult.setCode(((QueryPermissionUsedResult) result.getData()).getCode());
        queryPermissionUsedResult.setBeginTimeMillis(((QueryPermissionUsedResult) result.getData()).getBeginTimeMillis());
        queryPermissionUsedResult.setEndTimeMillis(((QueryPermissionUsedResult) result.getData()).getEndTimeMillis());
        queryPermissionUsedResult.setBundlePermissionUsedRecords(((QueryPermissionUsedResult) result.getData()).getBundlePermissionUsedRecords());
        return 0;
    }

    public static int getPermissionUsedRecords(QueryPermissionUsedRequest queryPermissionUsedRequest, final OnPermissionUsedRecord onPermissionUsedRecord) {
        if (Objects.isNull(queryPermissionUsedRequest) || Objects.isNull(onPermissionUsedRecord)) {
            HiLog.error(LABEL, "invalid param for request: %{public}s or callback: %{public}s", queryPermissionUsedRequest, onPermissionUsedRecord);
            return -1;
        }
        DistributedPermissionManager.getDefault().getPermissionRecordAsync(Base64.getEncoder().encodeToString(GzipUtil.compress(JsonUtil.toJson(queryPermissionUsedRequest).getBytes(StandardCharsets.UTF_8))), new IPermissionRecordQueryCallback.Stub() {
            /* class ohos.security.permission.PermissionInner.AnonymousClass2 */

            public void call(String str) {
                QueryPermissionUsedResult queryPermissionUsedResult = new QueryPermissionUsedResult();
                queryPermissionUsedResult.setCode(-1);
                try {
                    if (StringUtils.isEmpty(str)) {
                        OnPermissionUsedRecord.this.onQueried(-1, queryPermissionUsedResult);
                        return;
                    }
                    Optional fromJson = JsonUtil.fromJson(new String(GzipUtil.uncompress(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)))), new TypeReference<Result<QueryPermissionUsedResult>>() {
                        /* class ohos.security.permission.PermissionInner.AnonymousClass2.AnonymousClass1 */
                    });
                    if (!fromJson.isPresent() || ((Result) fromJson.get()).getData() == null) {
                        OnPermissionUsedRecord.this.onQueried(-1, queryPermissionUsedResult);
                        return;
                    }
                    Result result = (Result) fromJson.get();
                    OnPermissionUsedRecord.this.onQueried(((QueryPermissionUsedResult) result.getData()).getCode(), (QueryPermissionUsedResult) result.getData());
                } catch (RemoteException unused) {
                    HiLog.error(PermissionInner.LABEL, "Failed to notify query result because of RemoteException", new Object[0]);
                }
            }
        });
        return 0;
    }

    public static int checkCallerPermissionAndStartUsing(String str) {
        return checkPermissionAndStartUsing(str, AppIdInfoHelper.createAppIdInfo(IPCSkeleton.getCallingPid(), IPCSkeleton.getCallingUid(), IPCSkeleton.getCallingDeviceID()));
    }

    public static int checkPermissionAndStartUsing(String str, String str2) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "checkPermissionAndStartUsing::parameters(permName or appIdInfo) is illegal.", new Object[0]);
            return -1;
        }
        AppIdInfoHelper createAppIdInfo = AppIdInfoHelper.createAppIdInfo(str2);
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (createAppIdInfo.getAppUid() / 100000 != 126 && !isSelfDevice(createAppIdInfo.getAppDeviceId())) {
            return DistributedPermissionManager.getDefault().checkDPermissionAndStartUsing(aosPermissionNameIfPossible, str2);
        }
        int checkPermission = ActivityManagerAdapter.getInstance().checkPermission(aosPermissionNameIfPossible, createAppIdInfo.getAppPid(), createAppIdInfo.getAppUid());
        if (checkPermission == 0) {
            DistributedPermissionManager.getDefault().startUsingPermission(aosPermissionNameIfPossible, str2);
        } else {
            DistributedPermissionManager.getDefault().addPermissionRecord(aosPermissionNameIfPossible, createAppIdInfo.appDeviceId, createAppIdInfo.appUid, 0, 1);
        }
        return checkPermission;
    }

    private static boolean isSelfDevice(String str) {
        return StringUtils.isEmpty(str) || str.equals(DeviceIdUtil.getLocalNodeId());
    }

    public static void startUsingPermission(String str, String str2) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "startUsingPermission::parameters(permName or appIdInfo) is illegal.", new Object[0]);
            return;
        }
        DistributedPermissionManager.getDefault().startUsingPermission(PermissionConversion.getAosPermissionNameIfPossible(str), str2);
    }

    public static void stopUsingPermission(String str, String str2) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "stopUsingPermission::parameters(permName or appIdInfo) is illegal.", new Object[0]);
            return;
        }
        DistributedPermissionManager.getDefault().stopUsingPermission(PermissionConversion.getAosPermissionNameIfPossible(str), str2);
    }

    public static void postPermissionEvent(String str) {
        if (StringUtils.isEmpty(str)) {
            HiLog.error(LABEL, "PostPermissionEvent::event is illegal.", new Object[0]);
        } else {
            DistributedPermissionManager.getDefault().postPermissionEvent(str);
        }
    }

    public static int checkCallerPermissionAndUse(String str) {
        if (!StringUtils.isEmpty(str)) {
            return checkPermissionAndUse(str, AppIdInfoHelper.createAppIdInfo(IPCSkeleton.getCallingPid(), IPCSkeleton.getCallingUid(), IPCSkeleton.getCallingDeviceID()));
        }
        HiLog.error(LABEL, "checkCallerPermissionAndUse::permissionName is null.", new Object[0]);
        return -1;
    }

    public static int checkPermissionAndUse(String str, String str2) {
        if (str == null || str2 == null) {
            HiLog.error(LABEL, "checkPermissionAndUse::parameters(permName or appIdInfo) is illegal.", new Object[0]);
            return -1;
        }
        AppIdInfoHelper createAppIdInfo = AppIdInfoHelper.createAppIdInfo(str2);
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (createAppIdInfo.getAppUid() / 100000 != 126 && !isSelfDevice(createAppIdInfo.getAppDeviceId())) {
            return DistributedPermissionManager.getDefault().checkDPermissionAndUse(aosPermissionNameIfPossible, str2);
        }
        int checkPermission = ActivityManagerAdapter.getInstance().checkPermission(aosPermissionNameIfPossible, createAppIdInfo.getAppPid(), createAppIdInfo.getAppUid());
        int i = checkPermission == 0 ? 1 : 0;
        DistributedPermissionManager.getDefault().addPermissionRecord(aosPermissionNameIfPossible, createAppIdInfo.getAppDeviceId(), createAppIdInfo.getAppUid(), i, 1 - i);
        return checkPermission;
    }

    public static int checkSelfPermission(String str) {
        if (!StringUtils.isEmpty(str)) {
            return checkPermission(str, "", IPCSkeleton.getCallingPid(), IPCSkeleton.getCallingUid());
        }
        HiLog.error(LABEL, "checkSelfPermission::permissionName is null.", new Object[0]);
        return -1;
    }

    private static int checkPermission(String str, String str2, int i, int i2) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (i2 / 100000 == 126 || isSelfDevice(str2)) {
            return ActivityManagerAdapter.getInstance().checkPermission(aosPermissionNameIfPossible, i, i2);
        }
        return DistributedPermissionManager.getDefault().checkPermission(aosPermissionNameIfPossible, str2, i, i2);
    }

    public static void grantSensitivePermission(Object obj, String str, String str2, int i) {
        PackageManagerAdapter.getInstance().grantRuntimePermission(obj, str2, getAosPermissionName(str), i);
    }

    private static String getAosPermissionName(String str) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        HiLog.debug(LABEL, "permissionName: %{public}s", aosPermissionNameIfPossible);
        return aosPermissionNameIfPossible;
    }

    public static void revokeSensitivePermission(Object obj, String str, String str2, int i) {
        PackageManagerAdapter.getInstance().revokeRuntimePermission(obj, str2, getAosPermissionName(str), i);
    }

    public static int getPermissionStatus(Object obj, String str, String str2, int i) {
        return PackageManagerAdapter.getInstance().getPermissionFlags(obj, getAosPermissionName(str), str2, i);
    }

    public static void updatePermissionStatus(Object obj, String str, String str2, int i, int i2, int i3) {
        PackageManagerAdapter.getInstance().updatePermissionFlags(obj, PackageManagerAdapter.UpdatePermissionStatusRequest.newBuilder().setPermissionName(getAosPermissionName(str)).setPackageName(str2).setFlagsMask(i).setFlagValues(i2).setUserId(i3).build());
    }

    public static boolean isPermissionDiscarded(PermissionDef permissionDef) {
        return (permissionDef.permissionFlags & 1) != 0;
    }

    public static void setPermissionState(Object obj, String str, int i, int i2) {
        String aosPermissionName = getAosPermissionName(str);
        String permissionToOp = AppOpsManagerAdapter.permissionToOp(aosPermissionName);
        if (StringUtils.isEmpty(permissionToOp)) {
            HiLog.error(LABEL, "setPermissionState: opName is null for permName %{public}s, uid %{public}s, state %{public}s, aosPermissionName %{public}s", str, Integer.valueOf(i), Integer.valueOf(i2), aosPermissionName);
            return;
        }
        AppOpsManagerAdapter.getInstance().setUidMode(obj, permissionToOp, i, i2);
    }

    public static int verifyBundlePermission(String str, String str2) {
        if (checkCallerPermission("ohos.permission.MANAGE_APP_POLICY") == -1) {
            throw new SecurityException("verifyBundle:Permission denied. You need ohos.permission.MANAGE_APP_POLICY permission.");
        } else if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "verifyBundle:invalid param error, permissionName %{public}s, appidInfo %{public}s", str, str2);
            return -1;
        } else {
            AppIdInfoHelper createAppIdInfo = AppIdInfoHelper.createAppIdInfo(str2);
            int appUid = createAppIdInfo.getAppUid();
            String appBundleName = createAppIdInfo.getAppBundleName();
            HiLog.debug(LABEL, "verifyBundle:verify the permission %{public}s for :%{public}d,%{public}s", str, Integer.valueOf(appUid), appBundleName);
            if (appBundleName != null && appUid >= 0) {
                return verifyThePermission(appUid, appBundleName, str);
            }
            HiLog.error(LABEL, "verifyBundle:The appinfo data error :uid %{public}d,bundleName %{public}s.", Integer.valueOf(appUid), appBundleName);
            return -1;
        }
    }

    public static int checkCallerPermission(String str) {
        if (StringUtils.isEmpty(str)) {
            HiLog.error(LABEL, "checkCallerPermission::permissionName is null.", new Object[0]);
            return -1;
        }
        return checkPermission(str, IPCSkeleton.getCallingDeviceID(), IPCSkeleton.getCallingPid(), IPCSkeleton.getCallingUid());
    }

    private static int verifyThePermission(int i, String str, String str2) {
        if (AppOpsManagerAdapter.getInstance().checkPackage(i, str) == 1) {
            HiLog.error(LABEL, "verifyBundle:The UID %{public}d does not match the package name %{public}s.", Integer.valueOf(i), str);
            return -1;
        }
        AppOpsManagerAdapter.getInstance();
        int permissionToOpCode = AppOpsManagerAdapter.permissionToOpCode(getAosPermissionName(str2));
        if (permissionToOpCode == -1) {
            HiLog.error(LABEL, "verifyBundle:dont find the code for %{public}s", str2);
            return -1;
        }
        int noteOperation = AppOpsManagerAdapter.getInstance().noteOperation(permissionToOpCode, i, str);
        HiLog.debug(LABEL, "verifyBundle:check ops result:%{public}d", Integer.valueOf(noteOperation));
        return noteOperation == 0 ? 0 : -1;
    }

    public static int verifyPermissionAndState(Object obj, String str, String str2) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2) || !isValidContext(obj)) {
            HiLog.error(LABEL, "verifyPermissionAndState: invalid param. permissionName: %{public}s", str);
            return -1;
        } else if (isForegroundApp(obj, str2)) {
            return checkPermission(str, str2);
        } else {
            HiLog.error(LABEL, "the state of the app is not foreground", new Object[0]);
            return -1;
        }
    }

    private static boolean isValidContext(Object obj) {
        if (obj == null) {
            HiLog.error(LABEL, "the param should not be null", new Object[0]);
            return false;
        } else if (obj instanceof Context) {
            return true;
        } else {
            HiLog.error(LABEL, "param type error, %s", obj.toString());
            return false;
        }
    }

    private static boolean isForegroundApp(Object obj, String str) {
        int i;
        AppIdInfoHelper createAppIdInfo = AppIdInfoHelper.createAppIdInfo(str);
        int appUid = createAppIdInfo.getAppUid();
        String appDeviceId = createAppIdInfo.getAppDeviceId();
        if (isDuid(appUid)) {
            HiLog.error(LABEL, "only check for uid, dont suppot duid", new Object[0]);
            return false;
        } else if (!isDeviceIdValid(appDeviceId)) {
            HiLog.error(LABEL, "local node id get failed", new Object[0]);
            return false;
        } else {
            String localNodeId = DeviceIdUtil.getLocalNodeId();
            if (!isDeviceIdValid(localNodeId)) {
                HiLog.error(LABEL, "local node id get failed", new Object[0]);
                return false;
            }
            if (!localNodeId.equals(appDeviceId)) {
                i = getRemoteAppRunningState(str);
            } else {
                i = getLocalAppRunningState(obj, appUid);
            }
            HiLog.info(LABEL, "get the App %{public}d Running State %{public}d", Integer.valueOf(appUid), Integer.valueOf(i));
            if (i >= 0 && i <= 125) {
                return true;
            }
            return false;
        }
    }

    public static int checkPermission(String str, String str2) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "checkPermission::parameters(permName or appIdInfo) is illegal.", new Object[0]);
            return -1;
        }
        AppIdInfoHelper createAppIdInfo = AppIdInfoHelper.createAppIdInfo(str2);
        return checkPermission(str, createAppIdInfo.getAppDeviceId(), createAppIdInfo.getAppPid(), createAppIdInfo.getAppUid());
    }

    public static boolean isDuid(int i) {
        return DataValidUtil.isDuid(i);
    }

    public static boolean isDeviceIdValid(String str) {
        return DataValidUtil.isDeviceIdValid(str);
    }

    private static int getRemoteAppRunningState(String str) {
        String processZ2aMessage = DistributedPermissionManager.getDefault().processZ2aMessage("getAppRunningState", str);
        HiLog.debug(LABEL, "hos getAppRunningState result: %{public}s", processZ2aMessage);
        try {
            ZSONObject stringToZSON = ZSONObject.stringToZSON(processZ2aMessage);
            if (stringToZSON == null) {
                HiLog.error(LABEL, "create json object fail", new Object[0]);
                return -1;
            } else if (stringToZSON.getIntValue("code") == 0) {
                return stringToZSON.getInteger("data").intValue();
            } else {
                HiLog.error(LABEL, "hos getAppRunningState error info: %{public}s", stringToZSON.getString("message"));
                return -1;
            }
        } catch (JSONException e) {
            HiLog.error(LABEL, "translation appIdInfo JSONException: %{private}s", e.getMessage());
            return -1;
        } catch (ZSONException e2) {
            HiLog.error(LABEL, "translation appIdInfo ZSONException: %{private}s", e2.getMessage());
            return -1;
        } catch (Exception e3) {
            HiLog.error(LABEL, "translation appIdInfo Exception: %{private}s", e3.getMessage());
            return -1;
        }
    }

    private static int getLocalAppRunningState(Object obj, int i) {
        return ActivityManagerAdapter.getInstance().getUidImportance(obj, i);
    }

    public static int allocateDuid(String str, int i) {
        if (!isDeviceIdValid(str)) {
            return -3;
        }
        if (!isUidValid(i)) {
            return -4;
        }
        return DistributedPermissionManager.getDefault().allocateDuid(str, i);
    }

    public static boolean isUidValid(int i) {
        return DataValidUtil.isUidValid(i);
    }

    public static int queryDuid(String str, int i) {
        if (!isDeviceIdValid(str)) {
            return -3;
        }
        if (!isUidValid(i)) {
            return -4;
        }
        return DistributedPermissionManager.getDefault().queryDuid(str, i);
    }

    public static int checkDPermission(int i, String str) {
        if (StringUtils.isEmpty(str) || !isDuid(i)) {
            HiLog.error(LABEL, "checkDPermission: invalid params!", new Object[0]);
            return -1;
        }
        return DistributedPermissionManagerAdapter.getInstance().checkDPermission(i, PermissionConversion.getAosPermissionNameIfPossible(str));
    }

    public static Optional<BundleLabelInfo> getBundleLabelInfo(int i) {
        if (!isDuid(i)) {
            return Optional.empty();
        }
        return JsonUtil.fromJson(DistributedPermissionManager.getDefault().getBundleLabelInfo(i), BundleLabelInfo.class);
    }

    public static String processZ2aMessage(String str, String str2) {
        return DistributedPermissionManager.getDefault().processZ2aMessage(str, str2);
    }

    public static class AppIdInfoHelper {
        private static final String BUNDLE_NAME = "bundleName";
        private static final String DEVICE_ID = "deviceID";
        private static final String PID = "pid";
        private static final String UID = "uid";
        private String appBundleName;
        private String appDeviceId;
        private int appPid = -1;
        private int appUid = -1;

        private AppIdInfoHelper() {
        }

        public static String createAppIdInfo(int i, int i2) {
            ZSONObject zSONObject = new ZSONObject();
            zSONObject.put(PID, (Object) Integer.valueOf(i));
            zSONObject.put(UID, (Object) Integer.valueOf(i2));
            return zSONObject.toString();
        }

        public static String createAppIdInfo(int i, int i2, String str) {
            ZSONObject zSONObject = new ZSONObject();
            zSONObject.put(PID, (Object) Integer.valueOf(i));
            zSONObject.put(UID, (Object) Integer.valueOf(i2));
            if (!(str == null || str.length() == 0)) {
                zSONObject.put(DEVICE_ID, (Object) str);
            }
            return zSONObject.toString();
        }

        public static String createAppIdInfo(int i, int i2, String str, String str2) {
            ZSONObject zSONObject = new ZSONObject();
            zSONObject.put(PID, (Object) Integer.valueOf(i));
            zSONObject.put(UID, (Object) Integer.valueOf(i2));
            if (!(str == null || str.length() == 0)) {
                zSONObject.put(DEVICE_ID, (Object) str);
            }
            if (!(str2 == null || str2.length() == 0)) {
                zSONObject.put("bundleName", (Object) str2);
            }
            return zSONObject.toString();
        }

        public static AppIdInfoHelper createAppIdInfo(String str) {
            AppIdInfoHelper appIdInfoHelper = new AppIdInfoHelper();
            Objects.requireNonNull(str, "createAppIdInfo require non-null appIdInfo");
            try {
                ZSONObject stringToZSON = ZSONObject.stringToZSON(str);
                if (stringToZSON == null) {
                    HiLog.error(PermissionInner.LABEL, "stringToZSON failed: %{private}s", str);
                    return appIdInfoHelper;
                }
                appIdInfoHelper.appUid = stringToZSON.getInteger(UID).intValue();
                appIdInfoHelper.appPid = stringToZSON.getInteger(PID).intValue();
                appIdInfoHelper.appBundleName = stringToZSON.getString("bundleName");
                appIdInfoHelper.appDeviceId = stringToZSON.getString(DEVICE_ID);
                HiLog.debug(PermissionInner.LABEL, "get : uid %{public}d, pid %{public}d, dev %{private}s, bundlename %{public}s", Integer.valueOf(appIdInfoHelper.appUid), Integer.valueOf(appIdInfoHelper.appPid), appIdInfoHelper.appDeviceId, appIdInfoHelper.appBundleName);
                return appIdInfoHelper;
            } catch (JSONException e) {
                HiLog.error(PermissionInner.LABEL, "translation appIdInfo JSONException: %{private}s", e.getMessage());
                return appIdInfoHelper;
            } catch (ZSONException e2) {
                HiLog.error(PermissionInner.LABEL, "translation appIdInfo ZSONException: %{private}s", e2.getMessage());
                return appIdInfoHelper;
            } catch (Exception e3) {
                HiLog.error(PermissionInner.LABEL, "translation appIdInfo Exception: %{private}s", e3.getMessage());
                return appIdInfoHelper;
            }
        }

        public int getAppUid() {
            return this.appUid;
        }

        public int getAppPid() {
            return this.appPid;
        }

        public String getAppDeviceId() {
            return this.appDeviceId;
        }

        public String getAppBundleName() {
            return this.appBundleName;
        }
    }
}
