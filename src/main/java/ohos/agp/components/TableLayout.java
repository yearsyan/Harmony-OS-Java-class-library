package ohos.agp.components;

import ohos.agp.components.ComponentContainer;
import ohos.agp.components.GridLayout;
import ohos.app.Context;

public class TableLayout extends GridLayout {
    public static final int ALIGN_CONTENTS = 1;
    public static final int ALIGN_EDGES = 0;
    public static final int DEFAULT = Integer.MIN_VALUE;

    public interface Alignment {
        public static final int ALIGNMENT_BACK = 2;
        public static final int ALIGNMENT_BASELINE = 3;
        public static final int ALIGNMENT_BOTTOM = 2;
        public static final int ALIGNMENT_END = 2;
        public static final int ALIGNMENT_FILL = 4;
        public static final int ALIGNMENT_FRONT = 1;
        public static final int ALIGNMENT_START = 1;
        public static final int ALIGNMENT_TOP = 1;
        public static final int ALIGNMENT_UNDEFINED = 0;
        @Deprecated
        public static final int BASELINE = 3;
        @Deprecated
        public static final int BOTTOM = 2;
        @Deprecated
        public static final int END = 2;
        @Deprecated
        public static final int FILL = 4;
        @Deprecated
        public static final int LEADING = 1;
        @Deprecated
        public static final int START = 1;
        @Deprecated
        public static final int TOP = 1;
        @Deprecated
        public static final int TRAILING = 2;
        @Deprecated
        public static final int UNDEFINED_ALIGNMENT = 0;
    }

    public TableLayout(Context context) {
        super(context);
    }

