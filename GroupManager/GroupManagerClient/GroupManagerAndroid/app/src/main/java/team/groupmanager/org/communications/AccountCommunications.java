package team.groupmanager.org.communications;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponse;

import team.groupmanager.org.exceptions.GroupManagerClientException;

public class AccountCommunications {
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    public GroupManagerResponse sendAddAccount(UserDTO userDTO, String url) throws GroupManagerClientException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(20, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();

        String jsonString;
        try {
            jsonString = objMapper.writeValueAsString(userDTO);
            System.out.println(jsonString);
            RequestBody body = RequestBody.create(JSON, jsonString);
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            System.out.println(response.isSuccessful());

            GroupManagerResponse responseGroup = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerResponse.class);
            return responseGroup;
        } catch (JsonGenerationException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (JsonMappingException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (IOException e) {
            e.printStackTrace();
            throw new GroupManagerClientException("Failed to authenticate");
        }
    }

}
