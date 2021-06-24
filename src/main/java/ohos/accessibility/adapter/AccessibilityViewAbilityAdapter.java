package ohos.accessibility.adapter;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import java.util.List;
import ohos.accessibility.AccessibilityEventInfo;
import ohos.accessibility.utils.LogUtil;
import ohos.app.Context;

public class AccessibilityViewAbilityAdapter {
    protected static final int ABILITY_ACE = 1;
    protected static final int ABILITY_JAVA_UI = 0;
    private static final String TAG = "AccessibilityViewAbilityAdapter";
    private static final int TYPE_ACCESSIBILITY_ABILITY_INTERRUPT = -1;
    private static final int TYPE_ACE_DIALOG_DISMISS = 32;
    private static final int TYPE_SCROLL_START = 16777216;
    protected AccessibilityManager accessibilityManager = null;
    private ViewGroup commViewGroup;
    protected BarrierFreeDelegateHelper delegateHelper;
    private Activity hostActivity;
    protected View hostView;
    private AccessibilityViewAbilityAdapter parentAdapter;

    protected AccessibilityViewAbilityAdapter() {
    }

    public AccessibilityViewAbilityAdapter(Context context, int i) {
        if (context == null) {
            LogUtil.info(TAG, "create AccessibilityViewAbilityAdapter error, abilityContext is null, just return.");
            return;
        }
        Object hostContext = context.getHostContext();
        if (hostContext == null || !(hostContext instanceof Activity)) {
            LogUtil.error(TAG, "create AccessibilityViewAbilityAdapter error, context is null, just return.");
            return;
        }
        this.hostActivity = (Activity) hostContext;
        this.accessibilityManager = AccessibilityManager.getInstance(this.hostActivity.getApplicationContext());
        this.hostView = this.hostActivity.getWindow().findViewById(16908290);
        initBarrierFreeView(i);
    }

    public AccessibilityViewAbilityAdapter(Context context, int i, int i2) {
        if (context == null) {
            LogUtil.error(TAG, "create AccessibilityViewAbilityAdapter error, abilityContext is null, just return.");
            return;
        }
        Object hostContext = context.getHostContext();
        if (hostContext == null || !(hostContext instanceof Activity)) {
            LogUtil.error(TAG, "create AccessibilityViewAbilityAdapter error, context is null, just return.");
            return;
        }
        this.hostActivity = (Activity) hostContext;
        this.accessibilityManager = AccessibilityManager.getInstance(this.hostActivity.getApplicationContext());
        this.hostView = this.hostActivity.getWindow().findViewById(16908290);
    }

    public AccessibilityViewAbilityAdapter(ViewGroup viewGroup, int i, String str) {
        if (viewGroup == null) {
            LogUtil.info(TAG, "create AccessibilityViewAbilityAdapter error, viewGroup is null, just return.");
            return;
        }
        this.commViewGroup = viewGroup;
        this.accessibilityManager = AccessibilityManager.getInstance(viewGroup.getContext());
        initBarrierFreeCommView(i, str);
    }

    private void initBarrierFreeView(int i) {
        BarrierFreeDelegateHelper barrierFreeDelegateHelper;
        LogUtil.info(TAG, "initBarrierFreeView start. type:" + i);
        View view = this.hostView;
        if (view == null || this.hostActivity == null) {
            LogUtil.error(TAG, "initBarrierFreeView failed, hostView or hostActivity is null.");
            return;
        }
        if (i == 0) {
            barrierFreeDelegateHelper = new AccessibilityViewDelegate(view);
        } else if (i == 1) {
            barrierFreeDelegateHelper = new AccessibilityAceViewDelegate(view);
        } else {
            LogUtil.error(TAG, "initBarrierFreeView failed, ability type is illegal.");
            return;
        }
        this.delegateHelper = barrierFreeDelegateHelper;
        this.hostView.setAccessibilityDelegate(barrierFreeDelegateHelper);
        View decorView = this.hostActivity.getWindow().getDecorView();
        if (decorView == null) {
            LogUtil.error(TAG, "initBarrierFreeView end, decorView is null.");
            return;
        }
        AdapterTouchDelegate adapterTouchDelegate = new AdapterTouchDelegate(new Rect(0, 0, 2147483646, 2147483646), this.hostView, barrierFreeDelegateHelper);
        for (View view2 : getAllChildViews(decorView)) {
            view2.setTouchDelegate(adapterTouchDelegate);
        }
        decorView.setTouchDelegate(adapterTouchDelegate);
        LogUtil.info(TAG, "initBarrierFreeView end.");
    }

