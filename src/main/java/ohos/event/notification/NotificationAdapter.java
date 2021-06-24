package ohos.event.notification;

import java.util.List;
import java.util.Set;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.idn.DeviceManager;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public final class NotificationAdapter extends RemoteObject implements IAdvancedNotificationManager {
    private static final HiLogLabel LABEL = new HiLogLabel(3, EventConstant.NOTIFICATION_DOMAIN, TAG);
    private static final String TAG = "NotificationAdapter";
    private AndroidNotificationManager androidNotificationManager = new AndroidNotificationManager();
    private DeviceManager deviceManager = null;
    private ZidaneNotificationManager zidaneNotificationManger = new ZidaneNotificationManager();

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    static {
        try {
            HiLog.info(LABEL, "inner Load libipc_core.z.so", new Object[0]);
            System.loadLibrary("ipc_core.z");
        } catch (UnsatisfiedLinkError unused) {
            HiLog.error(LABEL, "ERROR: Could not load ipc_core.z ", new Object[0]);
        }
    }

    public NotificationAdapter() {
        super(IAdvancedNotificationManager.DESCRIPTOR);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void scheduleNotification(String str, String str2, NotificationRequest notificationRequest) throws RemoteException {
        this.androidNotificationManager.publishNotification(str, notificationRequest);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void cancelAllNotifications(String str, int i) throws RemoteException {
        this.androidNotificationManager.cancelAllNotifications();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void cancelNotification(String str, String str2) throws RemoteException {
        this.androidNotificationManager.cancelNotification(str, str2);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void subscribeNotification(INotificationSubscriber iNotificationSubscriber, NotificationSubscribeInfo notificationSubscribeInfo) throws RemoteException {
        if (iNotificationSubscriber == null) {
            throw new RemoteException();
        } else if (!(iNotificationSubscriber instanceof NotificationSubscriberHost)) {
            HiLog.warn(LABEL, "NotificationAdapter::subscribeNotification invalid INotificationSubscriber", new Object[0]);
        } else {
            this.androidNotificationManager.subscribeNotification((NotificationSubscriberHost) iNotificationSubscriber, notificationSubscribeInfo);
        }
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void unsubscribeNotification(INotificationSubscriber iNotificationSubscriber, NotificationSubscribeInfo notificationSubscribeInfo) throws RemoteException {
        if (iNotificationSubscriber == null) {
            throw new RemoteException();
        } else if (!(iNotificationSubscriber instanceof NotificationSubscriberHost)) {
            HiLog.warn(LABEL, "NotificationAdapter::unsubscribeNotification invalid INotificationSubscriber", new Object[0]);
        } else {
            this.androidNotificationManager.unsubscribeNotification((NotificationSubscriberHost) iNotificationSubscriber, notificationSubscribeInfo);
        }
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void addNotificationSlot(String str, NotificationSlot notificationSlot) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.addNotificationSlot(notificationSlot);
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void removeNotificationSlot(String str, String str2) throws RemoteException {
        if (str == null || str2 == null || str2.isEmpty()) {
            throw new RemoteException();
        }
        this.androidNotificationManager.removeNotificationSlot(str2);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public NotificationSlot getNotificationSlot(String str, String str2) throws RemoteException {
        if (str != null && str2 != null && !str2.isEmpty()) {
            return this.androidNotificationManager.getNotificationSlot(str2);
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public Set<NotificationRequest> getActiveNotifications(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getActiveNotifications();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public Set<NotificationRequest> getAllActiveNotifications(String[] strArr) throws RemoteException {
        return this.androidNotificationManager.getAllActiveNotifications(strArr);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public int getActiveNotificationNums(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getActiveNotificationNums();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void setNotificationBadgeNum(String str, int i) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.setNotificationBadgeNum(i);
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void setNotificationBadgeNum(String str) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.setNotificationBadgeNum();
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public boolean isAllowedNotify(String str) throws RemoteException {
        return this.androidNotificationManager.isAllowedNotify(str);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void setNotificationsEnabledForDefaultBundle(String str, boolean z) throws RemoteException {
        this.androidNotificationManager.setNotificationsEnabledForDefaultPackage(str, z);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void setNotificationsEnabledForSpecifiedBundle(String str, String str2, boolean z) throws RemoteException {
        this.androidNotificationManager.setNotificationsEnabledForSpecifiedPackage(str, str2, z);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void setNotificationsEnabledForAllBundles(String str, boolean z) throws RemoteException {
        this.androidNotificationManager.setNotificationsEnabledForAllPackages(str, z);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public NotificationSortingMap getCurrentAppSorting(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getCurrentAppSorting();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void addNotificationSlotGroup(String str, NotificationSlotGroup notificationSlotGroup) throws RemoteException {
        if (str == null || notificationSlotGroup == null) {
            throw new RemoteException();
        }
        this.androidNotificationManager.addNotificationSlotGroup(notificationSlotGroup);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void addNotificationSlotGroups(String str, List<NotificationSlotGroup> list) throws RemoteException {
        if (str == null || list == null) {
            throw new RemoteException();
        }
        this.androidNotificationManager.addNotificationSlotGroups(list);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void removeNotificationSlotGroup(String str, String str2) throws RemoteException {
        if (str == null || str2 == null || str2.isEmpty()) {
            throw new RemoteException();
        }
        this.androidNotificationManager.removeNotificationSlotGroup(str2);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public NotificationSlotGroup getNotificationSlotGroup(String str, String str2) throws RemoteException {
        if (str != null && str2 != null && !str2.isEmpty()) {
            return this.androidNotificationManager.getNotificationSlotGroup(str2);
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public boolean hasNotificationPolicyAccessPermission(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.hasNotificationPolicyAccessPermission();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void addNotificationSlots(String str, List<NotificationSlot> list) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.addNotificationSlots(list);
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public List<NotificationSlot> getNotificationSlots(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getNotificationSlots();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void publishNotificationAsBundle(String str, String str2, NotificationRequest notificationRequest) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.publishNotificationAsPackage(str2, notificationRequest);
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void setNotificationAgent(String str, String str2) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.setNotificationAgent(str2);
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public String getNotificationAgent(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getNotificationAgent();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public boolean canPublishNotificationAsBundle(String str, String str2) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.canPublishNotificationAsPackage(str2);
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public List<NotificationSlotGroup> getNotificationSlotGroups(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getNotificationSlotGroups();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public boolean isAllowedNotify() throws RemoteException {
        return this.androidNotificationManager.isAllowedNotify();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public boolean areNotificationsSuspended(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.areNotificationsSuspended();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public int getBundleImportance(String str) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getPackageImportance();
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void removeNotification(String str, String str2) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.removeNotification(str2);
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void removeNotifications(String str, String str2) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.removeNotifications(str2);
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void removeNotifications(String str) throws RemoteException {
        if (str != null) {
            this.androidNotificationManager.removeNotifications();
            return;
        }
        throw new RemoteException();
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public List<NotificationSlot> getNotificationSlotsForBundle(String str, String str2) throws RemoteException {
        if (str != null) {
            return this.androidNotificationManager.getNotificationSlotsForBundle(str2);
        }
        throw new RemoteException("Bundle name is invalid.");
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public void setDisturbMode(int i) throws RemoteException {
        this.androidNotificationManager.setDisturbMode(i);
    }

    @Override // ohos.event.notification.IAdvancedNotificationManager
    public int getDisturbMode() throws RemoteException {
        return this.androidNotificationManager.getDisturbMode();
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scheduleRemoteNotification(ohos.event.notification.NotificationRequest r3, java.lang.String r4) throws ohos.rpc.RemoteException {
        /*
            r2 = this;
            r0 = 0
            if (r4 == 0) goto L_0x0036
            boolean r1 = r4.isEmpty()
            if (r1 != 0) goto L_0x0036
            ohos.idn.DeviceManager r1 = r2.deviceManager
            if (r1 != 0) goto L_0x0014
            ohos.idn.DeviceManager r1 = new ohos.idn.DeviceManager
            r1.<init>()
            r2.deviceManager = r1
        L_0x0014:
            ohos.idn.DeviceManager r1 = r2.deviceManager
            java.util.Optional r1 = r1.getLocalNodeBasicInfo()
            java.lang.Object r1 = r1.orElse(r0)
            ohos.idn.BasicInfo r1 = (ohos.idn.BasicInfo) r1
            if (r1 == 0) goto L_0x002e
            java.lang.String r1 = r1.getNodeId()
            boolean r1 = r4.equals(r1)
            if (r1 != 0) goto L_0x0036
            r1 = 1
            goto L_0x0037
        L_0x002e:
            ohos.rpc.RemoteException r2 = new ohos.rpc.RemoteException
            java.lang.String r3 = "failed to get local device id"
            r2.<init>(r3)
            throw r2
        L_0x0036:
            r1 = 0
        L_0x0037:
            if (r1 == 0) goto L_0x003f
            ohos.event.notification.ZidaneNotificationManager r2 = r2.zidaneNotificationManger
            r2.publishRemoteNotification(r3, r4)
            goto L_0x0044
        L_0x003f:
            ohos.event.notification.AndroidNotificationManager r2 = r2.androidNotificationManager
            r2.publishNotification(r0, r3)
        L_0x0044:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.event.notification.NotificationAdapter.scheduleRemoteNotification(ohos.event.notification.NotificationRequest, java.lang.String):void");
    }
}
