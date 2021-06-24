package ohos.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.Application;
import android.app.Service;
import android.app.UriGrantsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.ability.IDataAbility;
import ohos.aafwk.ability.LocalRemoteObject;
import ohos.aafwk.ability.MissionInformation;
import ohos.aafwk.ability.startsetting.AbilityStartSetting;
import ohos.aafwk.content.Intent;
import ohos.abilityshell.AbilityShellData;
import ohos.abilityshell.ApplicationDataAbility;
import ohos.abilityshell.BundleMgrBridge;
import ohos.abilityshell.DataAbilityCallback;
import ohos.abilityshell.DistributedConnection;
import ohos.abilityshell.DistributedImpl;
import ohos.abilityshell.HarmonyResources;
import ohos.abilityshell.RemoteFreeInstallCallback;
import ohos.abilityshell.utils.AbilityResolver;
import ohos.abilityshell.utils.AbilityResolverSingleDevice;
import ohos.abilityshell.utils.AbilityShellConverterUtils;
import ohos.abilityshell.utils.AbilityStartSettingConverter;
import ohos.abilityshell.utils.IntentConverter;
import ohos.abilityshell.utils.SelectAbilityUtils;
import ohos.agp.components.LayoutScatter;
import ohos.annotation.SystemApi;
import ohos.app.dispatcher.SpecDispatcherConfig;
import ohos.app.dispatcher.SpecTaskDispatcher;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.TaskDispatcherContext;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.app.dispatcher.threading.AndroidTaskLooper;
import ohos.app.dispatcher.threading.TaskLooper;
import ohos.appexecfwk.utils.AppLog;
import ohos.appexecfwk.utils.FileUtils;
import ohos.appexecfwk.utils.HiViewUtil;
import ohos.appexecfwk.utils.JLogUtil;
import ohos.appexecfwk.utils.StringUtils;
import ohos.bundle.AbilityInfo;
import ohos.bundle.ApplicationInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.BundleManager;
import ohos.bundle.ElementName;
import ohos.bundle.HapModuleInfo;
import ohos.bundle.IBundleManager;
import ohos.bundle.InstallerCallback;
import ohos.bundle.ShellInfo;
import ohos.data.dataability.RemoteDataAbilityProxy;
import ohos.data.distributed.file.DistributedFileManager;
import ohos.data.distributed.file.DistributedFileManagerImpl;
import ohos.devtools.JLogConstants;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.innerkit.asset.Package;
import ohos.global.resource.Element;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.ResourceManagerInner;
import ohos.global.resource.ResourcePath;
import ohos.global.resource.ResourceUtils;
import ohos.global.resource.WrongTypeException;
import ohos.global.resource.solidxml.Pattern;
import ohos.global.resource.solidxml.Theme;
import ohos.hiviewdfx.HiLogLabel;
import ohos.hiviewdfx.HiTrace;
import ohos.hiviewdfx.HiTraceId;
import ohos.media.image.inner.ImageDoubleFwConverter;
import ohos.net.UriConverter;
import ohos.os.ProcessManager;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IPCSkeleton;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.security.permission.PermissionConversion;
import ohos.security.permission.PermissionInner;
import ohos.security.permission.UriPermissionDef;
import ohos.security.permissionkitinner.PermissionKitInner;
import ohos.tools.C0000Bytrace;
import ohos.utils.net.Uri;

public class ContextDeal implements Context, IAbilityLoader {
    private static final HiLogLabel APPKIT_LABEL = new HiLogLabel(3, 218108160, "AppKit");
    private static final String CALLER_BUNDLE_NAME = "callerBundleName";
    private static final Object CREATE_FILE_LOCK = new Object();
    private static final String DATABASE_DIRECTORY_NAME = "databases";
    private static final int DEFAULT_REQUEST_CODE = 0;
    private static final String DIRECTORY_DATA_DATA = "/data/data/";
    private static final String DIRECTORY_DATA_USER = "/data/user/";
    private static final String DISTRIBUTE_DATABASE_DIRECTORY_NAME = "distribute_databases";
    private static final int ERROR_TYPR_OF_ABILITY = -10;
    private static final int FERR_INSTALL_QUERY_ABILITY_RESULT_SIZE = 1;
    private static final int FREE_INSTALL_ERROR_CODE_FA_DISPATCHER_FAILED = 5;
    private static final int GET_REMOTE_DATA_ABILITY_TIMEOUT = 2000;
    private static final int INSTALLER_CALLBACK_DOWNLOAD_FAILURE = 12;
    private static final int INVALID_REQUEST_CODE = -1;
    private static final int INVALID_RESOURCE_VALUE = -1;
    private static final String KEY_START_TIME = "startTime";
    private static final String LIBS = "!/libs/";
    private static final String NULL_DEVICE_ID = "";
    private static final int PER_USER_RANGE = 100000;
    private static final String PREFERENCES_DIRECTORY_NAME = "preferences";
    private static final String RESOURCE_MANAGER_CONFIGURATION = "Configuration";
    private static final String RESOURCE_MANAGER_DEVICECAPABILITY = "DeviceCapability";
    private static final String TRANSITION_ANIMATION_SUFFIX = ".resource.ResourceTable";
    private static final String UNKNOWN_NAME = "unknown";
    private static final int URI_LOCAL = 0;
    private static final int USER_SYSTEM = 0;
    private static final String WEB_ABILITY_DEEPLINK_SCHEME = "hwfastapp://";
    private static boolean isInitResouceDone = false;
    private static CountDownLatch resourceLatch = new CountDownLatch(1);
    private final HashMap<IAbilityConnection, Object> abilityConnectionMap = new HashMap<>();
    private AbilityInfo abilityInfo;
    private AbilityManager abilityManager = new AbilityManager(new AbilityManagerImpl(this));
    private Context abilityShellContext;
    private volatile Activity activityContext = null;
    private Application application;
    private BundleMgrBridge bundleMgrImpl = new BundleMgrBridge();
    private ClassLoader classLoader;
    private String databasePath;
    private DistributedFileManager distributedFileManager;
    private final DistributedImpl distributedImpl = new DistributedImpl();
    private String distributedPath;
    private Element element;
    private final CountDownLatch getRemoteDataAbilityLatch = new CountDownLatch(1);
    private HapModuleInfo hapModuleInfo;
    private boolean isDatabasePathExits = false;
    private boolean isDistributedPathExits = false;
    private boolean isPreferencePathExists = false;
    private LayoutScatter layoutScatter;
    private int mFlags = 16;
    private TaskLooper mainLooper;
    private TaskDispatcher mainTaskDispatcher;
    private Pattern pPattern;
    private Theme pTheme;
    private String preferencePath;
    private ResourceManager resourceManager;
    private ResourceManagerInner resourceManagerInner;
    private int resourceState = 0;

    private boolean isFlagExists(int i, int i2) {
        return (i & i2) == i;
    }

    private boolean isRequestCodeValid(int i) {
        return i != -1;
    }

    @Override // ohos.app.IAbilityLoader
    public void onLoadAbility() {
    }

    private void resetPathInit() {
        synchronized (CREATE_FILE_LOCK) {
            this.databasePath = null;
            this.isDatabasePathExits = false;
            this.distributedPath = null;
            this.isDistributedPathExits = false;
            this.preferencePath = null;
            this.isPreferencePathExists = false;
        }
    }

    public ContextDeal(Context context, ClassLoader classLoader2) {
        this.abilityShellContext = context;
        if (context instanceof Activity) {
            this.activityContext = (Activity) this.abilityShellContext;
        }
        this.classLoader = classLoader2;
    }

    @Override // ohos.app.Context
    public Object getHarmonyosApp() {
        Application application2 = this.application;
        if (application2 != null) {
            return application2.getHarmonyosApplication();
        }
        return null;
    }

    @Override // ohos.app.Context
    public Object getHarmonyAbilityPkg(AbilityInfo abilityInfo2) {
        if (this.application == null || abilityInfo2 == null || abilityInfo2.getModuleName() == null) {
            return null;
        }
        return this.application.getAbilityPackage(abilityInfo2.getModuleName());
    }

    public void setApplication(Application application2) {
        this.application = application2;
    }

    public void setAbilityInfo(AbilityInfo abilityInfo2) {
        this.abilityInfo = abilityInfo2;
    }

    public void setHapModuleInfo(HapModuleInfo hapModuleInfo2) {
        this.hapModuleInfo = hapModuleInfo2;
    }

    public void setMainLooper(TaskLooper taskLooper) {
        this.mainLooper = taskLooper;
    }

    public void setResourceManagerInner(ResourceManagerInner resourceManagerInner2) {
        this.resourceManagerInner = resourceManagerInner2;
    }

    public void setBundleMgrBridge(BundleMgrBridge bundleMgrBridge) {
        this.bundleMgrImpl = bundleMgrBridge;
    }

    public void setDistributedFileManager(DistributedFileManager distributedFileManager2) {
        synchronized (CREATE_FILE_LOCK) {
            this.distributedFileManager = distributedFileManager2;
        }
    }

    public boolean initApplicationResourceManager(String str) {
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContextDeal initResource");
        boolean initResourceManager = initResourceManager(str, null);
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContextDeal initResource");
        isInitResouceDone = true;
        resourceLatch.countDown();
        return initResourceManager;
    }

    private static void waitforResourceInit() {
        if (!isInitResouceDone) {
            try {
                resourceLatch.await();
            } catch (InterruptedException unused) {
                AppLog.e(APPKIT_LABEL, "init application resouce InterruptedException occur", new Object[0]);
            }
        }
        if (!isInitResouceDone) {
            throw new IllegalStateException("init application resource failed!");
        }
    }

    public TaskDispatcherContext getTaskDispatcherContext() {
        return this.application.getTaskDispatcherContext();
    }

    @Override // ohos.app.Context
    public Context getHostContext() {
        return this.abilityShellContext;
    }

    @Override // ohos.app.Context
    public Context getApplicationContext() {
        return this.application.getContext();
    }

    @Override // ohos.app.Context
    public AbilityInfo getAbilityInfo() {
        return this.abilityInfo;
    }

    @Override // ohos.app.Context
    public ApplicationInfo getApplicationInfo() {
        return this.application.getApplicationInfo();
    }

    @Override // ohos.app.Context
    public ProcessInfo getProcessInfo() {
        return this.application.getProcessInfo();
    }

