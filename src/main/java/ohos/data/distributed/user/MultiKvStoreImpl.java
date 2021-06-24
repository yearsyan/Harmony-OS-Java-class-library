package ohos.data.distributed.user;

import java.util.List;
import java.util.Objects;
import ohos.data.distributed.common.Entry;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvStore;
import ohos.data.distributed.common.KvStoreErrorCode;
import ohos.data.distributed.common.KvStoreException;
import ohos.data.distributed.common.KvStoreObserver;
import ohos.data.distributed.common.Options;
import ohos.data.distributed.common.SubscribeType;
import ohos.data.distributed.common.TextUtils;
import ohos.data.distributed.common.Value;

public class MultiKvStoreImpl implements MultiKvStore {
    private KvManagerConfig config;
    private long nativeKvStore;
    private Options options;
    private String storeId;

    private native void nativeClear(long j);

    private native void nativeCommit(long j);

    private native boolean nativeDelete(byte[] bArr, long j);

    private native void nativeDeleteBatch(String[] strArr, long j);

    private native long nativeGetKvStoreSnapshot(KvStoreObserver kvStoreObserver, long j);

    private native boolean nativePut(byte[] bArr, Value value, long j);

    private native void nativePutBatch(Entry[] entryArr, long j);

    private native void nativeReleaseKvStoreSnapshot(long j, KvStoreSnapshot kvStoreSnapshot);

    private native void nativeRollback(long j);

    private native void nativeStartTransaction(long j);

    private native boolean nativeSubscribe(SubscribeType subscribeType, KvStoreObserver kvStoreObserver, KvManagerConfig kvManagerConfig, long j);

    private native boolean nativeUnSubscribe(SubscribeType subscribeType, KvStoreObserver kvStoreObserver, KvManagerConfig kvManagerConfig, long j);

    @Override // ohos.data.distributed.common.KvStore
    public void enableSync(boolean z) throws KvStoreException {
    }

    @Override // ohos.data.distributed.common.KvStore
    public void setSyncRange(List<String> list, List<String> list2) throws KvStoreException {
    }

    public MultiKvStoreImpl(KvManagerConfig kvManagerConfig, String str, long j, Options options2) {
        this.config = kvManagerConfig;
        this.storeId = str;
        this.nativeKvStore = j;
        this.options = options2;
    }

    public Options getOptions() {
        return this.options;
    }

    @Override // ohos.data.distributed.common.KvStore
    public String getStoreId() throws KvStoreException {
        return this.storeId;
    }

    public long getNativeKvStore() {
        return this.nativeKvStore;
    }

    @Override // ohos.data.distributed.common.KvStore
    public void putBoolean(String str, boolean z) throws KvStoreException {
        TextUtils.assertKey(str);
        putInt(str, z ? 1 : 0);
    }

    @Override // ohos.data.distributed.common.KvStore
    public void putInt(String str, int i) throws KvStoreException {
        TextUtils.assertKey(str);
        nativePut(TextUtils.getKeyBytes(str), Value.get(i), this.nativeKvStore);
    }

    @Override // ohos.data.distributed.common.KvStore
    public void putFloat(String str, float f) throws KvStoreException {
        TextUtils.assertKey(str);
        nativePut(TextUtils.getKeyBytes(str), Value.get(f), this.nativeKvStore);
    }

    @Override // ohos.data.distributed.common.KvStore
    public void putDouble(String str, double d) throws KvStoreException {
        TextUtils.assertKey(str);
        nativePut(TextUtils.getKeyBytes(str), Value.get(d), this.nativeKvStore);
    }

