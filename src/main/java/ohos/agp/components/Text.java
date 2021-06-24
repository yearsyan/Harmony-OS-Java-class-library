package ohos.agp.components;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentObserverHandler;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.styles.Style;
import ohos.agp.styles.Value;
import ohos.agp.styles.attributes.TextAttrsConstants;
import ohos.agp.text.Font;
import ohos.agp.text.RichText;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.inputmethod.EditingCapability;
import ohos.miscservices.inputmethod.EditingText;
import ohos.miscservices.inputmethod.InputDataChannel;
import ohos.miscservices.inputmethod.InputMethodController;
import ohos.miscservices.pasteboard.PasteData;
import ohos.miscservices.pasteboard.SystemPasteboard;
import ohos.multimodalinput.event.KeyBoardEvent;
import ohos.multimodalinput.event.KeyEvent;

public class Text extends Component {
    public static final int AUTO_CURSOR_POSITION = -1;
    public static final int AUTO_SCROLLING_FOREVER = -1;
    private static final int EDITING_NOT_PARTIAL_CHANGE = -1;
    private static final int ELEMENT_BOTTOM = 3;
    private static final int ELEMENT_CURSOR_BUBBLE = 6;
    private static final int ELEMENT_END = 2;
    private static final int ELEMENT_INDEX_BOTTOM = 3;
    private static final int ELEMENT_INDEX_END = 5;
    private static final int ELEMENT_INDEX_LEFT = 0;
    private static final int ELEMENT_INDEX_RIGHT = 2;
    private static final int ELEMENT_INDEX_START = 4;
    private static final int ELEMENT_INDEX_TOP = 1;
    private static final int ELEMENT_SELECTION_LEFT_BUBBLE = 7;
    private static final int ELEMENT_SELECTION_RIGHT_BUBBLE = 8;
    private static final int ELEMENT_START = 0;
    private static final int ELEMENT_TOP = 1;
    private static final int MINIMUM_SOFT_KEYBOARD_HEIGHT = 100;
    private static final Map<String, BiConsumer<Text, Value>> STYLE_METHOD_MAP = new LinkedHashMap<String, BiConsumer<Text, Value>>() {
        /* class ohos.agp.components.Text.AnonymousClass1 */

        {
            put(TextAttrsConstants.ELEMENT_LEFT, $$Lambda$Text$1$NzBGHfqAPsEUBe1veGtwWMF3kH0.INSTANCE);
            put(TextAttrsConstants.ELEMENT_TOP, $$Lambda$Text$1$rgyTu795Fg8GNjihUQtkoKCJVQ.INSTANCE);
            put(TextAttrsConstants.ELEMENT_RIGHT, $$Lambda$Text$1$TnLcJ0kie0dzPXUvG7RXRu9xv6U.INSTANCE);
            put(TextAttrsConstants.ELEMENT_BOTTOM, $$Lambda$Text$1$4xPEyZgSYM1EOeOcL73q0EDHIQ0.INSTANCE);
            put(TextAttrsConstants.ELEMENT_START, $$Lambda$Text$1$w6BdDIb7RKAppOiSKWCf7Rr9Ug.INSTANCE);
            put(TextAttrsConstants.ELEMENT_END, $$Lambda$Text$1$naYD7sFzHxEX2vMuzzddx8AmsJE.INSTANCE);
            put(TextAttrsConstants.ELEMENT_CURSOR_BUBBLE, $$Lambda$Text$1$2ZQr5R6a5vEJlNdnvcokaJcEA0.INSTANCE);
            put(TextAttrsConstants.ELEMENT_SELECTION_LEFT_BUBBLE, $$Lambda$Text$1$_vakBvz84yE_PNTVDGM_FucMufo.INSTANCE);
            put(TextAttrsConstants.ELEMENT_SELECTION_RIGHT_BUBBLE, $$Lambda$Text$1$Wp6pgYoN69GCb5uYppgXgxWAR4.INSTANCE);
            put(TextAttrsConstants.TEXT_FONT, $$Lambda$Text$1$yW92LCguMkWWqcX6K4Gc71kYVg.INSTANCE);
            put(TextAttrsConstants.TEXT_WEIGHT, $$Lambda$Text$1$qWO7W0KNKKmTvwWz7jlcN2MS6fY.INSTANCE);
            put(TextAttrsConstants.ITALIC, $$Lambda$Text$1$6TVR3fzgOtMrcXg1FXtPA6Gpluw.INSTANCE);
            put(TextAttrsConstants.INPUT_ENTER_KEY_TYPE, $$Lambda$Text$1$V87CoYbheWo9_pdaz581RDDFrhg.INSTANCE);
            put(TextAttrsConstants.AUTO_SCROLLING_DURATION, $$Lambda$Text$1$QDCHcoNhk0Jh1D2gkjG6mRm7ZQ.INSTANCE);
            put(TextAttrsConstants.AUTO_SCROLLING_COUNT, $$Lambda$Text$1$I2d86JY8q__xunm616avwihAzg8.INSTANCE);
            put(TextAttrsConstants.TRUNCATION_MODE, $$Lambda$Text$1$W3P3_RjQ2EoE3J_zzQiwfEopIIU.INSTANCE);
            put(TextAttrsConstants.PADDING_FOR_TEXT, $$Lambda$Text$1$UQglRzRbocOsR7wvrL4lspdAmIs.INSTANCE);
        }

        static /* synthetic */ void lambda$new$0(Text text, Value value) {
            text.mAttrElements[0] = value.asElement();
        }

        static /* synthetic */ void lambda$new$1(Text text, Value value) {
            text.mAttrElements[1] = value.asElement();
        }

        static /* synthetic */ void lambda$new$2(Text text, Value value) {
            text.mAttrElements[2] = value.asElement();
        }

        static /* synthetic */ void lambda$new$3(Text text, Value value) {
            text.mAttrElements[3] = value.asElement();
        }

        static /* synthetic */ void lambda$new$4(Text text, Value value) {
            text.mAttrElements[4] = value.asElement();
        }

        static /* synthetic */ void lambda$new$5(Text text, Value value) {
            text.mAttrElements[5] = value.asElement();
        }

        static /* synthetic */ void lambda$new$6(Text text, Value value) {
            text.mAttrElements[6] = value.asElement();
        }

        static /* synthetic */ void lambda$new$7(Text text, Value value) {
            text.mAttrElements[7] = value.asElement();
        }

        static /* synthetic */ void lambda$new$8(Text text, Value value) {
            text.mAttrElements[8] = value.asElement();
        }

        static /* synthetic */ void lambda$new$15(Text text, Value value) {
            try {
                text.setTruncationMode(TruncationMode.valueOf(value.asString().toUpperCase(Locale.ENGLISH)));
            } catch (IllegalArgumentException unused) {
                HiLog.error(Text.TAG, "wrong parameter in truncation mode", new Object[0]);
            }
        }
    };
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_VIEW");
    private static final int TEXTCOLOR_CALLBACK_PARAM_NUM = 2;
    private static final int TEXTSIZE_CALLBACK_PARAM_NUM = 2;
    private boolean adjustInputPanel;
    private Element[] mAttrElements;
    private Element mCursorBubbleElement;
    private Element mCursorElement;
    private Component.LayoutRefreshedListener mDefaultLayoutRefreshedListener;
    private EditingCapability mEditingCapability;
    private int mEditingFlag;
    private int mEditingToken;
    private Element[] mElements;
    private boolean mElementsRelative;
    private Font mFont;
    private int mImeOption;
    private TextViewInputDataChannel mInputDataChannel;
    private Component.LayoutDirection mLastLayoutDirection;
    private EditingFunctonListener mOnEditingFunctionListener;
    private EditorActionListener mOnEditorActionListener;
    private RichText mRichText;
    private Element mSelectionLeftBubbleElement;
    private Element mSelectionRightBubbleElement;
    private final TextColorObserversHandler mTextColorObserversHandler;
    private final TextSizeObserversHandler mTextSizeObserversHandler;
    private final TextWatchersHandler mTextWatchersHandler;

