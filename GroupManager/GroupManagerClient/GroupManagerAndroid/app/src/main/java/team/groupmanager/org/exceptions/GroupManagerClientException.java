package team.groupmanager.org.exceptions;

public class GroupManagerClientException extends Exception {
	private String status;

	public GroupManagerClientException() {
	}

	public GroupManagerClientException(String message) {
		super(message);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
