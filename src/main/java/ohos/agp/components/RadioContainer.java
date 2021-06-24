package ohos.agp.components;

import java.util.function.Consumer;
import ohos.agp.styles.attributes.RadioContainerAttrsConstants;
import ohos.app.Context;

public class RadioContainer extends DirectionalLayout {
    public static final int INVALID_ID = -1;
    protected CheckedStateChangedListener mMarkListener;

    public interface CheckedStateChangedListener {
        void onCheckedChanged(RadioContainer radioContainer, int i);
    }

    private native long nativeGetRadioGroupHandle();

    private native void nativeRadioGroupClear(long j);

    private native int nativeRadioGroupGetCheckedId(long j);

    private native void nativeRadioGroupSetChecked(long j, int i);

    private native void nativeSetRadioGroupCallback(long j, CheckedStateChangedListener checkedStateChangedListener);

    public RadioContainer(Context context) {
        this(context, null);
    }

    public RadioContainer(Context context, AttrSet attrSet) {
        this(context, attrSet, "RadioGroupDefaultStyle");
    }

    public RadioContainer(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mMarkListener = null;
        AttrSet mergeStyle = AttrHelper.mergeStyle(context, attrSet, 0);
        for (int i = 0; i < mergeStyle.getLength(); i++) {
            mergeStyle.getAttr(i).ifPresent(new Consumer(context) {
                /* class ohos.agp.components.$$Lambda$RadioContainer$pOKvrTRZcgOLZyh2d0aUvillvMk */
                private final /* synthetic */ Context f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    RadioContainer.this.lambda$new$0$RadioContainer(this.f$1, (Attr) obj);
                }
            });
        }
    }

    public /* synthetic */ void lambda$new$0$RadioContainer(Context context, Attr attr) {
        AttrWrapper attrWrapper = new AttrWrapper(context, attr);
        String name = attr.getName();
        if (((name.hashCode() == 1122682533 && name.equals(RadioContainerAttrsConstants.MARKED_BUTTON)) ? (char) 0 : 65535) == 0) {
            mark(attrWrapper.getIntegerValue());
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.ComponentContainer, ohos.agp.components.DirectionalLayout, ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetRadioGroupHandle();
        }
    }

    static /* synthetic */ boolean lambda$mark$1(Integer num) {
        return num.intValue() >= -1;
    }

    public void mark(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$RadioContainer$ZThE1tyqn8YktKWmbl9dYrZrRw.INSTANCE, "An argument value must be INVALID_ID or >= 0");
        nativeRadioGroupSetChecked(this.mNativeViewPtr, i);
    }

    public int getMarkedButtonId() {
        return nativeRadioGroupGetCheckedId(this.mNativeViewPtr);
    }

    public void cancelMarks() {
        nativeRadioGroupClear(this.mNativeViewPtr);
    }

    public void setMarkChangedListener(CheckedStateChangedListener checkedStateChangedListener) {
        this.mMarkListener = checkedStateChangedListener;
        nativeSetRadioGroupCallback(this.mNativeViewPtr, checkedStateChangedListener);
    }

    public CheckedStateChangedListener getMarkChangedListener() {
        return this.mMarkListener;
    }
}