    @Override // ohos.app.Context
    public ResourceManager getResourceManager() {
        if (this.resourceManager == null) {
            waitforResourceInit();
            if (equals(this.application.getContext())) {
                return this.resourceManager;
            }
            this.resourceManager = this.application.getResourceManager();
        } else if (!initResourceManager(getBundleName(), null)) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::getResourceManager failed, initResourceManager failed", new Object[0]);
            return null;
        }
        return this.resourceManager;
    }

    @Override // ohos.app.Context
    public TaskDispatcher getMainTaskDispatcher() {
        if (this.mainTaskDispatcher == null) {
            if (this.mainLooper != null) {
                this.mainTaskDispatcher = new SpecTaskDispatcher(SpecDispatcherConfig.MAIN, this.mainLooper);
            } else {
                AppLog.w(APPKIT_LABEL, "ContextDeal::getMainTaskDispatcher Cannot create dispatcher due to looper is not set", new Object[0]);
            }
        }
        return this.mainTaskDispatcher;
    }

    @Override // ohos.app.Context
    public final TaskDispatcher getUITaskDispatcher() {
        return getMainTaskDispatcher();
    }

    @Override // ohos.app.Context
    public TaskDispatcher createParallelTaskDispatcher(String str, TaskPriority taskPriority) {
        return this.application.getTaskDispatcherContext().createParallelDispatcher(str, taskPriority);
    }

    @Override // ohos.app.Context
    public TaskDispatcher createSerialTaskDispatcher(String str, TaskPriority taskPriority) {
        return this.application.getTaskDispatcherContext().createSerialDispatcher(str, taskPriority);
    }

    @Override // ohos.app.Context
    public TaskDispatcher getGlobalTaskDispatcher(TaskPriority taskPriority) {
        return this.application.getTaskDispatcherContext().getGlobalTaskDispatcher(taskPriority);
    }

    @Override // ohos.app.Context
    public ClassLoader getClassloader() {
        AppLog.d(APPKIT_LABEL, "ContextDeal::getClassLoader called", new Object[0]);
        return this.classLoader;
    }

    @Override // ohos.app.Context
    public File getPreferencesDir() {
        String preferenceDirectory = getPreferenceDirectory(this.abilityInfo);
        if (preferenceDirectory == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::preferenceDirectory failed, getPreferencesDir failed", new Object[0]);
            return null;
        }
        synchronized (CREATE_FILE_LOCK) {
            if (!this.isPreferencePathExists) {
                this.isPreferencePathExists = FileUtils.createDirectory(preferenceDirectory);
                if (!this.isPreferencePathExists) {
                    AppLog.w(APPKIT_LABEL, "ContextDeal::preferenceDirectory failed, isPreferencePathExists false", new Object[0]);
                    return null;
                }
            }
            return new File(preferenceDirectory);
        }
    }

    @Override // ohos.app.Context
    public File getDatabaseDir() {
        String databaseDirectory = getDatabaseDirectory(this.abilityInfo);
        if (databaseDirectory == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::getDatabaseDir failed, getDatabaseDirectory failed", new Object[0]);
            return null;
        }
        synchronized (CREATE_FILE_LOCK) {
            if (!this.isDatabasePathExits) {
                this.isDatabasePathExits = FileUtils.createDirectory(databaseDirectory);
                if (!this.isDatabasePathExits) {
                    return null;
                }
            }
            return new File(databaseDirectory);
        }
    }

    @Override // ohos.app.Context
    public File getDistributedDir() {
        String distributeDirectory = getDistributeDirectory(this.abilityInfo);
        if (distributeDirectory == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::getDistributedDir failed, getDistributeDirectory failed", new Object[0]);
            return null;
        }
        synchronized (CREATE_FILE_LOCK) {
            if (!this.isDistributedPathExits) {
                this.isDistributedPathExits = FileUtils.createDirectory(distributeDirectory);
                if (!this.isDistributedPathExits) {
                    return null;
                }
            }
            return new File(distributeDirectory);
        }
    }

    @Override // ohos.app.Context
    public IDataAbility getDataAbility(Uri uri) {
        return getDataAbility(uri, false);
    }

    private boolean checkLocalDeviceId(String str) {
        if (str == null || str.isEmpty() || str.equals(this.application.getLocalDeviceId())) {
            return true;
        }
        return false;
    }

    @Override // ohos.app.Context
    public IDataAbility getDataAbility(Uri uri, boolean z) {
        if (checkLocalDeviceId(uri.getDecodedAuthority())) {
            return ApplicationDataAbility.creator(this.abilityShellContext, uri, z);
        }
        DataAbilityCallback dataAbilityCallback = new DataAbilityCallback(this.getRemoteDataAbilityLatch);
        try {
            int remoteDataAbility = this.distributedImpl.getRemoteDataAbility(uri, dataAbilityCallback);
            if (remoteDataAbility != 0) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::getDataAbility getRemoteDataAbility failed %{public}d", Integer.valueOf(remoteDataAbility));
                return null;
            }
            try {
                if (!this.getRemoteDataAbilityLatch.await(2000, TimeUnit.MILLISECONDS)) {
                    AppLog.w(APPKIT_LABEL, "ContextDeal::getDataAbility await exceed time", new Object[0]);
                }
                IRemoteObject remoteDataAbility2 = dataAbilityCallback.getRemoteDataAbility();
                if (remoteDataAbility2 != null) {
                    return new RemoteDataAbilityProxy(remoteDataAbility2);
                }
                AppLog.e(APPKIT_LABEL, "ContextDeal::getDataAbility wait timeout", new Object[0]);
                return null;
            } catch (InterruptedException unused) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::getDataAbility InterruptedException occur", new Object[0]);
                return null;
            }
        } catch (RemoteException unused2) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::getDataAbility RemoteException occur", new Object[0]);
            return null;
        }
    }

    @Override // ohos.app.Context
    public boolean releaseDataAbility(IDataAbility iDataAbility) {
        AppLog.d(APPKIT_LABEL, "ContextDeal::releaseDataAbility called", new Object[0]);
        if (iDataAbility == null) {
            AppLog.e("ContextDeal::releaseDataAbility dataAbility is null", new Object[0]);
            return false;
        }
        iDataAbility.close();
        return true;
    }

    @Override // ohos.app.Context
    public void switchToDeviceEncryptedStorageContext() {
        this.mFlags = (this.mFlags & -17) | 8;
        Context context = this.abilityShellContext;
        if (context != null) {
            try {
                context.createDeviceProtectedStorageContext();
            } catch (NullPointerException unused) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::switch to de error", new Object[0]);
            }
        }
        resetPathInit();
    }

    @Override // ohos.app.Context
    public void switchToCredentialEncryptedStorageContext() {
        this.mFlags = (this.mFlags & -9) | 16;
        Context context = this.abilityShellContext;
        if (context != null) {
            try {
                context.createCredentialProtectedStorageContext();
            } catch (NullPointerException unused) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::switch to ce error", new Object[0]);
            }
        }
        resetPathInit();
    }

    @Override // ohos.app.Context
    public boolean isDeviceEncryptedStorage() {
        return (this.mFlags & 8) != 0;
    }

    @Override // ohos.app.Context
    public boolean isCredentialEncryptedStorage() {
        return (this.mFlags & 16) != 0;
    }

    @Override // ohos.app.Context
    @SystemApi
    public void makePermanentUriPermission(Uri uri, int i) throws DataAbilityRemoteException {
        if (uri != null) {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            ContentResolver contentResolver = this.abilityShellContext.getContentResolver();
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient makePermanentUriPermission");
                UriGrantsManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(convertToAndroidContentUri), i, (String) null, contentResolver.resolveUserId(convertToAndroidContentUri));
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient makePermanentUriPermission");
            } catch (android.os.RemoteException unused) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::makePermanentUriPermission RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ContextDeal makePermanentUriPermission failed");
            }
        } else {
            AppLog.e(APPKIT_LABEL, "ContextDeal::makePermanentUriPermission uri is null", new Object[0]);
            throw new IllegalArgumentException("null uri");
        }
    }

    @Override // ohos.app.Context
    @SystemApi
    public void giveUpPermanentUriPermission(Uri uri, int i) throws DataAbilityRemoteException {
        if (uri != null) {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            ContentResolver contentResolver = this.abilityShellContext.getContentResolver();
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient giveUpPermanentUriPermission");
                UriGrantsManager.getService().releasePersistableUriPermission(ContentProvider.getUriWithoutUserId(convertToAndroidContentUri), i, (String) null, contentResolver.resolveUserId(convertToAndroidContentUri));
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient giveUpPermanentUriPermission");
            } catch (android.os.RemoteException unused) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::giveUpPermanentUriPermission RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ContextDeal giveUpPermanentUriPermission failed");
            }
        } else {
            AppLog.e(APPKIT_LABEL, "ContextDeal::giveUpPermanentUriPermission uri is null", new Object[0]);
            throw new IllegalArgumentException("null uri");
        }
    }

    @Override // ohos.app.Context
    @SystemApi
    public int verifyCallingUriPermission(Uri uri, int i) {
        if (uri != null) {
            return this.abilityShellContext.checkCallingUriPermission(UriConverter.convertToAndroidContentUri(uri), i);
        }
        AppLog.e(APPKIT_LABEL, "ContextDeal::verifyCallingUriPermission uri is null", new Object[0]);
        throw new IllegalArgumentException("null uri");
    }

    @Override // ohos.app.Context
    @SystemApi
    public List<UriPermissionDef> getGrantedPermanentUriPermissionDefs() throws DataAbilityRemoteException {
        try {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContxtDeal getGrantedPermanentUriPermissionDefs");
            List<?> list = UriGrantsManager.getService().getUriPermissions(this.abilityShellContext.getOpPackageName(), true, true).getList();
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContxtDeal getGrantedPermanentUriPermissionDefs");
            return convertUriPermissionToUriPermissionDef(list);
        } catch (android.os.RemoteException unused) {
            AppLog.e(APPKIT_LABEL, "ContxtDeal::getGrantedPermanentUriPermissionDefs RemoteException occur", new Object[0]);
            throw new DataAbilityRemoteException("ContxtDeal getGrantedPermanentUriPermissionDefs failed");
        }
    }

    @Override // ohos.app.Context
    @SystemApi
    public List<UriPermissionDef> getHostedPermanentUriPermissionDefs() throws DataAbilityRemoteException {
        try {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContxtDeal getHostedPermanentUriPermissionDefs");
            List<?> list = UriGrantsManager.getService().getUriPermissions(this.abilityShellContext.getOpPackageName(), false, true).getList();
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContxtDeal getHostedPermanentUriPermissionDefs");
            return convertUriPermissionToUriPermissionDef(list);
        } catch (android.os.RemoteException unused) {
            AppLog.e(APPKIT_LABEL, "ContxtDeal::getHostedPermanentUriPermissionDefs RemoteException occur", new Object[0]);
            throw new DataAbilityRemoteException("ContxtDeal getHostedPermanentUriPermissionDefs failed");
        }
    }

    private List<UriPermissionDef> convertUriPermissionToUriPermissionDef(List<?> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null) {
            return arrayList;
        }
        for (Object obj : list) {
            if (obj instanceof UriPermission) {
                arrayList.add(PermissionKitInner.getInstance().translateUriPermission((UriPermission) obj).get());
            } else {
                AppLog.e(APPKIT_LABEL, "ContxtDeal::convertUriPermission UriPermission checked cast error", new Object[0]);
            }
        }
        return arrayList;
    }

    @Override // ohos.app.Context
    @SystemApi
    public void authUriPermission(String str, Uri uri, int i) {
        if (str == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::authUriPermission targetBundleName is null", new Object[0]);
            throw new IllegalArgumentException("null targetBundleName");
        } else if (uri == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::authUriPermission uri is null", new Object[0]);
            throw new IllegalArgumentException("null uri");
        } else if (i <= 0 || (i & 195) == 0) {
            throw new IllegalArgumentException("Requested flags 0x" + Integer.toHexString(i) + "not allowed ");
        } else {
            this.abilityShellContext.grantUriPermission(str, UriConverter.convertToAndroidContentUri(uri), i);
        }
    }

    @Override // ohos.app.Context
    @SystemApi
    public void unauthUriPermission(String str, Uri uri, int i) {
        if (str == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::unauthUriPermission targetBundleName is null", new Object[0]);
            throw new IllegalArgumentException("null targetBundleName");
        } else if (uri == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::unauthUriPermission uri is null", new Object[0]);
            throw new IllegalArgumentException("null uri");
        } else if (i <= 0 || (i & 195) == 0) {
            throw new IllegalArgumentException("Requested flags 0x" + Integer.toHexString(i) + "not allowed ");
        } else {
            this.abilityShellContext.revokeUriPermission(str, UriConverter.convertToAndroidContentUri(uri), i);
        }
    }

    @Override // ohos.app.Context
    @SystemApi
    public int verifyUriPermission(Uri uri, int i, int i2, int i3) {
        if (uri == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::verifyUriPermission uri is null", new Object[0]);
            throw new IllegalArgumentException("null uri");
        } else if (i < 0) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::verifyUriPermission pid is illegal", new Object[0]);
            return -1;
        } else {
            return this.abilityShellContext.checkUriPermission(UriConverter.convertToAndroidContentUri(uri), i, i2, i3);
        }
    }

    @Override // ohos.app.Context
    public int verifyCallingPermission(String str) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null) {
            return -1;
        }
        String callingDeviceID = IPCSkeleton.getCallingDeviceID();
        int callingPid = IPCSkeleton.getCallingPid();
        int callingUid = IPCSkeleton.getCallingUid();
        if (callingDeviceID == null || callingPid < 0 || callingPid == ProcessManager.getPid()) {
            return -1;
        }
        if (callingDeviceID.isEmpty()) {
            return this.abilityShellContext.checkPermission(aosPermissionNameIfPossible, callingPid, callingUid);
        }
        return PermissionInner.checkDPermission(PermissionInner.allocateDuid(callingDeviceID, callingUid), aosPermissionNameIfPossible);
    }

    @Override // ohos.app.Context
    public int verifySelfPermission(String str) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null) {
            return -1;
        }
        return this.abilityShellContext.checkSelfPermission(aosPermissionNameIfPossible);
    }

    @Override // ohos.app.Context
    public int verifyCallingOrSelfPermission(String str) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null) {
            return -1;
        }
        String callingDeviceID = IPCSkeleton.getCallingDeviceID();
        int callingPid = IPCSkeleton.getCallingPid();
        int callingUid = IPCSkeleton.getCallingUid();
        if (callingDeviceID == null || callingPid < 0) {
            return -1;
        }
        if (!callingDeviceID.isEmpty()) {
            return PermissionInner.checkDPermission(PermissionInner.allocateDuid(callingDeviceID, callingUid), aosPermissionNameIfPossible);
        }
        return this.abilityShellContext.checkPermission(aosPermissionNameIfPossible, callingPid, callingUid);
    }

    @Override // ohos.app.Context
    public int verifyPermission(String str, int i, int i2) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null || i < 0) {
            return -1;
        }
        return this.abilityShellContext.checkPermission(aosPermissionNameIfPossible, i, i2);
    }

    @Override // ohos.app.Context
    public void terminateAbility() {
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            ((Activity) context).finish();
        } else if (context instanceof Service) {
            ((Service) context).stopSelf();
        } else {
            AppLog.w(APPKIT_LABEL, "ContextDeal::terminateAbility only support Activity and Service", new Object[0]);
        }
    }

    @Override // ohos.app.Context
    public void terminateAbility(int i) {
        Context context = this.abilityShellContext;
        if (!(context instanceof Activity)) {
            AppLog.w(APPKIT_LABEL, "ContextDeal::terminateAbility only support Activity", new Object[0]);
        } else {
            ((Activity) context).finishActivity(i);
        }
    }

    @Override // ohos.app.Context
    public void displayUnlockMissionMessage() {
        Context context = this.abilityShellContext;
        if (!(context instanceof Activity)) {
            AppLog.w(APPKIT_LABEL, "ContextDeal::displayUnlockMissionMessage only support Activity", new Object[0]);
        } else {
            ((Activity) context).showLockTaskEscapeMessage();
        }
    }

    @Override // ohos.app.Context
    public void lockMission() {
        Context context = this.abilityShellContext;
        if (!(context instanceof Activity)) {
            AppLog.w(APPKIT_LABEL, "ContextDeal::lockMission only support Activity", new Object[0]);
        } else {
            ((Activity) context).startLockTask();
        }
    }

    @Override // ohos.app.Context
    public void unlockMission() {
        Context context = this.abilityShellContext;
        if (!(context instanceof Activity)) {
            AppLog.w(APPKIT_LABEL, "endLockTask::unlockMission only support Activity", new Object[0]);
        } else {
            ((Activity) context).stopLockTask();
        }
    }

    @Override // ohos.app.Context
    public final boolean terminateAbilityResult(int i) {
        Context context = this.abilityShellContext;
        if (context instanceof Service) {
            return ((Service) context).stopSelfResult(i);
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::terminateAbilityResult only support service", new Object[0]);
        return false;
    }

    @Override // ohos.app.Context
    @SystemApi
    public void terminateAndRemoveMission() {
        Context context = this.abilityShellContext;
        if (!(context instanceof Activity)) {
            AppLog.w(APPKIT_LABEL, "ContextDeal::terminateAndRemoveMission only support Activity", new Object[0]);
        } else {
            ((Activity) context).finishAndRemoveTask();
        }
    }

    @Override // ohos.app.Context
    public String getLocalClassName() {
        AppLog.i(APPKIT_LABEL, "ContextDeal::getLocalClassName called", new Object[0]);
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            return AbilityShellConverterUtils.convertToHarmonyClassName(((Activity) context).getLocalClassName());
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::getLocalClassName only support Activity", new Object[0]);
        return null;
    }

    @Override // ohos.app.Context
    public ElementName getElementName() {
        AppLog.i(APPKIT_LABEL, "ContextDeal::getElementName called", new Object[0]);
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            return convertComponentNameToElementName(((Activity) context).getComponentName());
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::getElementName only support Activity", new Object[0]);
        return null;
    }

    @Override // ohos.app.Context
    public ElementName getCallingAbility() {
        AppLog.i(APPKIT_LABEL, "ContextDeal::getCallingAbility called", new Object[0]);
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            return convertComponentNameToElementName(((Activity) context).getCallingActivity());
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::getCallingAbility only support Activity", new Object[0]);
        return null;
    }

    @Override // ohos.app.Context
    public String getCallingBundle() {
        AppLog.i(APPKIT_LABEL, "ContextDeal::getCallingBundle called", new Object[0]);
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            return ((Activity) context).getCallingPackage();
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::getCallingBundle only support Activity", new Object[0]);
        return null;
    }

    @Override // ohos.app.Context
    public void startAbility(Intent intent, int i) {
        startAbility(intent, i, AbilityStartSetting.getEmptySetting());
    }

    @Override // ohos.app.Context
    public void startAbility(Intent intent, int i, AbilityStartSetting abilityStartSetting) {
        AppLog.i(APPKIT_LABEL, "ContextDeal::startAbility called", new Object[0]);
        if (intent == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::startAbility failed, intent is null", new Object[0]);
        } else if (isFlagExists(2048, intent.getFlags())) {
            startFreeInstallAbility(intent, i, abilityStartSetting, null);
        } else {
            startAbilityInner(intent, i, abilityStartSetting);
        }
    }

    @Override // ohos.app.Context
    public void startAbilities(Intent[] intentArr) {
        for (Intent intent : intentArr) {
            startAbility(intent, 0);
        }
    }

    @Override // ohos.app.Context
    public Context createBundleContext(String str, int i) {
        if (StringUtils.isBlank(str)) {
            AppLog.e("ContextDeal::createBundleContext bundle name is empty", new Object[0]);
            return null;
        } else if (str.equals(getBundleName())) {
            AppLog.i("ContextDeal::createBundleContext bundleName is same as the current application bundle name", new Object[0]);
            return getApplicationContext();
        } else {
            BundleInfo bundleInfo = getBundleInfo(str);
            if (bundleInfo == null || bundleInfo.getAppInfo() == null) {
                AppLog.e("ContextDeal::createBundleContext get bundle info failed", new Object[0]);
                return null;
            }
            String originalName = bundleInfo.getOriginalName();
            if (StringUtils.isBlank(originalName)) {
                AppLog.e("ContextDeal::createBundleContext original name is empty", new Object[0]);
                return null;
            }
            try {
                Context createPackageContext = this.abilityShellContext.createPackageContext(originalName, i);
                if (createPackageContext != null || i == 8) {
                    if (i == 8) {
                        createPackageContext = this.abilityShellContext;
                    }
                    ClassLoader classLoader2 = createPackageContext.getClassLoader();
                    ContextDeal contextDeal = new ContextDeal(createPackageContext, classLoader2);
                    if ((i & 1) == 1) {
                        addHapSource(bundleInfo, classLoader2);
                    }
                    contextDeal.setMainLooper(new AndroidTaskLooper(Looper.getMainLooper()));
                    if (!contextDeal.initResourceManager(str, bundleInfo)) {
                        AppLog.e("ContextDeal::createBundleContext initResourceManager failed", new Object[0]);
                        return null;
                    }
                    Application application2 = new Application();
                    application2.attachBaseContext(contextDeal);
                    application2.setProcessInfo(new ProcessInfo(createPackageContext.getApplicationInfo().processName, Process.myPid()));
                    application2.setApplicationInfo(bundleInfo.getAppInfo());
                    return application2;
                }
                AppLog.e("ContextDeal::createBundleContext failed", new Object[0]);
                return null;
            } catch (PackageManager.NameNotFoundException unused) {
                AppLog.e("ContextDeal::createBundleContext cannot find bundle name", new Object[0]);
                return null;
            }
        }
    }

    @Override // ohos.app.Context
    public boolean stopAbility(Intent intent) {
        return stopAbilityInner(intent);
    }

    @Override // ohos.app.Context
    public boolean connectAbility(Intent intent, IAbilityConnection iAbilityConnection) {
        boolean z;
        HiTraceId begin = HiTrace.begin("connectAbility", 1);
        if (isFlagExists(2048, intent.getFlags())) {
            z = connectFreeInstallAbility(intent, iAbilityConnection);
        } else {
            z = connectAbilityInner(intent, iAbilityConnection);
            if (!z) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::connectAbility called failed", new Object[0]);
            }
        }
        HiTrace.end(begin);
        return z;
    }

    @Override // ohos.app.Context
    public void disconnectAbility(IAbilityConnection iAbilityConnection) {
        HiTraceId begin = HiTrace.begin("disconnectAbility", 1);
        AppLog.i(APPKIT_LABEL, "ContextDeal::disconnectAbility called", new Object[0]);
        disconnectAbilityInner(this.abilityConnectionMap, iAbilityConnection);
        HiTrace.end(begin);
    }

    @Override // ohos.app.Context
    public void setResult(int i, Intent intent) {
        AppLog.d(APPKIT_LABEL, "Context::setResult called", new Object[0]);
        if (!(this.abilityShellContext instanceof Activity)) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::setResult failed ability is not instance of AbilityShellActivity", new Object[0]);
            return;
        }
        android.content.Intent intent2 = null;
        if (intent != null) {
            Optional<android.content.Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, null);
            if (createAndroidIntent.isPresent()) {
                intent2 = createAndroidIntent.get();
            }
        }
        ((Activity) this.abilityShellContext).setResult(i, intent2);
    }

    @Override // ohos.app.Context
    public void setDisplayOrientation(AbilityInfo.DisplayOrientation displayOrientation) {
        AppLog.i(APPKIT_LABEL, "ContextDeal::setDisplayOrientation called", new Object[0]);
        Context context = this.abilityShellContext;
        if (!(context instanceof Activity)) {
            AppLog.w(APPKIT_LABEL, "AbilityShellActivity::setDisplayOrientation only Activity support this operation", new Object[0]);
            return;
        }
        Activity activity = (Activity) context;
        int i = AnonymousClass13.$SwitchMap$ohos$bundle$AbilityInfo$DisplayOrientation[displayOrientation.ordinal()];
        if (i == 1) {
            AppLog.d(APPKIT_LABEL, "ContextDeal::setDisplayOrientation LANDSCAPE", new Object[0]);
            activity.setRequestedOrientation(0);
        } else if (i == 2) {
            AppLog.d(APPKIT_LABEL, "ContextDeal::setDisplayOrientation PORTRAIT", new Object[0]);
            activity.setRequestedOrientation(1);
        } else if (i == 3) {
            AppLog.d(APPKIT_LABEL, "ContextDeal::setDisplayOrientation FOLLOW_RECENT", new Object[0]);
            activity.setRequestedOrientation(3);
        } else if (i == 4) {
            AppLog.d(APPKIT_LABEL, "ContextDeal::setDisplayOrientation UNSPECIFIED", new Object[0]);
            activity.setRequestedOrientation(-1);
        }
    }

    /* renamed from: ohos.app.ContextDeal$13  reason: invalid class name */
    static /* synthetic */ class AnonymousClass13 {
        static final /* synthetic */ int[] $SwitchMap$ohos$bundle$AbilityInfo$DisplayOrientation = new int[AbilityInfo.DisplayOrientation.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.bundle.AbilityInfo$DisplayOrientation[] r0 = ohos.bundle.AbilityInfo.DisplayOrientation.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.app.ContextDeal.AnonymousClass13.$SwitchMap$ohos$bundle$AbilityInfo$DisplayOrientation = r0
                int[] r0 = ohos.app.ContextDeal.AnonymousClass13.$SwitchMap$ohos$bundle$AbilityInfo$DisplayOrientation     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.bundle.AbilityInfo$DisplayOrientation r1 = ohos.bundle.AbilityInfo.DisplayOrientation.LANDSCAPE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.app.ContextDeal.AnonymousClass13.$SwitchMap$ohos$bundle$AbilityInfo$DisplayOrientation     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.bundle.AbilityInfo$DisplayOrientation r1 = ohos.bundle.AbilityInfo.DisplayOrientation.PORTRAIT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.app.ContextDeal.AnonymousClass13.$SwitchMap$ohos$bundle$AbilityInfo$DisplayOrientation     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.bundle.AbilityInfo$DisplayOrientation r1 = ohos.bundle.AbilityInfo.DisplayOrientation.FOLLOWRECENT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.app.ContextDeal.AnonymousClass13.$SwitchMap$ohos$bundle$AbilityInfo$DisplayOrientation     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.bundle.AbilityInfo$DisplayOrientation r1 = ohos.bundle.AbilityInfo.DisplayOrientation.UNSPECIFIED     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.app.ContextDeal.AnonymousClass13.<clinit>():void");
        }
    }

    @Override // ohos.app.Context
    public boolean canRequestPermission(String str) {
        AppLog.i(APPKIT_LABEL, "Context::canRequestPermission called permission: %{private}s", str);
        if (!(this.abilityShellContext instanceof Activity)) {
            AppLog.w(APPKIT_LABEL, "ContextDeal::canRequestPermission can't requestPermission,ability is not instance of AbilityShellActivity", new Object[0]);
            return false;
        }
        String bundleName = getBundleName();
        int uid = getUid() / 100000;
        if (bundleName != null && uid != -1) {
            return PermissionKitInner.getInstance().canRequestPermission(str, bundleName, uid);
        }
        AppLog.e(APPKIT_LABEL, "ContextDeal::canRequestPermission false, bundleName %{private}s or userId %{private}d is invalid", bundleName, Integer.valueOf(uid));
        return false;
    }

    @Override // ohos.app.Context
    public void requestPermissionsFromUser(String[] strArr, int i) {
        AppLog.d("Context::requestPermissionsFromUser called", new Object[0]);
        if (!(this.abilityShellContext instanceof Activity)) {
            AppLog.e("ContextDeal::requestPermissionsFromUser failed, ability is not AbilityShellActivity", new Object[0]);
        } else if (strArr == null) {
            AppLog.e("ContextDeal::requestPermissionsFromUser failed, permissions is null", new Object[0]);
        } else {
            int length = strArr.length;
            String[] strArr2 = new String[length];
            for (int i2 = 0; i2 < length; i2++) {
                strArr2[i2] = PermissionConversion.getAosPermissionNameIfPossible(strArr[i2]);
            }
            PermissionConversion.registerRequestPermssions(i, strArr);
            ((Activity) this.abilityShellContext).requestPermissions(strArr2, i);
        }
    }

    @Override // ohos.app.Context
    public Object getHostProtectedStorageContext() {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext();
        }
        return this.abilityShellContext.createCredentialProtectedStorageContext();
    }

    @Override // ohos.app.Context
    public String getBundleName() {
        AbilityInfo abilityInfo2 = this.abilityInfo;
        if (abilityInfo2 != null) {
            return abilityInfo2.getBundleName();
        }
        Context context = this.abilityShellContext;
        if (context != null) {
            return context.getPackageName();
        }
        return null;
    }

    @Override // ohos.app.Context
    public String getBundleCodePath() {
        Application application2 = this.application;
        if (!(application2 == null || application2.getBundleInfo() == null || this.application.getBundleInfo().getAppInfo() == null)) {
            List<String> moduleSourceDirs = this.application.getBundleInfo().getAppInfo().getModuleSourceDirs();
            if (!moduleSourceDirs.isEmpty()) {
                return moduleSourceDirs.get(0);
            }
        }
        return null;
    }

    @Override // ohos.app.Context
    public File getDataDir() {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().getDataDir();
        }
        return this.abilityShellContext.getDataDir();
    }

    @Override // ohos.app.Context
    public String getBundleResourcePath() {
        return getBundleCodePath();
    }

    @Override // ohos.app.Context
    public File getCacheDir() {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().getCacheDir();
        }
        return this.abilityShellContext.getCacheDir();
    }

    @Override // ohos.app.Context
    public File getFilesDir() {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().getFilesDir();
        }
        return this.abilityShellContext.getFilesDir();
    }

    @Override // ohos.app.Context
    public File getCodeCacheDir() {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().getCodeCacheDir();
        }
        return this.abilityShellContext.getCodeCacheDir();
    }

    @Override // ohos.app.Context
    public File[] getExternalMediaDirs() {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().getExternalMediaDirs();
        }
        return this.abilityShellContext.getExternalMediaDirs();
    }

    @Override // ohos.app.Context
    public File getNoBackupFilesDir() {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().getNoBackupFilesDir();
        }
        return this.abilityShellContext.getNoBackupFilesDir();
    }

    @Override // ohos.app.Context
    public File getDir(String str, int i) {
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().getDir(str, i);
        }
        return this.abilityShellContext.getDir(str, i);
    }

    @Override // ohos.app.Context
    public File getExternalCacheDir() {
        return this.abilityShellContext.getExternalCacheDir();
    }

    @Override // ohos.app.Context
    public File[] getExternalCacheDirs() {
        return this.abilityShellContext.getExternalCacheDirs();
    }

    @Override // ohos.app.Context
    public File getExternalFilesDir(String str) {
        return this.abilityShellContext.getExternalFilesDir(str);
    }

    @Override // ohos.app.Context
    public File[] getExternalFilesDirs(String str) {
        return this.abilityShellContext.getExternalFilesDirs(str);
    }

    @Override // ohos.app.Context
    public File getObbDir() {
        return this.abilityShellContext.getObbDir();
    }

    @Override // ohos.app.Context
    public File[] getObbDirs() {
        return this.abilityShellContext.getObbDirs();
    }

    @Override // ohos.app.Context
    public boolean deleteFile(String str) {
        if (str == null) {
            return false;
        }
        if (isDeviceEncryptedStorage()) {
            return this.abilityShellContext.createDeviceProtectedStorageContext().deleteFile(str);
        }
        return this.abilityShellContext.deleteFile(str);
    }

    @Override // ohos.app.Context
    public boolean moveMissionToEnd(boolean z) {
        if (this.activityContext == null) {
            return false;
        }
        return this.activityContext.moveTaskToBack(z);
    }

    @Override // ohos.app.Context
    public boolean isFirstInMission() {
        if (this.activityContext == null) {
            return false;
        }
        return this.activityContext.isTaskRoot();
    }

    @Override // ohos.app.Context
    public int getMissionId() {
        if (this.activityContext == null) {
            return -1;
        }
        return this.activityContext.getTaskId();
    }

    @Override // ohos.app.Context
    public boolean setMissionInformation(MissionInformation missionInformation) {
        if (this.activityContext == null || missionInformation == null) {
            return false;
        }
        this.activityContext.setTaskDescription(convertTaskDescriptionToAndroid(missionInformation));
        return true;
    }

    private ActivityManager.TaskDescription convertTaskDescriptionToAndroid(MissionInformation missionInformation) {
        if (missionInformation == null) {
            return new ActivityManager.TaskDescription();
        }
        Bitmap bitmap = null;
        if (missionInformation.getIcon() != null) {
            bitmap = ImageDoubleFwConverter.createShadowBitmap(missionInformation.getIcon());
        }
        return new ActivityManager.TaskDescription(missionInformation.getLabel(), bitmap);
    }

    private int getUid() {
        Application application2 = this.application;
        if (application2 != null && application2.getBundleInfo() != null) {
            return this.application.getBundleInfo().getUid();
        }
        BundleInfo bundleInfo = this.bundleMgrImpl.getBundleInfo(getBundleName(), 0);
        if (bundleInfo != null) {
            return bundleInfo.getUid();
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::getUserId failed, bundleInfo is null", new Object[0]);
        return -1;
    }

    @Override // ohos.app.Context
    public IAbilityManager getAbilityManager() {
        AppLog.d(APPKIT_LABEL, "ContextDeal::getAbilityManager called", new Object[0]);
        return this.abilityManager;
    }

    @Override // ohos.app.Context
    public int getDisplayOrientation() {
        if (this.activityContext == null) {
            return -10;
        }
        return this.activityContext.getRequestedOrientation();
    }

    @Override // ohos.app.Context
    public void setShowOnLockScreen(boolean z) {
        if (this.activityContext != null) {
            this.activityContext.setShowWhenLocked(z);
        }
    }

    @Override // ohos.app.Context
    public void setWakeUpScreen(boolean z) {
        if (this.activityContext != null) {
            this.activityContext.setTurnScreenOn(z);
        }
    }

    @Override // ohos.app.Context
    public void restart() {
        if (this.activityContext != null) {
            this.activityContext.recreate();
        }
    }

    @Override // ohos.app.Context
    public void setTransitionAnimation(int i, int i2) {
        if (!(this.abilityShellContext instanceof Activity)) {
            AppLog.i("ContextDeal::setTransitionAnimation, ability is not instance of AbilityShellActivity", new Object[0]);
            return;
        }
        try {
            Class<?> cls = Class.forName(this.abilityShellContext.getPackageName() + TRANSITION_ANIMATION_SUFFIX, false, this.abilityShellContext.getClassLoader());
            ((Activity) this.abilityShellContext).overridePendingTransition(ResourceManagerInner.getAResId(i, cls, this.abilityShellContext), ResourceManagerInner.getAResId(i2, cls, this.abilityShellContext));
        } catch (ClassNotFoundException unused) {
            AppLog.e("ContextDeal::setTransitionAnimation failed, get class error, use original id", new Object[0]);
            ((Activity) this.abilityShellContext).overridePendingTransition(i, i2);
        }
    }

    @Override // ohos.app.Context
    public void setTheme(int i) {
        ResourceManager resourceManager2 = this.resourceManager;
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::setTheme resourceManager is null", new Object[0]);
            return;
        }
        try {
            this.pTheme = resourceManager2.getElement(i).getTheme();
        } catch (NotExistException unused) {
            AppLog.e("ContextDeal::setTheme NotExistException happens", new Object[0]);
        } catch (IOException unused2) {
            AppLog.e("ContextDeal::setTheme IOException happens", new Object[0]);
        } catch (WrongTypeException unused3) {
            AppLog.e("ContextDeal::setTheme WrongTypeException happens", new Object[0]);
        }
        HapModuleInfo hapModuleInfo2 = this.hapModuleInfo;
        if (hapModuleInfo2 != null) {
            hapModuleInfo2.setThemeId(i);
        }
    }

    @Override // ohos.app.Context
    public Theme getTheme() {
        Theme theme = this.pTheme;
        if (theme != null) {
            return theme;
        }
        setTheme(getThemeId());
        return this.pTheme;
    }

    @Override // ohos.app.Context
    public int getThemeId() {
        HapModuleInfo hapModuleInfo2 = this.hapModuleInfo;
        if (hapModuleInfo2 != null) {
            return hapModuleInfo2.getThemeId();
        }
        return -1;
    }

    @Override // ohos.app.Context
    public Theme getCombinedTheme(Theme theme) {
        Theme theme2 = this.pTheme;
        if (theme2 != null) {
            return theme2.getCombinedTheme(theme);
        }
        AppLog.d("ContextDeal::getCombinedTheme pTheme is null, return child.", new Object[0]);
        return theme;
    }

    @Override // ohos.app.Context
    public void setPattern(int i) {
        ResourceManager resourceManager2 = this.resourceManager;
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::setPattern resourceManager is null", new Object[0]);
            return;
        }
        try {
            this.element = resourceManager2.getElement(i);
            this.pPattern = this.element.getPattern();
        } catch (NotExistException unused) {
            AppLog.e("ContextDeal::setPattern NotExistException happens", new Object[0]);
        } catch (IOException unused2) {
            AppLog.e("ContextDeal::setPattern IOException happens", new Object[0]);
        } catch (WrongTypeException unused3) {
            AppLog.e("ContextDeal::setPattern WrongTypeException happens", new Object[0]);
        }
    }

    @Override // ohos.app.Context
    public Pattern getPattern() {
        return this.pPattern;
    }

    @Override // ohos.app.Context
    public Pattern getCombinedPattern(Pattern pattern) {
        Pattern pattern2 = this.pPattern;
        if (pattern2 != null) {
            return pattern2.getCombinedPattern(pattern);
        }
        AppLog.d("ContextDeal::getCombinedPattern pPattern is null, return child.", new Object[0]);
        return pattern;
    }

    @Override // ohos.app.Context
    public void compelVerifyPermission(String str, String str2) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null) {
            AppLog.e("ContextDeal::compelVerifyPermission permission is null illegal", new Object[0]);
        } else {
            this.abilityShellContext.enforceCallingOrSelfPermission(aosPermissionNameIfPossible, str2);
        }
    }

    @Override // ohos.app.Context
    public void compelVerifyUriPermission(Uri uri, int i, String str) {
        if (uri == null) {
            AppLog.e("ContextDeal::compelVerifyUriPermission uri is null illegal", new Object[0]);
            return;
        }
        this.abilityShellContext.enforceCallingOrSelfUriPermission(UriConverter.convertToAndroidContentUri(uri), i, str);
    }

    @Override // ohos.app.Context
    public void compelVerifyCallerPermission(String str, String str2) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null) {
            AppLog.e("ContextDeal::compelVerifyCallerPermission permission is null illegal", new Object[0]);
        } else {
            this.abilityShellContext.enforceCallingPermission(aosPermissionNameIfPossible, str2);
        }
    }

    @Override // ohos.app.Context
    public void compelVerifyCallerUriPermission(Uri uri, int i, String str) {
        if (uri == null) {
            AppLog.e("ContextDeal::compelVerifyCallerUriPermission uri is null illegal", new Object[0]);
            return;
        }
        this.abilityShellContext.enforceCallingUriPermission(UriConverter.convertToAndroidContentUri(uri), i, str);
    }

    @Override // ohos.app.Context
    public void compelVerifyPermission(String str, int i, int i2, String str2) {
        String aosPermissionNameIfPossible = PermissionConversion.getAosPermissionNameIfPossible(str);
        if (aosPermissionNameIfPossible == null || i < 0) {
            AppLog.e("ContextDeal::compelVerifyPermission permission or pid is illegal", new Object[0]);
        } else {
            this.abilityShellContext.enforcePermission(aosPermissionNameIfPossible, i, i2, str2);
        }
    }

    @Override // ohos.app.Context
    public void compelVerifyUriPermission(Uri uri, int i, int i2, int i3, String str) {
        if (uri == null || i < 0) {
            AppLog.e("ContextDeal::compelVerifyUriPermission uri or pid is illegal", new Object[0]);
            return;
        }
        this.abilityShellContext.enforceUriPermission(UriConverter.convertToAndroidContentUri(uri), i, i2, i3, str);
    }

    @Override // ohos.app.Context
    public void compelVerifyUriPermission(Uri uri, String str, String str2, int i, int i2, int i3, String str3) {
        if (uri == null || i < 0) {
            AppLog.e("ContextDeal::compelVerifyUriPermission uri or pid is illegal", new Object[0]);
            return;
        }
        this.abilityShellContext.enforceUriPermission(UriConverter.convertToAndroidContentUri(uri), str, str2, i, i2, i3, str3);
    }

    @Override // ohos.app.Context
    public int getColor(int i) {
        ResourceManager resourceManager2 = this.resourceManager;
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::getColor resourceManager is null", new Object[0]);
            return -1;
        }
        try {
            return resourceManager2.getElement(i).getColor();
        } catch (NotExistException unused) {
            AppLog.e("ContextDeal::getColor NotExistException happens", new Object[0]);
            return -1;
        } catch (IOException unused2) {
            AppLog.e("ContextDeal::getColor IOException happens", new Object[0]);
            return -1;
        } catch (WrongTypeException unused3) {
            AppLog.e("ContextDeal::getColor WrongTypeException happens", new Object[0]);
            return -1;
        }
    }

    @Override // ohos.app.Context
    public String getString(int i) {
        ResourceManager resourceManager2 = this.resourceManager;
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::getString resourceManager is null", new Object[0]);
            return null;
        }
        try {
            return resourceManager2.getElement(i).getString();
        } catch (NotExistException unused) {
            AppLog.e("ContextDeal::getString NotExistException happens", new Object[0]);
            return null;
        } catch (IOException unused2) {
            AppLog.e("ContextDeal::getString IOException happens", new Object[0]);
            return null;
        } catch (WrongTypeException unused3) {
            AppLog.e("ContextDeal::getString WrongTypeException happens", new Object[0]);
            return null;
        }
    }

    @Override // ohos.app.Context
    public String getString(int i, Object... objArr) {
        ResourceManager resourceManager2 = this.resourceManager;
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::getString resourceManager is null", new Object[0]);
            return null;
        }
        try {
            return resourceManager2.getElement(i).getString(objArr);
        } catch (NotExistException unused) {
            AppLog.e("ContextDeal::getString NotExistException happens", new Object[0]);
            return null;
        } catch (IOException unused2) {
            AppLog.e("ContextDeal::getString IOException happens", new Object[0]);
            return null;
        } catch (WrongTypeException unused3) {
            AppLog.e("ContextDeal::getString WrongTypeException happens", new Object[0]);
            return null;
        }
    }

    @Override // ohos.app.Context
    public String[] getStringArray(int i) {
        ResourceManager resourceManager2 = this.resourceManager;
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::getStringArray resourceManager is null", new Object[0]);
            return null;
        }
        try {
            return resourceManager2.getElement(i).getStringArray();
        } catch (NotExistException unused) {
            AppLog.e("ContextDeal::getStringArray NotExistException happens", new Object[0]);
            return null;
        } catch (IOException unused2) {
            AppLog.e("ContextDeal::getStringArray IOException happens", new Object[0]);
            return null;
        } catch (WrongTypeException unused3) {
            AppLog.e("ContextDeal::getStringArray WrongTypeException happens", new Object[0]);
            return null;
        }
    }

    @Override // ohos.app.Context
    public int[] getIntArray(int i) {
        ResourceManager resourceManager2 = this.resourceManager;
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::getIntArray resourceManager is null", new Object[0]);
            return null;
        }
        try {
            return resourceManager2.getElement(i).getIntArray();
        } catch (NotExistException unused) {
            AppLog.e("ContextDeal::getIntArray NotExistException happens", new Object[0]);
            return null;
        } catch (IOException unused2) {
            AppLog.e("ContextDeal::getIntArray IOException happens", new Object[0]);
            return null;
        } catch (WrongTypeException unused3) {
            AppLog.e("ContextDeal::getIntArray WrongTypeException happens", new Object[0]);
            return null;
        }
    }

    @Override // ohos.app.Context
    public String getProcessName() {
        Application application2 = this.application;
        if (application2 != null && application2.getProcessInfo() != null) {
            return this.application.getProcessInfo().getProcessName();
        }
        AppLog.e("ContextDeal::application or processInfo is null", new Object[0]);
        return "";
    }

    @Override // ohos.app.Context
    public Context getAbilityPackageContext() {
        AbilityInfo abilityInfo2;
        if (!(this.application == null || (abilityInfo2 = this.abilityInfo) == null || abilityInfo2.getModuleName() == null)) {
            Object abilityPackage = this.application.getAbilityPackage(this.abilityInfo.getModuleName());
            if (abilityPackage instanceof AbilityContext) {
                return ((AbilityContext) abilityPackage).getContext();
            }
        }
        return null;
    }

    @Override // ohos.app.Context
    public HapModuleInfo getHapModuleInfo() {
        return this.hapModuleInfo;
    }

    private Object getTopAbility() {
        return this.application.getTopAbility();
    }

    private String getAppDataPath() {
        if (isDeviceEncryptedStorage()) {
            return this.application.getDeviceProtectedPath();
        }
        return this.application.getAppDataPath();
    }

    private boolean initResourceManager(String str, BundleInfo bundleInfo) {
        if (this.resourceManager != null) {
            if (this.resourceState == HarmonyResources.getNewResourceState()) {
                AppLog.d("ContextDeal::initResourceManager resource manager has been initialized successfully, don't need to be initialized again", new Object[0]);
                updateResourceManager(this.resourceManager);
                return true;
            }
            AppLog.i("ContextDeal::initResourceManager resource manager is invalid, need reinitialize", new Object[0]);
            ResourceManagerInner resourceManagerInner2 = this.resourceManagerInner;
            if (resourceManagerInner2 != null) {
                resourceManagerInner2.release();
            }
            this.resourceManagerInner = null;
            this.resourceManager = null;
        }
        if (StringUtils.isBlank(str)) {
            AppLog.e("ContextDeal::initResourceManager failed, bundleName is empty", new Object[0]);
            return false;
        }
        if (bundleInfo == null) {
            bundleInfo = getBundleInfo(str);
        }
        Package r2 = HarmonyResources.getPackage(bundleInfo);
        if (r2 == null) {
            AppLog.e("ContextDeal::initResourceManager failed, setResources is false", new Object[0]);
            return false;
        }
        ResourcePath[] resourcePath = HarmonyResources.getResourcePath(bundleInfo);
        if (resourcePath.length == 0) {
            AppLog.e("ContextDeal::initResourceManager failed, resourcePaths is empty", new Object[0]);
            return false;
        }
        if (this.resourceManagerInner == null) {
            this.resourceManagerInner = new ResourceManagerInner();
        }
        try {
            HashMap<String, Object> createHarmonyosConfiguration = createHarmonyosConfiguration();
            if (createHarmonyosConfiguration.get(RESOURCE_MANAGER_CONFIGURATION) instanceof Configuration) {
                if (createHarmonyosConfiguration.get(RESOURCE_MANAGER_DEVICECAPABILITY) instanceof DeviceCapability) {
                    if (this.resourceManagerInner.init(r2, resourcePath, (Configuration) createHarmonyosConfiguration.get(RESOURCE_MANAGER_CONFIGURATION), (DeviceCapability) createHarmonyosConfiguration.get(RESOURCE_MANAGER_DEVICECAPABILITY))) {
                        AppLog.d("ContextDeal::initResourceManager successfully", new Object[0]);
                        this.resourceManager = this.resourceManagerInner.getResourceManager();
                        this.resourceState = HarmonyResources.getNewResourceState();
                        return true;
                    }
                    AppLog.e("ContextDeal::initResourceManager result is false", new Object[0]);
                    HiViewUtil.sendGlobalEvent("resourceManagerInner.init", str, 0);
                    return tryInitResourceManager(bundleInfo, r2);
                }
            }
            AppLog.d("ContextDeal::initResourceManager failed, class transform failed", new Object[0]);
            return false;
        } catch (IOException e) {
            AppLog.e("ContextDeal::initResourceManager failed : %{public}s", e.getMessage());
        }
    }

    private boolean tryInitResourceManager(BundleInfo bundleInfo, Package r11) {
        if (bundleInfo == null || bundleInfo.getAppInfo() == null) {
            AppLog.e("ContextDeal::tryInitResourceManager failed, bundleInfo is empty", new Object[0]);
            return false;
        }
        List<String> moduleSourceDirs = bundleInfo.getAppInfo().getModuleSourceDirs();
        if (moduleSourceDirs.isEmpty()) {
            AppLog.e("ContextDeal::tryInitResourceManager failed, moduleSourceDirs is empty", new Object[0]);
            return false;
        }
        int size = moduleSourceDirs.size();
        ResourcePath[] resourcePathArr = new ResourcePath[size];
        for (int i = 0; i < size; i++) {
            String str = moduleSourceDirs.get(i);
            if (str == null || str.isEmpty()) {
                AppLog.i("ContextDeal::tryInitResourceManager moduleSourceDir is null", new Object[0]);
            } else {
                AppLog.d("ContextDeal::tryInitResourceManager moduleSourceDir: %{private}s", str);
                ResourcePath resourcePath = new ResourcePath();
                resourcePath.setResourcePath(str, null);
                resourcePathArr[i] = resourcePath;
            }
        }
        if (this.resourceManagerInner == null) {
            this.resourceManagerInner = new ResourceManagerInner();
        }
        try {
            HashMap<String, Object> createHarmonyosConfiguration = createHarmonyosConfiguration();
            if (createHarmonyosConfiguration.get(RESOURCE_MANAGER_CONFIGURATION) instanceof Configuration) {
                if (createHarmonyosConfiguration.get(RESOURCE_MANAGER_DEVICECAPABILITY) instanceof DeviceCapability) {
                    if (this.resourceManagerInner.init(r11, resourcePathArr, (Configuration) createHarmonyosConfiguration.get(RESOURCE_MANAGER_CONFIGURATION), (DeviceCapability) createHarmonyosConfiguration.get(RESOURCE_MANAGER_DEVICECAPABILITY))) {
                        AppLog.d("ContextDeal::tryInitResourceManager successfully", new Object[0]);
                        this.resourceManager = this.resourceManagerInner.getResourceManager();
                        this.resourceState = HarmonyResources.getNewResourceState();
                        return true;
                    }
                    AppLog.e("ContextDeal::tryInitResourceManager result is false", new Object[0]);
                    return false;
                }
            }
            AppLog.d("ContextDeal::initResourceManager failed, class transform failed", new Object[0]);
            return false;
        } catch (IOException e) {
            AppLog.e("ContextDeal::tryInitResourceManager failed : %{public}s", e.getMessage());
        }
    }

    private void updateResourceManager(ResourceManager resourceManager2) {
        if (resourceManager2 == null) {
            AppLog.d("ContextDeal::updateResourceManager resourceManager is null", new Object[0]);
            return;
        }
        HashMap<String, Object> createHarmonyosConfiguration = createHarmonyosConfiguration();
        if (!(createHarmonyosConfiguration.get(RESOURCE_MANAGER_CONFIGURATION) instanceof Configuration) || !(createHarmonyosConfiguration.get(RESOURCE_MANAGER_DEVICECAPABILITY) instanceof DeviceCapability)) {
            AppLog.d("ContextDeal::initResourceManager failed, class transform failed", new Object[0]);
            return;
        }
        Configuration configuration = (Configuration) createHarmonyosConfiguration.get(RESOURCE_MANAGER_CONFIGURATION);
        DeviceCapability deviceCapability = (DeviceCapability) createHarmonyosConfiguration.get(RESOURCE_MANAGER_DEVICECAPABILITY);
        if (!configuration.equals(resourceManager2.getConfiguration()) || !deviceCapability.equals(resourceManager2.getDeviceCapability())) {
            resourceManager2.updateConfiguration(configuration, deviceCapability);
        }
    }

    private HashMap<String, Object> createHarmonyosConfiguration() {
        HashMap<String, Object> hashMap = new HashMap<>();
        Context context = this.abilityShellContext;
        if (context == null) {
            hashMap.put(RESOURCE_MANAGER_CONFIGURATION, new Configuration());
            hashMap.put(RESOURCE_MANAGER_DEVICECAPABILITY, new DeviceCapability());
            return hashMap;
        }
        android.content.res.Configuration configuration = context.getResources().getConfiguration();
        Configuration convert = ResourceUtils.convert(configuration);
        convert.colorMode = getColorMode();
        hashMap.put(RESOURCE_MANAGER_CONFIGURATION, convert);
        hashMap.put(RESOURCE_MANAGER_DEVICECAPABILITY, ResourceUtils.convertToDeviceCapability(configuration));
        return hashMap;
    }

    private String getPreferenceDirectory(AbilityInfo abilityInfo2) {
        String str;
        synchronized (CREATE_FILE_LOCK) {
            if (this.preferencePath == null) {
                this.preferencePath = getExpectedDirectory(abilityInfo2, PREFERENCES_DIRECTORY_NAME);
            }
            str = this.preferencePath;
        }
        return str;
    }

    private String getDatabaseDirectory(AbilityInfo abilityInfo2) {
        String str;
        synchronized (CREATE_FILE_LOCK) {
            if (this.databasePath == null) {
                this.databasePath = getExpectedDirectory(abilityInfo2, DATABASE_DIRECTORY_NAME);
            }
            str = this.databasePath;
        }
        return str;
    }

    private String getDistributeDatabaseDirectory(AbilityInfo abilityInfo2) {
        return getExpectedDirectory(abilityInfo2, DISTRIBUTE_DATABASE_DIRECTORY_NAME);
    }

    private String getDistributeDirectory(AbilityInfo abilityInfo2) {
        synchronized (CREATE_FILE_LOCK) {
            if (this.distributedPath == null) {
                if (abilityInfo2 == null) {
                    AppLog.e("ContextDeal::getDistributeDirectory failed, abilityInfo is null", new Object[0]);
                    return null;
                }
                String bundleName = abilityInfo2.getBundleName();
                if (StringUtils.isBlank(bundleName)) {
                    AppLog.e("ContextDeal::getDistributeDirectory failed, bundleName is illegal", new Object[0]);
                    return null;
                }
                if (this.distributedFileManager == null) {
                    this.distributedFileManager = new DistributedFileManagerImpl();
                }
                String bundleDistributedDir = this.distributedFileManager.getBundleDistributedDir(bundleName);
                if (StringUtils.isBlank(bundleDistributedDir)) {
                    AppLog.e("ContextDeal::getDistributeDirectory failed, distributePath is illegal", new Object[0]);
                    HiViewUtil.sendDistributedDataEvent("getBundleDistributedDir", 0);
                    return null;
                }
                String[] split = abilityInfo2.getClassName().split("\\.");
                if (split.length == 0) {
                    AppLog.e("ContextDeal::getDistributeDirectory failed, ability className is illegal", new Object[0]);
                    return null;
                }
                String str = split[split.length - 1];
                if (!FileUtils.isLegalFileName(str)) {
                    AppLog.e("ContextDeal::getDistributeDirectory failed, ability directory name is illegal", new Object[0]);
                    return null;
                }
                this.distributedPath = FileUtils.getExpectedPath(bundleDistributedDir, File.separator, str);
            }
            return this.distributedPath;
        }
    }

    private String getUserDirectory() {
        int uid = getUid() / 100000;
        if (uid == -1) {
            AppLog.e(APPKIT_LABEL, "ContextDeal, userId %{private}d is invalid", Integer.valueOf(uid));
            return DIRECTORY_DATA_DATA;
        } else if (uid == 0) {
            return DIRECTORY_DATA_DATA;
        } else {
            return DIRECTORY_DATA_USER + String.valueOf(uid) + File.separator;
        }
    }

    private String getExpectedDirectory(AbilityInfo abilityInfo2, String str) {
        if (abilityInfo2 == null) {
            AppLog.e("abilityInfo is null, this is application context", new Object[0]);
            String packageName = this.abilityShellContext.getPackageName();
            return getUserDirectory() + packageName + File.separator + str;
        }
        String[] split = abilityInfo2.getClassName().split("\\.");
        if (split.length == 0) {
            AppLog.e("ContextDeal::getDistributeDirectory failed, ability className is illegal", new Object[0]);
            return null;
        }
        String str2 = split[split.length - 1];
        if (!FileUtils.isLegalFileName(str2)) {
            AppLog.e("ContextDeal::getExpectedDirectory failed, ability directory name is illegal", new Object[0]);
            return null;
        }
        String appDataPath = getAppDataPath();
        if (StringUtils.isBlank(appDataPath)) {
            AppLog.e("ContextDeal::getExpectedDirectory failed, dataPath is illegal", new Object[0]);
            return null;
        }
        return FileUtils.getExpectedPath(appDataPath, File.separator, str2, File.separator, str);
    }

    private boolean stopAbilityInner(Intent intent) {
        AbilityShellData selectAbility = selectAbility(intent);
        if (selectAbility == null) {
            AppLog.e("ContextDeal::stopAbility selectAbility failed", new Object[0]);
            return false;
        } else if (selectAbility.getLocal()) {
            return stopLocalAbility(selectAbility, intent);
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            int stopRemoteAbility = stopRemoteAbility(intent, selectAbility.getAbilityInfo());
            AbilityInfo abilityInfo2 = this.abilityInfo;
            if (abilityInfo2 != null) {
                JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_STOP_REMOTE_ABILITY, abilityInfo2.getBundleName(), this.abilityInfo.getClassName(), currentTimeMillis);
            }
            if (stopRemoteAbility != 0) {
                AppLog.e("ContextDeal::stopRemoteAbility failed code is %{public}d", Integer.valueOf(stopRemoteAbility));
            }
            if (stopRemoteAbility == 0) {
                return true;
            }
            return false;
        }
    }

    private AbilityShellData selectAbility(Intent intent) {
        HiTraceId begin = HiTrace.begin("selectAbility", 1);
        AbilityShellData selectAbilityInner = selectAbilityInner(intent);
        HiTrace.end(begin);
        return selectAbilityInner;
    }

    private AbilityShellData getAbilityShellDataLocal(Intent intent) {
        if (intent == null || intent.getElement() == null) {
            AppLog.d("ContextDeal::getAbilityShellDataLocal failed, no intent element", new Object[0]);
            return null;
        }
        AbilityShellData abilityShellDataInCurrentBundle = getAbilityShellDataInCurrentBundle(intent);
        if (abilityShellDataInCurrentBundle == null) {
            return getAbilityShellDataFromBms(intent);
        }
        AppLog.d("ContextDeal::getAbilityShellDataLocal result is matched in current bundle.", new Object[0]);
        return abilityShellDataInCurrentBundle;
    }

    private AbilityShellData getAbilityShellDataInCurrentBundle(Intent intent) {
        BundleInfo bundleInfo;
        Application application2 = this.application;
        if (application2 != null) {
            bundleInfo = application2.getBundleInfo();
        } else {
            bundleInfo = this.bundleMgrImpl.getBundleInfo(getBundleName(), 1);
        }
        if (bundleInfo == null) {
            AppLog.e("ContextDeal::getAbilityShellDataInCurrentBundle failed, bundleInfo is empty", new Object[0]);
            return null;
        } else if (!isSameBundle(bundleInfo, intent.getElement().getBundleName())) {
            return null;
        } else {
            AbilityInfo abilityInfoByName = bundleInfo.getAbilityInfoByName(getFullClassName(intent.getElement().getBundleName(), intent.getElement().getAbilityName()));
            if (abilityInfoByName != null) {
                return new AbilityShellData(true, abilityInfoByName, AbilityShellConverterUtils.convertToShellInfoSupportDiffPkg(abilityInfoByName, bundleInfo));
            }
            AppLog.e("ContextDeal::tmpInfo is null;AbilityName: %{public}s.", intent.getElement().getAbilityName());
            return null;
        }
    }

    private AbilityShellData getAbilityShellDataFromBms(Intent intent) {
        List<AbilityInfo> queryAbilityByIntent = new BundleMgrBridge().queryAbilityByIntent(intent);
        if (queryAbilityByIntent == null || queryAbilityByIntent.isEmpty()) {
            AppLog.e("ContextDeal::getAbilityShellDataFromBms failed, abilityInfos is empty", new Object[0]);
            return null;
        }
        return new AbilityShellData(true, queryAbilityByIntent.get(0), AbilityShellConverterUtils.convertToShellInfoSupportDiffPkg(queryAbilityByIntent.get(0), null));
    }

    private boolean isSameBundle(BundleInfo bundleInfo, String str) {
        if (this.abilityShellContext.getPackageName().equals(str)) {
            return true;
        }
        if (bundleInfo.isDifferentName()) {
            return bundleInfo.getOriginalName().equals(str);
        }
        return false;
    }

    private AbilityShellData getAbilityShellDataRemote(Intent intent) {
        try {
            return this.distributedImpl.selectAbility(intent);
        } catch (RemoteException e) {
            AppLog.e("ContextDeal::selectAbility RemoteException: %{public}s", e.getMessage());
            return null;
        }
    }

    private AbilityShellData selectAbilityInner(Intent intent) {
        AbilityShellData abilityShellData = null;
        if (intent == null) {
            AppLog.e("ContextDeal::selectAbilityInner failed, intent is empty.", new Object[0]);
            return null;
        } else if (intent.getElement() == null || intent.getElement().getDeviceId() == null || intent.getElement().getDeviceId().isEmpty() || intent.getElement().getDeviceId().equals("")) {
            if (!isFlagExists(256, intent.getFlags())) {
                abilityShellData = getAbilityShellDataLocal(intent);
            }
            if (abilityShellData != null) {
                return abilityShellData;
            }
            ResolveInfo androidComponent = AbilityShellConverterUtils.getAndroidComponent(this.abilityShellContext, intent);
            if (androidComponent != null) {
                return AbilityShellConverterUtils.createAbilityShellDataByResolveInfo(androidComponent, true);
            }
            return getAbilityShellDataRemote(intent);
        } else if (intent.getElement().getDeviceId().equals(this.application.getLocalDeviceId())) {
            return getAbilityShellDataLocal(intent);
        } else {
            return getAbilityShellDataRemote(intent);
        }
    }

    private static String getFullClassName(String str, String str2) {
        if (str == null || str2 == null) {
            return "";
        }
        if (!str2.isEmpty() && str2.charAt(0) == '.') {
            return str + str2;
        } else if (str2.startsWith(str)) {
            return str2;
        } else {
            return str + '.' + str2;
        }
    }

    private boolean stopLocalAbility(AbilityShellData abilityShellData, Intent intent) {
        ShellInfo shellInfo = abilityShellData.getShellInfo();
        if (shellInfo.getType() != ShellInfo.ShellType.SERVICE) {
            AppLog.e("ContextDeal::stopLocalAbility ShellType not SERVICE", new Object[0]);
            return false;
        }
        Optional<android.content.Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, shellInfo);
        if (!createAndroidIntent.isPresent()) {
            AppLog.e("ContextDeal::stopLocalAbility createAndroidIntent failed", new Object[0]);
            return false;
        }
        this.abilityShellContext.stopService(createAndroidIntent.get());
        return true;
    }

    private int stopRemoteAbility(Intent intent, AbilityInfo abilityInfo2) {
        int i;
        AppLog.d("ContextDeal::stopRemoteAbility called", new Object[0]);
        try {
            i = this.distributedImpl.stopRemoteAbility(intent, abilityInfo2);
        } catch (RemoteException e) {
            AppLog.e("ContextDeal::stopRemoteAbility RemoteException: %{public}s", e.getMessage());
            i = -1;
        }
        checkDmsInterfaceResult(i, "stopRemoteAbility");
        return i;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private List<AbilityShellData> getAbilityShellDatasLocal(Intent intent) {
        BundleInfo bundleInfo;
        ArrayList arrayList = new ArrayList();
        Application application2 = this.application;
        if (application2 != null) {
            bundleInfo = application2.getBundleInfo();
        } else {
            bundleInfo = this.bundleMgrImpl.getBundleInfo(getBundleName(), 1);
        }
        if (bundleInfo == null) {
            AppLog.e("ContextDeal::getAbilityShellDatasLocal failed, bundleInfo is empty", new Object[0]);
            return arrayList;
        }
        if (intent.getElement() != null && this.abilityShellContext.getPackageName().equals(intent.getElement().getBundleName())) {
            AbilityInfo abilityInfoByName = bundleInfo.getAbilityInfoByName(getFullClassName(intent.getElement().getBundleName(), intent.getElement().getAbilityName()));
            if (abilityInfoByName != null) {
                arrayList.add(new AbilityShellData(true, abilityInfoByName, AbilityShellConverterUtils.convertToShellInfoSupportDiffPkg(abilityInfoByName, bundleInfo)));
                return arrayList;
            } else if (!isFlagExists(2048, intent.getFlags())) {
                return arrayList;
            }
        }
        BundleMgrBridge bundleMgrBridge = new BundleMgrBridge();
        List<AbilityInfo> queryAbilityByIntent = bundleMgrBridge.queryAbilityByIntent(intent);
        if (queryAbilityByIntent == null || intent.getElement() == null) {
            AppLog.e("ContextDeal::getAbilityShellDatasLocal failed, abilityInfos is empty", new Object[0]);
            return arrayList;
        }
        String bundleName = intent.getElement().getBundleName();
        if (StringUtils.isEmpty(bundleName)) {
            AppLog.e("ContextDeal::getAbilityShellDatasLocal failed, targetBundleName is empty", new Object[0]);
            return arrayList;
        }
        BundleInfo bundleInfo2 = bundleMgrBridge.getBundleInfo(bundleName, 0);
        for (AbilityInfo abilityInfo2 : queryAbilityByIntent) {
            arrayList.add(new AbilityShellData(true, abilityInfo2, AbilityShellConverterUtils.convertToShellInfoSupportDiffPkg(abilityInfo2, bundleInfo2)));
        }
        return arrayList;
    }

    private void cancelDownload(final Intent intent) {
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(new Runnable() {
            /* class ohos.app.ContextDeal.AnonymousClass1 */

            public void run() {
                List abilityShellDatasLocal = ContextDeal.this.getAbilityShellDatasLocal(intent);
                if (abilityShellDatasLocal.size() != 1) {
                    AppLog.e("ContextDeal::cancelDownload getAbilityShellDatasLocal error, result size(=%{public}d) != 1", Integer.valueOf(abilityShellDatasLocal.size()));
                    return;
                }
                AbilityShellData abilityShellData = (AbilityShellData) abilityShellDatasLocal.get(0);
                if (abilityShellData.getAbilityInfo() == null) {
                    AppLog.e("ContextDeal::cancelDownload get AbilityInfo failed", new Object[0]);
                    return;
                }
                IBundleManager bundleManager = ContextDeal.this.getBundleManager();
                if (bundleManager == null) {
                    AppLog.e("ContextDeal::getBundleInstaller getBundleManager failed", new Object[0]);
                    return;
                }
                try {
                    bundleManager.cancelDownload(abilityShellData.getAbilityInfo());
                } catch (RemoteException e) {
                    AppLog.e("ContextDeal::cancelDownload failed, errormsg = %{public}s", e.getMessage());
                }
            }
        });
    }

    private void startAbilityInner(Intent intent, int i, AbilityStartSetting abilityStartSetting) {
        List<AbilityShellData> arrayList = new ArrayList<>();
        if (intent.getElement() == null || intent.getElement().getDeviceId() == null || intent.getElement().getDeviceId().isEmpty() || intent.getElement().getDeviceId().equals("")) {
            if (!isFlagExists(256, intent.getFlags())) {
                arrayList = getAbilityShellDatasLocal(intent);
            }
            if (arrayList.isEmpty()) {
                arrayList = SelectAbilityUtils.fetchAbilities(this.abilityShellContext, intent);
            }
        } else if (intent.getElement().getDeviceId().equals(this.application.getLocalDeviceId())) {
            arrayList = getAbilityShellDatasLocal(intent);
        } else {
            arrayList = SelectAbilityUtils.fetchAbilities(this.abilityShellContext, intent);
        }
        if (arrayList == null || arrayList.isEmpty()) {
            AppLog.e("ContextDeal::startAbility fetchAbilities failed", new Object[0]);
        } else if (arrayList.size() == 1) {
            AppLog.d("ContextDeal::startAbilityInner only one ability, don't show dialog!", new Object[0]);
            performStartAblilityInner(intent, i, arrayList.get(0), abilityStartSetting);
        } else if (isFlagExists(256, intent.getFlags())) {
            AppLog.d("ContextDeal::startAbilityInner with multi device", new Object[0]);
            getMainTaskDispatcher().asyncDispatch(new Runnable(new ArrayList(arrayList), intent, i, abilityStartSetting) {
                /* class ohos.app.$$Lambda$ContextDeal$FXbx1AWMpSyuDefpMQ1jTs6Z8 */
                private final /* synthetic */ List f$1;
                private final /* synthetic */ Intent f$2;
                private final /* synthetic */ int f$3;
                private final /* synthetic */ AbilityStartSetting f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                public final void run() {
                    ContextDeal.this.lambda$startAbilityInner$0$ContextDeal(this.f$1, this.f$2, this.f$3, this.f$4);
                }
            });
        } else {
            AppLog.d("ContextDeal::startAbilityInner with current device", new Object[0]);
            getMainTaskDispatcher().asyncDispatch(new Runnable(new ArrayList(arrayList), intent, i, abilityStartSetting) {
                /* class ohos.app.$$Lambda$ContextDeal$_jGSlH5_NhMZLjOaGdAl81DBwQQ */
                private final /* synthetic */ List f$1;
                private final /* synthetic */ Intent f$2;
                private final /* synthetic */ int f$3;
                private final /* synthetic */ AbilityStartSetting f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                public final void run() {
                    ContextDeal.this.lambda$startAbilityInner$1$ContextDeal(this.f$1, this.f$2, this.f$3, this.f$4);
                }
            });
        }
    }

    public /* synthetic */ void lambda$startAbilityInner$0$ContextDeal(List list, final Intent intent, final int i, final AbilityStartSetting abilityStartSetting) {
        new AbilityResolver(this.abilityShellContext, list, new AbilityResolver.IResolveResult() {
            /* class ohos.app.ContextDeal.AnonymousClass2 */

            @Override // ohos.abilityshell.utils.AbilityResolver.IResolveResult
            public void onResolveResult(AbilityShellData abilityShellData) {
                ContextDeal.this.performStartAblilityInner(intent, i, abilityShellData, abilityStartSetting);
            }
        }).show();
    }

    public /* synthetic */ void lambda$startAbilityInner$1$ContextDeal(List list, final Intent intent, final int i, final AbilityStartSetting abilityStartSetting) {
        new AbilityResolverSingleDevice(this.abilityShellContext, list, new AbilityResolverSingleDevice.IResolveResult() {
            /* class ohos.app.ContextDeal.AnonymousClass3 */

            @Override // ohos.abilityshell.utils.AbilityResolverSingleDevice.IResolveResult
            public void onResolveResult(AbilityShellData abilityShellData) {
                ContextDeal.this.performStartAblilityInner(intent, i, abilityShellData, abilityStartSetting);
            }
        }).show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void performStartAblilityInner(Intent intent, int i, AbilityShellData abilityShellData, AbilityStartSetting abilityStartSetting) {
        if (abilityShellData != null) {
            AbilityInfo abilityInfo2 = this.abilityInfo;
            if (abilityInfo2 != null) {
                intent.setParam(CALLER_BUNDLE_NAME, abilityInfo2.getBundleName());
            }
            if (abilityShellData.getLocal()) {
                long currentTimeMillis = System.currentTimeMillis();
                startLocalAbility(abilityShellData, intent, i, abilityStartSetting);
                AbilityInfo abilityInfo3 = this.abilityInfo;
                if (abilityInfo3 != null) {
                    JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_START_LOCAL_ABILITY, abilityInfo3.getBundleName(), this.abilityInfo.getClassName(), currentTimeMillis);
                    return;
                }
                return;
            }
            long currentTimeMillis2 = System.currentTimeMillis();
            int startRemoteAbility = startRemoteAbility(intent, abilityShellData.getAbilityInfo(), i);
            AbilityInfo abilityInfo4 = this.abilityInfo;
            if (abilityInfo4 != null) {
                JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_START_REMOTE_ABILITY, abilityInfo4.getBundleName(), this.abilityInfo.getClassName(), currentTimeMillis2);
            }
            JLogUtil.printStartAbilityInfo(intent, currentTimeMillis2, startRemoteAbility);
            if (startRemoteAbility != 0) {
                AppLog.e("ContextDeal::startRemoteAbility failed code is %{public}d", Integer.valueOf(startRemoteAbility));
            }
        }
    }

    private void startLocalAbility(AbilityShellData abilityShellData, Intent intent, int i) {
        startLocalAbility(abilityShellData, intent, i, AbilityStartSetting.getEmptySetting());
    }

    private void startLocalAbility(AbilityShellData abilityShellData, Intent intent, int i, AbilityStartSetting abilityStartSetting) {
        AppLog.i("ContextDeal::startLocalAbility called", new Object[0]);
        ShellInfo shellInfo = abilityShellData.getShellInfo();
        Optional<android.content.Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, shellInfo);
        if (!createAndroidIntent.isPresent()) {
            AppLog.e("ContextDeal::startLocalAbility createAndroidIntent failed", new Object[0]);
        } else if (createAndroidIntent.get().getComponent() == null) {
            AppLog.e("ContextDeal::startLocalAbility ComponentName is null", new Object[0]);
        } else {
            handleForwardFlag(intent, createAndroidIntent.get());
            if (this.abilityShellContext instanceof Service) {
                createAndroidIntent.get().addFlags(268435456);
            }
            if (shellInfo.getType() == ShellInfo.ShellType.ACTIVITY) {
                if (isRequestCodeValid(i)) {
                    startLocalActivityForResult(createAndroidIntent.get(), i, abilityStartSetting);
                } else {
                    startLocalActivity(createAndroidIntent.get(), abilityStartSetting);
                }
            } else if (shellInfo.getType() == ShellInfo.ShellType.SERVICE) {
                if (isFlagExists(512, intent.getFlags())) {
                    startForegroundService(createAndroidIntent.get());
                } else {
                    startLocalService(createAndroidIntent.get());
                }
            } else if (shellInfo.getType() == ShellInfo.ShellType.WEB) {
                android.content.Intent intent2 = new android.content.Intent();
                intent2.setAction("android.intent.action.VIEW");
                String str = WEB_ABILITY_DEEPLINK_SCHEME + intent.getElement().getAbilityName();
                intent2.setData(android.net.Uri.parse(str));
                AppLog.i("ContextDeal::startLocalAbility deepLinkUri %{public}s, intent %{public}s", str, intent.toString());
                startLocalActivity(intent2, abilityStartSetting);
            } else {
                AppLog.w("ContextDeal::startLocalAbility Unknown ShellType", new Object[0]);
            }
        }
    }

    private void handleForwardFlag(Intent intent, android.content.Intent intent2) {
        if ((intent.getFlags() & 4) != 0) {
            AppLog.i("ContextDeal::handleForwardFlag have FLAG_ABILITY_FORWARD_RESULT", new Object[0]);
            intent2.setFlags(intent2.getFlags() | 33554432);
        }
    }

    private void startLocalActivity(android.content.Intent intent, AbilityStartSetting abilityStartSetting) {
        AppLog.i("ContextDeal::startLocalActivity called", new Object[0]);
        Optional<ActivityOptions> createActivityOptions = AbilityStartSettingConverter.createActivityOptions(abilityStartSetting);
        try {
            if (!createActivityOptions.isPresent()) {
                this.abilityShellContext.startActivity(intent);
                return;
            }
            this.abilityShellContext.startActivity(intent, createActivityOptions.get().toBundle());
        } catch (ActivityNotFoundException unused) {
            AppLog.e("ContextDeal::startLocalActivity ability is not found, intent %{public}s", intent.toString());
        }
    }

    private void startLocalActivityForResult(android.content.Intent intent, int i, AbilityStartSetting abilityStartSetting) {
        AppLog.d("ContextDeal::startLocalActivityForResult called", new Object[0]);
        Optional<ActivityOptions> createActivityOptions = AbilityStartSettingConverter.createActivityOptions(abilityStartSetting);
        try {
            if (this.abilityShellContext instanceof Activity) {
                Activity activity = (Activity) this.abilityShellContext;
                if (createActivityOptions.isPresent()) {
                    activity.startActivityForResult(intent, i, createActivityOptions.get().toBundle());
                } else {
                    activity.startActivityForResult(intent, i);
                }
            } else if (this.abilityShellContext instanceof Application) {
                ((Application) this.abilityShellContext).startActivity(intent);
            } else {
                AppLog.w("ContextDeal::startLocalActivityForResult only Activity support", new Object[0]);
            }
        } catch (ActivityNotFoundException unused) {
            AppLog.e("ContextDeal::startLocalActivityForResult Ability not found", new Object[0]);
        }
    }

    private BundleInfo getBundleInfo(String str) {
        if (this.application == null || !str.equals(getBundleName())) {
            return this.bundleMgrImpl.getBundleInfo(str, 0);
        }
        return this.application.getBundleInfo();
    }

    private void addHapSource(BundleInfo bundleInfo, ClassLoader classLoader2) {
        boolean z;
        List<String> moduleSourceDirs = bundleInfo.getAppInfo().getModuleSourceDirs();
        if (moduleSourceDirs.isEmpty()) {
            AppLog.e("ContextDeal::createBundleContext get hap source dir failed", new Object[0]);
            return;
        }
        String cpuAbi = bundleInfo.getAppInfo().getCpuAbi();
        if (bundleInfo.getAppInfo().isCompressNativeLibs() || cpuAbi == null || cpuAbi.isEmpty()) {
            z = false;
        } else {
            AppLog.d("ContextDeal::createBundleContext get current device cpuAbi: %{private}s", cpuAbi);
            z = true;
        }
        ArrayList arrayList = new ArrayList(moduleSourceDirs.size());
        if (classLoader2 instanceof PathClassLoader) {
            PathClassLoader pathClassLoader = (PathClassLoader) classLoader2;
            for (String str : moduleSourceDirs) {
                pathClassLoader.addDexPath(str);
                if (z) {
                    arrayList.add(str + LIBS + cpuAbi);
                }
                AppLog.d("ContextDeal::createBundleContext add path %{private}s to classloader success", str);
            }
            pathClassLoader.addNativePath(arrayList);
            return;
        }
        throw new IllegalStateException("class loader is not a PathClassLoader");
    }

    private void startLocalService(android.content.Intent intent) {
        AppLog.d("ContextDeal::startLocalService called", new Object[0]);
        try {
            this.abilityShellContext.startService(intent);
        } catch (SecurityException unused) {
            AppLog.e("ContextDeal::startLocalAbility ability not found", new Object[0]);
        }
    }

    private void startForegroundService(android.content.Intent intent) {
        AppLog.d("ContextDeal::startForegroundService called", new Object[0]);
        try {
            this.abilityShellContext.startForegroundService(intent);
        } catch (SecurityException unused) {
            AppLog.e("ContextDeal::startLocalAbility ability not found", new Object[0]);
        }
    }

    private int startRemoteAbility(Intent intent, AbilityInfo abilityInfo2, int i) {
        HiTraceId begin = HiTrace.begin("startRemoteAbility", 1);
        int startRemoteAbilityInner = startRemoteAbilityInner(intent, abilityInfo2, i);
        HiTrace.end(begin);
        return startRemoteAbilityInner;
    }

    private int startRemoteAbilityInner(Intent intent, AbilityInfo abilityInfo2, int i) {
        int i2;
        AppLog.d("Context::startRemoteAbility called", new Object[0]);
        try {
            i2 = this.distributedImpl.startRemoteAbility(intent, abilityInfo2, i);
        } catch (RemoteException e) {
            AppLog.e("ContextDeal::startRemoteAbility RemoteException: %{public}s", e.getMessage());
            i2 = -1;
        }
        checkDmsInterfaceResult(i2, "startRemoteAbility");
        return i2;
    }

    private boolean connectAbilityInner(Intent intent, IAbilityConnection iAbilityConnection) {
        AbilityShellData abilityShellData;
        if (isFlagExists(32, intent.getFlags())) {
            abilityShellData = selectFormAbility(intent, ShellInfo.ShellType.SERVICE);
        } else {
            abilityShellData = selectAbility(intent);
        }
        if (abilityShellData == null) {
            AppLog.e("ContextDeal::connectAbility selectAbility failed", new Object[0]);
            return false;
        }
        AbilityInfo abilityInfo2 = this.abilityInfo;
        if (abilityInfo2 != null) {
            intent.setParam(CALLER_BUNDLE_NAME, abilityInfo2.getBundleName());
        }
        if (abilityShellData.getLocal()) {
            long currentTimeMillis = System.currentTimeMillis();
            boolean connectLocalAbility = connectLocalAbility(abilityShellData, intent, iAbilityConnection);
            AbilityInfo abilityInfo3 = this.abilityInfo;
            if (abilityInfo3 != null) {
                JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_CONNECT_LOCAL_ABILITY, abilityInfo3.getBundleName(), this.abilityInfo.getClassName(), currentTimeMillis);
            }
            return connectLocalAbility;
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        boolean connectRemoteAbility = connectRemoteAbility(abilityShellData, intent, iAbilityConnection);
        AbilityInfo abilityInfo4 = this.abilityInfo;
        if (abilityInfo4 != null) {
            JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_CONNECT_REMOTE_ABILITY, abilityInfo4.getBundleName(), this.abilityInfo.getClassName(), currentTimeMillis2);
        }
        return connectRemoteAbility;
    }

    private boolean connectLocalAbility(AbilityShellData abilityShellData, Intent intent, IAbilityConnection iAbilityConnection) {
        ServiceConnection serviceConnection;
        ShellInfo shellInfo = abilityShellData.getShellInfo();
        AbilityInfo abilityInfo2 = abilityShellData.getAbilityInfo();
        if (shellInfo.getType() != ShellInfo.ShellType.SERVICE) {
            AppLog.e("ContextDeal::connectLocalAbility Ability not SERVICE", new Object[0]);
            return false;
        }
        Optional<android.content.Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, shellInfo);
        if (!createAndroidIntent.isPresent()) {
            AppLog.e("ContextDeal::connectLocalAbility createAndroidIntent failed", new Object[0]);
            return false;
        } else if (createAndroidIntent.get().getComponent() == null) {
            AppLog.e("ContextDeal::connectLocalAbility ComponentName is null", new Object[0]);
            return false;
        } else {
            if (this.abilityConnectionMap.containsKey(iAbilityConnection)) {
                Object obj = this.abilityConnectionMap.get(iAbilityConnection);
                if (obj instanceof ServiceConnection) {
                    serviceConnection = (ServiceConnection) obj;
                } else {
                    AppLog.e("ContextDeal::connectAbility not ServiceConnection type", new Object[0]);
                    return false;
                }
            } else {
                serviceConnection = createServiceConnection(iAbilityConnection, abilityInfo2);
                this.abilityConnectionMap.put(iAbilityConnection, serviceConnection);
            }
            return this.abilityShellContext.bindService(createAndroidIntent.get(), serviceConnection, 1);
        }
    }

    private ServiceConnection createServiceConnection(final IAbilityConnection iAbilityConnection, final AbilityInfo abilityInfo2) {
        return new ServiceConnection() {
            /* class ohos.app.ContextDeal.AnonymousClass4 */

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                AppLog.i("ContextDeal::onServiceConnected", new Object[0]);
                Optional<ElementName> createElementName = IntentConverter.createElementName(componentName, abilityInfo2);
                if (!createElementName.isPresent()) {
                    AppLog.e("ContextDeal::onServiceConnected createElementName failed", new Object[0]);
                } else if (iBinder instanceof BinderAdapter) {
                    AppLog.i("is Binder Adapter", new Object[0]);
                    LocalRemoteObject localRemoteObject = ((BinderAdapter) iBinder).getLocalRemoteObject();
                    if (localRemoteObject != null) {
                        iAbilityConnection.onAbilityConnectDone(createElementName.get(), localRemoteObject, 0);
                    }
                } else {
                    C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "binderConverter_z");
                    Optional<IRemoteObject> translateToIRemoteObject = IPCAdapter.translateToIRemoteObject(iBinder);
                    C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "binderConverter_z");
                    translateToIRemoteObject.ifPresent(new Consumer(createElementName) {
                        /* class ohos.app.$$Lambda$ContextDeal$4$9YCHfjL1DmZfLApHSm023xqC3hs */
                        private final /* synthetic */ Optional f$1;

                        {
                            this.f$1 = r2;
                        }

                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            IAbilityConnection.this.onAbilityConnectDone((ElementName) this.f$1.get(), (IRemoteObject) obj, 0);
                        }
                    });
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                AppLog.i("ContextDeal::onServiceDisconnected", new Object[0]);
                Optional<ElementName> createElementName = IntentConverter.createElementName(componentName, abilityInfo2);
                if (!createElementName.isPresent()) {
                    AppLog.e("ContextDeal::onServiceConnected createElementName failed", new Object[0]);
                } else {
                    iAbilityConnection.onAbilityDisconnectDone(createElementName.get(), 0);
                }
            }
        };
    }

    private boolean connectRemoteAbility(AbilityShellData abilityShellData, Intent intent, IAbilityConnection iAbilityConnection) {
        HiTraceId begin = HiTrace.begin("connectRemoteAbility", 1);
        boolean connectRemoteAbilityInner = connectRemoteAbilityInner(abilityShellData, intent, iAbilityConnection);
        HiTrace.end(begin);
        return connectRemoteAbilityInner;
    }

    private boolean connectRemoteAbilityInner(AbilityShellData abilityShellData, Intent intent, IAbilityConnection iAbilityConnection) {
        DistributedConnection distributedConnection;
        AppLog.i("ContextDeal::connectRemoteAbility called", new Object[0]);
        AbilityInfo abilityInfo2 = abilityShellData.getAbilityInfo();
        if (this.abilityConnectionMap.containsKey(iAbilityConnection)) {
            Object obj = this.abilityConnectionMap.get(iAbilityConnection);
            if (obj instanceof DistributedConnection) {
                distributedConnection = (DistributedConnection) obj;
            } else {
                AppLog.e("ContextDeal::connectRemoteAbility not DistributedConnection type", new Object[0]);
                return false;
            }
        } else {
            DistributedConnection createDistributedConnection = createDistributedConnection(iAbilityConnection, new Handler(Looper.getMainLooper()));
            this.abilityConnectionMap.put(iAbilityConnection, createDistributedConnection);
            distributedConnection = createDistributedConnection;
        }
        int i = -1;
        try {
            i = this.distributedImpl.connectRemoteAbility(intent, abilityInfo2, distributedConnection.asObject());
        } catch (RemoteException e) {
            AppLog.e("ContextDeal::connectRemoteAbility RemoteException: %{public}s", e.getMessage());
        }
        if (i == 0) {
            return true;
        }
        AppLog.e("Context::connectRemoteAbility failed, errorCode is %{public}d", Integer.valueOf(i));
        return false;
    }

    private DistributedConnection createDistributedConnection(IAbilityConnection iAbilityConnection, Handler handler) {
        return new DistributedConnection(iAbilityConnection, handler);
    }

    private AbilityShellData selectFormAbility(Intent intent, ShellInfo.ShellType shellType) {
        List<AbilityInfo> queryAbilityByIntent = this.bundleMgrImpl.queryAbilityByIntent(intent);
        if (queryAbilityByIntent == null || queryAbilityByIntent.isEmpty()) {
            AppLog.e("ContextDeal::selectFormAbility failed", new Object[0]);
            return null;
        }
        AbilityInfo abilityInfo2 = queryAbilityByIntent.get(0);
        ShellInfo convertToFormShellInfo = AbilityShellConverterUtils.convertToFormShellInfo(abilityInfo2, shellType);
        if (convertToFormShellInfo != null) {
            return new AbilityShellData(true, abilityInfo2, convertToFormShellInfo);
        }
        AppLog.e("ContextDeal::selectFormAbility failed", new Object[0]);
        return null;
    }

    private void disconnectAbilityInner(HashMap<IAbilityConnection, Object> hashMap, IAbilityConnection iAbilityConnection) {
        Object obj = hashMap.get(iAbilityConnection);
        if (obj == null) {
            AppLog.e("ContextDeal::disconnectAbility IAbilityConnection not found", new Object[0]);
            return;
        }
        if (obj instanceof ServiceConnection) {
            disconnectLocalAbility(obj);
        }
        if (obj instanceof DistributedConnection) {
            long currentTimeMillis = System.currentTimeMillis();
            disconnectRemoteAbility(obj);
            AbilityInfo abilityInfo2 = this.abilityInfo;
            if (abilityInfo2 != null) {
                JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_DISCONNECT_REMOTE_ABILITY, abilityInfo2.getBundleName(), this.abilityInfo.getClassName(), currentTimeMillis);
            }
        }
        hashMap.remove(iAbilityConnection);
    }

    private void disconnectLocalAbility(Object obj) {
        if (!(obj instanceof ServiceConnection)) {
            AppLog.e("ContextDeal::disconnectLocalAbility param not ServiceConnection", new Object[0]);
        } else {
            this.abilityShellContext.unbindService((ServiceConnection) obj);
        }
    }

    private void disconnectRemoteAbility(Object obj) {
        if (!(obj instanceof IRemoteObject)) {
            AppLog.e("ContextDeal::disconnectRemoteAbility param not IRemoteObject", new Object[0]);
            return;
        }
        int i = -1;
        try {
            i = this.distributedImpl.disconnectRemoteAbility((IRemoteObject) obj);
        } catch (RemoteException e) {
            AppLog.e("ContextDeal::connectRemoteAbility RemoteException: %{public}s", e.getMessage());
        }
        checkDmsInterfaceResult(i, "disconnectRemoteAbility");
    }

    private void checkDmsInterfaceResult(int i, String str) {
        if (i != 0) {
            AppLog.e("ContextDeal::checkDmsInterfaceResult %{private}s failed, result is %{private}d", str, Integer.valueOf(i));
        }
    }

    private ElementName convertComponentNameToElementName(ComponentName componentName) {
        if (componentName == null) {
            return null;
        }
        ShellInfo shellInfo = new ShellInfo();
        shellInfo.setPackageName(componentName.getPackageName());
        shellInfo.setName(componentName.getClassName());
        shellInfo.setType(ShellInfo.ShellType.ACTIVITY);
        return IntentConverter.createElementName(null, AbilityShellConverterUtils.convertToAbilityInfo(shellInfo)).orElse(null);
    }

    @Override // ohos.app.Context
    public IBundleManager getBundleManager() {
        BundleManager instance = BundleManager.getInstance();
        if (instance != null) {
            instance.setContext(getApplicationContext());
        }
        return instance;
    }

    @Override // ohos.app.Context
    public boolean isUpdatingConfigurations() {
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            return ((Activity) context).isChangingConfigurations();
        }
        AppLog.i("ContextDeal::setTurnScreenOn, ability is not instance of AbilityShellActivity", new Object[0]);
        return false;
    }

    @Override // ohos.app.Context
    public String getAppType() {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager == null) {
            AppLog.e("get bundleManager failed", new Object[0]);
            return null;
        }
        try {
            return bundleManager.getAppType(getBundleName());
        } catch (RemoteException unused) {
            AppLog.e("remote exception", new Object[0]);
            return null;
        }
    }

    @Override // ohos.app.Context
    public Object getLayoutScatter() {
        return getContextLayoutScatter(this);
    }

    @Override // ohos.app.Context
    public Object getContextLayoutScatter(Context context) {
        if (this.layoutScatter == null) {
            this.layoutScatter = new LayoutScatter(context);
        }
        return this.layoutScatter;
    }

    @Override // ohos.app.Context
    public final boolean isAllowClassMap() {
        HapModuleInfo hapModuleInfo2 = this.hapModuleInfo;
        if (hapModuleInfo2 != null) {
            return hapModuleInfo2.isAllowClassMap();
        }
        return false;
    }

    @Override // ohos.app.Context
    public ResourceManager getResourceManager(Configuration configuration) {
        if (configuration != null) {
            if (this.resourceManagerInner == null) {
                this.resourceManagerInner = new ResourceManagerInner();
            }
            BundleInfo bundleInfo = this.application.getBundleInfo();
            Package r1 = HarmonyResources.getPackage(bundleInfo);
            if (r1 == null) {
                AppLog.e("ContextDeal::getResourceManager by configuration, getPackage is null", new Object[0]);
                return null;
            }
            ResourcePath[] resourcePath = HarmonyResources.getResourcePath(bundleInfo);
            if (resourcePath.length == 0) {
                AppLog.e("ContextDeal::getResourceManager by configuration, get resourcePaths is empty", new Object[0]);
                return null;
            }
            try {
                if (!this.resourceManagerInner.init(r1, resourcePath, configuration, ResourceUtils.convertToDeviceCapability(this.abilityShellContext.getResources().getConfiguration()))) {
                    AppLog.e("ContextDeal::getResourceManager by configuration, init failed", new Object[0]);
                    return null;
                }
            } catch (IOException e) {
                AppLog.e("ContextDeal::getResourceManager by configuration, exception: %{public}s", e.getMessage());
            }
            AppLog.d("ContextDeal::getResourceManager successfully", new Object[0]);
            return this.resourceManagerInner.getResourceManager();
        }
        throw new IllegalArgumentException("configuration must not be null");
    }

    @Override // ohos.app.Context
    public Object getLastStoredDataWhenConfigChanged() {
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            return ((Activity) context).getLastNonConfigurationInstance();
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::getLastStoreDataWhenConfigChange only support Activity", new Object[0]);
        return null;
    }

    @Override // ohos.app.Context
    public void printDrawnCompleted() {
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            ((Activity) context).reportFullyDrawn();
        } else {
            AppLog.w(APPKIT_LABEL, "ContextDeal::printDrawnCompleted only support Activity", new Object[0]);
        }
    }

    @Override // ohos.app.Context
    public Uri getCaller() {
        Context context = this.abilityShellContext;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.getReferrer() == null) {
                return null;
            }
            return UriConverter.convertToZidaneUri(activity.getReferrer());
        }
        AppLog.w(APPKIT_LABEL, "ContextDeal::getCaller only support Activity", new Object[0]);
        return null;
    }

    @Override // ohos.app.Context
    public void setColorMode(int i) {
        HapModuleInfo hapModuleInfo2 = this.hapModuleInfo;
        if (hapModuleInfo2 != null) {
            hapModuleInfo2.setColorMode(i);
        }
    }

    @Override // ohos.app.Context
    public int getColorMode() {
        HapModuleInfo hapModuleInfo2 = this.hapModuleInfo;
        if (hapModuleInfo2 != null) {
            return hapModuleInfo2.getColorMode();
        }
        return -1;
    }

    @Override // ohos.app.Context
    public void downloadAndInstall(Intent intent, InstallerCallback installerCallback) throws IllegalArgumentException {
        if (intent == null || installerCallback == null) {
            throw new IllegalArgumentException("intent or callback is null.");
        } else if (intent.getBundle() == null || intent.getElement() == null || intent.getElement().getAbilityName() == null) {
            throw new IllegalArgumentException("bundle name or ability name is null.");
        } else {
            intent.addFlags(2048);
            startFreeInstallAbility(intent, 0, AbilityStartSetting.getEmptySetting(), installerCallback);
        }
    }

    private void startFreeInstallAbility(final Intent intent, final int i, final AbilityStartSetting abilityStartSetting, final InstallerCallback installerCallback) {
        intent.setParam(KEY_START_TIME, System.nanoTime());
        if (intent.getBundle() == null || intent.getBundle().isEmpty() || intent.getElement() == null || intent.getElement().getAbilityName() == null || intent.getElement().getAbilityName().isEmpty()) {
            AppLog.e(APPKIT_LABEL, "bundle name or ability name is null or empty.", new Object[0]);
            return;
        }
        String deviceId = intent.getElement().getDeviceId();
        if (deviceId.isEmpty() || deviceId.equals(this.application.getLocalDeviceId())) {
            getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(new Runnable() {
                /* class ohos.app.ContextDeal.AnonymousClass6 */

                public void run() {
                    String bundle = intent.getBundle();
                    String abilityName = intent.getElement().getAbilityName();
                    HiViewUtil.sendAbilityStartup(bundle, abilityName, 1);
                    long longParam = intent.getLongParam(ContextDeal.KEY_START_TIME, 0);
                    Intent intent = new Intent(intent);
                    List abilityShellDatasLocal = ContextDeal.this.getAbilityShellDatasLocal(intent);
                    long nanoTime = System.nanoTime() - longParam;
                    AppLog.i(ContextDeal.APPKIT_LABEL, "ContextDeal::startFreeInstallAbility query local bundle %{public}s/%{public}s, size:%{public}d, cost: %{public}d", bundle, abilityName, Integer.valueOf(abilityShellDatasLocal.size()), Long.valueOf(nanoTime));
                    if (abilityShellDatasLocal.size() > 1) {
                        AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::startFreeInstallAbility getAbilityShellDatasLocal error, result size(=%{public}d) > 1", Integer.valueOf(abilityShellDatasLocal.size()));
                        HiViewUtil.sendAbilityStartResult(bundle, abilityName, 0, 1, 1);
                        InstallerCallback installerCallback = installerCallback;
                        if (installerCallback != null) {
                            installerCallback.onFinished(64, "ability not found.");
                        }
                    } else if (abilityShellDatasLocal.size() != 1) {
                        InstallerCallback installerCallback2 = installerCallback;
                        if (installerCallback2 == null) {
                            installerCallback2 = ContextDeal.this.createSilentInstallPageAbilityCallback(intent, i, abilityStartSetting);
                        }
                        if (!ContextDeal.this.silentInstall(intent, installerCallback2)) {
                            AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::startFreeInstallAbility silentInstall fail", new Object[0]);
                            HiViewUtil.sendDownloadAndInstallResult(bundle, 1);
                        }
                    } else if (!ContextDeal.this.upgradeCheckAndInstallPageAbility(intent, (AbilityShellData) abilityShellDatasLocal.get(0), i, abilityStartSetting, installerCallback)) {
                        HiViewUtil.sendDownloadAndInstallResult(bundle, 1);
                    }
                }
            });
            return;
        }
        try {
            this.distributedImpl.startRemoteFreeInstallAbility(intent, new RemoteFreeInstallCallback() {
                /* class ohos.app.ContextDeal.AnonymousClass5 */

                @Override // ohos.abilityshell.IRemoteFreeInstallCallback
                public void onResult(int i) {
                    if (i != 0) {
                        AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::RemoteFreeInstallCallback onResultresultCode:%{public}d", Integer.valueOf(i));
                    }
                    AppLog.i(ContextDeal.APPKIT_LABEL, "RemoteFreeInstallCallback successfully.", new Object[0]);
                }
            });
        } catch (RemoteException e) {
            AppLog.e(APPKIT_LABEL, "startRemoteFreeInstallAbility RemoteException: %{public}s", e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean upgradeCheckAndInstallPageAbility(Intent intent, AbilityShellData abilityShellData, int i, AbilityStartSetting abilityStartSetting, InstallerCallback installerCallback) {
        AbilityInfo abilityInfo2 = abilityShellData.getAbilityInfo();
        if (abilityInfo2 == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheckAndInstallPageAbility abilityInfo is null", new Object[0]);
            return false;
        }
        String bundle = intent.getBundle();
        String abilityName = intent.getElement().getAbilityName();
        if (abilityInfo2.getModuleName().isEmpty()) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheckAndInstallPageAbility moduleName is null or empty", new Object[0]);
            return false;
        }
        int moduleUpgradeFlag = getModuleUpgradeFlag(bundle, abilityName);
        if (moduleUpgradeFlag == 1 || moduleUpgradeFlag == 2) {
            if (installerCallback == null) {
                installerCallback = createUpgradeInstallPageAbilityCallback(intent, i, abilityStartSetting);
            }
            if (!upgradeInstall(intent, moduleUpgradeFlag, installerCallback)) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheckAndInstallPageAbility upgradeInstall fail", new Object[0]);
                installerCallback.onFinished(5, "");
            }
        } else {
            startLocalAbility(abilityShellData, intent, i, abilityStartSetting);
            if (installerCallback == null) {
                installerCallback = createUpgradeCheckCallback();
            }
            if (!upgradeCheck(intent, installerCallback)) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheckAndInstallPageAbility upgradeCheck fail", new Object[0]);
                installerCallback.onFinished(5, "");
            }
        }
        return true;
    }

    private boolean connectFreeInstallAbility(final Intent intent, final IAbilityConnection iAbilityConnection) {
        if (intent == null || intent.getBundle() == null || intent.getBundle().isEmpty() || intent.getElement() == null || intent.getElement().getAbilityName() == null || intent.getElement().getAbilityName().isEmpty()) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::connectFreeInstallAbility bundle name or ability name is null or empty.", new Object[0]);
            return false;
        }
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(new Runnable() {
            /* class ohos.app.ContextDeal.AnonymousClass7 */

            public void run() {
                String bundle = intent.getBundle();
                String abilityName = intent.getElement().getAbilityName();
                Intent intent = new Intent(intent);
                List abilityShellDatasLocal = ContextDeal.this.getAbilityShellDatasLocal(intent);
                AppLog.i(ContextDeal.APPKIT_LABEL, "ContextDeal::connectFreeInstallAbility query local bundleName:%{public}s abilityName:%{public}s,size:%{public}d", bundle, abilityName, Integer.valueOf(abilityShellDatasLocal.size()));
                if (abilityShellDatasLocal.size() > 1) {
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::connectFreeInstallAbility getAbilityShellDatasLocal error,result size(=%{public}d) > 1", Integer.valueOf(abilityShellDatasLocal.size()));
                } else if (abilityShellDatasLocal.size() == 1) {
                    ContextDeal.this.upgradeCheckAndUpgradeServiceAbility(intent, (AbilityShellData) abilityShellDatasLocal.get(0), iAbilityConnection);
                } else {
                    if (!ContextDeal.this.silentInstall(intent, ContextDeal.this.createSilentInstallServiceAbilityCallback(intent, iAbilityConnection))) {
                        AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::connectFreeInstallAbility silentInstall fail", new Object[0]);
                    }
                }
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean upgradeCheckAndUpgradeServiceAbility(Intent intent, AbilityShellData abilityShellData, IAbilityConnection iAbilityConnection) {
        AbilityInfo abilityInfo2 = abilityShellData.getAbilityInfo();
        if (abilityInfo2 == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::checkAndUpgradeServiceAbility abilityInfo is null", new Object[0]);
            return false;
        }
        String bundle = intent.getBundle();
        String abilityName = intent.getElement().getAbilityName();
        if (abilityInfo2.getModuleName().isEmpty()) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheckAndUpgradeServiceAbility moduleName is null or empty", new Object[0]);
            return false;
        }
        int moduleUpgradeFlag = getModuleUpgradeFlag(bundle, abilityName);
        if (moduleUpgradeFlag == 1 || moduleUpgradeFlag == 2) {
            InstallerCallback createUpgradeInstallServiceAbilityCallback = createUpgradeInstallServiceAbilityCallback(intent, iAbilityConnection);
            if (!upgradeInstall(intent, moduleUpgradeFlag, createUpgradeInstallServiceAbilityCallback)) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheckAndUpgradeServiceAbility upgradeInstall fail", new Object[0]);
                createUpgradeInstallServiceAbilityCallback.onFinished(5, "");
            }
        } else {
            connectLocalAbility(abilityShellData, intent, iAbilityConnection);
            InstallerCallback createUpgradeCheckCallback = createUpgradeCheckCallback();
            if (!upgradeCheck(intent, createUpgradeCheckCallback)) {
                AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheckAndUpgradeServiceAbility upgradeCheck fail", new Object[0]);
                createUpgradeCheckCallback.onFinished(5, "");
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startLocalAbility(Intent intent, int i, AbilityStartSetting abilityStartSetting) {
        List<AbilityShellData> abilityShellDatasLocal = getAbilityShellDatasLocal(intent);
        if (abilityShellDatasLocal.size() != 1) {
            AppLog.i(APPKIT_LABEL, "ContextDeal::startLocalAbility fail, ability size:%{public}d after silentInstall", Integer.valueOf(abilityShellDatasLocal.size()));
            return;
        }
        startLocalAbility(abilityShellDatasLocal.get(0), intent, i, abilityStartSetting);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private InstallerCallback createSilentInstallPageAbilityCallback(final Intent intent, final int i, final AbilityStartSetting abilityStartSetting) {
        return new InstallerCallback() {
            /* class ohos.app.ContextDeal.AnonymousClass8 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                long longParam = intent.getLongParam(ContextDeal.KEY_START_TIME, 0);
                AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::silentInstall done, cost: %{pulic}d", Long.valueOf(System.nanoTime() - longParam));
                if (i == 0) {
                    ContextDeal.this.startLocalAbility((ContextDeal) intent, (Intent) i, (int) abilityStartSetting);
                    long nanoTime = System.nanoTime() - longParam;
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::startLocalAbility done, total cost: %{pulic}d", Long.valueOf(nanoTime));
                    return;
                }
                AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::silentInstall onFinished resultCode:%{public}d, resultMsg:%{public}s", Integer.valueOf(i), str);
            }
        };
    }

    private InstallerCallback createUpgradeInstallPageAbilityCallback(final Intent intent, final int i, final AbilityStartSetting abilityStartSetting) {
        return new InstallerCallback() {
            /* class ohos.app.ContextDeal.AnonymousClass9 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                long longParam = intent.getLongParam(ContextDeal.KEY_START_TIME, 0);
                AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::upgradeInstall done, cost: %{pulic}d", Long.valueOf(System.nanoTime() - longParam));
                if (i != 0) {
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::upgradeInstall onFinished resultCode:%{public}d,resultMsg:%{public}s", Integer.valueOf(i), str);
                }
                ContextDeal.this.startLocalAbility((ContextDeal) intent, (Intent) i, (int) abilityStartSetting);
                long nanoTime = System.nanoTime() - longParam;
                AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::startLocalAbility done, total cost: %{pulic}d", Long.valueOf(nanoTime));
            }
        };
    }

    private InstallerCallback createUpgradeCheckCallback() {
        return new InstallerCallback() {
            /* class ohos.app.ContextDeal.AnonymousClass10 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                if (i != 0) {
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::upgradeCheck onFinished resultCode:%{public}d", Integer.valueOf(i));
                }
            }
        };
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean connectLocalAbility(Intent intent, IAbilityConnection iAbilityConnection) {
        AbilityShellData abilityShellData;
        if (isFlagExists(32, intent.getFlags())) {
            abilityShellData = selectFormAbility(intent, ShellInfo.ShellType.SERVICE);
        } else {
            abilityShellData = getAbilityShellDataLocal(intent);
        }
        if (abilityShellData == null) {
            AppLog.e("ContextDeal::connectLocalAbility selectAbility failed", new Object[0]);
            return false;
        }
        AbilityInfo abilityInfo2 = this.abilityInfo;
        if (abilityInfo2 != null) {
            intent.setParam(CALLER_BUNDLE_NAME, abilityInfo2.getBundleName());
        }
        return connectLocalAbility(abilityShellData, intent, iAbilityConnection);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private InstallerCallback createSilentInstallServiceAbilityCallback(final Intent intent, final IAbilityConnection iAbilityConnection) {
        return new InstallerCallback() {
            /* class ohos.app.ContextDeal.AnonymousClass11 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                if (i != 0) {
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::silentInstall onFinished resultCode:%{public}d,resultMsg:%{public}s", Integer.valueOf(i), str);
                } else if (!ContextDeal.this.connectLocalAbility(intent, iAbilityConnection)) {
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::silentInstall onFinished connectLocalAbility failed", new Object[0]);
                }
            }
        };
    }

    private InstallerCallback createUpgradeInstallServiceAbilityCallback(final Intent intent, final IAbilityConnection iAbilityConnection) {
        return new InstallerCallback() {
            /* class ohos.app.ContextDeal.AnonymousClass12 */

            @Override // ohos.bundle.IInstallerCallback, ohos.bundle.InstallerCallback
            public void onFinished(int i, String str) {
                if (i != 0) {
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::upgradeInstall onFinished resultCode:%{public}d, resultMsg:%{public}s", Integer.valueOf(i), str);
                }
                if (!ContextDeal.this.connectLocalAbility(intent, iAbilityConnection)) {
                    AppLog.e(ContextDeal.APPKIT_LABEL, "ContextDeal::upgradeInstall onFinished failed", new Object[0]);
                }
            }
        };
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean silentInstall(Intent intent, InstallerCallback installerCallback) {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::silentInstall getBundleManager failed", new Object[0]);
            return false;
        }
        try {
            return bundleManager.silentInstall(intent, installerCallback);
        } catch (RemoteException e) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::silentInstall failed, errormsg = %{public}s", e.getMessage());
            return false;
        }
    }

    private boolean upgradeInstall(Intent intent, int i, InstallerCallback installerCallback) {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::getBundleInstaller getBundleManager failed", new Object[0]);
            return false;
        }
        try {
            return bundleManager.upgradeInstall(intent, i, installerCallback);
        } catch (RemoteException e) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeInstall failed, errormsg = %{public}s", e.getMessage());
            return false;
        }
    }

    private boolean upgradeCheck(Intent intent, InstallerCallback installerCallback) {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheck getBundleManager failed", new Object[0]);
            return false;
        }
        try {
            return bundleManager.upgradeCheck(intent, installerCallback);
        } catch (RemoteException e) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::upgradeCheck failed, errormsg = %{public}s", e.getMessage());
            return false;
        }
    }

    private int getModuleUpgradeFlag(String str, String str2) {
        IBundleManager bundleManager = getBundleManager();
        if (bundleManager == null) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::getModuleUpgradeFlag getBundleManager failed", new Object[0]);
            return -1;
        }
        try {
            int moduleUpgradeFlag = bundleManager.getModuleUpgradeFlag(str, str2);
            AppLog.i(APPKIT_LABEL, "ContextDeal::getModuleUpgradeFlag bundleName:%{public}s, moduleName:%{public}s,flag:%{public}d", str, str2, Integer.valueOf(moduleUpgradeFlag));
            return moduleUpgradeFlag;
        } catch (RemoteException e) {
            AppLog.e(APPKIT_LABEL, "ContextDeal::getModuleUpgradeFlag failed, errormsg = %{public}s", e.getMessage());
            return -1;
        }
    }

    public String getAppLabel(String str) throws IllegalArgumentException {
        if (this.abilityShellContext == null || StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("no bundle name.");
        }
        try {
            BundleInfo bundleInfo = getBundleInfo(str);
            if (bundleInfo == null) {
                AppLog.e(APPKIT_LABEL, "bundle: %{public}s is not a valid harmony os app.", str);
                return null;
            }
            if (bundleInfo.isDifferentName()) {
                AppLog.d(APPKIT_LABEL, "use original bundle name: %{public}s.", bundleInfo.getOriginalName());
                str = bundleInfo.getOriginalName();
            }
            PackageManager packageManager = this.abilityShellContext.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(packageManager.getPackageInfo(str, 0).applicationInfo));
        } catch (Throwable th) {
            AppLog.e(APPKIT_LABEL, "getAppLabel failed, msg = %{public}s", th.getMessage());
            return null;
        }
    }
}
