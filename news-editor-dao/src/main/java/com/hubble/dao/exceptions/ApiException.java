package com.hubble.dao.exceptions;

/**
 *
 */
public class ApiException extends Exception {

    private static final long serialVersionUID = 8685894783987630352L;

    /**
     * @serial
     */
    private int apiErrorCode;

    /**
     * Constructs a <code>ApiException</code> object with a given
     * <code>reason</code> and <code>apiErrorCode</code>.
     *
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(java.lang.Throwable)} method.
     *
     * @param apiErrorCode an api error code
     * @param message a description of the exception
     */
    public ApiException(String message, int apiErrorCode) {
        super(message);
        this.apiErrorCode = apiErrorCode;
    }

    /**
     * Constructs a <code>ApiException</code> object with a given
     * <code>reason</code>. The <code>apiErrorCode</code> is initialized to 0.
     *
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(java.lang.Throwable)} method.
     *
     * @param message a description of the exception
     */
    public ApiException(String message) {
        super(message);
        this.apiErrorCode = 0;
    }

    /**
     * Constructs a <code>ApiException</code> object.
     * The <code>reason</code> is initialized to <code>null</code>
     * and the <code>apiErrorCode</code> is initialized to 0.
     *
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(java.lang.Throwable)} method.
     */
    public ApiException() {
        super();
        this.apiErrorCode = 0;
    }


    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String reason, Throwable cause) {
        super(reason,cause);
    }

    public ApiException(String reason, int apiErrorCode, Throwable cause) {
        super(reason,cause);
        this.apiErrorCode = apiErrorCode;
    }

    /**
     * Retrieves the exception code
     * for this <code>ApiException</code> object.
     *
     * @return the api error code
     */
    public int getErrorCode() {
        return (apiErrorCode);
    }
}
