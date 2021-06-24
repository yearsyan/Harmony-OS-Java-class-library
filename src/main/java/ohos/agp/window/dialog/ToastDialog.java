package ohos.agp.window.dialog;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.window.service.WindowManager;
import ohos.agp.window.wmc.AGPToastWindow;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ToastDialog extends CommonDialog {
    private static final long DEFAULT_TOAST_TIMEOUT = 2000;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "ToastDialog");
    private static final int MAX_TOASTS_IN_QUEUE = 50;
    private static final int TEXT_SIZE = 60;
    private static final int WIDTH_BASE = 3;
    private static final int WIDTH_SUM = 4;
    private static int sToastsInQueue = 0;
    private TaskDispatcher mTaskDispatcher;
    private int mToastGravity;
    private int mToastX;
    private int mToastY;

    public ToastDialog(Context context) {
        super(context);
        if (context != null) {
            this.mTaskDispatcher = context.getUITaskDispatcher();
            this.mFlag = 5;
            if (this.mDeviceWidth > 0) {
                this.mWidth = (this.mDeviceWidth * 3) / 4;
            }
            this.mHeight = -2;
            return;
        }
        throw new WindowManager.PermissionException("ToastDialog context is null.");
    }

    public ToastDialog setText(String str) {
        HiLog.debug(LABEL, "setText text = %{private}s", str);
        Text text = new Text(this.mContext);
        text.setText(str);
        text.setContentPosition(0.0f, 0.0f);
        text.setTextSize(60);
        text.setTextAlignment(72);
        text.setWidth(-1);
        text.setHeight(-2);
        setContentCustomComponent(text);
        setCornerRadius(15.0f);
        return this;
    }

    @Deprecated
    public ToastDialog setComponent(DirectionalLayout directionalLayout) {
        HiLog.debug(LABEL, "setView enter", new Object[0]);
        setContentCustomComponent(directionalLayout);
        return this;
    }

    public ToastDialog setComponent(Component component) {
        HiLog.debug(LABEL, "setView enter", new Object[0]);
        setContentCustomComponent(component);
        return this;
    }

    public Component getComponent() {
        HiLog.debug(LABEL, "getView", new Object[0]);
        return getContentCustomComponent();
    }

    public void cancel() {
        HiLog.debug(LABEL, "cancel toast", new Object[0]);
        if (this.mWindow != null && (this.mWindow instanceof AGPToastWindow)) {
            ((AGPToastWindow) this.mWindow).cancel();
        }
    }

    @Override // ohos.agp.window.dialog.BaseDialog
    public ToastDialog setAlignment(int i) {
        super.setAlignment(i);
        this.mToastGravity = i;
        return this;
    }

    @Override // ohos.agp.window.dialog.BaseDialog
    public ToastDialog setOffset(int i, int i2) {
        super.setOffset(i, i2);
        this.mToastX = i;
        this.mToastY = i2;
        return this;
    }

    @Override // ohos.agp.window.dialog.BaseDialog
    public ToastDialog setSize(int i, int i2) {
        if (i < -2 || i == 0 || i2 < -2 || i2 == 0) {
            HiLog.error(LABEL, "setSize() Invalied size.", new Object[0]);
            return this;
        }
        this.mWidth = i;
        this.mHeight = i2;
        this.mIsUserSetSize = true;
        return this;
    }

    @Override // ohos.agp.window.dialog.BaseDialog
    public ToastDialog setAutoClosable(boolean z) {
        HiLog.warn(LABEL, "Auto closing could not be applied for ToastDialog", new Object[0]);
        return this;
    }

    @Override // ohos.agp.window.dialog.BaseDialog
    public ToastDialog setDuration(int i) {
        HiLog.warn(LABEL, "Duration could not be set for ToastDialog", new Object[0]);
        return this;
    }

    @Override // ohos.agp.window.dialog.IDialog, ohos.agp.window.dialog.BaseDialog
    public void show() {
        int i = sToastsInQueue;
        if (i <= 50) {
            sToastsInQueue = i + 1;
            super.show();
            doTimeoutDestroy();
            return;
        }
        HiLog.warn(LABEL, "Max number of toasts in queue is reached. Not showing more", new Object[0]);
    }

    private void doTimeoutDestroy() {
        TaskDispatcher taskDispatcher = this.mTaskDispatcher;
        if (taskDispatcher != null) {
            taskDispatcher.delayDispatch(new Runnable() {
                /* class ohos.agp.window.dialog.$$Lambda$ToastDialog$tXhc1gBylYEcCQkoDOuuZv8zdsc */

                public final void run() {
                    ToastDialog.this.lambda$doTimeoutDestroy$0$ToastDialog();
                }
            }, ((long) sToastsInQueue) * DEFAULT_TOAST_TIMEOUT);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: callDestroy */
    public void lambda$doTimeoutDestroy$0$ToastDialog() {
        sToastsInQueue--;
        destroy();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.window.dialog.CommonDialog, ohos.agp.window.dialog.BaseDialog
    public void onCreate() {
        if (this.mWindow != null && (this.mWindow instanceof AGPToastWindow)) {
            ((AGPToastWindow) this.mWindow).setDefaultSize();
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.window.dialog.CommonDialog, ohos.agp.window.dialog.BaseDialog
    public void onShow() {
        super.onShow();
        if (this.mWidth > 0) {
            this.mLayout.setWidth(this.mWidth);
        }
        if (this.mHeight > 0) {
            this.mLayout.setHeight(this.mHeight);
        }
        if (this.mWindow != null && (this.mWindow instanceof AGPToastWindow)) {
            if (!(this.mToastX == 0 && this.mToastY == 0)) {
                ((AGPToastWindow) this.mWindow).setOffset(this.mToastX, this.mToastY);
            }
            if (this.mToastGravity != 0) {
                ((AGPToastWindow) this.mWindow).setGravity(this.mToastGravity);
            }
            if (this.mIsUserSetSize) {
                ((AGPToastWindow) this.mWindow).setSize(this.mWidth, this.mHeight);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.window.dialog.CommonDialog, ohos.agp.window.dialog.BaseDialog
    public void onDestroy() {
        super.onDestroy();
        cancel();
    }
}
