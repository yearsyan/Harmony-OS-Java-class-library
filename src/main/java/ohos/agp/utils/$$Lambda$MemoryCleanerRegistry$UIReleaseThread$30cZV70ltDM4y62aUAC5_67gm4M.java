package ohos.agp.utils;

import ohos.agp.utils.MemoryCleanerRegistry;
import ohos.agp.vsync.VsyncScheduler;

/* renamed from: ohos.agp.utils.-$$Lambda$MemoryCleanerRegistry$UIReleaseThread$30cZV70ltDM4y62aUAC5_67gm4M  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$MemoryCleanerRegistry$UIReleaseThread$30cZV70ltDM4y62aUAC5_67gm4M implements VsyncScheduler.FrameCallback {
    public static final /* synthetic */ $$Lambda$MemoryCleanerRegistry$UIReleaseThread$30cZV70ltDM4y62aUAC5_67gm4M INSTANCE = new $$Lambda$MemoryCleanerRegistry$UIReleaseThread$30cZV70ltDM4y62aUAC5_67gm4M();

    private /* synthetic */ $$Lambda$MemoryCleanerRegistry$UIReleaseThread$30cZV70ltDM4y62aUAC5_67gm4M() {
    }

    @Override // ohos.agp.vsync.VsyncScheduler.FrameCallback
    public final void doFrame(long j) {
        MemoryCleanerRegistry.UIReleaseThread.lambda$static$0(j);
    }
}
