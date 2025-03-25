package com.boostphysioclinic.util;

public class Result<T, E> {
    private final T data;
    private final E error;
    private final boolean isSuccess;

    public static Object NO_VALUE = new Object();

    private Result(T data, E error, boolean isSuccess) {
        this.data = data;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public static <T, E> Result<T, E> success(T data) {
        return new Result<>(data, null, true);
    }

    public static <T, E> Result<T, E> error(E error) {
        return new Result<>(null, error, false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isError() {
        return !isSuccess;
    }

    public T getData() {
        if (!isSuccess) {
            throw new IllegalStateException("Result is an error");
        }
        return data;
    }

    public E getError() {
        if (isSuccess) {
            throw new IllegalStateException("Result is a success");
        }
        return error;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", error=" + error +
                ", isSuccess=" + isSuccess +
                '}';
    }
}