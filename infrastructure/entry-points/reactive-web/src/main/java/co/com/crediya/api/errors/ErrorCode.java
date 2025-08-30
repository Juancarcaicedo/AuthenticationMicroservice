package co.com.crediya.api.errors;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    VALIDATION_ERROR("VALIDATION_ERROR", "La solicitud es inválida", HttpStatus.BAD_REQUEST, Level.WARN),
    BUSINESS_CONFLICT("BUSINESS_CONFLICT", "La operación entra en conflicto con el estado actual", HttpStatus.CONFLICT, Level.WARN),
    NOT_FOUND("NOT_FOUND", "Recurso no encontrado", HttpStatus.NOT_FOUND, Level.INFO),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "Método no permitido", HttpStatus.METHOD_NOT_ALLOWED, Level.INFO),
    UNSUPPORTED_MEDIA("UNSUPPORTED_MEDIA", "Tipo de contenido no soportado", HttpStatus.UNSUPPORTED_MEDIA_TYPE, Level.WARN),
    UNEXPECTED_ERROR("UNEXPECTED_ERROR", "Ocurrió un error inesperado", HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
    private final Level level;

    ErrorCode(String code, String message, HttpStatus status, Level level) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.level = level;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
    public Level getLevel() { return level; }

    public enum Level { INFO, WARN, ERROR }
}
