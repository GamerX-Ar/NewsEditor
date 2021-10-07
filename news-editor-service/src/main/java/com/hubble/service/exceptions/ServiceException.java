package com.hubble.service.exceptions;

/**
 *
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 7191715182213889347L;

    /**
     * @serial
     */
    private int errorCode;

    /**
     * Constructs a <code>ServiceException</code> object with a given
     * <code>reason</code> and <code>errorCode</code>.
     *
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(java.lang.Throwable)} method.
     *
     * @param message a description of the exception
     * @param errorCode an api error code
     */
    public ServiceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a <code>ServiceException</code> object with a given
     * <code>reason</code>. The <code>errorCode</code> is initialized to 0.
     *
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(java.lang.Throwable)} method.
     *
     * @param message a description of the exception
     */
    public ServiceException(String message) {
        super(message);
        this.errorCode = 0;
    }

    /**
     * Constructs a <code>KsiUserException</code> object.
     * The <code>reason</code> is initialized to <code>null</code>
     * and the <code>errorCode</code> is initialized to 0.
     *
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(java.lang.Throwable)} method.
     */
    public ServiceException() {
        super();
        this.errorCode = 0;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String reason, Throwable cause) {
        super(reason,cause);
    }

    public ServiceException(String reason, int errorCode, Throwable cause) {
        super(reason,cause);
        this.errorCode = errorCode;
    }

    /**
     * Retrieves the exception code
     * for this <code>KsiApiException</code> object.
     *
     * @return the api error code
     */
    public int getErrorCode() {
        return (errorCode);
    }
}
