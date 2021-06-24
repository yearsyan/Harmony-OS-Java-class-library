package ohos.security.permission.model;

import ohos.global.icu.impl.PatternTokenizer;

@Deprecated
public class Result<T> {
    private int code;
    private T data;
    private String message;

    public int getCode() {
        return this.code;
    }

    public Result<T> setCode(int i) {
        this.code = i;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public Result<T> setMessage(String str) {
        this.message = str;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public Result<T> setData(T t) {
        this.data = t;
        return this;
    }

    public String toString() {
        return "Result{code=" + this.code + ", message='" + this.message + PatternTokenizer.SINGLE_QUOTE + ", data=" + ((Object) this.data) + '}';
    }
}
