package ohos.com.sun.org.apache.xerces.internal.impl.dtd.models;

public class CMBinOp extends CMNode {
    private CMNode fLeftChild;
    private CMNode fRightChild;

    public CMBinOp(int i, CMNode cMNode, CMNode cMNode2) {
        super(i);
        if (type() == 4 || type() == 5) {
            this.fLeftChild = cMNode;
            this.fRightChild = cMNode2;
            return;
        }
        throw new RuntimeException("ImplementationMessages.VAL_BST");
    }

    /* access modifiers changed from: package-private */
    public final CMNode getLeft() {
        return this.fLeftChild;
    }

    /* access modifiers changed from: package-private */
    public final CMNode getRight() {
        return this.fRightChild;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public boolean isNullable() {
        if (type() == 4) {
            return this.fLeftChild.isNullable() || this.fRightChild.isNullable();
        }
        if (type() == 5) {
            return this.fLeftChild.isNullable() && this.fRightChild.isNullable();
        }
        throw new RuntimeException("ImplementationMessages.VAL_BST");
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public void calcFirstPos(CMStateSet cMStateSet) {
        if (type() == 4) {
            cMStateSet.setTo(this.fLeftChild.firstPos());
            cMStateSet.union(this.fRightChild.firstPos());
        } else if (type() == 5) {
            cMStateSet.setTo(this.fLeftChild.firstPos());
            if (this.fLeftChild.isNullable()) {
                cMStateSet.union(this.fRightChild.firstPos());
            }
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public void calcLastPos(CMStateSet cMStateSet) {
        if (type() == 4) {
            cMStateSet.setTo(this.fLeftChild.lastPos());
            cMStateSet.union(this.fRightChild.lastPos());
        } else if (type() == 5) {
            cMStateSet.setTo(this.fRightChild.lastPos());
            if (this.fRightChild.isNullable()) {
                cMStateSet.union(this.fLeftChild.lastPos());
            }
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }
}
