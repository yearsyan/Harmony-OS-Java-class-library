package ohos.multimodalinput.standard;

import ohos.multimodalinput.event.MultimodalEvent;

public interface TelephoneEventHandle extends StandardizedEventHandle {
    boolean onAnswer(MultimodalEvent multimodalEvent);

    boolean onHangup(MultimodalEvent multimodalEvent);

    boolean onRefuse(MultimodalEvent multimodalEvent);

    boolean onTelephoneControl(MultimodalEvent multimodalEvent);
}
