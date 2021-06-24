package ohos.aafwk.ability;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.tools.C0000Bytrace;
import ohos.utils.PacMap;
import ohos.utils.net.Uri;

public class DataAbilityHelper {
    private static final String MAKE_PERMISSION_TRACE = "provider makePersistentUriPermission";
    private static final String SCHEME_HARMONY = "dataability";
    private final Object OPERATOR_LOCK = new Object();
    private Context context;
    private IDataAbility dataAbility;
    private Map<IDataAbilityObserver, IDataAbility> registerMap = new HashMap();
    private Uri uri;

    private DataAbilityHelper(Context context2) {
        this.context = context2;
    }

    private DataAbilityHelper(Context context2, Uri uri2, IDataAbility iDataAbility) {
        this.context = context2;
        this.uri = uri2;
        this.dataAbility = iDataAbility;
    }

    public Context getContext() {
        return this.context;
    }

    public static DataAbilityHelper creator(Context context2) {
        if (context2 == null) {
            return null;
        }
        return new DataAbilityHelper(context2);
    }

    public static DataAbilityHelper creator(Context context2, Uri uri2) {
        return creator(context2, uri2, false);
    }

    public static DataAbilityHelper creator(Context context2, Uri uri2, boolean z) {
        if (uri2 == null || context2 == null || !SCHEME_HARMONY.equals(uri2.getScheme())) {
            return null;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "DataAbility Create");
        IDataAbility dataAbility2 = context2.getDataAbility(uri2, z);
        if (dataAbility2 == null) {
            return null;
        }
        C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "DataAbility Create");
        return new DataAbilityHelper(context2, uri2, dataAbility2);
    }

    public boolean release() {
        if (this.uri == null) {
            return true;
        }
        if (!this.context.releaseDataAbility(this.dataAbility)) {
            return false;
        }
        this.dataAbility = null;
        this.uri = null;
        return true;
    }

    public void registerObserver(Uri uri2, IDataAbilityObserver iDataAbilityObserver) throws IllegalArgumentException {
        IDataAbility iDataAbility;
        checkUriParam(uri2);
        if (iDataAbilityObserver != null) {
            synchronized (this.OPERATOR_LOCK) {
                if (this.uri == null) {
                    iDataAbility = this.registerMap.get(iDataAbilityObserver);
                    if (iDataAbility == null) {
                        iDataAbility = this.context.getDataAbility(uri2);
                        if (iDataAbility != null) {
                            this.registerMap.put(iDataAbilityObserver, iDataAbility);
                        } else {
                            throw new IllegalArgumentException("DataAbility register failed, there is no corresponding dataAbility");
                        }
                    }
                } else {
                    iDataAbility = this.dataAbility;
                }
            }
            iDataAbility.registerObserver(uri2, iDataAbilityObserver);
            return;
        }
        throw new IllegalArgumentException("DataAbility register failed, dataObserver argument is null");
    }

    public void notifyChange(Uri uri2) throws IllegalArgumentException {
        checkUriParam(uri2);
        if (this.dataAbility == null) {
            this.dataAbility = this.context.getDataAbility(uri2);
            if (this.dataAbility == null) {
                throw new IllegalArgumentException("DataAbility notify failed, dataAbility is illegal");
            }
        }
        try {
            this.dataAbility.notifyChange(uri2);
        } finally {
            if (this.uri == null) {
                this.context.releaseDataAbility(this.dataAbility);
                this.dataAbility = null;
            }
        }
    }

    public void unregisterObserver(Uri uri2, IDataAbilityObserver iDataAbilityObserver) throws IllegalArgumentException {
        IDataAbility iDataAbility;
        checkUriParam(uri2);
        if (iDataAbilityObserver != null) {
            synchronized (this.OPERATOR_LOCK) {
                if (this.uri == null) {
                    iDataAbility = this.registerMap.get(iDataAbilityObserver);
                    this.registerMap.remove(iDataAbilityObserver);
                } else {
                    iDataAbility = this.dataAbility;
                }
                if (iDataAbility != null) {
                    iDataAbility.unregisterObserver(iDataAbilityObserver);
                    this.context.releaseDataAbility(iDataAbility);
                    return;
                }
                return;
            }
        }
        throw new IllegalArgumentException("DataAbility unregister failed, dataObserver argument is null");
    }

    public int insert(Uri uri2, ValuesBucket valuesBucket) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerInsert");
        if (this.uri == null) {
            this.dataAbility = this.context.getDataAbility(uri2, true);
        }
        IDataAbility iDataAbility = this.dataAbility;
        if (iDataAbility != null) {
            try {
                return iDataAbility.insert(uri2, valuesBucket);
            } finally {
                if (this.uri == null) {
                    this.context.releaseDataAbility(this.dataAbility);
                    this.dataAbility = null;
                }
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerInsert");
            }
        } else {
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerInsert");
            throw new IllegalStateException("No corresponding dataAbility, insert failed");
        }
    }

    public int batchInsert(Uri uri2, ValuesBucket[] valuesBucketArr) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        if (valuesBucketArr != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerBatchInsert");
            if (this.uri == null) {
                this.dataAbility = this.context.getDataAbility(uri2, true);
            }
            IDataAbility iDataAbility = this.dataAbility;
            if (iDataAbility != null) {
                try {
                    return iDataAbility.batchInsert(uri2, valuesBucketArr);
                } finally {
                    if (this.uri == null) {
                        this.context.releaseDataAbility(this.dataAbility);
                        this.dataAbility = null;
                    }
                    C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerBatchInsert");
                }
            } else {
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerBatchInsert");
                throw new IllegalStateException("No corresponding dataAbility, batch insert failed");
            }
        } else {
            throw new IllegalArgumentException("Input uri and values can not be null.");
        }
    }

    public int delete(Uri uri2, DataAbilityPredicates dataAbilityPredicates) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerDelete");
        if (this.uri == null) {
            this.dataAbility = this.context.getDataAbility(uri2, true);
        }
        IDataAbility iDataAbility = this.dataAbility;
        if (iDataAbility != null) {
            try {
                return iDataAbility.delete(uri2, dataAbilityPredicates);
            } finally {
                if (this.uri == null) {
                    this.context.releaseDataAbility(this.dataAbility);
                    this.dataAbility = null;
                }
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerDelete");
            }
        } else {
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerDelete");
            throw new IllegalStateException("No corresponding dataAbility, delete failed");
        }
    }

    public int update(Uri uri2, ValuesBucket valuesBucket, DataAbilityPredicates dataAbilityPredicates) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerUpdate");
        if (this.uri == null) {
            this.dataAbility = this.context.getDataAbility(uri2, false);
        }
        IDataAbility iDataAbility = this.dataAbility;
        if (iDataAbility != null) {
            try {
                return iDataAbility.update(uri2, valuesBucket, dataAbilityPredicates);
            } finally {
                if (this.uri == null) {
                    this.context.releaseDataAbility(this.dataAbility);
                    this.dataAbility = null;
                }
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerUpdate");
            }
        } else {
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerUpdate");
            throw new IllegalStateException("No corresponding dataAbility, update failed");
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:4:0x001f */
    /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: ohos.aafwk.ability.DataAbilityHelper */
    /* JADX DEBUG: Multi-variable search result rejected for r5v5, resolved type: ohos.aafwk.ability.DataAbilityHelper */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v9, types: [ohos.data.resultset.ResultSet] */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:9|10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        r5.context.releaseDataAbility(r5.dataAbility);
        r5.dataAbility = r5.context.getDataAbility(r6, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0042, code lost:
        return r5.dataAbility.query(r6, r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
        ohos.tools.C0000Bytrace.finishTrace(ohos.tools.C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerQuery");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0029 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.data.resultset.ResultSet query(ohos.utils.net.Uri r6, java.lang.String[] r7, ohos.data.dataability.DataAbilityPredicates r8) throws ohos.aafwk.ability.DataAbilityRemoteException {
        /*
            r5 = this;
            r5.checkUriParam(r6)
            java.lang.String r0 = "providerQuery"
            r1 = 2147483648(0x80000000, double:1.0609978955E-314)
            ohos.tools.C0000Bytrace.startTrace(r1, r0)
            ohos.utils.net.Uri r3 = r5.uri
            if (r3 != 0) goto L_0x0052
            ohos.app.Context r3 = r5.context
            r4 = 0
            ohos.aafwk.ability.IDataAbility r3 = r3.getDataAbility(r6, r4)
            r5.dataAbility = r3
            ohos.aafwk.ability.IDataAbility r3 = r5.dataAbility
            if (r3 == 0) goto L_0x0047
            ohos.data.resultset.ResultSet r5 = r3.query(r6, r7, r8)     // Catch:{ DataAbilityDeadException -> 0x0029 }
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            return r5
        L_0x0027:
            r5 = move-exception
            goto L_0x0043
        L_0x0029:
            ohos.app.Context r3 = r5.context     // Catch:{ all -> 0x0027 }
            ohos.aafwk.ability.IDataAbility r4 = r5.dataAbility     // Catch:{ all -> 0x0027 }
            r3.releaseDataAbility(r4)     // Catch:{ all -> 0x0027 }
            ohos.app.Context r3 = r5.context     // Catch:{ all -> 0x0027 }
            r4 = 1
            ohos.aafwk.ability.IDataAbility r3 = r3.getDataAbility(r6, r4)     // Catch:{ all -> 0x0027 }
            r5.dataAbility = r3     // Catch:{ all -> 0x0027 }
            ohos.aafwk.ability.IDataAbility r5 = r5.dataAbility     // Catch:{ all -> 0x0027 }
            ohos.data.resultset.ResultSet r5 = r5.query(r6, r7, r8)     // Catch:{ all -> 0x0027 }
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            return r5
        L_0x0043:
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            throw r5
        L_0x0047:
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "No corresponding dataAbility, query failed"
            r5.<init>(r6)
            throw r5
        L_0x0052:
            ohos.aafwk.ability.IDataAbility r5 = r5.dataAbility     // Catch:{ all -> 0x005c }
            ohos.data.resultset.ResultSet r5 = r5.query(r6, r7, r8)     // Catch:{ all -> 0x005c }
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            return r5
        L_0x005c:
            r5 = move-exception
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.DataAbilityHelper.query(ohos.utils.net.Uri, java.lang.String[], ohos.data.dataability.DataAbilityPredicates):ohos.data.resultset.ResultSet");
    }

    public DataAbilityResult[] executeBatch(Uri uri2, ArrayList<DataAbilityOperation> arrayList) throws DataAbilityRemoteException, OperationExecuteException {
        checkUriParam(uri2);
        if (arrayList != null) {
            C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerExecuteBatch");
            if (this.uri == null) {
                this.dataAbility = this.context.getDataAbility(uri2, true);
            }
            IDataAbility iDataAbility = this.dataAbility;
            if (iDataAbility != null) {
                try {
                    return iDataAbility.executeBatch(arrayList);
                } finally {
                    if (this.uri == null) {
                        this.context.releaseDataAbility(this.dataAbility);
                        this.dataAbility = null;
                    }
                    C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerExecuteBatch");
                }
            } else {
                C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerExecuteBatch");
                throw new IllegalStateException("No corresponding dataAbility, execute batch failed");
            }
        } else {
            throw new IllegalArgumentException("operations is illegal, execute batch failed");
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:5:0x0025 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0, types: [ohos.utils.net.Uri] */
    /* JADX WARN: Type inference failed for: r7v2, types: [ohos.utils.net.Uri] */
    /* JADX WARN: Type inference failed for: r3v5, types: [ohos.app.Context] */
    /* JADX WARN: Type inference failed for: r3v7, types: [ohos.aafwk.ability.IDataAbility] */
    /* JADX WARN: Type inference failed for: r7v6, types: [java.io.FileDescriptor] */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r6.context.releaseDataAbility(r6.dataAbility);
        r6.dataAbility = r6.context.getDataAbility(r7, true);
        r7 = r6.dataAbility.openFile(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004f, code lost:
        r6.context.releaseDataAbility(r6.dataAbility);
        r6.dataAbility = null;
        ohos.tools.C0000Bytrace.finishTrace(ohos.tools.C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerOpenFile");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005b, code lost:
        throw r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0036, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0038 */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.FileDescriptor openFile(ohos.utils.net.Uri r7, java.lang.String r8) throws java.io.FileNotFoundException, ohos.aafwk.ability.DataAbilityRemoteException {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.DataAbilityHelper.openFile(ohos.utils.net.Uri, java.lang.String):java.io.FileDescriptor");
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:5:0x0026 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0, types: [ohos.utils.net.Uri] */
    /* JADX WARN: Type inference failed for: r7v2, types: [ohos.utils.net.Uri] */
    /* JADX WARN: Type inference failed for: r3v5, types: [ohos.app.Context] */
    /* JADX WARN: Type inference failed for: r3v7, types: [ohos.aafwk.ability.IDataAbility] */
    /* JADX WARN: Type inference failed for: r7v6, types: [ohos.global.resource.RawFileDescriptor] */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r6.context.releaseDataAbility(r6.dataAbility);
        r6.dataAbility = r6.context.getDataAbility(r7, true);
        r7 = r6.dataAbility.openRawFile(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0050, code lost:
        r6.context.releaseDataAbility(r6.dataAbility);
        r6.dataAbility = null;
        ohos.tools.C0000Bytrace.finishTrace(ohos.tools.C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerOpenRawFile");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005c, code lost:
        throw r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0037, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0039 */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.global.resource.RawFileDescriptor openRawFile(ohos.utils.net.Uri r7, java.lang.String r8) throws java.io.FileNotFoundException, ohos.aafwk.ability.DataAbilityRemoteException {
        /*
        // Method dump skipped, instructions count: 119
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.DataAbilityHelper.openRawFile(ohos.utils.net.Uri, java.lang.String):ohos.global.resource.RawFileDescriptor");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        r6.context.releaseDataAbility(r6.dataAbility);
        r6.dataAbility = r6.context.getDataAbility(r7, true);
        r6.dataAbility.makePersistentUriPermission(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0048, code lost:
        r6.context.releaseDataAbility(r6.dataAbility);
        r6.dataAbility = null;
        ohos.tools.C0000Bytrace.finishTrace(ohos.tools.C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, ohos.aafwk.ability.DataAbilityHelper.MAKE_PERMISSION_TRACE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0054, code lost:
        throw r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0030, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0032 */
    @ohos.annotation.SystemApi
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void makePersistentUriPermission(ohos.utils.net.Uri r7, int r8) throws ohos.aafwk.ability.DataAbilityRemoteException {
        /*
        // Method dump skipped, instructions count: 110
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.DataAbilityHelper.makePersistentUriPermission(ohos.utils.net.Uri, int):void");
    }

    public String[] getFileTypes(Uri uri2, String str) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        checkParamNotNull(str, "Parameter mimeTypeFilter is null");
        this.dataAbility = getDataAbility(uri2);
        if (this.dataAbility == null) {
            return null;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerGetFileTypes");
        try {
            return this.dataAbility.getFileTypes(uri2, str);
        } finally {
            if (this.uri == null) {
                this.context.releaseDataAbility(this.dataAbility);
                this.dataAbility = null;
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerGetFileTypes");
        }
    }

    public PacMap call(Uri uri2, String str, String str2, PacMap pacMap) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        checkParamNotNull(str, "Parameter method is null");
        this.dataAbility = getDataAbility(uri2);
        if (this.dataAbility == null) {
            return null;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerCall");
        try {
            return this.dataAbility.call(str, str2, pacMap);
        } finally {
            if (this.uri == null) {
                this.context.releaseDataAbility(this.dataAbility);
                this.dataAbility = null;
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerCall");
        }
    }

    public String getType(Uri uri2) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        this.dataAbility = getDataAbility(uri2);
        if (this.dataAbility == null) {
            return null;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerGetType");
        try {
            return this.dataAbility.getType(uri2);
        } finally {
            if (this.uri == null) {
                this.context.releaseDataAbility(this.dataAbility);
                this.dataAbility = null;
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerGetType");
        }
    }

    public Uri normalizeUri(Uri uri2) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        this.dataAbility = getDataAbility(uri2);
        if (this.dataAbility == null) {
            return null;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerNormalizeUri");
        try {
            return this.dataAbility.normalizeUri(uri2);
        } finally {
            if (this.uri == null) {
                this.context.releaseDataAbility(this.dataAbility);
                this.dataAbility = null;
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerNormalizeUri");
        }
    }

    public Uri denormalizeUri(Uri uri2) throws DataAbilityRemoteException {
        checkUriParam(uri2);
        this.dataAbility = getDataAbility(uri2);
        if (this.dataAbility == null) {
            return null;
        }
        C0000Bytrace.startTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerDenormalizeUri");
        try {
            return this.dataAbility.denormalizeUri(uri2);
        } finally {
            if (this.uri == null) {
                this.context.releaseDataAbility(this.dataAbility);
                this.dataAbility = null;
            }
            C0000Bytrace.finishTrace(C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerDenormalizeUri");
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:4:0x001f */
    /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: ohos.aafwk.ability.DataAbilityHelper */
    /* JADX DEBUG: Multi-variable search result rejected for r5v5, resolved type: ohos.aafwk.ability.DataAbilityHelper */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v9, types: [boolean] */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:9|10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        r5.context.releaseDataAbility(r5.dataAbility);
        r5.dataAbility = r5.context.getDataAbility(r6, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0042, code lost:
        return r5.dataAbility.reload(r6, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
        ohos.tools.C0000Bytrace.finishTrace(ohos.tools.C0000Bytrace.BYTRACE_TAG_ABILITY_MANAGER, "providerReload");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0027, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0029 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean reload(ohos.utils.net.Uri r6, ohos.utils.PacMap r7) throws ohos.aafwk.ability.DataAbilityRemoteException {
        /*
            r5 = this;
            r5.checkUriParam(r6)
            java.lang.String r0 = "providerReload"
            r1 = 2147483648(0x80000000, double:1.0609978955E-314)
            ohos.tools.C0000Bytrace.startTrace(r1, r0)
            ohos.utils.net.Uri r3 = r5.uri
            if (r3 != 0) goto L_0x0052
            ohos.app.Context r3 = r5.context
            r4 = 0
            ohos.aafwk.ability.IDataAbility r3 = r3.getDataAbility(r6, r4)
            r5.dataAbility = r3
            ohos.aafwk.ability.IDataAbility r3 = r5.dataAbility
            if (r3 == 0) goto L_0x0047
            boolean r5 = r3.reload(r6, r7)     // Catch:{ DataAbilityDeadException -> 0x0029 }
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            return r5
        L_0x0027:
            r5 = move-exception
            goto L_0x0043
        L_0x0029:
            ohos.app.Context r3 = r5.context     // Catch:{ all -> 0x0027 }
            ohos.aafwk.ability.IDataAbility r4 = r5.dataAbility     // Catch:{ all -> 0x0027 }
            r3.releaseDataAbility(r4)     // Catch:{ all -> 0x0027 }
            ohos.app.Context r3 = r5.context     // Catch:{ all -> 0x0027 }
            r4 = 1
            ohos.aafwk.ability.IDataAbility r3 = r3.getDataAbility(r6, r4)     // Catch:{ all -> 0x0027 }
            r5.dataAbility = r3     // Catch:{ all -> 0x0027 }
            ohos.aafwk.ability.IDataAbility r5 = r5.dataAbility     // Catch:{ all -> 0x0027 }
            boolean r5 = r5.reload(r6, r7)     // Catch:{ all -> 0x0027 }
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            return r5
        L_0x0043:
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            throw r5
        L_0x0047:
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "No corresponding dataAbility, reload failed"
            r5.<init>(r6)
            throw r5
        L_0x0052:
            ohos.aafwk.ability.IDataAbility r5 = r5.dataAbility     // Catch:{ all -> 0x005c }
            boolean r5 = r5.reload(r6, r7)     // Catch:{ all -> 0x005c }
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            return r5
        L_0x005c:
            r5 = move-exception
            ohos.tools.C0000Bytrace.finishTrace(r1, r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.DataAbilityHelper.reload(ohos.utils.net.Uri, ohos.utils.PacMap):boolean");
    }

    public InputStream obtainInputStream(Uri uri2) throws FileNotFoundException, DataAbilityRemoteException {
        return new FileInputStream(openFile(uri2, "r"));
    }

    public OutputStream obtainOutputStream(Uri uri2) throws FileNotFoundException, DataAbilityRemoteException {
        return new FileOutputStream(openFile(uri2, "w"));
    }

    private IDataAbility getDataAbility(Uri uri2) {
        if (this.uri == null) {
            return this.context.getDataAbility(uri2, true);
        }
        return this.dataAbility;
    }

    private String checkParamNotNull(String str, String str2) {
        if (str != null) {
            return str;
        }
        throw new NullPointerException(String.valueOf(str2));
    }

    private void checkUriParam(Uri uri2) {
        if (uri2 != null) {
            checkZidaneUri(uri2);
            Uri uri3 = this.uri;
            if (uri3 != null) {
                checkZidaneUri(uri3);
                if (!this.uri.getDecodedPathList().get(0).equals(uri2.getDecodedPathList().get(0))) {
                    throw new IllegalArgumentException("this uri paths first segment is not equal to uri paths first segment.");
                }
                return;
            }
            return;
        }
        throw new NullPointerException("Parameter uri is null.");
    }

    private void checkZidaneUri(Uri uri2) {
        if (!SCHEME_HARMONY.equals(uri2.getScheme())) {
            throw new IllegalArgumentException("Scheme is illegal.");
        } else if (uri2.getDecodedPathList() == null || uri2.getDecodedPathList().isEmpty()) {
            throw new IllegalArgumentException("paths is illegal.");
        } else if (uri2.getDecodedPathList().get(0) == null || "".equals(uri2.getDecodedPathList().get(0))) {
            throw new IllegalArgumentException("paths first segment is illegal.");
        }
    }
}
