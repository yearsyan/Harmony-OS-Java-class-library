package ohos.accessibility;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import ohos.accessibility.adapter.AccessibilityConst;
import ohos.accessibility.adapter.AccessibilityViewAbilityAdapter;
import ohos.accessibility.adapter.AccessibilityViewDialogAdapter;
import ohos.accessibility.utils.LogUtil;
import ohos.app.Context;
import ohos.global.icu.impl.locale.LanguageTag;

public class BarrierFreeInnerClient {
    public static final int ABILITY_ACE = 1;
    public static final int ABILITY_JAVA_UI = 0;
    private static final Object LOCK_OBJ = new Object();
    private static final String TAG = "BarrierFreeInnerClient";
    private static Map<String, AccessibilityViewAbilityAdapter> sChildMap = new BarrierFreeAdapterMap(16);
    private static Map<String, AccessibilityViewDialogAdapter> sDialogMap = new BarrierFreeAdapterMap(16);
    private static Map<String, AccessibilityViewAbilityAdapter> sInstanceMap = new BarrierFreeAdapterMap(16);
    private static Map<String, AccessibilityViewAbilityAdapter> sViewMap = new BarrierFreeAdapterMap(16);

    private BarrierFreeInnerClient() {
    }

    public static boolean registerBarrierFreeAbility(Context context, int i) {
        LogUtil.info(TAG, "registerBarrierFreeAbility start, type:" + i + ",context:" + context);
        if (context == null) {
            LogUtil.error(TAG, "registerBarrierFreeAbility failed, context is null.");
            return false;
        } else if (i == 0 || i == 1) {
            String mapKey = getMapKey(context);
            synchronized (LOCK_OBJ) {
                if (!sInstanceMap.containsKey(mapKey)) {
                    sInstanceMap.put(mapKey, new AccessibilityViewAbilityAdapter(context, i));
                }
                sInstanceMap.get(mapKey).clearBarrierFreeFocus();
            }
            LogUtil.info(TAG, "registerBarrierFreeAbility end.");
            return true;
        } else {
            LogUtil.error(TAG, "registerBarrierFreeAbility failed, type is illegal.");
            return false;
        }
    }

    public static boolean unRegisterBarrierFreeAbility(Context context) {
        LogUtil.info(TAG, "unRegisterBarrierFreeAbility start. context:" + context);
        if (context == null) {
            LogUtil.error(TAG, "unRegisterBarrierFreeAbility failed, context is null.");
            return false;
        }
        String mapKey = getMapKey(context);
        synchronized (LOCK_OBJ) {
            if (!sInstanceMap.containsKey(mapKey)) {
                return false;
            }
            sInstanceMap.get(mapKey).releaseBarrierFreeView();
            sInstanceMap.remove(mapKey);
            LogUtil.info(TAG, "unRegisterBarrierFreeAbility end.");
            return true;
        }
    }

    public static boolean registerBarrierFreeView(Context context, int i, int i2) {
        AccessibilityViewAbilityAdapter accessibilityViewAbilityAdapter;
        LogUtil.info(TAG, "registerBarrierFreeView start, type:" + i2 + ",context:" + context + ",virtualViewId：" + i);
        if (context == null) {
            LogUtil.error(TAG, "registerBarrierFreeView failed, context is null.");
            return false;
        } else if (i2 == 0 || i2 == 1) {
            String mapKey = getMapKey(context);
            String mapKey2 = getMapKey(context, i);
            synchronized (LOCK_OBJ) {
                if (sInstanceMap.containsKey(mapKey)) {
                    AccessibilityViewAbilityAdapter accessibilityViewAbilityAdapter2 = sInstanceMap.get(mapKey);
                    if (sChildMap.containsKey(mapKey2)) {
                        accessibilityViewAbilityAdapter = sChildMap.get(mapKey2);
                    } else {
                        AccessibilityViewAbilityAdapter accessibilityViewAbilityAdapter3 = new AccessibilityViewAbilityAdapter(context, i, i2);
                        sChildMap.put(mapKey2, accessibilityViewAbilityAdapter3);
                        accessibilityViewAbilityAdapter = accessibilityViewAbilityAdapter3;
                    }
                    accessibilityViewAbilityAdapter.setParentAdapter(accessibilityViewAbilityAdapter2);
                    accessibilityViewAbilityAdapter.initBarrierFreeDelegateHelper(i, i2, mapKey2);
                } else {
                    LogUtil.info(TAG, "not contains mapKey:" + mapKey);
                }
            }
            LogUtil.info(TAG, "registerBarrierFreeView end.");
            return true;
        } else {
            LogUtil.error(TAG, "registerBarrierFreeView failed, type is illegal.");
            return false;
        }
    }

