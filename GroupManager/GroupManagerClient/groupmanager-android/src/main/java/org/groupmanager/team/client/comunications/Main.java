package org.groupmanager.team.client.comunications;

import java.net.MalformedURLException;
import org.groupmanager.team.client.exceptions.GroupManagerClientException;
import org.groupmanager.team.dto.UserDTO;

public class Main {

	public static void main(String[] args) throws MalformedURLException,
			GroupManagerClientException {

		UserDTO userDTO = new UserDTO();

		userDTO.setEmail("alduleacristi@yahoo.com");

		LoginCommunications loginCom = new LoginCommunications();

		String token = loginCom.login(userDTO,
				"http://localhost:8080/GroupManager/api/login");

		UserCommunication userCom = new UserCommunication();
		userDTO.setEmail("alduleacristi@yahoo.com");
		userCom.getUsersByEmail(
				userDTO,
				"http://localhost:8080/GroupManager/api/security/users/getUsers",
				token);
	}
}
