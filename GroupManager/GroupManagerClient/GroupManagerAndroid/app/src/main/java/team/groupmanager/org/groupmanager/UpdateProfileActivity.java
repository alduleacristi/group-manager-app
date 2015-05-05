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
        showMessageUtil = new ShowMessageUtil(handler,UpdateProfileActivity.this);
        sharedPreferencesUtil = new SharedPreferencesUtil(UpdateProfileActivity.this);

        final String email = sharedPreferencesUtil.getEmail();
        //String password = sharedPreferencesUtil.getPw();
        //String lastName = sharedPreferencesUtil.getLastName();
        //String firstName = sharedPreferencesUtil.getFirstName();
        TextView helloUser = (TextView) findViewById(R.id.helloUser);
        helloUser.setText(email);

        final ImageButton updateProfile = (ImageButton) findViewById(R.id.UpdateProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){
                EditText firstNameET = (EditText)findViewById(R.id.textFirstName);
                EditText lastNameET = (EditText)findViewById(R.id.textLastName);
                EditText passwordET = (EditText)findViewById(R.id.new_password);
                EditText retypedPasswordET = (EditText)findViewById(R.id.new_retyped_Password);
                EditText emailET = (EditText)findViewById(R.id.new_Email);
                UserDTO userDTO = null;
                List<UserDTO> users = null;
                try{
                UserCommunication userCommunication = new UserCommunication();
                users = userCommunication.getUsersByEmail(email, Constants.URL+ "/GroupManager/api/security/users/getUsers", "token");}
                catch (GroupManagerClientException e) {
                    showMessageUtil.showToast("Failed to get users.", Toast.LENGTH_SHORT);
                }
                if(users.size()>0)
                    userDTO = users.get(0);
                if(firstNameET.getText().toString()!="")
                    userDTO.setFirstName(firstNameET.getText().toString());
                if(lastNameET.getText().toString()!="")
                    userDTO.setLastName(lastNameET.getText().toString());
                if(emailET.getText().toString()!="")
                    userDTO.setEmail(emailET.getText().toString());
                if(passwordET.getText().toString()!="" && passwordET.getText().toString() == retypedPasswordET.getText().toString())
                    userDTO.setPassword(passwordET.getText().toString());
                try{
                AccountCommunications acc = new AccountCommunications();
                GroupManagerResponse resp = acc.updateUser(userDTO,
                        Constants.URL+"/GroupManager/api/security/users/updateUser","f865eab2-c1b7-427d-9287-998453793001");

                showMessageUtil.showToast("Profile updated!", Toast.LENGTH_SHORT);
                //System.out.println(resp.getMessage());
                //System.out.println(resp.getErrorMessage());
                }
                catch (GroupManagerClientException e) {
                showMessageUtil.showToast("Failed to update profile", Toast.LENGTH_SHORT);
                }
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
