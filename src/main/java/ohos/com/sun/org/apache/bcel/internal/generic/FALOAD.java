package ohos.com.sun.org.apache.bcel.internal.generic;

public class FALOAD extends ArrayInstruction implements StackProducer {
    public FALOAD() {
        super(48);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor visitor) {
        visitor.visitStackProducer(this);
        visitor.visitExceptionThrower(this);
        visitor.visitTypedInstruction(this);
        visitor.visitArrayInstruction(this);
        visitor.visitFALOAD(this);
    }
}
