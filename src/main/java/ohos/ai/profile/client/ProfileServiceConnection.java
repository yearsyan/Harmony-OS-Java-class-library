package ohos.ai.profile.client;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.ai.profile.client.ProfileServiceConnection;
import ohos.ai.profile.harmonyadapter.HarmonyProfileServiceCallProxy;
import ohos.ai.profile.harmonyadapter.IHarmonyProfileServiceCall;
import ohos.ai.profile.util.LogUtil;
import ohos.ai.profile.util.SensitiveUtil;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.rpc.IRemoteObject;

@SystemApi
public class ProfileServiceConnection {
    private static final int DEATH_RECIPIENT_FLAG = 0;
    private static final String SERVICE_CLASS_NAME = "com.huawei.profile.harmonyadapter.HarmonyProfileService";
    private static final String SERVICE_PACKAGE_NAME = "com.huawei.profile";
    private static final String TAG = "ProfileServiceConnection";
    private IAbilityConnection connection;
    private Context context;
    private volatile IHarmonyProfileServiceCall profileService;

    ProfileServiceConnection(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public boolean connect(CompletableFuture<Boolean> completableFuture) {
        Intent intent = new Intent();
        intent.setElement(new ElementName("", SERVICE_PACKAGE_NAME, SERVICE_CLASS_NAME));
        intent.setFlags(16);
        this.connection = new AbilityConnection(completableFuture);
        try {
            if (this.context.connectAbility(intent, this.connection)) {
                return true;
            }
            LogUtil.warn(TAG, "Bind profile service failed.", new Object[0]);
            this.connection = null;
            return false;
        } catch (Throwable th) {
            LogUtil.error(TAG, "Bind profile service error, caused by " + SensitiveUtil.getMessage(th), new Object[0]);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean disconnect() {
        IAbilityConnection iAbilityConnection = this.connection;
        if (iAbilityConnection == null) {
            LogUtil.warn(TAG, "Unbind profile service error for connection is null.", new Object[0]);
            return true;
        }
        try {
            this.context.disconnectAbility(iAbilityConnection);
            this.connection = null;
            this.profileService = null;
            return true;
        } catch (RuntimeException e) {
            LogUtil.error(TAG, "Unbind profile service error, caused by " + SensitiveUtil.getMessage(e), new Object[0]);
            this.connection = null;
            this.profileService = null;
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasConnected() {
        if (this.profileService == null) {
            return false;
        }
        if (!this.profileService.asObject().isObjectDead()) {
            return true;
        }
        disconnect();
        return false;
    }

    /* access modifiers changed from: package-private */
    public Optional<IHarmonyProfileServiceCall> getProfileService() {
        return Optional.ofNullable(this.profileService);
    }

    /* access modifiers changed from: private */
    public class AbilityConnection implements IAbilityConnection {
        private CompletableFuture future;

        AbilityConnection(CompletableFuture<Boolean> completableFuture) {
            this.future = completableFuture;
        }

        @Override // ohos.aafwk.ability.IAbilityConnection
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
            LogUtil.info(ProfileServiceConnection.TAG, "Profile service has connected.", new Object[0]);
            Optional<IHarmonyProfileServiceCall> asInterface = HarmonyProfileServiceCallProxy.asInterface(iRemoteObject);
            if (asInterface.isPresent()) {
                ProfileServiceConnection.this.profileService = asInterface.get();
                ProfileServiceConnection.this.profileService.asObject().addDeathRecipient(new IRemoteObject.DeathRecipient() {
                    /* class ohos.ai.profile.client.$$Lambda$ProfileServiceConnection$AbilityConnection$7_IdtfHBTmmzy3gsXYZSkEHwGIU */

                    @Override // ohos.rpc.IRemoteObject.DeathRecipient
                    public final void onRemoteDied() {
                        ProfileServiceConnection.AbilityConnection.this.lambda$onAbilityConnectDone$0$ProfileServiceConnection$AbilityConnection();
                    }
                }, 0);
                this.future.complete(true);
                return;
            }
            LogUtil.error(ProfileServiceConnection.TAG, "Profile service connect failed for remote object is null.", new Object[0]);
            ProfileServiceConnection.this.profileService = null;
            this.future.complete(false);
        }

        public /* synthetic */ void lambda$onAbilityConnectDone$0$ProfileServiceConnection$AbilityConnection() {
            LogUtil.error(ProfileServiceConnection.TAG, "Profile service has died.", new Object[0]);
            ProfileServiceConnection.this.connection = null;
            ProfileServiceConnection.this.profileService = null;
        }

        @Override // ohos.aafwk.ability.IAbilityConnection
        public void onAbilityDisconnectDone(ElementName elementName, int i) {
            LogUtil.info(ProfileServiceConnection.TAG, "Profile service has disconnected.", new Object[0]);
            ProfileServiceConnection.this.profileService = null;
            this.future.complete(false);
        }
    }
}
