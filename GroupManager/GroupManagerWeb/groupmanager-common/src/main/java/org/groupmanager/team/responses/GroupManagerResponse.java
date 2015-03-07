package org.groupmanager.team.responses;

import org.groupmanager.team.common.ErrorList;

public class GroupManagerResponse {
	private String message;
	private String errorMessage;
	private ErrorList error;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ErrorList getError() {
		return error;
	}

	public void setError(ErrorList error) {
		this.error = error;
	}

}
