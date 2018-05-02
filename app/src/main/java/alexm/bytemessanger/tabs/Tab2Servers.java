package alexm.bytemessanger.tabs;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.activities.RoomActivity;
import alexm.bytemessanger.adapters.ServersAdapter;
import alexm.bytemessanger.utils.Server;

/**
 * Created by alexm on 11.04.2018.
 */

public class Tab2Servers extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab2servers, container, false);
        s = (ListView) rootView.findViewById(R.id.servers);
        //refresh();

        FloatingActionButton create_server = (FloatingActionButton) rootView.findViewById(R.id.create_server);
        create_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.activity_new_server ,null);
                final EditText mName = (EditText) mView.findViewById(R.id.name);

                Button create = (Button) mView.findViewById(R.id.create);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> userIDS = new ArrayList<>();
                        userIDS.add(SendBird.getCurrentUser().getUserId());
                        GroupChannel.createChannelWithUserIds(userIDS, false, mName.getText().toString(), null, null, new GroupChannel.GroupChannelCreateHandler() {
                            @Override
                            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                                refresh();
                                ad.cancel();
                            }
                        });
                    }
                });

                builder.setView(mView);
                AlertDialog dialog = builder.create();
                //dialog.show();
                ad = dialog;
                ad.show();
            }
        });

        return rootView;
    }

    void refresh() {
        servers = new ArrayList<>();
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setIncludeEmpty(true);
        channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                for (GroupChannel gc:
                        list) {
                    if (!gc.isDistinct()) {
                        String lm;
                        if (gc.getLastMessage() == null)
                            lm = "";
                        else
                            lm = ((UserMessage) (gc.getLastMessage())).getMessage();
                        servers.add(new Server(lm, gc.getUrl(), gc.getName(), gc.getUnreadMessageCount()));
                    }
                }
                ServersAdapter sa = new ServersAdapter(getContext() , servers);
                s.setAdapter(sa);
                s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), RoomActivity.class);
                        intent.putExtra("Name", servers.get(position).ID);
                        intent.putExtra("CanInvite", "true");
                        startActivity(intent);
                    }
                });
                sa.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public AlertDialog ad;
    public List<Server> servers;
    public ListView s;
}
