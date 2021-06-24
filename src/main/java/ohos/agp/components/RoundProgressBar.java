package ohos.agp.components;

import java.util.Objects;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.render.Canvas;
import ohos.agp.styles.Style;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class RoundProgressBar extends ProgressBar {
    private static final float MAX_SWEEP_ANGLE = 360.0f;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_ROUNDPROGRESSBAR");
    private Canvas mCanvasForIndeterminateLayer;
    private Component.DrawTask mIndeterminateDrawTask;

    private native void nativeAddIndeterminateLayerDrawTask(long j, Component.DrawTask drawTask, long j2);

    private native float nativeGetMaxAngle(long j);

    private native long nativeGetRoundProgressBarHandle();

    private native float nativeGetStartAngle(long j);

    private native void nativeSetMaxAngle(long j, float f);

    private native void nativeSetStartAngle(long j, float f);

    public /* synthetic */ void lambda$new$0$RoundProgressBar(Component component, Canvas canvas) {
        Element infiniteModeElement;
        if (isIndeterminate() && (infiniteModeElement = getInfiniteModeElement()) != null) {
            setIndeterminateLayerBounds(infiniteModeElement);
            infiniteModeElement.drawToCanvas(canvas);
        }
    }

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttrSet attrSet) {
        this(context, attrSet, "RoundProgressBarDefaultStyle");
    }

    public RoundProgressBar(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mCanvasForIndeterminateLayer = null;
        this.mIndeterminateDrawTask = new Component.DrawTask() {
            /* class ohos.agp.components.$$Lambda$RoundProgressBar$aSKMs8nOcdvcYzAKRom2IjEv0k */

            @Override // ohos.agp.components.Component.DrawTask
            public final void onDraw(Component component, Canvas canvas) {
                RoundProgressBar.this.lambda$new$0$RoundProgressBar(component, canvas);
            }
        };
        addIndeterminateLayerDrawTask();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ProgressBar, ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getRoundProgressBarAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    public void setStartAngle(float f) {
        nativeSetStartAngle(this.mNativeViewPtr, f);
    }

    public float getStartAngle() {
        return nativeGetStartAngle(this.mNativeViewPtr);
    }

    public void setMaxAngle(float f) {
        if (Float.compare(f, 0.0f) < 0 || Float.compare(f, MAX_SWEEP_ANGLE) > 0) {
            HiLog.error(TAG, "The max angle is invalid. The value ranges from 0 to 360 degrees.", new Object[0]);
        } else {
            nativeSetMaxAngle(this.mNativeViewPtr, f);
        }
    }

    public float getMaxAngle() {
        return nativeGetMaxAngle(this.mNativeViewPtr);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ProgressBar, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetRoundProgressBarHandle();
        }
    }

    private void addIndeterminateLayerDrawTask() {
        if (this.mCanvasForIndeterminateLayer == null) {
            this.mCanvasForIndeterminateLayer = new Canvas();
        }
        nativeAddIndeterminateLayerDrawTask(this.mNativeViewPtr, this.mIndeterminateDrawTask, this.mCanvasForIndeterminateLayer.getNativePtr());
    }

    private void setIndeterminateLayerBounds(Element element) {
        if (element != null) {
            Rect bounds = element.getBounds();
            Rect rect = new Rect(0, 0, getWidth(), getHeight());
            if (!Objects.equals(bounds, rect)) {
                element.setBounds(rect);
            }
        }
    }
}
