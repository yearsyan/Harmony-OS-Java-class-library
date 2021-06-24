package ohos.abilityshell;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.os.Parcel;
import java.util.HashMap;
import java.util.Map;
import ohos.aafwk.ability.DataAbilityOperation;
import ohos.aafwk.ability.DataAbilityResult;
import ohos.appexecfwk.utils.AppLog;
import ohos.data.dataability.ContentProviderConverter;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.UriConverter;
import ohos.utils.net.Uri;

public class AbilityContentProviderConverter {
    private static final int INVALID_OBJECT_FLAG = 0;
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private static final int VALID_OBJECT_FLAG = 1;

    private AbilityContentProviderConverter() {
    }

    public static DataAbilityResult contentProviderResultToDataAbilityResult(ContentProviderResult contentProviderResult) {
        if (contentProviderResult != null) {
            Parcel obtain = Parcel.obtain();
            contentProviderResult.writeToParcel(obtain, 0);
            obtain.setDataPosition(0);
            Uri uri = null;
            android.net.Uri uri2 = obtain.readInt() != 0 ? (android.net.Uri) android.net.Uri.CREATOR.createFromParcel(obtain) : null;
            Integer valueOf = obtain.readInt() != 0 ? Integer.valueOf(obtain.readInt()) : null;
            obtain.recycle();
            if (uri2 != null) {
                uri = UriConverter.convertToZidaneContentUri(uri2, "");
            }
            return new DataAbilityResult(uri, valueOf);
        }
        AppLog.e(SHELL_LABEL, "contentProviderToDataAbilityResult: contentProviderResult cannot be null.", new Object[0]);
        throw new IllegalArgumentException("contentProviderResult cannot be null.");
    }

