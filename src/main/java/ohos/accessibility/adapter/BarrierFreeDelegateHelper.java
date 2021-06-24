package ohos.accessibility.adapter;

import android.os.Bundle;
import android.util.IntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;
import ohos.accessibility.adapter.AccessibilityViewInfo;
import ohos.accessibility.utils.LogUtil;
import ohos.agp.utils.Rect;

public abstract class BarrierFreeDelegateHelper extends View.AccessibilityDelegate {
    private static final String DEFAULT_CLASS_NAME = View.class.getName();
    private static final int HOST_VIEW_ID = -1;
    private static final int INVALID_VIEW_ID = Integer.MIN_VALUE;
    private static final int POSITION_SIZE = 2;
    private static final String TAG = "BarrierFreeDelegateHelper";
    private static final int VIRTUAL_VIEW_ID_BASE = 10000;
    private static final int VIRTUAL_VIEW_ID_RATIO = 100000;
    private final AccessibilityManager accessibilityManager;
    private List<BarrierFreeDelegateHelper> childHelperList = new ArrayList();
    private int focusedViewId = Integer.MIN_VALUE;
    private View hostView;
    private int hoveredVirtualId = Integer.MIN_VALUE;
    protected String mapKey = null;
    private BarrierFreeDelegateProvider nodeProvider;
    private View parentView;
    private int virtualViewId = -1;

    private AccessibilityViewInfo getResultViewInfo(AccessibilityViewInfo accessibilityViewInfo, AccessibilityViewInfo accessibilityViewInfo2) {
        return accessibilityViewInfo2 == null ? accessibilityViewInfo : accessibilityViewInfo2;
    }

    /* access modifiers changed from: protected */
    public abstract boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract void onPopulateAllViewIds(IntArray intArray);

    /* access modifiers changed from: protected */
    public abstract void onPopulateEvent(AccessibilityViewInfo accessibilityViewInfo, AccessibilityEvent accessibilityEvent);

    /* access modifiers changed from: protected */
    public abstract void onPopulateNodeInfo(AccessibilityViewInfo accessibilityViewInfo, AccessibilityNodeInfo accessibilityNodeInfo);

    /* access modifiers changed from: protected */
    public abstract AccessibilityViewInfo queryAccessibilityViewInfoById(int i, String str);

    /* access modifiers changed from: protected */
    public abstract AccessibilityViewInfo queryRootAccessibilityViewInfo();

    /* access modifiers changed from: protected */
    public abstract AccessibilityViewInfo queryRootAccessibilityViewInfo(String str);

    public BarrierFreeDelegateHelper(View view) {
        if (view == null || view.getContext() == null) {
            throw new IllegalArgumentException("host view can not be null.");
        }
        this.hostView = view;
        this.accessibilityManager = (AccessibilityManager) view.getContext().getSystemService(AccessibilityManager.class);
    }

    public BarrierFreeDelegateHelper(View view, View view2, String str) {
        if (view == null || view.getContext() == null) {
            throw new IllegalArgumentException("host view can not be null.");
        }
        this.hostView = view;
        this.parentView = view2;
        this.mapKey = str;
        this.accessibilityManager = (AccessibilityManager) view.getContext().getSystemService(AccessibilityManager.class);
    }

    public BarrierFreeDelegateHelper(View view, int i, String str) {
        if (view == null || view.getContext() == null) {
            throw new IllegalArgumentException("host view can not be null.");
        }
        this.hostView = view;
        this.virtualViewId = i;
        this.mapKey = str;
        this.accessibilityManager = (AccessibilityManager) view.getContext().getSystemService(AccessibilityManager.class);
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider(View view) {
        if (this.nodeProvider == null) {
            this.nodeProvider = new BarrierFreeDelegateProvider();
        }
        return this.nodeProvider;
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            LogUtil.error(TAG, "event is null.");
            return false;
        } else if (!isAccessibilityManagerEnabled()) {
            LogUtil.error(TAG, "accessibility manager is not enabled.");
            return false;
        } else {
            int action = motionEvent.getAction();
            if (action == 7 || action == 9) {
                int viewIdByCoordinates = getViewIdByCoordinates(motionEvent.getRawX(), motionEvent.getRawY());
                updateHoveredEvent(viewIdByCoordinates);
                if (viewIdByCoordinates != Integer.MIN_VALUE) {
                    return true;
                }
                return false;
            } else if (action != 10 || this.focusedViewId == Integer.MIN_VALUE) {
                return false;
            } else {
                updateHoveredEvent(Integer.MIN_VALUE);
                return true;
            }
        }
    }

