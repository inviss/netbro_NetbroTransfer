package kr.co.d2net.netbro.common.exception;

public class TransferException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public TransferException(String message) {
		super(message);
	}
	
	public TransferException(char errorCode, Throwable cause) {
		super(cause);
		setErrorCode(errorCode);
	}
	
	public TransferException(Throwable cause) {
		super(cause);
	}
	
	public TransferException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TransferException(char errorCode, String message, Throwable cause) {
		super(message, cause);
		setErrorCode(errorCode);
	}
	
}
