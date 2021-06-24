package ohos.agp.components;

import java.util.function.Consumer;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.MagicLayout;
import ohos.agp.styles.Style;
import ohos.agp.styles.attributes.MagicLayoutAttrsConstants;
import ohos.app.Context;

public class MagicLayout extends ComponentContainer {
    public static final int FOLD_OBVERSE = 0;
    public static final int FOLD_REVERSE = 1;

    private native int nativeGetAlignment(long j);

    private native int nativeGetFoldAlignment(long j);

    private native int nativeGetFoldDirection(long j);

    private native boolean nativeGetFoldEnabled(long j);

    private native long nativeGetHandle();

    private native int nativeGetOrientation(long j);

    private native void nativeSetAlignment(long j, int i);

    private native void nativeSetFoldAlignment(long j, int i);

    private native void nativeSetFoldDirection(long j, int i);

    private native void nativeSetFoldEnabled(long j, boolean z);

    private native void nativeSetOrientation(long j, int i);

    public MagicLayout(Context context) {
        this(context, null);
    }

    public MagicLayout(Context context, AttrSet attrSet) {
        this(context, attrSet, "MagicLayoutDefaultStyle");
    }

    public MagicLayout(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ComponentContainer, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetHandle();
        }
    }

    public void setAlignment(int i) {
        nativeSetAlignment(this.mNativeViewPtr, i);
    }

    public void setOrientation(int i) {
        if (i == 0) {
            nativeSetOrientation(this.mNativeViewPtr, 0);
        } else {
            nativeSetOrientation(this.mNativeViewPtr, 1);
        }
    }

    public void setFoldEnabled(boolean z) {
        nativeSetFoldEnabled(this.mNativeViewPtr, z);
    }

    public void setFoldDirection(int i) {
        nativeSetFoldDirection(this.mNativeViewPtr, i);
    }

    public void setFoldAlignment(int i) {
        nativeSetFoldAlignment(this.mNativeViewPtr, i);
    }

    public int getAlignment() {
        return nativeGetAlignment(this.mNativeViewPtr);
    }

    public int getOrientation() {
        return nativeGetOrientation(this.mNativeViewPtr);
    }

    public boolean isFoldEnabled() {
        return nativeGetFoldEnabled(this.mNativeViewPtr);
    }

    public int getFoldDirection() {
        return nativeGetFoldDirection(this.mNativeViewPtr);
    }

    public int getFoldAlignment() {
        return nativeGetFoldAlignment(this.mNativeViewPtr);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getMagicLayoutAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer
    public ComponentContainer.LayoutConfig verifyLayoutConfig(ComponentContainer.LayoutConfig layoutConfig) {
        if (layoutConfig instanceof LayoutConfig) {
            return layoutConfig;
        }
        return new LayoutConfig(layoutConfig);
    }

    @Override // ohos.agp.components.ComponentContainer
    public ComponentContainer.LayoutConfig createLayoutConfig(Context context, AttrSet attrSet) {
        return new LayoutConfig(context, attrSet);
    }

    public static class LayoutConfig extends ComponentContainer.LayoutConfig {
        public static final int UNSPECIFIED_REFERENCE_SIZE = -1;
        public int referenceSize = -1;

        private native void nativeSetLayoutParams(long j, int[] iArr, boolean z, int i);

        public LayoutConfig() {
        }

        public LayoutConfig(Context context, AttrSet attrSet) {
            super(context, attrSet);
            attrSet.getAttr(MagicLayoutAttrsConstants.LayoutParamsAttrsConstants.REFERENCE_SIZE).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$MagicLayout$LayoutConfig$02NAjX7Gfmjan280AF_a7S58N_A */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    MagicLayout.LayoutConfig.this.lambda$new$0$MagicLayout$LayoutConfig((Attr) obj);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$MagicLayout$LayoutConfig(Attr attr) {
            this.referenceSize = attr.getIntegerValue();
        }

        public LayoutConfig(int i, int i2) {
            super(i, i2);
        }

        public LayoutConfig(ComponentContainer.LayoutConfig layoutConfig) {
            super(layoutConfig);
        }

        public LayoutConfig(LayoutConfig layoutConfig) {
            super(layoutConfig);
            this.referenceSize = layoutConfig.referenceSize;
        }

        @Override // ohos.agp.components.ComponentContainer.LayoutConfig
        public void applyToComponent(Component component) {
            int[] iArr = new int[6];
            iArr[0] = this.width;
            iArr[1] = this.height;
            iArr[2] = isMarginsRelative() ? getHorizontalStartMargin() : getMarginLeft();
            iArr[3] = getMarginTop();
            iArr[4] = isMarginsRelative() ? getHorizontalEndMargin() : getMarginRight();
            iArr[5] = getMarginBottom();
            nativeSetLayoutParams(component.getNativeViewPtr(), iArr, isMarginsRelative(), this.referenceSize);
        }
    }
}
