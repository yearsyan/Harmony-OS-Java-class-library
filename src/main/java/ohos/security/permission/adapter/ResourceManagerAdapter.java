package ohos.security.permission.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ohos.bundle.ApplicationInfo;
import ohos.bundle.BundleManager;
import ohos.bundle.ModuleInfo;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.innerkit.asset.Package;
import ohos.global.resource.Element;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.ResourceManagerInner;
import ohos.global.resource.ResourcePath;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

public class ResourceManagerAdapter {
    private static final HiLogLabel LABEL = HiLogLabelUtil.ADAPTER.newHiLogLabel(TAG);
    private static final String TAG = "ResourceManagerAdapter";

    private ResourceManagerAdapter() {
    }

    private static List<ModuleInfo> getModuleInfos(String str) {
        BundleManager instance = BundleManager.getInstance();
        if (instance == null) {
            HiLog.error(LABEL, "get BundleManager error", new Object[0]);
            return Collections.emptyList();
        }
        try {
            ApplicationInfo applicationInfo = instance.getApplicationInfo(str, 0, UserHandleAdapter.myUserId());
            if (applicationInfo == null) {
                HiLog.error(LABEL, "getApplicationInfo for %{public}s error", str);
                return Collections.emptyList();
            }
            List<ModuleInfo> moduleInfos = applicationInfo.getModuleInfos();
            if (moduleInfos != null) {
                if (!moduleInfos.isEmpty()) {
                    return moduleInfos;
                }
            }
            HiLog.error(LABEL, "moduleInfos == null or isEmpty", new Object[0]);
            return Collections.emptyList();
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "RemoteException e", new Object[0]);
            return Collections.emptyList();
        }
    }

    private static Optional<Package> getPackage(ResourceUtilsAdapter resourceUtilsAdapter, List<ModuleInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (ModuleInfo moduleInfo : list) {
            String moduleSourceDir = moduleInfo.getModuleSourceDir();
            HiLog.debug(LABEL, "getPackage:: add ModuleSourceDir %{public}s", moduleSourceDir);
            arrayList.add(moduleSourceDir);
        }
        if (!arrayList.isEmpty()) {
            return resourceUtilsAdapter.getPackage(arrayList);
        }
        HiLog.info(LABEL, "getPackage:: moduleSourceDirs is empty", new Object[0]);
        return Optional.empty();
    }

    private static ResourcePath[] getResourcePaths(List<ModuleInfo> list) {
        ResourcePath[] resourcePathArr = new ResourcePath[0];
        if (list == null || list.isEmpty()) {
            return resourcePathArr;
        }
        int size = list.size();
        ResourcePath[] resourcePathArr2 = new ResourcePath[size];
        for (int i = 0; i < size; i++) {
            ModuleInfo moduleInfo = list.get(i);
            if (moduleInfo == null || moduleInfo.getModuleSourceDir() == null || moduleInfo.getModuleName() == null) {
                HiLog.debug(LABEL, "getResourcePath moduleSourceDir or moduleName is null", new Object[0]);
            } else {
                HiLog.debug(LABEL, "getResourcePath moduleSourceDirs: %{public}s, moduleName: %{public}s", moduleInfo.getModuleSourceDir(), moduleInfo.getModuleName());
                ResourcePath resourcePath = new ResourcePath();
                resourcePath.setResourcePath(moduleInfo.getModuleSourceDir(), moduleInfo.getModuleName());
                resourcePathArr2[i] = resourcePath;
            }
        }
        return resourcePathArr2;
    }

    private static Optional<ResourceManager> getResourceManager(String str) {
        List<ModuleInfo> moduleInfos = getModuleInfos(str);
        ResourceUtilsAdapter resourceUtilsAdapter = new ResourceUtilsAdapter();
        Optional<Package> optional = getPackage(resourceUtilsAdapter, moduleInfos);
        if (!optional.isPresent()) {
            HiLog.error(LABEL, "getPackage for %{public}s err", str);
            return Optional.empty();
        }
        DeviceCapability deviceCapability = resourceUtilsAdapter.getDeviceCapability();
        Configuration configuration = resourceUtilsAdapter.getConfiguration();
        ResourcePath[] resourcePaths = getResourcePaths(moduleInfos);
        ResourceManagerInner resourceManagerInner = new ResourceManagerInner();
        try {
            if (!resourceManagerInner.init(optional.get(), resourcePaths, configuration, deviceCapability)) {
                HiLog.error(LABEL, "resourceManagerInner init failed", new Object[0]);
                return Optional.empty();
            }
        } catch (IOException unused) {
            HiLog.error(LABEL, "when resourceManagerInner init by configuration, exception error", new Object[0]);
        }
        return Optional.ofNullable(resourceManagerInner.getResourceManager());
    }

    public static Optional<String> getStringById(String str, int i) {
        if (str == null || str.isEmpty() || i <= 0) {
            HiLog.error(LABEL, "param error", new Object[0]);
            return Optional.empty();
        }
        Optional<ResourceManager> resourceManager = getResourceManager(str);
        if (!resourceManager.isPresent()) {
            HiLog.error(LABEL, "getResourceManager error, bundleName %{public}s, resId %{public}d", str, Integer.valueOf(i));
            return Optional.empty();
        }
        try {
            Element element = resourceManager.get().getElement(i);
            return element == null ? Optional.empty() : Optional.ofNullable(element.getString());
        } catch (NotExistException unused) {
            HiLog.error(LABEL, "NotExistException error, bundleName %{public}s, resId %{public}d", str, Integer.valueOf(i));
            return Optional.empty();
        } catch (IOException unused2) {
            HiLog.error(LABEL, "IOException error, bundleName %{public}s, resId %{public}d", str, Integer.valueOf(i));
            return Optional.empty();
        } catch (WrongTypeException unused3) {
            HiLog.error(LABEL, "WrongTypeException error, bundleName %{public}s, resId %{public}d", str, Integer.valueOf(i));
            return Optional.empty();
        }
    }
}
