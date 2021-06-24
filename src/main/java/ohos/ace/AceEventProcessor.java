package ohos.ace;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import ohos.devtools.JLogConstants;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.MouseEvent;
import ohos.multimodalinput.event.TouchEvent;

public class AceEventProcessor {
    private static final int BYTES_PER_FIELD = 8;
    private static final float EPSINON = 1.0E-5f;
    private static final int MOUSE_DATA_FIELD_COUNT = 13;
    private static final int POINTER_DATA_FIELD_COUNT = 10;
    private static final Map<Integer, Integer> Z2A_KEYMAP = new HashMap();

    public static int actionToActionType(int i) {
        if (i == 1) {
            return 4;
        }
        if (i == 2) {
            return 6;
        }
        if (i == 4) {
            return 4;
        }
        if (i == 5) {
            return 6;
        }
        if (i == 3) {
            return 5;
        }
        return i == 6 ? 0 : -1;
    }

    public static int keyActionToActionType(boolean z) {
        return z ? 0 : 1;
    }

    private static class ActionType {
        static final int ADD = 1;
        static final int CANCEL = 0;
        static final int DOWN = 4;
        static final int HOVER = 3;
        static final int MOVE = 5;
        static final int REMOVE = 2;
        static final int UP = 6;

        private ActionType() {
        }
    }

    private static class KeyActionType {
        static final int DOWN = 0;
        static final int UP = 1;

        private KeyActionType() {
        }
    }

    private static class KeyCodeOfCar {
        static final int KEYCODE_CUSTOM1 = 10001;
        static final int KEYCODE_LAUNCHER_MENU = 10009;
        static final int KEYCODE_LEFT_KNOB = 10004;
        static final int KEYCODE_LEFT_KNOB_ROLL_DOWN = 10003;
        static final int KEYCODE_LEFT_KNOB_ROLL_UP = 10002;
        static final int KEYCODE_RIGHT_KNOB = 10007;
        static final int KEYCODE_RIGHT_KNOB_ROLL_DOWN = 10006;
        static final int KEYCODE_RIGHT_KNOB_ROLL_UP = 10005;
        static final int KEYCODE_VOICE_SOURCE_SWITCH = 10008;

        private KeyCodeOfCar() {
        }
    }

