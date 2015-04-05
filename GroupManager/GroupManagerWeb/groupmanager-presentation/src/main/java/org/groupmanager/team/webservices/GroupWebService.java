package org.groupmanager.team.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.common.ErrorList;
import org.groupmanager.team.convertors.UserConvertor;
import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.PositionDTO;
import org.groupmanager.team.group.GroupService;
import org.groupmanager.team.model.Group;
import org.groupmanager.team.model.User;
import org.groupmanager.team.responses.GroupManagerGroupResponse;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/security/groups")
public class GroupWebService {
	private Logger logger = LoggerFactory.getLogger(GroupWebService.class);

	@Inject
	private GroupService groupService;
	@Inject
	private UserService userService;

	@POST
	@Path("/addGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addGroup(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		try {
			GroupDTO groupDTO = objMapper.readValue(incomingData,
					GroupDTO.class);
			logger.info("User with email: " + groupDTO.getOwner().getEmail()
					+ " try to create a new group with name: "
					+ groupDTO.getName());
			Group group = new Group();
			group.setName(groupDTO.getName());
			User owner = userService.getUserByEmail(groupDTO.getOwner()
					.getEmail());
			group.setOwner(owner);
			Long groupId = groupService.addGroup(group);
			response.setMessage("Group was created succesfully");
			response.setGroupId(groupId);

			String result = null;
			result = objMapper.writeValueAsString(response);
			return Response.status(200).entity(result).build();
		} catch (JsonParseException e) {
			logger.error("Group can not be parsed from JSON in addGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e) {
			logger.error("Group can not be parsed from JSON in addGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e) {
			logger.error("Group can not be parsed from JSON in addGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}

	@POST
	@Path("/getPositions")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getPositionForGroup(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		try {
			Long idGroup = objMapper.readValue(incomingData, Long.class);
			List<User> users = groupService.getUserForGroup(idGroup);
			List<PositionDTO> positions = new ArrayList<PositionDTO>();
			for (User user : users) {
				PositionDTO poz = new PositionDTO(user.getId(), user
						.getPosition().getxCoordonate(), user.getPosition()
						.getyCoordonate());
				positions.add(poz);
			}
			response.setPositions(positions);
			String result = objMapper.writeValueAsString(response);
			return Response.status(200).entity(result).build();
		} catch (JsonParseException e) {
			logger.error("Group can not be parsed from JSON in getPosition method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e) {
			logger.error("Group can not be parsed from JSON in getPosition method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e) {
			logger.error("Group can not be parsed from JSON in getPosition method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}

	@POST
	@Path("/getGroups")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getGroupsForUser(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		
		try {
			String email = objMapper.readValue(incomingData,
					String.class);
			System.out.println("!!!! Email recived!!!!: "+email);
			List<Group> groups = groupService.getGroupForUser(email);
			List<GroupDTO> groupsDTO = new ArrayList<GroupDTO>();

			for (Group group : groups) {
				GroupDTO groupDTO = new GroupDTO();
				groupDTO.setId(group.getId());
				groupDTO.setName(group.getName());
				groupDTO.setOwner(UserConvertor.convertToUserDTO(group.getOwner()));
				groupsDTO.add(groupDTO);
			}

			response.setGroups(groupsDTO);
			
			String result = objMapper.writeValueAsString(response);
			return Response.status(200).entity(result).build();
		} catch (JsonGenerationException e) {
			logger.error("Group can not be parsed from JSON in getGroups method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e) {
			logger.error("Group can not be parsed from JSON in getGroups method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e) {
			logger.error("Group can not be parsed from JSON in getGroups method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}
}
