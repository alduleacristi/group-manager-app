package team.groupmanager.org.groupmanager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

import team.groupmanager.org.communications.GroupCommunications;
import team.groupmanager.org.communications.LoginCommunications;
import team.groupmanager.org.exceptions.GroupManagerClientException;

/**
 * Created by Cristi on 4/5/2015.
 */
public class GroupListActivity extends ListActivity {
    private List<GroupDTO> groups = new ArrayList<>();;
    private GroupManagerClientException exc;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    GroupListActivity.this);

            if (exc != null) {
                builder.setMessage(exc.getMessage());

                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                builder.setMessage("Data load successfuly");

                AlertDialog dialog = builder.create();
                dialog.show();

                //for(PositionDTO poz:positions) {
                //mMap.addMarker(new MarkerOptions().position(new LatLng(poz.getxPosition(), poz.getyPosition())).title(poz.getIdUser().toString()));
                //}
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable runnable = new Runnable() {

            public void run() {
                GroupCommunications groupCommunications = new GroupCommunications();

                try {
                     groups = groupCommunications
                            .getGroupForUser("alduleacristi@yahoo.com",
                                    "http://groupmanagerservices-groupmanagerweb.rhcloud.com/GroupManager/api/security/groups/getGroups");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final ChooseGroupArrayAdapter adapter = new ChooseGroupArrayAdapter(GroupListActivity.this,groups);
                            setListAdapter(adapter);
                        }
                    });
                } catch (GroupManagerClientException e) {
                    exc = e;
                }
                handler.sendEmptyMessage(0);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(GroupListActivity.this,
                MapsActivity.class);
        GroupDTO group = (GroupDTO) getListAdapter().getItem(position);
        intent.putExtra("groupId",group.getId());
        startActivity(intent);
    }

    private class ChooseGroupArrayAdapter extends ArrayAdapter<GroupDTO>{
        private Context context;
        private List<GroupDTO> groups;

        public ChooseGroupArrayAdapter(Context ctx, List<GroupDTO> groups){
            super(ctx,R.layout.choose_group_layout,groups);
            this.context = ctx;
            this.groups = groups;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.choose_group_layout, parent, false);

            TextView groupName = (TextView) rowView.findViewById(R.id.groupName);
            TextView groupOwner = (TextView) rowView.findViewById(R.id.groupOwner);

            GroupDTO selectedGroup = groups.get(position);
            groupName.setText(selectedGroup.getName());
            groupOwner.setText(selectedGroup.getOwner().getEmail());

            return  rowView;
        }
    }

}