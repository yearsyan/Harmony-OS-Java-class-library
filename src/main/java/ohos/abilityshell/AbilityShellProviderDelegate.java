package ohos.abilityshell;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import libcore.io.Libcore;
import ohos.aafwk.ability.DataAbilityOperation;
import ohos.aafwk.ability.DataAbilityResult;
import ohos.aafwk.ability.OperationExecuteException;
import ohos.abilityshell.utils.AbilityShellConverterUtils;
import ohos.abilityshell.utils.IntentConverter;
import ohos.abilityshell.utils.LifecycleState;
import ohos.app.ContextDeal;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.ShellInfo;
import ohos.data.dataability.ContentProviderConverter;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.dataability.RemoteDataAbility;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.global.resource.RawFileDescriptor;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.UriConverter;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IRemoteObject;
import ohos.tools.C0000Bytrace;
import ohos.utils.PacMap;
import ohos.utils.adapter.PacMapUtils;

public class AbilityShellProviderDelegate extends AbilityShellDelegate {
    private static final String GET_REMOTE_DATA_ABILITY = "DMS_GetRemoteDataAbility";
    private static final String GET_REMOTE_DATA_ABILITY_KEY = "RemoteDataAbility";
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private static final int WAIT_PROVIDER_CREATE_DONE_TIMEOUT = 2000;
    private Object abilityShell;
    private ContextDeal contextDeal;
    private final CountDownLatch createLatch = new CountDownLatch(1);
    private ShellInfo shellInfo;

    public AbilityShellProviderDelegate(Object obj) {
        this.abilityShell = obj;
    }

    public boolean onCreate() {
        this.abilityInfo = AbilityShellConverterUtils.convertToAbilityInfo(this.shellInfo);
        if (this.abilityInfo == null) {
            AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::onCreate could not find ability info from bms, stop start!", new Object[0]);
            return false;
        }
        HarmonyApplication.registerDataAbility(new Runnable() {
            /* class ohos.abilityshell.$$Lambda$AbilityShellProviderDelegate$ZN38_YHwuO9SXx31gArK7O8emEU */

            public final void run() {
                AbilityShellProviderDelegate.this.lambda$onCreate$0$AbilityShellProviderDelegate();
            }
        });
        return true;
    }

