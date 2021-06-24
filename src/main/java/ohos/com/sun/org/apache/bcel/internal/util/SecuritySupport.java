package ohos.com.sun.org.apache.bcel.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ListResourceBundle;
import java.util.Locale;

public final class SecuritySupport {
    private static final SecuritySupport securitySupport = new SecuritySupport();

    public static SecuritySupport getInstance() {
        return securitySupport;
    }

    static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass1 */

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
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass2 */

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
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass3 */

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
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass4 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(str);
            }
        });
    }

    static FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass5 */

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
        return getResourceAsStream(findClassLoader(), str);
    }

    public static InputStream getResourceAsStream(final ClassLoader classLoader, final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass6 */

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
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass7 */

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
                throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass7.run():java.util.ListResourceBundle");
            }
        });
    }

    public static String[] getFileList(final File file, final FilenameFilter filenameFilter) {
        return (String[]) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass8 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return file.list(filenameFilter);
            }
        });
    }

    public static boolean getFileExists(final File file) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass9 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return file.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
    }

    static long getLastModified(final File file) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction() {
            /* class ohos.com.sun.org.apache.bcel.internal.util.SecuritySupport.AnonymousClass10 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                return new Long(file.lastModified());
            }
        })).longValue();
    }

    public static ClassLoader findClassLoader() {
        if (System.getSecurityManager() != null) {
            return null;
        }
        return SecuritySupport.class.getClassLoader();
    }

    private SecuritySupport() {
    }
}
