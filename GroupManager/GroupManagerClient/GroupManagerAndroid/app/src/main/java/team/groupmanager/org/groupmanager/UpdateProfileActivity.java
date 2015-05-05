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

import org.groupmanager.team.dto.UserDTO;

import team.groupmanager.org.communications.LoginCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;
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

        String email = sharedPreferencesUtil.getEmail();
        //String password = sharedPreferencesUtil.getPw();
        //String lastName = sharedPreferencesUtil.getLastName();
        //String firstName = sharedPreferencesUtil.getFirstName();
        TextView helloUser = (TextView) findViewById(R.id.helloUser);
        helloUser.setText(email);

        final ImageButton updateProfile = (ImageButton) findViewById(R.id.UpdateProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                EditText firstNameET = (EditText)findViewById(R.id.textFirstName);
                EditText lastNameET = (EditText)findViewById(R.id.textLastName);
                EditText passwordET = (EditText)findViewById(R.id.new_password);
                EditText retypedPasswordET = (EditText)findViewById(R.id.new_retyped_Password);
                EditText emailET = (EditText)findViewById(R.id.new_Email);
                UserDTO user = new UserDTO();
                if(firstNameET.getText().toString()!="")
                    user.setFirstName(firstNameET.getText().toString());
                if(lastNameET.getText().toString()!="")
                    user.setLastName(lastNameET.getText().toString());
                if(emailET.getText().toString()!="")
                    user.setEmail(emailET.getText().toString());
                if(passwordET.getText().toString()!="" && passwordET.getText().toString() == retypedPasswordET.getText().toString())
                    user.setPassword(passwordET.getText().toString());

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
