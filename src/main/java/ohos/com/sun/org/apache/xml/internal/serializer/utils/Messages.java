package ohos.com.sun.org.apache.xml.internal.serializer.utils;

import java.util.ListResourceBundle;
import java.util.Locale;
import ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport;

public final class Messages {
    private final Locale m_locale = Locale.getDefault();
    private ListResourceBundle m_resourceBundle;
    private String m_resourceBundleName;

    Messages(String str) {
        this.m_resourceBundleName = str;
    }

    private Locale getLocale() {
        return this.m_locale;
    }

    public final String createMessage(String str, Object[] objArr) {
        if (this.m_resourceBundle == null) {
            this.m_resourceBundle = SecuritySupport.getResourceBundle(this.m_resourceBundleName);
        }
        ListResourceBundle listResourceBundle = this.m_resourceBundle;
        if (listResourceBundle != null) {
            return createMsg(listResourceBundle, str, objArr);
        }
        return "Could not load the resource bundles: " + this.m_resourceBundleName;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x009c A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x009d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.String createMsg(java.util.ListResourceBundle r8, java.lang.String r9, java.lang.Object[] r10) {
        /*
        // Method dump skipped, instructions count: 163
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.serializer.utils.Messages.createMsg(java.util.ListResourceBundle, java.lang.String, java.lang.Object[]):java.lang.String");
    }
}
