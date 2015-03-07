package org.groupmanager.team.webservices;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.common.ErrorList;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponseLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class SecurityWebService {
	private Logger logger = LoggerFactory.getLogger(SecurityWebService.class);

	@POST
	@Path("/security/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerResponseLogin response = new GroupManagerResponseLogin();
		try {
			UserDTO userDTO = objMapper.readValue(incomingData, UserDTO.class);

			logger.info(String.format("User %1$s was autheticated with succes",
					userDTO.getEmail()));
			response.setMessage(String.format(
					"User %1$s was authenticated with succes",
					userDTO.getEmail()));
			response.setToken("Token test");
			return Response.status(Response.Status.OK).entity(response).build();
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