    /* access modifiers changed from: private */
    public interface EditingFunctonListener {
        void onCopy(String str);

        void onPaste();
    }

    public interface EditorActionListener {
        boolean onTextEditorAction(int i);
    }

    public interface TextColorObserver extends ComponentObserverHandler.Observer {
        void onTextColorChanged(int i, int i2);
    }

    public interface TextObserver extends ComponentObserverHandler.Observer {
        void onTextUpdated(String str, int i, int i2, int i3);
    }

    public interface TextSizeObserver extends ComponentObserverHandler.Observer {
        void onTextSizeChanged(int i, int i2);
    }

    static /* synthetic */ boolean lambda$setFont$2(Font font) {
        return font != null;
    }

    private native void nativeAppend(long j, String str);

    private native void nativeDeleteText(long j, int i, boolean z, int i2);

    private native boolean nativeGetAutoFontSize(long j);

    private native int nativeGetBubbleHeight(long j);

    private native int nativeGetBubbleWidth(long j);

    private native int nativeGetCompoundDrawablesPadding(long j);

    private native int nativeGetCursorHeight(long j);

    private native int nativeGetCursorPositionOnScreenX(long j);

    private native int nativeGetCursorPositionOnScreenY(long j);

    private native String nativeGetEditableText(long j);

    private native int nativeGetEllipsize(long j);

    private native int nativeGetFadeEffectBoundaryWidth(long j);

    private native String nativeGetFontVariations(long j);

    private native String nativeGetHint(long j);

    private native int nativeGetHintTextColor(long j);

    private native float nativeGetLineSpacingExtra(long j);

    private native float nativeGetLineSpacingMultiplier(long j);

    private native int nativeGetMarqueeCount(long j);

    private native long nativeGetMarqueeDuration(long j);

    private native int nativeGetMaxHeight(long j);

    private native int nativeGetMaxLines(long j);

    private native int nativeGetMaxWidth(long j);

    private native boolean nativeGetPaddingForText(long j);

    private native int nativeGetSelectionColor(long j);

    private native int nativeGetSelectionEnd(long j);

    private native int nativeGetSelectionLeftBubbleHeight(long j);

    private native int nativeGetSelectionLeftBubbleWidth(long j);

    private native int nativeGetSelectionRightBubbleHeight(long j);

    private native int nativeGetSelectionRightBubbleWidth(long j);

    private native int nativeGetSelectionStart(long j);

    private native String nativeGetText(long j);

    private native int nativeGetTextAlignment(long j);

    private native int nativeGetTextColor(long j);

    private native int nativeGetTextInputType(long j);

    private native int nativeGetTextSize(long j);

