package ohos.agp.animation;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.animation.Animator;
import ohos.agp.animation.util.ValueKeyFrame;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.global.resource.solidxml.Node;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AnimatorValue extends Animator {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_AnimatorValue");
    protected ValueUpdateListener mUpdateListener = null;
    private final HashMap<String, ValueContainer> mValueContainers = new HashMap<>();

    /* access modifiers changed from: private */
    public enum ValueContainerType {
        NONE,
        VALUE_KEY_FRAME,
        FLOAT
    }

    public interface ValueUpdateListener {
        void onUpdate(AnimatorValue animatorValue, float f);
    }

    private native long nativeGetValueAnimatorHandle();

    private native void nativeSetUpdateListener(long j, ValueUpdateListener valueUpdateListener);

    public AnimatorValue() {
        this.mNativeAnimatorPtr = nativeGetValueAnimatorHandle();
        initAnimator(this.mNativeAnimatorPtr);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.animation.Animator
    public void parse(Node node, ResourceManager resourceManager) {
        super.parse(node, resourceManager);
        for (TypedAttribute typedAttribute : node.getTypedAttributes(resourceManager)) {
            if (typedAttribute == null) {
                HiLog.error(TAG, "typedAttribute is null", new Object[0]);
            } else {
                HiLog.debug(TAG, "read viewAnimator attr: %{public}s", typedAttribute.getName());
                try {
                    String name = typedAttribute.getName();
                    char c = 65535;
                    switch (name.hashCode()) {
                        case -2032139925:
                            if (name.equals("repeat_count")) {
                                c = 2;
                                break;
                            }
                            break;
                        case -1992012396:
                            if (name.equals("duration")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 95467907:
                            if (name.equals("delay")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 2096253127:
                            if (name.equals("interpolator")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    if (c == 0) {
                        setDelay((long) typedAttribute.getIntegerValue());
                    } else if (c == 1) {
                        setDuration((long) typedAttribute.getIntegerValue());
                    } else if (c == 2) {
                        setLoopedCount(typedAttribute.getIntegerValue());
                    } else if (c != 3) {
                        HiLog.debug(TAG, "do not support this tag: %{public}s", typedAttribute.getName());
                    } else {
                        setCurveType(typedAttribute.getIntegerValue());
                    }
                } catch (IOException | NotExistException | WrongTypeException e) {
                    throw new AnimatorScatterException("set " + typedAttribute.getName() + " failed", e);
                }
            }
        }
    }

    public void setDuration(long j) {
        setDurationInternal(j);
    }

    public void setLoopedCount(int i) {
        setLoopedCountInternal(i);
    }

    public void setDelay(long j) {
        setDelayInternal(j);
    }

    public void setCurveType(int i) {
        setCurveTypeInternal(i);
    }

    public void setCurve(Animator.TimelineCurve timelineCurve) {
        setCurveInternal(timelineCurve);
    }

    public void setStateChangedListener(Animator.StateChangedListener stateChangedListener) {
        setPauseListenerInternal(stateChangedListener);
        setStartListenerInternal(stateChangedListener);
    }

    public void setLoopedListener(Animator.LoopedListener loopedListener) {
        setLoopedListenerInternal(loopedListener);
    }

    public void setValueUpdateListener(ValueUpdateListener valueUpdateListener) {
        this.mUpdateListener = valueUpdateListener;
        nativeSetUpdateListener(this.mNativeAnimatorPtr, this.mUpdateListener);
    }

    public ValueContainer newValueContainer(String str) {
        if (this.mValueContainers.containsKey(str)) {
            return this.mValueContainers.get(str);
        }
        this.mValueContainers.put(str, new ValueContainer(null));
        return this.mValueContainers.get(str);
    }

    public double estimateValue(String str, float f) {
        if (this.mValueContainers.containsKey(str)) {
            return this.mValueContainers.get(str).estimate(f);
        }
        HiLog.error(TAG, "valueholder don't have the key", new Object[0]);
        return (double) f;
    }

    public static class ValueContainer {
        private float mEnd;
        private ValueKeyFrame[] mFrames;
        private float mStart;
        private ValueContainerType mType;

        /* synthetic */ ValueContainer(AnonymousClass1 r1) {
            this();
        }

        private ValueContainer() {
            this.mType = ValueContainerType.NONE;
        }

        public double estimate(float f) {
            int i = AnonymousClass1.$SwitchMap$ohos$agp$animation$AnimatorValue$ValueContainerType[this.mType.ordinal()];
            if (i == 1) {
                HiLog.error(AnimatorValue.TAG, "ValueHolder type is None", new Object[0]);
            } else if (i == 2) {
                return estimateKeyFrame(f);
            } else {
                if (i == 3) {
                    return estimateFloat(f);
                }
                HiLog.error(AnimatorValue.TAG, "ValueHolder type is Error", new Object[0]);
            }
            return (double) f;
        }

        public void setFloat(float f, float f2) {
            this.mType = ValueContainerType.FLOAT;
            this.mStart = f;
            this.mEnd = f2;
        }

        public void setKeyFrame(ValueKeyFrame... valueKeyFrameArr) {
            this.mType = ValueContainerType.VALUE_KEY_FRAME;
            this.mFrames = valueKeyFrameArr;
            Arrays.sort(this.mFrames);
        }

        private double estimateFloat(float f) {
            float f2 = this.mEnd;
            float f3 = this.mStart;
            return (double) (((f2 - f3) * f) + f3);
        }

        private double estimateKeyFrame(float f) {
            return search(f);
        }

        private double search(float f) {
            ValueKeyFrame[] valueKeyFrameArr = this.mFrames;
            if (valueKeyFrameArr.length <= 0) {
                return XPath.MATCH_SCORE_QNAME;
            }
            if (valueKeyFrameArr.length == 1 || Float.compare(f, valueKeyFrameArr[0].getProgress()) <= 0) {
                return this.mFrames[0].getKeyValue();
            }
            ValueKeyFrame[] valueKeyFrameArr2 = this.mFrames;
            if (Float.compare(f, valueKeyFrameArr2[valueKeyFrameArr2.length - 1].getProgress()) >= 0) {
                ValueKeyFrame[] valueKeyFrameArr3 = this.mFrames;
                return valueKeyFrameArr3[valueKeyFrameArr3.length - 1].getKeyValue();
            }
            int i = 0;
            while (true) {
                ValueKeyFrame[] valueKeyFrameArr4 = this.mFrames;
                if (i < valueKeyFrameArr4.length - 1) {
                    if (Float.compare(f, valueKeyFrameArr4[i].getProgress()) >= 0 && Float.compare(f, this.mFrames[i + 1].getProgress()) <= 0) {
                        break;
                    }
                    i++;
                } else {
                    i = 0;
                    break;
                }
            }
            int i2 = i + 1;
            float progress = this.mFrames[i].getProgress();
            double keyValue = this.mFrames[i].getKeyValue();
            float progress2 = this.mFrames[i2].getProgress();
            double keyValue2 = this.mFrames[i2].getKeyValue();
            return Float.compare(progress, progress2) == 0 ? keyValue2 : (((double) ((f - progress) / (progress2 - progress))) * (keyValue2 - keyValue)) + keyValue;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.agp.animation.AnimatorValue$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$animation$AnimatorValue$ValueContainerType = new int[ValueContainerType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.agp.animation.AnimatorValue$ValueContainerType[] r0 = ohos.agp.animation.AnimatorValue.ValueContainerType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.agp.animation.AnimatorValue.AnonymousClass1.$SwitchMap$ohos$agp$animation$AnimatorValue$ValueContainerType = r0
                int[] r0 = ohos.agp.animation.AnimatorValue.AnonymousClass1.$SwitchMap$ohos$agp$animation$AnimatorValue$ValueContainerType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.agp.animation.AnimatorValue$ValueContainerType r1 = ohos.agp.animation.AnimatorValue.ValueContainerType.NONE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.agp.animation.AnimatorValue.AnonymousClass1.$SwitchMap$ohos$agp$animation$AnimatorValue$ValueContainerType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.agp.animation.AnimatorValue$ValueContainerType r1 = ohos.agp.animation.AnimatorValue.ValueContainerType.VALUE_KEY_FRAME     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.agp.animation.AnimatorValue.AnonymousClass1.$SwitchMap$ohos$agp$animation$AnimatorValue$ValueContainerType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.agp.animation.AnimatorValue$ValueContainerType r1 = ohos.agp.animation.AnimatorValue.ValueContainerType.FLOAT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.animation.AnimatorValue.AnonymousClass1.<clinit>():void");
        }
    }
}
