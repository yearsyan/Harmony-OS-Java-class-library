package ohos.agp.window.wmc;

import android.content.Context;
import android.graphics.Bitmap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.utils.DFXPerformanceTracker;
import ohos.agp.utils.Rect;
import ohos.agp.window.aspbshell.AppInfoGetter;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.inner.ImageDoubleFwConverter;

public class AGPWindowManager {
    public static final int FLAG_ANDROID_CONTEXT = 10;
    public static final int FLAG_DIALOG_COMMON = 2;
    public static final int FLAG_DIALOG_INPUTMETHOD = 8;
    public static final int FLAG_DIALOG_LIST = 3;
    public static final int FLAG_DIALOG_POPUP = 4;
    public static final int FLAG_DIALOG_PRESENTATION = 7;
    public static final int FLAG_DIALOG_TOAST = 5;
    public static final int FLAG_WINDOW_CUSTOM = 6;
    public static final int FLAG_WINDOW_NORMAL = 1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "AGPWindow");
    private static final Object LOCK = new Object();
    private static volatile AGPWindowManager sInstance;
    private ArrayList<AGPWindow> mWindows = new ArrayList<>();

    public static int getAndroidGravity(int i) {
        if (i == 4) {
            return 3;
        }
        if (i == 8) {
            return 1;
        }
        if (i == 16) {
            return 5;
        }
        if (i == 32) {
            return 48;
        }
        if (i == 64) {
            return 16;
        }
        if (i == 72) {
            return 17;
        }
        if (i != 128) {
            return i;
        }
        return 80;
    }

    public static int getZidaneTextAlignment(int i) {
        if (i == 1) {
            return 8;
        }
        if (i == 3) {
            return 4;
        }
        if (i == 5) {
            return 16;
        }
        if (i == 48) {
            return 32;
        }
        if (i == 80) {
            return 128;
        }
        if (i == 16) {
            return 64;
        }
        if (i != 17) {
            return i;
        }
        return 72;
    }

    static {
        System.loadLibrary("agpwindow.z");
        System.loadLibrary("agp.z");
    }

    private AGPWindowManager() {
    }

    public static AGPWindowManager getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (AGPWindowManager.class) {
            if (sInstance == null) {
                sInstance = new AGPWindowManager();
            }
        }
        return sInstance;
    }

    public AGPWindow createWindowForAndroid(Context context) {
        AGPWindow aGPWindow = new AGPWindow(context);
        addWindow(aGPWindow);
        return aGPWindow;
    }

    public AGPWindow createWindow(ohos.app.Context context) {
        Object hostContext = context.getHostContext();
        if (hostContext instanceof Context) {
            AppInfoGetter.setAppNameToNative(((Context) hostContext).getApplicationContext());
        } else {
            HiLog.error(LABEL, "AGPWindow createWindow() is not android content instance", new Object[0]);
        }
        DFXPerformanceTracker instance = DFXPerformanceTracker.getInstance();
        instance.dfxBegin("AGPWindowManager.getInstance().createWindow");
        AGPWindow aGPWindow = new AGPWindow(context);
        addWindow(aGPWindow);
        instance.dfxEnd("AGPWindowManager.getInstance().createWindow");
        return aGPWindow;
    }

    public AGPWindow createWindow(ohos.app.Context context, int i, int i2) {
        AGPCustomWindow aGPCustomWindow = new AGPCustomWindow(context, i, i2);
        addWindow(aGPCustomWindow);
        return aGPCustomWindow;
    }

    public AGPBaseDialogWindow createDialogWindow(ohos.app.Context context, int i) {
        AGPBaseDialogWindow aGPBaseDialogWindow;
        boolean z = false;
        HiLog.debug(LABEL, "AGPWindowManager createDialogWindow flag=%{public}%d", Integer.valueOf(i));
        if (i == 4) {
            aGPBaseDialogWindow = new AGPPopupDialogWindow(context, i);
        } else if (i == 5) {
            aGPBaseDialogWindow = new AGPToastWindow(context, i);
            z = true;
        } else if (i != 7) {
            aGPBaseDialogWindow = new AGPCommonDialogWindow(context, i);
        } else {
            aGPBaseDialogWindow = new AGPPresentationWindow(context, i);
        }
        if (!z) {
            addWindow(aGPBaseDialogWindow);
        }
        return aGPBaseDialogWindow;
    }

    public void onWindowShow(AGPWindow aGPWindow) {
        if (aGPWindow == null) {
            HiLog.error(LABEL, "AGPWindowManager onWindowShow failed due to window is null", new Object[0]);
        } else if (aGPWindow.mFlag != 5) {
            AGPWindow topShowingWindow = getTopShowingWindow(aGPWindow);
            if (topShowingWindow != null) {
                topShowingWindow.unRegisterBarrierFree();
            }
            aGPWindow.notifyBarrierFree();
        }
    }

    public void onWindowHide(AGPWindow aGPWindow) {
        if (aGPWindow == null) {
            HiLog.error(LABEL, "AGPWindowManager onWindowHide failed due to window is null", new Object[0]);
            return;
        }
        aGPWindow.unRegisterBarrierFree();
        AGPWindow topShowingWindow = getTopShowingWindow(aGPWindow);
        if (topShowingWindow != null) {
            topShowingWindow.notifyBarrierFree();
        }
    }

    public void destroyWindow(AGPWindow aGPWindow) {
        if (aGPWindow == null) {
            HiLog.error(LABEL, "AGPWindowManager destroyWindow failed due to window is null", new Object[0]);
            return;
        }
        synchronized (LOCK) {
            removeWindow(aGPWindow);
            aGPWindow.destroy();
        }
    }

    public Optional<AGPWindow> getTopWindow() {
        synchronized (LOCK) {
            int size = this.mWindows.size();
            if (size > 0) {
                HiLog.debug(LABEL, "AGPWindowManager getTopWindow size=%{public}d", Integer.valueOf(size));
                return Optional.of(this.mWindows.get(size - 1));
            }
            HiLog.error(LABEL, "AGPWindowManager getTopWindow return null", new Object[0]);
            return Optional.empty();
        }
    }

    public int windowsCount() {
        int size;
        synchronized (LOCK) {
            size = this.mWindows.size();
        }
        return size;
    }

    public void addView(AGPWindow aGPWindow) {
        if (aGPWindow != null) {
            aGPWindow.show();
        }
    }

    private void addWindow(AGPWindow aGPWindow) {
        if (aGPWindow == null) {
            HiLog.error(LABEL, "AGPWindowManager addWindow failed due to window is null", new Object[0]);
            return;
        }
        synchronized (LOCK) {
            this.mWindows.add(aGPWindow);
        }
    }

    private void removeWindow(AGPWindow aGPWindow) {
        if (aGPWindow == null) {
            HiLog.error(LABEL, "AGPWindowManager removeWindow failed due to window is null", new Object[0]);
        } else if (this.mWindows.contains(aGPWindow)) {
            this.mWindows.remove(aGPWindow);
            onWindowChange(aGPWindow);
        }
    }

    private void onWindowChange(AGPWindow aGPWindow) {
        AGPWindow aGPWindow2;
        HiLog.debug(LABEL, "AGPWindowManager onWindowChange", new Object[0]);
        int size = this.mWindows.size();
        if (size > 0) {
            HiLog.debug(LABEL, "AGPWindowManager getTopWindow size=%{public}d", Integer.valueOf(size));
            aGPWindow2 = getTopShowingWindow(aGPWindow);
        } else {
            AGPWindow.releaseBarrierFree();
            aGPWindow2 = null;
        }
        if (aGPWindow != null) {
            aGPWindow.unRegisterBarrierFree();
        }
        if (aGPWindow2 == null) {
            HiLog.debug(LABEL, "not fond window.", new Object[0]);
        } else {
            aGPWindow2.notifyBarrierFree();
        }
    }

    private AGPWindow getTopShowingWindow(AGPWindow aGPWindow) {
        synchronized (LOCK) {
            int size = this.mWindows.size();
            AGPWindow aGPWindow2 = null;
            if (size <= 0) {
                return null;
            }
            int i = 0;
            HiLog.debug(LABEL, "AGPWindowManager getTopWindow size=%{public}d", Integer.valueOf(size));
            while (true) {
                if (i < size) {
                    AGPWindow aGPWindow3 = this.mWindows.get((size - 1) - i);
                    if (aGPWindow3 != aGPWindow && aGPWindow3.isShowing()) {
                        aGPWindow2 = aGPWindow3;
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            return aGPWindow2;
        }
    }

    public static class BadWindowException extends RuntimeException {
        private static final long serialVersionUID = -7459801466658443342L;

        public BadWindowException() {
            this(null);
        }

        public BadWindowException(String str) {
            super(str);
        }
    }

    public PixelMap captureScreen(Rect rect, int i, int i2, int i3) {
        HiLog.debug(LABEL, "AGPWindowManager captureScreen", new Object[0]);
        try {
            Class<?> cls = Class.forName("com.huawei.screenrecorder.activities.SurfaceControlEx");
            Method declaredMethod = cls.getDeclaredMethod("screenshot", android.graphics.Rect.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            declaredMethod.setAccessible(true);
            Bitmap bitmap = (Bitmap) declaredMethod.invoke(cls, convertRect(rect), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
            if (bitmap != null) {
                return ImageDoubleFwConverter.createShellPixelMap(bitmap.copy(Bitmap.Config.ARGB_8888, bitmap.isMutable()));
            }
            HiLog.debug(LABEL, "bitmap is null.", new Object[0]);
            return null;
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
            HiLog.error(LABEL, "exception: %{public}s", e.toString());
            return null;
        }
    }

    private android.graphics.Rect convertRect(Rect rect) {
        return new android.graphics.Rect(rect.left, rect.top, rect.right, rect.bottom);
    }
}
