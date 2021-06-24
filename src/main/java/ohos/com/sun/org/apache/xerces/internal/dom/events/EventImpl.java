package ohos.com.sun.org.apache.xerces.internal.dom.events;

import ohos.org.w3c.dom.events.Event;
import ohos.org.w3c.dom.events.EventTarget;

public class EventImpl implements Event {
    public boolean bubbles = true;
    public boolean cancelable = false;
    public EventTarget currentTarget;
    public short eventPhase;
    public boolean initialized = false;
    public boolean preventDefault = false;
    public boolean stopPropagation = false;
    public EventTarget target;
    protected long timeStamp = System.currentTimeMillis();
    public String type = null;

    @Override // ohos.org.w3c.dom.events.Event
    public void initEvent(String str, boolean z, boolean z2) {
        this.type = str;
        this.bubbles = z;
        this.cancelable = z2;
        this.initialized = true;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public boolean getBubbles() {
        return this.bubbles;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public boolean getCancelable() {
        return this.cancelable;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public EventTarget getCurrentTarget() {
        return this.currentTarget;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public short getEventPhase() {
        return this.eventPhase;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public EventTarget getTarget() {
        return this.target;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public String getType() {
        return this.type;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public long getTimeStamp() {
        return this.timeStamp;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public void stopPropagation() {
        this.stopPropagation = true;
    }

    @Override // ohos.org.w3c.dom.events.Event
    public void preventDefault() {
        this.preventDefault = true;
    }
}
