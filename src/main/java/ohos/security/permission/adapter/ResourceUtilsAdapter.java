package ohos.security.permission.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import ohos.global.configuration.DeviceCapability;
import ohos.global.innerkit.asset.Package;
import ohos.global.resource.ResourceUtils;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class ResourceUtilsAdapter {
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    private static final int OP_FAIL = 1;
    private static final int OP_OK = 0;
    private static final String TAG = "ResourceUtilsAdapter";
    private Method addAssetPath;
    private AssetManager assetManager;
    private Configuration config;
    private Context context = AppContextAdapter.getInstance().getContext();

    public ResourceUtilsAdapter() {
        initAssetManager();
        initAddAssetPathInterface();
        initConfiguration();
    }

    private void initAssetManager() {
        try {
            this.assetManager = (AssetManager) AssetManager.class.newInstance();
        } catch (InstantiationException unused) {
            HiLog.error(LABEL, "InstantiationException e when get AssetManager instance", new Object[0]);
        } catch (IllegalAccessException unused2) {
            HiLog.error(LABEL, "IllegalAccessException e when get AssetManager instance", new Object[0]);
        }
    }

    private void initAddAssetPathInterface() {
        AssetManager assetManager2 = this.assetManager;
        if (assetManager2 != null) {
            try {
                this.addAssetPath = assetManager2.getClass().getMethod("addAssetPath", String.class);
            } catch (NoSuchMethodException unused) {
                HiLog.error(LABEL, "NoSuchMethodException e when init addAssetPath Interface", new Object[0]);
            }
        }
    }

    private void initConfiguration() {
        if (this.context.getResources() != null) {
            this.config = this.context.getResources().getConfiguration();
        }
    }

    public DeviceCapability getDeviceCapability() {
        return ResourceUtils.convertToDeviceCapability(this.config);
    }

    public ohos.global.configuration.Configuration getConfiguration() {
        return ResourceUtils.convert(this.config);
    }

    private int reflectAddAssetPath(List<String> list) {
        if (this.addAssetPath == null) {
            HiLog.error(LABEL, "addAssetPath should not be null", new Object[0]);
            return 1;
        }
        try {
            for (String str : list) {
                if (str != null) {
                    this.addAssetPath.invoke(this.assetManager, str);
                }
            }
            return 0;
        } catch (IllegalAccessException unused) {
            HiLog.error(LABEL, "IllegalAccessException e when invoke addAssetPath", new Object[0]);
            return 1;
        } catch (InvocationTargetException unused2) {
            HiLog.error(LABEL, "InvocationTargetException e when invoke addAssetPath", new Object[0]);
            return 1;
        }
    }

    public Optional<Package> getPackage(List<String> list) {
        if (list == null || list.isEmpty() || reflectAddAssetPath(list) == 1) {
            return Optional.empty();
        }
        Resources resources = new Resources(this.assetManager, null, null);
        Package r1 = new Package();
        r1.setResource(resources);
        return Optional.ofNullable(r1);
    }
}
