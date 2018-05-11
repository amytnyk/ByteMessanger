package alexm.bytemessanger.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sendbird.android.SendBird;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.utils.CircleTransform;
import alexm.bytemessanger.utils.Message;

/**
 * Created by alexm on 18.04.2018.
 */

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Message> messages;
    String userID;

    public MessageAdapter(Context context, List<Message> messages, String userID) {
        ctx = context;
        this.messages = messages;
        this.userID = userID;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (messages.get(position).ownerID.equals(SendBird.getCurrentUser().getUserId())) {
            // Own message
            view = lInflater.inflate(R.layout.sended, parent, false);
            Message s = getMessage(position);
            TextView messsage_text = (TextView) view.findViewById(R.id.message_text);
            messsage_text.setText(s.text);
            //TextView time = (TextView) view.findViewById(R.id.message_time);
            //String dateString = DateFormat.format("MM.dd (hh:mm)", new Date(s.time)).toString();
            //time.setText(dateString);

        } else {
            // Other Message
            view = lInflater.inflate(R.layout.received_message, parent, false);
            Message s = getMessage(position);
            TextView messsage_text = (TextView) view.findViewById(R.id.message_text);
            messsage_text.setText(s.text);
            ImageView iv = (ImageView) view.findViewById(R.id.imageView2);
            Picasso.get().load(s.avatar_URL).resize(100, 100).centerCrop().transform(new CircleTransform()).into(iv);
            //TextView time = (TextView) view.findViewById(R.id.message_time);
            //String dateString = DateFormat.format("MM.dd (hh:mm)", new Date(s.time)).toString();
            //time.setText(dateString);
        }

        //view.findViewById(R.id.ivImage)).setImageResource(p.image);
        return view;
    }

    // товар по позиции
    Message getMessage(int position) {
        return ((Message) getItem(position));
    }
}
