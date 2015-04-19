package team.groupmanager.org.communications;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponseLogin;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import team.groupmanager.org.exceptions.GroupManagerClientException;

public class LoginCommunications {
	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

    public GroupManagerResponseLogin login(UserDTO userDTO, String url)
            throws GroupManagerClientException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(20, TimeUnit.SECONDS);
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
            return responseLogin;
        } catch (JsonGenerationException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (JsonMappingException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (IOException e) {
            e.printStackTrace();
            throw new GroupManagerClientException("Failed to authenticate");
        }

    }


    public GroupManagerResponseLogin logout(String token, String url)
            throws GroupManagerClientException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(20, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();
        String jsonString;
        try {
            jsonString = objMapper.writeValueAsString(token);
            RequestBody body = RequestBody.create(JSON, jsonString);
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            response.code();
            GroupManagerResponseLogin responseLogin = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerResponseLogin.class);
            return responseLogin;
        } catch (JsonGenerationException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (JsonMappingException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (IOException exc) {
            throw new GroupManagerClientException("Failed to authenticate",exc);
        }

    }


    public void test() {
		System.out.println("Test method");
	}
}
