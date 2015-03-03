package org.groupmanager.team.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.common.UserDTO;
import org.groupmanager.team.convertors.UserConvertor;
import org.groupmanager.team.model.User;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/user")
public class AccountWebService {
	private Logger logger = LoggerFactory.getLogger(AccountWebService.class);

	@Inject
	private UserService userService;

	@POST
	@Path("/addUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		// logger.info("Try to add user with email {}", userDTO.getEmail());

		/* String succesfully = "User was added succesfully"; */

		UserDTO userDTO;
		try {
			userDTO = objMapper.readValue(incomingData, UserDTO.class);

			User user = UserConvertor.convertToUser(userDTO);
			logger.info(user.getEmail());
			userService.addUser(user);
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// StringBuilder crunchifyBuilder = new StringBuilder();
		// try {
		// BufferedReader in = new BufferedReader(new InputStreamReader(
		// incomingData));
		// String line = null;
		// while ((line = in.readLine()) != null) {
		// crunchifyBuilder.append(line);
		// }
		// } catch (Exception e) {
		// System.out.println("Error Parsing: - ");
		// }
		// System.out.println("Data Received: " + crunchifyBuilder.toString());
		org.groupmanager.team.responses.Response response = new org.groupmanager.team.responses.Response();
		response.setMessage("Account added succesfully");

		String result = null;
		try {
			result = objMapper.writeValueAsString(response);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Response.status(200).entity(result).build();
	}
}
