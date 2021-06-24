package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class ActionItem extends Schema<ActionItem> {
    public static final String ACTION_NAME = "actionName";

    public static List<IndexForm> getActionSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.ActionItem.AnonymousClass1 */

            {
                add(new IndexForm(ActionItem.ACTION_NAME, IndexType.ANALYZED, false, true, true));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public ActionItem() {
        super.set(this);
    }

    public ActionItem setActionName(String str) {
        super.put(ACTION_NAME, str);
        return this;
    }

    public String getActionName() {
        return super.getAsString(ACTION_NAME);
    }
}
