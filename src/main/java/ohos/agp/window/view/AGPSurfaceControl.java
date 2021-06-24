package ohos.agp.window.view;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.SystemProperties;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.Window;
import java.util.ArrayList;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.window.wmc.AGPWindowManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AGPSurfaceControl {
    private static final boolean BLUR_FEATURE_ENABLED;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "AGPSurfaceControl");
    private Window mAndroidWindow;

    static {
        boolean z = false;
        if (SystemProperties.getInt("emui_hwagp_window_blur_effect_enable", 0) != 0) {
            z = true;
        }
        BLUR_FEATURE_ENABLED = z;
    }

    public static boolean getBlurFeatureEnabled() {
        return BLUR_FEATURE_ENABLED;
    }

    public AGPSurfaceControl(Window window) {
        if (window != null) {
            View peekDecorView = window.peekDecorView();
            if (peekDecorView != null) {
                if (peekDecorView.getViewRootImpl() == null) {
                    HiLog.error(LABEL, "AGPSurfaceControl: window =%{public}s", String.valueOf(window));
                    HiLog.error(LABEL, "AGPSurfaceControl: ViewRootImpl is null decorview =%{public}s", String.valueOf(peekDecorView));
                }
                this.mAndroidWindow = window;
                return;
            }
            HiLog.error(LABEL, "AGPSurfaceControl:decorview is null, window =%{public}s", String.valueOf(window));
            throw new AGPWindowManager.BadWindowException("AGPSurfaceControl: decorview is null");
        }
        HiLog.error(LABEL, "AGPSurfaceControl: window is null", new Object[0]);
        throw new AGPWindowManager.BadWindowException("AGPSurfaceControl: window is null");
    }

    public void setBlurMode(int i) {
        if (getBlurFeatureEnabled()) {
            SurfaceControl validSurfaceControl = getValidSurfaceControl();
            if (validSurfaceControl != null) {
                validSurfaceControl.setBlurMode(i);
            } else {
                HiLog.error(LABEL, "setBlurMode: failed.", new Object[0]);
            }
        }
    }

    public void setBlurEnabled(boolean z) {
        if (getBlurFeatureEnabled()) {
            SurfaceControl validSurfaceControl = getValidSurfaceControl();
            if (validSurfaceControl != null) {
                validSurfaceControl.setBlurEnabled(z);
            } else {
                HiLog.error(LABEL, "setBlurEnabled: failed.", new Object[0]);
            }
        }
    }

    public void setBlurEnabledForViewRoot(boolean z) {
        if (getBlurFeatureEnabled()) {
            View peekDecorView = this.mAndroidWindow.peekDecorView();
            if (peekDecorView == null) {
                HiLog.error(LABEL, "AGPSurfaceControl: decorview is null, androidWindow =%{public}s", String.valueOf(this.mAndroidWindow));
                return;
            }
            ViewRootImpl viewRootImpl = peekDecorView.getViewRootImpl();
            if (viewRootImpl == null) {
                HiLog.error(LABEL, "AGPSurfaceControl: viewRootImpl is null, decorview =%{public}s", String.valueOf(peekDecorView));
                return;
            }
            viewRootImpl.setBlurEnabled(z);
        }
    }

    public void setBehindLayerBlurStyle(int i) {
        if (getBlurFeatureEnabled()) {
            SurfaceControl validSurfaceControl = getValidSurfaceControl();
            if (validSurfaceControl != null) {
                validSurfaceControl.setBehindLayerBlurStyle(i);
            } else {
                HiLog.error(LABEL, "setBehindLayerBlurStyle: failed.", new Object[0]);
            }
        }
    }

    public void setFrontLayerBlurStyle(int i) {
        if (getBlurFeatureEnabled()) {
            SurfaceControl validSurfaceControl = getValidSurfaceControl();
            if (validSurfaceControl != null) {
                validSurfaceControl.setFrontLayerBlurStyle(i);
            } else {
                HiLog.error(LABEL, "setFrontLayerBlurStyle: failed.", new Object[0]);
            }
        }
    }

    public void setBlurProgress(float f) {
        if (getBlurFeatureEnabled()) {
            SurfaceControl validSurfaceControl = getValidSurfaceControl();
            if (validSurfaceControl != null) {
                validSurfaceControl.setBlurProgress(f);
            } else {
                HiLog.error(LABEL, "setBlurProgress: failed.", new Object[0]);
            }
        }
    }

    public void setBlurCacheEnabled(boolean z) {
        if (getBlurFeatureEnabled()) {
            SurfaceControl validSurfaceControl = getValidSurfaceControl();
            if (validSurfaceControl != null) {
                validSurfaceControl.setBlurCacheEnabled(z);
            } else {
                HiLog.error(LABEL, "setBlurCacheEnabled: failed.", new Object[0]);
            }
        }
    }

    public void setBlurRegion(Region region) {
        if (getBlurFeatureEnabled()) {
            View peekDecorView = this.mAndroidWindow.peekDecorView();
            if (peekDecorView == null) {
                HiLog.error(LABEL, "AGPSurfaceControl: decorview is null, androidWindow =%{public}s", String.valueOf(this.mAndroidWindow));
                return;
            }
            ViewRootImpl viewRootImpl = peekDecorView.getViewRootImpl();
            if (viewRootImpl == null) {
                HiLog.error(LABEL, "AGPSurfaceControl: viewRootImpl is null, decorview =%{public}s", String.valueOf(peekDecorView));
                return;
            }
            Surface surface = viewRootImpl.mSurface;
            if (surface == null || !surface.isValid()) {
                HiLog.error(LABEL, "surface is null or not vaild", new Object[0]);
            } else {
                surface.setBlurRegion(region);
            }
        }
    }

    public void setViewParam(ArrayList<Rect> arrayList, float[] fArr, int i) {
        if (getBlurFeatureEnabled()) {
            View peekDecorView = this.mAndroidWindow.peekDecorView();
            if (peekDecorView == null) {
                HiLog.error(LABEL, "AGPSurfaceControl: decorview is null, androidWindow =%{public}s", String.valueOf(this.mAndroidWindow));
                return;
            }
            ViewRootImpl viewRootImpl = peekDecorView.getViewRootImpl();
            if (viewRootImpl == null) {
                HiLog.error(LABEL, "AGPSurfaceControl: viewRootImpl is null, decorview =%{public}s", String.valueOf(peekDecorView));
                return;
            }
            Surface surface = viewRootImpl.mSurface;
            if (surface == null || !surface.isValid()) {
                HiLog.error(LABEL, "surface is not vaild", new Object[0]);
            } else {
                surface.setViewParam(arrayList, fArr, i);
            }
        }
    }

    public void openTransaction() {
        SurfaceControl.openTransaction();
    }

    public void closeTransaction() {
        SurfaceControl.closeTransaction();
    }

    private SurfaceControl getValidSurfaceControl() {
        View peekDecorView = this.mAndroidWindow.peekDecorView();
        if (peekDecorView == null) {
            HiLog.error(LABEL, "AGPSurfaceControl: decorview is null, androidWindow =%{public}s", String.valueOf(this.mAndroidWindow));
            return null;
        }
        ViewRootImpl viewRootImpl = peekDecorView.getViewRootImpl();
        if (viewRootImpl == null) {
            HiLog.error(LABEL, "AGPSurfaceControl: viewRootImpl is null, decorview =%{public}s", String.valueOf(peekDecorView));
            return null;
        }
        SurfaceControl surfaceControl = viewRootImpl.getSurfaceControl();
        if (surfaceControl != null && surfaceControl.isValid()) {
            return surfaceControl;
        }
        HiLog.error(LABEL, "AGPSurfaceControl: SurfaceControl is null or invalid, viewRoot =%{public}s", String.valueOf(viewRootImpl));
        return null;
    }
}
