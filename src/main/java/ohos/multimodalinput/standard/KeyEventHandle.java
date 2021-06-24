package ohos.multimodalinput.standard;

import ohos.multimodalinput.event.KeyEvent;

public interface KeyEventHandle extends StandardizedEventHandle {
    boolean onKey(KeyEvent keyEvent);
}
