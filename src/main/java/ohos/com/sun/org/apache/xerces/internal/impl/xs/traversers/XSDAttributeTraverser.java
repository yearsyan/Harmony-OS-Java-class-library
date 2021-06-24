package ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers;

import ohos.com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import ohos.com.sun.org.apache.xerces.internal.util.DOMUtil;
import ohos.com.sun.org.apache.xerces.internal.xni.QName;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObject;
import ohos.org.w3c.dom.Element;

/* access modifiers changed from: package-private */
public class XSDAttributeTraverser extends XSDAbstractTraverser {
    public XSDAttributeTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    /* access modifiers changed from: protected */
    public XSAttributeUseImpl traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, XSComplexTypeDecl xSComplexTypeDecl) {
        XSAttributeDecl xSAttributeDecl;
        int i;
        XSObject xSObject;
        Object obj;
        String str;
        int i2;
        XSAttributeUseImpl xSAttributeUseImpl;
        short s;
        ValidatedInfo validatedInfo;
        short s2;
        XSObjectListImpl xSObjectListImpl;
        Object[] checkAttributes = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        String str2 = (String) checkAttributes[XSAttributeChecker.ATTIDX_DEFAULT];
        String str3 = (String) checkAttributes[XSAttributeChecker.ATTIDX_FIXED];
        Object obj2 = (String) checkAttributes[XSAttributeChecker.ATTIDX_NAME];
        QName qName = (QName) checkAttributes[XSAttributeChecker.ATTIDX_REF];
        XInt xInt = (XInt) checkAttributes[XSAttributeChecker.ATTIDX_USE];
        if (element.getAttributeNode(SchemaSymbols.ATT_REF) == null) {
            i = 1;
            obj = obj2;
            xSAttributeDecl = traverseNamedAttr(element, checkAttributes, xSDocumentInfo, schemaGrammar, false, xSComplexTypeDecl);
            xSObject = null;
        } else if (qName != null) {
            xSAttributeDecl = (XSAttributeDecl) this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 1, qName, element);
            Element firstChildElement = DOMUtil.getFirstChildElement(element);
            if (firstChildElement == null || !DOMUtil.getLocalName(firstChildElement).equals(SchemaSymbols.ELT_ANNOTATION)) {
                String syntheticAnnotation = DOMUtil.getSyntheticAnnotation(element);
                if (syntheticAnnotation != null) {
                    i = 1;
                    xSObject = traverseSyntheticAnnotation(element, syntheticAnnotation, checkAttributes, false, xSDocumentInfo);
                    firstChildElement = firstChildElement;
                } else {
                    i = 1;
                    xSObject = null;
                }
            } else {
                XSObject traverseAnnotationDecl = traverseAnnotationDecl(firstChildElement, checkAttributes, false, xSDocumentInfo);
                firstChildElement = DOMUtil.getNextSiblingElement(firstChildElement);
                xSObject = traverseAnnotationDecl;
                i = 1;
            }
            if (firstChildElement != null) {
                Object[] objArr = new Object[i];
                objArr[0] = qName.rawname;
                reportSchemaError("src-attribute.3.2", objArr, firstChildElement);
            }
            obj = qName.localpart;
        } else {
            i = 1;
            obj = obj2;
            xSObject = null;
            xSAttributeDecl = null;
        }
        if (str2 != null) {
            str = str3;
            str3 = str2;
            i2 = i;
        } else if (str3 != null) {
            i2 = 2;
            str = null;
        } else {
            str = str3;
            str3 = str2;
            i2 = 0;
        }
        if (xSAttributeDecl != null) {
            if (this.fSchemaHandler.fDeclPool != null) {
                xSAttributeUseImpl = this.fSchemaHandler.fDeclPool.getAttributeUse();
            } else {
                xSAttributeUseImpl = new XSAttributeUseImpl();
            }
            xSAttributeUseImpl.fAttrDecl = xSAttributeDecl;
            xSAttributeUseImpl.fUse = xInt.shortValue();
            short s3 = i2 == 1 ? (short) 1 : 0;
            short s4 = i2 == 1 ? (short) 1 : 0;
            short s5 = i2 == 1 ? (short) 1 : 0;
            short s6 = i2 == 1 ? (short) 1 : 0;
            xSAttributeUseImpl.fConstraintType = s3;
            if (str3 != null) {
                xSAttributeUseImpl.fDefault = new ValidatedInfo();
                xSAttributeUseImpl.fDefault.normalizedValue = str3;
            }
            if (element.getAttributeNode(SchemaSymbols.ATT_REF) == null) {
                xSAttributeUseImpl.fAnnotations = xSAttributeDecl.getAnnotations();
            } else {
                if (xSObject != null) {
                    xSObjectListImpl = new XSObjectListImpl();
                    xSObjectListImpl.addXSObject(xSObject);
                } else {
                    xSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
                }
                xSAttributeUseImpl.fAnnotations = xSObjectListImpl;
            }
        } else {
            xSAttributeUseImpl = null;
        }
        if (str3 == null || str == null) {
            s = 0;
        } else {
            Object[] objArr2 = new Object[i];
            s = 0;
            objArr2[0] = obj;
            reportSchemaError("src-attribute.1", objArr2, element);
        }
        if (!(i2 != i || xInt == null || xInt.intValue() == 0)) {
            Object[] objArr3 = new Object[i];
            objArr3[s] = obj;
            reportSchemaError("src-attribute.2", objArr3, element);
            xSAttributeUseImpl.fUse = s;
        }
        if (!(str3 == null || xSAttributeUseImpl == null)) {
            this.fValidationState.setNamespaceSupport(xSDocumentInfo.fNamespaceSupport);
            try {
                checkDefaultValid(xSAttributeUseImpl);
                s2 = 0;
                validatedInfo = null;
            } catch (InvalidDatatypeValueException e) {
                reportSchemaError(e.getKey(), e.getArgs(), element);
                Object[] objArr4 = new Object[2];
                s2 = 0;
                objArr4[0] = obj;
                objArr4[i] = str3;
                reportSchemaError("a-props-correct.2", objArr4, element);
                validatedInfo = null;
                xSAttributeUseImpl.fDefault = null;
                xSAttributeUseImpl.fConstraintType = 0;
            }
            if (((XSSimpleType) xSAttributeDecl.getTypeDefinition()).isIDType()) {
                Object[] objArr5 = new Object[i];
                objArr5[s2] = obj;
                reportSchemaError("a-props-correct.3", objArr5, element);
                xSAttributeUseImpl.fDefault = validatedInfo;
                xSAttributeUseImpl.fConstraintType = s2;
            }
            if (xSAttributeUseImpl.fAttrDecl.getConstraintType() == 2 && xSAttributeUseImpl.fConstraintType != 0 && (xSAttributeUseImpl.fConstraintType != 2 || !xSAttributeUseImpl.fAttrDecl.getValInfo().actualValue.equals(xSAttributeUseImpl.fDefault.actualValue))) {
                Object[] objArr6 = new Object[2];
                objArr6[0] = obj;
                objArr6[i] = xSAttributeUseImpl.fAttrDecl.getValInfo().stringValue();
                reportSchemaError("au-props-correct.2", objArr6, element);
                xSAttributeUseImpl.fDefault = xSAttributeUseImpl.fAttrDecl.getValInfo();
                xSAttributeUseImpl.fConstraintType = 2;
            }
        }
        this.fAttrChecker.returnAttrArray(checkAttributes, xSDocumentInfo);
        return xSAttributeUseImpl;
    }

    /* access modifiers changed from: protected */
    public XSAttributeDecl traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] checkAttributes = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        XSAttributeDecl traverseNamedAttr = traverseNamedAttr(element, checkAttributes, xSDocumentInfo, schemaGrammar, true, null);
        this.fAttrChecker.returnAttrArray(checkAttributes, xSDocumentInfo);
        return traverseNamedAttr;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r28v0, resolved type: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAttributeTraverser */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v6, types: [ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl, java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0133  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x013c  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0148  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0163  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0180  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0185  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01bf  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x022b  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0232  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl traverseNamedAttr(ohos.org.w3c.dom.Element r29, java.lang.Object[] r30, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r31, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar r32, boolean r33, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl r34) {
        /*
        // Method dump skipped, instructions count: 641
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAttributeTraverser.traverseNamedAttr(ohos.org.w3c.dom.Element, java.lang.Object[], ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar, boolean, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl):ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl");
    }

    /* access modifiers changed from: package-private */
    public void checkDefaultValid(XSAttributeDecl xSAttributeDecl) throws InvalidDatatypeValueException {
        ((XSSimpleType) xSAttributeDecl.getTypeDefinition()).validate(xSAttributeDecl.getValInfo().normalizedValue, (ValidationContext) this.fValidationState, xSAttributeDecl.getValInfo());
        ((XSSimpleType) xSAttributeDecl.getTypeDefinition()).validate(xSAttributeDecl.getValInfo().stringValue(), (ValidationContext) this.fValidationState, xSAttributeDecl.getValInfo());
    }

    /* access modifiers changed from: package-private */
    public void checkDefaultValid(XSAttributeUseImpl xSAttributeUseImpl) throws InvalidDatatypeValueException {
        ((XSSimpleType) xSAttributeUseImpl.fAttrDecl.getTypeDefinition()).validate(xSAttributeUseImpl.fDefault.normalizedValue, (ValidationContext) this.fValidationState, xSAttributeUseImpl.fDefault);
        ((XSSimpleType) xSAttributeUseImpl.fAttrDecl.getTypeDefinition()).validate(xSAttributeUseImpl.fDefault.stringValue(), (ValidationContext) this.fValidationState, xSAttributeUseImpl.fDefault);
    }
}
