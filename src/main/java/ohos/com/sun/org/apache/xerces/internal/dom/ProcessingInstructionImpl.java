package ohos.com.sun.org.apache.xerces.internal.dom;

import ohos.org.w3c.dom.ProcessingInstruction;

public class ProcessingInstructionImpl extends CharacterDataImpl implements ProcessingInstruction {
    static final long serialVersionUID = 7554435174099981510L;
    protected String target;

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public short getNodeType() {
        return 7;
    }

    public ProcessingInstructionImpl(CoreDocumentImpl coreDocumentImpl, String str, String str2) {
        super(coreDocumentImpl, str2);
        this.target = str;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.target;
    }

    @Override // ohos.org.w3c.dom.ProcessingInstruction
    public String getTarget() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.target;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.CharacterDataImpl, ohos.org.w3c.dom.ProcessingInstruction
    public String getData() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.data;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.CharacterDataImpl, ohos.org.w3c.dom.ProcessingInstruction
    public void setData(String str) {
        setNodeValue(str);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getBaseURI() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.ownerNode.getBaseURI();
    }
}
