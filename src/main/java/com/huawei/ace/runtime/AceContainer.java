package com.huawei.ace.runtime;

import java.io.FileDescriptor;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.ResourceManagerInner;

public final class AceContainer {
    public static final int COLOR_MODE_DARK = 1;
    public static final int COLOR_MODE_LIGHT = 0;
    public static final int CONTAINER_TYPE_JS = 1;
    public static final int CONTAINER_TYPE_JSON = 0;
    public static final int CONTAINER_TYPE_JS_CARD = 2;
    public static final int DEVICE_TYPE_DEFAULT = 0;
    public static final int DEVICE_TYPE_WEARABLE = 1;
    private static final String PAGE_URI = "url";
    private static final String START_INTENT_DATA_KEY = "remoteData";
    private static final String TAG = "AceContainer";
    private int[] byteLenArray;
    private AceEventCallback callback;
    private FileDescriptor[] fdArray;
    private float fontScale = 1.0f;
    private final int instanceId;
    private AtomicInteger pageIdGenerator = new AtomicInteger(1);
    private String[] picNameArray;
    private int type = 0;
    private IAceView view;
    private final IAceViewCreator viewCreator;

    private native void nativeAddAssetPath(int i, Object obj, String str);

    private native void nativeAddCustomAssetPath(int i, String str, String[] strArr);

    private native void nativeCreateContainer(int i, int i2, AceEventCallback aceEventCallback, AcePluginMessage acePluginMessage, String str, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr);

    private native void nativeCreatePage(int i, int i2);

    private native void nativeDestroyContainer(int i);

    private native void nativeInitDeviceInfo(int i, int i2, int i3, int i4, float f, boolean z);

    private native void nativeInitResourceManager(int i, long[] jArr, int i2, String str);

    private native void nativeLoadPluginJsCode(int i, String str);

    private native void nativeOnActive(int i);

    private native boolean nativeOnBackPressed(int i);

    private native void nativeOnCompleteContinuation(int i, int i2);

    private native void nativeOnHide(int i);

    private native void nativeOnInactive(int i);

    private native void nativeOnNewRequest(int i, String str);

    private native boolean nativeOnRestoreData(int i, String str);

    private native String nativeOnSaveData(int i);

    private native void nativeOnShow(int i);

    private native boolean nativeOnStartContinuation(int i);

    private native boolean nativePush(int i, String str, String str2);

    private native boolean nativeRun(int i, int i2, String str, String str2);

    private native void nativeSetActionEventCallback(int i, ActionEventCallback actionEventCallback);

    private native void nativeSetColorMode(int i, int i2);

    private native void nativeSetFontScale(int i, float f);

    private native void nativeSetHostClassName(int i, String str);

    private native void nativeSetMultimodal(int i, long j);

    private native void nativeSetSemiModalCustomStyle(int i, int i2, int i3);

    private native void nativeSetUseLiteStyle(int i, boolean z);

    private native void nativeSetView(int i, long j, float f, int i2, int i3);

    private native void nativeSetWindowStyle(int i, int i2, int i3);

    private native boolean nativeUpdate(int i, int i2, String str);

    private native void nativeUpdateFontWeightScale(int i);

    private native void nativeUpdateInstantData(int i, String str, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr);

    public AceContainer(int i, int i2, IAceViewCreator iAceViewCreator, AceEventCallback aceEventCallback, AcePluginMessage acePluginMessage, String str, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr) {
        String[] strArr2;
        int[] iArr2;
        this.instanceId = i;
        this.type = i2;
        this.viewCreator = iAceViewCreator;
        FileDescriptor[] fileDescriptorArr2 = null;
        this.view = null;
        this.callback = aceEventCallback;
        if (strArr == null) {
            strArr2 = null;
        } else {
            strArr2 = (String[]) strArr.clone();
        }
        this.picNameArray = strArr2;
        if (iArr == null) {
            iArr2 = null;
        } else {
            iArr2 = (int[]) iArr.clone();
        }
        this.byteLenArray = iArr2;
        this.fdArray = fileDescriptorArr != null ? (FileDescriptor[]) fileDescriptorArr.clone() : fileDescriptorArr2;
        nativeCreateContainer(i, i2, aceEventCallback, acePluginMessage, str, strArr, iArr, fileDescriptorArr);
    }

    public void destroyContainer() {
        nativeDestroyContainer(this.instanceId);
        this.view.releaseNativeView();
    }

