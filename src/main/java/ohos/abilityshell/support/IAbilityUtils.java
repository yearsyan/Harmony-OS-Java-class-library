package ohos.abilityshell.support;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import ohos.rpc.IRemoteObject;

public interface IAbilityUtils {
    int connectAbility(Context context, Intent intent, ServiceConnection serviceConnection);

    void disconnectAbility(Context context, ServiceConnection serviceConnection);

    boolean freeInstallWithCallback(Context context, ohos.aafwk.content.Intent intent, IRemoteObject iRemoteObject);

    void startAbility(Context context, Intent intent);

    void startAbilityForResult(Context context, Intent intent, int i);

    void startForegroundAbility(Context context, Intent intent);

    boolean stopAbility(Context context, Intent intent);
}
