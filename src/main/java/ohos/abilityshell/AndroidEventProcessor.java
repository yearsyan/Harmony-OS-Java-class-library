package ohos.abilityshell;

import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.Optional;
import ohos.multimodalinput.event.MultimodalEvent;
import ohos.multimodalinput.eventimpl.MultimodalEventFactory;
import ohos.multimodalinput.standard.StandardizedEventProcessorEx;

public class AndroidEventProcessor {
    public static Optional<MultimodalEvent> convertTouchEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return Optional.empty();
        }
        return MultimodalEventFactory.createEvent(motionEvent);
    }

    public static Optional<MultimodalEvent> convertKeyEvent(KeyEvent keyEvent) {
        if (keyEvent == null) {
            return Optional.empty();
        }
        return MultimodalEventFactory.createEvent(keyEvent);
    }

    public static boolean dispatchToStandardEvent(Object obj, InputEvent inputEvent) {
        return StandardizedEventProcessorEx.dispatch(obj, inputEvent);
    }
}
