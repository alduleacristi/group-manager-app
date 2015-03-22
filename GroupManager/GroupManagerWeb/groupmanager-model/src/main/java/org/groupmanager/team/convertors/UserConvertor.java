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
	
	public static UserDTO convertToUserDTO(User user){
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstname());
		userDTO.setLastName(user.getLastname());
		
		return userDTO;
	}
}
