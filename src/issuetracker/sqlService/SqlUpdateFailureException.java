package issuetracker.sqlService;

public class SqlUpdateFailureException extends RuntimeException {
    public SqlUpdateFailureException(String message) {
        super(message);
    }

    public SqlUpdateFailureException(Throwable cause) {
        super(cause);
    }

    public SqlUpdateFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
