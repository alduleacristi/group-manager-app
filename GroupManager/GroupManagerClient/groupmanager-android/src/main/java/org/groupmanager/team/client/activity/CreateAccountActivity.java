package org.groupmanager.team.client.activity;

import org.groupmanager.team.groupmanager_client.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccountActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);

		EditText email = (EditText) findViewById(R.id.textEmail);
		final EditText pass = (EditText) findViewById(R.id.password);
		final EditText retypePass = (EditText) findViewById(R.id.retypePassword);
		Button createAccount = (Button) findViewById(R.id.createAccount);

		createAccount.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (!pass.getText().toString()
						.equals(retypePass.getText().toString())) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CreateAccountActivity.this);
					builder.setMessage(R.string.error_password);

					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});

	}
}
