package ohos.global.icu.text;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import ohos.global.icu.impl.Assert;
import ohos.global.icu.impl.RBBIDataWrapper;
import ohos.global.icu.lang.UCharacter;
import ohos.global.icu.lang.UProperty;
import ohos.global.icu.text.RBBIRuleBuilder;

class RBBITableBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private List<RBBIStateDescriptor> fDStates = new ArrayList();
    int[] fLookAheadRuleMap;
    private RBBIRuleBuilder fRB;
    private int fRootIx;
    private List<short[]> fSafeTable;

    /* access modifiers changed from: package-private */
    public static class RBBIStateDescriptor {
        int fAccepting;
        int[] fDtran;
        int fLookAhead;
        boolean fMarked;
        Set<RBBINode> fPositions = new HashSet();
        SortedSet<Integer> fTagVals = new TreeSet();
        int fTagsIdx;

        RBBIStateDescriptor(int i) {
            this.fDtran = new int[(i + 1)];
        }
    }

    RBBITableBuilder(RBBIRuleBuilder rBBIRuleBuilder, int i) {
        this.fRootIx = i;
        this.fRB = rBBIRuleBuilder;
    }

    /* access modifiers changed from: package-private */
    public void buildForwardTable() {
        if (this.fRB.fTreeRoots[this.fRootIx] != null) {
            this.fRB.fTreeRoots[this.fRootIx] = this.fRB.fTreeRoots[this.fRootIx].flattenVariables();
            if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("ftree") >= 0) {
                System.out.println("Parse tree after flattening variable references.");
                this.fRB.fTreeRoots[this.fRootIx].printTree(true);
            }
            if (this.fRB.fSetBuilder.sawBOF()) {
                RBBINode rBBINode = new RBBINode(8);
                RBBINode rBBINode2 = new RBBINode(3);
                rBBINode.fLeftChild = rBBINode2;
                rBBINode.fRightChild = this.fRB.fTreeRoots[this.fRootIx];
                rBBINode2.fParent = rBBINode;
                rBBINode2.fVal = 2;
                this.fRB.fTreeRoots[this.fRootIx] = rBBINode;
            }
            RBBINode rBBINode3 = new RBBINode(8);
            rBBINode3.fLeftChild = this.fRB.fTreeRoots[this.fRootIx];
            this.fRB.fTreeRoots[this.fRootIx].fParent = rBBINode3;
            RBBINode rBBINode4 = new RBBINode(6);
            rBBINode3.fRightChild = rBBINode4;
            rBBINode3.fRightChild.fParent = rBBINode3;
            this.fRB.fTreeRoots[this.fRootIx] = rBBINode3;
            this.fRB.fTreeRoots[this.fRootIx].flattenSets();
            if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("stree") >= 0) {
                System.out.println("Parse tree after flattening Unicode Set references.");
                this.fRB.fTreeRoots[this.fRootIx].printTree(true);
            }
            calcNullable(this.fRB.fTreeRoots[this.fRootIx]);
            calcFirstPos(this.fRB.fTreeRoots[this.fRootIx]);
            calcLastPos(this.fRB.fTreeRoots[this.fRootIx]);
            calcFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
            if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("pos") >= 0) {
                System.out.print("\n");
                printPosSets(this.fRB.fTreeRoots[this.fRootIx]);
            }
            if (this.fRB.fChainRules) {
                calcChainedFollowPos(this.fRB.fTreeRoots[this.fRootIx], rBBINode4);
            }
            if (this.fRB.fSetBuilder.sawBOF()) {
                bofFixup();
            }
            buildStateTable();
            mapLookAheadRules();
            flagAcceptingStates();
            flagLookAheadStates();
            flagTaggedStates();
            mergeRuleStatusVals();
        }
    }

    /* access modifiers changed from: package-private */
    public void calcNullable(RBBINode rBBINode) {
        if (rBBINode != null) {
            boolean z = false;
            if (rBBINode.fType == 0 || rBBINode.fType == 6) {
                rBBINode.fNullable = false;
            } else if (rBBINode.fType == 4 || rBBINode.fType == 5) {
                rBBINode.fNullable = true;
            } else {
                calcNullable(rBBINode.fLeftChild);
                calcNullable(rBBINode.fRightChild);
                if (rBBINode.fType == 9) {
                    if (rBBINode.fLeftChild.fNullable || rBBINode.fRightChild.fNullable) {
                        z = true;
                    }
                    rBBINode.fNullable = z;
                } else if (rBBINode.fType == 8) {
                    if (rBBINode.fLeftChild.fNullable && rBBINode.fRightChild.fNullable) {
                        z = true;
                    }
                    rBBINode.fNullable = z;
                } else if (rBBINode.fType == 10 || rBBINode.fType == 12) {
                    rBBINode.fNullable = true;
                } else {
                    rBBINode.fNullable = false;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void calcFirstPos(RBBINode rBBINode) {
        if (rBBINode != null) {
            if (rBBINode.fType == 3 || rBBINode.fType == 6 || rBBINode.fType == 4 || rBBINode.fType == 5) {
                rBBINode.fFirstPosSet.add(rBBINode);
                return;
            }
            calcFirstPos(rBBINode.fLeftChild);
            calcFirstPos(rBBINode.fRightChild);
            if (rBBINode.fType == 9) {
                rBBINode.fFirstPosSet.addAll(rBBINode.fLeftChild.fFirstPosSet);
                rBBINode.fFirstPosSet.addAll(rBBINode.fRightChild.fFirstPosSet);
            } else if (rBBINode.fType == 8) {
                rBBINode.fFirstPosSet.addAll(rBBINode.fLeftChild.fFirstPosSet);
                if (rBBINode.fLeftChild.fNullable) {
                    rBBINode.fFirstPosSet.addAll(rBBINode.fRightChild.fFirstPosSet);
                }
            } else if (rBBINode.fType == 10 || rBBINode.fType == 12 || rBBINode.fType == 11) {
                rBBINode.fFirstPosSet.addAll(rBBINode.fLeftChild.fFirstPosSet);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void calcLastPos(RBBINode rBBINode) {
        if (rBBINode != null) {
            if (rBBINode.fType == 3 || rBBINode.fType == 6 || rBBINode.fType == 4 || rBBINode.fType == 5) {
                rBBINode.fLastPosSet.add(rBBINode);
                return;
            }
            calcLastPos(rBBINode.fLeftChild);
            calcLastPos(rBBINode.fRightChild);
            if (rBBINode.fType == 9) {
                rBBINode.fLastPosSet.addAll(rBBINode.fLeftChild.fLastPosSet);
                rBBINode.fLastPosSet.addAll(rBBINode.fRightChild.fLastPosSet);
            } else if (rBBINode.fType == 8) {
                rBBINode.fLastPosSet.addAll(rBBINode.fRightChild.fLastPosSet);
                if (rBBINode.fRightChild.fNullable) {
                    rBBINode.fLastPosSet.addAll(rBBINode.fLeftChild.fLastPosSet);
                }
            } else if (rBBINode.fType == 10 || rBBINode.fType == 12 || rBBINode.fType == 11) {
                rBBINode.fLastPosSet.addAll(rBBINode.fLeftChild.fLastPosSet);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void calcFollowPos(RBBINode rBBINode) {
        if (!(rBBINode == null || rBBINode.fType == 3 || rBBINode.fType == 6)) {
            calcFollowPos(rBBINode.fLeftChild);
            calcFollowPos(rBBINode.fRightChild);
            if (rBBINode.fType == 8) {
                for (RBBINode rBBINode2 : rBBINode.fLeftChild.fLastPosSet) {
                    rBBINode2.fFollowPos.addAll(rBBINode.fRightChild.fFirstPosSet);
                }
            }
            if (rBBINode.fType == 10 || rBBINode.fType == 11) {
                for (RBBINode rBBINode3 : rBBINode.fLastPosSet) {
                    rBBINode3.fFollowPos.addAll(rBBINode.fFirstPosSet);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addRuleRootNodes(List<RBBINode> list, RBBINode rBBINode) {
        if (rBBINode != null) {
            if (rBBINode.fRuleRoot) {
                list.add(rBBINode);
                return;
            }
            addRuleRootNodes(list, rBBINode.fLeftChild);
            addRuleRootNodes(list, rBBINode.fRightChild);
        }
    }

    /* access modifiers changed from: package-private */
    public void calcChainedFollowPos(RBBINode rBBINode, RBBINode rBBINode2) {
        int firstChar;
        ArrayList arrayList = new ArrayList();
        rBBINode.findNodes(arrayList, 3);
        ArrayList arrayList2 = new ArrayList();
        addRuleRootNodes(arrayList2, rBBINode);
        HashSet<RBBINode> hashSet = new HashSet();
        for (RBBINode rBBINode3 : arrayList2) {
            if (rBBINode3.fChainIn) {
                hashSet.addAll(rBBINode3.fFirstPosSet);
            }
        }
        for (RBBINode rBBINode4 : arrayList) {
            if (rBBINode4.fFollowPos.contains(rBBINode2) && (!this.fRB.fLBCMNoChain || (firstChar = this.fRB.fSetBuilder.getFirstChar(rBBINode4.fVal)) == -1 || UCharacter.getIntPropertyValue(firstChar, UProperty.LINE_BREAK) != 9)) {
                for (RBBINode rBBINode5 : hashSet) {
                    if (rBBINode5.fType == 3 && rBBINode4.fVal == rBBINode5.fVal) {
                        rBBINode4.fFollowPos.addAll(rBBINode5.fFollowPos);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void bofFixup() {
        RBBINode rBBINode = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fLeftChild;
        boolean z = true;
        Assert.assrt(rBBINode.fType == 3);
        if (rBBINode.fVal != 2) {
            z = false;
        }
        Assert.assrt(z);
        for (RBBINode rBBINode2 : this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fRightChild.fFirstPosSet) {
            if (rBBINode2.fType == 3 && rBBINode2.fVal == rBBINode.fVal) {
                rBBINode.fFollowPos.addAll(rBBINode2.fFollowPos);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void buildStateTable() {
        RBBIStateDescriptor rBBIStateDescriptor;
        int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories() - 1;
        this.fDStates.add(new RBBIStateDescriptor(numCharCategories));
        RBBIStateDescriptor rBBIStateDescriptor2 = new RBBIStateDescriptor(numCharCategories);
        rBBIStateDescriptor2.fPositions.addAll(this.fRB.fTreeRoots[this.fRootIx].fFirstPosSet);
        this.fDStates.add(rBBIStateDescriptor2);
        while (true) {
            int i = 1;
            while (true) {
                if (i >= this.fDStates.size()) {
                    rBBIStateDescriptor = null;
                    break;
                }
                rBBIStateDescriptor = this.fDStates.get(i);
                if (!rBBIStateDescriptor.fMarked) {
                    break;
                }
                i++;
            }
            if (rBBIStateDescriptor != null) {
                rBBIStateDescriptor.fMarked = true;
                for (int i2 = 1; i2 <= numCharCategories; i2++) {
                    Set<RBBINode> set = null;
                    for (RBBINode rBBINode : rBBIStateDescriptor.fPositions) {
                        if (rBBINode.fType == 3 && rBBINode.fVal == i2) {
                            if (set == null) {
                                set = new HashSet<>();
                            }
                            set.addAll(rBBINode.fFollowPos);
                        }
                    }
                    if (set != null) {
                        boolean z = false;
                        Assert.assrt(set.size() > 0);
                        int i3 = 0;
                        while (true) {
                            if (i3 >= this.fDStates.size()) {
                                i3 = 0;
                                break;
                            }
                            RBBIStateDescriptor rBBIStateDescriptor3 = this.fDStates.get(i3);
                            if (set.equals(rBBIStateDescriptor3.fPositions)) {
                                set = rBBIStateDescriptor3.fPositions;
                                z = true;
                                break;
                            }
                            i3++;
                        }
                        if (!z) {
                            RBBIStateDescriptor rBBIStateDescriptor4 = new RBBIStateDescriptor(numCharCategories);
                            rBBIStateDescriptor4.fPositions = set;
                            this.fDStates.add(rBBIStateDescriptor4);
                            i3 = this.fDStates.size() - 1;
                        }
                        rBBIStateDescriptor.fDtran[i2] = i3;
                    }
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void mapLookAheadRules() {
        this.fLookAheadRuleMap = new int[(this.fRB.fScanner.numRules() + 1)];
        int i = 0;
        for (RBBIStateDescriptor rBBIStateDescriptor : this.fDStates) {
            boolean z = false;
            int i2 = 0;
            for (RBBINode rBBINode : rBBIStateDescriptor.fPositions) {
                if (rBBINode.fType == 4) {
                    int i3 = this.fLookAheadRuleMap[rBBINode.fVal];
                    if (i3 != 0 && i2 == 0) {
                        i2 = i3;
                    }
                    z = true;
                }
            }
            if (z) {
                if (i2 == 0) {
                    i2 = i + 1;
                    i = i2;
                }
                for (RBBINode rBBINode2 : rBBIStateDescriptor.fPositions) {
                    if (rBBINode2.fType == 4) {
                        int i4 = rBBINode2.fVal;
                        int[] iArr = this.fLookAheadRuleMap;
                        int i5 = iArr[i4];
                        iArr[i4] = i2;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void flagAcceptingStates() {
        ArrayList arrayList = new ArrayList();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(arrayList, 6);
        for (int i = 0; i < arrayList.size(); i++) {
            RBBINode rBBINode = (RBBINode) arrayList.get(i);
            for (int i2 = 0; i2 < this.fDStates.size(); i2++) {
                RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i2);
                if (rBBIStateDescriptor.fPositions.contains(rBBINode)) {
                    if (rBBIStateDescriptor.fAccepting == 0) {
                        rBBIStateDescriptor.fAccepting = this.fLookAheadRuleMap[rBBINode.fVal];
                        if (rBBIStateDescriptor.fAccepting == 0) {
                            rBBIStateDescriptor.fAccepting = -1;
                        }
                    }
                    if (rBBIStateDescriptor.fAccepting == -1 && rBBINode.fVal != 0) {
                        rBBIStateDescriptor.fAccepting = this.fLookAheadRuleMap[rBBINode.fVal];
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void flagLookAheadStates() {
        ArrayList arrayList = new ArrayList();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(arrayList, 4);
        for (int i = 0; i < arrayList.size(); i++) {
            RBBINode rBBINode = (RBBINode) arrayList.get(i);
            for (int i2 = 0; i2 < this.fDStates.size(); i2++) {
                RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i2);
                if (rBBIStateDescriptor.fPositions.contains(rBBINode)) {
                    rBBIStateDescriptor.fLookAhead = this.fLookAheadRuleMap[rBBINode.fVal];
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void flagTaggedStates() {
        ArrayList arrayList = new ArrayList();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(arrayList, 5);
        for (int i = 0; i < arrayList.size(); i++) {
            RBBINode rBBINode = (RBBINode) arrayList.get(i);
            for (int i2 = 0; i2 < this.fDStates.size(); i2++) {
                RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i2);
                if (rBBIStateDescriptor.fPositions.contains(rBBINode)) {
                    rBBIStateDescriptor.fTagVals.add(Integer.valueOf(rBBINode.fVal));
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void mergeRuleStatusVals() {
        if (this.fRB.fRuleStatusVals.size() == 0) {
            this.fRB.fRuleStatusVals.add(1);
            this.fRB.fRuleStatusVals.add(0);
            this.fRB.fStatusSets.put(new TreeSet(), 0);
            TreeSet treeSet = new TreeSet();
            treeSet.add(0);
            this.fRB.fStatusSets.put(treeSet, 0);
        }
        for (int i = 0; i < this.fDStates.size(); i++) {
            RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i);
            SortedSet<Integer> sortedSet = rBBIStateDescriptor.fTagVals;
            Integer num = this.fRB.fStatusSets.get(sortedSet);
            if (num == null) {
                num = Integer.valueOf(this.fRB.fRuleStatusVals.size());
                this.fRB.fStatusSets.put(sortedSet, num);
                this.fRB.fRuleStatusVals.add(Integer.valueOf(sortedSet.size()));
                this.fRB.fRuleStatusVals.addAll(sortedSet);
            }
            rBBIStateDescriptor.fTagsIdx = num.intValue();
        }
    }

    /* access modifiers changed from: package-private */
    public void printPosSets(RBBINode rBBINode) {
        if (rBBINode != null) {
            RBBINode.printNode(rBBINode);
            PrintStream printStream = System.out;
            printStream.print("         Nullable:  " + rBBINode.fNullable);
            System.out.print("         firstpos:  ");
            printSet(rBBINode.fFirstPosSet);
            System.out.print("         lastpos:   ");
            printSet(rBBINode.fLastPosSet);
            System.out.print("         followpos: ");
            printSet(rBBINode.fFollowPos);
            printPosSets(rBBINode.fLeftChild);
            printPosSets(rBBINode.fRightChild);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004c, code lost:
        r10.first++;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean findDuplCharClassFrom(ohos.global.icu.text.RBBIRuleBuilder.IntPair r10) {
        /*
            r9 = this;
            java.util.List<ohos.global.icu.text.RBBITableBuilder$RBBIStateDescriptor> r0 = r9.fDStates
            int r0 = r0.size()
            ohos.global.icu.text.RBBIRuleBuilder r1 = r9.fRB
            ohos.global.icu.text.RBBISetBuilder r1 = r1.fSetBuilder
            int r1 = r1.getNumCharCategories()
            r2 = 0
            r3 = r2
            r4 = r3
        L_0x0011:
            int r5 = r10.first
            int r6 = r1 + -1
            if (r5 >= r6) goto L_0x0052
            int r5 = r10.first
            r6 = 1
        L_0x001a:
            int r5 = r5 + r6
            r10.second = r5
            int r5 = r10.second
            if (r5 >= r1) goto L_0x004c
            r5 = r4
            r4 = r3
            r3 = r2
        L_0x0024:
            if (r3 >= r0) goto L_0x0044
            java.util.List<ohos.global.icu.text.RBBITableBuilder$RBBIStateDescriptor> r4 = r9.fDStates
            java.lang.Object r4 = r4.get(r3)
            ohos.global.icu.text.RBBITableBuilder$RBBIStateDescriptor r4 = (ohos.global.icu.text.RBBITableBuilder.RBBIStateDescriptor) r4
            int[] r5 = r4.fDtran
            int r7 = r10.first
            r5 = r5[r7]
            int[] r4 = r4.fDtran
            int r7 = r10.second
            r4 = r4[r7]
            if (r5 == r4) goto L_0x003e
            r3 = r5
            goto L_0x0046
        L_0x003e:
            int r3 = r3 + 1
            r8 = r5
            r5 = r4
            r4 = r8
            goto L_0x0024
        L_0x0044:
            r3 = r4
            r4 = r5
        L_0x0046:
            if (r3 != r4) goto L_0x0049
            return r6
        L_0x0049:
            int r5 = r10.second
            goto L_0x001a
        L_0x004c:
            int r5 = r10.first
            int r5 = r5 + r6
            r10.first = r5
            goto L_0x0011
        L_0x0052:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.RBBITableBuilder.findDuplCharClassFrom(ohos.global.icu.text.RBBIRuleBuilder$IntPair):boolean");
    }

    /* access modifiers changed from: package-private */
    public void removeColumn(int i) {
        int size = this.fDStates.size();
        for (int i2 = 0; i2 < size; i2++) {
            RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i2);
            int[] copyOf = Arrays.copyOf(rBBIStateDescriptor.fDtran, rBBIStateDescriptor.fDtran.length - 1);
            System.arraycopy(rBBIStateDescriptor.fDtran, i + 1, copyOf, i, copyOf.length - i);
            rBBIStateDescriptor.fDtran = copyOf;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006f, code lost:
        r11.first++;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean findDuplicateState(ohos.global.icu.text.RBBIRuleBuilder.IntPair r11) {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.RBBITableBuilder.findDuplicateState(ohos.global.icu.text.RBBIRuleBuilder$IntPair):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0051, code lost:
        r11.first++;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean findDuplicateSafeState(ohos.global.icu.text.RBBIRuleBuilder.IntPair r11) {
        /*
            r10 = this;
            java.util.List<short[]> r0 = r10.fSafeTable
            int r0 = r0.size()
        L_0x0006:
            int r1 = r11.first
            int r2 = r0 + -1
            r3 = 0
            if (r1 >= r2) goto L_0x0057
            java.util.List<short[]> r1 = r10.fSafeTable
            int r2 = r11.first
            java.lang.Object r1 = r1.get(r2)
            short[] r1 = (short[]) r1
            int r2 = r11.first
            r4 = 1
        L_0x001a:
            int r2 = r2 + r4
            r11.second = r2
            int r2 = r11.second
            if (r2 >= r0) goto L_0x0051
            java.util.List<short[]> r2 = r10.fSafeTable
            int r5 = r11.second
            java.lang.Object r2 = r2.get(r5)
            short[] r2 = (short[]) r2
            int r5 = r1.length
            r6 = r3
        L_0x002d:
            if (r6 >= r5) goto L_0x004a
            short r7 = r1[r6]
            short r8 = r2[r6]
            if (r7 == r8) goto L_0x0047
            int r9 = r11.first
            if (r7 == r9) goto L_0x003d
            int r9 = r11.second
            if (r7 != r9) goto L_0x0045
        L_0x003d:
            int r7 = r11.first
            if (r8 == r7) goto L_0x0047
            int r7 = r11.second
            if (r8 == r7) goto L_0x0047
        L_0x0045:
            r2 = r3
            goto L_0x004b
        L_0x0047:
            int r6 = r6 + 1
            goto L_0x002d
        L_0x004a:
            r2 = r4
        L_0x004b:
            if (r2 == 0) goto L_0x004e
            return r4
        L_0x004e:
            int r2 = r11.second
            goto L_0x001a
        L_0x0051:
            int r1 = r11.first
            int r1 = r1 + r4
            r11.first = r1
            goto L_0x0006
        L_0x0057:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.RBBITableBuilder.findDuplicateSafeState(ohos.global.icu.text.RBBIRuleBuilder$IntPair):boolean");
    }

    /* access modifiers changed from: package-private */
    public void removeState(RBBIRuleBuilder.IntPair intPair) {
        int i = intPair.first;
        int i2 = intPair.second;
        this.fDStates.remove(i2);
        int size = this.fDStates.size();
        int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories();
        for (int i3 = 0; i3 < size; i3++) {
            RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i3);
            for (int i4 = 0; i4 < numCharCategories; i4++) {
                int i5 = rBBIStateDescriptor.fDtran[i4];
                if (i5 == i2) {
                    i5 = i;
                } else if (i5 > i2) {
                    i5--;
                }
                rBBIStateDescriptor.fDtran[i4] = i5;
            }
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:17:0x0029 */
    /* JADX DEBUG: Multi-variable search result rejected for r4v2, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r6v1, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r6v4, resolved type: int */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v2 */
    /* access modifiers changed from: package-private */
    public void removeSafeState(RBBIRuleBuilder.IntPair intPair) {
        int i = intPair.first;
        int i2 = intPair.second;
        this.fSafeTable.remove(i2);
        int size = this.fSafeTable.size();
        for (int i3 = 0; i3 < size; i3++) {
            short[] sArr = this.fSafeTable.get(i3);
            for (int i4 = 0; i4 < sArr.length; i4++) {
                short s = sArr[i4];
                if (s == i2) {
                    s = i;
                } else if (s > i2) {
                    s--;
                }
                sArr[i4] = s == true ? (short) 1 : 0;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int removeDuplicateStates() {
        int i = 0;
        RBBIRuleBuilder.IntPair intPair = new RBBIRuleBuilder.IntPair(3, 0);
        while (findDuplicateState(intPair)) {
            removeState(intPair);
            i++;
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public int getTableSize() {
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return 0;
        }
        return ((this.fDStates.size() * ((this.fRB.fSetBuilder.getNumCharCategories() * 2) + 8)) + 16 + 7) & -8;
    }

    /* access modifiers changed from: package-private */
    public RBBIDataWrapper.RBBIStateTable exportTable() {
        RBBIDataWrapper.RBBIStateTable rBBIStateTable = new RBBIDataWrapper.RBBIStateTable();
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return rBBIStateTable;
        }
        Assert.assrt(this.fRB.fSetBuilder.getNumCharCategories() < 32767 && this.fDStates.size() < 32767);
        rBBIStateTable.fNumStates = this.fDStates.size();
        int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories() + 4;
        rBBIStateTable.fTable = new short[((getTableSize() - 16) / 2)];
        rBBIStateTable.fRowLen = numCharCategories * 2;
        if (this.fRB.fLookAheadHardBreak) {
            rBBIStateTable.fFlags |= 1;
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            rBBIStateTable.fFlags |= 2;
        }
        int numCharCategories2 = this.fRB.fSetBuilder.getNumCharCategories();
        for (int i = 0; i < rBBIStateTable.fNumStates; i++) {
            RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i);
            int i2 = i * numCharCategories;
            Assert.assrt(-32768 < rBBIStateDescriptor.fAccepting && rBBIStateDescriptor.fAccepting <= 32767);
            Assert.assrt(-32768 < rBBIStateDescriptor.fLookAhead && rBBIStateDescriptor.fLookAhead <= 32767);
            rBBIStateTable.fTable[i2 + 0] = (short) rBBIStateDescriptor.fAccepting;
            rBBIStateTable.fTable[i2 + 1] = (short) rBBIStateDescriptor.fLookAhead;
            rBBIStateTable.fTable[i2 + 2] = (short) rBBIStateDescriptor.fTagsIdx;
            for (int i3 = 0; i3 < numCharCategories2; i3++) {
                rBBIStateTable.fTable[i2 + 4 + i3] = (short) rBBIStateDescriptor.fDtran[i3];
            }
        }
        return rBBIStateTable;
    }

    /* access modifiers changed from: package-private */
    public void buildSafeReverseTable() {
        int i;
        StringBuilder sb = new StringBuilder();
        int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories();
        int size = this.fDStates.size();
        for (int i2 = 0; i2 < numCharCategories; i2++) {
            for (int i3 = 0; i3 < numCharCategories; i3++) {
                int i4 = 0;
                int i5 = -1;
                for (int i6 = 1; i6 < size; i6++) {
                    i4 = this.fDStates.get(this.fDStates.get(i6).fDtran[i2]).fDtran[i3];
                    if (i5 < 0) {
                        i5 = i4;
                    } else if (i5 != i4) {
                        break;
                    }
                }
                if (i5 == i4) {
                    sb.append((char) i2);
                    sb.append((char) i3);
                }
            }
        }
        this.fSafeTable = new ArrayList();
        int i7 = 0;
        while (true) {
            i = numCharCategories + 2;
            if (i7 >= i) {
                break;
            }
            this.fSafeTable.add(new short[numCharCategories]);
            i7++;
        }
        short[] sArr = this.fSafeTable.get(1);
        for (int i8 = 0; i8 < numCharCategories; i8++) {
            sArr[i8] = (short) (i8 + 2);
        }
        for (int i9 = 2; i9 < i; i9++) {
            System.arraycopy(sArr, 0, this.fSafeTable.get(i9), 0, sArr.length);
        }
        for (int i10 = 0; i10 < sb.length(); i10 += 2) {
            this.fSafeTable.get(sb.charAt(i10 + 1) + 2)[sb.charAt(i10)] = 0;
        }
        RBBIRuleBuilder.IntPair intPair = new RBBIRuleBuilder.IntPair(1, 0);
        while (findDuplicateSafeState(intPair)) {
            removeSafeState(intPair);
        }
    }

    /* access modifiers changed from: package-private */
    public int getSafeTableSize() {
        List<short[]> list = this.fSafeTable;
        if (list == null) {
            return 0;
        }
        return ((list.size() * ((this.fSafeTable.get(0).length * 2) + 8)) + 16 + 7) & -8;
    }

    /* access modifiers changed from: package-private */
    public RBBIDataWrapper.RBBIStateTable exportSafeTable() {
        RBBIDataWrapper.RBBIStateTable rBBIStateTable = new RBBIDataWrapper.RBBIStateTable();
        rBBIStateTable.fNumStates = this.fSafeTable.size();
        int length = this.fSafeTable.get(0).length;
        int i = length + 4;
        rBBIStateTable.fTable = new short[((getSafeTableSize() - 16) / 2)];
        rBBIStateTable.fRowLen = i * 2;
        for (int i2 = 0; i2 < rBBIStateTable.fNumStates; i2++) {
            short[] sArr = this.fSafeTable.get(i2);
            int i3 = i2 * i;
            for (int i4 = 0; i4 < length; i4++) {
                rBBIStateTable.fTable[i3 + 4 + i4] = sArr[i4];
            }
        }
        return rBBIStateTable;
    }

    /* access modifiers changed from: package-private */
    public void printSet(Collection<RBBINode> collection) {
        for (RBBINode rBBINode : collection) {
            RBBINode.printInt(rBBINode.fSerialNum, 8);
        }
        System.out.println();
    }

    /* access modifiers changed from: package-private */
    public void printStates() {
        System.out.print("state |           i n p u t     s y m b o l s \n");
        System.out.print("      | Acc  LA    Tag");
        for (int i = 0; i < this.fRB.fSetBuilder.getNumCharCategories(); i++) {
            RBBINode.printInt(i, 3);
        }
        System.out.print("\n");
        System.out.print("      |---------------");
        for (int i2 = 0; i2 < this.fRB.fSetBuilder.getNumCharCategories(); i2++) {
            System.out.print("---");
        }
        System.out.print("\n");
        for (int i3 = 0; i3 < this.fDStates.size(); i3++) {
            RBBIStateDescriptor rBBIStateDescriptor = this.fDStates.get(i3);
            RBBINode.printInt(i3, 5);
            System.out.print(" | ");
            RBBINode.printInt(rBBIStateDescriptor.fAccepting, 3);
            RBBINode.printInt(rBBIStateDescriptor.fLookAhead, 4);
            RBBINode.printInt(rBBIStateDescriptor.fTagsIdx, 6);
            System.out.print(" ");
            for (int i4 = 0; i4 < this.fRB.fSetBuilder.getNumCharCategories(); i4++) {
                RBBINode.printInt(rBBIStateDescriptor.fDtran[i4], 3);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    /* access modifiers changed from: package-private */
    public void printReverseTable() {
        System.out.printf("    Safe Reverse Table \n", new Object[0]);
        List<short[]> list = this.fSafeTable;
        if (list == null) {
            System.out.printf("   --- nullptr ---\n", new Object[0]);
            return;
        }
        int length = list.get(0).length;
        System.out.printf("state |           i n p u t     s y m b o l s \n", new Object[0]);
        System.out.printf("      | Acc  LA    Tag", new Object[0]);
        for (int i = 0; i < length; i++) {
            System.out.printf(" %2d", Integer.valueOf(i));
        }
        System.out.printf("\n", new Object[0]);
        System.out.printf("      |---------------", new Object[0]);
        for (int i2 = 0; i2 < length; i2++) {
            System.out.printf("---", new Object[0]);
        }
        System.out.printf("\n", new Object[0]);
        for (int i3 = 0; i3 < this.fSafeTable.size(); i3++) {
            short[] sArr = this.fSafeTable.get(i3);
            System.out.printf("  %3d | ", Integer.valueOf(i3));
            System.out.printf("%3d %3d %5d ", 0, 0, 0);
            for (int i4 = 0; i4 < length; i4++) {
                System.out.printf(" %2d", Short.valueOf(sArr[i4]));
            }
            System.out.printf("\n", new Object[0]);
        }
        System.out.printf("\n\n", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public void printRuleStatusTable() {
        List<Integer> list = this.fRB.fRuleStatusVals;
        System.out.print("index |  tags \n");
        System.out.print("-------------------\n");
        int i = 0;
        while (i < list.size()) {
            int intValue = list.get(i).intValue() + i + 1;
            RBBINode.printInt(i, 7);
            while (true) {
                i++;
                if (i >= intValue) {
                    break;
                }
                RBBINode.printInt(list.get(i).intValue(), 7);
            }
            System.out.print("\n");
            i = intValue;
        }
        System.out.print("\n\n");
    }
}
