package ohos.com.sun.org.apache.bcel.internal.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import ohos.com.sun.org.apache.bcel.internal.util.ByteSequence;

public abstract class LocalVariableInstruction extends Instruction implements TypedInstruction, IndexedInstruction {
    private short c_tag = -1;
    private short canon_tag = -1;
    protected int n = -1;

    private final boolean wide() {
        return this.n > 255;
    }

    LocalVariableInstruction(short s, short s2) {
        this.canon_tag = s;
        this.c_tag = s2;
    }

    LocalVariableInstruction() {
    }

    protected LocalVariableInstruction(short s, short s2, int i) {
        super(s, 2);
        this.c_tag = s2;
        this.canon_tag = s;
        setIndex(i);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (wide()) {
            dataOutputStream.writeByte(196);
        }
        dataOutputStream.writeByte(this.opcode);
        if (this.length <= 1) {
            return;
        }
        if (wide()) {
            dataOutputStream.writeShort(this.n);
        } else {
            dataOutputStream.writeByte(this.n);
        }
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean z) {
        if ((this.opcode >= 26 && this.opcode <= 45) || (this.opcode >= 59 && this.opcode <= 78)) {
            return super.toString(z);
        }
        return super.toString(z) + " " + this.n;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction
    public void initFromFile(ByteSequence byteSequence, boolean z) throws IOException {
        if (z) {
            this.n = byteSequence.readUnsignedShort();
            this.length = 4;
        } else if ((this.opcode >= 21 && this.opcode <= 25) || (this.opcode >= 54 && this.opcode <= 58)) {
            this.n = byteSequence.readUnsignedByte();
            this.length = 2;
        } else if (this.opcode <= 45) {
            this.n = (this.opcode - 26) % 4;
            this.length = 1;
        } else {
            this.n = (this.opcode - 59) % 4;
            this.length = 1;
        }
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public final int getIndex() {
        return this.n;
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public void setIndex(int i) {
        if (i < 0 || i > 65535) {
            throw new ClassGenException("Illegal value: " + i);
        }
        this.n = i;
        if (i < 0 || i > 3) {
            this.opcode = this.canon_tag;
            if (wide()) {
                this.length = 4;
            } else {
                this.length = 2;
            }
        } else {
            this.opcode = (short) (this.c_tag + i);
            this.length = 1;
        }
    }

    public short getCanonicalTag() {
        return this.canon_tag;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0027  */
    @Override // ohos.com.sun.org.apache.bcel.internal.generic.TypedInstruction
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.bcel.internal.generic.Type getType(ohos.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen r3) {
        /*
            r2 = this;
            short r3 = r2.canon_tag
            switch(r3) {
                case 21: goto L_0x002d;
                case 22: goto L_0x002a;
                case 23: goto L_0x0027;
                case 24: goto L_0x0024;
                case 25: goto L_0x0021;
                default: goto L_0x0005;
            }
        L_0x0005:
            switch(r3) {
                case 54: goto L_0x002d;
                case 55: goto L_0x002a;
                case 56: goto L_0x0027;
                case 57: goto L_0x0024;
                case 58: goto L_0x0021;
                default: goto L_0x0008;
            }
        L_0x0008:
            ohos.com.sun.org.apache.bcel.internal.generic.ClassGenException r3 = new ohos.com.sun.org.apache.bcel.internal.generic.ClassGenException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Oops: unknown case in switch"
            r0.append(r1)
            short r2 = r2.canon_tag
            r0.append(r2)
            java.lang.String r2 = r0.toString()
            r3.<init>(r2)
            throw r3
        L_0x0021:
            ohos.com.sun.org.apache.bcel.internal.generic.ObjectType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.OBJECT
            return r2
        L_0x0024:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.DOUBLE
            return r2
        L_0x0027:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.FLOAT
            return r2
        L_0x002a:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.LONG
            return r2
        L_0x002d:
            ohos.com.sun.org.apache.bcel.internal.generic.BasicType r2 = ohos.com.sun.org.apache.bcel.internal.generic.Type.INT
            return r2
            switch-data {21->0x002d, 22->0x002a, 23->0x0027, 24->0x0024, 25->0x0021, }
            switch-data {54->0x002d, 55->0x002a, 56->0x0027, 57->0x0024, 58->0x0021, }
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction.getType(ohos.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen):ohos.com.sun.org.apache.bcel.internal.generic.Type");
    }
}
