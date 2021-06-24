package ohos.global.resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.innerkit.asset.Package;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ResourceManagerInner {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "ResourceManagerInner");
    private static final Object LOCK = new Object();
    private static volatile ResourceManagerImpl sysResManagerImpl;
    private ResourceManagerImpl resManagerImpl;

    public boolean init(Package r2, ResourcePath[] resourcePathArr, Configuration configuration, DeviceCapability deviceCapability) throws IOException {
        this.resManagerImpl = new ResourceManagerImpl();
        return this.resManagerImpl.init(r2, resourcePathArr, configuration, deviceCapability);
    }

    public static ResourceManager getSystemResourceManager() throws IOException {
        if (sysResManagerImpl == null) {
            synchronized (LOCK) {
                if (sysResManagerImpl == null) {
                    ResourceManagerImpl resourceManagerImpl = new ResourceManagerImpl();
                    resourceManagerImpl.initSys(new Configuration(), new DeviceCapability());
                    sysResManagerImpl = resourceManagerImpl;
                }
            }
        }
        return sysResManagerImpl;
    }

    public ResourceManager getResourceManager() {
        ResourceManagerImpl resourceManagerImpl = this.resManagerImpl;
        if (resourceManagerImpl != null) {
            return resourceManagerImpl;
        }
        HiLog.error(LABEL, "resManagerImpl is null", new Object[0]);
        return null;
    }

    public final void release() {
        this.resManagerImpl.release();
    }

    public List<ResourcePath> getResourcePath() {
        ResourceManagerImpl resourceManagerImpl = this.resManagerImpl;
        if (resourceManagerImpl == null) {
            return new ArrayList();
        }
        return resourceManagerImpl.getResourcePath();
    }

    public boolean addResourcePath(ResourcePath resourcePath) throws IOException {
        ResourceManagerImpl resourceManagerImpl = this.resManagerImpl;
        if (resourceManagerImpl == null) {
            return false;
        }
        return resourceManagerImpl.addResourcePath(resourcePath);
    }

    public static <T> int getAResId(int i, Class<?> cls, T t) {
        String str;
        Field[] fields = cls.getFields();
        int length = fields.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                str = null;
                break;
            }
            Field field = fields[i2];
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && "int".equals(field.getType().getName())) {
                try {
                    if (field.getInt(cls) == i) {
                        str = field.getName();
                        break;
                    }
                } catch (IllegalAccessException unused) {
                    continue;
                }
            }
            i2++;
        }
        if (str != null) {
            return getAResIdByType(str, cls, t);
        }
        HiLog.error(LABEL, "name of the resource is null", new Object[0]);
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00bc, code lost:
        if ("theme".equals(r7) != false) goto L_0x00c0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> int getAResIdByType(java.lang.String r6, java.lang.Class<?> r7, T r8) {
        /*
        // Method dump skipped, instructions count: 264
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.ResourceManagerInner.getAResIdByType(java.lang.String, java.lang.Class, java.lang.Object):int");
    }

    public static List<Long> getManagerHandle(ResourceManager resourceManager) {
        return resourceManager instanceof ResourceManagerImpl ? ((ResourceManagerImpl) resourceManager).getManagerHandle() : new ArrayList();
    }

    public static ArrayList<String> findValidAndSort(String str, List<String> list) throws LocaleFallBackException {
        return ResourceManagerImpl.findValidAndSort(str, list);
    }
}
