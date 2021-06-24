package com.huawei.ace.plugin;

public interface Result {
    void error(int i, Object obj);

    void notExistFunction();

    default void replyError(int i, Object obj) {
    }

    default void replyError(Object obj) {
    }

    default void replyJsonError(String str) {
    }

    default void replyJsonSuccess(String str) {
    }

    default void replySuccess(Object obj) {
    }

    void success(Object obj);

    void successWithRawString(String str);
}
