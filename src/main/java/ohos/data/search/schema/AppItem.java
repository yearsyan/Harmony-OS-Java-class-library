package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class AppItem extends Schema<AppItem> {
    public static final String APP_CATEGORY = "appCategory";
    public static final String COMMENT = "comment";
    public static final String DOWNLOAD_COUNT = "downloadCount";
    public static final String RATING = "rating";
    public static final String SIZE = "size";

    public static List<IndexForm> getAppSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.AppItem.AnonymousClass1 */

            {
                add(new IndexForm("comment", IndexType.ANALYZED, false, false, true));
                add(new IndexForm(AppItem.APP_CATEGORY, IndexType.SORTED, false, true, true));
                add(new IndexForm("size", IndexType.NO, false, true, false));
                add(new IndexForm("downloadCount", "long", false, true, false));
                add(new IndexForm("rating", "double", false, true, false));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public AppItem() {
        super.set(this);
    }

    public AppItem setAppCategory(String str) {
        super.put(APP_CATEGORY, str);
        return this;
    }

    public AppItem setComment(String str) {
        super.put("comment", str);
        return this;
    }

    public AppItem setSize(Integer num) {
        super.put("size", num);
        return this;
    }

    public AppItem setRating(Double d) {
        super.put("rating", d);
        return this;
    }

    public AppItem setDownloadCount(Long l) {
        super.put("downloadCount", l);
        return this;
    }

    public String getAppCategory() {
        return super.getAsString(APP_CATEGORY);
    }

    public String getComment() {
        return super.getAsString("comment");
    }

    public Integer getSize() {
        return super.getAsInteger("size");
    }

    public Double getRating() {
        return super.getAsDouble("rating");
    }

    public Long getDownloadCount() {
        return super.getAsLong("downloadCount");
    }
}
