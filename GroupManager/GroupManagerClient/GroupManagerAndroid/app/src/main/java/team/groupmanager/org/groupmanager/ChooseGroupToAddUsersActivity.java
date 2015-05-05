package team.groupmanager.org.groupmanager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.groupmanager.team.dto.GroupDTO;

import java.util.ArrayList;
import java.util.List;

import team.groupmanager.org.adapters.ChooseGroupArrayAdapter;
import team.groupmanager.org.communications.GroupCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.Constants;
import team.groupmanager.org.util.SharedPreferencesUtil;
import team.groupmanager.org.util.ShowMessageUtil;

public class ChooseGroupToAddUsersActivity extends ListActivity {
    private List<GroupDTO> groups = new ArrayList<>();
    private GroupManagerClientException exc;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ShowMessageUtil showMessageUtil;
    private String email;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        sharedPreferencesUtil = new SharedPreferencesUtil(ChooseGroupToAddUsersActivity.this);
        showMessageUtil = new ShowMessageUtil(handler,ChooseGroupToAddUsersActivity.this);
        email = sharedPreferencesUtil.getEmail();

        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.group_type_add_user_to_group_layout, lv, false);
        lv.addHeaderView(header);

        Runnable runnable = new Runnable() {

            public void run() {
                GroupCommunications groupCommunications = new GroupCommunications();

                try {
                    groups = groupCommunications
                            .getGroupForUser(email,
                                    Constants.URL+"/GroupManager/api/security/groups/getGroups");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final ChooseGroupArrayAdapter adapter = new ChooseGroupArrayAdapter(ChooseGroupToAddUsersActivity.this,groups);
                            setListAdapter(adapter);
                        }
                    });
                } catch (GroupManagerClientException e) {
                    showMessageUtil.showToast("Failed to get data", Toast.LENGTH_SHORT);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(ChooseGroupToAddUsersActivity.this,
                AddUserToGroupActivity.class);
        GroupDTO group = (GroupDTO) getListAdapter().getItem(position-1);
        intent.putExtra("groupId",group.getId());

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_group_to_add_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_home: {
                Intent intent = new Intent(ChooseGroupToAddUsersActivity.this,MainMenuActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
