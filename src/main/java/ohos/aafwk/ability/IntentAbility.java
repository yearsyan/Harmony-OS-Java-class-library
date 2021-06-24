package ohos.aafwk.ability;

import ohos.aafwk.content.Intent;
import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.eventhandler.NativeException;

public abstract class IntentAbility extends Ability {
    private static final LogLabel LABEL = LogLabel.create();
    private AbilityEventHandler abilityEventHandler;
    private String name;

    /* access modifiers changed from: protected */
    public abstract void onProcessIntent(Intent intent);

    public IntentAbility(String str) {
        this.name = str;
    }

    /* access modifiers changed from: private */
    public class AbilityEventHandler extends EventHandler {
        private AbilityEventHandler(EventRunner eventRunner) throws IllegalArgumentException, NativeException {
            super(eventRunner);
        }

        /* access modifiers changed from: protected */
        @Override // ohos.eventhandler.EventHandler
        public void processEvent(InnerEvent innerEvent) {
            if (innerEvent.object instanceof Intent) {
                IntentAbility.this.onProcessIntent((Intent) innerEvent.object);
            }
            if (IntentAbility.this.abilityEventHandler.isIdle()) {
                Log.info(IntentAbility.LABEL, "all event proceeded, terminate ability", new Object[0]);
                IntentAbility.this.terminateAbility();
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.aafwk.ability.Ability
    public void onStart(Intent intent) {
        super.onStart(intent);
        this.abilityEventHandler = new AbilityEventHandler(EventRunner.create("IntentAbility-" + this.name));
    }

    /* access modifiers changed from: protected */
    @Override // ohos.aafwk.ability.Ability
    @Deprecated
    public void onCommand(Intent intent, boolean z) {
        AbilityEventHandler abilityEventHandler2 = this.abilityEventHandler;
        if (abilityEventHandler2 == null) {
            Log.error(LABEL, "abilityEventHandler is null, return", new Object[0]);
        } else {
            abilityEventHandler2.sendEvent(InnerEvent.get(0, intent));
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.aafwk.ability.Ability
    public void onCommand(Intent intent, boolean z, int i) {
        AbilityEventHandler abilityEventHandler2 = this.abilityEventHandler;
        if (abilityEventHandler2 == null) {
            Log.error(LABEL, "abilityEventHandler is null, return", new Object[0]);
        } else {
            abilityEventHandler2.sendEvent(InnerEvent.get(0, intent));
        }
    }
}
