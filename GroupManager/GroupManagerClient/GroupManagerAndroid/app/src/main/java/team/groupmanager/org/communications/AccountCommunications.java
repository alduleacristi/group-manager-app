package team.groupmanager.org.communications;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponse;

public class AccountCommunications {
	public void sendAddAccount(UserDTO userDTO, String url) {
//		ObjectMapper objMapper = new ObjectMapper();
//		ClientConfig clientConfig = new DefaultClientConfig();
//		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
//				Boolean.TRUE);
//		Client client = Client.create(clientConfig);
//
//		WebResource webResource = client.resource(url);
//
//		ClientResponse response = webResource.accept("application/json")
//				.type("application/json").post(ClientResponse.class, userDTO);
//
//		if (response.getStatus() != 200) {
//			throw new RuntimeException("Failed : HTTP error code : "
//					+ response.getStatus());
//		}
//
//		GroupManagerResponse output = response.getEntity(GroupManagerResponse.class);

		System.out.println("Server response .... \n");
	}
}
