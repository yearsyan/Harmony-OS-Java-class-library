package ohos.security.permission.infrastructure.utils;

import java.util.function.Function;
import ohos.idn.BasicInfo;

/* renamed from: ohos.security.permission.infrastructure.utils.-$$Lambda$fC9po-oa3OKTyBEfmQtWaUxwcBc  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$fC9pooa3OKTyBEfmQtWaUxwcBc implements Function {
    public static final /* synthetic */ $$Lambda$fC9pooa3OKTyBEfmQtWaUxwcBc INSTANCE = new $$Lambda$fC9pooa3OKTyBEfmQtWaUxwcBc();

    private /* synthetic */ $$Lambda$fC9pooa3OKTyBEfmQtWaUxwcBc() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((BasicInfo) obj).getNodeId();
    }
}
