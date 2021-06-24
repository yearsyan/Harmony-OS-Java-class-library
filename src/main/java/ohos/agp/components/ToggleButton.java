package ohos.agp.components;

import java.util.function.Consumer;
import ohos.agp.styles.Style;
import ohos.app.Context;

public class ToggleButton extends AbsButton {
    private native long nativeGetToggleButtonHandle();

    private native String nativeGetToggleButtonTextOff(long j);

    private native String nativeGetToggleButtonTextOn(long j);

    private native void nativeSetToggleButtonTextOff(long j, String str);

    private native void nativeSetToggleButtonTextOn(long j, String str);

    public ToggleButton(Context context) {
        this(context, null);
    }

    public ToggleButton(Context context, AttrSet attrSet) {
        this(context, attrSet, "ToggleButtonDefaultStyle");
    }

    public ToggleButton(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        AttrSet mergeStyle = AttrHelper.mergeStyle(context, attrSet, 0);
        for (int i = 0; i < mergeStyle.getLength(); i++) {
            mergeStyle.getAttr(i).ifPresent(new Consumer() {
                /* class ohos.agp.components.$$Lambda$ToggleButton$i8evW0r_fQOEgz9Bi7xB6LUbIIM */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ToggleButton.this.lambda$new$0$ToggleButton((Attr) obj);
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0038  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void lambda$new$0$ToggleButton(ohos.agp.components.Attr r5) {
        /*
            r4 = this;
            java.lang.String r0 = r5.getName()
            int r1 = r0.hashCode()
            r2 = -1341563857(0xffffffffb0095c2f, float:-4.997131E-10)
            r3 = 1
            if (r1 == r2) goto L_0x001f
            r2 = -320370913(0xffffffffece7871f, float:-2.2391996E27)
            if (r1 == r2) goto L_0x0014
            goto L_0x002a
        L_0x0014:
            java.lang.String r1 = "text_state_on"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x002a
            r0 = 0
            goto L_0x002b
        L_0x001f:
            java.lang.String r1 = "text_state_off"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x002a
            r0 = r3
            goto L_0x002b
        L_0x002a:
            r0 = -1
        L_0x002b:
            if (r0 == 0) goto L_0x0038
            if (r0 == r3) goto L_0x0030
            goto L_0x003f
        L_0x0030:
            java.lang.String r5 = r5.getStringValue()
            r4.setStateOffText(r5)
            goto L_0x003f
        L_0x0038:
            java.lang.String r5 = r5.getStringValue()
            r4.setStateOnText(r5)
        L_0x003f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.ToggleButton.lambda$new$0$ToggleButton(ohos.agp.components.Attr):void");
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Text, ohos.agp.components.Button, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetToggleButtonHandle();
        }
    }

    public String getStateOnText() {
        return nativeGetToggleButtonTextOn(this.mNativeViewPtr);
    }

    public String getStateOffText() {
        return nativeGetToggleButtonTextOff(this.mNativeViewPtr);
    }

    public void setStateOnText(String str) {
        nativeSetToggleButtonTextOn(this.mNativeViewPtr, str);
    }

    public void setStateOffText(String str) {
        nativeSetToggleButtonTextOff(this.mNativeViewPtr, str);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Text, ohos.agp.components.AbsButton, ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getToggleButtonAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    @Override // ohos.agp.components.Text, ohos.agp.components.AbsButton, ohos.agp.components.Component
    public void applyStyle(Style style) {
        super.applyStyle(style);
    }
}
