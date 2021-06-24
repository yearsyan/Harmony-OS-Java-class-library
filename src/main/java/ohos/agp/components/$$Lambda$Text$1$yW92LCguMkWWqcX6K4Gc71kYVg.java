package ohos.agp.components;

import java.util.function.BiConsumer;
import ohos.agp.styles.Value;
import ohos.agp.text.Font;

/* renamed from: ohos.agp.components.-$$Lambda$Text$1$yW92LCgu-MkWWqcX6K4Gc71kYVg  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$Text$1$yW92LCguMkWWqcX6K4Gc71kYVg implements BiConsumer {
    public static final /* synthetic */ $$Lambda$Text$1$yW92LCguMkWWqcX6K4Gc71kYVg INSTANCE = new $$Lambda$Text$1$yW92LCguMkWWqcX6K4Gc71kYVg();

    private /* synthetic */ $$Lambda$Text$1$yW92LCguMkWWqcX6K4Gc71kYVg() {
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((Text) obj).setFont(new Font.Builder(((Value) obj2).asString()).build());
    }
}
