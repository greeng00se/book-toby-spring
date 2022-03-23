package springbook.user.dao.exception;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException() {
        super();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return super.initCause(cause);
    }
}