    static {
        Map<Integer, Integer> map = Z2A_KEYMAP;
        Integer valueOf = Integer.valueOf((int) KeyEvent.KEY_MENU);
        map.put(valueOf, 82);
        Z2A_KEYMAP.put(1, 3);
        Z2A_KEYMAP.put(2, 4);
        Z2A_KEYMAP.put(2012, 19);
        Z2A_KEYMAP.put(2013, 20);
        Z2A_KEYMAP.put(2014, 21);
        Z2A_KEYMAP.put(2015, 22);
        Z2A_KEYMAP.put(2016, 23);
        Z2A_KEYMAP.put(16, 24);
        Z2A_KEYMAP.put(17, 25);
        Z2A_KEYMAP.put(18, 26);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_ENTER), 66);
        Z2A_KEYMAP.put(3, 5);
        Z2A_KEYMAP.put(4, 6);
        Z2A_KEYMAP.put(5, 28);
        Z2A_KEYMAP.put(6, 79);
        Z2A_KEYMAP.put(7, 80);
        Z2A_KEYMAP.put(8, 83);
        Z2A_KEYMAP.put(9, 84);
        Z2A_KEYMAP.put(10, 85);
        Z2A_KEYMAP.put(11, 86);
        Z2A_KEYMAP.put(12, 87);
        Z2A_KEYMAP.put(13, 88);
        Z2A_KEYMAP.put(14, 89);
        Z2A_KEYMAP.put(15, 90);
        Z2A_KEYMAP.put(19, 27);
        Z2A_KEYMAP.put(20, 231);
        Z2A_KEYMAP.put(40, 221);
        Z2A_KEYMAP.put(41, 220);
        Z2A_KEYMAP.put(2000, 7);
        Z2A_KEYMAP.put(2001, 8);
        Z2A_KEYMAP.put(2002, 9);
        Z2A_KEYMAP.put(2003, 10);
        Z2A_KEYMAP.put(2004, 11);
        Z2A_KEYMAP.put(2005, 12);
        Z2A_KEYMAP.put(2006, 13);
        Z2A_KEYMAP.put(2007, 14);
        Z2A_KEYMAP.put(2008, 15);
        Z2A_KEYMAP.put(2009, 16);
        Z2A_KEYMAP.put(2010, 17);
        Z2A_KEYMAP.put(2011, 18);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_A), 29);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_B), 30);
        Z2A_KEYMAP.put(2019, 31);
        Z2A_KEYMAP.put(2020, 32);
        Z2A_KEYMAP.put(2021, 33);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F), 34);
        Z2A_KEYMAP.put(2023, 35);
        Z2A_KEYMAP.put(2024, 36);
        Z2A_KEYMAP.put(2025, 37);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_J), 38);
        Z2A_KEYMAP.put(2027, 39);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_L), 40);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_M), 41);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_N), 42);
        Z2A_KEYMAP.put(2031, 43);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_P), 44);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_Q), 45);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_R), 46);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_S), 47);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_T), 48);
        Z2A_KEYMAP.put(2037, 49);
        Z2A_KEYMAP.put(2038, 50);
        Z2A_KEYMAP.put(2039, 51);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_X), 52);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_Y), 53);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_Z), 54);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_COMMA), 55);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_PERIOD), 56);
        Z2A_KEYMAP.put(2045, 57);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_ALT_RIGHT), 58);
        Z2A_KEYMAP.put(2047, 59);
        Z2A_KEYMAP.put(2048, 60);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_TAB), 61);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_SPACE), 62);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_SYM), 63);
        Z2A_KEYMAP.put(2052, 64);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_ENVELOPE), 65);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_DEL), 67);
        Z2A_KEYMAP.put(2056, 68);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MINUS), 69);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_EQUALS), 70);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_LEFT_BRACKET), 71);
        Z2A_KEYMAP.put(2060, 72);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_BACKSLASH), 73);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_SEMICOLON), 74);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_APOSTROPHE), 75);
        Z2A_KEYMAP.put(2064, 76);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_AT), 77);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_PLUS), 81);
        Z2A_KEYMAP.put(valueOf, 82);
        Z2A_KEYMAP.put(2068, 92);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_PAGE_DOWN), 93);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_ESCAPE), 111);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_FORWARD_DEL), 112);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_CTRL_LEFT), 113);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_CTRL_RIGHT), 114);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_CAPS_LOCK), 115);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_SCROLL_LOCK), 116);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_META_LEFT), 117);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_META_RIGHT), 118);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_FUNCTION), 119);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_SYSRQ), 120);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_BREAK), 121);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MOVE_HOME), 122);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MOVE_END), 123);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_INSERT), 124);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_FORWARD), 125);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MEDIA_PLAY), 126);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MEDIA_PAUSE), 127);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MEDIA_CLOSE), 128);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MEDIA_EJECT), 129);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_MEDIA_RECORD), 130);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F1), 131);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F2), 132);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F3), 133);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F4), 134);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F5), 135);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F6), 136);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F7), 137);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F8), 138);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F9), 139);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F10), 140);
        Z2A_KEYMAP.put(2100, 141);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_F12), 142);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUM_LOCK), 143);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_0), 144);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_1), 145);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_2), 146);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_3), 147);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_4), 148);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_5), 149);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_6), 150);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_7), 151);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_8), 152);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_9), 153);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_DIVIDE), 154);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_MULTIPLY), 155);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_SUBTRACT), 156);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_ADD), 157);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_DOT), 158);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_COMMA), 159);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_ENTER), 160);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_EQUALS), 161);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_LEFT_PAREN), 162);
        Z2A_KEYMAP.put(Integer.valueOf((int) KeyEvent.KEY_NUMPAD_RIGHT_PAREN), 163);
        Z2A_KEYMAP.put(21, 10001);
        Z2A_KEYMAP.put(10001, 10002);
        Z2A_KEYMAP.put(10002, 10003);
        Z2A_KEYMAP.put(10003, 10004);
        Z2A_KEYMAP.put(10004, 10005);
        Z2A_KEYMAP.put(10005, 10006);
        Z2A_KEYMAP.put(10006, 10007);
        Z2A_KEYMAP.put(10007, 10008);
        Z2A_KEYMAP.put(10008, Integer.valueOf((int) JLogConstants.JLID_COAUTH_DATA_FIRST_FRAME_RECV));
    }

    public static ByteBuffer processTouchEvent(TouchEvent touchEvent, TouchInfo touchInfo) {
        int pointerCount = touchEvent.getPointerCount();
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(pointerCount * 10 * 8);
        allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
        int actionToActionType = actionToActionType(touchEvent.getAction());
        if (actionToActionType == 4 || actionToActionType == 6) {
            addTouchEventToBuffer(touchEvent, allocateDirect, touchInfo);
        } else {
            for (int i = 0; i < pointerCount; i++) {
                addTouchEventToBuffer(touchEvent, allocateDirect, touchInfo);
            }
        }
        if (allocateDirect.position() % 80 == 0) {
            return allocateDirect;
        }
        throw new AssertionError("Packet position is not on field boundary");
    }

    public static ByteBuffer processMouseEvent(MouseEvent mouseEvent) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(104);
        allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
        addMouseEventToBuffer(mouseEvent, allocateDirect);
        if (allocateDirect.position() % 104 == 0) {
            return allocateDirect;
        }
        throw new AssertionError("Packet position is not on field boundary");
    }

    private static void addTouchEventToBuffer(TouchEvent touchEvent, ByteBuffer byteBuffer, TouchInfo touchInfo) {
        float f;
        int actionToActionType = actionToActionType(touchEvent.getAction());
        if (actionToActionType != -1) {
            int index = touchEvent.getIndex();
            byteBuffer.putLong(touchEvent.getOccurredTime() * 1000);
            byteBuffer.putLong((long) actionToActionType);
            byteBuffer.putLong((long) touchEvent.getPointerId(index));
            float f2 = 1.0f;
            if (touchInfo.getScaleX() < -1.0E-5f || touchInfo.getScaleX() > EPSINON) {
                f = touchInfo.getScaleX();
            } else {
                f = 1.0f;
            }
            if (touchInfo.getScaleY() < -1.0E-5f || touchInfo.getScaleY() > EPSINON) {
                f2 = touchInfo.getScaleY();
            }
            float offsetX = touchInfo.getOffsetX();
            float offsetY = touchInfo.getOffsetY();
            byteBuffer.putDouble((double) ((touchEvent.getPointerPosition(index).getX() - offsetX) / f));
            byteBuffer.putDouble((double) ((touchEvent.getPointerPosition(index).getY() - offsetY) / f2));
            byteBuffer.putDouble((double) touchEvent.getForcePrecision());
            byteBuffer.putDouble((double) touchEvent.getMaxForce());
            byteBuffer.putDouble((double) touchEvent.getRadius(index));
            byteBuffer.putLong((long) touchEvent.getSourceDevice());
            byteBuffer.putLong((long) touchEvent.getInputDeviceId());
        }
    }

    private static void addMouseEventToBuffer(MouseEvent mouseEvent, ByteBuffer byteBuffer) {
        int action = mouseEvent.getAction();
        int actionButton = mouseEvent.getActionButton();
        int pressedButtons = mouseEvent.getPressedButtons();
        MmiPoint cursor = mouseEvent.getCursor();
        byteBuffer.putDouble((double) cursor.getX());
        byteBuffer.putDouble((double) cursor.getY());
        byteBuffer.putDouble((double) cursor.getZ());
        byteBuffer.putDouble((double) mouseEvent.getCursorDelta(0));
        byteBuffer.putDouble((double) mouseEvent.getCursorDelta(1));
        byteBuffer.putDouble((double) mouseEvent.getCursorDelta(2));
        byteBuffer.putDouble((double) mouseEvent.getScrollingDelta(0));
        byteBuffer.putDouble((double) mouseEvent.getScrollingDelta(1));
        byteBuffer.putDouble((double) mouseEvent.getScrollingDelta(2));
        byteBuffer.putLong((long) action);
        byteBuffer.putLong((long) actionButton);
        byteBuffer.putLong((long) pressedButtons);
        byteBuffer.putLong(mouseEvent.getOccurredTime() * 1000);
    }

    public static int keyToKeyCode(int i) {
        return Z2A_KEYMAP.containsKey(Integer.valueOf(i)) ? Z2A_KEYMAP.get(Integer.valueOf(i)).intValue() : i;
    }
}
