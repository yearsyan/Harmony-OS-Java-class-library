package ohos.ivicommon.drivingsafety.adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ohos.app.Context;
import ohos.bundle.ApplicationInfo;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.ivicommon.drivingsafety.model.AtomicAbilityInfo;
import ohos.ivicommon.drivingsafety.model.ControlItemEnum;
import ohos.ivicommon.drivingsafety.model.DrivingSafetyConst;
import ohos.ivicommon.drivingsafety.model.Position;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.sysability.samgr.SysAbilityManager;

public class DrivingSafetyClientProxy implements IDrivingSafetyClient {
    private static final int CHECK_DRIVING_SAFETY = 1;
    private static final String DRIVING_SAFETY_SERVICE = "DrivingSafetyService";
    private static final int DRIVING_SAFETY_SERVICE_ID = 4501;
    private static final int GET_DRIVING_MODE = 2;
    private static final int GET_DRIVING_RESTRAINT = 3;
    private static final int GET_SECONDARY_SCREEN_RANGE = 4;
    private static final String METHOD_GET_X_POS = "getXpos";
    private static final String METHOD_GET_Y_POS = "getYpos";
    private static final String METHOD_INIT_POSITION = "initPosition";
    private static final HiLogLabel TAG = new HiLogLabel(3, DrivingSafetyConst.IVI_DRIVING, "DrivingSafetyClientProxy");
    private static final String WINDOW_POSITION_PACKAGE = "ohos.ivicommon.drivingsafety.model.WindowPosition";
    private static volatile DrivingSafetyClientProxy instance;
    private final DrivingSafetyDeathRecipient deathRecipient = new DrivingSafetyDeathRecipient();
    private IRemoteObject drivingSafetyService = null;
    private String interfaceConfigDescriptor = "OHOS.IVI.DrivingSafety";
    private final Object lock = new Object();
    private MessageOption option = new MessageOption();
    private final Object posLock = new Object();
    private PositionInvoker positionInvoker = new PositionInvoker();

    private DrivingSafetyClientProxy() {
        synchronized (this.lock) {
            this.drivingSafetyService = SysAbilityManager.getSysAbility(DRIVING_SAFETY_SERVICE_ID);
            if (this.drivingSafetyService == null) {
                HiLog.error(TAG, "getSysAbility: %s failed", DRIVING_SAFETY_SERVICE);
            } else {
                this.drivingSafetyService.addDeathRecipient(this.deathRecipient, 0);
                if (this.drivingSafetyService.getInterfaceDescriptor() != null) {
                    HiLog.info(TAG, "use drivingSafetyService interface descriptor.", new Object[0]);
                    this.interfaceConfigDescriptor = this.drivingSafetyService.getInterfaceDescriptor();
                }
                HiLog.debug(TAG, "addDeathRecipient for DrivingSafetyClientProxy", new Object[0]);
            }
        }
    }

    public static DrivingSafetyClientProxy getInstance() {
        if (instance == null || instance.asObject() == null) {
            synchronized (DrivingSafetyClientProxy.class) {
                if (instance == null || instance.asObject() == null) {
                    instance = new DrivingSafetyClientProxy();
                }
            }
        }
        return instance;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        IRemoteObject iRemoteObject;
        synchronized (this.lock) {
            iRemoteObject = this.drivingSafetyService;
        }
        return iRemoteObject;
    }