    public static boolean unRegisterBarrierFreeView(Context context, int i) {
        LogUtil.info(TAG, "unRegisterBarrierFreeView start. context:" + context + ",virtualViewId:" + i);
        if (context == null) {
            LogUtil.error(TAG, "unRegisterBarrierFreeView failed, context is null.");
            return false;
        }
        String mapKey = getMapKey(context);
        String mapKey2 = getMapKey(context, i);
        synchronized (LOCK_OBJ) {
            if (!sInstanceMap.containsKey(mapKey)) {
                return false;
            }
            if (sChildMap.containsKey(mapKey2)) {
                sChildMap.get(mapKey2).removeAdapter();
                sChildMap.remove(mapKey2);
            }
            LogUtil.info(TAG, "unRegisterBarrierFreeView end.");
            return true;
        }
    }

    public static boolean registerBarrierFreeObject(ViewGroup viewGroup, int i) {
        LogUtil.info(TAG, "registerBarrierFreeObject start, type:" + i + ",viewGroup:" + viewGroup);
        if (i != 0 && i != 1) {
            LogUtil.error(TAG, "registerBarrierFreeObject failed, type is illegal.");
            return false;
        } else if (viewGroup == null) {
            LogUtil.error(TAG, "registerBarrierFreeObject failed, viewGroup is illegal.");
            return false;
        } else {
            String mapKey = getMapKey(viewGroup);
            synchronized (LOCK_OBJ) {
                if (!sViewMap.containsKey(mapKey)) {
                    sViewMap.put(mapKey, new AccessibilityViewAbilityAdapter(viewGroup, i, mapKey));
                }
                sViewMap.get(mapKey).clearBarrierFreeFocus();
            }
            LogUtil.info(TAG, "registerBarrierFreeObject end.");
            return true;
        }
    }

    public static boolean unRegisterBarrierFreeObject(ViewGroup viewGroup) {
        if (viewGroup == null) {
            LogUtil.error(TAG, "unRegisterBarrierFreeObject failed, viewGroup is illegal.");
            return false;
        }
        LogUtil.info(TAG, "unRegisterBarrierFreeObject start. viewGroup:" + viewGroup);
        String mapKey = getMapKey(viewGroup);
        synchronized (LOCK_OBJ) {
            if (!sViewMap.containsKey(mapKey)) {
                return false;
            }
            sViewMap.get(mapKey).releaseBarrierFreeCommView();
            sViewMap.remove(mapKey);
            LogUtil.info(TAG, "unRegisterBarrierFreeObject end.");
            return true;
        }
    }

    public static boolean registerBarrierFreeDialog(Context context, View view, View view2, int i) {
        LogUtil.info(TAG, "registerBarrierFreeDialog start contentView:" + view + " type: " + i);
        if (view == null) {
            LogUtil.error(TAG, "registerBarrierFreeDialog failed, contentView is null.");
            return false;
        } else if (view2 == null) {
            LogUtil.error(TAG, "registerBarrierFreeDialog failed, decorView is null.");
            return false;
        } else if (i == 0 || i == 1) {
            String mapKey = getMapKey(view);
            synchronized (LOCK_OBJ) {
                if (!sDialogMap.containsKey(mapKey)) {
                    sDialogMap.put(mapKey, new AccessibilityViewDialogAdapter(context, view, view2, i));
                }
                sDialogMap.get(mapKey).clearBarrierFreeFocus();
            }
            LogUtil.info(TAG, "registerBarrierFreeDialog end.");
            return true;
        } else {
            LogUtil.error(TAG, "registerBarrierFreeDialog failed, type is illegal.");
            return false;
        }
    }

    public static boolean unRegisterBarrierFreeDialog(Context context, View view) {
        LogUtil.info(TAG, "unRegisterBarrierFreeDialog start. contentView:" + view);
        if (view == null) {
            LogUtil.error(TAG, "unRegisterBarrierFreeAbility failed, contentView is null.");
            return false;
        }
        String mapKey = getMapKey(view);
        synchronized (LOCK_OBJ) {
            if (!sDialogMap.containsKey(mapKey)) {
                return false;
            }
            sDialogMap.get(mapKey).releaseBarrierFreeView();
            sDialogMap.remove(mapKey);
            LogUtil.info(TAG, "unRegisterBarrierFreeDialog end.");
            return true;
        }
    }

