package ohos.abilityshell;

import android.app.UriGrantsManager;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.OperationCanceledException;
import android.os.RemoteException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ohos.aafwk.ability.DataAbilityOperation;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.ability.DataAbilityResult;
import ohos.aafwk.ability.IDataAbility;
import ohos.aafwk.ability.IDataAbilityObserver;
import ohos.aafwk.ability.OperationExecuteException;
import ohos.abilityshell.utils.IntentConverter;
import ohos.appexecfwk.utils.AppLog;
import ohos.data.dataability.ContentProviderConverter;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.global.innerkit.asset.AfdAdapter;
import ohos.global.resource.RawFileDescriptor;
import ohos.global.resource.RawFileDescriptorImpl;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.UriConverter;
import ohos.tools.C0000Bytrace;
import ohos.utils.PacMap;
import ohos.utils.adapter.PacMapUtils;
import ohos.utils.net.Uri;

public class ApplicationDataAbility implements IDataAbility {
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private Context abilityContext;
    private Map<Object, AbilityShellObserver> observerMap;
    private ContentProviderClient providerClient;

    public ApplicationDataAbility() {
        this(null, null);
    }

    public ApplicationDataAbility(Context context, ContentProviderClient contentProviderClient) {
        this.observerMap = new HashMap();
        this.abilityContext = null;
        this.providerClient = null;
        this.abilityContext = context;
        this.providerClient = contentProviderClient;
    }

