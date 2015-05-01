package org.groupmanager.team.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.PositionDTO;
import org.groupmanager.team.dto.UserDTO;
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response addGroup(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		try {
			GroupDTO groupDTO = objMapper.readValue(incomingData,
					GroupDTO.class);
			logger.info("User with email: " + groupDTO.getOwner().getEmail()
					+ " try to create a new group with name: "
					+ groupDTO.getName());
			Group oldGroup = groupService.getGroupByName(groupDTO.getName());
			if (oldGroup != null) {
				logger.info("Group with name: " + groupDTO.getName()
						+ " already exist");
				response.setErrorMessage("Group with name: " + groupDTO.getName()
						+ " already exist");
				response.setError(ErrorList.DUPLICATE_GROUP);
			} else {
				Group group = new Group();
				group.setName(groupDTO.getName());
				User owner = userService.getUserByEmail(groupDTO.getOwner()
						.getEmail());
				group.setOwner(owner);
				group.addUserToGroup(owner);
				Long groupId = groupService.addGroup(group);
				logger.info("Group was created with succes");
				response.setMessage("Group was created succesfully");
				response.setGroupId(groupId);
			}

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
				PositionDTO poz = new PositionDTO(user.getEmail(), user
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
			String email = objMapper.readValue(incomingData, String.class);
			List<Group> groups = groupService.getGroupForUser(email);
			List<GroupDTO> groupsDTO = new ArrayList<GroupDTO>();

			for (Group group : groups) {
				GroupDTO groupDTO = new GroupDTO();
				groupDTO.setId(group.getId());
				groupDTO.setName(group.getName());
				groupDTO.setOwner(UserConvertor.convertToUserDTO(group
						.getOwner()));
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

	@POST
	@Path("/addUsersToGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUsersToGroup(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		logger.info("Try to add user to group");
		try {
			GroupDTO groupDTO = objMapper.readValue(incomingData,
					GroupDTO.class);

			Group group = groupService.getGroupById(groupDTO.getId());
			List<User> users = new ArrayList<User>();
			List<UserDTO> usersDTO = groupDTO.getUsers();
			for (UserDTO userDTO : usersDTO) {
				User user = userService.getUserByEmail(userDTO.getEmail());
				users.add(user);
			}

			groupService.addUsersToGroup(group, users);
			logger.info("Users added successfully");
			response.setMessage("Users added with success");

			String result = objMapper.writeValueAsString(response);
			return Response.status(200).entity(result).build();
		} catch (JsonGenerationException e) {
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
	@Path("/acceptPendingGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response acceptAddToGroup(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		logger.info("Try to add user to group");
		try {
			@SuppressWarnings("unchecked")
			UserDTO userDTO = objMapper.readValue(incomingData,
					UserDTO.class);

			List<GroupDTO> groupsDTO = userDTO.getPendingGroups();
			List<Group> groups = new ArrayList<Group>();
			for(GroupDTO groupDTO:groupsDTO){
				Group group = groupService.getGroupById(groupDTO.getId());
				groups.add(group);
			}

			User user = userService.getUserByEmail(userDTO.getEmail());
			groupService.acceptPendingGroup(groups, user);
			logger.info("Users added successfully");
			response.setMessage("Users added with success");

			String result = objMapper.writeValueAsString(response);
			return Response.status(200).entity(result).build();
		} catch (JsonGenerationException e) {
			logger.error("Group can not be parsed from JSON in acceptAddToGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e) {
			logger.error("Group can not be parsed from JSON in acceptAddToGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e) {
			logger.error("Group can not be parsed from JSON in acceptAddToGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}

	@POST
	@Path("/removeUsersFromGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeUsersFromGroup(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		logger.info("Try to remove user from group");
		try {
			GroupDTO groupDTO = objMapper.readValue(incomingData,
					GroupDTO.class);

			Group group = groupService.getGroupById(groupDTO.getId());
			List<User> users = new ArrayList<User>();
			List<UserDTO> usersDTO = groupDTO.getUsers();
			for (UserDTO userDTO : usersDTO) {
				User user = userService.getUserByEmail(userDTO.getEmail());
				users.add(user);
			}

			groupService.removeUsersFromGroup(group, users);
			logger.info("Users was removed successfully");
			response.setMessage("Users removed with success");

			String result = objMapper.writeValueAsString(response);
			return Response.status(200).entity(result).build();
		} catch (JsonGenerationException e) {
			logger.error("Group can not be parsed from JSON in removeUsersFromGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (JsonMappingException e) {
			logger.error("Group can not be parsed from JSON in removeUsersFromGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		} catch (IOException e) {
			logger.error("Group can not be parsed from JSON in removeUsersFromGroup method");
			response.setError(ErrorList.JSON_PARSER);
			response.setMessage("Internal problem with the server.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(response).build();
		}
	}
	
	@POST
	@Path("/addUsersToPendingGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUsersToPendingGroup(InputStream incomingData) {
		ObjectMapper objMapper = new ObjectMapper();
		GroupManagerGroupResponse response = new GroupManagerGroupResponse();
		logger.info("Try to add user to group");
		try {
			GroupDTO groupDTO = objMapper.readValue(incomingData,
					GroupDTO.class);

			Group group = groupService.getGroupById(groupDTO.getId());
			List<User> users = new ArrayList<User>();
			List<UserDTO> usersDTO = groupDTO.getUsers();
			for (UserDTO userDTO : usersDTO) {
				User user = userService.getUserByEmail(userDTO.getEmail());
				users.add(user);
			}

			groupService.addUsersToPendingGroup(group, users);
			logger.info("Users added successfully");
			response.setMessage("Users added with success");

			String result = objMapper.writeValueAsString(response);
			return Response.status(200).entity(result).build();
		} catch (JsonGenerationException e) {
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
}
