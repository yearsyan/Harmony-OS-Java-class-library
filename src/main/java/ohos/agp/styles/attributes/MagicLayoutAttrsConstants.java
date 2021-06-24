package ohos.agp.styles.attributes;

import ohos.agp.components.Attr;

public class MagicLayoutAttrsConstants extends ViewAttrsConstants {
    public static final String ALIGNMENT = "alignment";
    public static final String FOLD_ALIGNMENT = "fold_alignment";
    public static final String FOLD_DIRECTION = "fold_direction";
    public static final String FOLD_ENABLED = "fold_enabled";
    public static final String ORIENTATION = "orientation";

    public static class LayoutParamsAttrsConstants {
        public static final String REFERENCE_SIZE = "reference_size";
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // ohos.agp.styles.attributes.ViewAttrsConstants
    public Attr.AttrType getType(String str) {
        char c;
        switch (str.hashCode()) {
            case -1900007307:
                if (str.equals(LayoutParamsAttrsConstants.REFERENCE_SIZE)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1439500848:
                if (str.equals("orientation")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -968381181:
                if (str.equals(FOLD_ENABLED)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -501851355:
                if (str.equals(FOLD_ALIGNMENT)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1062650049:
                if (str.equals(FOLD_DIRECTION)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1767875043:
                if (str.equals("alignment")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            return Attr.AttrType.BOOLEAN;
        }
        if (c == 1 || c == 2 || c == 3 || c == 4 || c == 5) {
            return Attr.AttrType.INT;
        }
        return super.getType(str);
    }
}
