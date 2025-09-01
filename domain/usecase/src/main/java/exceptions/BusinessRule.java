package exceptions;

public enum BusinessRule {
        EMAIL_ALREADY_EXISTS("El correo ya está registrado"),
        DOCUMENT_ALREADY_EXISTS("El documento ya está registrado"),
        USER_NOT_FOUND("EL Usuario no Encontrado");

        private final String message;

        BusinessRule(String message) {
                this.message = message;
        }

        public String getMessage() {
                return message;
        }
}
