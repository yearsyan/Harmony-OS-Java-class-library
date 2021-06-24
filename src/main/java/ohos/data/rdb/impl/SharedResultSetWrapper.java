package ohos.data.rdb.impl;

import ohos.data.resultset.ResultSet;
import ohos.data.resultset.ResultSetWrapper;
import ohos.data.resultset.SharedBlock;
import ohos.data.resultset.SharedResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class SharedResultSetWrapper extends ResultSetWrapper implements SharedResultSet {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109520, "SharedResultSetWrapper");
    private SharedBlock mSharedBlock;

    public SharedResultSetWrapper(ResultSet resultSet) {
        super(resultSet);
        if (resultSet instanceof StepResultSet) {
            HiLog.info(LABEL, "SharedResultSetWrapper: inputResultSet can't be StepResultSet, please use query to obtain a SharedResultSet.", new Object[0]);
            throw new IllegalArgumentException("inputResultSet can't be StepResultSet, please use query to obtain a SharedResultSet.");
        }
    }

    @Override // ohos.data.resultset.SharedResultSet
    public SharedBlock getBlock() {
        if (this.mResultSet instanceof SharedResultSet) {
            return ((SharedResultSet) this.mResultSet).getBlock();
        }
        HiLog.info(LABEL, "SharedResultSetWrapper: doesn't exist SharedBlock", new Object[0]);
        return this.mSharedBlock;
    }

    @Override // ohos.data.resultset.SharedResultSet
    public void fillBlock(int i, SharedBlock sharedBlock) {
        if (this.mResultSet instanceof SharedResultSet) {
            ((SharedResultSet) this.mResultSet).fillBlock(i, sharedBlock);
        } else {
            resultSetFillBlock(this.mResultSet, i, sharedBlock);
        }
    }

    @Override // ohos.data.resultset.SharedResultSet
    public boolean onGo(int i, int i2) {
        if (this.mResultSet instanceof SharedResultSet) {
            return ((SharedResultSet) this.mResultSet).onGo(i, i2);
        }
        SharedBlock sharedBlock = this.mSharedBlock;
        if (sharedBlock != null && i2 >= sharedBlock.getStartRowIndex() && i2 < this.mSharedBlock.getStartRowIndex() + this.mSharedBlock.getRowCount()) {
            return true;
        }
        SharedBlock sharedBlock2 = new SharedBlock("");
        fillBlock(i2, sharedBlock2);
        this.mSharedBlock = sharedBlock2;
        return true;
    }

    public static void resultSetFillBlock(ResultSet resultSet, int i, SharedBlock sharedBlock) {
        if (i < 0 || i >= resultSet.getRowCount()) {
            HiLog.info(LABEL, "resultSetFillBlock: position is invalid.", new Object[0]);
            return;
        }
        int rowIndex = resultSet.getRowIndex();
        int columnCount = resultSet.getColumnCount();
        sharedBlock.clear();
        sharedBlock.setStartRowIndex(i);
        sharedBlock.setColumnCount(columnCount);
        if (resultSet.goToRow(i)) {
            resultSetFillBlockInner(i, sharedBlock, resultSet, columnCount);
        }
        resultSet.goToRow(rowIndex);
    }

    private static void resultSetFillBlockInner(int i, SharedBlock sharedBlock, ResultSet resultSet, int i2) {
        do {
            if (!sharedBlock.allocateRow()) {
                HiLog.info(LABEL, "resultSetFillBlockInner: block allocateRow failed.", new Object[0]);
                return;
            }
            for (int i3 = 0; i3 < i2; i3++) {
                if (!fillBlockUnit(i, sharedBlock, resultSet, i3)) {
                    sharedBlock.freeLastRow();
                    return;
                }
            }
            i++;
        } while (resultSet.goToNextRow());
    }

    private static boolean fillBlockUnit(int i, SharedBlock sharedBlock, ResultSet resultSet, int i2) {
        ResultSet.ColumnType columnTypeForIndex = resultSet.getColumnTypeForIndex(i2);
        if (columnTypeForIndex == null) {
            String string = resultSet.getString(i2);
            if (string != null) {
                return sharedBlock.putString(string, i, i2);
            }
            return sharedBlock.putNull(i, i2);
        }
        int i3 = AnonymousClass1.$SwitchMap$ohos$data$resultset$ResultSet$ColumnType[columnTypeForIndex.ordinal()];
        if (i3 == 1) {
            return sharedBlock.putLong(resultSet.getLong(i2), i, i2);
        }
        if (i3 == 2) {
            return sharedBlock.putDouble(resultSet.getDouble(i2), i, i2);
        }
        if (i3 == 3) {
            byte[] blob = resultSet.getBlob(i2);
            if (blob != null) {
                return sharedBlock.putBlob(blob, i, i2);
            }
            return sharedBlock.putNull(i, i2);
        } else if (i3 != 4) {
            return sharedBlock.putNull(i, i2);
        } else {
            String string2 = resultSet.getString(i2);
            if (string2 != null) {
                return sharedBlock.putString(string2, i, i2);
            }
            return sharedBlock.putNull(i, i2);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.data.rdb.impl.SharedResultSetWrapper$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$data$resultset$ResultSet$ColumnType = new int[ResultSet.ColumnType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.data.resultset.ResultSet$ColumnType[] r0 = ohos.data.resultset.ResultSet.ColumnType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.data.rdb.impl.SharedResultSetWrapper.AnonymousClass1.$SwitchMap$ohos$data$resultset$ResultSet$ColumnType = r0
                int[] r0 = ohos.data.rdb.impl.SharedResultSetWrapper.AnonymousClass1.$SwitchMap$ohos$data$resultset$ResultSet$ColumnType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.data.resultset.ResultSet$ColumnType r1 = ohos.data.resultset.ResultSet.ColumnType.TYPE_INTEGER     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.data.rdb.impl.SharedResultSetWrapper.AnonymousClass1.$SwitchMap$ohos$data$resultset$ResultSet$ColumnType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.data.resultset.ResultSet$ColumnType r1 = ohos.data.resultset.ResultSet.ColumnType.TYPE_FLOAT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.data.rdb.impl.SharedResultSetWrapper.AnonymousClass1.$SwitchMap$ohos$data$resultset$ResultSet$ColumnType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.data.resultset.ResultSet$ColumnType r1 = ohos.data.resultset.ResultSet.ColumnType.TYPE_BLOB     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.data.rdb.impl.SharedResultSetWrapper.AnonymousClass1.$SwitchMap$ohos$data$resultset$ResultSet$ColumnType     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.data.resultset.ResultSet$ColumnType r1 = ohos.data.resultset.ResultSet.ColumnType.TYPE_STRING     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.data.rdb.impl.SharedResultSetWrapper.AnonymousClass1.<clinit>():void");
        }
    }

    @Override // ohos.data.resultset.ResultSetWrapper, ohos.data.resultset.ResultSet
    public void close() {
        super.close();
        SharedBlock sharedBlock = this.mSharedBlock;
        if (sharedBlock != null) {
            sharedBlock.close();
            this.mSharedBlock = null;
        }
    }
}
