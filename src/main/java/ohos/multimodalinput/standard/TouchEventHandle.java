package ohos.multimodalinput.standard;

import ohos.multimodalinput.event.TouchEvent;

public interface TouchEventHandle extends StandardizedEventHandle {
    boolean onTouch(TouchEvent touchEvent);
}
