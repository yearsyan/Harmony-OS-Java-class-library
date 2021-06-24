package ohos.ivicommon.drivingsafety.adapter;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.ivicommon.drivingsafety.model.ConfigProxyResult;
import ohos.ivicommon.drivingsafety.model.DrivingSafetyConst;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class DrivingSafetyConfigProxy implements IDrivingSafetyConfig {
    private static final String CONFIG_SERVICE = "IVIConfigManagerSrv";
    private static final int CONFIG_SERVICE_ID = 4502;
    private static final String DRIVING_SAFETY_DRVMOD_CONFIG = "driving_safety_drvmod_config.xml";
    private static final String DRIVING_SAFETY_SETTING_CONFIG = "driving_safety_setting_config.xml";
    private static final int GET_CONFIG = 0;
    private static final String MAX_STRING_LENGTH_KEY = "driving_mode_maxStringLength";
    private static final int SET_CONFIG = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, DrivingSafetyConst.IVI_CONFIG, "DrivingSafetyConfigProxy");
    private static volatile DrivingSafetyConfigProxy instance;
    private IRemoteObject configService = null;
    private final ConfigServiceDeathRecipient deathRecipient = new ConfigServiceDeathRecipient();
    private String interfaceConfigDescriptor = "OHOS.IVI.Config";
    private final Object lock = new Object();
    private MessageOption option = new MessageOption();

    private DrivingSafetyConfigProxy() {
        synchronized (this.lock) {
            this.configService = SysAbilityManager.getSysAbility(CONFIG_SERVICE_ID);
            if (this.configService == null) {
                HiLog.error(TAG, "getSysAbility: %s failed", CONFIG_SERVICE);
            } else {
                this.configService.addDeathRecipient(this.deathRecipient, 0);
                if (this.configService.getInterfaceDescriptor() != null) {
                    HiLog.info(TAG, "use configservice interface descriptor.", new Object[0]);
                    this.interfaceConfigDescriptor = this.configService.getInterfaceDescriptor();
                }
                HiLog.debug(TAG, "addDeathRecipient for ConfigServiceDeathRecipient", new Object[0]);
            }
        }
    }

    public static DrivingSafetyConfigProxy getInstance() {
        if (instance == null || instance.asObject() == null) {
            synchronized (DrivingSafetyConfigProxy.class) {
                if (instance == null || instance.asObject() == null) {
                    instance = new DrivingSafetyConfigProxy();
                }
            }
        }
        return instance;
    }

    private class ConfigServiceDeathRecipient implements IRemoteObject.DeathRecipient {
        private ConfigServiceDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(DrivingSafetyConfigProxy.TAG, "ConfigServiceDeathRecipient::onRemoteDied.", new Object[0]);
            DrivingSafetyConfigProxy.this.setRemoteObject(null);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.lock) {
            this.configService = iRemoteObject;
        }
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        IRemoteObject iRemoteObject;
        synchronized (this.lock) {
            iRemoteObject = this.configService;
        }
        return iRemoteObject;
    }

    @Override // ohos.ivicommon.drivingsafety.adapter.IDrivingSafetyConfig
    public int setDrivingSafetyConfigure(String str, Boolean bool) throws RemoteException {
        int readInt;
        if (str == null || bool == null) {
            HiLog.error(TAG, "There is null in the input parameter.", new Object[0]);
            throw new IllegalArgumentException("The key or isOpen is null.");
        }
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            if (!obtain.writeInterfaceToken(this.interfaceConfigDescriptor) || !obtain.writeString(DRIVING_SAFETY_SETTING_CONFIG) || !obtain.writeString(str) || !obtain.writeString(bool.toString())) {
                HiLog.error(TAG, "Parcel request error.", new Object[0]);
                throw new IllegalArgumentException("Parcel request error.");
            }
            synchronized (this.lock) {
                if (this.configService == null) {
                    HiLog.error(TAG, "the configService is null.", new Object[0]);
                    throw new RemoteException();
                } else if (this.configService.sendRequest(1, obtain, obtain2, this.option)) {
                    readInt = obtain2.readInt();
                } else {
                    HiLog.error(TAG, "set DrivingSafetyConfigure failed", new Object[0]);
                    throw new RemoteException();
                }
            }
            return readInt;
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    public int getMaxRestrictedStringLength() throws RemoteException {
        try {
            StringBuilder sb = new StringBuilder();
            int drivingSafetyConfigure = getDrivingSafetyConfigure(DRIVING_SAFETY_DRVMOD_CONFIG, MAX_STRING_LENGTH_KEY, sb);
            if (drivingSafetyConfigure == 0) {
                return Integer.parseInt(sb.toString());
            }
            HiLog.error(TAG, "getDrivingSafetyConfigure failed: ret = %{public}d", Integer.valueOf(drivingSafetyConfigure));
            return -1;
        } catch (RemoteException e) {
            HiLog.error(TAG, "getMaxRestrictedStringLength failed: %{public}s", e.getLocalizedMessage());
            throw e;
        }
    }

    public int getDrivingSafetyConfigure(String str, ConfigProxyResult configProxyResult) throws RemoteException {
        if (str == null || configProxyResult == null) {
            HiLog.error(TAG, "There is null in the input parameter.", new Object[0]);
            throw new IllegalArgumentException("The key or result is null.");
        }
        StringBuilder sb = new StringBuilder();
        int drivingSafetyConfigure = getDrivingSafetyConfigure(DRIVING_SAFETY_SETTING_CONFIG, str, sb);
        configProxyResult.setControlled(Boolean.parseBoolean(sb.toString()));
        return drivingSafetyConfigure;
    }

    @Override // ohos.ivicommon.drivingsafety.adapter.IDrivingSafetyConfig
    public int getDrivingSafetyConfigure(String str, String str2, StringBuilder sb) throws RemoteException {
        if (str == null || str2 == null || sb == null) {
            HiLog.error(TAG, "There is null in the input parameter.", new Object[0]);
            throw new IllegalArgumentException("The fileName, key or retBuffer is null.");
        }
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            if (!obtain.writeInterfaceToken(this.interfaceConfigDescriptor) || !obtain.writeString(str) || !obtain.writeString(str2) || !obtain.writeInt(0)) {
                HiLog.error(TAG, "Parcel request error.", new Object[0]);
                throw new IllegalArgumentException("Parcel request error.");
            }
            synchronized (this.lock) {
                if (this.configService == null) {
                    HiLog.error(TAG, "the configService is null.", new Object[0]);
                    throw new RemoteException();
                } else if (!this.configService.sendRequest(0, obtain, obtain2, this.option)) {
                    HiLog.error(TAG, "get DrivingSafetyConfigure failed", new Object[0]);
                    throw new RemoteException();
                }
            }
            sb.append(obtain2.readString());
            return obtain2.readInt();
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }
}
