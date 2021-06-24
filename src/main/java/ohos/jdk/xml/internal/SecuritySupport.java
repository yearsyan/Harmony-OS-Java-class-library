package ohos.jdk.xml.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;

/* access modifiers changed from: package-private */
public class SecuritySupport {
    static final Properties cacheProps = new Properties();
    static volatile boolean firstTime = true;

    private SecuritySupport() {
    }

    public static String getSystemProperty(final String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            /* class ohos.jdk.xml.internal.SecuritySupport.AnonymousClass1 */

            @Override // java.security.PrivilegedAction
            public String run() {
                return System.getProperty(str);
            }
        });
    }

    public static <T> T getJAXPSystemProperty(Class<T> cls, String str, String str2) {
        String jAXPSystemProperty = getJAXPSystemProperty(str);
        if (jAXPSystemProperty == null) {
            jAXPSystemProperty = str2;
        }
        if (Integer.class.isAssignableFrom(cls)) {
            return cls.cast(Integer.valueOf(Integer.parseInt(jAXPSystemProperty)));
        }
        if (Boolean.class.isAssignableFrom(cls)) {
            return cls.cast(Boolean.valueOf(Boolean.parseBoolean(jAXPSystemProperty)));
        }
        return cls.cast(jAXPSystemProperty);
    }

    public static String getJAXPSystemProperty(String str) {
        String systemProperty = getSystemProperty(str);
        return systemProperty == null ? readJAXPProperty(str) : systemProperty;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005c, code lost:
        if (r2 != null) goto L_0x005e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x006d, code lost:
        if (r2 != null) goto L_0x005e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0068 A[SYNTHETIC, Splitter:B:35:0x0068] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readJAXPProperty(java.lang.String r4) {
        /*
        // Method dump skipped, instructions count: 113
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.jdk.xml.internal.SecuritySupport.readJAXPProperty(java.lang.String):java.lang.String");
    }

    static boolean getFileExists(final File file) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            /* class ohos.jdk.xml.internal.SecuritySupport.AnonymousClass2 */

            @Override // java.security.PrivilegedAction
            public Boolean run() {
                return file.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
    }

    static FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() {
                /* class ohos.jdk.xml.internal.SecuritySupport.AnonymousClass3 */

                @Override // java.security.PrivilegedExceptionAction
                public FileInputStream run() throws Exception {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((FileNotFoundException) e.getException());
        }
    }
}
