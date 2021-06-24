package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class DocumentItem extends Schema<DocumentItem> {
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String DOCUMENT_CATEGORY = "documentCategory";
    public static final String PATH = "path";
    public static final String SIZE = "size";

    public static List<IndexForm> getDocumentSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.DocumentItem.AnonymousClass1 */

            {
                add(new IndexForm(DocumentItem.AUTHOR, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(DocumentItem.DOCUMENT_CATEGORY, IndexType.SORTED, false, true, true));
                add(new IndexForm("size", IndexType.NO, false, true, false));
                add(new IndexForm("content", IndexType.ANALYZED, false, false, true));
                add(new IndexForm(DocumentItem.PATH, IndexType.NO, false, true, false));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public DocumentItem() {
        super.set(this);
    }

    public DocumentItem setAuthor(String str) {
        super.put(AUTHOR, str);
        return this;
    }

    public DocumentItem setDocumentCategory(String str) {
        super.put(DOCUMENT_CATEGORY, str);
        return this;
    }

    public DocumentItem setSize(Integer num) {
        super.put("size", num);
        return this;
    }

    public DocumentItem setContent(String str) {
        super.put("content", str);
        return this;
    }

    public DocumentItem setPath(String str) {
        super.put(PATH, str);
        return this;
    }

    public String getAuthor() {
        return super.getAsString(AUTHOR);
    }

    public String getDocumentCategory() {
        return super.getAsString(DOCUMENT_CATEGORY);
    }

    public Integer getSize() {
        return super.getAsInteger("size");
    }

    public String getContent() {
        return super.getAsString("content");
    }

    public String getPath() {
        return super.getAsString(PATH);
    }
}
