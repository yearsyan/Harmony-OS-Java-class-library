package ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers;

import java.util.Locale;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import ohos.com.sun.org.apache.xerces.internal.util.DOMUtil;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolTable;
import ohos.com.sun.org.apache.xerces.internal.util.XMLChar;
import ohos.com.sun.org.apache.xerces.internal.xni.QName;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObject;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList;
import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.Element;

/* access modifiers changed from: package-private */
public class XSDElementTraverser extends XSDAbstractTraverser {
    boolean fDeferTraversingLocalElements;
    protected final XSElementDecl fTempElementDecl = new XSElementDecl();

    XSDElementTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    /* access modifiers changed from: package-private */
    public XSParticleDecl traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int i, XSObject xSObject) {
        XSParticleDecl xSParticleDecl;
        if (this.fSchemaHandler.fDeclPool != null) {
            xSParticleDecl = this.fSchemaHandler.fDeclPool.getParticleDecl();
        } else {
            xSParticleDecl = new XSParticleDecl();
        }
        if (this.fDeferTraversingLocalElements) {
            xSParticleDecl.fType = 1;
            Attr attributeNode = element.getAttributeNode(SchemaSymbols.ATT_MINOCCURS);
            if (attributeNode != null) {
                try {
                    int parseInt = Integer.parseInt(XMLChar.trim(attributeNode.getValue()));
                    if (parseInt >= 0) {
                        xSParticleDecl.fMinOccurs = parseInt;
                    }
                } catch (NumberFormatException unused) {
                }
            }
            this.fSchemaHandler.fillInLocalElemInfo(element, xSDocumentInfo, i, xSObject, xSParticleDecl);
            return xSParticleDecl;
        }
        traverseLocal(xSParticleDecl, element, xSDocumentInfo, schemaGrammar, i, xSObject, null);
        if (xSParticleDecl.fType == 0) {
            return null;
        }
        return xSParticleDecl;
    }

    /* access modifiers changed from: protected */
    public void traverseLocal(XSParticleDecl xSParticleDecl, Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, int i, XSObject xSObject, String[] strArr) {
        short s;
        XSAnnotationImpl xSAnnotationImpl;
        XSElementDecl xSElementDecl;
        XSObjectList xSObjectList;
        XSObjectListImpl xSObjectListImpl;
        if (strArr != null) {
            xSDocumentInfo.fNamespaceSupport.setEffectiveContext(strArr);
        }
        Object[] checkAttributes = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        QName qName = (QName) checkAttributes[XSAttributeChecker.ATTIDX_REF];
        XInt xInt = (XInt) checkAttributes[XSAttributeChecker.ATTIDX_MINOCCURS];
        XInt xInt2 = (XInt) checkAttributes[XSAttributeChecker.ATTIDX_MAXOCCURS];
        XSAnnotationImpl xSAnnotationImpl2 = null;
        if (element.getAttributeNode(SchemaSymbols.ATT_REF) == null) {
            s = 1;
            xSElementDecl = traverseNamedElement(element, checkAttributes, xSDocumentInfo, schemaGrammar, false, xSObject);
            xSAnnotationImpl = null;
        } else if (qName != null) {
            XSElementDecl xSElementDecl2 = (XSElementDecl) this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 3, qName, element);
            Element firstChildElement = DOMUtil.getFirstChildElement(element);
            if (firstChildElement == null || !DOMUtil.getLocalName(firstChildElement).equals(SchemaSymbols.ELT_ANNOTATION)) {
                String syntheticAnnotation = DOMUtil.getSyntheticAnnotation(element);
                if (syntheticAnnotation != null) {
                    xSAnnotationImpl2 = traverseSyntheticAnnotation(element, syntheticAnnotation, checkAttributes, false, xSDocumentInfo);
                    firstChildElement = firstChildElement;
                }
            } else {
                XSAnnotationImpl traverseAnnotationDecl = traverseAnnotationDecl(firstChildElement, checkAttributes, false, xSDocumentInfo);
                firstChildElement = DOMUtil.getNextSiblingElement(firstChildElement);
                xSAnnotationImpl2 = traverseAnnotationDecl;
            }
            if (firstChildElement != null) {
                reportSchemaError("src-element.2.2", new Object[]{qName.rawname, DOMUtil.getLocalName(firstChildElement)}, firstChildElement);
            }
            s = 1;
            xSAnnotationImpl = xSAnnotationImpl2;
            xSElementDecl = xSElementDecl2;
        } else {
            s = 1;
            xSElementDecl = null;
            xSAnnotationImpl = null;
        }
        xSParticleDecl.fMinOccurs = xInt.intValue();
        xSParticleDecl.fMaxOccurs = xInt2.intValue();
        if (xSElementDecl != null) {
            xSParticleDecl.fType = s;
            xSParticleDecl.fValue = xSElementDecl;
        } else {
            xSParticleDecl.fType = 0;
        }
        if (qName != null) {
            if (xSAnnotationImpl != null) {
                xSObjectListImpl = new XSObjectListImpl();
                xSObjectListImpl.addXSObject(xSAnnotationImpl);
            } else {
                xSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
            }
            xSParticleDecl.fAnnotations = xSObjectListImpl;
        } else {
            if (xSElementDecl != null) {
                xSObjectList = xSElementDecl.fAnnotations;
            } else {
                xSObjectList = XSObjectListImpl.EMPTY_LIST;
            }
            xSParticleDecl.fAnnotations = xSObjectList;
        }
        checkOccurrences(xSParticleDecl, SchemaSymbols.ELT_ELEMENT, (Element) element.getParentNode(), i, ((Long) checkAttributes[XSAttributeChecker.ATTIDX_FROMDEFAULT]).longValue());
        this.fAttrChecker.returnAttrArray(checkAttributes, xSDocumentInfo);
    }

    /* access modifiers changed from: package-private */
    public XSElementDecl traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] checkAttributes = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        XSElementDecl traverseNamedElement = traverseNamedElement(element, checkAttributes, xSDocumentInfo, schemaGrammar, true, null);
        this.fAttrChecker.returnAttrArray(checkAttributes, xSDocumentInfo);
        return traverseNamedElement;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x02c5  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x02d7  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x02f8  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0319  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x031e  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0356  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x03c2  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x03fa A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x03fb  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00f3  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0103  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0114  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x015e  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0181  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x018d  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01c7  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01cd A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01ec  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01f2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl traverseNamedElement(ohos.org.w3c.dom.Element r23, java.lang.Object[] r24, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r25, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar r26, boolean r27, ohos.com.sun.org.apache.xerces.internal.xs.XSObject r28) {
        /*
        // Method dump skipped, instructions count: 1073
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDElementTraverser.traverseNamedElement(ohos.org.w3c.dom.Element, java.lang.Object[], ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar, boolean, ohos.com.sun.org.apache.xerces.internal.xs.XSObject):ohos.com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl");
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser
    public void reset(SymbolTable symbolTable, boolean z, Locale locale) {
        super.reset(symbolTable, z, locale);
        this.fDeferTraversingLocalElements = true;
    }
}
