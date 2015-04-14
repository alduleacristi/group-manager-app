package team.groupmanager.org.communications;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerGroupResponse;
import org.groupmanager.team.responses.GroupManagerResponseLogin;
import org.groupmanager.team.responses.GroupManagerResponseUsers;

import team.groupmanager.org.exceptions.GroupManagerClientException;

public class UserCommunication {
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

	public List<UserDTO> getUsersByEmail(String email, String url,
			String token) throws GroupManagerClientException {
        List<UserDTO> positions = null;
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(20, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();
        Response response = null;
        try {
            String emailJson = objMapper.writeValueAsString(email);
            RequestBody body = RequestBody.create(JSON, emailJson);
            Request request = new Request.Builder().addHeader("Authorization","token").url(url).post(body).build();
            response = client.newCall(request).execute();
            GroupManagerResponseUsers responseUsers = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerResponseUsers.class);
            return responseUsers.getUsers();
        } catch (IOException e) {
            //Log.e("MyApp",e.getMessage());
            throw new GroupManagerClientException("Failed to get users.");

        }
	}
}
