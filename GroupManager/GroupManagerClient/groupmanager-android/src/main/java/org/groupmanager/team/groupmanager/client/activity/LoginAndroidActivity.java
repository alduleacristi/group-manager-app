package org.groupmanager.team.groupmanager.client.activity;

import org.groupmanager.team.client.comunications.LoginCommunications;
import org.groupmanager.team.client.exceptions.GroupManagerClientException;
import org.groupmanager.team.dto.UserDTO;
import org.groupmanager.team.groupmanager_client.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginAndroidActivity extends Activity {
	private GroupManagerClientException exc;
	private String response;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message message) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginAndroidActivity.this);

			if (exc != null)
				builder.setMessage(exc.getMessage());
			else
				builder.setMessage("Token returned: " + response);

			AlertDialog dialog = builder.create();
			dialog.show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
							response = loginCommunications
									.login(user,
											"http://10.0.2.2:8080/GroupManager/api/login");
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
				Intent intent = new Intent(LoginAndroidActivity.this,
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