    private void initBarrierFreeCommView(int i, String str) {
        BarrierFreeDelegateHelper barrierFreeDelegateHelper;
        LogUtil.info(TAG, "initBarrierFreeCommView start.");
        ViewGroup viewGroup = this.commViewGroup;
        if (viewGroup == null) {
            LogUtil.error(TAG, "initBarrierFreeCommView failed, commViewGroup is null.");
            return;
        }
        ViewParent parentForAccessibility = viewGroup.getParentForAccessibility();
        LogUtil.info(TAG, "initBarrierFreeCommView parent:" + parentForAccessibility);
        if (parentForAccessibility != null && (parentForAccessibility instanceof View)) {
            View view = (View) parentForAccessibility;
            if (i == 0) {
                barrierFreeDelegateHelper = new AccessibilityViewDelegate(this.commViewGroup, view, (String) null);
            } else if (i == 1) {
                barrierFreeDelegateHelper = new AccessibilityAceViewDelegate(this.commViewGroup, view, str);
            } else {
                LogUtil.error(TAG, "initBarrierFreeCommView failed, type is illegal.");
                return;
            }
        } else if (i == 0) {
            barrierFreeDelegateHelper = new AccessibilityViewDelegate(this.commViewGroup);
        } else if (i == 1) {
            barrierFreeDelegateHelper = new AccessibilityAceViewDelegate(this.commViewGroup);
        } else {
            LogUtil.error(TAG, "initBarrierFreeCommView failed, type is illegal.");
            return;
        }
        this.delegateHelper = barrierFreeDelegateHelper;
        this.commViewGroup.setAccessibilityDelegate(barrierFreeDelegateHelper);
        AdapterTouchDelegate adapterTouchDelegate = new AdapterTouchDelegate(new Rect(0, 0, 2147483646, 2147483646), this.commViewGroup, barrierFreeDelegateHelper);
        for (View view2 : getAllChildViews(this.commViewGroup)) {
            view2.setTouchDelegate(adapterTouchDelegate);
        }
        this.commViewGroup.setTouchDelegate(adapterTouchDelegate);
        LogUtil.info(TAG, "initBarrierFreeCommView end.");
    }

    public void initBarrierFreeDelegateHelper(int i, int i2, String str) {
        BarrierFreeDelegateHelper barrierFreeDelegateHelper;
        if (i2 == 0) {
            barrierFreeDelegateHelper = new AccessibilityViewDelegate(this.hostView, i, (String) null);
        } else if (i2 == 1) {
            barrierFreeDelegateHelper = new AccessibilityAceViewDelegate(this.hostView, i, str);
        } else {
            LogUtil.error(TAG, "initBarrierFreeDelegateHelper failed, ability type is illegal.");
            return;
        }
        this.delegateHelper = barrierFreeDelegateHelper;
        AccessibilityViewAbilityAdapter accessibilityViewAbilityAdapter = this.parentAdapter;
        if (accessibilityViewAbilityAdapter == null) {
            LogUtil.info(TAG, "has no parent adapter.");
            return;
        }
        BarrierFreeDelegateHelper barrierFreeDelegateHelper2 = accessibilityViewAbilityAdapter.delegateHelper;
        if (barrierFreeDelegateHelper2 != null) {
            barrierFreeDelegateHelper2.addChildHepler(barrierFreeDelegateHelper);
        } else {
            LogUtil.info(TAG, "has no parent DelegateHelper.");
        }
    }

