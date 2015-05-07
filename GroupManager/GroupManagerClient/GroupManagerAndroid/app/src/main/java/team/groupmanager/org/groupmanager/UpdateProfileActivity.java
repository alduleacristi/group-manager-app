package team.groupmanager.org.groupmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.groupmanager.team.dto.UserDTO;

import team.groupmanager.org.communications.AccountCommunications;
import org.groupmanager.team.responses.GroupManagerResponse;

import java.util.List;

import team.groupmanager.org.communications.UserCommunication;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.Constants;
import team.groupmanager.org.util.SharedPreferencesUtil;
import team.groupmanager.org.util.ShowMessageUtil;


public class UpdateProfileActivity extends ActionBarActivity {
    private String msg;
    private GroupManagerClientException exc;
    private ShowMessageUtil showMessageUtil;
    private SharedPreferencesUtil sharedPreferencesUtil;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_update_profile);
        showMessageUtil = new ShowMessageUtil(handler, UpdateProfileActivity.this);
        sharedPreferencesUtil = new SharedPreferencesUtil(UpdateProfileActivity.this);

        final String email = sharedPreferencesUtil.getEmail();

        final EditText firstNameET = (EditText) findViewById(R.id.textFirstName);
        final EditText lastNameET = (EditText) findViewById(R.id.textLastName);
        final EditText passwordET = (EditText) findViewById(R.id.new_password);
        final EditText retypedPasswordET = (EditText) findViewById(R.id.new_retyped_Password);
        final EditText emailET = (EditText) findViewById(R.id.new_Email);
        final EditText oldPasswordET = (EditText) findViewById(R.id.oldPassword);

        Runnable runnableComplete = new Runnable() {
            @Override
            public void run() {

                try {
                    UserCommunication userCommunication = new UserCommunication();
                    final List<UserDTO> users = userCommunication.getUsersByEmail(email, Constants.URL + "/GroupManager/api/security/users/getUsers", sharedPreferencesUtil.getToken());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         UserDTO userDTO = null;
                         if (users.size() > 0) {
                            userDTO = users.get(0);
                            firstNameET.setText(userDTO.getFirstName());
                            lastNameET.setText(userDTO.getLastName());
                            emailET.setText(userDTO.getEmail());
                            emailET.setFocusable(false);
                            emailET.setClickable(false);
                            }
                        }
                    });
                } catch (GroupManagerClientException e) {
                    showMessageUtil.showToast("Failed to get users.", Toast.LENGTH_SHORT);
                }
            }};

        Thread threadComplete = new Thread(runnableComplete);
        threadComplete.start();

        final ImageButton updateProfile = (ImageButton) findViewById(R.id.UpdateProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Runnable runnable = new Runnable() {
                        @Override
                    public void run() {
                        UserDTO userDTO = null;
                        List<UserDTO> users = null;

                        try {
                            UserCommunication userCommunication = new UserCommunication();
                            users = userCommunication.getUsersByEmail(email, Constants.URL + "/GroupManager/api/security/users/getUsers", sharedPreferencesUtil.getToken());
                        } catch (GroupManagerClientException e) {
                            showMessageUtil.showToast("Failed to get users.", Toast.LENGTH_SHORT);
                        }
                        if (users.size() > 0)
                            userDTO = users.get(0);
                        if (!firstNameET.getText().toString().equals(""))
                            userDTO.setFirstName(firstNameET.getText().toString());
                        if (!lastNameET.getText().toString().equals(""))
                            userDTO.setLastName(lastNameET.getText().toString());
                        if (!emailET.getText().toString().equals(""))
                            userDTO.setEmail(emailET.getText().toString());
                        if ((passwordET.getText().toString().equals("")==false) && passwordET.getText().toString().equals(retypedPasswordET.getText().toString()))
                            if(oldPasswordET.getText().toString().equals(userDTO.getPassword()))
                                userDTO.setPassword(passwordET.getText().toString());
                        try {
                            AccountCommunications acc = new AccountCommunications();
                            GroupManagerResponse resp = acc.updateUser(userDTO,
                                    Constants.URL + "/GroupManager/api/security/users/updateUser", sharedPreferencesUtil.getToken());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showMessageUtil.showToast("Profile updated!", Toast.LENGTH_SHORT);
                                }
                            });

                        } catch (GroupManagerClientException e) {
                            showMessageUtil.showToast("Failed to update profile", Toast.LENGTH_SHORT);
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
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_home: {
                Intent intent = new Intent(UpdateProfileActivity.this,MainMenuActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_profile:{
                Intent intent = new Intent(UpdateProfileActivity.this,UpdateProfileActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
