package ohos.data.search.schema;

import ohos.data.search.model.IndexData;

public abstract class Schema<T> extends IndexData {
    private T item;

    /* access modifiers changed from: package-private */
    public void set(T t) {
        this.item = t;
    }

    public Long getDateCreate() {
        return super.getAsLong(CommonItem.DATE_CREATE);
    }

    public String getAlternateName() {
        return super.getAsString(CommonItem.ALTERNATE_NAME);
    }

    public String getCategory() {
        return super.getAsString(CommonItem.CATEGORY);
    }

    public String getDescription() {
        return super.getAsString("description");
    }

    public String getIdentifier() {
        return super.getAsString(CommonItem.IDENTIFIER);
    }

    public String getKeywords() {
        return super.getAsString(CommonItem.KEYWORDS);
    }

    public String getName() {
        return super.getAsString("name");
    }

    public String getPotentialAction() {
        return super.getAsString(CommonItem.POTENTIAL_ACTION);
    }

    public String getReserved1() {
        return super.getAsString(CommonItem.RESERVED1);
    }

    public String getReserved2() {
        return super.getAsString(CommonItem.RESERVED2);
    }

    public String getSubtitle() {
        return super.getAsString(CommonItem.SUBTITLE);
    }

    public String getThumbnailUrl() {
        return super.getAsString(CommonItem.THUMBNAIL_URL);
    }

    public String getTitle() {
        return super.getAsString("title");
    }

    public String getUrl() {
        return super.getAsString("url");
    }

    public T setAlternateName(String str) {
        super.put(CommonItem.ALTERNATE_NAME, str);
        return this.item;
    }

    public T setCategory(String str) {
        super.put(CommonItem.CATEGORY, str);
        return this.item;
    }

    public T setDateCreate(Long l) {
        super.put(CommonItem.DATE_CREATE, l);
        return this.item;
    }

    public T setDescription(String str) {
        super.put("description", str);
        return this.item;
    }

    public T setIdentifier(String str) {
        super.put(CommonItem.IDENTIFIER, str);
        return this.item;
    }

    public T setKeywords(String str) {
        super.put(CommonItem.KEYWORDS, str);
        return this.item;
    }

    public T setName(String str) {
        super.put("name", str);
        return this.item;
    }

    public T setPotentialAction(String str) {
        super.put(CommonItem.POTENTIAL_ACTION, str);
        return this.item;
    }

    public T setReserved1(String str) {
        super.put(CommonItem.RESERVED1, str);
        return this.item;
    }

    public T setReserved2(String str) {
        super.put(CommonItem.RESERVED2, str);
        return this.item;
    }

    public T setSubtitle(String str) {
        super.put(CommonItem.SUBTITLE, str);
        return this.item;
    }

    public T setThumbnailUrl(String str) {
        super.put(CommonItem.THUMBNAIL_URL, str);
        return this.item;
    }

    public T setTitle(String str) {
        super.put("title", str);
        return this.item;
    }

    public T setUrl(String str) {
        super.put("url", str);
        return this.item;
    }
}