    public static boolean sendBarrierFreeEvent(Context context, AccessibilityEventInfo accessibilityEventInfo) {
        String mapKey = getMapKey(context);
        synchronized (LOCK_OBJ) {
            if (sInstanceMap.containsKey(mapKey)) {
                LogUtil.info(TAG, "Use default adapter, sendBarrierFreeEvent end.");
                return sInstanceMap.get(mapKey).sendEvent(accessibilityEventInfo);
            }
            Iterator<Map.Entry<String, AccessibilityViewAbilityAdapter>> it = sInstanceMap.entrySet().iterator();
            Map.Entry<String, AccessibilityViewAbilityAdapter> entry = null;
            while (it.hasNext()) {
                entry = it.next();
            }
            if (entry != null) {
                LogUtil.info(TAG, "Use last visited adapter, sendBarrierFreeEvent end.");
                return entry.getValue().sendEvent(accessibilityEventInfo);
            }
            LogUtil.error(TAG, "sendBarrierFreeEvent failed.");
            return false;
        }
    }

    public static void fillAccessibilityEventInfo(Context context, AccessibilityEventInfo accessibilityEventInfo, AccessibilityEvent accessibilityEvent) {
        LogUtil.info(TAG, "fillAccessibilityEventInfo start.");
        if (context == null) {
            LogUtil.error(TAG, "convertEvent failed, context is null.");
        } else if (accessibilityEventInfo == null || accessibilityEvent == null) {
            LogUtil.error(TAG, "convertEvent failed, event is null.");
        } else {
            View view = null;
            Object hostContext = context.getHostContext();
            if (hostContext != null && (hostContext instanceof Activity)) {
                view = ((Activity) hostContext).getWindow().findViewById(16908290);
            }
            if (view == null) {
                LogUtil.error(TAG, "convertEvent failed, rootView is null.");
                return;
            }
            AccessibilityConst.convertEventInfoToEvent(view, accessibilityEventInfo, accessibilityEvent);
            LogUtil.info(TAG, "fillAccessibilityEventInfo end.");
        }
    }

    public static boolean sendBarrierFreeEvent(Context context, View view, AccessibilityEventInfo accessibilityEventInfo) {
        String mapKey = getMapKey(view);
        synchronized (LOCK_OBJ) {
            if (sDialogMap.containsKey(mapKey)) {
                LogUtil.info(TAG, "Use default adapter, sendBarrierFreeEvent for dialog end.");
                return sDialogMap.get(mapKey).sendEvent(accessibilityEventInfo);
            }
            Iterator<Map.Entry<String, AccessibilityViewDialogAdapter>> it = sDialogMap.entrySet().iterator();
            Map.Entry<String, AccessibilityViewDialogAdapter> entry = null;
            while (it.hasNext()) {
                entry = it.next();
            }
            if (entry != null) {
                LogUtil.info(TAG, "Use last visited adapter, sendBarrierFreeEvent for dialog end.");
                return entry.getValue().sendEvent(accessibilityEventInfo);
            }
            LogUtil.error(TAG, "sendBarrierFreeEvent for dialog failed.");
            return false;
        }
    }

    public static void fillBarrierFreeEventInfo(AccessibilityEvent accessibilityEvent, AccessibilityEventInfo accessibilityEventInfo) {
        LogUtil.info(TAG, "fillBarrierFreeEventInfo start.");
        if (accessibilityEvent == null || accessibilityEventInfo == null) {
            LogUtil.error(TAG, "convertEvent failed, event is null.");
            return;
        }
        AccessibilityConst.convertEventToEventInfo(accessibilityEvent, accessibilityEventInfo);
        LogUtil.info(TAG, "fillBarrierFreeEventInfo end.");
    }

    private static String getMapKey(Context context) {
        return context == null ? "" : context.toString();
    }

    private static String getMapKey(Context context, int i) {
        if (context == null) {
            return i + "";
        }
        return context.toString() + LanguageTag.SEP + i;
    }

    private static String getMapKey(ViewGroup viewGroup) {
        return viewGroup == null ? "" : viewGroup.toString();
    }

    private static String getMapKey(View view) {
        return view == null ? "" : view.toString();
    }

    private static class BarrierFreeAdapterMap<K, V> extends LinkedHashMap<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final float DEFAULT_LOAD_FACTOR = 0.75f;
        private static final long serialVersionUID = 1;
        private int maxEntityCount;

        BarrierFreeAdapterMap(int i) {
            super(16, DEFAULT_LOAD_FACTOR, true);
            this.maxEntityCount = i;
        }

        /* access modifiers changed from: protected */
        @Override // java.util.LinkedHashMap
        public boolean removeEldestEntry(Map.Entry<K, V> entry) {
            return size() > this.maxEntityCount;
        }
    }
}
