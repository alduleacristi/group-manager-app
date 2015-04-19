package team.groupmanager.org.groupmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.groupmanager.team.dto.PositionDTO;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import team.groupmanager.org.communications.LoginCommunications;
import team.groupmanager.org.communications.SendCoordonates;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.SharedPreferencesUtil;
import team.groupmanager.org.util.ShowMessageUtil;


public class MainMenuActivity extends ActionBarActivity {
    private LoginCommunications loginCommunications;
    private GroupManagerClientException exc;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ShowMessageUtil showMessageUtil;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider,email,savedToken;

    private final Handler handler = new Handler();

    @Override
    public void onBackPressed() {
        //Disabled back button from Login
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public TimerTask initializeTimerTask(){
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }

        return new TimerTask() {
            @Override
            public void run() {
                String email = sharedPreferencesUtil.getEmail();
                if(!email.equals("")) {
                    criteria = new Criteria();
                    //provider = locationManager.getBestProvider(criteria, false);
                    double x, y;
                    Location location;
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location == null) {
                            x = 35;
                            y = 45;
                            provider = "Default";
                        } else {
                            x = location.getLongitude();
                            y = location.getLatitude();
                            provider = "Network";
                        }
                    } else {
                        x = location.getLongitude();
                        y = location.getLatitude();
                        provider = "GPS";
                    }
                    PositionDTO positionDTO = new PositionDTO();
                    positionDTO.setEmail(email);
                    positionDTO.setxPosition(x);
                    positionDTO.setyPosition(y);

                    SendCoordonates sendCoordonates = new SendCoordonates();
                    try {
                        sendCoordonates.sendLocation(positionDTO, "http://groupmanagerservices-groupmanagerweb.rhcloud.com/GroupManager/api/security/location/updateLocation", savedToken);
                        showMessageUtil.showToast("Send location: " + x + " " + y + " " + provider, Toast.LENGTH_SHORT);
                    } catch (GroupManagerClientException e) {
                        showMessageUtil.showToast("Failed to send location."+email, Toast.LENGTH_SHORT);
                    }
                }
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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!isNetworkAvailable()){
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            startActivity(new Intent(android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS ));
        }

        sharedPreferencesUtil = new SharedPreferencesUtil(MainMenuActivity.this);
        showMessageUtil = new ShowMessageUtil(handler,MainMenuActivity.this);

        savedToken = sharedPreferencesUtil.getToken();
        if (savedToken.equals("")) {
            Intent intent = new Intent(MainMenuActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        loginCommunications = new LoginCommunications();
        email = sharedPreferencesUtil.getEmail();
        TextView helloUser = (TextView) findViewById(R.id.helloUser);
        helloUser.setText("Hi "+email);
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

        final Button leaveGroup = (Button) findViewById(R.id.leaveGroupOption);
        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ChooseGroupToRemoveUserActivity.class);
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

    private void logout(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String token = sharedPreferencesUtil.getToken();
                    loginCommunications.logout(token,"http://groupmanagerservices-groupmanagerweb.rhcloud.com/GroupManager/api/logout");
                    sharedPreferencesUtil.deleteToken();
                    sharedPreferencesUtil.deleteEmail();
                    Intent intent = new Intent(MainMenuActivity.this,LoginActivity.class);
                    startActivity(intent);
                } catch (GroupManagerClientException e) {
                    exc = e;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getApplicationContext(), exc.getMessage(), duration);
                            toast.show();
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), "Logout with success", duration);
                        toast.show();
                    }
                });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_home: {
                Intent intent = new Intent(MainMenuActivity.this,MainMenuActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_logout:{
                logout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
