package ohos.org.xml.sax;

public interface AttributeList {
    int getLength();

    String getName(int i);

    String getType(int i);

    String getType(String str);

    String getValue(int i);

    String getValue(String str);
}
