package ohos.agp.utils;

import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class MimeData implements Sequenceable {
    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        return false;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        return false;
    }
}
