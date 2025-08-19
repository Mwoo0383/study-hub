package lang.studyhub.common;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        ErrorInfo error,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }
    public static ApiResponse<Void> fail(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorInfo(code, message), Instant.now());
    }
    public record ErrorInfo(String code, String message) {}
}