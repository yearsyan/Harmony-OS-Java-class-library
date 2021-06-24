package ohos.aafwk.ability;

class FormResource {
    int eSystemPreviewLayoutId;
    String layoutIdConfig;
    int previewLayoutId;

    FormResource() {
    }

    /* access modifiers changed from: package-private */
    public boolean isValid() {
        String str;
        return (this.previewLayoutId > 0 && this.eSystemPreviewLayoutId > 0) || ((str = this.layoutIdConfig) != null && !str.isEmpty());
    }
}
