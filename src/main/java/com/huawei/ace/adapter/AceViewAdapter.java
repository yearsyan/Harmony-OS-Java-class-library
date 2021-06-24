package com.huawei.ace.adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.TextureLayer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.huawei.ace.plugin.texture.IAceTextureLayer;
import com.huawei.ace.runtime.ALog;
import java.lang.reflect.Field;
import java.util.List;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.MouseEvent;
import ohos.multimodalinput.event.TouchEvent;

public class AceViewAdapter extends View {
    private static final long INVALID_TEXTURELAYER_HANDLE = 0;
    private static final WindowManager.LayoutParams MATCH_PARENT = new WindowManager.LayoutParams(-1, -1);
    private static final int OFFSET_NUMS = 2;
    private static final String TAG = "AceViewAdapter";
    protected AceWindowBlurAdapter aceWindowBlurAdapter;
    private final Context context;
    protected int offsetX = 0;
    protected int offsetY = 0;

    public void createInputConnection(AceTextInputAdapter aceTextInputAdapter) {
    }

    /* access modifiers changed from: protected */
    public void processDraw(long j) {
    }

    public boolean processKeyEvent(KeyEvent keyEvent) {
        return false;
    }

    public boolean processMouseEventInner(MouseEvent mouseEvent) {
        return false;
    }

    public boolean processTouchEvent(TouchEvent touchEvent) {
        return false;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AceViewAdapter(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r4
            android.content.Context r0 = (android.content.Context) r0
            r3.<init>(r0)
            r1 = 0
            r3.offsetY = r1
            r3.offsetX = r1
            r3.context = r0
            android.content.Context r0 = r3.context
            java.lang.String r2 = "window"
            java.lang.Object r0 = r0.getSystemService(r2)
            boolean r2 = r0 instanceof android.view.WindowManager
            if (r2 == 0) goto L_0x001f
            android.view.WindowManager r0 = (android.view.WindowManager) r0
            com.huawei.ace.AceVsyncWaiter.getInstance(r0)
        L_0x001f:
            r3.setDefaultFocusHighlightEnabled(r1)
            com.huawei.ace.adapter.AceWindowBlurAdapter r0 = new com.huawei.ace.adapter.AceWindowBlurAdapter
            r0.<init>(r4)
            r3.aceWindowBlurAdapter = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ace.adapter.AceViewAdapter.<init>(java.lang.Object):void");
    }

    public void addToContent() {
        Context context2 = this.context;
        if (context2 != null && (context2 instanceof Activity)) {
            ((Activity) context2).setContentView(this);
        }
    }

    public void setParentTransform() {
        Context context2 = this.context;
        if (context2 != null && (context2 instanceof Activity)) {
            Activity activity = (Activity) context2;
            activity.getWindow().addFlags(67108864);
            activity.getWindow().addFlags(134217728);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.aceWindowBlurAdapter.updateWindowDrawOp(false);
        processDraw(canvas.getNativeCanvasWrapper());
    }

    /* access modifiers changed from: protected */
    public int getOffsetY() {
        return this.offsetY;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        this.offsetX = iArr[0];
        this.offsetY = iArr[1];
        super.onSizeChanged(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public boolean onMouseEvent(MouseEvent mouseEvent) {
        mouseEvent.setCursorOffset(0.0f, (float) (-this.offsetY));
        return processMouseEventInner(mouseEvent);
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        AceTextInputAdapter aceTextInputAdapter = new AceTextInputAdapter();
        aceTextInputAdapter.setEditorInfo(editorInfo);
        createInputConnection(aceTextInputAdapter);
        if (aceTextInputAdapter.getInputConnection() != null) {
            return aceTextInputAdapter.getInputConnection();
        }
        return super.onCreateInputConnection(editorInfo);
    }

    public IAceTextureLayer createIAceTextureLayer() {
        return new IAceTextureLayer() {
            /* class com.huawei.ace.adapter.AceViewAdapter.AnonymousClass1 */

            @Override // com.huawei.ace.plugin.texture.IAceTextureLayer
            public TextureLayer createTextureLayer() {
                if (AceViewAdapter.this.getThreadedRenderer() != null) {
                    return AceViewAdapter.this.getThreadedRenderer().createTextureLayer();
                }
                return null;
            }
        };
    }

    public void enableWindowBlurFlag() {
        this.aceWindowBlurAdapter.enableWindowBlurFlag();
    }

    public void updateWindowBlurRegion(float[][] fArr) {
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        this.aceWindowBlurAdapter.updateWindowBlurRegion(fArr, iArr[0], iArr[1]);
    }

    private ActivityManager.AppTask getCurrentTask() {
        Context context2 = this.context;
        Activity activity = (Activity) context2;
        Object systemService = context2.getSystemService("activity");
        if (!(systemService instanceof ActivityManager)) {
            ALog.e(TAG, "Get current task failed. manager type error.");
            return null;
        }
        List<ActivityManager.AppTask> appTasks = ((ActivityManager) systemService).getAppTasks();
        if (appTasks != null && appTasks.size() > 0) {
            for (ActivityManager.AppTask appTask : appTasks) {
                if (!(appTask == null || appTask.getTaskInfo() == null || appTask.getTaskInfo().taskId != activity.getTaskId())) {
                    return appTask;
                }
            }
        }
        ALog.w(TAG, "Get current task failed. task not found. taskId: " + activity.getTaskId());
        return null;
    }

    /* access modifiers changed from: protected */
    public void initExcludeFromRecents(int i) {
        if ((i & 8388608) == 0) {
            excludeFromRecents(false);
        } else {
            excludeFromRecents(true);
        }
    }

    /* access modifiers changed from: protected */
    public void excludeFromRecents(boolean z) {
        ActivityManager.AppTask currentTask = getCurrentTask();
        if (currentTask == null) {
            ALog.e(TAG, "exclude from recents failed. excludes: " + z);
            return;
        }
        currentTask.setExcludeFromRecents(z);
    }

    public void setTaskBackgroundColor(int i) {
        ActivityManager.AppTask currentTask = getCurrentTask();
        if (currentTask == null) {
            ALog.e(TAG, "Set background color failed, task is null.");
            return;
        }
        ActivityManager.RecentTaskInfo taskInfo = currentTask.getTaskInfo();
        if (taskInfo == null) {
            ALog.e(TAG, "Set background color failed, task info is null.");
            return;
        }
        ActivityManager.TaskDescription taskDescription = taskInfo.taskDescription;
        try {
            Field declaredField = ActivityManager.TaskDescription.class.getDeclaredField("mColorBackground");
            declaredField.setAccessible(true);
            declaredField.set(taskDescription, Integer.valueOf(i));
            ((Activity) this.context).setTaskDescription(taskDescription);
        } catch (IllegalAccessException | NoSuchFieldException unused) {
            ALog.e(TAG, "set background color failed. color: " + i);
        }
    }

    public float getShowScaleX() {
        Rect rect = new Rect();
        getBoundsOnScreen(rect);
        return ((float) rect.width()) / ((float) getWidth());
    }

    public float getShowScaleY() {
        Rect rect = new Rect();
        getBoundsOnScreen(rect);
        return ((float) rect.height()) / ((float) getHeight());
    }
}
