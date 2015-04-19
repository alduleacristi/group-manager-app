package team.groupmanager.org.exceptions;

public class GroupManagerClientException extends Exception {
	private String status;
    private Throwable cause;

	public GroupManagerClientException() {
	}

	public GroupManagerClientException(String message) {
		super(message);
	}

    public GroupManagerClientException(String message, Throwable cause) {
        super(message,cause);
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
