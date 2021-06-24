package ohos.agp.animation.styledsolutions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.animation.styledsolutions.GravitationalRankChain;
import ohos.agp.animation.timelinecurves.CubicBezierCurve;
import ohos.agp.animation.util.ValueKeyFrame;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ListContainer;
import ohos.agp.utils.Point;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

@SystemApi
public class GravitationalRankChain {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 218107648, "GravitationalRankChain");
    private static final String LOG_FORMAT = "%{public}s: %{public}s";
    private AnimatorGroup animatorGroup = new AnimatorGroup();
    private CubicBezierCurve cubicBezierCurveFrictionCurve = new CubicBezierCurve(0.2f, 0.0f, 0.2f, 1.0f);
    private CubicBezierCurve cubicBezierCurveReboundCurve = new CubicBezierCurve(0.33f, 3.0f, 0.5f, 1.0f);
    private CubicBezierCurve cubicBezierCurveSharpCurve = new CubicBezierCurve(0.33f, 0.0f, 0.67f, 1.0f);
    private ComponentInfo fromView = new ComponentInfo();
    private Map<Component, AnimatorInfo> mAnimators = new HashMap();
    private ComponentInfo tempView = new ComponentInfo();

    /* access modifiers changed from: private */
    public static class AnimatorInfo {
        AnimatorValue animatorValue;
        Point lastObjectivePoint;

        private AnimatorInfo(AnimatorValue animatorValue2, Point point) {
            this.animatorValue = animatorValue2;
            this.lastObjectivePoint = point;
        }
    }

    private ComponentInfo newComponentInfo(Component component, ComponentContainer componentContainer) {
        ComponentInfo componentInfo = new ComponentInfo();
        componentInfo.component = component;
        componentInfo.pointX = component.getContentPositionX();
        componentInfo.pointY = component.getContentPositionY();
        componentInfo.high = (float) component.getHeight();
        componentInfo.wide = (float) component.getWidth();
        componentInfo.distanceX = 0.0f;
        componentInfo.distanceY = 0.0f;
        if (componentContainer instanceof ListContainer) {
            componentInfo.index = ((ListContainer) componentContainer).getIndexForComponent(component);
        }
        componentInfo.childShrinkScaleFrom = 1.0f;
        componentInfo.childShrinkScaleto = 1.0f;
        return componentInfo;
    }

    /* access modifiers changed from: private */
    public static class ComponentInfo {
        private float childShrinkScaleFrom;
        private float childShrinkScaleto;
        private Component component;
        private float distanceX;
        private float distanceY;
        private float high;
        private int index;
        private float pointX;
        private float pointY;
        private float wide;

        static /* synthetic */ int access$812(ComponentInfo componentInfo, int i) {
            int i2 = componentInfo.index + i;
            componentInfo.index = i2;
            return i2;
        }

        static /* synthetic */ int access$820(ComponentInfo componentInfo, int i) {
            int i2 = componentInfo.index - i;
            componentInfo.index = i2;
            return i2;
        }

        private ComponentInfo() {
            this.pointX = 0.0f;
            this.pointY = 0.0f;
            this.distanceX = 0.0f;
            this.distanceY = 0.0f;
            this.high = 0.0f;
            this.wide = 0.0f;
            this.index = 0;
            this.childShrinkScaleFrom = 1.0f;
            this.childShrinkScaleto = 1.0f;
        }
    }

    private static class AnimatorMoveingEventType {
        private static final int CLICKDELMOVEDOWN = 5;
        private static final int CLICKDELMOVEUP = 4;
        private static final int MOVEDOWN = 3;
        private static final int MOVEUP = 2;
        private static final int ZOOMIN = 0;
        private static final int ZOOMOUT = 1;

        private AnimatorMoveingEventType() {
        }
    }

    /* access modifiers changed from: private */
    public static class AnimationParameters {
        private static int animatorValueCurvetype = 8;
        private static int animatorValueDelay = 0;
        private static int animatorValueLoopedCount = 0;
        private static long draggingOutstripsItemPaddingDurationTimes = 250;
        private static long endDraggingItemResetTimes = 200;
        private static long endDraggingReleaseItemZoomOutDurationTimes = 400;
        private static long removeItemOtherItemPaddingDurationTimes = 350;
        private static long removeItemScaleAlphaZoomDurationTimes = 300;
        private static long startDraggingItemZoomInDurationTimes = 150;
        private static float startDraggingItemZoomInscaleMultiple = 1.05f;

        private AnimationParameters() {
        }
    }

    private void setAnimatorValueDelay(int i) {
        if (i >= 0) {
            int unused = AnimationParameters.animatorValueDelay = i;
        }
    }

    private long getAnimationTimes(int i) {
        if (i == 0) {
            return AnimationParameters.startDraggingItemZoomInDurationTimes;
        }
        if (i == 1) {
            return AnimationParameters.endDraggingItemResetTimes;
        }
        if (i == 2) {
            return AnimationParameters.draggingOutstripsItemPaddingDurationTimes;
        }
        if (i == 3) {
            return AnimationParameters.draggingOutstripsItemPaddingDurationTimes;
        }
        if (i == 4) {
            return AnimationParameters.removeItemOtherItemPaddingDurationTimes;
        }
        if (i != 5) {
            return 0;
        }
        return AnimationParameters.removeItemOtherItemPaddingDurationTimes;
    }

    private Optional<AnimatorValue> driveItemMoveingto(ComponentInfo componentInfo, ComponentContainer componentContainer, int i) {
        Component component = componentInfo.component;
        if (component == null || !(componentContainer instanceof ListContainer)) {
            return Optional.empty();
        }
        Optional<AnimatorValue> animatorValueListening = setAnimatorValueListening(componentInfo, getAnimationTimes(i), this.cubicBezierCurveFrictionCurve, i);
        animatorValueListening.ifPresent(new Consumer(component, i) {
            /* class ohos.agp.animation.styledsolutions.$$Lambda$GravitationalRankChain$OuDTBggcBS27VsnkL9UoIcLJEn0 */
            private final /* synthetic */ Component f$1;
            private final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                GravitationalRankChain.this.lambda$driveItemMoveingto$0$GravitationalRankChain(this.f$1, this.f$2, (AnimatorValue) obj);
            }
        });
        return animatorValueListening;
    }

    public /* synthetic */ void lambda$driveItemMoveingto$0$GravitationalRankChain(final Component component, final int i, AnimatorValue animatorValue) {
        animatorValue.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalRankChain.AnonymousClass1 */

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onCancel(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onEnd(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onPause(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onResume(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStop(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStart(Animator animator) {
                GravitationalRankChain.this.tempView.pointY = component.getContentPositionY();
                GravitationalRankChain.this.tempView.pointX = component.getContentPositionX();
                if (i == 2) {
                    ComponentInfo.access$820(GravitationalRankChain.this.tempView, 1);
                }
                if (i == 3) {
                    ComponentInfo.access$812(GravitationalRankChain.this.tempView, 1);
                }
            }
        });
    }

    public void touchDraggingUpdateAnimation(Component component, Point point, ComponentContainer componentContainer) {
        float f;
        if (component != null && (componentContainer instanceof ListContainer)) {
            float height = (float) component.getHeight();
            float[] contentPosition = component.getContentPosition();
            ListContainer listContainer = (ListContainer) componentContainer;
            if (contentPosition[1] - point.getPointY() < 0.0f) {
                float f2 = height / 2.0f;
                f = (contentPosition[1] - point.getPointY()) + f2 <= 0.0f ? f2 * -1.0f : contentPosition[1] - point.getPointY();
            } else {
                float f3 = height / 2.0f;
                f = (contentPosition[1] - point.getPointY()) + f3 >= ((float) listContainer.getHeight()) ? ((float) listContainer.getHeight()) - f3 : contentPosition[1] - point.getPointY();
            }
            component.setContentPosition(contentPosition[0], f);
            int lastVisibleItemPosition = listContainer.getLastVisibleItemPosition();
            float marginBottom = height + ((float) component.getMarginBottom()) + ((float) component.getMarginTop());
            for (int firstVisibleItemPosition = listContainer.getFirstVisibleItemPosition(); firstVisibleItemPosition <= lastVisibleItemPosition; firstVisibleItemPosition++) {
                Component componentAt = listContainer.getComponentAt(firstVisibleItemPosition);
                if (!(componentAt == null || componentAt == component || listContainer.getIndexForComponent(componentAt) == -1)) {
                    if (!this.mAnimators.containsKey(componentAt)) {
                        this.mAnimators.put(componentAt, new AnimatorInfo(new AnimatorValue(), new Point()));
                    }
                    float[] contentPosition2 = componentAt.getContentPosition();
                    float[] contentPosition3 = component.getContentPosition();
                    setCoveredItemShrinkScale(componentAt, component, contentPosition3, contentPosition2);
                    if (contentPosition[1] > contentPosition2[1] && contentPosition3[1] <= contentPosition2[1]) {
                        matchItemUpOrDown(componentAt, componentContainer, marginBottom, 2);
                    } else if (contentPosition[1] < contentPosition2[1] && contentPosition3[1] >= contentPosition2[1]) {
                        matchItemUpOrDown(componentAt, componentContainer, marginBottom * -1.0f, 3);
                    }
                }
            }
        }
    }

    private void matchItemUpOrDown(Component component, ComponentContainer componentContainer, float f, int i) {
        if (component.getContentPositionY() >= 0.0f) {
            ComponentInfo newComponentInfo = newComponentInfo(component, componentContainer);
            newComponentInfo.distanceY = f;
            newComponentInfo.childShrinkScaleFrom = component.getScaleY();
            Optional<AnimatorValue> driveItemMoveingto = driveItemMoveingto(newComponentInfo, componentContainer, i);
            if (driveItemMoveingto.isPresent() && !driveItemMoveingto.get().isRunning()) {
                driveItemMoveingto.get().start();
            }
        }
    }

    private void setCoveredItemShrinkScale(Component component, Component component2, float[] fArr, float[] fArr2) {
        float f;
        double d;
        float height = (float) component.getHeight();
        float height2 = (float) component2.getHeight();
        if (this.mAnimators.containsKey(component) && !this.mAnimators.get(component).animatorValue.isRunning()) {
            if (fArr[1] >= fArr2[1] && fArr[1] <= fArr2[1] + height) {
                d = (double) ((fArr[1] - fArr2[1]) / height);
            } else if (fArr[1] + height2 < fArr2[1] || fArr[1] > fArr2[1]) {
                f = 1.0f;
                component.setScale(f, f);
            } else {
                d = (double) ((((fArr2[1] + height) - fArr[1]) - height2) / height);
            }
            f = (float) ((d * 0.05d) + 0.95d);
            component.setScale(f, f);
        }
    }

    private void setEndDragingItemRebounds(Component component, ComponentContainer componentContainer, AnimatorValue animatorValue) {
        setAnimatorValueDelay(150);
        AnimatorValue newAnimatorValue = newAnimatorValue(AnimationParameters.endDraggingReleaseItemZoomOutDurationTimes, null);
        setAnimatorValueDelay(0);
        newAnimatorValue.setCurve(this.cubicBezierCurveReboundCurve);
        newAnimatorValue.newValueContainer("name").setKeyFrame(ValueKeyFrame.createFloatFrame(0.0f, 1.05f), ValueKeyFrame.createFloatFrame(1.0f, 1.0f), ValueKeyFrame.createFloatFrame(1.6530043f, 0.95f));
        newAnimatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.$$Lambda$GravitationalRankChain$iMbqUx_P6F0YeOWGrWbdmMBcMB8 */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public final void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalRankChain.lambda$setEndDragingItemRebounds$1(Component.this, animatorValue, f);
            }
        });
        AnimatorGroup animatorGroup2 = new AnimatorGroup();
        animatorGroup2.runParallel(newAnimatorValue, animatorValue);
        setReboundsAnimatorsStateChangedListener(animatorGroup2, componentContainer);
        animatorGroup2.start();
    }

    static /* synthetic */ void lambda$setEndDragingItemRebounds$1(Component component, AnimatorValue animatorValue, float f) {
        float estimateValue = (float) animatorValue.estimateValue("name", f);
        component.setScale(estimateValue, estimateValue);
    }

    private void setReboundsAnimatorsStateChangedListener(AnimatorGroup animatorGroup2, final ComponentContainer componentContainer) {
        animatorGroup2.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalRankChain.AnonymousClass2 */

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onCancel(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onPause(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onResume(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStart(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStop(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onEnd(Animator animator) {
                BaseItemProvider itemProvider = ((ListContainer) componentContainer).getItemProvider();
                itemProvider.onItemMoved(GravitationalRankChain.this.fromView.index, GravitationalRankChain.this.tempView.index);
                itemProvider.notifyDataChanged();
                GravitationalRankChain.this.mAnimators.clear();
            }
        });
    }

    public void startDraggingItemZoomInAnimation(Component component, ComponentContainer componentContainer) {
        if (component != null && (componentContainer instanceof ListContainer)) {
            ComponentInfo newComponentInfo = newComponentInfo(component, componentContainer);
            newComponentInfo.childShrinkScaleto = AnimationParameters.startDraggingItemZoomInscaleMultiple;
            this.tempView.pointY = component.getContentPositionY();
            this.tempView.pointX = component.getContentPositionX();
            this.tempView.index = ((ListContainer) componentContainer).getIndexForComponent(component);
            this.fromView.index = this.tempView.index;
            animatorPropertyPrep(newComponentInfo, AnimationParameters.startDraggingItemZoomInDurationTimes, this.cubicBezierCurveFrictionCurve).ifPresent($$Lambda$P8PEtX8c5PZAnR4kHrPWT4n8_io.INSTANCE);
        }
    }

    public void endDragingItemZoomOutAnimation(Component component, ComponentContainer componentContainer) {
        if (component != null && (componentContainer instanceof ListContainer)) {
            ComponentInfo newComponentInfo = newComponentInfo(component, componentContainer);
            newComponentInfo.distanceX = this.tempView.pointX - component.getContentPositionX();
            newComponentInfo.distanceY = this.tempView.pointY - component.getContentPositionY();
            newComponentInfo.childShrinkScaleFrom = component.getScaleY();
            newComponentInfo.childShrinkScaleto = component.getScaleY();
            this.mAnimators.put(component, new AnimatorInfo(new AnimatorValue(), new Point()));
            driveItemMoveingto(newComponentInfo, componentContainer, 1).ifPresent(new Consumer(newComponentInfo, componentContainer) {
                /* class ohos.agp.animation.styledsolutions.$$Lambda$GravitationalRankChain$bPujydkk9FItzldiR0USuusoYG4 */
                private final /* synthetic */ GravitationalRankChain.ComponentInfo f$1;
                private final /* synthetic */ ComponentContainer f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    GravitationalRankChain.this.lambda$endDragingItemZoomOutAnimation$2$GravitationalRankChain(this.f$1, this.f$2, (AnimatorValue) obj);
                }
            });
            setLastOverwrittenItemScale(component, componentContainer);
        }
    }

    public /* synthetic */ void lambda$endDragingItemZoomOutAnimation$2$GravitationalRankChain(ComponentInfo componentInfo, ComponentContainer componentContainer, AnimatorValue animatorValue) {
        setEndDragingItemRebounds(componentInfo.component, componentContainer, animatorValue);
    }

    private AnimatorValue newAnimatorValue(long j, CubicBezierCurve cubicBezierCurve) {
        AnimatorValue animatorValue = new AnimatorValue();
        animatorValue.setDuration(j);
        animatorValue.setDelay((long) AnimationParameters.animatorValueDelay);
        animatorValue.setLoopedCount(AnimationParameters.animatorValueLoopedCount);
        if (cubicBezierCurve != null) {
            animatorValue.setCurve(cubicBezierCurve);
        }
        return animatorValue;
    }

    private void setLastOverwrittenItemScale(Component component, ComponentContainer componentContainer) {
        if (componentContainer instanceof ListContainer) {
            ListContainer listContainer = (ListContainer) componentContainer;
            int lastVisibleItemPosition = listContainer.getLastVisibleItemPosition();
            for (int firstVisibleItemPosition = listContainer.getFirstVisibleItemPosition(); firstVisibleItemPosition <= lastVisibleItemPosition; firstVisibleItemPosition++) {
                Component componentAt = listContainer.getComponentAt(firstVisibleItemPosition);
                if (!(componentAt == component || componentAt.getScaleY() == 1.0f || ((component.getContentPositionY() <= componentAt.getContentPositionY() || component.getContentPositionY() >= componentAt.getContentPositionY() + ((float) componentAt.getHeight())) && (component.getContentPositionY() >= componentAt.getContentPositionY() || component.getContentPositionY() + ((float) component.getHeight()) <= componentAt.getContentPositionY())))) {
                    animatorPropertyPrep(newComponentInfo(componentAt, null), AnimationParameters.startDraggingItemZoomInDurationTimes, this.cubicBezierCurveFrictionCurve).ifPresent($$Lambda$P8PEtX8c5PZAnR4kHrPWT4n8_io.INSTANCE);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public class DeleteResetInfo {
        boolean downFilling;
        float downwardDistance;
        boolean upFilling;
        float upwardDistance;

        private DeleteResetInfo() {
            this.upFilling = false;
            this.downFilling = false;
            this.upwardDistance = 0.0f;
            this.downwardDistance = 0.0f;
        }
    }

    private DeleteResetInfo matchDeletePadMovementDirection(Component component, ListContainer listContainer) {
        int count = listContainer.getItemProvider().getCount();
        int firstVisibleItemPosition = listContainer.getFirstVisibleItemPosition();
        int lastVisibleItemPosition = listContainer.getLastVisibleItemPosition();
        DeleteResetInfo deleteResetInfo = new DeleteResetInfo();
        float height = ((float) component.getHeight()) + ((float) component.getMarginTop()) + ((float) component.getMarginBottom());
        int i = lastVisibleItemPosition + 1;
        if (count > i) {
            deleteResetInfo.upFilling = true;
            deleteResetInfo.upwardDistance = height * -1.0f;
        } else if (count == i && firstVisibleItemPosition > 0) {
            Component componentAt = listContainer.getComponentAt(lastVisibleItemPosition);
            if (componentAt == null || componentAt.getContentPositionY() + ((float) componentAt.getHeight()) <= ((float) listContainer.getHeight())) {
                deleteResetInfo.downFilling = true;
                deleteResetInfo.downwardDistance = height;
            } else {
                deleteResetInfo.upFilling = true;
                deleteResetInfo.downFilling = true;
                if (((componentAt.getContentPositionY() + ((float) componentAt.getHeight())) + ((float) componentAt.getMarginBottom())) - ((float) listContainer.getHeight()) >= height) {
                    deleteResetInfo.upwardDistance = height * -1.0f;
                    deleteResetInfo.downwardDistance = 0.0f;
                } else {
                    deleteResetInfo.upwardDistance = (((componentAt.getContentPositionY() + ((float) componentAt.getHeight())) + ((float) componentAt.getMarginBottom())) - ((float) listContainer.getHeight())) * -1.0f;
                    deleteResetInfo.downwardDistance = height + deleteResetInfo.upwardDistance;
                }
            }
        } else if (count == i && firstVisibleItemPosition == 0) {
            deleteResetInfo.upFilling = true;
            deleteResetInfo.upwardDistance = height * -1.0f;
        } else {
            deleteResetInfo.upFilling = false;
            deleteResetInfo.downFilling = false;
        }
        return deleteResetInfo;
    }

    private List<AnimatorValue> getDelResetAnimatorList(Component component, int i, ComponentContainer componentContainer) {
        ListContainer listContainer = (ListContainer) componentContainer;
        int count = listContainer.getItemProvider().getCount();
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        if (count <= 0) {
            return arrayList;
        }
        DeleteResetInfo matchDeletePadMovementDirection = matchDeletePadMovementDirection(component, listContainer);
        int i2 = AnimationParameters.animatorValueDelay;
        int i3 = AnimationParameters.animatorValueDelay;
        int i4 = 2;
        char c = 1;
        char c2 = 0;
        if (matchDeletePadMovementDirection.downFilling && matchDeletePadMovementDirection.downwardDistance != 0.0f) {
            int i5 = i2;
            int i6 = i;
            while (true) {
                i6--;
                if (i6 < 0) {
                    break;
                }
                Component componentAt = listContainer.getComponentAt(i6);
                HiLogLabel hiLogLabel = LABEL_LOG;
                Object[] objArr = new Object[i4];
                objArr[c2] = "getDelResetAnimatorList get  upIndex=";
                objArr[c] = Integer.valueOf(i6);
                HiLog.info(hiLogLabel, LOG_FORMAT, objArr);
                if (componentAt == null) {
                    HiLogLabel hiLogLabel2 = LABEL_LOG;
                    Object[] objArr2 = new Object[i4];
                    objArr2[c2] = "getDelResetAnimatorList child == null  upIndex=";
                    objArr2[c] = Integer.valueOf(i6);
                    HiLog.info(hiLogLabel2, LOG_FORMAT, objArr2);
                    break;
                }
                i5 += 16;
                setAnimatorValueDelay(i5);
                this.mAnimators.put(componentAt, new AnimatorInfo(new AnimatorValue(), new Point()));
                ComponentInfo newComponentInfo = newComponentInfo(componentAt, componentContainer);
                newComponentInfo.distanceY = matchDeletePadMovementDirection.downwardDistance;
                driveItemMoveingto(newComponentInfo, componentContainer, 5).ifPresent(new Consumer(arrayList) {
                    /* class ohos.agp.animation.styledsolutions.$$Lambda$O0fgV9bO51zw90qeNGyVGfzOc */
                    private final /* synthetic */ List f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.add((AnimatorValue) obj);
                    }
                });
                i4 = 2;
                c = 1;
                c2 = 0;
            }
        }
        if (matchDeletePadMovementDirection.upFilling && matchDeletePadMovementDirection.upwardDistance != 0.0f) {
            int i7 = i;
            while (true) {
                i7++;
                if (i7 >= count) {
                    break;
                }
                Component componentAt2 = listContainer.getComponentAt(i7);
                HiLog.info(LABEL_LOG, LOG_FORMAT, "getDelResetAnimatorList get  downIndex=", Integer.valueOf(i7));
                if (componentAt2 == null) {
                    HiLog.info(LABEL_LOG, LOG_FORMAT, "getDelResetAnimatorList child == null  downIndex=", Integer.valueOf(i7));
                    break;
                }
                i3 += 16;
                setAnimatorValueDelay(i3);
                this.mAnimators.put(componentAt2, new AnimatorInfo(new AnimatorValue(), new Point()));
                ComponentInfo newComponentInfo2 = newComponentInfo(componentAt2, componentContainer);
                newComponentInfo2.distanceY = matchDeletePadMovementDirection.upwardDistance;
                driveItemMoveingto(newComponentInfo2, componentContainer, 4).ifPresent(new Consumer(arrayList) {
                    /* class ohos.agp.animation.styledsolutions.$$Lambda$O0fgV9bO51zw90qeNGyVGfzOc */
                    private final /* synthetic */ List f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.add((AnimatorValue) obj);
                    }
                });
            }
        }
        setAnimatorValueDelay(0);
        return arrayList;
    }

    private List<AnimatorValue> getDeleteItemScaleAnimatorList(Component component, ComponentContainer componentContainer, long j, CubicBezierCurve cubicBezierCurve) {
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        if (component instanceof ComponentContainer) {
            ComponentContainer componentContainer2 = (ComponentContainer) component;
            int childCount = componentContainer2.getChildCount();
            for (int i = 0; i < childCount; i++) {
                Component componentAt = componentContainer2.getComponentAt(i);
                if (componentAt != null) {
                    componentAt.setPivotX((componentAt.getContentPositionX() * -1.0f) + (((float) componentContainer.getWidth()) / 2.0f));
                    componentAt.setPivotY(Float.parseFloat(new DecimalFormat("0.00").format((long) (component.getHeight() / 2))));
                    arrayList.add(getDeleteItemScaleAnimator(componentAt, j, cubicBezierCurve));
                }
            }
        } else {
            arrayList.add(getDeleteItemScaleAnimator(component, j, cubicBezierCurve));
        }
        return arrayList;
    }

    private AnimatorValue getDeleteItemScaleAnimator(Component component, long j, CubicBezierCurve cubicBezierCurve) {
        AnimatorValue newAnimatorValue = newAnimatorValue(j, cubicBezierCurve);
        newAnimatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.$$Lambda$GravitationalRankChain$xwfkDyPv7QVEUDK5pLMkZMD5XSM */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public final void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalRankChain.lambda$getDeleteItemScaleAnimator$3(Component.this, animatorValue, f);
            }
        });
        return newAnimatorValue;
    }

    static /* synthetic */ void lambda$getDeleteItemScaleAnimator$3(Component component, AnimatorValue animatorValue, float f) {
        float f2 = 1.0f - (f * 0.25f);
        component.setScale(f2, f2);
    }

    private AnimatorValue getDeleteItemAlphaAnimator(Component component, long j, CubicBezierCurve cubicBezierCurve) {
        AnimatorValue newAnimatorValue = newAnimatorValue(j, cubicBezierCurve);
        newAnimatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            /* class ohos.agp.animation.styledsolutions.$$Lambda$GravitationalRankChain$c5YA3asoQNTOig1JGj8LmoCBlMc */

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public final void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalRankChain.lambda$getDeleteItemAlphaAnimator$4(Component.this, animatorValue, f);
            }
        });
        return newAnimatorValue;
    }

    private void setDeleteAnimatorGroupStateChangedListener(AnimatorGroup animatorGroup2, final Component component, final ComponentContainer componentContainer, final int i) {
        animatorGroup2.setStateChangedListener(new Animator.StateChangedListener() {
            /* class ohos.agp.animation.styledsolutions.GravitationalRankChain.AnonymousClass3 */

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onCancel(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onPause(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onResume(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStart(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onStop(Animator animator) {
            }

            @Override // ohos.agp.animation.Animator.StateChangedListener
            public void onEnd(Animator animator) {
                Component component = component;
                if (component instanceof ComponentContainer) {
                    int childCount = ((ComponentContainer) component).getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        Component componentAt = ((ComponentContainer) component).getComponentAt(i);
                        if (componentAt != null) {
                            componentAt.setScale(1.0f, 1.0f);
                            componentAt.setAlpha(1.0f);
                            componentAt.setPivotX(Float.parseFloat(new DecimalFormat("0.00").format((long) (componentAt.getWidth() / 2))));
                        }
                    }
                } else {
                    component.setScale(1.0f, 1.0f);
                    component.setAlpha(1.0f);
                }
                int indexForComponent = ((ListContainer) componentContainer).getIndexForComponent(component);
                BaseItemProvider itemProvider = ((ListContainer) componentContainer).getItemProvider();
                int i2 = i;
                if (i2 != 0) {
                    ((ListContainer) componentContainer).setExtraItemsNumber(i2);
                }
                GravitationalRankChain.this.mAnimators.clear();
                itemProvider.notifyDataSetItemRemoved(indexForComponent);
            }
        });
    }

    public boolean isAnimationRunning() {
        return this.animatorGroup.isRunning();
    }

    public boolean clickDeleteItemDone(Component component, ComponentContainer componentContainer) {
        int i;
        AnimatorValue next;
        if (this.animatorGroup.isRunning() || component == null || !(componentContainer instanceof ListContainer)) {
            return false;
        }
        this.animatorGroup.clear();
        ListContainer listContainer = (ListContainer) componentContainer;
        listContainer.moveChildToRear(component);
        if (listContainer.getExtraItemsNumber() >= 1) {
            i = 0;
        } else {
            i = listContainer.getExtraItemsNumber();
        }
        List<AnimatorValue> deleteItemScaleAnimatorList = getDeleteItemScaleAnimatorList(component, componentContainer, AnimationParameters.removeItemScaleAlphaZoomDurationTimes, this.cubicBezierCurveFrictionCurve);
        AnimatorValue deleteItemAlphaAnimator = getDeleteItemAlphaAnimator(component, AnimationParameters.removeItemScaleAlphaZoomDurationTimes, this.cubicBezierCurveSharpCurve);
        List<AnimatorValue> delResetAnimatorList = getDelResetAnimatorList(component, listContainer.getIndexForComponent(component), componentContainer);
        Animator[] animatorArr = new Animator[(delResetAnimatorList.size() + 2 + deleteItemScaleAnimatorList.size())];
        int i2 = 0;
        while (!delResetAnimatorList.isEmpty() && i2 < animatorArr.length && i2 < delResetAnimatorList.size()) {
            animatorArr[i2] = delResetAnimatorList.get(i2);
            i2++;
        }
        Iterator<AnimatorValue> it = deleteItemScaleAnimatorList.iterator();
        while (it.hasNext() && (next = it.next()) != null && i2 < animatorArr.length) {
            animatorArr[i2] = next;
            i2++;
        }
        if (i2 < animatorArr.length) {
            animatorArr[i2] = deleteItemAlphaAnimator;
        }
        HiLog.info(LABEL_LOG, LOG_FORMAT, "clickDeleteItemDone animatorArray.count:", Integer.valueOf(i2));
        this.animatorGroup.runParallel(animatorArr);
        setDeleteAnimatorGroupStateChangedListener(this.animatorGroup, component, componentContainer, i);
        this.animatorGroup.start();
        return true;
    }

    private Optional<AnimatorProperty> animatorPropertyPrep(ComponentInfo componentInfo, long j, CubicBezierCurve cubicBezierCurve) {
        Component component = componentInfo.component;
        if (this.mAnimators.containsKey(component) && this.mAnimators.get(component).animatorValue.isRunning()) {
            return Optional.empty();
        }
        AnimatorProperty animatorProperty = new AnimatorProperty();
        animatorProperty.scaleY(componentInfo.childShrinkScaleto);
        animatorProperty.scaleX(componentInfo.childShrinkScaleto);
        animatorProperty.setDuration(j);
        animatorProperty.setTarget(componentInfo.component);
        animatorProperty.setCurve(cubicBezierCurve);
        return Optional.of(animatorProperty);
    }

    private Point getResetAnimationObjectivePoint(ComponentInfo componentInfo, AnimatorValue animatorValue) {
        animatorValue.stop();
        componentInfo.pointX = componentInfo.component.getContentPositionX();
        componentInfo.pointY = componentInfo.component.getContentPositionY();
        float abs = Math.abs(componentInfo.distanceY);
        float pointY = this.mAnimators.get(componentInfo.component).lastObjectivePoint.getPointY();
        if (componentInfo.distanceY < 0.0f) {
            float f = pointY - abs;
            componentInfo.distanceY = f - componentInfo.pointY;
            componentInfo.distanceX = 0.0f;
            return new Point(0.0f, f);
        }
        float f2 = pointY + abs;
        componentInfo.distanceY = f2 - componentInfo.pointY;
        componentInfo.distanceX = 0.0f;
        return new Point(0.0f, f2);
    }

    private Optional<AnimatorValue> setAnimatorValueListening(ComponentInfo componentInfo, long j, CubicBezierCurve cubicBezierCurve, int i) {
        Point point;
        Component component = componentInfo.component;
        AnimatorValue animatorValue = this.mAnimators.get(component).animatorValue;
        if (animatorValue == null || (componentInfo.distanceY == 0.0f && componentInfo.distanceX == 0.0f && i != 1)) {
            return Optional.empty();
        }
        if (!animatorValue.isRunning()) {
            point = new Point(componentInfo.pointX + componentInfo.distanceX, componentInfo.pointY + componentInfo.distanceY);
        } else if ((componentInfo.distanceY < 0.0f && componentInfo.pointY > this.mAnimators.get(component).lastObjectivePoint.getPointY()) || (componentInfo.distanceY > 0.0f && componentInfo.pointY < this.mAnimators.get(component).lastObjectivePoint.getPointY())) {
            return Optional.empty();
        } else {
            point = getResetAnimationObjectivePoint(componentInfo, animatorValue);
        }
        this.mAnimators.put(component, new AnimatorInfo(animatorValue, point));
        animatorValue.setDuration(j);
        animatorValue.setDelay((long) AnimationParameters.animatorValueDelay);
        animatorValue.setLoopedCount(AnimationParameters.animatorValueLoopedCount);
        animatorValue.setCurve(cubicBezierCurve);
        animatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener(component, componentInfo.childShrinkScaleFrom, componentInfo.childShrinkScaleto) {
            /* class ohos.agp.animation.styledsolutions.$$Lambda$GravitationalRankChain$a49pYHmj0QSZbd_l1aSkLKjkLYA */
            private final /* synthetic */ Component f$1;
            private final /* synthetic */ float f$2;
            private final /* synthetic */ float f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // ohos.agp.animation.AnimatorValue.ValueUpdateListener
            public final void onUpdate(AnimatorValue animatorValue, float f) {
                GravitationalRankChain.lambda$setAnimatorValueListening$5(GravitationalRankChain.ComponentInfo.this, this.f$1, this.f$2, this.f$3, animatorValue, f);
            }
        });
        return Optional.of(animatorValue);
    }

    static /* synthetic */ void lambda$setAnimatorValueListening$5(ComponentInfo componentInfo, Component component, float f, float f2, AnimatorValue animatorValue, float f3) {
        if (!(componentInfo.distanceY == 0.0f && componentInfo.distanceX == 0.0f)) {
            component.setContentPositionY(componentInfo.pointY + (componentInfo.distanceY * f3));
            component.setContentPositionX(componentInfo.pointX + (componentInfo.distanceX * f3));
        }
        if (f != f2) {
            float f4 = f2 - ((f2 - f) * (1.0f - f3));
            component.setScaleX(f4);
            component.setScaleY(f4);
        }
    }
}
