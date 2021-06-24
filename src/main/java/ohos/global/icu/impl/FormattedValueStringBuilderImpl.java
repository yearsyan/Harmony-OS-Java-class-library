package ohos.global.icu.impl;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.FieldPosition;
import java.text.Format;
import ohos.global.icu.impl.StaticUnicodeSets;
import ohos.global.icu.text.ConstrainedFieldPosition;
import ohos.global.icu.text.ListFormatter;
import ohos.global.icu.text.NumberFormat;
import ohos.global.icu.text.UFormat;
import ohos.global.icu.text.UnicodeSet;

public class FormattedValueStringBuilderImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static class SpanFieldPlaceholder {
        public Format.Field normalField;
        public UFormat.SpanField spanField;
        public Object value;
    }

    public static int findSpan(FormattedStringBuilder formattedStringBuilder, Object obj) {
        for (int i = formattedStringBuilder.zero; i < formattedStringBuilder.zero + formattedStringBuilder.length; i++) {
            if ((formattedStringBuilder.fields[i] instanceof SpanFieldPlaceholder) && ((SpanFieldPlaceholder) formattedStringBuilder.fields[i]).value.equals(obj)) {
                return i - formattedStringBuilder.zero;
            }
        }
        return -1;
    }

    public static boolean nextFieldPosition(FormattedStringBuilder formattedStringBuilder, FieldPosition fieldPosition) {
        Format.Field fieldAttribute = fieldPosition.getFieldAttribute();
        if (fieldAttribute == null) {
            if (fieldPosition.getField() == 0) {
                fieldAttribute = NumberFormat.Field.INTEGER;
            } else if (fieldPosition.getField() != 1) {
                return false;
            } else {
                fieldAttribute = NumberFormat.Field.FRACTION;
            }
        }
        if (fieldAttribute instanceof NumberFormat.Field) {
            ConstrainedFieldPosition constrainedFieldPosition = new ConstrainedFieldPosition();
            constrainedFieldPosition.constrainField(fieldAttribute);
            constrainedFieldPosition.setState(fieldAttribute, null, fieldPosition.getBeginIndex(), fieldPosition.getEndIndex());
            if (nextPosition(formattedStringBuilder, constrainedFieldPosition, null)) {
                fieldPosition.setBeginIndex(constrainedFieldPosition.getStart());
                fieldPosition.setEndIndex(constrainedFieldPosition.getLimit());
                return true;
            }
            if (fieldAttribute == NumberFormat.Field.FRACTION && fieldPosition.getEndIndex() == 0) {
                int i = formattedStringBuilder.zero;
                boolean z = false;
                while (i < formattedStringBuilder.zero + formattedStringBuilder.length) {
                    if (isIntOrGroup(formattedStringBuilder.fields[i]) || formattedStringBuilder.fields[i] == NumberFormat.Field.DECIMAL_SEPARATOR) {
                        z = true;
                    } else if (z) {
                        break;
                    }
                    i++;
                }
                fieldPosition.setBeginIndex(i - formattedStringBuilder.zero);
                fieldPosition.setEndIndex(i - formattedStringBuilder.zero);
            }
            return false;
        }
        throw new IllegalArgumentException("You must pass an instance of ohos.global.icu.text.NumberFormat.Field as your FieldPosition attribute.  You passed: " + fieldAttribute.getClass().toString());
    }

    public static AttributedCharacterIterator toCharacterIterator(FormattedStringBuilder formattedStringBuilder, Format.Field field) {
        ConstrainedFieldPosition constrainedFieldPosition = new ConstrainedFieldPosition();
        AttributedString attributedString = new AttributedString(formattedStringBuilder.toString());
        while (nextPosition(formattedStringBuilder, constrainedFieldPosition, field)) {
            Object fieldValue = constrainedFieldPosition.getFieldValue();
            if (fieldValue == null) {
                fieldValue = constrainedFieldPosition.getField();
            }
            attributedString.addAttribute(constrainedFieldPosition.getField(), fieldValue, constrainedFieldPosition.getStart(), constrainedFieldPosition.getLimit());
        }
        return attributedString.getIterator();
    }

    /* access modifiers changed from: package-private */
    public static class NullField extends Format.Field {
        static final NullField END = new NullField("end");
        private static final long serialVersionUID = 1;

        private NullField(String str) {
            super(str);
        }
    }

    public static boolean nextPosition(FormattedStringBuilder formattedStringBuilder, ConstrainedFieldPosition constrainedFieldPosition, Format.Field field) {
        int limit = formattedStringBuilder.zero + constrainedFieldPosition.getLimit();
        int i = -1;
        Object obj = null;
        while (limit <= formattedStringBuilder.zero + formattedStringBuilder.length) {
            Object obj2 = limit < formattedStringBuilder.zero + formattedStringBuilder.length ? formattedStringBuilder.fields[limit] : NullField.END;
            if (obj == null) {
                if (constrainedFieldPosition.matchesField(NumberFormat.Field.INTEGER, null) && limit > formattedStringBuilder.zero && limit - formattedStringBuilder.zero > constrainedFieldPosition.getLimit()) {
                    int i2 = limit - 1;
                    if (isIntOrGroup(formattedStringBuilder.fields[i2]) && !isIntOrGroup(obj2)) {
                        while (i2 >= formattedStringBuilder.zero && isIntOrGroup(formattedStringBuilder.fields[i2])) {
                            i2--;
                        }
                        constrainedFieldPosition.setState(NumberFormat.Field.INTEGER, null, (i2 - formattedStringBuilder.zero) + 1, limit - formattedStringBuilder.zero);
                        return true;
                    }
                }
                if (field != null && constrainedFieldPosition.matchesField(field, null) && limit > formattedStringBuilder.zero && (limit - formattedStringBuilder.zero > constrainedFieldPosition.getLimit() || constrainedFieldPosition.getField() != field)) {
                    int i3 = limit - 1;
                    if (isNumericField(formattedStringBuilder.fields[i3]) && !isNumericField(obj2)) {
                        while (i3 >= formattedStringBuilder.zero && isNumericField(formattedStringBuilder.fields[i3])) {
                            i3--;
                        }
                        constrainedFieldPosition.setState(field, null, (i3 - formattedStringBuilder.zero) + 1, limit - formattedStringBuilder.zero);
                        return true;
                    }
                }
                if (limit > formattedStringBuilder.zero) {
                    int i4 = limit - 1;
                    if (formattedStringBuilder.fields[i4] instanceof SpanFieldPlaceholder) {
                        int i5 = i4;
                        while (i5 >= formattedStringBuilder.zero && formattedStringBuilder.fields[i5] == formattedStringBuilder.fields[i4]) {
                            i5--;
                        }
                        if (handleSpan(formattedStringBuilder.fields[i4], constrainedFieldPosition, (i5 - formattedStringBuilder.zero) + 1, limit - formattedStringBuilder.zero)) {
                            return true;
                        }
                    }
                }
                if (obj2 == NumberFormat.Field.INTEGER) {
                    obj2 = null;
                }
                if (!(obj2 == null || obj2 == NullField.END)) {
                    if (obj2 instanceof SpanFieldPlaceholder) {
                        SpanFieldPlaceholder spanFieldPlaceholder = (SpanFieldPlaceholder) obj2;
                        if (constrainedFieldPosition.matchesField(spanFieldPlaceholder.normalField, null) || constrainedFieldPosition.matchesField(spanFieldPlaceholder.spanField, spanFieldPlaceholder.value)) {
                            i = limit - formattedStringBuilder.zero;
                        }
                    } else if (constrainedFieldPosition.matchesField((Format.Field) obj2, null)) {
                        i = limit - formattedStringBuilder.zero;
                    }
                    obj = obj2;
                }
            } else if (obj != obj2) {
                int i6 = limit - formattedStringBuilder.zero;
                if (obj instanceof SpanFieldPlaceholder) {
                    return true;
                }
                if (isTrimmable(obj)) {
                    i6 = trimBack(formattedStringBuilder, i6);
                }
                if (i6 <= i) {
                    limit--;
                    i = -1;
                    obj = null;
                } else {
                    if (isTrimmable(obj)) {
                        i = trimFront(formattedStringBuilder, i);
                    }
                    constrainedFieldPosition.setState((Format.Field) obj, null, i, i6);
                    return true;
                }
            } else {
                continue;
            }
            limit++;
        }
        return false;
    }

    private static boolean isIntOrGroup(Object obj) {
        return obj == NumberFormat.Field.INTEGER || obj == NumberFormat.Field.GROUPING_SEPARATOR;
    }

    private static boolean isNumericField(Object obj) {
        return obj == null || NumberFormat.Field.class.isAssignableFrom(obj.getClass());
    }

    private static boolean isTrimmable(Object obj) {
        return obj != NumberFormat.Field.GROUPING_SEPARATOR && !(obj instanceof ListFormatter.Field);
    }

    private static int trimBack(FormattedStringBuilder formattedStringBuilder, int i) {
        return StaticUnicodeSets.get(StaticUnicodeSets.Key.DEFAULT_IGNORABLES).spanBack(formattedStringBuilder, i, UnicodeSet.SpanCondition.CONTAINED);
    }

    private static int trimFront(FormattedStringBuilder formattedStringBuilder, int i) {
        return StaticUnicodeSets.get(StaticUnicodeSets.Key.DEFAULT_IGNORABLES).span(formattedStringBuilder, i, UnicodeSet.SpanCondition.CONTAINED);
    }

    private static boolean handleSpan(Object obj, ConstrainedFieldPosition constrainedFieldPosition, int i, int i2) {
        SpanFieldPlaceholder spanFieldPlaceholder = (SpanFieldPlaceholder) obj;
        if (constrainedFieldPosition.matchesField(spanFieldPlaceholder.spanField, spanFieldPlaceholder.value) && constrainedFieldPosition.getLimit() < i2) {
            constrainedFieldPosition.setState(spanFieldPlaceholder.spanField, spanFieldPlaceholder.value, i, i2);
            return true;
        } else if (!constrainedFieldPosition.matchesField(spanFieldPlaceholder.normalField, null)) {
            return false;
        } else {
            if (constrainedFieldPosition.getLimit() >= i2 && constrainedFieldPosition.getField() == spanFieldPlaceholder.normalField) {
                return false;
            }
            constrainedFieldPosition.setState(spanFieldPlaceholder.normalField, null, i, i2);
            return true;
        }
    }
}
