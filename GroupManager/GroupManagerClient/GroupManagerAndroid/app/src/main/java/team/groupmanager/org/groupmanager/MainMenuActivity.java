package team.groupmanager.org.groupmanager;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MainMenuActivity extends ActionBarActivity {
   final Handler handler = new Handler();

    public TimerTask initializeTimerTask(){
        return new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(getApplicationContext(), "S-a apelat send location", duration);
                        toast.show();
                    }
                });
            }
        };
    }

    private void startSendLocation(){
        Timer timer = new Timer();
        TimerTask task = initializeTimerTask();
        timer.schedule(task,0, TimeUnit.SECONDS.toMillis(30));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        startSendLocation();

        final Button viewGroup = (Button) findViewById(R.id.viewGroup);
        viewGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                 Intent intent = new Intent(MainMenuActivity.this,
                        GroupListActivity.class);
                 startActivity(intent);
            }
        });

        final Button createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this,CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        final Button addUser = (Button) findViewById(R.id.addUserToGroupOption);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ChooseGroupToAddUsersActivity.class);
                startActivity(intent);
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
