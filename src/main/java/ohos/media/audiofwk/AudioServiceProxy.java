package ohos.media.audiofwk;

import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;
import ohos.rpc.IRemoteObject;

public class AudioServiceProxy implements IAudioServiceProxy {
    private static final int GET_CURRENT_VOLUME = 1;
    private static final int GET_MAX_VOLUME = 3;
    private static final int GET_MIN_VOLUME = 2;
    private static final int GET_VOLUME_INFO_CMD_ID = 22;
    private static final int INVALID_VOLUME_INDEX = -1;
    private static final Logger LOGGER = LoggerFactory.getAudioLogger(AudioServiceProxy.class);
    private static final int MAX_AUDIO_STREAM_TYPE_COUNT = 11;
    private static final int SET_VOLUME_CALLBACK_CMD_ID = 21;
    private static final int SET_VOLUME_CMD_ID = 20;
    private final IRemoteObject remote;

    AudioServiceProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0062, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.media.audiofwk.AudioServiceProxy.LOGGER.error("SetVolumeByStreamType set volume failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0074, code lost:
        r3.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007a, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0064 */
    @Override // ohos.media.audiofwk.IAudioServiceProxy
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int setVolumeByStreamType(int r6, int r7, java.util.ArrayList<java.lang.Integer> r8) {
        /*
        // Method dump skipped, instructions count: 123
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.audiofwk.AudioServiceProxy.setVolumeByStreamType(int, int, java.util.ArrayList):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        ohos.media.audiofwk.AudioServiceProxy.LOGGER.error("getVolumeByStreamType get volume failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005a, code lost:
        r3.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004f */
    @Override // ohos.media.audiofwk.IAudioServiceProxy
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getVolumeByStreamType(int r8) {
        /*
            r7 = this;
            ohos.rpc.IRemoteObject r0 = r7.remote
            r1 = -1
            r2 = 0
            if (r0 != 0) goto L_0x0010
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER
            java.lang.Object[] r8 = new java.lang.Object[r2]
            java.lang.String r0 = "getVolumeByStreamType error, remote object is null"
            r7.error(r0, r8)
            return r1
        L_0x0010:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r3 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r4 = new ohos.rpc.MessageOption
            r4.<init>()
            r0.writeInt(r8)
            r8 = 1
            r0.writeInt(r8)
            ohos.media.utils.log.Logger r8 = ohos.media.audiofwk.AudioServiceProxy.LOGGER
            java.lang.Object[] r5 = new java.lang.Object[r2]
            java.lang.String r6 = "getVolumeByStreamType sendRequest to service"
            r8.debug(r6, r5)
            ohos.rpc.IRemoteObject r7 = r7.remote     // Catch:{ RemoteException -> 0x004f }
            r8 = 22
            boolean r7 = r7.sendRequest(r8, r0, r3, r4)     // Catch:{ RemoteException -> 0x004f }
            if (r7 != 0) goto L_0x0041
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER     // Catch:{ RemoteException -> 0x004f }
            java.lang.String r8 = "getVolumeByStreamType sendRequest failed"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ RemoteException -> 0x004f }
            r7.error(r8, r4)     // Catch:{ RemoteException -> 0x004f }
            goto L_0x0046
        L_0x0041:
            int r7 = r3.readInt()     // Catch:{ RemoteException -> 0x004f }
            r1 = r7
        L_0x0046:
            r3.reclaim()
            r0.reclaim()
            goto L_0x0059
        L_0x004d:
            r7 = move-exception
            goto L_0x005a
        L_0x004f:
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER     // Catch:{ all -> 0x004d }
            java.lang.String r8 = "getVolumeByStreamType get volume failed"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x004d }
            r7.error(r8, r2)     // Catch:{ all -> 0x004d }
            goto L_0x0046
        L_0x0059:
            return r1
        L_0x005a:
            r3.reclaim()
            r0.reclaim()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.audiofwk.AudioServiceProxy.getVolumeByStreamType(int):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        ohos.media.audiofwk.AudioServiceProxy.LOGGER.error("getMinVolumeByStreamType get min volume failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005a, code lost:
        r3.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004f */
    @Override // ohos.media.audiofwk.IAudioServiceProxy
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getMinVolumeByStreamType(int r8) {
        /*
            r7 = this;
            ohos.rpc.IRemoteObject r0 = r7.remote
            r1 = -1
            r2 = 0
            if (r0 != 0) goto L_0x0010
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER
            java.lang.Object[] r8 = new java.lang.Object[r2]
            java.lang.String r0 = "getMinVolumeByStreamType error, remote object is null"
            r7.error(r0, r8)
            return r1
        L_0x0010:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r3 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r4 = new ohos.rpc.MessageOption
            r4.<init>()
            r0.writeInt(r8)
            r8 = 2
            r0.writeInt(r8)
            ohos.media.utils.log.Logger r8 = ohos.media.audiofwk.AudioServiceProxy.LOGGER
            java.lang.Object[] r5 = new java.lang.Object[r2]
            java.lang.String r6 = "getMinVolumeByStreamType sendRequest to service"
            r8.debug(r6, r5)
            ohos.rpc.IRemoteObject r7 = r7.remote     // Catch:{ RemoteException -> 0x004f }
            r8 = 22
            boolean r7 = r7.sendRequest(r8, r0, r3, r4)     // Catch:{ RemoteException -> 0x004f }
            if (r7 != 0) goto L_0x0041
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER     // Catch:{ RemoteException -> 0x004f }
            java.lang.String r8 = "getMinVolumeByStreamType sendRequest failed"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ RemoteException -> 0x004f }
            r7.error(r8, r4)     // Catch:{ RemoteException -> 0x004f }
            goto L_0x0046
        L_0x0041:
            int r7 = r3.readInt()     // Catch:{ RemoteException -> 0x004f }
            r1 = r7
        L_0x0046:
            r3.reclaim()
            r0.reclaim()
            goto L_0x0059
        L_0x004d:
            r7 = move-exception
            goto L_0x005a
        L_0x004f:
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER     // Catch:{ all -> 0x004d }
            java.lang.String r8 = "getMinVolumeByStreamType get min volume failed"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x004d }
            r7.error(r8, r2)     // Catch:{ all -> 0x004d }
            goto L_0x0046
        L_0x0059:
            return r1
        L_0x005a:
            r3.reclaim()
            r0.reclaim()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.audiofwk.AudioServiceProxy.getMinVolumeByStreamType(int):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        ohos.media.audiofwk.AudioServiceProxy.LOGGER.error("getMaxVolumeByStreamType get max volume failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005a, code lost:
        r3.reclaim();
        r0.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004f */
    @Override // ohos.media.audiofwk.IAudioServiceProxy
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getMaxVolumeByStreamType(int r8) {
        /*
            r7 = this;
            ohos.rpc.IRemoteObject r0 = r7.remote
            r1 = -1
            r2 = 0
            if (r0 != 0) goto L_0x0010
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER
            java.lang.Object[] r8 = new java.lang.Object[r2]
            java.lang.String r0 = "getMaxVolumeByStreamType error, remote object is null"
            r7.error(r0, r8)
            return r1
        L_0x0010:
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r3 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r4 = new ohos.rpc.MessageOption
            r4.<init>()
            r0.writeInt(r8)
            r8 = 3
            r0.writeInt(r8)
            ohos.media.utils.log.Logger r8 = ohos.media.audiofwk.AudioServiceProxy.LOGGER
            java.lang.Object[] r5 = new java.lang.Object[r2]
            java.lang.String r6 = "getMaxVolumeByStreamType sendRequest to service"
            r8.debug(r6, r5)
            ohos.rpc.IRemoteObject r7 = r7.remote     // Catch:{ RemoteException -> 0x004f }
            r8 = 22
            boolean r7 = r7.sendRequest(r8, r0, r3, r4)     // Catch:{ RemoteException -> 0x004f }
            if (r7 != 0) goto L_0x0041
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER     // Catch:{ RemoteException -> 0x004f }
            java.lang.String r8 = "getMaxVolumeByStreamType sendRequest failed"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ RemoteException -> 0x004f }
            r7.error(r8, r4)     // Catch:{ RemoteException -> 0x004f }
            goto L_0x0046
        L_0x0041:
            int r7 = r3.readInt()     // Catch:{ RemoteException -> 0x004f }
            r1 = r7
        L_0x0046:
            r3.reclaim()
            r0.reclaim()
            goto L_0x0059
        L_0x004d:
            r7 = move-exception
            goto L_0x005a
        L_0x004f:
            ohos.media.utils.log.Logger r7 = ohos.media.audiofwk.AudioServiceProxy.LOGGER     // Catch:{ all -> 0x004d }
            java.lang.String r8 = "getMaxVolumeByStreamType get max volume failed"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x004d }
            r7.error(r8, r2)     // Catch:{ all -> 0x004d }
            goto L_0x0046
        L_0x0059:
            return r1
        L_0x005a:
            r3.reclaim()
            r0.reclaim()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.audiofwk.AudioServiceProxy.getMaxVolumeByStreamType(int):int");
    }
}
