package ohos.accessibility.adapter;

import android.graphics.Rect;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AccessibilityViewDialogAdapter extends AccessibilityViewAbilityAdapter {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111232, "AccessibilityViewDialogAdapter");
    private View mDecorView;

    public AccessibilityViewDialogAdapter(Context context, View view, View view2, int i) {
        if (view == null) {
            HiLog.warn(LABEL, "create AccessibilityViewDialogAdapter error, contentView is null, just return.", new Object[0]);
        } else if (view2 == null) {
            HiLog.warn(LABEL, "create AccessibilityViewDialogAdapter error, decorView is null, just return.", new Object[0]);
        } else {
            this.hostView = view;
            this.mDecorView = view2;
            this.accessibilityManager = AccessibilityManager.getInstance(view.getContext());
            initBarrierFreeView(i);
        }
    }

    private void initBarrierFreeView(int i) {
        BarrierFreeDelegateHelper barrierFreeDelegateHelper;
        HiLog.info(LABEL, "initBarrierFreeView start. type: %{public}d", Integer.valueOf(i));
        if (i == 0) {
            barrierFreeDelegateHelper = new AccessibilityViewDelegate(this.hostView);
        } else if (i == 1) {
            barrierFreeDelegateHelper = new AccessibilityAceViewDelegate(this.hostView);
        } else {
            HiLog.warn(LABEL, "initBarrierFreeView failed, ability type is illegal.", new Object[0]);
            return;
        }
        this.delegateHelper = barrierFreeDelegateHelper;
        this.hostView.setAccessibilityDelegate(barrierFreeDelegateHelper);
        AdapterTouchDelegate adapterTouchDelegate = new AdapterTouchDelegate(new Rect(0, 0, 2147483646, 2147483646), this.hostView, barrierFreeDelegateHelper);
        for (View view : getAllChildViews(this.mDecorView)) {
            view.setTouchDelegate(adapterTouchDelegate);
        }
        this.mDecorView.setTouchDelegate(adapterTouchDelegate);
        HiLog.info(LABEL, "initBarrierFreeView end.", new Object[0]);
    }

    @Override // ohos.accessibility.adapter.AccessibilityViewAbilityAdapter
    public void releaseBarrierFreeView() {
        clearBarrierFreeFocus();
        if (this.hostView != null) {
            this.hostView.setAccessibilityDelegate(null);
            this.hostView = null;
        }
        View view = this.mDecorView;
        if (view != null) {
            view.setTouchDelegate(null);
            this.mDecorView = null;
        }
    }

    @Override // ohos.accessibility.adapter.AccessibilityViewAbilityAdapter
    public void clearBarrierFreeFocus() {
        if (this.hostView != null) {
            this.hostView.clearAccessibilityFocus();
            HiLog.info(LABEL, "clearBarrierFreeFocus successful.", new Object[0]);
        }
    }
}
