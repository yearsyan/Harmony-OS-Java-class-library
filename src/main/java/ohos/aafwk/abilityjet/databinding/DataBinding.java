package ohos.aafwk.abilityjet.databinding;

import ohos.aafwk.ability.Lifecycle;
import ohos.agp.components.ComponentContainer;

public abstract class DataBinding {
    private static final Object OBJECT = new Object();
    private Lifecycle lifecycle;

    public abstract void initComponent(ComponentContainer componentContainer);

    public void setLifecycle(Lifecycle lifecycle2) {
        if (lifecycle2 != null) {
            synchronized (OBJECT) {
                if (this.lifecycle == null) {
                    this.lifecycle = lifecycle2;
                } else {
                    throw new IllegalStateException("lifecycle can't set twice");
                }
            }
            return;
        }
        throw new IllegalArgumentException("lifecycle can't be null");
    }

    public Lifecycle getLifecycle() {
        Lifecycle lifecycle2;
        synchronized (OBJECT) {
            lifecycle2 = this.lifecycle;
        }
        return lifecycle2;
    }
}
