package ohos.com.sun.org.apache.xml.internal.serializer.utils;

import java.util.ListResourceBundle;

public class SerializerMessages_zh_CN extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{new Object[]{MsgKey.BAD_MSGKEY, "消息关键字 ''{0}'' 不在消息类 ''{1}'' 中"}, new Object[]{MsgKey.BAD_MSGFORMAT, "消息类 ''{1}'' 中消息 ''{0}'' 的格式化失败。"}, new Object[]{"ER_SERIALIZER_NOT_CONTENTHANDLER", "串行器类 ''{0}'' 不实现 ohos.org.xml.sax.ContentHandler。"}, new Object[]{"ER_RESOURCE_COULD_NOT_FIND", "找不到资源 [ {0} ]。\n {1}"}, new Object[]{"ER_RESOURCE_COULD_NOT_LOAD", "资源 [ {0} ] 无法加载: {1} \n {2} \t {3}"}, new Object[]{"ER_BUFFER_SIZE_LESSTHAN_ZERO", "缓冲区大小 <=0"}, new Object[]{"ER_INVALID_UTF16_SURROGATE", "检测到无效的 UTF-16 代理: {0}?"}, new Object[]{"ER_OIERROR", "IO 错误"}, new Object[]{"ER_ILLEGAL_ATTRIBUTE_POSITION", "在生成子节点之后或在生成元素之前无法添加属性 {0}。将忽略属性。"}, new Object[]{"ER_NAMESPACE_PREFIX", "没有说明名称空间前缀 ''{0}''。"}, new Object[]{MsgKey.ER_STRAY_ATTRIBUTE, "属性 ''{0}'' 在元素外部。"}, new Object[]{"ER_STRAY_NAMESPACE", "名称空间声明 ''{0}''=''{1}'' 在元素外部。"}, new Object[]{"ER_COULD_NOT_LOAD_RESOURCE", "无法加载 ''{0}'' (检查 CLASSPATH), 现在只使用默认值"}, new Object[]{"ER_ILLEGAL_CHARACTER", "尝试输出未以{1}的指定输出编码表示的整数值 {0} 的字符。"}, new Object[]{"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "无法为输出方法 ''{1}'' 加载属性文件 ''{0}'' (检查 CLASSPATH)"}, new Object[]{"ER_INVALID_PORT", "无效的端口号"}, new Object[]{"ER_PORT_WHEN_HOST_NULL", "主机为空时, 无法设置端口"}, new Object[]{"ER_HOST_ADDRESS_NOT_WELLFORMED", "主机不是格式良好的地址"}, new Object[]{"ER_SCHEME_NOT_CONFORMANT", "方案不一致。"}, new Object[]{"ER_SCHEME_FROM_NULL_STRING", "无法从空字符串设置方案"}, new Object[]{"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "路径包含无效的转义序列"}, new Object[]{"ER_PATH_INVALID_CHAR", "路径包含无效的字符: {0}"}, new Object[]{"ER_FRAG_INVALID_CHAR", "片段包含无效的字符"}, new Object[]{"ER_FRAG_WHEN_PATH_NULL", "路径为空时, 无法设置片段"}, new Object[]{"ER_FRAG_FOR_GENERIC_URI", "只能为一般 URI 设置片段"}, new Object[]{"ER_NO_SCHEME_IN_URI", "在 URI 中找不到方案"}, new Object[]{"ER_CANNOT_INIT_URI_EMPTY_PARMS", "无法以空参数初始化 URI"}, new Object[]{"ER_NO_FRAGMENT_STRING_IN_PATH", "路径和片段中都无法指定片段"}, new Object[]{"ER_NO_QUERY_STRING_IN_PATH", "路径和查询字符串中不能指定查询字符串"}, new Object[]{"ER_NO_PORT_IF_NO_HOST", "如果没有指定主机, 则不可以指定端口"}, new Object[]{"ER_NO_USERINFO_IF_NO_HOST", "如果没有指定主机, 则不可以指定 Userinfo"}, new Object[]{MsgKey.ER_XML_VERSION_NOT_SUPPORTED, "警告: 输出文档的版本应为 ''{0}''。不支持此版本的 XML。输出文档的版本将为 ''1.0''。"}, new Object[]{"ER_SCHEME_REQUIRED", "方案是必需的!"}, new Object[]{MsgKey.ER_FACTORY_PROPERTY_MISSING, "传递到 SerializerFactory 的 Properties 对象没有 ''{0}'' 属性。"}, new Object[]{"ER_ENCODING_NOT_SUPPORTED", "警告: Java 运行时不支持编码 ''{0}''。"}};
    }
}
