package ohos.agp.window.service;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;
import ohos.aafwk.utils.log.LogDomain;
import ohos.accessibility.AccessibilityEventInfo;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.WindowManager;
import ohos.agp.window.wmc.AGPCommonDialogWindow;
import ohos.agp.window.wmc.AGPWindow;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class Window {
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "Window");
    public AGPWindow agpWindow;
    private IAccessibilityEventListener mIPopAccListener;

    public interface IAccessibilityEventListener {
        void onDispatchEvent(AccessibilityEventInfo accessibilityEventInfo);
    }

    public Window() {
    }

    public Window(AGPWindow aGPWindow) {
        this.agpWindow = aGPWindow;
    }

    public boolean isMovable() {
        AGPWindow aGPWindow = this.agpWindow;
        if (aGPWindow == null) {
            return false;
        }
        return aGPWindow.isMovable();
    }

    public void setMovable(boolean z) {
        AGPWindow aGPWindow = this.agpWindow;
        if (aGPWindow != null) {
            aGPWindow.setMovable(z);
        }
    }

    public Rect getBoundRect() {
        AGPWindow aGPWindow = this.agpWindow;
        if (aGPWindow == null) {
            return new Rect(0, 0, 0, 0);
        }
        return aGPWindow.getBoundRect();
    }

    public void setBoundRect(Rect rect) {
        AGPWindow aGPWindow = this.agpWindow;
        if (aGPWindow != null) {
            aGPWindow.setBoundRect(rect);
        }
    }

    public Optional<WindowManager.LayoutConfig> getLayoutConfig() {
        checkWindow();
        AGPWindow.LayoutParams attributes = this.agpWindow.getAttributes();
        WindowManager.LayoutConfig layoutConfig = new WindowManager.LayoutConfig();
        if (attributes == null) {
            HiLog.error(LABEL, "Window getAttributes return null", new Object[0]);
            return Optional.empty();
        }
        layoutConfig.alpha = attributes.alpha;
        layoutConfig.dim = attributes.dimAmount;
        layoutConfig.windowBrightness = attributes.screenBrightness;
        layoutConfig.alignment = attributes.gravity;
        layoutConfig.height = attributes.height;
        layoutConfig.width = attributes.width;
        layoutConfig.type = attributes.type;
        layoutConfig.x = attributes.x;
        layoutConfig.y = attributes.y;
        layoutConfig.flags = attributes.flags;
        layoutConfig.title = attributes.title;
        layoutConfig.animations = attributes.windowAnimations;
        layoutConfig.layoutInDisplaySideMode = attributes.layoutInDisplaySideMode;
        layoutConfig.pixelFormat = attributes.format;
        return Optional.of(layoutConfig);
    }

    public void setLayoutConfig(WindowManager.LayoutConfig layoutConfig) {
        if (layoutConfig == null) {
            HiLog.error(LABEL, "Window can not set LayoutParm to null", new Object[0]);
            return;
        }
        checkWindow();
        AGPWindow.LayoutParams layoutParams = new AGPWindow.LayoutParams();
        layoutParams.alpha = layoutConfig.alpha;
        layoutParams.dimAmount = layoutConfig.dim;
        layoutParams.screenBrightness = layoutConfig.windowBrightness;
        layoutParams.gravity = layoutConfig.alignment;
        layoutParams.height = layoutConfig.height;
        layoutParams.width = layoutConfig.width;
        layoutParams.x = layoutConfig.x;
        layoutParams.y = layoutConfig.y;
        layoutParams.flags = layoutConfig.flags;
        layoutParams.title = layoutConfig.title;
        layoutParams.windowAnimations = layoutConfig.animations;
        layoutParams.layoutInDisplaySideMode = layoutConfig.layoutInDisplaySideMode;
        layoutParams.format = layoutConfig.pixelFormat;
        this.agpWindow.setAttributes(layoutParams);
    }

    public void setLayoutAlignment(int i) {
        getLayoutConfig().ifPresent(new Consumer(i) {
            /* class ohos.agp.window.service.$$Lambda$Window$K37FfD18nDKBD8xKEMoSvsfzgd0 */
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Window.this.lambda$setLayoutAlignment$0$Window(this.f$1, (WindowManager.LayoutConfig) obj);
            }
        });
    }

    public /* synthetic */ void lambda$setLayoutAlignment$0$Window(int i, WindowManager.LayoutConfig layoutConfig) {
        layoutConfig.alignment = i;
        setLayoutConfig(layoutConfig);
    }

    public void addFlags(int i) {
        setLayoutFlags(i, i);
    }

    public void clearFlags(int i) {
        setLayoutFlags(0, i);
    }

    public void setLayoutFlags(int i, int i2) {
        checkWindow();
        getLayoutConfig().ifPresent(new Consumer(i2, i) {
            /* class ohos.agp.window.service.$$Lambda$Window$nCvBPvLH92lHzTWHDILrsmXCEY */
            private final /* synthetic */ int f$1;
            private final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Window.this.lambda$setLayoutFlags$1$Window(this.f$1, this.f$2, (WindowManager.LayoutConfig) obj);
            }
        });
    }

    public /* synthetic */ void lambda$setLayoutFlags$1$Window(int i, int i2, WindowManager.LayoutConfig layoutConfig) {
        layoutConfig.flags = (i & i2) | (layoutConfig.flags & (~i));
        setLayoutConfig(layoutConfig);
    }

    public void setWindowLayout(int i, int i2) {
        getLayoutConfig().ifPresent(new Consumer(i, i2) {
            /* class ohos.agp.window.service.$$Lambda$Window$saGQN7djoJ8ahh7R2H7c5CNQS7E */
            private final /* synthetic */ int f$1;
            private final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Window.this.lambda$setWindowLayout$2$Window(this.f$1, this.f$2, (WindowManager.LayoutConfig) obj);
            }
        });
    }

    public /* synthetic */ void lambda$setWindowLayout$2$Window(int i, int i2, WindowManager.LayoutConfig layoutConfig) {
        layoutConfig.width = i;
        layoutConfig.height = i2;
        setLayoutConfig(layoutConfig);
    }

    public void setNavigationBarColor(int i) {
        checkWindow();
        this.agpWindow.setNavigationBarColor(i);
    }

    public void setStatusBarColor(int i) {
        checkWindow();
        this.agpWindow.setStatusBarColor(i);
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        checkWindow();
        this.agpWindow.setPadding(i, i2, i3, i4);
    }

    public void setStatusBarVisibility(int i) {
        checkWindow();
        this.agpWindow.setStatusBarVisibility(i);
    }

    public int getStatusBarVisibility() {
        checkWindow();
        return this.agpWindow.getStatusBarVisibility();
    }

    public Optional<Component> getCurrentComponentFocus() {
        checkWindow();
        ComponentContainer containerLayout = this.agpWindow.getContainerLayout();
        if (containerLayout != null) {
            return Optional.ofNullable(containerLayout.findFocus());
        }
        return Optional.empty();
    }

    private void checkWindow() {
        if (this.agpWindow == null) {
            HiLog.error(LABEL, "agpWindow is null", new Object[0]);
            throw new WindowManager.PermissionException("reason: agpWindow is null");
        }
    }

    public void setBackgroundColor(RgbColor rgbColor) {
        checkWindow();
        this.agpWindow.setBackgroundColor(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue());
    }

    public void setBackground(String str) {
        if (new File(str).exists()) {
            if (str.isEmpty()) {
                HiLog.error(LABEL, "Window can not set background to an empty element.", new Object[0]);
                return;
            }
            checkWindow();
            this.agpWindow.setBackground(str);
            HiLog.debug(LABEL, "Window call AGPWindow set background success.", new Object[0]);
        }
    }

    public void addWindowFlags(int i) {
        setWindowFlags(i, i);
    }

    public void clearWindowFlags(int i) {
        setWindowFlags(0, i);
    }

    public void setWindowFlags(int i, int i2) {
        checkWindow();
        AGPWindow aGPWindow = this.agpWindow;
        aGPWindow.setWindowFlag((i & i2) | (aGPWindow.getWindowFlag() & (~i2)));
    }

    public void setTransparent(boolean z) {
        checkWindow();
        this.agpWindow.setTransparent(z);
    }

    public void setOnApplyComponentPaddingListener(IApplyComponentPaddingListener iApplyComponentPaddingListener) {
        if (iApplyComponentPaddingListener != null) {
            this.agpWindow.setOnApplyComponentPaddingListener(iApplyComponentPaddingListener);
            return;
        }
        throw new WindowManager.PermissionException("reason: IApplyComponentPaddingListener is null");
    }

    public LayoutScatter getLayoutScatter() {
        checkWindow();
        return this.agpWindow.getLayoutScatter();
    }

    public boolean isActive() {
        checkWindow();
        return this.agpWindow.isActive();
    }

    public boolean isWCGMode() {
        checkWindow();
        return this.agpWindow.isWCGMode();
    }

    public void setPixelFormat(int i) {
        checkWindow();
        getLayoutConfig().ifPresent(new Consumer(i) {
            /* class ohos.agp.window.service.$$Lambda$Window$LIHoXxZj_rwYukoSbywQf5xIWc */
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Window.this.lambda$setPixelFormat$3$Window(this.f$1, (WindowManager.LayoutConfig) obj);
            }
        });
    }

    public /* synthetic */ void lambda$setPixelFormat$3$Window(int i, WindowManager.LayoutConfig layoutConfig) {
        this.agpWindow.setPixelFormat(i);
    }

    public int replicateFrom(WindowManager.LayoutConfig layoutConfig) {
        checkWindow();
        int i = 0;
        if (!getLayoutConfig().isPresent()) {
            return 0;
        }
        WindowManager.LayoutConfig layoutConfig2 = getLayoutConfig().get();
        if (layoutConfig2.flags != layoutConfig.flags) {
            i = 512;
        }
        if (layoutConfig2.dim != layoutConfig.dim) {
            i |= 2048;
        }
        if (!(layoutConfig2.width == layoutConfig.width && layoutConfig2.height == layoutConfig.height && layoutConfig2.x == layoutConfig.x && layoutConfig2.y == layoutConfig.y)) {
            i |= 2;
        }
        if (layoutConfig2.alpha != layoutConfig.alpha) {
            i |= 128;
        }
        if (layoutConfig2.title != layoutConfig.title) {
            i |= 32;
        }
        setLayoutConfig(layoutConfig);
        return i;
    }

    public void setLayoutInDisplaySideMode(int i) {
        checkWindow();
        getLayoutConfig().ifPresent(new Consumer(i) {
            /* class ohos.agp.window.service.$$Lambda$Window$NXPAm0z04a25effviOQYJ84w7A */
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Window.this.lambda$setLayoutInDisplaySideMode$4$Window(this.f$1, (WindowManager.LayoutConfig) obj);
            }
        });
    }

    public /* synthetic */ void lambda$setLayoutInDisplaySideMode$4$Window(int i, WindowManager.LayoutConfig layoutConfig) {
        layoutConfig.layoutInDisplaySideMode = i;
        setLayoutConfig(layoutConfig);
    }

    public void setAccessibilityEventListener(IAccessibilityEventListener iAccessibilityEventListener) {
        this.mIPopAccListener = iAccessibilityEventListener;
        AGPWindow aGPWindow = this.agpWindow;
        if (aGPWindow instanceof AGPCommonDialogWindow) {
            ((AGPCommonDialogWindow) aGPWindow).setComDialogPAEventListener(new AGPCommonDialogWindow.IComDialogPAEventLisener() {
                /* class ohos.agp.window.service.Window.AnonymousClass1 */

                @Override // ohos.agp.window.wmc.AGPCommonDialogWindow.IComDialogPAEventLisener
                public void onDispatch(AccessibilityEventInfo accessibilityEventInfo) {
                    Window.this.mIPopAccListener.onDispatchEvent(accessibilityEventInfo);
                }
            });
        }
    }

    public void setInputPanelDisplayType(int i) {
        checkWindow();
        this.agpWindow.setSoftInputMode(i);
    }
}
