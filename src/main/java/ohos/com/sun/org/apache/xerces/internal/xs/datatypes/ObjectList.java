package ohos.com.sun.org.apache.xerces.internal.xs.datatypes;

import java.util.List;

public interface ObjectList extends List {
    boolean contains(Object obj);

    int getLength();

    Object item(int i);
}
