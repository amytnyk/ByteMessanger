package alexm.bytemessanger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.utils.FoundItem;
import alexm.bytemessanger.utils.InviteItem;

public class InviteAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<InviteItem> users;

    public InviteAdapter(Context context, List<InviteItem> users) {
        ctx = context;
        this.users = users;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return users.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return users.get(position);
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
            view = lInflater.inflate(R.layout.invite_el, parent, false);
        }

        InviteItem s = getInviteItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // // и картинка
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(s.name);
        //TextView new_messages = (TextView) view.findViewById(R.id.new_messages);
        //new_messages.setText(String.valueOf(s.new_messages));
        //view.findViewById(R.id.ivImage)).setImageResource(p.image);
        return view;
    }

    // товар по позиции
    InviteItem getInviteItem(int position) {
        return ((InviteItem) getItem(position));
    }

}
