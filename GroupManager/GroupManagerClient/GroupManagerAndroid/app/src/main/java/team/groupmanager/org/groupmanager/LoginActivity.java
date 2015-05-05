package team.groupmanager.org.groupmanager;

import org.groupmanager.team.common.ErrorList;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.responses.GroupManagerResponseLogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import team.groupmanager.org.communications.LoginCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.Constants;
import team.groupmanager.org.util.SharedPreferencesUtil;
import team.groupmanager.org.util.ShowMessageUtil;

public class LoginActivity extends ActionBarActivity {
    private GroupManagerClientException exc;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ShowMessageUtil showMessageUtil;

    private final Handler handler = new Handler();

    private String getToken() {
        SharedPreferences tokenPref = getSharedPreferences("TokenPref",
                Context.MODE_PRIVATE);
        return tokenPref.getString("Token", "");
    }

    @Override
    public void onBackPressed() {
        //Disabled back button from Login
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        sharedPreferencesUtil = new SharedPreferencesUtil(LoginActivity.this);
        showMessageUtil = new ShowMessageUtil(handler,LoginActivity.this);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final ImageButton login = (ImageButton) findViewById(R.id.loginButton);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBarLogin);

        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(login.getWindowToken(), 0);
                spinner.setVisibility(View.VISIBLE);

                Runnable runnable = new Runnable() {

                    public void run() {
                        UserDTO user = new UserDTO();
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());

                        LoginCommunications loginCommunications = new LoginCommunications();

                        try {
                            GroupManagerResponseLogin response = loginCommunications
                                    .login(user,
                                            Constants.URL+"/GroupManager/api/login");
                            if(response.getError() == ErrorList.FAILED_TO_AUTHENTICATE){
                                showMessageUtil.showToast(response.getErrorMessage(), Toast.LENGTH_SHORT);
                                return;
                            }
                            sharedPreferencesUtil.setToken(response.getToken());
                            sharedPreferencesUtil.setEmail(user.getEmail());

                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    MainMenuActivity.class);
                            startActivity(intent);
                        } catch (GroupManagerClientException e) {
                            showMessageUtil.showToast("Failed to authenticate", Toast.LENGTH_SHORT);
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

        ImageButton textView = (ImageButton) findViewById(R.id.createAccount);
        textView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        CreateAccountActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(
        // org.groupmanager.team.groupmanager_client.R.menu.main, menu);
        return true;
    }

}
