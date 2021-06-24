package ohos.agp.styles.attributes;

import ohos.agp.components.Attr;

public class SlideDrawerAttrsConstants extends ViewAttrsConstants {
    public static final String BOTTOM_DISPLAY_MODE = "bottom_display_mode";
    public static final String END_DISPLAY_MODE = "end_display_mode";
    public static final String MAX_SLIDE_DISTANCE = "max_slide_distance";
    public static final String OPEN_THRESHOLD = "open_threshold";
    public static final String SLIDE_ENABLED = "slide_enabled";
    public static final String STARTUP_DIRECTION = "startup_direction";
    public static final String START_DISPLAY_MODE = "start_display_mode";
    public static final String TOP_DISPLAY_MODE = "top_display_mode";
    public static final String TOUCH_FOR_CLOSE = "touch_for_close";

    public static class SlideDrawerConfigAttrsConstants {
        public static final String SLIDE_DIRECTION = "slide_direction";
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // ohos.agp.styles.attributes.ViewAttrsConstants
    public Attr.AttrType getType(String str) {
        char c;
        switch (str.hashCode()) {
            case -1914032461:
                if (str.equals(SLIDE_ENABLED)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1847699523:
                if (str.equals(START_DISPLAY_MODE)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1761922890:
                if (str.equals(OPEN_THRESHOLD)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1202036764:
                if (str.equals(END_DISPLAY_MODE)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1107234668:
                if (str.equals(BOTTOM_DISPLAY_MODE)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -534299222:
                if (str.equals(TOP_DISPLAY_MODE)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 444885506:
                if (str.equals(TOUCH_FOR_CLOSE)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 905063166:
                if (str.equals(MAX_SLIDE_DISTANCE)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1020070973:
                if (str.equals(STARTUP_DIRECTION)) {
                    c = 2;
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
                return Attr.AttrType.BOOLEAN;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return Attr.AttrType.INT;
            case 7:
                return Attr.AttrType.FLOAT;
            case '\b':
                return Attr.AttrType.DIMENSION;
            default:
                return super.getType(str);
        }
    }
}
