package ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import ohos.com.sun.org.apache.bcel.internal.classfile.Method;
import ohos.com.sun.org.apache.bcel.internal.generic.ALOAD;
import ohos.com.sun.org.apache.bcel.internal.generic.ASTORE;
import ohos.com.sun.org.apache.bcel.internal.generic.BranchHandle;
import ohos.com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import ohos.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import ohos.com.sun.org.apache.bcel.internal.generic.DLOAD;
import ohos.com.sun.org.apache.bcel.internal.generic.DSTORE;
import ohos.com.sun.org.apache.bcel.internal.generic.FLOAD;
import ohos.com.sun.org.apache.bcel.internal.generic.FSTORE;
import ohos.com.sun.org.apache.bcel.internal.generic.GOTO;
import ohos.com.sun.org.apache.bcel.internal.generic.ICONST;
import ohos.com.sun.org.apache.bcel.internal.generic.ILOAD;
import ohos.com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import ohos.com.sun.org.apache.bcel.internal.generic.ISTORE;
import ohos.com.sun.org.apache.bcel.internal.generic.IfInstruction;
import ohos.com.sun.org.apache.bcel.internal.generic.Instruction;
import ohos.com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import ohos.com.sun.org.apache.bcel.internal.generic.InstructionList;
import ohos.com.sun.org.apache.bcel.internal.generic.InstructionTargeter;
import ohos.com.sun.org.apache.bcel.internal.generic.LLOAD;
import ohos.com.sun.org.apache.bcel.internal.generic.LSTORE;
import ohos.com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import ohos.com.sun.org.apache.bcel.internal.generic.MethodGen;
import ohos.com.sun.org.apache.bcel.internal.generic.TargetLostException;
import ohos.com.sun.org.apache.bcel.internal.generic.Type;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import ohos.com.sun.org.apache.xpath.internal.compiler.Keywords;

public class MethodGenerator extends MethodGen implements Constants {
    private static final int DOM_INDEX = 1;
    private static final String END_ELEMENT_SIG = "(Ljava/lang/String;)V";
    private static final int HANDLER_INDEX = 3;
    protected static final int INVALID_INDEX = -1;
    private static final int ITERATOR_INDEX = 2;
    private static final int MAX_BRANCH_TARGET_OFFSET = 32767;
    private static final int MAX_METHOD_SIZE = 65535;
    private static final int MINIMUM_OUTLINEABLE_CHUNK_SIZE = 1000;
    private static final int MIN_BRANCH_TARGET_OFFSET = -32768;
    private static final String START_ELEMENT_SIG = "(Ljava/lang/String;)V";
    private static final int TARGET_METHOD_SIZE = 60000;
    private boolean _allocatorInit = false;
    private final Instruction _aloadDom = new ALOAD(1);
    private final Instruction _aloadHandler = new ALOAD(3);
    private final Instruction _aloadIterator = new ALOAD(2);
    private final Instruction _astoreDom = new ASTORE(1);
    private final Instruction _astoreHandler = new ASTORE(3);
    private final Instruction _astoreIterator = new ASTORE(2);
    private final Instruction _attribute;
    private final Instruction _endDocument;
    private final Instruction _endElement;
    private Instruction _iloadCurrent;
    private Instruction _istoreCurrent;
    private LocalVariableRegistry _localVariableRegistry;
    private InstructionList _mapTypeSub;
    private final Instruction _namespace;
    private final Instruction _nextNode;
    private Map<Pattern, InstructionList> _preCompiled = new HashMap();
    private final Instruction _reset;
    private final Instruction _setStartNode;
    private SlotAllocator _slotAllocator;
    private final Instruction _startDocument;
    private final Instruction _startElement;
    private final Instruction _uniqueAttribute;
    private int m_openChunks = 0;
    private int m_totalChunks = 0;

