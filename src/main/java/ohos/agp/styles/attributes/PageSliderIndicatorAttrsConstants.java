package ohos.agp.styles.attributes;

import ohos.agp.components.Attr;

public class PageSliderIndicatorAttrsConstants extends ViewAttrsConstants {
    public static final String BACKGROUNG_END_COLOR = "background_end_color";
    public static final String BACKGROUNG_START_COLOR = "background_start_color";
    public static final String ITEM_OFFSET = "item_offset";
    public static final String NORMAL_ELEMENT = "normal_element";
    public static final String SELECTED_DOT_COLOR = "selected_dot_color";
    public static final String SELECTED_ELEMENT = "selected_element";
    public static final String UNSELECTED_DOT_COLOR = "unselected_dot_color";

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // ohos.agp.styles.attributes.ViewAttrsConstants
    public Attr.AttrType getType(String str) {
        char c;
        switch (str.hashCode()) {
            case -1866617948:
                if (str.equals(NORMAL_ELEMENT)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1729305527:
                if (str.equals(SELECTED_DOT_COLOR)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1648428754:
                if (str.equals(BACKGROUNG_END_COLOR)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -719724894:
                if (str.equals(UNSELECTED_DOT_COLOR)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -509879489:
                if (str.equals(ITEM_OFFSET)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 299232600:
                if (str.equals(SELECTED_ELEMENT)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1753637813:
                if (str.equals(BACKGROUNG_START_COLOR)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                return Attr.AttrType.ELEMENT;
            case 2:
                return Attr.AttrType.DIMENSION;
            case 3:
            case 4:
            case 5:
            case 6:
                return Attr.AttrType.COLOR;
            default:
                return super.getType(str);
        }
    }
}
