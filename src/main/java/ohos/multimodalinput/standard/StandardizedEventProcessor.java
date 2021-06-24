package ohos.multimodalinput.standard;

import android.os.Handler;
import android.os.Message;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.List;
import ohos.aafwk.ability.Ability;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MultimodalEvent;
import ohos.multimodalinput.event.TouchEvent;
import ohos.multimodalinput.eventimpl.MultimodalEventFactory;

public class StandardizedEventProcessor {
    private static final long DOUBLE_TAP_TIMEOUT = 300;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218114065, "StandardizedEventProcessor");
    private static final long LONG_TAP_TIMEOUT = 500;
    private static final int MSG_HEADSETHOOK_DOUBLE_TAP = 3;
    private static final int MSG_HEADSETHOOK_LONG_TAP = 2;
    private static final int MSG_HEADSETHOOK_SINGLE_TAP = 1;
    private static StandardizedEventProcessor processor;
    private Ability abilityKey;
    private List<CommonEventHandle> commonHandleList = new ArrayList();
    private MultimodalStandardizedEventManager eventManager = MultimodalStandardizedEventManager.getInstance();
    private StandardizedEventHandler handler = new StandardizedEventHandler();
    private boolean isHeadSetSingleTap = false;
    private boolean isHeadSetUp = false;
    private KeyEventTracker keyEventTracker = new KeyEventTracker();
    private List<KeyEventHandle> keyHandleList = new ArrayList();
    private List<MediaEventHandle> mediaHandleList = new ArrayList();
    private KeyEvent specialKeyEvent;
    private List<SystemEventHandle> systemHandleList = new ArrayList();
    private List<TelephoneEventHandle> telephoneHandleList = new ArrayList();
    private List<TouchEventHandle> touchHandleList = new ArrayList();

    private StandardizedEventProcessor() {
    }

    public static synchronized StandardizedEventProcessor getInstance() {
        StandardizedEventProcessor standardizedEventProcessor;
        synchronized (StandardizedEventProcessor.class) {
            if (processor == null) {
                processor = new StandardizedEventProcessor();
            }
            standardizedEventProcessor = processor;
        }
        return standardizedEventProcessor;
    }

    public boolean dispatch(Object obj, InputEvent inputEvent) {
        if (updateEventHandle(obj)) {
            HiLog.info(LOG_LABEL, "Find no registered StandardizedEventHandle.", new Object[0]);
            return false;
        } else if (inputEvent instanceof MotionEvent) {
            return dispatchMotionEvent((MotionEvent) inputEvent);
        } else {
            if (inputEvent instanceof KeyEvent) {
                return dispatchKeyEvent((KeyEvent) inputEvent);
            }
            return false;
        }
    }

    private boolean dispatchKeyEvent(KeyEvent keyEvent) {
        boolean z;
        if (keyEvent.getAction() == 0) {
            z = handleSpecialKeyDown(keyEvent);
            if (!z) {
                z = handleKeyDown(keyEvent);
            }
        } else {
            z = handleSpecialKeyUp(keyEvent);
            if (!z) {
                z = handleKeyUp(keyEvent);
            }
        }
        return !z ? handleKeyEvent(keyEvent) : z;
    }

    private boolean handleKeyDown(KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() != 0) {
            return false;
        }
        this.keyEventTracker.startEventTracking(keyEvent);
        boolean dispatchShortcutKeyDownEvent = dispatchShortcutKeyDownEvent(keyEvent);
        if (dispatchShortcutKeyDownEvent) {
            this.keyEventTracker.consumeEvent();
        }
        return dispatchShortcutKeyDownEvent;
    }

    private boolean handleKeyUp(KeyEvent keyEvent) {
        if (!this.keyEventTracker.isConsumed()) {
            return false;
        }
        this.keyEventTracker.resetTrackingState();
        return true;
    }

    private boolean handleKeyEvent(KeyEvent keyEvent) {
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(keyEvent).get();
        boolean z = false;
        if (multimodalEvent instanceof TouchEvent) {
            for (TouchEventHandle touchEventHandle : this.touchHandleList) {
                z |= touchEventHandle.onTouch((TouchEvent) multimodalEvent);
            }
            return z;
        }
        if (multimodalEvent instanceof ohos.multimodalinput.event.KeyEvent) {
            for (KeyEventHandle keyEventHandle : this.keyHandleList) {
                z |= keyEventHandle.onKey((ohos.multimodalinput.event.KeyEvent) multimodalEvent);
            }
        }
        return z;
    }

    private boolean dispatchShortcutKeyDownEvent(KeyEvent keyEvent) {
        if (noMetaKeyPressed(keyEvent)) {
            return handleShortcutKeyDownWithoutMeta(keyEvent);
        }
        return handleShortcutKeyDownWithMeta(keyEvent);
    }

    private boolean handleShortcutKeyDownWithoutMeta(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(keyEvent).get();
        boolean z = false;
        if (keyCode == 4) {
            for (CommonEventHandle commonEventHandle : this.commonHandleList) {
                z |= commonEventHandle.onBack(multimodalEvent);
            }
            for (SystemEventHandle systemEventHandle : this.systemHandleList) {
                z |= systemEventHandle.onClosePage(multimodalEvent);
            }
        } else if (keyCode == 62) {
            for (MediaEventHandle mediaEventHandle : this.mediaHandleList) {
                z |= mediaEventHandle.onMediaControl(multimodalEvent);
            }
        } else if (keyCode == 66) {
            for (CommonEventHandle commonEventHandle2 : this.commonHandleList) {
                z = commonEventHandle2.onEnter(multimodalEvent) | z | commonEventHandle2.onSend(multimodalEvent);
            }
        } else if (keyCode == 82) {
            for (CommonEventHandle commonEventHandle3 : this.commonHandleList) {
                z |= commonEventHandle3.onShowMenu(multimodalEvent);
            }
        } else if (keyCode == 111) {
            for (CommonEventHandle commonEventHandle4 : this.commonHandleList) {
                z = commonEventHandle4.onCancel(multimodalEvent) | z | commonEventHandle4.onBack(multimodalEvent);
            }
        } else if (keyCode == 135) {
            for (CommonEventHandle commonEventHandle5 : this.commonHandleList) {
                z |= commonEventHandle5.onRefresh(multimodalEvent);
            }
        } else if (keyCode != 164) {
            if (keyCode != 92) {
                if (keyCode != 93) {
                    switch (keyCode) {
                        case 23:
                            for (CommonEventHandle commonEventHandle6 : this.commonHandleList) {
                                z = z | commonEventHandle6.onEnter(multimodalEvent) | commonEventHandle6.onSend(multimodalEvent);
                            }
                            for (MediaEventHandle mediaEventHandle2 : this.mediaHandleList) {
                                z |= mediaEventHandle2.onMediaControl(multimodalEvent);
                            }
                            break;
                    }
                }
                for (CommonEventHandle commonEventHandle7 : this.commonHandleList) {
                    z |= commonEventHandle7.onNext(multimodalEvent);
                }
            }
            for (CommonEventHandle commonEventHandle8 : this.commonHandleList) {
                z |= commonEventHandle8.onPrevious(multimodalEvent);
            }
        } else {
            for (SystemEventHandle systemEventHandle2 : this.systemHandleList) {
                z |= systemEventHandle2.onMute(multimodalEvent);
            }
        }
        return z;
    }

    private boolean handleShortcutKeyDownWithMeta(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(keyEvent).get();
        boolean z = false;
        if (keyCode == 31 && isMetaKeyPressed(keyEvent, 4096)) {
            for (CommonEventHandle commonEventHandle : this.commonHandleList) {
                z |= commonEventHandle.onCopy(multimodalEvent);
            }
        } else if (keyCode == 46 && isMetaKeyPressed(keyEvent, 4096)) {
            for (CommonEventHandle commonEventHandle2 : this.commonHandleList) {
                z |= commonEventHandle2.onRefresh(multimodalEvent);
            }
        } else if (keyCode == 50 && isMetaKeyPressed(keyEvent, 4096)) {
            for (CommonEventHandle commonEventHandle3 : this.commonHandleList) {
                z |= commonEventHandle3.onPaste(multimodalEvent);
            }
        } else if (keyCode == 52 && isMetaKeyPressed(keyEvent, 4096)) {
            for (CommonEventHandle commonEventHandle4 : this.commonHandleList) {
                z |= commonEventHandle4.onCut(multimodalEvent);
            }
        } else if (keyCode == 140 && isMetaKeyPressed(keyEvent, 1)) {
            for (CommonEventHandle commonEventHandle5 : this.commonHandleList) {
                z |= commonEventHandle5.onShowMenu(multimodalEvent);
            }
        } else if (keyCode == 54 && isMetaKeyPressed(keyEvent, 4096)) {
            for (CommonEventHandle commonEventHandle6 : this.commonHandleList) {
                z |= commonEventHandle6.onUndo(multimodalEvent);
            }
        } else if (keyCode == 44 && isMetaKeyPressed(keyEvent, 4096)) {
            for (CommonEventHandle commonEventHandle7 : this.commonHandleList) {
                z |= commonEventHandle7.onPrint(multimodalEvent);
            }
        } else if (keyCode == 134 && isMetaKeyPressed(keyEvent, 2)) {
            for (SystemEventHandle systemEventHandle : this.systemHandleList) {
                z |= systemEventHandle.onClosePage(multimodalEvent);
            }
        } else if (keyCode == 51 && isMetaKeyPressed(keyEvent, 4096)) {
            for (SystemEventHandle systemEventHandle2 : this.systemHandleList) {
                z |= systemEventHandle2.onClosePage(multimodalEvent);
            }
        }
        return z;
    }

    private boolean dispatchMotionEvent(MotionEvent motionEvent) {
        boolean dispatchMouseEvent = motionEvent.isFromSource(8194) ? dispatchMouseEvent(motionEvent) : false;
        if (motionEvent.isFromSource(4194304)) {
            dispatchMouseEvent = dispatchRotaryEncoder(motionEvent);
        }
        return !dispatchMouseEvent ? handleMotionEvent(motionEvent) : dispatchMouseEvent;
    }

    private boolean dispatchRotaryEncoder(MotionEvent motionEvent) {
        boolean z = false;
        if (motionEvent.getActionMasked() != 8 || this.commonHandleList.isEmpty()) {
            return false;
        }
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(motionEvent).get();
        if (motionEvent.getAxisValue(26) < 0.0f) {
            for (CommonEventHandle commonEventHandle : this.commonHandleList) {
                z |= commonEventHandle.onNext(multimodalEvent);
            }
            return z;
        }
        for (CommonEventHandle commonEventHandle2 : this.commonHandleList) {
            z |= commonEventHandle2.onPrevious(multimodalEvent);
        }
        return z;
    }

    private boolean dispatchMouseEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 8) {
            return handleMouseScrollEvent(motionEvent);
        }
        if (actionMasked == 11) {
            return handleMouseButtonPress(motionEvent);
        }
        if (actionMasked != 12) {
            return false;
        }
        return handleMouseButtonRelease(motionEvent);
    }

    private boolean handleMouseScrollEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (this.commonHandleList.isEmpty()) {
            return false;
        }
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(motionEvent).get();
        float axisValue = motionEvent.getAxisValue(9);
        if (axisValue < 0.0f) {
            for (CommonEventHandle commonEventHandle : this.commonHandleList) {
                z |= commonEventHandle.onNext(multimodalEvent);
            }
            return z;
        } else if (axisValue > 0.0f) {
            for (CommonEventHandle commonEventHandle2 : this.commonHandleList) {
                z |= commonEventHandle2.onPrevious(multimodalEvent);
            }
            return z;
        } else {
            float axisValue2 = motionEvent.getAxisValue(10);
            if (axisValue2 > 0.0f) {
                for (CommonEventHandle commonEventHandle3 : this.commonHandleList) {
                    z |= commonEventHandle3.onNext(multimodalEvent);
                }
                return z;
            }
            if (axisValue2 < 0.0f) {
                for (CommonEventHandle commonEventHandle4 : this.commonHandleList) {
                    z |= commonEventHandle4.onPrevious(multimodalEvent);
                }
            }
            return z;
        }
    }

    private boolean handleMouseButtonPress(MotionEvent motionEvent) {
        int actionButton = motionEvent.getActionButton();
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(motionEvent).get();
        boolean z = false;
        if (actionButton == 1) {
            for (CommonEventHandle commonEventHandle : this.commonHandleList) {
                z |= commonEventHandle.onStartDrag(multimodalEvent);
            }
        }
        return z;
    }

    private boolean handleMouseButtonRelease(MotionEvent motionEvent) {
        int actionButton = motionEvent.getActionButton();
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(motionEvent).get();
        boolean z = false;
        if (actionButton == 2) {
            for (CommonEventHandle commonEventHandle : this.commonHandleList) {
                z |= commonEventHandle.onShowMenu(multimodalEvent);
            }
        }
        return z;
    }

    private boolean handleMotionEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (this.touchHandleList.isEmpty()) {
            return false;
        }
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(motionEvent).get();
        if (multimodalEvent instanceof TouchEvent) {
            for (TouchEventHandle touchEventHandle : this.touchHandleList) {
                z |= touchEventHandle.onTouch((TouchEvent) multimodalEvent);
            }
        }
        return z;
    }

    private List<StandardizedEventHandle> getCallbacks(Object obj) {
        if (obj instanceof Ability) {
            return this.eventManager.getStandardizedEventHandleMap().get(obj);
        }
        return null;
    }

    private boolean updateEventHandle(Object obj) {
        resetEventHandle();
        List<StandardizedEventHandle> callbacks = getCallbacks(obj);
        if (callbacks != null && !callbacks.isEmpty()) {
            for (StandardizedEventHandle standardizedEventHandle : callbacks) {
                if (standardizedEventHandle instanceof CommonEventHandle) {
                    this.commonHandleList.add((CommonEventHandle) standardizedEventHandle);
                } else if (standardizedEventHandle instanceof SystemEventHandle) {
                    this.systemHandleList.add((SystemEventHandle) standardizedEventHandle);
                } else if (standardizedEventHandle instanceof MediaEventHandle) {
                    this.mediaHandleList.add((MediaEventHandle) standardizedEventHandle);
                } else if (standardizedEventHandle instanceof TelephoneEventHandle) {
                    this.telephoneHandleList.add((TelephoneEventHandle) standardizedEventHandle);
                } else if (standardizedEventHandle instanceof KeyEventHandle) {
                    this.keyHandleList.add((KeyEventHandle) standardizedEventHandle);
                } else if (standardizedEventHandle instanceof TouchEventHandle) {
                    this.touchHandleList.add((TouchEventHandle) standardizedEventHandle);
                }
            }
        }
        return this.commonHandleList.isEmpty() && this.systemHandleList.isEmpty() && this.mediaHandleList.isEmpty() && this.telephoneHandleList.isEmpty() && this.keyHandleList.isEmpty() && this.touchHandleList.isEmpty();
    }

    private void resetEventHandle() {
        this.commonHandleList.clear();
        this.systemHandleList.clear();
        this.mediaHandleList.clear();
        this.telephoneHandleList.clear();
        this.keyHandleList.clear();
        this.touchHandleList.clear();
    }

    private boolean noMetaKeyPressed(KeyEvent keyEvent) {
        return isMetaKeyPressed(keyEvent, 0);
    }

    private boolean isMetaKeyPressed(KeyEvent keyEvent, int i) {
        return getMetaState(keyEvent) == i;
    }

    private int getMetaState(KeyEvent keyEvent) {
        int i = keyEvent.isCtrlPressed() ? 4096 : 0;
        if (keyEvent.isAltPressed()) {
            i |= 2;
        }
        if (keyEvent.isShiftPressed()) {
            i |= 1;
        }
        return keyEvent.isMetaPressed() ? i | 65536 : i;
    }

    /* access modifiers changed from: private */
    public class KeyEventTracker {
        private boolean mIsConsumed;
        private int mTrackingDownKeyCode;

        private KeyEventTracker() {
            this.mTrackingDownKeyCode = 0;
            this.mIsConsumed = false;
        }

        public void resetTrackingState() {
            this.mIsConsumed = false;
        }

        public void startEventTracking(KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                this.mTrackingDownKeyCode = keyEvent.getKeyCode();
                this.mIsConsumed = false;
            }
        }

        public void consumeEvent() {
            this.mIsConsumed = true;
        }

        public boolean isEventTracking(KeyEvent keyEvent) {
            return this.mTrackingDownKeyCode == keyEvent.getKeyCode();
        }

        public boolean isConsumed() {
            return this.mIsConsumed;
        }
    }

    private boolean handleSpecialKeyDown(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getRepeatCount() != 0 || keyCode != 79 || (this.commonHandleList.isEmpty() && this.telephoneHandleList.isEmpty() && this.mediaHandleList.isEmpty())) {
            return false;
        }
        this.isHeadSetUp = false;
        this.isHeadSetSingleTap = false;
        boolean hasMessages = this.handler.hasMessages(1);
        boolean hasMessages2 = this.handler.hasMessages(3);
        if (hasMessages) {
            this.handler.removeMessages(1);
            handleHeadSetHookDoubleTap(keyEvent);
            this.handler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
        } else if (hasMessages2) {
            this.handler.removeMessages(3);
        } else {
            recycleSpecialEvent();
            this.specialKeyEvent = new KeyEvent(keyEvent);
            this.handler.sendEmptyMessageDelayed(1, DOUBLE_TAP_TIMEOUT);
            this.handler.sendEmptyMessageDelayed(2, LONG_TAP_TIMEOUT);
        }
        return false;
    }

    private boolean handleSpecialKeyUp(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 79) {
            if (this.handler.hasMessages(2)) {
                this.handler.removeMessages(2);
            }
            this.isHeadSetUp = true;
            if (this.isHeadSetSingleTap) {
                handleHeadSetHookSingleTap();
                this.isHeadSetSingleTap = false;
                this.isHeadSetUp = false;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handleHeadSetHookSingleTap() {
        HiLog.info(LOG_LABEL, "handleHeadSetHookSingleTap.", new Object[0]);
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(this.specialKeyEvent).get();
        for (TelephoneEventHandle telephoneEventHandle : this.telephoneHandleList) {
            telephoneEventHandle.onTelephoneControl(multimodalEvent);
        }
        for (MediaEventHandle mediaEventHandle : this.mediaHandleList) {
            mediaEventHandle.onMediaControl(multimodalEvent);
        }
    }

    private void handleHeadSetHookDoubleTap(KeyEvent keyEvent) {
        HiLog.info(LOG_LABEL, "handleHeadSetHookDoubleTap.", new Object[0]);
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(keyEvent).get();
        for (CommonEventHandle commonEventHandle : this.commonHandleList) {
            commonEventHandle.onNext(multimodalEvent);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void handleHeadSetHookLongTap() {
        HiLog.info(LOG_LABEL, "handleHeadSetHookLongTap.", new Object[0]);
        MultimodalEvent multimodalEvent = MultimodalEventFactory.createStandardizedEvent(this.specialKeyEvent).get();
        for (TelephoneEventHandle telephoneEventHandle : this.telephoneHandleList) {
            telephoneEventHandle.onTelephoneControl(multimodalEvent);
        }
    }

    private void recycleSpecialEvent() {
        KeyEvent keyEvent = this.specialKeyEvent;
        if (keyEvent != null) {
            keyEvent.recycle();
            this.specialKeyEvent = null;
        }
    }

    /* access modifiers changed from: private */
    public class StandardizedEventHandler extends Handler {
        private StandardizedEventHandler() {
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                StandardizedEventProcessor.this.isHeadSetSingleTap = true;
                if (StandardizedEventProcessor.this.isHeadSetUp) {
                    StandardizedEventProcessor.this.handleHeadSetHookSingleTap();
                    StandardizedEventProcessor.this.isHeadSetSingleTap = false;
                    StandardizedEventProcessor.this.isHeadSetUp = false;
                }
            } else if (i == 2) {
                StandardizedEventProcessor.this.isHeadSetSingleTap = false;
                StandardizedEventProcessor.this.handleHeadSetHookLongTap();
            }
        }
    }
}
