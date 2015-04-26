package team.groupmanager.org.groupmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.groupmanager.team.dto.PositionDTO;
import org.groupmanager.team.dto.UserDTO;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import team.groupmanager.org.communications.GroupCommunications;
import team.groupmanager.org.communications.LoginCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.ShowMessageUtil;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GroupManagerClientException exc;
    private List<PositionDTO> positions;
    private ScheduledExecutorService scheduleTaskExecutor;
    private ShowMessageUtil showMessageUtil;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_maps);

        showMessageUtil = new ShowMessageUtil(handler,MapsActivity.this);

        final Long groupId = getIntent().getLongExtra("groupId",1);

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

            public void run() {
                GroupCommunications groupCommunications = new GroupCommunications();

                try {
                    positions = groupCommunications
                            .getPositionForGroup(groupId,
                                    "http://groupmanagerservices-groupmanagerweb.rhcloud.com/GroupManager/api/security/groups/getPositions");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.clear();
                            for(PositionDTO poz:positions) {
                                mMap.addMarker(new MarkerOptions().position(new LatLng(poz.getxPosition(), poz.getyPosition())).title(poz.getEmail().toString()));
                            }
                        }
                    });

                } catch (GroupManagerClientException e) {
                    showMessageUtil.showToast("Failed to get data", Toast.LENGTH_SHORT);
                }

            }
        },0,30, TimeUnit.SECONDS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_home: {
                Intent intent = new Intent(MapsActivity.this,MainMenuActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
