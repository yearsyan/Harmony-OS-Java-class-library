package ohos.security.permission.infrastructure.model;

import ohos.global.icu.impl.PatternTokenizer;

public class Result<T> {
    private int code;
    private T data;
    private String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public Result<T> setCode(int i) {
        this.code = i;
        return this;
    }

    public Result<T> setMessage(String str) {
        this.message = str;
        return this;
    }

    public Result<T> setData(T t) {
        this.data = t;
        return this;
    }

    public String toString() {
        return "Result{code=" + this.code + ", message='" + this.message + PatternTokenizer.SINGLE_QUOTE + ", data=" + ((Object) this.data) + '}';
    }
}
