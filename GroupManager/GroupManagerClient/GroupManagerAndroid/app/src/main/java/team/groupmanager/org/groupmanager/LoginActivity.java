package team.groupmanager.org.groupmanager;

import org.groupmanager.team.dto.UserDTO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import team.groupmanager.org.communications.LoginCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;

public class LoginActivity extends Activity {
    private GroupManagerClientException exc;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    LoginActivity.this);

            if (exc != null) {
                builder.setMessage(exc.getMessage());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    };

    private void saveToken(String token) {
        SharedPreferences tokenPref = getSharedPreferences("TokenPref",
                Context.MODE_PRIVATE);
        Editor tokenEditor = tokenPref.edit();
        tokenEditor.putString("Token", token);
        tokenEditor.commit();
    }

    private String getToken() {
        SharedPreferences tokenPref = getSharedPreferences("TokenPref",
                Context.MODE_PRIVATE);
        return tokenPref.getString("Token", "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String savedToken = getToken();
        if (!savedToken.equals("")) {
            Intent intent = new Intent(LoginActivity.this,
                    MainMenuActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Runnable runnable = new Runnable() {

                    public void run() {
                        UserDTO user = new UserDTO();
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());

                        LoginCommunications loginCommunications = new LoginCommunications();

                        try {
                            String token = loginCommunications
                                    .login(user,
                                            "http://10.0.2.2:8080/GroupManager/api/login");
                            saveToken(token);

                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    MainMenuActivity.class);
                            startActivity(intent);
                        } catch (GroupManagerClientException e) {
                            exc = e;
                        }
                        handler.sendEmptyMessage(0);
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        });

        TextView textView = (TextView) findViewById(R.id.createAccount);
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
