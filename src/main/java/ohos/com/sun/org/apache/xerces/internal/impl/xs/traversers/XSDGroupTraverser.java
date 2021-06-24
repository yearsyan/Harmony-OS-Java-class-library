package ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers;

import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSGroupDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import ohos.com.sun.org.apache.xerces.internal.util.DOMUtil;
import ohos.com.sun.org.apache.xerces.internal.util.XMLSymbols;
import ohos.com.sun.org.apache.xerces.internal.xni.QName;
import ohos.org.w3c.dom.Element;

/* access modifiers changed from: package-private */
public class XSDGroupTraverser extends XSDAbstractParticleTraverser {
    XSDGroupTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0101  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0114  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl traverseLocal(ohos.org.w3c.dom.Element r21, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r22, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar r23) {
        /*
        // Method dump skipped, instructions count: 288
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDGroupTraverser.traverseLocal(ohos.org.w3c.dom.Element, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar):ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl");
    }

    /* access modifiers changed from: package-private */
    public XSGroupDecl traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        XSGroupDecl xSGroupDecl;
        XSAnnotationImpl xSAnnotationImpl;
        XSParticleDecl xSParticleDecl;
        Object grpOrAttrGrpRedefinedByRestriction;
        XSObjectListImpl xSObjectListImpl;
        String str;
        XSAnnotationImpl xSAnnotationImpl2;
        Element element2;
        int i;
        String str2;
        Element element3;
        String str3;
        Object[] checkAttributes = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        String str4 = (String) checkAttributes[XSAttributeChecker.ATTIDX_NAME];
        if (str4 == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{"group (global)", "name"}, element);
        }
        XSGroupDecl xSGroupDecl2 = new XSGroupDecl();
        Element firstChildElement = DOMUtil.getFirstChildElement(element);
        XSGroupDecl xSGroupDecl3 = null;
        if (firstChildElement == null) {
            reportSchemaError("s4s-elt-must-match.2", new Object[]{"group (global)", "(annotation?, (all | choice | sequence))"}, element);
            xSGroupDecl = xSGroupDecl2;
            xSParticleDecl = null;
            xSAnnotationImpl = null;
        } else {
            String localName = firstChildElement.getLocalName();
            if (localName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                XSAnnotationImpl traverseAnnotationDecl = traverseAnnotationDecl(firstChildElement, checkAttributes, true, xSDocumentInfo);
                Element nextSiblingElement = DOMUtil.getNextSiblingElement(firstChildElement);
                if (nextSiblingElement != null) {
                    localName = nextSiblingElement.getLocalName();
                }
                xSAnnotationImpl2 = traverseAnnotationDecl;
                str = "s4s-elt-must-match.2";
                xSGroupDecl = xSGroupDecl2;
                element2 = nextSiblingElement;
            } else {
                String syntheticAnnotation = DOMUtil.getSyntheticAnnotation(element);
                if (syntheticAnnotation != null) {
                    str = "s4s-elt-must-match.2";
                    xSGroupDecl = xSGroupDecl2;
                    element2 = firstChildElement;
                    localName = localName;
                    xSAnnotationImpl2 = traverseSyntheticAnnotation(element, syntheticAnnotation, checkAttributes, false, xSDocumentInfo);
                } else {
                    str = "s4s-elt-must-match.2";
                    xSGroupDecl = xSGroupDecl2;
                    element2 = firstChildElement;
                    xSAnnotationImpl2 = null;
                }
            }
            if (element2 == null) {
                reportSchemaError(str, new Object[]{"group (global)", "(annotation?, (all | choice | sequence))"}, element);
                str2 = "s4s-elt-must-match.1";
                i = 3;
                element3 = element2;
            } else {
                if (localName.equals(SchemaSymbols.ELT_ALL)) {
                    str3 = "s4s-elt-must-match.1";
                    i = 3;
                    xSParticleDecl = traverseAll(element2, xSDocumentInfo, schemaGrammar, 4, xSGroupDecl);
                    element3 = element2;
                } else {
                    str3 = "s4s-elt-must-match.1";
                    i = 3;
                    if (localName.equals(SchemaSymbols.ELT_CHOICE)) {
                        element3 = element2;
                        xSParticleDecl = traverseChoice(element3, xSDocumentInfo, schemaGrammar, 4, xSGroupDecl);
                    } else if (localName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                        element3 = element2;
                        xSParticleDecl = traverseSequence(element3, xSDocumentInfo, schemaGrammar, 4, xSGroupDecl);
                    } else {
                        element3 = element2;
                        str2 = str3;
                        reportSchemaError(str2, new Object[]{"group (global)", "(annotation?, (all | choice | sequence))", DOMUtil.getLocalName(element2)}, element3);
                    }
                }
                str2 = str3;
                if (!(element3 == null || DOMUtil.getNextSiblingElement(element3) == null)) {
                    Object[] objArr = new Object[i];
                    objArr[0] = "group (global)";
                    objArr[1] = "(annotation?, (all | choice | sequence))";
                    objArr[2] = DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(element3));
                    reportSchemaError(str2, objArr, DOMUtil.getNextSiblingElement(element3));
                }
                xSAnnotationImpl = xSAnnotationImpl2;
            }
            xSParticleDecl = null;
            Object[] objArr2 = new Object[i];
            objArr2[0] = "group (global)";
            objArr2[1] = "(annotation?, (all | choice | sequence))";
            objArr2[2] = DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(element3));
            reportSchemaError(str2, objArr2, DOMUtil.getNextSiblingElement(element3));
            xSAnnotationImpl = xSAnnotationImpl2;
        }
        if (str4 != null) {
            xSGroupDecl.fName = str4;
            xSGroupDecl.fTargetNamespace = xSDocumentInfo.fTargetNamespace;
            if (xSParticleDecl == null) {
                xSParticleDecl = XSConstraints.getEmptySequence();
            }
            xSGroupDecl.fModelGroup = (XSModelGroupImpl) xSParticleDecl.fValue;
            if (xSAnnotationImpl != null) {
                xSObjectListImpl = new XSObjectListImpl();
                xSObjectListImpl.addXSObject(xSAnnotationImpl);
            } else {
                xSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
            }
            xSGroupDecl.fAnnotations = xSObjectListImpl;
            if (schemaGrammar.getGlobalGroupDecl(xSGroupDecl.fName) == null) {
                schemaGrammar.addGlobalGroupDecl(xSGroupDecl);
            }
            String schemaDocument2SystemId = this.fSchemaHandler.schemaDocument2SystemId(xSDocumentInfo);
            XSGroupDecl globalGroupDecl = schemaGrammar.getGlobalGroupDecl(xSGroupDecl.fName, schemaDocument2SystemId);
            if (globalGroupDecl == null) {
                schemaGrammar.addGlobalGroupDecl(xSGroupDecl, schemaDocument2SystemId);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                XSGroupDecl xSGroupDecl4 = globalGroupDecl != null ? globalGroupDecl : xSGroupDecl;
                this.fSchemaHandler.addGlobalGroupDecl(xSGroupDecl4);
                xSGroupDecl3 = xSGroupDecl4;
            } else {
                xSGroupDecl3 = xSGroupDecl;
            }
        }
        if (!(xSGroupDecl3 == null || (grpOrAttrGrpRedefinedByRestriction = this.fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(4, new QName(XMLSymbols.EMPTY_STRING, str4, str4, xSDocumentInfo.fTargetNamespace), xSDocumentInfo, element)) == null)) {
            schemaGrammar.addRedefinedGroupDecl(xSGroupDecl3, (XSGroupDecl) grpOrAttrGrpRedefinedByRestriction, this.fSchemaHandler.element2Locator(element));
        }
        this.fAttrChecker.returnAttrArray(checkAttributes, xSDocumentInfo);
        return xSGroupDecl3;
    }
}