    public IAceView getView(float f, int i, int i2) {
        if (this.view == null) {
            this.view = this.viewCreator.createView(this.instanceId, f);
            this.view.initDeviceType();
            nativeSetView(this.instanceId, this.view.getNativePtr(), f, i, i2);
        }
        return this.view;
    }

    public AcePage createPage() {
        int generatePageId = generatePageId();
        AcePage acePage = new AcePage(generatePageId, this.callback);
        nativeCreatePage(this.instanceId, generatePageId);
        return acePage;
    }

    public void setPageContent(AcePage acePage, String str, String str2) {
        nativeRun(this.instanceId, acePage.getId(), str, str2);
    }

    public void setPushPage(String str, String str2) {
        nativePush(this.instanceId, str, str2);
    }

    public void updatePageContent(AcePage acePage, String str) {
        nativeUpdate(this.instanceId, acePage.getId(), str);
    }

    public boolean onBackPressed() {
        return nativeOnBackPressed(this.instanceId);
    }

    public void onShow() {
        nativeOnShow(this.instanceId);
    }

    public void onHide() {
        nativeOnHide(this.instanceId);
    }

    public void initDeviceInfo(int i, int i2, int i3, float f, boolean z) {
        nativeInitDeviceInfo(this.instanceId, i, i2, i3, f, z);
    }

    public void onActive() {
        nativeOnActive(this.instanceId);
    }

    public void onInactive() {
        nativeOnInactive(this.instanceId);
    }

    public boolean onStartContinuation() {
        return nativeOnStartContinuation(this.instanceId);
    }

    public void onCompleteContinuation(int i) {
        nativeOnCompleteContinuation(this.instanceId, i);
    }

    public String onSaveData() {
        return nativeOnSaveData(this.instanceId);
    }

    public boolean onRestoreData(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return nativeOnRestoreData(this.instanceId, str);
    }

    public void onNewRequest(String str) {
        nativeOnNewRequest(this.instanceId, str);
    }

    public void addAssetPath(Object obj, String str) {
        nativeAddAssetPath(this.instanceId, obj, str);
    }

    public void addCustomAssetPath(String str, String[] strArr) {
        nativeAddCustomAssetPath(this.instanceId, str, strArr);
    }

    public void setMultimodalObject(long j) {
        nativeSetMultimodal(this.instanceId, j);
    }

    public void setFontScale(float f) {
        this.fontScale = f;
        nativeSetFontScale(this.instanceId, f);
    }

    public void updateFontWeightScale() {
        nativeUpdateFontWeightScale(this.instanceId);
    }

    private int generatePageId() {
        return this.pageIdGenerator.getAndIncrement();
    }

    public void loadPluginJsCode(String str) {
        nativeLoadPluginJsCode(this.instanceId, str);
    }

    public int getInstanceId() {
        return this.instanceId;
    }

    public void updateInstantData(String str, String[] strArr, int[] iArr, FileDescriptor[] fileDescriptorArr) {
        nativeUpdateInstantData(this.instanceId, str, strArr, iArr, fileDescriptorArr);
    }

    public void setActionEventCallback(ActionEventCallback actionEventCallback) {
        nativeSetActionEventCallback(this.instanceId, actionEventCallback);
    }

    public void setWindowStyle(int i, int i2) {
        nativeSetWindowStyle(this.instanceId, i, i2);
    }

    public void setSemiModalCustomStyle(int i, int i2) {
        nativeSetSemiModalCustomStyle(this.instanceId, i, i2);
    }

    public void setUseLiteStyle(boolean z) {
        nativeSetUseLiteStyle(this.instanceId, z);
    }

    public void setColorMode(int i) {
        nativeSetColorMode(this.instanceId, i);
    }

    public void initResourceManager(ResourceManager resourceManager, String str, int i) {
        List<Long> managerHandle = resourceManager != null ? ResourceManagerInner.getManagerHandle(resourceManager) : null;
        if (managerHandle == null) {
            ALog.w(TAG, "Init resource manager without handlers.");
            nativeInitResourceManager(this.instanceId, new long[0], i, str);
            return;
        }
        nativeInitResourceManager(this.instanceId, managerHandle.stream().mapToLong($$Lambda$AceContainer$rr9PnncZVy5LMeJZJWs1ycdkU.INSTANCE).toArray(), i, str);
    }

    public void initResourceManager(String str, int i) {
        nativeInitResourceManager(this.instanceId, new long[0], i, str);
    }

    public void setHostClassName(String str) {
        nativeSetHostClassName(this.instanceId, str);
    }
}
