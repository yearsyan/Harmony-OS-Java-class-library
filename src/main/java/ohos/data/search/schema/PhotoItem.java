package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class PhotoItem extends Schema<PhotoItem> {
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String DISTRICT = "district";
    public static final String FEATURE = "feature";
    public static final String HEIGHT = "height";
    public static final String HOLIDAY = "holiday";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String OCR_TEXT = "ocrText";
    public static final String PHOTO_CATEGORY = "photoCategory";
    public static final String PROVINCE = "province";
    public static final String ROAD = "road";
    public static final String SHOOTING_DATE = "shootingDate";
    public static final String SHOOTING_MODE = "shootingMode";
    public static final String SIZE = "size";
    public static final String TAG = "tag";
    public static final String WIDTH = "width";

    public static List<IndexForm> getPhotoSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.PhotoItem.AnonymousClass1 */

            {
                add(new IndexForm(PhotoItem.PHOTO_CATEGORY, IndexType.SORTED, false, true, true));
                add(new IndexForm(PhotoItem.OCR_TEXT, IndexType.ANALYZED, false, false, true));
                add(new IndexForm(PhotoItem.HOLIDAY, IndexType.SORTED, false, true, true));
                add(new IndexForm(PhotoItem.TAG, IndexType.SORTED, false, true, true));
                add(new IndexForm(PhotoItem.FEATURE, IndexType.SORTED, false, true, true));
                add(new IndexForm(PhotoItem.SHOOTING_DATE, IndexType.SORTED, false, true, true));
                add(new IndexForm(PhotoItem.SHOOTING_MODE, IndexType.SORTED, false, true, true));
                add(new IndexForm("size", IndexType.NO, false, true, false));
                add(new IndexForm("width", IndexType.NO, false, true, false));
                add(new IndexForm("height", IndexType.NO, false, true, false));
                add(new IndexForm("country", IndexType.SORTED, false, true, true));
                add(new IndexForm("province", IndexType.SORTED, false, true, true));
                add(new IndexForm("city", IndexType.SORTED, false, true, true));
                add(new IndexForm("district", IndexType.SORTED, false, true, true));
                add(new IndexForm("road", IndexType.ANALYZED, false, true, true));
                add(new IndexForm("latitude", "double", false, true, false));
                add(new IndexForm("longitude", "double", false, true, false));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public PhotoItem() {
        super.set(this);
    }

    public PhotoItem setPhotoCategory(String str) {
        super.put(PHOTO_CATEGORY, str);
        return this;
    }

    public PhotoItem setOcrText(String str) {
        super.put(OCR_TEXT, str);
        return this;
    }

    public PhotoItem setHoliday(String str) {
        super.put(HOLIDAY, str);
        return this;
    }

    public PhotoItem setTag(String str) {
        super.put(TAG, str);
        return this;
    }

    public PhotoItem setFeature(String str) {
        super.put(FEATURE, str);
        return this;
    }

    public PhotoItem setShootingDate(String str) {
        super.put(SHOOTING_DATE, str);
        return this;
    }

    public PhotoItem setShootingMode(String str) {
        super.put(SHOOTING_MODE, str);
        return this;
    }

    public PhotoItem setLongitude(Double d) {
        super.put("longitude", d);
        return this;
    }

    public PhotoItem setLatitude(Double d) {
        super.put("latitude", d);
        return this;
    }

    public PhotoItem setCountry(String str) {
        super.put("country", str);
        return this;
    }

    public PhotoItem setProvince(String str) {
        super.put("province", str);
        return this;
    }

    public PhotoItem setCity(String str) {
        super.put("city", str);
        return this;
    }

    public PhotoItem setDistrict(String str) {
        super.put("district", str);
        return this;
    }

    public PhotoItem setRoad(String str) {
        super.put("road", str);
        return this;
    }

    public PhotoItem setSize(Integer num) {
        super.put("size", num);
        return this;
    }

    public PhotoItem setHeight(Integer num) {
        super.put("height", num);
        return this;
    }

    public PhotoItem setWidth(Integer num) {
        super.put("width", num);
        return this;
    }

    public String getPhotoCategory() {
        return super.getAsString(PHOTO_CATEGORY);
    }

    public String getOcrText() {
        return super.getAsString(OCR_TEXT);
    }

    public String getHoliday() {
        return super.getAsString(HOLIDAY);
    }

    public String getTag() {
        return super.getAsString(TAG);
    }

    public String getFeature() {
        return super.getAsString(FEATURE);
    }

    public String getShootingDate() {
        return super.getAsString(SHOOTING_DATE);
    }

    public String getShootingMode() {
        return super.getAsString(SHOOTING_MODE);
    }

    public Double getLatitude() {
        return super.getAsDouble("latitude");
    }

    public Double getLongitude() {
        return super.getAsDouble("longitude");
    }

    public String getCountry() {
        return super.getAsString("country");
    }

    public String getProvince() {
        return super.getAsString("province");
    }

    public String getCity() {
        return super.getAsString("city");
    }

    public String getDistrict() {
        return super.getAsString("district");
    }

    public String getRoad() {
        return super.getAsString("road");
    }

    public Integer getSize() {
        return super.getAsInteger("size");
    }

    public Integer getHeight() {
        return super.getAsInteger("height");
    }

    public Integer getWidth() {
        return super.getAsInteger("width");
    }
}
