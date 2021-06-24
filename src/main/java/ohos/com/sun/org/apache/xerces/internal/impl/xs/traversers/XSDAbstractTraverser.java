package ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers;

import java.util.Locale;
import java.util.Vector;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import ohos.com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
import ohos.com.sun.org.apache.xerces.internal.util.DOMUtil;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolTable;
import ohos.com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList;
import ohos.com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import ohos.com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import ohos.org.w3c.dom.Element;

/* access modifiers changed from: package-private */
public abstract class XSDAbstractTraverser {
    protected static final int CHILD_OF_GROUP = 4;
    protected static final int GROUP_REF_WITH_ALL = 2;
    protected static final int NOT_ALL_CONTEXT = 0;
    protected static final String NO_NAME = "(no name)";
    protected static final int PROCESSING_ALL_EL = 1;
    protected static final int PROCESSING_ALL_GP = 8;
    private static final XSSimpleType fQNameDV = ((XSSimpleType) SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(SchemaSymbols.ATTVAL_QNAME));
    protected XSAttributeChecker fAttrChecker = null;
    private StringBuffer fPattern = new StringBuffer();
    protected XSDHandler fSchemaHandler = null;
    protected SymbolTable fSymbolTable = null;
    protected boolean fValidateAnnotations = false;
    ValidationState fValidationState = new ValidationState();
    private final XSFacets xsFacets = new XSFacets();

    XSDAbstractTraverser(XSDHandler xSDHandler, XSAttributeChecker xSAttributeChecker) {
        this.fSchemaHandler = xSDHandler;
        this.fAttrChecker = xSAttributeChecker;
    }

    /* access modifiers changed from: package-private */
    public void reset(SymbolTable symbolTable, boolean z, Locale locale) {
        this.fSymbolTable = symbolTable;
        this.fValidateAnnotations = z;
        this.fValidationState.setExtraChecking(false);
        this.fValidationState.setSymbolTable(symbolTable);
        this.fValidationState.setLocale(locale);
    }

