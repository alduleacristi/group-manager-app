package org.groupmanager.team.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.crypto.spec.PSource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.gropumanager.team.comunications.CoordonatesComunication;
import org.groupmanager.team.common.ErrorList;
import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.PositionDTO;
import org.groupmanager.team.model.Group;
import org.groupmanager.team.model.Position;
import org.groupmanager.team.model.User;
import org.groupmanager.team.responses.GroupManagerGroupResponse;
import org.groupmanager.team.responses.GroupManagerResponse;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Cristi This class is used to get position from client.
 */

@Path("/security/location")
public class UserLocationWebService {
	private Logger logger = LoggerFactory
			.getLogger(UserLocationWebService.class);

	@Inject
	private CoordonatesComunication coordCom;
	@Inject
	private UserService userService;

	@POST
	@Path("/updateLocation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationREST(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerResponse response = new GroupManagerResponse();
		try {
			PositionDTO positionDTO = objMapper.readValue(incomingData,
					PositionDTO.class);
			logger.info("User with email: " + positionDTO.getEmail()
					+ " try to update hsi location.");

			User user = userService.getUserByEmail(positionDTO.getEmail());
			Position position = user.getPosition();
			if(position == null)
				position = new Position();
			position.setUser(user);
			position.setxCoordonate(positionDTO.getxPosition());
			position.setyCoordonate(positionDTO.getyPosition());

			coordCom.updateUserLocation(position);

			logger.info("Position was updated with succes");
			response.setMessage("Position was updated with succes");

			String result = null;
			result = objMapper.writeValueAsString(response);
			
			return Response.status(200).entity(result).build();
		} catch (JsonParseException e) {
			logger.error("Position can not be parsed from JSON in updatePosition method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e) {
			logger.error("Position can not be parsed from JSON in updatePosition method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e) {
			logger.error("Position can not be parsed from JSON in updatePosition method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}
}
