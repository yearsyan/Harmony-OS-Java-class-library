package ohos.ai.engine.pluginservice;

import java.util.ArrayList;
import java.util.List;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.PacMap;

public class PluginServiceProxy implements IPluginService {
    private final IRemoteObject remote;

    public PluginServiceProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public int checkPluginInstalled(List<PluginRequest> list) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            if (list != null) {
                if (list.size() <= 128) {
                    obtain.writeInt(list.size());
                    for (PluginRequest pluginRequest : list) {
                        if (pluginRequest != null) {
                            obtain.writeInt(1);
                            pluginRequest.marshalling(obtain);
                        } else {
                            obtain.writeInt(0);
                        }
                    }
                    this.remote.sendRequest(1, obtain, obtain2, messageOption);
                    obtain2.readInt();
                    return obtain2.readInt();
                }
            }
            obtain.writeInt(-1);
            this.remote.sendRequest(1, obtain, obtain2, messageOption);
            obtain2.readInt();
            return obtain2.readInt();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004b A[Catch:{ all -> 0x0064 }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0050 A[Catch:{ all -> 0x0064 }] */
    @Override // ohos.ai.engine.pluginservice.IPluginService
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startInstallPlugin(java.util.List<ohos.ai.engine.pluginservice.PluginRequest> r8, java.lang.String r9, ohos.ai.engine.pluginservice.ILoadPluginCallback r10) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 108
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.ai.engine.pluginservice.PluginServiceProxy.startInstallPlugin(java.util.List, java.lang.String, ohos.ai.engine.pluginservice.ILoadPluginCallback):void");
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public IRemoteObject getSplitRemoteObject(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            obtain.writeInt(i);
            this.remote.sendRequest(3, obtain, obtain2, messageOption);
            obtain2.readInt();
            return obtain2.readRemoteObject();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public IRemoteObject getHostRemoteObject() throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            this.remote.sendRequest(4, obtain, obtain2, messageOption);
            obtain2.readInt();
            return obtain2.readRemoteObject();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public String getPluginName(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            obtain.writeInt(i);
            this.remote.sendRequest(5, obtain, obtain2, messageOption);
            obtain2.readInt();
            return obtain2.readString();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public List<String> getPluginNames(int[] iArr) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        ArrayList arrayList = new ArrayList();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            obtain.writeIntArray(iArr);
            this.remote.sendRequest(6, obtain, obtain2, messageOption);
            obtain2.readInt();
            int readInt = obtain2.readInt();
            if (readInt >= 0) {
                if (readInt <= 500) {
                    for (int i = 0; i < readInt; i++) {
                        arrayList.add(obtain2.readString());
                    }
                    obtain2.reclaim();
                    obtain.reclaim();
                    return arrayList;
                }
            }
            return arrayList;
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public String getSplitRemoteObjectName(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            obtain.writeInt(i);
            this.remote.sendRequest(7, obtain, obtain2, messageOption);
            obtain2.readInt();
            return obtain2.readString();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public List<String> getSplitRemoteObjectNames(int[] iArr) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        ArrayList arrayList = new ArrayList();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            obtain.writeIntArray(iArr);
            this.remote.sendRequest(8, obtain, obtain2, messageOption);
            obtain2.readInt();
            int readInt = obtain2.readInt();
            if (readInt >= 0) {
                if (readInt <= 500) {
                    for (int i = 0; i < readInt; i++) {
                        arrayList.add(obtain2.readString());
                    }
                    obtain2.reclaim();
                    obtain.reclaim();
                    return arrayList;
                }
            }
            return arrayList;
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public boolean isOpen(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            obtain.writeInt(i);
            this.remote.sendRequest(9, obtain, obtain2, messageOption);
            obtain2.readInt();
            return obtain2.readInt() != 0;
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ai.engine.pluginservice.IPluginService
    public PacMap process(PacMap pacMap) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        PacMap pacMap2 = new PacMap();
        try {
            obtain.writeInterfaceToken(IPluginService.DESCRIPTOR);
            if (pacMap != null) {
                obtain.writeInt(1);
                pacMap.marshalling(obtain);
            } else {
                obtain.writeInt(0);
            }
            this.remote.sendRequest(10, obtain, obtain2, messageOption);
            obtain2.readInt();
            if (obtain2.readInt() != 0) {
                pacMap2.unmarshalling(obtain2);
            } else {
                pacMap2 = null;
            }
            return pacMap2;
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }
}
