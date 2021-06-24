package ohos.bluetooth;

import java.io.FileDescriptor;
import java.util.Optional;
import java.util.function.Consumer;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;

/* access modifiers changed from: package-private */
public class SppSocketProxy implements ISppSocket {
    private static final int COMMAND_SPP_CONNECT_SOCKET = 1;
    private static final int COMMAND_SPP_CREATE_SOCKET_SERVER = 2;
    private static final int ERR_OK = 0;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "SppSocketProxy");
    private IRemoteObject remote = null;

    public SppSocketProxy() {
        BluetoothHostProxy.getInstace().getSaProfileProxy(20).ifPresent(new Consumer() {
            /* class ohos.bluetooth.$$Lambda$SppSocketProxy$e_30vefinko58Z9Z6DDNbkqXOZU */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SppSocketProxy.this.lambda$new$0$SppSocketProxy((IRemoteObject) obj);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$SppSocketProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        this.remote = null;
        BluetoothHostProxy.getInstace().getSaProfileProxy(20).ifPresent(new Consumer() {
            /* class ohos.bluetooth.$$Lambda$SppSocketProxy$zDLOLye87aaOsTkMo9AQ29f5BA */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SppSocketProxy.this.lambda$asObject$1$SppSocketProxy((IRemoteObject) obj);
            }
        });
        return this.remote;
    }

    public /* synthetic */ void lambda$asObject$1$SppSocketProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005b, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r3 = java.util.Optional.ofNullable(null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0062, code lost:
        r0.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0068, code lost:
        throw r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x005d */
    @Override // ohos.bluetooth.ISppSocket
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<java.io.FileDescriptor> sppConnectSocket(ohos.bluetooth.BluetoothRemoteDevice r4, int r5, java.util.UUID r6, int r7, int r8) {
        /*
        // Method dump skipped, instructions count: 105
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.SppSocketProxy.sppConnectSocket(ohos.bluetooth.BluetoothRemoteDevice, int, java.util.UUID, int, int):java.util.Optional");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005b, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r3 = java.util.Optional.ofNullable(null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0062, code lost:
        r0.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0068, code lost:
        throw r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x005d */
    @Override // ohos.bluetooth.ISppSocket
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<java.io.FileDescriptor> sppCreateSocketServer(java.lang.String r4, int r5, java.util.UUID r6, int r7, int r8) {
        /*
        // Method dump skipped, instructions count: 105
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.SppSocketProxy.sppCreateSocketServer(java.lang.String, int, java.util.UUID, int, int):java.util.Optional");
    }

    private Optional<FileDescriptor> createFileDescriptor(MessageParcel messageParcel) {
        int readInt = messageParcel.readInt();
        HiLog.info(TAG, "createFileDescriptor read ec %{public}d.", Integer.valueOf(readInt));
        if (readInt != 0) {
            return Optional.ofNullable(null);
        }
        FileDescriptor readFileDescriptor = messageParcel.readFileDescriptor();
        HiLog.info(TAG, "createFileDescriptor is %{public}d.", readFileDescriptor);
        return Optional.ofNullable(readFileDescriptor);
    }
}
