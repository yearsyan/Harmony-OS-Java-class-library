package ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers;

import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObject;
import ohos.org.w3c.dom.Element;

/* access modifiers changed from: package-private */
public abstract class XSDAbstractParticleTraverser extends XSDAbstractTraverser {
    ParticleArray fPArray = new ParticleArray();

    XSDAbstractParticleTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00bd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl traverseAll(ohos.org.w3c.dom.Element r15, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r16, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar r17, int r18, ohos.com.sun.org.apache.xerces.internal.xs.XSObject r19) {
        /*
        // Method dump skipped, instructions count: 245
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractParticleTraverser.traverseAll(ohos.org.w3c.dom.Element, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar, int, ohos.com.sun.org.apache.xerces.internal.xs.XSObject):ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl");
    }

    /* access modifiers changed from: package-private */
    public XSParticleDecl traverseSequence(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int i, XSObject xSObject) {
        return traverseSeqChoice(element, xSDocumentInfo, schemaGrammar, i, false, xSObject);
    }

    /* access modifiers changed from: package-private */
    public XSParticleDecl traverseChoice(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int i, XSObject xSObject) {
        return traverseSeqChoice(element, xSDocumentInfo, schemaGrammar, i, true, xSObject);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0110  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0113  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0129  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0135  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0152  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0155  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl traverseSeqChoice(ohos.org.w3c.dom.Element r16, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r17, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar r18, int r19, boolean r20, ohos.com.sun.org.apache.xerces.internal.xs.XSObject r21) {
        /*
        // Method dump skipped, instructions count: 371
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractParticleTraverser.traverseSeqChoice(ohos.org.w3c.dom.Element, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar, int, boolean, ohos.com.sun.org.apache.xerces.internal.xs.XSObject):ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl");
    }

    /* access modifiers changed from: protected */
    public boolean hasAllContent(XSParticleDecl xSParticleDecl) {
        if (xSParticleDecl != null && xSParticleDecl.fType == 3 && ((XSModelGroupImpl) xSParticleDecl.fValue).fCompositor == 103) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public static class ParticleArray {
        int fContextCount = 0;
        XSParticleDecl[] fParticles = new XSParticleDecl[10];
        int[] fPos = new int[5];

        protected ParticleArray() {
        }

        /* access modifiers changed from: package-private */
        public void pushContext() {
            this.fContextCount++;
            int i = this.fContextCount;
            int[] iArr = this.fPos;
            if (i == iArr.length) {
                int[] iArr2 = new int[(i * 2)];
                System.arraycopy(iArr, 0, iArr2, 0, i);
                this.fPos = iArr2;
            }
            int[] iArr3 = this.fPos;
            int i2 = this.fContextCount;
            iArr3[i2] = iArr3[i2 - 1];
        }

        /* access modifiers changed from: package-private */
        public int getParticleCount() {
            int[] iArr = this.fPos;
            int i = this.fContextCount;
            return iArr[i] - iArr[i - 1];
        }

        /* access modifiers changed from: package-private */
        public void addParticle(XSParticleDecl xSParticleDecl) {
            int[] iArr = this.fPos;
            int i = this.fContextCount;
            int i2 = iArr[i];
            XSParticleDecl[] xSParticleDeclArr = this.fParticles;
            if (i2 == xSParticleDeclArr.length) {
                XSParticleDecl[] xSParticleDeclArr2 = new XSParticleDecl[(iArr[i] * 2)];
                System.arraycopy(xSParticleDeclArr, 0, xSParticleDeclArr2, 0, iArr[i]);
                this.fParticles = xSParticleDeclArr2;
            }
            XSParticleDecl[] xSParticleDeclArr3 = this.fParticles;
            int[] iArr2 = this.fPos;
            int i3 = this.fContextCount;
            int i4 = iArr2[i3];
            iArr2[i3] = i4 + 1;
            xSParticleDeclArr3[i4] = xSParticleDecl;
        }

        /* access modifiers changed from: package-private */
        public XSParticleDecl[] popContext() {
            int[] iArr = this.fPos;
            int i = this.fContextCount;
            int i2 = iArr[i] - iArr[i - 1];
            XSParticleDecl[] xSParticleDeclArr = null;
            if (i2 != 0) {
                XSParticleDecl[] xSParticleDeclArr2 = new XSParticleDecl[i2];
                System.arraycopy(this.fParticles, iArr[i - 1], xSParticleDeclArr2, 0, i2);
                for (int i3 = this.fPos[this.fContextCount - 1]; i3 < this.fPos[this.fContextCount]; i3++) {
                    this.fParticles[i3] = null;
                }
                xSParticleDeclArr = xSParticleDeclArr2;
            }
            this.fContextCount--;
            return xSParticleDeclArr;
        }
    }
}
