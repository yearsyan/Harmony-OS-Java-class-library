package ohos.multimodalinput.standard;

import ohos.multimodalinput.event.MultimodalEvent;

public interface MediaEventHandle extends StandardizedEventHandle {
    boolean onMediaControl(MultimodalEvent multimodalEvent);

    boolean onPause(MultimodalEvent multimodalEvent);

    boolean onPlay(MultimodalEvent multimodalEvent);
}