    public boolean sendAccessibilityEvent(int i, int i2) {
        LogUtil.info(TAG, "sendAccessibilityEvent virtualViewId:" + i + " eventType:" + i2);
        if (i == Integer.MIN_VALUE || !isAccessibilityEnabled()) {
            LogUtil.error(TAG, "sendAccessibilityEvent failed, accessibility is not enabled.");
            return false;
        }
        ViewParent parent = this.hostView.getParent();
        if (parent == null) {
            LogUtil.error(TAG, "sendAccessibilityEvent failed, parent is null.");
            return false;
        }
        return parent.requestSendAccessibilityEvent(this.hostView, createEvent(i, i2));
    }

    public void addChildHepler(BarrierFreeDelegateHelper barrierFreeDelegateHelper) {
        if (!this.childHelperList.contains(barrierFreeDelegateHelper)) {
            this.childHelperList.add(barrierFreeDelegateHelper);
        }
    }

    public void removeChildHepler(BarrierFreeDelegateHelper barrierFreeDelegateHelper) {
        if (this.childHelperList.contains(barrierFreeDelegateHelper)) {
            this.childHelperList.remove(barrierFreeDelegateHelper);
        }
    }

    public int getVirtualViewId() {
        return this.virtualViewId;
    }

    /* access modifiers changed from: protected */
    public int getViewIdByCoordinates(float f, float f2) {
        AccessibilityViewInfo queryRootAccessibilityViewInfo = queryRootAccessibilityViewInfo(this.mapKey);
        if (queryRootAccessibilityViewInfo == null) {
            LogUtil.info(TAG, "getViewIdByCoordinates failed, root view is null.");
            return -1;
        }
        AccessibilityViewInfo clickedAccessibilityView = getClickedAccessibilityView(queryRootAccessibilityViewInfo, (int) f, (int) f2);
        if (clickedAccessibilityView != null) {
            return clickedAccessibilityView.getId();
        }
        return -1;
    }

    private boolean isAccessibilityManagerEnabled() {
        AccessibilityManager accessibilityManager2 = this.accessibilityManager;
        if (accessibilityManager2 == null) {
            return false;
        }
        if (accessibilityManager2.isEnabled() || this.accessibilityManager.isTouchExplorationEnabled()) {
            return true;
        }
        return false;
    }

    private boolean isAccessibilityEnabled() {
        AccessibilityManager accessibilityManager2 = this.accessibilityManager;
        return accessibilityManager2 != null && accessibilityManager2.isEnabled();
    }

    private AccessibilityEvent createEvent(int i, int i2) {
        if (i != -1) {
            return createChildEvent(i, i2);
        }
        return createHostEvent(i2);
    }

