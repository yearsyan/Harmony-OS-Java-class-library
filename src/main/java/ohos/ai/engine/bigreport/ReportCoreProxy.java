package ohos.ai.engine.bigreport;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class ReportCoreProxy implements IReportCore {
    private final IRemoteObject mRemote;

    ReportCoreProxy(IRemoteObject iRemoteObject) {
        this.mRemote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.mRemote;
    }

    @Override // ohos.ai.engine.bigreport.IReportCore
    public void onInterfaceReport(String str, String str2, InterfaceInfo interfaceInfo) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IReportCore.DESCRIPTOR);
            obtain.writeString(str);
            obtain.writeString(str2);
            obtain.writeSequenceable(interfaceInfo);
            this.mRemote.sendRequest(1, obtain, obtain2, messageOption);
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.bigreport.IReportCore
    public void onOperationReport(String str, String str2, OperationInfo operationInfo) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IReportCore.DESCRIPTOR);
            obtain.writeString(str);
            obtain.writeString(str2);
            obtain.writeSequenceable(operationInfo);
            this.mRemote.sendRequest(2, obtain, obtain2, messageOption);
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.bigreport.IReportCore
    public void onScheduleReport(String str, String str2, ScheduleInfo scheduleInfo) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IReportCore.DESCRIPTOR);
            obtain.writeString(str);
            obtain.writeString(str2);
            obtain.writeSequenceable(scheduleInfo);
            this.mRemote.sendRequest(3, obtain, obtain2, messageOption);
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.bigreport.IReportCore
    public void onOriginDataReport(String str, OriginInfo originInfo) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IReportCore.DESCRIPTOR);
            obtain.writeString(str);
            obtain.writeSequenceable(originInfo);
            this.mRemote.sendRequest(4, obtain, obtain2, messageOption);
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.bigreport.IReportCore
    public void onMixedBuildInterfaceReport(MixedBuildInterfaceInfo mixedBuildInterfaceInfo) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IReportCore.DESCRIPTOR);
            obtain.writeSequenceable(mixedBuildInterfaceInfo);
            this.mRemote.sendRequest(5, obtain, obtain2, messageOption);
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }
}
