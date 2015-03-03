package org.groupmanager.team.comunications;

import java.net.MalformedURLException;
import java.net.URL;

import org.groupmanager.team.common.UserDTO;

public class Main {

	public static void main(String[] args) throws MalformedURLException {
		UserDTO userDTO = new UserDTO();
		
		userDTO.setEmail("alduleacristi@yahoo.com");
		userDTO.setFirstName("Aldulea");
		userDTO.setLastName("Cristian-Ionel");
		
		AccountCommunications account = new AccountCommunications();
		
		account.sendAddAccount(userDTO,"http://localhost:8080/GroupManager/api/user/addUser");
	}
}
