package ohos.agp.components.webengine.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.concurrent.atomic.AtomicBoolean;
import ohos.aafwk.utils.log.LogDomain;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class FullScreenViewControllerBridge {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "FullScreenViewControllerBridge");
    private Context mAndroidContext;
    private View mAndroidView;
    private ohos.app.Context mContext;
    private final AtomicBoolean mIsFullScreen = new AtomicBoolean(false);

    public FullScreenViewControllerBridge(ohos.app.Context context, View view) {
        if (context == null || view == null) {
            HiLog.error(TAG, "FullScreenViewControllerBridge param error", new Object[0]);
            return;
        }
        this.mAndroidView = view;
        this.mContext = context;
        Object hostContext = context.getHostContext();
        if (hostContext instanceof Context) {
            this.mAndroidContext = (Context) hostContext;
        }
    }

    public void enterFullScreen() {
        if (this.mAndroidView == null || this.mContext == null) {
            HiLog.error(TAG, "enterFullScreen param error", new Object[0]);
        } else if (this.mIsFullScreen.get()) {
            HiLog.info(TAG, "already fullscreen now", new Object[0]);
        } else {
            this.mIsFullScreen.compareAndSet(false, true);
            ViewGroup.LayoutParams layoutParams = this.mAndroidView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = -1;
                layoutParams.width = -1;
            } else {
                layoutParams = new ViewGroup.LayoutParams(-1, -1);
            }
            this.mAndroidView.setLayoutParams(layoutParams);
            addToWindow();
        }
    }

    public void exitFullScreen() {
        if (this.mAndroidView == null) {
            HiLog.error(TAG, "exitFullScreen,param is null", new Object[0]);
        } else if (!this.mIsFullScreen.get()) {
            HiLog.info(TAG, "no need to exit", new Object[0]);
        } else {
            this.mIsFullScreen.compareAndSet(true, false);
            Context context = this.mAndroidContext;
            if (context instanceof Activity) {
                View findViewById = ((Activity) context).getWindow().getDecorView().findViewById(16908290);
                if (findViewById instanceof FrameLayout) {
                    ((FrameLayout) findViewById).removeView(this.mAndroidView);
                }
            }
        }
    }

    private void addToWindow() {
        Context context = this.mAndroidContext;
        if (context == null || this.mAndroidView == null) {
            HiLog.error(TAG, "addToWindow param error", new Object[0]);
        } else if (context instanceof Activity) {
            View findViewById = ((Activity) context).getWindow().getDecorView().findViewById(16908290);
            if (findViewById instanceof FrameLayout) {
                ((FrameLayout) findViewById).addView(this.mAndroidView);
            }
        }
    }
}
