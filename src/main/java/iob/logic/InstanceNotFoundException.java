package iob.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class InstanceNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public InstanceNotFoundException() {
//		super();
	}

	public InstanceNotFoundException(String message) {
		super(message);
	}

	public InstanceNotFoundException(Throwable cause) {
		super(cause);
	}

	public InstanceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
