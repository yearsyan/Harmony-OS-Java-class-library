package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class PlaceItem extends Schema<PlaceItem> {
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String DISTRICT = "district";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String POSTAL_CODE = "postalCode";
    public static final String PROVINCE = "province";
    public static final String ROAD = "road";

    public static List<IndexForm> getPlaceSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.PlaceItem.AnonymousClass1 */

            {
                add(new IndexForm(PlaceItem.POSTAL_CODE, IndexType.SORTED, false, true, true));
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

    public PlaceItem() {
        super.set(this);
    }

    public PlaceItem setLatitude(Double d) {
        super.put("latitude", d);
        return this;
    }

    public PlaceItem setLongitude(Double d) {
        super.put("longitude", d);
        return this;
    }

    public PlaceItem setCountry(String str) {
        super.put("country", str);
        return this;
    }

    public PlaceItem setProvince(String str) {
        super.put("province", str);
        return this;
    }

    public PlaceItem setCity(String str) {
        super.put("city", str);
        return this;
    }

    public PlaceItem setDistrict(String str) {
        super.put("district", str);
        return this;
    }

    public PlaceItem setRoad(String str) {
        super.put("road", str);
        return this;
    }

    public PlaceItem setPostalCode(Long l) {
        super.put(POSTAL_CODE, l);
        return this;
    }

    public Double getLatitude() {
        return super.getAsDouble("latitude");
    }

    public Double getLongitude() {
        return super.getAsDouble("longitude");
    }

    public Long getPostalCode() {
        return super.getAsLong(POSTAL_CODE);
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
}