    /* access modifiers changed from: package-private */
    public XSAnnotationImpl traverseAnnotationDecl(Element element, Object[] objArr, boolean z, XSDocumentInfo xSDocumentInfo) {
        String str;
        String str2;
        this.fAttrChecker.returnAttrArray(this.fAttrChecker.checkAttributes(element, z, xSDocumentInfo), xSDocumentInfo);
        String annotation = DOMUtil.getAnnotation(element);
        Element firstChildElement = DOMUtil.getFirstChildElement(element);
        if (firstChildElement != null) {
            do {
                String localName = DOMUtil.getLocalName(firstChildElement);
                if (localName.equals(SchemaSymbols.ELT_APPINFO) || localName.equals(SchemaSymbols.ELT_DOCUMENTATION)) {
                    this.fAttrChecker.returnAttrArray(this.fAttrChecker.checkAttributes(firstChildElement, true, xSDocumentInfo), xSDocumentInfo);
                } else {
                    reportSchemaError("src-annotation", new Object[]{localName}, firstChildElement);
                }
                firstChildElement = DOMUtil.getNextSiblingElement(firstChildElement);
            } while (firstChildElement != null);
        }
        if (annotation == null) {
            return null;
        }
        SchemaGrammar grammar = this.fSchemaHandler.getGrammar(xSDocumentInfo.fTargetNamespace);
        Vector vector = (Vector) objArr[XSAttributeChecker.ATTIDX_NONSCHEMA];
        if (vector == null || vector.isEmpty()) {
            if (this.fValidateAnnotations) {
                xSDocumentInfo.addAnnotation(new XSAnnotationInfo(annotation, element));
            }
            return new XSAnnotationImpl(annotation, grammar);
        }
        StringBuffer stringBuffer = new StringBuffer(64);
        stringBuffer.append(" ");
        int i = 0;
        while (i < vector.size()) {
            int i2 = i + 1;
            String str3 = (String) vector.elementAt(i);
            int indexOf = str3.indexOf(58);
            if (indexOf == -1) {
                str2 = "";
                str = str3;
            } else {
                str2 = str3.substring(0, indexOf);
                str = str3.substring(indexOf + 1);
            }
            if (element.getAttributeNS(xSDocumentInfo.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(str2)), str).length() != 0) {
                i = i2 + 1;
            } else {
                stringBuffer.append(str3);
                stringBuffer.append("=\"");
                i = i2 + 1;
                stringBuffer.append(processAttValue((String) vector.elementAt(i2)));
                stringBuffer.append("\" ");
            }
        }
        StringBuffer stringBuffer2 = new StringBuffer(annotation.length() + stringBuffer.length());
        int indexOf2 = annotation.indexOf(SchemaSymbols.ELT_ANNOTATION);
        if (indexOf2 == -1) {
            return null;
        }
        int length = indexOf2 + SchemaSymbols.ELT_ANNOTATION.length();
        stringBuffer2.append(annotation.substring(0, length));
        stringBuffer2.append(stringBuffer.toString());
        stringBuffer2.append(annotation.substring(length, annotation.length()));
        String stringBuffer3 = stringBuffer2.toString();
        if (this.fValidateAnnotations) {
            xSDocumentInfo.addAnnotation(new XSAnnotationInfo(stringBuffer3, element));
        }
        return new XSAnnotationImpl(stringBuffer3, grammar);
    }

    /* access modifiers changed from: package-private */
    public XSAnnotationImpl traverseSyntheticAnnotation(Element element, String str, Object[] objArr, boolean z, XSDocumentInfo xSDocumentInfo) {
        String str2;
        SchemaGrammar grammar = this.fSchemaHandler.getGrammar(xSDocumentInfo.fTargetNamespace);
        Vector vector = (Vector) objArr[XSAttributeChecker.ATTIDX_NONSCHEMA];
        if (vector == null || vector.isEmpty()) {
            if (this.fValidateAnnotations) {
                xSDocumentInfo.addAnnotation(new XSAnnotationInfo(str, element));
            }
            return new XSAnnotationImpl(str, grammar);
        }
        StringBuffer stringBuffer = new StringBuffer(64);
        stringBuffer.append(" ");
        int i = 0;
        while (i < vector.size()) {
            int i2 = i + 1;
            String str3 = (String) vector.elementAt(i);
            int indexOf = str3.indexOf(58);
            if (indexOf == -1) {
                str2 = "";
            } else {
                str2 = str3.substring(0, indexOf);
                str3.substring(indexOf + 1);
            }
            xSDocumentInfo.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(str2));
            stringBuffer.append(str3);
            stringBuffer.append("=\"");
            i = i2 + 1;
            stringBuffer.append(processAttValue((String) vector.elementAt(i2)));
            stringBuffer.append("\" ");
        }
        StringBuffer stringBuffer2 = new StringBuffer(str.length() + stringBuffer.length());
        int indexOf2 = str.indexOf(SchemaSymbols.ELT_ANNOTATION);
        if (indexOf2 == -1) {
            return null;
        }
        int length = indexOf2 + SchemaSymbols.ELT_ANNOTATION.length();
        stringBuffer2.append(str.substring(0, length));
        stringBuffer2.append(stringBuffer.toString());
        stringBuffer2.append(str.substring(length, str.length()));
        String stringBuffer3 = stringBuffer2.toString();
        if (this.fValidateAnnotations) {
            xSDocumentInfo.addAnnotation(new XSAnnotationInfo(stringBuffer3, element));
        }
        return new XSAnnotationImpl(stringBuffer3, grammar);
    }

    static final class FacetInfo {
        final short fFixedFacets;
        final short fPresentFacets;
        final XSFacets facetdata;
        final Element nodeAfterFacets;

        FacetInfo(XSFacets xSFacets, Element element, short s, short s2) {
            this.facetdata = xSFacets;
            this.nodeAfterFacets = element;
            this.fPresentFacets = s;
            this.fFixedFacets = s2;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser.FacetInfo traverseFacets(ohos.org.w3c.dom.Element r28, ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType r29, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r30) {
        /*
        // Method dump skipped, instructions count: 1280
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser.traverseFacets(ohos.org.w3c.dom.Element, ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType, ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo):ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser$FacetInfo");
    }

    private boolean containsQName(XSSimpleType xSSimpleType) {
        if (xSSimpleType.getVariety() == 1) {
            short primitiveKind = xSSimpleType.getPrimitiveKind();
            return primitiveKind == 18 || primitiveKind == 20;
        } else if (xSSimpleType.getVariety() == 2) {
            return containsQName((XSSimpleType) xSSimpleType.getItemType());
        } else {
            if (xSSimpleType.getVariety() == 3) {
                XSObjectList memberTypes = xSSimpleType.getMemberTypes();
                for (int i = 0; i < memberTypes.getLength(); i++) {
                    if (containsQName((XSSimpleType) memberTypes.item(i))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public Element traverseAttrsAndAttrGrps(Element element, XSAttributeGroupDecl xSAttributeGroupDecl, XSDocumentInfo xSDocumentInfo, SchemaGrammar schemaGrammar, XSComplexTypeDecl xSComplexTypeDecl) {
        String str;
        String str2;
        String str3;
        int i;
        XSObjectList xSObjectList;
        short s;
        String str4;
        String str5;
        Element element2 = element;
        while (true) {
            str = "src-attribute_group.2";
            str2 = "src-ct.4";
            if (element2 == null) {
                break;
            }
            String localName = DOMUtil.getLocalName(element2);
            String str6 = "ag-props-correct.3";
            String str7 = "ag-props-correct.2";
            short s2 = 2;
            if (!localName.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                if (!localName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                    break;
                }
                XSAttributeGroupDecl traverseLocal = this.fSchemaHandler.fAttributeGroupTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar);
                if (traverseLocal != null) {
                    XSObjectList attributeUses = traverseLocal.getAttributeUses();
                    int length = attributeUses.getLength();
                    int i2 = 0;
                    while (i2 < length) {
                        XSAttributeUseImpl xSAttributeUseImpl = (XSAttributeUseImpl) attributeUses.item(i2);
                        if (xSAttributeUseImpl.fUse == s2) {
                            xSAttributeGroupDecl.addAttributeUse(xSAttributeUseImpl);
                            i = length;
                            xSObjectList = attributeUses;
                            str3 = str6;
                            s = s2;
                        } else {
                            XSAttributeUse attributeUseNoProhibited = xSAttributeGroupDecl.getAttributeUseNoProhibited(xSAttributeUseImpl.fAttrDecl.getNamespace(), xSAttributeUseImpl.fAttrDecl.getName());
                            if (attributeUseNoProhibited == null) {
                                String addAttributeUse = xSAttributeGroupDecl.addAttributeUse(xSAttributeUseImpl);
                                if (addAttributeUse != null) {
                                    if (xSComplexTypeDecl == null) {
                                        str5 = str6;
                                    } else {
                                        str5 = "ct-props-correct.5";
                                    }
                                    i = length;
                                    xSObjectList = attributeUses;
                                    str3 = str6;
                                    reportSchemaError(str5, new Object[]{xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName(), xSAttributeUseImpl.fAttrDecl.getName(), addAttributeUse}, element2);
                                } else {
                                    i = length;
                                    xSObjectList = attributeUses;
                                    str3 = str6;
                                }
                            } else {
                                i = length;
                                xSObjectList = attributeUses;
                                str3 = str6;
                                if (xSAttributeUseImpl != attributeUseNoProhibited) {
                                    if (xSComplexTypeDecl == null) {
                                        str4 = str7;
                                    } else {
                                        str4 = "ct-props-correct.4";
                                    }
                                    s = 2;
                                    reportSchemaError(str4, new Object[]{xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName(), xSAttributeUseImpl.fAttrDecl.getName()}, element2);
                                }
                            }
                            s = 2;
                        }
                        i2++;
                        s2 = s;
                        attributeUses = xSObjectList;
                        str2 = str2;
                        str = str;
                        length = i;
                        str6 = str3;
                    }
                    if (traverseLocal.fAttributeWC != null) {
                        if (xSAttributeGroupDecl.fAttributeWC == null) {
                            xSAttributeGroupDecl.fAttributeWC = traverseLocal.fAttributeWC;
                        } else {
                            xSAttributeGroupDecl.fAttributeWC = xSAttributeGroupDecl.fAttributeWC.performIntersectionWith(traverseLocal.fAttributeWC, xSAttributeGroupDecl.fAttributeWC.fProcessContents);
                            if (xSAttributeGroupDecl.fAttributeWC == null) {
                                reportSchemaError(xSComplexTypeDecl == null ? str : str2, new Object[]{xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName()}, element2);
                            }
                        }
                    }
                }
            } else {
                XSAttributeUseImpl traverseLocal2 = this.fSchemaHandler.fAttributeTraverser.traverseLocal(element2, xSDocumentInfo, schemaGrammar, xSComplexTypeDecl);
                if (traverseLocal2 != null) {
                    if (traverseLocal2.fUse == 2) {
                        xSAttributeGroupDecl.addAttributeUse(traverseLocal2);
                    } else {
                        XSAttributeUse attributeUseNoProhibited2 = xSAttributeGroupDecl.getAttributeUseNoProhibited(traverseLocal2.fAttrDecl.getNamespace(), traverseLocal2.fAttrDecl.getName());
                        if (attributeUseNoProhibited2 == null) {
                            String addAttributeUse2 = xSAttributeGroupDecl.addAttributeUse(traverseLocal2);
                            if (addAttributeUse2 != null) {
                                if (xSComplexTypeDecl != null) {
                                    str6 = "ct-props-correct.5";
                                }
                                reportSchemaError(str6, new Object[]{xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName(), traverseLocal2.fAttrDecl.getName(), addAttributeUse2}, element2);
                            }
                        } else if (attributeUseNoProhibited2 != traverseLocal2) {
                            if (xSComplexTypeDecl != null) {
                                str7 = "ct-props-correct.4";
                            }
                            reportSchemaError(str7, new Object[]{xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName(), traverseLocal2.fAttrDecl.getName()}, element2);
                        }
                    }
                }
            }
            element2 = DOMUtil.getNextSiblingElement(element2);
        }
        if (element2 == null || !DOMUtil.getLocalName(element2).equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
            return element2;
        }
        XSWildcardDecl traverseAnyAttribute = this.fSchemaHandler.fWildCardTraverser.traverseAnyAttribute(element2, xSDocumentInfo, schemaGrammar);
        if (xSAttributeGroupDecl.fAttributeWC == null) {
            xSAttributeGroupDecl.fAttributeWC = traverseAnyAttribute;
        } else {
            xSAttributeGroupDecl.fAttributeWC = traverseAnyAttribute.performIntersectionWith(xSAttributeGroupDecl.fAttributeWC, traverseAnyAttribute.fProcessContents);
            if (xSAttributeGroupDecl.fAttributeWC == null) {
                reportSchemaError(xSComplexTypeDecl == null ? str : str2, new Object[]{xSComplexTypeDecl == null ? xSAttributeGroupDecl.fName : xSComplexTypeDecl.getName()}, element2);
            }
        }
        return DOMUtil.getNextSiblingElement(element2);
    }

    /* access modifiers changed from: package-private */
    public void reportSchemaError(String str, Object[] objArr, Element element) {
        this.fSchemaHandler.reportSchemaError(str, objArr, element);
    }

    /* access modifiers changed from: package-private */
    public void checkNotationType(String str, XSTypeDefinition xSTypeDefinition, Element element) {
        if (xSTypeDefinition.getTypeCategory() == 16) {
            XSSimpleType xSSimpleType = (XSSimpleType) xSTypeDefinition;
            if (xSSimpleType.getVariety() == 1 && xSSimpleType.getPrimitiveKind() == 20 && (xSSimpleType.getDefinedFacets() & 2048) == 0) {
                reportSchemaError("enumeration-required-notation", new Object[]{xSTypeDefinition.getName(), str, DOMUtil.getLocalName(element)}, element);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0090, code lost:
        if (r3 > 1) goto L_0x0092;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x009f, code lost:
        if (r3 > 1) goto L_0x0092;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl checkOccurrences(ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl r15, java.lang.String r16, ohos.org.w3c.dom.Element r17, int r18, long r19) {
        /*
        // Method dump skipped, instructions count: 168
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser.checkOccurrences(ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl, java.lang.String, ohos.org.w3c.dom.Element, int, long):ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl");
    }

    private static String processAttValue(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt == '\"' || charAt == '<' || charAt == '&' || charAt == '\t' || charAt == '\n' || charAt == '\r') {
                return escapeAttValue(str, i);
            }
        }
        return str;
    }

    private static String escapeAttValue(String str, int i) {
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length);
        stringBuffer.append(str.substring(0, i));
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt == '\"') {
                stringBuffer.append(SerializerConstants.ENTITY_QUOT);
            } else if (charAt == '<') {
                stringBuffer.append(SerializerConstants.ENTITY_LT);
            } else if (charAt == '&') {
                stringBuffer.append(SerializerConstants.ENTITY_AMP);
            } else if (charAt == '\t') {
                stringBuffer.append("&#x9;");
            } else if (charAt == '\n') {
                stringBuffer.append(SerializerConstants.ENTITY_CRLF);
            } else if (charAt == '\r') {
                stringBuffer.append("&#xD;");
            } else {
                stringBuffer.append(charAt);
            }
            i++;
        }
        return stringBuffer.toString();
    }
}
