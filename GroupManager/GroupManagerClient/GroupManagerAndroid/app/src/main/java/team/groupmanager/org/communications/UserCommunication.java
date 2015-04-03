package team.groupmanager.org.communications;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponseLogin;
import org.groupmanager.team.responses.GroupManagerResponseUsers;

public class UserCommunication {
	public List<UserDTO> getUsersByEmail(UserDTO userDTO, String url,
			String token) {
		// ClientConfig clientConfig = new DefaultClientConfig();
		// clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
		// Boolean.TRUE);
		// Client client = Client.create(clientConfig);
		//
		// WebResource webResource = client.resource(url);
		//
		// ClientResponse response = webResource.accept("application/json")
		// .type("application/json").header("Authorization", token)
		// .post(ClientResponse.class, userDTO);
		//
		// if (response.getStatus() != 200) {
		// throw new RuntimeException("Failed : HTTP error code : "
		// + response.getStatus());
		// }
		//
		// GroupManagerResponseUsers output = response
		// .getEntity(GroupManagerResponseUsers.class);
		//
		// System.out.println("Server response .... \n");
		// System.out.println(output.getUsers());
		//
		// return output.getUsers();

		return null;
	}
}
