package ohos.com.sun.org.apache.xerces.internal.util;

public final class SecurityManager {
    private static final int DEFAULT_ELEMENT_ATTRIBUTE_LIMIT = 10000;
    private static final int DEFAULT_ENTITY_EXPANSION_LIMIT = 64000;
    private static final int DEFAULT_MAX_OCCUR_NODE_LIMIT = 5000;
    private int entityExpansionLimit = DEFAULT_ENTITY_EXPANSION_LIMIT;
    private int fElementAttributeLimit = 10000;
    private int maxOccurLimit = DEFAULT_MAX_OCCUR_NODE_LIMIT;

    public SecurityManager() {
        readSystemProperties();
    }

    public void setEntityExpansionLimit(int i) {
        this.entityExpansionLimit = i;
    }

    public int getEntityExpansionLimit() {
        return this.entityExpansionLimit;
    }

    public void setMaxOccurNodeLimit(int i) {
        this.maxOccurLimit = i;
    }

    public int getMaxOccurNodeLimit() {
        return this.maxOccurLimit;
    }

    public int getElementAttrLimit() {
        return this.fElementAttributeLimit;
    }

    public void setElementAttrLimit(int i) {
        this.fElementAttributeLimit = i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x004b A[Catch:{ Exception -> 0x0060 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readSystemProperties() {
        /*
            r4 = this;
            java.lang.String r0 = ""
            java.lang.String r1 = "entityExpansionLimit"
            java.lang.String r1 = java.lang.System.getProperty(r1)     // Catch:{ Exception -> 0x0022 }
            r2 = 64000(0xfa00, float:8.9683E-41)
            if (r1 == 0) goto L_0x0020
            boolean r3 = r1.equals(r0)     // Catch:{ Exception -> 0x0022 }
            if (r3 != 0) goto L_0x0020
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0022 }
            r4.entityExpansionLimit = r1     // Catch:{ Exception -> 0x0022 }
            int r1 = r4.entityExpansionLimit     // Catch:{ Exception -> 0x0022 }
            if (r1 >= 0) goto L_0x0022
            r4.entityExpansionLimit = r2     // Catch:{ Exception -> 0x0022 }
            goto L_0x0022
        L_0x0020:
            r4.entityExpansionLimit = r2     // Catch:{ Exception -> 0x0022 }
        L_0x0022:
            java.lang.String r1 = "maxOccurLimit"
            java.lang.String r1 = java.lang.System.getProperty(r1)     // Catch:{ Exception -> 0x0041 }
            r2 = 5000(0x1388, float:7.006E-42)
            if (r1 == 0) goto L_0x003f
            boolean r3 = r1.equals(r0)     // Catch:{ Exception -> 0x0041 }
            if (r3 != 0) goto L_0x003f
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0041 }
            r4.maxOccurLimit = r1     // Catch:{ Exception -> 0x0041 }
            int r1 = r4.maxOccurLimit     // Catch:{ Exception -> 0x0041 }
            if (r1 >= 0) goto L_0x0041
            r4.maxOccurLimit = r2     // Catch:{ Exception -> 0x0041 }
            goto L_0x0041
        L_0x003f:
            r4.maxOccurLimit = r2     // Catch:{ Exception -> 0x0041 }
        L_0x0041:
            java.lang.String r1 = "elementAttributeLimit"
            java.lang.String r1 = java.lang.System.getProperty(r1)     // Catch:{ Exception -> 0x0060 }
            r2 = 10000(0x2710, float:1.4013E-41)
            if (r1 == 0) goto L_0x005e
            boolean r0 = r1.equals(r0)     // Catch:{ Exception -> 0x0060 }
            if (r0 != 0) goto L_0x005e
            int r0 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0060 }
            r4.fElementAttributeLimit = r0     // Catch:{ Exception -> 0x0060 }
            int r0 = r4.fElementAttributeLimit     // Catch:{ Exception -> 0x0060 }
            if (r0 >= 0) goto L_0x0060
            r4.fElementAttributeLimit = r2     // Catch:{ Exception -> 0x0060 }
            goto L_0x0060
        L_0x005e:
            r4.fElementAttributeLimit = r2     // Catch:{ Exception -> 0x0060 }
        L_0x0060:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.util.SecurityManager.readSystemProperties():void");
    }
}
