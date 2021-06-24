package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class NoticeItem extends Schema<NoticeItem> {
    public static final String CONTENT = "content";
    public static final String IMPORTANCE = "importance";
    public static final String LATITUDE = "latitude";
    public static final String LOCATION = "location";
    public static final String LONGITUDE = "longitude";
    public static final String NOTICE_CATEGORY = "noticeCategory";
    public static final String NOTICE_DATE = "noticeDate";
    public static final String REPEAT_STATUS = "repeatStatus";

    public static List<IndexForm> getNoticeSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.NoticeItem.AnonymousClass1 */

            {
                add(new IndexForm(NoticeItem.NOTICE_CATEGORY, IndexType.SORTED, false, true, true));
                add(new IndexForm("content", IndexType.ANALYZED, false, false, true));
                add(new IndexForm(NoticeItem.NOTICE_DATE, "long", false, true, false));
                add(new IndexForm(NoticeItem.REPEAT_STATUS, IndexType.SORTED, false, true, true));
                add(new IndexForm("importance", IndexType.SORTED, false, true, true));
                add(new IndexForm("location", IndexType.ANALYZED, false, true, true));
                add(new IndexForm("latitude", "double", false, true, false));
                add(new IndexForm("longitude", "double", false, true, false));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public NoticeItem() {
        super.set(this);
    }

    public NoticeItem setNoticeCategory(String str) {
        super.put(NOTICE_CATEGORY, str);
        return this;
    }

    public NoticeItem setContent(String str) {
        super.put("content", str);
        return this;
    }

    public NoticeItem setNoticeDate(Long l) {
        super.put(NOTICE_DATE, l);
        return this;
    }

    public NoticeItem setRepeatStatus(String str) {
        super.put(REPEAT_STATUS, str);
        return this;
    }

    public NoticeItem setImportance(String str) {
        super.put("importance", str);
        return this;
    }

    public NoticeItem setLocation(String str) {
        super.put("location", str);
        return this;
    }

    public NoticeItem setLongitude(Double d) {
        super.put("longitude", d);
        return this;
    }

    public NoticeItem setLatitude(Double d) {
        super.put("latitude", d);
        return this;
    }

    public String getNoticeCategory() {
        return super.getAsString(NOTICE_CATEGORY);
    }

    public String getContent() {
        return super.getAsString("content");
    }

    public Long getNoticeDate() {
        return super.getAsLong(NOTICE_DATE);
    }

    public String getRepeatStatus() {
        return super.getAsString(REPEAT_STATUS);
    }

    public String getImportance() {
        return super.getAsString("importance");
    }

    public String getLocation() {
        return super.getAsString("location");
    }

    public Double getLatitude() {
        return super.getAsDouble("latitude");
    }

    public Double getLongitude() {
        return super.getAsDouble("longitude");
    }
}
