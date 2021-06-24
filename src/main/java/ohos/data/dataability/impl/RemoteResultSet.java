package ohos.data.dataability.impl;

import java.lang.ref.WeakReference;
import java.util.Vector;
import ohos.data.rdb.DataObserver;
import ohos.data.resultset.AbsSharedResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;

public class RemoteResultSet extends AbsSharedResultSet {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109520, "RemoteResultSet");
    private final Object LOCK = new Object();
    private String[] columns;
    private int count;
    private RemoteDataObservable dataObservable = new RemoteDataObservable();
    private boolean isClosed = false;
    private IResultSetRemoteTransport remoteTransport;
    private RemoteResultSetObserverStub stub;

    public RemoteResultSet(ResultSetRemoteTransportDescriptor resultSetRemoteTransportDescriptor) {
        super("");
        if (resultSetRemoteTransportDescriptor != null) {
            this.remoteTransport = resultSetRemoteTransportDescriptor.getRemoteTransport();
            this.columns = resultSetRemoteTransportDescriptor.getColumnNames();
            this.count = resultSetRemoteTransportDescriptor.getCount();
            this.sharedBlock = null;
            return;
        }
        HiLog.info(LABEL, "RemoteResultSet: descriptor cannot be null.", new Object[0]);
        throw new IllegalArgumentException("descriptor cannot be null");
    }

    private void throwIfResultSetIsClosed() {
        if (this.remoteTransport == null) {
            throw new IllegalStateException("Attempted to access a resultSet after it has been closed.");
        }
    }

    @Override // ohos.data.resultset.SharedResultSet, ohos.data.resultset.AbsSharedResultSet
    public boolean onGo(int i, int i2) {
        throwIfResultSetIsClosed();
        try {
            if (this.sharedBlock == null) {
                if (!this.remoteTransport.onMove(i, i2)) {
                    return false;
                }
                setBlock(this.remoteTransport.getBlock());
            }
            if (this.sharedBlock == null) {
                HiLog.info(LABEL, "sharedBlock is null object.", new Object[0]);
                return false;
            }
            if (i2 < this.sharedBlock.getStartRowIndex() || i2 >= this.sharedBlock.getStartRowIndex() + this.sharedBlock.getRowCount()) {
                if (!this.remoteTransport.onMove(i, i2)) {
                    return false;
                }
                setBlock(this.remoteTransport.getBlock());
            }
            if (this.sharedBlock == null) {
                return false;
            }
            return true;
        } catch (RemoteException unused) {
            HiLog.info(LABEL, "Unable to getBlock for the remote connection is dead.", new Object[0]);
            return false;
        }
    }

    @Override // ohos.data.resultset.AbsResultSet, ohos.data.resultset.ResultSet
    public int getRowCount() {
        throwIfResultSetIsClosed();
        return this.count;
    }

    @Override // ohos.data.resultset.AbsResultSet, ohos.data.resultset.ResultSet
    public int getColumnCount() {
        throwIfResultSetIsClosed();
        return this.columns.length;
    }

    @Override // ohos.data.resultset.AbsResultSet, ohos.data.resultset.ResultSet
    public String[] getAllColumnNames() {
        throwIfResultSetIsClosed();
        return this.columns;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0027, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        ohos.hiviewdfx.HiLog.info(ohos.data.dataability.impl.RemoteResultSet.LABEL, "Remote process exception when block is closed.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0034, code lost:
        r5.remoteTransport = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0036, code lost:
        throw r2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0029 */
    @Override // ohos.data.resultset.AbsResultSet, ohos.data.resultset.AbsSharedResultSet, ohos.data.resultset.ResultSet
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.LOCK
            monitor-enter(r0)
            super.close()     // Catch:{ all -> 0x0039 }
            ohos.data.resultset.SharedBlock r1 = r5.sharedBlock     // Catch:{ all -> 0x0039 }
            if (r1 == 0) goto L_0x000f
            ohos.data.resultset.SharedBlock r1 = r5.sharedBlock     // Catch:{ all -> 0x0039 }
            r1.close()     // Catch:{ all -> 0x0039 }
        L_0x000f:
            r1 = 1
            r5.isClosed = r1     // Catch:{ all -> 0x0039 }
            ohos.data.dataability.impl.IResultSetRemoteTransport r1 = r5.remoteTransport     // Catch:{ all -> 0x0039 }
            if (r1 == 0) goto L_0x0037
            r1 = 0
            r5.unregisterRemoteObserver()     // Catch:{ RemoteException -> 0x0029 }
            ohos.data.dataability.impl.RemoteResultSet$RemoteDataObservable r2 = r5.dataObservable     // Catch:{ RemoteException -> 0x0029 }
            r2.removeAll()     // Catch:{ RemoteException -> 0x0029 }
            ohos.data.dataability.impl.IResultSetRemoteTransport r2 = r5.remoteTransport     // Catch:{ RemoteException -> 0x0029 }
            r2.close()     // Catch:{ RemoteException -> 0x0029 }
        L_0x0024:
            r5.remoteTransport = r1
            goto L_0x0037
        L_0x0027:
            r2 = move-exception
            goto L_0x0034
        L_0x0029:
            ohos.hiviewdfx.HiLogLabel r2 = ohos.data.dataability.impl.RemoteResultSet.LABEL     // Catch:{ all -> 0x0027 }
            java.lang.String r3 = "Remote process exception when block is closed."
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0027 }
            ohos.hiviewdfx.HiLog.info(r2, r3, r4)     // Catch:{ all -> 0x0027 }
            goto L_0x0024
        L_0x0034:
            r5.remoteTransport = r1
            throw r2
        L_0x0037:
            monitor-exit(r0)
            return
        L_0x0039:
            r5 = move-exception
            monitor-exit(r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.dataability.impl.RemoteResultSet.close():void");
    }

    @Override // ohos.data.resultset.AbsResultSet, ohos.data.resultset.ResultSet
    public boolean isClosed() {
        return this.isClosed;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.data.resultset.AbsResultSet
    public void notifyChange() {
        super.notifyChange();
    }

    @Override // ohos.data.resultset.AbsResultSet, ohos.data.resultset.ResultSet
    public void registerObserver(DataObserver dataObserver) {
        synchronized (this.LOCK) {
            throwIfResultSetIsClosed();
            super.registerObserver(dataObserver);
            this.dataObservable.add(dataObserver);
            try {
                if (this.stub == null) {
                    this.stub = new RemoteResultSetObserverStub("RemoteResultSetObserver", new WeakReference(this));
                    this.remoteTransport.registerRemoteObserver(this.stub);
                }
            } catch (RemoteException unused) {
                HiLog.info(LABEL, "Remote process exception when block is closed.", new Object[0]);
            }
        }
    }

    @Override // ohos.data.resultset.AbsResultSet, ohos.data.resultset.ResultSet
    public void unregisterObserver(DataObserver dataObserver) {
        synchronized (this.LOCK) {
            throwIfResultSetIsClosed();
            super.unregisterObserver(dataObserver);
            this.dataObservable.remove(dataObserver);
            if (this.dataObservable.isEmpty()) {
                unregisterRemoteObserver();
            }
        }
    }

    private void unregisterRemoteObserver() {
        try {
            if (this.stub != null) {
                this.remoteTransport.unregisterRemoteObserver();
                this.stub = null;
            }
        } catch (RemoteException unused) {
            HiLog.info(LABEL, "Remote process exception when block is closed.", new Object[0]);
        }
    }

    private static class RemoteDataObservable {
        private Vector<DataObserver> observers;

        private RemoteDataObservable() {
            this.observers = new Vector<>();
        }

        public void add(DataObserver dataObserver) {
            if (dataObserver == null) {
                throw new IllegalArgumentException("input observer cannot be null!");
            } else if (!this.observers.contains(dataObserver)) {
                this.observers.add(dataObserver);
            }
        }

        public void remove(DataObserver dataObserver) {
            if (dataObserver != null) {
                this.observers.remove(dataObserver);
                return;
            }
            throw new IllegalArgumentException("input observer cannot be null!");
        }

        public boolean isEmpty() {
            return this.observers.isEmpty();
        }

        public void removeAll() {
            this.observers.clear();
        }
    }
}
