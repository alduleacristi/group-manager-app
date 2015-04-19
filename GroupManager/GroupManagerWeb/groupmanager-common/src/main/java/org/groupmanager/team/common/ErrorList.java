package org.groupmanager.team.common;

public enum ErrorList {
	JSON_PARSER("Problem with parsing the String that was submited to server."), FAILED_TO_AUTHENTICATE(
			"Failed to authenticate"), DUPLICATE_USER("User already exist"), DUPLICATE_GROUP(
			"Group already exist");

	private String message;

	private ErrorList(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