    @Override // ohos.data.distributed.common.KvStore
    public void putString(String str, String str2) throws KvStoreException {
        TextUtils.assertKey(str);
        if (TextUtils.lenLessEqualThan(str2, KvStore.MAX_VALUE_LENGTH)) {
            nativePut(TextUtils.getKeyBytes(str), Value.get(str2), this.nativeKvStore);
            return;
        }
        throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "value over maximum size.");
    }

    @Override // ohos.data.distributed.common.KvStore
    public void putByteArray(String str, byte[] bArr) throws KvStoreException {
        TextUtils.assertKey(str);
        if (Objects.isNull(bArr) || bArr.length > 4194303) {
            throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "over maximum value length.");
        }
        nativePut(TextUtils.getKeyBytes(str), Value.get(bArr), this.nativeKvStore);
    }

    @Override // ohos.data.distributed.common.KvStore
    public void delete(String str) throws KvStoreException {
        TextUtils.assertKey(str);
        nativeDelete(TextUtils.getKeyBytes(str), this.nativeKvStore);
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0010  */
    @Override // ohos.data.distributed.common.KvStore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void putBatch(java.util.List<ohos.data.distributed.common.Entry> r4) throws ohos.data.distributed.common.KvStoreException {
        /*
            r3 = this;
            boolean r0 = ohos.data.distributed.common.TextUtils.isListEmpty(r4)
            if (r0 != 0) goto L_0x004b
            java.util.Iterator r0 = r4.iterator()
        L_0x000a:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0039
            java.lang.Object r1 = r0.next()
            ohos.data.distributed.common.Entry r1 = (ohos.data.distributed.common.Entry) r1
            boolean r2 = java.util.Objects.isNull(r1)
            if (r2 != 0) goto L_0x0031
            java.lang.String r2 = r1.getKey()
            boolean r2 = ohos.data.distributed.common.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x0031
            ohos.data.distributed.common.Value r1 = r1.getValue()
            boolean r1 = java.util.Objects.isNull(r1)
            if (r1 != 0) goto L_0x0031
            goto L_0x000a
        L_0x0031:
            ohos.data.distributed.common.KvStoreException r3 = new ohos.data.distributed.common.KvStoreException
            ohos.data.distributed.common.KvStoreErrorCode r4 = ohos.data.distributed.common.KvStoreErrorCode.INVALID_ARGUMENT
            r3.<init>(r4)
            throw r3
        L_0x0039:
            int r0 = r4.size()
            ohos.data.distributed.common.Entry[] r0 = new ohos.data.distributed.common.Entry[r0]
            java.lang.Object[] r4 = r4.toArray(r0)
            ohos.data.distributed.common.Entry[] r4 = (ohos.data.distributed.common.Entry[]) r4
            long r0 = r3.nativeKvStore
            r3.nativePutBatch(r4, r0)
            return
        L_0x004b:
            ohos.data.distributed.common.KvStoreException r3 = new ohos.data.distributed.common.KvStoreException
            ohos.data.distributed.common.KvStoreErrorCode r4 = ohos.data.distributed.common.KvStoreErrorCode.INVALID_ARGUMENT
            java.lang.String r0 = "entries is empty."
            r3.<init>(r4, r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.distributed.user.MultiKvStoreImpl.putBatch(java.util.List):void");
    }

    @Override // ohos.data.distributed.common.KvStore
    public void deleteBatch(List<String> list) throws KvStoreException {
        if (!TextUtils.isListEmpty(list)) {
            for (String str : list) {
                if (!TextUtils.lenLessEqualThan(str, 1024)) {
                    KvStoreErrorCode kvStoreErrorCode = KvStoreErrorCode.INVALID_ARGUMENT;
                    throw new KvStoreException(kvStoreErrorCode, "key:" + str + ", over maximum length.");
                }
            }
            nativeDeleteBatch((String[]) list.toArray(new String[list.size()]), this.nativeKvStore);
            return;
        }
        throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "keys is empty.");
    }

    @Override // ohos.data.distributed.user.MultiKvStore
    public void clear() throws KvStoreException {
        nativeClear(this.nativeKvStore);
    }

    @Override // ohos.data.distributed.user.MultiKvStore
    public KvStoreSnapshot getKvStoreSnapshot(KvStoreObserver kvStoreObserver) throws KvStoreException {
        return new KvStoreSnapshotImpl(nativeGetKvStoreSnapshot(kvStoreObserver, this.nativeKvStore));
    }

    @Override // ohos.data.distributed.user.MultiKvStore
    public void releaseKvStoreSnapshot(KvStoreSnapshot kvStoreSnapshot) throws KvStoreException {
        if (Objects.isNull(kvStoreSnapshot)) {
            throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "input param is null.");
        } else if (!kvStoreSnapshot.getClass().isAssignableFrom(KvStoreSnapshotImpl.class)) {
            throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "invalid instance.");
        } else if (((KvStoreSnapshotImpl) KvStoreSnapshotImpl.class.cast(kvStoreSnapshot)).getSnapshot() > 0) {
            nativeReleaseKvStoreSnapshot(this.nativeKvStore, kvStoreSnapshot);
        } else {
            throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "invalid instance, snapshot closed.");
        }
    }

    @Override // ohos.data.distributed.common.KvStore
    public void subscribe(SubscribeType subscribeType, KvStoreObserver kvStoreObserver) throws KvStoreException {
        if (!Objects.isNull(kvStoreObserver)) {
            nativeSubscribe(subscribeType, kvStoreObserver, this.config, this.nativeKvStore);
            return;
        }
        throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "observer is null.");
    }

    @Override // ohos.data.distributed.common.KvStore
    public void unSubscribe(KvStoreObserver kvStoreObserver) throws KvStoreException {
        if (!Objects.isNull(kvStoreObserver)) {
            nativeUnSubscribe(SubscribeType.SUBSCRIBE_TYPE_ALL, kvStoreObserver, this.config, this.nativeKvStore);
            return;
        }
        throw new KvStoreException(KvStoreErrorCode.INVALID_ARGUMENT, "observer is null.");
    }

    @Override // ohos.data.distributed.common.KvStore
    public void startTransaction() throws KvStoreException {
        nativeStartTransaction(this.nativeKvStore);
    }

    @Override // ohos.data.distributed.common.KvStore
    public void commit() throws KvStoreException {
        nativeCommit(this.nativeKvStore);
    }

    @Override // ohos.data.distributed.common.KvStore
    public void rollback() throws KvStoreException {
        nativeRollback(this.nativeKvStore);
    }
}
