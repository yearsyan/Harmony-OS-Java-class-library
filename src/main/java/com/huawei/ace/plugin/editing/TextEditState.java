package com.huawei.ace.plugin.editing;

import ohos.agp.styles.attributes.TextAttrsConstants;
import ohos.utils.fastjson.JSONObject;

public class TextEditState {
    private final String hint;
    private final int selectionEnd;
    private final int selectionStart;
    private final String text;

    public static TextEditState fromJson(JSONObject jSONObject) {
        return new TextEditState(jSONObject.getString("text"), jSONObject.getString(TextAttrsConstants.HINT), jSONObject.getIntValue("selectionStart"), jSONObject.getIntValue("selectionEnd"));
    }

    private TextEditState(String str, String str2, int i, int i2) {
        this.text = str;
        this.hint = str2;
        this.selectionStart = i;
        this.selectionEnd = i2;
    }

    public String getText() {
        return this.text;
    }

    public String getHint() {
        return this.hint;
    }

    public int getSelectionStart() {
        return this.selectionStart;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }
}
