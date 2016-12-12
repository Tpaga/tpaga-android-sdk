package co.tpaga.android.Tools;

public class TpagaException extends Exception {

    private int statusCode;

    public TpagaException() {
        super();
        statusCode = 500;
    }

    public TpagaException(int code) {
        super();
        statusCode = code;
    }

    public TpagaException(String message) {
        super(message);
        statusCode = 500;
    }

    public TpagaException(String message, Throwable cause) {
        super(message, cause);
        statusCode = 500;
    }

    public TpagaException(String message, int code) {
        super(message);
        statusCode = code;
    }

    public TpagaException(Throwable cause, int code) {
        super(cause);
        statusCode = code;
    }

    public TpagaException(String message, Throwable cause, int code) {
        super(message, cause);
        statusCode = code;
    }

    public int getStatusCode() {
        return statusCode;
    }



}
