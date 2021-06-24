package ohos.com.sun.org.apache.xml.internal.utils.res;

import ohos.global.icu.text.PluralRules;
import ohos.telephony.TelephoneNumberUtils;

public class XResources_zh_CN extends XResourceBundle {
    private static final Object[][] _contents = {new Object[]{"ui_language", "zh"}, new Object[]{"help_language", "zh"}, new Object[]{"language", "zh"}, new Object[]{XResourceBundle.LANG_ALPHABET, new CharArrayWrapper(new char[]{65313, 65314, 65315, 65316, 65317, 65318, 65319, 65320, 65321, 65322, 65323, 65324, 65325, 65326, 65327, 65328, 65329, 65330, 65331, 65332, 65333, 65334, 65335, 65336, 65337, 65338})}, new Object[]{XResourceBundle.LANG_TRAD_ALPHABET, new CharArrayWrapper(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', TelephoneNumberUtils.WILD, 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'})}, new Object[]{"orientation", "LeftToRight"}, new Object[]{XResourceBundle.LANG_NUMBERING, XResourceBundle.LANG_MULT_ADD}, new Object[]{XResourceBundle.MULT_ORDER, XResourceBundle.MULT_FOLLOWS}, new Object[]{XResourceBundle.LANG_NUMBERGROUPS, new IntArrayWrapper(new int[]{1})}, new Object[]{PluralRules.KEYWORD_ZERO, new CharArrayWrapper(new char[]{38646})}, new Object[]{XResourceBundle.LANG_MULTIPLIER, new LongArrayWrapper(new long[]{100000000, 10000, 1000, 100, 10})}, new Object[]{XResourceBundle.LANG_MULTIPLIER_CHAR, new CharArrayWrapper(new char[]{20159, 19975, 21315, 30334, 21313})}, new Object[]{"digits", new CharArrayWrapper(new char[]{19968, 20108, 19977, 22235, 20116, 20845, 19971, 20843, 20061})}, new Object[]{XResourceBundle.LANG_NUM_TABLES, new StringArrayWrapper(new String[]{"digits"})}};

    @Override // ohos.com.sun.org.apache.xml.internal.utils.res.XResourceBundle
    public Object[][] getContents() {
        return _contents;
    }
}
