package ohos.multimodalinput.eventimpl;

import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.Optional;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MultimodalEvent;

public class MultimodalEventFactory {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218114065, "MultimodalEventFactory");

    private MultimodalEventFactory() {
    }

    public static Optional<MultimodalEvent> createEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            HiLog.error(LOG_LABEL, "invalid input android keyEvent", new Object[0]);
            return Optional.empty();
        }
        int source = motionEvent.getSource();
        if ((source & 4098) == 4098) {
            return Optional.of(new TouchEventImpl(motionEvent));
        }
        if ((source & 8194) == 8194) {
            return Optional.of(new MouseEventImpl(motionEvent));
        }
        if ((source & 4194304) == 4194304) {
            return Optional.of(new RotationEventImpl(motionEvent));
        }
        if ((source & 16384) == 16384) {
            return Optional.of(new StylusEventImpl(motionEvent));
        }
        HiLog.warn(LOG_LABEL, "unsupported event source: %{public}d", Integer.valueOf(source));
        return Optional.empty();
    }

    public static Optional<MultimodalEvent> createEvent(KeyEvent keyEvent) {
        if (keyEvent == null) {
            HiLog.error(LOG_LABEL, "invalid input android keyEvent", new Object[0]);
            return Optional.empty();
        }
        int keyCode = keyEvent.getKeyCode();
        if (KeyBoardEventImpl.isKeyBoardKeyCode(keyCode)) {
            return Optional.of(new KeyBoardEventImpl(keyEvent));
        }
        if (BuiltinKeyEventImpl.isBuiltInKeyCode(keyCode)) {
            return Optional.of(new BuiltinKeyEventImpl(keyEvent));
        }
        if (StylusEventImpl.isStylusKeyCode(keyCode)) {
            return Optional.of(new StylusEventImpl(keyEvent));
        }
        return Optional.of(new KeyEventImpl(keyEvent));
    }

    public static Optional<MultimodalEvent> createStandardizedEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            HiLog.error(LOG_LABEL, "invalid input android keyEvent", new Object[0]);
            return Optional.empty();
        }
        int source = motionEvent.getSource();
        if ((source & 4098) == 4098) {
            return Optional.of(new TouchEventImpl(motionEvent));
        }
        if ((source & 8194) == 8194) {
            TouchEventImpl touchEventImpl = new TouchEventImpl(motionEvent);
            touchEventImpl.setMultimodalEvent(new MouseEventImpl(motionEvent));
            return Optional.of(touchEventImpl);
        } else if ((source & 4194304) == 4194304) {
            TouchEventImpl touchEventImpl2 = new TouchEventImpl(motionEvent);
            touchEventImpl2.setMultimodalEvent(new RotationEventImpl(motionEvent));
            return Optional.of(touchEventImpl2);
        } else if ((source & 16384) == 16384) {
            TouchEventImpl touchEventImpl3 = new TouchEventImpl(motionEvent);
            touchEventImpl3.setMultimodalEvent(new StylusEventImpl(motionEvent));
            return Optional.of(touchEventImpl3);
        } else {
            HiLog.warn(LOG_LABEL, "unsupported event source: %{public}d", Integer.valueOf(source));
            return Optional.empty();
        }
    }

    public static Optional<MultimodalEvent> createStandardizedEvent(KeyEvent keyEvent) {
        if (!StylusEventImpl.isStylusKeyCode(keyEvent.getKeyCode())) {
            return createEvent(keyEvent);
        }
        TouchEventImpl touchEventImpl = new TouchEventImpl(null);
        touchEventImpl.setMultimodalEvent(new StylusEventImpl(keyEvent));
        return Optional.of(touchEventImpl);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002c, code lost:
        if (r1.equals("KeyBoardEvent") == false) goto L_0x0039;
     */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0050  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<ohos.multimodalinput.event.MultimodalEvent> createEvent(ohos.utils.Parcel r6) {
        /*
            r0 = 0
            if (r6 != 0) goto L_0x0011
            ohos.hiviewdfx.HiLogLabel r6 = ohos.multimodalinput.eventimpl.MultimodalEventFactory.LOG_LABEL
            java.lang.Object[] r0 = new java.lang.Object[r0]
            java.lang.String r1 = "invalid input parcel"
            ohos.hiviewdfx.HiLog.error(r6, r1, r0)
            java.util.Optional r6 = java.util.Optional.empty()
            return r6
        L_0x0011:
            java.lang.String r1 = r6.readString()
            r2 = -1
            int r3 = r1.hashCode()
            r4 = 541813659(0x204b6b9b, float:1.7230372E-19)
            r5 = 1
            if (r3 == r4) goto L_0x002f
            r4 = 665678579(0x27ad72f3, float:4.8141774E-15)
            if (r3 == r4) goto L_0x0026
            goto L_0x0039
        L_0x0026:
            java.lang.String r3 = "KeyBoardEvent"
            boolean r1 = r1.equals(r3)
            if (r1 == 0) goto L_0x0039
            goto L_0x003a
        L_0x002f:
            java.lang.String r0 = "KeyEvent"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0039
            r0 = r5
            goto L_0x003a
        L_0x0039:
            r0 = r2
        L_0x003a:
            if (r0 == 0) goto L_0x0050
            if (r0 == r5) goto L_0x0043
            java.util.Optional r6 = java.util.Optional.empty()
            return r6
        L_0x0043:
            ohos.utils.Sequenceable$Producer<ohos.multimodalinput.event.KeyEvent> r0 = ohos.multimodalinput.eventimpl.KeyEventImpl.PRODUCER
            java.lang.Object r6 = r0.createFromParcel(r6)
            ohos.multimodalinput.event.KeyEvent r6 = (ohos.multimodalinput.event.KeyEvent) r6
            java.util.Optional r6 = java.util.Optional.of(r6)
            return r6
        L_0x0050:
            ohos.utils.Sequenceable$Producer<ohos.multimodalinput.event.KeyBoardEvent> r0 = ohos.multimodalinput.eventimpl.KeyBoardEventImpl.PRODUCER
            java.lang.Object r6 = r0.createFromParcel(r6)
            ohos.multimodalinput.event.KeyBoardEvent r6 = (ohos.multimodalinput.event.KeyBoardEvent) r6
            java.util.Optional r6 = java.util.Optional.of(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.multimodalinput.eventimpl.MultimodalEventFactory.createEvent(ohos.utils.Parcel):java.util.Optional");
    }

    public static Optional<KeyEvent> getHostKeyEvent(MultimodalEvent multimodalEvent) {
        if (multimodalEvent == null) {
            HiLog.error(LOG_LABEL, "event is null", new Object[0]);
            return Optional.empty();
        }
        KeyEvent keyEvent = null;
        if (multimodalEvent instanceof KeyEventImpl) {
            keyEvent = ((KeyEventImpl) multimodalEvent).getHostKeyEvent();
        } else if (multimodalEvent instanceof KeyBoardEventImpl) {
            keyEvent = ((KeyBoardEventImpl) multimodalEvent).getHostKeyEvent();
        } else {
            HiLog.error(LOG_LABEL, "invalid multimodal event, source:%{public}d", Integer.valueOf(multimodalEvent.getSourceDevice()));
        }
        if (keyEvent == null) {
            return Optional.empty();
        }
        return Optional.of(keyEvent);
    }

    public static Optional<MotionEvent> getHostMotionEvent(MultimodalEvent multimodalEvent) {
        if (multimodalEvent == null) {
            HiLog.error(LOG_LABEL, "event is null", new Object[0]);
            return Optional.empty();
        }
        MotionEvent motionEvent = null;
        if (multimodalEvent instanceof TouchEventImpl) {
            motionEvent = ((TouchEventImpl) multimodalEvent).getHostMotionEvent();
        } else if (multimodalEvent instanceof MouseEventImpl) {
            motionEvent = ((MouseEventImpl) multimodalEvent).getHostMotionEvent();
        } else if (multimodalEvent instanceof RotationEventImpl) {
            motionEvent = ((RotationEventImpl) multimodalEvent).getHostMotionEvent();
        } else {
            HiLog.error(LOG_LABEL, "invalid multimodal event, source:%{public}d", Integer.valueOf(multimodalEvent.getSourceDevice()));
        }
        if (motionEvent == null) {
            return Optional.empty();
        }
        return Optional.of(motionEvent);
    }

    public static Optional<ohos.multimodalinput.event.KeyEvent> createKeyEvent(int i, int i2) {
        if ((i != 0 && i != 1) || i2 <= 0 || i2 > 10008) {
            HiLog.error(LOG_LABEL, "unsupported create keyevent, action: %{public}d, keyCode: %{public}d", Integer.valueOf(i), Integer.valueOf(i2));
            return Optional.empty();
        } else if (KeyBoardEventImpl.isOhosKeyBoardKeyCode(i2)) {
            return Optional.of(new KeyBoardEventImpl(i, i2));
        } else {
            if (BuiltinKeyEventImpl.isOhosBuiltInKeyCode(i2)) {
                return Optional.of(new BuiltinKeyEventImpl(i, i2));
            }
            return Optional.of(new KeyEventImpl(i, i2));
        }
    }
}
