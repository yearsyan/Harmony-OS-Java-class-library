package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import ohos.com.sun.org.apache.xml.internal.dtm.DTMDOMException;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMIterator;
import ohos.com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.traversal.NodeFilter;
import ohos.org.w3c.dom.traversal.NodeIterator;

public class DTMNodeIterator implements NodeIterator {
    private DTMIterator dtm_iter;
    private boolean valid = true;

    @Override // ohos.org.w3c.dom.traversal.NodeIterator
    public boolean getExpandEntityReferences() {
        return false;
    }

    public DTMNodeIterator(DTMIterator dTMIterator) {
        try {
            this.dtm_iter = (DTMIterator) dTMIterator.clone();
        } catch (CloneNotSupportedException e) {
            throw new WrappedRuntimeException(e);
        }
    }

    public DTMIterator getDTMIterator() {
        return this.dtm_iter;
    }

    @Override // ohos.org.w3c.dom.traversal.NodeIterator
    public void detach() {
        this.valid = false;
    }

    @Override // ohos.org.w3c.dom.traversal.NodeIterator
    public NodeFilter getFilter() {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.traversal.NodeIterator
    public Node getRoot() {
        int root = this.dtm_iter.getRoot();
        return this.dtm_iter.getDTM(root).getNode(root);
    }

    @Override // ohos.org.w3c.dom.traversal.NodeIterator
    public int getWhatToShow() {
        return this.dtm_iter.getWhatToShow();
    }

    @Override // ohos.org.w3c.dom.traversal.NodeIterator
    public Node nextNode() throws DOMException {
        if (this.valid) {
            int nextNode = this.dtm_iter.nextNode();
            if (nextNode == -1) {
                return null;
            }
            return this.dtm_iter.getDTM(nextNode).getNode(nextNode);
        }
        throw new DTMDOMException(11);
    }

    @Override // ohos.org.w3c.dom.traversal.NodeIterator
    public Node previousNode() {
        if (this.valid) {
            int previousNode = this.dtm_iter.previousNode();
            if (previousNode == -1) {
                return null;
            }
            return this.dtm_iter.getDTM(previousNode).getNode(previousNode);
        }
        throw new DTMDOMException(11);
    }
}