    public static ContentProviderResult dataAbilityResultToContentProviderResult(DataAbilityResult dataAbilityResult) {
        if (dataAbilityResult != null) {
            Uri uri = dataAbilityResult.getUri();
            Integer count = dataAbilityResult.getCount();
            android.net.Uri convertToAndroidContentUri = uri != null ? UriConverter.convertToAndroidContentUri(uri) : null;
            Parcel obtain = Parcel.obtain();
            if (convertToAndroidContentUri != null) {
                obtain.writeInt(1);
                convertToAndroidContentUri.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            if (count != null) {
                obtain.writeInt(1);
                obtain.writeInt(count.intValue());
            } else {
                obtain.writeInt(0);
            }
            obtain.setDataPosition(0);
            ContentProviderResult contentProviderResult = new ContentProviderResult(obtain);
            obtain.recycle();
            return contentProviderResult;
        }
        AppLog.e(SHELL_LABEL, "dataAbilityToContentProviderResult: dataAbilityResult cannot be null.", new Object[0]);
        throw new IllegalArgumentException("dataAbilityResult cannot be null.");
    }

    public static ContentProviderOperation dataAbilityOperationToContentProviderOperation(DataAbilityOperation dataAbilityOperation) {
        if (dataAbilityOperation != null) {
            ValuesBucket valuesBucket = dataAbilityOperation.getValuesBucket();
            DataAbilityPredicates dataAbilityPredicates = dataAbilityOperation.getDataAbilityPredicates();
            ContentProviderOperation.Builder contentProviderBuilder = getContentProviderBuilder(dataAbilityOperation);
            if (valuesBucket != null) {
                contentProviderBuilder.withValues(ContentProviderConverter.valuesBucketToContentValues(valuesBucket));
            }
            if (dataAbilityPredicates != null) {
                contentProviderBuilder.withSelection(ContentProviderConverter.dataAbilityPredicatesToSelection(dataAbilityPredicates), ContentProviderConverter.dataAbilityPredicatesToSelectionArgs(dataAbilityPredicates));
            }
            Integer expectedCount = dataAbilityOperation.getExpectedCount();
            if (expectedCount != null) {
                contentProviderBuilder.withExpectedCount(expectedCount.intValue());
            }
            ValuesBucket valuesBucketReferences = dataAbilityOperation.getValuesBucketReferences();
            if (valuesBucketReferences != null) {
                contentProviderBuilder.withValueBackReferences(ContentProviderConverter.valuesBucketToContentValues(valuesBucketReferences));
            }
            Map<Integer, Integer> dataAbilityPredicatesBackReferences = dataAbilityOperation.getDataAbilityPredicatesBackReferences();
            if (dataAbilityPredicatesBackReferences != null) {
                for (Map.Entry<Integer, Integer> entry : dataAbilityPredicatesBackReferences.entrySet()) {
                    contentProviderBuilder.withSelectionBackReference(entry.getKey().intValue(), entry.getValue().intValue());
                }
            }
            contentProviderBuilder.withYieldAllowed(dataAbilityOperation.isInterruptionAllowed());
            return contentProviderBuilder.build();
        }
        AppLog.e(SHELL_LABEL, "dataAbilityToContentProviderOperation: dataAbilityOperation cannot be null.", new Object[0]);
        throw new IllegalArgumentException("dataAbilityOperation cannot be null.");
    }

    private static ContentProviderOperation.Builder getContentProviderBuilder(DataAbilityOperation dataAbilityOperation) {
        int type = dataAbilityOperation.getType();
        android.net.Uri convertToAndroidContentUri = UriConverter.convertToAndroidContentUri(dataAbilityOperation.getUri());
        if (type == 1) {
            return ContentProviderOperation.newInsert(convertToAndroidContentUri);
        }
        if (type == 2) {
            return ContentProviderOperation.newUpdate(convertToAndroidContentUri);
        }
        if (type == 3) {
            return ContentProviderOperation.newDelete(convertToAndroidContentUri);
        }
        if (type == 4) {
            return ContentProviderOperation.newAssertQuery(convertToAndroidContentUri);
        }
        AppLog.e(SHELL_LABEL, "dataAbilityOperationToContentProviderOperation: contentType is invalid.", new Object[0]);
        throw new IllegalArgumentException("contentType is invalid: " + type);
    }

    public static DataAbilityOperation contentProviderOperationToDataAbilityOperation(ContentProviderOperation contentProviderOperation) {
        if (contentProviderOperation != null) {
            Parcel obtain = Parcel.obtain();
            contentProviderOperation.writeToParcel(obtain, 0);
            obtain.setDataPosition(0);
            int readInt = obtain.readInt();
            android.net.Uri uri = (android.net.Uri) android.net.Uri.CREATOR.createFromParcel(obtain);
            HashMap hashMap = null;
            ContentValues contentValues = obtain.readInt() != 0 ? (ContentValues) ContentValues.CREATOR.createFromParcel(obtain) : null;
            String readString = obtain.readInt() != 0 ? obtain.readString() : null;
            String[] readStringArray = obtain.readInt() != 0 ? obtain.readStringArray() : null;
            Integer valueOf = obtain.readInt() != 0 ? Integer.valueOf(obtain.readInt()) : null;
            ContentValues contentValues2 = obtain.readInt() != 0 ? (ContentValues) ContentValues.CREATOR.createFromParcel(obtain) : null;
            if (obtain.readInt() != 0) {
                hashMap = new HashMap();
            }
            if (hashMap != null) {
                int readInt2 = obtain.readInt();
                for (int i = 0; i < readInt2; i++) {
                    hashMap.put(Integer.valueOf(obtain.readInt()), Integer.valueOf(obtain.readInt()));
                }
            }
            int readInt3 = obtain.readInt();
            obtain.recycle();
            DataAbilityOperation.Builder dataAbilityBuilder = getDataAbilityBuilder(readInt, uri);
            if (contentValues != null) {
                dataAbilityBuilder.withValuesBucket(ContentProviderConverter.contentValuesToValuesBucket(contentValues));
            }
            if (valueOf != null) {
                dataAbilityBuilder.withExpectedCount(valueOf.intValue());
            }
            setDataAbilityBuilder(dataAbilityBuilder, readStringArray, readString, contentValues2, hashMap);
            if (readInt3 != 0) {
                dataAbilityBuilder.withInterruptionAllowed(true);
            }
            return dataAbilityBuilder.build();
        }
        AppLog.e(SHELL_LABEL, "contentProviderOperationToDataAbilityOperation: contentProviderOperation cannot be null.", new Object[0]);
        throw new IllegalArgumentException("contentProviderOperation cannot be null.");
    }

    private static void setDataAbilityBuilder(DataAbilityOperation.Builder builder, String[] strArr, String str, ContentValues contentValues, Map<Integer, Integer> map) {
        if (str != null) {
            builder.withPredicates(ContentProviderConverter.selectionToDataAbilityPredicates(str, strArr));
        }
        if (contentValues != null) {
            builder.withValueBackReferences(ContentProviderConverter.contentValuesToValuesBucket(contentValues));
        }
        if (map != null) {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                builder.withPredicatesBackReference(entry.getKey().intValue(), entry.getValue().intValue());
            }
        }
    }

    private static DataAbilityOperation.Builder getDataAbilityBuilder(int i, android.net.Uri uri) {
        if (i == 1) {
            return DataAbilityOperation.newInsertBuilder(UriConverter.convertToZidaneContentUri(uri, ""));
        }
        if (i == 2) {
            return DataAbilityOperation.newUpdateBuilder(UriConverter.convertToZidaneContentUri(uri, ""));
        }
        if (i == 3) {
            return DataAbilityOperation.newDeleteBuilder(UriConverter.convertToZidaneContentUri(uri, ""));
        }
        if (i == 4) {
            return DataAbilityOperation.newAssertBuilder(UriConverter.convertToZidaneContentUri(uri, ""));
        }
        AppLog.e(SHELL_LABEL, "contentProviderOperationToDataAbilityOperation: type is invalid.", new Object[0]);
        throw new IllegalArgumentException("type is invalid: " + i);
    }
}
