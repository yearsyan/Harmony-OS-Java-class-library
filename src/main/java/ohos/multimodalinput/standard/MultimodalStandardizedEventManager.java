package ohos.multimodalinput.standard;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import ohos.aafwk.ability.Ability;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MultimodalStandardizedEventManager {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218114065, "MultimodalStandardizedEventManager");
    private static final int MAP_DEFAULT_SIZE = 16;
    public static final int MMI_STANDARD_EVENT_HANDLE_TYPE_COMMON = 8;
    public static final int MMI_STANDARD_EVENT_HANDLE_TYPE_KEY = 1;
    public static final int MMI_STANDARD_EVENT_HANDLE_TYPE_MEDIA = 32;
    public static final int MMI_STANDARD_EVENT_HANDLE_TYPE_SYSTEM = 4;
    public static final int MMI_STANDARD_EVENT_HANDLE_TYPE_TELEPHONE = 16;
    public static final int MMI_STANDARD_EVENT_HANDLE_TYPE_TOUCH = 2;
    public static final int MMI_STANDARD_EVENT_HANDLE_TYPE_UNKNOWN = 0;
    private static final Object WAIT_LOCK = new Object();
    private static volatile MultimodalStandardizedEventManager eventManager;
    private WeakHashMap<Ability, List<StandardizedEventHandle>> eventMap = new WeakHashMap<>(16);

    public static MultimodalStandardizedEventManager getInstance() {
        if (eventManager == null) {
            synchronized (WAIT_LOCK) {
                if (eventManager == null) {
                    eventManager = new MultimodalStandardizedEventManager();
                }
            }
        }
        return eventManager;
    }

    public void releaseInstance() {
        synchronized (WAIT_LOCK) {
            if (eventManager != null) {
                eventManager = null;
            }
            this.eventMap.clear();
        }
    }

    public int registerStandardizedEventHandle(Ability ability, StandardizedEventHandle standardizedEventHandle) {
        if (ability == null || standardizedEventHandle == null) {
            HiLog.error(LOG_LABEL, "register invalid parameter", new Object[0]);
            return -1;
        }
        synchronized (WAIT_LOCK) {
            if (this.eventMap.containsKey(ability)) {
                List<StandardizedEventHandle> list = this.eventMap.get(ability);
                if (list.contains(standardizedEventHandle)) {
                    return 2;
                }
                list.add(standardizedEventHandle);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(standardizedEventHandle);
                this.eventMap.put(ability, arrayList);
            }
            return 1;
        }
    }

    public int unregisterStandardizedEventHandle(Ability ability, StandardizedEventHandle standardizedEventHandle) {
        if (ability == null || standardizedEventHandle == null) {
            HiLog.error(LOG_LABEL, "unregister invalid parameter", new Object[0]);
            return -1;
        }
        synchronized (WAIT_LOCK) {
            if (!this.eventMap.containsKey(ability)) {
                return 3;
            }
            List<StandardizedEventHandle> list = this.eventMap.get(ability);
            if (list != null) {
                if (!list.isEmpty()) {
                    list.remove(standardizedEventHandle);
                    if (list.isEmpty()) {
                        this.eventMap.remove(ability);
                    }
                    return 1;
                }
            }
            this.eventMap.remove(ability);
            return 1;
        }
    }

    public WeakHashMap<Ability, List<StandardizedEventHandle>> getStandardizedEventHandleMap() {
        WeakHashMap<Ability, List<StandardizedEventHandle>> cloneMap;
        synchronized (WAIT_LOCK) {
            cloneMap = cloneMap(this.eventMap);
        }
        return cloneMap;
    }

    public int getStandardizedEventHandleType(StandardizedEventHandle standardizedEventHandle) {
        if (standardizedEventHandle == null) {
            HiLog.error(LOG_LABEL, "get type invalid parameter", new Object[0]);
            return 0;
        } else if (standardizedEventHandle instanceof KeyEventHandle) {
            return 1;
        } else {
            if (standardizedEventHandle instanceof TouchEventHandle) {
                return 2;
            }
            if (standardizedEventHandle instanceof SystemEventHandle) {
                return 4;
            }
            if (standardizedEventHandle instanceof CommonEventHandle) {
                return 8;
            }
            if (standardizedEventHandle instanceof TelephoneEventHandle) {
                return 16;
            }
            if (standardizedEventHandle instanceof MediaEventHandle) {
                return 32;
            }
            return 0;
        }
    }

    private WeakHashMap<Ability, List<StandardizedEventHandle>> cloneMap(WeakHashMap<Ability, List<StandardizedEventHandle>> weakHashMap) {
        WeakHashMap<Ability, List<StandardizedEventHandle>> weakHashMap2 = new WeakHashMap<>(weakHashMap.size());
        for (Ability ability : weakHashMap.keySet()) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(weakHashMap.get(ability));
            weakHashMap2.put(ability, arrayList);
        }
        return weakHashMap2;
    }
}
