package ohos.global.icu.text;

import java.text.Format;
import java.util.Objects;
import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;

public class ConstrainedFieldPosition {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private Class<?> fClassConstraint;
    private ConstraintType fConstraint;
    private long fContext;
    private Format.Field fField;
    private int fLimit;
    private int fStart;
    private Object fValue;

    /* access modifiers changed from: private */
    public enum ConstraintType {
        NONE,
        CLASS,
        FIELD,
        VALUE
    }

    public ConstrainedFieldPosition() {
        reset();
    }

    public void reset() {
        this.fConstraint = ConstraintType.NONE;
        this.fClassConstraint = Object.class;
        this.fField = null;
        this.fValue = null;
        this.fStart = 0;
        this.fLimit = 0;
        this.fContext = 0;
    }

    public void constrainField(Format.Field field) {
        if (field != null) {
            this.fConstraint = ConstraintType.FIELD;
            this.fClassConstraint = Object.class;
            this.fField = field;
            this.fValue = null;
            return;
        }
        throw new IllegalArgumentException("Cannot constrain on null field");
    }

    public void constrainClass(Class<?> cls) {
        if (cls != null) {
            this.fConstraint = ConstraintType.CLASS;
            this.fClassConstraint = cls;
            this.fField = null;
            this.fValue = null;
            return;
        }
        throw new IllegalArgumentException("Cannot constrain on null field class");
    }

    @Deprecated
    public void constrainFieldAndValue(Format.Field field, Object obj) {
        this.fConstraint = ConstraintType.VALUE;
        this.fClassConstraint = Object.class;
        this.fField = field;
        this.fValue = obj;
    }

    public Format.Field getField() {
        return this.fField;
    }

    public int getStart() {
        return this.fStart;
    }

    public int getLimit() {
        return this.fLimit;
    }

    public Object getFieldValue() {
        return this.fValue;
    }

    public long getInt64IterationContext() {
        return this.fContext;
    }

    public void setInt64IterationContext(long j) {
        this.fContext = j;
    }

    public void setState(Format.Field field, Object obj, int i, int i2) {
        this.fField = field;
        this.fValue = obj;
        this.fStart = i;
        this.fLimit = i2;
    }

    /* renamed from: ohos.global.icu.text.ConstrainedFieldPosition$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$text$ConstrainedFieldPosition$ConstraintType = new int[ConstraintType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.global.icu.text.ConstrainedFieldPosition$ConstraintType[] r0 = ohos.global.icu.text.ConstrainedFieldPosition.ConstraintType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.text.ConstrainedFieldPosition.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ConstrainedFieldPosition$ConstraintType = r0
                int[] r0 = ohos.global.icu.text.ConstrainedFieldPosition.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ConstrainedFieldPosition$ConstraintType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.text.ConstrainedFieldPosition$ConstraintType r1 = ohos.global.icu.text.ConstrainedFieldPosition.ConstraintType.NONE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.text.ConstrainedFieldPosition.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ConstrainedFieldPosition$ConstraintType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.text.ConstrainedFieldPosition$ConstraintType r1 = ohos.global.icu.text.ConstrainedFieldPosition.ConstraintType.CLASS     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.text.ConstrainedFieldPosition.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ConstrainedFieldPosition$ConstraintType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.text.ConstrainedFieldPosition$ConstraintType r1 = ohos.global.icu.text.ConstrainedFieldPosition.ConstraintType.FIELD     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.global.icu.text.ConstrainedFieldPosition.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ConstrainedFieldPosition$ConstraintType     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.global.icu.text.ConstrainedFieldPosition$ConstraintType r1 = ohos.global.icu.text.ConstrainedFieldPosition.ConstraintType.VALUE     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.ConstrainedFieldPosition.AnonymousClass1.<clinit>():void");
        }
    }

    public boolean matchesField(Format.Field field, Object obj) {
        if (field != null) {
            int i = AnonymousClass1.$SwitchMap$ohos$global$icu$text$ConstrainedFieldPosition$ConstraintType[this.fConstraint.ordinal()];
            if (i == 1) {
                return true;
            }
            if (i == 2) {
                return this.fClassConstraint.isAssignableFrom(field.getClass());
            }
            if (i == 3) {
                return this.fField == field;
            }
            if (i == 4) {
                return this.fField == field && Objects.equals(this.fValue, obj);
            }
            throw new AssertionError();
        }
        throw new IllegalArgumentException("field must not be null");
    }

    public String toString() {
        return "CFPos[" + this.fStart + LocaleUtility.IETF_SEPARATOR + this.fLimit + ' ' + this.fField + ']';
    }
}
