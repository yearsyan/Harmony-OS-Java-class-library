package ohos.agp.components;

import ohos.agp.database.DataSetPublisher;
import ohos.agp.database.DataSetSubscriber;
import ohos.agp.utils.MemoryCleaner;
import ohos.agp.utils.MemoryCleanerRegistry;
import ohos.annotation.SystemApi;

public abstract class BaseItemProvider {
    private static final int DEFAULT_COMPONENT_COUNT_NUM = 1;
    private static final int DEFAULT_COMPONENT_TYPE = 0;
    private TextFilter mFilter = null;
    protected long mNativeInstancePtr = 0;
    private final DataSetPublisher mPublisher = new DataSetPublisher();

    private native long nativeGetHandle();

    public abstract Component getComponent(int i, Component component, ComponentContainer componentContainer);

    public int getComponentTypeCount() {
        return 1;
    }

    public abstract int getCount();

    public abstract Object getItem(int i);

    public int getItemComponentType(int i) {
        return 0;
    }

    public abstract long getItemId(int i);

    @SystemApi
    public boolean isScaleInCentralScrollMode(int i) {
        return true;
    }

    @SystemApi
    public boolean isSnapInCentralScrollMode(int i) {
        return true;
    }

    public void onItemMoved(int i, int i2) {
    }

    public BaseItemProvider() {
        createNativePtr();
        registerCleaner();
    }

    public void notifyDataChanged() {
        this.mPublisher.notifyChanged();
    }

    public void notifyDataInvalidated() {
        this.mPublisher.informInvalidated();
    }

    public void notifyDataSetItemChanged(int i) {
        this.mPublisher.notifyItemChanged(i);
    }

    public void notifyDataSetItemInserted(int i) {
        this.mPublisher.notifyItemInserted(i);
    }

    public void notifyDataSetItemRemoved(int i) {
        this.mPublisher.notifyItemRemoved(i);
    }

    public void notifyDataSetItemRangeChanged(int i, int i2) {
        this.mPublisher.notifyItemRangeChanged(i, i2);
    }

    public void notifyDataSetItemRangeInserted(int i, int i2) {
        this.mPublisher.notifyItemRangeInserted(i, i2);
    }

    public void notifyDataSetItemRangeRemoved(int i, int i2) {
        this.mPublisher.notifyItemRangeRemoved(i, i2);
    }

    public final void addDataSubscriber(DataSetSubscriber dataSetSubscriber) {
        this.mPublisher.registerSubscriber(dataSetSubscriber);
    }

    public final void removeDataSubscriber(DataSetSubscriber dataSetSubscriber) {
        this.mPublisher.unregisterSubscriber(dataSetSubscriber);
    }

    private void removeDataSubscriber(long j) {
        this.mPublisher.unregisterSubscriber(j);
    }

    public void setFilter(TextFilter textFilter) {
        this.mFilter = textFilter;
    }

    public TextFilter getFilter() {
        return this.mFilter;
    }

    public void filter(CharSequence charSequence) {
        TextFilter textFilter = this.mFilter;
        if (textFilter != null) {
            textFilter.filter(charSequence);
        }
    }

    private void createNativePtr() {
        if (this.mNativeInstancePtr == 0) {
            this.mNativeInstancePtr = nativeGetHandle();
        }
    }

    /* access modifiers changed from: private */
    public static class Cleaner implements MemoryCleaner {
        protected long mNativePtr;

        private native void nativeRelease(long j);

        public Cleaner(long j) {
            this.mNativePtr = j;
        }

        @Override // ohos.agp.utils.MemoryCleaner
        public void run() {
            long j = this.mNativePtr;
            if (j != 0) {
                nativeRelease(j);
                this.mNativePtr = 0;
            }
        }
    }

    private void registerCleaner() {
        MemoryCleanerRegistry.getInstance().registerWithNativeBind(this, new Cleaner(this.mNativeInstancePtr), this.mNativeInstancePtr);
    }
}