    private AccessibilityEvent createHostEvent(int i) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
        this.hostView.onInitializeAccessibilityEvent(obtain);
        return obtain;
    }

    private AccessibilityEvent createChildEvent(int i, int i2) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
        obtain.setEnabled(true);
        obtain.setClassName(DEFAULT_CLASS_NAME);
        AccessibilityViewInfo queryAccessibilityViewInfoById = queryAccessibilityViewInfoById(i, this.mapKey);
        if (queryAccessibilityViewInfoById != null) {
            obtain.setContentDescription(queryAccessibilityViewInfoById.getDescription());
            AccessibilityViewInfo.ProgressInfo progressInfo = queryAccessibilityViewInfoById.getProgressInfo();
            if (progressInfo != null) {
                obtain.setItemCount(progressInfo.getMax() - progressInfo.getMin());
                obtain.setCurrentItemIndex(progressInfo.getValue());
            }
        }
        onPopulateEvent(queryAccessibilityViewInfoById, obtain);
        obtain.setPackageName(this.hostView.getContext().getPackageName());
        obtain.setSource(this.hostView, i);
        return obtain;
    }

    private void updateHoveredEvent(int i) {
        int i2 = this.hoveredVirtualId;
        this.hoveredVirtualId = i;
        sendAccessibilityEvent(i, 128);
        if (i2 != i) {
            sendAccessibilityEvent(i2, 256);
        }
    }

    private BarrierFreeDelegateHelper getChildHelperByViewId(int i) {
        for (BarrierFreeDelegateHelper barrierFreeDelegateHelper : this.childHelperList) {
            if (barrierFreeDelegateHelper.getVirtualViewId() == i) {
                return barrierFreeDelegateHelper;
            }
        }
        return null;
    }

    private BarrierFreeDelegateHelper getChildHelperByAceId(int i) {
        for (BarrierFreeDelegateHelper barrierFreeDelegateHelper : this.childHelperList) {
            if (barrierFreeDelegateHelper.getVirtualViewId() == i / 100000) {
                return barrierFreeDelegateHelper;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private AccessibilityNodeInfo createNode(int i) {
        if (i == -1) {
            return createHostNodeInfo(this.parentView);
        }
        BarrierFreeDelegateHelper childHelperByAceId = getChildHelperByAceId(i);
        if (childHelperByAceId != null) {
            return childHelperByAceId.createChildNodeInfo(i);
        }
        return createChildNodeInfo(i);
    }

    private AccessibilityNodeInfo createHostNodeInfo(View view) {
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(this.hostView);
        this.hostView.onInitializeAccessibilityNodeInfo(obtain);
        obtain.setParent(view);
        IntArray intArray = new IntArray();
        onPopulateAllViewIds(intArray);
        int size = intArray.size();
        for (int i = 0; i < size; i++) {
            obtain.addChild(this.hostView, intArray.get(i));
        }
        return obtain;
    }

    private AccessibilityNodeInfo createChildNodeInfo(int i) {
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
        obtain.setEnabled(true);
        obtain.setClassName(DEFAULT_CLASS_NAME);
        obtain.setPackageName(this.hostView.getContext().getPackageName());
        obtain.setSource(this.hostView, i);
        obtain.setImportantForAccessibility(true);
        AccessibilityViewInfo queryAccessibilityViewInfoById = queryAccessibilityViewInfoById(i, this.mapKey);
        fillNodeInfo(i, obtain, queryAccessibilityViewInfoById);
        if (this.hostView.getTouchDelegate() != null) {
            obtain.setTouchDelegateInfo(this.hostView.getTouchDelegate().getTouchDelegateInfo());
        }
        onPopulateNodeInfo(queryAccessibilityViewInfoById, obtain);
        if (this.focusedViewId == i) {
            obtain.setAccessibilityFocused(true);
            obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        } else {
            obtain.setAccessibilityFocused(false);
            obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
        }
        return obtain;
    }

    private void fillNodeInfo(int i, AccessibilityNodeInfo accessibilityNodeInfo, AccessibilityViewInfo accessibilityViewInfo) {
        if (accessibilityViewInfo != null) {
            accessibilityNodeInfo.setText(accessibilityViewInfo.getText());
            accessibilityNodeInfo.setContentDescription(accessibilityViewInfo.getDescription());
            accessibilityNodeInfo.setViewIdResourceName(accessibilityViewInfo.getResourceName());
            accessibilityNodeInfo.setCheckable(accessibilityViewInfo.isCheckable());
            accessibilityNodeInfo.setClickable(accessibilityViewInfo.isClickable());
            accessibilityNodeInfo.setFocusable(accessibilityViewInfo.isFocusable());
            accessibilityNodeInfo.setLongClickable(accessibilityViewInfo.isLongClickable());
            accessibilityNodeInfo.setScrollable(accessibilityViewInfo.isScrollable());
            accessibilityNodeInfo.setChecked(accessibilityViewInfo.isChecked());
            accessibilityNodeInfo.setEnabled(accessibilityViewInfo.isEnabled());
            accessibilityNodeInfo.setEditable(accessibilityViewInfo.isEditable());
            accessibilityNodeInfo.setFocused(accessibilityViewInfo.isFocused());
            accessibilityNodeInfo.setSelected(accessibilityViewInfo.isSelected());
            accessibilityNodeInfo.setMultiLine(accessibilityViewInfo.isMultiLine());
            accessibilityNodeInfo.setPassword(accessibilityViewInfo.isPassword());
            accessibilityNodeInfo.setShowingHintText(accessibilityViewInfo.isShowingHintText());
            accessibilityNodeInfo.setTextSelection(accessibilityViewInfo.getTextSelectionStart(), accessibilityViewInfo.getTextSelectionEnd());
            accessibilityNodeInfo.setHintText(accessibilityViewInfo.getHintText());
            accessibilityNodeInfo.setError(accessibilityViewInfo.getError());
            accessibilityNodeInfo.setVisibleToUser(accessibilityViewInfo.isVisible());
            if (i % 100000 == 10000) {
                accessibilityNodeInfo.setParent(this.hostView, i / 100000);
            } else {
                accessibilityNodeInfo.setParent(this.hostView, accessibilityViewInfo.getParentId());
            }
            populateRangInfoForNode(accessibilityViewInfo, accessibilityNodeInfo);
            populateListInfoForNode(accessibilityViewInfo, accessibilityNodeInfo);
            populateListItemInfoForNode(accessibilityViewInfo, accessibilityNodeInfo);
            populateChildrenForNode(accessibilityViewInfo, accessibilityNodeInfo);
            populateRectForNode(accessibilityNodeInfo, accessibilityViewInfo);
        }
    }

    private void populateRangInfoForNode(AccessibilityViewInfo accessibilityViewInfo, AccessibilityNodeInfo accessibilityNodeInfo) {
        AccessibilityViewInfo.ProgressInfo progressInfo = accessibilityViewInfo.getProgressInfo();
        if (progressInfo != null) {
            accessibilityNodeInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(0, (float) progressInfo.getMin(), (float) progressInfo.getMax(), (float) progressInfo.getValue()));
        }
    }

    private void populateListInfoForNode(AccessibilityViewInfo accessibilityViewInfo, AccessibilityNodeInfo accessibilityNodeInfo) {
        AccessibilityViewInfo.ListInfo listInfo = accessibilityViewInfo.getListInfo();
        if (listInfo != null && listInfo.getRowCount() > 1) {
            accessibilityNodeInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(listInfo.getRowCount(), listInfo.getColumnCount(), false, 0));
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
            accessibilityNodeInfo.setScrollable(true);
        }
    }

    private void populateListItemInfoForNode(AccessibilityViewInfo accessibilityViewInfo, AccessibilityNodeInfo accessibilityNodeInfo) {
        AccessibilityViewInfo.ListItemInfo listItemInfo = accessibilityViewInfo.getListItemInfo();
        if (listItemInfo != null && listItemInfo.getRowIndex() > 0) {
            accessibilityNodeInfo.setCollectionItemInfo(AccessibilityNodeInfo.CollectionItemInfo.obtain(listItemInfo.getRowIndex(), 1, listItemInfo.getColumnIndex(), 1, listItemInfo.isHeading(), listItemInfo.isSelected()));
        }
    }

    private void populateChildrenForNode(AccessibilityViewInfo accessibilityViewInfo, AccessibilityNodeInfo accessibilityNodeInfo) {
        int[] childIdList = accessibilityViewInfo.getChildIdList();
        if (childIdList.length > 0) {
            for (int i = 0; i < childIdList.length; i++) {
                if (childIdList[i] >= 0) {
                    accessibilityNodeInfo.addChild(this.hostView, childIdList[i]);
                }
            }
        } else if (getChildHelperByViewId(accessibilityViewInfo.getId()) != null) {
            accessibilityNodeInfo.addChild(this.hostView, (accessibilityViewInfo.getId() * 100000) + 10000);
        }
    }

    private void populateRectForNode(AccessibilityNodeInfo accessibilityNodeInfo, AccessibilityViewInfo accessibilityViewInfo) {
        Rect rect = accessibilityViewInfo.getRect();
        accessibilityNodeInfo.setBoundsInParent(new android.graphics.Rect(rect.left, rect.top, rect.right, rect.bottom));
        accessibilityNodeInfo.setBoundsInScreen(getRectOnScreen(rect));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean performAction(int i, int i2, Bundle bundle) {
        if (i == -1) {
            return performHostAction(i2, bundle);
        }
        BarrierFreeDelegateHelper childHelperByAceId = getChildHelperByAceId(i);
        if (childHelperByAceId != null) {
            return childHelperByAceId.performChildAction(i, i2, bundle);
        }
        return performChildAction(i, i2, bundle);
    }

    private boolean performHostAction(int i, Bundle bundle) {
        return this.hostView.performAccessibilityAction(i, bundle);
    }

    private boolean performChildAction(int i, int i2, Bundle bundle) {
        boolean onPerformActionForVirtualView = onPerformActionForVirtualView(i, i2, bundle);
        if (i2 == 64) {
            return requestAccessibilityFocus(i);
        }
        if (i2 != 128) {
            return onPerformActionForVirtualView;
        }
        return clearAccessibilityFocus(i);
    }

    private boolean requestAccessibilityFocus(int i) {
        if (!isAccessibilityManagerEnabled()) {
            LogUtil.error(TAG, "requestAccessibilityFocus failed, accessibility manager is not enable.");
            return false;
        } else if (isAccessibilityFocused(i)) {
            return false;
        } else {
            int i2 = this.focusedViewId;
            if (i2 != Integer.MIN_VALUE) {
                sendAccessibilityEvent(i2, 65536);
            }
            this.focusedViewId = i;
            this.hostView.invalidate();
            sendAccessibilityEvent(i, 32768);
            return true;
        }
    }

    private boolean clearAccessibilityFocus(int i) {
        if (!isAccessibilityFocused(i)) {
            return false;
        }
        this.focusedViewId = Integer.MIN_VALUE;
        this.hostView.invalidate();
        sendAccessibilityEvent(i, 65536);
        return true;
    }

    private boolean isAccessibilityFocused(int i) {
        return this.focusedViewId == i;
    }

    private AccessibilityViewInfo getClickedAccessibilityView(AccessibilityViewInfo accessibilityViewInfo, int i, int i2) {
        BarrierFreeDelegateHelper barrierFreeDelegateHelper;
        AccessibilityViewInfo accessibilityViewInfo2;
        Rect rect = accessibilityViewInfo.getRect();
        android.graphics.Rect rectOnScreen = getRectOnScreen(rect);
        AccessibilityViewInfo accessibilityViewInfo3 = null;
        if (accessibilityViewInfo.getId() > 0 && !rectOnScreen.contains(i, i2)) {
            return null;
        }
        int[] childIdList = accessibilityViewInfo.getChildIdList();
        LogUtil.info(TAG, "getClickedAccessibilityView clickId: " + accessibilityViewInfo.getId() + " groupRegion:" + rectOnScreen.toString() + " rect:" + rect + " childSize:" + childIdList.length);
        if (childIdList.length == 0) {
            barrierFreeDelegateHelper = getChildHelperByViewId(accessibilityViewInfo.getId());
            if (barrierFreeDelegateHelper != null) {
                accessibilityViewInfo3 = barrierFreeDelegateHelper.queryAccessibilityViewInfoById(accessibilityViewInfo.getId() * 100000, barrierFreeDelegateHelper.mapKey);
                childIdList = accessibilityViewInfo3.getChildIdList();
                LogUtil.info(TAG, "getClickedAccessibilityView clickId: " + accessibilityViewInfo.getId() + " childSize: " + childIdList.length);
            }
        } else {
            barrierFreeDelegateHelper = null;
        }
        if (childIdList.length > 0) {
            int length = childIdList.length - 1;
            while (true) {
                if (length < 0) {
                    break;
                }
                int i3 = childIdList[length];
                if (i3 <= 0) {
                    LogUtil.error(TAG, "Child id is not right, just skip.");
                } else {
                    if (barrierFreeDelegateHelper == null) {
                        accessibilityViewInfo2 = queryAccessibilityViewInfoById(i3, this.mapKey);
                        if (accessibilityViewInfo2 == null) {
                            LogUtil.error(TAG, "getVirtualViewAt can not find view:" + i3);
                        }
                    } else {
                        accessibilityViewInfo2 = barrierFreeDelegateHelper.queryAccessibilityViewInfoById(i3, barrierFreeDelegateHelper.mapKey);
                        if (accessibilityViewInfo2 == null || accessibilityViewInfo2.getId() == 0) {
                            LogUtil.error(TAG, "childHepler getVirtualViewAt can not find view:" + i3);
                        }
                    }
                    accessibilityViewInfo3 = getAccessibilityViewInfo(i, i2, barrierFreeDelegateHelper, accessibilityViewInfo2);
                    if (accessibilityViewInfo3 != null) {
                        LogUtil.info(TAG, "getVirtualViewAt end, tempViewInfo:" + accessibilityViewInfo3.getId());
                        break;
                    }
                }
                length--;
            }
        }
        return getResultViewInfo(accessibilityViewInfo, accessibilityViewInfo3);
    }

    private AccessibilityViewInfo getAccessibilityViewInfo(int i, int i2, BarrierFreeDelegateHelper barrierFreeDelegateHelper, AccessibilityViewInfo accessibilityViewInfo) {
        if (barrierFreeDelegateHelper == null) {
            return getClickedAccessibilityView(accessibilityViewInfo, i, i2);
        }
        return barrierFreeDelegateHelper.getClickedAccessibilityView(accessibilityViewInfo, i, i2);
    }

    private android.graphics.Rect getRectOnScreen(Rect rect) {
        int[] iArr = new int[2];
        View view = this.hostView;
        if (view != null) {
            view.getLocationOnScreen(iArr);
        }
        android.graphics.Rect rect2 = new android.graphics.Rect(rect.left, rect.top, rect.right, rect.bottom);
        rect2.offset(iArr[0], iArr[1]);
        return rect2;
    }

    private class BarrierFreeDelegateProvider extends AccessibilityNodeProvider {
        private BarrierFreeDelegateProvider() {
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
            LogUtil.debug(BarrierFreeDelegateHelper.TAG, "createAccessibilityNodeInfo id:" + i);
            return BarrierFreeDelegateHelper.this.createNode(i);
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            LogUtil.debug(BarrierFreeDelegateHelper.TAG, "performAction id:" + i + " action:" + i2);
            return BarrierFreeDelegateHelper.this.performAction(i, i2, bundle);
        }
    }
}
