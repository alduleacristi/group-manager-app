package team.groupmanager.org.communications;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.PositionDTO;
import org.groupmanager.team.responses.GroupManagerGroupResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import team.groupmanager.org.exceptions.GroupManagerClientException;

/**
 * Created by Cristi on 4/4/2015.
 */
public class GroupCommunications {
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    public List<PositionDTO> getPositionForGroup(Long groupId,String url) throws GroupManagerClientException {
        List<PositionDTO> positions = null;
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.setReadTimeout(5, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();
        Response response = null;
        try {
            String idGroupStr = objMapper.writeValueAsString(groupId);
            RequestBody body = RequestBody.create(JSON, idGroupStr);
            Request request = new Request.Builder().addHeader("Authorization","token").url(url).post(body).build();
            response = client.newCall(request).execute();
            Log.e("TAG","code");
            GroupManagerGroupResponse responseLogin = objMapper.readValue(
					response.body().byteStream(),
					GroupManagerGroupResponse.class);
			return responseLogin.getPositions();
        } catch (IOException e) {
            //Log.e("MyApp",e.getMessage());
            throw new GroupManagerClientException("Failed to get positions for group.");

        }
    }

    public List<GroupDTO> getGroupForUser(String email,String url) throws GroupManagerClientException {
        List<GroupDTO> positions = null;
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
            GroupManagerGroupResponse responseLogin = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerGroupResponse.class);
            return responseLogin.getGroups();
        } catch (IOException e) {
            //Log.e("MyApp",e.getMessage());
            throw new GroupManagerClientException("Failed to get groups for user.");

        }
    }

    public GroupManagerGroupResponse createGroup(String url, GroupDTO groupDTO,String token)
            throws GroupManagerClientException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.setReadTimeout(5, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();

        String jsonString;
        try {
            jsonString = objMapper.writeValueAsString(groupDTO);
            RequestBody body = RequestBody.create(JSON, jsonString);
            Request request = new Request.Builder().addHeader("Authorization", token).url(url).post(body).build();
            Response response = client.newCall(request).execute();

            GroupManagerGroupResponse responseGroup = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerGroupResponse.class);
            return responseGroup;
        } catch (JsonGenerationException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (JsonMappingException e) {
            throw new GroupManagerClientException("Failed to authenticate");
        } catch (IOException e) {
            throw new GroupManagerClientException("Failed to authenticate",e);
        }
    }

    public String addUsersToGroup(String url, GroupDTO groupDTO,String token)
            throws GroupManagerClientException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.setReadTimeout(5, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();

        String jsonString;
        try {
            jsonString = objMapper.writeValueAsString(groupDTO);
            RequestBody body = RequestBody.create(JSON, jsonString);
            Request request = new Request.Builder().addHeader("Authorization", token).url(url).post(body).build();
            Response response = client.newCall(request).execute();

            GroupManagerGroupResponse responseGroup = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerGroupResponse.class);
            return responseGroup.getMessage();
        } catch (JsonGenerationException e) {
            throw new GroupManagerClientException("Failed to authenticate",e);
        } catch (JsonMappingException e) {
            throw new GroupManagerClientException("Failed to authenticate",e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GroupManagerClientException("Failed to authenticate",e);
        }
    }

    public String removeUsersFromGroup(String url, GroupDTO groupDTO,String token)
            throws GroupManagerClientException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.setReadTimeout(5, TimeUnit.SECONDS);
        ObjectMapper objMapper = new ObjectMapper();

        String jsonString;
        try {
            jsonString = objMapper.writeValueAsString(groupDTO);
            RequestBody body = RequestBody.create(JSON, jsonString);
            Request request = new Request.Builder().addHeader("Authorization", token).url(url).post(body).build();
            Response response = client.newCall(request).execute();

            GroupManagerGroupResponse responseGroup = objMapper.readValue(
                    response.body().byteStream(),
                    GroupManagerGroupResponse.class);
            return responseGroup.getMessage();
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
