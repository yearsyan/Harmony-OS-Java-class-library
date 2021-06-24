package ohos.workscheduler;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class WorkSchedulerProxy implements IWorkScheduler {
    private static final int COMMAND_ON_COMMON_EVENT = 3;
    private static final int COMMAND_ON_SEND_REMOTE = 4;
    private static final int COMMAND_ON_WORK_START = 1;
    private static final int COMMAND_ON_WORK_STOP = 2;
    private static final int ERR_OK = 0;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218109696, "WorkSchedulerProxy");
    private static final String WORKSCHEDULER_INTERFACE_TOKEN = "ohos.workscheduler.IWorkScheduler";
    private final IRemoteObject remote;

    public WorkSchedulerProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.workscheduler.WorkSchedulerProxy.LOG_LABEL, "onWorkStart Exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0049, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0031 */
    @Override // ohos.workscheduler.IWorkScheduler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onWorkStart(ohos.workscheduler.WorkInfo r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.workscheduler.IWorkScheduler"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x001d
            r0.reclaim()
            r1.reclaim()
            return
        L_0x001d:
            r0.writeSequenceable(r6)     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            r6 = 1
            boolean r5 = r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            if (r5 == 0) goto L_0x003a
            int r5 = r1.readInt()     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            r3 = r5
            goto L_0x003a
        L_0x002f:
            r5 = move-exception
            goto L_0x0049
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.workscheduler.WorkSchedulerProxy.LOG_LABEL     // Catch:{ all -> 0x002f }
            java.lang.String r6 = "onWorkStart Exception"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r5, r6, r2)     // Catch:{ all -> 0x002f }
        L_0x003a:
            r0.reclaim()
            r1.reclaim()
            if (r3 != 0) goto L_0x0043
            return
        L_0x0043:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0049:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workscheduler.WorkSchedulerProxy.onWorkStart(ohos.workscheduler.WorkInfo):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.workscheduler.WorkSchedulerProxy.LOG_LABEL, "onWorkStop Exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0049, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0031 */
    @Override // ohos.workscheduler.IWorkScheduler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onWorkStop(ohos.workscheduler.WorkInfo r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.workscheduler.IWorkScheduler"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x001d
            r0.reclaim()
            r1.reclaim()
            return
        L_0x001d:
            r0.writeSequenceable(r6)     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            r6 = 2
            boolean r5 = r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            if (r5 == 0) goto L_0x003a
            int r5 = r1.readInt()     // Catch:{ RemoteException | ParcelException -> 0x0031 }
            r3 = r5
            goto L_0x003a
        L_0x002f:
            r5 = move-exception
            goto L_0x0049
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.workscheduler.WorkSchedulerProxy.LOG_LABEL     // Catch:{ all -> 0x002f }
            java.lang.String r6 = "onWorkStop Exception"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r5, r6, r2)     // Catch:{ all -> 0x002f }
        L_0x003a:
            r0.reclaim()
            r1.reclaim()
            if (r3 != 0) goto L_0x0043
            return
        L_0x0043:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0049:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workscheduler.WorkSchedulerProxy.onWorkStop(ohos.workscheduler.WorkInfo):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0047, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.workscheduler.WorkSchedulerProxy.LOG_LABEL, "onCommonEventTriggered Exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0061, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0067, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0049 */
    @Override // ohos.workscheduler.IWorkScheduler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCommonEventTriggered(ohos.aafwk.content.Intent r6) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 104
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workscheduler.WorkSchedulerProxy.onCommonEventTriggered(ohos.aafwk.content.Intent):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0031, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.workscheduler.WorkSchedulerProxy.LOG_LABEL, "RemoteException", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004c, code lost:
        r4.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0033 */
    @Override // ohos.workscheduler.IWorkScheduler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendRemote(ohos.rpc.IRemoteObject r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r4 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r2 = 0
            r1.<init>(r2)
            java.lang.String r3 = "ohos.workscheduler.IWorkScheduler"
            boolean r3 = r4.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0033 }
            if (r3 != 0) goto L_0x001d
        L_0x0016:
            r4.reclaim()
            r0.reclaim()
            return r2
        L_0x001d:
            boolean r3 = r4.writeRemoteObject(r5)
            if (r3 != 0) goto L_0x0024
            goto L_0x0016
        L_0x0024:
            r3 = 4
            boolean r5 = r5.sendRequest(r3, r4, r0, r1)
            if (r5 == 0) goto L_0x003c
            int r5 = r0.readInt()
            r2 = r5
            goto L_0x003c
        L_0x0031:
            r5 = move-exception
            goto L_0x004c
        L_0x0033:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.workscheduler.WorkSchedulerProxy.LOG_LABEL     // Catch:{ all -> 0x0031 }
            java.lang.String r1 = "RemoteException"
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ all -> 0x0031 }
            ohos.hiviewdfx.HiLog.error(r5, r1, r3)     // Catch:{ all -> 0x0031 }
        L_0x003c:
            r4.reclaim()
            r0.reclaim()
            if (r2 != 0) goto L_0x0046
            r4 = 1
            return r4
        L_0x0046:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x004c:
            r4.reclaim()
            r0.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workscheduler.WorkSchedulerProxy.sendRemote(ohos.rpc.IRemoteObject):boolean");
    }
}
