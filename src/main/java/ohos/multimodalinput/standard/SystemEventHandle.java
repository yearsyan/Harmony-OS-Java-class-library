package ohos.multimodalinput.standard;

import ohos.multimodalinput.event.MultimodalEvent;

public interface SystemEventHandle extends StandardizedEventHandle {
    boolean onClosePage(MultimodalEvent multimodalEvent);

    boolean onMute(MultimodalEvent multimodalEvent);
}
