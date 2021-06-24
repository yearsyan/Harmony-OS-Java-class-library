package ohos.multimodalinput.standard;

import ohos.multimodalinput.event.MultimodalEvent;

public interface CommonEventHandle extends StandardizedEventHandle {
    boolean onBack(MultimodalEvent multimodalEvent);

    boolean onCancel(MultimodalEvent multimodalEvent);

    boolean onCopy(MultimodalEvent multimodalEvent);

    boolean onCut(MultimodalEvent multimodalEvent);

    boolean onEnter(MultimodalEvent multimodalEvent);

    boolean onNext(MultimodalEvent multimodalEvent);

    boolean onPaste(MultimodalEvent multimodalEvent);

    boolean onPrevious(MultimodalEvent multimodalEvent);

    boolean onPrint(MultimodalEvent multimodalEvent);

    boolean onRefresh(MultimodalEvent multimodalEvent);

    boolean onSend(MultimodalEvent multimodalEvent);

    boolean onShowMenu(MultimodalEvent multimodalEvent);

    boolean onStartDrag(MultimodalEvent multimodalEvent);

    boolean onUndo(MultimodalEvent multimodalEvent);
}
