package ohos.com.sun.org.apache.xerces.internal.dom;

import java.io.OutputStream;
import java.io.Writer;
import ohos.org.w3c.dom.ls.LSOutput;

public class DOMOutputImpl implements LSOutput {
    protected OutputStream fByteStream = null;
    protected Writer fCharStream = null;
    protected String fEncoding = null;
    protected String fSystemId = null;

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public Writer getCharacterStream() {
        return this.fCharStream;
    }

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public void setCharacterStream(Writer writer) {
        this.fCharStream = writer;
    }

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public OutputStream getByteStream() {
        return this.fByteStream;
    }

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public void setByteStream(OutputStream outputStream) {
        this.fByteStream = outputStream;
    }

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public String getSystemId() {
        return this.fSystemId;
    }

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public void setSystemId(String str) {
        this.fSystemId = str;
    }

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public String getEncoding() {
        return this.fEncoding;
    }

    @Override // ohos.org.w3c.dom.ls.LSOutput
    public void setEncoding(String str) {
        this.fEncoding = str;
    }
}
