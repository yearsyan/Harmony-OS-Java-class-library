package ohos.devicesecmgr;

import java.util.HashMap;

public class Legacy {
    private static final int CHILDREN_WATCH = 133;
    private static final int DESKTOP_PC = 11;
    private static final int LAPTOP = 12;
    private static final int LITE_HARMONY_L0 = 240;
    private static final int LITE_HARMONY_L1 = 241;
    private static final int LOUD_SPEAKER = 174;
    private static final int PROJECTOR = 152;
    private static final int SCREEN_THROWER = 177;
    private static final int SET_TOP_BOX = 3;
    private static final int SMART_CAR = 131;
    private static final int SMART_PAD = 17;
    private static final int SMART_PHONE = 14;
    private static final int SMART_SPEAKER = 10;
    private static final int SMART_TV = 156;
    private static final int SMART_WATCH = 109;
    private static final int THIRD_LAPTOP = 160;
    private static final int THIRD_PAD = 158;
    private static final int THIRD_PHONE = 157;
    private static final int THIRD_SPEAKERS = 167;
    private static final int THIRD_TV = 46;
    private static final HashMap<Integer, Integer> TRANS_MAP = new HashMap<>();
    private static final int UNKNOWN_TYPE = 0;
    private static final int WHITEBOARD = 178;

    static {
        TRANS_MAP.put(11, 2);
        TRANS_MAP.put(12, 2);
        TRANS_MAP.put(14, 4);
        TRANS_MAP.put(17, 4);
        TRANS_MAP.put(109, 2);
        TRANS_MAP.put(133, 1);
        TRANS_MAP.put(156, 3);
        TRANS_MAP.put(240, 1);
        TRANS_MAP.put(241, 1);
    }

    public static int legacyTrans(int i) {
        HashMap<Integer, Integer> hashMap = TRANS_MAP;
        if (hashMap == null) {
            return 0;
        }
        return hashMap.get(Integer.valueOf(i)).intValue();
    }
}
