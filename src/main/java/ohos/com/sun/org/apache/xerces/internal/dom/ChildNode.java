package ohos.com.sun.org.apache.xerces.internal.dom;

import ohos.org.w3c.dom.Node;

public abstract class ChildNode extends NodeImpl {
    static final long serialVersionUID = -6112455738802414002L;
    transient StringBuffer fBufferStr = null;
    protected ChildNode nextSibling;
    protected ChildNode previousSibling;

    protected ChildNode(CoreDocumentImpl coreDocumentImpl) {
        super(coreDocumentImpl);
    }

    public ChildNode() {
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public Node cloneNode(boolean z) {
        ChildNode childNode = (ChildNode) super.cloneNode(z);
        childNode.previousSibling = null;
        childNode.nextSibling = null;
        childNode.isFirstChild(false);
        return childNode;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public Node getParentNode() {
        if (isOwned()) {
            return this.ownerNode;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl
    public final NodeImpl parentNode() {
        if (isOwned()) {
            return this.ownerNode;
        }
        return null;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public Node getNextSibling() {
        return this.nextSibling;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public Node getPreviousSibling() {
        if (isFirstChild()) {
            return null;
        }
        return this.previousSibling;
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl
    public final ChildNode previousSibling() {
        if (isFirstChild()) {
            return null;
        }
        return this.previousSibling;
    }
}
