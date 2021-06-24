package ohos.nfc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityLifecycleCallbacks;
import ohos.aafwk.ability.HarmonyosApplication;
import ohos.aafwk.ability.Lifecycle;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.utils.PacMapEx;
import ohos.nfc.NfcController;
import ohos.nfc.tag.NdefMessage;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.utils.PacMap;
import ohos.utils.net.Uri;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

/* access modifiers changed from: package-private */
public class NfcAbilityManager extends NfcCommProxy implements AbilityLifecycleCallbacks {
    static final HiLogLabel LABEL = new HiLogLabel(3, NfcKitsUtils.NFC_DOMAIN_ID, "NfcAbilityManager");
    final List<NfcAbilityState> mAbilities = new LinkedList();
    final List<NfcAppState> mAppStates = new ArrayList(1);
    final NfcController mNfcController;
    private NfcControllerProxy mNfcControllerProxy = NfcControllerProxy.getInstance();

    @Override // ohos.aafwk.ability.AbilityLifecycleCallbacks
    public void onAbilityActive(Ability ability) {
    }

    @Override // ohos.aafwk.ability.AbilityLifecycleCallbacks
    public void onAbilityInactive(Ability ability) {
    }

    @Override // ohos.aafwk.ability.AbilityLifecycleCallbacks
    public void onAbilitySaveState(PacMap pacMap) {
    }

    @Override // ohos.aafwk.ability.AbilityLifecycleCallbacks
    public void onAbilityStart(Ability ability) {
    }

    /* access modifiers changed from: package-private */
    public class NfcAppState {
        final HarmonyosApplication app;
        int refCount = 0;

        public NfcAppState(HarmonyosApplication harmonyosApplication) {
            this.app = harmonyosApplication;
        }

        public void register() {
            this.refCount++;
            if (this.refCount == 1) {
                this.app.registerAbilityLifecycleCallbacks(NfcAbilityManager.this);
            }
        }

