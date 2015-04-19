package org.groupmanager.team.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.groupmanager.team.common.ErrorList;
import org.groupmanager.team.convertors.UserConvertor;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.model.User;
import org.groupmanager.team.responses.GroupManagerResponseUsers;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/security/users")
public class UserWebService {
	private Logger logger = LoggerFactory.getLogger(UserWebService.class);

	@Inject
	private UserService userService;

	@POST
	@Path("/getUsers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUsersbyEmail(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerResponseUsers response = new GroupManagerResponseUsers();

		try {
			String email = objMapper.readValue(incomingData, String.class);
			List<User> users = userService.getUsersByEmail(email);
			List<UserDTO> usersDTO = new ArrayList<UserDTO>();
			for (User user : users)
				usersDTO.add(UserConvertor.convertToUserDTO(user));
			response.setUsers(usersDTO);

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
		} catch (JsonParseException e) {
			logger.error("User can not be parsed from JSON");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e) {
			logger.error("User can not be parsed from JSON");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e) {
			logger.error("User can not be parsed from JSON");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}
}
