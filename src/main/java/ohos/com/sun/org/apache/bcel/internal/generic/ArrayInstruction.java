package ohos.com.sun.org.apache.bcel.internal.generic;

import ohos.com.sun.org.apache.bcel.internal.ExceptionConstants;

public abstract class ArrayInstruction extends Instruction implements ExceptionThrower, TypedInstruction {
    ArrayInstruction() {
    }

    protected ArrayInstruction(short s) {
        super(s, 1);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return ExceptionConstants.EXCS_ARRAY_EXCEPTION;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0027  */
    @Override // ohos.com.sun.org.apache.bcel.internal.generic.TypedInstruction
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.bcel.internal.generic.Type getType(ohos.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen r3) {
        /*
            r2 = this;
            short r3 = r2.opcode
            switch(r3) {
                case 46: goto L_0x0036;
                case 47: goto L_0x0033;
                case 48: goto L_0x0030;
                case 49: goto L_0x002d;
                case 50: goto L_0x002a;
                case 51: goto L_0x0027;
                case 52: goto L_0x0024;
                case 53: goto L_0x0021;
                default: goto L_0x0005;
            }
        L_0x0005:
            switch(r3) {
                case 79: goto L_0x0036;
                case 80: goto L_0x0033;
                case 81: goto L_0x0030;
                case 82: goto L_0x002d;
                case 83: goto L_0x002a;
                case 84: goto L_0x0027;
                case 85: goto L_0x0024;
                case 86: goto L_0x0021;
                default: goto L_0x0008;
            }
        L_0x0008:
            ohos.com.sun.org.apache.bcel.internal.generic.ClassGenException r3 = new ohos.com.sun.org.apache.bcel.internal.generic.ClassGenException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Oops: unknown case in switch"
            r0.append(r1)
            short r2 = r2.opcode
            r0.append(r2)
            java.lang.String r2 = r0.toString()
            r3.<init>(r2)
            throw r3
        L_0x0021:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.SHORT
            return r2
        L_0x0024:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.CHAR
            return r2
        L_0x0027:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.BYTE
            return r2
        L_0x002a:
            ohos.com.sun.org.apache.bcel.internal.generic.ObjectType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.OBJECT
            return r2
        L_0x002d:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.DOUBLE
            return r2
        L_0x0030:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.FLOAT
            return r2
        L_0x0033:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.LONG
            return r2
        L_0x0036:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.INT
            return r2
            switch-data {46->0x0036, 47->0x0033, 48->0x0030, 49->0x002d, 50->0x002a, 51->0x0027, 52->0x0024, 53->0x0021, }
            switch-data {79->0x0036, 80->0x0033, 81->0x0030, 82->0x002d, 83->0x002a, 84->0x0027, 85->0x0024, 86->0x0021, }
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.bcel.internal.generic.ArrayInstruction.getType(ohos.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen):ohos.com.sun.org.apache.bcel.internal.generic.Type");
    }
}
