package ohos.global.icu.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;
import ohos.global.icu.text.NumberFormat;
import ohos.global.icu.text.SymbolTable;
import ohos.telephony.TelephoneNumberUtils;

public class FormattedStringBuilder implements CharSequence, Appendable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final FormattedStringBuilder EMPTY = new FormattedStringBuilder();
    private static final Map<Object, Character> fieldToDebugChar = new HashMap();
    int appendOffset;
    Object appendableField;
    char[] chars;
    Object[] fields;
    int length;
    int zero;

    static {
        fieldToDebugChar.put(NumberFormat.Field.SIGN, Character.valueOf(LocaleUtility.IETF_SEPARATOR));
        fieldToDebugChar.put(NumberFormat.Field.INTEGER, Character.valueOf(UCharacterProperty.LATIN_SMALL_LETTER_I_));
        fieldToDebugChar.put(NumberFormat.Field.FRACTION, 'f');
        fieldToDebugChar.put(NumberFormat.Field.EXPONENT, 'e');
        fieldToDebugChar.put(NumberFormat.Field.EXPONENT_SIGN, '+');
        fieldToDebugChar.put(NumberFormat.Field.EXPONENT_SYMBOL, 'E');
        fieldToDebugChar.put(NumberFormat.Field.DECIMAL_SEPARATOR, '.');
        fieldToDebugChar.put(NumberFormat.Field.GROUPING_SEPARATOR, Character.valueOf(TelephoneNumberUtils.PAUSE));
        fieldToDebugChar.put(NumberFormat.Field.PERCENT, '%');
        fieldToDebugChar.put(NumberFormat.Field.PERMILLE, (char) 8240);
        fieldToDebugChar.put(NumberFormat.Field.CURRENCY, Character.valueOf(SymbolTable.SYMBOL_REF));
        fieldToDebugChar.put(NumberFormat.Field.MEASURE_UNIT, 'u');
        fieldToDebugChar.put(NumberFormat.Field.COMPACT, 'C');
    }

    public FormattedStringBuilder() {
        this(40);
    }

    public FormattedStringBuilder(int i) {
        this.appendOffset = 0;
        this.appendableField = null;
        this.chars = new char[i];
        this.fields = new Object[i];
        this.zero = i / 2;
        this.length = 0;
    }

    public FormattedStringBuilder(FormattedStringBuilder formattedStringBuilder) {
        this.appendOffset = 0;
        this.appendableField = null;
        copyFrom(formattedStringBuilder);
    }

    public void copyFrom(FormattedStringBuilder formattedStringBuilder) {
        char[] cArr = formattedStringBuilder.chars;
        this.chars = Arrays.copyOf(cArr, cArr.length);
        Object[] objArr = formattedStringBuilder.fields;
        this.fields = Arrays.copyOf(objArr, objArr.length);
        this.zero = formattedStringBuilder.zero;
        this.length = formattedStringBuilder.length;
    }

    public int length() {
        return this.length;
    }

    public int codePointCount() {
        return Character.codePointCount(this, 0, length());
    }

    public char charAt(int i) {
        return this.chars[this.zero + i];
    }

    public Object fieldAt(int i) {
        return this.fields[this.zero + i];
    }

    public int getFirstCodePoint() {
        int i = this.length;
        if (i == 0) {
            return -1;
        }
        char[] cArr = this.chars;
        int i2 = this.zero;
        return Character.codePointAt(cArr, i2, i + i2);
    }

    public int getLastCodePoint() {
        int i = this.length;
        if (i == 0) {
            return -1;
        }
        char[] cArr = this.chars;
        int i2 = this.zero;
        return Character.codePointBefore(cArr, i + i2, i2);
    }

    public int codePointAt(int i) {
        char[] cArr = this.chars;
        int i2 = this.zero;
        return Character.codePointAt(cArr, i + i2, i2 + this.length);
    }

    public int codePointBefore(int i) {
        char[] cArr = this.chars;
        int i2 = this.zero;
        return Character.codePointBefore(cArr, i + i2, i2);
    }

    public FormattedStringBuilder clear() {
        this.zero = getCapacity() / 2;
        this.length = 0;
        return this;
    }

    public void setAppendIndex(int i) {
        this.appendOffset = this.length - i;
    }

    public int appendChar16(char c, Object obj) {
        return insertChar16(this.length - this.appendOffset, c, obj);
    }

    public int insertChar16(int i, char c, Object obj) {
        int prepareForInsert = prepareForInsert(i, 1);
        this.chars[prepareForInsert] = c;
        this.fields[prepareForInsert] = obj;
        return 1;
    }

    public int appendCodePoint(int i, Object obj) {
        return insertCodePoint(this.length - this.appendOffset, i, obj);
    }

    public int insertCodePoint(int i, int i2, Object obj) {
        int charCount = Character.charCount(i2);
        int prepareForInsert = prepareForInsert(i, charCount);
        Character.toChars(i2, this.chars, prepareForInsert);
        Object[] objArr = this.fields;
        objArr[prepareForInsert] = obj;
        if (charCount == 2) {
            objArr[prepareForInsert + 1] = obj;
        }
        return charCount;
    }

    public int append(CharSequence charSequence, Object obj) {
        return insert(this.length - this.appendOffset, charSequence, obj);
    }

    public int insert(int i, CharSequence charSequence, Object obj) {
        if (charSequence.length() == 0) {
            return 0;
        }
        if (charSequence.length() == 1) {
            return insertCodePoint(i, charSequence.charAt(0), obj);
        }
        return insert(i, charSequence, 0, charSequence.length(), obj);
    }

    public int insert(int i, CharSequence charSequence, int i2, int i3, Object obj) {
        int i4 = i3 - i2;
        int prepareForInsert = prepareForInsert(i, i4);
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = prepareForInsert + i5;
            this.chars[i6] = charSequence.charAt(i2 + i5);
            this.fields[i6] = obj;
        }
        return i4;
    }

    public int splice(int i, int i2, CharSequence charSequence, int i3, int i4, Object obj) {
        int i5;
        int i6 = i4 - i3;
        int i7 = i6 - (i2 - i);
        if (i7 > 0) {
            i5 = prepareForInsert(i, i7);
        } else {
            i5 = remove(i, -i7);
        }
        for (int i8 = 0; i8 < i6; i8++) {
            int i9 = i5 + i8;
            this.chars[i9] = charSequence.charAt(i3 + i8);
            this.fields[i9] = obj;
        }
        return i7;
    }

    public int append(char[] cArr, Object[] objArr) {
        return insert(this.length - this.appendOffset, cArr, objArr);
    }

    public int insert(int i, char[] cArr, Object[] objArr) {
        int length2 = cArr.length;
        if (length2 == 0) {
            return 0;
        }
        int prepareForInsert = prepareForInsert(i, length2);
        for (int i2 = 0; i2 < length2; i2++) {
            int i3 = prepareForInsert + i2;
            this.chars[i3] = cArr[i2];
            this.fields[i3] = objArr == null ? null : objArr[i2];
        }
        return length2;
    }

    public int append(FormattedStringBuilder formattedStringBuilder) {
        return insert(this.length - this.appendOffset, formattedStringBuilder);
    }

    public int insert(int i, FormattedStringBuilder formattedStringBuilder) {
        if (this != formattedStringBuilder) {
            int i2 = formattedStringBuilder.length;
            if (i2 == 0) {
                return 0;
            }
            int prepareForInsert = prepareForInsert(i, i2);
            for (int i3 = 0; i3 < i2; i3++) {
                int i4 = prepareForInsert + i3;
                this.chars[i4] = formattedStringBuilder.charAt(i3);
                this.fields[i4] = formattedStringBuilder.fieldAt(i3);
            }
            return i2;
        }
        throw new IllegalArgumentException("Cannot call insert/append on myself");
    }

    private int prepareForInsert(int i, int i2) {
        if (i == -1) {
            i = this.length;
        }
        if (i == 0) {
            int i3 = this.zero;
            if (i3 - i2 >= 0) {
                this.zero = i3 - i2;
                this.length += i2;
                return this.zero;
            }
        }
        int i4 = this.length;
        if (i != i4 || this.zero + i4 + i2 >= getCapacity()) {
            return prepareForInsertHelper(i, i2);
        }
        this.length += i2;
        return (this.zero + this.length) - i2;
    }

    private int prepareForInsertHelper(int i, int i2) {
        int capacity = getCapacity();
        int i3 = this.zero;
        char[] cArr = this.chars;
        Object[] objArr = this.fields;
        int i4 = this.length;
        if (i4 + i2 > capacity) {
            int i5 = (i4 + i2) * 2;
            int i6 = (i5 / 2) - ((i4 + i2) / 2);
            char[] cArr2 = new char[i5];
            Object[] objArr2 = new Object[i5];
            System.arraycopy(cArr, i3, cArr2, i6, i);
            int i7 = i3 + i;
            int i8 = i6 + i + i2;
            System.arraycopy(cArr, i7, cArr2, i8, this.length - i);
            System.arraycopy(objArr, i3, objArr2, i6, i);
            System.arraycopy(objArr, i7, objArr2, i8, this.length - i);
            this.chars = cArr2;
            this.fields = objArr2;
            this.zero = i6;
            this.length += i2;
        } else {
            int i9 = (capacity / 2) - ((i4 + i2) / 2);
            System.arraycopy(cArr, i3, cArr, i9, i4);
            int i10 = i9 + i;
            int i11 = i10 + i2;
            System.arraycopy(cArr, i10, cArr, i11, this.length - i);
            System.arraycopy(objArr, i3, objArr, i9, this.length);
            System.arraycopy(objArr, i10, objArr, i11, this.length - i);
            this.zero = i9;
            this.length += i2;
        }
        return this.zero + i;
    }

    private int remove(int i, int i2) {
        int i3 = this.zero + i;
        char[] cArr = this.chars;
        int i4 = i3 + i2;
        System.arraycopy(cArr, i4, cArr, i3, (this.length - i) - i2);
        Object[] objArr = this.fields;
        System.arraycopy(objArr, i4, objArr, i3, (this.length - i) - i2);
        this.length -= i2;
        return i3;
    }

    private int getCapacity() {
        return this.chars.length;
    }

    @Deprecated
    public CharSequence subSequence(int i, int i2) {
        FormattedStringBuilder formattedStringBuilder = new FormattedStringBuilder(this);
        formattedStringBuilder.zero = this.zero + i;
        formattedStringBuilder.length = i2 - i;
        return formattedStringBuilder;
    }

    public String subString(int i, int i2) {
        if (i >= 0 && i2 <= this.length && i2 >= i) {
            return new String(this.chars, this.zero + i, i2 - i);
        }
        throw new IndexOutOfBoundsException();
    }

    public String toString() {
        return new String(this.chars, this.zero, this.length);
    }

    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<FormattedStringBuilder [");
        sb.append(toString());
        sb.append("] [");
        for (int i = this.zero; i < this.zero + this.length; i++) {
            Object[] objArr = this.fields;
            if (objArr[i] == null) {
                sb.append('n');
            } else if (fieldToDebugChar.containsKey(objArr[i])) {
                sb.append(fieldToDebugChar.get(this.fields[i]));
            } else {
                sb.append('?');
            }
        }
        sb.append("]>");
        return sb.toString();
    }

    public char[] toCharArray() {
        char[] cArr = this.chars;
        int i = this.zero;
        return Arrays.copyOfRange(cArr, i, this.length + i);
    }

    public Object[] toFieldArray() {
        Object[] objArr = this.fields;
        int i = this.zero;
        return Arrays.copyOfRange(objArr, i, this.length + i);
    }

    public void setAppendableField(Object obj) {
        this.appendableField = obj;
    }

    @Override // java.lang.Appendable
    public Appendable append(CharSequence charSequence) {
        insert(this.length - this.appendOffset, charSequence, this.appendableField);
        return this;
    }

    @Override // java.lang.Appendable
    public Appendable append(CharSequence charSequence, int i, int i2) {
        insert(this.length - this.appendOffset, charSequence, i, i2, this.appendableField);
        return this;
    }

    @Override // java.lang.Appendable
    public Appendable append(char c) {
        insertChar16(this.length - this.appendOffset, c, this.appendableField);
        return this;
    }

    public boolean contentEquals(char[] cArr, Object[] objArr) {
        int length2 = cArr.length;
        int i = this.length;
        if (!(length2 == i && objArr.length == i)) {
            return false;
        }
        for (int i2 = 0; i2 < this.length; i2++) {
            char[] cArr2 = this.chars;
            int i3 = this.zero;
            if (!(cArr2[i3 + i2] == cArr[i2] && this.fields[i3 + i2] == objArr[i2])) {
                return false;
            }
        }
        return true;
    }

    public boolean contentEquals(FormattedStringBuilder formattedStringBuilder) {
        if (this.length != formattedStringBuilder.length) {
            return false;
        }
        for (int i = 0; i < this.length; i++) {
            if (!(charAt(i) == formattedStringBuilder.charAt(i) && fieldAt(i) == formattedStringBuilder.fieldAt(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Don't call #hashCode() or #equals() on a mutable.");
    }

    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Don't call #hashCode() or #equals() on a mutable.");
    }
}
