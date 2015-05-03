package team.groupmanager.org.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.groupmanager.team.dto.GroupDTO;

import java.util.List;

import team.groupmanager.org.groupmanager.R;

/**
 * Created by Cristi on 4/14/2015.
 */
public class ChooseGroupArrayAdapter extends ArrayAdapter<GroupDTO> {
    private Context context;
    private List<GroupDTO> groups;

    public ChooseGroupArrayAdapter(Context ctx, List<GroupDTO> groups){
        super(ctx, R.layout.choose_group_layout,groups);
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
        groupOwner.setText("-"+selectedGroup.getOwner().getEmail());

        return  rowView;
    }
}