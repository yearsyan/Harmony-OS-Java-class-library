package ohos.com.sun.org.apache.xalan.internal.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_zh_TW extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{new Object[]{BasisLibrary.RUN_TIME_INTERNAL_ERR, "''{0}'' 中的執行階段內部錯誤"}, new Object[]{BasisLibrary.RUN_TIME_COPY_ERR, "執行 <xsl:copy> 時發生執行階段錯誤"}, new Object[]{"DATA_CONVERSION_ERR", "從 ''{0}'' 至 ''{1}'' 的轉換無效。"}, new Object[]{BasisLibrary.EXTERNAL_FUNC_ERR, "XSLTC 不支援外部函數 ''{0}''。"}, new Object[]{BasisLibrary.EQUALITY_EXPR_ERR, "相等性表示式中的引數類型不明。"}, new Object[]{BasisLibrary.INVALID_ARGUMENT_ERR, "呼叫 ''{1}'' 中的引數類型 ''{0}'' 無效"}, new Object[]{BasisLibrary.FORMAT_NUMBER_ERR, "嘗試使用樣式 ''{1}'' 格式化數字 ''{0}''。"}, new Object[]{BasisLibrary.ITERATOR_CLONE_ERR, "無法複製重複程式 ''{0}''。"}, new Object[]{BasisLibrary.AXIS_SUPPORT_ERR, "不支援軸 ''{0}'' 的重複程式。"}, new Object[]{BasisLibrary.TYPED_AXIS_SUPPORT_ERR, "不支援類型軸 ''{0}'' 的重複程式。"}, new Object[]{"STRAY_ATTRIBUTE_ERR", "屬性 ''{0}'' 在元素之外。"}, new Object[]{BasisLibrary.STRAY_NAMESPACE_ERR, "命名空間宣告 ''{0}''=''{1}'' 超出元素外。"}, new Object[]{BasisLibrary.NAMESPACE_PREFIX_ERR, "字首 ''{0}'' 的命名空間尚未宣告。"}, new Object[]{BasisLibrary.DOM_ADAPTER_INIT_ERR, "使用錯誤的來源 DOM 類型建立 DOMAdapter。"}, new Object[]{BasisLibrary.PARSER_DTD_SUPPORT_ERR, "您正在使用的 SAX 剖析器不會處理 DTD 宣告事件。"}, new Object[]{BasisLibrary.NAMESPACES_SUPPORT_ERR, "您正在使用的 SAX 剖析器不支援 XML 命名空間。"}, new Object[]{BasisLibrary.CANT_RESOLVE_RELATIVE_URI_ERR, "無法解析 URI 參照 ''{0}''。"}, new Object[]{"UNSUPPORTED_XSL_ERR", "不支援的 XSL 元素 ''{0}''"}, new Object[]{"UNSUPPORTED_EXT_ERR", "無法辨識的 XSLTC 擴充套件 ''{0}''"}, new Object[]{BasisLibrary.UNKNOWN_TRANSLET_VERSION_ERR, "建立指定 translet ''{0}'' 的 XSLTC 版本比使用中 XSLTC 執行階段的版本較新。您必須重新編譯樣式表，或使用較新的 XSLTC 版本來執行此 translet。"}, new Object[]{"INVALID_QNAME_ERR", "值必須為 QName 的屬性，具有值 ''{0}''"}, new Object[]{"INVALID_NCNAME_ERR", "值必須為 NCName 的屬性，具有值 ''{0}''"}, new Object[]{BasisLibrary.UNALLOWED_EXTENSION_FUNCTION_ERR, "當安全處理功能設為真時，不允許使用擴充套件函數 ''{0}''。"}, new Object[]{BasisLibrary.UNALLOWED_EXTENSION_ELEMENT_ERR, "當安全處理功能設為真時，不允許使用擴充套件元素 ''{0}''。"}};
    }
}
