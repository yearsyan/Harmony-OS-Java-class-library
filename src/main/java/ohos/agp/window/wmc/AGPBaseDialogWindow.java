package ohos.agp.window.wmc;

import android.content.res.Configuration;
import android.view.WindowManager;
import java.util.Optional;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.ComponentContainer;
import ohos.agp.utils.Point;
import ohos.agp.window.wmc.AGPWindow;
import ohos.agp.window.wmc.AGPWindowManager;
import ohos.agp.window.wmc.DisplayManagerWrapper;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AGPBaseDialogWindow extends AGPWindow {
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "AGPWindow");
    protected boolean isUserSetSize;
    protected WindowManager mAndroidWindowManager;
    protected int mDeviceHeight;
    protected int mDeviceWidth;
    protected DialogDestoryListener mDialogDestoryListener;
    protected int mHeight;
    protected DialogListener mListener;
    protected int mWidth;

    public interface DialogDestoryListener {
        void dialogDestroy();
    }

    public interface DialogListener {
        boolean isTouchOutside();
    }

    public AGPBaseDialogWindow(Context context, int i) {
        super(context, i);
        if (this.mAndroidContext != null) {
            Object systemService = this.mAndroidContext.getSystemService("window");
            if (systemService instanceof WindowManager) {
                this.mAndroidWindowManager = (WindowManager) systemService;
                this.isUserSetSize = false;
                getDisplaySize(context);
                return;
            }
            throw new AGPWindowManager.BadWindowException("Obj getSystemService is not instance of WindowManager");
        }
        throw new AGPWindowManager.BadWindowException("AGPBaseDialogWindow: mAndroidContext is null.");
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.mListener = dialogListener;
    }

    public void setSize(int i, int i2, boolean z) {
        HiLog.debug(LABEL, "setSize width = %{private}d; height = %{private}d", Integer.valueOf(i), Integer.valueOf(i2));
        if (i < -2 || i == 0 || i2 < -2 || i2 == 0) {
            HiLog.error(LABEL, "setSize() Invalied size.", new Object[0]);
            return;
        }
        this.mWidth = i;
        this.mHeight = i2;
        this.isUserSetSize = z;
    }

    public void setDialogSize(int i, int i2) {
        HiLog.debug(LABEL, "enter setDialogSize", new Object[0]);
        if (this.mAndroidParam == null) {
            HiLog.error(LABEL, "setDialogSize failed for mAndroidParam is null", new Object[0]);
            return;
        }
        this.mAndroidParam.width = i;
        this.mAndroidParam.height = i2;
        this.mAndroidParam.flags |= 1024;
    }

    public void setDialogDestoryListener(DialogDestoryListener dialogDestoryListener) {
        this.mDialogDestoryListener = dialogDestoryListener;
    }

    /* access modifiers changed from: protected */
    public void getDisplaySize(Context context) {
        Optional<DisplayManagerWrapper.DisplayWrapper> defaultDisplay = DisplayManagerWrapper.getInstance().getDefaultDisplay(context);
        if (!defaultDisplay.isPresent()) {
            HiLog.error(LABEL, "AGPBaseDialogWindow defaultDisplay is empty.", new Object[0]);
            return;
        }
        Point point = new Point();
        defaultDisplay.get().getSize(point);
        this.mDeviceWidth = (int) point.position[0];
        this.mDeviceHeight = (int) point.position[1];
    }

    @Override // ohos.agp.window.wmc.AGPWindow
    public void onSizeChange(Configuration configuration) {
        int i;
        int i2;
        boolean z = false;
        HiLog.debug(LABEL, "onSizeChange.", new Object[0]);
        super.onSizeChange(configuration);
        ComponentContainer windowRoot = getWindowRoot();
        if (windowRoot == null) {
            HiLog.error(LABEL, "onSizeChange viewGroup is empty.", new Object[0]);
            return;
        }
        getDisplaySize(this.mContext);
        AGPWindow.LayoutParams attributes = getAttributes();
        if (attributes == null || this.mAndroidWindow == null) {
            HiLog.error(LABEL, "params is null or mAndroidWindow is null.", new Object[0]);
            return;
        }
        WindowManager.LayoutParams attributes2 = this.mAndroidWindow.getAttributes();
        int i3 = this.mWidth;
        int i4 = i3 == -1 || i3 == -2 || attributes2.width < 0 ? this.mDeviceWidth : attributes2.width;
        int i5 = this.mHeight;
        int i6 = i5 == -1 || i5 == -2 || attributes2.height < 0 ? this.mDeviceHeight : attributes2.height;
        HiLog.debug(LABEL, "onSizeChange, mWidth = %{private}d, mHeight = %{private}d; myParam.width = %{private}d, myParam.height = %{private}d; preWindowWidth = %{private}d, preWindowHeight = %{private}d", Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), Integer.valueOf(attributes2.width), Integer.valueOf(attributes2.height), Integer.valueOf(i4), Integer.valueOf(i6));
        super.setPreContentLayout(windowRoot, i4, i6);
        if ((!this.isUserSetSize || (i2 = this.mWidth) == -1 || i2 == -2) && attributes.width != windowRoot.getWidth()) {
            attributes2.width = windowRoot.getWidth();
            z = true;
        }
        if ((!this.isUserSetSize || (i = this.mHeight) == -1 || i == -2) && attributes.height != windowRoot.getHeight()) {
            attributes2.height = windowRoot.getHeight();
            z = true;
        }
        if (z) {
            this.mAndroidWindow.setAttributes(attributes2);
        }
    }
}
