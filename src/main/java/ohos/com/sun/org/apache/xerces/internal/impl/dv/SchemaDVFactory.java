package ohos.com.sun.org.apache.xerces.internal.impl.dv;

import ohos.com.sun.org.apache.xerces.internal.util.SymbolHash;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList;

public abstract class SchemaDVFactory {
    private static final String DEFAULT_FACTORY_CLASS = "ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl";

    public abstract XSSimpleType createTypeList(String str, String str2, short s, XSSimpleType xSSimpleType, XSObjectList xSObjectList);

    public abstract XSSimpleType createTypeRestriction(String str, String str2, short s, XSSimpleType xSSimpleType, XSObjectList xSObjectList);

    public abstract XSSimpleType createTypeUnion(String str, String str2, short s, XSSimpleType[] xSSimpleTypeArr, XSObjectList xSObjectList);

    public abstract XSSimpleType getBuiltInType(String str);

    public abstract SymbolHash getBuiltInTypes();

    public static final synchronized SchemaDVFactory getInstance() throws DVFactoryException {
        SchemaDVFactory instance;
        synchronized (SchemaDVFactory.class) {
            instance = getInstance(DEFAULT_FACTORY_CLASS);
        }
        return instance;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0029, code lost:
        throw new ohos.com.sun.org.apache.xerces.internal.impl.dv.DVFactoryException("Schema factory class " + r4 + " does not extend from SchemaDVFactory.");
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x000e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final synchronized ohos.com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory getInstance(java.lang.String r4) throws ohos.com.sun.org.apache.xerces.internal.impl.dv.DVFactoryException {
        /*
            java.lang.Class<ohos.com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory> r0 = ohos.com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory.class
            monitor-enter(r0)
            r1 = 1
            java.lang.Object r1 = ohos.com.sun.org.apache.xerces.internal.utils.ObjectFactory.newInstance(r4, r1)     // Catch:{ ClassCastException -> 0x000e }
            ohos.com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory r1 = (ohos.com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory) r1     // Catch:{ ClassCastException -> 0x000e }
            monitor-exit(r0)
            return r1
        L_0x000c:
            r4 = move-exception
            goto L_0x002a
        L_0x000e:
            ohos.com.sun.org.apache.xerces.internal.impl.dv.DVFactoryException r1 = new ohos.com.sun.org.apache.xerces.internal.impl.dv.DVFactoryException     // Catch:{ all -> 0x000c }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x000c }
            r2.<init>()     // Catch:{ all -> 0x000c }
            java.lang.String r3 = "Schema factory class "
            r2.append(r3)     // Catch:{ all -> 0x000c }
            r2.append(r4)     // Catch:{ all -> 0x000c }
            java.lang.String r4 = " does not extend from SchemaDVFactory."
            r2.append(r4)     // Catch:{ all -> 0x000c }
            java.lang.String r4 = r2.toString()     // Catch:{ all -> 0x000c }
            r1.<init>(r4)     // Catch:{ all -> 0x000c }
            throw r1     // Catch:{ all -> 0x000c }
        L_0x002a:
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory.getInstance(java.lang.String):ohos.com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory");
    }

    protected SchemaDVFactory() {
    }
}
