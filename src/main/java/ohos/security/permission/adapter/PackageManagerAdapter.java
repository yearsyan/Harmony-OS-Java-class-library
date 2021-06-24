package ohos.security.permission.adapter;

import android.content.Context;
import com.huawei.android.os.UserHandleEx;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;
import ohos.utils.StringUtils;

public class PackageManagerAdapter {
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    private static final String TAG = "PackageManagerAdapter";

    private PackageManagerAdapter() {
    }

    public static PackageManagerAdapter getInstance() {
        return SingletonClassHolder.INSTANCE;
    }

    public void grantRuntimePermission(Object obj, String str, String str2, int i) {
        if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "grantRuntimePermission: contextObject type error, %{public}s", obj);
        } else if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "grantRuntimePermission: packageName or permissionName is null or empty", new Object[0]);
        } else {
            ((Context) obj).getPackageManager().grantRuntimePermission(str, str2, UserHandleEx.getUserHandle(i));
        }
    }

    public void revokeRuntimePermission(Object obj, String str, String str2, int i) {
        if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "revokeSensitivePermission: contextObject type error, %{public}s", obj);
        } else if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
            HiLog.error(LABEL, "revokeSensitivePermission: packageName or permissionName is null or empty", new Object[0]);
        } else {
            ((Context) obj).getPackageManager().revokeRuntimePermission(str, str2, UserHandleEx.getUserHandle(i));
        }
    }

    public int getPermissionFlags(Object obj, String str, String str2, int i) {
        if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "getPermissionStatus: contextObject type error, %{public}s", obj);
            return 0;
        } else if (!StringUtils.isEmpty(str) && !StringUtils.isEmpty(str2)) {
            return ((Context) obj).getPackageManager().getPermissionFlags(str, str2, UserHandleEx.getUserHandle(i));
        } else {
            HiLog.error(LABEL, "getPermissionStatus: permissionName or packageName is null or empty", new Object[0]);
            return 0;
        }
    }

    public void updatePermissionFlags(Object obj, UpdatePermissionStatusRequest updatePermissionStatusRequest) {
        if (!(obj instanceof Context)) {
            HiLog.error(LABEL, "updatePermissionStatus: contextObject type error, %{public}s", obj);
        } else if (StringUtils.isEmpty(updatePermissionStatusRequest.getPermissionName()) || StringUtils.isEmpty(updatePermissionStatusRequest.getPackageName())) {
            HiLog.error(LABEL, "updatePermissionFlags: permissionName or packageName is null or empty", new Object[0]);
        } else {
            ((Context) obj).getPackageManager().updatePermissionFlags(updatePermissionStatusRequest.getPermissionName(), updatePermissionStatusRequest.getPackageName(), updatePermissionStatusRequest.getFlagsMask(), updatePermissionStatusRequest.getFlagValues(), UserHandleEx.getUserHandle(updatePermissionStatusRequest.getUserId()));
        }
    }

    /* access modifiers changed from: private */
    public static final class SingletonClassHolder {
        private static final PackageManagerAdapter INSTANCE = new PackageManagerAdapter();

        private SingletonClassHolder() {
        }
    }

    public static class UpdatePermissionStatusRequest {
        private final int flagValues;
        private final int flagsMask;
        private final String packageName;
        private final String permissionName;
        private final int userId;

        private UpdatePermissionStatusRequest(UpdatePermissionStatusRequestBuilder updatePermissionStatusRequestBuilder) {
            this.permissionName = updatePermissionStatusRequestBuilder.permissionName;
            this.packageName = updatePermissionStatusRequestBuilder.packageName;
            this.flagsMask = updatePermissionStatusRequestBuilder.flagsMask;
            this.flagValues = updatePermissionStatusRequestBuilder.flagValues;
            this.userId = updatePermissionStatusRequestBuilder.userId;
        }

        public static UpdatePermissionStatusRequestBuilder newBuilder() {
            return new UpdatePermissionStatusRequestBuilder();
        }

        public String getPermissionName() {
            return this.permissionName;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public int getFlagsMask() {
            return this.flagsMask;
        }

        public int getFlagValues() {
            return this.flagValues;
        }

        public int getUserId() {
            return this.userId;
        }

        public static class UpdatePermissionStatusRequestBuilder {
            private int flagValues;
            private int flagsMask;
            private String packageName;
            private String permissionName;
            private int userId;

            UpdatePermissionStatusRequestBuilder() {
            }

            public UpdatePermissionStatusRequestBuilder setPermissionName(String str) {
                this.permissionName = str;
                return this;
            }

            public UpdatePermissionStatusRequestBuilder setPackageName(String str) {
                this.packageName = str;
                return this;
            }

            public UpdatePermissionStatusRequestBuilder setFlagsMask(int i) {
                this.flagsMask = i;
                return this;
            }

            public UpdatePermissionStatusRequestBuilder setFlagValues(int i) {
                this.flagValues = i;
                return this;
            }

            public UpdatePermissionStatusRequestBuilder setUserId(int i) {
                this.userId = i;
                return this;
            }

            public UpdatePermissionStatusRequest build() {
                return new UpdatePermissionStatusRequest(this);
            }
        }
    }
}
