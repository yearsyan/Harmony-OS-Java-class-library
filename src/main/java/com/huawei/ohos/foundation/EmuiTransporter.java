package com.huawei.ohos.foundation;

import android.os.IBinder;
import java.util.Optional;
import ohos.bundle.ElementName;
import ohos.distributedschedule.adapter.ElementNameAdapter;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;

public final class EmuiTransporter {
    private static final HiLogLabel DMS_LABEL = new HiLogLabel(3, 218109952, "DmsProxy_EmuiTransporter");
    private static final String INTERFACE_TOKEN = "com.huawei.harmonyos.interwork.IAbilityConnection";
    private static final int ON_ABILITY_CONNECT_DONE = 1;
    private static final int ON_ABILITY_DISCONNECT_DONE = 2;

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x005c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        ohos.hiviewdfx.HiLog.error(com.huawei.ohos.foundation.EmuiTransporter.DMS_LABEL, "onServiceConnected binder transact exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006f, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0075, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x005e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void transactConnectToRemoteService(ohos.rpc.IRemoteObject r4, ohos.bundle.ElementName r5, android.os.IBinder r6, int r7) {
        /*
        // Method dump skipped, instructions count: 119
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ohos.foundation.EmuiTransporter.transactConnectToRemoteService(ohos.rpc.IRemoteObject, ohos.bundle.ElementName, android.os.IBinder, int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        ohos.hiviewdfx.HiLog.error(com.huawei.ohos.foundation.EmuiTransporter.DMS_LABEL, "EmuiTransporter: onServiceConnected binder transact exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0067, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0051 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void transactDisconnectToRemoteService(ohos.rpc.IRemoteObject r5, ohos.bundle.ElementName r6, int r7) {
        /*
        // Method dump skipped, instructions count: 105
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ohos.foundation.EmuiTransporter.transactDisconnectToRemoteService(ohos.rpc.IRemoteObject, ohos.bundle.ElementName, int):void");
    }

    static Optional<IRemoteObject> convertToHarymonyRemote(IBinder iBinder) {
        if (iBinder == null) {
            return Optional.empty();
        }
        return IPCAdapter.translateToIRemoteObject(iBinder);
    }

    static boolean writeConnectParcel(MessageParcel messageParcel, ElementName elementName, IRemoteObject iRemoteObject, int i) {
        if (messageParcel == null || elementName == null || iRemoteObject == null || !messageParcel.writeInterfaceToken(INTERFACE_TOKEN) || !new ElementNameAdapter(elementName).marshalling(messageParcel) || !messageParcel.writeRemoteObject(iRemoteObject) || !messageParcel.writeInt(i)) {
            return false;
        }
        return true;
    }

    static boolean writeDisConnectParcel(MessageParcel messageParcel, ElementName elementName, int i) {
        if (messageParcel == null || elementName == null || !messageParcel.writeInterfaceToken(INTERFACE_TOKEN) || !new ElementNameAdapter(elementName).marshalling(messageParcel) || !messageParcel.writeInt(i)) {
            return false;
        }
        return true;
    }
}
