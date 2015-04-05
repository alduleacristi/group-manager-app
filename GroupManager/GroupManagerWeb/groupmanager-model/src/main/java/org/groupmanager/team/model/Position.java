package org.groupmanager.team.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "gm_position")
public class Position {
	private Long id;
	private Double xCoordonate;
	private Double yCoordonate;
	private User user;

	@Column(name="position_id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="x_coordonate")
	public Double getxCoordonate() {
		return xCoordonate;
	}

	public void setxCoordonate(Double xCoordonate) {
		this.xCoordonate = xCoordonate;
	}

	@Column(name="y_coordonate")
	public Double getyCoordonate() {
		return yCoordonate;
	}

	public void setyCoordonate(Double yCoordonate) {
		this.yCoordonate = yCoordonate;
	}

	@OneToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
