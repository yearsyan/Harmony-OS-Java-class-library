package ohos.workscheduler;

import java.util.ArrayList;
import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.workscheduler.WorkInfo;

public class WorkSchedulerServiceProxy implements IWorkSchedulerService {
    private static final int COMMAND_GET_ALL_WORK_STATUS = 4;
    private static final int COMMAND_GET_WORK_STATUS = 3;
    private static final int COMMAND_LAST_WORK_TIME_OUT = 6;
    private static final int COMMAND_START_WORK_NOW = 1;
    private static final int COMMAND_STOP_ALL_WORK_STATUS = 5;
    private static final int COMMAND_STOP_WORK = 2;
    private static final int ERR_NULL_EXCEPTION = -2;
    private static final int ERR_OK = 0;
    private static final int ERR_REMOTE_EXCEPTION = -1;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218109696, "WorkSchedulerServiceProxy");
    private static final int MAX_WORK_INFO_LENGTH = 1024;
    private static final String WORKSCHEDULER_INTERFACE_TOKEN = "ohos.workscheduler.IWorkSchedulerService";
    private final IRemoteObject remote;

    public WorkSchedulerServiceProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.workscheduler.WorkSchedulerServiceProxy.LOG_LABEL, "startWorkNow Exception !!", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006e, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0074, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005e */
    @Override // ohos.workscheduler.IWorkSchedulerService
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startWorkNow(ohos.workscheduler.WorkInfo r6, boolean r7) {
        /*
        // Method dump skipped, instructions count: 117
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workscheduler.WorkSchedulerServiceProxy.startWorkNow(ohos.workscheduler.WorkInfo, boolean):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0068, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.workscheduler.WorkSchedulerServiceProxy.LOG_LABEL, "stopWork Exception!!", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007a, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0080, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x006a */
    @Override // ohos.workscheduler.IWorkSchedulerService
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean stopWork(ohos.workscheduler.WorkInfo r6, boolean r7, boolean r8) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workscheduler.WorkSchedulerServiceProxy.stopWork(ohos.workscheduler.WorkInfo, boolean, boolean):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004f, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.workscheduler.WorkSchedulerServiceProxy.LOG_LABEL, "getWorkStatus RemoteException", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0061, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0067, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0051 */
    @Override // ohos.workscheduler.IWorkSchedulerService
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.workscheduler.WorkInfo getWorkStatus(int r7) {
        /*
        // Method dump skipped, instructions count: 104
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.workscheduler.WorkSchedulerServiceProxy.getWorkStatus(int):ohos.workscheduler.WorkInfo");
    }

    @Override // ohos.workscheduler.IWorkSchedulerService
    public List<WorkInfo> obtainAllWorks() throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        ArrayList arrayList = new ArrayList();
        HiLog.error(LOG_LABEL, "getWorkStatus begin!!", new Object[0]);
        try {
            if (obtain.writeInterfaceToken(WORKSCHEDULER_INTERFACE_TOKEN)) {
                if (this.remote.sendRequest(4, obtain, obtain2, messageOption)) {
                    int readInt = obtain2.readInt();
                    if (!(readInt == -2 || readInt == -1)) {
                        int readInt2 = obtain2.readInt();
                        if (readInt2 <= 1024 && readInt2 >= 0) {
                            for (int i = 0; i < readInt2; i++) {
                                WorkInfo build = new WorkInfo.Builder().build();
                                if (obtain2.readSequenceable(build)) {
                                    arrayList.add(build);
                                }
                            }
                        }
                    }
                } else {
                    HiLog.error(LOG_LABEL, "getWorkStatus reply is false!!", new Object[0]);
                }
                obtain.reclaim();
                obtain2.reclaim();
                return arrayList;
            }
            return arrayList;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.workscheduler.IWorkSchedulerService
    public boolean stopAndClearWorks() throws RemoteException {
        int readInt;
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            boolean z = false;
            if (!obtain.writeInterfaceToken(WORKSCHEDULER_INTERFACE_TOKEN)) {
                return false;
            }
            if (!(!this.remote.sendRequest(5, obtain, obtain2, messageOption) || (readInt = obtain2.readInt()) == -2 || readInt == -1)) {
                z = obtain2.readBoolean();
            }
            obtain.reclaim();
            obtain2.reclaim();
            return z;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    @Override // ohos.workscheduler.IWorkSchedulerService
    public boolean isLastWorkTimeOut(int i) throws RemoteException {
        int readInt;
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        boolean z = false;
        HiLog.debug(LOG_LABEL, "isLastWorkTimeOut %{private}d", Integer.valueOf(i));
        try {
            if (!obtain.writeInterfaceToken(WORKSCHEDULER_INTERFACE_TOKEN) || !obtain.writeInt(i)) {
                return false;
            }
            if (!(!this.remote.sendRequest(6, obtain, obtain2, messageOption) || (readInt = obtain2.readInt()) == -2 || readInt == -1)) {
                z = obtain2.readBoolean();
            }
            obtain.reclaim();
            obtain2.reclaim();
            return z;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }
}