    private class DrivingSafetyDeathRecipient implements IRemoteObject.DeathRecipient {
        private DrivingSafetyDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(DrivingSafetyClientProxy.TAG, "DrivingSafetyDeathRecipient::onRemoteDied.", new Object[0]);
            DrivingSafetyClientProxy.this.setRemoteObject(null);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.lock) {
            this.drivingSafetyService = iRemoteObject;
        }
    }

    /* access modifiers changed from: private */
    public class PositionInvoker {
        private boolean isInit;
        private Method methodGetXpos;
        private Method methodGetYpos;
        private Method methodInitPosition;
        private Object winPosObj;

        private PositionInvoker() {
            this.isInit = false;
        }

        public boolean isInitialized() {
            return this.isInit;
        }

        public void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
            Class<?> cls = Class.forName(DrivingSafetyClientProxy.WINDOW_POSITION_PACKAGE);
            this.winPosObj = cls.newInstance();
            this.methodInitPosition = cls.getDeclaredMethod(DrivingSafetyClientProxy.METHOD_INIT_POSITION, new Class[0]);
            this.methodGetXpos = cls.getDeclaredMethod(DrivingSafetyClientProxy.METHOD_GET_X_POS, new Class[0]);
            this.methodGetYpos = cls.getDeclaredMethod(DrivingSafetyClientProxy.METHOD_GET_Y_POS, new Class[0]);
            this.isInit = true;
        }

        public void invokeInit() throws InvocationTargetException, IllegalAccessException {
            this.methodInitPosition.invoke(this.winPosObj, new Object[0]);
        }

        public int invokeGetX() throws InvocationTargetException, IllegalAccessException {
            return ((Integer) this.methodGetXpos.invoke(this.winPosObj, new Object[0])).intValue();
        }

        public int invokeGetY() throws InvocationTargetException, IllegalAccessException {
            return ((Integer) this.methodGetYpos.invoke(this.winPosObj, new Object[0])).intValue();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0031, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy.TAG, "get ability position Exception: %{public}s", ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy.WINDOW_POSITION_PACKAGE);
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[ExcHandler: ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException (unused java.lang.Throwable), SYNTHETIC, Splitter:B:11:0x0030] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.ivicommon.drivingsafety.model.Position getWindow() {
        /*
            r4 = this;
            ohos.ivicommon.drivingsafety.model.Position r0 = new ohos.ivicommon.drivingsafety.model.Position
            r0.<init>()
            java.lang.Object r1 = r4.posLock
            monitor-enter(r1)
            ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy$PositionInvoker r2 = r4.positionInvoker     // Catch:{ all -> 0x002e }
            boolean r2 = r2.isInitialized()     // Catch:{ all -> 0x002e }
            if (r2 != 0) goto L_0x0015
            ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy$PositionInvoker r2 = r4.positionInvoker     // Catch:{ all -> 0x002e }
            r2.init()     // Catch:{ all -> 0x002e }
        L_0x0015:
            ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy$PositionInvoker r2 = r4.positionInvoker     // Catch:{ all -> 0x002e }
            r2.invokeInit()     // Catch:{ all -> 0x002e }
            ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy$PositionInvoker r2 = r4.positionInvoker     // Catch:{ all -> 0x002e }
            int r2 = r2.invokeGetX()     // Catch:{ all -> 0x002e }
            r0.setX(r2)     // Catch:{ all -> 0x002e }
            ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy$PositionInvoker r4 = r4.positionInvoker     // Catch:{ all -> 0x002e }
            int r4 = r4.invokeGetY()     // Catch:{ all -> 0x002e }
            r0.setY(r4)     // Catch:{ all -> 0x002e }
            monitor-exit(r1)     // Catch:{ all -> 0x002e }
            goto L_0x0040
        L_0x002e:
            r4 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002e }
            throw r4     // Catch:{ ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException -> 0x0031, ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException -> 0x0031, ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException -> 0x0031, ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException -> 0x0031 }
        L_0x0031:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy.TAG
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r2 = 0
            java.lang.String r3 = "ohos.ivicommon.drivingsafety.model.WindowPosition"
            r1[r2] = r3
            java.lang.String r2 = "get ability position Exception: %{public}s"
            ohos.hiviewdfx.HiLog.error(r4, r2, r1)
        L_0x0040:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.ivicommon.drivingsafety.adapter.DrivingSafetyClientProxy.getWindow():ohos.ivicommon.drivingsafety.model.Position");
    }

    private AtomicAbilityInfo getAbilityInfo(Context context, ControlItemEnum controlItemEnum, Position position) {
        ApplicationInfo applicationInfo;
        AtomicAbilityInfo atomicAbilityInfo = new AtomicAbilityInfo();
        if (position != null) {
            atomicAbilityInfo.setX(position.getX());
            atomicAbilityInfo.setY(position.getY());
        } else {
            Position window = getWindow();
            atomicAbilityInfo.setX(window.getX());
            atomicAbilityInfo.setY(window.getY());
        }
        if (!(context == null || (applicationInfo = context.getApplicationInfo()) == null)) {
            atomicAbilityInfo.setAbilityName(applicationInfo.getName());
            boolean z = true;
            if ((applicationInfo.getSupportedModes() & 1) != 1) {
                z = false;
            }
            atomicAbilityInfo.enableDriveMode(z);
        }
        if (controlItemEnum != null) {
            atomicAbilityInfo.setControlItem(controlItemEnum.getName());
        }
        return atomicAbilityInfo;
    }

    public boolean isDrivingSafety(Context context, ControlItemEnum controlItemEnum) throws RemoteException {
        return isDrivingSafety(context, controlItemEnum, null);
    }

    @Override // ohos.ivicommon.drivingsafety.adapter.IDrivingSafetyClient
    public boolean isDrivingSafety(Context context, ControlItemEnum controlItemEnum, Position position) throws RemoteException {
        boolean readBoolean;
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            if (!obtain.writeInterfaceToken(this.interfaceConfigDescriptor)) {
                HiLog.error(TAG, "Parcel request error.", new Object[0]);
                throw new IllegalArgumentException("Parcel request error.");
            } else if (getAbilityInfo(context, controlItemEnum, position).marshalling(obtain)) {
                synchronized (this.lock) {
                    if (this.drivingSafetyService == null) {
                        HiLog.error(TAG, "drivingSafetyService is null", new Object[0]);
                        throw new RemoteException();
                    } else if (this.drivingSafetyService.sendRequest(1, obtain, obtain2, this.option)) {
                        readBoolean = obtain2.readBoolean();
                    } else {
                        HiLog.error(TAG, "check drivingsafety failed", new Object[0]);
                        throw new RemoteException();
                    }
                }
                return readBoolean;
            } else {
                HiLog.error(TAG, "write atomicAbilityInfo to parcel failed", new Object[0]);
                throw new RemoteException();
            }
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ivicommon.drivingsafety.adapter.IDrivingSafetyClient
    public boolean isDrivingMode() throws RemoteException {
        boolean readBoolean;
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            if (obtain.writeInterfaceToken(this.interfaceConfigDescriptor)) {
                synchronized (this.lock) {
                    if (this.drivingSafetyService == null) {
                        HiLog.error(TAG, "drivingSafetyService is null", new Object[0]);
                        throw new RemoteException();
                    } else if (this.drivingSafetyService.sendRequest(2, obtain, obtain2, this.option)) {
                        readBoolean = obtain2.readBoolean();
                    } else {
                        HiLog.error(TAG, "get drivingmode failed", new Object[0]);
                        throw new RemoteException();
                    }
                }
                return readBoolean;
            }
            HiLog.error(TAG, "Parcel request error.", new Object[0]);
            throw new IllegalArgumentException("Parcel request error.");
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ivicommon.drivingsafety.adapter.IDrivingSafetyClient
    public int getRestraint() throws RemoteException {
        int readInt;
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            if (obtain.writeInterfaceToken(this.interfaceConfigDescriptor)) {
                synchronized (this.lock) {
                    if (this.drivingSafetyService == null) {
                        HiLog.error(TAG, "drivingSafetyService is null", new Object[0]);
                        throw new RemoteException();
                    } else if (this.drivingSafetyService.sendRequest(3, obtain, obtain2, this.option)) {
                        readInt = obtain2.readInt();
                    } else {
                        HiLog.error(TAG, "get drivingrestraint failed", new Object[0]);
                        throw new RemoteException();
                    }
                }
                return readInt;
            }
            HiLog.error(TAG, "Parcel request error.", new Object[0]);
            throw new IllegalArgumentException("Parcel request error.");
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }

    @Override // ohos.ivicommon.drivingsafety.adapter.IDrivingSafetyClient
    public boolean isSecondaryScreenRange(Position position) throws RemoteException {
        Position secondaryScreenRange = getSecondaryScreenRange();
        if (secondaryScreenRange.compareTo(position) != -1 && getWindow().getX() >= secondaryScreenRange.getX()) {
            return true;
        }
        return false;
    }

    @Override // ohos.ivicommon.drivingsafety.adapter.IDrivingSafetyClient
    public Position getSecondaryScreenRange() throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        try {
            if (obtain.writeInterfaceToken(this.interfaceConfigDescriptor)) {
                synchronized (this.lock) {
                    if (this.drivingSafetyService == null) {
                        HiLog.error(TAG, "drivingSafetyService is null.", new Object[0]);
                        throw new RemoteException();
                    } else if (!this.drivingSafetyService.sendRequest(4, obtain, obtain2, this.option)) {
                        HiLog.error(TAG, "getSecondaryScreenRange sendRequest failed", new Object[0]);
                        throw new RemoteException();
                    }
                }
                return new Position(new StringBuilder(obtain2.readString()));
            }
            HiLog.error(TAG, "Parcel request error.", new Object[0]);
            throw new IllegalArgumentException("Parcel request error.");
        } finally {
            obtain2.reclaim();
            obtain.reclaim();
        }
    }
}
