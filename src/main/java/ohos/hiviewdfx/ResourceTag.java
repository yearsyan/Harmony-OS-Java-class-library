package ohos.hiviewdfx;

public class ResourceTag {
    private ResourceTagAdapter adapter = new ResourceTagAdapter();

    private ResourceTag() {
    }

    public static ResourceTag get() {
        return new ResourceTag();
    }

    public void markInUse(String str) {
        this.adapter.markInUse(str);
    }

    public void release() {
        this.adapter.release();
    }

    public void warnIfNeed() {
        this.adapter.warnIfNeed();
    }
}
