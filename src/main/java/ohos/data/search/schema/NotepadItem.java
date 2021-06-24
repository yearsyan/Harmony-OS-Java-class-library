package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class NotepadItem extends Schema<NotepadItem> {
    public static final String ATTACHMENT = "attachment";
    public static final String CONTENT = "content";
    public static final String NOTEPAD_CATEGORY = "notepadCategory";

    public static List<IndexForm> getNotepadSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.NotepadItem.AnonymousClass1 */

            {
                add(new IndexForm(NotepadItem.NOTEPAD_CATEGORY, IndexType.SORTED, false, true, true));
                add(new IndexForm("attachment", IndexType.ANALYZED, false, false, true));
                add(new IndexForm("content", IndexType.ANALYZED, false, false, true));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public NotepadItem() {
        super.set(this);
    }

    public NotepadItem setNotepadCategory(String str) {
        super.put(NOTEPAD_CATEGORY, str);
        return this;
    }

    public NotepadItem setAttachment(String str) {
        super.put("attachment", str);
        return this;
    }

    public NotepadItem setContent(String str) {
        super.put("content", str);
        return this;
    }

    public String getNotepadCategory() {
        return super.getAsString(NOTEPAD_CATEGORY);
    }

    public String getAttachment() {
        return super.getAsString("attachment");
    }

    public String getContent() {
        return super.getAsString("content");
    }
}
