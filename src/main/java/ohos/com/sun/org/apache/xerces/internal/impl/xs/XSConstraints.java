package ohos.com.sun.org.apache.xerces.internal.impl.xs;

import java.util.Comparator;
import java.util.Vector;
import ohos.com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolHash;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLLocator;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList;
import ohos.com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

public class XSConstraints {
    private static final Comparator ELEMENT_PARTICLE_COMPARATOR = new Comparator() {
        /* class ohos.com.sun.org.apache.xerces.internal.impl.xs.XSConstraints.AnonymousClass1 */

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            XSElementDecl xSElementDecl = (XSElementDecl) ((XSParticleDecl) obj).fValue;
            XSElementDecl xSElementDecl2 = (XSElementDecl) ((XSParticleDecl) obj2).fValue;
            String namespace = xSElementDecl.getNamespace();
            String namespace2 = xSElementDecl2.getNamespace();
            String name = xSElementDecl.getName();
            String name2 = xSElementDecl2.getName();
            int i = 1;
            if (namespace == namespace2) {
                i = 0;
            } else if (namespace == null) {
                i = -1;
            } else if (namespace2 != null) {
                i = namespace.compareTo(namespace2);
            }
            return i != 0 ? i : name.compareTo(name2);
        }
    };
    static final int OCCURRENCE_UNKNOWN = -2;
    static final XSSimpleType STRING_TYPE = ((XSSimpleType) SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("string"));
    private static XSParticleDecl fEmptyParticle = null;

    private static void checkIDConstraintRestriction(XSElementDecl xSElementDecl, XSElementDecl xSElementDecl2) throws XMLSchemaException {
    }

    private static boolean checkOccurrenceRange(int i, int i2, int i3, int i4) {
        if (i < i3) {
            return false;
        }
        if (i4 != -1) {
            return i2 != -1 && i2 <= i4;
        }
        return true;
    }

    public static XSParticleDecl getEmptySequence() {
        if (fEmptyParticle == null) {
            XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
            xSModelGroupImpl.fCompositor = 102;
            xSModelGroupImpl.fParticleCount = 0;
            xSModelGroupImpl.fParticles = null;
            xSModelGroupImpl.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            XSParticleDecl xSParticleDecl = new XSParticleDecl();
            xSParticleDecl.fType = 3;
            xSParticleDecl.fValue = xSModelGroupImpl;
            xSParticleDecl.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            fEmptyParticle = xSParticleDecl;
        }
        return fEmptyParticle;
    }

    public static boolean checkTypeDerivationOk(XSTypeDefinition xSTypeDefinition, XSTypeDefinition xSTypeDefinition2, short s) {
        if (xSTypeDefinition == SchemaGrammar.fAnyType) {
            return xSTypeDefinition == xSTypeDefinition2;
        }
        if (xSTypeDefinition == SchemaGrammar.fAnySimpleType) {
            return xSTypeDefinition2 == SchemaGrammar.fAnyType || xSTypeDefinition2 == SchemaGrammar.fAnySimpleType;
        }
        if (xSTypeDefinition.getTypeCategory() != 16) {
            return checkComplexDerivation((XSComplexTypeDecl) xSTypeDefinition, xSTypeDefinition2, s);
        }
        if (xSTypeDefinition2.getTypeCategory() == 15) {
            if (xSTypeDefinition2 != SchemaGrammar.fAnyType) {
                return false;
            }
            xSTypeDefinition2 = SchemaGrammar.fAnySimpleType;
        }
        return checkSimpleDerivation((XSSimpleType) xSTypeDefinition, (XSSimpleType) xSTypeDefinition2, s);
    }

    public static boolean checkSimpleDerivationOk(XSSimpleType xSSimpleType, XSTypeDefinition xSTypeDefinition, short s) {
        if (xSSimpleType != SchemaGrammar.fAnySimpleType) {
            if (xSTypeDefinition.getTypeCategory() == 15) {
                if (xSTypeDefinition != SchemaGrammar.fAnyType) {
                    return false;
                }
                xSTypeDefinition = SchemaGrammar.fAnySimpleType;
            }
            return checkSimpleDerivation(xSSimpleType, (XSSimpleType) xSTypeDefinition, s);
        } else if (xSTypeDefinition == SchemaGrammar.fAnyType || xSTypeDefinition == SchemaGrammar.fAnySimpleType) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkComplexDerivationOk(XSComplexTypeDecl xSComplexTypeDecl, XSTypeDefinition xSTypeDefinition, short s) {
        if (xSComplexTypeDecl == SchemaGrammar.fAnyType) {
            return xSComplexTypeDecl == xSTypeDefinition;
        }
        return checkComplexDerivation(xSComplexTypeDecl, xSTypeDefinition, s);
    }

    private static boolean checkSimpleDerivation(XSSimpleType xSSimpleType, XSSimpleType xSSimpleType2, short s) {
        if (xSSimpleType == xSSimpleType2) {
            return true;
        }
        if ((s & 2) == 0 && (xSSimpleType.getBaseType().getFinal() & 2) == 0) {
            XSSimpleType xSSimpleType3 = (XSSimpleType) xSSimpleType.getBaseType();
            if (xSSimpleType3 == xSSimpleType2) {
                return true;
            }
            if (xSSimpleType3 != SchemaGrammar.fAnySimpleType && checkSimpleDerivation(xSSimpleType3, xSSimpleType2, s)) {
                return true;
            }
            if ((xSSimpleType.getVariety() == 2 || xSSimpleType.getVariety() == 3) && xSSimpleType2 == SchemaGrammar.fAnySimpleType) {
                return true;
            }
            if (xSSimpleType2.getVariety() == 3) {
                XSObjectList memberTypes = xSSimpleType2.getMemberTypes();
                int length = memberTypes.getLength();
                for (int i = 0; i < length; i++) {
                    if (checkSimpleDerivation(xSSimpleType, (XSSimpleType) memberTypes.item(i), s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkComplexDerivation(XSComplexTypeDecl xSComplexTypeDecl, XSTypeDefinition xSTypeDefinition, short s) {
        if (xSComplexTypeDecl == xSTypeDefinition) {
            return true;
        }
        if ((xSComplexTypeDecl.fDerivedBy & s) != 0) {
            return false;
        }
        XSTypeDefinition xSTypeDefinition2 = xSComplexTypeDecl.fBaseType;
        if (xSTypeDefinition2 == xSTypeDefinition) {
            return true;
        }
        if (!(xSTypeDefinition2 == SchemaGrammar.fAnyType || xSTypeDefinition2 == SchemaGrammar.fAnySimpleType)) {
            if (xSTypeDefinition2.getTypeCategory() == 15) {
                return checkComplexDerivation((XSComplexTypeDecl) xSTypeDefinition2, xSTypeDefinition, s);
            }
            if (xSTypeDefinition2.getTypeCategory() == 16) {
                if (xSTypeDefinition.getTypeCategory() == 15) {
                    if (xSTypeDefinition != SchemaGrammar.fAnyType) {
                        return false;
                    }
                    xSTypeDefinition = SchemaGrammar.fAnySimpleType;
                }
                return checkSimpleDerivation((XSSimpleType) xSTypeDefinition2, (XSSimpleType) xSTypeDefinition, s);
            }
        }
        return false;
    }

    public static Object ElementDefaultValidImmediate(XSTypeDefinition xSTypeDefinition, String str, ValidationContext validationContext, ValidatedInfo validatedInfo) {
        XSSimpleType xSSimpleType;
        if (xSTypeDefinition.getTypeCategory() == 16) {
            xSSimpleType = (XSSimpleType) xSTypeDefinition;
        } else {
            XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl) xSTypeDefinition;
            if (xSComplexTypeDecl.fContentType == 1) {
                xSSimpleType = xSComplexTypeDecl.fXSSimpleType;
            } else if (xSComplexTypeDecl.fContentType != 3 || !((XSParticleDecl) xSComplexTypeDecl.getParticle()).emptiable()) {
                return null;
            } else {
                xSSimpleType = null;
            }
        }
        if (xSSimpleType == null) {
            xSSimpleType = STRING_TYPE;
        }
        try {
            return validatedInfo != null ? xSSimpleType.validate(validatedInfo.stringValue(), validationContext, validatedInfo) : xSSimpleType.validate(str, validationContext, validatedInfo);
        } catch (InvalidDatatypeValueException unused) {
            return null;
        }
    }

    static void reportSchemaError(XMLErrorReporter xMLErrorReporter, SimpleLocator simpleLocator, String str, Object[] objArr) {
        if (simpleLocator != null) {
            xMLErrorReporter.reportError((XMLLocator) simpleLocator, XSMessageFormatter.SCHEMA_DOMAIN, str, objArr, (short) 1);
        } else {
            xMLErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, str, objArr, 1);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:61:0x0195 A[SYNTHETIC, Splitter:B:61:0x0195] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void fullSchemaChecking(ohos.com.sun.org.apache.xerces.internal.impl.xs.XSGrammarBucket r18, ohos.com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r19, ohos.com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder r20, ohos.com.sun.org.apache.xerces.internal.impl.XMLErrorReporter r21) {
        /*
        // Method dump skipped, instructions count: 465
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XSConstraints.fullSchemaChecking(ohos.com.sun.org.apache.xerces.internal.impl.xs.XSGrammarBucket, ohos.com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler, ohos.com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder, ohos.com.sun.org.apache.xerces.internal.impl.XMLErrorReporter):void");
    }

    public static void checkElementDeclsConsistent(XSComplexTypeDecl xSComplexTypeDecl, XSParticleDecl xSParticleDecl, SymbolHash symbolHash, SubstitutionGroupHandler substitutionGroupHandler) throws XMLSchemaException {
        short s = xSParticleDecl.fType;
        if (s != 2) {
            int i = 0;
            if (s == 1) {
                XSElementDecl xSElementDecl = (XSElementDecl) xSParticleDecl.fValue;
                findElemInTable(xSComplexTypeDecl, xSElementDecl, symbolHash);
                if (xSElementDecl.fScope == 1) {
                    XSElementDecl[] substitutionGroup = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl);
                    while (i < substitutionGroup.length) {
                        findElemInTable(xSComplexTypeDecl, substitutionGroup[i], symbolHash);
                        i++;
                    }
                    return;
                }
                return;
            }
            XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl) xSParticleDecl.fValue;
            while (i < xSModelGroupImpl.fParticleCount) {
                checkElementDeclsConsistent(xSComplexTypeDecl, xSModelGroupImpl.fParticles[i], symbolHash, substitutionGroupHandler);
                i++;
            }
        }
    }

    public static void findElemInTable(XSComplexTypeDecl xSComplexTypeDecl, XSElementDecl xSElementDecl, SymbolHash symbolHash) throws XMLSchemaException {
        String str = xSElementDecl.fName + "," + xSElementDecl.fTargetNamespace;
        XSElementDecl xSElementDecl2 = (XSElementDecl) symbolHash.get(str);
        if (xSElementDecl2 == null) {
            symbolHash.put(str, xSElementDecl);
        } else if (xSElementDecl != xSElementDecl2 && xSElementDecl.fType != xSElementDecl2.fType) {
            throw new XMLSchemaException("cos-element-consistent", new Object[]{xSComplexTypeDecl.fName, xSElementDecl.fName});
        }
    }

    private static boolean particleValidRestriction(XSParticleDecl xSParticleDecl, SubstitutionGroupHandler substitutionGroupHandler, XSParticleDecl xSParticleDecl2, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        return particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler2, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:138:0x024e  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0108  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean particleValidRestriction(ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl r25, ohos.com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r26, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl r27, ohos.com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r28, boolean r29) throws ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException {
        /*
        // Method dump skipped, instructions count: 746
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XSConstraints.particleValidRestriction(ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl, ohos.com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl, ohos.com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler, boolean):boolean");
    }

    private static void addElementToParticleVector(Vector vector, XSElementDecl xSElementDecl) {
        XSParticleDecl xSParticleDecl = new XSParticleDecl();
        xSParticleDecl.fValue = xSElementDecl;
        xSParticleDecl.fType = 1;
        vector.addElement(xSParticleDecl);
    }

    private static XSParticleDecl getNonUnaryGroup(XSParticleDecl xSParticleDecl) {
        return (xSParticleDecl.fType == 1 || xSParticleDecl.fType == 2 || xSParticleDecl.fMinOccurs != 1 || xSParticleDecl.fMaxOccurs != 1 || xSParticleDecl.fValue == null || ((XSModelGroupImpl) xSParticleDecl.fValue).fParticleCount != 1) ? xSParticleDecl : getNonUnaryGroup(((XSModelGroupImpl) xSParticleDecl.fValue).fParticles[0]);
    }

    private static Vector removePointlessChildren(XSParticleDecl xSParticleDecl) {
        if (xSParticleDecl.fType == 1 || xSParticleDecl.fType == 2) {
            return null;
        }
        Vector vector = new Vector();
        XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl) xSParticleDecl.fValue;
        for (int i = 0; i < xSModelGroupImpl.fParticleCount; i++) {
            gatherChildren(xSModelGroupImpl.fCompositor, xSModelGroupImpl.fParticles[i], vector);
        }
        return vector;
    }

    private static void gatherChildren(int i, XSParticleDecl xSParticleDecl, Vector vector) {
        int i2 = xSParticleDecl.fMinOccurs;
        int i3 = xSParticleDecl.fMaxOccurs;
        short s = xSParticleDecl.fType;
        if (s == 3) {
            s = ((XSModelGroupImpl) xSParticleDecl.fValue).fCompositor;
        }
        if (s == 1 || s == 2) {
            vector.addElement(xSParticleDecl);
        } else if (i2 != 1 || i3 != 1) {
            vector.addElement(xSParticleDecl);
        } else if (i == s) {
            XSModelGroupImpl xSModelGroupImpl = (XSModelGroupImpl) xSParticleDecl.fValue;
            for (int i4 = 0; i4 < xSModelGroupImpl.fParticleCount; i4++) {
                gatherChildren(s, xSModelGroupImpl.fParticles[i4], vector);
            }
        } else if (!xSParticleDecl.isEmpty()) {
            vector.addElement(xSParticleDecl);
        }
    }

    private static void checkNameAndTypeOK(XSElementDecl xSElementDecl, int i, int i2, XSElementDecl xSElementDecl2, int i3, int i4) throws XMLSchemaException {
        String str;
        if (xSElementDecl.fName != xSElementDecl2.fName || xSElementDecl.fTargetNamespace != xSElementDecl2.fTargetNamespace) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.1", new Object[]{xSElementDecl.fName, xSElementDecl.fTargetNamespace, xSElementDecl2.fName, xSElementDecl2.fTargetNamespace});
        } else if (!xSElementDecl2.getNillable() && xSElementDecl.getNillable()) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.2", new Object[]{xSElementDecl.fName});
        } else if (!checkOccurrenceRange(i, i2, i3, i4)) {
            Object[] objArr = new Object[5];
            objArr[0] = xSElementDecl.fName;
            objArr[1] = Integer.toString(i);
            String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
            if (i2 == -1) {
                str = str2;
            } else {
                str = Integer.toString(i2);
            }
            objArr[2] = str;
            objArr[3] = Integer.toString(i3);
            if (i4 != -1) {
                str2 = Integer.toString(i4);
            }
            objArr[4] = str2;
            throw new XMLSchemaException("rcase-NameAndTypeOK.3", objArr);
        } else {
            if (xSElementDecl2.getConstraintType() == 2) {
                if (xSElementDecl.getConstraintType() == 2) {
                    boolean z = xSElementDecl.fType.getTypeCategory() == 16 || ((XSComplexTypeDecl) xSElementDecl.fType).fContentType == 1;
                    if ((!z && !xSElementDecl2.fDefault.normalizedValue.equals(xSElementDecl.fDefault.normalizedValue)) || (z && !xSElementDecl2.fDefault.actualValue.equals(xSElementDecl.fDefault.actualValue))) {
                        throw new XMLSchemaException("rcase-NameAndTypeOK.4.b", new Object[]{xSElementDecl.fName, xSElementDecl.fDefault.stringValue(), xSElementDecl2.fDefault.stringValue()});
                    }
                } else {
                    throw new XMLSchemaException("rcase-NameAndTypeOK.4.a", new Object[]{xSElementDecl.fName, xSElementDecl2.fDefault.stringValue()});
                }
            }
            checkIDConstraintRestriction(xSElementDecl, xSElementDecl2);
            short s = xSElementDecl.fBlock;
            short s2 = xSElementDecl2.fBlock;
            if ((s & s2) != s2 || (s == 0 && s2 != 0)) {
                throw new XMLSchemaException("rcase-NameAndTypeOK.6", new Object[]{xSElementDecl.fName});
            } else if (!checkTypeDerivationOk(xSElementDecl.fType, xSElementDecl2.fType, 25)) {
                throw new XMLSchemaException("rcase-NameAndTypeOK.7", new Object[]{xSElementDecl.fName, xSElementDecl.fType.getName(), xSElementDecl2.fType.getName()});
            }
        }
    }

    private static void checkNSCompat(XSElementDecl xSElementDecl, int i, int i2, XSWildcardDecl xSWildcardDecl, int i3, int i4, boolean z) throws XMLSchemaException {
        String str;
        if (z && !checkOccurrenceRange(i, i2, i3, i4)) {
            Object[] objArr = new Object[5];
            objArr[0] = xSElementDecl.fName;
            objArr[1] = Integer.toString(i);
            String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
            if (i2 == -1) {
                str = str2;
            } else {
                str = Integer.toString(i2);
            }
            objArr[2] = str;
            objArr[3] = Integer.toString(i3);
            if (i4 != -1) {
                str2 = Integer.toString(i4);
            }
            objArr[4] = str2;
            throw new XMLSchemaException("rcase-NSCompat.2", objArr);
        } else if (!xSWildcardDecl.allowNamespace(xSElementDecl.fTargetNamespace)) {
            throw new XMLSchemaException("rcase-NSCompat.1", new Object[]{xSElementDecl.fName, xSElementDecl.fTargetNamespace});
        }
    }

    private static void checkNSSubset(XSWildcardDecl xSWildcardDecl, int i, int i2, XSWildcardDecl xSWildcardDecl2, int i3, int i4) throws XMLSchemaException {
        String str;
        if (!checkOccurrenceRange(i, i2, i3, i4)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(i);
            String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
            if (i2 == -1) {
                str = str2;
            } else {
                str = Integer.toString(i2);
            }
            objArr[1] = str;
            objArr[2] = Integer.toString(i3);
            if (i4 != -1) {
                str2 = Integer.toString(i4);
            }
            objArr[3] = str2;
            throw new XMLSchemaException("rcase-NSSubset.2", objArr);
        } else if (!xSWildcardDecl.isSubsetOf(xSWildcardDecl2)) {
            throw new XMLSchemaException("rcase-NSSubset.1", null);
        } else if (xSWildcardDecl.weakerProcessContents(xSWildcardDecl2)) {
            throw new XMLSchemaException("rcase-NSSubset.3", new Object[]{xSWildcardDecl.getProcessContentsAsString(), xSWildcardDecl2.getProcessContentsAsString()});
        }
    }

    private static void checkNSRecurseCheckCardinality(Vector vector, int i, int i2, SubstitutionGroupHandler substitutionGroupHandler, XSParticleDecl xSParticleDecl, int i3, int i4, boolean z) throws XMLSchemaException {
        String str;
        if (!z || checkOccurrenceRange(i, i2, i3, i4)) {
            int size = vector.size();
            for (int i5 = 0; i5 < size; i5++) {
                try {
                    particleValidRestriction((XSParticleDecl) vector.elementAt(i5), substitutionGroupHandler, xSParticleDecl, null, false);
                } catch (XMLSchemaException unused) {
                    throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.1", null);
                }
            }
            return;
        }
        Object[] objArr = new Object[4];
        objArr[0] = Integer.toString(i);
        String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
        if (i2 == -1) {
            str = str2;
        } else {
            str = Integer.toString(i2);
        }
        objArr[1] = str;
        objArr[2] = Integer.toString(i3);
        if (i4 != -1) {
            str2 = Integer.toString(i4);
        }
        objArr[3] = str2;
        throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.2", objArr);
    }

    private static void checkRecurse(Vector vector, int i, int i2, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int i3, int i4, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        String str;
        int i5 = 0;
        if (!checkOccurrenceRange(i, i2, i3, i4)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(i);
            String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
            if (i2 == -1) {
                str = str2;
            } else {
                str = Integer.toString(i2);
            }
            objArr[1] = str;
            objArr[2] = Integer.toString(i3);
            if (i4 != -1) {
                str2 = Integer.toString(i4);
            }
            objArr[3] = str2;
            throw new XMLSchemaException("rcase-Recurse.1", objArr);
        }
        int size = vector.size();
        int size2 = vector2.size();
        int i6 = 0;
        while (i5 < size) {
            XSParticleDecl xSParticleDecl = (XSParticleDecl) vector.elementAt(i5);
            int i7 = i6;
            while (i6 < size2) {
                XSParticleDecl xSParticleDecl2 = (XSParticleDecl) vector2.elementAt(i6);
                i7++;
                try {
                    particleValidRestriction(xSParticleDecl, substitutionGroupHandler, xSParticleDecl2, substitutionGroupHandler2);
                    i5++;
                    i6 = i7;
                } catch (XMLSchemaException unused) {
                    if (xSParticleDecl2.emptiable()) {
                        i6++;
                    } else {
                        throw new XMLSchemaException("rcase-Recurse.2", null);
                    }
                }
            }
            throw new XMLSchemaException("rcase-Recurse.2", null);
        }
        while (i6 < size2) {
            if (((XSParticleDecl) vector2.elementAt(i6)).emptiable()) {
                i6++;
            } else {
                throw new XMLSchemaException("rcase-Recurse.2", null);
            }
        }
    }

    private static void checkRecurseUnordered(Vector vector, int i, int i2, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int i3, int i4, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        String str;
        if (!checkOccurrenceRange(i, i2, i3, i4)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(i);
            String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
            if (i2 == -1) {
                str = str2;
            } else {
                str = Integer.toString(i2);
            }
            objArr[1] = str;
            objArr[2] = Integer.toString(i3);
            if (i4 != -1) {
                str2 = Integer.toString(i4);
            }
            objArr[3] = str2;
            throw new XMLSchemaException("rcase-RecurseUnordered.1", objArr);
        }
        int size = vector.size();
        int size2 = vector2.size();
        boolean[] zArr = new boolean[size2];
        for (int i5 = 0; i5 < size; i5++) {
            XSParticleDecl xSParticleDecl = (XSParticleDecl) vector.elementAt(i5);
            for (int i6 = 0; i6 < size2; i6++) {
                try {
                    particleValidRestriction(xSParticleDecl, substitutionGroupHandler, (XSParticleDecl) vector2.elementAt(i6), substitutionGroupHandler2);
                    if (!zArr[i6]) {
                        zArr[i6] = true;
                    } else {
                        throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
                    }
                } catch (XMLSchemaException unused) {
                }
            }
            throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
        }
        for (int i7 = 0; i7 < size2; i7++) {
            XSParticleDecl xSParticleDecl2 = (XSParticleDecl) vector2.elementAt(i7);
            if (!zArr[i7] && !xSParticleDecl2.emptiable()) {
                throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
            }
        }
    }

    private static void checkRecurseLax(Vector vector, int i, int i2, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int i3, int i4, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        String str;
        if (!checkOccurrenceRange(i, i2, i3, i4)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(i);
            String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
            if (i2 == -1) {
                str = str2;
            } else {
                str = Integer.toString(i2);
            }
            objArr[1] = str;
            objArr[2] = Integer.toString(i3);
            if (i4 != -1) {
                str2 = Integer.toString(i4);
            }
            objArr[3] = str2;
            throw new XMLSchemaException("rcase-RecurseLax.1", objArr);
        }
        int size = vector.size();
        int size2 = vector2.size();
        int i5 = 0;
        for (int i6 = 0; i6 < size; i6++) {
            XSParticleDecl xSParticleDecl = (XSParticleDecl) vector.elementAt(i6);
            int i7 = i5;
            while (i5 < size2) {
                i7++;
                try {
                    if (particleValidRestriction(xSParticleDecl, substitutionGroupHandler, (XSParticleDecl) vector2.elementAt(i5), substitutionGroupHandler2)) {
                        i7--;
                    }
                    i5 = i7;
                } catch (XMLSchemaException unused) {
                    i5++;
                }
            }
            throw new XMLSchemaException("rcase-RecurseLax.2", null);
        }
    }

    private static void checkMapAndSum(Vector vector, int i, int i2, SubstitutionGroupHandler substitutionGroupHandler, Vector vector2, int i3, int i4, SubstitutionGroupHandler substitutionGroupHandler2) throws XMLSchemaException {
        String str;
        if (!checkOccurrenceRange(i, i2, i3, i4)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(i);
            String str2 = SchemaSymbols.ATTVAL_UNBOUNDED;
            if (i2 == -1) {
                str = str2;
            } else {
                str = Integer.toString(i2);
            }
            objArr[1] = str;
            objArr[2] = Integer.toString(i3);
            if (i4 != -1) {
                str2 = Integer.toString(i4);
            }
            objArr[3] = str2;
            throw new XMLSchemaException("rcase-MapAndSum.2", objArr);
        }
        int size = vector.size();
        int size2 = vector2.size();
        for (int i5 = 0; i5 < size; i5++) {
            XSParticleDecl xSParticleDecl = (XSParticleDecl) vector.elementAt(i5);
            for (int i6 = 0; i6 < size2; i6++) {
                try {
                    particleValidRestriction(xSParticleDecl, substitutionGroupHandler, (XSParticleDecl) vector2.elementAt(i6), substitutionGroupHandler2);
                } catch (XMLSchemaException unused) {
                }
            }
            throw new XMLSchemaException("rcase-MapAndSum.1", null);
        }
    }

    public static boolean overlapUPA(XSElementDecl xSElementDecl, XSElementDecl xSElementDecl2, SubstitutionGroupHandler substitutionGroupHandler) {
        if (xSElementDecl.fName == xSElementDecl2.fName && xSElementDecl.fTargetNamespace == xSElementDecl2.fTargetNamespace) {
            return true;
        }
        XSElementDecl[] substitutionGroup = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl);
        for (int length = substitutionGroup.length - 1; length >= 0; length--) {
            if (substitutionGroup[length].fName == xSElementDecl2.fName && substitutionGroup[length].fTargetNamespace == xSElementDecl2.fTargetNamespace) {
                return true;
            }
        }
        XSElementDecl[] substitutionGroup2 = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl2);
        for (int length2 = substitutionGroup2.length - 1; length2 >= 0; length2--) {
            if (substitutionGroup2[length2].fName == xSElementDecl.fName && substitutionGroup2[length2].fTargetNamespace == xSElementDecl.fTargetNamespace) {
                return true;
            }
        }
        return false;
    }

    public static boolean overlapUPA(XSElementDecl xSElementDecl, XSWildcardDecl xSWildcardDecl, SubstitutionGroupHandler substitutionGroupHandler) {
        if (xSWildcardDecl.allowNamespace(xSElementDecl.fTargetNamespace)) {
            return true;
        }
        XSElementDecl[] substitutionGroup = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl);
        for (int length = substitutionGroup.length - 1; length >= 0; length--) {
            if (xSWildcardDecl.allowNamespace(substitutionGroup[length].fTargetNamespace)) {
                return true;
            }
        }
        return false;
    }

    public static boolean overlapUPA(XSWildcardDecl xSWildcardDecl, XSWildcardDecl xSWildcardDecl2) {
        XSWildcardDecl performIntersectionWith = xSWildcardDecl.performIntersectionWith(xSWildcardDecl2, xSWildcardDecl.fProcessContents);
        return (performIntersectionWith != null && performIntersectionWith.fType == 3 && performIntersectionWith.fNamespaceList.length == 0) ? false : true;
    }

    public static boolean overlapUPA(Object obj, Object obj2, SubstitutionGroupHandler substitutionGroupHandler) {
        if (obj instanceof XSElementDecl) {
            if (obj2 instanceof XSElementDecl) {
                return overlapUPA((XSElementDecl) obj, (XSElementDecl) obj2, substitutionGroupHandler);
            }
            return overlapUPA((XSElementDecl) obj, (XSWildcardDecl) obj2, substitutionGroupHandler);
        } else if (obj2 instanceof XSElementDecl) {
            return overlapUPA((XSElementDecl) obj2, (XSWildcardDecl) obj, substitutionGroupHandler);
        } else {
            return overlapUPA((XSWildcardDecl) obj, (XSWildcardDecl) obj2);
        }
    }
}
