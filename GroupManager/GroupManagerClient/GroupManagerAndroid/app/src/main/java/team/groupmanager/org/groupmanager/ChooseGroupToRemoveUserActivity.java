package team.groupmanager.org.groupmanager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

import team.groupmanager.org.adapters.ChooseGroupArrayAdapter;
import team.groupmanager.org.communications.GroupCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.SharedPreferencesUtil;
import team.groupmanager.org.util.ShowMessageUtil;


public class ChooseGroupToRemoveUserActivity extends ListActivity {
    private List<GroupDTO> groups = new ArrayList<>();
    private GroupManagerClientException exc;
    private ShowMessageUtil showMessageUtil;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private String email,token;

    final private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesUtil = new SharedPreferencesUtil(ChooseGroupToRemoveUserActivity.this);
        showMessageUtil = new ShowMessageUtil(handler,ChooseGroupToRemoveUserActivity.this);
        email = sharedPreferencesUtil.getEmail();
        token = sharedPreferencesUtil.getToken();

        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.group_type_leave_group_layout, lv, false);
        lv.addHeaderView(header);

        Runnable runnable = new Runnable() {

            public void run() {
                GroupCommunications groupCommunications = new GroupCommunications();

                try {
                    groups = groupCommunications
                            .getGroupForUser(email,
                                    "http://groupmanagerservices-groupmanagerweb.rhcloud.com/GroupManager/api/security/groups/getGroups");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final ChooseGroupArrayAdapter adapter = new ChooseGroupArrayAdapter(ChooseGroupToRemoveUserActivity.this,groups);
                            setListAdapter(adapter);
                        }
                    });
                } catch (GroupManagerClientException e) {
                    showMessageUtil.showToast("Failed to get data",Toast.LENGTH_SHORT);
                }
                //handler.sendEmptyMessage(0);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                GroupDTO groupDTO = new GroupDTO();
                groupDTO.setId(groups.get(position - 1).getId());
                List<UserDTO> usersDTO = new ArrayList<UserDTO>();
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(email);
                usersDTO.add(userDTO);
                groupDTO.setUsers(usersDTO);

                GroupCommunications groupCommunications = new GroupCommunications();
                try {
                    groupCommunications.addUsersToGroup("http://groupmanagerservices-groupmanagerweb.rhcloud.com/GroupManager/api/security/groups/removeUsersFromGroup",groupDTO,token);
                    showMessageUtil.showToast("Uses removed with success.", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(ChooseGroupToRemoveUserActivity.this,MainMenuActivity.class);
                    startActivity(intent);
                } catch (GroupManagerClientException e) {
                    showMessageUtil.showToast(e, Toast.LENGTH_SHORT);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_group_to_remove_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_home: {
                Intent intent = new Intent(ChooseGroupToRemoveUserActivity.this,MainMenuActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