    public static ApplicationDataAbility creator(Object obj, Uri uri, boolean z) {
        ContentProviderClient contentProviderClient;
        if (obj instanceof Context) {
            Context context = (Context) obj;
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "acquireContentProviderClient");
            if (z) {
                contentProviderClient = context.getContentResolver().acquireContentProviderClient(convertToAndroidContentUri);
            } else {
                contentProviderClient = context.getContentResolver().acquireUnstableContentProviderClient(convertToAndroidContentUri);
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "acquireContentProviderClient");
            if (contentProviderClient != null) {
                return new ApplicationDataAbility(context, contentProviderClient);
            }
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::creator ApplicationDataAbility was null", new Object[0]);
            return null;
        } else if (obj instanceof ContentProvider) {
            AppLog.d(SHELL_LABEL, "DataAbility server side call getDataAbility method", new Object[0]);
            return new ApplicationDataAbility(((ContentProvider) obj).getContext(), null);
        } else {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::creator abilityShell not Context type", new Object[0]);
            return new ApplicationDataAbility();
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public ResultSet query(Uri uri, String[] strArr, DataAbilityPredicates dataAbilityPredicates) throws DataAbilityRemoteException {
        String str;
        String str2;
        String[] strArr2;
        String[] strArr3;
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::query client not ready", new Object[0]);
            return null;
        } else if (uri == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::query param invalid", new Object[0]);
            return null;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            if (dataAbilityPredicates != null) {
                String dataAbilityPredicatesToSelection = ContentProviderConverter.dataAbilityPredicatesToSelection(dataAbilityPredicates);
                List<String> whereArgs = dataAbilityPredicates.getWhereArgs();
                if (whereArgs != null) {
                    strArr2 = new String[whereArgs.size()];
                    whereArgs.toArray(strArr2);
                } else {
                    strArr2 = null;
                }
                str2 = dataAbilityPredicatesToSelection;
                str = dataAbilityPredicates.getOrder();
            } else {
                strArr2 = null;
                str2 = null;
                str = null;
            }
            if (strArr2 == null) {
                try {
                    strArr3 = new String[0];
                } catch (RemoteException unused) {
                    AppLog.e(SHELL_LABEL, "ApplicationDataAbility::query RemoteException occur", new Object[0]);
                    throw new DataAbilityRemoteException("ApplicationDataAbility query failed");
                } catch (OperationCanceledException unused2) {
                    throw new DataAbilityRemoteException("ApplicationDataAbility query canceld");
                } catch (SQLException unused3) {
                    throw new DataAbilityRemoteException("ApplicationDataAbility sql exception");
                }
            } else {
                strArr3 = strArr2;
            }
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient query");
            Cursor query = this.providerClient.query(convertToAndroidContentUri, strArr, str2, strArr3, str);
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient query");
            if (query != null) {
                return ContentProviderConverter.cursorToResultSet(query);
            }
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::query return is null", new Object[0]);
            return null;
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public int insert(Uri uri, ValuesBucket valuesBucket) throws DataAbilityRemoteException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::insert client not ready", new Object[0]);
            return -1;
        } else if (uri == null || valuesBucket == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::insert param invalid", new Object[0]);
            return -1;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            ContentValues valuesBucketToContentValues = ContentProviderConverter.valuesBucketToContentValues(valuesBucket);
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient insert");
                android.net.Uri insert = this.providerClient.insert(convertToAndroidContentUri, valuesBucketToContentValues);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient insert");
                if (insert != null) {
                    return convertUriToIndex(insert);
                }
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::insert failed", new Object[0]);
                return -1;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::insert RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility insert failed");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public int delete(Uri uri, DataAbilityPredicates dataAbilityPredicates) throws DataAbilityRemoteException {
        String str;
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::delete client not ready", new Object[0]);
            return -1;
        } else if (uri == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::delete param invalid", new Object[0]);
            return -1;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            String[] strArr = null;
            if (dataAbilityPredicates != null) {
                str = ContentProviderConverter.dataAbilityPredicatesToSelection(dataAbilityPredicates);
                List<String> whereArgs = dataAbilityPredicates.getWhereArgs();
                if (whereArgs != null) {
                    strArr = new String[whereArgs.size()];
                    whereArgs.toArray(strArr);
                }
            } else {
                str = null;
            }
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient delete");
                int delete = this.providerClient.delete(convertToAndroidContentUri, str, strArr);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient delete");
                return delete;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::delete RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility delete failed");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public int update(Uri uri, ValuesBucket valuesBucket, DataAbilityPredicates dataAbilityPredicates) throws DataAbilityRemoteException {
        String str;
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::update client not ready", new Object[0]);
            return -1;
        } else if (uri == null || valuesBucket == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::update param invalid", new Object[0]);
            return -1;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            ContentValues valuesBucketToContentValues = ContentProviderConverter.valuesBucketToContentValues(valuesBucket);
            String[] strArr = null;
            if (dataAbilityPredicates != null) {
                str = ContentProviderConverter.dataAbilityPredicatesToSelection(dataAbilityPredicates);
                List<String> whereArgs = dataAbilityPredicates.getWhereArgs();
                if (whereArgs != null) {
                    strArr = new String[whereArgs.size()];
                    whereArgs.toArray(strArr);
                }
            } else {
                str = null;
            }
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient update");
                int update = this.providerClient.update(convertToAndroidContentUri, valuesBucketToContentValues, str, strArr);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient update");
                return update;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::update RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility update failed");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public void registerObserver(Uri uri, Object obj) {
        AbilityShellObserver abilityShellObserver;
        if (uri == null || obj == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::registerObserver param invalid", new Object[0]);
        } else if (!(obj instanceof IDataAbilityObserver)) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::registerObserver observer not IDataAbilityObserver type", new Object[0]);
        } else {
            if (this.observerMap.containsKey(obj)) {
                AppLog.w(SHELL_LABEL, "ApplicationDataAbility::registerObserver already contain observer", new Object[0]);
                abilityShellObserver = this.observerMap.get(obj);
            } else {
                AbilityShellObserver abilityShellObserver2 = new AbilityShellObserver(new Handler(this.abilityContext.getMainLooper()), (IDataAbilityObserver) obj);
                this.observerMap.put(obj, abilityShellObserver2);
                abilityShellObserver = abilityShellObserver2;
            }
            this.abilityContext.getContentResolver().registerContentObserver(UriConverter.convertToAndroidContentUri(uri), true, abilityShellObserver);
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public void unregisterObserver(Object obj) {
        if (obj == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::unregisterObserver param invalid", new Object[0]);
            return;
        }
        AbilityShellObserver abilityShellObserver = this.observerMap.get(obj);
        if (abilityShellObserver == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::unregisterObserver AbilityShellObserver not found", new Object[0]);
            return;
        }
        this.abilityContext.getContentResolver().unregisterContentObserver(abilityShellObserver);
        this.observerMap.remove(obj);
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public void notifyChange(Uri uri) {
        if (uri == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::notifyChange param invalid", new Object[0]);
            return;
        }
        this.abilityContext.getContentResolver().notifyChange(UriConverter.convertToAndroidContentUri(uri), null);
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public void close() {
        this.providerClient.close();
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public int batchInsert(Uri uri, ValuesBucket[] valuesBucketArr) throws DataAbilityRemoteException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::batchInsert client not ready", new Object[0]);
            return -1;
        } else if (uri == null || valuesBucketArr == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::batchInsert param invalid", new Object[0]);
            return -1;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            int length = valuesBucketArr.length;
            ContentValues[] contentValuesArr = new ContentValues[length];
            for (int i = 0; i < length; i++) {
                contentValuesArr[i] = ContentProviderConverter.valuesBucketToContentValues(valuesBucketArr[i]);
            }
            try {
                return this.providerClient.bulkInsert(convertToAndroidContentUri, contentValuesArr);
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::batchInsert RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility batchInsert failed");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public DataAbilityResult[] executeBatch(ArrayList<DataAbilityOperation> arrayList) throws DataAbilityRemoteException, OperationExecuteException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::executeBatch client not ready", new Object[0]);
            return new DataAbilityResult[0];
        } else if (arrayList == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::executeBatch param invalid", new Object[0]);
            return new DataAbilityResult[0];
        } else {
            ArrayList<ContentProviderOperation> arrayList2 = new ArrayList<>();
            Iterator<DataAbilityOperation> it = arrayList.iterator();
            while (it.hasNext()) {
                arrayList2.add(AbilityContentProviderConverter.dataAbilityOperationToContentProviderOperation(it.next()));
            }
            try {
                ContentProviderResult[] applyBatch = this.providerClient.applyBatch(arrayList2);
                int length = applyBatch.length;
                if (length == 0) {
                    AppLog.e(SHELL_LABEL, "ApplicationDataAbility::executeBatch return array length is 0", new Object[0]);
                    return new DataAbilityResult[0];
                }
                DataAbilityResult[] dataAbilityResultArr = new DataAbilityResult[length];
                for (int i = 0; i < length; i++) {
                    ContentProviderResult contentProviderResult = applyBatch[i];
                    if (!(contentProviderResult == null || contentProviderResult.uri == null)) {
                        contentProviderResult = new ContentProviderResult(convertUriToIndex(contentProviderResult.uri));
                    }
                    dataAbilityResultArr[i] = AbilityContentProviderConverter.contentProviderResultToDataAbilityResult(contentProviderResult);
                }
                return dataAbilityResultArr;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::executeBatch RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility executeBatch failed");
            } catch (OperationApplicationException unused2) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::executeBatch OperationApplicationException occur", new Object[0]);
                throw new OperationExecuteException("ApplicationDataAbility executeBatch failed");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public PacMap call(String str, String str2, PacMap pacMap) throws DataAbilityRemoteException {
        Bundle bundle;
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::call client not ready", new Object[0]);
            return null;
        } else if (str == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::call param invalid", new Object[0]);
            return null;
        } else {
            if (pacMap != null) {
                bundle = pacMap.getClassLoader() == null ? PacMapUtils.convertIntoBundle(pacMap) : IntentConverter.convertPacMapToBundle(pacMap).orElse(new Bundle());
            } else {
                bundle = null;
            }
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient call");
                Bundle call = this.providerClient.call(str, str2, bundle);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient call");
                if (call == null) {
                    AppLog.e(SHELL_LABEL, "ApplicationDataAbility::call return is null", new Object[0]);
                    return null;
                } else if (pacMap == null || pacMap.getClassLoader() == null) {
                    return PacMapUtils.convertFromBundle(call);
                } else {
                    call.setClassLoader(pacMap.getClassLoader());
                    return IntentConverter.convertBundleToPacMap(call).orElse(new PacMap());
                }
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::call RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility call failed");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public String getType(Uri uri) throws DataAbilityRemoteException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getType client not ready", new Object[0]);
            return null;
        } else if (uri == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getType param invalid", new Object[0]);
            return null;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient getType");
                String type = this.providerClient.getType(convertToAndroidContentUri);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient getType");
                if (type != null) {
                    return type;
                }
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getType return is null", new Object[0]);
                return null;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getType RemoteException", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility getType failed");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public String[] getFileTypes(Uri uri, String str) throws DataAbilityRemoteException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getFileTypes client not ready", new Object[0]);
            return new String[0];
        } else if (uri == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getFileTypes param invalid", new Object[0]);
            return new String[0];
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient getFileTypes");
                String[] streamTypes = this.providerClient.getStreamTypes(convertToAndroidContentUri, str);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient getFileTypes");
                if (streamTypes != null) {
                    return streamTypes;
                }
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getFileTypes return is null", new Object[0]);
                return new String[0];
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::getFileTypes RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility::getFileTypes failed");
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005d, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.appexecfwk.utils.AppLog.e(ohos.abilityshell.ApplicationDataAbility.SHELL_LABEL, "ApplicationDataAbility::openFile dup FileDescriptor errno exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006e, code lost:
        ohos.appexecfwk.utils.AppLog.e(ohos.abilityshell.ApplicationDataAbility.SHELL_LABEL, "ApplicationDataAbility::openFile close FileDescriptor errno exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0075, code lost:
        throw r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x005f */
    @Override // ohos.aafwk.ability.IDataAbility
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.FileDescriptor openFile(ohos.utils.net.Uri r8, java.lang.String r9) throws ohos.aafwk.ability.DataAbilityRemoteException, java.io.FileNotFoundException {
        /*
        // Method dump skipped, instructions count: 165
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.abilityshell.ApplicationDataAbility.openFile(ohos.utils.net.Uri, java.lang.String):java.io.FileDescriptor");
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public RawFileDescriptor openRawFile(Uri uri, String str) throws DataAbilityRemoteException, FileNotFoundException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::openRawFile client not ready", new Object[0]);
            return null;
        } else if (uri == null || uri.getEncodedPath().contains("../")) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::openRawFile param invalid", new Object[0]);
            return null;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient openAssetFile");
                AssetFileDescriptor openAssetFile = this.providerClient.openAssetFile(convertToAndroidContentUri, str);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient openAssetFile");
                if (openAssetFile != null) {
                    return new RawFileDescriptorImpl(new AfdAdapter(openAssetFile));
                }
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::openRawFile return is null", new Object[0]);
                return null;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::openRawFile RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility openRawFile failed");
            } catch (FileNotFoundException e) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::openRawFile FileNotFoundException occur", new Object[0]);
                throw e;
            } catch (OperationCanceledException unused2) {
                throw new DataAbilityRemoteException("ApplicationDataAbility openAssetFile canceld");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public void makePersistentUriPermission(Uri uri, int i) throws DataAbilityRemoteException {
        if (uri == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::makePersistentUriPermission param invalid", new Object[0]);
            return;
        }
        android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
        ContentResolver contentResolver = this.abilityContext.getContentResolver();
        try {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient makePersistentUriPermission");
            UriGrantsManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(convertToAndroidContentUri), i, (String) null, contentResolver.resolveUserId(convertToAndroidContentUri));
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient makePersistentUriPermission");
        } catch (RemoteException unused) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::makePersistentUriPermission RemoteException occur", new Object[0]);
            throw new DataAbilityRemoteException("ApplicationDataAbility makePersistentUriPermission failed");
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public Uri normalizeUri(Uri uri) throws DataAbilityRemoteException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::normalizeUri client not ready", new Object[0]);
            return null;
        } else if (uri == null) {
            return null;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient normalizeUri");
                android.net.Uri canonicalize = this.providerClient.canonicalize(convertToAndroidContentUri);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient normalizeUri");
                if (canonicalize != null) {
                    return UriConverter.convertToZidaneContentUri(canonicalize, "");
                }
                AppLog.d(SHELL_LABEL, "canonicalize return null.", new Object[0]);
                return null;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::normalizeUri occur exception.", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility normalizeUri failed.");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public Uri denormalizeUri(Uri uri) throws DataAbilityRemoteException {
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::denormalizeUri client not ready", new Object[0]);
            return null;
        } else if (uri == null) {
            return null;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient denormalizeUri");
                android.net.Uri uncanonicalize = this.providerClient.uncanonicalize(convertToAndroidContentUri);
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient denormalizeUri");
                if (uncanonicalize != null) {
                    return UriConverter.convertToZidaneContentUri(uncanonicalize, "");
                }
                AppLog.d(SHELL_LABEL, "uncanonicalize return null.", new Object[0]);
                return null;
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::denormalizeUri occur exception.", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility denormalizeUri failed.");
            }
        }
    }

    @Override // ohos.aafwk.ability.IDataAbility
    public boolean reload(Uri uri, PacMap pacMap) throws DataAbilityRemoteException {
        Bundle bundle;
        if (!isClientReady()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::reload client not ready", new Object[0]);
            return false;
        } else if (uri == null) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::reload param invalid", new Object[0]);
            return false;
        } else {
            android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(uri);
            if (pacMap != null) {
                bundle = pacMap.getClassLoader() == null ? PacMapUtils.convertIntoBundle(pacMap) : IntentConverter.convertPacMapToBundle(pacMap).orElse(new Bundle());
            } else {
                bundle = null;
            }
            Boolean.valueOf(false);
            try {
                C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient refresh");
                Boolean valueOf = Boolean.valueOf(this.providerClient.refresh(convertToAndroidContentUri, bundle, null));
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "ContentProviderClient refresh");
                return valueOf.booleanValue();
            } catch (RemoteException unused) {
                AppLog.e(SHELL_LABEL, "ApplicationDataAbility::reload RemoteException occur", new Object[0]);
                throw new DataAbilityRemoteException("ApplicationDataAbility reload failed");
            }
        }
    }

    private boolean isClientReady() {
        return this.providerClient != null;
    }

    private int convertUriToIndex(android.net.Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments == null || pathSegments.isEmpty()) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::convertUriToIndex failed", new Object[0]);
            return -1;
        }
        try {
            return Integer.parseInt(pathSegments.get(pathSegments.size() - 1));
        } catch (NumberFormatException unused) {
            AppLog.e(SHELL_LABEL, "ApplicationDataAbility::convertUriToIndex integer parse error", new Object[0]);
            return -1;
        }
    }
}
