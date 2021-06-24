package ohos.aafwk.ability;

import java.util.HashMap;
import java.util.Map;
import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.rpc.MessageParcel;
import ohos.utils.Ashmem;
import ohos.utils.Pair;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.zson.ZSONObject;

public class FormBindingData implements Sequenceable {
    private static final int IMAGE_DATA_STATE_ADDED = 1;
    private static final int IMAGE_DATA_STATE_NO_OPERATION = 0;
    private static final int IMAGE_DATA_STATE_REMOVED = -1;
    private static final LogLabel LABEL = LogLabel.create();
    public static final Sequenceable.Producer<FormBindingData> PRODUCER = $$Lambda$FormBindingData$vDWuSXSXpk4IkXWojSyKdNteC3U.INSTANCE;
    private ZSONObject formBindingData = new ZSONObject();
    private Map<String, Pair<Ashmem, Integer>> imageDataMap;
    private int imageDataState = 0;
    private Map<String, byte[]> rawImageBytesMap;

    static /* synthetic */ FormBindingData lambda$static$0(Parcel parcel) {
        FormBindingData formBindingData2 = new FormBindingData();
        formBindingData2.unmarshalling(parcel);
        return formBindingData2;
    }

    public FormBindingData() {
    }

    public FormBindingData(ZSONObject zSONObject) {
        if (zSONObject != null) {
            this.formBindingData = zSONObject;
        }
    }

    public FormBindingData(String str) {
        ZSONObject stringToZSON = ZSONObject.stringToZSON(str);
        if (stringToZSON != null) {
            this.formBindingData = stringToZSON;
        }
    }

    public void updateData(ZSONObject zSONObject) {
        if (zSONObject != null) {
            this.formBindingData = zSONObject;
        }
    }

    public String getDataString() {
        return ZSONObject.toZSONString(this.formBindingData);
    }

    public Map<String, Pair<Ashmem, Integer>> getImageDataMap() {
        return this.imageDataMap;
    }

    public int getImageDataState() {
        return this.imageDataState;
    }

    public void mergeData(ZSONObject zSONObject) {
        if (zSONObject != null) {
            if (this.formBindingData == null) {
                this.formBindingData = zSONObject;
                return;
            }
            for (Map.Entry<String, Object> entry : zSONObject.entrySet()) {
                this.formBindingData.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void setImageDataState(int i) {
        this.imageDataState = i;
    }

    public void setImageDataMap(Map<String, Pair<Ashmem, Integer>> map) {
        this.imageDataMap = map;
    }

    private boolean writeImageDataToParcel(Parcel parcel, String str, byte[] bArr) {
        Ashmem createAshmem = Ashmem.createAshmem(str, bArr.length);
        if (createAshmem == null) {
            Log.error(LABEL, "create shared memory fail", new Object[0]);
            return false;
        } else if (!createAshmem.mapReadAndWriteAShmem()) {
            Log.error(LABEL, "map shared memory fail", new Object[0]);
            return false;
        } else if (!createAshmem.writeToAShmem(bArr, bArr.length, 0)) {
            Log.error(LABEL, "write image data to shared memory fail", new Object[0]);
            return false;
        } else {
            createAshmem.unmapAShmem();
            boolean writeAshmem = ((MessageParcel) parcel).writeAshmem(createAshmem);
            createAshmem.closeAshmem();
            if (writeAshmem) {
                return true;
            }
            Log.error(LABEL, "writeAshmem fail, the picture name is %{public}s", str);
            return false;
        }
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        parcel.writeString(ZSONObject.toZSONString(this.formBindingData));
        if (!(parcel instanceof MessageParcel)) {
            return true;
        }
        parcel.writeInt(this.imageDataState);
        int i = this.imageDataState;
        if (!(i == -1 || i == 0)) {
            if (i != 1) {
                Log.warn(LABEL, "unexpected imageDataState %{public}d", Integer.valueOf(i));
            } else {
                Map<String, byte[]> map = this.rawImageBytesMap;
                if (map == null) {
                    Log.error(LABEL, "rawImageBytesMap is null", new Object[0]);
                    return false;
                }
                parcel.writeInt(map.size());
                for (Map.Entry<String, byte[]> entry : this.rawImageBytesMap.entrySet()) {
                    if (!writeImageDataToParcel(parcel, entry.getKey(), entry.getValue())) {
                        Log.error(LABEL, "writeImageDataToParcel fail, the picture name is %{public}s", entry.getKey());
                        return false;
                    }
                    parcel.writeInt(entry.getValue().length);
                    parcel.writeString(entry.getKey());
                }
            }
        }
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            return false;
        }
        this.formBindingData = ZSONObject.stringToZSON(parcel.readString());
        if (!(parcel instanceof MessageParcel)) {
            return true;
        }
        this.imageDataState = parcel.readInt();
        int i = this.imageDataState;
        if (!(i == -1 || i == 0)) {
            if (i != 1) {
                Log.warn(LABEL, "unexpected imageDataState %{public}d", Integer.valueOf(i));
            } else {
                int readInt = parcel.readInt();
                if (this.imageDataMap == null) {
                    this.imageDataMap = new HashMap();
                }
                for (int i2 = 0; i2 < readInt; i2++) {
                    Ashmem readAshmem = ((MessageParcel) parcel).readAshmem();
                    if (readAshmem == null) {
                        Log.error(LABEL, "ashmem is null", new Object[0]);
                        return false;
                    }
                    this.imageDataMap.put(parcel.readString(), Pair.create(readAshmem, Integer.valueOf(parcel.readInt())));
                }
            }
        }
        return true;
    }

    public void addImageData(String str, byte[] bArr) {
        if (bArr == null || str == null || bArr.length == 0) {
            Log.error(LABEL, "input param is null!", new Object[0]);
            return;
        }
        if (this.rawImageBytesMap == null) {
            this.rawImageBytesMap = new HashMap();
        }
        this.rawImageBytesMap.put(str, (byte[]) bArr.clone());
        this.imageDataState = 1;
    }

    public void removeImageData(String str) {
        this.rawImageBytesMap.remove(str);
    }
}
