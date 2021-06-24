package ohos.aafwk.ability;

import java.util.HashMap;
import ohos.aafwk.ability.FormAdapter;
import ohos.aafwk.ability.ISupplyHost;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class SupplyHostClient extends ISupplyHost.SupplyHostStub {
    private static final Object CLIENT_LOCK = new Object();
    static final int CREATE_FORM = 1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108160, "SupplyHostClient");
    static final int RECREATE_FORM = 2;
    private static volatile SupplyHostClient client;
    private static HashMap<Long, FormAdapter.BaseConnection> connections = new HashMap<>();

    static SupplyHostClient getInstance() {
        if (client == null) {
            synchronized (CLIENT_LOCK) {
                if (client == null) {
                    client = new SupplyHostClient();
                }
            }
        }
        return client;
    }

    SupplyHostClient() {
    }

    @Override // ohos.aafwk.ability.ISupplyHost
    public void onAcquire(ProviderFormInfo providerFormInfo, Intent intent) {
        long longParam = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, 0);
        long longParam2 = intent.getLongParam(ISupplyHost.FORM_CONNECT_ID, 0);
        int intParam = intent.getIntParam(ISupplyHost.ACQUIRE_TYPE, 0);
        String stringParam = intent.getStringParam(ISupplyHost.FORM_SUPPLY_INFO);
        HiLog.debug(LABEL, "onAcquire come: %{public}d, %{public}d, %{public}d, %{public}s", Long.valueOf(longParam), Long.valueOf(longParam2), Integer.valueOf(intParam), stringParam);
        delConnectAbility(longParam2, stringParam);
        if (intParam == 1) {
            FormAdapter.getInstance().handleAcquireBack(longParam, providerFormInfo);
        } else if (intParam != 2) {
            HiLog.warn(LABEL, "onAcquired type: %{public}d", Integer.valueOf(intParam));
        } else {
            FormAdapter.getInstance().handleAcquireAndUpdateForm(longParam, providerFormInfo);
        }
    }

    @Override // ohos.aafwk.ability.ISupplyHost
    public void onEventHandle(Intent intent) {
        long longParam = intent.getLongParam(ISupplyHost.FORM_CONNECT_ID, 0);
        String stringParam = intent.getStringParam(ISupplyHost.FORM_SUPPLY_INFO);
        HiLog.debug(LABEL, "onEventHandle come: %{public}d, %{public}s", Long.valueOf(longParam), stringParam);
        delConnectAbility(longParam, stringParam);
    }

    /* access modifiers changed from: package-private */
    public void delConnectAbility(long j, String str) {
        if (j == 0) {
            FormAdapter.getInstance().deleteBinder(str);
            return;
        }
        FormAdapter.BaseConnection cleanConnection = cleanConnection(j);
        if (cleanConnection != null) {
            cleanConnection.disconnectSupply(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void addConnection(FormAdapter.BaseConnection baseConnection) {
        synchronized (CLIENT_LOCK) {
            if (baseConnection.connectId != 0) {
                long nanoTime = System.nanoTime();
                while (connections.containsKey(Long.valueOf(nanoTime))) {
                    nanoTime++;
                }
                connections.put(Long.valueOf(nanoTime), baseConnection);
                baseConnection.connectId = nanoTime;
                HiLog.debug(LABEL, "addConnection currentTime is %{public}d", Long.valueOf(nanoTime));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public FormAdapter.BaseConnection cleanConnection(long j) {
        FormAdapter.BaseConnection baseConnection;
        synchronized (CLIENT_LOCK) {
            baseConnection = connections.get(Long.valueOf(j));
            connections.remove(Long.valueOf(j));
        }
        return baseConnection;
    }
}
