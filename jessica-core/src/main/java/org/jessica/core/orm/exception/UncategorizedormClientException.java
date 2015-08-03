package org.jessica.core.orm.exception;

import org.springframework.dao.UncategorizedDataAccessException;

public class UncategorizedormClientException extends UncategorizedDataAccessException {
	private static final long serialVersionUID = -5001927974502714777L;

	public UncategorizedormClientException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
