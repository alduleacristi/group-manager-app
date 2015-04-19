package org.groupmanager.team.webservices;

import java.io.IOException;
import java.io.InputStream;

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
import org.groupmanager.team.common.ErrorList;
import org.groupmanager.team.convertors.UserConvertor;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.model.User;
import org.groupmanager.team.responses.GroupManagerResponse;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/users")
public class AccountWebService {
	private Logger logger = LoggerFactory.getLogger(AccountWebService.class);

	@Inject
	private UserService userService;

	@POST
	@Path("/addUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(InputStream incomingData) {
		logger.info("In add user");
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerResponse response = new GroupManagerResponse();

		UserDTO userDTO;
		try {
			userDTO = objMapper.readValue(incomingData, UserDTO.class);

			User user = UserConvertor.convertToUser(userDTO);
			User oldUser = userService.getUserByEmail(userDTO.getEmail());
			if (oldUser != null) {
				logger.info("User with eamil: " + user.getEmail()
						+ " already exist");
				response.setMessage("User with eamil: " + user.getEmail()
						+ " already exist");
				response.setError(ErrorList.DUPLICATE_USER);
			} else {
				logger.info(user.getEmail());
				userService.addUser(user);
				response.setMessage("User with eamil: " + user.getEmail()
						+ " added succesfully");
			}

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

			logger.info("Result: "+result);
			return Response.status(200).entity(result).build();
		} catch (JsonParseException e1) {
			logger.error("User can not be parsed from JSON");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e1) {
			logger.error("User can not be parsed from JSON");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e1) {
			logger.error("User can not be parsed from JSON");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}
}
