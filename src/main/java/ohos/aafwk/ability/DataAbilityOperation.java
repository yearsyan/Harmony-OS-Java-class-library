package ohos.aafwk.ability;

import java.util.HashMap;
import java.util.Map;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.net.Uri;

public class DataAbilityOperation implements Sequenceable {
    private static final int MAX_UNMARSHALLING_SIZE = 3145728;
    public static final Sequenceable.Producer<DataAbilityOperation> PRODUCER = $$Lambda$DataAbilityOperation$37fR43AmUeA0Y4ziZYjHs0QOLPA.INSTANCE;
    public static final int TYPE_ASSERT = 4;
    public static final int TYPE_DELETE = 3;
    public static final int TYPE_INSERT = 1;
    public static final int TYPE_UPDATE = 2;
    private boolean interrupted;
    private DataAbilityPredicates mDataAbilityPredicates;
    private HashMap<Integer, Integer> mDataAbilityPredicatesBackReferences;
    private Integer mExpectedCount;
    private int mType;
    private Uri mUri;
    private ValuesBucket mValuesBucket;
    private ValuesBucket mValuesBucketReferences;

    private DataAbilityOperation(Builder builder) {
        this.mType = builder.mType;
        this.mUri = builder.mUri;
        this.mValuesBucket = builder.mValuesBucket;
        this.mExpectedCount = builder.mExpectedCount;
        this.mDataAbilityPredicates = builder.mDataAbilityPredicates;
        this.mValuesBucketReferences = builder.mValuesBucketReferences;
        this.mDataAbilityPredicatesBackReferences = builder.mDataAbilityPredicatesBackReferences;
        this.interrupted = builder.interrupted;
    }

    public DataAbilityOperation(DataAbilityOperation dataAbilityOperation, Uri uri) {
        this.mUri = uri;
        if (dataAbilityOperation != null) {
            this.mType = dataAbilityOperation.mType;
            this.mValuesBucket = dataAbilityOperation.mValuesBucket;
            this.mExpectedCount = dataAbilityOperation.mExpectedCount;
            this.mDataAbilityPredicates = dataAbilityOperation.mDataAbilityPredicates;
            this.mValuesBucketReferences = dataAbilityOperation.mValuesBucketReferences;
            this.mDataAbilityPredicatesBackReferences = dataAbilityOperation.mDataAbilityPredicatesBackReferences;
            this.interrupted = dataAbilityOperation.interrupted;
            return;
        }
        this.mType = 0;
        this.mExpectedCount = null;
        this.mValuesBucket = new ValuesBucket();
        this.mDataAbilityPredicates = new DataAbilityPredicates();
        this.mValuesBucketReferences = new ValuesBucket();
        this.mDataAbilityPredicatesBackReferences = new HashMap<>();
        this.interrupted = false;
    }

    public DataAbilityOperation(Parcel parcel) {
        int readInt;
        HashMap<Integer, Integer> hashMap = null;
        if (parcel != null) {
            this.mType = parcel.readInt();
            this.mUri = parcel.readInt() != 0 ? Uri.readFromParcel(parcel) : null;
            this.mValuesBucket = parcel.readInt() != 0 ? new ValuesBucket(parcel) : null;
            this.mExpectedCount = parcel.readInt() != 0 ? Integer.valueOf(parcel.readInt()) : null;
            this.mDataAbilityPredicates = parcel.readInt() != 0 ? new DataAbilityPredicates(parcel) : null;
            this.mValuesBucketReferences = parcel.readInt() != 0 ? new ValuesBucket(parcel) : null;
            this.mDataAbilityPredicatesBackReferences = parcel.readInt() != 0 ? new HashMap<>() : hashMap;
            if (this.mDataAbilityPredicatesBackReferences != null && (readInt = parcel.readInt()) > 0 && readInt < 3145728) {
                for (int i = 0; i < readInt; i++) {
                    this.mDataAbilityPredicatesBackReferences.put(Integer.valueOf(parcel.readInt()), Integer.valueOf(parcel.readInt()));
                }
            }
            this.interrupted = parcel.readBoolean();
            return;
        }
        this.mType = 0;
        this.mUri = null;
        this.mExpectedCount = null;
        this.mValuesBucket = new ValuesBucket();
        this.mDataAbilityPredicates = new DataAbilityPredicates();
        this.mValuesBucketReferences = new ValuesBucket();
        this.mDataAbilityPredicatesBackReferences = new HashMap<>();
        this.interrupted = false;
    }