    private native long nativeGetTextViewHandle();

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeInputDataChannelDeleteBackward(long j, int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeInputDataChannelDeleteForward(long j, int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native long nativeInputDataChannelGetHandle(long j);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native String nativeInputDataChannelGetTextAfterCursor(long j, int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native String nativeInputDataChannelGetTextBeforeCursor(long j, int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeInputDataChannelInsertText(long j, String str);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeInputDataChannelSendKeyFunction(long j, int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native boolean nativeInputDataChannelSendMenuFunction(long j, int i);

    private native void nativeInsertText(long j, String str, int i);

    private native boolean nativeIsCursorVisible(long j);

    private native boolean nativeIsEditable(long j);

    private native boolean nativeIsMultipleLine(long j);

    private native boolean nativeIsScrollable(long j);

    private native boolean nativeIsTextSelected(long j);

    private native int nativeLength(long j);

    private native void nativeSetAutoFontSize(long j, boolean z, boolean z2);

    private native void nativeSetAutoFontSizeConfiguration(long j, int i, int i2, int i3);

    private native void nativeSetAutoFontSizePreSet(long j, int[] iArr);

    private native void nativeSetBubbleSize(long j, int i, int i2);

    private native void nativeSetCompoundDrawables(long j, long[] jArr);

    private native void nativeSetCompoundDrawablesPadding(long j, int i);

    private native void nativeSetCompoundDrawablesRelative(long j, long[] jArr);

    private native void nativeSetCursorBubbleElement(long j, long j2);

    private native void nativeSetCursorVisible(long j, boolean z);

    private native void nativeSetEllipsize(long j, int i);

    private native void nativeSetFadeEffectBoundaryWidth(long j, int i);

    private native boolean nativeSetFontVariations(long j, String str);

    private native void nativeSetHint(long j, String str);

    private native void nativeSetHintTextColor(long j, int i);

    private native void nativeSetLeftBubbleSize(long j, int i, int i2);

    private native void nativeSetLineSpacing(long j, float f, float f2);

    private native void nativeSetMarqueeCount(long j, int i);

    private native void nativeSetMarqueeDuration(long j, long j2);

    private native void nativeSetMaxHeight(long j, int i);

    private native void nativeSetMaxLines(long j, int i);

    private native void nativeSetMaxWidth(long j, int i);

    private native void nativeSetMultipleLine(long j, boolean z);

    private native void nativeSetOnEditingFunctionCallback(long j, EditingFunctonListener editingFunctonListener);

    private native void nativeSetPaddingForText(long j, boolean z);

    private native void nativeSetRichText(long j, long j2);

    private native void nativeSetRightBubbleSize(long j, int i, int i2);

    private native void nativeSetScrollable(long j, boolean z);

    private native boolean nativeSetSelection(long j, int i, int i2);

    private native void nativeSetSelectionColor(long j, int i);

    private native void nativeSetSelectionLeftBubbleElement(long j, long j2);

    private native void nativeSetSelectionRightBubbleElement(long j, long j2);

    private native void nativeSetText(long j, String str);

    private native boolean nativeSetTextAlignment(long j, int i);

    private native void nativeSetTextColor(long j, int i);

    private native void nativeSetTextCursorDrawable(long j, long j2);

    private native void nativeSetTextInputType(long j, int i);

    private native void nativeSetTextSize(long j, int i);

    private native void nativeSetTextWatchersHandler(long j, TextWatchersHandler textWatchersHandler);

    private native void nativeSetTypeface(long j, long j2);

    private native void nativeStartMarquee(long j);

    private native void nativeStopMarquee(long j);

    private int px2fp(int i, float f, float f2) {
        return (int) ((((float) i) / (f * f2)) + 0.5f);
    }

    private int px2vp(int i, float f) {
        return (int) ((((float) i) / f) + 0.5f);
    }

    public /* synthetic */ void lambda$new$0$Text(Component component) {
        notifyCursorPositionAndEditingTextChange();
    }

    public enum TruncationMode {
        NONE(0),
        ELLIPSIS_AT_START(1),
        ELLIPSIS_AT_MIDDLE(2),
        ELLIPSIS_AT_END(3),
        AUTO_SCROLLING(4);
        
        private final int enumInt;

        private TruncationMode(int i) {
            this.enumInt = i;
        }

        public int value() {
            return this.enumInt;
        }

        public static TruncationMode getByInt(int i) {
            return (TruncationMode) Arrays.stream(values()).filter(new Predicate(i) {
                /* class ohos.agp.components.$$Lambda$Text$TruncationMode$bRMtM2MUyjYxbvDr6eB3B8HCI */
                private final /* synthetic */ int f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Text.TruncationMode.lambda$getByInt$0(this.f$0, (Text.TruncationMode) obj);
                }
            }).findAny().orElse(NONE);
        }

        static /* synthetic */ boolean lambda$getByInt$0(int i, TruncationMode truncationMode) {
            return truncationMode.value() == i;
        }
    }

    /* access modifiers changed from: private */
    public class TextViewInputDataChannel extends TextInputDataChannel {
        private static final int INVALID_INDEX = -1;
        private int deleteLength = 1;
        private int mMarkEnd = -1;
        private int mMarkStart = -1;
        private long mNativeInputDataChannelPtr = 0;
        private final Text mParent;

        TextViewInputDataChannel(Text text) {
            createNativePtr();
            this.mParent = text;
        }

        private void createNativePtr() {
            if (this.mNativeInputDataChannelPtr == 0) {
                Text text = Text.this;
                this.mNativeInputDataChannelPtr = text.nativeInputDataChannelGetHandle(text.mNativeViewPtr);
            }
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean selectText(int i, int i2) {
            return Text.this.setSelection(i, i2);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean insertText(String str) {
            if (isTextMarking()) {
                if (Text.this.getSelectionStart() != Text.this.getSelectionEnd()) {
                    Text text = Text.this;
                    int i = this.mMarkEnd;
                    text.setSelection(i, i);
                }
                deleteBackward(this.mMarkEnd - this.mMarkStart);
                unmarkText();
            }
            return Text.this.nativeInputDataChannelInsertText(this.mNativeInputDataChannelPtr, str);
        }

        private boolean isTextMarking() {
            return (this.mMarkStart == -1 && this.mMarkEnd == -1) ? false : true;
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean deleteBackward(int i) {
            return Text.this.nativeInputDataChannelDeleteBackward(this.mNativeInputDataChannelPtr, i);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean deleteForward(int i) {
            return Text.this.nativeInputDataChannelDeleteForward(this.mNativeInputDataChannelPtr, i);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public String getForward(int i) {
            return Text.this.nativeInputDataChannelGetTextBeforeCursor(this.mNativeInputDataChannelPtr, i);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public String getBackward(int i) {
            return Text.this.nativeInputDataChannelGetTextAfterCursor(this.mNativeInputDataChannelPtr, i);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean sendKeyEvent(KeyEvent keyEvent) {
            Text text = this.mParent;
            if (text == null) {
                return false;
            }
            if (text.mKeyEventListener == null) {
                return handleKeyEvent(keyEvent);
            }
            if (this.mParent.mKeyEventListener.onKeyEvent(this.mParent, keyEvent)) {
                return true;
            }
            return handleKeyEvent(keyEvent);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean subscribeCaretContext(int i) {
            float[] fArr = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
            InputMethodController instance = InputMethodController.getInstance();
            if (instance == null) {
                HiLog.error(Text.TAG, "subscribeCaretContext InputMethodController is null", new Object[0]);
                return false;
            }
            instance.notifyCursorCoordinateChanged((float) (Text.this.getCursorPositionOnScreenX() - 1), (float) (Text.this.getCursorPositionOnScreenY() + Text.this.getCursorHeight()), (float) Text.this.getCursorPositionOnScreenY(), fArr);
            return true;
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public EditingText subscribeEditingText(EditingCapability editingCapability) {
            EditingText editingText = new EditingText();
            editingText.setTextContent(this.mParent.getEditableString());
            editingText.setPrompt(this.mParent.getHint());
            editingText.setChangedStart(-1);
            editingText.setChangedEnd(-1);
            editingText.setFlags((!this.mParent.isMultipleLine()) | 0 | (this.mParent.isTextSelected() ? 2 : 0));
            editingText.setSelectionStart(this.mParent.getSelectionStart());
            editingText.setSelectionEnd(this.mParent.getSelectionEnd());
            return editingText;
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean sendKeyFunction(int i) {
            if (this.mParent.mOnEditorActionListener == null) {
                return Text.this.nativeInputDataChannelSendKeyFunction(this.mNativeInputDataChannelPtr, i);
            }
            InputMethodController instance = InputMethodController.getInstance();
            if (instance == null) {
                HiLog.error(Text.TAG, "sendKeyFunction InputMethodController is null", new Object[0]);
            } else {
                instance.stopInput(0);
            }
            return this.mParent.mOnEditorActionListener.onTextEditorAction(i);
        }

        private boolean handleKeyEvent(KeyEvent keyEvent) {
            if (keyEvent instanceof KeyBoardEvent) {
                return handleKeyBoardEvent((KeyBoardEvent) keyEvent);
            }
            return false;
        }

        private boolean handleKeyBoardEvent(KeyBoardEvent keyBoardEvent) {
            HiLog.debug(Text.TAG, "KeyBoardEvent is %{public}d %{public}d %{public}d", Integer.valueOf(keyBoardEvent.getKeyCode()), Long.valueOf(keyBoardEvent.getOccurredTime()), Long.valueOf(keyBoardEvent.getKeyDownDuration()));
            if (keyBoardEvent.getKeyCode() != 2055 || !keyBoardEvent.isKeyDown()) {
                return false;
            }
            HiLog.debug(Text.TAG, "delete length is %{public}d", Integer.valueOf(this.deleteLength));
            return deleteBackward(this.deleteLength);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean sendMenuFunction(int i) {
            return Text.this.nativeInputDataChannelSendMenuFunction(this.mNativeInputDataChannelPtr, i);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public String getSelectedText(int i) {
            int selectionStart;
            int selectionEnd;
            String editableString = Text.this.getEditableString();
            if (editableString == null || (selectionStart = Text.this.getSelectionStart()) == (selectionEnd = Text.this.getSelectionEnd()) || selectionStart < 0 || selectionEnd > editableString.length()) {
                return "";
            }
            if (selectionStart <= selectionEnd) {
                selectionStart = selectionEnd;
                selectionEnd = selectionStart;
            }
            return editableString.substring(selectionEnd, selectionStart);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean unmarkText() {
            this.mMarkStart = -1;
            this.mMarkEnd = -1;
            return true;
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public boolean replaceMarkedText(String str) {
            if (str == null || str.length() == 0) {
                HiLog.warn(Text.TAG, "replaceMarkedText, text is empty, unmark text.", new Object[0]);
                unmarkText();
                return true;
            }
            if (!isTextMarking()) {
                Text.this.insert("");
                this.mMarkStart = Text.this.getSelectionStart();
                this.mMarkEnd = this.mMarkStart;
            } else if (Text.this.getSelectionStart() != Text.this.getSelectionEnd()) {
                Text text = Text.this;
                int i = this.mMarkEnd;
                text.setSelection(i, i);
            }
            String editableString = Text.this.getEditableString();
            ensureMarkIndex(editableString.length());
            Text.this.setText(editableString.substring(0, this.mMarkStart) + str + editableString.substring(this.mMarkEnd));
            this.mMarkEnd = this.mMarkStart + str.length();
            Text text2 = Text.this;
            int i2 = this.mMarkEnd;
            text2.setSelection(i2, i2);
            return true;
        }

        private void ensureMarkIndex(int i) {
            int i2 = this.mMarkStart;
            int i3 = this.mMarkEnd;
            if (i2 > i3) {
                this.mMarkEnd = i2;
                this.mMarkStart = i3;
            }
            this.mMarkStart = Math.max(0, this.mMarkStart);
            this.mMarkStart = Math.min(i, this.mMarkStart);
            this.mMarkEnd = Math.max(0, this.mMarkEnd);
            this.mMarkEnd = Math.min(i, this.mMarkEnd);
        }

        @Override // ohos.miscservices.inputmethod.InputDataChannel, ohos.agp.components.TextInputDataChannel
        public void close() {
            unmarkText();
        }
    }

    private static class TextSizeObserversHandler extends ComponentObserverHandler<TextSizeObserver> {
        private TextSizeObserversHandler() {
        }

        @Override // ohos.agp.components.ComponentObserverHandler
        public void onChange(int[] iArr) {
            super.onChange(iArr);
            if (iArr.length != 2) {
                HiLog.error(Text.TAG, "Illegal return params, should be size %{public}d.", 2);
                return;
            }
            for (TextSizeObserver textSizeObserver : this.mObservers) {
                textSizeObserver.onTextSizeChanged(iArr[0], iArr[1]);
            }
        }
    }

    private static class TextColorObserversHandler extends ComponentObserverHandler<TextColorObserver> {
        private TextColorObserversHandler() {
        }

        @Override // ohos.agp.components.ComponentObserverHandler
        public void onChange(int[] iArr) {
            super.onChange(iArr);
            if (iArr.length != 2) {
                HiLog.error(Text.TAG, "Illegal return params, should be size %{public}d", 2);
                return;
            }
            for (TextColorObserver textColorObserver : this.mObservers) {
                textColorObserver.onTextColorChanged(iArr[0], iArr[1]);
            }
        }
    }

    public Text(Context context) {
        this(context, null);
    }

    public Text(Context context, AttrSet attrSet) {
        this(context, attrSet, "TextViewDefaultStyle");
    }

    public Text(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mTextColorObserversHandler = new TextColorObserversHandler();
        this.mTextSizeObserversHandler = new TextSizeObserversHandler();
        this.mEditingCapability = null;
        this.mEditingToken = 0;
        this.mEditingFlag = 0;
        this.mDefaultLayoutRefreshedListener = new Component.LayoutRefreshedListener() {
            /* class ohos.agp.components.$$Lambda$Text$t25mmvHTNfIM5XaXqmX3jlEjDBA */

            @Override // ohos.agp.components.Component.LayoutRefreshedListener
            public final void onRefreshed(Component component) {
                Text.this.lambda$new$0$Text(component);
            }
        };
        this.mTextWatchersHandler = new TextWatchersHandler();
        this.mElementsRelative = false;
        this.mRichText = null;
        this.mOnEditingFunctionListener = null;
        this.adjustInputPanel = false;
        this.mCursorElement = new ShapeElement();
        ((ShapeElement) this.mCursorElement).setRgbColor(RgbPalette.BLUE);
        setCursorElement(this.mCursorElement);
        this.mLastLayoutDirection = getLayoutDirectionResolved();
        setLayoutRefreshedListener(this.mDefaultLayoutRefreshedListener);
        setOnEditingFunctionCallback(new DefaultEditingFunctonListener());
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetTextViewHandle();
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getTextAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    @Override // ohos.agp.components.Component
    public void applyStyle(Style style) {
        super.applyStyle(style);
        applyStyleImplementation(style);
    }

    private void applyStyleImplementation(Style style) {
        init();
        for (Map.Entry<String, BiConsumer<Text, Value>> entry : STYLE_METHOD_MAP.entrySet()) {
            if (style.hasProperty(entry.getKey())) {
                entry.getValue().accept(this, style.getPropertyValue(entry.getKey()));
            }
        }
        applyCompoundElementsFromAttr();
    }

    private synchronized void init() {
        this.mAttrElements = new Element[]{null, null, null, null, null, null, null, null, null};
        if (this.mElements == null) {
            this.mElements = new Element[]{null, null, null, null};
        }
    }

    private void applyCompoundElementsFromAttr() {
        Element[] elementArr = this.mAttrElements;
        if (elementArr[4] != null || elementArr[5] != null) {
            Element[] elementArr2 = this.mAttrElements;
            setAroundElementsRelative(elementArr2[4], elementArr2[1], elementArr2[5], elementArr2[3]);
        } else if (!(elementArr[0] == null && elementArr[1] == null && elementArr[2] == null && elementArr[3] == null)) {
            Element[] elementArr3 = this.mAttrElements;
            setAroundElements(elementArr3[0], elementArr3[1], elementArr3[2], elementArr3[3]);
        }
        Element[] elementArr4 = this.mAttrElements;
        if (elementArr4[6] != null) {
            setBubbleElement(elementArr4[6]);
        }
        Element[] elementArr5 = this.mAttrElements;
        if (elementArr5[7] != null) {
            setSelectionLeftBubbleElement(elementArr5[7]);
        }
        Element[] elementArr6 = this.mAttrElements;
        if (elementArr6[8] != null) {
            setSelectionRightBubbleElement(elementArr6[8]);
        }
        Arrays.fill(this.mAttrElements, (Object) null);
    }

    @Override // ohos.agp.components.Component
    public void setLayoutRefreshedListener(Component.LayoutRefreshedListener layoutRefreshedListener) {
        if (layoutRefreshedListener == null) {
            HiLog.error(TAG, "setLayoutRefreshedListener listener is null", new Object[0]);
        } else {
            super.setLayoutRefreshedListener(new Component.LayoutRefreshedListener(layoutRefreshedListener) {
                /* class ohos.agp.components.$$Lambda$Text$7DDHcWO0p5jnk6tklo2ncjFm4 */
                private final /* synthetic */ Component.LayoutRefreshedListener f$1;

                {
                    this.f$1 = r2;
                }

                @Override // ohos.agp.components.Component.LayoutRefreshedListener
                public final void onRefreshed(Component component) {
                    Text.this.lambda$setLayoutRefreshedListener$1$Text(this.f$1, component);
                }
            });
        }
    }

    public /* synthetic */ void lambda$setLayoutRefreshedListener$1$Text(Component.LayoutRefreshedListener layoutRefreshedListener, Component component) {
        this.mDefaultLayoutRefreshedListener.onRefreshed(component);
        if (!layoutRefreshedListener.equals(this.mDefaultLayoutRefreshedListener)) {
            layoutRefreshedListener.onRefreshed(component);
        }
    }

    private static class TextWatchersHandler extends ComponentObserverHandler<TextObserver> {
        private TextWatchersHandler() {
        }

        /* access modifiers changed from: package-private */
        public void onTextChanged(String str, int i, int i2, int i3) {
            if (this.mObservers.size() == 0) {
                HiLog.error(Text.TAG, "mTextWatchers is null, or size is 0.", new Object[0]);
                return;
            }
            for (TextObserver textObserver : this.mObservers) {
                textObserver.onTextUpdated(str, i, i2, i3);
            }
        }

        @Override // ohos.agp.components.ComponentObserverHandler
        public void onChange(int[] iArr) {
            super.onChange(iArr);
        }
    }

    public void addTextObserver(TextObserver textObserver) {
        if (this.mTextWatchersHandler.getObserversCount() == 0) {
            nativeSetTextWatchersHandler(this.mNativeViewPtr, this.mTextWatchersHandler);
        }
        this.mTextWatchersHandler.addObserver(textObserver);
    }

    public void removeTextObserver(TextObserver textObserver) {
        this.mTextWatchersHandler.removeObserver(textObserver);
        if (this.mTextWatchersHandler.getObserversCount() == 0) {
            nativeSetTextWatchersHandler(this.mNativeViewPtr, null);
        }
    }

    public void setEditorActionListener(EditorActionListener editorActionListener) {
        this.mOnEditorActionListener = editorActionListener;
    }

    public void setFont(Font font) {
        validateParam(font, $$Lambda$Text$iHqDz36YbeETL3bxwOT8vKS4xM.INSTANCE, "The font must have valid value!");
        long nativeTypefacePtr = font.convertToTypeface().getNativeTypefacePtr();
        if (nativeTypefacePtr == 0) {
            HiLog.warn(TAG, "invalid typeface", new Object[0]);
        } else if (!font.convertToTypeface().equals(getFont().convertToTypeface())) {
            nativeSetTypeface(this.mNativeViewPtr, nativeTypefacePtr);
            this.mFont = font;
        }
    }

    public Font getFont() {
        if (this.mFont == null) {
            this.mFont = Font.DEFAULT;
        }
        return this.mFont;
    }

    public void setTruncationMode(TruncationMode truncationMode) {
        nativeSetEllipsize(this.mNativeViewPtr, truncationMode.value());
    }

    public TruncationMode getTruncationMode() {
        return TruncationMode.getByInt(nativeGetEllipsize(this.mNativeViewPtr));
    }

    public void setInputMethodOption(int i) {
        this.mImeOption = i;
    }

    public int getInputMethodOption() {
        return this.mImeOption;
    }

    static /* synthetic */ boolean lambda$setAutoFontSizeRule$3(Integer num) {
        return num.intValue() >= 0;
    }

    public void setAutoFontSizeRule(int i, int i2, int i3) {
        validateParam(Integer.valueOf(i2), $$Lambda$Text$RdiX4vpSWf4MkKXf26rXK8Bg.INSTANCE, "Max font size must be positive");
        validateParam(Integer.valueOf(i), new Predicate(i2) {
            /* class ohos.agp.components.$$Lambda$Text$vOlMTniLjSvCJusr5ykHbv0QMc */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Text.lambda$setAutoFontSizeRule$4(this.f$0, (Integer) obj);
            }
        }, "Min font size must be positive and cannot exceed the size of the max");
        validateParam(Integer.valueOf(i3), $$Lambda$Text$OzNohrJ62D_g0V3xqw7eyiYhfew.INSTANCE, "Step must be positive");
        nativeSetAutoFontSizeConfiguration(this.mNativeViewPtr, i, i2, i3);
    }

    static /* synthetic */ boolean lambda$setAutoFontSizeRule$4(int i, Integer num) {
        return num.intValue() >= 0 && num.intValue() <= i;
    }

    static /* synthetic */ boolean lambda$setAutoFontSizeRule$5(Integer num) {
        return num.intValue() > 0;
    }

    public void setAutoFontSizeRule(int[] iArr) {
        nativeSetAutoFontSizePreSet(this.mNativeViewPtr, iArr);
    }

    public boolean isAutoFontSize() {
        return nativeGetAutoFontSize(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setAutoScrollingDuration$6(Long l) {
        return l.longValue() >= 0;
    }

    public void setAutoScrollingDuration(long j) {
        validateParam(Long.valueOf(j), $$Lambda$Text$nPSuQrwyE6YXxWIiEElFmfqmOwE.INSTANCE, "The duration must be non negative");
        nativeSetMarqueeDuration(this.mNativeViewPtr, j);
    }

    public long getAutoScrollingDuration() {
        return nativeGetMarqueeDuration(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setAutoScrollingCount$7(int i, Integer num) {
        return num.intValue() >= 1 || i == -1;
    }

    public void setAutoScrollingCount(int i) {
        validateParam(Integer.valueOf(i), new Predicate(i) {
            /* class ohos.agp.components.$$Lambda$Text$gNxKzLvHFDCHGxwqJg16cO5bbfU */
            private final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Text.lambda$setAutoScrollingCount$7(this.f$0, (Integer) obj);
            }
        }, "The Auto Scrolling Count must be more than or equal to 1, or AUTO_SCROLLING_FOREVER");
        nativeSetMarqueeCount(this.mNativeViewPtr, i);
    }

    public int getAutoScrollingCount() {
        return nativeGetMarqueeCount(this.mNativeViewPtr);
    }

    public void startAutoScrolling() {
        nativeStartMarquee(this.mNativeViewPtr);
    }

    public void stopAutoScrolling() {
        nativeStopMarquee(this.mNativeViewPtr);
    }

    public void setCursorElement(Element element) {
        this.mCursorElement = element;
        nativeSetTextCursorDrawable(this.mNativeViewPtr, element.getNativeElementPtr());
    }

    public Element getCursorElement() {
        return this.mCursorElement;
    }

    public void setAroundElements(Element element, Element element2, Element element3, Element element4) {
        long j;
        long j2;
        long j3;
        Element element5 = isRtl() ? element3 : element;
        Element element6 = isRtl() ? element : element3;
        Element[] elementArr = this.mElements;
        System.arraycopy(new Element[]{element5, element2, element6, element4}, 0, elementArr, 0, elementArr.length);
        long j4 = 0;
        if (element == null) {
            j = 0;
        } else {
            j = element.getNativeElementPtr();
        }
        if (element2 == null) {
            j2 = 0;
        } else {
            j2 = element2.getNativeElementPtr();
        }
        if (element3 == null) {
            j3 = 0;
        } else {
            j3 = element3.getNativeElementPtr();
        }
        if (element4 != null) {
            j4 = element4.getNativeElementPtr();
        }
        nativeSetCompoundDrawables(this.mNativeViewPtr, new long[]{j, j2, j3, j4});
        if (element == null && element3 == null) {
            unsubscribeRtlPropertiesChangedCallback();
        } else {
            subscribeRtlPropertiesChangedCallback();
        }
        this.mElementsRelative = false;
    }

    public Element[] getAroundElements() {
        Element element = this.mElements[isRtl() ? (char) 2 : 0];
        Element[] elementArr = this.mElements;
        return new Element[]{element, elementArr[1], elementArr[isRtl() ? (char) 0 : 2], this.mElements[3]};
    }

    public Element getLeftElement() {
        return this.mElements[isRtl() ? (char) 2 : 0];
    }

    public Element getRightElement() {
        return this.mElements[isRtl() ? (char) 0 : 2];
    }

    public Element getTopElement() {
        return this.mElements[1];
    }

    public Element getBottonElement() {
        return this.mElements[3];
    }

    public Element getStartElement() {
        return this.mElements[0];
    }

    public Element getEndElement() {
        return this.mElements[2];
    }

    public void setAroundElementsRelative(Element element, Element element2, Element element3, Element element4) {
        long j;
        long j2;
        long j3;
        Element[] elementArr = this.mElements;
        System.arraycopy(new Element[]{element, element2, element3, element4}, 0, elementArr, 0, elementArr.length);
        long j4 = 0;
        if (element == null) {
            j = 0;
        } else {
            j = element.getNativeElementPtr();
        }
        if (element2 == null) {
            j2 = 0;
        } else {
            j2 = element2.getNativeElementPtr();
        }
        if (element3 == null) {
            j3 = 0;
        } else {
            j3 = element3.getNativeElementPtr();
        }
        if (element4 != null) {
            j4 = element4.getNativeElementPtr();
        }
        nativeSetCompoundDrawablesRelative(this.mNativeViewPtr, new long[]{j, j2, j3, j4});
        unsubscribeRtlPropertiesChangedCallback();
        this.mElementsRelative = true;
    }

    public Element[] getAroundElementsRelative() {
        return (Element[]) this.mElements.clone();
    }

    static /* synthetic */ boolean lambda$setAroundElementsPadding$8(Integer num) {
        return num.intValue() >= 0;
    }

    public void setAroundElementsPadding(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$Text$D_NCi3XE_qzRGbUYLDJ8GfW5PzM.INSTANCE, "Padding values should be non negative.");
        nativeSetCompoundDrawablesPadding(this.mNativeViewPtr, i);
    }

    public int getAroundElementsPadding() {
        return nativeGetCompoundDrawablesPadding(this.mNativeViewPtr);
    }

    public void setTextInputType(int i) {
        HiLog.debug(TAG, "TextView setTextInputType called, inputType=%{public}d", Integer.valueOf(i));
        nativeSetTextInputType(this.mNativeViewPtr, i);
    }

    public int getTextInputType() {
        int nativeGetTextInputType = nativeGetTextInputType(this.mNativeViewPtr);
        HiLog.debug(TAG, "TextView getTextInputType called, inputType=%{public}d", Integer.valueOf(nativeGetTextInputType));
        return nativeGetTextInputType;
    }

    public void setRichText(RichText richText) {
        this.mRichText = richText;
        if (this.mRichText != null) {
            nativeSetRichText(this.mNativeViewPtr, this.mRichText.getNativeRichText());
        } else {
            nativeSetRichText(this.mNativeViewPtr, 0);
        }
    }

    public RichText getRichText() {
        return this.mRichText;
    }

    public void setText(String str) {
        long j = this.mNativeViewPtr;
        if (str == null) {
            str = "";
        }
        nativeSetText(j, str);
    }

    public void setText(int i) {
        HiLog.debug(TAG, " setText using resId: %{public}d", Integer.valueOf(i));
        if (this.mContext == null) {
            HiLog.error(TAG, " mContext is null!", new Object[0]);
            return;
        }
        ResourceManager resourceManager = this.mContext.getResourceManager();
        if (resourceManager == null) {
            HiLog.error(TAG, " Fail to get resource manager!", new Object[0]);
            return;
        }
        try {
            setText(resourceManager.getElement(i).getString());
        } catch (IOException | NotExistException | WrongTypeException unused) {
            HiLog.error(TAG, " Fail to get text source", new Object[0]);
        }
    }

    public String getText() {
        return nativeGetText(this.mNativeViewPtr);
    }

    public void setHint(String str) {
        nativeSetHint(this.mNativeViewPtr, str);
    }

    public String getHint() {
        return nativeGetHint(this.mNativeViewPtr);
    }

    public String getEditableString() {
        return nativeGetEditableText(this.mNativeViewPtr);
    }

    private boolean isEditable() {
        return nativeIsEditable(this.mNativeViewPtr);
    }

    public void setTextSize(int i) {
        if (TextTool.validateTextSizeParam(i)) {
            nativeSetTextSize(this.mNativeViewPtr, i);
        }
    }

    public void setTextSize(int i, TextSizeType textSizeType) {
        if (TextTool.validateTextSizeParam(i)) {
            if (TextSizeType.PX.equals(textSizeType)) {
                setTextSize(i);
            } else if (!TextSizeType.VP.equals(textSizeType) && !TextSizeType.FP.equals(textSizeType)) {
                HiLog.error(TAG, "do not support this type of text size", new Object[0]);
            } else if (this.mContext == null) {
                HiLog.error(TAG, "context is null", new Object[0]);
            } else if (AttrHelper.getDensity(this.mContext) == 0.0f) {
                setTextSize(i);
            } else {
                setTextSize(convertToPx(i, textSizeType));
            }
        }
    }

    private int convertToPx(int i, TextSizeType textSizeType) {
        if (TextSizeType.VP.equals(textSizeType)) {
            return AttrHelper.vp2px((float) i, this.mContext);
        }
        return TextSizeType.FP.equals(textSizeType) ? AttrHelper.fp2px((float) i, this.mContext) : i;
    }

    public int getTextSize() {
        return nativeGetTextSize(this.mNativeViewPtr);
    }

    public int getTextSize(TextSizeType textSizeType) {
        int nativeGetTextSize = nativeGetTextSize(this.mNativeViewPtr);
        if (TextSizeType.VP.equals(textSizeType) || TextSizeType.FP.equals(textSizeType)) {
            return convertFromPx(nativeGetTextSize, textSizeType);
        }
        return nativeGetTextSize;
    }

    private int convertFromPx(int i, TextSizeType textSizeType) {
        float density = AttrHelper.getDensity(this.mContext);
        if (density == 0.0f) {
            return i;
        }
        if (TextSizeType.VP.equals(textSizeType)) {
            return px2vp(i, density);
        }
        return TextSizeType.FP.equals(textSizeType) ? px2fp(i, density, AttrHelper.getFontRatio(this.mContext)) : i;
    }

    public enum TextSizeType {
        VP(0),
        FP(1),
        PX(2);
        
        private int textSizeTypeNum;

        private TextSizeType(int i) {
            this.textSizeTypeNum = i;
        }

        public int textSizeTypeValue() {
            return this.textSizeTypeNum;
        }

        public static TextSizeType getTextSizeType(int i) {
            if (i == 0) {
                return VP;
            }
            if (i == 1) {
                return FP;
            }
            if (i != 2) {
                return null;
            }
            return PX;
        }
    }

    public void setTextColor(Color color) {
        nativeSetTextColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getTextColor() {
        return new Color(nativeGetTextColor(this.mNativeViewPtr));
    }

    public void setHintColor(Color color) {
        nativeSetHintTextColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getHintColor() {
        return new Color(nativeGetHintTextColor(this.mNativeViewPtr));
    }

    public void setTextAlignment(int i) {
        if (!nativeSetTextAlignment(this.mNativeViewPtr, i)) {
            throw new IllegalArgumentException("The text alignment value should corresponds to alignment modes");
        }
    }

    public int getTextAlignment() {
        return nativeGetTextAlignment(this.mNativeViewPtr);
    }

    public void setTextCursorVisible(boolean z) {
        nativeSetCursorVisible(this.mNativeViewPtr, z);
    }

    public boolean isTextCursorVisible() {
        return nativeIsCursorVisible(this.mNativeViewPtr);
    }

    public int length() {
        return nativeLength(this.mNativeViewPtr);
    }

    public void append(String str) {
        nativeAppend(this.mNativeViewPtr, str);
    }

    public void insert(String str) {
        insert(str, -1);
    }

    static /* synthetic */ boolean lambda$insert$9(Integer num) {
        return num.intValue() >= 0 || num.intValue() == -1;
    }

    public void insert(String str, int i) {
        validateParam(Integer.valueOf(i), $$Lambda$Text$8ZwOzr4YQ1MCnzY0jvihVaYMTAc.INSTANCE, "The position must be non negative or AUTO_CURSOR_POSITION");
        nativeInsertText(this.mNativeViewPtr, str, i);
    }

    public void delete(int i) {
        delete(i, true);
    }

    public void delete(int i, boolean z) {
        delete(i, z, -1);
    }

    static /* synthetic */ boolean lambda$delete$10(Integer num) {
        return num.intValue() >= 0;
    }

    public void delete(int i, boolean z, int i2) {
        validateParam(Integer.valueOf(i), $$Lambda$Text$an5wtIvr4QKWTiZLADCtxch9Y.INSTANCE, "The length size must be non negative");
        validateParam(Integer.valueOf(i2), $$Lambda$Text$q82Caol0IL6t7erSG2awJ3fiilc.INSTANCE, "The position must be non negative or AUTO_CURSOR_POSITION");
        nativeDeleteText(this.mNativeViewPtr, i, z, i2);
    }

    static /* synthetic */ boolean lambda$delete$11(Integer num) {
        return num.intValue() >= 0 || num.intValue() == -1;
    }

    public void setMultipleLine(boolean z) {
        nativeSetMultipleLine(this.mNativeViewPtr, z);
    }

    public boolean isMultipleLine() {
        return nativeIsMultipleLine(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setMaxTextLines$12(Integer num) {
        return num.intValue() > 0;
    }

    public void setMaxTextLines(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$Text$L9QMFgUFuY8eUCVRysnmmLZVF0.INSTANCE, "The value should be positive.");
        nativeSetMaxLines(this.mNativeViewPtr, i);
    }

    public int getMaxTextLines() {
        return nativeGetMaxLines(this.mNativeViewPtr);
    }

    public void setScrollable(boolean z) {
        nativeSetScrollable(this.mNativeViewPtr, z);
    }

    public boolean isScrollable() {
        return nativeIsScrollable(this.mNativeViewPtr);
    }

    public void setAutoFontSize(boolean z) {
        setAutoFontSize(z, false);
    }

    public void setAutoFontSize(boolean z, boolean z2) {
        nativeSetAutoFontSize(this.mNativeViewPtr, z, z2);
    }

    public void setLineSpacing(float f, float f2) {
        nativeSetLineSpacing(this.mNativeViewPtr, f, f2);
    }

    public float getNumOfFontHeight() {
        return nativeGetLineSpacingMultiplier(this.mNativeViewPtr);
    }

    public float getAdditionalLineSpacing() {
        return nativeGetLineSpacingExtra(this.mNativeViewPtr);
    }

    public boolean isAdjustInputPanel() {
        return this.adjustInputPanel;
    }

    public void setAdjustInputPanel(boolean z) {
        this.adjustInputPanel = z;
    }

    static /* synthetic */ boolean lambda$setMaxTextHeight$13(Integer num) {
        return num.intValue() >= 0;
    }

    public void setMaxTextHeight(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$Text$re7h_YN7wYV9gs5gaph_PC25oOc.INSTANCE, "Max text height size must be non negative");
        nativeSetMaxHeight(this.mNativeViewPtr, i);
    }

    public int getMaxTextHeight() {
        return nativeGetMaxHeight(this.mNativeViewPtr);
    }

    static /* synthetic */ boolean lambda$setMaxTextWidth$14(Integer num) {
        return num.intValue() >= 0;
    }

    public void setMaxTextWidth(int i) {
        validateParam(Integer.valueOf(i), $$Lambda$Text$UKTFHFzS29kEuOmyr02RU8MHHmE.INSTANCE, "Max text width size must be non negative");
        nativeSetMaxWidth(this.mNativeViewPtr, i);
    }

    public int getMaxTextWidth() {
        return nativeGetMaxWidth(this.mNativeViewPtr);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public void onRtlChanged(Component.LayoutDirection layoutDirection) {
        super.onRtlChanged(layoutDirection);
        if (this.mLastLayoutDirection != layoutDirection) {
            this.mLastLayoutDirection = layoutDirection;
            Element[] elementArr = this.mElements;
            if (elementArr != null && !this.mElementsRelative) {
                Element element = elementArr[0];
                elementArr[0] = elementArr[2];
                elementArr[2] = element;
            }
        }
    }

    public InputDataChannel getInputDataChannel() {
        if (this.mInputDataChannel == null) {
            this.mInputDataChannel = new TextViewInputDataChannel(this);
        }
        HiLog.debug(TAG, "TextView get input data channel instance success.", new Object[0]);
        return this.mInputDataChannel;
    }

    public void addTextSizeObserver(TextSizeObserver textSizeObserver) {
        if (this.mTextSizeObserversHandler.getObserversCount() == 0) {
            addObserverHandler(this.mTextSizeObserversHandler);
        }
        this.mTextSizeObserversHandler.addObserver(textSizeObserver);
    }

    public void removeSizeTextObserver(TextSizeObserver textSizeObserver) {
        this.mTextSizeObserversHandler.removeObserver(textSizeObserver);
        if (this.mTextSizeObserversHandler.getObserversCount() == 0) {
            removeObserverHandler(this.mTextSizeObserversHandler);
        }
    }

    public void addTextColorObserver(TextColorObserver textColorObserver) {
        if (this.mTextColorObserversHandler.getObserversCount() == 0) {
            addObserverHandler(this.mTextColorObserversHandler);
        }
        this.mTextColorObserversHandler.addObserver(textColorObserver);
    }

    public void removeColorTextObserver(TextColorObserver textColorObserver) {
        this.mTextColorObserversHandler.removeObserver(textColorObserver);
        if (this.mTextColorObserversHandler.getObserversCount() == 0) {
            removeObserverHandler(this.mTextColorObserversHandler);
        }
    }

    public void setBubbleSize(int i, int i2) {
        nativeSetBubbleSize(this.mNativeViewPtr, i, i2);
    }

    public void setBubbleWidth(int i) {
        setBubbleSize(i, getBubbleHeight());
    }

    public void setBubbleHeight(int i) {
        setBubbleSize(getBubbleWidth(), i);
    }

    public int getBubbleWidth() {
        return nativeGetBubbleWidth(this.mNativeViewPtr);
    }

    public int getBubbleHeight() {
        return nativeGetBubbleHeight(this.mNativeViewPtr);
    }

    public void setLeftBubbleSize(int i, int i2) {
        nativeSetLeftBubbleSize(this.mNativeViewPtr, i, i2);
    }

    public void setLeftBubbleWidth(int i) {
        setLeftBubbleSize(i, getSelectionLeftBubbleHeight());
    }

    public void setLeftBubbleHeight(int i) {
        setLeftBubbleSize(getSelectionLeftBubbleWidth(), i);
    }

    public int getSelectionLeftBubbleWidth() {
        return nativeGetSelectionLeftBubbleWidth(this.mNativeViewPtr);
    }

    public int getSelectionLeftBubbleHeight() {
        return nativeGetSelectionLeftBubbleHeight(this.mNativeViewPtr);
    }

    public void setRightBubbleSize(int i, int i2) {
        nativeSetRightBubbleSize(this.mNativeViewPtr, i, i2);
    }

    public void setRightBubbleWidth(int i) {
        setRightBubbleSize(i, getSelectionRightBubbleHeight());
    }

    public void setRightBubbleHeight(int i) {
        setRightBubbleSize(getSelectionRightBubbleWidth(), i);
    }

    public int getSelectionRightBubbleWidth() {
        return nativeGetSelectionRightBubbleWidth(this.mNativeViewPtr);
    }

    public int getSelectionRightBubbleHeight() {
        return nativeGetSelectionRightBubbleHeight(this.mNativeViewPtr);
    }

    public void setSelectionColor(Color color) {
        nativeSetSelectionColor(this.mNativeViewPtr, color.getValue());
    }

    public Color getSelectionColor() {
        return new Color(nativeGetSelectionColor(this.mNativeViewPtr));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getSelectionStart() {
        return nativeGetSelectionStart(this.mNativeViewPtr);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getSelectionEnd() {
        return nativeGetSelectionEnd(this.mNativeViewPtr);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean isTextSelected() {
        return nativeIsTextSelected(this.mNativeViewPtr);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean setSelection(int i, int i2) {
        return nativeSetSelection(this.mNativeViewPtr, i, i2);
    }

    public void updateEditingInfo(EditingCapability editingCapability, int i, int i2) {
        this.mEditingCapability = editingCapability;
        this.mEditingToken = i;
        this.mEditingFlag = i2;
        HiLog.debug(TAG, "updateEditingInfo successful. mEditingToken %{public}d mMonitorFlag %{public}d", this.mEditingCapability, Integer.valueOf(this.mEditingFlag));
    }

    public void setBubbleElement(Element element) {
        this.mCursorBubbleElement = element;
        nativeSetCursorBubbleElement(this.mNativeViewPtr, element.getNativeElementPtr());
    }

    public Element getBubbleElement() {
        return this.mCursorBubbleElement;
    }

    public void setSelectionLeftBubbleElement(Element element) {
        this.mSelectionLeftBubbleElement = element;
        nativeSetSelectionLeftBubbleElement(this.mNativeViewPtr, element.getNativeElementPtr());
    }

    public Element getSelectionLeftBubbleElement() {
        return this.mSelectionLeftBubbleElement;
    }

    public void setSelectionRightBubbleElement(Element element) {
        this.mSelectionRightBubbleElement = element;
        nativeSetSelectionRightBubbleElement(this.mNativeViewPtr, element.getNativeElementPtr());
    }

    public Element getSelectionRightBubbleElement() {
        return this.mSelectionRightBubbleElement;
    }

    @Override // ohos.agp.components.Component
    public void setFadeEffectBoundaryWidth(int i) {
        nativeSetFadeEffectBoundaryWidth(this.mNativeViewPtr, i);
    }

    @Override // ohos.agp.components.Component
    public int getFadeEffectBoundaryWidth() {
        return nativeGetFadeEffectBoundaryWidth(this.mNativeViewPtr);
    }

    public void setPaddingForText(boolean z) {
        nativeSetPaddingForText(this.mNativeViewPtr, z);
    }

    public boolean getPaddingForText() {
        return nativeGetPaddingForText(this.mNativeViewPtr);
    }

    public String getFontVariations() {
        return nativeGetFontVariations(this.mNativeViewPtr);
    }

    public boolean setFontVariations(String str) {
        return nativeSetFontVariations(this.mNativeViewPtr, str);
    }

    private void notifyEditingTextChangeInternal(InputMethodController inputMethodController) {
        EditingCapability editingCapability = this.mEditingCapability;
        if (editingCapability == null) {
            HiLog.error(TAG, "notifyEditingTextChangeInternal mEditingCapability is null", new Object[0]);
            return;
        }
        inputMethodController.notifyEditingTextChanged(this.mEditingToken, this.mInputDataChannel.subscribeEditingText(editingCapability));
    }

    private void notifyCursorPositionInternal(InputMethodController inputMethodController) {
        if (isEditable() && isFocused() && inputMethodController.getKeyboardWindowHeight() < 100) {
            inputMethodController.notifyCursorCoordinateChanged((float) getCursorPositionOnScreenX(), (float) (getCursorPositionOnScreenY() + getCursorHeight()), (float) getCursorPositionOnScreenY(), new float[]{1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        }
    }

    private void notifyCursorPositionAndEditingTextChange() {
        InputMethodController instance = InputMethodController.getInstance();
        if (instance == null) {
            HiLog.error(TAG, "notifyCursorPositionAndEditingTextChange InputMethodController is null, could not notify cursor position change and editing text change.", new Object[0]);
            return;
        }
        notifyEditingTextChangeInternal(instance);
        notifyCursorPositionInternal(instance);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getCursorPositionOnScreenX() {
        return nativeGetCursorPositionOnScreenX(this.mNativeViewPtr);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getCursorPositionOnScreenY() {
        return nativeGetCursorPositionOnScreenY(this.mNativeViewPtr);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int getCursorHeight() {
        return nativeGetCursorHeight(this.mNativeViewPtr);
    }

    private class DefaultEditingFunctonListener implements EditingFunctonListener {
        private DefaultEditingFunctonListener() {
        }

        @Override // ohos.agp.components.Text.EditingFunctonListener
        public void onCopy(String str) {
            SystemPasteboard systemPasteboard = SystemPasteboard.getSystemPasteboard(Text.this.mContext);
            if (systemPasteboard != null) {
                systemPasteboard.setPasteData(PasteData.creatPlainTextData(str));
            }
        }

        @Override // ohos.agp.components.Text.EditingFunctonListener
        public void onPaste() {
            PasteData pasteData;
            SystemPasteboard systemPasteboard = SystemPasteboard.getSystemPasteboard(Text.this.mContext);
            if (!(systemPasteboard == null || (pasteData = systemPasteboard.getPasteData()) == null || !pasteData.getProperty().hasMimeType(PasteData.MIMETYPE_TEXT_PLAIN))) {
                for (int i = 0; i < pasteData.getRecordCount(); i++) {
                    PasteData.Record recordAt = pasteData.getRecordAt(i);
                    if (recordAt.getMimeType().equals(PasteData.MIMETYPE_TEXT_PLAIN)) {
                        Text.this.insert(recordAt.getPlainText().toString());
                        return;
                    }
                }
            }
        }
    }

    private void setOnEditingFunctionCallback(EditingFunctonListener editingFunctonListener) {
        this.mOnEditingFunctionListener = editingFunctonListener;
        nativeSetOnEditingFunctionCallback(this.mNativeViewPtr, this.mOnEditingFunctionListener);
    }
}