    public MethodGenerator(int i, Type type, Type[] typeArr, String[] strArr, String str, String str2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        super(i, type, typeArr, strArr, str, str2, instructionList, constantPoolGen);
        this._startElement = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("ohos.com.sun.org.apache.xml.internal.serializer.SerializationHandler", "startElement", "(Ljava/lang/String;)V"), 2);
        this._endElement = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("ohos.com.sun.org.apache.xml.internal.serializer.SerializationHandler", "endElement", "(Ljava/lang/String;)V"), 2);
        this._attribute = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("ohos.com.sun.org.apache.xml.internal.serializer.SerializationHandler", Constants.ADD_ATTRIBUTE, "(Ljava/lang/String;Ljava/lang/String;)V"), 3);
        this._uniqueAttribute = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("ohos.com.sun.org.apache.xml.internal.serializer.SerializationHandler", "addUniqueAttribute", "(Ljava/lang/String;Ljava/lang/String;I)V"), 4);
        this._namespace = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("ohos.com.sun.org.apache.xml.internal.serializer.SerializationHandler", "namespaceAfterStartElement", "(Ljava/lang/String;Ljava/lang/String;)V"), 3);
        this._startDocument = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("ohos.com.sun.org.apache.xml.internal.serializer.SerializationHandler", "startDocument", "()V"), 1);
        this._endDocument = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref("ohos.com.sun.org.apache.xml.internal.serializer.SerializationHandler", "endDocument", "()V"), 1);
        this._setStartNode = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.SET_START_NODE, "(I)Lohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;"), 2);
        this._reset = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.RESET, "()Lohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;"), 1);
        this._nextNode = new INVOKEINTERFACE(constantPoolGen.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.NEXT, "()I"), 1);
        this._slotAllocator = new SlotAllocator();
        this._slotAllocator.initialize(getLocalVariableRegistry().getLocals(false));
        this._allocatorInit = true;
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.MethodGen
    public LocalVariableGen addLocalVariable(String str, Type type, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        if (this._allocatorInit) {
            return addLocalVariable2(str, type, instructionHandle);
        }
        LocalVariableGen addLocalVariable = super.addLocalVariable(str, type, instructionHandle, instructionHandle2);
        getLocalVariableRegistry().registerLocalVariable(addLocalVariable);
        return addLocalVariable;
    }

    public LocalVariableGen addLocalVariable2(String str, Type type, InstructionHandle instructionHandle) {
        LocalVariableGen addLocalVariable = super.addLocalVariable(str, type, this._slotAllocator.allocateSlot(type), instructionHandle, null);
        getLocalVariableRegistry().registerLocalVariable(addLocalVariable);
        return addLocalVariable;
    }

    private LocalVariableRegistry getLocalVariableRegistry() {
        if (this._localVariableRegistry == null) {
            this._localVariableRegistry = new LocalVariableRegistry();
        }
        return this._localVariableRegistry;
    }

    /* access modifiers changed from: protected */
    public class LocalVariableRegistry {
        protected HashMap _nameToLVGMap = new HashMap();
        protected ArrayList _variables = new ArrayList();

        protected LocalVariableRegistry() {
        }

        /* access modifiers changed from: protected */
        public void registerLocalVariable(LocalVariableGen localVariableGen) {
            int index = localVariableGen.getIndex();
            int size = this._variables.size();
            if (index >= size) {
                while (size < index) {
                    this._variables.add(null);
                    size++;
                }
                this._variables.add(localVariableGen);
            } else {
                Object obj = this._variables.get(index);
                if (obj == null) {
                    this._variables.set(index, localVariableGen);
                } else if (obj instanceof LocalVariableGen) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(obj);
                    arrayList.add(localVariableGen);
                    this._variables.set(index, arrayList);
                } else {
                    ((ArrayList) obj).add(localVariableGen);
                }
            }
            registerByName(localVariableGen);
        }

        /* access modifiers changed from: protected */
        public LocalVariableGen lookupRegisteredLocalVariable(int i, int i2) {
            ArrayList arrayList = this._variables;
            Object obj = arrayList != null ? arrayList.get(i) : null;
            if (obj != null) {
                if (obj instanceof LocalVariableGen) {
                    LocalVariableGen localVariableGen = (LocalVariableGen) obj;
                    if (MethodGenerator.this.offsetInLocalVariableGenRange(localVariableGen, i2)) {
                        return localVariableGen;
                    }
                } else {
                    ArrayList arrayList2 = (ArrayList) obj;
                    int size = arrayList2.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        LocalVariableGen localVariableGen2 = (LocalVariableGen) arrayList2.get(i3);
                        if (MethodGenerator.this.offsetInLocalVariableGenRange(localVariableGen2, i2)) {
                            return localVariableGen2;
                        }
                    }
                }
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void registerByName(LocalVariableGen localVariableGen) {
            ArrayList arrayList;
            Object obj = this._nameToLVGMap.get(localVariableGen.getName());
            if (obj == null) {
                this._nameToLVGMap.put(localVariableGen.getName(), localVariableGen);
                return;
            }
            if (obj instanceof ArrayList) {
                arrayList = (ArrayList) obj;
                arrayList.add(localVariableGen);
            } else {
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(obj);
                arrayList2.add(localVariableGen);
                arrayList = arrayList2;
            }
            this._nameToLVGMap.put(localVariableGen.getName(), arrayList);
        }

        /* access modifiers changed from: protected */
        public void removeByNameTracking(LocalVariableGen localVariableGen) {
            Object obj = this._nameToLVGMap.get(localVariableGen.getName());
            if (obj instanceof ArrayList) {
                ArrayList arrayList = (ArrayList) obj;
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i) == localVariableGen) {
                        arrayList.remove(i);
                        return;
                    }
                }
                return;
            }
            this._nameToLVGMap.remove(localVariableGen);
        }

        /* access modifiers changed from: protected */
        public LocalVariableGen lookUpByName(String str) {
            Object obj = this._nameToLVGMap.get(str);
            if (!(obj instanceof ArrayList)) {
                return (LocalVariableGen) obj;
            }
            ArrayList arrayList = (ArrayList) obj;
            LocalVariableGen localVariableGen = null;
            for (int i = 0; i < arrayList.size(); i++) {
                localVariableGen = (LocalVariableGen) arrayList.get(i);
                if (localVariableGen.getName() == str) {
                    return localVariableGen;
                }
            }
            return localVariableGen;
        }

        /* access modifiers changed from: protected */
        public LocalVariableGen[] getLocals(boolean z) {
            ArrayList arrayList = new ArrayList();
            if (z) {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    Object obj = this._variables.get(i);
                    if (obj != null) {
                        if (obj instanceof ArrayList) {
                            ArrayList arrayList2 = (ArrayList) obj;
                            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                                arrayList.add(arrayList2.get(i));
                            }
                        } else {
                            arrayList.add(obj);
                        }
                    }
                }
            } else {
                for (Map.Entry entry : this._nameToLVGMap.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        if (value instanceof ArrayList) {
                            ArrayList arrayList3 = (ArrayList) value;
                            for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                                arrayList.add(arrayList3.get(i3));
                            }
                        } else {
                            arrayList.add(value);
                        }
                    }
                }
            }
            LocalVariableGen[] localVariableGenArr = new LocalVariableGen[arrayList.size()];
            arrayList.toArray(localVariableGenArr);
            return localVariableGenArr;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean offsetInLocalVariableGenRange(LocalVariableGen localVariableGen, int i) {
        InstructionHandle start = localVariableGen.getStart();
        InstructionHandle end = localVariableGen.getEnd();
        if (start == null) {
            start = getInstructionList().getStart();
        }
        if (end == null) {
            end = getInstructionList().getEnd();
        }
        return start.getPosition() <= i && end.getPosition() + end.getInstruction().getLength() >= i;
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.MethodGen
    public void removeLocalVariable(LocalVariableGen localVariableGen) {
        this._slotAllocator.releaseSlot(localVariableGen);
        getLocalVariableRegistry().removeByNameTracking(localVariableGen);
        super.removeLocalVariable(localVariableGen);
    }

    public Instruction loadDOM() {
        return this._aloadDom;
    }

    public Instruction storeDOM() {
        return this._astoreDom;
    }

    public Instruction storeHandler() {
        return this._astoreHandler;
    }

    public Instruction loadHandler() {
        return this._aloadHandler;
    }

    public Instruction storeIterator() {
        return this._astoreIterator;
    }

    public Instruction loadIterator() {
        return this._aloadIterator;
    }

    public final Instruction setStartNode() {
        return this._setStartNode;
    }

    public final Instruction reset() {
        return this._reset;
    }

    public final Instruction nextNode() {
        return this._nextNode;
    }

    public final Instruction startElement() {
        return this._startElement;
    }

    public final Instruction endElement() {
        return this._endElement;
    }

    public final Instruction startDocument() {
        return this._startDocument;
    }

    public final Instruction endDocument() {
        return this._endDocument;
    }

    public final Instruction attribute() {
        return this._attribute;
    }

    public final Instruction uniqueAttribute() {
        return this._uniqueAttribute;
    }

    public final Instruction namespace() {
        return this._namespace;
    }

    public Instruction loadCurrentNode() {
        if (this._iloadCurrent == null) {
            int localIndex = getLocalIndex(Keywords.FUNC_CURRENT_STRING);
            if (localIndex > 0) {
                this._iloadCurrent = new ILOAD(localIndex);
            } else {
                this._iloadCurrent = new ICONST(0);
            }
        }
        return this._iloadCurrent;
    }

    public Instruction storeCurrentNode() {
        Instruction instruction = this._istoreCurrent;
        if (instruction != null) {
            return instruction;
        }
        ISTORE istore = new ISTORE(getLocalIndex(Keywords.FUNC_CURRENT_STRING));
        this._istoreCurrent = istore;
        return istore;
    }

    public Instruction loadContextNode() {
        return loadCurrentNode();
    }

    public Instruction storeContextNode() {
        return storeCurrentNode();
    }

    public int getLocalIndex(String str) {
        return getLocalVariable(str).getIndex();
    }

    public LocalVariableGen getLocalVariable(String str) {
        return getLocalVariableRegistry().lookUpByName(str);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.MethodGen
    public void setMaxLocals() {
        int maxLocals = super.getMaxLocals();
        LocalVariableGen[] localVariables = super.getLocalVariables();
        if (localVariables != null && localVariables.length > maxLocals) {
            maxLocals = localVariables.length;
        }
        if (maxLocals < 5) {
            maxLocals = 5;
        }
        super.setMaxLocals(maxLocals);
    }

    public void addInstructionList(Pattern pattern, InstructionList instructionList) {
        this._preCompiled.put(pattern, instructionList);
    }

    public InstructionList getInstructionList(Pattern pattern) {
        return this._preCompiled.get(pattern);
    }

    /* access modifiers changed from: private */
    public class Chunk implements Comparable {
        private InstructionHandle m_end;
        private int m_size;
        private InstructionHandle m_start;

        Chunk(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
            this.m_start = instructionHandle;
            this.m_end = instructionHandle2;
            this.m_size = instructionHandle2.getPosition() - instructionHandle.getPosition();
        }

        /* access modifiers changed from: package-private */
        public boolean isAdjacentTo(Chunk chunk) {
            return getChunkEnd().getNext() == chunk.getChunkStart();
        }

        /* access modifiers changed from: package-private */
        public InstructionHandle getChunkStart() {
            return this.m_start;
        }

        /* access modifiers changed from: package-private */
        public InstructionHandle getChunkEnd() {
            return this.m_end;
        }

        /* access modifiers changed from: package-private */
        public int getChunkSize() {
            return this.m_size;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object obj) {
            return getChunkSize() - ((Chunk) obj).getChunkSize();
        }
    }

    private ArrayList getCandidateChunks(ClassGenerator classGenerator, int i) {
        InstructionHandle instructionHandle;
        int size;
        Iterator it = getInstructionList().iterator();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Stack stack = new Stack();
        if (this.m_openChunks == 0) {
            ArrayList arrayList3 = arrayList2;
            boolean z = true;
            boolean z2 = false;
            do {
                ArrayList arrayList4 = null;
                instructionHandle = it.hasNext() ? (InstructionHandle) it.next() : null;
                Instruction instruction = instructionHandle != null ? instructionHandle.getInstruction() : null;
                if (z) {
                    arrayList3.add(instructionHandle);
                    z2 = true;
                    z = false;
                }
                if (instruction instanceof OutlineableChunkStart) {
                    if (z2) {
                        stack.push(arrayList3);
                        arrayList3 = new ArrayList();
                    }
                    arrayList3.add(instructionHandle);
                    z2 = true;
                    continue;
                } else if (instructionHandle == null || (instruction instanceof OutlineableChunkEnd)) {
                    if (!z2) {
                        arrayList4 = arrayList3;
                        arrayList3 = (ArrayList) stack.pop();
                    }
                    if ((instructionHandle != null ? instructionHandle.getPosition() : i) - ((InstructionHandle) arrayList3.get(arrayList3.size() - 1)).getPosition() <= 60000) {
                        arrayList3.add(instructionHandle);
                    } else {
                        if (!z2 && (size = arrayList4.size() / 2) > 0) {
                            Chunk[] chunkArr = new Chunk[size];
                            for (int i2 = 0; i2 < size; i2++) {
                                int i3 = i2 * 2;
                                chunkArr[i2] = new Chunk((InstructionHandle) arrayList4.get(i3), (InstructionHandle) arrayList4.get(i3 + 1));
                            }
                            ArrayList mergeAdjacentChunks = mergeAdjacentChunks(chunkArr);
                            for (int i4 = 0; i4 < mergeAdjacentChunks.size(); i4++) {
                                Chunk chunk = (Chunk) mergeAdjacentChunks.get(i4);
                                int chunkSize = chunk.getChunkSize();
                                if (chunkSize >= 1000 && chunkSize <= 60000) {
                                    arrayList.add(chunk);
                                }
                            }
                        }
                        arrayList3.remove(arrayList3.size() - 1);
                    }
                    z2 = (arrayList3.size() & 1) == 1;
                    continue;
                }
            } while (instructionHandle != null);
            return arrayList;
        }
        throw new InternalError(new ErrorMsg(ErrorMsg.OUTLINE_ERR_UNBALANCED_MARKERS).toString());
    }

    private ArrayList mergeAdjacentChunks(Chunk[] chunkArr) {
        int[] iArr = new int[chunkArr.length];
        int[] iArr2 = new int[chunkArr.length];
        boolean[] zArr = new boolean[chunkArr.length];
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 1; i4 < chunkArr.length; i4++) {
            if (!chunkArr[i4 - 1].isAdjacentTo(chunkArr[i4])) {
                int i5 = i4 - i;
                if (i2 < i5) {
                    i2 = i5;
                }
                if (i5 > 1) {
                    iArr2[i3] = i5;
                    iArr[i3] = i;
                    i3++;
                }
                i = i4;
            }
        }
        if (chunkArr.length - i > 1) {
            int length = chunkArr.length - i;
            if (i2 < length) {
                i2 = length;
            }
            iArr2[i3] = chunkArr.length - i;
            iArr[i3] = i;
            i3++;
        }
        while (i2 > 1) {
            int i6 = 0;
            while (i6 < i3) {
                int i7 = iArr[i6];
                int i8 = (iArr2[i6] + i7) - 1;
                int i9 = i3;
                boolean z = false;
                while (true) {
                    int i10 = (i7 + i2) - 1;
                    if (i10 > i8 || z) {
                        i6++;
                        i3 = i9;
                    } else {
                        int i11 = 0;
                        for (int i12 = i7; i12 <= i10; i12++) {
                            i11 += chunkArr[i12].getChunkSize();
                        }
                        if (i11 <= 60000) {
                            for (int i13 = i7; i13 <= i10; i13++) {
                                zArr[i13] = true;
                            }
                            arrayList.add(new Chunk(chunkArr[i7].getChunkStart(), chunkArr[i10].getChunkEnd()));
                            iArr2[i6] = iArr[i6] - i7;
                            int i14 = i8 - i10;
                            if (i14 >= 2) {
                                iArr[i9] = i10 + 1;
                                iArr2[i9] = i14;
                                i9++;
                            }
                            z = true;
                        }
                        i7++;
                    }
                }
                i6++;
                i3 = i9;
            }
            i2--;
        }
        for (int i15 = 0; i15 < chunkArr.length; i15++) {
            if (!zArr[i15]) {
                arrayList.add(chunkArr[i15]);
            }
        }
        return arrayList;
    }

    public Method[] outlineChunks(ClassGenerator classGenerator, int i) {
        ArrayList arrayList = new ArrayList();
        String name = getName();
        if (name.equals(ohos.com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME)) {
            name = "$lt$init$gt$";
        } else if (name.equals(ohos.com.sun.org.apache.bcel.internal.Constants.STATIC_INITIALIZER_NAME)) {
            name = "$lt$clinit$gt$";
        }
        int i2 = 0;
        while (true) {
            ArrayList candidateChunks = getCandidateChunks(classGenerator, i);
            Collections.sort(candidateChunks);
            int size = candidateChunks.size() - 1;
            int i3 = i2;
            boolean z = false;
            while (size >= 0 && i > 60000) {
                Chunk chunk = (Chunk) candidateChunks.get(size);
                arrayList.add(outline(chunk.getChunkStart(), chunk.getChunkEnd(), name + "$outline$" + i3, classGenerator));
                i3++;
                InstructionList instructionList = getInstructionList();
                InstructionHandle end = instructionList.getEnd();
                instructionList.setPositions();
                i = end.getPosition() + end.getInstruction().getLength();
                size--;
                z = true;
            }
            if (z && i > 60000) {
                i2 = i3;
            }
        }
        if (i <= 65535) {
            Method[] methodArr = new Method[(arrayList.size() + 1)];
            arrayList.toArray(methodArr);
            methodArr[arrayList.size()] = getThisMethod();
            return methodArr;
        }
        throw new InternalError(new ErrorMsg(ErrorMsg.OUTLINE_ERR_METHOD_TOO_BIG).toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:105:0x03f3  */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x03f8 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x02d2  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x02dc  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x02e4  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x02f5  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x03c8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.com.sun.org.apache.bcel.internal.classfile.Method outline(ohos.com.sun.org.apache.bcel.internal.generic.InstructionHandle r43, ohos.com.sun.org.apache.bcel.internal.generic.InstructionHandle r44, java.lang.String r45, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator r46) {
        /*
        // Method dump skipped, instructions count: 1266
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator.outline(ohos.com.sun.org.apache.bcel.internal.generic.InstructionHandle, ohos.com.sun.org.apache.bcel.internal.generic.InstructionHandle, java.lang.String, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator):ohos.com.sun.org.apache.bcel.internal.classfile.Method");
    }

    private static Instruction loadLocal(int i, Type type) {
        if (type == Type.BOOLEAN) {
            return new ILOAD(i);
        }
        if (type == Type.INT) {
            return new ILOAD(i);
        }
        if (type == Type.SHORT) {
            return new ILOAD(i);
        }
        if (type == Type.LONG) {
            return new LLOAD(i);
        }
        if (type == Type.BYTE) {
            return new ILOAD(i);
        }
        if (type == Type.CHAR) {
            return new ILOAD(i);
        }
        if (type == Type.FLOAT) {
            return new FLOAD(i);
        }
        if (type == Type.DOUBLE) {
            return new DLOAD(i);
        }
        return new ALOAD(i);
    }

    private static Instruction storeLocal(int i, Type type) {
        if (type == Type.BOOLEAN) {
            return new ISTORE(i);
        }
        if (type == Type.INT) {
            return new ISTORE(i);
        }
        if (type == Type.SHORT) {
            return new ISTORE(i);
        }
        if (type == Type.LONG) {
            return new LSTORE(i);
        }
        if (type == Type.BYTE) {
            return new ISTORE(i);
        }
        if (type == Type.CHAR) {
            return new ISTORE(i);
        }
        if (type == Type.FLOAT) {
            return new FSTORE(i);
        }
        if (type == Type.DOUBLE) {
            return new DSTORE(i);
        }
        return new ASTORE(i);
    }

    public void markChunkStart() {
        getInstructionList().append(OutlineableChunkStart.OUTLINEABLECHUNKSTART);
        this.m_totalChunks++;
        this.m_openChunks++;
    }

    public void markChunkEnd() {
        getInstructionList().append(OutlineableChunkEnd.OUTLINEABLECHUNKEND);
        this.m_openChunks--;
        if (this.m_openChunks < 0) {
            throw new InternalError(new ErrorMsg(ErrorMsg.OUTLINE_ERR_UNBALANCED_MARKERS).toString());
        }
    }

    /* access modifiers changed from: package-private */
    public Method[] getGeneratedMethods(ClassGenerator classGenerator) {
        InstructionList instructionList = getInstructionList();
        InstructionHandle end = instructionList.getEnd();
        instructionList.setPositions();
        int position = end.getPosition() + end.getInstruction().getLength();
        if (position > MAX_BRANCH_TARGET_OFFSET && widenConditionalBranchTargetOffsets()) {
            instructionList.setPositions();
            InstructionHandle end2 = instructionList.getEnd();
            position = end2.getPosition() + end2.getInstruction().getLength();
        }
        if (position > 65535) {
            return outlineChunks(classGenerator, position);
        }
        return new Method[]{getThisMethod()};
    }

    /* access modifiers changed from: protected */
    public Method getThisMethod() {
        stripAttributes(true);
        setMaxLocals();
        setMaxStack();
        removeNOPs();
        return getMethod();
    }

    /* access modifiers changed from: package-private */
    public boolean widenConditionalBranchTargetOffsets() {
        InstructionList instructionList = getInstructionList();
        int i = 0;
        for (InstructionHandle start = instructionList.getStart(); start != null; start = start.getNext()) {
            short opcode = start.getInstruction().getOpcode();
            if (opcode == 170 || opcode == 171) {
                i += 3;
            } else {
                if (!(opcode == 198 || opcode == 199)) {
                    switch (opcode) {
                        case 167:
                        case 168:
                            i += 2;
                            break;
                    }
                }
                i += 5;
            }
        }
        BranchHandle start2 = instructionList.getStart();
        boolean z = false;
        while (start2 != null) {
            Instruction instruction = start2.getInstruction();
            if (instruction instanceof IfInstruction) {
                IfInstruction ifInstruction = (IfInstruction) instruction;
                BranchHandle branchHandle = (BranchHandle) start2;
                InstructionHandle target = ifInstruction.getTarget();
                int position = target.getPosition() - branchHandle.getPosition();
                if (position - i < MIN_BRANCH_TARGET_OFFSET || position + i > MAX_BRANCH_TARGET_OFFSET) {
                    InstructionHandle next = branchHandle.getNext();
                    BranchHandle append = instructionList.append((InstructionHandle) branchHandle, (BranchInstruction) ifInstruction.negate());
                    BranchHandle append2 = instructionList.append((InstructionHandle) append, (BranchInstruction) new GOTO(target));
                    if (next == null) {
                        next = instructionList.append(append2, NOP);
                    }
                    append.updateTarget(target, next);
                    if (branchHandle.hasTargeters()) {
                        InstructionTargeter[] targeters = branchHandle.getTargeters();
                        for (InstructionTargeter instructionTargeter : targeters) {
                            if (instructionTargeter instanceof LocalVariableGen) {
                                LocalVariableGen localVariableGen = (LocalVariableGen) instructionTargeter;
                                if (localVariableGen.getStart() == branchHandle) {
                                    localVariableGen.setStart(append);
                                } else if (localVariableGen.getEnd() == branchHandle) {
                                    localVariableGen.setEnd(append2);
                                }
                            } else {
                                instructionTargeter.updateTarget(branchHandle, append);
                            }
                        }
                    }
                    try {
                        instructionList.delete(branchHandle);
                        z = true;
                        start2 = append2;
                    } catch (TargetLostException e) {
                        throw new InternalError(new ErrorMsg(ErrorMsg.OUTLINE_ERR_DELETED_TARGET, e.getMessage()).toString());
                    }
                }
            }
            start2 = start2.getNext();
        }
        return z;
    }
}