    public void releaseBarrierFreeView() {
        clearBarrierFreeFocus();
        View view = this.hostView;
        if (view != null) {
            view.setAccessibilityDelegate(null);
            this.hostView = null;
        }
        Activity activity = this.hostActivity;
        if (activity != null) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                for (View view2 : getAllChildViews(decorView)) {
                    view2.setTouchDelegate(null);
                }
            }
            this.hostActivity = null;
        }
    }

    public void setParentAdapter(AccessibilityViewAbilityAdapter accessibilityViewAbilityAdapter) {
        this.parentAdapter = accessibilityViewAbilityAdapter;
    }

    public void removeAdapter() {
        BarrierFreeDelegateHelper barrierFreeDelegateHelper;
        AccessibilityViewAbilityAdapter accessibilityViewAbilityAdapter = this.parentAdapter;
        if (accessibilityViewAbilityAdapter != null && (barrierFreeDelegateHelper = this.delegateHelper) != null) {
            accessibilityViewAbilityAdapter.delegateHelper.removeChildHepler(barrierFreeDelegateHelper);
            this.delegateHelper = null;
        }
    }

    public BarrierFreeDelegateHelper getBarrierFreeDelegateHelper() {
        return this.delegateHelper;
    }

    public void releaseBarrierFreeCommView() {
        LogUtil.info(TAG, "releaseBarrierFreeCommView start");
        clearBarrierFreeCommFocus();
        ViewGroup viewGroup = this.commViewGroup;
        if (viewGroup != null) {
            viewGroup.setAccessibilityDelegate(null);
            for (View view : getAllChildViews(this.commViewGroup)) {
                view.setTouchDelegate(null);
            }
            this.commViewGroup.setTouchDelegate(null);
            this.commViewGroup = null;
        }
        LogUtil.info(TAG, "releaseBarrierFreeCommView end");
    }

    public void clearBarrierFreeCommFocus() {
        ViewGroup viewGroup = this.commViewGroup;
        if (viewGroup == null) {
            LogUtil.error(TAG, "clearBarrierFreeCommFocus commViewGroup is null");
            return;
        }
        Handler handler = viewGroup.getHandler();
        if (handler == null) {
            LogUtil.error(TAG, "clearBarrierFreeCommFocus handler is null");
        } else {
            handler.post(new Runnable() {
                /* class ohos.accessibility.adapter.$$Lambda$AccessibilityViewAbilityAdapter$iepfCFs6pXYVxaRx0l_UB8ePct4 */

                public final void run() {
                    AccessibilityViewAbilityAdapter.this.lambda$clearBarrierFreeCommFocus$0$AccessibilityViewAbilityAdapter();
                }
            });
        }
    }

    public /* synthetic */ void lambda$clearBarrierFreeCommFocus$0$AccessibilityViewAbilityAdapter() {
        ViewGroup viewGroup = this.commViewGroup;
        if (viewGroup != null) {
            viewGroup.clearAccessibilityFocus();
            LogUtil.info(TAG, "clearBarrierFreeCommFocus successful.");
        }
    }

    public void clearBarrierFreeFocus() {
        Activity activity = this.hostActivity;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                /* class ohos.accessibility.adapter.$$Lambda$AccessibilityViewAbilityAdapter$pB26uEtZZQNYMoHgoAjecHIHaMI */

                public final void run() {
                    AccessibilityViewAbilityAdapter.this.lambda$clearBarrierFreeFocus$1$AccessibilityViewAbilityAdapter();
                }
            });
        }
    }

    public /* synthetic */ void lambda$clearBarrierFreeFocus$1$AccessibilityViewAbilityAdapter() {
        View view = this.hostView;
        if (view != null) {
            view.clearAccessibilityFocus();
            LogUtil.debug(TAG, "clearBarrierFreeFocus successful.");
        }
    }

    protected static List<View> getAllChildViews(View view) {
        ArrayList arrayList = new ArrayList();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                arrayList.add(childAt);
                arrayList.addAll(getAllChildViews(childAt));
            }
        }
        return arrayList;
    }

    public boolean sendEvent(AccessibilityEventInfo accessibilityEventInfo) {
        ViewParent parent;
        LogUtil.info(TAG, "sendEventInfo start.");
        if (accessibilityEventInfo == null) {
            LogUtil.error(TAG, "eventInfo is null.");
            return false;
        }
        LogUtil.info(TAG, "sendEvent id:" + accessibilityEventInfo.getViewId() + " type:" + accessibilityEventInfo.getAccessibilityEventType());
        if (this.accessibilityManager == null) {
            LogUtil.error(TAG, "manager is null.");
            return false;
        } else if (startProcessCustomEvent(accessibilityEventInfo)) {
            return true;
        } else {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            AccessibilityConst.convertEventInfoToEvent(this.hostView, accessibilityEventInfo, obtain);
            View view = this.hostView;
            if (view != null && (parent = view.getParent()) != null) {
                return parent.requestSendAccessibilityEvent(this.hostView, obtain);
            }
            this.accessibilityManager.sendAccessibilityEvent(obtain);
            return true;
        }
    }

    private boolean startProcessCustomEvent(AccessibilityEventInfo accessibilityEventInfo) {
        int accessibilityEventType = accessibilityEventInfo.getAccessibilityEventType();
        if (accessibilityEventType == -1) {
            this.accessibilityManager.interrupt();
            return true;
        } else if (accessibilityEventType == 32) {
            clearBarrierFreeFocus();
            return false;
        } else if (accessibilityEventType == 200) {
            LogUtil.info(TAG, "start refresh ace page. ");
            clearBarrierFreeFocus();
            return true;
        } else if (accessibilityEventType != 16777216) {
            return false;
        } else {
            LogUtil.info(TAG, "scroll start, clear barrier free focus.");
            clearBarrierFreeFocus();
            return true;
        }
    }
}
