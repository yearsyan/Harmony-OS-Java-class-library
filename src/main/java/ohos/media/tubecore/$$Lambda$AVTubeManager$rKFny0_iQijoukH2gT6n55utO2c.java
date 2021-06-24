package ohos.media.tubecore;

import java.util.function.Function;
import ohos.media.tubecore.adapter.AVTubeAdapter;
import ohos.media.tubecore.adapter.TubeMappingTable;

/* renamed from: ohos.media.tubecore.-$$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c implements Function {
    public static final /* synthetic */ $$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c INSTANCE = new $$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c();

    private /* synthetic */ $$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        AVTubeAdapter aVTubeAdapter;
        return TubeMappingTable.findAVTube(aVTubeAdapter).orElseGet(
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0006: RETURN  
              (wrap: ohos.media.tubecore.AVTube : 0x0002: ONE_ARG  (r0v1 ohos.media.tubecore.AVTube) = 
              (wrap: ohos.media.tubecore.AVTube : 0x000d: INVOKE  
              (wrap: java.util.Optional<ohos.media.tubecore.AVTube> : 0x0000: INVOKE  (r1v1 'aVTubeAdapter' ohos.media.tubecore.adapter.AVTubeAdapter) type: STATIC call: ohos.media.tubecore.adapter.TubeMappingTable.findAVTube(ohos.media.tubecore.adapter.AVTubeAdapter):java.util.Optional)
              (wrap: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c : 0x0006: CONSTRUCTOR  
              (wrap: ohos.media.tubecore.adapter.AVTubeAdapter : 0x0000: CHECK_CAST (r1v1 'aVTubeAdapter' ohos.media.tubecore.adapter.AVTubeAdapter) = (ohos.media.tubecore.adapter.AVTubeAdapter) (r1v0 'obj' java.lang.Object))
             call: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c.<init>(ohos.media.tubecore.adapter.AVTubeAdapter):void type: CONSTRUCTOR)
             type: VIRTUAL call: java.util.Optional.orElseGet(java.util.function.Supplier):java.lang.Object)
            )
             in method: ohos.media.tubecore.-$$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c.apply(java.lang.Object):java.lang.Object, file: D:\Workspace\hm\classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:255)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:217)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:110)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:56)
            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:244)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:237)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:342)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:295)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:264)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.util.ArrayList.forEach(ArrayList.java:1257)
            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0002: ONE_ARG  (r0v1 ohos.media.tubecore.AVTube) = 
              (wrap: ohos.media.tubecore.AVTube : 0x000d: INVOKE  
              (wrap: java.util.Optional<ohos.media.tubecore.AVTube> : 0x0000: INVOKE  (r1v1 'aVTubeAdapter' ohos.media.tubecore.adapter.AVTubeAdapter) type: STATIC call: ohos.media.tubecore.adapter.TubeMappingTable.findAVTube(ohos.media.tubecore.adapter.AVTubeAdapter):java.util.Optional)
              (wrap: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c : 0x0006: CONSTRUCTOR  
              (wrap: ohos.media.tubecore.adapter.AVTubeAdapter : 0x0000: CHECK_CAST (r1v1 'aVTubeAdapter' ohos.media.tubecore.adapter.AVTubeAdapter) = (ohos.media.tubecore.adapter.AVTubeAdapter) (r1v0 'obj' java.lang.Object))
             call: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c.<init>(ohos.media.tubecore.adapter.AVTubeAdapter):void type: CONSTRUCTOR)
             type: VIRTUAL call: java.util.Optional.orElseGet(java.util.function.Supplier):java.lang.Object)
             in method: ohos.media.tubecore.-$$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c.apply(java.lang.Object):java.lang.Object, file: D:\Workspace\hm\classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:255)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:313)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:249)
            	... 14 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000d: INVOKE  
              (wrap: java.util.Optional<ohos.media.tubecore.AVTube> : 0x0000: INVOKE  (r1v1 'aVTubeAdapter' ohos.media.tubecore.adapter.AVTubeAdapter) type: STATIC call: ohos.media.tubecore.adapter.TubeMappingTable.findAVTube(ohos.media.tubecore.adapter.AVTubeAdapter):java.util.Optional)
              (wrap: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c : 0x0006: CONSTRUCTOR  
              (wrap: ohos.media.tubecore.adapter.AVTubeAdapter : 0x0000: CHECK_CAST (r1v1 'aVTubeAdapter' ohos.media.tubecore.adapter.AVTubeAdapter) = (ohos.media.tubecore.adapter.AVTubeAdapter) (r1v0 'obj' java.lang.Object))
             call: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c.<init>(ohos.media.tubecore.adapter.AVTubeAdapter):void type: CONSTRUCTOR)
             type: VIRTUAL call: java.util.Optional.orElseGet(java.util.function.Supplier):java.lang.Object in method: ohos.media.tubecore.-$$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c.apply(java.lang.Object):java.lang.Object, file: D:\Workspace\hm\classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:255)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:94)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:481)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
            	... 18 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0006: CONSTRUCTOR  
              (wrap: ohos.media.tubecore.adapter.AVTubeAdapter : 0x0000: CHECK_CAST (r1v1 'aVTubeAdapter' ohos.media.tubecore.adapter.AVTubeAdapter) = (ohos.media.tubecore.adapter.AVTubeAdapter) (r1v0 'obj' java.lang.Object))
             call: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c.<init>(ohos.media.tubecore.adapter.AVTubeAdapter):void type: CONSTRUCTOR in method: ohos.media.tubecore.-$$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c.apply(java.lang.Object):java.lang.Object, file: D:\Workspace\hm\classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:255)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:806)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:746)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:367)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
            	... 23 more
            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: ohos.media.tubecore.-$$Lambda$AVTubeManager$qq4KA4znrlSIBQo3kOn7LD8YK9c, state: NOT_LOADED
            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:215)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:630)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:363)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
            	... 29 more
            */
        /*
            this = this;
            ohos.media.tubecore.adapter.AVTubeAdapter r1 = (ohos.media.tubecore.adapter.AVTubeAdapter) r1
            ohos.media.tubecore.AVTube r0 = ohos.media.tubecore.AVTubeManager.lambda$tubeInUse$3(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.tubecore.$$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c.apply(java.lang.Object):java.lang.Object");
    }
}
