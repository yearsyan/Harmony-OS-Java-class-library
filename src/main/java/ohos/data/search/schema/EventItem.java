package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class EventItem extends Schema<EventItem> {
    public static final String END_DATE = "endDate";
    public static final String LATITUDE = "latitude";
    public static final String LOCATION = "location";
    public static final String LONGITUDE = "longitude";
    public static final String START_DATE = "startDate";

    public static List<IndexForm> getEventSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.EventItem.AnonymousClass1 */

            {
                add(new IndexForm(EventItem.START_DATE, "long", false, true, false));
                add(new IndexForm(EventItem.END_DATE, "long", false, true, false));
                add(new IndexForm("location", IndexType.ANALYZED, false, true, true));
                add(new IndexForm("latitude", "double", false, true, false));
                add(new IndexForm("longitude", "double", false, true, false));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public EventItem() {
        super.set(this);
    }

    public EventItem setStartDate(Long l) {
        super.put(START_DATE, l);
        return this;
    }

    public EventItem setEndDate(Long l) {
        super.put(END_DATE, l);
        return this;
    }

    public EventItem setLocation(String str) {
        super.put("location", str);
        return this;
    }

    public EventItem setLongitude(Double d) {
        super.put("longitude", d);
        return this;
    }

    public EventItem setLatitude(Double d) {
        super.put("latitude", d);
        return this;
    }

    public Long getStartDate() {
        return super.getAsLong(START_DATE);
    }

    public Long getEndDate() {
        return super.getAsLong(END_DATE);
    }

    public String getLocation() {
        return super.getAsString("location");
    }

    public Double getLongitude() {
        return super.getAsDouble("longitude");
    }

    public Double getLatitude() {
        return super.getAsDouble("latitude");
    }
}
