package ohos.com.sun.org.apache.xalan.internal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Properties;
import ohos.ai.asr.util.AsrConstants;

public final class SecuritySupport {
    static final Properties cacheProps = new Properties();
    static volatile boolean firstTime = true;
    private static final SecuritySupport securitySupport = new SecuritySupport();

    public static SecuritySupport getInstance() {
        return securitySupport;
    }

    public static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass1 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                try {
                    return Thread.currentThread().getContextClassLoader();
                } catch (SecurityException unused) {
                    return null;
                }
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass2 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                try {
                    return ClassLoader.getSystemClassLoader();
                } catch (SecurityException unused) {
                    return null;
                }
            }
        });
    }

    static ClassLoader getParentClassLoader(final ClassLoader classLoader) {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass3 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader classLoader;
                try {
                    classLoader = classLoader.getParent();
                } catch (SecurityException unused) {
                    classLoader = null;
                }
                if (classLoader == classLoader) {
                    return null;
                }
                return classLoader;
            }
        });
    }

    public static String getSystemProperty(final String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass4 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(str);
            }
        });
    }

    public static String getSystemProperty(final String str, final String str2) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass5 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(str, str2);
            }
        });
    }

    static FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass6 */

                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((FileNotFoundException) e.getException());
        }
    }

    public static InputStream getResourceAsStream(String str) {
        if (System.getSecurityManager() != null) {
            return getResourceAsStream(null, str);
        }
        return getResourceAsStream(ObjectFactory.findClassLoader(), str);
    }

    public static InputStream getResourceAsStream(final ClassLoader classLoader, final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass7 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader classLoader = classLoader;
                if (classLoader != null) {
                    return classLoader.getResourceAsStream(str);
                }
                return Object.class.getResourceAsStream("/" + str);
            }
        });
    }

    public static ListResourceBundle getResourceBundle(String str) {
        return getResourceBundle(str, Locale.getDefault());
    }

    public static ListResourceBundle getResourceBundle(final String str, final Locale locale) {
        return (ListResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ListResourceBundle>() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass8 */

            /* JADX WARNING: Code restructure failed: missing block: B:5:0x001c, code lost:
                return (java.util.ListResourceBundle) java.util.ResourceBundle.getBundle(r1, new java.util.Locale("en", "US"));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:8:0x0039, code lost:
                throw new java.util.MissingResourceException("Could not load any resource bundle by " + r1, r1, "");
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x000b */
            @Override // java.security.PrivilegedAction
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.util.ListResourceBundle run() {
                /*
                    r4 = this;
                    java.lang.String r0 = r1     // Catch:{ MissingResourceException -> 0x000b }
                    java.util.Locale r1 = r2     // Catch:{ MissingResourceException -> 0x000b }
                    java.util.ResourceBundle r0 = java.util.ResourceBundle.getBundle(r0, r1)     // Catch:{ MissingResourceException -> 0x000b }
                    java.util.ListResourceBundle r0 = (java.util.ListResourceBundle) r0     // Catch:{ MissingResourceException -> 0x000b }
                    return r0
                L_0x000b:
                    java.lang.String r0 = r1     // Catch:{ MissingResourceException -> 0x001d }
                    java.util.Locale r1 = new java.util.Locale     // Catch:{ MissingResourceException -> 0x001d }
                    java.lang.String r2 = "en"
                    java.lang.String r3 = "US"
                    r1.<init>(r2, r3)     // Catch:{ MissingResourceException -> 0x001d }
                    java.util.ResourceBundle r0 = java.util.ResourceBundle.getBundle(r0, r1)     // Catch:{ MissingResourceException -> 0x001d }
                    java.util.ListResourceBundle r0 = (java.util.ListResourceBundle) r0     // Catch:{ MissingResourceException -> 0x001d }
                    return r0
                L_0x001d:
                    java.util.MissingResourceException r0 = new java.util.MissingResourceException
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r2 = "Could not load any resource bundle by "
                    r1.append(r2)
                    java.lang.String r2 = r1
                    r1.append(r2)
                    java.lang.String r1 = r1.toString()
                    java.lang.String r4 = r1
                    java.lang.String r2 = ""
                    r0.<init>(r1, r4, r2)
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass8.run():java.util.ListResourceBundle");
            }
        });
    }

    public static boolean getFileExists(final File file) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass9 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return file.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
    }

    static long getLastModified(final File file) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.AnonymousClass10 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return new Long(file.lastModified());
            }
        })).longValue();
    }

    public static String sanitizePath(String str) {
        int lastIndexOf;
        if (str != null && (lastIndexOf = str.lastIndexOf("/")) > 0) {
            return str.substring(lastIndexOf + 1, str.length());
        }
        return "";
    }

    public static String checkAccess(String str, String str2, String str3) throws IOException {
        String str4;
        if (str == null || (str2 != null && str2.equalsIgnoreCase(str3))) {
            return null;
        }
        if (str.indexOf(":") == -1) {
            str4 = AsrConstants.ASR_SRC_FILE;
        } else {
            URL url = new URL(str);
            str4 = url.getProtocol();
            if (str4.equalsIgnoreCase("jar")) {
                String path = url.getPath();
                str4 = path.substring(0, path.indexOf(":"));
            }
        }
        if (isProtocolAllowed(str4, str2)) {
            return null;
        }
        return str4;
    }

    private static boolean isProtocolAllowed(String str, String str2) {
        if (str2 == null) {
            return false;
        }
        for (String str3 : str2.split(",")) {
            if (str3.trim().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
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
    static java.lang.String readJAXPProperty(java.lang.String r4) {
        /*
        // Method dump skipped, instructions count: 113
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.utils.SecuritySupport.readJAXPProperty(java.lang.String):java.lang.String");
    }

    private SecuritySupport() {
    }
}
