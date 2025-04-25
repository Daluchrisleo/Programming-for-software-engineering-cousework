package com.boostphysioclinic.util;

/**
 * A generic class that encapsulates the result of an operation.
 * It can either represent a success with associated data or an error with an error message.
 *
 * @param <T> the type of data returned on success
 * @param <E> the type of error returned on failure
 */
public class Result<T, E> {
    private final T data;
    private final E error;
    private final boolean isSuccess;

    public static Object NO_VALUE = new Object();

    /**
     * Private constructor for creating a result with data, error, and success status.
     *
     * @param data     the data to be returned on success
     * @param error    the error to be returned on failure
     * @param isSuccess the status indicating whether the result is a success or an error
     */
    private Result(T data, E error, boolean isSuccess) {
        this.data = data;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    /**
     * Creates a success result with the given data.
     *
     * @param data the data of the successful operation
     * @param <T>  the type of data
     * @param <E>  the type of error
     * @return a Result instance representing success
     */
    public static <T, E> Result<T, E> success(T data) {
        return new Result<>(data, null, true);
    }

    /**
     * Creates an error result with the given error message.
     *
     * @param error the error of the failed operation
     * @param <T>   the type of data
     * @param <E>   the type of error
     * @return a Result instance representing an error
     */
    public static <T, E> Result<T, E> error(E error) {
        return new Result<>(null, error, false);
    }

    /**
     * Returns true if the result is a success.
     *
     * @return true if the result is a success, false otherwise
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Returns true if the result is an error.
     *
     * @return true if the result is an error, false otherwise
     */
    public boolean isError() {
        return !isSuccess;
    }

    /**
     * Retrieves the data if the result is a success.
     *
     * @return the data of type T if the result is a success
     * @throws IllegalStateException if the result is an error
     */
    public T getData() {
        if (!isSuccess) {
            throw new IllegalStateException("Result is an error");
        }
        return data;
    }

    /**
     * Retrieves the error if the result is an error.
     *
     * @return the error of type E if the result is an error
     * @throws IllegalStateException if the result is a success
     */
    public E getError() {
        if (isSuccess) {
            throw new IllegalStateException("Result is a success");
        }
        return error;
    }

    /**
     * Returns the string representation of the result, including data, error, and success status.
     *
     * @return a string representing the result
     */
    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", error=" + error +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
