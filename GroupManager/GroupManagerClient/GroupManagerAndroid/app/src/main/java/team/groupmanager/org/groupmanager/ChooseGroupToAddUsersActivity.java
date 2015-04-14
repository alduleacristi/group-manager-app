package team.groupmanager.org.groupmanager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.groupmanager.team.dto.GroupDTO;

import java.util.ArrayList;
import java.util.List;

import team.groupmanager.org.adapters.ChooseGroupArrayAdapter;
import team.groupmanager.org.communications.GroupCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;


public class ChooseGroupToAddUsersActivity extends ListActivity {
    private List<GroupDTO> groups = new ArrayList<>();
    private GroupManagerClientException exc;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ChooseGroupToAddUsersActivity.this);

            if (exc != null) {
                builder.setMessage(exc.getMessage());

                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                builder.setMessage("Data load successfuly");

                AlertDialog dialog = builder.create();
                dialog.show();
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Runnable runnable = new Runnable() {

            public void run() {
                GroupCommunications groupCommunications = new GroupCommunications();

                try {
                    groups = groupCommunications
                            .getGroupForUser("alduleacristi@yahoo.com",
                                    "http://groupmanagerservices-groupmanagerweb.rhcloud.com/GroupManager/api/security/groups/getGroups");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final ChooseGroupArrayAdapter adapter = new ChooseGroupArrayAdapter(ChooseGroupToAddUsersActivity.this,groups);
                            setListAdapter(adapter);
                        }
                    });
                } catch (GroupManagerClientException e) {
                    exc = e;
                }
                handler.sendEmptyMessage(0);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(ChooseGroupToAddUsersActivity.this,
                AddUserToGroupActivity.class);
        GroupDTO group = (GroupDTO) getListAdapter().getItem(position);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
