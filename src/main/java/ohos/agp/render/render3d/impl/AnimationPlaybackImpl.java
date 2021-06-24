package ohos.agp.render.render3d.impl;

import java.util.Optional;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.render.render3d.Engine;
import ohos.agp.render.render3d.SceneNode;
import ohos.agp.render.render3d.resources.AnimationPlayback;
import ohos.agp.render.render3d.resources.ResourceHandle;
import ohos.hiviewdfx.HiLogLabel;

class AnimationPlaybackImpl implements AnimationPlayback {
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "core: AnimationPlaybackImpl");
    private CoreAnimationSystem mAnimationSystem;
    private Engine mEngine;
    private CoreAnimationPlayback mPlayback;

    private AnimationPlaybackImpl(Engine engine, CoreAnimationSystem coreAnimationSystem, CoreAnimationPlayback coreAnimationPlayback) {
        this.mEngine = engine;
        this.mAnimationSystem = coreAnimationSystem;
        this.mPlayback = coreAnimationPlayback;
    }

    static Optional<AnimationPlayback> createPlayback(Engine engine, CoreAnimationSystem coreAnimationSystem, ResourceHandle resourceHandle, SceneNode sceneNode) {
        if (coreAnimationSystem == null) {
            throw new NullPointerException();
        } else if (resourceHandle == null) {
            throw new NullPointerException();
        } else if (sceneNode != null) {
            engine.requireRenderThread();
            Optional<CoreSceneNode> nativeSceneNode = SceneImpl.getNativeSceneNode(sceneNode);
            return (!resourceHandle.isValid() || !nativeSceneNode.isPresent()) ? Optional.empty() : Optional.of(new AnimationPlaybackImpl(engine, coreAnimationSystem, coreAnimationSystem.createPlayback(ResourceHandleImpl.getNativeHandle(resourceHandle), nativeSceneNode.get())));
        } else {
            throw new NullPointerException();
        }
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public void release() {
        Engine engine = this.mEngine;
        if (engine != null) {
            engine.requireRenderThread();
        }
        CoreAnimationPlayback coreAnimationPlayback = this.mPlayback;
        if (coreAnimationPlayback != null) {
            this.mAnimationSystem.destroyPlayback(coreAnimationPlayback);
            this.mAnimationSystem = null;
            this.mPlayback = null;
        }
        this.mEngine = null;
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public void setPlaybackState(AnimationPlayback.State state) {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$resources$AnimationPlayback$State[state.ordinal()];
        if (i == 1) {
            this.mPlayback.setPlaybackState(CoreAnimationPlaybackState.STOP);
        } else if (i == 2) {
            this.mPlayback.setPlaybackState(CoreAnimationPlaybackState.PLAY);
        } else if (i == 3) {
            this.mPlayback.setPlaybackState(CoreAnimationPlaybackState.PAUSE);
        } else {
            throw new IllegalStateException();
        }
    }

    /* renamed from: ohos.agp.render.render3d.impl.AnimationPlaybackImpl$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$impl$CoreAnimationPlaybackState = new int[CoreAnimationPlaybackState.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$resources$AnimationPlayback$State = new int[AnimationPlayback.State.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        static {
            /*
                ohos.agp.render.render3d.impl.CoreAnimationPlaybackState[] r0 = ohos.agp.render.render3d.impl.CoreAnimationPlaybackState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreAnimationPlaybackState = r0
                r0 = 1
                int[] r1 = ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreAnimationPlaybackState     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.agp.render.render3d.impl.CoreAnimationPlaybackState r2 = ohos.agp.render.render3d.impl.CoreAnimationPlaybackState.STOP     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreAnimationPlaybackState     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.agp.render.render3d.impl.CoreAnimationPlaybackState r3 = ohos.agp.render.render3d.impl.CoreAnimationPlaybackState.PLAY     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreAnimationPlaybackState     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.agp.render.render3d.impl.CoreAnimationPlaybackState r4 = ohos.agp.render.render3d.impl.CoreAnimationPlaybackState.PAUSE     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                ohos.agp.render.render3d.resources.AnimationPlayback$State[] r3 = ohos.agp.render.render3d.resources.AnimationPlayback.State.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$resources$AnimationPlayback$State = r3
                int[] r3 = ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$resources$AnimationPlayback$State     // Catch:{ NoSuchFieldError -> 0x003d }
                ohos.agp.render.render3d.resources.AnimationPlayback$State r4 = ohos.agp.render.render3d.resources.AnimationPlayback.State.STOP     // Catch:{ NoSuchFieldError -> 0x003d }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x003d }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x003d }
            L_0x003d:
                int[] r0 = ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$resources$AnimationPlayback$State     // Catch:{ NoSuchFieldError -> 0x0047 }
                ohos.agp.render.render3d.resources.AnimationPlayback$State r3 = ohos.agp.render.render3d.resources.AnimationPlayback.State.PLAY     // Catch:{ NoSuchFieldError -> 0x0047 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0047 }
                r0[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0047 }
            L_0x0047:
                int[] r0 = ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$resources$AnimationPlayback$State     // Catch:{ NoSuchFieldError -> 0x0051 }
                ohos.agp.render.render3d.resources.AnimationPlayback$State r1 = ohos.agp.render.render3d.resources.AnimationPlayback.State.PAUSE     // Catch:{ NoSuchFieldError -> 0x0051 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0051 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0051 }
            L_0x0051:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.render.render3d.impl.AnimationPlaybackImpl.AnonymousClass1.<clinit>():void");
        }
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public AnimationPlayback.State getPlaybackState() {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreAnimationPlaybackState[this.mPlayback.getPlaybackState().ordinal()];
        if (i == 1) {
            return AnimationPlayback.State.STOP;
        }
        if (i == 2) {
            return AnimationPlayback.State.PLAY;
        }
        if (i == 3) {
            return AnimationPlayback.State.PAUSE;
        }
        throw new IllegalStateException();
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public void setRepeatCount(int i) {
        if (i == Integer.MAX_VALUE) {
            this.mPlayback.setRepeatCount(Core.getAnimationRepeatCountInfinite());
        } else {
            this.mPlayback.setRepeatCount((long) i);
        }
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public int getRepeatCount() {
        long repeatCount = this.mPlayback.getRepeatCount();
        if (repeatCount == Core.getAnimationRepeatCountInfinite()) {
            return Integer.MAX_VALUE;
        }
        return (int) repeatCount;
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public void setWeight(float f) {
        this.mPlayback.setWeight(f);
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public float getWeight() {
        return this.mPlayback.getWeight();
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public boolean isCompleted() {
        return this.mPlayback.isCompleted();
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public void setSpeed(float f) {
        this.mPlayback.setSpeed(f);
    }

    @Override // ohos.agp.render.render3d.resources.AnimationPlayback
    public float getSpeed() {
        return this.mPlayback.getSpeed();
    }
}
