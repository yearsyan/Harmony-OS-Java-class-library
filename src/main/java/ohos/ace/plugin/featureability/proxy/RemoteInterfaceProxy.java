package ohos.ace.plugin.featureability.proxy;

import com.huawei.ace.runtime.ALog;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.ace.ability.AceAbility;
import ohos.ace.plugin.featureability.dto.InterfaceElement;
import ohos.bundle.ElementName;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class RemoteInterfaceProxy extends InterfaceProxy<IRemoteObject> implements IAbilityConnection {
    private static final String TAG = RemoteInterfaceProxy.class.getSimpleName();
    private CountDownLatch latch;
    private int status = 0;

    public RemoteInterfaceProxy(InterfaceProxyManager interfaceProxyManager, AceAbility aceAbility, InterfaceElement interfaceElement) {
        super(interfaceProxyManager, aceAbility, interfaceElement);
    }

    @Override // ohos.aafwk.ability.IAbilityConnection
    public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
        String str = TAG;
        ALog.i(str, "connect remote interface '" + this.interfaceElement.toString() + "' success");
        this.interfaceInstance = iRemoteObject;
        CountDownLatch countDownLatch = this.latch;
        if (countDownLatch != null && countDownLatch.getCount() > 0) {
            this.latch.countDown();
        }
    }

    @Override // ohos.aafwk.ability.IAbilityConnection
    public void onAbilityDisconnectDone(ElementName elementName, int i) {
        String str = TAG;
        ALog.i(str, "remote interface disconnected, interfaceElement: " + this.interfaceElement.toString());
        this.status = -1;
        ((InterfaceProxyManager) this.proxyManager.get()).removeInterfaceProxy(this.interfaceElement);
    }

    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public boolean checkConnect() {
        if (this.status == 0) {
            if (this.aceAbility == null) {
                String str = TAG;
                ALog.e(str, "ability context of '" + this.interfaceElement.toString() + "' is null");
                return false;
            }
            synchronized (this.lock) {
                if (this.status == 0) {
                    if (!connectAbility() || this.interfaceInstance == null) {
                        this.status = -1;
                    } else {
                        this.status = 1;
                    }
                }
            }
        }
        return this.status == 1;
    }

    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public void releaseConnect() {
        if (this.status != -1) {
            String str = TAG;
            ALog.i(str, "release connection, interfaceElement: " + this.interfaceElement.toString());
            this.aceAbility.disconnectAbility(this);
        }
    }

    private boolean connectAbility() {
        String str = TAG;
        ALog.i(str, "connect remote interface, interfaceElement: " + this.interfaceElement.toString());
        this.latch = new CountDownLatch(1);
        Intent intent = new Intent();
        intent.setElement(new ElementName(this.interfaceElement.getDeviceId(), this.interfaceElement.getBundleName(), this.interfaceElement.getName()));
        if (!this.aceAbility.connectAbility(intent, this)) {
            String str2 = TAG;
            ALog.e(str2, "connect remote interface '" + this.interfaceElement.toString() + "' failed");
            return false;
        }
        try {
            this.latch.await(5, TimeUnit.SECONDS);
            if (this.interfaceInstance != null) {
                return true;
            }
            String str3 = TAG;
            ALog.e(str3, "interfaceInstance is null after connect remote interface '" + this.interfaceElement.toString() + "' failed");
            return false;
        } catch (InterruptedException unused) {
            String str4 = TAG;
            ALog.e(str4, "wait connect remote interface '" + this.interfaceElement.toString() + "' result Interrupted");
            return false;
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.ace.plugin.featureability.proxy.InterfaceProxy
    public boolean sendRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        return ((IRemoteObject) this.interfaceInstance).sendRequest(i, messageParcel, messageParcel2, messageOption);
    }
}
