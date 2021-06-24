package com.huawei.ace.plugin.editing;

import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import com.huawei.ace.runtime.ALog;

/* access modifiers changed from: package-private */
public class InputConnectionWrapper extends BaseInputConnection {
    private static final String LOG_TAG = "Ace_IME";
    private final View aceView;
    private int batchCount;
    private final int clientId;
    private final TextInputDelegate delegate;
    private final Editable editable;
    private int extractedTextRequestToken = 0;
    private final String hint;
    private InputMethodManager imm;

    public InputConnectionWrapper(View view, int i, TextInputDelegate textInputDelegate, Editable editable2, String str) {
        super(view, true);
        this.aceView = view;
        this.clientId = i;
        this.delegate = textInputDelegate;
        this.editable = editable2;
        this.batchCount = 0;
        this.hint = str;
        Object systemService = view.getContext().getSystemService("input_method");
        if (systemService instanceof InputMethodManager) {
            this.imm = (InputMethodManager) systemService;
        }
    }

    public ExtractedText getExtractedText(ExtractedTextRequest extractedTextRequest, int i) {
        if (extractedTextRequest == null || this.editable == null) {
            ALog.w(LOG_TAG, "request or editable is null");
            return null;
        }
        if (extractedTextRequest.token != 0) {
            this.extractedTextRequestToken = extractedTextRequest.token;
        }
        ExtractedText extractedText = new ExtractedText();
        extractedText.flags = 0;
        extractedText.partialStartOffset = -1;
        extractedText.partialEndOffset = -1;
        extractedText.selectionStart = Selection.getSelectionStart(this.editable);
        extractedText.selectionEnd = Selection.getSelectionEnd(this.editable);
        extractedText.startOffset = 0;
        extractedText.hint = this.hint;
        if ((extractedTextRequest.flags & 1) != 0) {
            extractedText.text = new SpannableString(this.editable);
        } else {
            extractedText.text = this.editable.toString();
        }
        return extractedText;
    }

    public Editable getEditable() {
        return this.editable;
    }

    public boolean beginBatchEdit() {
        this.batchCount++;
        return super.beginBatchEdit();
    }

    public boolean endBatchEdit() {
        boolean endBatchEdit = super.endBatchEdit();
        this.batchCount--;
        onStateUpdated();
        return endBatchEdit;
    }

    public boolean commitText(CharSequence charSequence, int i) {
        boolean commitText = super.commitText(charSequence, i);
        onStateUpdated();
        return commitText;
    }

    public boolean deleteSurroundingText(int i, int i2) {
        if (Selection.getSelectionStart(this.editable) == -1) {
            return true;
        }
        boolean deleteSurroundingText = super.deleteSurroundingText(i, i2);
        onStateUpdated();
        return deleteSurroundingText;
    }

    public boolean setComposingRegion(int i, int i2) {
        boolean composingRegion = super.setComposingRegion(i, i2);
        onStateUpdated();
        return composingRegion;
    }

    public boolean setComposingText(CharSequence charSequence, int i) {
        boolean z;
        if (charSequence.length() == 0) {
            z = super.commitText(charSequence, i);
        } else {
            z = super.setComposingText(charSequence, i);
        }
        onStateUpdated();
        return z;
    }

    public boolean setSelection(int i, int i2) {
        boolean selection = super.setSelection(i, i2);
        onStateUpdated();
        return selection;
    }

    public boolean sendKeyEvent(KeyEvent keyEvent) {
        ALog.d(LOG_TAG, "action & keycode: " + keyEvent.getAction() + " , " + keyEvent.getKeyCode());
        if (keyEvent.getAction() != 0) {
            return false;
        }
        if (keyEvent.getKeyCode() == 67) {
            return deleteSelection(keyEvent);
        }
        if (keyEvent.getKeyCode() == 21) {
            moveToLeft();
            return true;
        } else if (keyEvent.getKeyCode() != 22) {
            return enterCharacter(keyEvent);
        } else {
            moveToRight();
            return true;
        }
    }

