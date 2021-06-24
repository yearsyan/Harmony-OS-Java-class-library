package ohos.security.permission.adapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ohos.bundle.BundleInfo;
import ohos.bundle.BundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;
import ohos.security.permission.PermissionConversion;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;
import ohos.utils.StringUtils;

public class PermissionReasonAdapter {
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    private static final String TAG = "PermissionReasonAdapter";
    private BundleManager bundleManager = BundleManager.getInstance();

    private Optional<String> getPermissionUseReason(List<BundleInfo.ReqPermissionDetail> list, List<String> list2) {
        if (list == null) {
            HiLog.error(LABEL, "reqPermissionDetailList should not be null", new Object[0]);
            return Optional.empty();
        }
        dumpThePermissionsReason(list);
        for (String str : list2) {
            Optional<String> theReasonResId = getTheReasonResId(list, PermissionConversion.getZosPermissionNameIfPossible(str));
            if (theReasonResId.isPresent()) {
                return theReasonResId;
            }
        }
        return Optional.empty();
    }

    private Optional<String> getTheReason(List<BundleInfo.ReqPermissionDetail> list, String str) {
        for (BundleInfo.ReqPermissionDetail reqPermissionDetail : list) {
            if (!(!str.equals(reqPermissionDetail.getName()) || reqPermissionDetail.getReason() == null || reqPermissionDetail.getReason().isEmpty())) {
                return Optional.ofNullable(reqPermissionDetail.getReason());
            }
        }
        return Optional.empty();
    }

    private Optional<String> getTheReasonResId(List<BundleInfo.ReqPermissionDetail> list, String str) {
        for (BundleInfo.ReqPermissionDetail reqPermissionDetail : list) {
            if (!(!str.equals(reqPermissionDetail.getName()) || reqPermissionDetail.getReason() == null || reqPermissionDetail.getReason().isEmpty())) {
                HiLog.debug(LABEL, "find the reason id for permission %{public}s", str);
                if (reqPermissionDetail.getReasonId() <= 0) {
                    return Optional.ofNullable(reqPermissionDetail.getReason());
                }
                return Optional.ofNullable(Integer.toString(reqPermissionDetail.getReasonId()));
            }
        }
        HiLog.debug(LABEL, "dont find the reason id for permission %{public}s", str);
        return Optional.empty();
    }

    private void dumpThePermissionsReason(List<BundleInfo.ReqPermissionDetail> list) {
        for (BundleInfo.ReqPermissionDetail reqPermissionDetail : list) {
            HiLog.debug(LABEL, "permName : %{public}s, permReason : %{public}s, permReasonId: %{public}d", reqPermissionDetail.getName(), reqPermissionDetail.getReason(), Integer.valueOf(reqPermissionDetail.getReasonId()));
        }
    }

    private String[] sortInputPermission(String[] strArr) {
        List asList = Arrays.asList(strArr);
        Collections.sort(asList);
        return (String[]) asList.toArray(new String[0]);
    }

    private Optional<String> getPermissionUsagesInfoOrResId(String str, String[] strArr) {
        HiLog.info(LABEL, "getPermissionUsagesInfoOrResId: called", new Object[0]);
        if (StringUtils.isEmpty(str) || strArr == null || strArr.length == 0) {
            HiLog.error(LABEL, "getPermissionUsagesInfo param error, packageName : %{public}s", str);
            return Optional.empty();
        }
        String[] sortInputPermission = sortInputPermission(strArr);
        try {
            if (this.bundleManager == null) {
                HiLog.error(LABEL, "BundleManager.getInstance", new Object[0]);
                return Optional.empty();
            }
            BundleInfo bundleInfo = this.bundleManager.getBundleInfo(str, 0);
            if (bundleInfo == null) {
                HiLog.error(LABEL, "bundleManager.getBundleInfo", new Object[0]);
                return Optional.empty();
            }
            Optional<String> permissionUseReason = getPermissionUseReason(bundleInfo.getReqPermissionDetail(), (List) Arrays.stream(sortInputPermission).collect(Collectors.toList()));
            HiLog.info(LABEL, "the reason we get is : %{public}s", permissionUseReason);
            return permissionUseReason;
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "error in getPermissionUsagesInfo", new Object[0]);
            return Optional.empty();
        }
    }

    public Optional<String> getPermissionUsagesInfo(String str, String[] strArr) {
        HiLog.info(LABEL, "a getPermissionUsagesInfo: called", new Object[0]);
        Optional<String> permissionUsagesInfoOrResId = getPermissionUsagesInfoOrResId(str, strArr);
        try {
            if (permissionUsagesInfoOrResId.isPresent()) {
                if (Integer.parseInt(permissionUsagesInfoOrResId.get()) > 0) {
                    HiLog.debug(LABEL, "a getPermissionUsagesInfoOrResId: %{public}s", permissionUsagesInfoOrResId.get());
                    return ResourceManagerAdapter.getStringById(str, Integer.parseInt(permissionUsagesInfoOrResId.get()));
                }
            }
            return Optional.empty();
        } catch (NumberFormatException unused) {
            return permissionUsagesInfoOrResId;
        }
    }
}
