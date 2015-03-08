package org.groupmanager.team.comunications;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class AccountCommunications {
	public void sendAddAccount(UserDTO userDTO, String url) {
		ObjectMapper objMapper = new ObjectMapper();
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.accept("application/json")
				.type("application/json").post(ClientResponse.class, userDTO);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		GroupManagerResponse output = response.getEntity(GroupManagerResponse.class);

		System.out.println("Server response .... \n");
	}
}
