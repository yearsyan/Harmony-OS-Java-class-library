package ohos.com.sun.org.apache.xerces.internal.xni;

public class XNIException extends RuntimeException {
    static final long serialVersionUID = 9019819772686063775L;
    private Exception fException;

    public XNIException(String str) {
        super(str);
    }

    public XNIException(Exception exc) {
        super(exc.getMessage());
        this.fException = exc;
    }

    public XNIException(String str, Exception exc) {
        super(str);
        this.fException = exc;
    }

    public Exception getException() {
        return this.fException;
    }

    public Throwable getCause() {
        return this.fException;
    }
}