    public /* synthetic */ void lambda$onCreate$0$AbilityShellProviderDelegate() {
        AppLog.i(SHELL_LABEL, "AbilityShellProviderDelegate::onCreate registerDataAbility", new Object[0]);
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "create Data Ability");
        BundleInfo bundleInfo = HarmonyApplication.getInstance().getBundleInfo();
        if (bundleInfo.isDifferentName()) {
            this.abilityInfo.setClassName(this.abilityInfo.getClassName().replaceFirst(bundleInfo.getOriginalName(), bundleInfo.getName()));
            AppLog.d(SHELL_LABEL, "AbilityShellProviderDelegate::onCreate ability class name %{private}s", this.abilityInfo.getClassName());
        }
        AbilityInfo abilityInfoByName = bundleInfo.getAbilityInfoByName(this.abilityInfo.getClassName());
        if (abilityInfoByName != null) {
            this.abilityInfo = abilityInfoByName;
        }
        checkHapHasLoaded(this.abilityInfo);
        this.contextDeal = createProviderContextdeal(this.abilityInfo);
        if (this.abilityShell instanceof ContentProvider) {
            loadAbility(this.abilityInfo, this.contextDeal, this.abilityShell);
            HarmonyApplication.getInstance().waitForUserApplicationStart();
            scheduleAbilityLifecycle(null, LifecycleState.AbilityState.INACTIVE_STATE.getValue());
            this.createLatch.countDown();
        }
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "create Data Ability");
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider query");
            DataAbilityPredicates selectionToPredicates = selectionToPredicates(uri, str, strArr2, str2);
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability query");
            ResultSet query = this.ability.query(convertToZidaneContentUri, strArr, selectionToPredicates);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability query");
            if (query == null) {
                AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::query failed", new Object[0]);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider query");
                return null;
            }
            Cursor resultSetToCursor = ContentProviderConverter.resultSetToCursor(query);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider query");
            return resultSetToCursor;
        }
        throw new IllegalStateException("ability is uninitialized, query failed");
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider insert");
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            ValuesBucket contentValuesToValuesBucket = ContentProviderConverter.contentValuesToValuesBucket(contentValues);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability insert");
            int insert = this.ability.insert(convertToZidaneContentUri, contentValuesToValuesBucket);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability insert");
            Uri convertIndexToUri = convertIndexToUri(uri, insert);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider insert");
            return convertIndexToUri;
        }
        throw new IllegalStateException("ability is uninitialized, insert failed");
    }

    public int delete(Uri uri, String str, String[] strArr) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider delete");
            DataAbilityPredicates selectionToPredicates = selectionToPredicates(uri, str, strArr, null);
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability delete");
            int delete = this.ability.delete(convertToZidaneContentUri, selectionToPredicates);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability delete");
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider delete");
            return delete;
        }
        throw new IllegalStateException("ability is uninitialized, delete failed");
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider update");
            DataAbilityPredicates selectionToPredicates = selectionToPredicates(uri, str, strArr, null);
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            ValuesBucket contentValuesToValuesBucket = ContentProviderConverter.contentValuesToValuesBucket(contentValues);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability update");
            int update = this.ability.update(convertToZidaneContentUri, contentValuesToValuesBucket, selectionToPredicates);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability update");
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider update");
            return update;
        }
        throw new IllegalStateException("ability is uninitialized, update failed");
    }

    public String getType(Uri uri) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider getType");
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability getType");
            String type = this.ability.getType(convertToZidaneContentUri);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability getType");
            if (type == null) {
                AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::getType failed", new Object[0]);
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider getType");
            return type;
        }
        throw new IllegalStateException("ability is uninitialized, getType failed");
    }

    public String[] getStreamTypes(Uri uri, String str) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider getFileTypes");
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability getFileTypes");
            String[] fileTypes = this.ability.getFileTypes(convertToZidaneContentUri, str);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability getFileTypes");
            if (fileTypes == null) {
                AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::getStreamTypes failed", new Object[0]);
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider getFileTypes");
            return fileTypes;
        }
        throw new IllegalStateException("ability is uninitialized, getFileTypes failed");
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openFile");
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability openFile");
                FileDescriptor openFile = this.ability.openFile(convertToZidaneContentUri, str);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability openFile");
                ParcelFileDescriptor parcelFileDescriptor = null;
                if (openFile != null) {
                    parcelFileDescriptor = new ParcelFileDescriptor(openFile);
                }
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openFile");
                return parcelFileDescriptor;
            } catch (FileNotFoundException e) {
                AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::openFile FileNotFoundException occur", new Object[0]);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openFile");
                throw e;
            }
        } else {
            throw new IllegalStateException("ability is uninitialized, openFile failed");
        }
    }

    public AssetFileDescriptor openAssetFile(Uri uri, String str) throws FileNotFoundException {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openAssetFile");
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability openRawFile");
                RawFileDescriptor openRawFile = this.ability.openRawFile(convertToZidaneContentUri, str);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability openRawFile");
                AssetFileDescriptor assetFileDescriptor = null;
                if (!(openRawFile == null || openRawFile.getFileDescriptor() == null)) {
                    try {
                        AssetFileDescriptor assetFileDescriptor2 = new AssetFileDescriptor(new ParcelFileDescriptor(Libcore.os.dup(openRawFile.getFileDescriptor())), openRawFile.getStartPosition(), openRawFile.getFileSize());
                        try {
                            Libcore.os.close(openRawFile.getFileDescriptor());
                            assetFileDescriptor = assetFileDescriptor2;
                        } catch (ErrnoException unused) {
                            assetFileDescriptor = assetFileDescriptor2;
                            AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::openAssetFile dup FileDescriptor error", new Object[0]);
                            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openAssetFile");
                            return assetFileDescriptor;
                        }
                    } catch (ErrnoException unused2) {
                        AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::openAssetFile dup FileDescriptor error", new Object[0]);
                        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openAssetFile");
                        return assetFileDescriptor;
                    }
                }
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openAssetFile");
                return assetFileDescriptor;
            } catch (FileNotFoundException e) {
                AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::openAssetFile FileNotFoundException occur", new Object[0]);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider openAssetFile");
                throw e;
            }
        } else {
            throw new IllegalStateException("ability is uninitialized, openRawFile failed");
        }
    }

    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        waitOnCreateDone();
        if (this.ability != null) {
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            int length = contentValuesArr.length;
            ValuesBucket[] valuesBucketArr = new ValuesBucket[length];
            for (int i = 0; i < length; i++) {
                valuesBucketArr[i] = ContentProviderConverter.contentValuesToValuesBucket(contentValuesArr[i]);
            }
            return this.ability.batchInsert(convertToZidaneContentUri, valuesBucketArr);
        }
        throw new IllegalStateException("ability is uninitialized, batchInsert failed");
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        PacMap pacMap;
        Bundle bundle2;
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider call");
            if (GET_REMOTE_DATA_ABILITY.equals(str)) {
                AppLog.i(SHELL_LABEL, "AbilityShellProviderDelegate::call getRemoteDataAbility", new Object[0]);
                Optional<Object> translateToIBinder = IPCAdapter.translateToIBinder(createRemoteDataAbility());
                if (!translateToIBinder.isPresent()) {
                    AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::call createAndroidBinder failed", new Object[0]);
                    C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider call");
                    return null;
                }
                Bundle bundle3 = new Bundle();
                if (translateToIBinder.get() instanceof IBinder) {
                    bundle3.putBinder(GET_REMOTE_DATA_ABILITY_KEY, (IBinder) translateToIBinder.get());
                }
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider call");
                return bundle3;
            }
            boolean isParcelledDataNull = isParcelledDataNull(bundle);
            if (bundle == null) {
                pacMap = null;
            } else if (isParcelledDataNull) {
                pacMap = PacMapUtils.convertFromBundle(bundle);
            } else {
                bundle.setClassLoader(HarmonyApplication.getInstance().getClassLoader());
                pacMap = IntentConverter.convertBundleToPacMap(bundle).orElse(new PacMap());
            }
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability call");
            PacMap call = this.ability.call(str, str2, pacMap);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability call");
            if (call == null) {
                AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::call failed", new Object[0]);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider call");
                return null;
            }
            if (isParcelledDataNull) {
                bundle2 = PacMapUtils.convertIntoBundle(call);
            } else {
                bundle2 = IntentConverter.convertPacMapToBundle(call).orElse(new Bundle());
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider call");
            return bundle2;
        }
        throw new IllegalStateException("ability is uninitialized, call failed");
    }

    private boolean isParcelledDataNull(Bundle bundle) {
        if (bundle == null) {
            return true;
        }
        try {
            Field declaredField = bundle.getClass().getSuperclass().getDeclaredField("mParcelledData");
            declaredField.setAccessible(true);
            return declaredField.get(bundle) == null;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::isParcelledDataNull error: %{private}s", e.getMessage());
            return true;
        }
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arrayList) throws OperationApplicationException {
        waitOnCreateDone();
        if (this.ability != null) {
            ArrayList<DataAbilityOperation> arrayList2 = new ArrayList<>();
            Iterator<ContentProviderOperation> it = arrayList.iterator();
            while (it.hasNext()) {
                arrayList2.add(AbilityContentProviderConverter.contentProviderOperationToDataAbilityOperation(it.next()));
            }
            try {
                DataAbilityResult[] executeBatch = this.ability.executeBatch(arrayList2);
                int length = executeBatch.length;
                ContentProviderResult[] contentProviderResultArr = new ContentProviderResult[length];
                for (int i = 0; i < length; i++) {
                    contentProviderResultArr[i] = AbilityContentProviderConverter.dataAbilityResultToContentProviderResult(executeBatch[i]);
                }
                return contentProviderResultArr;
            } catch (OperationExecuteException e) {
                AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::applyBatch failed", new Object[0]);
                throw new OperationApplicationException(e.getMessage(), e.getCause());
            }
        } else {
            throw new IllegalStateException("ability is uninitialized, executeBatch failed");
        }
    }

    public void createProviderShellInfo(String str) {
        this.shellInfo = new ShellInfo();
        this.shellInfo.setPackageName(str);
        this.shellInfo.setName(this.abilityShell.getClass().getName());
        this.shellInfo.setType(ShellInfo.ShellType.PROVIDER);
    }

    public void onTrimMemory(int i) {
        waitOnCreateDone();
        if (this.ability != null) {
            this.ability.onMemoryLevel(i);
        }
    }

    public Uri canonicalize(Uri uri) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider normalizeUri");
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability normalizeUri");
            ohos.utils.net.Uri normalizeUri = this.ability.normalizeUri(convertToZidaneContentUri);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability normalizeUri");
            Uri uri2 = null;
            if (normalizeUri != null) {
                uri2 = UriConverter.convertToAndroidContentUri(normalizeUri);
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider normalizeUri");
            return uri2;
        }
        throw new IllegalStateException("ability is uninitialized, normalizeUri failed");
    }

    public Uri uncanonicalize(Uri uri) {
        waitOnCreateDone();
        if (this.ability != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider denormalizeUri");
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability denormalizeUri");
            ohos.utils.net.Uri denormalizeUri = this.ability.denormalizeUri(convertToZidaneContentUri);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability denormalizeUri");
            Uri uri2 = null;
            if (denormalizeUri != null) {
                uri2 = UriConverter.convertToAndroidContentUri(denormalizeUri);
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ShellProvider denormalizeUri");
            return uri2;
        }
        throw new IllegalStateException("ability is uninitialized, denormalizeUri failed");
    }

    /* access modifiers changed from: protected */
    @Override // ohos.abilityshell.AbilityShellDelegate
    public void updateConfiguration(Configuration configuration) {
        waitOnCreateDone();
        super.updateConfiguration(configuration);
    }

    public boolean reload(Uri uri, Bundle bundle) {
        waitOnCreateDone();
        if (this.ability != null) {
            ohos.utils.net.Uri convertToZidaneContentUri = UriConverter.convertToZidaneContentUri(uri, "");
            PacMap pacMap = null;
            if (bundle != null) {
                if (isParcelledDataNull(bundle)) {
                    pacMap = PacMapUtils.convertFromBundle(bundle);
                } else {
                    bundle.setClassLoader(HarmonyApplication.getInstance().getClassLoader());
                    pacMap = IntentConverter.convertBundleToPacMap(bundle).orElse(new PacMap());
                }
            }
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability reload");
            boolean reload = this.ability.reload(convertToZidaneContentUri, pacMap);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ability reload");
            return reload;
        }
        throw new IllegalStateException("ability is uninitialized, reload failed");
    }

    private IRemoteObject createRemoteDataAbility() {
        Context context = ((ContentProvider) this.abilityShell).getContext();
        try {
            ProviderInfo providerInfo = context.getPackageManager().getProviderInfo(new ComponentName(context.getPackageName(), this.abilityShell.getClass().getName()), 0);
            return new RemoteDataAbility.Builder(this.ability).context(context).permission(this.contextDeal, "", providerInfo.readPermission, providerInfo.writePermission).build();
        } catch (PackageManager.NameNotFoundException unused) {
            AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::createRemoteDataAbility NameNotFoundException occur", new Object[0]);
            return null;
        }
    }

    private Uri convertIndexToUri(Uri uri, int i) {
        if (i < 0) {
            return null;
        }
        return Uri.withAppendedPath(uri, String.valueOf(i));
    }

    private void waitOnCreateDone() {
        try {
            if (!this.createLatch.await(2000, TimeUnit.MILLISECONDS)) {
                AppLog.w(SHELL_LABEL, "AbilityShellProviderDelegate::waitOnCreateDone exceed time", new Object[0]);
            }
        } catch (InterruptedException unused) {
            AppLog.e(SHELL_LABEL, "AbilityShellProviderDelegate::waitOnCreateDone InterruptedException occur", new Object[0]);
        }
    }

    private DataAbilityPredicates selectionToPredicates(Uri uri, String str, String[] strArr, String str2) {
        if (str == null || strArr == null) {
            return null;
        }
        DataAbilityPredicates selectionToDataAbilityPredicates = ContentProviderConverter.selectionToDataAbilityPredicates(str, strArr);
        if (str2 == null) {
            return selectionToDataAbilityPredicates;
        }
        selectionToDataAbilityPredicates.setOrder(str2);
        return selectionToDataAbilityPredicates;
    }

    private ContextDeal createProviderContextdeal(AbilityInfo abilityInfo) {
        ContentProvider contentProvider = (ContentProvider) this.abilityShell;
        ClassLoader classLoaderByAbilityInfo = HarmonyApplication.getInstance().getClassLoaderByAbilityInfo(abilityInfo);
        if (classLoaderByAbilityInfo == null) {
            classLoaderByAbilityInfo = contentProvider.getContext().getClassLoader();
        }
        ContextDeal contextDeal2 = new ContextDeal(contentProvider.getContext(), classLoaderByAbilityInfo);
        contextDeal2.setAbilityInfo(abilityInfo);
        contextDeal2.setHapModuleInfo(HarmonyApplication.getInstance().getHapModuleInfoByAbilityInfo(abilityInfo));
        contextDeal2.setApplication(HarmonyApplication.getInstance().getApplication());
        HarmonyApplication.getInstance().getApplication().addAbilityRecord(this.abilityShell, contextDeal2);
        return contextDeal2;
    }
}
