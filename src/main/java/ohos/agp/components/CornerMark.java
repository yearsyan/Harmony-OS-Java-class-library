package ohos.agp.components;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.MemoryCleaner;
import ohos.agp.utils.MemoryCleanerRegistry;
import ohos.app.Context;
import ohos.hiviewdfx.HiLogLabel;

public class CornerMark {
    private static final int BACKGROUND_COLOR = -434694118;
    private static final int CORNER_MARK_SIZE = 20;
    private static final float[] GRADIENT = {8.0f, 8.0f, 0.0f, 0.0f, 8.0f, 8.0f, 0.0f, 0.0f};
    private static final float PRECISION = 0.5f;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_CornerMark");
    private static final int TEXT_COLOR = -419430401;
    private static final int TEXT_SIZE = 14;
    private ShapeElement mBackground;
    private Color mColor = new Color(TEXT_COLOR);
    private Context mContext;
    private long mNativeCornerMarkPtr = 0;

    private native long nativeGetCornerMarkHandle();

    private native int nativeGetHeight(long j);

    private native String nativeGetText(long j);

    private native int nativeGetTextSize(long j);

    private native int nativeGetWidth(long j);

    private native void nativeSetElement(long j, long j2);

    private native void nativeSetHeight(long j, int i);

    private native void nativeSetText(long j, String str);

    private native void nativeSetTextColor(long j, int i);

    private native void nativeSetTextSize(long j, int i);

    private native void nativeSetWidth(long j, int i);

    /* access modifiers changed from: protected */
    public static class CornerMarkCleaner implements MemoryCleaner {
        protected long mNativePtr;

        private native void nativeCornerMarkRelease(long j);

        public CornerMarkCleaner(long j) {
            this.mNativePtr = j;
        }

        @Override // ohos.agp.utils.MemoryCleaner
        public void run() {
            long j = this.mNativePtr;
            if (j != 0) {
                nativeCornerMarkRelease(j);
                this.mNativePtr = 0;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void registerCleaner() {
        MemoryCleanerRegistry.getInstance().registerWithNativeBind(this, new CornerMarkCleaner(this.mNativeCornerMarkPtr), this.mNativeCornerMarkPtr);
    }

    CornerMark(Context context) {
        if (this.mNativeCornerMarkPtr == 0) {
            this.mNativeCornerMarkPtr = nativeGetCornerMarkHandle();
        }
        if (this.mBackground == null) {
            this.mBackground = new ShapeElement();
        }
        registerCleaner();
        this.mContext = context;
        this.mBackground.setShape(0);
        this.mBackground.setRgbColor(RgbColor.fromArgbInt(BACKGROUND_COLOR));
        this.mBackground.setCornerRadiiArray(GRADIENT);
        setElement(this.mBackground);
        setDefaultCornerMark(context);
    }

    /* access modifiers changed from: package-private */
    public long getNativeCornerMarkPtr() {
        return this.mNativeCornerMarkPtr;
    }

    public void setText(String str) {
        long j = this.mNativeCornerMarkPtr;
        if (str == null) {
            str = "";
        }
        nativeSetText(j, str);
    }

    public String getText() {
        return nativeGetText(this.mNativeCornerMarkPtr);
    }

    public ShapeElement getElement() {
        return this.mBackground;
    }

    public void setElement(ShapeElement shapeElement) {
        this.mBackground = shapeElement;
        nativeSetElement(this.mNativeCornerMarkPtr, shapeElement == null ? 0 : shapeElement.getNativeElementPtr());
    }

    public void setWidth(int i) {
        nativeSetWidth(this.mNativeCornerMarkPtr, i);
    }

    public int getWidth() {
        return nativeGetWidth(this.mNativeCornerMarkPtr);
    }

    public void setHeight(int i) {
        nativeSetHeight(this.mNativeCornerMarkPtr, i);
    }

    public int getHeight() {
        return nativeGetHeight(this.mNativeCornerMarkPtr);
    }

    public void setTextSize(int i) {
        if (i > 0) {
            nativeSetTextSize(this.mNativeCornerMarkPtr, i);
        }
    }

    public int getTextSize() {
        return nativeGetTextSize(this.mNativeCornerMarkPtr);
    }

    public void setTextColor(Color color) {
        this.mColor = color;
        nativeSetTextColor(this.mNativeCornerMarkPtr, color.getValue());
    }

    public Color getTextColor() {
        return this.mColor;
    }

    private void setDefaultCornerMark(Context context) {
        if (this.mNativeCornerMarkPtr != 0) {
            float density = AttrHelper.getDensity(context);
            int vp2px = AttrHelper.vp2px(20.0f, density);
            setWidth(vp2px);
            setHeight(vp2px);
            setTextSize(AttrHelper.fp2px(14.0f, density));
            setTextColor(this.mColor);
        }
    }
}
