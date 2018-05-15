package alexm.bytemessanger.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.database.sqlite.SQLiteOpenHelper;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.List;
import alexm.bytemessanger.R;
import alexm.bytemessanger.adapters.InviteAdapter;
import alexm.bytemessanger.adapters.MessageAdapter;
import alexm.bytemessanger.utils.InviteItem;
import alexm.bytemessanger.utils.Message;
import alexm.bytemessanger.utils.DBHelper;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class RoomFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.room_fragment, container, false);

        mv = rootView;
        String name = getActivity().getIntent().getStringExtra("Name");

        messages = new ArrayList<>();
        SendBird.addChannelHandler("test", new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals("test") && baseMessage instanceof UserMessage) {
                    refresh_messages();
                }
            }
        });

        FloatingActionButton invite = (FloatingActionButton) mv.findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.invite ,null);

                dlv = (ListView) mView.findViewById(R.id.lv);
                lii = new ArrayList<>();

                GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
                channelListQuery.setIncludeEmpty(true);
                channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                    @Override
                    public void onResult(List<GroupChannel> list, SendBirdException e) {
                        if (e != null) {
                            // Error.
                            return;
                        }
                        for (GroupChannel grc:
                                list) {
                            if (grc.isDistinct()) {
                                String name = grc.getMembers().get(0).getUserId().equals(SendBird.getCurrentUser().getUserId()) ? grc.getMembers().get(1).getNickname() : grc.getMembers().get(0).getNickname();
                                String id = grc.getMembers().get(0).getUserId().equals(SendBird.getCurrentUser().getUserId()) ? grc.getMembers().get(1).getUserId() : grc.getMembers().get(0).getUserId();
                                lii.add(new InviteItem(id, name));
                            }
                        }

                        InviteAdapter ia = new InviteAdapter(getContext(), lii);
                        dlv.setAdapter(ia);

                        dlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                gc.inviteWithUserId(lii.get(position).id, new GroupChannel.GroupChannelInviteHandler() {
                                    @Override
                                    public void onResult(SendBirdException e) {
                                        ad.cancel();
                                    }
                                });
                            }
                        });
                        ia.notifyDataSetChanged();
                    }
                });





                builder.setView(mView);
                AlertDialog dialog = builder.create();
                ad = dialog;
                ad.show();
            }
        });

        GroupChannel.getChannel(name, new GroupChannel.GroupChannelGetHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                gc = groupChannel;
                gc.markAsRead();
                FloatingActionButton fab = (FloatingActionButton) mv.findViewById(R.id.send);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        gc.sendUserMessage(edittext.getText().toString(), null, null, new BaseChannel.SendUserMessageHandler() {
                            @Override
                            public void onSent(UserMessage userMessage, SendBirdException e) {
                                if (e != null) {
                                    // Error.
                                    return;
                                }

                                refresh_messages();
                            }
                        });
                        edittext.setText("");
                    }
                });

                refresh_messages();
                ma = new MessageAdapter(getContext(), messages, SendBird.getCurrentUser().getUserId());
                lv = (ListView) mv.findViewById(R.id.messages);
                lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                lv.setSelector(new ColorDrawable(2));
                lv.setItemsCanFocus(true);
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        lv.setItemChecked(position, true);
                        return false;
                    }
                });
                lv.setAdapter(ma);


            }
        });

        edittext = (EmojiconEditText) mv.findViewById(R.id.editText2);
        ImageView iv = (ImageView) mv.findViewById(R.id.imageView7);

        emojIcon = new EmojIconActions(getContext(), rootView, edittext, iv);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);

        return rootView;
    }

    public void refresh_messages() {
        /*DBHelper dbHelper = new DBHelper(getContext());
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        String selection = COLUMN_NAME_CHANNEL_URL + " = ?";
        String[] selectionArgs = { CURRENT_CHANNEL_URL };

        String sortOrder = COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor cursor = database.query(
                TABLE_NAME,
                null, // The columns to return; all if null.
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // Don't group the rows
                null, // Don't filter by row groups
                sortOrder, // The sort order
                "30" // The limit
        );

        // Create a List of BaseMessages by deserializing each item.
        List<BaseMessage> prevMessageList = new ArrayList<>();

        while (cursor.moveToNext()) {
            byte[] data = cursor.getBlob(cursor.getColumnIndex(COLUMN_NAME_PAYLOAD));
            BaseMessage message = BaseMessage.buildFromSerializedData(data);
            prevMessageList.add(message);
        }

        cursor.close();

        */



        PreviousMessageListQuery prevMessageListQuery = this.gc.createPreviousMessageListQuery();
        prevMessageListQuery.load(30, true, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> mes, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                messages.clear();
                for (BaseMessage bm:
                        mes) {
                    UserMessage message = (UserMessage) bm;
                    messages.add(0, new Message(message.getMessage(), message.getSender().getNickname(), message.getSender().getUserId(), message.getCreatedAt(), message.getSender().getProfileUrl()));
                    ma.notifyDataSetChanged();
                }
            }
        });
    }

    public EmojiconEditText edittext;
    public ListView dlv;
    public List<InviteItem> lii;
    public AlertDialog ad;
    public GroupChannel gc;
    public List<Message> messages;
    public MessageAdapter ma;
    public ListView lv;
    public View mv;
    public EmojIconActions emojIcon;
}
