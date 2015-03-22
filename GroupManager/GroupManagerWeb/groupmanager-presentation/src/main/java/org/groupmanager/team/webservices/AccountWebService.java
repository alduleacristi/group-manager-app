package org.groupmanager.team.webservices;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.convertors.UserConvertor;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.model.User;
import org.groupmanager.team.responses.GroupManagerResponse;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountWebService {
	private Logger logger = LoggerFactory.getLogger(AccountWebService.class);

	@Inject
	private UserService userService;

	@POST
	@Path("users/addUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();

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

		GroupManagerResponse response = new GroupManagerResponse();
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
