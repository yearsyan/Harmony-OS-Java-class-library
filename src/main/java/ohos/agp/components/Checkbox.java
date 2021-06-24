package ohos.agp.components;

import ohos.agp.styles.Style;
import ohos.agp.utils.Color;
import ohos.app.Context;

public class Checkbox extends AbsButton {
    private native long nativeGetCheckboxHandle();

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Text, ohos.agp.components.Button, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetCheckboxHandle();
        }
    }

    public Checkbox(Context context) {
        this(context, null);
    }

    public Checkbox(Context context, AttrSet attrSet) {
        this(context, attrSet, "CheckboxDefaultStyle");
    }

    public Checkbox(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
    }

    @Override // ohos.agp.components.Text
    public void setTextColor(Color color) {
        setTextColorOff(color);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Text, ohos.agp.components.AbsButton, ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getCheckboxAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }
}
