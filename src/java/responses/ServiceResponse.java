package responses;

import constants.MessageKey;

public class ServiceResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ServiceResponse() {
        this.message = MessageKey.SUCCESS;
    }

    public ServiceResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ServiceResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public static <T> ServiceResponse<T> success(String message, T data) {
        return new ServiceResponse<>(true, message, data);
    }

    public static <T> ServiceResponse<T> success(String message) {
        return new ServiceResponse<>(true, message, null);
    }

    public static <T> ServiceResponse<T> failure(String message) {
        return new ServiceResponse<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
