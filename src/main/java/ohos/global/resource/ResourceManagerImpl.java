package ohos.global.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import ohos.global.config.ConfigManager;
import ohos.global.config.ConfigManagerImpl;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.configuration.LocaleProfile;
import ohos.global.icu.text.PluralRules;
import ohos.global.innerkit.asset.Package;
import ohos.global.resource.solidxml.PatternImpl;
import ohos.global.resource.solidxml.SolidXmllmpl;
import ohos.global.resource.solidxml.Theme;
import ohos.global.resource.solidxml.ThemeImpl;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.global.resource.solidxml.TypedAttributeImpl;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ResourceManagerImpl extends ResourceManager {
    private static final String CONFIG_FILE = "config.json";
    private static final int CORE_POOL_SIZE = 3;
    private static final int DEFAULT_DENSITY = 160;
    private static final String DENSITY_INDEPENDENT_PIXEL = "dp";
    private static final String[] DIMENSION_UNIT_STRS = {PIXEL_UNIT, DENSITY_INDEPENDENT_PIXEL, SCALE_INDEPENDENT_PIXEL, VIRTUAL_PIXEL, FONT_SIZE_PIXEL};
    private static final String FONT_SIZE_PIXEL = "fp";
    private static final String HARMONY_PATTERN_REFERENCE_PREFIX = "@ohos:theme/";
    private static final String[][] HARMONY_REFERENCE_PREFIX = {new String[]{"@ohos:bool/", "@ohos:color/", "@ohos:dimen/", "@ohos:integer/", "@ohos:string/", "@ohos:layout/", "@ohos:drawable/"}, new String[]{"$ohos:boolean:", "$ohos:color:", "$ohos:float:", "$ohos:integer:", "$ohos:string:", "$ohos:layout:", "$ohos:media:"}};
    private static final String HARMONY_THEME_REFERENCE_PREFIX = "@ohos:theme/";
    private static final int ID_MASK = -16777216;
    private static final String JS = "js";
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "ResourceManagerImpl");
    private static final int MAX_CACHE_SIZE = 10;
    private static final int MAX_ELEMENT_CACHE_SIZE = 50;
    private static final int MAX_POOL_SIZE = Math.max(3, Runtime.getRuntime().availableProcessors());
    private static final int MAX_QUEUE_SIZE = 20;
    private static final int MAX_WAITING_TIME = 60000;
    private static final int MIN_SYSTEM_ID = 117440512;
    private static final String MODULE = "module";
    private static final Object MODULES_LOCK = new Object();
    private static final String MODULE_NAME = "name";
    private static final String ONLINE_THEME_COUNT_DOWN_LATCH = "onlineThemeCountDownLatch";
    private static final String PATTERN_REFERENCE_PREFIX = "@theme/";
    private static final String PIXEL_UNIT = "px";
    private static final Object PLURALRULES_LOCK = new Object();
    private static final String[][] REFERENCE_PREFIX = {new String[]{"@string/", "@bool/", "@color/", "@dimen/", "@integer/", "@layout/", "@drawable/"}, new String[]{"$string:", "$boolean:", "$color:", "$float:", "$integer:", "$layout:", "$media:"}};
    private static final Pattern REGEX_UNIT = Pattern.compile("^(?<value>-?\\d+(\\.\\d+)?)(?<unit>px|dp|sp|vp|fp)?$");
    private static final String RESOURCE_INDEX_NAME = "resources.index";
    private static final String[] RTL_LANGUAGES = {"ar", "ur", "fa", "he", "iw", "ug"};
    private static final String SCALE_INDEPENDENT_PIXEL = "sp";
    private static final String SYSTEM_MODULE_NAME = "entry";
    private static final String SYS_RES_COUNT_DOWN_LATCH = "sysResCountDownLatch";
    private static final Object SYS_RES_LOCK = new Object();
    private static final String THEME_REFERENCE_PREFIX = "@theme/";
    private static final String VIRTUAL_PIXEL = "vp";
    private static Executor executor = new ThreadPoolExecutor(3, MAX_POOL_SIZE, 1, TimeUnit.MINUTES, new LinkedBlockingQueue(20), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    private static volatile String processModules = null;
    private static volatile boolean sysInit = false;
    private final Object LOCK = new Object();
    private List<String> aceResLocales = new ArrayList();
    private CacheMap<Integer, Object> cacheMap = new CacheMap<>(10);
    private ConfigManagerImpl configManager;
    private ElementCache<Integer, Element> elementCache = new ElementCache<>(50);
    private long handle;
    private OnlineTheme onlineTheme;
    private volatile PluralRules pluralRules = null;
    private DeviceCapability resCapability;
    private Configuration resConfig;
    private Package resPackage = new Package();
    private List<ResourcePath> resPathList = new ArrayList();
    private long sysHandle;
    private String unit = PIXEL_UNIT;

    private native boolean nativeAddIndex(long j, byte[] bArr);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeAddOverlay(long j, String str, String[] strArr);

    private static native String[] nativeFindValidAndSort(String str, String[] strArr);

    private native int nativeGetBestLocale(long j, String[] strArr, int[] iArr, String[] strArr2);

    private native Object nativeGetElement(long j, int i);

    private native String nativeGetIdentifier(long j, int i);

    private native long nativeGetResource(long j, int i);

    private native String nativeGetString(long j, int i);

    private native Object[] nativeGetStringArray(long j, int i);

    private native void nativeInitThemeColor(long j, String[] strArr, String[] strArr2);

    private native void nativeRelease(long j);

    private native long nativeSetup(Locale locale, int[] iArr);

    static {
        String str;
        String property = System.getProperty("os.name");
        if (property != null) {
            String upperCase = property.toUpperCase(Locale.US);
            if (upperCase.contains("WINDOWS")) {
                str = "libresourcemanager_jni_win";
            } else if (upperCase.contains("MAC")) {
                str = "resourcemanager_jni_mac";
            }
            System.loadLibrary(str);
        }
        str = "resourcemanager_jni.z";
        System.loadLibrary(str);
    }

    public boolean init(Package r10, ResourcePath[] resourcePathArr, Configuration configuration, DeviceCapability deviceCapability) throws IOException {
        if (resourcePathArr == null || resourcePathArr.length == 0 || r10 == null) {
            HiLog.error(LABEL, "ResourceManager Init, resource paths is null, init failed", new Object[0]);
            return false;
        }
        this.resPackage = r10;
        this.configManager = new ConfigManagerImpl();
        if (!setupManager(configuration, deviceCapability)) {
            return false;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        HashMap<String, CountDownLatch> hashMap = new HashMap<>();
        hashMap.put(ONLINE_THEME_COUNT_DOWN_LATCH, countDownLatch);
        hashMap.put(SYS_RES_COUNT_DOWN_LATCH, countDownLatch2);
        executor.execute(createRunnableForTheme(resourcePathArr, countDownLatch));
        executor.execute(createRunnableForSystemRes(countDownLatch, countDownLatch2));
        CountDownLatch countDownLatch3 = new CountDownLatch(1);
        executor.execute(createRunnableForAppResourceInit(resourcePathArr, configuration, deviceCapability, countDownLatch3, hashMap));
        try {
            if (!countDownLatch3.await(60000, TimeUnit.MILLISECONDS)) {
                HiLog.error(LABEL, "countDownLatch timeout", new Object[0]);
                return false;
            }
        } catch (InterruptedException unused) {
            HiLog.error(LABEL, "countDownLatch interrupt", new Object[0]);
        }
        this.cacheMap.clear();
        return true;
    }

    private Runnable createRunnableForTheme(final ResourcePath[] resourcePathArr, final CountDownLatch countDownLatch) {
        return new Runnable() {
            /* class ohos.global.resource.ResourceManagerImpl.AnonymousClass1 */

            public void run() {
                Optional<OnlineTheme> createOnlineTheme = OnlineTheme.createOnlineTheme(resourcePathArr[0]);
                ResourceManagerImpl.this.onlineTheme = createOnlineTheme.isPresent() ? createOnlineTheme.get() : null;
                ResourceManagerImpl.this.initAceResources(resourcePathArr[0]);
                countDownLatch.countDown();
            }
        };
    }

    private Runnable createRunnableForSystemRes(final CountDownLatch countDownLatch, final CountDownLatch countDownLatch2) {
        return new Runnable() {
            /* class ohos.global.resource.ResourceManagerImpl.AnonymousClass2 */

            public void run() {
                ResourceManagerImpl.this.initSystemResouce();
                try {
                    countDownLatch.await();
                } catch (InterruptedException unused) {
                    HiLog.error(ResourceManagerImpl.LABEL, "onlineThemeCountDownLatch interrupt", new Object[0]);
                }
                ResourceManagerImpl.this.initOnlineThemeColor(true);
                countDownLatch2.countDown();
            }
        };
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void awaitHelper(CountDownLatch countDownLatch, String str) {
        try {
            countDownLatch.await();
        } catch (InterruptedException unused) {
            HiLog.error(LABEL, "%{public}s", str);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private HashMap<String, List<ResourcePath>> collectModule(ResourcePath[] resourcePathArr) {
        HashMap<String, List<ResourcePath>> hashMap = new HashMap<>();
        for (int i = 0; i < resourcePathArr.length; i++) {
            if (resourcePathArr[i] != null) {
                String aaName = resourcePathArr[i].getAaName();
                if (hashMap.containsKey(aaName)) {
                    hashMap.get(aaName).add(resourcePathArr[i]);
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(resourcePathArr[i]);
                    hashMap.put(aaName, arrayList);
                }
            }
        }
        return hashMap;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void markProcessModule() {
        synchronized (MODULES_LOCK) {
            if (processModules == null) {
                StringBuilder sb = new StringBuilder();
                for (ResourcePath resourcePath : this.resPathList) {
                    sb.append(resourcePath.getResourcePath());
                    sb.append(";");
                }
                processModules = sb.toString();
            }
        }
    }

    private Runnable createRunnableForAppResourceInit(final ResourcePath[] resourcePathArr, Configuration configuration, DeviceCapability deviceCapability, final CountDownLatch countDownLatch, final HashMap<String, CountDownLatch> hashMap) {
        return new Runnable() {
            /* class ohos.global.resource.ResourceManagerImpl.AnonymousClass3 */

            /* JADX WARNING: Code restructure failed: missing block: B:31:0x00f5, code lost:
                r3 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
                ohos.hiviewdfx.HiLog.error(ohos.global.resource.ResourceManagerImpl.LABEL, "init failed with io exception", new java.lang.Object[0]);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x010a, code lost:
                if (r4.getCount() != 0) goto L_0x010c;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x010c, code lost:
                r12 = r4;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:40:0x0119, code lost:
                if (r4.getCount() != 0) goto L_0x011b;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:41:0x011b, code lost:
                r4.countDown();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:42:0x0120, code lost:
                throw r3;
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:32:0x00f7 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                // Method dump skipped, instructions count: 292
                */
                throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.ResourceManagerImpl.AnonymousClass3.run():void");
            }
        };
    }

    public boolean initSys(Configuration configuration, DeviceCapability deviceCapability) throws IOException {
        HiLog.debug(LABEL, "enter InitSys", new Object[0]);
        synchronized (this.LOCK) {
            if (!setupManager(configuration, deviceCapability)) {
                return false;
            }
            initSystemResouce();
            this.cacheMap.clear();
            return true;
        }
    }

    public List<ResourcePath> getResourcePath() {
        ArrayList arrayList = new ArrayList(this.resPathList.size());
        for (ResourcePath resourcePath : this.resPathList) {
            ResourcePath resourcePath2 = new ResourcePath();
            resourcePath2.setResourcePath(resourcePath.getResourcePath(), resourcePath.getAaName());
            arrayList.add(resourcePath2);
        }
        return arrayList;
    }

    public boolean addResourcePath(ResourcePath resourcePath) throws IOException {
        boolean addResourceInternal;
        if (resourcePath == null || resourcePath.getAaName() == null) {
            HiLog.error(LABEL, "new Resource Path empty", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "add new Resource Path %{public}s", resourcePath.getAaName());
        String aaName = resourcePath.getAaName();
        for (ResourcePath resourcePath2 : this.resPathList) {
            if (aaName.equals(resourcePath2.getAaName())) {
                return true;
            }
        }
        synchronized (this.LOCK) {
            addResourceInternal = addResourceInternal(resourcePath, false);
            if (addResourceInternal) {
                this.resPathList.add(resourcePath);
                List<String> overlayPaths = resourcePath.getOverlayPaths();
                if (overlayPaths == null || overlayPaths.size() <= 0) {
                    loadOverlays(resourcePath.getResourcePath(), false);
                } else {
                    this.resPackage.addOverlays(false, overlayPaths);
                    nativeAddOverlay(this.handle, resourcePath.getResourcePath(), (String[]) overlayPaths.toArray(new String[0]));
                }
            } else {
                HiLog.error(LABEL, "nativeAddIndex failed", new Object[0]);
            }
        }
        return addResourceInternal;
    }

    @Override // ohos.global.resource.ResourceManager
    public ResourceImpl getResource(int i) throws NotExistException, IOException {
        long j;
        long nativeGetResource;
        synchronized (this.LOCK) {
            j = this.handle;
            if ((-16777216 & i) == 117440512) {
                j = this.sysHandle;
            }
            nativeGetResource = nativeGetResource(j, i);
        }
        if (nativeGetResource == 0) {
            return null;
        }
        return new ResourceImpl(this.resPackage, nativeGetResource, this.onlineTheme, j == this.sysHandle);
    }

    @Override // ohos.global.resource.ResourceManager
    public String getIdentifier(int i) throws NotExistException, IOException {
        String nativeGetIdentifier;
        synchronized (this.LOCK) {
            long j = this.handle;
            if ((-16777216 & i) == 117440512) {
                j = this.sysHandle;
            }
            nativeGetIdentifier = nativeGetIdentifier(j, i);
        }
        return nativeGetIdentifier;
    }

    private String getString(int i) throws NotExistException, IOException, WrongTypeException {
        String nativeGetString;
        synchronized (this.LOCK) {
            long j = this.handle;
            if ((-16777216 & i) == 117440512) {
                j = this.sysHandle;
            }
            nativeGetString = nativeGetString(j, i);
        }
        return tryDereferrence(nativeGetString);
    }

    @Override // ohos.global.resource.ResourceManager
    @Deprecated
    public Theme getTheme(int i) throws NotExistException, IOException, WrongTypeException {
        String[] stringArray = getElement(i).getStringArray();
        Stack stack = new Stack();
        if (stringArray == null || stringArray.length == 0) {
            return new ThemeImpl(new HashMap());
        }
        stack.push(stringArray);
        while (true) {
            String[] strArr = (String[]) stack.peek();
            if (strArr == null || strArr.length <= 0) {
                break;
            }
            String str = "@ohos:theme/";
            if (!(strArr[0].startsWith("@theme/") || strArr[0].startsWith(str))) {
                break;
            }
            if (strArr[0].startsWith("@theme/")) {
                str = "@theme/";
            }
            try {
                String[] stringArray2 = getElement(Integer.parseInt(strArr[0].substring(str.length()))).getStringArray();
                if (stringArray2 != null) {
                    if (stringArray2.length == 0) {
                        break;
                    }
                    stack.push(stringArray2);
                    if (stack.size() <= 0) {
                        break;
                    }
                } else {
                    break;
                }
            } catch (NumberFormatException unused) {
                HiLog.error(LABEL, "Theme reference invalid", new Object[0]);
            }
        }
        HashMap hashMap = new HashMap();
        do {
            String[] strArr2 = (String[]) stack.pop();
            for (int i2 = (strArr2.length <= 0 || !strArr2[0].startsWith("@theme/")) ? 0 : 1; i2 < strArr2.length - 1; i2 += 2) {
                hashMap.put(strArr2[i2], new TypedAttributeImpl(this, strArr2[i2], strArr2[i2 + 1]));
            }
        } while (stack.size() != 0);
        return new ThemeImpl(hashMap);
    }

    @Override // ohos.global.resource.ResourceManager
    public String getMediaPath(int i) throws NotExistException, IOException, WrongTypeException {
        String string = getString(i);
        if ((i & -16777216) != 117440512) {
            return string;
        }
        return Package.SYS_RESOURCE_PREFIX + string;
    }

    @Override // ohos.global.resource.ResourceManager
    public SolidXmllmpl getSolidXml(int i) throws NotExistException, IOException, WrongTypeException {
        if (this.cacheMap.containsKey(Integer.valueOf(i))) {
            Object obj = this.cacheMap.get(Integer.valueOf(i));
            if (obj instanceof SolidXmllmpl) {
                return (SolidXmllmpl) obj;
            }
        }
        String string = getString(i);
        if (string != null) {
            if ((-16777216 & i) == 117440512) {
                string = Package.SYS_RESOURCE_PREFIX + string;
            }
            SolidXmllmpl solidXmllmpl = new SolidXmllmpl(this, this.resPackage, string);
            this.cacheMap.put(Integer.valueOf(i), solidXmllmpl);
            return solidXmllmpl;
        }
        throw new NotExistException("the resId is not exsit, resId is " + String.valueOf(i));
    }

    private boolean hasExtension(String str) {
        int lastIndexOf = str.lastIndexOf(File.separatorChar);
        if (lastIndexOf != -1) {
            str = str.substring(lastIndexOf + 1);
        }
        if (str.lastIndexOf(46) != -1) {
            return true;
        }
        return false;
    }

    private boolean hasFile(String str) {
        try {
            InputStream open = this.resPackage.open(str);
            if (open != null) {
                $closeResource(null, open);
                return true;
            }
            if (open != null) {
                $closeResource(null, open);
            }
            return false;
        } catch (IOException unused) {
            HiLog.error(LABEL, "hasFile failed, try next moduler", new Object[0]);
        }
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    private boolean hasDirectory(String str) {
        try {
            if (this.resPackage.list(str).length > 0) {
                return true;
            }
            return false;
        } catch (IOException unused) {
            HiLog.error(LABEL, "hasDirectory failed, try next moduler", new Object[0]);
        }
    }

    @Override // ohos.global.resource.ResourceManager
    public RawFileEntry getRawFileEntry(String str) {
        if (str == null) {
            return new RawFileEntryImpl(this.resPackage, str);
        }
        boolean startsWith = str.startsWith(Package.SYS_RESOURCE_PREFIX);
        ArrayList<String> arrayList = new ArrayList(this.resPathList.size() + 1);
        arrayList.add(str);
        if (!startsWith) {
            for (ResourcePath resourcePath : this.resPathList) {
                String aaName = resourcePath.getAaName();
                if (aaName != null && aaName.length() > 0) {
                    arrayList.add(aaName + File.separator + str);
                }
            }
        }
        boolean hasExtension = hasExtension(str);
        for (String str2 : arrayList) {
            if ((hasExtension && hasFile(str2)) || (!hasExtension && hasDirectory(str2))) {
                return new RawFileEntryImpl(this.resPackage, str2);
            }
        }
        return new RawFileEntryImpl(this.resPackage, str);
    }

    @Override // ohos.global.resource.ResourceManager
    public ConfigManager getConfigManager() {
        ConfigManagerImpl configManagerImpl = this.configManager;
        if (configManagerImpl != null) {
            return configManagerImpl;
        }
        HiLog.error(LABEL, "configManager is null, pls call the function of init", new Object[0]);
        return null;
    }

    @Override // ohos.global.resource.ResourceManager
    public Configuration getConfiguration() {
        Configuration configuration;
        synchronized (this.LOCK) {
            configuration = new Configuration(this.resConfig);
        }
        return configuration;
    }

    @Override // ohos.global.resource.ResourceManager
    public DeviceCapability getDeviceCapability() {
        DeviceCapability deviceCapability;
        synchronized (this.LOCK) {
            deviceCapability = new DeviceCapability(this.resCapability);
        }
        return deviceCapability;
    }

    @Override // ohos.global.resource.ResourceManager
    public void updateConfiguration(Configuration configuration, DeviceCapability deviceCapability) {
        synchronized (this.LOCK) {
            this.cacheMap.clear();
            this.elementCache.clear();
            initConfigInner(configuration, deviceCapability);
            computePreferedLocale(this.handle, false);
            computePreferedLocale(this.sysHandle, true);
        }
        synchronized (PLURALRULES_LOCK) {
            this.pluralRules = null;
        }
    }

    public void release() {
        synchronized (this.LOCK) {
            if (this.handle != 0) {
                nativeRelease(this.handle);
                this.handle = 0;
            }
            if (this.sysHandle != 0) {
                nativeRelease(this.sysHandle);
                this.sysHandle = 0;
            }
            if (this.onlineTheme != null) {
                try {
                    this.onlineTheme.close();
                } catch (IOException unused) {
                    HiLog.error(LABEL, "online theme close failed", new Object[0]);
                }
                this.onlineTheme = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        release();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0089, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x008a, code lost:
        if (r6 != null) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x008c, code lost:
        $closeResource(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x008f, code lost:
        throw r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean addResourceInternal(ohos.global.resource.ResourcePath r12, boolean r13) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 160
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.ResourceManagerImpl.addResourceInternal(ohos.global.resource.ResourcePath, boolean):boolean");
    }

    public float parseFloat(String str) throws WrongTypeException {
        String str2;
        int i;
        if (str.length() <= 2 || Character.isDigit(str.charAt(str.length() - 1)) || Character.isDigit(str.charAt(str.length() - 2))) {
            str2 = "";
        } else {
            String substring = str.substring(0, str.length() - 2);
            str2 = str.substring(str.length() - 2);
            str = substring;
        }
        try {
            float parseFloat = Float.parseFloat(str);
            if (this.resCapability.screenDensity / 160 > 0) {
                i = this.resCapability.screenDensity / 160;
            } else {
                i = ResourceUtils.getDensity();
            }
            if (VIRTUAL_PIXEL.equals(str2) || DENSITY_INDEPENDENT_PIXEL.equals(str2)) {
                return parseFloat * ((float) i);
            }
            if (FONT_SIZE_PIXEL.equals(str2) || SCALE_INDEPENDENT_PIXEL.equals(str2)) {
                return parseFloat * ((float) i) * (this.resConfig.fontRatio != 0.0f ? this.resConfig.fontRatio : 1.0f);
            } else if (PIXEL_UNIT.equals(str2) || "".equals(str2)) {
                return parseFloat;
            } else {
                throw new WrongTypeException("float unit not correct.");
            }
        } catch (NumberFormatException unused) {
            throw new WrongTypeException("float format not correct.");
        }
    }

    /* access modifiers changed from: package-private */
    public String tryDereferrence1(String str) throws WrongTypeException {
        String substring;
        if (!(str == null || str.length() == 0 || !(str.startsWith("@") || str.startsWith("$")))) {
            int length = REFERENCE_PREFIX[0].length;
            for (int i = 0; i < length; i++) {
                if (str.startsWith(REFERENCE_PREFIX[0][i])) {
                    substring = str.substring(REFERENCE_PREFIX[0][i].length());
                } else if (str.startsWith(REFERENCE_PREFIX[1][i])) {
                    substring = str.substring(REFERENCE_PREFIX[1][i].length());
                } else if (str.startsWith(HARMONY_REFERENCE_PREFIX[0][i])) {
                    substring = str.substring(HARMONY_REFERENCE_PREFIX[0][i].length());
                } else if (str.startsWith(HARMONY_REFERENCE_PREFIX[1][i])) {
                    substring = str.substring(HARMONY_REFERENCE_PREFIX[1][i].length());
                }
                try {
                    Integer valueOf = Integer.valueOf(Integer.parseInt(substring));
                    synchronized (this.LOCK) {
                        Object nativeGetElement = nativeGetElement((valueOf.intValue() & -16777216) == 117440512 ? this.sysHandle : this.handle, valueOf.intValue());
                        if (nativeGetElement instanceof String) {
                            return (String) nativeGetElement;
                        }
                        throw new WrongTypeException("dereferrence error");
                    }
                } catch (NumberFormatException unused) {
                    HiLog.error(LABEL, "dereferrence format error", new Object[0]);
                }
            }
        }
        return str;
    }

    private String tryDereferrence(String str) {
        String substring;
        String nativeGetString;
        if (!(str == null || str.length() == 0 || !(str.startsWith("@") || str.startsWith("$")))) {
            int length = REFERENCE_PREFIX[0].length;
            for (int i = 0; i < length; i++) {
                if (str.startsWith(REFERENCE_PREFIX[0][i])) {
                    substring = str.substring(REFERENCE_PREFIX[0][i].length());
                } else if (str.startsWith(REFERENCE_PREFIX[1][i])) {
                    substring = str.substring(REFERENCE_PREFIX[1][i].length());
                } else if (str.startsWith(HARMONY_REFERENCE_PREFIX[0][i])) {
                    substring = str.substring(HARMONY_REFERENCE_PREFIX[0][i].length());
                } else if (str.startsWith(HARMONY_REFERENCE_PREFIX[1][i])) {
                    substring = str.substring(HARMONY_REFERENCE_PREFIX[1][i].length());
                }
                try {
                    Integer valueOf = Integer.valueOf(Integer.parseInt(substring));
                    synchronized (this.LOCK) {
                        nativeGetString = nativeGetString((valueOf.intValue() & -16777216) == 117440512 ? this.sysHandle : this.handle, valueOf.intValue());
                    }
                    return nativeGetString;
                } catch (NumberFormatException unused) {
                    HiLog.error(LABEL, "dereferrence format error", new Object[0]);
                }
            }
        }
        return str;
    }

    private void initConfigInner(Configuration configuration, DeviceCapability deviceCapability) {
        if (configuration == null) {
            HiLog.warn(LABEL, "ResourceManager Init, config is null, use default config", new Object[0]);
            this.resConfig = new Configuration();
            modifyColorMode();
            this.resConfig.setLocaleProfile(new LocaleProfile(new Locale[]{Locale.getDefault()}));
        } else {
            this.resConfig = new Configuration(configuration);
            modifyColorMode();
            if (configuration.getFirstLocale() == null) {
                this.resConfig.setLocaleProfile(new LocaleProfile(new Locale[]{Locale.getDefault()}));
            }
        }
        if (deviceCapability == null) {
            this.resCapability = new DeviceCapability();
        } else {
            this.resCapability = new DeviceCapability(deviceCapability);
        }
    }

    private void modifyColorMode() {
        if (this.resConfig.colorMode == 0) {
            this.resConfig.realColorMode = 0;
        } else if (this.resConfig.colorMode == 1) {
            this.resConfig.realColorMode = 1;
        }
    }

    private boolean setupManager(Configuration configuration, DeviceCapability deviceCapability) {
        initConfigInner(configuration, deviceCapability);
        int[] iArr = new int[6];
        iArr[Configuration.Position.ORIENTATION_POS.getValue()] = this.resConfig.direction;
        iArr[Configuration.Position.RESOLUTION_POS.getValue()] = this.resCapability.screenDensity;
        iArr[Configuration.Position.DEVICETYPE_POS.getValue()] = this.resCapability.deviceType;
        iArr[Configuration.Position.NIGHTMODE_POS.getValue()] = this.resConfig.realColorMode;
        iArr[Configuration.Position.MCC_POS.getValue()] = this.resConfig.mcc;
        iArr[Configuration.Position.MNC_POS.getValue()] = this.resConfig.mnc;
        this.handle = nativeSetup(this.resConfig.getFirstLocale(), iArr);
        if (this.handle == 0) {
            HiLog.error(LABEL, "handle failed", new Object[0]);
            return false;
        }
        this.sysHandle = nativeSetup(this.resConfig.getFirstLocale(), iArr);
        if (this.sysHandle != 0) {
            return true;
        }
        HiLog.error(LABEL, "handle failed", new Object[0]);
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0078, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0079, code lost:
        if (r0 != null) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x007b, code lost:
        $closeResource(r4, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x007e, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initSystemResouce() {
        /*
        // Method dump skipped, instructions count: 145
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.ResourceManagerImpl.initSystemResouce():void");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadOverlays(String str, boolean z) {
        if (str != null) {
            ArrayList arrayList = new ArrayList();
            String replace = new File(str).getName().replace(".hap", ".overlay.hap");
            for (String str2 : new String[]{"/system/emui/base/overlay/", "/hw_product/overlay/"}) {
                File file = new File(str2 + replace);
                if (file.exists()) {
                    arrayList.add(file.getPath());
                }
            }
            if (arrayList.size() > 0) {
                nativeAddOverlay(z ? this.sysHandle : this.handle, str, (String[]) arrayList.toArray(new String[0]));
                this.resPackage.addOverlays(z, arrayList);
            }
        }
    }

    @Override // ohos.global.resource.ResourceManager
    public Element getElement(int i) throws NotExistException, IOException, WrongTypeException {
        Object nativeGetElement;
        if (this.elementCache.containsKey(Integer.valueOf(i))) {
            return this.elementCache.get(Integer.valueOf(i));
        }
        synchronized (this.LOCK) {
            long j = this.handle;
            if ((-16777216 & i) == 117440512) {
                j = this.sysHandle;
            }
            nativeGetElement = nativeGetElement(j, i);
        }
        ElementImpl elementImpl = new ElementImpl(nativeGetElement, this);
        if (nativeGetElement instanceof String[]) {
            this.elementCache.put(Integer.valueOf(i), elementImpl);
        }
        return elementImpl;
    }

    public static ArrayList<String> findValidAndSort(String str, List<String> list) throws LocaleFallBackException {
        if (list == null || str == null) {
            return new ArrayList<>();
        }
        String[] nativeFindValidAndSort = nativeFindValidAndSort(str, (String[]) list.toArray(new String[0]));
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str2 : nativeFindValidAndSort) {
            arrayList.add(str2);
        }
        return arrayList;
    }

    public Object getAAssetManager() {
        return this.resPackage.getAAssetManager();
    }

    @Override // ohos.global.resource.ResourceManager
    public ohos.global.resource.solidxml.Pattern createPattern(List<TypedAttribute.AttrData> list) {
        if (list == null || list.size() == 0) {
            return new PatternImpl(new HashMap());
        }
        HashMap hashMap = new HashMap(list.size());
        for (TypedAttribute.AttrData attrData : list) {
            hashMap.put(attrData.getAttrName(), new TypedAttributeImpl(this, attrData.getAttrName(), attrData.getAttrValue()));
        }
        return new PatternImpl(hashMap);
    }

    @Override // ohos.global.resource.ResourceManager
    public Theme createTheme(List<TypedAttribute.AttrData> list) {
        if (list == null || list.size() == 0) {
            return new ThemeImpl(new HashMap());
        }
        HashMap hashMap = new HashMap(list.size());
        for (TypedAttribute.AttrData attrData : list) {
            hashMap.put(attrData.getAttrName(), new TypedAttributeImpl(this, attrData.getAttrName(), attrData.getAttrValue()));
        }
        return new ThemeImpl(hashMap);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void computePreferedLocale(long j, boolean z) {
        Locale[] locales = this.resConfig.getLocaleProfile().getLocales();
        int length = locales.length;
        int i = 0;
        if (length == 0) {
            locales = new Locale[]{Locale.getDefault()};
            length = 1;
        }
        String[] strArr = new String[length];
        for (int i2 = 0; i2 < length; i2++) {
            strArr[i2] = locales[i2].toLanguageTag();
        }
        int[] iArr = new int[6];
        iArr[Configuration.Position.ORIENTATION_POS.getValue()] = this.resConfig.direction;
        iArr[Configuration.Position.RESOLUTION_POS.getValue()] = this.resCapability.screenDensity;
        iArr[Configuration.Position.DEVICETYPE_POS.getValue()] = this.resCapability.deviceType;
        iArr[Configuration.Position.NIGHTMODE_POS.getValue()] = this.resConfig.realColorMode;
        iArr[Configuration.Position.MCC_POS.getValue()] = this.resConfig.mcc;
        iArr[Configuration.Position.MNC_POS.getValue()] = this.resConfig.mnc;
        int size = this.aceResLocales.size();
        String[] strArr2 = (String[]) this.aceResLocales.toArray(new String[0]);
        if (z) {
            strArr2 = new String[0];
        }
        int nativeGetBestLocale = nativeGetBestLocale(j, strArr, iArr, strArr2);
        if (z) {
            return;
        }
        if (nativeGetBestLocale != 0 || (nativeGetBestLocale == 0 && size > 0)) {
            Locale locale = locales[nativeGetBestLocale];
            while (nativeGetBestLocale > 0) {
                locales[nativeGetBestLocale] = locales[nativeGetBestLocale - 1];
                nativeGetBestLocale--;
            }
            locales[0] = locale;
            this.resConfig.setLocaleProfile(new LocaleProfile(locales));
            synchronized (MODULES_LOCK) {
                if (processModules != null && this.resPathList.size() > 0 && processModules.contains(this.resPathList.get(0).getResourcePath())) {
                    Locale.setDefault(locale);
                }
            }
            String language = locale.getLanguage();
            boolean z2 = false;
            while (true) {
                String[] strArr3 = RTL_LANGUAGES;
                if (i < strArr3.length) {
                    if (language.equals(strArr3[i])) {
                        z2 = true;
                    }
                    i++;
                } else {
                    this.resConfig.isLayoutRTL = z2;
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initAceResources(ResourcePath resourcePath) {
        String jSModuleName = getJSModuleName(resourcePath);
        if (!"".equals(jSModuleName)) {
            try {
                String[] list = this.resPackage.list("js/" + jSModuleName + "/i18n");
                int length = list.length;
                for (int i = 0; i < length; i++) {
                    String str = list[i];
                    String str2 = null;
                    if (str.endsWith(".json")) {
                        str2 = str.split("\\.json")[0];
                    }
                    if (str2 != null && !this.aceResLocales.contains(str2)) {
                        this.aceResLocales.add(str2);
                    }
                }
            } catch (IOException unused) {
                HiLog.error(LABEL, "Get ace resource locale list error.", new Object[0]);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x008b, code lost:
        $closeResource(r7, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x008e, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x008f, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.global.resource.ResourceManagerImpl.LABEL, "Exception occurs while getting js module.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A[ExcHandler: IOException | IllegalStateException | SecurityException (unused java.lang.Throwable), SYNTHETIC, Splitter:B:14:0x0031] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getJSModuleName(ohos.global.resource.ResourcePath r8) {
        /*
        // Method dump skipped, instructions count: 153
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.ResourceManagerImpl.getJSModuleName(ohos.global.resource.ResourcePath):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    public void initOnlineThemeColor(boolean z) {
        HashMap<String, String> themeColors;
        OnlineTheme onlineTheme2 = this.onlineTheme;
        if (!(onlineTheme2 == null || (themeColors = onlineTheme2.getThemeColors(z)) == null || themeColors.size() == 0)) {
            String[] strArr = new String[themeColors.size()];
            String[] strArr2 = new String[themeColors.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : themeColors.entrySet()) {
                strArr[i] = entry.getKey();
                strArr2[i] = entry.getValue();
                i++;
            }
            nativeInitThemeColor(z ? this.sysHandle : this.handle, strArr, strArr2);
        }
    }

    public List<Long> getManagerHandle() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.LOCK) {
            arrayList.add(Long.valueOf(this.handle));
            arrayList.add(Long.valueOf(this.sysHandle));
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public PluralRules getPluralRules() {
        PluralRules pluralRules2;
        synchronized (PLURALRULES_LOCK) {
            if (this.pluralRules == null) {
                this.pluralRules = PluralRules.forLocale(this.resConfig.getFirstLocale());
            }
            pluralRules2 = this.pluralRules;
        }
        return pluralRules2;
    }
}
