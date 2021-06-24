package ohos.ivicommon.drivingsafety.adapter;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

public interface IDrivingSafetyConfig extends IRemoteBroker {
    int getDrivingSafetyConfigure(String str, String str2, StringBuilder sb) throws RemoteException;

    int setDrivingSafetyConfigure(String str, Boolean bool) throws RemoteException;
}