        public void unregister() {
            this.refCount--;
            int i = this.refCount;
            if (i == 0) {
                this.app.unregisterAbilityLifecycleCallbacks(NfcAbilityManager.this);
            } else if (i < 0) {
                HiLog.error(NfcAbilityManager.LABEL, "refCount less than zero.", new Object[0]);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Optional<NfcAppState> getAppState(HarmonyosApplication harmonyosApplication) {
        for (NfcAppState nfcAppState : this.mAppStates) {
            if (nfcAppState.app == harmonyosApplication) {
                return Optional.of(nfcAppState);
            }
        }
        return Optional.empty();
    }

    /* access modifiers changed from: package-private */
    public void registerHarmonyApplication(HarmonyosApplication harmonyosApplication) {
        Optional<NfcAppState> appState = getAppState(harmonyosApplication);
        if (!appState.isPresent()) {
            appState = Optional.of(new NfcAppState(harmonyosApplication));
            this.mAppStates.add(appState.get());
        }
        appState.get().register();
    }

    /* access modifiers changed from: package-private */
    public void unregisterHarmonyApplication(HarmonyosApplication harmonyosApplication) {
        Optional<NfcAppState> appState = getAppState(harmonyosApplication);
        if (!appState.isPresent()) {
            HiLog.error(LABEL, "app was not registered : %{public}s", harmonyosApplication);
            return;
        }
        appState.get().unregister();
    }

    /* access modifiers changed from: package-private */
    public class NfcAbilityState {
        Ability ability;
        boolean activated = false;
        NfcController controller;
        int flags = 0;
        Optional<NdefMessage> ndefMessage = Optional.empty();
        NfcController.ReaderModeCallback readerModeCallback = null;
        PacMapEx readerModeExtras = null;
        int readerModeFlags = 0;
        RemoteObject token;
        Uri[] uris = null;

        public NfcAbilityState(Ability ability2) {
            boolean z = false;
            if (ability2.getLifecycle().getLifecycleState() != Lifecycle.Event.valueOf("ON_STOP")) {
                this.activated = ability2.getLifecycle().getLifecycleState() == Lifecycle.Event.valueOf("ON_ACTIVE") ? true : z;
                this.ability = ability2;
                this.token = new RemoteObject("NfcAbilityState");
                this.controller = NfcAbilityManager.this.mNfcController;
                NfcAbilityManager.this.registerHarmonyApplication(ability2.getHarmonyosApplication());
                return;
            }
            throw new IllegalStateException("ability is already stopped.");
        }

        public void terminate() {
            NfcAbilityManager.this.unregisterHarmonyApplication(this.ability.getHarmonyosApplication());
            this.activated = false;
            this.ability = null;
            this.ndefMessage = Optional.empty();
            this.uris = null;
            this.readerModeFlags = 0;
            this.token = null;
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void terminateAbilityState(Ability ability) {
        Optional<NfcAbilityState> findAbilityState = findAbilityState(ability);
        if (!findAbilityState.isPresent()) {
            findAbilityState.get().terminate();
            this.mAbilities.remove(findAbilityState.get());
        }
    }

    public NfcAbilityManager(NfcController nfcController) {
        super(SystemAbilityDefinition.NFC_MANAGER_SYS_ABILITY_ID);
        this.mNfcController = nfcController;
    }

    public void unsetReaderMode(Ability ability) {
        RemoteObject remoteObject;
        boolean z;
        synchronized (this) {
            NfcAbilityState nfcAbilityState = getAbilityState(ability).get();
            nfcAbilityState.readerModeCallback = null;
            nfcAbilityState.readerModeFlags = 0;
            nfcAbilityState.readerModeExtras = null;
            remoteObject = nfcAbilityState.token;
            z = nfcAbilityState.activated;
        }
        if (z) {
            setReaderMode(remoteObject, (NfcController.ReaderModeCallback) null, 0, (PacMapEx) null, (NfcController) null);
        }
    }

    public void setReaderMode(Ability ability, NfcController.ReaderModeCallback readerModeCallback, int i, PacMapEx pacMapEx, NfcController nfcController) {
        RemoteObject remoteObject;
        boolean z;
        synchronized (this) {
            NfcAbilityState nfcAbilityState = getAbilityState(ability).get();
            nfcAbilityState.readerModeCallback = readerModeCallback;
            nfcAbilityState.readerModeFlags = i;
            nfcAbilityState.readerModeExtras = pacMapEx;
            remoteObject = nfcAbilityState.token;
            z = nfcAbilityState.activated;
        }
        if (z) {
            setReaderMode(remoteObject, readerModeCallback, i, pacMapEx, nfcController);
        }
    }

    public void setReaderMode(RemoteObject remoteObject, NfcController.ReaderModeCallback readerModeCallback, int i, PacMapEx pacMapEx, NfcController nfcController) {
        HiLog.debug(LABEL, "Setting reader mode", new Object[0]);
        try {
            this.mNfcControllerProxy.setReaderMode(remoteObject, readerModeCallback, i, pacMapEx, nfcController);
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "setReaderMode failed!", new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized Optional<NfcAbilityState> getAbilityState(Ability ability) {
        Optional<NfcAbilityState> findAbilityState;
        findAbilityState = findAbilityState(ability);
        if (!findAbilityState.isPresent()) {
            findAbilityState = Optional.of(new NfcAbilityState(ability));
            this.mAbilities.add(findAbilityState.get());
        }
        return findAbilityState;
    }

    /* access modifiers changed from: package-private */
    public synchronized Optional<NfcAbilityState> findAbilityState(Ability ability) {
        for (NfcAbilityState nfcAbilityState : this.mAbilities) {
            if (nfcAbilityState.ability == ability) {
                return Optional.of(nfcAbilityState);
            }
        }
        return Optional.empty();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x004b, code lost:
        if (r4 == 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x004d, code lost:
        setReaderMode(r2, r3, r4, r5, r6);
     */
    @Override // ohos.aafwk.ability.AbilityLifecycleCallbacks
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onAbilityForeground(ohos.aafwk.ability.Ability r8) {
        /*
            r7 = this;
            monitor-enter(r7)
            java.util.Optional r0 = r7.findAbilityState(r8)     // Catch:{ all -> 0x0052 }
            ohos.hiviewdfx.HiLogLabel r1 = ohos.nfc.NfcAbilityManager.LABEL     // Catch:{ all -> 0x0052 }
            java.lang.String r2 = "onResume() for  %{public}s state"
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0052 }
            r5 = 0
            r4[r5] = r8     // Catch:{ all -> 0x0052 }
            ohos.hiviewdfx.HiLog.debug(r1, r2, r4)     // Catch:{ all -> 0x0052 }
            boolean r8 = r0.isPresent()     // Catch:{ all -> 0x0052 }
            if (r8 != 0) goto L_0x001a
            monitor-exit(r7)     // Catch:{ all -> 0x0052 }
            return
        L_0x001a:
            java.lang.Object r8 = r0.get()     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r8 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r8     // Catch:{ all -> 0x0052 }
            r8.activated = r3     // Catch:{ all -> 0x0052 }
            java.lang.Object r8 = r0.get()     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r8 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r8     // Catch:{ all -> 0x0052 }
            ohos.rpc.RemoteObject r2 = r8.token     // Catch:{ all -> 0x0052 }
            java.lang.Object r8 = r0.get()     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r8 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r8     // Catch:{ all -> 0x0052 }
            int r4 = r8.readerModeFlags     // Catch:{ all -> 0x0052 }
            java.lang.Object r8 = r0.get()     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r8 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r8     // Catch:{ all -> 0x0052 }
            ohos.interwork.utils.PacMapEx r5 = r8.readerModeExtras     // Catch:{ all -> 0x0052 }
            java.lang.Object r8 = r0.get()     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r8 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r8     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcController$ReaderModeCallback r3 = r8.readerModeCallback     // Catch:{ all -> 0x0052 }
            java.lang.Object r8 = r0.get()     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r8 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r8     // Catch:{ all -> 0x0052 }
            ohos.nfc.NfcController r6 = r8.controller     // Catch:{ all -> 0x0052 }
            monitor-exit(r7)     // Catch:{ all -> 0x0052 }
            if (r4 == 0) goto L_0x0051
            r1 = r7
            r1.setReaderMode(r2, r3, r4, r5, r6)
        L_0x0051:
            return
        L_0x0052:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.nfc.NfcAbilityManager.onAbilityForeground(ohos.aafwk.ability.Ability):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003f, code lost:
        if (r3 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
        setReaderMode(r7, (ohos.nfc.NfcController.ReaderModeCallback) null, 0, (ohos.interwork.utils.PacMapEx) null, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    @Override // ohos.aafwk.ability.AbilityLifecycleCallbacks
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onAbilityBackground(ohos.aafwk.ability.Ability r13) {
        /*
            r12 = this;
            monitor-enter(r12)
            java.util.Optional r0 = r12.findAbilityState(r13)     // Catch:{ all -> 0x0049 }
            ohos.hiviewdfx.HiLogLabel r1 = ohos.nfc.NfcAbilityManager.LABEL     // Catch:{ all -> 0x0049 }
            java.lang.String r2 = "onPause() for  %{public}s state"
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0049 }
            r5 = 0
            r4[r5] = r13     // Catch:{ all -> 0x0049 }
            ohos.hiviewdfx.HiLog.debug(r1, r2, r4)     // Catch:{ all -> 0x0049 }
            boolean r13 = r0.isPresent()     // Catch:{ all -> 0x0049 }
            if (r13 != 0) goto L_0x001a
            monitor-exit(r12)     // Catch:{ all -> 0x0049 }
            return
        L_0x001a:
            java.lang.Object r13 = r0.get()     // Catch:{ all -> 0x0049 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r13 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r13     // Catch:{ all -> 0x0049 }
            r13.activated = r5     // Catch:{ all -> 0x0049 }
            java.lang.Object r13 = r0.get()     // Catch:{ all -> 0x0049 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r13 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r13     // Catch:{ all -> 0x0049 }
            ohos.rpc.RemoteObject r7 = r13.token     // Catch:{ all -> 0x0049 }
            java.lang.Object r13 = r0.get()     // Catch:{ all -> 0x0049 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r13 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r13     // Catch:{ all -> 0x0049 }
            int r13 = r13.readerModeFlags     // Catch:{ all -> 0x0049 }
            if (r13 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r3 = r5
        L_0x0036:
            java.lang.Object r13 = r0.get()     // Catch:{ all -> 0x0049 }
            ohos.nfc.NfcAbilityManager$NfcAbilityState r13 = (ohos.nfc.NfcAbilityManager.NfcAbilityState) r13     // Catch:{ all -> 0x0049 }
            ohos.nfc.NfcController r11 = r13.controller     // Catch:{ all -> 0x0049 }
            monitor-exit(r12)     // Catch:{ all -> 0x0049 }
            if (r3 == 0) goto L_0x0048
            r8 = 0
            r9 = 0
            r10 = 0
            r6 = r12
            r6.setReaderMode(r7, r8, r9, r10, r11)
        L_0x0048:
            return
        L_0x0049:
            r13 = move-exception
            monitor-exit(r12)
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.nfc.NfcAbilityManager.onAbilityBackground(ohos.aafwk.ability.Ability):void");
    }

    @Override // ohos.aafwk.ability.AbilityLifecycleCallbacks
    public void onAbilityStop(Ability ability) {
        synchronized (this) {
            Optional<NfcAbilityState> findAbilityState = findAbilityState(ability);
            HiLog.debug(LABEL, "onAbilityStop() for %{public}s state", ability);
            if (findAbilityState.isPresent()) {
                terminateAbilityState(ability);
            }
        }
    }
}
