package ohos.agp.components;

@Deprecated
public abstract class RecycleItemProvider extends BaseItemProvider {
    @Deprecated
    public int getCacheSize() {
        return Integer.MAX_VALUE;
    }

    @Override // ohos.agp.components.BaseItemProvider
    @Deprecated
    public void onItemMoved(int i, int i2) {
    }

    @Deprecated
    public void setCacheSize(int i) {
    }
}
