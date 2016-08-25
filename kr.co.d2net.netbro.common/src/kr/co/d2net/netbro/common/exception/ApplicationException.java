package kr.co.d2net.netbro.common.exception;


/**
 * <pre>
 * 사용자 정의 에러클래스의 최상위 클래스
 * </pre>
 * @author Administrator
 *
 */
public class ApplicationException extends Exception implements ErrorCoded {

	private static final long serialVersionUID = 1L;

	private char errorCode;
	
	public char getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(char errorCode) {
		this.errorCode = errorCode;
	}

	public ApplicationException(String message) {
		super(message);
	}
	
	public ApplicationException(char errorCode, Throwable cause) {
		super(cause);
		setErrorCode(errorCode);
	}
	
	public ApplicationException(Throwable cause) {
		super(cause);
	}
	
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ApplicationException(char errorCode, String message, Throwable cause) {
		super(message, cause);
		setErrorCode(errorCode);
	}
	
}
