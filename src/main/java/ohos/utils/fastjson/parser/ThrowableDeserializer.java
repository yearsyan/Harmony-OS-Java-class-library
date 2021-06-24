package ohos.utils.fastjson.parser;

public class ThrowableDeserializer extends JavaBeanDeserializer {
    public ThrowableDeserializer(ParserConfig parserConfig, Class<?> cls) {
        super(parserConfig, cls, cls);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [ohos.utils.fastjson.parser.JavaBeanDeserializer] */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        if (java.lang.Throwable.class.isAssignableFrom(r2) != false) goto L_0x0036;
     */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0185  */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // ohos.utils.fastjson.parser.deserializer.ObjectDeserializer, ohos.utils.fastjson.parser.JavaBeanDeserializer
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(ohos.utils.fastjson.parser.DefaultJSONParser r18, java.lang.reflect.Type r19, java.lang.Object r20) {
        /*
        // Method dump skipped, instructions count: 445
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.ThrowableDeserializer.deserialze(ohos.utils.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }
}
