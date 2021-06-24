package ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers;

import ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xerces.internal.util.DOMUtil;
import ohos.com.sun.org.apache.xerces.internal.xni.QName;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList;
import ohos.com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import ohos.org.w3c.dom.Element;

/* access modifiers changed from: package-private */
public class XSDSimpleTypeTraverser extends XSDAbstractTraverser {
    private boolean fIsBuiltIn = false;

    XSDSimpleTypeTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        super(xSDHandler, xSAttributeChecker);
    }

    /* access modifiers changed from: package-private */
    public XSSimpleType traverseGlobal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] checkAttributes = this.fAttrChecker.checkAttributes(element, true, xSDocumentInfo);
        String str = (String) checkAttributes[XSAttributeChecker.ATTIDX_NAME];
        if (str == null) {
            checkAttributes[XSAttributeChecker.ATTIDX_NAME] = "(no name)";
        }
        XSSimpleType traverseSimpleTypeDecl = traverseSimpleTypeDecl(element, checkAttributes, xSDocumentInfo, schemaGrammar);
        this.fAttrChecker.returnAttrArray(checkAttributes, xSDocumentInfo);
        if (str == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, SchemaSymbols.ATT_NAME}, element);
            traverseSimpleTypeDecl = null;
        }
        if (traverseSimpleTypeDecl != null) {
            if (schemaGrammar.getGlobalTypeDecl(traverseSimpleTypeDecl.getName()) == null) {
                schemaGrammar.addGlobalSimpleTypeDecl(traverseSimpleTypeDecl);
            }
            String schemaDocument2SystemId = this.fSchemaHandler.schemaDocument2SystemId(xSDocumentInfo);
            XSTypeDefinition globalTypeDecl = schemaGrammar.getGlobalTypeDecl(traverseSimpleTypeDecl.getName(), schemaDocument2SystemId);
            if (globalTypeDecl == null) {
                schemaGrammar.addGlobalSimpleTypeDecl(traverseSimpleTypeDecl, schemaDocument2SystemId);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (globalTypeDecl != null && (globalTypeDecl instanceof XSSimpleType)) {
                    traverseSimpleTypeDecl = (XSSimpleType) globalTypeDecl;
                }
                this.fSchemaHandler.addGlobalTypeDecl(traverseSimpleTypeDecl);
            }
        }
        return traverseSimpleTypeDecl;
    }

    /* access modifiers changed from: package-private */
    public XSSimpleType traverseLocal(Element element, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        Object[] checkAttributes = this.fAttrChecker.checkAttributes(element, false, xSDocumentInfo);
        XSSimpleType simpleType = getSimpleType(genAnonTypeName(element), element, checkAttributes, xSDocumentInfo, schemaGrammar);
        if (simpleType instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl) simpleType).setAnonymous(true);
        }
        this.fAttrChecker.returnAttrArray(checkAttributes, xSDocumentInfo);
        return simpleType;
    }

    private XSSimpleType traverseSimpleTypeDecl(Element element, Object[] objArr, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar) {
        return getSimpleType((String) objArr[XSAttributeChecker.ATTIDX_NAME], element, objArr, xSDocumentInfo, schemaGrammar);
    }

    private String genAnonTypeName(Element element) {
        StringBuffer stringBuffer = new StringBuffer("#AnonType_");
        Element parent = DOMUtil.getParent(element);
        while (parent != null && parent != DOMUtil.getRoot(DOMUtil.getDocument(parent))) {
            stringBuffer.append(parent.getAttribute(SchemaSymbols.ATT_NAME));
            parent = DOMUtil.getParent(parent);
        }
        return stringBuffer.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:127:0x0266  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x026a  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x028a A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x029a  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0078  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType getSimpleType(java.lang.String r28, ohos.org.w3c.dom.Element r29, java.lang.Object[] r30, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r31, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar r32) {
        /*
        // Method dump skipped, instructions count: 1027
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDSimpleTypeTraverser.getSimpleType(java.lang.String, ohos.org.w3c.dom.Element, java.lang.Object[], ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo, ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar):ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType");
    }

    private XSSimpleType findDTValidator(Element element, String str, QName qName, short s, XSDocumentInfo xSDocumentInfo) {
        XSTypeDefinition xSTypeDefinition;
        if (qName == null || (xSTypeDefinition = (XSTypeDefinition) this.fSchemaHandler.getGlobalDecl(xSDocumentInfo, 7, qName, element)) == null) {
            return null;
        }
        if (xSTypeDefinition.getTypeCategory() != 16) {
            reportSchemaError("cos-st-restricts.1.1", new Object[]{qName.rawname, str}, element);
            return null;
        } else if (xSTypeDefinition == SchemaGrammar.fAnySimpleType && s == 2) {
            if (checkBuiltIn(str, xSDocumentInfo.fTargetNamespace)) {
                return null;
            }
            reportSchemaError("cos-st-restricts.1.1", new Object[]{qName.rawname, str}, element);
            return null;
        } else if ((xSTypeDefinition.getFinal() & s) == 0) {
            return (XSSimpleType) xSTypeDefinition;
        } else {
            if (s == 2) {
                reportSchemaError("st-props-correct.3", new Object[]{str, qName.rawname}, element);
            } else if (s == 16) {
                reportSchemaError("cos-st-restricts.2.3.1.1", new Object[]{qName.rawname, str}, element);
            } else if (s == 8) {
                reportSchemaError("cos-st-restricts.3.3.1.1", new Object[]{qName.rawname, str}, element);
            }
            return null;
        }
    }

    private final boolean checkBuiltIn(String str, String str2) {
        if (str2 != SchemaSymbols.URI_SCHEMAFORSCHEMA) {
            return false;
        }
        if (SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(str) != null) {
            this.fIsBuiltIn = true;
        }
        return this.fIsBuiltIn;
    }

    private boolean isListDatatype(XSSimpleType xSSimpleType) {
        if (xSSimpleType.getVariety() == 2) {
            return true;
        }
        if (xSSimpleType.getVariety() == 3) {
            XSObjectList memberTypes = xSSimpleType.getMemberTypes();
            for (int i = 0; i < memberTypes.getLength(); i++) {
                if (((XSSimpleType) memberTypes.item(i)).getVariety() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private XSSimpleType errorType(String str, String str2, short s) {
        XSSimpleType xSSimpleType = (XSSimpleType) SchemaGrammar.SG_SchemaNS.getTypeDefinition("string");
        if (s == 2) {
            return this.fSchemaHandler.fDVFactory.createTypeRestriction(str, str2, 0, xSSimpleType, null);
        }
        if (s == 8) {
            return this.fSchemaHandler.fDVFactory.createTypeUnion(str, str2, 0, new XSSimpleType[]{xSSimpleType}, null);
        } else if (s != 16) {
            return null;
        } else {
            return this.fSchemaHandler.fDVFactory.createTypeList(str, str2, 0, xSSimpleType, null);
        }
    }
}
