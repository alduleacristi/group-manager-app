package org.groupmanager.team.dto;

public class PositionDTO {
	String email;
	Double xPosition, yPosition;

	public PositionDTO(String email, Double xPosition, Double yPosition) {
		this.email = email;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	public PositionDTO(){
		
	}

	public Double getxPosition() {
		return xPosition;
	}

	public void setxPosition(Double xPosition) {
		this.xPosition = xPosition;
	}

	public Double getyPosition() {
		return yPosition;
	}

	public void setyPosition(Double yPosition) {
		this.yPosition = yPosition;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
