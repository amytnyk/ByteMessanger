package alexm.bytemessanger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.utils.Server;

/**
 * Created by alexm on 12.04.2018.
 */

public class ServersAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Server> servers;

    public ServersAdapter(Context context, List<Server> servers) {
        ctx = context;
        this.servers = servers;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return servers.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return servers.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Server s = getServer(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        TextView server_name = (TextView) view.findViewById(R.id.server_name);
        server_name.setText(s.server_name);
        TextView new_messages = (TextView) view.findViewById(R.id.new_messages);
        if (s.new_messages != 0) {
            new_messages.setText(String.valueOf(s.new_messages));
        }
        else {
            new_messages.setVisibility(View.INVISIBLE);
        }
        if (s.last_message != null) {
            TextView last_message = (TextView) view.findViewById(R.id.last_message);
            last_message.setText(s.last_message);
        }
        //view.findViewById(R.id.ivImage)).setImageResource(p.image);
        return view;
    }

    // товар по позиции
    Server getServer(int position) {
        return ((Server) getItem(position));
    }

}