    private DataAbilityOperation() {
        this.mType = 0;
        this.mUri = null;
        this.mExpectedCount = null;
        this.mValuesBucket = new ValuesBucket();
        this.mDataAbilityPredicates = new DataAbilityPredicates();
        this.mValuesBucketReferences = new ValuesBucket();
        this.mDataAbilityPredicatesBackReferences = new HashMap<>();
        this.interrupted = false;
    }

    static /* synthetic */ DataAbilityOperation lambda$static$0(Parcel parcel) {
        DataAbilityOperation dataAbilityOperation = new DataAbilityOperation();
        dataAbilityOperation.unmarshalling(parcel);
        return dataAbilityOperation;
    }

    public static Builder newInsertBuilder(Uri uri) {
        return new Builder(1, uri);
    }

    public static Builder newUpdateBuilder(Uri uri) {
        return new Builder(2, uri);
    }

    public static Builder newDeleteBuilder(Uri uri) {
        return new Builder(3, uri);
    }

    public static Builder newAssertBuilder(Uri uri) {
        return new Builder(4, uri);
    }

    public int getType() {
        return this.mType;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public ValuesBucket getValuesBucket() {
        return this.mValuesBucket;
    }

    public Integer getExpectedCount() {
        return this.mExpectedCount;
    }

    public DataAbilityPredicates getDataAbilityPredicates() {
        return this.mDataAbilityPredicates;
    }

    public ValuesBucket getValuesBucketReferences() {
        return this.mValuesBucketReferences;
    }

    public Map<Integer, Integer> getDataAbilityPredicatesBackReferences() {
        return this.mDataAbilityPredicatesBackReferences;
    }

    public boolean isInsertOperation() {
        return this.mType == 1;
    }

    public boolean isDeleteOperation() {
        return this.mType == 3;
    }

    public boolean isUpdateOperation() {
        return this.mType == 2;
    }

    public boolean isAssertOperation() {
        return this.mType == 4;
    }

    public boolean isInterruptionAllowed() {
        return this.interrupted;
    }

    public String toString() {
        return " (" + "mType: " + this.mType + ", mUri: " + this.mUri + ", mExpectedCount: " + this.mExpectedCount + ", mValuesBucket: " + this.mValuesBucket + ", mDataAbilityPredicates: " + this.mDataAbilityPredicates + ", mValuesBucketReferences: " + this.mValuesBucketReferences + ", mDataAbilityPredicatesBackReferences: " + this.mDataAbilityPredicatesBackReferences + ", interrupted: " + this.interrupted + ")";
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (!parcel.writeInt(this.mType)) {
            return false;
        }
        if (this.mUri != null) {
            if (!parcel.writeInt(1)) {
                return false;
            }
            parcel.writeSequenceable(this.mUri);
        } else if (!parcel.writeInt(0)) {
            return false;
        }
        if (this.mValuesBucket != null) {
            if (!parcel.writeInt(1) || !this.mValuesBucket.marshalling(parcel)) {
                return false;
            }
        } else if (!parcel.writeInt(0)) {
            return false;
        }
        if (this.mExpectedCount != null) {
            if (!parcel.writeInt(1) || !parcel.writeInt(this.mExpectedCount.intValue())) {
                return false;
            }
        } else if (!parcel.writeInt(0)) {
            return false;
        }
        if (this.mDataAbilityPredicates != null) {
            if (!parcel.writeInt(1) || !this.mDataAbilityPredicates.marshalling(parcel)) {
                return false;
            }
        } else if (!parcel.writeInt(0)) {
            return false;
        }
        if (this.mValuesBucketReferences != null) {
            if (!parcel.writeInt(1) || !this.mValuesBucketReferences.marshalling(parcel)) {
                return false;
            }
        } else if (!parcel.writeInt(0)) {
            return false;
        }
        HashMap<Integer, Integer> hashMap = this.mDataAbilityPredicatesBackReferences;
        if (hashMap != null) {
            int size = hashMap.size();
            if (parcel.writeInt(1) && parcel.writeInt(size)) {
                for (Map.Entry<Integer, Integer> entry : this.mDataAbilityPredicatesBackReferences.entrySet()) {
                    if (parcel.writeInt(entry.getKey().intValue())) {
                        if (!parcel.writeInt(entry.getValue().intValue())) {
                        }
                    }
                }
            }
            return false;
        } else if (!parcel.writeInt(0)) {
            return false;
        }
        return parcel.writeBoolean(this.interrupted);
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        this.mType = parcel.readInt();
        if (parcel.readInt() == 1) {
            this.mUri = Uri.readFromParcel(parcel);
        }
        if (parcel.readInt() == 1) {
            this.mValuesBucket = new ValuesBucket(parcel);
        }
        if (parcel.readInt() == 1) {
            this.mExpectedCount = Integer.valueOf(parcel.readInt());
        }
        if (parcel.readInt() == 1) {
            this.mDataAbilityPredicates = new DataAbilityPredicates(parcel);
        }
        if (parcel.readInt() == 1) {
            this.mValuesBucketReferences = new ValuesBucket(parcel);
        }
        if (parcel.readInt() == 1) {
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                this.mDataAbilityPredicatesBackReferences.put(Integer.valueOf(parcel.readInt()), Integer.valueOf(parcel.readInt()));
            }
        }
        boolean readBoolean = parcel.readBoolean();
        this.interrupted = readBoolean;
        return readBoolean;
    }

