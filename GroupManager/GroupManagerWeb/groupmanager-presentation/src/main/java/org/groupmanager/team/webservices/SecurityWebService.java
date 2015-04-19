package org.groupmanager.team.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

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
import org.groupmanager.team.responses.GroupManagerResponseLogin;
import org.groupmanager.team.user.GroupManagerSession;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class SecurityWebService {
	private Logger logger = LoggerFactory.getLogger(SecurityWebService.class);

	@Inject
	private GroupManagerSession session;
	@Inject
	private UserService userService;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerResponseLogin response = new GroupManagerResponseLogin();
		try {
			UserDTO userDTO = objMapper.readValue(incomingData, UserDTO.class);

			logger.info(String.format("User %1$s was autheticated with succes",
					userDTO.getEmail()));

			User user = userService.getUserByEmail(userDTO.getEmail());
			if (user != null
					&& user.getPassword().equals(userDTO.getPassword())) {
				response.setMessage(String.format(
						"User %1$s was authenticated with succes",
						userDTO.getEmail()));

				UUID token = UUID.randomUUID();
				session.addUser(token.toString(),
						UserConvertor.convertToUser(userDTO));
				response.setToken(token.toString());
			} else {
				response.setError(ErrorList.FAILED_TO_AUTHENTICATE);
				response.setErrorMessage("Invalid email or password");
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
			return Response.status(Response.Status.OK).entity(result).build();
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

	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerResponseLogin response = new GroupManagerResponseLogin();
		try {
			String key = objMapper.readValue(incomingData, String.class);

			logger.info(String.format("User %1$s was logout with succes", key));
			response.setMessage(String.format(
					"User %1$s was logut with succes", key));

			session.removeUser(key);
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
			return Response.status(Response.Status.OK).entity(result).build();
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
