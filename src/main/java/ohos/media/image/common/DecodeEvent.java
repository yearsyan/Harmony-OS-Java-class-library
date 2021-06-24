package ohos.media.image.common;

public enum DecodeEvent {
    EVENT_COMPLETE_DECODE(0),
    EVENT_PARTIAL_DECODE(1),
    EVENT_HEADER_DECODE(2),
    EVENT_LAST(3);
    
    private final int decodeEventValue;

    private DecodeEvent(int i) {
        this.decodeEventValue = i;
    }

    public static DecodeEvent getDecodeEvent(int i) {
        DecodeEvent[] values = values();
        for (DecodeEvent decodeEvent : values) {
            if (decodeEvent.getValue() == i) {
                return decodeEvent;
            }
        }
        return EVENT_LAST;
    }

    public int getValue() {
        return this.decodeEventValue;
    }
}
