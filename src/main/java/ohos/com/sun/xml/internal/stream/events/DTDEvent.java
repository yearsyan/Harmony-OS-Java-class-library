package ohos.com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import ohos.javax.xml.stream.events.DTD;

public class DTDEvent extends DummyEvent implements DTD {
    private String fDoctypeDeclaration;
    private List fEntities;
    private List fNotations;

    @Override // ohos.javax.xml.stream.events.DTD
    public Object getProcessedDTD() {
        return null;
    }

    public DTDEvent() {
        init();
    }

    public DTDEvent(String str) {
        init();
        this.fDoctypeDeclaration = str;
    }

    public void setDocumentTypeDeclaration(String str) {
        this.fDoctypeDeclaration = str;
    }

    @Override // ohos.javax.xml.stream.events.DTD
    public String getDocumentTypeDeclaration() {
        return this.fDoctypeDeclaration;
    }

    public void setEntities(List list) {
        this.fEntities = list;
    }

    @Override // ohos.javax.xml.stream.events.DTD
    public List getEntities() {
        return this.fEntities;
    }

    public void setNotations(List list) {
        this.fNotations = list;
    }

    @Override // ohos.javax.xml.stream.events.DTD
    public List getNotations() {
        return this.fNotations;
    }

    /* access modifiers changed from: protected */
    public void init() {
        setEventType(11);
    }

    public String toString() {
        return this.fDoctypeDeclaration;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.xml.internal.stream.events.DummyEvent
    public void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(this.fDoctypeDeclaration);
    }
}
