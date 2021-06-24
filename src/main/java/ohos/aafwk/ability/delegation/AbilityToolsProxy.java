package ohos.aafwk.ability.delegation;

import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class AbilityToolsProxy {
    private static final String ABILITY_TOOLS_TOKEN = "ohos.abilitytools.accessToken";
    private static final LogLabel LABEL = LogLabel.create();
    public static final int MSG_LEN_LIMIT = 1000;
    public static final int OUTPUT_MSG = 0;
    private AbilityToolsDeathRecipient abilityToolsDeathRecipient;
    private final String deviceId;
    private IRemoteObject remote;
    private final Object remoteLock = new Object();
    private final int serviceId;

    public AbilityToolsProxy(int i, String str) {
        this.serviceId = i;
        this.deviceId = str;
    }

    /* access modifiers changed from: package-private */
    public void output(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Send message should not be null");
        } else if (str.length() <= 1000) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            if (obtain.writeInterfaceToken(ABILITY_TOOLS_TOKEN)) {
                obtain.writeString(str);
                synchronized (this.remoteLock) {
                    if (this.remote != null) {
                        try {
                            this.remote.sendRequest(0, obtain, obtain2, messageOption);
                        } catch (RemoteException unused) {
                            Log.error(LABEL, "Remote object communicate exception", new Object[0]);
                        }
                    } else {
                        obtain2.reclaim();
                        obtain.reclaim();
                        throw new IllegalStateException("You should get remote object first");
                    }
                }
                obtain2.reclaim();
                obtain.reclaim();
            }
        } else {
            throw new IllegalArgumentException("Message length limit is 1000");
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void start() {
        /*
            r4 = this;
            java.lang.Object r0 = r4.remoteLock
            monitor-enter(r0)
            ohos.rpc.IRemoteObject r1 = r4.remote     // Catch:{ all -> 0x005c }
            if (r1 == 0) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            return
        L_0x0009:
            java.lang.String r1 = r4.deviceId     // Catch:{ all -> 0x005c }
            if (r1 == 0) goto L_0x0021
            java.lang.String r1 = r4.deviceId     // Catch:{ all -> 0x005c }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x005c }
            if (r1 == 0) goto L_0x0016
            goto L_0x0021
        L_0x0016:
            int r1 = r4.serviceId     // Catch:{ all -> 0x005c }
            java.lang.String r2 = r4.deviceId     // Catch:{ all -> 0x005c }
            ohos.rpc.IRemoteObject r1 = ohos.sysability.samgr.SysAbilityManager.getSysAbility(r1, r2)     // Catch:{ all -> 0x005c }
            r4.remote = r1     // Catch:{ all -> 0x005c }
            goto L_0x0029
        L_0x0021:
            int r1 = r4.serviceId     // Catch:{ all -> 0x005c }
            ohos.rpc.IRemoteObject r1 = ohos.sysability.samgr.SysAbilityManager.getSysAbility(r1)     // Catch:{ all -> 0x005c }
            r4.remote = r1     // Catch:{ all -> 0x005c }
        L_0x0029:
            ohos.rpc.IRemoteObject r1 = r4.remote     // Catch:{ all -> 0x005c }
            if (r1 == 0) goto L_0x0043
            ohos.aafwk.ability.delegation.AbilityToolsProxy$AbilityToolsDeathRecipient r1 = r4.abilityToolsDeathRecipient     // Catch:{ all -> 0x005c }
            if (r1 != 0) goto L_0x0041
            ohos.aafwk.ability.delegation.AbilityToolsProxy$AbilityToolsDeathRecipient r1 = new ohos.aafwk.ability.delegation.AbilityToolsProxy$AbilityToolsDeathRecipient     // Catch:{ all -> 0x005c }
            r2 = 0
            r1.<init>()     // Catch:{ all -> 0x005c }
            r4.abilityToolsDeathRecipient = r1     // Catch:{ all -> 0x005c }
            ohos.rpc.IRemoteObject r1 = r4.remote     // Catch:{ all -> 0x005c }
            ohos.aafwk.ability.delegation.AbilityToolsProxy$AbilityToolsDeathRecipient r4 = r4.abilityToolsDeathRecipient     // Catch:{ all -> 0x005c }
            r2 = 0
            r1.addDeathRecipient(r4, r2)     // Catch:{ all -> 0x005c }
        L_0x0041:
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            return
        L_0x0043:
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException     // Catch:{ all -> 0x005c }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x005c }
            r2.<init>()     // Catch:{ all -> 0x005c }
            java.lang.String r3 = "Get remote service error, serviceId is:"
            r2.append(r3)     // Catch:{ all -> 0x005c }
            int r4 = r4.serviceId     // Catch:{ all -> 0x005c }
            r2.append(r4)     // Catch:{ all -> 0x005c }
            java.lang.String r4 = r2.toString()     // Catch:{ all -> 0x005c }
            r1.<init>(r4)     // Catch:{ all -> 0x005c }
            throw r1     // Catch:{ all -> 0x005c }
        L_0x005c:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x005c }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.delegation.AbilityToolsProxy.start():void");
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        synchronized (this.remoteLock) {
            if (!(this.abilityToolsDeathRecipient == null || this.remote == null)) {
                this.remote.removeDeathRecipient(this.abilityToolsDeathRecipient, 0);
            }
            this.remote = null;
            this.abilityToolsDeathRecipient = null;
        }
    }

    /* access modifiers changed from: private */
    public class AbilityToolsDeathRecipient implements IRemoteObject.DeathRecipient {
        private AbilityToolsDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            synchronized (AbilityToolsProxy.this.remoteLock) {
                Log.info(AbilityToolsProxy.LABEL, "Remote died", new Object[0]);
                AbilityToolsProxy.this.remote = null;
                AbilityToolsProxy.this.abilityToolsDeathRecipient = null;
            }
        }
    }
}
