package ohos.agp.styles.attributes;

import ohos.agp.components.Attr;

public class TextFieldAttrsConstants extends TextAttrsConstants {
    public static final String BASEMENT = "basement";

    @Override // ohos.agp.styles.attributes.ViewAttrsConstants, ohos.agp.styles.attributes.TextAttrsConstants
    public Attr.AttrType getType(String str) {
        if (str.equals(BASEMENT)) {
            return Attr.AttrType.ELEMENT;
        }
        return super.getType(str);
    }
}
