package ohos.data.rdb.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import ohos.app.Context;
import ohos.data.DatabaseFileConfig;
import ohos.data.DatabaseFileSecurityLevel;
import ohos.data.DatabaseFileType;
import ohos.data.rdb.AbsRdbPredicates;
import ohos.data.rdb.RdbConstraintException;
import ohos.data.rdb.RdbCorruptException;
import ohos.data.rdb.RdbException;
import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.RdbUtils;
import ohos.data.rdb.Statement;
import ohos.data.rdb.StoreConfig;
import ohos.data.rdb.TransactionObserver;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.data.resultset.ResultSetHook;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Pair;

public class RdbStoreImpl extends CoreCloseable implements RdbStore {
    private static final int DEFAULT_SQL_LENGTH = 120;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109520, "RdbStoreImpl");
    private static final String[] ON_CONFLICT_CLAUSE = {"", " OR ROLLBACK", " OR ABORT", " OR FAIL", " OR IGNORE", " OR REPLACE"};
    private ConnectionPool connectionPool;
    private Context context;
    private RdbOpenCallback openCallback;
    private final Object parametersLock = new Object();
    private ResultSetHook resultSetHook;
    private final ThreadLocal<StoreSession> threadSession = ThreadLocal.withInitial(new Supplier() {
        /* class ohos.data.rdb.impl.$$Lambda$RdbStoreImpl$AWJbKkBloWskVVMYAR35sv1Ti0s */

        @Override // java.util.function.Supplier
        public final Object get() {
            return RdbStoreImpl.this.createSession();
        }
    });
    private int version;

    private RdbStoreImpl(Context context2, int i, RdbOpenCallback rdbOpenCallback, ResultSetHook resultSetHook2) {
        this.version = i;
        this.openCallback = rdbOpenCallback;
        this.context = context2;
        this.resultSetHook = resultSetHook2;
    }

    public static RdbStoreImpl open(Context context2, StoreConfig storeConfig, int i, RdbOpenCallback rdbOpenCallback, ResultSetHook resultSetHook2) {
        if (context2 == null) {
            throw new IllegalArgumentException("the context config cannot be null.");
        } else if (storeConfig == null) {
            throw new IllegalArgumentException("the store config cannot be null.");
        } else if (i >= 1) {
            RdbStoreImpl rdbStoreImpl = new RdbStoreImpl(context2, i, rdbOpenCallback, resultSetHook2);
            SqliteDatabaseConfig create = SqliteDatabaseConfig.create(context2, storeConfig);
            rdbStoreImpl.open(create);
            if (storeConfig.getStorageMode() == StoreConfig.StorageMode.MODE_MEMORY) {
                return rdbStoreImpl;
            }
            FileProtectHelper.setFileLabel(context2, create.getPath(), storeConfig.getDatabaseFileSecurityLevel());
            return rdbStoreImpl;
        } else {
            throw new IllegalArgumentException("the store version cannot less than 1.");
        }
    }

    private void open(SqliteDatabaseConfig sqliteDatabaseConfig) {
        try {
            this.connectionPool = ConnectionPool.createInstance(sqliteDatabaseConfig);
            sqliteDatabaseConfig.destroyEncryptKey();
            processHelper(sqliteDatabaseConfig);
            if ((sqliteDatabaseConfig.getOpenFlags() & 1) == 1) {
                HiLog.info(LABEL, "Opened %{public}s in read-only mode.", sqliteDatabaseConfig.getName());
            }
        } catch (RdbCorruptException e) {
            HiLog.error(LABEL, "File is not a database or encrypt key is error.", new Object[0]);
            if (this.openCallback != null) {
                this.openCallback.onCorruption(new File(sqliteDatabaseConfig.getPath()));
            }
            throw e;
        } catch (Throwable th) {
            sqliteDatabaseConfig.destroyEncryptKey();
            throw th;
        }
    }

    private void processHelper(SqliteDatabaseConfig sqliteDatabaseConfig) {
        int version2 = getVersion();
        if (this.version == version2) {
            RdbOpenCallback rdbOpenCallback = this.openCallback;
            if (rdbOpenCallback != null) {
                rdbOpenCallback.onOpen(this);
            }
        } else if ((sqliteDatabaseConfig.getOpenFlags() & 1) == 1) {
            throw new IllegalStateException("Can't upgrade read-only store from version " + version2 + " to " + this.version + ": " + sqliteDatabaseConfig.getName());
        } else if (this.openCallback == null) {
            setVersion(this.version);
        } else {
            beginTransaction();
            if (version2 == 0) {
                try {
                    this.openCallback.onCreate(this);
                } catch (Throwable th) {
                    endTransaction();
                    throw th;
                }
            } else if (this.version > version2) {
                this.openCallback.onUpgrade(this, version2, this.version);
            } else {
                this.openCallback.onDowngrade(this, version2, this.version);
            }
            setVersion(this.version);
            markAsCommit();
            endTransaction();
            this.openCallback.onOpen(this);
        }
    }

    /* access modifiers changed from: private */
    public StoreSession createSession() {
        ConnectionPool connectionPool2;
        synchronized (this.parametersLock) {
            if (isOpen()) {
                connectionPool2 = this.connectionPool;
            } else {
                throw new IllegalStateException("The store of '" + this.connectionPool.getName() + "' is not open.");
            }
        }
        return new StoreSession(connectionPool2);
    }

    /* access modifiers changed from: package-private */
    public StoreSession getThreadSession() {
        return this.threadSession.get();
    }

    @Override // ohos.data.rdb.RdbStore
    public long insert(String str, ValuesBucket valuesBucket) {
        try {
            return insertWithConflictResolution(str, valuesBucket, RdbStore.ConflictResolution.ON_CONFLICT_NONE);
        } catch (RdbException unused) {
            return -1;
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public long insertOrThrowException(String str, ValuesBucket valuesBucket) {
        return insertWithConflictResolution(str, valuesBucket, RdbStore.ConflictResolution.ON_CONFLICT_NONE);
    }

    @Override // ohos.data.rdb.RdbStore
    public List<Long> batchInsertOrThrowException(String str, List<ValuesBucket> list, RdbStore.ConflictResolution conflictResolution) {
        if (isEmpty(str)) {
            throw new IllegalArgumentException("no tableName specified.");
        } else if (list == null) {
            throw new IllegalArgumentException("no values specified, if you want to insert an all null row, please specified a null column with no not null constraints.");
        } else if (list.size() > 1000 || list.size() == 0) {
            throw new IllegalArgumentException("size of initialValues should not be larger than 1000 and larger than 0.");
        } else if (!list.stream().anyMatch($$Lambda$RdbStoreImpl$BtIfp8rdbA_Hnsc_g80wMpA7gmE.INSTANCE)) {
            RdbStore.ConflictResolution analysisResolution = analysisResolution(conflictResolution);
            if (analysisResolution.equals(RdbStore.ConflictResolution.ON_CONFLICT_ROLLBACK)) {
                return innerBatchInsertOrRollback(str, list);
            }
            return innerBatchInsert(str, list, analysisResolution);
        } else {
            throw new IllegalArgumentException("any element in initialValues should not be null or empty.");
        }
    }

    static /* synthetic */ boolean lambda$batchInsertOrThrowException$0(ValuesBucket valuesBucket) {
        return valuesBucket == null || valuesBucket.isEmpty();
    }

    private List<Long> innerBatchInsert(String str, List<ValuesBucket> list, RdbStore.ConflictResolution conflictResolution) {
        ArrayList arrayList = new ArrayList(list.size());
        try {
            acquireRef();
            beginTransaction();
            for (ValuesBucket valuesBucket : list) {
                arrayList.add(Long.valueOf(insertWithConflictResolution(str, valuesBucket, conflictResolution)));
            }
            markAsCommit();
            endTransaction();
            releaseRef();
            return arrayList;
        } catch (RdbConstraintException e) {
            markAsCommit();
            throw e;
        } catch (Throwable th) {
            endTransaction();
            releaseRef();
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0056  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.Long> innerBatchInsertOrRollback(java.lang.String r6, java.util.List<ohos.data.rdb.ValuesBucket> r7) {
        /*
            r5 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            int r1 = r7.size()
            r0.<init>(r1)
            r5.acquireRef()
            r1 = 0
            r2 = 1
            r5.beginTransaction()     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            java.util.Iterator r7 = r7.iterator()     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
        L_0x0015:
            boolean r3 = r7.hasNext()     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            if (r3 == 0) goto L_0x002f
            java.lang.Object r3 = r7.next()     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            ohos.data.rdb.ValuesBucket r3 = (ohos.data.rdb.ValuesBucket) r3     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            ohos.data.rdb.RdbStore$ConflictResolution r4 = ohos.data.rdb.RdbStore.ConflictResolution.ON_CONFLICT_ROLLBACK     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            long r3 = r5.insertWithConflictResolution(r6, r3, r4)     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            r0.add(r3)     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            goto L_0x0015
        L_0x002f:
            r5.markAsCommit()     // Catch:{ RdbConstraintException -> 0x003b, all -> 0x0039 }
            r5.endTransaction()
            r5.releaseRef()
            return r0
        L_0x0039:
            r6 = move-exception
            goto L_0x0054
        L_0x003b:
            r6 = move-exception
            r5.endTransaction()     // Catch:{ RuntimeException -> 0x0043 }
            goto L_0x0053
        L_0x0040:
            r6 = move-exception
            r2 = r1
            goto L_0x0054
        L_0x0043:
            r7 = move-exception
            ohos.hiviewdfx.HiLogLabel r0 = ohos.data.rdb.impl.RdbStoreImpl.LABEL     // Catch:{ all -> 0x0040 }
            java.lang.String r3 = "end transaction occur an error %{private}s."
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0040 }
            java.lang.String r7 = r7.getMessage()     // Catch:{ all -> 0x0040 }
            r2[r1] = r7     // Catch:{ all -> 0x0040 }
            ohos.hiviewdfx.HiLog.error(r0, r3, r2)     // Catch:{ all -> 0x0040 }
        L_0x0053:
            throw r6     // Catch:{ all -> 0x0040 }
        L_0x0054:
            if (r2 == 0) goto L_0x0059
            r5.endTransaction()
        L_0x0059:
            r5.releaseRef()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.innerBatchInsertOrRollback(java.lang.String, java.util.List):java.util.List");
    }

    @Override // ohos.data.rdb.RdbStore
    public long replace(String str, ValuesBucket valuesBucket) {
        try {
            return insertWithConflictResolution(str, valuesBucket, RdbStore.ConflictResolution.ON_CONFLICT_REPLACE);
        } catch (RdbException unused) {
            return -1;
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public long replaceOrThrowException(String str, ValuesBucket valuesBucket) {
        return insertWithConflictResolution(str, valuesBucket, RdbStore.ConflictResolution.ON_CONFLICT_REPLACE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a2, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a3, code lost:
        $closeResource(r7, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a6, code lost:
        throw r8;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long insertWithConflictResolution(java.lang.String r6, ohos.data.rdb.ValuesBucket r7, ohos.data.rdb.RdbStore.ConflictResolution r8) {
        /*
        // Method dump skipped, instructions count: 190
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.insertWithConflictResolution(java.lang.String, ohos.data.rdb.ValuesBucket, ohos.data.rdb.RdbStore$ConflictResolution):long");
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

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0028, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0029, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002c, code lost:
        throw r0;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int update(ohos.data.rdb.ValuesBucket r3, ohos.data.rdb.AbsRdbPredicates r4) {
        /*
            r2 = this;
            r2.checkParam(r3, r4)
            r2.acquireRef()
            int r0 = r3.size()     // Catch:{ all -> 0x002d }
            int r1 = r2.getWhereArgsLength(r4)     // Catch:{ all -> 0x002d }
            int r0 = r0 + r1
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x002d }
            r1 = 0
            java.lang.String r3 = ohos.data.rdb.impl.SqliteSqlBuilder.buildUpdateString(r3, r4, r0, r1)     // Catch:{ all -> 0x002d }
            ohos.data.rdb.impl.GeneralStatement r4 = new ohos.data.rdb.impl.GeneralStatement     // Catch:{ all -> 0x002d }
            r4.<init>(r2, r3, r0)     // Catch:{ all -> 0x002d }
            int r3 = r4.executeAndGetChanges()     // Catch:{ all -> 0x0026 }
            $closeResource(r1, r4)
            r2.releaseRef()
            return r3
        L_0x0026:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0028 }
        L_0x0028:
            r0 = move-exception
            $closeResource(r3, r4)
            throw r0
        L_0x002d:
            r3 = move-exception
            r2.releaseRef()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.update(ohos.data.rdb.ValuesBucket, ohos.data.rdb.AbsRdbPredicates):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002d, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0030, code lost:
        throw r5;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int updateWithConflictResolution(ohos.data.rdb.ValuesBucket r3, ohos.data.rdb.AbsRdbPredicates r4, ohos.data.rdb.RdbStore.ConflictResolution r5) {
        /*
            r2 = this;
            r2.checkParam(r3, r4)
            ohos.data.rdb.RdbStore$ConflictResolution r5 = r2.analysisResolution(r5)
            r2.acquireRef()
            int r0 = r3.size()     // Catch:{ all -> 0x0031 }
            int r1 = r2.getWhereArgsLength(r4)     // Catch:{ all -> 0x0031 }
            int r0 = r0 + r1
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0031 }
            java.lang.String r3 = ohos.data.rdb.impl.SqliteSqlBuilder.buildUpdateString(r3, r4, r0, r5)     // Catch:{ all -> 0x0031 }
            ohos.data.rdb.impl.GeneralStatement r4 = new ohos.data.rdb.impl.GeneralStatement     // Catch:{ all -> 0x0031 }
            r4.<init>(r2, r3, r0)     // Catch:{ all -> 0x0031 }
            r3 = 0
            int r5 = r4.executeAndGetChanges()     // Catch:{ all -> 0x002a }
            $closeResource(r3, r4)
            r2.releaseRef()
            return r5
        L_0x002a:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x002c }
        L_0x002c:
            r5 = move-exception
            $closeResource(r3, r4)
            throw r5
        L_0x0031:
            r3 = move-exception
            r2.releaseRef()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.updateWithConflictResolution(ohos.data.rdb.ValuesBucket, ohos.data.rdb.AbsRdbPredicates, ohos.data.rdb.RdbStore$ConflictResolution):int");
    }

    private void checkParam(ValuesBucket valuesBucket, AbsRdbPredicates absRdbPredicates) {
        if (valuesBucket == null || valuesBucket.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        } else if (absRdbPredicates == null) {
            throw new IllegalArgumentException("Execute update failed : The parameter rdbPredicates is null.");
        }
    }

    private int getWhereArgsLength(AbsRdbPredicates absRdbPredicates) {
        List<String> whereArgs = absRdbPredicates.getWhereArgs();
        if (whereArgs == null || whereArgs.isEmpty()) {
            return 0;
        }
        return whereArgs.size();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0034, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0035, code lost:
        $closeResource(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
        throw r0;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int delete(ohos.data.rdb.AbsRdbPredicates r4) {
        /*
            r3 = this;
            if (r4 == 0) goto L_0x003e
            r3.acquireRef()
            java.lang.String r0 = ohos.data.rdb.impl.SqliteSqlBuilder.buildDeleteString(r4)     // Catch:{ all -> 0x0039 }
            java.util.List r4 = r4.getWhereArgs()     // Catch:{ all -> 0x0039 }
            r1 = 0
            if (r4 == 0) goto L_0x0021
            boolean r2 = r4.isEmpty()     // Catch:{ all -> 0x0039 }
            if (r2 == 0) goto L_0x0017
            goto L_0x0021
        L_0x0017:
            r2 = 0
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ all -> 0x0039 }
            java.lang.Object[] r4 = r4.toArray(r2)     // Catch:{ all -> 0x0039 }
            java.lang.String[] r4 = (java.lang.String[]) r4     // Catch:{ all -> 0x0039 }
            goto L_0x0022
        L_0x0021:
            r4 = r1
        L_0x0022:
            ohos.data.rdb.impl.GeneralStatement r2 = new ohos.data.rdb.impl.GeneralStatement     // Catch:{ all -> 0x0039 }
            r2.<init>(r3, r0, r4)     // Catch:{ all -> 0x0039 }
            int r4 = r2.executeAndGetChanges()     // Catch:{ all -> 0x0032 }
            $closeResource(r1, r2)
            r3.releaseRef()
            return r4
        L_0x0032:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0034 }
        L_0x0034:
            r0 = move-exception
            $closeResource(r4, r2)
            throw r0
        L_0x0039:
            r4 = move-exception
            r3.releaseRef()
            throw r4
        L_0x003e:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "Execute delete failed : The parameter absRdbPredicates is null."
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.delete(ohos.data.rdb.AbsRdbPredicates):int");
    }

    @Override // ohos.data.rdb.RdbStore
    public ResultSet query(AbsRdbPredicates absRdbPredicates, String[] strArr) {
        return queryWithHook(absRdbPredicates, strArr, null);
    }

    @Override // ohos.data.rdb.RdbStore
    public ResultSet queryWithHook(AbsRdbPredicates absRdbPredicates, String[] strArr, ResultSetHook resultSetHook2) {
        if (absRdbPredicates != null) {
            acquireRef();
            try {
                return querySqlWithHook(SqliteSqlBuilder.buildQueryString(absRdbPredicates, strArr), (String[]) absRdbPredicates.getWhereArgs().toArray(new String[0]), resultSetHook2);
            } finally {
                releaseRef();
            }
        } else {
            throw new IllegalArgumentException("Execute query failed : The parameter absRdbPredicates is null.");
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public ResultSet querySql(String str, String[] strArr) {
        return querySqlWithHook(str, strArr, null);
    }

    @Override // ohos.data.rdb.RdbStore
    public ResultSet querySqlWithHook(String str, String[] strArr, ResultSetHook resultSetHook2) {
        acquireRef();
        try {
            QueryStatement queryStatement = new QueryStatement(this, str);
            try {
                queryStatement.setStrings(strArr);
                SqliteSharedResultSet sqliteSharedResultSet = new SqliteSharedResultSet(queryStatement);
                if (resultSetHook2 != null) {
                    resultSetHook2.createHook(str, strArr, sqliteSharedResultSet);
                } else if (this.resultSetHook != null) {
                    this.resultSetHook.createHook(str, strArr, sqliteSharedResultSet);
                }
                return sqliteSharedResultSet;
            } catch (RdbException e) {
                queryStatement.close();
                throw e;
            }
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public ResultSet queryByStep(AbsRdbPredicates absRdbPredicates, String[] strArr) {
        if (absRdbPredicates != null) {
            acquireRef();
            try {
                return querySqlByStep(SqliteSqlBuilder.buildQueryString(absRdbPredicates, strArr), (String[]) absRdbPredicates.getWhereArgs().toArray(new String[0]));
            } finally {
                releaseRef();
            }
        } else {
            throw new IllegalArgumentException("Execute queryByStep failed : The parameter absRdbPredicates is null.");
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public ResultSet querySqlByStep(String str, String[] strArr) {
        acquireRef();
        try {
            QueryStatement queryStatement = new QueryStatement(this, str);
            try {
                queryStatement.setStrings(strArr);
                StepResultSet stepResultSet = new StepResultSet(queryStatement);
                if (this.resultSetHook != null) {
                    this.resultSetHook.createHook(str, strArr, stepResultSet);
                }
                return stepResultSet;
            } catch (RdbException e) {
                queryStatement.close();
                throw e;
            }
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public void executeSql(String str) {
        executeSql(str, null);
    }

    @Override // ohos.data.rdb.RdbStore
    public void executeSql(String str, Object[] objArr) {
        if (SqliteDatabaseUtils.getSqlStatementType(str) != 3) {
            innerExecuteSql(str, objArr);
            return;
        }
        throw new IllegalStateException("The ATTACH statement is not supported in WAL mode");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0036, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0037, code lost:
        $closeResource(r5, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x003a, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void innerExecuteSql(java.lang.String r4, java.lang.Object[] r5) {
        /*
            r3 = this;
            r3.acquireRef()
            r0 = 9
            ohos.data.rdb.impl.GeneralStatement r1 = new ohos.data.rdb.impl.GeneralStatement     // Catch:{ all -> 0x003b }
            r1.<init>(r3, r4, r5)     // Catch:{ all -> 0x003b }
            r5 = 0
            r1.executeAndGetChanges()     // Catch:{ all -> 0x0034 }
            $closeResource(r5, r1)
            int r4 = ohos.data.rdb.impl.SqliteDatabaseUtils.getSqlStatementType(r4)     // Catch:{ all -> 0x005c }
            if (r4 != r0) goto L_0x0030
            java.lang.Object r4 = r3.parametersLock     // Catch:{ all -> 0x005c }
            monitor-enter(r4)     // Catch:{ all -> 0x005c }
            ohos.data.rdb.impl.ConnectionPool r5 = r3.connectionPool     // Catch:{ all -> 0x002d }
            if (r5 == 0) goto L_0x0025
            ohos.data.rdb.impl.ConnectionPool r5 = r3.connectionPool     // Catch:{ all -> 0x002d }
            r5.closeAllReadConnections()     // Catch:{ all -> 0x002d }
            monitor-exit(r4)     // Catch:{ all -> 0x002d }
            goto L_0x0030
        L_0x0025:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch:{ all -> 0x002d }
            java.lang.String r0 = "The RdbStore has been closed."
            r5.<init>(r0)     // Catch:{ all -> 0x002d }
            throw r5     // Catch:{ all -> 0x002d }
        L_0x002d:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x002d }
            throw r5
        L_0x0030:
            r3.releaseRef()
            return
        L_0x0034:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0036 }
        L_0x0036:
            r2 = move-exception
            $closeResource(r5, r1)
            throw r2
        L_0x003b:
            r5 = move-exception
            int r4 = ohos.data.rdb.impl.SqliteDatabaseUtils.getSqlStatementType(r4)
            if (r4 != r0) goto L_0x005b
            java.lang.Object r4 = r3.parametersLock
            monitor-enter(r4)
            ohos.data.rdb.impl.ConnectionPool r0 = r3.connectionPool     // Catch:{ all -> 0x0058 }
            if (r0 != 0) goto L_0x0051
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0058 }
            java.lang.String r0 = "The RdbStore has been closed."
            r5.<init>(r0)     // Catch:{ all -> 0x0058 }
            throw r5     // Catch:{ all -> 0x0058 }
        L_0x0051:
            ohos.data.rdb.impl.ConnectionPool r0 = r3.connectionPool     // Catch:{ all -> 0x0058 }
            r0.closeAllReadConnections()     // Catch:{ all -> 0x0058 }
            monitor-exit(r4)     // Catch:{ all -> 0x0058 }
            goto L_0x005b
        L_0x0058:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0058 }
            throw r5
        L_0x005b:
            throw r5
        L_0x005c:
            r4 = move-exception
            r3.releaseRef()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.innerExecuteSql(java.lang.String, java.lang.Object[]):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0048, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        throw r5;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long count(java.lang.String r3, java.lang.String r4, java.lang.String[] r5) {
        /*
            r2 = this;
            boolean r0 = r2.isEmpty(r3)
            if (r0 != 0) goto L_0x0051
            r2.acquireRef()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x004c }
            r0.<init>()     // Catch:{ all -> 0x004c }
            java.lang.String r1 = "SELECT COUNT(*) FROM "
            r0.append(r1)     // Catch:{ all -> 0x004c }
            r0.append(r3)     // Catch:{ all -> 0x004c }
            java.lang.String r3 = r0.toString()     // Catch:{ all -> 0x004c }
            boolean r0 = r2.isNotEmpty(r4)     // Catch:{ all -> 0x004c }
            if (r0 == 0) goto L_0x0034
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x004c }
            r0.<init>()     // Catch:{ all -> 0x004c }
            r0.append(r3)     // Catch:{ all -> 0x004c }
            java.lang.String r3 = " WHERE "
            r0.append(r3)     // Catch:{ all -> 0x004c }
            r0.append(r4)     // Catch:{ all -> 0x004c }
            java.lang.String r3 = r0.toString()     // Catch:{ all -> 0x004c }
        L_0x0034:
            ohos.data.rdb.impl.GeneralStatement r4 = new ohos.data.rdb.impl.GeneralStatement     // Catch:{ all -> 0x004c }
            r4.<init>(r2, r3, r5)     // Catch:{ all -> 0x004c }
            r3 = 0
            long r0 = r4.executeAndGetLong()     // Catch:{ all -> 0x0045 }
            $closeResource(r3, r4)
            r2.releaseRef()
            return r0
        L_0x0045:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r5 = move-exception
            $closeResource(r3, r4)
            throw r5
        L_0x004c:
            r3 = move-exception
            r2.releaseRef()
            throw r3
        L_0x0051:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r3 = "no tableName specified."
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.count(java.lang.String, java.lang.String, java.lang.String[]):long");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0029, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        $closeResource(r5, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002d, code lost:
        throw r0;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long count(ohos.data.rdb.AbsRdbPredicates r5) {
        /*
            r4 = this;
            if (r5 == 0) goto L_0x0033
            r4.acquireRef()
            java.lang.String r0 = ohos.data.rdb.impl.SqliteSqlBuilder.buildCountString(r5)     // Catch:{ all -> 0x002e }
            java.util.List r5 = r5.getWhereArgs()     // Catch:{ all -> 0x002e }
            r1 = 0
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ all -> 0x002e }
            java.lang.Object[] r5 = r5.toArray(r1)     // Catch:{ all -> 0x002e }
            java.lang.String[] r5 = (java.lang.String[]) r5     // Catch:{ all -> 0x002e }
            ohos.data.rdb.impl.GeneralStatement r1 = new ohos.data.rdb.impl.GeneralStatement     // Catch:{ all -> 0x002e }
            r1.<init>(r4, r0, r5)     // Catch:{ all -> 0x002e }
            r5 = 0
            long r2 = r1.executeAndGetLong()     // Catch:{ all -> 0x0027 }
            $closeResource(r5, r1)
            r4.releaseRef()
            return r2
        L_0x0027:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0029 }
        L_0x0029:
            r0 = move-exception
            $closeResource(r5, r1)
            throw r0
        L_0x002e:
            r5 = move-exception
            r4.releaseRef()
            throw r5
        L_0x0033:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Execute count failed : The parameter rdbPredicates is null."
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.count(ohos.data.rdb.AbsRdbPredicates):long");
    }

    private boolean isNotEmpty(String str) {
        return (str == null || str.length() == 0) ? false : true;
    }

    private boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0020, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        $closeResource(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
        throw r2;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getVersion() {
        /*
            r5 = this;
            r5.acquireRef()
            ohos.data.rdb.impl.GeneralStatement r0 = new ohos.data.rdb.impl.GeneralStatement     // Catch:{ all -> 0x0025 }
            java.lang.String r1 = "PRAGMA user_version;"
            r2 = 0
            r0.<init>(r5, r1, r2)     // Catch:{ all -> 0x0025 }
            long r3 = r0.executeAndGetLong()     // Catch:{ all -> 0x001e }
            java.lang.Long r1 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x001e }
            int r1 = r1.intValue()     // Catch:{ all -> 0x001e }
            $closeResource(r2, r0)
            r5.releaseRef()
            return r1
        L_0x001e:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0020 }
        L_0x0020:
            r2 = move-exception
            $closeResource(r1, r0)
            throw r2
        L_0x0025:
            r0 = move-exception
            r5.releaseRef()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.getVersion():int");
    }

    @Override // ohos.data.rdb.RdbStore
    public void setVersion(int i) {
        executeSql("PRAGMA user_version = " + i);
    }

    @Override // ohos.data.rdb.RdbStore
    public Statement buildStatement(String str) {
        acquireRef();
        try {
            return new GeneralStatement(this, str, null);
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public void beginTransaction() {
        beginTransaction(null);
    }

    @Override // ohos.data.rdb.RdbStore
    public void beginTransactionWithObserver(TransactionObserver transactionObserver) {
        beginTransaction(transactionObserver);
    }

    private void beginTransaction(TransactionObserver transactionObserver) {
        acquireRef();
        try {
            getThreadSession().beginTransaction(transactionObserver);
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public void markAsCommit() {
        acquireRef();
        try {
            getThreadSession().markAsCommit();
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public void endTransaction() {
        acquireRef();
        try {
            getThreadSession().endTransaction();
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean isInTransaction() {
        acquireRef();
        try {
            return getThreadSession().inTransaction();
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public void giveConnectionTemporarily(long j) {
        acquireRef();
        try {
            getThreadSession().giveConnectionTemporarily(j);
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean isHoldingConnection() {
        acquireRef();
        try {
            return getThreadSession().isHoldingConnection();
        } finally {
            releaseRef();
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.data.rdb.impl.CoreCloseable
    public void onAllRefReleased() {
        ConnectionPool connectionPool2;
        synchronized (this.parametersLock) {
            connectionPool2 = this.connectionPool;
            this.connectionPool = null;
        }
        if (connectionPool2 != null) {
            connectionPool2.close();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean isOpen() {
        boolean z;
        synchronized (this.parametersLock) {
            z = this.connectionPool != null;
        }
        return z;
    }

    @Override // ohos.data.rdb.RdbStore
    public final String getPath() {
        String path;
        synchronized (this.parametersLock) {
            if (this.connectionPool != null) {
                path = this.connectionPool.getPath();
            } else {
                throw new IllegalStateException("The RdbStore has been closed.");
            }
        }
        return path;
    }

    private DatabaseFileSecurityLevel getDatabaseFileSecurityLevel() {
        DatabaseFileSecurityLevel databaseFileSecurityLevel;
        synchronized (this.parametersLock) {
            if (this.connectionPool != null) {
                databaseFileSecurityLevel = this.connectionPool.getDatabaseFileSecurityLevel();
            } else {
                throw new IllegalStateException("The RdbStore has been closed.");
            }
        }
        return databaseFileSecurityLevel;
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean isReadOnly() {
        boolean isReadOnly;
        synchronized (this.parametersLock) {
            if (this.connectionPool != null) {
                isReadOnly = this.connectionPool.isReadOnly();
            } else {
                throw new IllegalStateException("The RdbStore has been closed.");
            }
        }
        return isReadOnly;
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean isMemoryRdb() {
        boolean isMemoryDb;
        synchronized (this.parametersLock) {
            if (this.connectionPool != null) {
                isMemoryDb = this.connectionPool.isMemoryDb();
            } else {
                throw new IllegalStateException("The RdbStore has been closed.");
            }
        }
        return isMemoryDb;
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean checkIntegrity() {
        acquireRef();
        try {
            for (Pair<String, String> pair : listAttached()) {
                Statement statement = null;
                try {
                    Statement buildStatement = buildStatement("pragma " + ((String) pair.f) + " .integrity_check(1)");
                    String executeAndGetString = buildStatement.executeAndGetString();
                    if (!"ok".equalsIgnoreCase(executeAndGetString)) {
                        HiLog.error(LABEL, "Pragma integrity_check on %{private}s returned: %{private}s", pair.s, executeAndGetString);
                        buildStatement.close();
                        return false;
                    }
                    buildStatement.close();
                } catch (Throwable th) {
                    if (0 != 0) {
                        statement.close();
                    }
                    throw th;
                }
            }
            releaseRef();
            return true;
        } finally {
            releaseRef();
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public String toString() {
        return "Relation database store: " + getPath();
    }

    /* access modifiers changed from: package-private */
    public void handleCorruption() {
        HiLog.error(LABEL, "File is not a database or encrypt key is error.", new Object[0]);
        RdbOpenCallback rdbOpenCallback = this.openCallback;
        if (rdbOpenCallback != null) {
            rdbOpenCallback.onCorruption(new File(getPath()));
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean backup(String str) {
        return backup(str, null);
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean backup(String str, byte[] bArr) {
        boolean z = (bArr == null || bArr.length == 0) ? false : true;
        if (z && bArr.length > 1024) {
            throw new IllegalArgumentException("Encrypt Key size exceeds maximum limit.");
        } else if (!isInTransaction()) {
            DatabaseFileSecurityLevel databaseFileSecurityLevel = getDatabaseFileSecurityLevel();
            File databasePath = SqliteDatabaseUtils.getDatabasePath(this.context, new DatabaseFileConfig.Builder().setName(str).setEncrypted(z).setDatabaseFileType(DatabaseFileType.BACKUP).setDatabaseFileSecurityLevel(databaseFileSecurityLevel).build());
            if (!databasePath.exists() || databasePath.delete()) {
                SqliteEncryptKeyLoader generate = SqliteEncryptKeyLoader.generate(this.context, bArr);
                try {
                    executeBackup(databasePath.getPath(), generate);
                    FileProtectHelper.setFileLabel(this.context, databasePath.getPath(), databaseFileSecurityLevel);
                    return true;
                } finally {
                    generate.destroy();
                }
            } else {
                HiLog.error(LABEL, "Can not delete backed up file", new Object[0]);
                throw new IllegalArgumentException("Can not delete backed up file");
            }
        } else {
            HiLog.info(LABEL, "The rdb is in transaction. %{private}s", getPath());
            throw new IllegalArgumentException("The rdb is in transaction.");
        }
    }

    private void executeBackup(String str, SqliteEncryptKeyLoader sqliteEncryptKeyLoader) {
        getThreadSession().backup(str, sqliteEncryptKeyLoader);
    }

    private RdbStore.ConflictResolution analysisResolution(RdbStore.ConflictResolution conflictResolution) {
        return conflictResolution == null ? RdbStore.ConflictResolution.ON_CONFLICT_NONE : conflictResolution;
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean restore(String str) {
        return restore(str, null, null);
    }

    @Override // ohos.data.rdb.RdbStore
    public boolean restore(String str, byte[] bArr, byte[] bArr2) {
        if ((bArr != null && bArr.length > 1024) || (bArr2 != null && bArr2.length > 1024)) {
            throw new IllegalArgumentException("Encrypt Key size exceeds maximum limit.");
        } else if (!isInTransaction()) {
            DatabaseFileSecurityLevel databaseFileSecurityLevel = getDatabaseFileSecurityLevel();
            if (SqliteDatabaseUtils.getDatabasePath(this.context, new DatabaseFileConfig.Builder().setName(str).setEncrypted((bArr == null || bArr.length == 0) ? false : true).setDatabaseFileType(DatabaseFileType.BACKUP).setDatabaseFileSecurityLevel(databaseFileSecurityLevel).build()).exists()) {
                return executeRestore(str, bArr, bArr2, databaseFileSecurityLevel);
            }
            HiLog.error(LABEL, "File %{public}s doesn't exist.", str);
            throw new IllegalArgumentException("The file doesn't exist.");
        } else {
            HiLog.error(LABEL, "The rdb is in transaction. %{private}s", getPath());
            throw new IllegalArgumentException("The rdb is in transaction.");
        }
    }

    private boolean executeRestore(String str, byte[] bArr, byte[] bArr2, DatabaseFileSecurityLevel databaseFileSecurityLevel) {
        try {
            RdbStoreImpl open = open(this.context, new StoreConfig.Builder().setName(str).setStorageMode(StoreConfig.StorageMode.MODE_DISK).setReadOnly(false).setEncryptKey(bArr).setDatabaseFileType(DatabaseFileType.BACKUP).setDatabaseFileSecurityLevel(databaseFileSecurityLevel).build(), this.version, null, this.resultSetHook);
            String str2 = open.getPath() + "-temp-" + System.currentTimeMillis();
            SqliteEncryptKeyLoader generate = SqliteEncryptKeyLoader.generate(this.context, bArr2);
            try {
                open.executeBackup(str2, generate);
                boolean z = (bArr2 == null || bArr2.length == 0) ? false : true;
                synchronized (this.parametersLock) {
                    if (isOpen()) {
                        String path = SqliteDatabaseUtils.getDatabasePath(this.context, new DatabaseFileConfig.Builder().setName(this.connectionPool.getName()).setEncrypted(z).setDatabaseFileType(this.connectionPool.getDatabaseFileType()).setDatabaseFileSecurityLevel(databaseFileSecurityLevel).build()).getPath();
                        if (!this.connectionPool.changeDbFileForRestore(path, str2, generate)) {
                            return false;
                        }
                        FileProtectHelper.setFileLabel(this.context, path, databaseFileSecurityLevel);
                        generate.destroy();
                        open.close();
                        return true;
                    }
                    throw new IllegalStateException("The RdbStore has been closed.");
                }
            } finally {
                generate.destroy();
                open.close();
            }
        } catch (RuntimeException unused) {
            HiLog.error(LABEL, "Open Rdb store failed. %{public}s", str);
            return false;
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00a3, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a4, code lost:
        $closeResource(r7, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00a7, code lost:
        throw r8;
     */
    @Override // ohos.data.rdb.RdbStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addAttach(java.lang.String r7, java.lang.String r8, byte[] r9) {
        /*
        // Method dump skipped, instructions count: 181
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.addAttach(java.lang.String, java.lang.String, byte[]):void");
    }

    @Override // ohos.data.rdb.RdbStore
    public List<Pair<String, String>> listAttached() {
        ArrayList arrayList = new ArrayList();
        acquireRef();
        ResultSet resultSet = null;
        try {
            ResultSet querySqlByStep = querySqlByStep("pragma database_list", null);
            while (querySqlByStep.goToNextRow()) {
                arrayList.add(new Pair(querySqlByStep.getString(1), querySqlByStep.getString(2)));
            }
            try {
                querySqlByStep.close();
                return arrayList;
            } finally {
                releaseRef();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                resultSet.close();
            }
            throw th;
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public void changeEncryptKey(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("Empty new encrypt key.");
        } else if (bArr.length <= 1024) {
            synchronized (this.parametersLock) {
                if (isOpen()) {
                    try {
                        this.connectionPool.changeEncryptKey(SqliteEncryptKeyLoader.generate(this.context, bArr));
                    } catch (IllegalStateException | RdbException e) {
                        HiLog.error(LABEL, "Failed to change the encryption key of database %{public}s", this.connectionPool.getName());
                        throw e;
                    }
                } else {
                    throw new IllegalStateException("Change encrypt key on a no-open database.");
                }
            }
        } else {
            throw new IllegalArgumentException("Encrypt Key size exceeds maximum limit.");
        }
    }

    public static int releaseRdbMemory() {
        return SqliteGlobalConfig.releaseRdbMemory();
    }

    public void verifySQl(String str) {
        acquireRef();
        try {
            getThreadSession().prepare(str, true);
        } finally {
            releaseRef();
        }
    }

    public void verifyPredicates(RdbUtils.OperationType operationType, AbsRdbPredicates absRdbPredicates) {
        acquireRef();
        int i = AnonymousClass1.$SwitchMap$ohos$data$rdb$RdbUtils$OperationType[operationType.ordinal()];
        String str = null;
        if (i == 1) {
            str = SqliteSqlBuilder.buildQueryString(absRdbPredicates, null);
        } else if (i == 2) {
            str = SqliteSqlBuilder.buildDeleteString(absRdbPredicates);
        } else if (i != 3) {
            HiLog.error(LABEL, "no type matched in verifyPredicates", new Object[0]);
        } else {
            str = SqliteSqlBuilder.buildCountString(absRdbPredicates);
        }
        try {
            getThreadSession().prepare(str, true);
        } finally {
            releaseRef();
        }
    }

    /* renamed from: ohos.data.rdb.impl.RdbStoreImpl$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$data$rdb$RdbUtils$OperationType = new int[RdbUtils.OperationType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.data.rdb.RdbUtils$OperationType[] r0 = ohos.data.rdb.RdbUtils.OperationType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.data.rdb.impl.RdbStoreImpl.AnonymousClass1.$SwitchMap$ohos$data$rdb$RdbUtils$OperationType = r0
                int[] r0 = ohos.data.rdb.impl.RdbStoreImpl.AnonymousClass1.$SwitchMap$ohos$data$rdb$RdbUtils$OperationType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.data.rdb.RdbUtils$OperationType r1 = ohos.data.rdb.RdbUtils.OperationType.QUERY_TYPE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.data.rdb.impl.RdbStoreImpl.AnonymousClass1.$SwitchMap$ohos$data$rdb$RdbUtils$OperationType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.data.rdb.RdbUtils$OperationType r1 = ohos.data.rdb.RdbUtils.OperationType.DELETE_TYPE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.data.rdb.impl.RdbStoreImpl.AnonymousClass1.$SwitchMap$ohos$data$rdb$RdbUtils$OperationType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.data.rdb.RdbUtils$OperationType r1 = ohos.data.rdb.RdbUtils.OperationType.COUNT_TYPE     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.RdbStoreImpl.AnonymousClass1.<clinit>():void");
        }
    }

    @Override // ohos.data.rdb.RdbStore
    public void configLocale(Locale locale) {
        if (locale != null) {
            synchronized (this.parametersLock) {
                if (!isOpen()) {
                    throw new IllegalStateException("Config locale on a no-open database.");
                } else if (!isInTransaction()) {
                    this.connectionPool.configLocale(locale);
                } else {
                    HiLog.info(LABEL, "The rdb is in transaction. %{private}s", getPath());
                    throw new IllegalArgumentException("The rdb is in transaction.");
                }
            }
            return;
        }
        throw new IllegalArgumentException("Locale must not be null.");
    }
}