    public static DataAbilityOperation createFromParcel(Parcel parcel) {
        return new DataAbilityOperation(parcel);
    }

    public static class Builder {
        private boolean interrupted;
        private DataAbilityPredicates mDataAbilityPredicates;
        private HashMap<Integer, Integer> mDataAbilityPredicatesBackReferences;
        private Integer mExpectedCount;
        private final int mType;
        private final Uri mUri;
        private ValuesBucket mValuesBucket;
        private ValuesBucket mValuesBucketReferences;

        private Builder(int i, Uri uri) {
            if (uri != null) {
                this.mType = i;
                this.mUri = uri;
                return;
            }
            throw new IllegalArgumentException("uri must not be null");
        }

        public DataAbilityOperation build() {
            ValuesBucket valuesBucket;
            if (this.mType != 2 || ((valuesBucket = this.mValuesBucket) != null && !valuesBucket.isEmpty())) {
                return new DataAbilityOperation(this);
            }
            throw new IllegalArgumentException("Empty values");
        }

        public Builder withValuesBucket(ValuesBucket valuesBucket) {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 4) {
                if (this.mValuesBucket == null) {
                    this.mValuesBucket = new ValuesBucket();
                }
                this.mValuesBucket.putValues(valuesBucket);
                return this;
            }
            throw new IllegalArgumentException("only inserts, updates can have values");
        }

        public Builder withPredicates(DataAbilityPredicates dataAbilityPredicates) {
            int i = this.mType;
            if (i == 3 || i == 2 || i == 4) {
                this.mDataAbilityPredicates = dataAbilityPredicates;
                return this;
            }
            throw new IllegalArgumentException("only deletes and updates can have selections");
        }

        public Builder withExpectedCount(int i) {
            int i2 = this.mType;
            if (i2 == 2 || i2 == 3 || i2 == 4) {
                this.mExpectedCount = Integer.valueOf(i);
                return this;
            }
            throw new IllegalArgumentException("only updates, deletes can have expected counts");
        }

        public Builder withPredicatesBackReference(int i, int i2) {
            int i3 = this.mType;
            if (i3 == 2 || i3 == 3 || i3 == 4) {
                if (this.mDataAbilityPredicatesBackReferences == null) {
                    this.mDataAbilityPredicatesBackReferences = new HashMap<>();
                }
                this.mDataAbilityPredicatesBackReferences.put(Integer.valueOf(i), Integer.valueOf(i2));
                return this;
            }
            throw new IllegalArgumentException("only updates, deletes, and asserts can have select back-references");
        }

        public Builder withValueBackReferences(ValuesBucket valuesBucket) {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 4) {
                this.mValuesBucketReferences = valuesBucket;
                return this;
            }
            throw new IllegalArgumentException("only inserts, updates, and asserts can have value back-references");
        }

        public Builder withInterruptionAllowed(boolean z) {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 4 || i == 3) {
                this.interrupted = z;
                return this;
            }
            throw new IllegalArgumentException("only inserts, updates, delete, and asserts can have value back-references");
        }
    }
}
