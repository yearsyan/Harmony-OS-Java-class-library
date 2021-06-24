package ohos.com.sun.org.apache.xml.internal.serializer;

import java.util.Properties;

public final class OutputPropertiesFactory {
    private static final Class ACCESS_CONTROLLER_CLASS = findAccessControllerClass();
    public static final String ORACLE_IS_STANDALONE = "http://www.oracle.com/xml/is-standalone";
    private static final String PROP_DIR = "com/sun/org/apache/xml/internal/serializer/";
    private static final String PROP_FILE_HTML = "output_html.properties";
    private static final String PROP_FILE_TEXT = "output_text.properties";
    private static final String PROP_FILE_UNKNOWN = "output_unknown.properties";
    private static final String PROP_FILE_XML = "output_xml.properties";
    public static final String S_BUILTIN_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xalan}";
    private static final String S_BUILTIN_EXTENSIONS_URL = "http://xml.apache.org/xalan";
    public static final String S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xslt}";
    public static final int S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN = 28;
    private static final String S_BUILTIN_OLD_EXTENSIONS_URL = "http://xml.apache.org/xslt";
    public static final String S_KEY_CONTENT_HANDLER = "{http://xml.apache.org/xalan}content-handler";
    public static final String S_KEY_ENTITIES = "{http://xml.apache.org/xalan}entities";
    public static final String S_KEY_INDENT_AMOUNT = "{http://xml.apache.org/xalan}indent-amount";
    public static final String S_KEY_LINE_SEPARATOR = "{http://xml.apache.org/xalan}line-separator";
    public static final String S_OMIT_META_TAG = "{http://xml.apache.org/xalan}omit-meta-tag";
    public static final String S_USE_URL_ESCAPING = "{http://xml.apache.org/xalan}use-url-escaping";
    private static final String S_XALAN_PREFIX = "org.apache.xslt.";
    private static final int S_XALAN_PREFIX_LEN = 16;
    private static final String S_XSLT_PREFIX = "xslt.output.";
    private static final int S_XSLT_PREFIX_LEN = 12;
    private static Properties m_html_properties = null;
    private static Integer m_synch_object = new Integer(1);
    private static Properties m_text_properties = null;
    private static Properties m_unknown_properties = null;
    private static Properties m_xml_properties = null;

    private static Class findAccessControllerClass() {
        try {
            return Class.forName("java.security.AccessController");
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001b, code lost:
        if (r7.equals("xml") == false) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001d, code lost:
        r7 = ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_xml_properties;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0027, code lost:
        if (r7.equals("html") == false) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002b, code lost:
        if (ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_html_properties != null) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0030, code lost:
        ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_html_properties = loadPropertiesFile(ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.PROP_FILE_HTML, ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_xml_properties);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003a, code lost:
        r7 = ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_html_properties;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0044, code lost:
        if (r7.equals("text") == false) goto L_0x0073;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0048, code lost:
        if (ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_text_properties != null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_text_properties = loadPropertiesFile(ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.PROP_FILE_TEXT, ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_xml_properties);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x005d, code lost:
        if (ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_text_properties.getProperty("encoding") != null) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005f, code lost:
        ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_text_properties.put("encoding", ohos.com.sun.org.apache.xml.internal.serializer.Encodings.getMimeEncoding(null));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006c, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006d, code lost:
        r2 = ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.PROP_FILE_TEXT;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0070, code lost:
        r7 = ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_text_properties;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0079, code lost:
        if (r7.equals("") == false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007d, code lost:
        if (ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_unknown_properties != null) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x007f, code lost:
        ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_unknown_properties = loadPropertiesFile(ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.PROP_FILE_UNKNOWN, ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_xml_properties);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008c, code lost:
        r7 = ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_unknown_properties;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x008f, code lost:
        r7 = ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.m_xml_properties;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0096, code lost:
        return new java.util.Properties(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x009d, code lost:
        r0 = e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.util.Properties getDefaultMethodProperties(java.lang.String r7) {
        /*
        // Method dump skipped, instructions count: 187
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.getDefaultMethodProperties(java.lang.String):java.util.Properties");
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x008e A[SYNTHETIC, Splitter:B:52:0x008e] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x008f A[Catch:{ IOException -> 0x009f, SecurityException -> 0x008a, all -> 0x0087, all -> 0x00b4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00a3 A[Catch:{ IOException -> 0x009f, SecurityException -> 0x008a, all -> 0x0087, all -> 0x00b4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00a4 A[Catch:{ IOException -> 0x009f, SecurityException -> 0x008a, all -> 0x0087, all -> 0x00b4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00b7  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00bc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Properties loadPropertiesFile(final java.lang.String r7, java.util.Properties r8) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 192
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory.loadPropertiesFile(java.lang.String, java.util.Properties):java.util.Properties");
    }

    private static String fixupPropertyString(String str, boolean z) {
        if (z && str.startsWith(S_XSLT_PREFIX)) {
            str = str.substring(S_XSLT_PREFIX_LEN);
        }
        if (str.startsWith(S_XALAN_PREFIX)) {
            str = S_BUILTIN_EXTENSIONS_UNIVERSAL + str.substring(S_XALAN_PREFIX_LEN);
        }
        int indexOf = str.indexOf("\\u003a");
        if (indexOf <= 0) {
            return str;
        }
        return str.substring(0, indexOf) + ":" + str.substring(indexOf + 6);
    }
}
