package org.groupmanager.team.convertors;

import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.model.User;

public class UserConvertor {
	public static User convertToUser(UserDTO userDTO){
		User user = new User();
		user.setEmail(userDTO.getEmail());
		user.setId(userDTO.getId());
		user.setFirstname(userDTO.getFirstName());
		user.setLastname(userDTO.getLastName());
		
		return user;
	}
}
