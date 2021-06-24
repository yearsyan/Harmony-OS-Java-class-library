package ohos.javax.xml.transform;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Objects;

public class TransformerException extends Exception {
    private static final long serialVersionUID = 975798773772956428L;
    Throwable containedException;
    SourceLocator locator;

    public SourceLocator getLocator() {
        return this.locator;
    }

    public void setLocator(SourceLocator sourceLocator) {
        this.locator = sourceLocator;
    }

    public Throwable getException() {
        return this.containedException;
    }

    public Throwable getCause() {
        Throwable th = this.containedException;
        if (th == this) {
            return null;
        }
        return th;
    }

    public synchronized Throwable initCause(Throwable th) {
        if (this.containedException != null) {
            throw new IllegalStateException("Can't overwrite cause");
        } else if (th != this) {
            this.containedException = th;
        } else {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        return this;
    }

    public TransformerException(String str) {
        this(str, null, null);
    }

    public TransformerException(Throwable th) {
        this(null, null, th);
    }

    public TransformerException(String str, Throwable th) {
        this(str, null, th);
    }

    public TransformerException(String str, SourceLocator sourceLocator) {
        this(str, sourceLocator, null);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TransformerException(java.lang.String r2, ohos.javax.xml.transform.SourceLocator r3, java.lang.Throwable r4) {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x0008
            int r0 = r2.length()
            if (r0 != 0) goto L_0x0011
        L_0x0008:
            if (r4 != 0) goto L_0x000d
            java.lang.String r2 = ""
            goto L_0x0011
        L_0x000d:
            java.lang.String r2 = r4.toString()
        L_0x0011:
            r1.<init>(r2)
            r1.containedException = r4
            r1.locator = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.javax.xml.transform.TransformerException.<init>(java.lang.String, ohos.javax.xml.transform.SourceLocator, java.lang.Throwable):void");
    }

    public String getMessageAndLocation() {
        return Objects.toString(super.getMessage(), "") + Objects.toString(getLocationAsString(), "");
    }

    public String getLocationAsString() {
        if (this.locator == null) {
            return null;
        }
        if (System.getSecurityManager() == null) {
            return getLocationString();
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            /* class ohos.javax.xml.transform.TransformerException.AnonymousClass1 */

            @Override // java.security.PrivilegedAction
            public String run() {
                return TransformerException.this.getLocationString();
            }
        }, new AccessControlContext(new ProtectionDomain[]{getNonPrivDomain()}));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String getLocationString() {
        if (this.locator == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String systemId = this.locator.getSystemId();
        int lineNumber = this.locator.getLineNumber();
        int columnNumber = this.locator.getColumnNumber();
        if (systemId != null) {
            sb.append("; SystemID: ");
            sb.append(systemId);
        }
        if (lineNumber != 0) {
            sb.append("; Line#: ");
            sb.append(lineNumber);
        }
        if (columnNumber != 0) {
            sb.append("; Column#: ");
            sb.append(columnNumber);
        }
        return sb.toString();
    }

    public void printStackTrace() {
        printStackTrace(new PrintWriter((OutputStream) System.err, true));
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream printStream) {
        printStackTrace(new PrintWriter(printStream));
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter printWriter) {
        String locationAsString;
        if (printWriter == null) {
            printWriter = new PrintWriter((OutputStream) System.err, true);
        }
        try {
            String locationAsString2 = getLocationAsString();
            if (locationAsString2 != null) {
                printWriter.println(locationAsString2);
            }
            super.printStackTrace(printWriter);
        } catch (Throwable unused) {
        }
        Throwable exception = getException();
        for (int i = 0; i < 10 && exception != null; i++) {
            printWriter.println("---------");
            try {
                if ((exception instanceof TransformerException) && (locationAsString = ((TransformerException) exception).getLocationAsString()) != null) {
                    printWriter.println(locationAsString);
                }
                exception.printStackTrace(printWriter);
            } catch (Throwable unused2) {
                printWriter.println("Could not print stack trace...");
            }
            Throwable th = null;
            try {
                Method method = exception.getClass().getMethod("getException", null);
                if (method == null) {
                    continue;
                } else {
                    Throwable th2 = (Throwable) method.invoke(exception, null);
                    if (exception == th2) {
                        break;
                    }
                    th = th2;
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused3) {
            }
            exception = th;
        }
        printWriter.flush();
    }

    private ProtectionDomain getNonPrivDomain() {
        return new ProtectionDomain(new CodeSource((URL) null, (CodeSigner[]) null), new Permissions());
    }
}
