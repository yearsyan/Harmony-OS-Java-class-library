package ohos.com.sun.org.apache.xpath.internal;

import java.io.PrintStream;
import java.io.PrintWriter;
import ohos.javax.xml.transform.TransformerException;
import ohos.org.w3c.dom.Node;

public class XPathException extends TransformerException {
    static final long serialVersionUID = 4263549717619045963L;
    protected Exception m_exception;
    Object m_styleNode = null;

    public Object getStylesheetNode() {
        return this.m_styleNode;
    }

    public void setStylesheetNode(Object obj) {
        this.m_styleNode = obj;
    }

    public XPathException(String str, ExpressionNode expressionNode) {
        super(str);
        setLocator(expressionNode);
        setStylesheetNode(getStylesheetNode(expressionNode));
    }

    public XPathException(String str) {
        super(str);
    }

    public Node getStylesheetNode(ExpressionNode expressionNode) {
        ExpressionNode expressionOwner = getExpressionOwner(expressionNode);
        if (expressionOwner == null || !(expressionOwner instanceof Node)) {
            return null;
        }
        return (Node) expressionOwner;
    }

    /* access modifiers changed from: protected */
    public ExpressionNode getExpressionOwner(ExpressionNode expressionNode) {
        ExpressionNode exprGetParent = expressionNode.exprGetParent();
        while (exprGetParent != null && (exprGetParent instanceof Expression)) {
            exprGetParent = exprGetParent.exprGetParent();
        }
        return exprGetParent;
    }

    public XPathException(String str, Object obj) {
        super(str);
        this.m_styleNode = obj;
    }

    public XPathException(String str, Node node, Exception exc) {
        super(str);
        this.m_styleNode = node;
        this.m_exception = exc;
    }

    public XPathException(String str, Exception exc) {
        super(str);
        this.m_exception = exc;
    }

    @Override // java.lang.Throwable, ohos.javax.xml.transform.TransformerException
    public void printStackTrace(PrintStream printStream) {
        if (printStream == null) {
            printStream = System.err;
        }
        try {
            super.printStackTrace(printStream);
        } catch (Exception unused) {
        }
        Exception exc = this.m_exception;
        for (int i = 0; i < 10 && exc != null; i++) {
            printStream.println("---------");
            exc.printStackTrace(printStream);
            if (exc instanceof TransformerException) {
                Throwable exception = ((TransformerException) exc).getException();
                if (exc != exception) {
                    exc = exception;
                } else {
                    return;
                }
            } else {
                exc = null;
            }
        }
    }

    public String getMessage() {
        String message = super.getMessage();
        Exception exc = this.m_exception;
        while (exc != null) {
            String message2 = exc.getMessage();
            if (message2 != null) {
                message = message2;
            }
            if (exc instanceof TransformerException) {
                Throwable exception = ((TransformerException) exc).getException();
                if (exc == exception) {
                    break;
                }
                exc = exception;
            } else {
                exc = null;
            }
        }
        return message != null ? message : "";
    }

    @Override // java.lang.Throwable, ohos.javax.xml.transform.TransformerException
    public void printStackTrace(PrintWriter printWriter) {
        boolean z;
        if (printWriter == null) {
            printWriter = new PrintWriter(System.err);
        }
        try {
            super.printStackTrace(printWriter);
        } catch (Exception unused) {
        }
        try {
            Throwable.class.getMethod("getCause", null);
            z = true;
        } catch (NoSuchMethodException unused2) {
            z = false;
        }
        if (!z) {
            Exception exc = this.m_exception;
            for (int i = 0; i < 10 && exc != null; i++) {
                printWriter.println("---------");
                try {
                    exc.printStackTrace(printWriter);
                } catch (Exception unused3) {
                    printWriter.println("Could not print stack trace...");
                }
                if (exc instanceof TransformerException) {
                    Throwable exception = ((TransformerException) exc).getException();
                    if (exc != exception) {
                        exc = exception;
                    } else {
                        return;
                    }
                } else {
                    exc = null;
                }
            }
        }
    }

    @Override // ohos.javax.xml.transform.TransformerException
    public Throwable getException() {
        return this.m_exception;
    }
}