    public boolean performEditorAction(int i) {
        TextInputAction textInputAction;
        ALog.d(LOG_TAG, "performEditorAction: " + i);
        if (i == 0) {
            textInputAction = TextInputAction.UNSPECIFIED;
        } else if (i == 1) {
            textInputAction = TextInputAction.NONE;
        } else if (i == 2) {
            textInputAction = TextInputAction.GO;
        } else if (i == 3) {
            textInputAction = TextInputAction.SEARCH;
        } else if (i == 4) {
            textInputAction = TextInputAction.SEND;
        } else if (i == 5) {
            textInputAction = TextInputAction.NEXT;
        } else if (i != 7) {
            textInputAction = TextInputAction.DONE;
        } else {
            textInputAction = TextInputAction.PREVIOUS;
        }
        this.delegate.performAction(this.clientId, textInputAction);
        return true;
    }

    private void onStateUpdated() {
        if (this.batchCount <= 0 && this.imm != null) {
            int selectionStart = Selection.getSelectionStart(this.editable);
            int selectionEnd = Selection.getSelectionEnd(this.editable);
            int composingSpanStart = BaseInputConnection.getComposingSpanStart(this.editable);
            int composingSpanEnd = BaseInputConnection.getComposingSpanEnd(this.editable);
            this.imm.updateSelection(this.aceView, selectionStart, selectionEnd, composingSpanStart, composingSpanEnd);
            ExtractedText extractedText = new ExtractedText();
            Editable editable2 = this.editable;
            int length = editable2.length();
            extractedText.partialStartOffset = -1;
            extractedText.partialEndOffset = -1;
            extractedText.startOffset = 0;
            extractedText.selectionStart = selectionStart;
            extractedText.selectionEnd = selectionEnd;
            extractedText.flags = 0;
            extractedText.text = editable2.subSequence(0, length);
            extractedText.hint = this.hint;
            this.imm.updateExtractedText(this.aceView, this.extractedTextRequestToken, extractedText);
            this.delegate.updateEditingState(this.clientId, this.editable.toString(), selectionStart, selectionEnd, composingSpanStart, composingSpanEnd);
        }
    }

    private static int sanitizeIndex(int i, Editable editable2) {
        return Math.max(0, Math.min(editable2.length(), i));
    }

    private boolean deleteSelection(KeyEvent keyEvent) {
        int sanitizeIndex = sanitizeIndex(Selection.getSelectionStart(this.editable), this.editable);
        int sanitizeIndex2 = sanitizeIndex(Selection.getSelectionEnd(this.editable), this.editable);
        if (sanitizeIndex2 > sanitizeIndex) {
            Selection.setSelection(this.editable, sanitizeIndex);
            this.editable.delete(sanitizeIndex, sanitizeIndex2);
            onStateUpdated();
            return true;
        } else if (sanitizeIndex <= 0) {
            ALog.w(LOG_TAG, "illegal selection.");
            return false;
        } else if (!TextKeyListener.getInstance().onKeyDown(null, this.editable, keyEvent.getKeyCode(), keyEvent)) {
            return false;
        } else {
            onStateUpdated();
            return true;
        }
    }

    private void moveToRight() {
        int min = Math.min(Selection.getSelectionStart(this.editable) + 1, this.editable.length());
        setSelection(min, min);
    }

    private void moveToLeft() {
        int max = Math.max(Selection.getSelectionStart(this.editable) - 1, 0);
        setSelection(max, max);
    }

    private boolean enterCharacter(KeyEvent keyEvent) {
        int unicodeChar = keyEvent.getUnicodeChar();
        if (unicodeChar == 0) {
            return false;
        }
        int max = Math.max(0, Selection.getSelectionStart(this.editable));
        int max2 = Math.max(0, Selection.getSelectionEnd(this.editable));
        if (max2 != max) {
            this.editable.delete(max, max2);
        }
        this.editable.insert(max, String.valueOf((char) unicodeChar));
        int i = max + 1;
        setSelection(i, i);
        onStateUpdated();
        return true;
    }
}
