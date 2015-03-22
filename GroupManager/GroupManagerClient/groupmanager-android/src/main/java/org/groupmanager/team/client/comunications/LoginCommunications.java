package org.groupmanager.team.client.comunications;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.client.exceptions.GroupManagerClientException;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponseLogin;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class LoginCommunications {
	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

	public String login(UserDTO userDTO, String url)
			throws GroupManagerClientException {
		OkHttpClient client = new OkHttpClient();
		ObjectMapper objMapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = objMapper.writeValueAsString(userDTO);
			RequestBody body = RequestBody.create(JSON, jsonString);
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			GroupManagerResponseLogin responseLogin = objMapper.readValue(
					response.body().byteStream(),
					GroupManagerResponseLogin.class);
			return responseLogin.getToken();
		} catch (JsonGenerationException e) {
			throw new GroupManagerClientException("Failed to authenticate");
		} catch (JsonMappingException e) {
			throw new GroupManagerClientException("Failed to authenticate");
		} catch (IOException e) {
			throw new GroupManagerClientException("Failed to authenticate");
		}

	}

	public void test() {
		System.out.println("Test method");
	}
}
