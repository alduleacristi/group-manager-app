package team.groupmanager.org.groupmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.groupmanager.team.common.ErrorList;
import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerGroupResponse;

import team.groupmanager.org.communications.GroupCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.Constants;
import team.groupmanager.org.util.SharedPreferencesUtil;
import team.groupmanager.org.util.ShowMessageUtil;


public class CreateGroupActivity extends Activity {
    private String msg;
    private GroupManagerClientException exc;
    private ShowMessageUtil showMessageUtil;
    private SharedPreferencesUtil sharedPreferencesUtil;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        // getActionBar().hide();
        showMessageUtil = new ShowMessageUtil(handler,CreateGroupActivity.this);
        sharedPreferencesUtil = new SharedPreferencesUtil(CreateGroupActivity.this);

        final EditText groupName = (EditText) findViewById(R.id.groupNameEdit);
        final ImageButton creatGroup = (ImageButton) findViewById(R.id.createGroup);
        //final ImageButton login = (ImageButton) findViewById(R.id.loginButton);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBarLogin);

        creatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(creatGroup.getWindowToken(), 0);
                spinner.setVisibility(View.VISIBLE);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        boolean ok = true;
                        if(groupName.getText().toString().length() < 3){
                            showMessageUtil.showToast("Group name must have at least 3 characters",Toast.LENGTH_SHORT);
                            ok = false;
                        }

                        String token = sharedPreferencesUtil.getToken();
                        String email = sharedPreferencesUtil.getEmail();
                        GroupCommunications groupCommunications = new GroupCommunications();
                        UserDTO owner = new UserDTO();
                        owner.setEmail(email);
                        GroupDTO groupDTO = new GroupDTO();
                        groupDTO.setName(groupName.getText().toString());
                        groupDTO.setOwner(owner);

                        try {
                            if(ok) {
                                GroupManagerGroupResponse response = groupCommunications.createGroup(Constants.URL+"/GroupManager/api/security/groups/addGroup", groupDTO, token);
                                if (response.getError() == ErrorList.DUPLICATE_GROUP) {
                                    showMessageUtil.showToast("Group with name " + groupDTO.getName() + " already exist.", Toast.LENGTH_SHORT);
                                }else{
                                    showMessageUtil.showToast("Group created with success", Toast.LENGTH_SHORT);
                                    Intent intent = new Intent(CreateGroupActivity.this, MainMenuActivity.class);
                                    startActivity(intent);
                                }
                            }
                        } catch (GroupManagerClientException e) {
                            showMessageUtil.showToast(e,Toast.LENGTH_SHORT);
                        }finally {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spinner.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_home: {
                Intent intent = new Intent(CreateGroupActivity.this,MainMenuActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
