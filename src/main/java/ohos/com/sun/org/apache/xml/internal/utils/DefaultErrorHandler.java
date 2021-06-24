package ohos.com.sun.org.apache.xml.internal.utils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import ohos.com.sun.org.apache.xml.internal.res.XMLMessages;
import ohos.javax.xml.transform.ErrorListener;
import ohos.javax.xml.transform.SourceLocator;
import ohos.javax.xml.transform.TransformerException;
import ohos.org.xml.sax.ErrorHandler;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXParseException;

public class DefaultErrorHandler implements ErrorHandler, ErrorListener {
    PrintWriter m_pw;
    boolean m_throwExceptionOnError;

    public DefaultErrorHandler(PrintWriter printWriter) {
        this.m_throwExceptionOnError = true;
        this.m_pw = printWriter;
    }

    public DefaultErrorHandler(PrintStream printStream) {
        this.m_throwExceptionOnError = true;
        this.m_pw = new PrintWriter((OutputStream) printStream, true);
    }

    public DefaultErrorHandler() {
        this(true);
    }

    public DefaultErrorHandler(boolean z) {
        this.m_throwExceptionOnError = true;
        this.m_pw = new PrintWriter((OutputStream) System.err, true);
        this.m_throwExceptionOnError = z;
    }

    @Override // ohos.org.xml.sax.ErrorHandler
    public void warning(SAXParseException sAXParseException) throws SAXException {
        printLocation(this.m_pw, sAXParseException);
        PrintWriter printWriter = this.m_pw;
        printWriter.println("Parser warning: " + sAXParseException.getMessage());
    }

    @Override // ohos.org.xml.sax.ErrorHandler
    public void error(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    @Override // ohos.org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    @Override // ohos.javax.xml.transform.ErrorListener
    public void warning(TransformerException transformerException) throws TransformerException {
        printLocation(this.m_pw, transformerException);
        this.m_pw.println(transformerException.getMessage());
    }

    @Override // ohos.javax.xml.transform.ErrorListener
    public void error(TransformerException transformerException) throws TransformerException {
        if (!this.m_throwExceptionOnError) {
            printLocation(this.m_pw, transformerException);
            this.m_pw.println(transformerException.getMessage());
            return;
        }
        throw transformerException;
    }

    @Override // ohos.javax.xml.transform.ErrorListener
    public void fatalError(TransformerException transformerException) throws TransformerException {
        if (!this.m_throwExceptionOnError) {
            printLocation(this.m_pw, transformerException);
            this.m_pw.println(transformerException.getMessage());
            return;
        }
        throw transformerException;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:16:0x0035 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1 */
    public static void ensureLocationSet(TransformerException transformerException) {
        SourceLocator locator;
        Throwable th = transformerException;
        SourceLocator sourceLocator = null;
        do {
            if (th instanceof SAXParseException) {
                sourceLocator = new SAXSourceLocator((SAXParseException) th);
            } else if ((th instanceof TransformerException) && (locator = ((TransformerException) th).getLocator()) != null) {
                sourceLocator = locator;
            }
            if (th instanceof TransformerException) {
                th = ((TransformerException) th).getCause();
                continue;
            } else if (th instanceof SAXException) {
                th = ((SAXException) th).getException();
                continue;
            } else {
                th = null;
                continue;
            }
        } while (th != null);
        transformerException.setLocator(sourceLocator);
    }

    public static void printLocation(PrintStream printStream, TransformerException transformerException) {
        printLocation(new PrintWriter(printStream), transformerException);
    }

    public static void printLocation(PrintStream printStream, SAXParseException sAXParseException) {
        printLocation(new PrintWriter(printStream), sAXParseException);
    }

    public static void printLocation(PrintWriter printWriter, Throwable th) {
        String str;
        SourceLocator locator;
        SourceLocator sourceLocator = null;
        do {
            if (th instanceof SAXParseException) {
                sourceLocator = new SAXSourceLocator((SAXParseException) th);
            } else if ((th instanceof TransformerException) && (locator = ((TransformerException) th).getLocator()) != null) {
                sourceLocator = locator;
            }
            if (th instanceof TransformerException) {
                th = ((TransformerException) th).getCause();
                continue;
            } else if (th instanceof WrappedRuntimeException) {
                th = ((WrappedRuntimeException) th).getException();
                continue;
            } else if (th instanceof SAXException) {
                th = ((SAXException) th).getException();
                continue;
            } else {
                th = null;
                continue;
            }
        } while (th != null);
        if (sourceLocator != null) {
            if (sourceLocator.getPublicId() != null) {
                str = sourceLocator.getPublicId();
            } else {
                str = sourceLocator.getSystemId() != null ? sourceLocator.getSystemId() : XMLMessages.createXMLMessage("ER_SYSTEMID_UNKNOWN", null);
            }
            printWriter.print(str + "; " + XMLMessages.createXMLMessage("line", null) + sourceLocator.getLineNumber() + "; " + XMLMessages.createXMLMessage("column", null) + sourceLocator.getColumnNumber() + "; ");
            return;
        }
        printWriter.print("(" + XMLMessages.createXMLMessage("ER_LOCATION_UNKNOWN", null) + ")");
    }
}
