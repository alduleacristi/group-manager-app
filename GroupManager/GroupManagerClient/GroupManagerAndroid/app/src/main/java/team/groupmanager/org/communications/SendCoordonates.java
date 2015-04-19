package team.groupmanager.org.communications;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.PositionDTO;
import org.groupmanager.team.responses.GroupManagerResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import team.groupmanager.org.exceptions.GroupManagerClientException;

public class SendCoordonates {
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    public GroupManagerResponse sendLocation(PositionDTO positionDTO, String url, String token)
            throws GroupManagerClientException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(20, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();
        String jsonString;
        try {
            jsonString = objMapper.writeValueAsString(positionDTO);
            RequestBody body = RequestBody.create(JSON, jsonString);
            Request request = new Request.Builder().addHeader("Authorization", token).url(url).post(body).build();
            Response response = client.newCall(request).execute();
            GroupManagerResponse responseLogin = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerResponse.class);
            return responseLogin;
        } catch (JsonGenerationException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (IOException e) {
            e.printStackTrace();
            throw new GroupManagerClientException("Failed to authenticate");
        }

    }
}
