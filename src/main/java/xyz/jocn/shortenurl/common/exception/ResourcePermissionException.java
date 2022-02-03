package xyz.jocn.shortenurl.common.exception;

public class ResourcePermissionException extends RuntimeException{
	public ResourcePermissionException() {
	}

	public ResourcePermissionException(String message) {
		super(message);
	}

	public ResourcePermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourcePermissionException(Throwable cause) {
		super(cause);
	}
}