    public TableLayout(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public TableLayout(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
    }

    @Override // ohos.agp.components.GridLayout
    public void setColumnCount(int i) {
        super.setColumnCount(i);
    }

    @Override // ohos.agp.components.GridLayout
    public int getColumnCount() {
        return super.getColumnCount();
    }

    @Override // ohos.agp.components.GridLayout
    public void setRowCount(int i) {
        super.setRowCount(i);
    }

    @Override // ohos.agp.components.GridLayout
    public int getRowCount() {
        return super.getRowCount();
    }

    @Override // ohos.agp.components.GridLayout
    public void setOrientation(int i) {
        super.setOrientation(i);
    }

    @Override // ohos.agp.components.GridLayout
    public int getOrientation() {
        return super.getOrientation();
    }

    @Override // ohos.agp.components.GridLayout
    public void setAlignmentType(int i) {
        super.setAlignmentType(i);
    }

    @Override // ohos.agp.components.GridLayout
    public int getAlignmentType() {
        return super.getAlignmentType();
    }

    @Override // ohos.agp.components.GridLayout
    public void setUseDefaultMargins(boolean z) {
        super.setUseDefaultMargins(z);
    }

    @Override // ohos.agp.components.GridLayout
    public boolean getUseDefaultMargins() {
        return super.getUseDefaultMargins();
    }

    public static Specification specification(float f) {
        return specification(Integer.MIN_VALUE, f);
    }

    public static Specification specification(int i) {
        return specification(i, 1);
    }

    public static Specification specification(int i, float f) {
        return specification(i, 1, f);
    }

    public static Specification specification(int i, int i2) {
        return specification(i, i2, 0);
    }

    public static Specification specification(int i, int i2, float f) {
        return specification(i, i2, 0, f);
    }

    public static Specification specification(int i, int i2, int i3) {
        return specification(i, i2, i3, 0.0f);
    }

    public static Specification specification(int i, int i2, int i3, float f) {
        return new Specification(i != Integer.MIN_VALUE, i, i2, i3, f);
    }

    public static class Specification {
        static final float DEFAULT_WEIGHT = 0.0f;
        static final Specification UNDEFINED = TableLayout.specification(Integer.MIN_VALUE);
        public final int alignment;
        public final CellSpan span;
        public final boolean startDefined;
        public final float weight;

        private Specification(boolean z, CellSpan cellSpan, int i, float f) {
            this.startDefined = z;
            this.span = cellSpan;
            this.alignment = i;
            this.weight = f;
        }

        private Specification(boolean z, int i, int i2, int i3, float f) {
            this(z, new CellSpan(i, i2 + i), i3, f);
        }

        public int getMinSpan() {
            return this.span.min;
        }

        public int getMaxSpan() {
            return this.span.max;
        }
    }

    @Override // ohos.agp.components.ComponentParent, ohos.agp.components.ComponentContainer, ohos.agp.components.GridLayout
    public ComponentContainer.LayoutConfig verifyLayoutConfig(ComponentContainer.LayoutConfig layoutConfig) {
        return super.verifyLayoutConfig(layoutConfig);
    }

    @Override // ohos.agp.components.ComponentContainer, ohos.agp.components.GridLayout
    public ComponentContainer.LayoutConfig createLayoutConfig(Context context, AttrSet attrSet) {
        return super.createLayoutConfig(context, attrSet);
    }

    public static class LayoutConfig extends GridLayout.LayoutParams {
        private static final int DEFAULT_HEIGHT = -2;
        private static final int DEFAULT_MARGIN = Integer.MIN_VALUE;
        private static final int DEFAULT_WIDTH = -2;
        public Specification columnSpec;
        public Specification rowSpec;

        private LayoutConfig(int i, int i2, int i3, int i4, int i5, int i6, Specification specification, Specification specification2) {
            super(i, i2);
            this.rowSpec = Specification.UNDEFINED;
            this.columnSpec = Specification.UNDEFINED;
            setMargins(i3, i4, i5, i6);
            this.rowSpec = specification;
            this.columnSpec = specification2;
        }

        public LayoutConfig() {
            this(Specification.UNDEFINED, Specification.UNDEFINED);
        }

        public LayoutConfig(Context context, AttrSet attrSet) {
            super(context, attrSet);
            this.rowSpec = Specification.UNDEFINED;
            this.columnSpec = Specification.UNDEFINED;
        }

        public LayoutConfig(Specification specification, Specification specification2) {
            this(-2, -2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, specification, specification2);
        }

        public LayoutConfig(int i, int i2) {
            this(i, i2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Specification.UNDEFINED, Specification.UNDEFINED);
        }

        public LayoutConfig(LayoutConfig layoutConfig) {
            super((GridLayout.LayoutParams) layoutConfig);
            this.rowSpec = Specification.UNDEFINED;
            this.columnSpec = Specification.UNDEFINED;
            this.rowSpec = layoutConfig.rowSpec;
            this.columnSpec = layoutConfig.columnSpec;
        }

        public LayoutConfig(ComponentContainer.LayoutConfig layoutConfig) {
            super(layoutConfig);
            this.rowSpec = Specification.UNDEFINED;
            this.columnSpec = Specification.UNDEFINED;
        }

        @Override // ohos.agp.components.ComponentContainer.LayoutConfig
        public void applyToComponent(Component component) {
            int[] iArr = new int[6];
            iArr[0] = this.width;
            iArr[1] = this.height;
            iArr[2] = isMarginsRelative() ? getHorizontalStartMargin() : getMarginLeft();
            iArr[3] = getMarginTop();
            iArr[4] = isMarginsRelative() ? getHorizontalEndMargin() : getMarginRight();
            iArr[5] = getMarginBottom();
            super.applyToView(component.getNativeViewPtr(), iArr, isMarginsRelative(), new int[]{this.columnSpec.startDefined ? 1 : 0, this.columnSpec.getMinSpan(), this.columnSpec.getMaxSpan(), this.columnSpec.alignment}, new int[]{this.rowSpec.startDefined ? 1 : 0, this.rowSpec.getMinSpan(), this.rowSpec.getMaxSpan(), this.rowSpec.alignment}, new float[]{this.columnSpec.weight, this.rowSpec.weight});
        }
    }

    public static final class CellSpan {
        public final int max;
        public final int min;

        public CellSpan(int i, int i2) {
            this.min = i;
            this.max = i2;
        }

        /* access modifiers changed from: protected */
        public int size() {
            return this.max - this.min;
        }
    }
}
