package org.groupmanager.team.responses;

import java.util.List;

import org.groupmanager.team.dto.UserDTO;

public class GroupManagerResponseUsers extends GroupManagerResponse {
	private List<UserDTO> users;

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
}
