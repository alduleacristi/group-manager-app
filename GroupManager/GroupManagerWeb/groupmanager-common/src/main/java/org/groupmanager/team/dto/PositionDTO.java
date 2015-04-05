package org.groupmanager.team.dto;

public class PositionDTO {
	Long idUser;
	Double xPosition, yPosition;

	public PositionDTO(Long idUser, Double xPosition, Double yPosition) {
		this.idUser = idUser;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	public PositionDTO(){
		
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
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

}
