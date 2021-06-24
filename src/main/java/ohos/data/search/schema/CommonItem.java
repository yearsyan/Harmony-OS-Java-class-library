package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class CommonItem extends Schema<CommonItem> {
    public static final String ALTERNATE_NAME = "alternateName";
    public static final String CATEGORY = "category";
    public static final String DATE_CREATE = "dateCreate";
    public static final String DESCRIPTION = "description";
    public static final String IDENTIFIER = "identifier";
    public static final String KEYWORDS = "keywords";
    public static final String NAME = "name";
    public static final String POTENTIAL_ACTION = "potentialAction";
    public static final String RESERVED1 = "reserved1";
    public static final String RESERVED2 = "reserved2";
    public static final String SUBTITLE = "subtitle";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String TITLE = "title";
    public static final String URL = "url";

    public static List<IndexForm> getCommonSchema() {
        return new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.CommonItem.AnonymousClass1 */

            {
                add(new IndexForm(CommonItem.IDENTIFIER, IndexType.NO_ANALYZED, true, true, false));
                add(new IndexForm("title", IndexType.ANALYZED, false, true, true));
                add(new IndexForm("name", IndexType.ANALYZED, false, true, true));
                add(new IndexForm(CommonItem.CATEGORY, IndexType.SORTED, false, true, true));
                add(new IndexForm(CommonItem.DATE_CREATE, "long", false, true, false));
                add(new IndexForm(CommonItem.SUBTITLE, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(CommonItem.ALTERNATE_NAME, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(CommonItem.KEYWORDS, IndexType.ANALYZED, false, true, true));
                add(new IndexForm("description", IndexType.ANALYZED, false, true, true));
                add(new IndexForm(CommonItem.THUMBNAIL_URL, IndexType.NO, false, true, false));
                add(new IndexForm(CommonItem.POTENTIAL_ACTION, IndexType.NO, false, true, false));
                add(new IndexForm("url", IndexType.NO, false, true, false));
                add(new IndexForm(CommonItem.RESERVED1, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(CommonItem.RESERVED2, IndexType.NO_ANALYZED, false, true, false));
            }
        };
    }

    public CommonItem() {
        super.set(this);
    }
}
