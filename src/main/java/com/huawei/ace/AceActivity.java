package com.huawei.ace;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import com.huawei.ace.plugin.internal.PluginJNI;
import com.huawei.ace.plugin.video.AceVideoPlugin;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.AceApplicationInfo;
import com.huawei.ace.runtime.AceContainer;
import com.huawei.ace.runtime.AceEnv;
import com.huawei.ace.runtime.AceEventCallback;
import com.huawei.ace.runtime.AcePage;
import com.huawei.ace.runtime.IAceView;
import com.huawei.ace.runtime.LibraryLoader;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

public class AceActivity extends Activity {
    private static final String ASSET_PATH = "js/";
    private static final String ASSET_PATH_SHARE = "share";
    private static final int COMPONENT_ID = 1;
    private static final String ICU_RESOURCE_NAME = "icudt66l.dat";
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private static final String INSTANCE_DEFAULT_NAME = "default";
    private static final String JS_FRAMEWORK_DATA_NAME = "strip.native.min.js.bin";
    private static final String LOG_TAG = "AceActivity";
    private int activityId = ID_GENERATOR.getAndIncrement();
    private AceContainer container = null;
    private float density = 1.0f;
    private int heightPixels = 0;
    private String instanceName;
    private AcePage mainPage = null;
    private PluginJNI pluginJni = null;
    private AceViewCreator viewCreator = null;
    private int widthPixels = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        ALog.d(LOG_TAG, "AceActivity::onCreate called");
        super.onCreate(bundle);
        if (DeviceInfoHelper.isWatchType(getApplicationContext())) {
            LibraryLoader.setUseWatchLib();
        }
        boolean z = true;
        requestWindowFeature(1);
        this.viewCreator = new AceViewCreator(this, 0);
        AceEnv.setViewCreator(this.viewCreator);
        AceEnv.getInstance().setupFirstFrameHandler(0);
        AceEnv.getInstance().setupNatives(0, 0);
        if (getApplicationContext().getApplicationInfo() == null || (getApplicationContext().getApplicationInfo().flags & 2) == 0) {
            z = false;
        }
        String copyAssetToSDCard = copyAssetToSDCard(this, JS_FRAMEWORK_DATA_NAME);
        String copyAssetToSDCard2 = copyAssetToSDCard(this, ICU_RESOURCE_NAME);
        AceApplicationInfo.getInstance().setJsEngineParam(JS_FRAMEWORK_DATA_NAME, copyAssetToSDCard);
        AceApplicationInfo.getInstance().setupIcuRes(copyAssetToSDCard2);
        AceApplicationInfo.getInstance().setPackageInfo(getPackageName(), getUid(this), z, false);
        AceApplicationInfo.getInstance().setPid(Process.myPid());
        AceApplicationInfo.getInstance().setLocale();
        createContainer();
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00a8 A[SYNTHETIC, Splitter:B:45:0x00a8] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00b0 A[Catch:{ IOException -> 0x00ac }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String copyAssetToSDCard(android.content.Context r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 187
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ace.AceActivity.copyAssetToSDCard(android.content.Context, java.lang.String):java.lang.String");
    }

    private void createContainer() {
        AnonymousClass1 r0 = new AceEventCallback() {
            /* class com.huawei.ace.AceActivity.AnonymousClass1 */

            @Override // com.huawei.ace.runtime.AceEventCallback
            public String onEvent(int i, String str, String str2) {
                return AceActivity.this.onCallbackWithReturn(i, str, str2);
            }

            @Override // com.huawei.ace.runtime.AceEventCallback
            public void onFinish() {
                ALog.d(AceActivity.LOG_TAG, "finish current activity");
                AceActivity.this.finish();
            }

            @Override // com.huawei.ace.runtime.AceEventCallback
            public void onStatusBarBgColorChanged(int i) {
                AceActivity.this.statusBarBgColorChanged(i);
            }
        };
        this.pluginJni = new PluginJNI();
        this.container = AceEnv.createContainer(r0, this.pluginJni, this.activityId, getInstanceName(), null, null, null);
        AceContainer aceContainer = this.container;
        if (aceContainer != null) {
            aceContainer.setHostClassName(getClass().getName());
            this.container.setMultimodalObject(0);
            initDeviceInfo();
            initAsset();
            this.mainPage = this.container.createPage();
            if (!AceEnv.isJSONContainerType()) {
                this.container.setPageContent(this.mainPage, "", "");
            }
            Boolean valueOf = Boolean.valueOf(DeviceInfoHelper.isWatchType(getApplicationContext()));
            if (valueOf.booleanValue()) {
                this.viewCreator.setIsWearable();
            }
            IAceView view = this.container.getView(this.density, this.widthPixels, this.heightPixels);
            if (view instanceof View) {
                setContentView((View) view);
            }
            view.viewCreated();
            if (!valueOf.booleanValue()) {
                view.addResourcePlugin(AceVideoPlugin.createRegister(this, getInstanceName()));
            }
        }
    }

    private void initAsset() {
        AceContainer aceContainer = this.container;
        AssetManager assets = getAssets();
        aceContainer.addAssetPath(assets, ASSET_PATH + getInstanceName());
        this.container.addAssetPath(getAssets(), "js/share");
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        ALog.d(LOG_TAG, "AceActivity::onStart called");
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        ALog.d(LOG_TAG, "AceActivity::onResume called");
        super.onResume();
        AceContainer aceContainer = this.container;
        if (aceContainer != null) {
            aceContainer.getView(this.density, this.widthPixels, this.heightPixels).onResume();
            this.container.onShow();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        ALog.d(LOG_TAG, "AceActivity::onRestart called");
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        ALog.d(LOG_TAG, "AceActivity::onPause called");
        super.onPause();
        AceContainer aceContainer = this.container;
        if (aceContainer != null) {
            aceContainer.getView(this.density, this.widthPixels, this.heightPixels).onPause();
            this.container.onHide();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        ALog.d(LOG_TAG, "AceActivity::onStop called");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        ALog.d(LOG_TAG, "AceActivity::onDestroy called");
        super.onDestroy();
        AceEnv.destroyContainer(this.container);
    }

    public void onBackPressed() {
        ALog.d(LOG_TAG, "AceActivity::onBackPressed");
        AceContainer aceContainer = this.container;
        if (aceContainer != null && !aceContainer.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = configuration.orientation;
        if (i == 1) {
            ALog.i(LOG_TAG, "AceActivity:onConfigurationChanged ORIENTATION_PORTRAIT");
        } else if (i != 2) {
            ALog.i(LOG_TAG, "AceActivity:onConfigurationChanged unknown");
        } else {
            ALog.i(LOG_TAG, "AceActivity:onConfigurationChanged ORIENTATION_LANDSCAPE");
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AceEnv.dump(str, fileDescriptor, printWriter, strArr);
    }

    public void setPageContent(String str) {
        AceContainer aceContainer = this.container;
        if (aceContainer != null) {
            aceContainer.setPageContent(this.mainPage, str, "");
        }
    }

    public void updatePageContent(String str) {
        AceContainer aceContainer = this.container;
        if (aceContainer != null) {
            aceContainer.updatePageContent(this.mainPage, str);
        }
    }

    public void setInstanceName(String str) {
        if (str.charAt(str.length() - 1) == '/') {
            this.instanceName = str.substring(0, str.length() - 1);
        } else {
            this.instanceName = str;
        }
    }

    /* access modifiers changed from: protected */
    public String onCallbackWithReturn(int i, String str, String str2) {
        if (str != null && !str.isEmpty()) {
            ALog.d(LOG_TAG, "AceActivity::onCallbackWithReturn");
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void statusBarBgColorChanged(int i) {
        ALog.d(LOG_TAG, "set status bar. light: " + i);
        if (getWindow() == null) {
            ALog.w(LOG_TAG, "statusBarBgColorChanged failed due to Window is null");
            return;
        }
        View decorView = getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(AceApplicationInfo.toGray(Color.red(i), Color.green(i), Color.blue(i)) > AceApplicationInfo.getGrayThreshHold() ? systemUiVisibility | 8192 : systemUiVisibility & -8193);
    }

    private String getInstanceName() {
        String str = this.instanceName;
        return str == null ? "default" : str;
    }

    private void initDeviceInfo() {
        Resources resources = getResources();
        if (resources != null) {
            boolean isScreenRound = resources.getConfiguration().isScreenRound();
            int i = resources.getConfiguration().orientation;
            this.widthPixels = resources.getDisplayMetrics().widthPixels;
            this.heightPixels = resources.getDisplayMetrics().heightPixels;
            this.density = resources.getDisplayMetrics().density;
            this.container.initDeviceInfo(this.widthPixels, this.heightPixels, i, this.density, isScreenRound);
        }
    }

    public int getActivityId() {
        return this.activityId;
    }

    private int getUid(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return 0;
            }
            return packageManager.getApplicationInfo(getPackageName(), 128).uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
