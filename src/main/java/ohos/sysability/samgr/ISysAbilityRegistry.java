package ohos.sysability.samgr;

import java.util.List;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

public interface ISysAbilityRegistry extends IRemoteBroker {
    public static final int ADD_SYSABILITY_TRANSACTION = 3;
    public static final int ADD_SYSTEM_CAPABILITY = 26;
    public static final int CHECK_REMOTE_SYSABILITY_TRANSACTION = 16;
    public static final int CHECK_SYSABILITY_TRANSACTION = 2;
    public static final int DUMP_FLAG_PRIORITY_ALL = 15;
    public static final int DUMP_FLAG_PRIORITY_CRITICAL = 1;
    public static final int DUMP_FLAG_PRIORITY_DEFAULT = 8;
    public static final int DUMP_FLAG_PRIORITY_HIGH = 2;
    public static final int DUMP_FLAG_PRIORITY_NORMAL = 4;
    public static final int DUMP_FLAG_PROTO = 16;
    public static final int GET_AVAILABLE_SYSTEM_CAPABILITY = 28;
    public static final int GET_SYSABILITY_TRANSACTION = 1;
    public static final int HAS_SYSTEM_CAPABILITY = 27;
    public static final int LIST_SYSABILITY_TRANSACTION = 5;
    public static final int REGISTER_SYSTEM_READY_CALLBACK = 24;
    public static final int REMOVE_SYSABILITY_TRANSACTION = 4;

    int addSysAbility(int i, IRemoteObject iRemoteObject) throws RemoteException;

    int addSysAbility(int i, IRemoteObject iRemoteObject, boolean z, int i2) throws RemoteException;

    int addSysAbility(int i, IRemoteObject iRemoteObject, boolean z, int i2, String str) throws RemoteException;

    int addSystemCapability(String str) throws RemoteException;

    IRemoteObject checkSysAbility(int i) throws RemoteException;

    IRemoteObject getSysAbility(int i) throws RemoteException;

    IRemoteObject getSysAbility(int i, String str) throws RemoteException;

    List<String> getSystemAvailableCapabilities() throws RemoteException;

    boolean hasSystemCapability(String str) throws RemoteException;

    String[] listSysAbilities(int i) throws RemoteException;

    boolean reRegisterSysAbility();

    int registerSystemReadyCallback(IRemoteObject iRemoteObject) throws RemoteException;

    int removeSysAbility(int i) throws RemoteException;
}
