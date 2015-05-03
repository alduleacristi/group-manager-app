package team.groupmanager.org.groupmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

import team.groupmanager.org.communications.GroupCommunications;
import team.groupmanager.org.communications.LoginCommunications;
import team.groupmanager.org.communications.UserCommunication;
import team.groupmanager.org.exceptions.GroupManagerClientException;
import team.groupmanager.org.util.Constants;
import team.groupmanager.org.util.SharedPreferencesUtil;
import team.groupmanager.org.util.ShowMessageUtil;


public class AddUserToGroupActivity extends ListActivity {
    private GroupManagerClientException exc;
    private List<UserDTO> users;
    private List<String> selectedEmails;
    private Long groupId;
    private String token;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ShowMessageUtil showMessageUtil;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);
        selectedEmails = new ArrayList<>();
        Intent intent = getIntent();
        groupId = intent.getLongExtra("groupId",1);
        showMessageUtil = new ShowMessageUtil(handler,AddUserToGroupActivity.this);
        sharedPreferencesUtil = new SharedPreferencesUtil(AddUserToGroupActivity.this);
        token = sharedPreferencesUtil.getToken();

        final ImageButton searchButton = (ImageButton) findViewById(R.id.emailSearchButton);
        final EditText searchEmail = (EditText) findViewById(R.id.emailSearchText);
        final ImageButton addUserToGroup = (ImageButton) findViewById(R.id.addSelectedUserToGroup);

        searchEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToGroup.setVisibility(View.INVISIBLE);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchEmail.getWindowToken(), 0);
                        selectedEmails.clear();
                        String email = searchEmail.getText().toString();

                        UserCommunication userCommunication = new UserCommunication();
                        try {
                            users = userCommunication.getUsersByEmail(email, Constants.URL+ "/GroupManager/api/security/users/getUsers", "token");

                            runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                 if (users.size() > 0) {
                                     TextView noResult = (TextView) findViewById(R.id.noResultSearch);
                                     noResult.setVisibility(View.INVISIBLE);
                                     ListView listView = (ListView) findViewById(android.R.id.list);
                                     listView.setVisibility(View.VISIBLE);
                                     addUserToGroup.setVisibility(View.VISIBLE);
                                     final ChooseUserArrayAdapter adapter = new ChooseUserArrayAdapter(AddUserToGroupActivity.this, users);
                                     setListAdapter(adapter);
                                }else {
                                     TextView noResult = (TextView) findViewById(R.id.noResultSearch);
                                     noResult.setVisibility(View.VISIBLE);
                                     ListView listView = (ListView) findViewById(android.R.id.list);
                                     listView.setVisibility(View.INVISIBLE);
                                 }
                                }
                            });
                        }
                        catch(GroupManagerClientException e){
                            showMessageUtil.showToast("Failed to get users.", Toast.LENGTH_SHORT);
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        });

        final Button addUser = (Button) findViewById(R.id.addSelectedUserToGroup);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        GroupDTO groupDTO = new GroupDTO();
                        groupDTO.setId(groupId);
                        List<UserDTO> usersDTO = new ArrayList<UserDTO>();
                        for(String email:selectedEmails){
                            UserDTO userDTO = new UserDTO();
                            userDTO.setEmail(email);
                            usersDTO.add(userDTO);
                        }
                        groupDTO.setUsers(usersDTO);

                        GroupCommunications groupCommunications = new GroupCommunications();
                        try {
                            groupCommunications.addUsersToGroup(Constants.URL+"/GroupManager/api/security/groups/addUsersToGroup",groupDTO,token);
                            showMessageUtil.showToast("Uses added with success.", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(AddUserToGroupActivity.this,MainMenuActivity.class);
                            startActivity(intent);
                        } catch (GroupManagerClientException e) {
                            showMessageUtil.showToast(e, Toast.LENGTH_SHORT);
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        });

        ListView listView = (ListView) findViewById(android.R.id.list);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_user_to_group, menu);
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

    private class ChooseUserArrayAdapter extends ArrayAdapter<UserDTO> {
        private Context context;
        private List<UserDTO> users;

        public ChooseUserArrayAdapter(Context ctx, List<UserDTO> users){
            super(ctx,R.layout.choose_user_layout,users);
            this.context = ctx;
            this.users = users;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.choose_user_layout, parent, false);

            TextView userEmail = (TextView) rowView.findViewById(R.id.chooseUserEmail);
            CheckBox checkUser = (CheckBox) rowView.findViewById(R.id.checkBoxUser);

            checkUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = users.get(position).getEmail();
                    if(selectedEmails.contains(email)){
                        selectedEmails.remove(email);
                    }else{
                        selectedEmails.add(email);
                    }
                }
            });

            UserDTO selectedUser = users.get(position);
            userEmail.setText(selectedUser.getEmail());

            return  rowView;
        }
    }
}
