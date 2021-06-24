package ohos.global.innerkit.asset;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import ohos.bundle.ProfileConstants;
import ohos.event.notification.NotificationRequest;
import ohos.global.resource.RawFileDescriptor;
import ohos.global.resource.RawFileDescriptorImpl;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class Package {
    public static final int ACCESS_BUFFER = 3;
    public static final int ACCESS_RANDOM = 1;
    public static final int ACCESS_STREAMING = 2;
    public static final int ACCESS_UNKNOWN = 0;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "Package");
    public static final String SYS_RESOURCE_PREFIX = "ohos:";
    private static Resources sysResource = null;
    private Resources resource = null;

    public void setResource(Resources resources) {
        this.resource = resources;
    }

    public InputStream open(String str) throws IOException {
        if (!(str == null || str.length() == 0)) {
            if (sysResource != null && str.startsWith(SYS_RESOURCE_PREFIX)) {
                return sysResource.getAssets().open(str.substring(5), 1);
            } else if (this.resource != null && !str.startsWith(SYS_RESOURCE_PREFIX)) {
                return this.resource.getAssets().open(str, 1);
            }
        }
        return null;
    }

    public RawFileDescriptor openRawFileDescriptor(String str) throws IOException {
        AssetFileDescriptor assetFileDescriptor;
        if (str == null || str.length() == 0) {
            HiLogLabel hiLogLabel = LABEL;
            Object[] objArr = new Object[1];
            if (str == null) {
                str = "null";
            }
            objArr[0] = str;
            HiLog.error(hiLogLabel, "open file path is %{public}s", objArr);
            return null;
        }
        if (sysResource != null && str.startsWith(SYS_RESOURCE_PREFIX)) {
            assetFileDescriptor = sysResource.getAssets().openFd(str.substring(5));
        } else if (this.resource == null || str.startsWith(SYS_RESOURCE_PREFIX)) {
            return null;
        } else {
            assetFileDescriptor = this.resource.getAssets().openFd(str);
        }
        return new RawFileDescriptorImpl(new AfdAdapter(assetFileDescriptor));
    }

    public String[] list(String str) throws IOException {
        if (str == null) {
            HiLog.error(LABEL, "list path is null", new Object[0]);
            return new String[0];
        } else if (sysResource == null || !str.startsWith(SYS_RESOURCE_PREFIX)) {
            return (this.resource == null || str.startsWith(SYS_RESOURCE_PREFIX)) ? new String[0] : this.resource.getAssets().list(str);
        } else {
            return sysResource.getAssets().list(str.substring(5));
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:27|28|(2:30|31)|34) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0053, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.global.innerkit.asset.Package.LABEL, "getEntryType open error.", new java.lang.Object[0]);
        r6 = ohos.global.resource.Entry.Type.FOLDER;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0060, code lost:
        if (0 != 0) goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0066, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.global.innerkit.asset.Package.LABEL, "getEntryType close error.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006d, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006e, code lost:
        if (0 != 0) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0074, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.global.innerkit.asset.Package.LABEL, "getEntryType close error.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x007b, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0055 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.global.resource.Entry.Type getEntryType2(java.lang.String r7) {
        /*
        // Method dump skipped, instructions count: 142
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.innerkit.asset.Package.getEntryType2(java.lang.String):ohos.global.resource.Entry$Type");
    }

    public void loadSystemResource(String str) {
        if (str != null && str.length() != 0 && sysResource == null && new File(str).exists()) {
            try {
                AssetManager assetManager = new AssetManager();
                AssetManager.class.getMethod("addAssetPath", String.class).invoke(assetManager, str);
                HiLog.debug(LABEL, "load system resource OK", new Object[0]);
                sysResource = new Resources(assetManager, null, null);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
                HiLog.error(LABEL, "load system resource Failed", new Object[0]);
            }
        }
    }

    public static <T> int getAResId(String str, String str2, T t) {
        if (t == null) {
            HiLog.error(LABEL, "context is null", new Object[0]);
            return 0;
        } else if (t instanceof Context) {
            T t2 = t;
            Resources resources = t2.getResources();
            if (resources != null) {
                return resources.getIdentifier(str, str2, t2.getPackageName());
            }
            HiLog.error(LABEL, "reses is null", new Object[0]);
            return 0;
        } else {
            HiLog.error(LABEL, "context is not an instance of Context", new Object[0]);
            return 0;
        }
    }

    public AssetManager getAAssetManager() {
        Resources resources = this.resource;
        if (resources == null) {
            return null;
        }
        return resources.getAssets();
    }

    public void addOverlays(boolean z, List<String> list) {
        Resources resources = z ? sysResource : this.resource;
        if (resources != null) {
            try {
                AssetManager assets = resources.getAssets();
                Method method = AssetManager.class.getMethod("addAssetPath", String.class);
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    method.invoke(assets, it.next());
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
                HiLogLabel hiLogLabel = LABEL;
                Object[] objArr = new Object[1];
                objArr[0] = z ? NotificationRequest.CLASSIFICATION_SYSTEM : ProfileConstants.APP;
                HiLog.error(hiLogLabel, "load overlay %{public}s resource Failed", objArr);
            }
        }
    }
}
