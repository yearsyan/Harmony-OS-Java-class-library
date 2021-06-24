package com.huawei.ace.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.huawei.ace.AceVsyncWaiter;
import java.util.function.Consumer;

public class AceContextAdapter {
    public static final int GET_SYSTEM_BRIGHTNESS_FAIL = -1;
    private static final WindowManager.LayoutParams MATCH_PARENT = new WindowManager.LayoutParams(-1, -1);
    private View animateView;
    private final Context context;
    private InnerReceiver innerReceiver;

    public interface HomeKeyPressedCallback {
        void onHomeKeyPressed();
    }

    public AceContextAdapter(Object obj) {
        if (obj instanceof Context) {
            this.context = (Context) obj;
        } else {
            this.context = null;
        }
    }

    public static void initVsync(Object obj) {
        Object systemService = ((Context) obj).getSystemService("window");
        if (systemService instanceof WindowManager) {
            AceVsyncWaiter.getInstance((WindowManager) systemService);
        }
    }

    public static int[] getWindowOffset(Object obj) {
        Window window;
        View findViewById;
        int[] iArr = new int[2];
        if (!(obj == null || !(obj instanceof Activity) || (window = ((Activity) obj).getWindow()) == null || (findViewById = window.findViewById(16908290)) == null)) {
            iArr[0] = findViewById.getLeft();
            iArr[1] = findViewById.getTop();
        }
        return iArr;
    }

    public static int getUserId() {
        return UserHandle.myUserId();
    }

    public Context getContext() {
        return this.context;
    }

    public AssetManager getAssetManager() {
        return this.context.getAssets();
    }

    public Resources getResources() {
        return this.context.getResources();
    }

    public boolean invalid() {
        return this.context == null;
    }

    public void addAnimateView(int i) {
        if (!invalid()) {
            Context context2 = this.context;
            if (context2 instanceof Activity) {
                Activity activity = (Activity) context2;
                this.animateView = new View(activity);
                this.animateView.setLayoutParams(MATCH_PARENT);
                this.animateView.setBackgroundColor(i);
                activity.addContentView(this.animateView, MATCH_PARENT);
            }
        }
    }

    static class InnerReceiver extends BroadcastReceiver {
        static final String REASON_HOME_KEY = "homekey";
        static final String REASON_KEY = "reason";
        private HomeKeyPressedCallback homeKeyPressedCallback;

        InnerReceiver(HomeKeyPressedCallback homeKeyPressedCallback2) {
            this.homeKeyPressedCallback = homeKeyPressedCallback2;
        }

        public void onReceive(Context context, Intent intent) {
            String stringExtra;
            HomeKeyPressedCallback homeKeyPressedCallback2;
            if (intent != null && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction()) && (stringExtra = intent.getStringExtra(REASON_KEY)) != null && stringExtra.equals(REASON_HOME_KEY) && (homeKeyPressedCallback2 = this.homeKeyPressedCallback) != null) {
                homeKeyPressedCallback2.onHomeKeyPressed();
            }
        }
    }

    public void setOnHomeKeyPressedListener(HomeKeyPressedCallback homeKeyPressedCallback) {
        if (this.innerReceiver == null) {
            this.innerReceiver = new InnerReceiver(homeKeyPressedCallback);
            ((Activity) this.context).registerReceiver(this.innerReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        }
    }

    public void clearHomeKeyPressedListener() {
        InnerReceiver innerReceiver2 = this.innerReceiver;
        if (innerReceiver2 != null) {
            ((Activity) this.context).unregisterReceiver(innerReceiver2);
            this.innerReceiver = null;
        }
    }

    public void setOnApplyWindowInsetsListener(ViewportMetricsAdapter viewportMetricsAdapter, Consumer<ViewportMetricsAdapter> consumer) {
        if (!invalid()) {
            Context context2 = this.context;
            if (context2 instanceof Activity) {
                ((Activity) context2).getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener(consumer) {
                    /* class com.huawei.ace.adapter.$$Lambda$AceContextAdapter$3YK0El6I5vmmV6Pge3_vav9jiY */
                    private final /* synthetic */ Consumer f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                        return AceContextAdapter.lambda$setOnApplyWindowInsetsListener$0(ViewportMetricsAdapter.this, this.f$1, view, windowInsets);
                    }
                });
            }
        }
    }

    static /* synthetic */ WindowInsets lambda$setOnApplyWindowInsetsListener$0(ViewportMetricsAdapter viewportMetricsAdapter, Consumer consumer, View view, WindowInsets windowInsets) {
        int i;
        boolean z = true;
        int i2 = 0;
        boolean z2 = (view.getWindowSystemUiVisibility() & 4) != 0;
        if ((view.getWindowSystemUiVisibility() & 2) == 0) {
            z = false;
        }
        if (z2) {
            i = 0;
        } else {
            i = windowInsets.getSystemWindowInsetTop();
        }
        viewportMetricsAdapter.physicalPaddingTop = i;
        if (!z) {
            i2 = windowInsets.getSystemWindowInsetBottom();
        }
        viewportMetricsAdapter.physicalViewInsetBottom = i2;
        consumer.accept(viewportMetricsAdapter);
        return windowInsets;
    }

    public void blurAnimateView() {
        View view = this.animateView;
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.animateView);
            }
            this.animateView = null;
        }
    }

    public int getSystemScreenBrightness() {
        try {
            return Settings.System.getInt(this.context.getContentResolver(), "screen_brightness");
        } catch (Settings.SettingNotFoundException unused) {
            return -1;
        }
    }
}
