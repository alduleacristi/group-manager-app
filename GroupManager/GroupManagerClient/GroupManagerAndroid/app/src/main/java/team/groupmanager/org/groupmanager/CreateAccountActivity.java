package team.groupmanager.org.groupmanager;

import android.app.AlertDialog;
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

import org.groupmanager.team.dto.UserDTO;

import team.groupmanager.org.communications.AccountCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.Constants;
import team.groupmanager.org.util.ShowMessageUtil;


public class CreateAccountActivity extends ActionBarActivity {
    private final Handler handler = new Handler();
    private GroupManagerClientException exc;
    private ShowMessageUtil showMessageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().hide();
        showMessageUtil = new ShowMessageUtil(handler,CreateAccountActivity.this);

        final EditText email = (EditText) findViewById(R.id.textEmail);
        final EditText pass = (EditText) findViewById(R.id.password);
        final EditText firstNameET = (EditText) findViewById(R.id.firstName);
        final EditText lastNameET = (EditText) findViewById(R.id.lastName);
        final EditText retypePass = (EditText) findViewById(R.id.retypePassword);
        final ImageButton createAccount = (ImageButton) findViewById(R.id.createAccount);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBarLogin);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(createAccount.getWindowToken(), 0);

               spinner.setVisibility(View.VISIBLE);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        boolean ok = true;
                        if (!pass.getText().toString()
                                .equals(retypePass.getText().toString())) {
                            showMessageUtil.showToast("Password and retype password must be the same", Toast.LENGTH_SHORT);
                            ok = false;
                        }
                        if (pass.getText().toString().equals("")) {
                            showMessageUtil.showToast("You must type a password",Toast.LENGTH_SHORT);
                            ok = false;
                        }
                        if (email.getText().toString().equals("")) {
                            showMessageUtil.showToast("You must type a email",Toast.LENGTH_SHORT);
                            ok = false;
                        }

                        AccountCommunications accountCommunications = new AccountCommunications();
                        try {
                            if(ok) {
                                UserDTO userDTO = new UserDTO();
                                userDTO.setEmail(email.getText().toString());
                                userDTO.setPassword(pass.getText().toString());
                                userDTO.setFirstName(firstNameET.getText().toString());
                                userDTO.setLastName(lastNameET.getText().toString());

                                accountCommunications.sendAddAccount(userDTO, Constants.URL+"/GroupManager/api/users/addUser");
                                Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        } catch (GroupManagerClientException e) {
                            showMessageUtil.showToast(e.getMessage(),Toast.LENGTH_SHORT);
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
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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
