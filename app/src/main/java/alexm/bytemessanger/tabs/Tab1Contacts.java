package alexm.bytemessanger.tabs;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;
import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.activities.RoomActivity;
import alexm.bytemessanger.adapters.ContactsAdapter;
import alexm.bytemessanger.utils.Contact;

/**
 * Created by alexm on 11.04.2018.
 */

public class Tab1Contacts extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1contacts, container, false);
        updateDatabase(rootView);
        return rootView;
    }

    public List<Contact> contacts;

    private void updateDatabase(View rootView) {

        contacts = new ArrayList<>();
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
                    if (gc.isDistinct()) {
                        String name = gc.getMembers().get(0).getUserId().equals(SendBird.getCurrentUser().getUserId()) ? gc.getMembers().get(0).getNickname() : gc.getMembers().get(1).getNickname();
                        contacts.add(new Contact(gc.getUrl(), name, gc.getUnreadMessageCount()));
                    }
                }
            }
        });

        ContactsAdapter sa = new ContactsAdapter(getContext(), contacts);
        ListView s = (ListView) rootView.findViewById(R.id.contacts_list);
        s.setAdapter(sa);
        s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RoomActivity.class);
                intent.putExtra("Name", contacts.get(position).contact_name);
                startActivity(intent);
            }
        });

    }
}
