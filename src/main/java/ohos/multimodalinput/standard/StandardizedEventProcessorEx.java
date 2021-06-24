package ohos.multimodalinput.standard;

import android.view.InputEvent;

public class StandardizedEventProcessorEx {
    public static boolean dispatch(Object obj, InputEvent inputEvent) {
        StandardizedEventProcessor instance = StandardizedEventProcessor.getInstance();
        if (instance != null) {
            return instance.dispatch(obj, inputEvent);
        }
        return false;
    }
}
